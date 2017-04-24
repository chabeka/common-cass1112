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
import fr.urssaf.image.sae.commons.exception.ParameterRuntimeException;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
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

   /**
    * Prefixe pour la clef cassandra.
    */
   private static final String PREFIXE_SEMAPHORE_JOB = "semaphore_";

   /**
    * Tag semaphore Job
    */
   private static final String SEMAPHORE_JOB_TAG = PREFIXE_SEMAPHORE_JOB
         + CODE_TRAITEMENT;

   /**
    * Tag semaphore Zookeeper
    */
   private static final String SEMAPHORE_ZOOKEEPER_TAG = PREFIXE_SEMAPHORE
         + CODE_TRAITEMENT;

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

   private JobToCreate jobEncours;

   @Before
   public void before() throws UnknownHostException, JobDejaReserveException,
         JobInexistantException, LockTimeoutException {

      // Job
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("jobTest");
      Map<String, String> jobParameters = new HashMap<String, String>();
      jobParameters.put(CODE_TRAITEMENT, CODE_TRAITEMENT);
      job.setJobParameters(jobParameters);;

      jobQueueService.addJob(job);

      // Job en cours de traitement
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      jobEncours = new JobToCreate();
      jobEncours.setIdJob(idJob);
      jobEncours.setType("jobTestEnCours");
      Map<String, String> jobParameters2 = new HashMap<String, String>();
      jobParameters2.put(CODE_TRAITEMENT, CODE_TRAITEMENT);
      jobEncours.setJobParameters(jobParameters2);

      jobQueueService.addJob(jobEncours);
   }

   @After
   public void after() {

      // suppression du traitement
      if (job != null) {
         jobQueueService.deleteJobAndSemaphoreFromJobsQueues(job.getIdJob(),
               CODE_TRAITEMENT);
      }

      if (jobEncours != null) {
         jobQueueService.deleteJobAndSemaphoreFromJobsQueues(
               jobEncours.getIdJob(), CODE_TRAITEMENT);
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
               "Impossible de lancer, de modifier ou de réserver le traitement n°" + idJob
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
   public void isJobCodeTraitementEnCoursOuFailure_true() {
      // récupération des traitements à lancer
      List<JobQueue> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());
      
      JobQueue job = jobsAlancer.get(0);

      try {
         jobQueueService.reserveJob(jobEncours.getIdJob(),
 SEMAPHORE_JOB_TAG,
               new Date());

         jobQueueService.startingJob(jobEncours.getIdJob(), new Date());
      } catch (Exception e) {
         Assert.fail("Erreur non permise : " + e.getMessage());
      }

      Assert.assertTrue(jobService.isJobCodeTraitementEnCoursOuFailure(job));

   }

   @Test
   public void isJobCodeTraitementEnCoursOuFailure_false() {
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

      Assert.assertFalse(jobService.isJobCodeTraitementEnCoursOuFailure(job));
   }

   @Test(expected = OrdonnanceurRuntimeException.class)
   public void isJobCodeTraitementEnCoursOuFailure_OrdonnanceurRuntimeException() {
      // récupération des traitements à lancer
      List<JobQueue> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());

      JobQueue job = jobsAlancer.get(0);

      try {
         jobQueueService.reserveJob(jobEncours.getIdJob(),
 SEMAPHORE_JOB_TAG,
               new Date());
         jobQueueService.reserveJob(job.getIdJob(),
 SEMAPHORE_JOB_TAG,
               new Date());
      } catch (Exception e) {
         Assert.fail("Erreur non permise : " + e.getMessage());
      }

      jobService.isJobCodeTraitementEnCoursOuFailure(job);
   }

   @Test
   public void confirmerJobALancer_success() {// récupération des traitements à
                                              // lancer
      List<JobQueue> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());

      JobQueue job = jobsAlancer.get(0);

      jobService.reserverCodeTraitementJobALancer(job);

      List<JobRequest> jobRequestList = jobLectureService
            .getNonTerminatedJobs(SEMAPHORE_JOB_TAG);

      Assert.assertNotNull(jobRequestList);
      Assert.assertFalse(jobRequestList.isEmpty());
      Assert.assertTrue(jobRequestList.size() == 1);

      JobRequest jobTrouve = jobRequestList.get(0);

      Assert.assertEquals(job.getIdJob(), jobTrouve.getIdJob());
   }

   @Test
   public void confirmerJobALancer_failed() {// récupération des traitements à
                                             // lancer
      List<JobQueue> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());

      Assert.assertEquals(2, jobsAlancer.size());

      // 2 jobs peuvent etre lancés
      JobQueue job = jobsAlancer.get(0);
      JobQueue jobEnCours = jobsAlancer.get(1);

      // On reserve 1 job en positionnant le semaphore manuellement
      try {
         jobQueueService.reserveJob(job.getIdJob(), SEMAPHORE_JOB_TAG,
               new Date());

         jobQueueService.startingJob(job.getIdJob(), new Date());
      } catch (Exception e) {
         Assert.fail("Erreur non permise : " + e.getMessage());
      }

      // On essaie de reserver l'autre job et il n'y a pas d'erreur si la
      // reservation n'est pas possible.
      jobService.reserverCodeTraitementJobALancer(jobsAlancer.get(1));

      // Récupération des job en cours pour le semaphore
      List<JobRequest> jobRequestList = jobLectureService
            .getNonTerminatedJobs(SEMAPHORE_JOB_TAG);

      Assert.assertNotNull(jobRequestList);
      Assert.assertFalse(jobRequestList.isEmpty());
      Assert.assertTrue(jobRequestList.size() == 1);

      JobRequest jobTrouve = jobRequestList.get(0);

      // Job en cours est bien le premier job reservé.
      Assert.assertFalse(jobEnCours.getIdJob().toString()
            .equals(jobTrouve.getIdJob().toString()));
   }

   @Test(expected = ParameterRuntimeException.class)
   public void confirmerJobALancer_ParameterRuntimeException() {
      // Récupération des traitements à lancer
      List<JobQueue> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());

      JobQueue job = jobsAlancer.get(0);



      // Création du mutex
      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator,
            SEMAPHORE_ZOOKEEPER_TAG);

      try {
         ZookeeperUtils.acquire(mutex, SEMAPHORE_ZOOKEEPER_TAG);

         jobService.reserverCodeTraitementJobALancer(job);

      } finally {
         mutex.release();
      }

   }

}
