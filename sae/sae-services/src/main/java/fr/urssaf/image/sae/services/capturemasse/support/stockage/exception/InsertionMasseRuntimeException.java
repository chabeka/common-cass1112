package fr.urssaf.image.sae.services.capturemasse.support.stockage.exception;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Exception levée dans le traitement de capture en masse quand une exception
 * est levée lors de l'insertion dans DFCE
 * 
 * 
 */
public class InsertionMasseRuntimeException extends
      AbstractInsertionMasseRuntimeException {

   private static final long serialVersionUID = 1L;

   private final StorageDocument storageDocument;

   /**
    * 
    * @param index
    *           index dans le sommaire où l'insertion a échoué
    * @param storageDocument
    *           document du SAE qui a échoué dans l'insertion
    * @param cause
    *           cause de l'échec
    */
   public InsertionMasseRuntimeException(int index,
         StorageDocument storageDocument, Exception cause) {
      super(index, cause);
      this.storageDocument = storageDocument;
   }

   /**
    * @return document du SAE qui a échoué dans l'insertion
    */
   public final StorageDocument getStorageDocument() {
      return storageDocument;
   }

}
