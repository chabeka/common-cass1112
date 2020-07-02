package fr.urssaf.image.sae.services.batch.transfert.support.stockage.batch;

import java.util.Date;
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

import com.docubase.dfce.exception.runtime.DFCERuntimeException;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch.AbstractDocumentWriterListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.suppression.support.stockage.batch.StorageDocumentToRecycleWriter;
import fr.urssaf.image.sae.services.batch.transfert.support.stockage.multithreading.TransfertPoolThreadExecutor;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.AbstractStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;

/**
 * Item writer du transfert des documents dans la GNS
 */
@Component
public class TransfertDocumentWriter extends AbstractDocumentWriterListener
implements
ItemWriter<StorageDocument> {

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

   private static final String TRC_INSERT = "TransfertDocumentWriter()";

   /**
    * Index du document
    */
   int docIndexInWriter = 0;

   @Override
   // Attention : cette méthode est appelée depuis un pool de thread. Elle ne doit pas utiliser docIndexInWriter dont la valeur change
   public UUID launchTraitement(final AbstractStorageDocument storageDocument, final int docIndex) throws Exception {
      StorageDocument document = new StorageDocument();
      final String actionType = checkActionType((StorageDocument) storageDocument);
      if (actionType != null) {
         if (actionType.equals("SUPPRESSION")) {
            final StorageDocument storageDoc = (StorageDocument) storageDocument;
            storageDoc.getMetadatas()
            .add(new StorageMetadata(Constantes.CODE_COURT_META_DATE_CORBEILLE, new Date()));
            document = moveDocumentToRecycleBin(storageDoc);
         } else {
            document = transfertDocument((StorageDocument) storageDocument, docIndex);
         }
      }
      final UUID uuid = document != null ? document.getUuid() : null;
      return uuid;
   }

   /**
    * Transfert du document
    *
    * @param document
    * @param docIndex
    *           L'index du document
    * @return Le document transféré
    * @throws TransfertException
    * @{@link TransfertException}
    */
   public final StorageDocument transfertDocument(final StorageDocument document, final int docIndex) throws TransfertException {
      try {
         transfertService.transfertDocMasse(document);
      }
      catch (final TransfertException e) {
         if (isModePartielBatch()) {
            sendExceptionInPartielMode(e, docIndex);
            return null;
         } else {
            throw e;
         }

      }
      catch (Exception except) {
         if (isModePartielBatch()) {
        	 if(except.getMessage().isEmpty() && except instanceof DFCERuntimeException) {
        	      except = new Exception("Erreur DFCE");
        	 }
            sendExceptionInPartielMode(except, docIndex);
            return null;
         } else {
            throw new TransfertException(except.getMessage(), except);
         }

      }
      return document;
   }

   private void sendExceptionInPartielMode(final Exception e, final int docIndex) {
      synchronized (this) {
         getCodesErreurListe().add(Constantes.ERR_BUL002);
         getIndexErreurListe().add(docIndex);
         getErrorMessageList().add(e.getMessage());
      }
      LOGGER.warn("Erreur lors du transfert du document de la GNT vers la GNS", e);
   }

   /**
    * Mise en corbeille du document passé en paramètre
    *
    * @param document
    * @return le document supprimé
    * @throws SuppressionException
    * @{@link SuppressionException}
    */
   public final StorageDocument moveDocumentToRecycleBin(final StorageDocument document) throws SuppressionException {

      try {
         // Récupère l'id du traitement en cours
         final String idJob = getStepExecution().getJobParameters().getString(Constantes.ID_TRAITEMENT);

         UUID uuidJob = null;
         if (StringUtils.isNotEmpty(idJob)) {
            // conversion
            uuidJob = UUID.fromString(idJob);
         }

         suppressionService.moveToRecycleBeanStorageDocument(uuidJob, document);
      }
      catch (final Exception except) {
         throw new SuppressionException(
                                        "Erreur Suppression - identifiant archivage "
                                              + document.getUuid() + " : " + except.getMessage(),
                                              except);
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
   public void write(final List<? extends StorageDocument> items) throws Exception {

      Runnable command;
      for (final StorageDocument storageDocument : Utils.nullSafeIterable(items)) {
         final boolean isdocumentATraite = isDocumentATraite(docIndexInWriter);
         // Si le document n'est pas en erreur ou dans la liste de document déjà
         // traité (Reprise), on traite, sinon on passe au suivant.
         if (isdocumentATraite) {

            command = new InsertionRunnable(docIndexInWriter,
                                            storageDocument,
                                            this);

            try {
               poolExecutor.execute(command);
            }
            catch (final Exception e) {
               if (isModePartielBatch()) {
                  sendExceptionInPartielMode(e, docIndexInWriter);
               }
               else {
                  throw e;
               }
            }
            if (LOGGER.isDebugEnabled()) {
               LOGGER.debug("{} - nombre de documents en attente dans le pool : {}",
                            TRC_INSERT,
                            "Queue : " + poolExecutor.getQueue().size() + " - Total : " + poolExecutor.getTaskCount()
                            + " - Actifs : " + poolExecutor.getActiveCount());
            }
         } else if (isDocumentDejaTraite(docIndexInWriter)) {
            poolExecutor.getIntegratedDocuments()
            .add(
                 new TraitementMasseIntegratedDocument(storageDocument.getUuid(),
                                                       null,
                                                       docIndexInWriter));
         }
         docIndexInWriter++;
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      final String trcPrefix = "specificInitOperations()";

      super.specificInitOperations();

      try {
         storageTransfertService.openConnexion();
         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      }
      catch (final Exception e) {
         getLogger().warn("{} - erreur d'ouverture des services de transfert",
                          trcPrefix,
                          e);
         getCodesErreurListe().add(Constantes.ERR_BUL001);
         getIndexErreurListe().add(0);
         getErrorMessageList().add(e.getMessage());
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
      final String trcPrefix = "specificAfterStepOperations()";
      ExitStatus exitStatus = super.specificAfterStepOperations();
      try {
         storageTransfertService.closeConnexion();
         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      }
      catch (final Exception e) {
         getLogger().warn("{} - erreur lors de la fermeture des services de transfert",
                          trcPrefix,
                          e);
         getCodesErreurListe().add(Constantes.ERR_BUL001);
         getIndexErreurListe().add(0);
         getErrorMessageList().add(e.getMessage());
         if (!isModePartielBatch()) {
            exitStatus = ExitStatus.FAILED;
         }
      }
      getLogger().debug("{} - fermeture du service de trace", trcPrefix);
      return exitStatus;
   }

}
