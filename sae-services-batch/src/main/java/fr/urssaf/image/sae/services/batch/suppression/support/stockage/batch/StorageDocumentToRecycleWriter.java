package fr.urssaf.image.sae.services.batch.suppression.support.stockage.batch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.suppression.support.stockage.multithreading.SuppressionPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.suppression.support.stockage.multithreading.SuppressionRunnable;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item writer permettant de déplacer un document dans la corbeille.
 *
 */
@Component
public class StorageDocumentToRecycleWriter implements
      ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentToRecycleWriter.class);
   
   /**
    * Pool de thread de suppression.
    */
   @Autowired
   private SuppressionPoolThreadExecutor poolExecutor;
   
   /**
    * Provider pour le stockage.
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;
   
   private static final String TRC_INSERT = "StorageDocumentToRecycleWriter()";
   
   /**
    * {@inheritDoc}
    */
   @Override
   public void write(List<? extends StorageDocument> items) throws Exception {
      Runnable command;

      for (StorageDocument storageDocument : Utils.nullSafeIterable(items)) {

         command = new SuppressionRunnable(storageDocument, this);

         poolExecutor.execute(command);

         LOGGER.debug("{} - nombre de documents en attente dans le pool : {}",
               TRC_INSERT, poolExecutor.getQueue().size());
      }
   }

   /**
    * Deplacement du document dans la corbeille
    * 
    * @param storageDocument
    *           document à mettre dans la corbeille
    * @return le document avec l'uuid renseigné
    * @throws UpdateServiceEx
    *            Exception levée lors de la persistance
    */
   public final void moveToRecycleBeanStorageDocument(
         final StorageDocument storageDocument) throws UpdateServiceEx {

      // constitue la liste des métadonnées à modifier
      List<StorageMetadata> modifiedMetadatas = new ArrayList<StorageMetadata>();
      for (StorageMetadata metadata : storageDocument.getMetadatas()) {
         // on ne remplit que les dates de mise a la corbeille
         // et l'id de suppression
         if (Constantes.CODE_COURT_META_ID_SUPPRESSION.equals(metadata.getShortCode())
               || Constantes.CODE_COURT_META_DATE_CORBEILLE.equals(metadata.getShortCode())) {
            modifiedMetadatas.add(metadata);
         }
      }
      
      // ETAPE 1 : mise a jour de l'identifiant de suppression et de la date de mise a la corbeille
      updateDocument(storageDocument, modifiedMetadatas, null);
      
      // ETAPE 2 : mise a la corbeille du document
      try {
         
         serviceProvider.getStorageDocumentService().moveStorageDocumentToRecycleBin(
               storageDocument.getUuid());
         
      } catch (Exception except) {
         
         // quand il y a une erreur de mise a la corbeille, on tente de supprimer les metas ajoutees
         updateDocument(storageDocument, null, modifiedMetadatas);

         throw new UpdateServiceEx(except);

         // nous sommes obligés de récupérer les throwable pour les erreurs DFCE 
      } catch (Throwable except) {
         
         // quand il y a une erreur de mise a la corbeille, on tente de supprimer les metas ajoutees
         updateDocument(storageDocument, null, modifiedMetadatas);

         throw new UpdateServiceEx(new Exception(except));

      }
   }

   /**
    * Methode permettant de modifier le document.
    * 
    * @param storageDocument document
    * @param modifiedMetadatas liste des metas modifiées
    * @param deletedMetadatas liste des metas supprimées
    * @throws UpdateServiceEx 
    *            Exception levée lors de la persistance
    */
   private void updateDocument(final StorageDocument storageDocument,
         List<StorageMetadata> modifiedMetadatas,
         List<StorageMetadata> deletedMetadatas) throws UpdateServiceEx {
      try {
         
         serviceProvider.getStorageDocumentService().updateStorageDocument(
               storageDocument.getUuid(), modifiedMetadatas, deletedMetadatas);

      } catch (Exception except) {

         throw new UpdateServiceEx(except);

         // nous sommes obligés de récupérer les throwable pour les erreurs DFCE 
      } catch (Throwable except) {

         throw new UpdateServiceEx(new Exception(except));

      }
   }
}
