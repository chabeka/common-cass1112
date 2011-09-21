package fr.urssaf.image.sae.exception.enrichment;

import fr.urssaf.image.sae.metadata.exceptions.ControlException;

/**
 *Exception est levée s’il y a des erreurs lors de la récupération des codes
 * RND.
 * 
 */
public class ReferentialRndException extends ControlException {
   private static final long serialVersionUID = 5812830110677764248L;

   /**
    * Construit une nouvelle {@link ReferentialRndException } avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public ReferentialRndException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link ReferentialRndException }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public ReferentialRndException(final String message) {
      super(message);
   }
}