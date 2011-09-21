package fr.urssaf.image.tests.dfcetest;

import static org.junit.Assert.*;
import net.docubase.toolkit.model.base.Base;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import fr.urssaf.image.tests.dfcetest.helpers.DocubaseHelper;
import fr.urssaf.image.tests.dfcetest.helpers.NcotiHelper;

/**
 * Création/Suppression d'une base de type NCOTI.
 * 
 */
public class CreateNcotiTest extends AbstractNcotiTest {

   @Test
   public void create() throws Exception {
      Base base = NcotiHelper.createOrReplaceBase(BASE_ID);
      assertNotNull(base);
      assertNotNull(base.getBaseCategories());
      // getBaseCategories() renvoie un hashset vide en 0.9.1
      assertEquals(8, base.getBaseCategories().size());
      
      // FIXME : comment récupérer la description ? 
      // Il y a un setter mais pas de getter
      //assertEquals("Base des cotisants", base.);
   }

   @Test
   public void drop() throws Exception {
      DocubaseHelper.dropBase(BASE_ID);
      try {
         DocubaseHelper.getBase(BASE_ID);
         fail("La base " + BASE_ID + "aurait du être supprimée");
      } catch (RuntimeException e) {
         if (!StringUtils.contains(e.getMessage(), "introuvable")) {
            throw e;
         } else {
            // OK, la base a bien été supprimée
         }
      }
   }
}
