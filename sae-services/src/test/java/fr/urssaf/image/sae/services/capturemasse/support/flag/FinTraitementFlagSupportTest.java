/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class FinTraitementFlagSupportTest {

   @Autowired
   private FinTraitementFlagSupport support;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeObligatoire() {

      support.writeFinTraitementFlag(null);

   }

   @Test
   public void testFinFlag() {
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      support.writeFinTraitementFlag(ecdeDirectory);

      File file = new File(ecdeDirectory, "fin_traitement.flag");

      Assert.assertTrue("le fichier fin_traitement.flag doit exister", file
            .exists());
   }

}
