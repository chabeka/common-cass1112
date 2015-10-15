/**
 * 
 */
package fr.urssaf.image.sae.services.exception.modification;

/**
 * Exception levée lors d'une erreur de modification d'un document
 * 
 */
public class ModificationException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public ModificationException(Exception exception) {
      super(exception);
   }
   
   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ModificationException(String message) {
      super(message);
   }
}
