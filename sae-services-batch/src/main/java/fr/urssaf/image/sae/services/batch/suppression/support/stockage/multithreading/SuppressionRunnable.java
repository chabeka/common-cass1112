/**
 * 
 */
package fr.urssaf.image.sae.services.batch.suppression.support.stockage.multithreading;

import fr.urssaf.image.sae.services.batch.suppression.support.stockage.batch.StorageDocumentToRecycleWriter;
import fr.urssaf.image.sae.services.batch.suppression.support.stockage.exception.SuppressionMasseRuntimeException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Thread de suppression d'un document dans DFCE dans les traitements de suppression de
 * masse
 * 
 */
public class SuppressionRunnable implements Runnable {

   private final StorageDocument storageDocument;

   private final StorageDocumentToRecycleWriter service;

   /**
    * 
    * @param storageDocument
    *           document à insérer dans DFCE
    * @param service
    *           service de suppression
    */
   public SuppressionRunnable(final StorageDocument storageDocument,
         final StorageDocumentToRecycleWriter service) {
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
               .moveToRecycleBeanStorageDocument(storageDocument);

      } catch (Exception e) {

         throw new SuppressionMasseRuntimeException(storageDocument, e);

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
