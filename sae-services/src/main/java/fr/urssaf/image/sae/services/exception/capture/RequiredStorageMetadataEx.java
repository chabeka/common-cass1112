/**
 * 
 */
package fr.urssaf.image.sae.services.exception.capture;

/**
 * Exception est levée lors du contrôle de présence des métadonnées obligatoires
 * 
 * @author rhofir.
 * 
 */
public class RequiredStorageMetadataEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link RequiredStorageMetadataEx} avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public RequiredStorageMetadataEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link RequiredStorageMetadataEx }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public RequiredStorageMetadataEx(final String message) {
      super(message);
   }
}
