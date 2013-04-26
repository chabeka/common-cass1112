package fr.urssaf.image.sae.rnd.exception;

/**
 * Exception à utiliser pour les erreurs lors de la mise à jour globale du RND
 * 
 */
public class MajRndException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link MajRndException }.
    */
   public MajRndException() {
      super();
   }

   /**
    * Construit une nouvelle {@link MajRndException } avec un message et une
    * cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public MajRndException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link MajRndException }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public MajRndException(final String message) {
      super(message);
   }

}
