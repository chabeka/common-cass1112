package fr.urssaf.image.sae.services.batch.restore.support.stockage.batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.restore.support.stockage.multithreading.RestorePoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.restore.support.stockage.multithreading.RestoreRunnable;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item writer permettant de déplacer un document depuis la corbeille.
 *
 */
@Component
public class StorageDocumentFromRecycleWriter implements
      ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentFromRecycleWriter.class);

   /**
    * Pool de thread de restore.
    */
   @Autowired
   private RestorePoolThreadExecutor poolExecutor;

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   /**
    * Provider pour le stockage.
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   private static final String TRC_INSERT = "StorageDocumentFromRecycleWriter()";

   /**
    * {@inheritDoc}
    */
   @Override
   public void write(List<? extends StorageDocument> items) throws Exception {
      Runnable command;

      for (StorageDocument storageDocument : Utils.nullSafeIterable(items)) {

         command = new RestoreRunnable(storageDocument, this);

         poolExecutor.execute(command);

         LOGGER.debug("{} - nombre de documents en attente dans le pool : {}",
               TRC_INSERT, poolExecutor.getQueue().size());
      }
   }

   /**
    * Restore du document depuis la corbeille
    * 
    * @param storageDocument
    *           document à mettre dans la corbeille
    * @return le document avec l'uuid renseigné
    * @throws UpdateServiceEx
    *            Exception levée lors de la persistance
    */
   public final void restoreFromRecycleBeanStorageDocument(
         final StorageDocument storageDocument) throws UpdateServiceEx {

      // constitue la liste des métadonnées à modifier
      List<StorageMetadata> modifiedMetadatas = new ArrayList<StorageMetadata>();
      List<StorageMetadata> deletedMetadatas = new ArrayList<StorageMetadata>();
      for (StorageMetadata metadata : storageDocument.getMetadatas()) {
         // on ne remplit que les dates de mise a la corbeille
         // et l'id de suppression
         if (Constantes.CODE_COURT_META_ID_RESTORE.equals(metadata
               .getShortCode())) {
            modifiedMetadatas.add(metadata);
         } else if (Constantes.CODE_COURT_META_DATE_CORBEILLE.equals(metadata
               .getShortCode())) {
            deletedMetadatas.add(metadata);
         }
      }

      // ETAPE 1 : on restore le document
      restoreDocument(storageDocument);

      // ETAPE 2 : mise à jour de l'identifiant de restore de masse et
      // suppression de la date de mise a la corbeille
      try {

         serviceProvider.getStorageDocumentService().updateStorageDocument(
               storageDocument.getUuid(), modifiedMetadatas, deletedMetadatas);

      } catch (Exception except) {

         // quand il y a une erreur de mise a jour du document, on tente de
         // remettre le doc a la corbeille
         moveToRecycleBean(storageDocument);

         throw new UpdateServiceEx(except);

         // nous sommes obligés de récupérer les throwable pour les erreurs DFCE
      } catch (Throwable except) {

         // quand il y a une erreur de mise a jour du document, on tente de
         // remettre le doc a la corbeille
         moveToRecycleBean(storageDocument);

         throw new UpdateServiceEx(new Exception(except));

      }
   }
   
   /**
    * Retourne la liste des métadonnées du document stocké dans la corbeille
    * 
    * @param storageDocument
    * @return
    * @throws ReferentialException
    * @throws IOException 
    * @throws StorageException 
    */
   public List<StorageMetadata> getMetadatasDocFromRecycleBean(
         StorageDocument storageDocument) throws ReferentialException,
         StorageException, IOException {

      String prefixeTrc= "getMetadatasDocFromRecycleBean()";
      
      // constitue la liste des métadonnées du document à retourner
      List<StorageMetadata> metadatasStorageDoc = new ArrayList<StorageMetadata>();
      
      UUIDCriteria uuidCriteria = new UUIDCriteria(storageDocument.getUuid(),
            null);
    
      try {
         metadatasStorageDoc = serviceProvider.getStorageDocumentService()
               .getStorageDocumentFromRecycleBin(uuidCriteria).getMetadatas();
         
      } catch (RecycleBinServiceEx frozenExcept) {
         LOGGER.debug(
               "{} - Une exception a été levée lors de la recherche du document dans la corbeille: {}",
               prefixeTrc, frozenExcept.getMessage());
         throw new RecycleBinServiceEx(
               StorageMessageHandler.getMessage(Constants.RST_CODE_ERROR),
               frozenExcept.getMessage(), frozenExcept);
      }
      return metadatasStorageDoc;
   }

   /**
    * Methode permettant de restorer un document.
    * 
    * @param storageDocument
    *           document
    * @throws UpdateServiceEx
    *            Exception levée lors de la persistance
    */
   private void restoreDocument(final StorageDocument storageDocument)
         throws UpdateServiceEx {

      try {
         serviceProvider.getStorageDocumentService()
               .restoreStorageDocumentFromRecycleBin(storageDocument.getUuid());
      } catch (Exception except) {

         throw new UpdateServiceEx(except);

         // nous sommes obligés de récupérer les throwable pour les erreurs DFCE
      } catch (Throwable except) {

         throw new UpdateServiceEx(new Exception(except));

      }
   }

   /**
    * Methode permettant de deplacer un document dans la corbeille.
    * 
    * @param storageDocument
    *           document
    * @throws UpdateServiceEx
    *            Exception levée lors de la persistance
    */
   private void moveToRecycleBean(final StorageDocument storageDocument)
         throws UpdateServiceEx {

      try {
         serviceProvider.getStorageDocumentService()
               .moveStorageDocumentToRecycleBin(storageDocument.getUuid());
      } catch (Exception except) {

         throw new UpdateServiceEx(except);

         // nous sommes obligés de récupérer les throwable pour les erreurs DFCE
      } catch (Throwable except) {

         throw new UpdateServiceEx(new Exception(except));

      }
   }

}
