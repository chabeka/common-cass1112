/**
 * 
 */
package fr.urssaf.image.sae.services.exception.transfert;

/**
 * Exception levée lors d'une erreur de transfert d'un document
 * 
 */
public class TransfertException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public TransfertException(Exception exception) {
      super(exception);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message d'erreur
    */
   public TransfertException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message d'erreur
    * @param exception
    *           l'exception mère
    */
   public TransfertException(String message, Exception exception) {
      super(message, exception);
   }

}
