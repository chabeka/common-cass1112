package fr.urssaf.image.commons.exemples.cassandra.piletravaux;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;

import org.junit.Ignore;
import org.junit.Test;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobHistoryDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobQueuesDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobRequestDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobRequest;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobToCreate;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.service.PileTravauxEcritureService;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.service.PileTravauxLectureService;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;


public class PileTravauxTest {

   private String uuidTest = "148e7ab0-8ea7-11e1-ac4c-005056c00008";
   
   
   @Test
   @Ignore
   public void leTest() {
      
      // Les paramètres
      CassandraEtZookeeperConfig connexionConfig = new CassandraEtZookeeperConfig();
      // connexionConfig.setZookeeperHosts("cer69-ds4int.cer69.recouv:2181");
      connexionConfig.setZookeeperHosts("cer69-saeint1.cer69.recouv:2181");
      connexionConfig.setZookeeperNamespace("PMA1");
      connexionConfig.setCassandraHosts("cer69-saeint1.cer69.recouv:9160");
      // connexionConfig.setCassandraHosts("cer69imageint9.cer69.recouv:9160");
      connexionConfig.setCassandraUserName("root");
      connexionConfig.setCassandraPassword("regina4932");
      connexionConfig.setCassandraKeySpace("PMA1");
      
      // Connexion à Zookeeper
      CuratorFramework zkClient;
      try {
         zkClient = ZookeeperClientFactory.getClient(connexionConfig
               .getZookeeperHosts(), connexionConfig.getZookeeperNamespace());
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      try {

         // Connexion à Cassandra
         CassandraServerBean cassandraServer = new CassandraServerBean();
         cassandraServer.setHosts(connexionConfig.getCassandraHosts());
         cassandraServer.setStartLocal(false);
         cassandraServer.setDataSet(null);
         CassandraClientFactory cassandraClientFactory;
         try {
            cassandraClientFactory = new CassandraClientFactory(
                  cassandraServer, connexionConfig.getCassandraKeySpace(),
                  connexionConfig.getCassandraUserName(), connexionConfig
                        .getCassandraPassword());
         } catch (InterruptedException e) {
            throw new RuntimeException(e);
         }

         // TODO Appel de la méthode à tester
         truncateAll(cassandraClientFactory.getKeyspace());
         // ajouterNouveauJob(cassandraClientFactory.getKeyspace(), zkClient);
         // lireJob(cassandraClientFactory.getKeyspace(), zkClient);
         // reserverUnJob(cassandraClientFactory.getKeyspace(), zkClient);
         // lireJob(cassandraClientFactory.getKeyspace(), zkClient);
         // passerJobAetatEnCours(cassandraClientFactory.getKeyspace(), zkClient);
         // lireJob(cassandraClientFactory.getKeyspace(), zkClient);
         // renseignerPidJob(cassandraClientFactory.getKeyspace(), zkClient);
         // lireJob(cassandraClientFactory.getKeyspace(), zkClient);
         // passerJobAetatTermine(cassandraClientFactory.getKeyspace(), zkClient);
         // lireJob(cassandraClientFactory.getKeyspace(), zkClient);
         // ajouterTrace(cassandraClientFactory.getKeyspace(), zkClient);
         // lireJob(cassandraClientFactory.getKeyspace(), zkClient);
         

      } finally {
         // Fermeture de la connexion à Zookeeper
         if (zkClient != null) {
            zkClient.close();
         }
      }
      
   }
   
   
   
   private PileTravauxEcritureService createServiceEcriture(
         Keyspace keyspace,
         CuratorFramework zkClient) {
      
      return new PileTravauxEcritureService(
            keyspace, 
            zkClient,
            new JobRequestDao(),
            new JobQueuesDao(),
            new JobHistoryDao());
      
   }
   
   
   private PileTravauxLectureService createServiceLecture(
         Keyspace keyspace,
         CuratorFramework zkClient) {
      
      return new PileTravauxLectureService(
            keyspace, 
            zkClient,
            new JobRequestDao(),
            new JobQueuesDao(),
            new JobHistoryDao());
      
   }
   
   
   protected void truncateAll(Keyspace keyspace) {
      try {
      
      truncate(keyspace,"JobHistory");
      truncate(keyspace,"JobRequest");
      truncate(keyspace,"JobsQueue");
      
      } catch(Exception ex) {
         throw new RuntimeException(ex);
      }
      
   }
   
   
   private void truncate(
         Keyspace keyspace,
         String cfName) throws Exception {
      BytesArraySerializer  bytesSerializer = BytesArraySerializer.get();
      CqlQuery<byte[],byte[],byte[]> cqlQuery = new CqlQuery<byte[],byte[],byte[]>(keyspace, bytesSerializer, bytesSerializer, bytesSerializer);
      String query = "truncate " + cfName;
      cqlQuery.setQuery(query);
      cqlQuery.execute();
   }
   
   
   protected void ajouterNouveauJob(
         Keyspace keyspace,
         CuratorFramework zkClient) {
      
      // Valeurs du test
      JobToCreate jobToCreate = new JobToCreate();
      jobToCreate.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
      jobToCreate.setType("capture_masse");
      jobToCreate.setParameters("ecde://cnp69devecde.cer69.recouv/SAE_INTEGRATION/20110822/CaptureMasse-200/sommaire.xml");
      jobToCreate.setCreationDate(new Date());
      jobToCreate.setSaeHost("leSaeHost");
      jobToCreate.setClientHost("leClientHost");
      jobToCreate.setDocCount(10);
      System.out.println("UUID du job : " + jobToCreate.getIdJob());
      
      // Appel du service
      PileTravauxEcritureService ecritureService = createServiceEcriture(keyspace, zkClient);
      ecritureService.ajouterNouveauJob(jobToCreate);
      
   }
   
   
   protected void reserverUnJob(
         Keyspace keyspace,
         CuratorFramework zkClient) {
      
      // Valeurs du test
      UUID idJob = UUID.fromString(uuidTest);
      String reservedBy = "moi";
      Date reservationDate = new Date();
      
      // Appel du service
      PileTravauxEcritureService ecritureService = createServiceEcriture(keyspace, zkClient);
      ecritureService.reserverUnJob(idJob, reservedBy, reservationDate);
      
   }
   
   
   protected void passerJobAetatEnCours(
         Keyspace keyspace,
         CuratorFramework zkClient) {
      
      
      // Valeurs du test
      UUID idJob = UUID.fromString(uuidTest);
      Date startingDate = new Date();
      
      // Appel du service
      PileTravauxEcritureService ecritureService = createServiceEcriture(keyspace, zkClient);
      ecritureService.passerJobAetatEnCours(idJob, startingDate);
      
   }
   
   
   protected void passerJobAetatTermine(
         Keyspace keyspace,
         CuratorFramework zkClient) {
      
      
      // Valeurs du test
      UUID idJob = UUID.fromString(uuidTest);
      Date endingDate = new Date();
      boolean success = true;
      String message = "Toto";
      
      // Appel du service
      PileTravauxEcritureService ecritureService = createServiceEcriture(keyspace, zkClient);
      ecritureService.passerJobAetatTermine(idJob, endingDate, success, message);
      
   }
   
   
   protected void renseignerPidJob(
         Keyspace keyspace,
         CuratorFramework zkClient) {
    
      // Valeurs du test
      UUID idJob = UUID.fromString(uuidTest);
      Integer pid = 1234;
      
      // Appel du service
      PileTravauxEcritureService ecritureService = createServiceEcriture(keyspace, zkClient);
      ecritureService.renseignerPidJob(idJob, pid);
      
   }
   
   
   protected void ajouterTrace(
         Keyspace keyspace,
         CuratorFramework zkClient) {
    
      // Valeurs du test
      UUID idJob = UUID.fromString(uuidTest);
      String messageTrace = "UNE TRACE";
      
      // Appel du service
      PileTravauxEcritureService ecritureService = createServiceEcriture(keyspace, zkClient);
      ecritureService.ajouterTrace(idJob, messageTrace);
      
   }

   
   
   protected void lireJob(
         Keyspace keyspace,
         CuratorFramework zkClient) {
      
      // Valeurs du test
      UUID idJob = UUID.fromString(uuidTest);
      
      // Appel du service
      PileTravauxLectureService lectureService = createServiceLecture(keyspace, zkClient);
      JobRequest job = lectureService.lireJob(idJob);
      
      // sysout des infos
      System.out.println("clientHost = " + job.getClientHost());
      System.out.println("creationDate = " + job.getCreationDate());
      System.out.println("docCount = " + job.getDocCount());
      System.out.println("endingDate = " + job.getEndingDate());
      System.out.println("idJob = " + job.getIdJob());
      System.out.println("message = " + job.getMessage());
      System.out.println("parameters = " + job.getParameters());
      System.out.println("pid = " + job.getPid());
      System.out.println("reservationDate = " + job.getReservationDate());
      System.out.println("reservedBy = " + job.getReservedBy());
      System.out.println("saeHost = " + job.getSaeHost());
      System.out.println("startingDate = " + job.getStartingDate());
      System.out.println("state = " + job.getState());
      System.out.println("type = " + job.getType());
      
   }
   
   
   
}
