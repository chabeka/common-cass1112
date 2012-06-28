/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * Exception levée lorsque la référence au contrat n'est pas trouvée
 * 
 */
public class ContratServiceReferenceException extends RuntimeException {

   private static final long serialVersionUID = -4838452143076871370L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    */
   public ContratServiceReferenceException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    * @param cause
    *           cause mère
    */
   public ContratServiceReferenceException(String message, Throwable cause) {
      super(message, cause);
   }

}
