/**
 * 
 */
package fr.urssaf.image.sae.services.exception.capture;

/**
 * Exception est levée lors de la vérification des métadonnées archivables.
 */
public class NotArchivableMetadataEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link NotArchivableMetadataEx} avec un
    * message et une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public NotArchivableMetadataEx(final String message,
         final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link NotArchivableMetadataEx }avec un
    * message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public NotArchivableMetadataEx(final String message) {
      super(message);
   }
}
