/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capturemasse-test.xml" })
public class EcdeSommaireFileSupportTest {

   @Autowired
   private EcdeSommaireFileSupport support;

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
   public void testSommaireObligatoire() {
      try {
         support.convertURLtoFile(null);
      } catch (CaptureMasseSommaireEcdeURLException e) {
         Assert.fail("On doit sortir avant par vérification aspect");
      } catch (CaptureMasseSommaireFileNotFoundException e) {
         Assert.fail("On doit sortir avant par vérification aspect");
      }

      Assert.fail("On doit sortir avant par vérification aspect");
   }

   @Test(expected = CaptureMasseSommaireEcdeURLException.class)
   public void testUrlEcdeIncorrecte()
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException, URISyntaxException {

      URI sommaireURL = new URI("ecd://test.eronne");

      support.convertURLtoFile(sommaireURL);
   }

   @Test(expected = CaptureMasseSommaireFileNotFoundException.class)
   public void testUrlEcdeExisteFichierInexistant()
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException, URISyntaxException {

      support.convertURLtoFile(ecdeTestSommaire.getUrlEcde());
   }

}
