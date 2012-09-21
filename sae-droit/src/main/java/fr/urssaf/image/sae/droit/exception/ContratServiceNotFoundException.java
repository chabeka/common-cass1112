/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * Exception levée lorsque la référence au contrat n'est pas trouvée
 * 
 */
public class ContratServiceNotFoundException extends DroitGeneralException {

   private static final long serialVersionUID = -4838452143076871370L;

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause mère
    */
   public ContratServiceNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }

   // /**
   // * Constructeur
   // *
   // * @param message
   // * message de l'erreur
   // */
   // public ContratServiceNotFoundException(String message) {
   // super(message);
   // }

}
