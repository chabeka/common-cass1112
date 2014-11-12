package fr.urssaf.image.commons.cassandra.support.clock.impl;

import me.prettyprint.cassandra.utils.Assert;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.cassandra.exception.ClockSynchronizationException;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;

/**
 * Implémentation du support {@link JobClockSupport}
 * 
 * 
 */
public class JobClockSupportImpl implements JobClockSupport {

   private static final Logger LOG = LoggerFactory
         .getLogger(JobClockSupport.class);

   private static final long ONE_THOUSAND = 1000L;

   private final Keyspace keyspace;

   private final JobClockConfiguration clockConfiguration;

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   public JobClockSupportImpl(Keyspace keyspace,
         JobClockConfiguration clockConfiguration) {

      Assert.notNull(keyspace, "'keyspace' is required");
      Assert.notNull(clockConfiguration, "'clockConfiguration' is required");

      Assert.isTrue(clockConfiguration.getMaxTimeSynchroError() > 0,
            "la propriété 'maxTimeSynchroError' doit supérieure à 0");

      Assert.isTrue(clockConfiguration.getMaxTimeSynchroWarn() > 0,
            "la propriété 'maxTimeSynchroWarn' doit supérieure à 0");

      this.keyspace = keyspace;
      this.clockConfiguration = clockConfiguration;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final long currentCLock() {

      return keyspace.createClock();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final long currentCLock(HColumn<?, ?> column) {
      Assert.notNull(column, "'column' is required");

      long actualClock = keyspace.createClock();

      LOG.debug("Le timestamp de l'horloge du keyspace {} est de {}",
            new Object[] { keyspace.getKeyspaceName(), actualClock });

      long columnClock = column.getClock();

      long newClock;

      LOG.debug("Le timestamp de de la colonne {} est de {}", new Object[] {
            column.getName(), columnClock });

      // vérification que l'horloge de la colonne ne soit pas postérieure à
      // l'horloge actuelle du serveur
      if (columnClock > actualClock) {

         // si la différence est trop importante alors on lève une exception

         if ((columnClock - actualClock) > clockConfiguration
               .getMaxTimeSynchroError()) {
            throw new ClockSynchronizationException(columnClock, actualClock);
         }

         if ((columnClock - actualClock) > clockConfiguration
               .getMaxTimeSynchroWarn()) {
            LOG
                  .warn("Attention, les horloges des serveurs semblent désynchronisées. Le décalage est au moins de "
                        + (columnClock - actualClock) / ONE_THOUSAND + " ms");
         }

         // Sinon, on positionne le nouveau timestamp juste au dessus de
         // l'ancien
         newClock = columnClock + 1;

         LOG.debug("La position du nouveau timestamp est de {}",
               new Object[] { newClock });
      } else {

         newClock = keyspace.createClock();
      }

      return newClock;
   }

}
