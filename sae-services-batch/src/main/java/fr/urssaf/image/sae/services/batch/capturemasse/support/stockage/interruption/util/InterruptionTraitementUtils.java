package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.LocalTimeUtils;



/**
 * Classe utilitaire pour l'interruption du traitement
 * 
 * 
 */
public final class InterruptionTraitementUtils {

   private InterruptionTraitementUtils() {

   }

   /**
    * Retourne la durée qu'il reste pour une date courante
    * 
    * @param currentDate
    *           date courante
    * @param config
    *           configuration de l'interruption
    * @return durée en millisecondes qu'il reste à l'heure locale de
    *         <code>currentDate</code> pour finir l'intervalle. Si l'heure
    *         locale n'est pas dans l'intervalle [<code>start</code>,
    *         <code>start</code>+delay] alors la valeur renvoyée est
    *         <code>-1</code>
    */
   public static long waitTime(final DateTime currentDate,
         final InterruptionTraitementConfig config) {

      final LocalTime startLocalTime = LocalTimeUtils.parse(config
            .getStart());

      final LocalDateTime currentLocalDate = new LocalDateTime(currentDate);

      final long diffTime = LocalTimeUtils.getDifference(currentLocalDate,
            startLocalTime, config.getDelay());

      return diffTime;
   }

}
