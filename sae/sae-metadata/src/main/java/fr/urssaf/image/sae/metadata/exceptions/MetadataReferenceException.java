package fr.urssaf.image.sae.metadata.exceptions;

/**
 * 
 * Exception levée lors d’une erreur inattendue
 *
 */
public class MetadataReferenceException extends RuntimeException {
   /**
    * constructeur de l'exception
    * @param message message associé à l'exception
    */
    public MetadataReferenceException(String message) {
       super(message);
    }
}
