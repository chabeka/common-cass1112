package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.After;
import org.junit.Assert;
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
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceTest {

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private JobLectureService jobLectureService;

   @After
   public final void init() throws Exception {
      // Après chaque test, on reset les données de cassandra
      cassandraServer.resetData();
   }

   @Test
   public void addJob_success() {
      // On crée un job
      UUID id = addJobForTest(1);
      // On vérifie qu'on le retrouve
      JobRequest jobRequest = jobLectureService.getJobRequest(id);
      Assert.assertNotNull(jobRequest);
   }

   @Test
   public void reserveJob() throws Exception {
      // On crée un job
      UUID id = addJobForTest(1);
      // On le réserve
      jobQueueService.reserveJob(id, "hostname", new Date());

      // On vérifie qu'il est bien réservé
      JobRequest jobRequest = jobLectureService.getJobRequest(id);
      Assert.assertEquals("hostname", jobRequest.getReservedBy());
      Assert.assertNotNull(jobRequest.getReservationDate());

      // On vérifie qu'on ne peut plus le réserver
      try {
         jobQueueService.reserveJob(id, "hostname2", new Date());
      } catch (JobDejaReserveException e) {
         // Normal
         return;
      }
      Assert.fail("On ne devrait pas pouvoir réserver 2 fois le même job");
   }

   @Test
   public void concurrentReservation() throws Exception {
      // On crée 5 jobs
      UUID[] uuids = new UUID[5];
      for (int i = 0; i < 5; i++) {
         uuids[i] = addJobForTest(i);
      }
      // On crée 10 threads
      Map<UUID, UUID> resevationMap = new ConcurrentHashMap<UUID, UUID>();
      SimpleThread[] threads = new SimpleThread[10];
      for (int i = 0; i < 10; i++) {
         threads[i] = new SimpleThread(uuids, resevationMap);
         threads[i].start();
      }
      // On attend la fin d'exécution de toutes les threads
      for (int i = 0; i < 10; i++) {
         threads[i].join();
      }
      // On vérifie qu'il y a eu 5 jobs de réservé, ni plus ni moins
      Assert.assertEquals(5, resevationMap.size());
   }

   private class SimpleThread extends Thread {

      Map<UUID, UUID> map;
      UUID[] uuids;

      public SimpleThread(UUID[] uuids, Map<UUID, UUID> reservationMap) {
         super();
         this.map = reservationMap;
         this.uuids = uuids;
      }

      public void run() {
         for (int i = 0; i < 5; i++) {
            try {
               jobQueueService.reserveJob(uuids[i], "hostname" + this.getId(),
                     new Date());
            } catch (JobDejaReserveException e) {
               // rien
            } catch (JobInexistantException e) {
               e.printStackTrace();
               // On génère une erreur
               map.put(UUID.randomUUID(), UUID.randomUUID());
            } catch (LockTimeoutException e) {
               e.printStackTrace();
               // On génère une erreur
               map.put(UUID.randomUUID(), UUID.randomUUID());
            }
            map.put(uuids[i], uuids[i]);
         }
      }
   }

   @Test(expected = JobInexistantException.class)
   public void reserveJob_failure_jobInexistantException() throws Exception {
      jobQueueService.reserveJob(UUID.randomUUID(), "hostname", new Date());
   }

   @Test
   public void startingJob_success() throws Exception {
      // On crée un job
      UUID id = addJobForTest(1);
      // On le réserve
      jobQueueService.reserveJob(id, "hostname", new Date());
      // On le lance
      jobQueueService.startingJob(id, new Date());
      // On vérifie qu'il est bien lancé
      JobRequest jobRequest = jobLectureService.getJobRequest(id);
      Assert.assertEquals("hostname", jobRequest.getReservedBy());
      Assert.assertNotNull(jobRequest.getStartingDate());
      Assert.assertEquals(JobState.STARTING, jobRequest.getState());
   }

   @Test
   public void endingJob_success() throws Exception {
      // On crée un job
      UUID id = addJobForTest(1);
      // On le réserve
      jobQueueService.reserveJob(id, "hostname", new Date());
      // On le lance
      jobQueueService.startingJob(id, new Date());
      // On le termine
      jobQueueService.endingJob(id, true, new Date());
      // On vérifie qu'il est bien terminé
      JobRequest jobRequest = jobLectureService.getJobRequest(id);
      Assert.assertEquals("hostname", jobRequest.getReservedBy());
      Assert.assertNotNull(jobRequest.getEndingDate());
      Assert.assertEquals(JobState.SUCCESS, jobRequest.getState());
   }

   @Test
   public void endingJob_failure() throws Exception {
      // On crée un job
      UUID id = addJobForTest(1);
      // On le réserve
      jobQueueService.reserveJob(id, "hostname", new Date());
      // On le lance
      jobQueueService.startingJob(id, new Date());
      // On le termine
      jobQueueService.endingJob(id, false, new Date());
      // On vérifie qu'il est bien terminé, mais en erreur
      JobRequest jobRequest = jobLectureService.getJobRequest(id);
      Assert.assertEquals("hostname", jobRequest.getReservedBy());
      Assert.assertNotNull(jobRequest.getEndingDate());
      Assert.assertEquals(JobState.FAILURE, jobRequest.getState());
   }

   @Test
   public void updateToCheckFlag_success() throws JobInexistantException {

      String raison = "raison de la vérification";

      // On crée un job
      UUID idJob = addJobForTest(1);

      // on renseigne son checkFlag
      jobQueueService.updateToCheckFlag(idJob, true,
            "raison de la vérification");

      JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("la propriété toCheckFlag est inattendu", true,
            jobRequest.getToCheckFlag());
      Assert.assertEquals("la propriété toCheckFlagRaison devrait être à true",
            raison, jobRequest.getToCheckFlagRaison());

   }

   /**
    * Crée un job
    * 
    * @param index
    *           Un n° permettant de différencier les différents jobs
    * @return L'id du job créé
    */
   private UUID addJobForTest(int index) {
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      String parameters = "sommaire=ecde:/toto.toto.com/sommaire.xml&idTraitement="
            + index;

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setParameters(parameters);
      job.setCreationDate(new Date());

      jobQueueService.addJob(job);
      return idJob;
   }

}
