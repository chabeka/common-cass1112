/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Pagma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePagmaServiceTest {

   @Autowired
   private SaePagmaService service;

   @Test
   public void testPagmaObligatoire() {

      try {
         service.createPagma(null);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient pagma", e
               .getMessage().contains("pagma"));
      }

   }

   @Test
   public void testCodePagmaObligatoire() {

      try {
         Pagma pagma = new Pagma();
         pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));

         service.createPagma(pagma);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient code", e
               .getMessage().contains("code"));
      }

   }

   @Test
   public void testActionsUnitairesPagmaObligatoire() {

      try {
         Pagma pagma = new Pagma();
         pagma.setCode("code");

         service.createPagma(pagma);
         Assert.fail("erreur attendue");
      } catch (Exception e) {
         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient actions unitaires",
               e.getMessage().contains("actions unitaires"));
      }

   }

}
