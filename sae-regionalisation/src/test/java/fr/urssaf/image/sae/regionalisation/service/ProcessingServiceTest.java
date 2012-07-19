package fr.urssaf.image.sae.regionalisation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;

import org.apache.commons.lang.math.RandomUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
   public void launch() {

      // connexion à DFCE

      providerSupport.connect();

      EasyMock.expectLastCall().once();

      // récupération des critères

      List<SearchCriterion> searchCriterions = new ArrayList<SearchCriterion>();

      searchCriterions.add(createSearchCriterion());
      searchCriterions.add(createSearchCriterion());
      searchCriterions.add(createSearchCriterion());

      EasyMock.expect(
            searchCriterionDao.getSearchCriteria(EasyMock.anyInt(), EasyMock
                  .anyInt())).andReturn(searchCriterions).andReturn(
            new ArrayList<SearchCriterion>());

      // récupération des métadonnées

      Map<String, Object> metadatas = new HashMap<String, Object>();
      metadatas.put("nne", "value1");
      metadatas.put("nbp", null);
      metadatas.put("metadataUnknown", "valueUnknown");

      EasyMock.expect(
            metadataDao.getMetadatas(EasyMock.anyObject(BigDecimal.class)))
            .andReturn(metadatas).times(3);

      // récupération des documents

      List<Document> docs0 = new ArrayList<Document>();

      Document doc0 = new DocumentImpl();
      doc0.addCriterion("nne", "oldValue");
      doc0.addCriterion("nbp", "oldValue");
      doc0.addCriterion("zzz", "oldValue");

      docs0.add(doc0);
      docs0.add(new DocumentImpl());

      List<Document> docs1 = new ArrayList<Document>();
      docs1.add(new DocumentImpl());

      List<Document> docs2 = new ArrayList<Document>();

      EasyMock.expect(
            saeDocumentDao.getDocuments(EasyMock.anyObject(String.class)))
            .andReturn(docs0).andReturn(docs1).andReturn(docs2);

      // trace dans trace rec

      traceDao.addTraceRec(EasyMock.anyObject(BigDecimal.class), EasyMock
            .anyInt());

      EasyMock.expectLastCall().times(3);

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

      EasyMock.expectLastCall().times(3);

      // déconnexion à DFCE

      providerSupport.disconnect();

      EasyMock.expectLastCall().once();

      EasyMock.replay(saeDocumentDao);
      EasyMock.replay(providerSupport);
      EasyMock.replay(searchCriterionDao);
      EasyMock.replay(metadataDao);
      EasyMock.replay(traceDao);

      service.launch(true, 0, 3);

      // vérification des services
      assertSaeDocumentDao();

   }

   private SearchCriterion createSearchCriterion() {

      SearchCriterion criterion = new SearchCriterion();

      criterion.setId(new BigDecimal(RandomUtils.nextInt()));

      return criterion;
   }

   private void assertSaeDocumentDao() {

      EasyMock.verify(saeDocumentDao);
      EasyMock.verify(providerSupport);
      EasyMock.verify(searchCriterionDao);
      EasyMock.verify(metadataDao);
      EasyMock.verify(traceDao);
   }

}
