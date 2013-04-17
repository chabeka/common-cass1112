package fr.urssaf.image.sae.metadata.exceptions;

public class MetadataRuntimeException extends RuntimeException {

  /**
   * Exception levée lors d’une erreur inattendue
   */
   public MetadataRuntimeException(String message) {
      super(message);
   }
}
