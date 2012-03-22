/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class ResultatsFileFailureTaskletTest {

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
            3, new NullPointerException("erreur sur l'enregistrement en cours"));
      context.put(Constantes.DOC_EXCEPTION, exception);

      List<UUID> list = new ArrayList<UUID>();
      for (int i = 0; i < 21; i++) {
         list.add(UUID.randomUUID());
      }
      context.put(Constantes.INTEG_DOCS, list);

      launcher.launchStep("finErreur", context);

      File resultats = new File(testSommaire.getRepEcde(), "resultats.xml");

      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
            .exists());
      Assert.assertTrue("le fichier resultats.xml doit etre non vide",
            resultats.length() > 0);
   }
}
