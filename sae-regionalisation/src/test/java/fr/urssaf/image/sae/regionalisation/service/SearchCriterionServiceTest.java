package fr.urssaf.image.sae.regionalisation.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;
import fr.urssaf.image.sae.regionalisation.dao.MetadataDao;
import fr.urssaf.image.sae.regionalisation.dao.SearchCriterionDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-regionalisation-service-test.xml")
@SuppressWarnings("PMD.MethodNamingConventions")
public class SearchCriterionServiceTest {

   @Autowired
   private MetadataDao metadataDao;

   @Autowired
   private SearchCriterionDao searchCriterionDao;

   @Autowired
   private SearchCriterionService searchCriterionService;

   @Test
   @Transactional
   public void enregistrerSearchCriterion() throws IOException {

      File csv = new File("src/test/resources/csv/metadonnees.csv");
      searchCriterionService.enregistrerSearchCriterion(csv);

      List<SearchCriterion> criterias = searchCriterionDao.getSearchCriteria(7,
            Integer.MAX_VALUE);

      Assert.assertEquals("le nombre de critères est inattendu", 3, criterias
            .size());

      assertSearchCriterion(criterias.get(0), "nce:630000000004296911", false);
      assertSearchCriterion(criterias.get(1), "nci:0023552", false);
      assertSearchCriterion(criterias.get(2), "npe:0015197", false);

      Map<String, Object> metadata0 = metadataDao
            .find(criterias.get(0).getId());
      assertMetadata(metadata0, "cog", "837");
      assertMetadata(metadata0, "nce", "837000000000012658");

      Map<String, Object> metadata1 = metadataDao
            .find(criterias.get(1).getId());
      assertMetadata(metadata1, "cog", "837");
      assertMetadata(metadata1, "nci", "0009586");

      Map<String, Object> metadata2 = metadataDao
            .find(criterias.get(2).getId());
      assertMetadata(metadata2, "cog", "837");
      assertMetadata(metadata2, "npe", "2015197");

   }

   private void assertSearchCriterion(SearchCriterion criterion,
         String expectedLucene, boolean expectedUpdated) {

      Assert.assertEquals("la requête lucène est inattendue", expectedLucene,
            criterion.getLucene());

      Assert.assertEquals("le flag traite est inattendu", expectedUpdated,
            criterion.isUpdated());

   }

   private void assertMetadata(Map<String, Object> values,
         String expectedMetadata, Object expectedValue) {

      Assert.assertTrue("la métadonnées '" + expectedMetadata
            + "' n'est pas retrouvée", values.containsKey(expectedMetadata));

      Assert.assertEquals("la valeur de la métadonnée " + expectedMetadata
            + " est inattendu", expectedValue, values.get(expectedMetadata));

   }

}
