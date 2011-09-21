package fr.urssaf.image.sae.exception.capture;

/**
 *Exception est levée lors du contrôle de présence des métadonnées
 * obligatoires.
 * 
 *@author rhofir.
 */
public class RequiredArchivableMetadataEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link RequiredArchivableMetadataEx} avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public RequiredArchivableMetadataEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link RequiredArchivableMetadataEx }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public RequiredArchivableMetadataEx(final String message) {
      super(message);
   }
}
