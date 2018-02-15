/**
 * 
 */
package fr.urssaf.image.sae.services.exception.suppression;


/**
 * Exception levée lors d'une erreur de suppression d'un document
 * 
 */
public class SuppressionException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public SuppressionException(Exception exception) {
      super(exception);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message d'erreur
    */
   public SuppressionException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message d'erreur
    * @param cause
    *           l'exception
    */
   public SuppressionException(String message, Throwable cause) {
      super(message, cause);
   }

}
