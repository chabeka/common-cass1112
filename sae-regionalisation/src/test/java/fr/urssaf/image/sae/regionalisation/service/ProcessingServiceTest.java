package fr.urssaf.image.sae.regionalisation.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.easymock.EasyMock;
import org.junit.After;
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
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-mock-test.xml" })
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

   @After
   public void after() {

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
      EasyMock.expectLastCall().times(3);

      traceDao.open("12");
      EasyMock.expectLastCall().once();

      traceDao.close();
      EasyMock.expectLastCall().once();

      // persistance des modifications
      providerSupport.updateCriterion(EasyMock.anyObject(Document.class),
            EasyMock.anyObject(String.class), EasyMock.anyObject());

      // 3 documents avec 2 métadonnées valides
      EasyMock.expectLastCall().times(6);
      saeDocumentDao.update(EasyMock.anyObject(Document.class));

      EasyMock.expectLastCall().times(3);

      // trace dans trace Maj
      // 3 documents avec 2 métadonnées valides
      traceDao.addTraceMaj(EasyMock.anyObject(Trace.class));
      EasyMock.expectLastCall().times(6);

      // déconnexion à DFCE

      providerSupport.disconnect();

      EasyMock.expectLastCall().once();

      EasyMock.replay(saeDocumentDao);
      EasyMock.replay(providerSupport);
      EasyMock.replay(traceDao);

      Resource fichier = new ClassPathResource("csv/fichier_format_correct");

      service.launchWithFile(true, fichier.getFile(), "12", 0, 7, repository
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
      EasyMock.expectLastCall().times(3);

      traceDao.open("12");
      EasyMock.expectLastCall().once();

      traceDao.close();
      EasyMock.expectLastCall().once();

      providerSupport.disconnect();

      EasyMock.expectLastCall().once();

      EasyMock.replay(saeDocumentDao);
      EasyMock.replay(providerSupport);
      EasyMock.replay(traceDao);

      Resource fichier = new ClassPathResource("csv/fichier_format_correct");

      service.launchWithFile(false, fichier.getFile(), "12", 0, 7, repository
            .getAbsolutePath());

      // vérification des services
      assertSaeDocumentDao();

   }

   private void launchCommun() {

      // récupération des métadonnées

      Map<String, Object> metadatas = new HashMap<String, Object>();
      metadatas.put("nne", "value1");
      metadatas.put("nbp", null);
      metadatas.put("metadataUnknown", "valueUnknown");

      List<Document> docs0 = new ArrayList<Document>();

      Document doc0 = createDocument();
      doc0.addCriterion("nne", "oldValue");
      doc0.addCriterion("nbp", "oldValue");
      doc0.addCriterion("zzz", "oldValue");

      docs0.add(doc0);
      docs0.add(createDocument());

      List<Document> docs1 = new ArrayList<Document>();
      docs1.add(createDocument());

      List<Document> docs2 = new ArrayList<Document>();

      EasyMock.expect(
            saeDocumentDao.getDocuments(EasyMock.anyObject(String.class)))
            .andReturn(docs0).andReturn(docs1).andReturn(docs2);
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
