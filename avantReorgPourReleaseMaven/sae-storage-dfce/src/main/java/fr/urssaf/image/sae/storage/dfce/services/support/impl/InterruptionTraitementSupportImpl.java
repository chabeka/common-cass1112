package fr.urssaf.image.sae.storage.dfce.services.support.impl;

import java.text.MessageFormat;
import java.util.Date;

import me.prettyprint.cassandra.utils.Assert;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.dfce.utils.LocalTimeUtils;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

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

   private final StorageServiceProvider storageProvider;

   private static final String EXCEPTION_MESSAGE = "Après une déconnexion DFCE programmée à {0} il est impossible de reprendre le traitement après {1} secondes et {2} tentatives.";

   /**
    * 
    * @param storageProvider
    *           ensemble des services de manipulation de la surcouche de DFCE
    */
   @Autowired
   public InterruptionTraitementSupportImpl(
         StorageServiceProvider storageProvider) {

      Assert.notNull(storageProvider, "'storageProvider' is required");

      this.storageProvider = storageProvider;
   }

   private static final int DELAY = 120;

   private int delay = DELAY;

   /**
    * 
    * @param delay
    *           temps d'attente en secondes entre chaque tentatives après la
    *           première
    */
   public final void setDelay(int delay) {
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

      Date currentDate = new Date();

      interruption(currentDate, start, delay, tentatives);

   }

   private static final String LOG_PREFIX = "Interruption programmée d'un traitement";

   protected final void interruption(Date currentDate, String startTime,
         int delay, int tentatives) {

      LocalTime startLocalTime = LocalTimeUtils.parse(startTime);

      LocalDateTime currentLocalDate = LocalDateTime
            .fromDateFields(currentDate);

      if (LocalTimeUtils.isSameTime(currentLocalDate, startLocalTime, delay)) {

         LOG.debug("{} - début programmé à {}", LOG_PREFIX, startTime);

         storageProvider.closeConnexion();

         ConnectionResult connectionResult = pause(delay, null, tentatives,
               tentatives);

         if (connectionResult.lastException != null) {

            String exceptionMessage = MessageFormat.format(EXCEPTION_MESSAGE,
                  startTime, delay, tentatives);

            throw new InterruptionTraitementException(exceptionMessage,
                  connectionResult.lastException);
         }

         LOG.debug(
               "{} - Réussite de la tentative n°{}/{} de reconnexion à DFCE ",
               new Object[] { LOG_PREFIX, connectionResult.step, tentatives });
      }
   }

   private ConnectionResult pause(long delay, Exception lastException,
         int tentatives, int total) {

      int step = total - tentatives + 1;

      ConnectionResult connectionResult = new ConnectionResult();
      connectionResult.lastException = lastException;
      connectionResult.step = step;

      if (tentatives > 0) {

         LOG.debug("{} - Interruption de {} secondes", LOG_PREFIX, delay);

         pause(delay);

         try {

            LOG.debug("{} - Tentative n°{}/{} de reconnexion à DFCE",
                  new Object[] { LOG_PREFIX, step, total });

            storageProvider.openConnexion();

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

   private void pause(long delay) {

      try {
         Thread.sleep(delay * 1000);
      } catch (InterruptedException e) {
         throw new InterruptionTraitementException(e);
      }
   }

}
