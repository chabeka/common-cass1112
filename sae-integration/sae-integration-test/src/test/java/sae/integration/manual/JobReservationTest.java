
package sae.integration.manual;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.CassandraHelper;

public class JobReservationTest {
   private static final Logger LOGGER = LoggerFactory.getLogger(JobReservationTest.class);

   @Test
   public void reservationJobTest() throws Exception {
      final Environment environnement = Environments.LOCAL_BATCH;
      final UUID jobId = UUID.fromString("ffe47d50-313a-11ea-ad2c-e98955d81e06");
      final CassandraHelper cassandraHelper = new CassandraHelper(environnement);
      cassandraHelper.reserveJobAgain(jobId);
   }

}