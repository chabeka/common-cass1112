package fr.urssaf.image.sae.webservices.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.EcdeManager;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaireResponseType;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModelFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;
import fr.urssaf.image.sae.webservices.util.SoapTestUtils;

/**
 * Tests de l'opération "archivageUnitaire" pour lesquels on attend une réponse
 * correcte (pas de SoapFault)<br>
 * <br>
 * Il faut penser à configurer son SAE local pour établir une configuration ECDE
 * entre :<br>
 * - DNS : ecde.cer69.recouv - Point de montage :
 * REPERTOIRE_TEMP_UTILISATEUR\ecde Exemple :
 * C:\DOCUME~1\CER699~1\LOCALS~1\Temp\ecde
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ArchivageUnitaireTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(ArchivageUnitaireTest.class);

   @Autowired
   private ArchivageUnitaireService archivage;

   @Autowired
   private ConsultationService consultation;

   @BeforeClass
   public static void beforeClass() throws ConfigurationException, IOException {

      EcdeManager.cleanEcde();
   }

   @Test
   public void archivageUnitaire_success() throws URISyntaxException,
         FileNotFoundException, IOException {

      // enregistrement du fichier dans l'ECDE
      File srcFile = new File("src/test/resources/storage/attestation.pdf");
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
            "ADELAIDE"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
      //metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            "fmt/354"));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            "2012-01-01"));
      metadatas.add(ObjectModelFactory.createMetadata("Titre",
            "Attestation de vigilance"));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
      metadatas.add(ObjectModelFactory.createMetadata("Hash", hash));
      metadatas.add(ObjectModelFactory.createMetadata("DateReception",
            "1999-11-25"));
      metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
            "2011-09-02"));
      metadatas.add(ObjectModelFactory.createMetadata("ReferenceDocumentaire",
            "213039953275"));

      URI urlEcde = URI
            .create("ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation.pdf");

      ArchivageUnitaireResponseType archivageResponse = archivage
            .archivageUnitaire(urlEcde, metadatas);

      // récupération de l'uuid d'archivage
      String idArchive = archivageResponse.getIdArchive().getUuidType();

      LOG.debug("UUID du document archivé: " + idArchive.toLowerCase());

      Assert.assertNotNull("L'identifiant d'archivage doit être renseigné",
            idArchive);

      // vérification du contenu du document et des métadonnées

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("Titre", "Attestation de vigilance");
      expectedMetadatas.put("DateCreation", "2012-01-01");
      expectedMetadatas.put("DateReception", "1999-11-25");
      expectedMetadatas.put("CodeOrganismeGestionnaire", "UR750");
      expectedMetadatas.put("CodeOrganismeProprietaire", "CER69");
      expectedMetadatas.put("CodeRND", "2.3.1.1.12");
      expectedMetadatas.put("NomFichier", "attestation.pdf");
      expectedMetadatas.put("FormatFichier", "fmt/354");
      expectedMetadatas.put("ContratDeService", "CS_ANCIEN_SYSTEME");
      expectedMetadatas.put("Hash", hash);
      expectedMetadatas.put("TailleFichier", Long.toString(FileUtils
            .sizeOf(srcFile)));
      expectedMetadatas.put("ReferenceDocumentaire", "213039953275");

      ConsultationUtilsTest consultationTest = new ConsultationUtilsTest();

      consultationTest.assertConsultationResponse(consultation
            .consultation(idArchive), expectedMetadatas, srcFile);

   }

   /**
    * Test permettant de vérifier qu'une capture échoue si une métadonnée avec
    * dictionnaire présente une valeur qui n'est référencée
    * 
    * @throws URISyntaxException
    * @throws FileNotFoundException
    * @throws IOException
    */

   @Test
   public void archivageUnitaire_failure_metadataValueNotInDict()
         throws URISyntaxException, FileNotFoundException, IOException {

      // appel du service archivage unitaire

      List<Metadata> metadatas = new ArrayList<Metadata>();

      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
      //metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            "fmt/354"));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            "2012-01-01"));
      metadatas.add(ObjectModelFactory.createMetadata("Titre",
            "Attestation de vigilance"));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
      metadatas.add(ObjectModelFactory.createMetadata("Hash", "128775498884"));
      metadatas.add(ObjectModelFactory.createMetadata("DateReception",
            "1999-11-25"));
      metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
            "2011-09-02"));
      // cette metadonnées nes pas référencée dans le dictionnaire des données
      // dictMeta2
      metadatas.add(ObjectModelFactory.createMetadata("metadonne2", "3"));

      URI urlEcde = URI
            .create(" ecde.cer69.recouv/DCL001/19991231/3/documents/attestation.pdf");
      // URI urlEcde = URI
      // .create("ecde://CER69-TEC24251/SAE_INTEGRATION/20110822/CaptureUnitaire-101-CaptureUnitaire-OK-Standard/documents/doc1.PDF");
      try {
         ArchivageUnitaireResponseType archivageResponse = archivage
               .archivageUnitaire(urlEcde, metadatas);
      } catch (Exception e) {
         Assert
               .assertEquals(
                     "La valeur de la métadonnée metadonne2 est incorrecte: elle n'est pas comprise dans le dictionnaire de données associé",
                     e.getMessage());
      }

   }
   
   /**
    * test vérifiant l'échec d'une capture si le dictionnaire est inexistant. la MetadataRuntimeException est transformée en erreur interne de capture
    * @throws URISyntaxException
    * @throws FileNotFoundException
    * @throws IOException
    */
   @Test
   public void archivageUnitaireFailureNoDict()
         throws URISyntaxException, FileNotFoundException, IOException {

      // appel du service archivage unitaire

      List<Metadata> metadatas = new ArrayList<Metadata>();

      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
      //metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            "fmt/354"));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            "2012-01-01"));
      metadatas.add(ObjectModelFactory.createMetadata("Titre",
            "Attestation de vigilance"));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
      metadatas.add(ObjectModelFactory.createMetadata("Hash", "128775498884"));
      metadatas.add(ObjectModelFactory.createMetadata("DateReception",
            "1999-11-25"));
      metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
            "2011-09-02"));
      // cette metadonnées nes pas référencée dans le dictionnaire des données
      // dictMeta2
      metadatas.add(ObjectModelFactory.createMetadata("metadonne3", "3"));

//      URI urlEcde = URI
//            .create(" ecde.cer69.recouv/DCL001/19991231/3/documents/attestation.pdf");
       URI urlEcde = URI
       .create("ecde://CER69-TEC24251/SAE_INTEGRATION/20110822/CaptureUnitaire-101-CaptureUnitaire-OK-Standard/documents/doc1.PDF");
      try {
         ArchivageUnitaireResponseType archivageResponse = archivage
               .archivageUnitaire(urlEcde, metadatas);
      } catch (AxisFault fault) {
         
         Assert.assertTrue(fault.getDetail().toString().contains("MetadataRuntimeException: Le dictionnaire dictExistePas n'a pas été trouvée"));
         // on vérifie que l'exception levée est une erreur interne de capture.
         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "Une erreur interne à l'application est survenue lors de la capture.",
                     "ErreurInterneCapture", SoapTestUtils.SAE_NAMESPACE,
                     SoapTestUtils.SAE_PREFIX);

      }      
   }
   
   /**
    * test vérifiant l'échec d'une capture si le dictionnaire est inexistant. la MetadataRuntimeException est transformée en erreur interne de capture
    * @throws URISyntaxException
    * @throws FileNotFoundException
    * @throws IOException
    */
   @Test
   public void archivageUnitaireNokAfterMetaCreation()
         throws URISyntaxException, FileNotFoundException, IOException {

      // appel du service archivage unitaire

      List<Metadata> metadatas = new ArrayList<Metadata>();

      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
      //metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            "fmt/354"));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            "2012-01-01"));
      metadatas.add(ObjectModelFactory.createMetadata("Titre",
            "Attestation de vigilance"));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
      metadatas.add(ObjectModelFactory.createMetadata("Hash", "128775498884"));
      metadatas.add(ObjectModelFactory.createMetadata("DateReception",
            "1999-11-25"));
      metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
            "2011-09-02"));
      // cette metadonnées nes pas référencée dans le dictionnaire des données
      // dictMeta2
      metadatas.add(ObjectModelFactory.createMetadata("metadonne4", "3"));

//      URI urlEcde = URI
//            .create(" ecde.cer69.recouv/DCL001/19991231/3/documents/attestation.pdf");
       URI urlEcde = URI
       .create("ecde://CER69-TEC24251/SAE_INTEGRATION/20110822/CaptureUnitaire-101-CaptureUnitaire-OK-Standard/documents/doc1.PDF");
      try {
         ArchivageUnitaireResponseType archivageResponse = archivage
               .archivageUnitaire(urlEcde, metadatas);
      } catch (AxisFault fault) {
         // on vérifie que l'exception levée est liée à l'inexistance de la métadonnée.
         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "La ou les métadonnées suivantes n'existent pas dans le référentiel des métadonnées : metadonne4",
                     "CaptureMetadonneesInconnu", SoapTestUtils.SAE_NAMESPACE,
                     SoapTestUtils.SAE_PREFIX);

      }      
   }
   
}