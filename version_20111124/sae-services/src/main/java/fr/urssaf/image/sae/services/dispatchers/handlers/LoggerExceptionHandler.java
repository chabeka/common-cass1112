package fr.urssaf.image.sae.services.dispatchers.handlers;

import org.apache.log4j.Logger;

import fr.urssaf.image.sae.services.dispatchers.AbstractExceptionHandler;

/**
 * Logger d'exception pour la chaine de responsabilité utilisée par le
 * {@link fr.urssaf.image.sae.services.dispatchers.ExceptionDispatcher
 * dispatcher d'exceptions}.
 */
public class LoggerExceptionHandler extends AbstractExceptionHandler {

   static final private Logger LOGGER = Logger.getLogger(LoggerExceptionHandler.class);

   /**
    * Log l'exception reçue en paramètre
    */
   @Override
   public <T extends Exception> void handleException(T exception) throws T {
      LOGGER.error(exception);
   }
}