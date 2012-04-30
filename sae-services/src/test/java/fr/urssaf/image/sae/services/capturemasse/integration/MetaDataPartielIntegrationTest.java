/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-test.xml",
      "/applicationContext-sae-services-integration-test.xml",
      "/applicationContext-sae-traitement-masse-writer-test.xml" })
@DirtiesContext
public class MetaDataPartielIntegrationTest {

   private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
         "yyyy-MM-dd");

   @Autowired
   private SAECaptureMasseService service;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(MetaDataPartielIntegrationTest.class);

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      LOGGER.debug("initialisation du répertoire de traitetement :"
            + ecdeTestSommaire.getRepEcde());
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
   public void testLancement() throws ConnectionServiceEx, DeletionServiceEx,
         InsertionServiceEx, IOException, JAXBException, SAXException {

      initDatas();

      ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
            .getUrlEcde(), UUID.randomUUID());

      Assert.assertTrue("le traitement doit etre en succès", exitStatus
            .isSucces());

      checkFiles();

   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/metadatapartiel/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/metadatapartiel/documents/doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

   }

   private void checkFiles() throws IOException {

      Properties result = new Properties();

      File datas = new File(ecdeTestSommaire.getRepEcde(), "datas.properties");

      if (!datas.exists()) {
         Assert.fail("le fichier contenant les données n'existe pas");
      }

      FileInputStream stream = new FileInputStream(datas);

      try {
         result.load(stream);
      } finally {
         stream.close();
      }

      Map<String, String> waited = waitedDatas();

      Assert
            .assertTrue(
                  "tous les éléments présents dans le résultat attendu doit etre dans l'obtenu",
                  waited.keySet().containsAll(result.keySet()));
      Assert
            .assertTrue(
                  "tous les éléments présents dans le résultat obtenu doit etre dans l'attendu",
                  result.keySet().containsAll(waited.keySet()));

      checkValues(waited, result);
   }

   /**
    * @param waited
    * @param result
    */
   private void checkValues(Map<String, String> waited, Properties result) {

      for (String code : waited.keySet()) {
         Assert.assertEquals("le code " + code
               + " attendu et obtenu doivent etre identiques", (String) waited
               .get(code), result.getProperty(code));
      }

   }

   private Map<String, String> waitedDatas() throws IOException {

      Properties debutTraitement = new Properties();
      File debut = new File(ecdeTestSommaire.getRepEcde(),
            "debut_traitement.flag");
      FileInputStream debutStream = new FileInputStream(debut);

      try {
         debutTraitement.load(debutStream);
      } finally {
         debutStream.close();
      }

      Map<String, String> datas = new HashMap<String, String>();
      Date startDate = new Date();
      datas.put("ApplicationProductrice", "ADELAIDE");
      datas.put("CodeActivite", "3");
      datas.put("CodeFonction", "2");
      datas.put("CodeOrganismeGestionnaire", "CER69");
      datas.put("CodeOrganismeProprietaire", "UR750");
      datas.put("CodeRND", "2.3.1.1.12");
      datas.put("ContratDeService", "ATT_PROD_001");
      datas.put("DateArchivage", FORMAT.format(startDate));
      datas.put("DateCreation", "2011-09-08");
      datas.put("DateDebutConservation", "2011-09-02");
      datas.put("DateFinConservation", "2016-08-31");
      datas.put("FormatFichier", "fmt/354");
      datas.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      datas.put("IdTraitementMasseInterne", debutTraitement
            .getProperty("idTraitementMasse"));
      datas.put("NbPages", "2");
      datas.put("NomFichier", "doc1.PDF");
      datas.put("Titre", "Attestation de vigilance");
      datas.put("TypeHash", "SHA-1");
      datas.put("VersionRND", "5.3");
      datas.put("DocumentVirtuel", "false");

      return datas;

   }
}
