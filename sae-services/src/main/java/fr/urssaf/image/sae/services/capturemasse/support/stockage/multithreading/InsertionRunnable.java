/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.batch.StorageDocumentWriter;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Thread d'insertion d'un document dans DFCE dans les traitements de capture de
 * masse
 * 
 */
public class InsertionRunnable implements Runnable {

   private final int indexDocument;

   private final StorageDocument storageDocument;

   private final StorageDocumentWriter service;

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
         final StorageDocumentWriter service) {
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

         final StorageDocument newDocument = this.service
               .insertStorageDocument(storageDocument);
         storageDocument.setUuid(newDocument.getUuid());

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
