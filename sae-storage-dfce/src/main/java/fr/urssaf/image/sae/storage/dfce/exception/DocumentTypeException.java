/**
 * 
 */
package fr.urssaf.image.sae.storage.dfce.exception;

import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

/**
 * Exception levée lors d'une erreur de type de document
 * 
 */
public class DocumentTypeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    * @param exception
    *           exception mère
    */
   public DocumentTypeException(String message, ConnectionServiceEx exception) {
      super(message, exception);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public DocumentTypeException(String message) {
      super(message);
   }

}
