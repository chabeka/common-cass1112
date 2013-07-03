package fr.urssaf.image.sae.services.dispatchers.handlers;

import fr.urssaf.image.sae.services.dispatchers.AbstractExceptionHandler;

/**
 * Handler utilisé par la chaine de responsabilité du dispatcher d'exception.
 * 
 * Le but de cet handler est uniquement de lever une exception. C'est le dernier
 * maillon de la chaine car l'exception provoque l'arrêt du traitement.
 */
public class ThrowerExceptionHandler extends AbstractExceptionHandler {

   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public final <T extends Exception> void handleException(T exception) throws T {
      throw exception;
   }
}
