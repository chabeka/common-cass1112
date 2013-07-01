package fr.urssaf.image.sae.services.capturemasse.support.stockage.exception;

import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Exception levée dans le traitement de capture en masse quand une exception
 * est levée lors de l'insertion dans DFCE
 * 
 * 
 */
public class InsertionMasseVirtualRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private final int index;
   private final VirtualStorageDocument storageDocument;
   private final Exception cause;

   /**
    * 
    * @param index
    *           index dans le sommaire où l'insertion a échoué
    * @param storageDocument
    *           document du SAE qui a échoué dans l'insertion
    * @param cause
    *           cause de l'échec
    */
   public InsertionMasseVirtualRuntimeException(int index,
         VirtualStorageDocument storageDocument, Exception cause) {
      super(cause);
      this.index = index;
      this.storageDocument = storageDocument;
      this.cause = cause;
   }

   /**
    * @return index dans le sommaire où l'insertion a échoué
    */
   public final int getIndex() {
      return index;
   }

   /**
    * @return document du SAE qui a échoué dans l'insertion
    */
   public final VirtualStorageDocument getStorageDocument() {
      return storageDocument;
   }

   /**
    * @return cause de l'échec
    */
   @Override
   public final Exception getCause() {
      return cause;
   }

}
