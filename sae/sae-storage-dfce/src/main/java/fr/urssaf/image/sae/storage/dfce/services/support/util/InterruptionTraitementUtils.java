package fr.urssaf.image.sae.storage.dfce.services.support.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.dfce.utils.LocalTimeUtils;

/**
 * Classe utilitaire pour l'interruption du traitement
 * 
 * 
 */
@Deprecated
public final class InterruptionTraitementUtils {

   private InterruptionTraitementUtils() {

   }

   /**
    * Retourne la durée qu'il reste pour une date courante
    * 
    * @param currentDate
    *           date courante
    * @param interruptionConfig
    *           configuration de l'interruption
    * @return durée en millisecondes qu'il reste à l'heure locale de
    *         <code>currentDate</code> pour finir l'intervalle. Si l'heure
    *         locale n'est pas dans l'intervalle [<code>start</code>,
    *         <code>start</code>+delay] alors la valeur renvoyée est
    *         <code>-1</code>
    */
   public static long waitTime(DateTime currentDate,
         InterruptionTraitementConfig interruptionConfig) {

      LocalTime startLocalTime = LocalTimeUtils.parse(interruptionConfig
            .getStart());

      LocalDateTime currentLocalDate = new LocalDateTime(currentDate);

      long diffTime = LocalTimeUtils.getDifference(currentLocalDate,
            startLocalTime, interruptionConfig.getDelay());

      return diffTime;
   }

}
