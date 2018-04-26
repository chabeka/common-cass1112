package fr.urssaf.image.sae.format.validation.exceptions;

/**
 * Toutes exceptions levées par l’outil de validation appelé.
 */
public class ValidationRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ValidationRuntimeException(String message) {
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
   public ValidationRuntimeException(String message, Throwable cause) {
      super(message,cause);
   }
   
}
