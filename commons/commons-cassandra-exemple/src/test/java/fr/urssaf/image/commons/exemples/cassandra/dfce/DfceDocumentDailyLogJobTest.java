package fr.urssaf.image.commons.exemples.cassandra.dfce;

import java.io.IOException;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.exemples.cassandra.dfce.dao.JobsDao;
import fr.urssaf.image.commons.exemples.cassandra.dfce.modele.Job;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.CassandraEtZookeeperConfig;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;

/**
 * TODO: Mettre le code du test setRunningFalse dans un service de la partie main<br>
 * <br>
 * Code posé à la rache pour résoudre un problème survenu lors de l'intégration interne du SAE<br>
 */
public class DfceDocumentDailyLogJobTest {

   
   private static CassandraEtZookeeperConfig connexionConfig;
   private static CuratorFramework zkClient;
   private static CassandraClientFactory cassandraClientFactory;
   
   private JobsDao jobsDao = new JobsDao();
   
   private static final String NOM_JOB = "DOCUMENT_DAILY_LOG_JOB";
   
   
   private static void initConfig() {
      
      // Config ds4/int9
      String zookeeperHosts = "cer69-ds4int.cer69.recouv:2181";
      String cassandraHosts = "cer69imageint9.cer69.recouv:9160";
      
      
      // Config saeint1
//      String zookeeperHosts = "cer69-saeint1.cer69.recouv:2181";
//      String cassandraHosts = "cer69-saeint1.cer69.recouv:9160";
      
      
      // Config hwidevsae 
//      String zookeeperHosts = "hwi69devsaeapp1.cer69.recouv,hwi69devsaeapp2.cer69.recouv";
//      String cassandraHosts = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      
      
      
      // Création de l'objet de config
      
      connexionConfig = new CassandraEtZookeeperConfig();
      
      connexionConfig.setZookeeperHosts(zookeeperHosts);
      connexionConfig.setZookeeperNamespace("SAE");
      
      connexionConfig.setCassandraHosts(cassandraHosts);
      connexionConfig.setCassandraKeySpace("Docubase");
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
   
   
   
   private void dumpJob(
         ColumnFamilyTemplate<String, String> cfTemplate) {
      
      // Requêtage dans Cassandra
      ColumnFamilyResult<String, String> result = cfTemplate.queryColumns(NOM_JOB);
      
      // Conversion en objet du modèle
      Job job = jobsDao.createModeleFromResult(result);
      
      // sysout
      System.out.println("TYPE_ATTRIBUTE_NAME : " + job.getTypeAttributeName());
      System.out.println("key : " + job.getKey());
      System.out.println("lastSuccessfullRunDate : " + job.getLastSuccessfullRunDate());
      System.out.println("launchDate : " + job.getLaunchDate());
      System.out.println("running : " + job.isRunning());
      
   }
   
   
   /**
    * Affiche les colonnes du job DFCE DOCUMENT_DAILY_LOG_JOB
    */
   @Test
   @Ignore
   public void dumpDocumentDailyLogJob() {
      
      ColumnFamilyTemplate<String, String> cfTemplate = jobsDao.createCFTemplate(
            cassandraClientFactory.getKeyspace());
      
      dumpJob(cfTemplate);
      
   }
   
   
   /**
    * Met à <code>false</code> le flag "running" du job DFCE "DOCUMENT_DAILY_LOG_JOB"<br>
    * <br>
    * Permet de résoudre le cas suivant :<br>
    * <br>
    * <ul>
    *    <li>on demande à DFCE de lancer le job de création des journaux du cycle
    *    de vie des archives</li>
    *    <li>dans la base Cassandra, dans le keyspace "Docubase", DFCE écrit la colonne 
    *    "running" à true pour la clé DOCUMENT_DAILY_LOG_JOB de la column family "Jobs"</li>
    *    <li>DFCE est arrêté brutalement</li>
    *    <li>le flag running reste à true, DFCE croit que le job est toujours en cours
    *    lorsqu'on lui demande de le ré-exécuter</li>
    *    <li>Résultat : il n'est plus possible de lancer le job de création des journaux
    *    du cycle de vie des archives</li>
    * </ul>
    */
   @Test
   @Ignore
   public void setRunningFalse() throws InterruptedException {
      
      ColumnFamilyTemplate<String, String> cfTemplate = jobsDao.createCFTemplate(
            cassandraClientFactory.getKeyspace());
      
      ColumnFamilyUpdater<String, String> cfUpdater = cfTemplate.createUpdater(NOM_JOB);
      
      jobsDao.ecritColonneRunning(cfUpdater, false);
      
      cfTemplate.update(cfUpdater);
      
      Thread.sleep(2000);
      
      dumpJob(cfTemplate);
      
   }
   
   
   
   
   
}
