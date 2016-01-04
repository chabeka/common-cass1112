/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.exception;


/**
 * Exception levée lors de la capture de masse
 * 
 */
public class CaptureMasseRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private static final String MESSAGE = "Une erreur interne à l'application "
         + "est survenue lors du traitement de masse";

   /**
    * Constructeur
    * 
    * @param cause
    *           cause de l'exception
    */
   public CaptureMasseRuntimeException(final Throwable cause) {
      super(MESSAGE, cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public CaptureMasseRuntimeException(final String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    * @param cause
    *           exception mère
    */
   public CaptureMasseRuntimeException(final String message,
         final Throwable cause) {
      super(message, cause);
   }

}