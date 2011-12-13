package fr.urssaf.image.sae.storage.dfce.utils;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.springframework.util.Assert;

/**
 * Classe utilitaire de manipulation des heures
 * 
 * 
 */
public final class LocalTimeUtils {

   private LocalTimeUtils() {

   }

   private static final String TIME_PATTERN = "HH:mm:ss";

   private static final DateTimeFormatter TIME_FORMATTER;

   static {

      DateTimePrinter printer = DateTimeFormat.forPattern(TIME_PATTERN)
            .getPrinter();
      DateTimeParser parser = DateTimeFormat.forPattern(TIME_PATTERN)
            .getParser();

      TIME_FORMATTER = new DateTimeFormatter(printer, parser);

   }

   /**
    * 
    * Vérification que l'heure est au format {@value #TIME_PATTERN}<br>
    * 
    * @param time
    *           heure pour qui on veut vérifier le format
    * @return <code>true</code> si <code>time</code> est au format
    *         {@value #TIME_PATTERN} <code>faux</code> sinon
    */
   public static boolean isValidate(String time) {

      Assert.notNull(time);

      boolean isValidate;

      try {

         TIME_FORMATTER.parseLocalTime(time);

         isValidate = true;

      } catch (IllegalArgumentException e) {

         isValidate = false;
      }

      return isValidate;

   }

   /**
    * Transforme une chaine de caractère au format {@value #TIME_PATTERN}<br>
    * 
    * @param time
    *           heure à parser
    * @return l'heure parsée
    */
   public static LocalTime parse(String time) {

      Assert.notNull(time);

      return TIME_FORMATTER.parseLocalTime(time);

   }

   // TODO renvoyer des millisecondes plutôt que des secondes
   /**
    * Retourne la durée qu'il reste à une heure dans un intervalle de temps.<br>
    * <br>
    * Ex :<br>
    * <code>getDifference([01-01-1999 03:00:00],[03:00:00], 120) = 120</code><br>
    * <code>getDifference([01-01-1999 03:00:01],[03:00:00], 120) = 119</code><br>
    * <code>getDifference([01-01-1999 03:01:59],[03:00:00], 120) = 1</code><br>
    * <code>getDifference([01-01-1999 03:02:00],[03:00:00], 120) = 0</code><br>
    * <code>getDifference([01-01-1999 03:02:01],[03:00:00], 120) = -1</code><br>
    * <code>getDifference([01-01-1999 02:59:59],[03:00:00], 120) = -1</code><br>
    * 
    * @param currentDate
    *           date à vérifier
    * @param startTime
    *           heure du début de l'intervalle
    * @param delay
    *           limite en secondes de l'intervalle
    * @return durée en secondes qu'il reste à l'heure locale de
    *         <code>currentDate</code> pour finir l'intervalle. Si l'heure
    *         locale n'est pas dans l'intervalle alors la valeur renvoyée est
    *         <code>-1</code>
    */
   public static long getDifference(LocalDateTime currentDate,
         LocalTime startTime, int delay) {

      Assert.notNull(currentDate);
      Assert.notNull(startTime);

      LocalTime endTime = new LocalTime(startTime);
      endTime = endTime.plusSeconds(delay);

      LocalTime currentHour = currentDate.toLocalTime();

      long diff;

      if (currentHour.getMillisOfDay() >= startTime.getMillisOfDay()) {

         if (currentHour.getMillisOfDay() <= endTime.getMillisOfDay()) {

            Duration duration = new Duration(endTime.getMillisOfDay()
                  - currentHour.getMillisOfDay());

            diff = duration.getStandardSeconds();

         } else {

            diff = -1;
         }

      } else {
         diff = -1;
      }

      return diff;
   }

}
