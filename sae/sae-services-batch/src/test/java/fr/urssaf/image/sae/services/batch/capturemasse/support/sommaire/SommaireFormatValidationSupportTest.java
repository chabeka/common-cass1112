/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml" })
public class SommaireFormatValidationSupportTest {

   @Autowired
   private SommaireFormatValidationSupport support;

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
         // rien à faire
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeObligatoire()
         throws CaptureMasseSommaireFormatValidationException {

      support.validationSommaire(null);
      Assert.fail("sortie aspect attendue");

   }

   @Test
   public void testSommaireErrone() throws IOException,
         CaptureMasseSommaireFormatValidationException {

      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      File sommaire = new File(ecdeDirectory, "sommaire.xml");

      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_format_failure.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      try {

         support.validationSommaire(sommaire);

         Assert
               .fail("la validation doit lever une exception de type CaptureMasseSommaireFormatValidationException");

      } catch (CaptureMasseSommaireFormatValidationException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Aucun document du sommaire ne sera intégré dans le SAE.", e
                     .getMessage());
      }

   }

   @Test
   public void testSommaireValide() {

      try {

         File ecdeDirectory = ecdeTestSommaire.getRepEcde();
         File sommaire = new File(ecdeDirectory, "sommaire.xml");

         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         support.validationSommaire(sommaire);
      } catch (IOException e) {
         Assert.fail("le fichier sommaire.xml doit être valide");
      } catch (CaptureMasseSommaireFormatValidationException e) {
         Assert.fail("le fichier sommaire.xml doit être valide");
      }

   }

   @Test(expected = IllegalArgumentException.class)
   public void testFichierSommaireBatchFichierNull()
         throws CaptureMasseSommaireFormatValidationException {

      support.validerModeBatch(null, "RR");

      Assert.fail("exception attendue");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testFichierSommaireBatchModeBatchVide()
         throws CaptureMasseSommaireFormatValidationException {

      support.validerModeBatch(new File(""), "");

      Assert.fail("exception attendue");
   }

   @Test(expected = CaptureMasseSommaireFormatValidationException.class)
   public void testBatchModeNonAttendu()
         throws CaptureMasseSommaireFormatValidationException {

      File sommaire = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      support.validerModeBatch(sommaire, "RR");

   }

   @Test
   public void testBatchModeValide() {

      File sommaire = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      try {
         support.validerModeBatch(sommaire, "TOUT_OU_RIEN");
      } catch (CaptureMasseSommaireFormatValidationException e) {
         Assert.fail("on attend un retour valide");
      }

   }

}
