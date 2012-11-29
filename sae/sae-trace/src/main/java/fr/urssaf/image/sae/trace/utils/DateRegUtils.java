/**
 * 
 */
package fr.urssaf.image.sae.trace.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * classe utilitaire sur le calcul d'intervalle de dates
 * 
 */
public final class DateRegUtils {

   private DateRegUtils() {
   }

   /**
    * Cherche la nouvelle date de début en fonction de la précédente
    * 
    * @param startDate
    *           date précédemment calculée
    * @param dateDebut
    *           date de référence, date minimale
    * @return la nouvelle date de début calculée
    */
   public static Date getStartDate(Date startDate, Date dateDebut) {

      Date date, newDay;
      newDay = DateUtils.addDays(startDate, -1);
      if (DateUtils.isSameDay(newDay, dateDebut)) {
         date = dateDebut;
      } else {
         date = newDay;
      }

      return date;
   }

   /**
    * @param dateDebut
    *           date de début de référence
    * @param dateFin
    *           date de fin de référence
    * @return la date de début ad'hoc
    */
   public static Date getFirstDate(Date dateDebut, Date dateFin) {

      Date startDate;
      if (DateUtils.isSameDay(dateDebut, dateFin)) {
         startDate = dateDebut;
      } else {
         startDate = DateUtils.truncate(dateFin, Calendar.DATE);
      }

      return startDate;
   }

}
