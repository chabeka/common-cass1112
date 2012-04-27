package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class JobLectureServiceTest {

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;

   @Test
   public void getNonTerminatedJobs() throws JobDejaReserveException,
         JobInexistantException, LockTimeoutException {

      String hostname = "myHostname";

      // création d'un job
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      // réservation d'un job
      jobQueueService.reserveJob(idJob, hostname, new Date());

      List<JobRequest> jobs = jobLectureService.getNonTerminatedJobs(hostname);

      Assert.assertFalse(
            "il doit exister au moins un traitement en cours ou réservé", jobs
                  .isEmpty());

   }

   private static final String TRACE_MESSAGE = "la trace est inattendue";

   @Test
   public void getJobHistoryByUUID() {

      // création d'un job
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      // création d'un autre job
      UUID otherJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(otherJob);

      // ajout des traces dans le premier job
      jobQueueService.addHistory(idJob, TimeUUIDUtils
            .getUniqueTimeUUIDinMillis(), "message n°1");
      jobQueueService.addHistory(idJob, TimeUUIDUtils
            .getUniqueTimeUUIDinMillis(), "message n°2");
      jobQueueService.addHistory(idJob, TimeUUIDUtils
            .getUniqueTimeUUIDinMillis(), "message n°3");
      jobQueueService.addHistory(idJob, TimeUUIDUtils
            .getUniqueTimeUUIDinMillis(), "message n°4");

      // ajout des traces dans le second job
      jobQueueService.addHistory(otherJob, TimeUUIDUtils
            .getUniqueTimeUUIDinMillis(), "message n°5");
      jobQueueService.addHistory(otherJob, TimeUUIDUtils
            .getUniqueTimeUUIDinMillis(), "message n°6");

      // test sur le premier job
      List<String> histories = jobLectureService.getJobHistory(idJob);

      // 5 à cause de la trace laissée par la création du job
      Assert.assertEquals("la taille de l'historique est inattendue", 5,
            histories.size());

      // Assert.assertEquals(TRACE_MESSAGE, "CREATION DU JOB",
      // histories.get(0));
      Assert.assertEquals(TRACE_MESSAGE, "message n°1", histories.get(1));
      Assert.assertEquals(TRACE_MESSAGE, "message n°2", histories.get(2));
      Assert.assertEquals(TRACE_MESSAGE, "message n°3", histories.get(3));
      Assert.assertEquals(TRACE_MESSAGE, "message n°4", histories.get(4));

      // test sur le second job
      List<String> otherhistories = jobLectureService.getJobHistory(otherJob);

      // 3 à cause de la trace laissée par la création du job
      Assert.assertEquals("la taille de l'historique est inattendue", 3,
            otherhistories.size());

      // Assert.assertEquals(TRACE_MESSAGE, "CREATION DU JOB",
      // histories.get(0));
      Assert.assertEquals(TRACE_MESSAGE, "message n°5", otherhistories.get(1));
      Assert.assertEquals(TRACE_MESSAGE, "message n°6", otherhistories.get(2));

   }

   private void createJob(UUID idJob) {

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setParameters("parameters");
      job.setCreationDate(new Date());

      jobQueueService.addJob(job);

   }

}
