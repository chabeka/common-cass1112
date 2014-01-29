package fr.urssaf.image.sae.services.exception.capture;


/**
 * Exception levée lorsque l'URL ECDE fournit à un des services de capture est incorrecte.
 */
public class CaptureBadEcdeUrlEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link CaptureBadEcdeUrlEx } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public CaptureBadEcdeUrlEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link CaptureBadEcdeUrlEx }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public CaptureBadEcdeUrlEx(final String message) {
      super(message);
   }
}
