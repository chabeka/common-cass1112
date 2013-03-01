package fr.urssaf.image.sae.trace.utils;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

/**
 * Méthodes utilitaires pour les TimeUUID<br>
 * Les timestamp (long) utilisés pour calculer les TimeUUID sont en
 * microsecondes divisés par 10<br>
 * Il faut rester cohérent avec le ClockResolution utilisé pour générer les
 * TimeUUID lors de l'écriture
 */
public final class TimeUUIDTraceUtils {

   private static final int QUATRE_VINGT_DIX_NEUF = 99;

   /**
    * La valeur 100
    */
   public static final int CENT = 100;

   private TimeUUIDTraceUtils() {
      // constructeur privé
   }

   /**
    * Créé un TimeUUID à partir d'un timestamp en micro-secondes divisé par 10
    * 
    * @param timestampMicro
    *           le timestamp en micro-secondes
    * @return l'UUID
    */
   public static UUID buildUUIDFromTimestampMicroSecDivDix(
         long timestampMicroDivDix) {
      return TimeUUIDUtils.getTimeUUID(timestampMicroDivDix);
   }

   /**
    * Créé un TimeUUID à partir d'un objet Date
    * 
    * @param date
    *           l'objet Date
    * @return l'UUID
    */
   public static UUID buildUUIDFromDate(Date date) {
      long microsecDivDix = (date.getTime() * CENT);
      return TimeUUIDUtils.getTimeUUID(microsecDivDix);
   }

   /**
    * Créé un TimeUUID à partir d'un objet Date, en utilisant un timestamp dont
    * l'unité de temps est poussé à *999
    * 
    * @param date
    *           l'objet Date
    * @return l'UUID
    */
   public static UUID buildUUIDFromDateBorneSup(Date date) {
      long microsecDivDix = (date.getTime() * CENT);
      microsecDivDix = microsecDivDix + QUATRE_VINGT_DIX_NEUF;
      return TimeUUIDUtils.getTimeUUID(microsecDivDix);
   }

}
