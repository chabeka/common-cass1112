package fr.urssaf.image.sae.services.executable.traitementmasse;

import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.executable.service.SAEServiceProvider;

@SuppressWarnings("PMD.MethodNamingConventions")
public class TraitementMasseMainTest {

   private TraitementMasseMain instance;

   private TraitementAsynchroneService traitementService;

   @Before
   public void before() throws CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, CaptureEcdeWriteFileEx {
      instance = new TraitementMasseMain(
            "/applicationContext-sae-services-executable-test.xml");

      traitementService = SAEServiceProvider
            .getInstanceTraitementAsynchroneService();

   }

   @After
   public void after() {

      EasyMock.reset(traitementService);

   }

   @Test
   public void captureMasseMain_success() {

      String[] args = new String[] { UUID.randomUUID().toString(),
            "src/test/resources/config_sae.properties", "context_log" };

      instance.execute(args);

   }

   @Test
   public void captureMasseMain_failure_empty_sommaire() {

      String[] args = new String[0];

      try {

         instance.execute(args);

         Assert
               .fail("le test doit échouer car le sommaire.xml n'est pas renseigné");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(
               "L'identifiant du traitement de masse doit être renseigné.", e
                     .getMessage());
      }

   }

   @Test
   public void captureMasseMain_failure_empty_configSAE() {

      String[] args = new String[] { "sommaire.xml" };

      try {

         instance.execute(args);

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

         instance.execute(args);

         Assert
               .fail("le test doit échouer car l'identifiant du contexte de LOGBACK n'est pas renseigné");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(
               "L'identifiant du contexte de log doit être renseigné.", e
                     .getMessage());
      }

   }
}
