package fr.urssaf.image.sae.services.exception.format.validation;

/**
 * Exception levée lorsque le format du fichier est invalide.
 * 
 */
public class ValidationExceptionInvalidFile extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ValidationExceptionInvalidFile(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param cause
    *           cause d'origine
    */
   public ValidationExceptionInvalidFile(Throwable cause) {
      super(cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    * @param cause
    *           cause d'origine
    */
   public ValidationExceptionInvalidFile(String message, Throwable cause) {
      super(message, cause);
   }
}
