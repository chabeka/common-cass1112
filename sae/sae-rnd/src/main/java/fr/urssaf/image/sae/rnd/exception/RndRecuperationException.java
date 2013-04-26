package fr.urssaf.image.sae.rnd.exception;

/**
 * Exception à utiliser pour les erreurs lors de la récupération du RND par les
 * WS de l'ADRN
 * 
 */
public class RndRecuperationException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Construit une nouvelle {@link RndRecuperationException }.
    */
   public RndRecuperationException() {
      super();
   }

   /**
    * Construit une nouvelle {@link RndRecuperationException } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public RndRecuperationException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link RndRecuperationException }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public RndRecuperationException(final String message) {
      super(message);
   }

}
