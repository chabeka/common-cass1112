/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.exception.LineFormatException;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ProcessingServiceFileTest {

   @Autowired
   private ServiceProviderSupport providerSupport;

   @Autowired
   private SaeDocumentDao saeDocumentDao;

   @Autowired
   private TraceDao traceDao;

   @Autowired
   private File repository;

   @Autowired
   private ApplicationContext context;

   @Autowired
   ProcessingService service;

   @Test
   public void test_failure_format_fichier() throws IOException {

      try {
         // connexion à DFCE
         providerSupport.connect();
         EasyMock.expectLastCall().once();

         // déconnexion à DFCE
         providerSupport.disconnect();
         EasyMock.expectLastCall().once();

         EasyMock.replay(providerSupport);

         Resource resource = context.getResource("csv/fichier_format_errone");
         File fichier = resource.getFile();

         service.launchWithFile(false, fichier, "12", 0, 12, repository
               .getAbsolutePath());

      } catch (Exception e) {

         EasyMock.verify(providerSupport);

         Assert.assertEquals("le classe d'exception doit être "
               + LineFormatException.class.getName(),
               LineFormatException.class, e.getClass());
      }

      EasyMock.reset(providerSupport, traceDao, saeDocumentDao);
   }

   @Test
   public void test_succes_traitement_fichier() throws IOException {

      // connexion à DFCE
      providerSupport.connect();
      EasyMock.expectLastCall().once();

      // déconnexion de DFCE
      providerSupport.disconnect();
      EasyMock.expectLastCall().once();

      // persistance des modifications - 3 doc * 2 critères
      providerSupport.updateCriterion(EasyMock.anyObject(Document.class),
            EasyMock.anyObject(String.class), EasyMock.anyObject());
      EasyMock.expectLastCall().times(6);

      recuperationDonneesDocuments();

      saeDocumentDao.update(EasyMock.anyObject(Document.class));
      EasyMock.expectLastCall().times(3);

      // trace dans trace Maj
      // 3 documents avec 2 métadonnées valides
      traceDao.addTraceMaj(EasyMock.anyObject(Trace.class));
      EasyMock.expectLastCall().times(6);

      // trace dans trace Maj
      // 3 documents avec 2 métadonnées valides
      traceDao.addTraceRec(EasyMock.anyObject(String.class), EasyMock.anyInt(),
            EasyMock.anyInt(), EasyMock.anyBoolean());
      EasyMock.expectLastCall().times(3);

      traceDao.open("12");
      EasyMock.expectLastCall().once();

      traceDao.close();
      EasyMock.expectLastCall().once();

      EasyMock.replay(traceDao, providerSupport, saeDocumentDao);

      Resource resource = context.getResource("csv/fichier_format_correct");
      File fichier = resource.getFile();

      service
            .launchWithFile(true, fichier, "12", 0, 50, repository.getAbsolutePath());

      EasyMock.verify(providerSupport, traceDao, saeDocumentDao);

      EasyMock.reset(providerSupport, traceDao, saeDocumentDao);

   }

   /**
    *
    */
   private void recuperationDonneesDocuments() {
      // récupération des documents

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

}
