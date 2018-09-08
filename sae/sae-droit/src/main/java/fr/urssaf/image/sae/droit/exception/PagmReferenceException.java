/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * Exception levée lorsque la référence au contrat n'est pas trouvée
 * 
 */
public class PagmReferenceException extends RuntimeException {

   private static final long serialVersionUID = -4838452143076871370L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause mère
    */
   public PagmReferenceException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'exception
    */
   public PagmReferenceException(String message) {
      super(message);
   }

}
