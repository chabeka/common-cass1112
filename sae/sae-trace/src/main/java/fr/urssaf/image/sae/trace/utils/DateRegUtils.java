/**
 * 
 */
package fr.urssaf.image.sae.trace.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;

/**
 * classe utilitaire sur le calcul d'intervalle de dates
 * 
 */
public final class DateRegUtils {

   private DateRegUtils() {
   }

   /**
    * Détermine la date de début en cours
    * 
    * @param currentDate
    *           date en cours
    * @param dateDebut
    *           date de référence, date minimale
    * @return la nouvelle date de début calculée
    */
   public static Date getStartDate(Date currentDate, Date dateDebut) {

      Date date;
      if (DateUtils.isSameDay(currentDate, dateDebut)) {
         date = dateDebut;
      } else {
         date = DateUtils.truncate(currentDate, Calendar.DATE);
      }

      return date;
   }

   /**
    * Détermine la date de fin en cours
    * 
    * @param currentDate
    *           date en cours
    * @param dateFin
    *           de fin de référence
    * @return la nouvelle date de fin calculée
    */
   public static Date getEndDate(Date currentDate, Date dateFin) {
      Date date;
      if (DateUtils.isSameDay(currentDate, dateFin)) {
         date = dateFin;
      } else {
         date = DateUtils.addDays(currentDate, 1);
         date = DateUtils.truncate(date, Calendar.DATE);
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

   /**
    * parcours tous les jours entre les deux dates fournies et les ajoute à la
    * liste. <br>
    * La liste retournée inclus les deux jours passés en paramètre tels quels
    * 
    * @param dateDebut
    *           première date
    * @param dateFin
    *           dernière date
    * @return la liste des dates comprises dans cet intervalle
    */
   public static List<Date> getListFromDates(Date dateDebut, Date dateFin) {
      List<Date> list = new ArrayList<Date>();

      Date currentDate = dateDebut;

      do {
         list.add(currentDate);
         currentDate = DateUtils.addDays(currentDate, 1);
      } while (!DateUtils.isSameDay(currentDate, dateFin)
            && currentDate.before(dateFin));

      list.add(dateFin);

      return list;
   }

   /**
    * Renvoie la journée au format AAAAMMJJ correspondant à un timestamp<br>
    * Sert aux index en bdd, à la purge
    * 
    * @param timestamp
    *           le timestamp
    * @return la journée au format AAAAMMJJ
    */
   public static String getJournee(Date timestamp) {
      return new SimpleDateFormat("yyyyMMdd", Locale.FRANCE).format(timestamp);
   }

}
