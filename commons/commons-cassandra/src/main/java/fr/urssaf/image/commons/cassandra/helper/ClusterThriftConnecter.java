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

public class ClusterThriftConnecter {

	private static final Logger LOG = LoggerFactory.getLogger(ClusterThriftConnecter.class);
	
	private static final int WAIT_MAX_TRY = 12;
	private static final long WAIT_MS = 1000;
	private static final String TEST_CLUSTER_NAME = "TestCluster";
	public static final String KEYSPACE_TU = "keyspace_tu";
	
	private Cluster testCluster = null;
	private String hosts;
	
	public ClusterThriftConnecter(String hosts) throws InterruptedException {
		this.hosts =hosts;
		waitForServer();
	}

	/**
   * Il arrive que le serveur cassandra local mette du temps avant d'être
   * opérationnel. Cette méthode fait en sorte d'attendre jusqu'à ce qu'il soit
   * opérationnel
   *
   * @throws InterruptedException
   *           : on a été interrompu
   */
  
	private void waitForServer() throws InterruptedException {
	    createTestCluster();
	    for (int i = 0; i < WAIT_MAX_TRY; i++) {
	      try {
	    	  testCluster.describeKeyspaces();
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
	        createTestCluster();
	      }
	    }
	}
  
  private void createTestCluster() {
    if (testCluster == null) {
      final CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(hosts);
      hostConfigurator.setMaxActive(1);
      testCluster = HFactory.getOrCreateCluster(TEST_CLUSTER_NAME, hostConfigurator);
    }
  }
  
  public Cluster getTestCluster() {
	  return testCluster;
  }
  
  private DataSet mergeDataThriftSets(final String... dataSets) {
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
  
  public void loadDataSetToServer(boolean dropAndCreateKeyspace,  String... newDataSets) {
	DataSet dataSet = mergeDataThriftSets(newDataSets);
	// Charge les données
    final DataLoader dataLoader = new DataLoader(TEST_CLUSTER_NAME, "localhost:9171");
    dataLoader.load(dataSet, !(testCluster.describeKeyspace(KEYSPACE_TU) != null && !dropAndCreateKeyspace));
  }
}
