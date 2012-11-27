package fr.urssaf.image.sae.services.exception;

/**
 * Exception de parsing de requete
 */
public class SAESearchQueryParseException extends Exception {

   /**
    * Identifiant unique qui caractérise l'excepion
    */
   private static final long serialVersionUID = 1L;

   private String codeError;

   /**
    * Construit une nouvelle {@link SAESearchQueryParseException}.
    */
   public SAESearchQueryParseException() {
      super();
   }

   /**
    * Construit une nouvelle {@link SAESearchQueryParseException} avec un
    * message.
    * 
    * @param message
    *           Le message de l'erreur
    */
   public SAESearchQueryParseException(final String message) {
      super(message);
   }

   /**
    * Construit une nouvelle {@link SAESearchQueryParseException} avec un
    * message et une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public SAESearchQueryParseException(final String message,
         final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link SAESearchQueryParseException} avec un
    * message ,une cause données et un code erreur donné .
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    * @param codeErreur
    *           : Le code d'erreur
    */
   public SAESearchQueryParseException(final String codeErreur,
         final String message, final Throwable cause) {
      super(message, cause);
      setCodeError(codeErreur);
   }

   /**
    * @param codeError
    *           : Le code erreur.
    */
   public final void setCodeError(final String codeError) {
      this.codeError = codeError;
   }

   /**
    * @return Le code erreur.
    */
   public final String getCodeError() {
      return codeError;

   }
}
