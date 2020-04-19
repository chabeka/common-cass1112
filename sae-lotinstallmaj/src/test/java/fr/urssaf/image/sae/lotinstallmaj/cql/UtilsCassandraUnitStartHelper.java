package fr.urssaf.image.sae.lotinstallmaj.cql;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;

public class UtilsCassandraUnitStartHelper {
	  
	
	  private Cluster testCluster = null;
	  private Session testSession = null;
	  protected static final int WAIT_MAX_TRY = 12;
	  protected static final long WAIT_MS = 1000;
	  protected static final String TEST_CLUSTER_NAME = "TestCluster";
	
	  
	  @SuppressWarnings("resource")
	  public void waitForServer() throws InterruptedException {
	    Cluster cluster = getTestCluster();
	    for (int i = 0; i < WAIT_MAX_TRY; i++) {
	      try {
	        cluster.getClusterName();
	        break;
	      } catch (final Exception e) {
	        Thread.sleep(WAIT_MS);
	        try {
	          testCluster.close();
	        } catch (final Exception ex) {
	        }
	        cluster = getTestCluster();
	      }
	    }
	  }
	  private Cluster getTestCluster() {
	    if (testCluster == null) {
	      try {
	        final CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(getHosts());
	        hostConfigurator.setMaxActive(1);
	
	        final Builder testBuilder = Cluster.builder().addContactPoints("localhost").withClusterName(TEST_CLUSTER_NAME).withPort(9142);
	        testCluster = Cluster.buildFrom(testBuilder);
	        final Session session = testCluster.connect();
	        testSession = session;
	      } catch (final Throwable e) {
	    	  System.out.println(e);
	      }
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
	  public final String getHosts() {
	      // Petite bidouille : on met le serveur localhost 3 fois : ça permet de
	      // tenter 3 fois
	      // l'opération si elle échoue la 1ere fois (ça arrive lorsque le
	      // serveur cassandra local
	      // ne se lance pas assez rapidement)
	      return "localhost:9142,localhost:9142,localhost:9142";
	  }
	public Session getTestSession() {
		return testSession;
	}
	  
	  
}
