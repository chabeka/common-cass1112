package fr.urssaf.image.sae.storage.dfce.services.support.impl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
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
import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.dfce.services.support.util.InterruptionTraitementUtils;
import fr.urssaf.image.sae.storage.model.jmx.BulkProgress;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

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

   /**
    * Le délai d'attente par défaut entre deux tentatives de reconnexion
    * après une interruption
    */
   protected static final long DEFAULT_DELAY = 120000;

   private long defaultDelay = DEFAULT_DELAY;

   /**
    * 
    * @param defaultDelay
    *           temps d'attente en secondes entre chaque tentatives après la
    *           première
    */
   public final void setDelay(long defaultDelay) {
      this.defaultDelay = defaultDelay;
   }

   private static final String LOG_PREFIX = "Interruption programmée d'un traitement";

   /**
    * {@inheritDoc} <br>
    * Après la première tentative le delai d'attente entre chaque tentative est
    * fixé par {@link #setDelay(long)} en secondes.<br>
    * Par défaut cette valeur est fixé à {@value #DEFAULT_DELAY}
    * 
    * 
    */
   @Override
   public final void interruption(DateTime currentDate,
         InterruptionTraitementConfig interruptionConfig, JmxIndicator indicator)
         throws InterruptionTraitementException {

      long diffTime = InterruptionTraitementUtils.waitTime(currentDate,
            interruptionConfig);

      if (diffTime > 0) {

         LOG.debug("{} - début programmé à {} - indicator {}", new Object[] {
               LOG_PREFIX, interruptionConfig.getStart(),
               Integer.valueOf(indicator.getJmxStorageIndex()) });

         DateTime endDate = currentDate.plus(diffTime);

         DateTimeFormatter formatter = DateTimeFormat
               .forPattern(Constants.DATE_TIME_PATTERN);

         // renseignement de l'indicateur JMX pour un traitement
         indicator.setJmxTreatmentState(BulkProgress.INTERRUPTED_TREATMENT);
         Duration duration = Duration.millis(diffTime);
         indicator.setInterruptionDelay(duration.getStandardSeconds());
         indicator.setInterruptionStart(currentDate);
         indicator.setInterruptionEnd(endDate);

         LOG.debug("{} - Reprise prévue à {}", LOG_PREFIX, formatter
               .print(endDate));

         // On ne ferme pas la connexion avec DFCE pour éviter une levée
         // d'exception sur les Threads en cours d'exécution qui effectuent une
         // insertion dans DFCE
         // dfceManager.closeConnection();

         ConnectionResult connectionResult;
         try {
            connectionResult = pause(diffTime, null, interruptionConfig
                  .getTentatives(), interruptionConfig.getTentatives(),
                  dfceManager);
         } catch (InterruptedException e) {
            // Interruption lors de la mise en pause du traitement
            throw new InterruptionTraitementException(interruptionConfig, e);
         }

         if (connectionResult.lastException != null) {

            throw new InterruptionTraitementException(interruptionConfig,
                  connectionResult.lastException);
         }

         LOG.debug(
               "{} - Réussite de la tentative n°{}/{} de reconnexion à DFCE ",
               new Object[] { LOG_PREFIX, connectionResult.step,
                     interruptionConfig.getTentatives() });

         // renseignement de l'indicateur JMX pour un traitement
         indicator.setJmxTreatmentState(BulkProgress.RESTART_TREATMENT);

      }
   }

   private ConnectionResult pause(long delay, Exception lastException,
         int tentatives, int total, DFCEServicesManager dfceManager)
         throws InterruptedException {

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

            connectionResult = pause(this.defaultDelay, e, newTentatives,
                  total, dfceManager);
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

   /**
    * {@inheritDoc}
    * 
    * 
    */
   @Override
   public final boolean hasInterrupted(DateTime currentDate,
         InterruptionTraitementConfig interruptionConfig) {

      return InterruptionTraitementUtils.waitTime(currentDate,
            interruptionConfig) > 0;
   }

}
