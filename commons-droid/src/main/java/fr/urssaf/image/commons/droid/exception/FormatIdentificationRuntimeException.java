package fr.urssaf.image.commons.droid.exception;

/**
 * Exception levée lors d'un problème non prévu lors de l'identification du
 * format d'un fichier.
 */
public class FormatIdentificationRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -831717734825912969L;

   /**
    * Constructeur
    * 
    * @param cause
    *           l'exception mère
    */
   public FormatIdentificationRuntimeException(Throwable cause) {
      super(cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    */
   public FormatIdentificationRuntimeException(String message) {
      super(message);
   }

}
