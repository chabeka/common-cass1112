/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.batch.VirtualStorageDocumentWriter;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.exception.InsertionMasseVirtualRuntimeException;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Thread d'insertion d'un document virtuel dans DFCE pour les traitements de
 * capture de masse
 * 
 */
public class InsertionVirtualRunnable implements Runnable {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(InsertionVirtualRunnable.class);

   private final int indexDocument;

   private final VirtualStorageDocument storageDocument;

   private final VirtualStorageDocumentWriter service;

   /**
    * 
    * @param indexDocument
    *           index du document dans le sommaire, commence à 0
    * @param storageDocument
    *           document à insérer dans DFCE
    * @param service
    *           service d'insertion
    */
   public InsertionVirtualRunnable(final int indexDocument,
         final VirtualStorageDocument storageDocument,
         final VirtualStorageDocumentWriter service) {
      this.indexDocument = indexDocument;
      this.storageDocument = storageDocument;
      this.service = service;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void run() {
      String trcPrefix = "run";
      LOGGER.debug("{} - début", trcPrefix);

      try {

         final VirtualStorageDocument newDocument = this.service
               .insertStorageDocument(storageDocument);
         storageDocument.setUuid(newDocument.getUuid());

      } catch (Exception e) {

         throw new InsertionMasseVirtualRuntimeException(indexDocument,
               storageDocument, e);

      }

      LOGGER.debug("{} - fin", trcPrefix);

   }

   /**
    * @return le document virtuel
    */
   public final VirtualStorageDocument getStorageDocument() {
      return storageDocument;
   }

   /**
    * @return l'index du document dans le fichier sommaire
    */
   public final int getIndexDocument() {
      return indexDocument;
   }

}
