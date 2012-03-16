package fr.urssaf.image.sae.pile.travaux.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.pile.travaux.dao.impl.SimpleJobRequestSerializer;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)      // Pour fermer le serveur zookeeper à la fin de la classe 
public class JobQueueDaoTest {

   @Autowired
   private JobQueueDao jobQueueDao;

   @Autowired
   private CassandraServerBean cassandraServer;
   
   @After
   public final void init() throws Exception {
      // Après chaque test, on reset les données de cassandra
      cassandraServer.resetData();
   }

   @Test
   public void getJobRequest_notExistant() {
      JobRequest jobRequest = jobQueueDao.getJobRequest(UUID.randomUUID());
      Assert.assertNull(jobRequest);
      
   }
   
   @Test
   public void getJobRequest_OK() throws Exception {
      Map<Integer, JobRequest> map = createCreatedJobRequestsForTest(1);
      JobRequest jobRequest1 = map.get(0);
      JobRequest jobRequest2 = jobQueueDao.getJobRequest(jobRequest1.getIdJob());
      assertEquals(jobRequest1, jobRequest2);
   }

  
   @Test
   public void saveJobRequest() {
      Map<Integer, JobRequest> map = createCreatedJobRequestsForTest(1);
      JobRequest jobRequest1 = map.get(0);
      Assert.assertEquals("ArchivageMasse", jobRequest1.getType());
   }

   @Test
   public void updateJobRequest() {
      Map<Integer, JobRequest> map = createCreatedJobRequestsForTest(1);
      JobRequest jobRequest1 = map.get(0);
      jobRequest1.setState(JobState.FAILURE);
      Date endingDate = new Date();
      jobRequest1.setEndingDate(endingDate);
      
      jobQueueDao.updateJobRequest(jobRequest1);
      JobRequest jobRequest2 = jobQueueDao.getJobRequest(jobRequest1.getIdJob());
      Assert.assertEquals(JobState.FAILURE, jobRequest2.getState());
      Assert.assertEquals(endingDate, jobRequest2.getEndingDate());
   }

   @Test
   public void getUnreservedJobRequestIterator() {
      // On crée 6 jobs
      Map<Integer, JobRequest> map = createCreatedJobRequestsForTest(6);
      Iterator<SimpleJobRequest> iterator = jobQueueDao.getUnreservedJobRequestIterator();
      int compteur = 0;
      while (iterator.hasNext()) {
         compteur++;
         SimpleJobRequest simpleJobRequest = iterator.next();
         String parameters = simpleJobRequest.getParameters(); 
         int index = Integer.parseInt(parameters.substring(parameters.length() - 1));
         assertEquals(map.get(index).getSimpleJob(), simpleJobRequest);
      }
      Assert.assertEquals(6, compteur);
      
      // On réserve 2 jobs
      jobQueueDao.reserveJobRequest(map.get(0), "myHostname", new Date());
      jobQueueDao.reserveJobRequest(map.get(5), "myHostname", new Date());
      
      // On ne devrait avoir plus que 4 jobs réservés
      Assert.assertEquals(4, getJobCount(jobQueueDao.getUnreservedJobRequestIterator()));      
   }

   @Test
   public void getNonTerminatedJobs() {
      // On crée 6 jobs
      Map<Integer, JobRequest> jobRequests = createCreatedJobRequestsForTest(6);
      // On en réserve 1
      jobQueueDao.reserveJobRequest(jobRequests.get(0), "myHostname", new Date());
      // On en passe un en terminé
      jobRequests.get(1).setEndingDate(new Date());
      jobRequests.get(1).setState(JobState.SUCCESS);
      jobRequests.get(1).setReservedBy("myHostname");
      jobQueueDao.updateJobRequest(jobRequests.get(1));
      // On en passe un en échec
      jobRequests.get(2).setEndingDate(new Date());
      jobRequests.get(2).setState(JobState.FAILURE);
      jobRequests.get(2).setReservedBy("myHostname");
      jobQueueDao.updateJobRequest(jobRequests.get(2));
      // On en démarre un
      jobRequests.get(3).setStartingDate(new Date());
      jobRequests.get(3).setState(JobState.STARTING);
      jobRequests.get(3).setReservedBy("myHostname");
      jobQueueDao.updateJobRequest(jobRequests.get(3));

      // On récupère les jobs non terminés
      List<JobRequest> runningJobs = jobQueueDao.getNonTerminatedJobs("myHostname");
      JacksonSerializer<JobRequest> jSlz = new JacksonSerializer<JobRequest>(JobRequest.class);      
      System.out.println("Running jobs : ");
      for (JobRequest jobRequest : runningJobs) {
         System.out.println(jSlz.toString(jobRequest));
      }
      Assert.assertEquals(2, runningJobs.size());
      
      // Pareil, mais on récupère seulement les infos principales des jobs (plus rapide)
      List<SimpleJobRequest> runningSimpleJobs = jobQueueDao.getNonTerminatedSimpleJobs("myHostname");
      System.out.println("Running simpleJobs : ");
      for (SimpleJobRequest simpleJobRequest : runningSimpleJobs) {
         System.out.println(SimpleJobRequestSerializer.get().toString(simpleJobRequest));
      }
      Assert.assertEquals(2, runningSimpleJobs.size());
   }

   @Test
   public void delete() {
      // On crée 6 jobs
      Map<Integer, JobRequest> jobRequests = createCreatedJobRequestsForTest(6);
      // On en réserve 1
      jobQueueDao.reserveJobRequest(jobRequests.get(0), "myHostname", new Date());
      // On en passe un en terminé
      jobRequests.get(1).setEndingDate(new Date());
      jobRequests.get(1).setState(JobState.SUCCESS);
      jobRequests.get(1).setReservedBy("myHostname");
      // On en supprime 3
      for (int i = 0; i< 3; i++) {
         jobQueueDao.deleteJobRequest(jobRequests.get(i));
         Assert.assertNull(jobQueueDao.getJobRequest(jobRequests.get(i).getIdJob()));
      }
      // On vérifie qu'aucun job n'est en cours
      Assert.assertEquals(0,jobQueueDao.getNonTerminatedJobs("myHostname").size());
      // On vérifie qu'il n'y a plus que 3 job en attente
      Assert.assertEquals(3, getJobCount(jobQueueDao.getUnreservedJobRequestIterator()));      
   }

   private int getJobCount(Iterator<SimpleJobRequest> iterator) {
      int compteur = 0;
      while (iterator.hasNext()) {
         compteur++;
         iterator.next();
      }
      return compteur;
   }
   
   private void assertEquals(JobRequest jobRequest1, JobRequest jobRequest2) {
      JacksonSerializer<JobRequest> jSlz = new JacksonSerializer<JobRequest>(JobRequest.class);
      Assert.assertEquals(jSlz.toString(jobRequest1) , jSlz.toString(jobRequest2));
   }
   private void assertEquals(SimpleJobRequest jobRequest1, SimpleJobRequest jobRequest2) {
      JacksonSerializer<SimpleJobRequest> jSlz = SimpleJobRequestSerializer.get();
      Assert.assertEquals(jSlz.toString(jobRequest1) , jSlz.toString(jobRequest2));
   }
   
   /**
    * Crée un certain nombre jobRequest, à l'état "CREATED", et les persiste
    * @param count      nombre de job à créer
    * @return Les jobRequest créés
    */
   private Map<Integer, JobRequest> createCreatedJobRequestsForTest(int count) {
      Map<Integer, JobRequest> map = getCreatedJobRequestsForTest(count);
      for (Map.Entry<Integer, JobRequest> entry : map.entrySet()) {
         jobQueueDao.saveJobRequest(entry.getValue());
      }
      return map;
   }

   /**
    * Crée un certain nombre jobRequest, à l'état "CREATED", sans les persister
    * @param count      nombre de job à créer
    * @return Les jobRequest créés
    */
   private Map<Integer, JobRequest> getCreatedJobRequestsForTest(int count) {
      Map<Integer, JobRequest> map = new HashMap<Integer, JobRequest>(count);
      for (int i = 0; i < count; i++) {
         JobRequest jobRequest = getCreatedJobRequestForTest(i);
         map.put(i, jobRequest);
      }
      return map;
   }

   /**
    * Crée un jobRequest, à l'état "CREATED", sans le persister
    * @param index      Un n° permettant de différencier les différents jobRequest
    * @return Le jobRequest créé
    */
   private JobRequest getCreatedJobRequestForTest(int index) {
      JobRequest jobRequest = new JobRequest();
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobRequest.setIdJob(idJob);
      jobRequest.setType("ArchivageMasse");
      jobRequest.setCreationDate(new Date());
      jobRequest.setParameters("sommaire=ecde:/toto.toto.com/sommaire.xml&idTraitement=" + index);
      jobRequest.setState(JobState.CREATED);
      return jobRequest;      
   }

   
}
