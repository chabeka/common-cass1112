/**
 * 
 */
package fr.urssaf.image.sae.services.exception.transfert;


/**
 * Exception levée lors de la capture de masse
 * 
 */
public class TransfertMasseRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private static final String MESSAGE = "Une erreur interne à l'application "
         + "est survenue lors du transfert de masse";

   /**
    * Constructeur
    * 
    * @param cause
    *           cause de l'exception
    */
   public TransfertMasseRuntimeException(final Throwable cause) {
      super(MESSAGE, cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public TransfertMasseRuntimeException(final String message) {
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
   public TransfertMasseRuntimeException(final String message,
         final Throwable cause) {
      super(message, cause);
   }

}
