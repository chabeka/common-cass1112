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
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;

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

   @Autowired
   private InsertionPoolThreadExecutor executor;

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

   // T
   @Test
   public void testLancement() throws Exception {

      ExecutionContext context = new ExecutionContext();

      context.put(Constantes.DOC_COUNT, 10);
      context.put(Constantes.RESTITUTION_UUIDS, false);

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());

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
            "sommaire/sommaire_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
      context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());

      // Liste des documents intégrés
      CaptureMasseIntegratedDocument doc1 = new CaptureMasseIntegratedDocument();
      doc1.setDocumentFile(null);
      doc1.setIdentifiant(UUID.randomUUID());
      doc1.setIndex(0);
      CaptureMasseIntegratedDocument doc2 = new CaptureMasseIntegratedDocument();
      doc2.setDocumentFile(null);
      doc2.setIdentifiant(UUID.randomUUID());
      doc2.setIndex(1);
      CaptureMasseIntegratedDocument doc3 = new CaptureMasseIntegratedDocument();
      doc3.setDocumentFile(null);
      doc3.setIdentifiant(UUID.randomUUID());
      doc3.setIndex(2);
      executor.getIntegratedDocuments().add(doc1);
      executor.getIntegratedDocuments().add(doc2);
      executor.getIntegratedDocuments().add(doc3);

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
         StaxValidateUtils.parse(resultats, xsdSchema);
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      } catch (SAXException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      }
   }

}
