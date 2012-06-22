package fr.urssaf.image.sae.services.dispatchers.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.services.dispatchers.AbstractExceptionHandler;

/**
 * Logger d'exception pour la chaine de responsabilité utilisée par le
 * {@link fr.urssaf.image.sae.services.dispatchers.ExceptionDispatcher
 * dispatcher d'exceptions}.
 */
public class LoggerExceptionHandler extends AbstractExceptionHandler {

   private static final Logger LOGGER = LoggerFactory.getLogger(LoggerExceptionHandler.class);

   /**
    * Log l'exception reçue en paramètre
    */
   @Override
   public final <T extends Exception> void handleException(T exception) throws T {
      if (exception!=null) {
         LOGGER.error(exception.getMessage(),exception);
      }
   }
}