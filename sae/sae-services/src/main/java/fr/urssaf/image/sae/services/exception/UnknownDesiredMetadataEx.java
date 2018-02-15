package fr.urssaf.image.sae.services.exception;

/**
 * Exception à utiliser pour les erreurs lié aux métadonnées Umétadonnées
 * absentes du référentiel.
 * 
 */
public class UnknownDesiredMetadataEx extends Exception {
   private static final long serialVersionUID = 5812830110677764248L;

   /**
    * Construit une nouvelle {@link UnknownDesiredMetadataEx} avec un
    * message et une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public UnknownDesiredMetadataEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link UnknownDesiredMetadataEx}avec un
    * message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public UnknownDesiredMetadataEx(final String message) {
      super(message);
   }

   /**
    * Construit une nouvelle {@link UnknownDesiredMetadataEx}avec une
    * erreur.
    * 
    * @param cause
    *           l'erreur source
    */
   public UnknownDesiredMetadataEx(Throwable cause) {
      super(cause);
   }

}