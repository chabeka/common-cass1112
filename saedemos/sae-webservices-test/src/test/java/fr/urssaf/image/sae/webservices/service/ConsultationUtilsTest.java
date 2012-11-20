package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.axiom.util.base64.Base64Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.EcdeManager;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaireResponseType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationResponseType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModelFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ConsultationUtilsTest {

   private static final Logger LOG = LoggerFactory.getLogger(ConsultationUtilsTest.class);
   
   private static final String CONTRAT_SERVICE = "ATT_PROD_001";
   private static final String FILE_NAME = "attestation.pdf";
   private static final String PDF_FILE_PATH = "src/test/resources/storage/attestation.pdf";
   private static final String DATE_DEB_CONSERV = "2011-09-02";
   private static final String DATE_RECEPT = "1999-11-25";
   private static final String TYPE_HASH = "SHA-1";
   private static final String TITRE = "Attestation de vigilance";
   private static final String DATE_CREATION = "2012-01-01";
   private static final String FORMAT_FICHIER = "fmt/354";
   private static final String NB_PAGE = "2";
   private static final String VERSION_RND = "11.1";
   private static final String APPLICATION_PRODUCTRICE = "ADELAIDE";
   private static final String CODE_ORGA_PROPRIO = "CER69";
   private static final String CODE_ORGA_GESTION = "UR750";
   private static final String CODE_RND = "2.3.1.1.12";
   
   
   @Autowired
   private ArchivageUnitaireService archivageService;
   
   
   /**
    * Préparation du jeu de données
    * 
    * @throws IOException
    *            exception levée si problème avec le document à envoyer
    */
   @Test
   @Ignore
   public final void prepareDatas() throws IOException {
      EcdeManager.cleanEcde();

      // enregistrement du fichier dans l'ECDE
      File srcFile = new File(PDF_FILE_PATH);
      EcdeManager.copyFile(srcFile,
            "DCL001/19991231/3/documents/attestation.pdf");

      // affichage du Hash du fichier
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));

      LOG
            .debug("le hash du document 'attestation.pdf' à archiver est: "
                  + hash);

      // appel du service archivage unitaire

      List<Metadata> metadatas = new ArrayList<Metadata>();

      metadatas.add(ObjectModelFactory.createMetadata("ApplicationProductrice",
            APPLICATION_PRODUCTRICE));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", CODE_ORGA_PROPRIO));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", CODE_ORGA_GESTION));
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", CODE_RND));
      metadatas.add(ObjectModelFactory
            .createMetadata("VersionRND", VERSION_RND));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", NB_PAGE));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            FORMAT_FICHIER));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            DATE_CREATION));
      metadatas.add(ObjectModelFactory.createMetadata("Titre", TITRE));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", TYPE_HASH));
      metadatas.add(ObjectModelFactory.createMetadata("Hash", hash));
      metadatas.add(ObjectModelFactory.createMetadata("DateReception",
            DATE_RECEPT));
      metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
            DATE_DEB_CONSERV));

      URI urlEcde = URI
            .create("ecde://ecde.local.recouv/DCL001/19991231/3/documents/attestation.pdf");

      ArchivageUnitaireResponseType archivageResponse = archivageService
            .archivageUnitaire(urlEcde, metadatas);

      // récupération de l'uuid d'archivage
      String idArchive = archivageResponse.getIdArchive().getUuidType();

      String date = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());

      LOG.info("===================");
      LOG.info("UUID du document archivé: " + idArchive.toLowerCase());
      LOG.info("===================");
      LOG.info("hash du document : " + hash);
      LOG.info("===================");
      LOG.info("date archivage : " + date);
      LOG.info("===================");

   }
   
   
   
   public static final Map<String, Object> getExpectedMetadatas() {
      
      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("ApplicationProductrice", APPLICATION_PRODUCTRICE);
      expectedMetadatas.put("CodeOrganismeProprietaire", CODE_ORGA_PROPRIO);
      expectedMetadatas.put("CodeOrganismeGestionnaire", CODE_ORGA_GESTION);
      expectedMetadatas.put("CodeRND", CODE_RND);
      expectedMetadatas.put("VersionRND", VERSION_RND);
      expectedMetadatas.put("NbPages", NB_PAGE);
      expectedMetadatas.put("FormatFichier", FORMAT_FICHIER);
      expectedMetadatas.put("DateCreation", DATE_CREATION);
      expectedMetadatas.put("Titre", TITRE);
      expectedMetadatas.put("TypeHash", TYPE_HASH);
      expectedMetadatas.put("DateReception", DATE_RECEPT);
      expectedMetadatas.put("DateDebutConservation", DATE_DEB_CONSERV);
      expectedMetadatas.put("NomFichier", FILE_NAME);
      expectedMetadatas.put("ContratDeService", CONTRAT_SERVICE);
      expectedMetadatas.put("Hash", "4bf2ddbd82d5fd38e821e6aae434ac989972a043");
      expectedMetadatas.put("TailleFichier", "73791");
      expectedMetadatas.put("DateArchivage", getDateNow());
      
      return expectedMetadatas;
      
   }
   
   
   private static String getDateNow() {
      
      String pattern = "yyyy-MM-dd";
      Date maintenant = new Date();
      DateFormat dateFormat = new SimpleDateFormat(pattern);
      String dateFormatee = dateFormat.format(maintenant);
      return dateFormatee;
         
   }
   
   
   public void assertConsultationResponse(ConsultationResponseType response,
         Map<String, Object> expectedMetadatas, File expectedContent)
         throws IOException {

      MetadonneeType[] metadatas = response.getMetadonnees().getMetadonnee();

      assertNotNull("la liste des metadonnées doit être renseignée", metadatas);

      boolean expectedDateArchivage = false;

      for (MetadonneeType metadata : metadatas) {

         if ("DateArchivage".equals(metadata.getCode().getMetadonneeCodeType())) {

            expectedDateArchivage = true;

         } else {
            assertMetadata(metadata, expectedMetadatas);
         }
      }

      assertTrue("la métadonnée 'DateArchivage' est attendue",
            expectedDateArchivage);

      DataHandler actualContent = response.getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu();

      assertEquals("le contenu n'est pas attendu en base64", Base64Utils
            .encode(FileUtils.readFileToByteArray(expectedContent)),
            Base64Utils.encode(actualContent));

      assertTrue("le contenu n'est pas attendu", IOUtils.contentEquals(
            FileUtils.openInputStream(expectedContent), actualContent
                  .getInputStream()));

      assertNull(
            "Test de l'archivage unitaire : doit avoir aucune url directe de consultation",
            response.getObjetNumerique()
                  .getObjetNumeriqueConsultationTypeChoice_type0().getUrl());

   }

   private static void assertMetadata(MetadonneeType metadata,
         Map<String, Object> expectedMetadatas) {

      assertTrue("la metadonnée '" + metadata.getCode().getMetadonneeCodeType()
            + "' est inattendue", expectedMetadatas.containsKey(metadata
            .getCode().getMetadonneeCodeType()));

      assertEquals("la valeur de la metadonnée '"
            + metadata.getCode().getMetadonneeCodeType() + "' est inattendue",
            expectedMetadatas.get(metadata.getCode().getMetadonneeCodeType()),
            metadata.getValeur().getMetadonneeValeurType());

      expectedMetadatas.remove(metadata.getCode().getMetadonneeCodeType());
   }
   
   
   public void assertConsultationResponse(ConsultationResponseType response,
         Map<String, Object> expectedMetadatas)
         throws IOException {

      MetadonneeType[] metadatas = response.getMetadonnees().getMetadonnee();

      assertNotNull("la liste des metadonnées doit être renseignée", metadatas);

      boolean expectedDateArchivage = false;
      
      for (MetadonneeType metadata : metadatas) {

         if ("DateArchivage".equals(metadata.getCode().getMetadonneeCodeType())) {

            expectedDateArchivage = true;

         } else {
            assertMetadata(metadata, expectedMetadatas);
         }
      }

      assertTrue("la métadonnée 'DateArchivage' est attendue",
            expectedDateArchivage);

//      DataHandler actualContent = response.getObjetNumerique()
//            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu();

      assertNull(
            "Test de l'archivage unitaire : doit avoir aucune url directe de consultation",
            response.getObjetNumerique()
                  .getObjetNumeriqueConsultationTypeChoice_type0().getUrl());

   }
   

}
