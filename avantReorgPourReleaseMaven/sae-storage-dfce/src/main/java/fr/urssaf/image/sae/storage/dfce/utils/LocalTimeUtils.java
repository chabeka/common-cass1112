package fr.urssaf.image.sae.storage.dfce.utils;

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

   /**
    * Vérification si l'heure d'une date est postérieure à une certaine heure
    * dans la limite d'un delai.<br>
    * 
    * @param currentDate
    *           date à vérifier
    * @param startTime
    *           heure du début
    * @param delay
    *           limite en secondes
    * @return <code>true</code> si l'heure locale de <code>currentDate</code>
    *         est situé après <code>startTime</code> dans la limite de
    *         <code>delay</delay>
    */
   public static boolean isSameTime(LocalDateTime currentDate,
         LocalTime startTime, int delay) {

      Assert.notNull(currentDate);
      Assert.notNull(startTime);

      LocalTime endTime = new LocalTime(startTime);
      endTime = endTime.plusSeconds(delay);

      LocalTime currentHour = currentDate.toLocalTime();

      boolean isSameTime;

      if (currentHour.getMillisOfDay() >= startTime.getMillisOfDay()) {

         if (currentHour.getMillisOfDay() < endTime.getMillisOfDay()) {

            isSameTime = true;

         } else {

            isSameTime = false;
         }

      } else {
         isSameTime = false;
      }

      return isSameTime;
   }
}
