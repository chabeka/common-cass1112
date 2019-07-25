package fr.urssaf.image.sae.pile.travaux.service.cql;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-pile-travaux-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceAddJobCqlTest {

  @Autowired
  private JobQueueCqlService jobQueueService;

  @Autowired
  private JobLectureCqlService jobLectureService;

  @Autowired
  private CassandraServerBean cassandraServer;

  private UUID idJob;

  private void setJob(final UUID idJob) {
    this.idJob = idJob;
  }

  @Before
  public void before() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
    setJob(null);
  }

  @After
  public void after() {

    // suppression du traitement de masse
    if (idJob != null) {

      // jobQueueService.deleteJob(idJob);

    }
  }

  @Test
  public void addJob_success() {

    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

    final Date dateCreation = new Date();
    final Map<String, String> jobParam = new HashMap<>();
    jobParam.put("parameters", "param");

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("ArchivageMasse");
    job.setJobParameters(jobParam);
    job.setClientHost("clientHost");
    job.setDocCount(100);
    job.setSaeHost("saeHost");
    job.setCreationDate(dateCreation);
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());
    jobQueueService.addJob(job);

    // vérification de JobRequest
    final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);

    Assert.assertNotNull("le job doit exister", jobRequest);
    Assert.assertEquals("l'état est inattendu", JobState.CREATED.name(), jobRequest.getState());
    Assert.assertEquals("le type de traitement est inattendu", "ArchivageMasse", jobRequest.getType());
    Assert.assertEquals("les paramètres sont inattendus", jobParam, jobRequest.getJobParameters());
    Assert.assertEquals("le clientHost est inattendu", "clientHost", jobRequest.getClientHost());
    Assert.assertEquals("le docCount est inattendu", Integer.valueOf(100), jobRequest.getDocCount());
    Assert.assertEquals("le saeHost est inattendu", "saeHost", jobRequest.getSaeHost());

    // vérification de JobsQueue
    final Iterator<JobQueueCql> jobQueues = jobLectureService.getUnreservedJobRequestIterator();

    JobQueueCql jobQueue = null;
    while (jobQueues.hasNext() && jobQueue == null) {
      final JobQueueCql jobQueueElement = jobQueues.next();
      if (jobQueueElement.getIdJob().equals(idJob)) {
        jobQueue = jobQueueElement;
      }
    }

    Assert.assertNotNull("le job doit exister dans la file d'attente", jobQueue);

    Assert.assertEquals("le type de traitement est inattendu", "ArchivageMasse", jobQueue.getType());
    Assert.assertEquals("les paramètres sont inattendus", jobParam, jobQueue.getJobParameters());
    // vérification de JobHistory
    final List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJob);

    Assert.assertNotNull(histories.get(0));
    Assert.assertEquals("le nombre de message est inattendu", 1, histories.get(0).getTrace().size());

    final Map<UUID, String> map = histories.get(0).getTrace();
    Assert.assertNotNull(map);
    Assert.assertFalse("la liste doit être non vide ", map.keySet().isEmpty());
    Assert.assertEquals("le message de l'ajout d'un traitement est inattendu", "CREATION DU JOB", map.values().toArray()[0]);

  }

  @Test
  public void addJobAvecHash_success() {

    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

    final Map<String, String> jobParam = new HashMap<>();
    jobParam.put("ECDE_URL", "url");
    jobParam.put("HASH", "hash");
    jobParam.put("TYPE_HASH", "typeHash");

    final Date dateCreation = new Date();

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("ArchivageMasse");
    job.setJobParameters(jobParam);
    job.setClientHost("clientHost");
    job.setDocCount(100);
    job.setSaeHost("saeHost");
    job.setCreationDate(dateCreation);
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());

    jobQueueService.addJob(job);

    // vérification de JobRequest
    final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);

    Assert.assertNotNull("le job doit exister", jobRequest);
    Assert.assertEquals("l'état est inattendu", JobState.CREATED.name(), jobRequest.getState());
    Assert.assertEquals("le type de traitement est inattendu", "ArchivageMasse", jobRequest.getType());
    Assert.assertEquals("Les job parameters sont inattendu ", jobParam, jobRequest.getJobParameters());
    Assert.assertEquals("le clientHost est inattendu", "clientHost", jobRequest.getClientHost());
    Assert.assertEquals("le docCount est inattendu", Integer.valueOf(100), jobRequest.getDocCount());
    Assert.assertEquals("le saeHost est inattendu", "saeHost", jobRequest.getSaeHost());

    // vérification de JobsQueue
    final Iterator<JobQueueCql> jobQueues = jobLectureService.getUnreservedJobRequestIterator();

    JobQueueCql jobQueue = null;
    while (jobQueues.hasNext() && jobQueue == null) {
      final JobQueueCql jobQueueElement = jobQueues.next();
      if (jobQueueElement.getIdJob().equals(idJob)) {
        jobQueue = jobQueueElement;
      }
    }

    Assert.assertNotNull("le job doit exister dans la file d'attente", jobQueue);

    Assert.assertEquals("le type de traitement est inattendu", "ArchivageMasse", jobQueue.getType());
    Assert.assertEquals("Les job parameters sont inattendu ", jobParam, jobQueue.getJobParameters());
    // vérification de JobHistory
    final List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJob);

    Assert.assertNotNull(histories.get(0));
    Assert.assertEquals("le nombre de message est inattendu", 1, histories.get(0).getTrace().size());

    final Map<UUID, String> map = histories.get(0).getTrace();
    Assert.assertNotNull(map);
    Assert.assertFalse("la liste doit être non vide ", map.keySet().isEmpty());
    Assert.assertEquals("le message de l'ajout d'un traitement est inattendu", "CREATION DU JOB", map.values().toArray()[0]);

  }

}
