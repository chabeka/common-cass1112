package fr.urssaf.image.sae.services.exception.capture;

/**
 * Exception levée lorsque l’URL ECDE fournit à un des services de capture
 * pointe sur un fichier inexistant.
 */
public class CaptureEcdeUrlFileNotFoundEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link CaptureEcdeUrlFileNotFoundEx } avec un
    * message et une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public CaptureEcdeUrlFileNotFoundEx(final String message,
         final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link CaptureEcdeUrlFileNotFoundEx }avec un
    * message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public CaptureEcdeUrlFileNotFoundEx(final String message) {
      super(message);
   }
}
