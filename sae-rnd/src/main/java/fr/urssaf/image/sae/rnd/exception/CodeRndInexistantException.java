package fr.urssaf.image.sae.rnd.exception;

/**
 * Exception à utiliser lorsque le code RND n'existe pas
 * 
 */
public class CodeRndInexistantException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link CodeRndInexistantException }.
    */
   public CodeRndInexistantException() {
      super();
   }

   /**
    * Construit une nouvelle {@link CodeRndInexistantException } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public CodeRndInexistantException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link CodeRndInexistantException }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public CodeRndInexistantException(final String message) {
      super(message);
   }

}
