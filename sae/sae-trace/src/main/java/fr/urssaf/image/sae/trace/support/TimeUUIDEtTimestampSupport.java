package fr.urssaf.image.sae.trace.support;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.ClockResolution;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.clock.MySyncClockResolution;

/**
 * Utilitaires pour créer des TimeUUID.<br>
 * Unité de temps pour les timestamp : micro-secondes /10
 */
@Component
public final class TimeUUIDEtTimestampSupport {

   private final ClockResolution clockResolution = new MySyncClockResolution();

   private static final int QUATRE_VINGT_DIX_NEUF = 99;

   private static final int CENT = 100;

   /**
    * Renvoie le timestamp courant dans l'unité de temps géré par la classe
    * 
    * @return le timestamp courant dans l'unité de temps géré par la classe
    */
   public long getCurrentTimestamp() {
      return clockResolution.createClock();
   }

   /**
    * Renvoie le timestamp correspondant à une date, dans l'unité de temps géré
    * par la classe
    * 
    * @param date
    *           l'objet date
    * @return le timestamp dans l'unité de temps géré par la classe
    */
   public long getTimestampFromDate(Date date) {
      return date.getTime() * CENT;
   }

   /**
    * Créé un TimeUUID à partir d'un timestamp obtenu via une méthode de cette
    * classe
    * 
    * @param timestamp
    *           le timestamp obtenu via une méthode de cette classe
    * @return le TimeUUID correspondant
    */
   public UUID buildUUIDFromTimestamp(long timestamp) {
      return TimeUUIDUtils.getTimeUUID(timestamp);
   }

   /**
    * Créé un TimeUUID à partir d'un objet Date
    * 
    * @param date
    *           l'objet Date
    * @return le TimeUUID
    */
   public UUID buildUUIDFromDate(Date date) {
      long microsecDivDix = (date.getTime() * CENT);
      return TimeUUIDUtils.getTimeUUID(microsecDivDix);
   }

   private long getTimestampEnMicroSecDivDix(Date date) {
      return date.getTime() * CENT;
   }

   /**
    * Créé un TimeUUID à partir d'un objet Date, en se calant sur la borne
    * supérieure pour les unités de temps qui dépasse la millisecondes (*999)
    * 
    * @param date
    *           l'objet Date
    * @return le TimeUUID
    */
   public UUID buildUUIDFromDateBorneSup(Date date) {
      long microsecDivDix = getTimestampEnMicroSecDivDix(date);
      microsecDivDix = microsecDivDix + QUATRE_VINGT_DIX_NEUF;
      return TimeUUIDUtils.getTimeUUID(microsecDivDix);
   }

   /**
    * Créé un TimeUUID à partir du timestamp courant
    * 
    * @return le TimeUUID
    */
   public UUID buildUUIDFromCurrentTimestamp() {
      long timestampMicroDivDix = getCurrentTimestamp();
      return TimeUUIDUtils.getTimeUUID(timestampMicroDivDix);
   }

   /**
    * Renvoie l'objet Date correspondant à un timestamp obtenu via une méthode
    * de cette classe
    * 
    * @param timestamp
    *           un timestamp obtenu via une méthode de cette classe
    * @return l'objet Date correspondant
    */
   public Date getDateFromTimestamp(long timestamp) {
      return new Date(timestamp / CENT);
   }

}
