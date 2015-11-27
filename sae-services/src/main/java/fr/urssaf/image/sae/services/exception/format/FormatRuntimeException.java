package fr.urssaf.image.sae.services.exception.format;

/**
 * Exception levée lorsque qu'une erreur de type Runtime survient lors des
 * contrôles du format de fichier
 * 
 */
public class FormatRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 6799939258355439L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public FormatRuntimeException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param cause
    *           cause d'origine
    */
   public FormatRuntimeException(Throwable cause) {
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
   public FormatRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }
}
