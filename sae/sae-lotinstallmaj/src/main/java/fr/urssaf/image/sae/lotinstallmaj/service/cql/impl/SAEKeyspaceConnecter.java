package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Session;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

@Component
public class SAEKeyspaceConnecter {

   private static final Logger LOG = LoggerFactory.getLogger(SAEKeyspaceConnecter.class);
   private static String keyspaceName;
   
   CassandraCQLClientFactory ccf;
   private Session session;    
 
 
   @Autowired
   SAEKeyspaceConnecter (CassandraCQLClientFactory ccf, CassandraConfig config) throws InterruptedException{ 
	   this.ccf = ccf;
	   this.session = ccf.getSession();
	   keyspaceName = config.getKeyspaceName();
   }

   
   public Session getSession() {
		connectToKeyspace();
		return session;
	}
   
	
	public void setSession(Session session) {
	this.session = session;
}

	// connection au keyspace SAE
	public void connectToKeyspace() {
		try {
			if(!ccf.getStartLocal())
				session.execute("use \""+ keyspaceName +"\"");
			
		}catch (Exception e) {
			LOG.error("Problème de connection au keyspace "+ keyspaceName); 
		}
	}

	
	public Logger getLogger() {
		return LOG;
	}


	public CassandraCQLClientFactory getCcf() {
		return ccf;
	}

}
