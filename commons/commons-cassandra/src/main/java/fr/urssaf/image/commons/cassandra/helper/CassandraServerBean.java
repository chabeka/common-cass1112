package fr.urssaf.image.commons.cassandra.helper;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.commons.lang.StringUtils;
import org.cassandraunit.DataLoader;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.cassandraunit.model.ColumnFamilyModel;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.model.MemoryDataSet;

/**
 * Classe utilitaire facilitant la création d'un serveur cassandra local par un
 * bean spring.
 * 
 */
public class CassandraServerBean implements InitializingBean, DisposableBean {

   private static final Logger LOG = LoggerFactory
         .getLogger(CassandraServerBean.class);
   private static final int WAIT_MAX_TRY = 12;
   private static final long WAIT_MS = 1000;
   private static final String TEST_CLUSTER_NAME = "TestCluster";
   private String[] dataSets;
   private boolean startLocal = false;
   private String hosts = null;
   private Cluster testCluster = null;

   /**
    * Indique quel jeu de données cassandraUnit doit être utilisé lors de
    * l'initialisation du serveur cassandra
    * 
    * @param dataSet
    *           Jeu de données
    */
   public final void setDataSet(String dataSet) {

      // Il peut y avoir plusieurs dataSets séparés par des ;
      this.dataSets = StringUtils.split(dataSet, ';');

   }

   /**
    * Indique s'il faut lancer un serveur cassandra local
    * 
    * @param startLocal
    *           vrai s'il faut lancer un serveur local
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
      LOG.debug("CassandraServerBean : startLocal={} - dataSet={}", startLocal,
            dataSets);
      resetData(dataSets);
   }

   /**
    * Réinitialise les données de la base cassandra locale, avec le jeu de
    * données utilisé initialement lors de la création du serveur
    * 
    * @throws Exception
    *            Une erreur est survenue
    */
   public final void resetData() throws Exception {
      resetData(dataSets);
   }

   /**
    * Réinitialise les données de la base cassandra locale
    * 
    * @param newDataSets
    *           Jeu(x) de données à utiliser
    * @throws Exception
    *            Une erreur est survenue
    */
   public final void resetData(String... newDataSets) throws Exception {

      if (!startLocal)
         return;

      LOG.debug("CassandraServerBean : reseting data...");

      EmbeddedCassandraServerHelper.startEmbeddedCassandra();

      // On attend que le serveur soit prêt
      waitForServer();

      // Fusionne les DataSets
      DataSet dataSet = mergeDataSets(newDataSets);

      // Charge les données
      DataLoader dataLoader = new DataLoader(TEST_CLUSTER_NAME,
            "localhost:9171");
      dataLoader.load(dataSet);

   }

   /**
    * Il arrive que le serveur cassandra local mette du temps avant d'être
    * opérationnel. Cette méthode fait en sorte d'attendre jusqu'à ce qu'il soit
    * opérationnel
    * 
    * @throws InterruptedException
    *            : on a été interrompu
    * 
    */
   private void waitForServer() throws InterruptedException {
      Cluster cluster = getTestCluster();
      for (int i = 0; i < WAIT_MAX_TRY; i++) {
         try {
            cluster.describeKeyspaces();
            break;
         } catch (Exception e) {
            LOG
                  .debug("CassandraServerBean : waiting for server (" + i
                        + ")...");
            Thread.sleep(WAIT_MS);
            LOG.debug("CassandraServerBean : reseting cluster (" + i + ")...");
            try {
               HFactory.shutdownCluster(testCluster);
            } catch (Exception ex) {
               LOG.debug(
                     "CassandraServerBean : error while shutdowning cluster",
                     ex);
            }
            cluster = getTestCluster();
         }
      }
   }

   private Cluster getTestCluster() {
      if (testCluster == null) {
         CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
               getHosts());
         hostConfigurator.setMaxActive(1);
         testCluster = HFactory.getOrCreateCluster(TEST_CLUSTER_NAME,
               hostConfigurator);
      }
      return testCluster;
   }

   /**
    * Arrête le cluster (partie cliente) de test
    */
   public final void shutdownTestCluster() {
      if (testCluster != null)
         HFactory.shutdownCluster(testCluster);
   }

   /**
    * Dans le cas d'un cassandra zookeeper non local, il s'agit de la chaîne de
    * connexion
    * 
    * @param hosts
    *           Chaîne de connexion (ex :
    *           "toto.toto.com:9160,titi.titi.com:9160")
    */
   public final void setHosts(String hosts) {
      this.hosts = hosts;
   }

   /**
    * Renvoie la chaîne de connexion au serveur cassandra
    * 
    * @return chaîne de connexion
    */
   public final String getHosts() {
      if (startLocal) {
         // Petite bidouille : on met le serveur localhost 3 fois : ça permet de
         // tenter 3 fois
         // l'opération si elle échoue la 1ere fois (ça arrive lorsque le
         // serveur cassandra local
         // ne se lance pas assez rapidement)
         return "localhost:9171,localhost:9171,localhost:9171";
      } else {
         return hosts;
      }
   }

   private DataSet mergeDataSets(String... dataSets) {

      // Vérification des paramètres d'entrée
      Assert.notEmpty(dataSets, "La liste des Dataset est vide");

      // Construit l'objet de résultat de la méthode : un dataSet
      // dans lequel on va fusionner les datasets passés en arguments
      MemoryDataSet dataSetResult = new MemoryDataSet();

      // Récupère la définition du keyspace et des CF dans le 1er dataset
      String premierDataSet = dataSets[0];
      ClassPathXmlDataSet premierDataSetObj = new ClassPathXmlDataSet(
            premierDataSet);
      dataSetResult.setKeyspace(premierDataSetObj.getKeyspace());
      dataSetResult.setColumnFamilies(premierDataSetObj.getColumnFamilies());

      // Boucle sur le reste des DataSet
      // Et fusionne les CF avec celles du premier DataSet
      for (int i = 1; i < dataSets.length; i++) {
         String dataSet = dataSets[i];
         ClassPathXmlDataSet dataSetObj = new ClassPathXmlDataSet(dataSet);
         if (!StringUtils.equals(dataSetObj.getKeyspace().getName(),
               dataSetResult.getKeyspace().getName())) {
            throw new IllegalArgumentException(
                  "Les KeySpace des datasets sont différents !");
         }
         for (ColumnFamilyModel cfm : dataSetObj.getColumnFamilies()) {
            dataSetResult.getColumnFamilies().add(cfm);
         }
      }

      // Renvoie l'objet Dataset fusionné
      return dataSetResult;

   }

}
