package fr.urssaf.image.sae.storage.dfce.services.support.multithreading;

import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;

/**
 * classe de Thread pour l'insertion d'un document dans DFCE lors du traitement
 * de capture en masse
 * 
 * 
 */
public class InsertionRunnable implements Runnable {

   private final int indexDocument;

   private final StorageDocument storageDocument;

   private final InsertionService service;

   /**
    * 
    * @param indexDocument
    *           index du document dans le sommaire, commence à 0
    * @param storageDocument
    *           document à insérer dans DFCE
    * @param service
    *           service d'insertion
    */
   public InsertionRunnable(int indexDocument, StorageDocument storageDocument,
         InsertionService service) {
      this.indexDocument = indexDocument;
      this.storageDocument = storageDocument;
      this.service = service;

   }

   @Override
   public final void run() {

      try {

         StorageDocument newDocument = this.service
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
