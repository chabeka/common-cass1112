package fr.urssaf.image.sae.regionalisation.dao;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-regionalisation-service-test.xml")
@SuppressWarnings("PMD.MethodNamingConventions")
public class SearchCriterionDaoTest {

   @Autowired
   private SearchCriterionDao dao;

   private void assertSearchCriterion(SearchCriterion criterion,
         int idExpected, String expectedLucene, boolean expectedUpdated) {

      Assert.assertEquals("l'identifiant est inattendu", idExpected, criterion
            .getId().intValue());

      Assert.assertEquals("la requête lucène est inattendue", expectedLucene,
            criterion.getLucene());

      Assert.assertEquals("le flag traite est inattendu", expectedUpdated,
            criterion.isUpdated());

   }

   @Test
   public void getSearchCriteria() {

      List<SearchCriterion> searchCriterions = dao.getSearchCriteria(1, 2);

      Assert.assertEquals("le nombre de resultats de la requête est inattendu",
            2, searchCriterions.size());

      assertSearchCriterion(searchCriterions.get(0), 1, "lucene1", true);
      assertSearchCriterion(searchCriterions.get(1), 2, "lucene2", false);

   }

   @Test
   @Transactional
   public void updateSearchCriterion() {

      SearchCriterion oldCriterion = dao.find(new BigDecimal(4));

      assertSearchCriterion(oldCriterion, 4, "lucene4", false);

      dao.updateSearchCriterion(new BigDecimal(4));

      SearchCriterion newCriterion = dao.find(new BigDecimal(4));

      assertSearchCriterion(newCriterion, 4, "lucene4", true);

   }

}
