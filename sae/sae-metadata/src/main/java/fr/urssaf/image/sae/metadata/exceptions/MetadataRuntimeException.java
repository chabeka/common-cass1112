package fr.urssaf.image.sae.metadata.exceptions;


/**
 * Exception levée lors d’une erreur inattendue
 */
public class MetadataRuntimeException extends RuntimeException {

  /**
   * constructeur de l'exception
   * @param message le message à renvoyer
   */
   public MetadataRuntimeException(String message) {
      super(message);
   }
}
