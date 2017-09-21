package fr.urssaf.image.sae.services.batch.transfert.support.stockage.batch;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch.AbstractDocumentWriterListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.suppression.support.stockage.batch.StorageDocumentToRecycleWriter;
import fr.urssaf.image.sae.services.batch.transfert.support.stockage.multithreading.TransfertPoolThreadExecutor;
import fr.urssaf.image.sae.services.controles.traces.TracesControlesSupport;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.AbstractStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;

/**
 * Item writer du transfert des documents dans la GNS
 * 
 */
@Component
public class TransfertDocumentWriter extends AbstractDocumentWriterListener
      implements ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TransfertDocumentWriter.class);

   /**
    * Pool executor
    */
   @Autowired
   private TransfertPoolThreadExecutor poolExecutor;

   /**
    * Provider pour le transfert.
    */
   @Autowired
   private SAETransfertService transfertService;

   /**
    * Provider pour la mise des documents dans la corbeille.
    */
   @Autowired
   private StorageDocumentToRecycleWriter suppressionService;

   /**
    * Provider pour les traces.
    */
   @Autowired
   private ServiceProviderSupport traceServiceSupport;

   /**
    * Provider de service pour la connexion DFCE de la GNS
    */
   @Autowired
   private StorageTransfertService storageTransfertService;

   /**
    * Provider pour le transfert.
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;
   
   private static volatile Integer index = 0;

   /**
    * Gestionnaire des traces.
    */
   @Autowired
   private TracesControlesSupport tracesSupport;

   private static final String TRC_INSERT = "TransfertDocumentWriter()";
   private static final String CATCH = "AvoidCatchingThrowable";

   @Override
   public UUID launchTraitement(final AbstractStorageDocument storageDocument, int indexRun)
         throws Exception {

      StorageDocument document = new StorageDocument();
      String actionType = checkActionType((StorageDocument) storageDocument);
      if (actionType != null) {
         if (actionType.equals("SUPPRESSION")) {
            document = moveDocumentToRecycleBin((StorageDocument) storageDocument);
         } else {
            document = transfertDocument((StorageDocument) storageDocument);
         }  
      }
      UUID uuid = document != null ? document.getUuid() : null;
      return uuid;
   }

   /**
    * Transfert du document
    * 
    * @param document
    * @return Le document transféré
    * @throws TransfertException
    * @{@link TransfertException}
    */
   @SuppressWarnings(CATCH)
   public final StorageDocument transfertDocument(final StorageDocument document)
         throws TransfertException {
      try {
         transfertService.transfertDocMasse(document);
      } catch (Throwable except) {
         throw new TransfertException(
               "Erreur transfert - identifiant archivage " + document.getUuid()
                     + " : " + except.getMessage(), except);
      }
      return document;
   }

   /**
    * Mise en corbeille du document passé en paramètre
    * 
    * @param document
    * @return le document supprimé
    * @throws SuppressionException
    * @{@link SuppressionException}
    */
   @SuppressWarnings(CATCH)
   public final StorageDocument moveDocumentToRecycleBin(final StorageDocument document)
         throws SuppressionException {

      try {
         // Récuperer l'id du traitement en cours
         String idJob = (String) getStepExecution().getJobParameters()
               .getString(Constantes.ID_TRAITEMENT);
         
         UUID uuidJob = null;
         if(StringUtils.isNotEmpty(idJob)){
            // conversion
            uuidJob = UUID.fromString(idJob);
         }
         
         suppressionService.moveToRecycleBeanStorageDocument(uuidJob, document);
      } catch (Throwable except) {
         throw new SuppressionException(
               "Erreur Suppression - identifiant archivage "
                     + document.getUuid() + " : " + except.getMessage(), except);
      }
      return document;
   }

   @Override
   protected StorageServiceProvider getServiceProvider() {
      return serviceProvider;
   }

   @Override
   protected Logger getLogger() {
      return LOGGER;
   }

   public String checkActionType(final StorageDocument storageDocument) {
      return storageDocument.getBatchTypeAction();
   }

   @Override
   public void write(final List<? extends StorageDocument> items)
         throws Exception {

      Runnable command;

      for (StorageDocument storageDocument : Utils.nullSafeIterable(items)) {
         boolean isdocumentATraite = isDocumentATraite(index);
         // Si le document n'est pas en erreur ou dans la liste de document déjà
         // traité (Reprise), on traite, sinon on passe au
         // suivant.
         if (isdocumentATraite) {
            command = new InsertionRunnable(getStepExecution().getReadCount()
                  + index, storageDocument, this, getStepExecution()
                  .getReadCount() + index);

            try {
               poolExecutor.execute(command);
            } catch (Exception e) {
               if (isModePartielBatch()) {
                  getCodesErreurListe().add(Constantes.ERR_BUL002);
                  getIndexErreurListe().add(
                        getStepExecution().getExecutionContext().getInt(
                              Constantes.CTRL_INDEX));
                  final String message = e.getMessage();
                  getExceptionErreurListe().add(new Exception(message));
                  LOGGER.error(message, e);
               }

            }
            LOGGER.debug(
                  "{} - nombre de documents en attente dans le pool : {}",
                  TRC_INSERT, poolExecutor.getQueue().size());

         } else if (!isdocumentATraite && isDocumentDejaTraite(index)) {
            poolExecutor.getIntegratedDocuments().add(
                  new TraitementMasseIntegratedDocument(storageDocument
                        .getUuid(), null,
                        index));
         }

         index++;
      }

      // Reinitialisation du compteur si prochain passage.
      index = 0;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      String trcPrefix = "specificInitOperations()";

      super.specificInitOperations();

      try {
         storageTransfertService.openConnexion();
         traceServiceSupport.connect();

         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      } catch (Throwable e) {
         getLogger().warn("{} - erreur d'ouverture des services de transfert",
               trcPrefix, e);

         getCodesErreurListe().add(Constantes.ERR_BUL001);
         getIndexErreurListe().add(0);
         getExceptionErreurListe().add(new Exception(e.getMessage()));

         getStepExecution().setExitStatus(new ExitStatus("FAILED_NO_ROLLBACK"));

         throw new CaptureMasseRuntimeException(e);
      }

      getLogger().debug("{} - ouverture du service de trace", trcPrefix);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected ExitStatus specificAfterStepOperations() {
      String trcPrefix = "specificAfterStepOperations()";
      ExitStatus exitStatus = super.specificAfterStepOperations();

      try {
         storageTransfertService.closeConnexion();
         traceServiceSupport.disconnect();
         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      } catch (Throwable e) {
         getLogger().warn(
               "{} - erreur lors de la fermeture des services de transfert",
               trcPrefix, e);

         getCodesErreurListe().add(Constantes.ERR_BUL001);
         getIndexErreurListe().add(0);
         getExceptionErreurListe().add(new Exception(e.getMessage()));

         if (!isModePartielBatch()) {
            exitStatus = ExitStatus.FAILED;
         }
      }

      getLogger().debug("{} - fermeture du service de trace", trcPrefix);

      return exitStatus;
   }

}
