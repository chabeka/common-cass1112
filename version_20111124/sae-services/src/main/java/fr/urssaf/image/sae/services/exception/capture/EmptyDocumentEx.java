/**
 * 
 */
package fr.urssaf.image.sae.services.exception.capture;


/**
 * Une exception est levée si la taille du document est égale à 0 octet.
 * 
 * @author rhofir.
 */
public class EmptyDocumentEx extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link EmptyDocumentEx} avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public EmptyDocumentEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link EmptyDocumentEx }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public EmptyDocumentEx(final String message) {
      super(message);
   }

}
