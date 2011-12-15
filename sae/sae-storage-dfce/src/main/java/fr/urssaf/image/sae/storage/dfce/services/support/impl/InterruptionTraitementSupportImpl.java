package fr.urssaf.image.sae.storage.dfce.services.support.impl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.dfce.utils.LocalTimeUtils;

/**
 * Implémentation du service {@link InterruptionTraitementSupport}
 * 
 * 
 */
@Component
public class InterruptionTraitementSupportImpl implements
      InterruptionTraitementSupport {

   private static final Logger LOG = LoggerFactory
         .getLogger(InterruptionTraitementSupportImpl.class);

   private final DFCEServicesManager dfceManager;

   /**
    * 
    * @param dfceManager
    *           ensemble des services de manipulation DFCE
    */
   @Autowired
   public InterruptionTraitementSupportImpl(DFCEServicesManager dfceManager) {

      Assert.notNull(dfceManager, "'dfceManager' is required");

      this.dfceManager = dfceManager;
   }

   private static final long DELAY = 120000;

   private long delay = DELAY;

   /**
    * 
    * @param delay
    *           temps d'attente en secondes entre chaque tentatives après la
    *           première
    */
   public final void setDelay(long delay) {
      this.delay = delay;
   }

   /**
    * {@inheritDoc} <br>
    * Après la première tentative le delai d'attente entre chaque tentative est
    * fixé par {@value #setDelay(int)} en secondes.<br>
    * Par défaut cette valeur est fixé à {@value #DELAY}
    * 
    */
   @Override
   public final void interruption(String start, int delay, int tentatives) {

      DateTime currentDate = new DateTime();

      interruption(currentDate, start, delay, tentatives);

   }

   private static final String LOG_PREFIX = "Interruption programmée d'un traitement";

   protected final void interruption(DateTime currentDate, String startTime,
         int delay, int tentatives) {

      LocalTime startLocalTime = LocalTimeUtils.parse(startTime);

      LocalDateTime currentLocalDate = new LocalDateTime(currentDate);

      long diffTime = LocalTimeUtils.getDifference(currentLocalDate,
            startLocalTime, delay);

      if (diffTime > 0) {

         LOG.debug("{} - début programmé à {}", LOG_PREFIX, startTime);

         DateTime endDate = currentDate.plus(diffTime);

         DateTimeFormatter formatter = DateTimeFormat
               .forPattern(Constants.DATE_TIME_PATTERN);

         LOG.debug("{} - Reprise prévue à {}", LOG_PREFIX, formatter
               .print(endDate));

         dfceManager.closeConnection();

         ConnectionResult connectionResult;
         try {
            connectionResult = pause(diffTime, null, tentatives, tentatives);
         } catch (InterruptedException e) {
            // Interruption lors de la mise en pause du traitement
            throw new InterruptionTraitementException(startTime, delay,
                  tentatives, e);
         }

         if (connectionResult.lastException != null) {

            throw new InterruptionTraitementException(startTime, delay,
                  tentatives, connectionResult.lastException);
         }

         LOG.debug(
               "{} - Réussite de la tentative n°{}/{} de reconnexion à DFCE ",
               new Object[] { LOG_PREFIX, connectionResult.step, tentatives });
      }
   }

   private ConnectionResult pause(long delay, Exception lastException,
         int tentatives, int total) throws InterruptedException {

      int step = total - tentatives + 1;

      ConnectionResult connectionResult = new ConnectionResult();
      connectionResult.lastException = lastException;
      connectionResult.step = step;

      if (tentatives > 0) {

         Duration duration = Duration.millis(delay);

         LOG.debug("{} - Interruption de {} secondes", LOG_PREFIX, duration
               .getStandardSeconds());

         Thread.sleep(delay);

         try {

            LOG.debug("{} - Tentative n°{}/{} de reconnexion à DFCE",
                  new Object[] { LOG_PREFIX, step, total });

            dfceManager.getConnection();

            // réussite de la connexion à DFCE

            connectionResult.step = step;
            connectionResult.setLastException(null);

         } catch (Exception e) {

            // échec de la connection

            int newTentatives = tentatives - 1;

            LOG.debug(
                  "{} - Echec de la tentative n°{}/{} de reconnexion à DFCE ",
                  new Object[] { LOG_PREFIX, step, total });

            connectionResult = pause(this.delay, e, newTentatives, total);
         }

      }

      return connectionResult;

   }

   private static class ConnectionResult {

      private Exception lastException;

      private int step;

      private void setLastException(Exception lastException) {
         this.lastException = lastException;
      }
   }

}
