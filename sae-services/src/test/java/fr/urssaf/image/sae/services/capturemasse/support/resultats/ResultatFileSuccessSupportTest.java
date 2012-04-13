/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class ResultatFileSuccessSupportTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private ResultatFileSuccessSupport support;

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
   public void testEcdeDirectoryObligatoire() {

      support.writeResultatsFile(null,
            new ArrayList<CaptureMasseIntegratedDocument>(), 0);
      Assert.fail("Vérification aspect doit se déclencher");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDocumentsCountObligatoire() {

      support.writeResultatsFile(new File("fichier"),
            new ArrayList<CaptureMasseIntegratedDocument>(), -1);
      Assert.fail("Vérification aspect doit se déclencher");
   }

   @Test
   public void testWriteFileSuccess() {

      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      support.writeResultatsFile(ecdeDirectory,
            new ArrayList<CaptureMasseIntegratedDocument>(), 10);

      File resultatsFile = new File(ecdeDirectory, "resultats.xml");

      Assert.assertTrue("le fichier resultats.xml doit exister", resultatsFile
            .exists());
      Assert.assertTrue("Le fichier doit avoir une taille > 0", resultatsFile
            .length() > 0);

   }

}
