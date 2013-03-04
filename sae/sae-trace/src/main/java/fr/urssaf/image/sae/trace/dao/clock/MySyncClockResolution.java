package fr.urssaf.image.sae.trace.dao.clock;

import me.prettyprint.cassandra.service.clock.AbstractClockResolution;
import me.prettyprint.hector.api.ClockResolution;

/**
 * Synchronized Microseconds divisé par 10 resolution used to create clocks.<br>
 * Un copier/collé de {@link MicrosecondsSyncClockResolution}<br>
 * TODO : à mettre dans commons-cassandra
 */
public final class MySyncClockResolution extends AbstractClockResolution
      implements ClockResolution {

   private static final long serialVersionUID = 1L;

   private static final long ONE_HUNDRED = 100L;

   /**
    * The last time value issued. Used to try to prevent duplicates.
    */
   private static long lastTime = -1;

   @Override
   public long createClock() {
      // The following simulates a microseconds /10 resolution by advancing a
      // static
      // counter
      // every time a client calls the createClock method, simulating a tick.
      long theClock = getSystemMilliseconds() * ONE_HUNDRED;
      // Synchronized to guarantee unique time within and across threads.
      synchronized (MySyncClockResolution.class) {
         if (theClock > lastTime) {
            lastTime = theClock;
         } else {
            // the time i got from the system is equals or less
            // (hope not - clock going backwards)
            // One more "microsecond"
            theClock = ++lastTime;
         }
      }
      return theClock;
   }

}
