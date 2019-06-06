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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-pile-travaux-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
public class JobLectureServiceCqlTest {

  @Autowired
  private JobQueueCqlService jobQueueService;

  @Autowired
  private JobLectureCqlService jobLectureService;

  @Autowired
  private CassandraServerBeanCql cassandraServer;

  private UUID idJob;

  private UUID otherJob;

  private void setJob(final UUID idJob) {
    this.idJob = idJob;
    otherJob = idJob;
  }

  @Before
  public void before() throws Exception {
    cassandraServer.resetData(true);
    setJob(null);
  }

  @After
  public void after() {

    // List<JobRequest> jobList = jobLectureService.getAllJobs(keyspace);
    // suppression du traitement de masse
    if (idJob != null) {

      jobQueueService.deleteJob(idJob);

    }

    if (otherJob != null) {

      jobQueueService.deleteJob(otherJob);

    }
  }

  @Test
  public void getUnreservedJobRequestIterator() {

    // création d'un job
    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    createJob(idJob);

    final Iterator<JobQueueCql> jobs = jobLectureService.getUnreservedJobRequestIterator();

    Assert.assertTrue("il doit exister au moins un traitement en cours ou réservé", jobs.hasNext());

  }

  @Test
  public void getNonTerminatedJobs() throws JobDejaReserveException, JobInexistantException, LockTimeoutException {

    final String hostname = "myHostname";

    // création d'un job
    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    createJob(idJob);

    // réservation d'un job
    jobQueueService.reserveJob(idJob, hostname, new Date());

    final List<JobRequestCql> jobs = jobLectureService.getNonTerminatedJobs(hostname);

    Assert.assertFalse("il doit exister au moins un traitement en cours ou réservé", jobs.isEmpty());
    // verifier que le job n'est plus dans la liste des job non reservé
    final Iterator<JobQueueCql> it = jobLectureService.getUnreservedJobRequestIterator();
    while (it.hasNext()) {
      final JobQueueCql job = it.next();
      Assert.assertFalse(" Le job est censé être supprimé de la liste d'attente ", job.getIdJob().equals(idJob));
    }
  }

  @Test
  public void getJobHistoryByUUID() {

    // création d'un job
    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    createJob(idJob);

    // création d'un autre job
    otherJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    createJob(otherJob);

    // ajout des traces dans le premier job

    final Date date = new Date();
    final long timestamp = date.getTime();

    jobQueueService.addHistory(idJob, TimeUUIDUtils.getTimeUUID(timestamp + 1), "message n°1");
    jobQueueService.addHistory(idJob, TimeUUIDUtils.getTimeUUID(timestamp + 2), "message n°2");
    jobQueueService.addHistory(idJob, TimeUUIDUtils.getTimeUUID(timestamp + 3), "message n°3");
    jobQueueService.addHistory(idJob, TimeUUIDUtils.getTimeUUID(timestamp + 4), "message n°4");

    // ajout des traces dans le second job
    jobQueueService.addHistory(otherJob, TimeUUIDUtils.getTimeUUID(timestamp + 1), "message n°5");
    jobQueueService.addHistory(otherJob, TimeUUIDUtils.getTimeUUID(timestamp + 2), "message n°6");

    // test sur le premier job
    final JobHistoryCql history = jobLectureService.getJobHistory(idJob).get(0);

    // 1 à cause de la trace laissée par la création du job
    Assert.assertNotNull("la taille de l'historique est inattendue", history);

    // 5 traces dans la map à cause de la trace laissée par la création du job
    Assert.assertEquals("la taille de l'historique est inattendue", 5, history.getTrace().size());

    // Assert.assertEquals(TRACE_MESSAGE, "CREATION DU JOB",
    // histories.get(0));
    Assert.assertTrue(history.getTrace().containsValue("message n°1"));
    Assert.assertTrue(history.getTrace().containsValue("message n°2"));
    Assert.assertTrue(history.getTrace().containsValue("message n°3"));
    Assert.assertTrue(history.getTrace().containsValue("message n°4"));

    // test sur le second job
    final JobHistoryCql otherhistory = jobLectureService.getJobHistory(otherJob).get(0);

    // 3 à cause de la trace laissée par la création du job
    Assert.assertEquals("la taille de l'historique est inattendue", 3, otherhistory.getTrace().size());

    // Assert.assertEquals(TRACE_MESSAGE, "CREATION DU JOB",
    // histories.get(0));
    Assert.assertTrue(otherhistory.getTrace().containsValue("message n°5"));
    Assert.assertTrue(otherhistory.getTrace().containsValue("message n°6"));

  }

  @Test
  public void testIsResettable() {
    // création d'un job
    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    final JobRequestCql job = new JobRequestCql();

    // Le job est à l'état CREATED, sa réinitialisation doit être possible
    job.setState(JobState.RESERVED.name());
    Assert.assertTrue("Le job est à l'état RESERVED, il doit pouvoir être réinitialisé", jobLectureService.isJobResettable(job));

    // Le job est dans un état différente de CREATED, RESERVED ou STARTING, sa
    // réinitialisation est impossible
    job.setState(JobState.FAILURE.name());
    Assert.assertFalse("Le job est à l'état FAILURE, il ne doit pas pouvoir être réinitialisé", jobLectureService.isJobResettable(job));
  }

  @Test
  public void testIsRemovable() {
    // création d'un job
    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    final JobRequestCql job = new JobRequestCql();

    // Le job est à l'état CREATED, sa suppression doit être possible
    job.setState(JobState.CREATED.name());
    Assert.assertTrue("Le job est à l'état CREATED, il doit pouvoir être supprimé", jobLectureService.isJobRemovable(job));

    // Le job est dans un état différente de CREATED, RESERVED ou STARTING, sa
    // suppression est impossible
    job.setState(JobState.FAILURE.name());
    Assert.assertFalse("Le job est à l'état FAILURE, il ne doit pas pouvoir être supprimé", jobLectureService.isJobRemovable(job));
  }

  private void createJob(final UUID idJob) {
    final Map<String, String> jobParam = new HashMap<>();
    jobParam.put("parameters", "param");

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("ArchivageMasse");
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());
    job.setJobParameters(jobParam);
    job.setCreationDate(new Date());

    jobQueueService.addJob(job);

  }

  /**
   * TU pour les Redmine 3406 et 3407 Si la colonne dateCreation d'un
   * JobRequest est vide => erreur technique
   */
  @Test
  public void getJobRequest_DateCreationVide() {

  }

}
