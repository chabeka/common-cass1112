package fr.urssaf.image.commons.cassandra.helper;

import org.apache.commons.lang.StringUtils;
import org.cassandraunit.DataLoader;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.cassandraunit.model.ColumnFamilyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.model.MemoryDataSet;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

/**
 * Classe utilitaire facilitant la création d'un serveur cassandra local par un
 * bean spring.
 */
public class CassandraServerBean extends AbstractCassandraServer {

  private static final Logger LOG = LoggerFactory.getLogger(CassandraServerBean.class);

  private Cluster testCluster = null;

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetData(final String... newDataSets) throws Exception {
    resetData(false, newDataSets);

  }

  /**
   * Réinitialise les données de la base cassandra locale, avec le jeu de
   * données utilisé initialement lors de la création du serveur
   *
   * @param dropAndCreateKeyspace
   *          Si true, suppression et creation d'un nouveau keyspace sinon false.
   * @param newDataSets
   * @throws Exception
   *           Une erreur est survenue
   */
  public void resetData(final boolean dropAndCreateKeyspace, final String... newDataSets) throws Exception {

    if (!startLocal) {
      return;
    }

    // demarage du serveur
    cassandraUnitInitilization();

    // On attend que le serveur soit prêt
    waitForServer();

    // Fusionne les DataSets
    final DataSet dataSet = mergeDataSets(newDataSets);
    // Charge les données
    final DataLoader dataLoader = new DataLoader(TEST_CLUSTER_NAME, "localhost:9171");
    dataLoader.load(dataSet, !(testCluster.describeKeyspace(KEYSPACE_TU) != null && !dropAndCreateKeyspace));

  }

  /**
   * Il arrive que le serveur cassandra local mette du temps avant d'être
   * opérationnel. Cette méthode fait en sorte d'attendre jusqu'à ce qu'il soit
   * opérationnel
   *
   * @throws InterruptedException
   *           : on a été interrompu
   */
  @Override
  protected void waitForServer() throws InterruptedException {
    Cluster cluster = getTestCluster();
    for (int i = 0; i < WAIT_MAX_TRY; i++) {
      try {
        cluster.describeKeyspaces();
        break;
      } catch (final Exception e) {
        LOG.debug("CassandraServerBean : waiting for server (" + i + ")...");
        Thread.sleep(WAIT_MS);
        LOG.debug("CassandraServerBean : reseting cluster (" + i + ")...");
        try {
          HFactory.shutdownCluster(testCluster);
        } catch (final Exception ex) {
          LOG.debug("CassandraServerBean : error while shutdowning cluster", ex);
        }
        cluster = getTestCluster();
      }
    }
  }

  private Cluster getTestCluster() {
    if (testCluster == null) {
      final CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(getHosts());
      hostConfigurator.setMaxActive(1);
      testCluster = HFactory.getOrCreateCluster(TEST_CLUSTER_NAME, hostConfigurator);
    }
    return testCluster;
  }

  /**
   * Arrête le cluster (partie cliente) de test
   */
  @Override
  public final void shutdownTestCluster() {
    if (testCluster != null) {
      HFactory.shutdownCluster(testCluster);
    }
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
      return "localhost:9171,localhost:9171,localhost:9171";
    } else {
      return hosts;
    }
  }

  private DataSet mergeDataSets(final String... dataSets) {
    // Vérification des paramètres d'entrée
    Assert.notEmpty(dataSets, "La liste des Dataset est vide");

    // Construit l'objet de résultat de la méthode : un dataSet
    // dans lequel on va fusionner les datasets passés en arguments
    final MemoryDataSet dataSetResult = new MemoryDataSet();

    // Récupère la définition du keyspace et des CF dans le 1er dataset
    final String premierDataSet = dataSets[0];
    final ClassPathXmlDataSet premierDataSetObj = new ClassPathXmlDataSet(premierDataSet);
    dataSetResult.setKeyspace(premierDataSetObj.getKeyspace());
    dataSetResult.setColumnFamilies(premierDataSetObj.getColumnFamilies());

    // Boucle sur le reste des DataSet
    // Et fusionne les CF avec celles du premier DataSet
    for (int i = 1; i < dataSets.length; i++) {
      final String dataSet = dataSets[i];
      final ClassPathXmlDataSet dataSetObj = new ClassPathXmlDataSet(dataSet);
      if (!StringUtils.equals(dataSetObj.getKeyspace().getName(), dataSetResult.getKeyspace().getName())) {
        throw new IllegalArgumentException("Les KeySpace des datasets sont différents !");
      }
      for (final ColumnFamilyModel cfm : dataSetObj.getColumnFamilies()) {
        dataSetResult.getColumnFamilies().add(cfm);
      }
    }

    // Renvoie l'objet Dataset fusionné
    return dataSetResult;

  }

  /**
   * @return the keyspaceTu
   */
  @Override
  public String getKeyspaceTu() {
    return KEYSPACE_TU;
  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.commons.cassandra.helper.AbstractCassandraServer#getLogger()
   */
  @Override
  public Logger getLogger() {
    return LOG;
  }
}
