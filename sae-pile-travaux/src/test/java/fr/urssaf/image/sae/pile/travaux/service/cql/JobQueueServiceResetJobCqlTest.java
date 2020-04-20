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
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobNonReinitialisableException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceResetJobCqlTest {

  @Autowired
  private JobQueueCqlService jobQueueService;

  @Autowired
  private JobLectureCqlService jobLectureService;

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  private UUID idJob;

  @Before
  public void setup() throws Exception {
    modeApiCqlSupport.initTables(MODE_API.DATASTAX);
  }

  @After
  public void after() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  /*
   * @Test public void resetJob_success() throws JobDejaReserveException,
   * JobInexistantException, LockTimeoutException,
   * JobNonReinitialisableException {
   * idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis(); createJob(idJob);
   * // On réserve le job afin d'attribuer des valeurs aux paramètres Date
   * dateReservation = new Date(); String reservedBy = "hostname";
   * jobQueueService.reserveJob(idJob, reservedBy, dateReservation);
   * // Reset du job jobQueueService.resetJob(idJob);
   * // vérification de JobRequest JobRequest jobRequest =
   * jobLectureService.getJobRequest(idJob);
   * Assert.assertEquals("l'état est inattendu", JobState.CREATED, jobRequest
   * .getState());
   * Assert.assertEquals("le reservedBy est inattendu", "", jobRequest
   * .getReservedBy());
   * Assert.assertNull("la date de réservation est inattendue", jobRequest
   * .getReservationDate()); Assert .assertNull("startingDate inattendue",
   * jobRequest.getStartingDate()); Assert.assertEquals("le Pid est inattendu",
   * Integer.valueOf(0), jobRequest.getPid());
   * Assert.assertNull("endingDate inattendue", jobRequest.getEndingDate());
   * Assert.assertEquals("le message est inattendu", "", jobRequest
   * .getMessage());
   * // vérification de JobHistory List<JobHistory> histories =
   * jobLectureService.getJobHistory(idJob);
   * Assert.assertEquals("le nombre de message est inattendu", 3, histories
   * .size());
   * Assert.assertEquals(
   * "le message de l'ajout d'un traitement est inattendu", "RESET DU JOB",
   * histories.get(2).getTrace()); }
   */

  @Test
  public void resetJob_success() throws JobDejaReserveException,
  JobInexistantException, LockTimeoutException,
  JobNonReinitialisableException {

    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    // Création du job
    createJob(idJob);

    // Réservation du job afin de le rendre reinitialisable
    final Date dateReservation = new Date();
    final String reservedBy = "hostname";
    jobQueueService.reserveJob(idJob, reservedBy, dateReservation);

    // Reset du job
    jobQueueService.resetJob(idJob);

    // vérification de JobRequest
    final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);
    Assert.assertEquals("l'état est inattendu", JobState.CREATED.name(), jobRequest
                        .getState());

    // vérification de JobsQueues
    final Iterator<JobQueueCql> jobQueuesEnAttente = jobLectureService
        .getUnreservedJobRequestIterator();

    JobQueueCql jobQueueEnAttente = null;
    while (jobQueuesEnAttente.hasNext() && jobQueueEnAttente == null) {
      final JobQueueCql jobQueueElement = jobQueuesEnAttente.next();
      if (jobQueueElement.getIdJob().equals(idJob)) {
        jobQueueEnAttente = jobQueueElement;
      }
    }

    Assert.assertNotNull("le job doit exister dans la file d'attente",
                         jobQueueEnAttente);
    Assert.assertEquals("le type de traitement est inattendu",
                        "ArchivageMasse",
                        jobQueueEnAttente.getType());
    Assert.assertEquals("les paramètres sont inattendus",
                        "param",
                        jobQueueEnAttente.getJobParameters().get("parameters"));

    final Iterator<JobQueueCql> jobQueues = jobLectureService
        .getNonTerminatedSimpleJobs(reservedBy).iterator();

    JobQueueCql jobQueue = null;
    while (jobQueues.hasNext() && jobQueue == null) {
      final JobQueueCql jobQueueElement = jobQueues.next();
      if (jobQueueElement.getIdJob().equals(idJob)) {
        jobQueue = jobQueueElement;
      }
    }

    Assert.assertNull(
                      "le job ne doit pas exister dans la file de réservation de "
                          + reservedBy,
                          jobQueue);

    // vérification de JobHistory
    final List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJob);

    Assert.assertNotNull(histories.get(0));
    Assert.assertEquals("le nombre de message est inattendu", 3, histories.get(0).getTrace().size());

    boolean isResetJob = false;

    final Map<UUID, String> map2 = histories.get(0).getTrace();
    for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
      if ("RESET DU JOB".equals(entry.getValue())) {
        isResetJob = true;
      }
    }
    Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isResetJob);
  }

  @Test(expected = JobNonReinitialisableException.class)
  public void resetJob_failure() throws JobNonReinitialisableException {

    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    createJob(idJob);

    // On tente de relancer un job qui n'est ni à l'état RESERVED, ni à l'état
    // STARTING
    jobQueueService.resetJob(idJob);
  }

  private void createJob(final UUID idJob) {

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
}
