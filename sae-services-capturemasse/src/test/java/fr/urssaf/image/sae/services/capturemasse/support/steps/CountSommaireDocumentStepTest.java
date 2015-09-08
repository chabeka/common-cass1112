/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.steps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-capturemasse-test.xml" })
public class CountSommaireDocumentStepTest {

   @Autowired
   private JobLauncherTestUtils launcher;

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

   @Test
   public void testNbreDocs() throws IOException, InsertionServiceEx {

      // création de l'arbo
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_success.xml");
      FileOutputStream fos = null;

      try {
         fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         ExecutionContext context = new ExecutionContext();
         context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
               .toString());
         context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

         JobExecution execution = launcher
               .launchStep("compteElements", context);

         Assert.assertEquals("le step doit etre completed", new ExitStatus(
               "DOCS"), execution.getExitStatus());

         int nbreDocs = execution.getExecutionContext().getInt(
               Constantes.DOC_COUNT);

         Assert
               .assertEquals("il doit y avoir 3 éléments présents", 3, nbreDocs);

      } finally {

         if (fos != null) {
            try {
               fos.close();
            } catch (Exception e) {
               // nothing to do
            }
         }
      }
   }
}
