package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

@Component
public class SAEKeyspaceConnecter implements CLientFactory {

   private static final Logger LOG = LoggerFactory.getLogger(SAEKeyspaceConnecter.class);
   private static final String SAE_KEYSPACE_NAME = "SAE";
   
   private Cluster cluster;
   private Session session;    
   private CassandraConfig config;
   
 
   @Autowired
   SAEKeyspaceConnecter (CassandraConfig config) throws InterruptedException{ 
	   this.config = config;
	   this.cluster = connectToCluster();
	   this.config.setKeyspaceName(SAE_KEYSPACE_NAME); 
   }

   @Override
   public Session getSession() {
		connectToKeyspace();
		return session;
	}
   
	
	public void setSession(Session session) {
	this.session = session;
}

	// connection au keyspace SAE
	@Override
	public void connectToKeyspace() {
		try {
			
			session = cluster.connect('\"' + SAE_KEYSPACE_NAME + '\"');
			session.execute("use \"SAE\"");
			
		}catch (Exception e) {
			LOG.error("Problème de connection au keyspace "+ SAE_KEYSPACE_NAME); 
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
