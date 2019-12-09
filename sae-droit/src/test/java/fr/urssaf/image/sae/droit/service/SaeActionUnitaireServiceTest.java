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

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaeActionUnitaireServiceTest {

   @Autowired
   private SaeActionUnitaireService service;

   @Test
   public void testActionUnitaireObligatoire() {

      try {
         service.createActionUnitaire(null);
         Assert.fail("exception attendue");
      } catch (Exception e) {

         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
      }

   }

   @Test
   public void testCodeObligatoire() {

      try {
         ActionUnitaire actionUnitaire = new ActionUnitaire();
         actionUnitaire.setDescription("test création");

         service.createActionUnitaire(actionUnitaire);
         Assert.fail("exception attendue");
      } catch (Exception e) {

         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient code", e
               .getMessage().contains("code"));
      }

   }

   @Test
   public void testDescriptionObligatoire() {

      try {
         ActionUnitaire actionUnitaire = new ActionUnitaire();
         actionUnitaire.setCode("test1");

         service.createActionUnitaire(actionUnitaire);
         Assert.fail("exception attendue");
      } catch (Exception e) {

         Assert.assertEquals("type de l'exception correcte",
               IllegalArgumentException.class, e.getClass());
         Assert.assertTrue("message de l'exception contient description", e
               .getMessage().contains("description"));
      }

   }

}
