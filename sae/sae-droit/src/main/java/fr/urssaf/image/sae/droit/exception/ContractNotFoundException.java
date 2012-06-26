/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * Exception levée lorsque la référence au contrat n'est pas trouvée
 * 
 */
public class ContractNotFoundException extends Exception {

   private static final long serialVersionUID = -4838452143076871370L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    */
   public ContractNotFoundException(String message) {
      super(message);
   }

}
