package fr.urssaf.image.sae.extraitdonnees.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.extraitdonnees.dao.BasesReferenceDao;
import fr.urssaf.image.sae.extraitdonnees.dao.cf.BasesReferenceCF;
import fr.urssaf.image.sae.extraitdonnees.support.CassandraSupport;

/**
 * Implémentation de la DAO {@link BasesReferenceDao}
 */
@Component
public final class BasesReferenceDaoImpl implements BasesReferenceDao {

   private static final int ROW_LIMIT = 100;

   public static final String CF_BASES_REFERENCE = "BasesReference";

   @Autowired
   private CassandraSupport cassandraSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public AllRowsQuery<String, String> getQuery() {

      return cassandraSupport.getKeySpace().prepareQuery(
            BasesReferenceCF.CF_BASES_REFERENCES).getAllRows().setRowLimit(
            ROW_LIMIT);

   }

}
