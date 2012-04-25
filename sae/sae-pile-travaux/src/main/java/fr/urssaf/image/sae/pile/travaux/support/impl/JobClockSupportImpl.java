package fr.urssaf.image.sae.pile.travaux.support.impl;

import me.prettyprint.cassandra.utils.Assert;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.pile.travaux.exception.ClockSynchronizationException;
import fr.urssaf.image.sae.pile.travaux.support.JobClockSupport;

/**
 * Implémentation du support {@link JobClockSupport}
 * 
 * 
 */
@Component
public class JobClockSupportImpl implements JobClockSupport {

   private static final Logger LOG = LoggerFactory
         .getLogger(JobClockSupport.class);

   private static final long ONE_THOUSAND = 1000L;

   private final Keyspace keyspace;

   @Autowired
   public JobClockSupportImpl(Keyspace keyspace) {

      Assert.notNull(keyspace, "'keyspace' is required");

      this.keyspace = keyspace;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public long currentCLock() {

      return keyspace.createClock();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public long currentCLock(HColumn<?, ?> column) {
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

         if ((columnClock - actualClock) > MAX_TIME_SYNCHRO_ERROR) {
            throw new ClockSynchronizationException(actualClock, columnClock);
         }

         if ((columnClock - actualClock) > MAX_TIME_SYNCHRO_WARN) {
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
