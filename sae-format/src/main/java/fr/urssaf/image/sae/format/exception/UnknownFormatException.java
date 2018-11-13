package fr.urssaf.image.sae.format.exception;

/**
 * Le format demandé n’existe pas en base.
 */
public class UnknownFormatException extends Exception {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public UnknownFormatException(String message) {
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
   public UnknownFormatException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructeur
    * 
    * @param cause
    *           la cause de l'exception
    */
   public UnknownFormatException(Throwable cause) {
      super(cause);
   }

}