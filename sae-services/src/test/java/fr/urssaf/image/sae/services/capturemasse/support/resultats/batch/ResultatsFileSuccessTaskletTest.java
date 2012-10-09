/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.ParserConfigurationException;

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
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.util.XmlValidationUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@DirtiesContext
public class ResultatsFileSuccessTaskletTest {

   @Autowired
   private ApplicationContext applicationContext;

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
      ConcurrentLinkedQueue<UUID> listUuids = new ConcurrentLinkedQueue<UUID>();
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
      context.put(Constantes.DOC_COUNT, 10);

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

      Resource sommaireXSD = applicationContext
            .getResource("xsd_som_res/resultats.xsd");
      URL xsdSchema = sommaireXSD.getURL();

      File resultats = new File(ecdeTestSommaire.getRepEcde(), "resultats.xml");

      try {
         XmlValidationUtils.parse(resultats, xsdSchema);
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      } catch (SAXException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      }
   }

}
