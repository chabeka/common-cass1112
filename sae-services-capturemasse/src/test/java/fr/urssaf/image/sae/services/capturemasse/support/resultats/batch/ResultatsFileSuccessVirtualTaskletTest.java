/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
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
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.commons.xml.StaxValidateUtils;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadVirtualExecutor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capturemasse-test.xml" })
@DirtiesContext
public class ResultatsFileSuccessVirtualTaskletTest {

   @Autowired
   private ApplicationContext applicationContext;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private JobLauncherTestUtils launcher;

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   private InsertionPoolThreadVirtualExecutor executor;

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

      ExecutionContext context = new ExecutionContext();

      context.put(Constantes.DOC_COUNT, 3);
      context.put(Constantes.RESTITUTION_UUIDS, false);
      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      JobExecution execution = launcher.launchStep("finSuccesVirtuel", context);

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
         StaxValidateUtils.parse(resultats, xsdSchema);
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      } catch (SAXException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      }
   }

   @Test
   public void testLancementAvecUUIDDansResultat() throws Exception {

      ExecutionContext context = new ExecutionContext();

      context.put(Constantes.DOC_COUNT, 10);
      context.put(Constantes.RESTITUTION_UUIDS, true);

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire_virtuel.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      // Liste des documents intégrés
      CaptureMasseVirtualDocument doc1 = new CaptureMasseVirtualDocument();
      doc1.setUuid(UUID.randomUUID());
      doc1.setIndex(0);
      CaptureMasseVirtualDocument doc2 = new CaptureMasseVirtualDocument();
      doc2.setUuid(UUID.randomUUID());
      doc2.setIndex(1);
      CaptureMasseVirtualDocument doc3 = new CaptureMasseVirtualDocument();
      doc3.setUuid(UUID.randomUUID());
      doc3.setIndex(2);
      executor.getIntegratedDocuments().add(doc1);
      executor.getIntegratedDocuments().add(doc2);
      executor.getIntegratedDocuments().add(doc3);

      JobExecution execution = launcher.launchStep("finSuccesVirtuel", context);

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
         StaxValidateUtils.parse(resultats, xsdSchema);
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      } catch (SAXException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      }
   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);
   }

}
