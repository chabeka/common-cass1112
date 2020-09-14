package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Session;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;

@Component
public class DFCEKeyspaceConnecter{

   private static final Logger LOG = LoggerFactory.getLogger(DFCEKeyspaceConnecter.class);
   private static final String DFCE_KEYSPACE_NAME = "dfce";
   
   CassandraCQLClientFactory ccf;
   private Session session;  
 
   @Autowired
   DFCEKeyspaceConnecter (CassandraCQLClientFactory ccf) throws InterruptedException{ 
	   this.ccf = ccf;
	   this.session = ccf.getSession();
   }
   
   public Session getSession() { 
		connectToKeyspace();
		return session;
   }
	
	// connection au keyspace dfce
	
	public void connectToKeyspace() {
		
		try {
			
			if(!ccf.getStartLocal())
				session.execute("USE "+ DFCE_KEYSPACE_NAME);
			
		}catch (Exception e) {
			LOG.error("Problème de connection au keyspace "+ DFCE_KEYSPACE_NAME); 
		}

	}
	
	
	public Logger getLogger() {
		return LOG;
	}
	
}
