package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.Iterator;
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
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceResetJobTest {

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
   public void resetJob_success() throws JobDejaReserveException, JobInexistantException, LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      // On réserve le job afin d'attribuer des valeurs aux paramètres
      Date dateReservation = new Date();
      String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, dateReservation);
      
      // Reset du job
      jobQueueService.resetJob(idJob);

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.CREATED, jobRequest
            .getState());
     
       Assert.assertEquals("le ReservedBy est inattendu", "", jobRequest
            .getReservedBy());

      Assert.assertNull("la date de réservation est inattendue", jobRequest.getReservationDate());
      Assert.assertNull("startingDate inattendue", jobRequest.getStartingDate());
      Assert.assertNull("le Pid est inattendu", jobRequest.getPid());
      Assert.assertNull("endingDate inattendue", jobRequest.getEndingDate());
      Assert.assertEquals("le message est inattendu", "", jobRequest.getMessage());
      
      // vérification de JobHistory
      List<JobHistory> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertEquals("le nombre de message est inattendu", 2, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "RESET DU JOB", histories.get(1).getTrace());
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
}
