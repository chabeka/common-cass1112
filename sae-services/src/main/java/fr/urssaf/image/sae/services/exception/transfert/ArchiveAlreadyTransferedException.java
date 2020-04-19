/**
 * 
 */
package fr.urssaf.image.sae.services.exception.transfert;


/**
 * Exception levée quand le transfert du document à déjà été réalisé
 * 
 */
public class ArchiveAlreadyTransferedException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public ArchiveAlreadyTransferedException(Exception exception) {
      super(exception);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message d'erreur
    */
   public ArchiveAlreadyTransferedException(String message) {
      super(message);
   }

}
