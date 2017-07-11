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

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
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
public class JobQueueServiceProcessParamAndJobParamTest {

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;

   @Autowired
   private CassandraServerBean serverBean;

   private UUID idJobWithParam;
   private UUID idJobWithJobParam;

   private void setJobWithParam(UUID idJob) {
      this.idJobWithParam = idJob;
   }

   private void setJobWithJobParam(UUID idJob) {
      this.idJobWithJobParam = idJob;
   }

   @Before
   public void before() {

      setJobWithParam(null);
      setJobWithJobParam(null);
   }

   @After
   public void after() throws Exception {

      // suppression du traitement de masse
      if (idJobWithParam != null) {

         jobQueueService.deleteJob(idJobWithParam);

      }
      // suppression du traitement de masse avec les job parameters
      if (idJobWithJobParam != null) {

         jobQueueService.deleteJob(idJobWithJobParam);

      }

      serverBean.resetData();
   }

   @Test
   public void startingJob_success() throws JobInexistantException,
         JobDejaReserveException, LockTimeoutException {

      idJobWithParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJobWithParam(idJobWithParam);

      idJobWithJobParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJobWithJobParam(idJobWithJobParam);

      jobQueueService.reserveJob(idJobWithParam, "hostname", new Date());

      jobQueueService.reserveJob(idJobWithJobParam, "hostname", new Date());

      Date dateDebutTraitement = new Date();
      jobQueueService.startingJob(idJobWithParam, dateDebutTraitement);

      jobQueueService.startingJob(idJobWithJobParam, dateDebutTraitement);

      // vérification de JobRequest
      JobRequest jobRequest = jobLectureService.getJobRequest(idJobWithParam);
      Assert.assertEquals("l'état est inattendu", JobState.STARTING, jobRequest
            .getState());
      Assert.assertEquals("la date de démarrage est inattendue",
            dateDebutTraitement, jobRequest.getStartingDate());

      // vérification de JobRequest
      JobRequest jobRequestJobParam = jobLectureService
            .getJobRequest(idJobWithJobParam);
      Assert.assertEquals("l'état est inattendu", JobState.STARTING,
            jobRequestJobParam.getState());
      Assert.assertEquals("la date de démarrage est inattendue",
            dateDebutTraitement, jobRequestJobParam.getStartingDate());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      List<JobHistory> histories = jobLectureService
            .getJobHistory(idJobWithParam);

      Assert.assertEquals("le nombre de message est inattendu", 3, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "DEMARRAGE DU JOB", histories.get(2).getTrace());

      // vérification de JobHistory
      List<JobHistory> historiesJobParam = jobLectureService
            .getJobHistory(idJobWithJobParam);

      Assert.assertEquals("le nombre de message est inattendu", 3,
            historiesJobParam.size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "DEMARRAGE DU JOB", historiesJobParam.get(2).getTrace());
   }

   private void createJobWithJobParam(UUID idJob) {

      Date dateCreation = new Date();

      Map<String, String> jobParam = new HashMap<String, String>();
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

   private void createJobWithParam(UUID idJob) {

      Date dateCreation = new Date();

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setParameters("Parameters");
      job.setClientHost("clientHost");
      job.setDocCount(100);
      job.setSaeHost("saeHost");
      job.setCreationDate(dateCreation);

      jobQueueService.addJob(job);
   }

   @Test
   public void startingJob_failure_jobInexistantException() {

      idJobWithParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJobWithParam);

      try {
         jobQueueService.startingJob(idJobWithParam, new Date());
         Assert.fail("Une exception JobInexistantException devrait être lever");
      } catch (JobInexistantException e) {

         Assert.assertEquals("l'identifiant du job est inattendu",
               idJobWithParam, e.getInstanceId());
         Assert
               .assertEquals("le message de l'exception est inattendu",
                     "Impossible de lancer, de modifier ou de réserver le traitement n°"
                           + idJobWithParam + " car il n'existe pas.", e
                           .getMessage());
      }
   }

   @Test
   public void startingJobWithJobParam_failure_jobInexistantException() {

      idJobWithJobParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJobWithJobParam);

      try {
         jobQueueService.startingJob(idJobWithJobParam, new Date());
         Assert.fail("Une exception JobInexistantException devrait être lever");
      } catch (JobInexistantException e) {

         Assert.assertEquals("l'identifiant du job est inattendu",
               idJobWithJobParam, e.getInstanceId());
         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible de lancer, de modifier ou de réserver le traitement n°"
                     + idJobWithJobParam + " car il n'existe pas.", e
                     .getMessage());
      }
   }

}
