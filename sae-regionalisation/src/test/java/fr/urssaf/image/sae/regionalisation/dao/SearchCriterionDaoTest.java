package fr.urssaf.image.sae.regionalisation.dao;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-regionalisation-service-test.xml")
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
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
   public void getSearchCriteria_partiel() {

      List<SearchCriterion> searchCriterions = dao.getSearchCriteria(1, 5);

      Assert.assertEquals("le nombre de resultats de la requête est inattendu",
            5, searchCriterions.size());

      assertSearchCriterion(searchCriterions.get(0), 1, "lucene1", true);
      assertSearchCriterion(searchCriterions.get(1), 2, "lucene2", false);
      assertSearchCriterion(searchCriterions.get(2), 3, "lucene3", false);
      assertSearchCriterion(searchCriterions.get(3), 4, "lucene4", false);
      assertSearchCriterion(searchCriterions.get(4), 5, "lucene5", false);

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

   @Test
   @Transactional
   public void save() {

      SearchCriterion searchCriterion = new SearchCriterion();
      searchCriterion.setLucene("new lucene");
      searchCriterion.setUpdated(true);

      dao.save(searchCriterion);

      SearchCriterion newCriterion = dao.find(searchCriterion.getId());

      assertSearchCriterion(newCriterion, searchCriterion.getId().intValue(),
            searchCriterion.getLucene(), searchCriterion.isUpdated());

   }

}
