package fr.urssaf.image.sae.rnd.exception;

/**
 * Exception à utiliser pour les erreurs lors des appels à la BDD du SAE
 * 
 */
public class SaeBddRuntimeException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link SaeBddRuntimeException }.
    */
   public SaeBddRuntimeException() {
      super();
   }

   /**
    * Construit une nouvelle {@link SaeBddRuntimeException } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public SaeBddRuntimeException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link SaeBddRuntimeException }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public SaeBddRuntimeException(final String message) {
      super(message);
   }

}
