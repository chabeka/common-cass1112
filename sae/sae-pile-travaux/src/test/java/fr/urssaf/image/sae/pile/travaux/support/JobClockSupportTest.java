package fr.urssaf.image.sae.pile.travaux.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.exception.ClockSynchronizationException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import junit.framework.Assert;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;

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

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  private ColumnFamilyTemplate<UUID, String> template;

  @Before
  public void before() {

    template = new ThriftColumnFamilyTemplate<>(keyspace,
        "JobRequest", UUIDSerializer.get(), StringSerializer.get());
    modeApiCqlSupport.initTables(MODE_API.HECTOR);
  }

  private HColumn<?, ?> addJob() {

    final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

    final Date dateCreation = new Date();

    final Map<String,String> jobParam= new HashMap<>();
    jobParam.put("parameters", "param");

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("type");
    job.setJobParameters(jobParam);
    job.setCreationDate(dateCreation);
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());

    jobQueueService.addJob(job);

    final HColumn<?, ?> column = template.querySingleColumn(idJob, "state",
                                                            StringSerializer.get());

    return column;
  }

  @Test
  public void synchronisation_success() {

    final HColumn<?, ?> column = addJob();

    final long time = support.currentCLock(column);

    Assert.assertTrue("Il n'y a aucun problème de synchronisation", time > 0);
  }

  @Test
  public void synchronisation_warning() {

    final long decalage = configuration.getMaxTimeSynchroWarn() + 100;

    Assert.assertTrue("Il n'y a aucun problème de synchronisation",
                      decalage < configuration.getMaxTimeSynchroError());

    final HColumn<?, ?> column = addJob();
    column.setClock(keyspace.createClock() + decalage);

    final long time = support.currentCLock(column);

    Assert.assertTrue("Il y a aucun problème de synchronisation", time > 0);

  }

  @Test(expected = ClockSynchronizationException.class)
  public void synchronisation_failure() {

    final long decalage = configuration.getMaxTimeSynchroError() + 10000;

    final HColumn<?, ?> column = addJob();
    column.setClock(keyspace.createClock() + decalage);

    support.currentCLock(column);

  }
}
