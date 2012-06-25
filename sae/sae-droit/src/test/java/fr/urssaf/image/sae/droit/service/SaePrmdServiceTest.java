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

import fr.urssaf.image.sae.droit.dao.model.Prmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePrmdServiceTest {

   @Autowired
   private SaePrmdService service;

   @Test
   public void testCreatePrmdObligatoire() {
      try {
         service.createPrmd(null);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient prmd", e
               .getMessage().contains("prmd"));
      }

   }

   @Test
   public void testCreateCodePrmdObligatoire() {
      try {

         Prmd prmd = new Prmd();
         prmd.setDescription("description");
         prmd.setLucene("lucène");

         service.createPrmd(prmd);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient code", e
               .getMessage().contains("code"));
      }

   }

   @Test
   public void testCreateDescriptionObligatoire() {
      try {

         Prmd prmd = new Prmd();
         prmd.setCode("code");
         prmd.setLucene("lucène");

         service.createPrmd(prmd);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient description", e
               .getMessage().contains("description"));
      }
   }

   @Test
   public void testCreateLuceneObligatoire() {
      try {

         Prmd prmd = new Prmd();
         prmd.setCode("code");
         prmd.setDescription("description");

         service.createPrmd(prmd);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient lucène", e
               .getMessage().contains("lucène"));
      }
   }
}
