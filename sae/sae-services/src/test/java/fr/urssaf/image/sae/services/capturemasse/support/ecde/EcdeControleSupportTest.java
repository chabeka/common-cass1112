/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class EcdeControleSupportTest {

   @Autowired
   private EcdeControleSupport support;

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

   /**
    * Test Argument sommaireFile obligatoire
    */
   @Test(expected = IllegalArgumentException.class)
   public void testSommaireFileObligatoire() {

      try {
         support.checkEcdeWrite(null);
      } catch (CaptureMasseEcdeWriteFileException e) {
         Assert.fail("On doit sortir avant par vérification aspect");
      }

      Assert.fail("On doit sortir avant par vérification aspect");
   }

   @Test
   public void testRepertoireNonInscriptible() {

      try {
         File repEcde = ecdeTestSommaire.getRepEcde();
         File fileSommaire = new File(repEcde, "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(fileSommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);
      } catch (FileNotFoundException e) {
         Assert.fail();
      } catch (IOException e) {
         Assert.fail();
      }

   }

}
