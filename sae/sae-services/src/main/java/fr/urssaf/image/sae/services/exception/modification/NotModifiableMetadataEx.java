/**
 * 
 */
package fr.urssaf.image.sae.services.exception.modification;

/**
 * Erreur levée lorsqu'une métadonnée n'est pas modifiable
 * 
 */
public class NotModifiableMetadataEx extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public NotModifiableMetadataEx(String message) {
      super(message);
   }

}
