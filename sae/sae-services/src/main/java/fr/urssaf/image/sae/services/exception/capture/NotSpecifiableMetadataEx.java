/**
 * 
 */
package fr.urssaf.image.sae.services.exception.capture;

/**
 * Exception est levée si les métadonnées spécifiables sont présentes.
 */
public class NotSpecifiableMetadataEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link NotSpecifiableMetadataEx} avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public NotSpecifiableMetadataEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link NotSpecifiableMetadataEx }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public NotSpecifiableMetadataEx(final String message) {
      super(message);
   }
}
