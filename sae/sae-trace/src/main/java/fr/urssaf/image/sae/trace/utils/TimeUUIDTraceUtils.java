package fr.urssaf.image.sae.trace.utils;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

/**
 * Méthodes utilitaires pour les TimeUUID
 */
public final class TimeUUIDTraceUtils {

   private static final int MILLE = 1000;

   private TimeUUIDTraceUtils() {
      // constructeur privé
   }

   /**
    * Créé un TimeUUID à partir d'un timestamp en micro-secondes
    * 
    * @param timestampMicro
    *           le timestamp en micro-secondes
    * @return l'UUID
    */
   public static UUID buildUUIDFromTimestampMicro(long timestampMicro) {
      return TimeUUIDUtils.getTimeUUID(timestampMicro);
   }

   /**
    * Créé un TimeUUID à partir d'un objet Date
    * 
    * @param date
    *           l'objet Date
    * @return l'UUID
    */
   public static UUID buildUUIDFromDate(Date date) {
      return TimeUUIDUtils.getTimeUUID(date.getTime() * MILLE);
   }

}
