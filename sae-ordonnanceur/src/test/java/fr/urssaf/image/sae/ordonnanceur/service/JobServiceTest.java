package fr.urssaf.image.sae.ordonnanceur.service;

import java.net.UnknownHostException;
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

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;
import fr.urssaf.image.sae.ordonnanceur.util.HostUtils;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-job-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobServiceTest {
   
   /**
    * Le code du traitement
    */
   private static final String CODE_TRAITEMENT = "codeTraitement";

   /**
    * Prefixe pour la clef zookeeper.
    */
   private static final String PREFIXE_SEMAPHORE = "/Semaphore/";

   @Autowired
   private JobService jobService;

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;

   /**
    * Zookeeper curator.
    */
   @Autowired
   private CuratorFramework curator;

   private JobToCreate job;

   @Before
   public void before() throws UnknownHostException {

      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("jobTest");
      Map<String, String> jobParameters = new HashMap<String, String>();
      jobParameters.put(CODE_TRAITEMENT, CODE_TRAITEMENT);
      job.setJobParameters(jobParameters);;

      jobQueueService.addJob(job);

   }

   @After
   public void after() {

      // suppression du traitement
      if (job != null) {

         jobQueueService.deleteJob(job.getIdJob());
      }
   }

   @Test
   public void recupJobEnCours_success() throws UnknownHostException,
         JobDejaReserveException, JobInexistantException, LockTimeoutException {

      jobQueueService.reserveJob(job.getIdJob(), HostUtils.getLocalHostName(),
            new Date());

      // récupération des traitements en cours
      List<JobRequest> jobEnCours = jobService.recupJobEnCours();

      Assert.assertTrue("la liste des job en cours doit être non vide",
            !jobEnCours.isEmpty());

   }

   @Test
   public void recupJobsALancer_success() {

      // récupération des traitements à lancer
      List<JobQueue> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());

   }

   @Test
   public void reserveJob_success() throws JobDejaReserveException,
         JobInexistantException, UnknownHostException {

      UUID idJob = job.getIdJob();
      jobService.reserveJob(idJob);

      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);

      String reservingServer = jobRequest.getReservedBy();

      String serverName = HostUtils.getLocalHostName();

      Assert.assertEquals("le traitement doit être réservé", serverName,
            reservingServer);

      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.RESERVED, jobRequest.getState());

   }

   @Test
   public void reserveJob_failure_jobInexistantException()
         throws JobDejaReserveException {

      UUID idJob = job.getIdJob();

      // on s'assure que le traitement n'existe pas!
      jobQueueService.deleteJob(idJob);

      try {
         jobService.reserveJob(idJob);

         Assert
               .fail("une exception de type JobInexistantException doit être levée");

      } catch (JobInexistantException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible de lancer ou de réserver le traitement n°" + idJob
                     + " car il n'existe pas.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getInstanceId());

      }

   }

   @Test
   public void reserveJob_failure_jobDejaReserveException()
         throws JobDejaReserveException, UnknownHostException,
         JobInexistantException {

      UUID idJob = job.getIdJob();

      // réserve une premiere fois le traitement
      jobService.reserveJob(idJob);

      try {
         // réserve une seconde fois le même fois le traitement
         jobService.reserveJob(idJob);

         Assert
               .fail("une exception de type JobDejaReserveException doit être levée");

      } catch (JobDejaReserveException e) {

         String reservingServer = jobLectureService.getJobRequest(idJob)
               .getReservedBy();

         Assert.assertEquals("le message de l'exception est inattendu",
               "Le traitement n°" + idJob
                     + " est déjà réservé par le serveur '" + reservingServer
                     + "'.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getInstanceId());

         Assert.assertEquals("le nom du serveur est incorrect",
               reservingServer, e.getServer());

      }

   }

   @Test
   public void testFlag() throws JobInexistantException {

      jobService.updateToCheckFlag(job.getIdJob(), true, "test");

      JobRequest jobRequest = jobLectureService.getJobRequest(job.getIdJob());

      Assert.assertTrue("le flag d'erreur doit être à vrai", jobRequest
            .getToCheckFlag());

      Assert.assertEquals(
            "le champ description doit être rensigné correctement", "test",
            jobRequest.getToCheckFlagRaison());
   }

   @Test
   public void isJobCodeTraitementEnCoursOuFailure_success_true() {
      // récupération des traitements à lancer
      List<JobQueue> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());
      
      Assert.assertTrue(jobService
            .isJobCodeTraitementEnCoursOuFailure(jobsAlancer.get(0)));
   }

   @Test
   public void isJobCodeTraitementEnCoursOuFailure_success_false() {
      // récupération des traitements à lancer
      List<JobQueue> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());

      JobQueue job = jobsAlancer.get(0);

      Map<String, String> jobParams = new HashMap<String, String>(
            job.getJobParameters());

      job.setJobParameters(new HashMap<String, String>());

      Assert.assertFalse(jobService.isJobCodeTraitementEnCoursOuFailure(job));

      job.setJobParameters(jobParams);

      String semaphore = PREFIXE_SEMAPHORE + CODE_TRAITEMENT;

      // Création du mutex
      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator, semaphore);

      try {
         ZookeeperUtils.acquire(mutex, semaphore);

         Assert.assertFalse(jobService.isJobCodeTraitementEnCoursOuFailure(job));
      } finally {
         mutex.release();
      }

   }

}
