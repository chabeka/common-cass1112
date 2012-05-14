package fr.urssaf.image.commons.exemples.cassandra.grossecolonne;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.exemples.cassandra.config.CassandraEtZookeeperConfig;
import fr.urssaf.image.commons.exemples.cassandra.grossecolonne.service.GrosseColonneService;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;

public class GrosseColonneServiceTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(GrosseColonneServiceTest.class);
   
   
   private static CassandraEtZookeeperConfig connexionConfig;
   private static CuratorFramework zkClient;
   private static CassandraClientFactory cassandraClientFactory;
   
   private GrosseColonneService leService = new GrosseColonneService();
   
   
   private static void initConfig() {
      
      // Config ds4/int9
//      String zookeeperHosts = "cer69-ds4int.cer69.recouv:2181";
//      String cassandraHosts = "cer69imageint9.cer69.recouv:9160";
      
      
      // Config saeint1
//      String zookeeperHosts = "cer69-saeint1.cer69.recouv:2181";
//      String cassandraHosts = "cer69-saeint1.cer69.recouv:9160";
      
      
      // Config hwidevsae 
      String zookeeperHosts = "hwi69devsaeapp1.cer69.recouv,hwi69devsaeapp2.cer69.recouv";
      String cassandraHosts = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      
      
      
      // Création de l'objet de config
      
      connexionConfig = new CassandraEtZookeeperConfig();
      
      connexionConfig.setZookeeperHosts(zookeeperHosts);
      connexionConfig.setZookeeperNamespace("Titi");
      
      connexionConfig.setCassandraHosts(cassandraHosts);
      connexionConfig.setCassandraKeySpace(GrosseColonneService.KEYSPACE_NAME);
      connexionConfig.setCassandraUserName("root");
      connexionConfig.setCassandraPassword("regina4932");
      
      
   }
   
   
   @BeforeClass
   public static void beforeClass() {
      
      // Initialisation des paramètres de configuration
      initConfig();
      
      
      // Connexion à Zookeeper
      try {
         zkClient = ZookeeperClientFactory.getClient(connexionConfig
               .getZookeeperHosts(), connexionConfig.getZookeeperNamespace());
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      
      
      // Connexion à Cassandra
      CassandraServerBean cassandraServer = new CassandraServerBean();
      cassandraServer.setHosts(connexionConfig.getCassandraHosts());
      cassandraServer.setStartLocal(false);
      cassandraServer.setDataSet(null);
      try {
         cassandraClientFactory = new CassandraClientFactory(
               cassandraServer, connexionConfig.getCassandraKeySpace(),
               connexionConfig.getCassandraUserName(), connexionConfig
                     .getCassandraPassword());
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
      
   }
   
   
   @AfterClass
   public static void afterClass() {
      
      // Fermeture de la connexion à Zookeeper
      if (zkClient != null) {
         zkClient.close();
      }
      
   }

   
   @Test
   @Ignore
   public void creationDuKeyspaceEtDesCF() {
      
      leService.createKeyspaceEtColumnFamily(connexionConfig);
      
   }
   
   
   /**
    * Pour pouvoir exécuter ce test sur un volume de 500 000 UUID, il faut
    * penser à augmenter la mémoire de la JVM dans le lancement du JUnit<br>
    * <br>
    * Modèle : <br>
    * <br>
    * -Xms500m -Xmx500m
    * 
    * @throws Throwable
    */
   @Test
   @Ignore
   public void insererGrosseColonne() throws Throwable {
      
      LOGGER.debug("Début");
      
      Long idJob = 14L;
      
      int nbUUID = 500000;
      
      try {
         
         leService.ecrireExecutionContext(
               cassandraClientFactory.getKeyspace(), 
               idJob, 
               nbUUID);
         
         LOGGER.debug("Fin sans erreur");
         
      } catch (Throwable throwable) {
         
         LOGGER.debug("Fin avec erreur");
         throw throwable;
         
      }
      
   }
   
}
