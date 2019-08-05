package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

@Component
public class DFCEKeyspaceConnecter implements CLientFactory {

   private static final Logger LOG = LoggerFactory.getLogger(DFCECassandraUpdaterCQL.class);
   private static final String DFCE_KEYSPACE_NAME = "dfce";
   
   private Cluster cluster;
   private Session session;  
   private CassandraConfig config;
 
   @Autowired
   DFCEKeyspaceConnecter (CassandraConfig config) throws InterruptedException{ 
	   this.config = config;
	   this.cluster = connectToCluster();
   }
   
   @Override
   public Session getSession() { 
		connectToKeyspace();
		return session;
   }
	
	// connection au keyspace dfce
	@Override
	public void connectToKeyspace() {
		
		try {
			
			session = cluster.connect(DFCE_KEYSPACE_NAME);
			session.execute("USE "+ DFCE_KEYSPACE_NAME);
			
		}catch (Exception e) {
			LOG.error("Problème de connection au keyspace "+ DFCE_KEYSPACE_NAME); 
		}

	}
	
	@Override
	public Logger getLogger() {
		return LOG;
	}
	
	@Override
	public CassandraConfig getConfig() {
		return config;
	}
}
