/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.impl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.util.InterruptionTraitementUtils;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * 
 * 
 */
@Component
public class InterruptionTraitementMasseSupportImpl implements
      InterruptionTraitementMasseSupport {

   private static final Logger LOG = LoggerFactory
         .getLogger(InterruptionTraitementMasseSupportImpl.class);

   private final DFCEServicesManager dfceManager;

   private static final String CATCH = "AvoidCatchingThrowable";

   private final StorageServiceProvider serviceProvider;

   /**
    * 
    * @param dfceManager
    *           ensemble des services de manipulation DFCE
    * @param serviceProvider
    *           le provider de service
    */
   @Autowired
   public InterruptionTraitementMasseSupportImpl(
         final DFCEServicesManager dfceManager,
         final @Qualifier("storageServiceProvider") StorageServiceProvider serviceProvider) {

      Assert.notNull(dfceManager, "'dfceManager' is required");

      Assert.notNull(serviceProvider, "'serviceProvider' is required");

      this.dfceManager = dfceManager;
      this.serviceProvider = serviceProvider;

   }

   /**
    * Délai par défaut d'attente entre chaque tentative de reconnexion après une
    * interruption
    */
   protected static final long DEFAULT_DELAY = 120000;

   private long defaultDelay = DEFAULT_DELAY;

   /**
    * 
    * @param defaultDelay
    *           temps d'attente en secondes entre chaque tentatives après la
    *           première
    */
   public final void setDelay(final long defaultDelay) {
      this.defaultDelay = defaultDelay;
   }

   private static final String LOG_PREFIX = "Interruption programmée d'un traitement";

   // format de date avec heure
   private static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

   /**
    * {@inheritDoc} <br>
    * Après la première tentative le delai d'attente entre chaque tentative est
    * fixé par {@link #setDelay(long)} en secondes.<br>
    * Par défaut cette valeur est fixé à {@value #DEFAULT_DELAY}
    * 
    * 
    */
   @Override
   public final void interruption(final DateTime currentDate,
         InterruptionTraitementConfig config)
         throws InterruptionTraitementException {

      final long diffTime = InterruptionTraitementUtils.waitTime(currentDate,
            config);

      if (diffTime > 0) {

         final DateTime endDate = currentDate.plus(diffTime);

         final DateTimeFormatter formatter = DateTimeFormat
               .forPattern(DATE_TIME_PATTERN);

         LOG.debug("{} - Reprise prévue à {}", LOG_PREFIX, formatter
               .print(endDate));

         // On ne ferme pas la connexion avec DFCE pour éviter une levée
         // d'exception sur les Threads en cours d'exécution qui effectuent une
         // insertion dans DFCE
         // dfceManager.closeConnection();

         ConnectionResult connectionResult;
         try {
            connectionResult = pause(diffTime, null, config.getTentatives(),
                  config.getTentatives(), dfceManager);
         } catch (InterruptedException e) {
            // Interruption lors de la mise en pause du traitement
            throw new InterruptionTraitementException(config, e);
         }

         if (connectionResult.exception != null) {

            throw new InterruptionTraitementException(config,
                  connectionResult.exception);
         }

         LOG.debug(
               "{} - Réussite de la tentative n°{}/{} de reconnexion à DFCE ",
               new Object[] { LOG_PREFIX, connectionResult.step,
                     config.getTentatives() });

      }
   }

   @SuppressWarnings(CATCH)
   private ConnectionResult pause(final long delay,
         final Exception lastException, final int tentatives, final int total,
         final DFCEServicesManager dfceManager) throws InterruptedException {

      final int step = total - tentatives + 1;

      ConnectionResult connectionResult = new ConnectionResult();
      connectionResult.exception = lastException;
      connectionResult.step = step;

      if (tentatives > 0) {

         final Duration duration = Duration.millis(delay);

         LOG.debug("{} - Interruption de {} secondes", LOG_PREFIX, duration
               .getStandardSeconds());

         Thread.sleep(delay);

         try {

            LOG.debug("{} - Tentative n°{}/{} de reconnexion à DFCE",
                  new Object[] { LOG_PREFIX, step, total });

            dfceManager.getConnection(Boolean.TRUE);

            // Reconnexion pour la couche storage
            serviceProvider.openConnexion();

            // réussite de la connexion à DFCE

            connectionResult.step = step;
            connectionResult.setLastException(null);

         } catch (Exception e) {

            // échec de la connection

            final int newTentatives = tentatives - 1;

            LOG.debug(
                  "{} - Echec de la tentative n°{}/{} de reconnexion à DFCE ",
                  new Object[] { LOG_PREFIX, step, total });

            connectionResult = pause(this.defaultDelay, e, newTentatives,
                  total, dfceManager);

            /* erreur DFCE catchée */
         } catch (Throwable throwable) {

            final int newTentatives = tentatives - 1;

            LOG.debug(
                  "{} - Echec de la tentative n°{}/{} de reconnexion à DFCE ",
                  new Object[] { LOG_PREFIX, step, total });

            connectionResult = pause(this.defaultDelay,
                  new ConnectionServiceEx("erreur de reconnexion", throwable),
                  newTentatives, total, dfceManager);
         }

      }

      return connectionResult;

   }

   private static final class ConnectionResult {
      private ConnectionResult() {
      }

      private Exception exception;

      private int step;

      private void setLastException(final Exception lastException) {
         this.exception = lastException;
      }
   }

   /**
    * {@inheritDoc}
    * 
    * 
    */
   @Override
   public final boolean hasInterrupted(final DateTime currentDate,
         final InterruptionTraitementConfig config) {

      return InterruptionTraitementUtils.waitTime(currentDate, config) > 0;
   }

}
