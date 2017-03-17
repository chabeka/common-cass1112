/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading;

import java.util.UUID;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch.AbstractDocumentWriterListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Thread d'insertion d'un document dans DFCE dans les traitements de capture de
 * masse
 * 
 */
public class InsertionRunnable implements Runnable {

   private final int indexDocument;

   private final StorageDocument storageDocument;

   private final AbstractDocumentWriterListener service;

   /**
    * 
    * @param indexDocument
    *           index du document dans le sommaire, commence à 0
    * @param storageDocument
    *           document à insérer dans DFCE
    * @param service
    *           service d'insertion
    */
   public InsertionRunnable(final int indexDocument,
         final StorageDocument storageDocument,
         final AbstractDocumentWriterListener service) {
      this.indexDocument = indexDocument;
      this.storageDocument = storageDocument;
      this.service = service;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() {

      try {

         final UUID uuid = this.service.launchTraitement(storageDocument);
         storageDocument.setUuid(uuid);

      } catch (Exception e) {

         throw new InsertionMasseRuntimeException(indexDocument,
               storageDocument, e);

      }

   }

   /**
    * 
    * @return document à insérer
    */
   public final StorageDocument getStorageDocument() {
      return this.storageDocument;
   }

   /**
    * 
    * @return index du document dans le sommaire
    */
   public final int getIndexDocument() {
      return this.indexDocument;
   }

}
