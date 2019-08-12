package fr.urssaf.image.sae.lotinstallmaj.service.utils.cql;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Session;

/**
 * Classe permettant de charger toutes les requetes se trouvant dans un fichier
 * et de les exécuter un à un.
 */
public class CQLDataFileLoader {

    private static final Logger log = LoggerFactory.getLogger(CQLDataFileLoader.class);

    private final Session session;
    
    public Session getSession() {
        return session;
    }

    public CQLDataFileLoader(Session session) {
        this.session = session;
    }

    public void load(CQLDataFileSet dataSet) {
        log.debug("loading data");
        
        for (String query : dataSet.getCQLStatements()) {
            log.debug("executing : " + query);
            try {
            	session.execute(query);
            	log.info("Query ===>  " + query);
            } catch (Exception e) {
            	e.printStackTrace();
				log.error("Problème d'execution de la requete : " + query);
				//return;
			}
        }
    }

    public String createBachStatement(CQLDataFileSet dataSet) {
    	
    	StringBuffer statement = new StringBuffer("BEGIN BATCH ");
    	for (String query : dataSet.getCQLStatements()) {
            statement.append(query );
        }
    	statement.append(" APPLY BATCH;");
		return statement.toString();
    }
}
