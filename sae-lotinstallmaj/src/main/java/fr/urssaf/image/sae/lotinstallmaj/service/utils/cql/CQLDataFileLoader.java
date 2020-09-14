package fr.urssaf.image.sae.lotinstallmaj.service.utils.cql;




import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Session;

/**
 * Classe permettant de charger toutes les requêtes se trouvant dans un fichier
 * et de les exécuter un à un.
 */
public class CQLDataFileLoader {

   private static final Logger log = LoggerFactory.getLogger(CQLDataFileLoader.class);

   private final Session session;

   public Session getSession() {
      return session;
   }

   public CQLDataFileLoader(final Session session) {
      this.session = session;
   }

   public void load(final CQLDataFileSet dataSet) {
      log.debug("loading data");

      for (final String query : dataSet.getCQLStatements()) {
         log.debug("executing : " + query);
         try {
            session.execute(query);
            log.info("Query ===>  " + query);
         } catch (final Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            log.error("Problème d'execution de la requete : " + query);
         }
      }
   }

   public String createBachStatement(final CQLDataFileSet dataSet) {

      final StringBuffer statement = new StringBuffer("BEGIN BATCH ");
      for (final String query : dataSet.getCQLStatements()) {
         statement.append(query );
      }
      statement.append(" APPLY BATCH;");
      return statement.toString();
   }
}
