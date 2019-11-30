package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Session;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;

@Component
public class SAEKeyspaceConnecter {

   private static final Logger LOG = LoggerFactory.getLogger(SAEKeyspaceConnecter.class);
   private static final String SAE_KEYSPACE_NAME = "SAE";
   
   CassandraCQLClientFactory ccf;
   private Session session;    
   
 
   @Autowired
   SAEKeyspaceConnecter (CassandraCQLClientFactory ccf) throws InterruptedException{ 
	   this.ccf = ccf;
	   this.session = ccf.getSession();
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
				session.execute("use \"SAE\"");
			
		}catch (Exception e) {
			LOG.error("Problème de connection au keyspace "+ SAE_KEYSPACE_NAME); 
		}
	}

	
	public Logger getLogger() {
		return LOG;
	}


	public CassandraCQLClientFactory getCcf() {
		return ccf;
	}

}
