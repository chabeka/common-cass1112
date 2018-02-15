/**
 * 
 */
package fr.urssaf.image.sae.services.reprise.exception;


/**
 * Exception levée quand le transfert du document à déjà été réalisé
 * 
 */
public class TraitementRepriseAlreadyDoneException extends Exception {

   /**
    * SUID
    */
   private static final long serialVersionUID = 7160415792425883040L;

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public TraitementRepriseAlreadyDoneException(Exception exception) {
      super(exception);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message d'erreur
    */
   public TraitementRepriseAlreadyDoneException(String message) {
      super(message);
   }

}
