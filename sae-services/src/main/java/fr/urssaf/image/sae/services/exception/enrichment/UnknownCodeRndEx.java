package fr.urssaf.image.sae.services.exception.enrichment;

import fr.urssaf.image.sae.metadata.exceptions.ControlException;

/**
 *Exception est levée si le code RND est inconnu.
 * 
 */
public class UnknownCodeRndEx extends ControlException {
   private static final long serialVersionUID = 5812830110677764248L;

   /**
    * Construit une nouvelle {@link UnknownCodeRndEx } avec un message et une
    * cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public UnknownCodeRndEx(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link UnknownCodeRndEx }avec un message.
    * 
    * @param message
    *           : Le message de l'erreur
    */
   public UnknownCodeRndEx(final String message) {
      super(message);
   }
}