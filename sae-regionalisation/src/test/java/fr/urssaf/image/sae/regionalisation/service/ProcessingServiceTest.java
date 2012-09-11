package fr.urssaf.image.sae.regionalisation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.apache.commons.lang.math.RandomUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;
import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.MetadataDao;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.SearchCriterionDao;
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
   private SearchCriterionDao searchCriterionDao;

   @Autowired
   private MetadataDao metadataDao;

   @Autowired
   private TraceDao traceDao;

   @Autowired
   private ServiceProviderSupport providerSupport;

   @After
   public void after() {

      EasyMock.reset(saeDocumentDao);
      EasyMock.reset(providerSupport);
      EasyMock.reset(searchCriterionDao);
      EasyMock.reset(metadataDao);
      EasyMock.reset(traceDao);
   }

   @Test
   public void launch_mise_a_jour() {

      EasyMock.reset(saeDocumentDao);
      EasyMock.reset(providerSupport);
      EasyMock.reset(searchCriterionDao);
      EasyMock.reset(metadataDao);
      EasyMock.reset(traceDao);
      
      // connexion à DFCE

      providerSupport.connect();

      EasyMock.expectLastCall().once();

      // récupération des critères

      EasyMock.expect(
            searchCriterionDao
                  .getSearchCriteria(EasyMock.eq(0), EasyMock.eq(5)))
            .andReturn(createSearchCriterions(5));

      EasyMock.expect(
            searchCriterionDao
                  .getSearchCriteria(EasyMock.eq(5), EasyMock.eq(2)))
            .andReturn(createSearchCriterions(2));

      launchCommun();

      // trace dans trace rec

      traceDao.addTraceRec(EasyMock.anyObject(BigDecimal.class), EasyMock
            .anyInt(), EasyMock.eq(true));

      EasyMock.expectLastCall().times(7);

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

      // mise à jour des critères de recherche
      searchCriterionDao.updateSearchCriterion(EasyMock
            .anyObject(BigDecimal.class));

      EasyMock.expectLastCall().times(7);

      // déconnexion à DFCE

      providerSupport.disconnect();

      EasyMock.expectLastCall().once();

      EasyMock.replay(saeDocumentDao);
      EasyMock.replay(providerSupport);
      EasyMock.replay(searchCriterionDao);
      EasyMock.replay(metadataDao);
      EasyMock.replay(traceDao);

      service.launch(true, 0, 7);

      // vérification des services
      assertSaeDocumentDao();

   }

   @Test
   public void launch_tir_a_blanc() {

      // connexion à DFCE

      providerSupport.connect();

      EasyMock.expectLastCall().once();

      // récupération des critères

      EasyMock.expect(
            searchCriterionDao
                  .getSearchCriteria(EasyMock.eq(0), EasyMock.eq(5)))
            .andReturn(createSearchCriterions(5));

      EasyMock.expect(
            searchCriterionDao
                  .getSearchCriteria(EasyMock.eq(5), EasyMock.eq(2)))
            .andReturn(createSearchCriterions(2));

      launchCommun();

      // trace dans trace rec

      traceDao.addTraceRec(EasyMock.anyObject(BigDecimal.class), EasyMock
            .anyInt(), EasyMock.eq(false));

      EasyMock.expectLastCall().times(7);

      // aucune trace

      // persistance des modifications

      // aucune persistance

      // trace dans trace Maj

      // aucune trace

      // mise à jour des critères de recherche

      // aucune mise à jour

      // déconnexion à DFCE

      providerSupport.disconnect();

      EasyMock.expectLastCall().once();

      EasyMock.replay(saeDocumentDao);
      EasyMock.replay(providerSupport);
      EasyMock.replay(searchCriterionDao);
      EasyMock.replay(metadataDao);
      EasyMock.replay(traceDao);

      service.launch(false, 0, 7);

      // vérification des services
      assertSaeDocumentDao();

   }

   private void launchCommun() {

      // récupération des métadonnées

      Map<String, Object> metadatas = new HashMap<String, Object>();
      metadatas.put("nne", "value1");
      metadatas.put("nbp", null);
      metadatas.put("metadataUnknown", "valueUnknown");

      EasyMock.expect(
            metadataDao.getMetadatas(EasyMock.anyObject(BigDecimal.class)))
            .andReturn(metadatas).times(7);

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
            .andReturn(docs0).andReturn(docs1).andReturn(docs2).times(5);
   }

   private List<SearchCriterion> createSearchCriterions(int nbCriterion) {

      List<SearchCriterion> criterions = new ArrayList<SearchCriterion>();

      for (int i = 0; i < nbCriterion; i++) {

         SearchCriterion criterion = new SearchCriterion();

         criterion.setId(new BigDecimal(RandomUtils.nextInt()));
         criterion
               .setLucene("lucene request n°" + criterion.getId().intValue());

         criterions.add(criterion);

      }

      return criterions;
   }

   private Document createDocument() {

      Document document = new DocumentImpl();

      document.setUuid(UUID.randomUUID());

      return document;
   }

   private void assertSaeDocumentDao() {

      EasyMock.verify(saeDocumentDao);
      EasyMock.verify(providerSupport);
      EasyMock.verify(searchCriterionDao);
      EasyMock.verify(metadataDao);
      EasyMock.verify(traceDao);
   }

}
