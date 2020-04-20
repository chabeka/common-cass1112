package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import junit.framework.Assert;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
public class OperationPileTravauxServiceTest {


  @Autowired
  private JobQueueService jobQueueService;

  @Autowired
  private JobLectureService jobLectureService;

  @Autowired
  private Keyspace keyspace;

  @Autowired
  private OperationPileTravauxService operationPileTravauxService;

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  @Before
  public void before() {
    modeApiCqlSupport.initTables(MODE_API.HECTOR);
  }

  @After
  public final void init() throws Exception {
    // Après chaque test, on reset les données de cassandra
    cassandraServer.resetData(true, MODE_API.HECTOR);
  }

  @Test
  public void test_purger_success() throws JobDejaReserveException, JobInexistantException, LockTimeoutException {

    // On créé 3 jobs avec différentes dates de création
    final UUID[] uuids = new UUID[3];

    Date date = new Date();
    uuids[0] = addJobForTest(0, DateUtils.addDays(date, -5) );
    uuids[1] = addJobForTest(1, DateUtils.addDays(date, -10) );
    uuids[2] = addJobForTest(2, DateUtils.addDays(date, -20) );

    // Réservation du job 0 et 1
    final String reservedBy = "hostname";
    jobQueueService.reserveJob(uuids[0], reservedBy, new Date());
    jobQueueService.reserveJob(uuids[1], reservedBy, new Date());

    date = DateUtils.setHours(date, 0);
    date = DateUtils.setMinutes(date, 0);
    date = DateUtils.setSeconds(date, 0);
    date = DateUtils.setMilliseconds(date, 0);

    operationPileTravauxService.purger(DateUtils.addDays(date, -10));

    // Vérification non suppression du job 1
    assertJobNotDelete(uuids[0]);

    // Vérification suppression du job 1 et 2
    assertJobDelete(uuids[1]);
    assertJobDelete(uuids[2]);

    // vérification de JobsQueues pour les job 0 et 1 qui ont été reservés

    final Iterator<JobQueue> jobQueues = jobLectureService
        .getNonTerminatedSimpleJobs(reservedBy).iterator();

    JobQueue jobQueue0 = null;
    JobQueue jobQueue1 = null;
    while (jobQueues.hasNext() && (jobQueue0 == null || jobQueue1 == null)) {
      final JobQueue jobQueueElement = jobQueues.next();
      if (jobQueueElement.getIdJob().equals(uuids[0])) {
        jobQueue0 = jobQueueElement;
      }
      if (jobQueueElement.getIdJob().equals(uuids[1])) {
        jobQueue1 = jobQueueElement;
      }
    }

    Assert.assertNotNull(
                         "le job 0 doit encore exister dans la file d'execution de "
                             + reservedBy, jobQueue0);

    Assert.assertNull(
                      "le job 1 ne doit plus exister dans la file d'execution de "
                          + reservedBy, jobQueue1);

  }


  @Test(expected = IllegalArgumentException.class)
  public void test_purger_error() throws JobDejaReserveException, JobInexistantException, LockTimeoutException {
    operationPileTravauxService.purger(null);
  }


  /**
   * Crée un job
   * 
   * @param index
   *           Un n° permettant de différencier les différents jobs
   * @return L'id du job créé
   */
  private UUID addJobForTest(final int index, final Date dateCreation) {
    final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

    final String parameters = "sommaire=ecde:/toto.toto.com/sommaire.xml&idTraitement="
        + index;
    final Map<String,String> jobParam= new HashMap<>();
    jobParam.put("parameters", parameters);

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("ArchivageMasse");
    job.setJobParameters(jobParam);
    job.setCreationDate(dateCreation);
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());

    jobQueueService.addJob(job);
    return idJob;
  }

  private void assertJobDelete(final UUID idJob) {

    // vérification de JobRequest

    final JobRequest job = jobLectureService.getJobRequest(idJob);
    Assert.assertNull("le job ne doit plus exister", job);

    // vérification de JobHistory
    final List<JobHistory> jobHistory = jobLectureService.getJobHistory(idJob);

    Assert.assertTrue("le job ne doit plus avoir d'historique", jobHistory
                      .isEmpty());

  }

  private void assertJobNotDelete(final UUID idJob) {

    // vérification de JobRequest

    final JobRequest job = jobLectureService.getJobRequest(idJob);
    Assert.assertNotNull("le job doit exister", job);

    // vérification de JobHistory
    final List<JobHistory> jobHistory = jobLectureService.getJobHistory(idJob);

    Assert.assertFalse("le job doit avoir un historique", jobHistory
                       .isEmpty());

  }

}
