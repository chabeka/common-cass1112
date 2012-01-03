package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
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
      final List<StorageDocument> storageDocDone = new ArrayList<StorageDocument>();
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

      try {

         for (StorageDocument storageDocument : Utils
               .nullSafeIterable(storageDocuments.getAllStorageDocuments())) {

            currentDocument = storageDocument;

            // TODO revoir l'emplacement de l'interruption du traitement de
            // capture
            // en masse lors de l'injection
            interruptionTraitement(indicator);

            if (indicator != null) {
               indicator.setJmxTreatmentState(BulkProgress.INSERTION_DOCUMENTS);
            }

            LOGGER.debug("{} - Stockage du document #{} ({})", new Object[] {
                  prefixeTrc, ++indexDocument, storageDocument.getFilePath() });
            storageDocument.setUuid(insertStorageDocument(storageDocument)
                  .getUuid());
            storageDocDone.add(storageDocument);
            jmxStorageIndex++;
            if (indicator != null) {
               indicator.setJmxStorageIndex(jmxStorageIndex);
            }
         }
      } catch (InsertionServiceEx insertExcp) {

         if (currentDocument != null) {

            String codeError = "SAE-CA-BUL001";
            String messageError = MessageFormat
                  .format(
                        "Une erreur interne à l''application est survenue lors de la capture du document {0}. Détails : {1}",
                        new File(currentDocument.getFilePath()).getName(),
                        "INSERROR : " + insertExcp.getCodeError());

            StorageDocumentOnError storageDocumentOnError = createStorageDocumentOnError(
                  currentDocument, codeError, messageError, jmxStorageIndex);

            storageDocFailed.add(storageDocumentOnError);
         }
         if (allOrNothing) {
            LOGGER.debug("{} - Déclenchement de la procédure de rollback",
                  prefixeTrc);
            rollback(storageDocDone);
            // Les documents "rollbackés" ne doivent pas apparaitre dans
            // BulkInsertionResults

            LOGGER.debug("{} - La procédure de rollback est terminée",
                  prefixeTrc);
            storageDocDone.clear();

         }

      } catch (RuntimeException e) {

         if (currentDocument != null) {

            String codeError = "SAE-CA-BUL003";
            String messageError = "La capture de masse en mode 'Tout ou rien' a été interrompue. Une procédure d'exploitation a été initialisée pour supprimer les données qui auraient pu être stockées.";

            StorageDocumentOnError storageDocumentOnError = createStorageDocumentOnError(
                  currentDocument, codeError, messageError, jmxStorageIndex);

            storageDocFailed.add(storageDocumentOnError);
         }

         if (allOrNothing && CollectionUtils.isNotEmpty(storageDocDone)) {

            String idTraitement = storageDocDone.get(0).getProcessId();

            logRollbackFailure(idTraitement);
         }
         storageDocDone.clear();

      }

      LOGGER.debug("{} - Fin de la boucle d'insertion des documents dans DFCE",
            prefixeTrc);
      LOGGER.debug("{} - Sortie", prefixeTrc);

      return new BulkInsertionResults(new StorageDocuments(storageDocDone),
            new StorageDocumentsOnError(storageDocFailed));
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

   private void logRollbackFailure(String idTraitement) {

      LOGGER
            .error(

                  "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                  idTraitement);
   }

   private void interruptionTraitement(JmxIndicator indicator) {

      if (interruptionConfig != null) {
         // on vérifie l'interruption
         interruption.interruption(interruptionConfig, indicator);

         // on renseigne le nouveau le fournisseur de service DFCE pour
         // éviter en cas de close de la session pendant l'interruption de
         // perdre la session DFCE
         // TODO recourir à un ThreadLocal pour garder appeller l'objet
         // DFCEService
         this.setDfceService(dfceManager.getDFCEService());

      }
   }

   /**
    * Supprime les documents qui ont déjà été insérés avec succès. Appelée dans
    * le cadre d'un traitement "tout ou rien" qui serait en erreur.
    * 
    * @param storageDocDone
    * @throws InsertionServiceEx
    *            Levée si le rollback (donc la suppression) échoue.
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   private void rollback(final List<StorageDocument> storageDocDone)
         throws InsertionServiceEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "rollback()";
      LOGGER.debug("{} - Début", prefixeTrc);
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

            // TODO revoir l'emplacement de l'interruption du traitement de
            // capture
            // en masse lors du rollback
            interruptionTraitement(indicator);

            LOGGER.debug("{} - Rollback du document #{} ({})", new Object[] {
                  prefixeTrc, ++indexDocument, strDocument.getUuid() });
            deletionService.setDeletionServiceParameter(getDfceService());
            deletionService.deleteStorageDocument(new UUIDCriteria(strDocument
                  .getUuid(), null));
            jmxStorageIndex++;
            if (indicator != null) {
               indicator.setJmxStorageIndex(jmxStorageIndex);
            }
         } catch (DeletionServiceEx delSerEx) {
            // FIXME: lever une exception plus parlante pour l'appelant
            // (par exemple : AllOrNothingRollbackException).
            // Pour l'instant on laisse cette exception pour ne pas casser
            // les
            // signatures.

            // TODO : il faut continuer le rollback sur les autres
            // documents.
            // L'appelant pourrait obtenir une liste des documents non
            // rollbackés via un attribut
            // de l'exception AllOrNothingRollbackException

            String idTraitement = strDocument.getProcessId();

            logRollbackFailure(idTraitement);

            LOGGER.debug(
                  "{} - Une exception a été levée lors du rollback : {}",
                  prefixeTrc, delSerEx.getMessage());
            throw new InsertionServiceEx(StorageMessageHandler
                  .getMessage(Constants.DEL_CODE_ERROR), delSerEx.getMessage(),
                  delSerEx);
         } catch (RuntimeException e) {

            String idTraitement = storageDocDone.get(0).getProcessId();

            logRollbackFailure(idTraitement);

            throw e;
         }
      }
      LOGGER.debug("{} - Sortie", prefixeTrc);
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
