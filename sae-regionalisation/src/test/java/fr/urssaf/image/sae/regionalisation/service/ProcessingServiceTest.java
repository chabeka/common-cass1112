package fr.urssaf.image.sae.regionalisation.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.regionalisation.bean.CassandraConfig;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringCF;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringColumn;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringKey;
import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.support.CassandraSupport;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-mock-test.xml",
      "/applicationContext-sae-regionalisation-cassandra-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class ProcessingServiceTest {

   @Autowired
   private SaeDocumentDao saeDocumentDao;

   @Autowired
   private ProcessingService service;

   @Autowired
   private TraceDao traceDao;

   @Autowired
   private ServiceProviderSupport providerSupport;

   @Autowired
   private File repository;

   @Autowired
   private CassandraConfig config;

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

   @After
   public void end() throws Exception {
      cassandraSupport.disconnect();
      server.resetData();
      EasyMock.reset(saeDocumentDao);
      EasyMock.reset(providerSupport);
      EasyMock.reset(traceDao);
   }

   @Test
   public void launch_mise_a_jour() throws IOException {

      EasyMock.reset(saeDocumentDao);
      EasyMock.reset(providerSupport);
      EasyMock.reset(traceDao);

      // connexion à DFCE

      providerSupport.connect();

      EasyMock.expectLastCall().once();

      launchCommun();

      // trace dans trace rec
      traceDao.addTraceRec(EasyMock.anyObject(String.class), EasyMock.anyInt(),
            EasyMock.anyInt(), EasyMock.eq(true));
      EasyMock.expectLastCall().times(1);

      traceDao.open("12");
      EasyMock.expectLastCall().once();

      traceDao.close();
      EasyMock.expectLastCall().once();

      // persistance des modifications
      providerSupport.updateCriterion(EasyMock.anyObject(Document.class),
            EasyMock.anyObject(String.class), EasyMock.anyObject());

      // 3 documents avec 2 métadonnées valides
      EasyMock.expectLastCall().times(2);
      saeDocumentDao.update(EasyMock.anyObject(Document.class));

      EasyMock.expectLastCall().times(1);

      // trace dans trace Maj
      // 3 documents avec 2 métadonnées valides
      traceDao.addTraceMaj(EasyMock.anyObject(Trace.class));
      EasyMock.expectLastCall().times(2);

      traceDao.addTraceRec(EasyMock.anyObject(String.class), EasyMock.anyInt(),
            EasyMock.anyInt(), EasyMock.anyBoolean());
      EasyMock.expectLastCall().anyTimes();

      // déconnexion à DFCE

      providerSupport.disconnect();

      EasyMock.expectLastCall().once();

      EasyMock.replay(saeDocumentDao);
      EasyMock.replay(providerSupport);
      EasyMock.replay(traceDao);

      Resource fichier = new ClassPathResource("cassandra/file-datas.txt");

      service.launchWithFile(true, fichier.getFile(), "12", 1, 50, repository
            .getAbsolutePath());

      // vérification des services
      assertSaeDocumentDao();

   }

   @Test
   public void launch_tir_a_blanc() throws IOException {

      // connexion à DFCE

      providerSupport.connect();

      EasyMock.expectLastCall().once();

      // récupération des critères
      launchCommun();

      // trace dans trace rec

      traceDao.addTraceRec(EasyMock.anyObject(String.class), EasyMock.anyInt(),
            EasyMock.anyInt(), EasyMock.eq(false));
      EasyMock.expectLastCall().times(1);

      traceDao.open("12");
      EasyMock.expectLastCall().once();

      traceDao.close();
      EasyMock.expectLastCall().once();

      providerSupport.disconnect();

      EasyMock.expectLastCall().once();

      EasyMock.replay(saeDocumentDao);
      EasyMock.replay(providerSupport);
      EasyMock.replay(traceDao);

      Resource fichier = new ClassPathResource("cassandra/file-datas.txt");

      service.launchWithFile(false, fichier.getFile(), "12", 1, 50, repository
            .getAbsolutePath());

      // vérification des services
      assertSaeDocumentDao();

   }

   private void launchCommun() {

      List<Document> docs = new ArrayList<Document>();

      Document doc0 = createDocument();
      doc0.addCriterion("cog", "100");
      doc0.addCriterion("nce", "23456789012345");

      doc0 = createDocument();
      doc0.addCriterion("cog", "112");
      doc0.addCriterion("nce", "23456789012345");
      docs.add(doc0);

      doc0 = createDocument();
      doc0.addCriterion("cog", "234");
      doc0.addCriterion("nce", "23456789012345");
      docs.add(doc0);

      doc0 = createDocument();
      doc0.addCriterion("cog", "448");
      doc0.addCriterion("nce", "23456789012345");
      docs.add(doc0);

      EasyMock.expect(
            saeDocumentDao.getDocuments(EasyMock.anyObject(String.class)))
            .andReturn(docs);
   }

   private Document createDocument() {

      Document document = new DocumentImpl();

      document.setUuid(UUID.randomUUID());

      return document;
   }

   private void assertSaeDocumentDao() {

      EasyMock.verify(saeDocumentDao);
      EasyMock.verify(providerSupport);
      EasyMock.verify(traceDao);
   }

}
