/**
 * 
 */
package fr.urssaf.image.sae.services.exception.transfert;

/**
 * Erreur levée lorsqu'une métadonnée n'est pas modifiable
 * 
 */
public class NotTransferableMetadataEx extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public NotTransferableMetadataEx(String message) {
      super(message);
   }

}
