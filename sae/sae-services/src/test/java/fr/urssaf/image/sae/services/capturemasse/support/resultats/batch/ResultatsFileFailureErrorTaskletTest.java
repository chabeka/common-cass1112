/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class ResultatsFileFailureErrorTaskletTest {
   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools tools;

   private EcdeTestSommaire testSommaire;

   @Before
   public void init() {
      testSommaire = tools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         tools.cleanEcdeTestSommaire(testSommaire);
      } catch (IOException e) {
         // rien à faire
      }
   }

   @Test
   public void testLancementStep() throws IOException {

      File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      ExecutionContext context = new ExecutionContext();
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      CaptureMasseSommaireDocumentException exception = new CaptureMasseSommaireDocumentException(
            3, new CaptureMasseSommaireFormatValidationException(testSommaire
                  .getUrlEcde().toString(), new Exception(
                  "erreur enregistrement null")));
      context.put(Constantes.DOC_EXCEPTION, exception);

      launcher.launchStep("finBloquant", context);

      File resultats = new File(testSommaire.getRepEcde(), "resultats.xml");
      
      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
            .exists());
      Assert.assertTrue("le fichier resultats.xml doit etre non vide",
            resultats.length() > 0);
   }
}
