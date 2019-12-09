package fr.urssaf.image.sae.webservices.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LoggingFilter extends Filter<ILoggingEvent> {


   @Override
   public FilterReply decide(ILoggingEvent event) {
      if (event.getLoggerName().contains("LogHandler")
            || event.getLoggerName().contains("LogSkeletonAspect")) {
         return FilterReply.ACCEPT;
      } else {
         return FilterReply.DENY;
      }

   }

}
