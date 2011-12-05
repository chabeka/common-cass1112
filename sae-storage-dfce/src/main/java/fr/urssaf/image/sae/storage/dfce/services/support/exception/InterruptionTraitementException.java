package fr.urssaf.image.sae.storage.dfce.services.support.exception;

/**
 * Exception levée lorsque la reprise d'un traitement après une interruption a
 * échoué
 * 
 * 
 */
public class InterruptionTraitementException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause de l'exception
    */
   public InterruptionTraitementException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * 
    * @param cause
    *           cause de l'exception
    */
   public InterruptionTraitementException(Throwable cause) {
      super(cause);
   }
}
