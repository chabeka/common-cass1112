package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.HashMap;
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

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceRenseignerDocCountJobTest {

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
   public void renseignerDocCountJob_success() throws JobInexistantException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      createJob(idJob);

      Integer docCount = 100;
      jobQueueService.renseignerDocCountJob(idJob, docCount);

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);

      Assert.assertEquals("le nombre de docs traités est inattendu", docCount, jobRequest
            .getDocCount());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      List<JobHistory> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertEquals("le nombre de message est inattendu", 2, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "DOC_COUNT RENSEIGNE", histories.get(1).getTrace());

      // on renseigne une seconde fois le nombre de docs
      Integer otherDocCount = 150;
      jobQueueService.renseignerDocCountJob(idJob, otherDocCount);

      // vérification de JobRequest
      jobRequest = jobLectureService.getJobRequest(idJob);

      Assert.assertEquals("le nombre de docs traités est inattendu", otherDocCount,
            jobRequest.getDocCount());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      histories = jobLectureService.getJobHistory(idJob);

      Assert.assertEquals("le nombre de message est inattendu", 3, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "DOC_COUNT RENSEIGNE", histories.get(2).getTrace());

   }

   @Test
   public void renseignerPidJob_failure_jobInexistantException() {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJob);

      try {
         jobQueueService.renseignerDocCountJob(idJob, 100);
         Assert.fail("Une exception JobInexistantException devrait être lever");
      } catch (JobInexistantException e) {

         Assert.assertEquals("l'identifiant du job est inattendu", idJob, e
               .getInstanceId());
         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible de lancer, de modifier ou de réserver le traitement n°" + idJob
                     + " car il n'existe pas.", e.getMessage());
      }
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

}
