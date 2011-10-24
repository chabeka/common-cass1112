package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentsOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;

/**
 * Implémente les services de l'interface {@link InsertionService}.
 * 
 * @author Akenore, Rhofir
 * 
 */
@Service
@Qualifier("insertionService")
@SuppressWarnings("PMD.ExcessiveImports")
public class InsertionServiceImpl extends AbstractServices implements
      InsertionService {
   @Autowired
   @Qualifier("deletionService")
   private DeletionService deletionService;

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
    * {@inheritDoc}
    * 
    * @throws InsertionServiceEx
    *            Exception levée lorsque l'insertion ne se déroule pas bien
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final BulkInsertionResults bulkInsertStorageDocument(
         final StorageDocuments storageDocuments, final boolean allOrNothing)
         throws InsertionServiceEx {
      final List<StorageDocument> storageDocDone = new ArrayList<StorageDocument>();
      final List<StorageDocumentOnError> storageDocFailed = new ArrayList<StorageDocumentOnError>();

      for (StorageDocument storageDocument : Utils
            .nullSafeIterable(storageDocuments.getAllStorageDocuments())) {
         try {
            storageDocument.setUuid(insertStorageDocument(storageDocument)
                  .getUuid());
            storageDocDone.add(storageDocument);
         } catch (InsertionServiceEx insertExcp) {
            if (storageDocument != null) {
               StorageDocumentOnError storageDocumentOnError = new StorageDocumentOnError(
                     storageDocument.getMetadatas(), storageDocument
                           .getContent(), storageDocument.getFilePath(),
                     "INSERROR : " + insertExcp.getMessage());
               storageDocumentOnError.setMessageError("INSERROR");
               storageDocFailed.add(storageDocumentOnError);
            }
            if (allOrNothing) {
               rollback(storageDocDone);
               // Les documents "rollbackés" ne doivent pas apparaitre dans
               // BulkInsertionResults
               storageDocDone.clear();
               break;
            }
         }
      }
      return new BulkInsertionResults(new StorageDocuments(storageDocDone),
            new StorageDocumentsOnError(storageDocFailed));
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
      for (StorageDocument strDocument : Utils.nullSafeIterable(storageDocDone)) {
         try {
            deletionService.setDeletionServiceParameter(getDfceService());
            deletionService.deleteStorageDocument(new UUIDCriteria(strDocument
                  .getUuid(), null));
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
            throw new InsertionServiceEx(StorageMessageHandler
                  .getMessage(Constants.DEL_CODE_ERROR), delSerEx.getMessage(),
                  delSerEx);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx {
      try {
         Document docDfce = BeanMapper.storageDocumentToDfceDocument(
               getBaseDFCE(), storageDocument);
         final InputStream docContent = new ByteArrayInputStream(
               storageDocument.getContent());
         final String[] file = BeanMapper.findFileNameAndExtension(
               storageDocument, StorageTechnicalMetadatas.NOM_FICHIER
                     .getShortCode().toString());
         docDfce = getDfceService().getStoreService().storeDocument(docDfce,
               file[0], file[1], docContent);
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

   /**
    * {@inheritDoc}
    */
   public final <T> void setInsertionServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);

   }

}
