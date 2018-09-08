package fr.urssaf.image.sae.services.exception.format.identification;

/**
 * Exception levée lorsque le format du fichier est invalide.
 * 
 * Erreur technique lors de l'identification d'un fichier.<br>
 * Exemple : {@link java.io.IOException}
 */
public class FormatIdentificationRuntimeException extends RuntimeException {

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
   public FormatIdentificationRuntimeException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param cause
    *           cause d'origine
    */
   public FormatIdentificationRuntimeException(Throwable cause) {
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
   public FormatIdentificationRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }
}
