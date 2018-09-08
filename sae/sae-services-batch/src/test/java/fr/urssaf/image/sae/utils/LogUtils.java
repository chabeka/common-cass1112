/**
 * 
 */
package fr.urssaf.image.sae.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 
 * 
 */
public final class LogUtils {

   private LogUtils() {
   }
   
   /**
    * Recherche le message donné dans le log fourni
    * 
    * @param event
    *           l'objet correspondant au log
    * @param message
    *           le message à rechercher
    * @return la validation de la présence du message dans les logs
    */
   public static boolean logContainsMessage(ILoggingEvent event, String message) {

      boolean messageFound = false;

      if (event != null && message != null) {

         if (event.getFormattedMessage() != null) {
            messageFound = event.getFormattedMessage().contains(message);
         }

         if (!messageFound && event.getThrowableProxy() != null
               && event.getThrowableProxy().getMessage() != null) {
            messageFound = event.getThrowableProxy().getMessage().contains(
                  message);
         }

      }

      return messageFound;

   }

   /**
    * compte le nombre de logs avec le niveau passé en paramètre
    * 
    * @param loggingEvents
    *           liste des logs
    * @param level
    *           niveau de log
    * @return le nombre de logs
    */
   public static int countLogsWithLevel(List<ILoggingEvent> loggingEvents,
         Level level) {

      int nbreErreur = 0;
      if (CollectionUtils.isNotEmpty(loggingEvents)) {
         for (ILoggingEvent iLoggingEvent : loggingEvents) {
            if (Level.ERROR.equals(iLoggingEvent.getLevel())) {
               nbreErreur++;
            }
         }
      }

      return nbreErreur;
   }

   /**
    * Vérifie l'existence d'un message avec un niveau et un message dans la
    * liste des logs.
    * 
    * @param loggingEvents
    *           liste des logs
    * @param level
    *           niveau du log à chercher
    * @param message
    *           message du log à chercher
    * @return l'existence ou non du message
    */
   public static boolean logExists(List<ILoggingEvent> loggingEvents,
         Level level, String message) {

      boolean msgFound = false;
      int index = 0;
      ILoggingEvent event = null;

      while (!msgFound && index < loggingEvents.size()) {
         event = loggingEvents.get(index);
         if (level.equals(event.getLevel())
               && logContainsMessage(event, message)) {
            msgFound = true;
         }
         index++;
      }

      return msgFound;
   }

   /**
    * Retourne la liste des logs du niveau passé en paramètre de la liste
    * 
    * @param loggingEvents
    *           liste des logs
    * @param level
    *           niveau des logs à récupérer
    * @return un liste de logs
    */
   public static List<ILoggingEvent> getLogsByLevel(
         List<ILoggingEvent> loggingEvents, Level level) {

      List<ILoggingEvent> events = new ArrayList<ILoggingEvent>();

      if (CollectionUtils.isNotEmpty(loggingEvents)) {
         for (ILoggingEvent iLoggingEvent : loggingEvents) {
            if (level.equals(iLoggingEvent.getLevel())) {
               events.add(iLoggingEvent);
            }
         }
      }

      return events;
   }

}
