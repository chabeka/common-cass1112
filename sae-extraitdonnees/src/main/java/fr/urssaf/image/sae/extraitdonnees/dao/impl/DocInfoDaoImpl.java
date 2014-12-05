package fr.urssaf.image.sae.extraitdonnees.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.extraitdonnees.dao.DocInfoDao;
import fr.urssaf.image.sae.extraitdonnees.dao.cf.DocInfoCF;
import fr.urssaf.image.sae.extraitdonnees.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.extraitdonnees.support.CassandraSupport;

/**
 * Implémentation de l'interface {@link DocInfoDao}
 * 
 */
@Component
public class DocInfoDaoImpl implements DocInfoDao {

   private static final int ROW_LIMIT = 100;

   public static final String CF_DOCINFO = "DocInfo";

   @Autowired
   private CassandraSupport cassandraSupport;

   /**
    * {@inheritDoc}
    */
   public final AllRowsQuery<DocInfoKey, String> getQuery(String... metas) {
      return cassandraSupport.getKeySpace().prepareQuery(DocInfoCF.CF_DOC_INFO)
            .getAllRows().setRowLimit(ROW_LIMIT).withColumnSlice(metas);
   }

}
