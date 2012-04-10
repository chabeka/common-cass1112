package fr.urssaf.image.sae.services.exception.capture;

/**
 * Exception levée lorsque l'URL ECDE fournie au service de capture pointe sur
 * un répertoire de traitement pour lequel le SAE n'a pas les droits d’écriture.
 * 
 * @author rhofir.
 */
public class CaptureEcdeWriteFileEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link CaptureEcdeWriteFileEx } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public CaptureEcdeWriteFileEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link CaptureEcdeWriteFileEx }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public CaptureEcdeWriteFileEx(final String message) {
      super(message);
   }
}
