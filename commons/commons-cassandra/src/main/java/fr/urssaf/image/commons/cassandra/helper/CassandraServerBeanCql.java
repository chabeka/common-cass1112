package fr.urssaf.image.commons.cassandra.helper;

import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;

/**
 * Classe utilitaire facilitant la création d'un serveur cassandra local par un
 * bean spring.
 */
public class CassandraServerBeanCql extends AbstractCassandraServer {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraServerBeanCql.class);

   private Cluster testCluster = null;

   private Session testSession = null;

   public static final String KEYSPACE_TU = "keyspace_cql";

   /**
    * Réinitialise les données de la base cassandra locale
    *
    * @param newDataSets
    *           Jeu(x) de données à utiliser
    * @throws Exception
    *            Une erreur est survenue
    */
   @Override
   public void resetData(final String... newDataSets) throws Exception {
      if (!startLocal) {
         return;
      }
      // demarage du serveur
      cassandraUnitInitilization();

      // On attend que le serveur soit prêt
      waitForServer();

      // On inject les jeux de données
      if (newDataSets != null && newDataSets.length > 0) {
         final String dataSet = newDataSets[0];

         final CQLDataLoader cqlDataLoader = new CQLDataLoader(testSession);
         cqlDataLoader.load(new ClassPathCQLDataSet(dataSet, true, true, KEYSPACE_TU));
      }
      final Session session = testCluster.connect(CassandraServerBeanCql.KEYSPACE_TU);
      testSession = session;
   }

   /**
    * Il arrive que le serveur cassandra local mette du temps avant d'être
    * opérationnel. Cette méthode fait en sorte d'attendre jusqu'à ce qu'il soit
    * opérationnel
    *
    * @throws InterruptedException
    *            : on a été interrompu
    */
   @Override
   @SuppressWarnings("resource")
   protected void waitForServer() throws InterruptedException {
      Cluster cluster = getTestCluster();
      for (int i = 0; i < WAIT_MAX_TRY; i++) {
         try {
            cluster.getClusterName();
            break;
         }
         catch (final Exception e) {
            LOG.debug("CassandraServerBean : waiting for server (" + i + ")...");
            Thread.sleep(WAIT_MS);
            LOG.debug("CassandraServerBean : reseting cluster (" + i + ")...");
            try {
               testCluster.close();
            }
            catch (final Exception ex) {
               LOG.debug("CassandraServerBean : error while shutdowning cluster", ex);
            }
            cluster = getTestCluster();
         }
      }
   }

   private Cluster getTestCluster() {
      if (testCluster == null) {
         final CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
                                                                                          getHosts());
         hostConfigurator.setMaxActive(1);

         final Builder testBuilder = Cluster.builder()
                                            .addContactPoints("localhost")
                                            .withClusterName(TEST_CLUSTER_NAME)
                                            .withPort(9142);
         testCluster = Cluster.buildFrom(testBuilder);
         final Session session = testCluster.connect();
         testSession = session;
      }
      testCluster.getConfiguration().getSocketOptions().setConnectTimeoutMillis(20000000);
      testCluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(20000000);
      return testCluster;
   }

   /**
    * Renvoie la chaîne de connexion au serveur cassandra
    *
    * @return chaîne de connexion
    */
   @Override
   public final String getHosts() {
      if (startLocal) {
         // Petite bidouille : on met le serveur localhost 3 fois : ça permet de
         // tenter 3 fois
         // l'opération si elle échoue la 1ere fois (ça arrive lorsque le
         // serveur cassandra local
         // ne se lance pas assez rapidement)
         return "localhost:9142,localhost:9142,localhost:9142";
      } else {
         return hosts;
      }
   }

   /**
    * Arrête le cluster (partie cliente) de test
    */
   @Override
   public final void shutdownTestCluster() {
      if (testCluster != null) {
         testCluster.close();
      }
   }

   /**
    * @return the testSession
    */
   public Session getTestSession() {
      return testSession;
   }

   /**
    * @return the keyspaceTu
    */
   @Override
   public String getKeyspaceTu() {
      return KEYSPACE_TU;
   }

   @Override
   public Logger getLogger() {
      return LOG;
   }
   //
}
