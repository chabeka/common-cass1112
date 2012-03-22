package fr.urssaf.image.sae.services.exception.consultation;

/**
 * Exception à utiliser pour les erreurs lié aux métadonnées.<BR/>
 * 
 */
public class MetaDataUnauthorizedToConsultEx extends Exception {
   private static final long serialVersionUID = 5812830110677764248L;

   /**
    * Construit une nouvelle {@link MetaDataUnauthorizedToSearchEx } avec un
    * message et une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public MetaDataUnauthorizedToConsultEx(final String message,
         final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link MetaDataUnauthorizedToSearchEx }avec un
    * message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public MetaDataUnauthorizedToConsultEx(final String message) {
      super(message);
   }

   /**
    * Construit une nouvelle {@link MetaDataUnauthorizedToSearchEx } avec
    * l'erreur source
    * 
    * @param cause
    *           l'erreur source
    */
   public MetaDataUnauthorizedToConsultEx(Throwable cause) {
      super(cause);
   }
}