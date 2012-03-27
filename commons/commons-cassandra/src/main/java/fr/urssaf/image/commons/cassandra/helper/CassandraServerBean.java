package fr.urssaf.image.commons.cassandra.helper;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

import org.cassandraunit.DataLoader;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * Classe utilitaire facilitant la création d'un serveur cassandra local
 * par un bean spring.
 *
 */
public class CassandraServerBean implements InitializingBean, DisposableBean  {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraServerBean.class);
   private static final int WAIT_MAX_TRY = 5;
   private static final long WAIT_MS = 1000;
   private static final String TEST_CLUSTER_NAME = "TestCluster";
   private String dataSet;
   private boolean startLocal= false;
   private String hosts = null;
   private Cluster testCluster = null;
   
   /**
    * Indique quel jeu de données cassandraUnit doit être utilisé lors
    * de l'initialisation du serveur cassandra
    * @param dataSet Jeu de données
    */
   public final void setDataSet(String dataSet) {
      this.dataSet = dataSet;
   }
   
   /**
    * Indique s'il faut lancer un serveur cassandra local
    * @param startLocal vrai s'il faut lancer un serveur local
    */
   public final void setStartLocal(boolean startLocal) {
      this.startLocal = startLocal;
   }
   
   /**
    * @return vrai si le serveur cassandra est lancé localement
    */
   public final boolean getStartLocal() {
      return this.startLocal;
   }
   
   @Override
   public final void destroy() throws Exception {
      // Pas besoin d'arrêter le serveur
   }

   @Override
   public final void afterPropertiesSet() throws Exception {
      LOG.debug("CassandraServerBean : startLocal={} - dataSet={}", startLocal, dataSet);
      resetData(dataSet);
   }

   /**
    * Réinitialise les données de la base cassandra locale, avec le jeu de données
    * utilisé initialement lors de la création du serveur
    * @throws Exception    Une erreur est survenue
    */
   public final void resetData() throws Exception {
      resetData(dataSet);
   }
   
   /**
    * Réinitialise les données de la base cassandra locale
    * @param newDataSets    Jeu(x) de données à utiliser
    * @throws Exception    Une erreur est survenue
    */
   public final void resetData(String... newDataSets) throws Exception {
      if (!startLocal) return;
      LOG.debug("CassandraServerBean : reseting data...");
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
      // On attend que le serveur soit prêt
      waitForServer();
      
      for (String newDataSet : newDataSets) {
         if (newDataSet!=null && !newDataSet.isEmpty()) {
            DataLoader dataLoader = new DataLoader(TEST_CLUSTER_NAME, "localhost:9171");
            ClassPathXmlDataSet set = new ClassPathXmlDataSet(newDataSet);
            dataLoader.load(set);
         }
      }
   }

   /**
    * Il arrive que le serveur cassandra local mette du temps avant d'être opérationnel.
    * Cette méthode fait en sorte d'attendre jusqu'à ce qu'il soit opérationnel
    * @throws InterruptedException : on a été interrompu
    * 
    */
   private void waitForServer() throws InterruptedException {
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(getHosts());
      hostConfigurator.setMaxActive(1);
      Cluster cluster = getTestCluster();
      for (int i=0; i < WAIT_MAX_TRY; i++) {
         try {
            cluster.describeKeyspaces();
            break;
         }
         catch (Exception e) {
            LOG.debug("CassandraServerBean : waiting for server (" + i + ")...");            
            Thread.sleep(WAIT_MS);
         }
      }
   }
   
   private Cluster getTestCluster() {
      if (testCluster == null) {
         CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(getHosts());
         hostConfigurator.setMaxActive(1);
         testCluster = HFactory.getOrCreateCluster(TEST_CLUSTER_NAME, hostConfigurator);
      }
      return testCluster;
   }
   
   /**
    * Arrête le cluster (partie cliente) de test
    */
   public final void shutdownTestCluster() {
      if (testCluster != null) HFactory.shutdownCluster(testCluster);
   }
   
   
   /**
    * Dans le cas d'un cassandra zookeeper non local, il s'agit de la chaîne de connexion
    * @param hosts    Chaîne de connexion (ex : "toto.toto.com:9160,titi.titi.com:9160")
    */
   public final void setHosts(String hosts) {
      this.hosts = hosts;
   }

   /**
    * Renvoie la chaîne de connexion au serveur cassandra
    * @return chaîne de connexion
    */
   public final String getHosts() {
      if (startLocal) {
         // Petite bidouille : on met le serveur localhost 3 fois : ça permet de tenter 3 fois
         // l'opération si elle échoue la 1ere fois (ça arrive lorsque le serveur cassandra local
         // ne se lance pas assez rapidement)
         return "localhost:9171,localhost:9171,localhost:9171"; 
      }
      else {
         return hosts;
      }
   }

}
