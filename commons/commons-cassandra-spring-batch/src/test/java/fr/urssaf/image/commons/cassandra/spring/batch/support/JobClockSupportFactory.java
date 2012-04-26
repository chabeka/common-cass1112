package fr.urssaf.image.commons.cassandra.spring.batch.support;

import me.prettyprint.hector.api.Keyspace;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.cassandra.support.clock.impl.JobClockSupportImpl;

public final class JobClockSupportFactory {

   /**
    * Temps maximum de décalage d'horloge qu'il nous parait acceptable, en
    * micro-secondes
    */
   private static final int MAX_TIME_SYNCHRO_ERROR = 10 * 1000 * 1000;
   /**
    * Temps maximum de décalage d'horloge, en micro-secondes. Au delà, on logue
    * une warning.
    */
   private static final int MAX_TIME_SYNCHRO_WARN = 2 * 1000 * 1000;

   private JobClockSupportFactory() {

   }

   public static JobClockSupport createJobClockSupport(Keyspace keyspace) {

      JobClockConfiguration clockConfiguration = new JobClockConfiguration();
      clockConfiguration.setMaxTimeSynchroError(MAX_TIME_SYNCHRO_ERROR);
      clockConfiguration.setMaxTimeSynchroWarn(MAX_TIME_SYNCHRO_WARN);
      JobClockSupport support = new JobClockSupportImpl(keyspace,
            clockConfiguration);

      return support;

   }

}
