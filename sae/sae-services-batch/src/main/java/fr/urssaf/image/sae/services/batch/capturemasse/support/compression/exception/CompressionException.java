package fr.urssaf.image.sae.services.batch.capturemasse.support.compression.exception;

/**
 * Exception levée lorsqu'il a une erreur de compression de document.
 * 
 */
public class CompressionException extends Exception {

   /**
    * serialVersionUID.
    */
   private static final long serialVersionUID = 1L;

   /**
    * Constructeur avec la cause de l'erreur.
    * 
    * @param cause
    *           cause
    */
   public CompressionException(Throwable cause) {
      super(cause);
   }
}
