package fr.urssaf.image.sae.services.executable.capturemasse;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.executable.service.SAEServiceProvider;

@SuppressWarnings("PMD.MethodNamingConventions")
public class CaptureMasseMainTest {

   private CaptureMasseMain instance;

   private SAEBulkCaptureService bulkCapture;

   @Before
   public void before() throws CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, CaptureEcdeWriteFileEx {
      instance = new CaptureMasseMain(
            "/applicationContext-sae-services-executable-test.xml");

      bulkCapture = SAEServiceProvider.getInstanceSAEBulkCaptureService();

   }

   @After
   public void after() {

      EasyMock.reset(bulkCapture);

   }

   @Test
   public void captureMasseMain_success() {

      String[] args = new String[] {
            "ecde://ecde.cer69.recouv/DCL001/19991231/3/sommaire.xml",
            "src/test/resources/config_sae.properties", "context_log" };

      instance.captureMasse(args);

   }

   @Test
   public void captureMasseMain_failure_empty_sommaire() {

      String[] args = new String[0];

      try {

         instance.captureMasse(args);

         Assert
               .fail("le test doit échouer car le sommaire.xml n'est pas renseigné");

      } catch (IllegalArgumentException e) {

         Assert
               .assertEquals(
                     "L'URL ECDE du fichier sommaire.xml décrivant le traitement de capture en masse doit être renseigné.",
                     e.getMessage());
      }

   }

   @Test
   public void captureMasseMain_failure_empty_configSAE() {

      String[] args = new String[] { "sommaire.xml" };

      try {

         instance.captureMasse(args);

         Assert
               .fail("le test doit échouer car le fichier de configuration du SAE n'est pas renseigné");

      } catch (IllegalArgumentException e) {

         Assert
               .assertEquals(
                     "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.",
                     e.getMessage());
      }

   }

   @Test
   public void captureMasseMain_failure_empty_uuidLogBack() {

      String[] args = new String[] { "sommaire.xml", "configSAE" };

      try {

         instance.captureMasse(args);

         Assert
               .fail("le test doit échouer car l'identifiant du contexte de LOGBACK n'est pas renseigné");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(
               "L'identifiant du contexte de log doit être renseigné.", e
                     .getMessage());
      }

   }
}
