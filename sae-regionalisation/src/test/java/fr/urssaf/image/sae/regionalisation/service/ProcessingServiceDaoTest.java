package fr.urssaf.image.sae.regionalisation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;
import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.MetadataDao;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.SearchCriterionDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-service-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ProcessingServiceDaoTest {

   @Autowired
   private SaeDocumentDao saeDocumentDao;

   @Autowired
   private ProcessingService service;

   @Autowired
   private SearchCriterionDao searchCriterionDao;

   @Autowired
   private TraceDao traceDao;

   @Autowired
   private ServiceProviderSupport providerSupport;

   @After
   public void after() {

      EasyMock.reset(saeDocumentDao);
      EasyMock.reset(providerSupport);

   }

   @Before
   public void before() {

      // connexion à DFCE

      providerSupport.connect();

      EasyMock.expectLastCall().once();

      // récupération des documents

      List<Document> docs = new ArrayList<Document>();

      Document doc = createDocument();

      for (String metadata : MetadataDao.METADATAS) {
         doc.addCriterion(metadata, metadata + "_old");
      }

      docs.add(doc);

      EasyMock.expect(
            saeDocumentDao.getDocuments(EasyMock.anyObject(String.class)))
            .andReturn(docs).anyTimes();

      // persistance des modifications

      providerSupport.updateCriterion(EasyMock.anyObject(Document.class),
            EasyMock.anyObject(String.class), EasyMock.anyObject());

      EasyMock.expectLastCall().atLeastOnce();

      saeDocumentDao.update(EasyMock.anyObject(Document.class));

      EasyMock.expectLastCall().atLeastOnce();

      // déconnexion à DFCE

      providerSupport.disconnect();

      EasyMock.expectLastCall().once();

      EasyMock.replay(saeDocumentDao);
      EasyMock.replay(providerSupport);
   }

   @Test
   @Transactional
   public void launch_global() {

      service.launch(true, 1, 5);

      // vérification de la mise à jour des critères de recherches
      assertSearchCriterion(0, false);
      assertSearchCriterion(1, true);
      assertSearchCriterion(2, true);
      assertSearchCriterion(3, false);
      assertSearchCriterion(4, true);
      assertSearchCriterion(5, true);
      assertSearchCriterion(6, true);
      assertSearchCriterion(7, true);
      assertSearchCriterion(8, true);
      assertSearchCriterion(9, false);
   }

   @Test
   @Transactional
   public void launch_detail() {

      int idCritere = 0;

      service.launch(true, idCritere, 1);

      // vérification de la mise à jour des critères de recherches
      assertSearchCriterion(idCritere, true);

      // vérification des traces

      int rec = traceDao.findNbreDocs(BigDecimal.valueOf(idCritere));

      Assert.assertEquals("le trace rec " + idCritere
            + " a un nombre de documents inattendu", 1, rec);

      List<Trace> traces = traceDao.findTraceMajByCriterion(BigDecimal
            .valueOf(idCritere));

      Assert.assertEquals("le nombre de traces de mise à jour de " + idCritere
            + " est inattendu", 5, traces.size());

      // on trie traces en fonction de la métadonnée et de l'index
      Comparator<Trace> comparator = new Comparator<Trace>() {
         @Override
         public int compare(Trace trace1, Trace trace2) {

            return trace1.getMetaName().compareTo(trace2.getMetaName());

         }
      };
      Collections.sort(traces, comparator);

      assertTrace(traces.get(0), "dre", "dre_old", "2007-07-12");
      assertTrace(traces.get(1), "nbp", "nbp_old", "4");
      //impossible de connaitre l'ordre
      //assertTrace(traces.get(2), "nne", "?", "?");
      //assertTrace(traces.get(3), "nne", "?", "?");
      assertTrace(traces.get(4), "npe", "npe_old", "123854");

   }

   private void assertTrace(Trace trace, String expectedMetadata,
         String expectedOldValue, String expectedNewValue) {

      Assert.assertEquals("la métadonnée est inattendue", expectedMetadata,
            trace.getMetaName());

      Assert.assertEquals("la nouvelle valeur de la métadonnée est inattendue",
            expectedOldValue, trace.getOldValue());

      Assert.assertEquals("la nouvelle valeur de la métadonnée est inattendue",
            expectedNewValue, trace.getNewValue());

   }

   private void assertSearchCriterion(int idCritere, boolean expectedState) {

      SearchCriterion criterion = searchCriterionDao.find(BigDecimal
            .valueOf(idCritere));

      Assert.assertEquals("le critère " + idCritere
            + " a un état TRAITE inattendu", expectedState, criterion
            .isUpdated());
   }

   private Document createDocument() {

      Document document = new DocumentImpl();

      document.setUuid(UUID.randomUUID());

      return document;
   }

}
