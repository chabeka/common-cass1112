/**
 * 
 */
package fr.urssaf.image.sae.services.exception.modification;


/**
 * Exception levée lors d'une erreur de modification d'un document
 * 
 */
public class ModificationRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param exception
    *           erreur mère
    */
   public ModificationRuntimeException(Exception exception) {
      super(exception);
   }
}
