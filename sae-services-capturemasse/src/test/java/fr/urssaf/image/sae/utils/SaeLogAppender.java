/**
 * 
 */
package fr.urssaf.image.sae.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * classe Appender de log afin de réaliser les tests sur les logs
 */
public class SaeLogAppender extends AppenderBase<ILoggingEvent> {

   private final Level level;

   private final String loggerName;

   private final List<ILoggingEvent> loggingEvents;

   /**
    * 
    * @param level
    *           niveau de filtre de l'appender
    * @param loggerName
    *           nom du package où est appliqué l'appender
    */
   public SaeLogAppender(Level level, String loggerName) {
      super();
      this.level = level;
      this.loggerName = loggerName;

      this.loggingEvents = new ArrayList<ILoggingEvent>();

      this.start();
   }

   /**
    * Les logs sont stockés dans une Collection en mémoire de
    * {@link ILoggingEvent}
    */
   @Override
   protected final void append(ILoggingEvent eventObject) {

      boolean levelFilter = eventObject.getLevel().isGreaterOrEqual(level);
      boolean loggerNameFilter = eventObject.getLoggerName().startsWith(
            this.loggerName);

      if (levelFilter && loggerNameFilter) {

         loggingEvents.add(eventObject);

      }
   }

   /**
    * 
    * @return liste des logs stockés en mémoire
    */
   public final List<ILoggingEvent> getLoggingEvents() {
      return Collections.unmodifiableList(loggingEvents);
   }

}