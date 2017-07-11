package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceTest {

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;

   @Autowired
   private CassandraServerBean cassandraServer;

   @After
   public final void init() throws Exception {
      // Après chaque test, on reset les données de cassandra
      cassandraServer.resetData();
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

   @Test
   public void delete_success_en_attente() {

      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      // création d'un job en attente
      createJob(idJob);

      // suppression du job
      jobQueueService.deleteJob(idJob);

      // vérification

      assertJobDelete(idJob);

      // vérification de JobsQueues

      Iterator<JobQueue> jobQueuesEnAttente = jobLectureService
            .getUnreservedJobRequestIterator();

      JobQueue jobQueueEnAttente = null;
      while (jobQueuesEnAttente.hasNext() && jobQueueEnAttente == null) {
         JobQueue jobQueueElement = jobQueuesEnAttente.next();
         if (jobQueueElement.getIdJob().equals(idJob)) {
            jobQueueEnAttente = jobQueueElement;
         }
      }

      Assert.assertNull("le job ne doit plus exister dans la file d'attente",
            jobQueueEnAttente);

   }

   @Test
   public void delete_success_en_reservation() throws JobDejaReserveException,
         JobInexistantException, LockTimeoutException {

      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      // création + réservation d'un job
      createJob(idJob);
      String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, new Date());

      // suppression du job
      jobQueueService.deleteJob(idJob);

      // vérification

      assertJobDelete(idJob);

      // vérification de JobsQueues

      Iterator<JobQueue> jobQueues = jobLectureService
            .getNonTerminatedSimpleJobs(reservedBy).iterator();

      JobQueue jobQueue = null;
      while (jobQueues.hasNext() && jobQueue == null) {
         JobQueue jobQueueElement = jobQueues.next();
         if (jobQueueElement.getIdJob().equals(idJob)) {
            jobQueue = jobQueueElement;
         }
      }

      Assert.assertNull(
            "le job ne doit plus exister dans la file d'execution de "
                  + reservedBy, jobQueue);
   }

   private void assertJobDelete(UUID idJob) {

      // vérification de JobRequest

      JobRequest job = jobLectureService.getJobRequest(idJob);
      Assert.assertNull("le job ne doit plus exister", job);

      // vérification de JobHistory
      List<JobHistory> jobHistory = jobLectureService.getJobHistory(idJob);

      Assert.assertTrue("le job ne doit plus avoir d'historique", jobHistory
            .isEmpty());

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
      Map<String,String> jobParam= new HashMap<String, String>();
      jobParam.put("parameters", parameters);
      
      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setJobParameters(jobParam);
      job.setCreationDate(new Date());

      jobQueueService.addJob(job);
      return idJob;
   }

}
