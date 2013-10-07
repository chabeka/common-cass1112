package fr.urssaf.image.sae.services.exception.search;


/**
 * Exception à utiliser pour les erreurs lié aux métadonnées absentes du
 * référentiel.
 * 
 */
public class UnknownLuceneMetadataEx extends Exception {
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
   public UnknownLuceneMetadataEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link MetaDataUnauthorizedToSearchEx }avec un
    * message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public UnknownLuceneMetadataEx(final String message) {
      super(message);
   }
}