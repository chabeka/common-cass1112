package fr.urssaf.image.sae.services.batch.suppression.support.stockage.exception;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Exception levée dans le traitement de suppression en masse quand une exception
 * est levée lors de la mise a la corbeille dans DFCE
 * 
 * 
 */
public class SuppressionMasseRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private final Exception cause;
   
   private final StorageDocument storageDocument;

   /**
    * constructeur
    * 
    * @param index
    *           index de l'erreur
    * @param cause
    *           erreur source
    */
   public SuppressionMasseRuntimeException(StorageDocument storageDocument, Exception cause) {
      super(cause);
      this.storageDocument = storageDocument;
      this.cause = cause;
   }

   /**
    * @return cause de l'échec
    */
   @Override
   public final Exception getCause() {
      return cause;
   }
   
   /**
    * @return document du SAE qui a échoué dans l'insertion
    */
   public final StorageDocument getStorageDocument() {
      return storageDocument;
   }
}
