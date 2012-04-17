/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Classe permettant de réaliser un filtre sur les logs spring
 * 
 */
public class SaeSpringFilter extends Filter<ILoggingEvent> {

   /**
    * {@inheritDoc}
    */
   @Override
   public FilterReply decide(ILoggingEvent event) {

      if (event.getLoggerName().contains("org.springframework.batch")) {
         return FilterReply.DENY;
      } else {
         return FilterReply.ACCEPT;
      }
   }

}
