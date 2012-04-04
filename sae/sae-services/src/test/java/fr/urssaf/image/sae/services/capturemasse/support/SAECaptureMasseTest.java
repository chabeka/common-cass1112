/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.util.XmlValidationUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAECaptureMasseTest {

   @Autowired
   private ApplicationContext applicationContext;

   @Autowired
   private SAECaptureMasseService service;

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
         // rien a faire
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testURLObligatoire() {
      service.captureMasse(null, UUID.randomUUID());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testUUIDObligatoire() {
      service.captureMasse(testSommaire.getUrlEcde(), null);
   }

   @Test
   @Ignore
   public void testLancementService() {

      try {
         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         File repertoireEcdeDocuments = new File(testSommaire.getRepEcde(),
               "documents");
         ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
         File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
         fos = new FileOutputStream(fileAttestation1);
         IOUtils.copy(resAttestation1.getInputStream(), fos);

         ExitTraitement exitTraitement = service.captureMasse(testSommaire
               .getUrlEcde(), UUID.randomUUID());

         Assert.assertTrue("l'opération doit etre ok", exitTraitement
               .isSucces());

      } catch (Exception e) {
         Assert.fail("pas d'erreur attendue");
      }

   }

   @Test
   public void testLancementSommaireErroneHashCodeErrone() {

      try {
         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource(
               "sommaire/sommaire_failure_HashIncorrect.xml");
         FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

         File repDocuments = new File(testSommaire.getRepEcde(), "documents");
         ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
         File fileAttestation1 = new File(repDocuments, "doc1.PDF");
         FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

      } catch (Exception e) {
         Assert.fail("pas d'erreur attendue");
      }

      ExitTraitement exitTraitement = service.captureMasse(testSommaire
            .getUrlEcde(), UUID.randomUUID());

      Assert.assertFalse("l'opération doit etre en erreur", exitTraitement
            .isSucces());

      File resultats = new File(testSommaire.getRepEcde(), "resultats.xml");

      Assert.assertTrue("le fichier résultats.xml doit exister", resultats
            .exists());

      Resource sommaireXSD = applicationContext
            .getResource("xsd_som_res/resultats.xsd");

      try {
         URL xsdSchema = sommaireXSD.getURL();
         XmlValidationUtils.parse(resultats, xsdSchema);
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      } catch (SAXException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      } catch (IOException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      }

   }

   @Test
   public void testLancementSommaireInexistant() {

      new File(testSommaire.getRepEcde(), "sommaire.xml");

      ExitTraitement exitTraitement = service.captureMasse(testSommaire
            .getUrlEcde(), UUID.randomUUID());

      Assert.assertFalse("l'opération doit etre en erreur", exitTraitement
            .isSucces());

      File resultats = new File(testSommaire.getRepEcde(), "resultats.xml");

      Assert.assertFalse("le fichier résultats.xml ne doit pas exister",
            resultats.exists());
   }

   @Test
   public void testLancementSommaireFormatErrone() {

      try {
         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource(
               "sommaire/sommaire_format_failure.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         File repDocuments = new File(testSommaire.getRepEcde(), "documents");
         ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
         File fileAttestation1 = new File(repDocuments, "doc1.PDF");
         fos = new FileOutputStream(fileAttestation1);
         IOUtils.copy(resAttestation1.getInputStream(), fos);

         ExitTraitement exitTraitement = service.captureMasse(testSommaire
               .getUrlEcde(), UUID.randomUUID());

         Assert.assertFalse("l'opération doit etre en erreur", exitTraitement
               .isSucces());

         File resultats = new File(testSommaire.getRepEcde(), "resultats.xml");

         Assert.assertTrue("le fichier résultats.xml doit exister", resultats
               .exists());

         System.out.println(FileUtils.readFileToString(resultats));

         Resource sommaireXSD = applicationContext
               .getResource("xsd_som_res/resultats.xsd");
         URL xsdSchema = sommaireXSD.getURL();

         try {
            XmlValidationUtils.parse(resultats, xsdSchema);
         } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Assert.fail("le fichier resultats.xml doit etre valide");
         } catch (SAXException e) {
            e.printStackTrace();
            Assert.fail("le fichier resultats.xml doit etre valide");
         }

      } catch (Exception e) {
         Assert.fail("pas d'erreur attendue");
      }

   }
}
