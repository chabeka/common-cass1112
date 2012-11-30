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

import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceAddJobTest {

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
   public void addJob_success() {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      
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

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);

      Assert.assertNotNull("le job doit exister", jobRequest);
      Assert.assertEquals("l'état est inattendu", JobState.CREATED, jobRequest
            .getState());
      Assert.assertEquals("le type de traitement est inattendu",
            "ArchivageMasse", jobRequest.getType());
      Assert.assertEquals("les paramètres sont inattendus", jobParam,
            jobRequest.getJobParameters());
      Assert.assertEquals("le clientHost est inattendu", "clientHost",
            jobRequest.getClientHost());
      Assert.assertEquals("le docCount est inattendu", Integer.valueOf(100),
            jobRequest.getDocCount());
      Assert.assertEquals("le saeHost est inattendu", "saeHost", jobRequest
            .getSaeHost());

      // vérification de JobsQueue
      Iterator<JobQueue> jobQueues = jobLectureService
            .getUnreservedJobRequestIterator();

      JobQueue jobQueue = null;
      while (jobQueues.hasNext() && jobQueue == null) {
         JobQueue jobQueueElement = jobQueues.next();
         if (jobQueueElement.getIdJob().equals(idJob)) {
            jobQueue = jobQueueElement;
         }
      }

      Assert.assertNotNull("le job doit exister dans la file d'attente",
            jobQueue);

      Assert.assertEquals("le type de traitement est inattendu",
            "ArchivageMasse", jobQueue.getType());
      Assert.assertEquals("les paramètres sont inattendus", jobParam,
            jobQueue.getJobParameters());
      // vérification de JobHistory
      List<JobHistory> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertEquals("le nombre de message est inattendu", 1, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "CREATION DU JOB", histories.get(0).getTrace());

   }
   
   @Test
   public void addJobAvecHash_success() {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      
      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put("ECDE_URL", "url");
      jobParam.put("HASH", "hash");
      jobParam.put("TYPE_HASH", "typeHash");

      Date dateCreation = new Date();

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setJobParameters(jobParam);
      job.setClientHost("clientHost");
      job.setDocCount(100);
      job.setSaeHost("saeHost");
      job.setCreationDate(dateCreation);

      jobQueueService.addJob(job);

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);

      Assert.assertNotNull("le job doit exister", jobRequest);
      Assert.assertEquals("l'état est inattendu", JobState.CREATED, jobRequest
            .getState());
      Assert.assertEquals("le type de traitement est inattendu",
            "ArchivageMasse", jobRequest.getType());
      Assert.assertEquals("Les job parameters sont inattendu ", jobParam, jobRequest.getJobParameters());
      Assert.assertEquals("le clientHost est inattendu", "clientHost",
            jobRequest.getClientHost());
      Assert.assertEquals("le docCount est inattendu", Integer.valueOf(100),
            jobRequest.getDocCount());
      Assert.assertEquals("le saeHost est inattendu", "saeHost", jobRequest
            .getSaeHost());

      // vérification de JobsQueue
      Iterator<JobQueue> jobQueues = jobLectureService
            .getUnreservedJobRequestIterator();

      JobQueue jobQueue = null;
      while (jobQueues.hasNext() && jobQueue == null) {
         JobQueue jobQueueElement = jobQueues.next();
         if (jobQueueElement.getIdJob().equals(idJob)) {
            jobQueue = jobQueueElement;
         }
      }

      Assert.assertNotNull("le job doit exister dans la file d'attente",
            jobQueue);

      Assert.assertEquals("le type de traitement est inattendu",
            "ArchivageMasse", jobQueue.getType());
      Assert.assertEquals("Les job parameters sont inattendu ", jobParam, jobQueue.getJobParameters());
      // vérification de JobHistory
      List<JobHistory> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertEquals("le nombre de message est inattendu", 1, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "CREATION DU JOB", histories.get(0).getTrace());

   }

}
