package fr.urssaf.image.commons.padaf.exception;

/**
 * Exception levée lors d'un plantage du moteur de validation du format
 */
public class FormatValidationException extends Exception {

   private static final long serialVersionUID = -2275794908109305813L;

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    * @param cause
    *           la cause de l'exception
    */
   public FormatValidationException(String message, Throwable cause) {
      super(message, cause);
   }

}
