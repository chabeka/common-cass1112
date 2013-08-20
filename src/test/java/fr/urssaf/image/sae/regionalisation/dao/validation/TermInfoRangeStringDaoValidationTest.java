/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.dao.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.dao.TermInfoRangeStringDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-mock-test.xml",
      "/applicationContext-sae-regionalisation-cassandra-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class TermInfoRangeStringDaoValidationTest {

   @Autowired
   private TermInfoRangeStringDao dao;

   @Test
   public void test_query_first_obligatoire() {
      try {
         dao.getQuery(null, null, null);

         Assert.fail("on attend une exception");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message concerne l'argument first", exception
               .getMessage().contains("first"));
      } catch (Exception exception) {
         Assert.fail("on attend une IllegalArgumentException");
      }
   }

   @Test
   public void test_query_last_obligatoire() {
      try {
         dao.getQuery("aa", null, null);

         Assert.fail("on attend une exception");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message concerne l'argument first", exception
               .getMessage().contains("last"));
      } catch (Exception exception) {
         Assert.fail("on attend une IllegalArgumentException");
      }
   }

   @Test
   public void test_query_indexName_obligatoire() {
      try {
         dao.getQuery("aa", "bb", null);

         Assert.fail("on attend une exception");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message concerne l'argument first", exception
               .getMessage().contains("indexName"));
      } catch (Exception exception) {
         Assert.fail("on attend une IllegalArgumentException");
      }
   }

   @Test
   public void test_query_first_greaterThan_last_obligatoire() {
      try {
         dao.getQuery("z", "e", "nce");

         Assert.fail("on attend une exception");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue(
               "le message concerne l'ordre des argument (first > last)",
               exception.getMessage().contains("first")
                     && exception.getMessage().contains("last"));
      } catch (Exception exception) {
         Assert.fail("on attend une IllegalArgumentException");
      }
   }

}
