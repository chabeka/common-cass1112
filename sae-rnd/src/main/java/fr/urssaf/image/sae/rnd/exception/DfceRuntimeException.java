package fr.urssaf.image.sae.rnd.exception;

/**
 * Exception à utiliser pour les erreurs lors des appels à DFCE
 * 
 */
public class DfceRuntimeException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link DfceRuntimeException }.
    */
   public DfceRuntimeException() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public DfceRuntimeException(Exception exception) {
      super(exception);
   }
   
   /**
    * Construit une nouvelle {@link DfceRuntimeException } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public DfceRuntimeException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link DfceRuntimeException }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public DfceRuntimeException(final String message) {
      super(message);
   }

}
