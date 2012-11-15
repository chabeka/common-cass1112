/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.astyanax.query.RowQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.bean.CassandraConfig;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.TermInfoRangeUuidDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidCF;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidColumn;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

/**
 * Classe implémentant l'interface {@link TermInfoRangeUuidDao}
 * 
 */
@Component
public class TermInfoRangeUuidDaoImpl implements TermInfoRangeUuidDao {

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private CassandraConfig cassandraConfig;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RowQuery<TermInfoRangeUuidKey, TermInfoRangeUuidColumn> getAllUuidColumns() {

      return cassandraSupport.getKeySpace().prepareQuery(
            TermInfoRangeUuidCF.CF_TERM_INFO_RANGE_UUID).getKey(
            new TermInfoRangeUuidKey("SM_UUID", cassandraConfig.getBaseUuid()))
            .autoPaginate(true);
   }

}
