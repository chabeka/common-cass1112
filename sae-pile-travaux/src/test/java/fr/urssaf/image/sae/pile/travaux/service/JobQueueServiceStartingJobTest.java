package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.List;
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
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceStartingJobTest {

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
   public void startingJob_success() throws JobInexistantException,
         JobDejaReserveException, LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      jobQueueService.reserveJob(idJob, "hostname", new Date());

      Date dateDebutTraitement = new Date();
      jobQueueService.startingJob(idJob, dateDebutTraitement);

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.STARTING, jobRequest
            .getState());
      Assert.assertEquals("la date de démarrage est inattendue",
            dateDebutTraitement, jobRequest.getStartingDate());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      List<JobHistory> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertEquals("le nombre de message est inattendu", 3, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "DEMARRAGE DU JOB", histories.get(2).getTrace());
   }

   private void createJob(UUID idJob) {

      Date dateCreation = new Date();

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setParameters("parameters");
      job.setClientHost("clientHost");
      job.setDocCount(100);
      job.setSaeHost("saeHost");
      job.setCreationDate(dateCreation);

      jobQueueService.addJob(job);
   }

   @Test
   public void startingJob_failure_jobInexistantException() {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJob);

      try {
         jobQueueService.startingJob(idJob, new Date());
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
