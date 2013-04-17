package fr.urssaf.image.sae.metadata.exceptions;

public class MetadataReferenceException extends RuntimeException {
   /**
    * Exception levée lors d’une erreur inattendue
    */
    public MetadataReferenceException(String message) {
       super(message);
    }
}
