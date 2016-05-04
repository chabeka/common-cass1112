package fr.urssaf.image.sae.format.validation.exceptions;

/**
 * Exception remontée lors de l'impossibilité d'initialiser le validateur.
 */
public class ValidatorInitialisationException extends Exception {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ValidatorInitialisationException(String message) {
      super(message);
   }
   
   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    * @param cause
    *           cause          
    */
   public ValidatorInitialisationException(String message, Throwable cause) {
      super(message,cause);
   }
   
}   