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
    * Constructeur par défaut
    */
   public MajRndException() {
      super();
   }
   
   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public MajRndException(Exception exception) {
      super(exception);
   }

   /**
    * Constructeur avec un message et une cause données.
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
    * Constructeur avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public MajRndException(final String message) {
      super(message);
   }

}
