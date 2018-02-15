package fr.urssaf.image.sae.services.exception.capture;


/**
 * Exception levée lorsque l'UUID fournie à l'archivage existe déjà en base
 */
public class CaptureExistingUuuidException extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link CaptureExistingUuuidException } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public CaptureExistingUuuidException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link CaptureExistingUuuidException }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public CaptureExistingUuuidException(final String message) {
      super(message);
   }
}
