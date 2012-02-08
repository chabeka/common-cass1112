package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.dfce.services.support.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.storage.dfce.services.support.multithreading.InsertionThreadPoolExecutor;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.jmx.BulkProgress;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentsOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;

/**
 * Implémente les services de l'interface {@link InsertionService}.
 * 
 * @author Akenore, Rhofir
 * 
 */
@Service
@Qualifier("insertionService")
public class InsertionServiceImpl extends AbstractServices implements
      InsertionService {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(InsertionServiceImpl.class);
   @Autowired
   @Qualifier("deletionService")
   private DeletionService deletionService;
   private int jmxStorageIndex;
   private int totalDocument;
   private JmxIndicator indicator;

   private final InterruptionTraitementSupport interruption;

   private InterruptionTraitementConfig interruptionConfig;

   @Autowired
   private DFCEServicesManager dfceManager;

   /**
    * 
    * @param interruption
    *           support pour l'interruption des traitements
    */
   @Autowired
   public InsertionServiceImpl(InterruptionTraitementSupport interruption) {
      super();

      Assert.notNull(interruption, "'interruption' is required");
      this.interruption = interruption;
   }

   /**
    * 
    * @param interruptionConfig
    *           configuration de l'interruption programmée du traitement de la
    *           capture en masse
    */
   @Autowired(required = false)
   public final void setInterruptionConfig(
         @Qualifier("interruption_capture_masse") InterruptionTraitementConfig interruptionConfig) {

      this.interruptionConfig = interruptionConfig;
   }

   /**
    * @return : Le service de suppression
    */
   public final DeletionService getDeletionService() {
      return deletionService;
   }

   /**
    * @param deletionService
    *           : Le service de suppression.
    */
   public final void setDeletionService(final DeletionService deletionService) {
      this.deletionService = deletionService;
   }

   /**
    * @param indicator
    *           : Les indicateurs de l'archivage de masse.
    */
   public final void setIndicator(final JmxIndicator indicator) {
      this.indicator = indicator;
   }

   /**
    * @return Les indicateurs de l'archivage de masse.
    */
   public final JmxIndicator getIndicator() {
      return indicator;
   }

   private static final String PREFIX_TRACE = "bulkInsertStorageDocument()";

   /**
    * {@inheritDoc}
    * 
    * @throws InsertionServiceEx
    *            Exception levée lorsque l'insertion ne se déroule pas bien
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final BulkInsertionResults bulkInsertStorageDocument(
         final StorageDocuments storageDocuments, final boolean allOrNothing)
         throws InsertionServiceEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "bulkInsertStorageDocument()";
      LOGGER.debug("{} - Début", prefixeTrc);
      LOGGER.debug(
            "{} - Début de la boucle d'insertion des documents dans DFCE",
            prefixeTrc);
      // Fin des traces debug - entrée méthode

      final List<StorageDocumentOnError> storageDocFailed = new ArrayList<StorageDocumentOnError>();
      jmxStorageIndex = 0;
      totalDocument = 0;
      int indexDocument = 0;
      if (storageDocuments != null
            && storageDocuments.getAllStorageDocuments() != null) {
         totalDocument = storageDocuments.getAllStorageDocuments().size();
      }
      if (indicator != null) {
         indicator.setJmxCountDocument(totalDocument);
         indicator.setJmxStorageIndex(jmxStorageIndex);
         indicator.setJmxTreatmentState(BulkProgress.INSERTION_DOCUMENTS);
      }

      StorageDocument currentDocument = null;

      InsertionThreadPoolExecutor poolExecutor = new InsertionThreadPoolExecutor(
            this.interruption, this.interruptionConfig, this.indicator);

      try {

         for (StorageDocument storageDocument : Utils
               .nullSafeIterable(storageDocuments.getAllStorageDocuments())) {

            currentDocument = storageDocument;

            if (indicator != null) {
               indicator.setJmxTreatmentState(BulkProgress.INSERTION_DOCUMENTS);
            }

            Runnable command = new InsertionRunnable(indexDocument,
                  currentDocument, this);

            poolExecutor.execute(command);

            indexDocument++;

         }

         // on attend l'ensemble des insertions en exécution
         poolExecutor.waitFinishInsertion();

         // si une exception a eu lieu alors on la lève
         if (poolExecutor.getInsertionMasseException() != null) {
            throw poolExecutor.getInsertionMasseException();
         }

      } catch (InsertionMasseRuntimeException e) {

         LOGGER.error("la capture en masse a échouée", e.getCause());

         StorageDocumentOnError storageDocumentOnError = bulkInsertStorageDocumentFailure(
               e, allOrNothing, poolExecutor.getStorageDocDone());

         storageDocFailed.add(storageDocumentOnError);

         // on vide la collection de storageDocDone;
         poolExecutor.clearStorageDocDone();

      }

      LOGGER.debug("{} - Fin de la boucle d'insertion des documents dans DFCE",
            PREFIX_TRACE);
      LOGGER.debug("{} - Sortie", PREFIX_TRACE);

      return new BulkInsertionResults(new StorageDocuments(poolExecutor
            .getStorageDocDone()),
            new StorageDocumentsOnError(storageDocFailed));
   }

   private StorageDocumentOnError bulkInsertStorageDocumentFailure(
         InsertionMasseRuntimeException exception, boolean allOrNothing,
         List<StorageDocument> storageDocDone) {

      StorageDocument docFailure = exception.getStorageDocument();
      int indexFailure = exception.getIndex();

      StorageDocumentOnError storageDocumentOnError;

      try {

         throw exception.getCause();

      } catch (InterruptionTraitementException e) {

         String codeError = "SAE-CA-BUL003";
         String messageError = "La capture de masse en mode 'Tout ou rien' a été interrompue. Une procédure d'exploitation a été initialisée pour supprimer les données qui auraient pu être stockées.";

         storageDocumentOnError = createStorageDocumentOnError(docFailure,
               codeError, messageError, indexFailure);

         if (allOrNothing && docFailure != null) {

            String idTraitement = docFailure.getProcessId();

            logRollbackFailure(idTraitement, e);

         }

      } catch (Exception e) {

         LOGGER.error("la capture en masse a échouée", e);

         String codeError = "SAE-CA-BUL001";
         String messageError = MessageFormat
               .format(
                     "Une erreur interne à l''application est survenue lors de la capture du document {0}. Détails : {1}",
                     new File(docFailure.getFilePath()).getName(),
                     "INSERROR : " + e.getCause());

         storageDocumentOnError = createStorageDocumentOnError(docFailure,
               codeError, messageError, indexFailure);

         if (allOrNothing) {

            LOGGER.debug("{} - Déclenchement de la procédure de rollback",
                  PREFIX_TRACE);

            // TODO le rollback devrait être fait dans un processus à part
            rollback(storageDocDone);
            // Les documents "rollbackés" ne doivent pas apparaitre dans
            // BulkInsertionResults

            LOGGER.debug("{} - La procédure de rollback est terminée",
                  PREFIX_TRACE);

         }

      }

      return storageDocumentOnError;

   }

   private StorageDocumentOnError createStorageDocumentOnError(
         StorageDocument storageDocument, String codeError,
         String messageError, int index) {

      StorageDocumentOnError storageDocumentOnError = new StorageDocumentOnError(
            storageDocument.getMetadatas(),
            storageDocument.getContent() == null ? "DOCERROR!".getBytes()
                  : storageDocument.getContent(),
            storageDocument.getFilePath(), codeError);
      storageDocumentOnError.setMessageError(messageError);
      storageDocumentOnError.setIndex(index);

      return storageDocumentOnError;

   }

   private void logRollbackFailure(String idTraitement, Exception cause) {

      String errorMessage = MessageFormat.format(
            "{0} - Une exception a été levée lors du rollback : {1}",
            TRC_ROLLBACK, idTraitement);

      LOGGER.error(errorMessage, cause);

      LOGGER
            .error(

                  "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                  idTraitement);
   }

   private void interruptionTraitement(JmxIndicator indicator)
         throws InterruptionTraitementException {

      if (interruptionConfig != null) {

         DateTime currentDate = new DateTime();

         interruption.interruption(currentDate, interruptionConfig, indicator);

         // on renseigne le nouveau le fournisseur de service DFCE pour
         // éviter en cas de close de la session pendant l'interruption de
         // perdre la session DFCE
         // TODO recourir à un ThreadLocal pour garder appeller l'objet
         // DFCEService
         this.setDfceService(dfceManager.getDFCEService());

      }
   }

   private static final String TRC_ROLLBACK = "rollback()";

   /**
    * Supprime les documents qui ont déjà été insérés avec succès. Appelée dans
    * le cadre d'un traitement "tout ou rien" qui serait en erreur.
    * 
    * @param storageDocDone
    * @throws InsertionServiceEx
    *            Levée si le rollback (donc la suppression) échoue.
    */
   private void rollback(final List<StorageDocument> storageDocDone) {
      // Traces debug - entrée méthode

      LOGGER.debug("{} - Début", TRC_ROLLBACK);
      // Fin des traces debug - entrée méthode
      jmxStorageIndex = 0;
      totalDocument = 0;
      int indexDocument = 0;
      if (storageDocDone != null) {
         totalDocument = storageDocDone.size();
      }
      if (indicator != null) {
         indicator.setJmxCountDocument(totalDocument);
         indicator.setJmxStorageIndex(jmxStorageIndex);
         indicator.setJmxTreatmentState(BulkProgress.DELETION_DOCUMENTS);
      }

      for (StorageDocument strDocument : Utils.nullSafeIterable(storageDocDone)) {

         try {

            interruptionTraitement(indicator);

            LOGGER.debug("{} - Rollback du document #{} ({})", new Object[] {
                  TRC_ROLLBACK, ++indexDocument, strDocument.getUuid() });
            deletionService.setDeletionServiceParameter(getDfceService());
            deletionService.deleteStorageDocument(new UUIDCriteria(strDocument
                  .getUuid(), null));
            jmxStorageIndex++;
            if (indicator != null) {
               indicator.setJmxStorageIndex(jmxStorageIndex);
            }
         } catch (Exception exception) {

            // TODO poursuivre le rollback pour les autres documents.

            String idTraitement = strDocument.getProcessId();

            logRollbackFailure(idTraitement, exception);

         }
      }
      LOGGER.debug("{} - Sortie", TRC_ROLLBACK);
   }

   private static final String TRC_INSERT = "insertStorageDocument()";

   /**
    * {@inheritDoc}
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx {
      // Traces debug - entrée méthode

      LOGGER.debug("{} - Début", TRC_INSERT);
      // Fin des traces debug - entrée méthode
      try {
         Document docDfce = BeanMapper.storageDocumentToDfceDocument(
               getBaseDFCE(), storageDocument);
         // ici on récupère le chemin du fichier.
         byte[] docContentByte = FileUtils.readFileToByteArray(new File(
               storageDocument.getFilePath()));
         final InputStream docContent = new ByteArrayInputStream(docContentByte);
         final String[] file = BeanMapper.findFileNameAndExtension(
               storageDocument, StorageTechnicalMetadatas.NOM_FICHIER
                     .getShortCode().toString());
         LOGGER.debug("{} - Enrichissement des métadonnées : "
               + "ajout de la métadonnée NomFichier valeur : {}.{}",
               new Object[] { TRC_INSERT, file[0], file[1] });
         LOGGER.debug("{} - Début insertion du document dans DFCE", TRC_INSERT);

         // TODO passer 'isCheckHash' en argument de la méthode d'insertion
         if (this.getCnxParameters().isCheckHash()) {

            // on récupère le paramètre général de l'algorithme de hachage des
            // documents dans DFCE

            String digest = null;

            // on récupère l'algorithme de hachage passé dans les métadonnées
            String typeHash = StorageMetadataUtils.valueMetadataFinder(
                  storageDocument.getMetadatas(),
                  StorageTechnicalMetadatas.TYPE_HASH.getShortCode());

            String digestAlgo = this.getCnxParameters().getDigestAlgo();

            LOGGER.debug("{} - Vérification du hash '" + digestAlgo
                  + "' du document dans DFCE", TRC_INSERT);

            if (StringUtils.isNotEmpty(digestAlgo)
                  && StringUtils.isNotEmpty(typeHash)
                  && digestAlgo.equals(typeHash)) {

               // on récupère la valeur du hash contenu dans les métadonnées
               digest = StringUtils.trim(StorageMetadataUtils
                     .valueMetadataFinder(storageDocument.getMetadatas(),
                           StorageTechnicalMetadatas.HASH.getShortCode()));

               LOGGER.debug("{} - Récupération du hash '" + digest
                     + "' des métadonnées", TRC_INSERT);

            } else {

               // on recalcule le hash
               digest = HashUtils.hashHex(docContentByte, digestAlgo);
               LOGGER
                     .debug("{} - Calcule du hash '" + digest + "'", TRC_INSERT);
            }

            docDfce = insertStorageDocument(docDfce, file[0], file[1], digest,
                  docContent);
         } else {

            docDfce = insertStorageDocument(docDfce, file[0], file[1], null,
                  docContent);
         }

         LOGGER.debug("{} - Document inséré dans DFCE (UUID: {})", TRC_INSERT,
               docDfce.getUuid());
         LOGGER.debug("{} - Fin insertion du document dans DFCE", TRC_INSERT);
         LOGGER.debug("{} - Sortie", TRC_INSERT);
         return BeanMapper.dfceDocumentToStorageDocument(docDfce, null,
               getDfceService(), false);
      } catch (TagControlException tagCtrlEx) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), tagCtrlEx.getMessage(),
               tagCtrlEx);
      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      }
   }

   protected final Document insertStorageDocument(Document document,
         String originalFilename, String extension, String digest,
         InputStream inputStream) throws TagControlException {

      Document doc;

      if (StringUtils.isEmpty(digest)) {

         doc = getDfceService().getStoreService().storeDocument(document,
               originalFilename, extension, inputStream);

      } else {

         doc = getDfceService().getStoreService().storeDocument(document,
               originalFilename, extension, digest, inputStream);
      }

      return doc;
   }

   /**
    * {@inheritDoc}
    */
   public final <T> void setInsertionServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);

   }

   /**
    * @return L'indice du document stocké.
    */
   public final int getJmxStorageIndex() {
      return jmxStorageIndex;
   }

   /**
    * @param jmxStorageIndex
    *           : Indicateur jmx qui permet de retourner l'indice du document
    *           stocké.
    */
   public final void setJmxStorageIndex(int jmxStorageIndex) {
      this.jmxStorageIndex = jmxStorageIndex;
   }

   /**
    * {@inheritDoc}
    */
   public JmxIndicator retrieveJmxStorageIndicator() {
      return indicator;
   }

   /**
    * @param totalDocument
    *           : Le nombre total de document à insérer.
    */
   public void setTotalDocument(int totalDocument) {
      this.totalDocument = totalDocument;
   }

   /**
    * @return Le nombre total de document à insérer.
    */
   public int getTotalDocument() {
      return totalDocument;
   }

   /**
    * {@inheritDoc}
    */
   public void setJmxIndicator(JmxIndicator indicator) {
      setIndicator(indicator);
   }
}
