package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

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
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceEndingJobTest {

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;

   private UUID idJob;

   private void setJob(UUID idJob) {
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

      String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, new Date());
      jobQueueService.startingJob(idJob, new Date());

      Date dateFinTraitement = new Date();
      jobQueueService.endingJob(idJob, true, dateFinTraitement,
            "traitement a réussi", null);

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.SUCCESS, jobRequest
            .getState());
      Assert.assertEquals("le message est inattendu", "traitement a réussi",
            jobRequest.getMessage());
      Assert.assertEquals("la date de fin est inattendue", dateFinTraitement,
            jobRequest.getEndingDate());

      // vérification de JobsQueues

      Iterator<JobQueue> jobQueues = jobLectureService
            .getNonTerminatedSimpleJobs(reservedBy).iterator();

      JobQueue jobQueue = null;
      while (jobQueues.hasNext() && jobQueue == null) {
         JobQueue jobQueueElement = jobQueues.next();
         if (jobQueueElement.getIdJob().equals(idJob)) {
            jobQueue = jobQueueElement;
         }
      }

      Assert.assertNull(
            "le job ne doit plus exister dans la file d'execution de "
                  + reservedBy, jobQueue);

      // vérification de JobHistory
      List<JobHistory> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertEquals("le nombre de message est inattendu", 4, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "FIN DU JOB", histories.get(3).getTrace());
   }

   @Test
   public void endingJob_success_endingByFailure()
         throws JobDejaReserveException, JobInexistantException,
         LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, new Date());
      jobQueueService.startingJob(idJob, new Date());

      Date dateFinTraitement = new Date();
      jobQueueService.endingJob(idJob, false, dateFinTraitement,
            "traitement en échec", null);

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.FAILURE, jobRequest
            .getState());
      Assert.assertEquals("le message est inattendu", "traitement en échec",
            jobRequest.getMessage());
      Assert.assertEquals("la date de fin est inattendue", dateFinTraitement,
            jobRequest.getEndingDate());

   }

   @Test
   public void endingJob_success_withoutmessage()
         throws JobDejaReserveException, JobInexistantException,
         LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, new Date());
      jobQueueService.startingJob(idJob, new Date());

      Date dateFinTraitement = new Date();
      jobQueueService.endingJob(idJob, false, dateFinTraitement);

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.FAILURE, jobRequest
            .getState());
      Assert.assertNull("aucun message n'est attendu", jobRequest.getMessage());
      Assert.assertEquals("la date de fin est inattendue", dateFinTraitement,
            jobRequest.getEndingDate());

   }

   private void createJob(UUID idJob) {

      Date dateCreation = new Date();

      Map<String,String> jobParam= new HashMap<String, String>();
      jobParam.put("parameters", "param");
      
      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setJobParameters(jobParam);
      job.setClientHost("clientHost");
      job.setDocCount(100);
      job.setSaeHost("saeHost");
      job.setCreationDate(dateCreation);

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
      } catch (JobInexistantException e) {

         Assert.assertEquals("l'identifiant du job est inattendu", idJob, e
               .getInstanceId());
         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible de lancer ou de réserver le traitement n°" + idJob
                     + " car il n'existe pas.", e.getMessage());
      }
   }
}
