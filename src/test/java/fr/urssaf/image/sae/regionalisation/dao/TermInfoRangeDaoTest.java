package fr.urssaf.image.sae.regionalisation.dao;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.regionalisation.bean.CassandraConfig;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringCF;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringColumn;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringKey;
import fr.urssaf.image.sae.regionalisation.dao.TermInfoRangeStringDao;
import fr.urssaf.image.sae.regionalisation.datas.TermInfoResultSet;
import fr.urssaf.image.sae.regionalisation.support.CassandraSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-dfce-test.xml",
      "/applicationContext-sae-regionalisation-cassandra-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class TermInfoRangeDaoTest {

   @Autowired
   private CassandraConfig config;

   @Autowired
   private TermInfoRangeStringDao dao;

   private Keyspace keyspace;
   private AstyanaxContext<Keyspace> context;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private CassandraSupport cassandraSupport;

   @Before
   public void init() throws IOException, ConnectionException {
      String servers = "localhost";

      context = new AstyanaxContext.Builder().forCluster("TestCluster")
            .forKeyspace("REGIO").withAstyanaxConfiguration(
                  new AstyanaxConfigurationImpl().setDiscoveryType(
                        NodeDiscoveryType.NONE).setDefaultReadConsistencyLevel(
                        ConsistencyLevel.CL_ONE)
                        .setDefaultWriteConsistencyLevel(
                              ConsistencyLevel.CL_ONE))
            .withConnectionPoolConfiguration(
                  new ConnectionPoolConfigurationImpl("MyConnectionPool")
                        .setPort(9171).setMaxConnsPerHost(1).setSeeds(servers))
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();
      keyspace = context.getEntity();

      initDatas();

      cassandraSupport.connect();
   }

   @After
   public void end() throws Exception {
      cassandraSupport.disconnect();
      server.resetData();
   }

   public void initDatas() throws IOException, ConnectionException {

      ClassPathResource resource = new ClassPathResource(
            "cassandra/cassandra-datas.txt");

      List<String> lines = FileUtils.readLines(resource.getFile());

      TermInfoRangeStringKey key = new TermInfoRangeStringKey("nce", UUID
            .fromString(config.getBaseUuid()));

      TermInfoRangeStringColumn column;
      for (String line : lines) {
         column = new TermInfoRangeStringColumn();
         column.setCategoryValue(line);
         column.setDocumentUUID(UUID.randomUUID());
         column.setDocumentVersion("1");

         keyspace.prepareColumnMutation(
               TermInfoRangeStringCF.CF_TERM_INFO_RANGE_STRING, key, column)
               .putValue("test florent", null).execute();
      }
   }

   @Test
   public void TestGetRecords() {

      RowQuery<TermInfoRangeStringKey, TermInfoRangeStringColumn> query = dao
            .getQuery("00000000000000", "99999999999999", "nce");

      TermInfoResultSet infoResultSet = new TermInfoResultSet(query);
      int index = 0;
      String last = "";
      String value;
      while ((value = infoResultSet.getNextValue()) != null) {
         Assert.assertTrue(
               "la valeur actuelle doit être plus grande que l'ancienne : ["
                     + last + "] / [" + value + "]", value.compareTo(last) > 1);
         index++;
      }

      Assert.assertEquals("le nombre de résultat retourné doit être exact", 7,
            index);
   }
}
