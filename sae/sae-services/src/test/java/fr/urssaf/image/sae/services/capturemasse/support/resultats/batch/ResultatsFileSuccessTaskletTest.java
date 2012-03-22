/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class ResultatsFileSuccessTaskletTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private JobLauncherTestUtils launcher;

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
   public void testLancement() throws Exception {

      ExecutionContext context = new ExecutionContext();
      List<UUID> listUuids = new ArrayList<UUID>();
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      listUuids.add(UUID.randomUUID());
      context.put(Constantes.INTEG_DOCS, listUuids);

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      JobExecution execution = launcher.launchStep("finSucces", context);

      File resultatsFile = new File(ecdeTestSommaire.getRepEcde(),
            "resultats.xml");

      Assert.assertTrue("le step doit etre COMPLETED", ExitStatus.COMPLETED
            .equals(execution.getExitStatus()));
      Assert.assertTrue("le fichier resultats.xml doit exister", resultatsFile
            .exists());
      Assert.assertTrue("le fichier doit etre non vide",
            resultatsFile.length() > 0);
   }

}
