package fr.urssaf.image.sae.rnd.exception;

/**
 * Exception à utiliser pour les erreurs lors de la mise à jour des correspondances
 * 
 */
public class MajCorrespondancesException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link MajCorrespondancesException }.
    */
   public MajCorrespondancesException() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public MajCorrespondancesException(Exception exception) {
      super(exception);
   }
   
   /**
    * Construit une nouvelle {@link MajCorrespondancesException } avec un message et une
    * cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public MajCorrespondancesException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link MajCorrespondancesException }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public MajCorrespondancesException(final String message) {
      super(message);
   }

}
