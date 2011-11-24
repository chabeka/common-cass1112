/**
 * 
 */
package fr.urssaf.image.sae.services.exception.capture;

/**
 * Exception est levée lors de la vérification du hash code.
 * 
 * @author rhofir.
 * 
 */
public class UnknownHashCodeEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link UnknownHashCodeEx} avec un message et une
    * cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public UnknownHashCodeEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link UnknownHashCodeEx }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public UnknownHashCodeEx(final String message) {
      super(message);
   }
}
