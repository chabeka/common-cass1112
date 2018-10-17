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
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceEndingJobTest {

   @Autowired
   private JobQueueCqlService jobQueueService;

   @Autowired
   private JobLectureCqlService jobLectureService;

   private UUID idJob;

   private void setJob(final UUID idJob) {
      this.idJob = idJob;
   }

   @Before
   public void before() {

      setJob(null);
   }

   @After
   public void after() {

      // suppression du traitement de masse
      if (idJob != null) {

         jobQueueService.deleteJob(idJob);

      }
   }

   @Test
   public void endingJob_success_endingBySuccess()
         throws JobDejaReserveException, JobInexistantException,
         LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      final String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, new Date());
      jobQueueService.startingJob(idJob, new Date());

      final Date dateFinTraitement = new Date();
      jobQueueService.endingJob(idJob,
                                true,
                                dateFinTraitement,
                                "traitement a réussi",
                                null);

      // vérification de JobRequest
      final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.SUCCESS.name(), jobRequest.getState());
      Assert.assertEquals("le message est inattendu", "traitement a réussi", jobRequest.getMessage());
      Assert.assertEquals("la date de fin est inattendue", dateFinTraitement, jobRequest.getEndingDate());

      // vérification de JobsQueues

      final Iterator<JobQueueCql> jobQueues = jobLectureService.getNonTerminatedSimpleJobs(reservedBy).iterator();

      JobQueueCql jobQueue = null;
      while (jobQueues.hasNext() && jobQueue == null) {
         final JobQueueCql jobQueueElement = jobQueues.next();
         if (jobQueueElement.getIdJob().equals(idJob)) {
            jobQueue = jobQueueElement;
         }
      }

      Assert.assertNull("le job ne doit plus exister dans la file d'execution de " + reservedBy, jobQueue);

      // vérification de JobHistory
      final List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJob);
      boolean isJobCreated = false;

      Assert.assertNotNull(histories.get(0));
      Assert.assertEquals("le nombre de message est inattendu", 4, histories.get(0).getTrace().size());
      final Map<UUID, String> map = histories.get(0).getTrace();
      for (final Map.Entry<UUID, String> entry : map.entrySet()) {
         if ("CREATION DU JOB".equals(entry.getValue())) {
            isJobCreated = true;
         }
      }

      Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isJobCreated);
   }

   @Test
   public void endingJob_success_endingByFailure()
         throws JobDejaReserveException, JobInexistantException,
         LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      final String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, new Date());
      jobQueueService.startingJob(idJob, new Date());

      final Date dateFinTraitement = new Date();
      jobQueueService.endingJob(idJob, false, dateFinTraitement, "traitement en échec", null);

      // vérification de JobRequest
      final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.FAILURE.name(), jobRequest.getState());
      Assert.assertEquals("le message est inattendu", "traitement en échec", jobRequest.getMessage());
      Assert.assertEquals("la date de fin est inattendue", dateFinTraitement, jobRequest.getEndingDate());

   }

   @Test
   public void endingJob_success_withoutmessage()
         throws JobDejaReserveException, JobInexistantException,
         LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      final String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, new Date());
      jobQueueService.startingJob(idJob, new Date());

      final Date dateFinTraitement = new Date();
      jobQueueService.endingJob(idJob, false, dateFinTraitement);

      // vérification de JobRequest
      final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.FAILURE.name(), jobRequest.getState());
      Assert.assertNull("aucun message n'est attendu", jobRequest.getMessage());
      Assert.assertEquals("la date de fin est inattendue", dateFinTraitement, jobRequest.getEndingDate());

   }

   private void createJob(final UUID idJob) {

      final Date dateCreation = new Date();

      final Map<String, String> jobParam = new HashMap<String, String>();
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

   @Test
   public void endingJob_failure_jobInexistantException() {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJob);

      try {
         jobQueueService.endingJob(idJob, true, new Date());
         Assert.fail("Une exception JobInexistantException devrait être lever");
      }
      catch (final JobInexistantException e) {

         Assert.assertEquals("l'identifiant du job est inattendu", idJob, e.getInstanceId());
         Assert.assertEquals("le message de l'exception est inattendu",
                             "Impossible de lancer, de modifier ou de réserver le traitement n°" + idJob
                                   + " car il n'existe pas.",
                             e.getMessage());
      }
   }
}
