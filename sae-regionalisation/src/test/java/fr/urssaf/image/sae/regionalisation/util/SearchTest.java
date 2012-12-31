/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.util;

import java.io.IOException;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Equality;
import com.netflix.astyanax.query.RowQuery;

import fr.urssaf.image.sae.regionalisation.bean.CassandraConfig;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringCF;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringColumn;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringKey;
import fr.urssaf.image.sae.regionalisation.datas.TermInfoResultSet;
import fr.urssaf.image.sae.regionalisation.support.CassandraSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-int9-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SearchTest {

   @Autowired
   private CassandraConfig config;
   @Autowired
   private CassandraSupport cassandraSupport;

   @Before
   public void init() throws IOException, ConnectionException {
      cassandraSupport.connect();
   }

   @After
   public void end() throws Exception {
      cassandraSupport.disconnect();
   }

   @Test
   @Ignore
   public void search() throws IOException {
      String searched = "000071756410197521";

      RowQuery<TermInfoRangeStringKey, TermInfoRangeStringColumn> query = cassandraSupport
            .getKeySpace().prepareQuery(
                  TermInfoRangeStringCF.CF_TERM_INFO_RANGE_STRING).getKey(
                  new TermInfoRangeStringKey("nce", UUID.fromString(config
                        .getBaseUuid()))).autoPaginate(true).withColumnRange(
                  TermInfoRangeStringCF.COLUMN_SERIALIZER.makeEndpoint(
                        searched, Equality.EQUAL).toBytes(),
                  TermInfoRangeStringCF.COLUMN_SERIALIZER.makeEndpoint(
                        searched, Equality.LESS_THAN_EQUALS).toBytes(), false,
                  10);

      TermInfoResultSet resultSet = new TermInfoResultSet(query);

      String value = resultSet.getNextValue();

      Assert.assertNotNull(value);

   }
}
