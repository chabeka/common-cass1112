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

   private void createJob(UUID idJob) {

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setParameters("parameters");
      job.setCreationDate(new Date());

      jobQueueService.addJob(job);

   }

}
