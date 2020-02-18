package fr.urssaf.image.sae.pile.travaux.service.cql;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-pile-travaux-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceProcessParamAndJobParamCqlTest {

  @Autowired
  private JobQueueCqlService jobQueueService;

  @Autowired
  private JobLectureCqlService jobLectureService;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  private UUID idJobWithParam;

  private UUID idJobWithJobParam;

  @Before
  public void setup() throws Exception {
    modeApiCqlSupport.initTables(MODE_API.DATASTAX);
  }

  @Test
  public void startingJob_success() throws JobInexistantException, JobDejaReserveException, LockTimeoutException, InterruptedException {

    idJobWithParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();


    createJobWithParam(idJobWithParam);

    idJobWithJobParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    createJobWithJobParam(idJobWithJobParam);

    jobQueueService.reserveJob(idJobWithParam, "hostname", new Date());

    jobQueueService.reserveJob(idJobWithJobParam, "hostname", new Date());

    final Date dateDebutTraitement = new Date();
    jobQueueService.startingJob(idJobWithParam, dateDebutTraitement);

    jobQueueService.startingJob(idJobWithJobParam, dateDebutTraitement);

    // vérification de JobRequest
    final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJobWithParam);
    Assert.assertEquals("l'état est inattendu", JobState.STARTING.name(), jobRequest.getState());
    Assert.assertEquals("la date de démarrage est inattendue", dateDebutTraitement, jobRequest.getStartingDate());

    // vérification de JobRequest
    final JobRequestCql jobRequestJobParam = jobLectureService.getJobRequest(idJobWithJobParam);
    Assert.assertEquals("l'état est inattendu", JobState.STARTING.name(), jobRequestJobParam.getState());
    Assert.assertEquals("la date de démarrage est inattendue", dateDebutTraitement, jobRequestJobParam.getStartingDate());

    // vérification de JobsQueues

    // rien à vérifier

    // vérification de JobHistory
    final List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJobWithParam);

    /*
     * Assert.assertEquals("le nombre de message est inattendu", 3, histories
     * .size());
     * Assert.assertEquals(
     * "le message de l'ajout d'un traitement est inattendu",
     * "DEMARRAGE DU JOB",
     * histories.get(2).getTrace());
     */

    Assert.assertNotNull(histories.get(0));
    Assert.assertEquals("le nombre de message est inattendu", 3, histories.get(0).getTrace().size());

    boolean isJobCreated = false;

    final Map<UUID, String> map = histories.get(0).getTrace();
    for (final Map.Entry<UUID, String> entry : map.entrySet()) {
      if ("DEMARRAGE DU JOB".equals(entry.getValue())) {
        isJobCreated = true;
      }
    }
    Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isJobCreated);

    // vérification de JobHistory
    final List<JobHistoryCql> historiesJobParam = jobLectureService.getJobHistory(idJobWithJobParam);

    Assert.assertNotNull(historiesJobParam.get(0));
    Assert.assertEquals("le nombre de message est inattendu", 3, historiesJobParam.get(0).getTrace().size());

    boolean isJobCreated2 = false;

    final Map<UUID, String> map2 = histories.get(0).getTrace();
    for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
      if ("DEMARRAGE DU JOB".equals(entry.getValue())) {
        isJobCreated2 = true;
      }
    }
    Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isJobCreated2);
  }

  private void createJobWithJobParam(final UUID idJob) {

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
  }

  private void createJobWithParam(final UUID idJob) {

    final Date dateCreation = new Date();

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("ArchivageMasse");
    job.setParameters("Parameters");
    job.setClientHost("clientHost");
    job.setDocCount(100);
    job.setSaeHost("saeHost");
    job.setCreationDate(dateCreation);
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());

    jobQueueService.addJob(job);
  }

  @Test
  public void startingJob_failure_jobInexistantException() {

    idJobWithParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

    // on s'assure qu'il n'existe pas
    jobQueueService.deleteJob(idJobWithParam);

    try {
      jobQueueService.startingJob(idJobWithParam, new Date());
      Assert.fail("Une exception JobInexistantException devrait être lever");
    } catch (final JobInexistantException e) {

      Assert.assertEquals("l'identifiant du job est inattendu", idJobWithParam, e.getInstanceId());
      Assert.assertEquals("le message de l'exception est inattendu",
                          "Impossible de lancer, de modifier ou de réserver le traitement n°" + idJobWithParam + " car il n'existe pas.",
                          e.getMessage());
    }
  }

  @Test
  public void startingJobWithJobParam_failure_jobInexistantException() {

    idJobWithJobParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

    // on s'assure qu'il n'existe pas
    jobQueueService.deleteJob(idJobWithJobParam);

    try {
      jobQueueService.startingJob(idJobWithJobParam, new Date());
      Assert.fail("Une exception JobInexistantException devrait être lever");
    } catch (final JobInexistantException e) {

      Assert.assertEquals("l'identifiant du job est inattendu", idJobWithJobParam, e.getInstanceId());
      Assert.assertEquals("le message de l'exception est inattendu",
                          "Impossible de lancer, de modifier ou de réserver le traitement n°" + idJobWithJobParam + " car il n'existe pas.",
                          e.getMessage());
    }
  }

}
