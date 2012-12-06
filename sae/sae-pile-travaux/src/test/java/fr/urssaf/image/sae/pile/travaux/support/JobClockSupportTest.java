package fr.urssaf.image.sae.pile.travaux.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.exception.ClockSynchronizationException;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;

//TODO  à transporter dans commons-cassandra
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@SuppressWarnings("PMD.MethodNamingConventions")
public class JobClockSupportTest {

   @Autowired
   private JobClockSupport support;

   @Autowired
   private JobClockConfiguration configuration;

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private Keyspace keyspace;

   private ColumnFamilyTemplate<UUID, String> template;

   @Before
   public void before() {

      template = new ThriftColumnFamilyTemplate<UUID, String>(keyspace,
            "JobRequest", UUIDSerializer.get(), StringSerializer.get());

   }

   private HColumn<?, ?> addJob() {

      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      Date dateCreation = new Date();
      
      Map<String,String> jobParam= new HashMap<String, String>();
      jobParam.put("parameters", "param");

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("type");
      job.setJobParameters(jobParam);
      job.setCreationDate(dateCreation);

      jobQueueService.addJob(job);

      HColumn<?, ?> column = template.querySingleColumn(idJob, "state",
            StringSerializer.get());

      return column;
   }

   @Test
   public void synchronisation_success() {

      HColumn<?, ?> column = addJob();

      long time = support.currentCLock(column);

      Assert.assertTrue("Il n'y a aucun problème de synchronisation", time > 0);
   }

   @Test
   public void synchronisation_warning() {

      long decalage = configuration.getMaxTimeSynchroWarn() + 100;

      Assert.assertTrue("Il n'y a aucun problème de synchronisation",
            decalage < configuration.getMaxTimeSynchroError());

      HColumn<?, ?> column = addJob();
      column.setClock(keyspace.createClock() + decalage);

      long time = support.currentCLock(column);

      Assert.assertTrue("Il y a aucun problème de synchronisation", time > 0);

   }

   @Test(expected = ClockSynchronizationException.class)
   public void synchronisation_failure() {

      long decalage = configuration.getMaxTimeSynchroError() + 10000;

      HColumn<?, ?> column = addJob();
      column.setClock(keyspace.createClock() + decalage);

      support.currentCLock(column);

   }
}
