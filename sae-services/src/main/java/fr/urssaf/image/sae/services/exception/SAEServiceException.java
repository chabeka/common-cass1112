package fr.urssaf.image.sae.services.exception;

/**
 * Super classe des exceptions correspondant à des cas d'erreur <b>
 * prévus et contrôlés par l'appelé</b> et que la couche webServices doit catcher.</br>
 */
public class SAEServiceException extends Exception {
   private String codeError;

   /**
    * Identifiant unique qui caractérise l'excepion
    */
   private static final long serialVersionUID = -6505532287814387009L;

   /**
    * Construit une nouvelle {@link RetrievalServiceEx }.
    */
   public SAEServiceException() {
      super();
   }

   /**
    * Construit une nouvelle {@link SAEServiceException } avec un message.
    * 
    * @param message
    *           Le message de l'erreur
    */
   public SAEServiceException(final String message) {
      super(message);
   }

   /**
    * Construit une nouvelle {@link SAEServiceException } avec un message et une
    * cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public SAEServiceException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link SAEServiceException } avec un message ,une
    * cause données et un code erreur donné .
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    * @param codeErreur
    *           : Le code d'erreur
    */
   public SAEServiceException(final String codeErreur, final String message,
         final Throwable cause) {
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
