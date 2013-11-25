/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-test.xml" })
public class FinTraitementTaskletTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private JobLauncherTestUtils launcher;

   private EcdeTestSommaire ecdeTestSommaire;

   @Before
   public void init() throws IOException {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
      initDatas();
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
   public void testLancement() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      String sommaireFilePath = sommaire.getAbsolutePath();

      ExecutionContext context = new ExecutionContext();
      context.put(Constantes.SOMMAIRE_FILE, sommaireFilePath);
      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());

      JobExecution execution = launcher.launchStep("finTraitement", context);

      Assert.assertEquals("le statut doit être à COMPLETED",
            ExitStatus.COMPLETED, execution.getExitStatus());
   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);
   }
}
