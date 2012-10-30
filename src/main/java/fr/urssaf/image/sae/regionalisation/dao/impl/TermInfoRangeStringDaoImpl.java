/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.astyanax.model.Equality;
import com.netflix.astyanax.query.RowQuery;

import fr.urssaf.image.sae.regionalisation.bean.CassandraConfig;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringCF;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringColumn;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringKey;
import fr.urssaf.image.sae.regionalisation.dao.TermInfoRangeStringDao;
import fr.urssaf.image.sae.regionalisation.support.CassandraSupport;

/**
 * Implémentation de l'interface {@link TermInfoRangeStringDao}<br>
 * Récupération des index nécessaires au traitement de données
 * 
 */
@Component
public class TermInfoRangeStringDaoImpl implements TermInfoRangeStringDao {

   private static final int BLOC_SIZE = 100;

   @Autowired
   private CassandraConfig cassandraConfig;

   @Autowired
   private CassandraSupport cassandraSupport;

   @Override
   public final RowQuery<TermInfoRangeStringKey, TermInfoRangeStringColumn> getQuery(
         String first, String last, String indexName) {

      UUID baseUUID = UUID.fromString(cassandraConfig.getBaseUuid());

      return cassandraSupport.getKeySpace().prepareQuery(
            TermInfoRangeStringCF.CF_TERM_INFO_RANGE_STRING).getKey(
            new TermInfoRangeStringKey(indexName, baseUUID)).autoPaginate(true)
            .withColumnRange(
                  TermInfoRangeStringCF.COLUMN_SERIALIZER.makeEndpoint(first,
                        Equality.EQUAL).toBytes(),
                  TermInfoRangeStringCF.COLUMN_SERIALIZER.makeEndpoint(last,
                        Equality.LESS_THAN_EQUALS).toBytes(), false, BLOC_SIZE);
   }
}
