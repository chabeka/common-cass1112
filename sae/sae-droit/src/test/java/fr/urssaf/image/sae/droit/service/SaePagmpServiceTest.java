/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Pagmp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePagmpServiceTest {

   @Autowired
   private SaePagmpService service;

   @Test
   public void testCreatePagmpObligatoire() {

      try {
         service.createPagmp(null);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient pagm", e
               .getMessage().contains("pagmp"));
      }

   }

   @Test
   public void testCreateCodePagmpObligatoire() {

      try {
         Pagmp pagmp = new Pagmp();
         pagmp.setDescription("description");
         pagmp.setPrmd("prmd");

         service.createPagmp(pagmp);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient code", e
               .getMessage().contains("code"));
      }

   }

   @Test
   public void testCreateDescriptionPagmpObligatoire() {

      try {
         Pagmp pagmp = new Pagmp();
         pagmp.setCode("code");
         pagmp.setPrmd("prmd");

         service.createPagmp(pagmp);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient description", e
               .getMessage().contains("description"));
      }

   }

   @Test
   public void testCreatePrmdPagmpObligatoire() {

      try {
         Pagmp pagmp = new Pagmp();
         pagmp.setDescription("description");
         pagmp.setCode("code");

         service.createPagmp(pagmp);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient prmd", e
               .getMessage().contains("prmd"));
      }

   }
}
