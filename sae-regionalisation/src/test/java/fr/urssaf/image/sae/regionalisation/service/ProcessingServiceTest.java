package fr.urssaf.image.sae.regionalisation.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.Keyspace;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-mock-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
@Ignore("à revoir")
public class ProcessingServiceTest {

//   @Autowired
//   private SaeDocumentDao saeDocumentDao;
//
//   @Autowired
//   private ProcessingService service;
//
//   @Autowired
//   private TraceDao traceDao;
//
//   @Autowired
//   private ServiceProviderSupport providerSupport;
//
//   @Autowired
//   private File repository;
//
//   
//   @After
//   public void end() throws Exception {
//      EasyMock.reset(saeDocumentDao);
//      EasyMock.reset(providerSupport);
//      EasyMock.reset(traceDao);
//   }
//
//   
//   @Test
//   public void launch_mise_a_jour() throws IOException {
//
//      EasyMock.reset(saeDocumentDao);
//      EasyMock.reset(providerSupport);
//      EasyMock.reset(traceDao);
//
//      // connexion à DFCE
//
//      providerSupport.connect();
//
//      EasyMock.expectLastCall().once();
//
//      launchCommun();
//
//      // trace dans trace rec
//      traceDao.addTraceRec(EasyMock.anyObject(String.class), EasyMock.anyInt(),
//            EasyMock.anyInt(), EasyMock.eq(true));
//      EasyMock.expectLastCall().times(1);
//
//      traceDao.open("12");
//      EasyMock.expectLastCall().once();
//
//      traceDao.close();
//      EasyMock.expectLastCall().once();
//
//      // persistance des modifications
//      providerSupport.updateCriterion(EasyMock.anyObject(Document.class),
//            EasyMock.anyObject(String.class), EasyMock.anyObject());
//
//      // 3 documents avec 2 métadonnées valides, dont 1 code organisme que l'on ne change pas
//      EasyMock.expectLastCall().times(1);
//      saeDocumentDao.update(EasyMock.anyObject(Document.class));
//
//      EasyMock.expectLastCall().times(1);
//
//      // trace dans trace Maj
//      // 3 documents avec 2 métadonnées valides, dont 1 code organisme que l'on ne change pas
//      traceDao.addTraceMaj(EasyMock.anyObject(Trace.class));
//      EasyMock.expectLastCall().times(1);
//
//      traceDao.addTraceRec(EasyMock.anyObject(String.class), EasyMock.anyInt(),
//            EasyMock.anyInt(), EasyMock.anyBoolean());
//      EasyMock.expectLastCall().anyTimes();
//
//      // déconnexion à DFCE
//
//      providerSupport.disconnect();
//
//      EasyMock.expectLastCall().once();
//
//      EasyMock.replay(saeDocumentDao);
//      EasyMock.replay(providerSupport);
//      EasyMock.replay(traceDao);
//
//      Resource fichier = new ClassPathResource("cassandra/file-datas.txt");
//
//      service.launchWithFile(true, fichier.getFile(), "12", 1, 50, repository
//            .getAbsolutePath());
//
//      // vérification des services
//      assertSaeDocumentDao();
//
//   }
//
//   @Test
//   public void launch_tir_a_blanc() throws IOException {
//
//      // connexion à DFCE
//
//      providerSupport.connect();
//
//      EasyMock.expectLastCall().once();
//
//      // récupération des critères
//      launchCommun();
//
//      // trace dans trace rec
//
//      traceDao.addTraceRec(EasyMock.anyObject(String.class), EasyMock.anyInt(),
//            EasyMock.anyInt(), EasyMock.eq(false));
//      EasyMock.expectLastCall().times(6);
//
//      traceDao.open("12");
//      EasyMock.expectLastCall().once();
//
//      traceDao.close();
//      EasyMock.expectLastCall().once();
//
//      providerSupport.disconnect();
//
//      EasyMock.expectLastCall().once();
//
//      EasyMock.replay(saeDocumentDao);
//      EasyMock.replay(providerSupport);
//      EasyMock.replay(traceDao);
//
//      Resource fichier = new ClassPathResource("cassandra/file-datas.txt");
//
//      service.launchWithFile(false, fichier.getFile(), "12", 1, 50, repository
//            .getAbsolutePath());
//
//      // vérification des services
//      assertSaeDocumentDao();
//
//   }
//
//   private void launchCommun() {
//
//      List<Document> docs = new ArrayList<Document>();
//
//      Document doc0 = createDocument();
//      doc0.addCriterion("cog", "100");
//      doc0.addCriterion("nce", "23456789012345");
//
//      doc0 = createDocument();
//      doc0.addCriterion("cog", "112");
//      doc0.addCriterion("nce", "23456789012345");
//      docs.add(doc0);
//
//      doc0 = createDocument();
//      doc0.addCriterion("cog", "234");
//      doc0.addCriterion("nce", "23456789012345");
//      docs.add(doc0);
//
//      doc0 = createDocument();
//      doc0.addCriterion("cog", "448");
//      doc0.addCriterion("nce", "23456789012345");
//      docs.add(doc0);
//
//      EasyMock.expect(
//            saeDocumentDao.getDocuments(EasyMock.anyObject(String.class)))
//            .andReturn(docs);
//   }
//
//   private Document createDocument() {
//
//      Document document = new DocumentImpl();
//
//      document.setUuid(UUID.randomUUID());
//
//      return document;
//   }
//
//   private void assertSaeDocumentDao() {
//
//      EasyMock.verify(saeDocumentDao);
//      EasyMock.verify(providerSupport);
//      EasyMock.verify(traceDao);
//   }

}
