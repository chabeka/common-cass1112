/**
 * 
 */
package fr.urssaf.image.sae.services.batch.restore.support.stockage.multithreading;

import fr.urssaf.image.sae.services.batch.restore.support.stockage.batch.StorageDocumentFromRecycleWriter;
import fr.urssaf.image.sae.services.batch.restore.support.stockage.exception.RestoreMasseRuntimeException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Thread de restore d'un document dans DFCE dans les traitements de restore de
 * masse
 * 
 */
public class RestoreRunnable implements Runnable {

   private final StorageDocument storageDocument;

   private final StorageDocumentFromRecycleWriter service;

   /**
    * 
    * @param storageDocument
    *           document à insérer dans DFCE
    * @param service
    *           service de suppression
    */
   public RestoreRunnable(final StorageDocument storageDocument,
         final StorageDocumentFromRecycleWriter service) {
      this.storageDocument = storageDocument;
      this.service = service;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() {

      try {

         this.service
               .restoreFromRecycleBeanStorageDocument(storageDocument);

      } catch (Exception e) {

         throw new RestoreMasseRuntimeException(storageDocument, e);

      }

   }

   /**
    * 
    * @return document à insérer
    */
   public final StorageDocument getStorageDocument() {
      return this.storageDocument;
   }

}
