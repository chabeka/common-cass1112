package fr.urssaf.image.sae.metadata.exceptions;

/**
 * Exception levée lors d’une erreur inattendue
 */
public class MetadataRuntimeException extends RuntimeException {

   /**
    * constructeur de l'exception
    * 
    * @param message
    *           le message à renvoyer
    */
   public MetadataRuntimeException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message d'origine
    * @param exception
    *           l'exception d'origine
    */
   public MetadataRuntimeException(String message, RuntimeException exception) {
      super(message, exception);
   }
}
