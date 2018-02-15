package fr.urssaf.image.sae.webservices.util;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * Classe de manipulation de {@link DateTime}
 * 
 * 
 */
public final class DateTimeUtils {

   private DateTimeUtils() {

   }

   /**
    * 
    * @param startDate
    *           date de début
    * @param endDate
    *           date de fin
    * @return nombre d'heures qui sépare les deux dates
    */
   public static int diffHours(DateTime startDate, DateTime endDate) {

      Period period = new Period(startDate, endDate, PeriodType.hours());

      return period.getHours();
   }

}
