package fr.urssaf.image.sae.webservices.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.EcdeManager;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModelFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;
import fr.urssaf.image.sae.webservices.util.SoapTestUtils;


/**
 * Tests de l'opération "archivageUnitairePJ" pour lesquels on attend une SoapFault<br>
 * <br>
 * Il faut penser à configurer son SAE local pour établir une configuration ECDE entre :<br>
 *  - DNS : ecde.cer69.recouv
 *  - Point de montage : REPERTOIRE_TEMP_UTILISATEUR\ecde
 *                       Exemple : C:\DOCUME~1\CER699~1\LOCALS~1\Temp\ecde 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.VariableNamingConventions" })
public class ArchivageUnitairePJFailureTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(ArchivageUnitairePJFailureTest.class);

   @Autowired
   private ArchivageUnitairePJService service;

   private static File att_file;

   private static File att_vide_file;

   private static String att_hash;

   private static String att_vide_hash;

   private static final String ECDE_HOST = "ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/";

   private static final URI ECDE_URL = URI
         .create(ECDE_HOST + "attestation.pdf");

   @BeforeClass
   public static void beforeClass() throws ConfigurationException, IOException {

      // mise en place du point dans le répertoire temporaire
      EcdeManager.cleanEcde();

      att_vide_file = new File(
            "src/test/resources/storage/attestation_vide.txt");
      att_vide_hash = DigestUtils.shaHex(new FileInputStream(att_vide_file));

      LOG.debug("le hash du document 'attestation_vide.txt' à archiver est: "
            + att_vide_hash);

      att_file = new File("src/test/resources/storage/attestation.pdf");
      att_hash = DigestUtils.shaHex(new FileInputStream(att_file));

      // enregistrement du fichier dans l'ECDE
      EcdeManager.copyFile(att_vide_file,
            "DCL001/19991231/3/documents/attestation_vide.txt");
      EcdeManager.copyFile(att_file,
            "DCL001/19991231/3/documents/attestation.pdf");

      LOG.debug("le hash du document 'attestation.pdf' à archiver est: "
            + att_hash);
   }

   private Map<String, Metadata> metadatasRef;

   @Before
   public final void before() throws IOException {

      metadatasRef = new HashMap<String, Metadata>();

      putMetadata("CodeRND", "2.3.1.1.12");
      putMetadata("ApplicationProductrice", "ADELAIDE");
      putMetadata("CodeOrganismeProprietaire", "CER69");
      putMetadata("CodeOrganismeGestionnaire", "UR750");
      putMetadata("VersionRND", "11.1");
      putMetadata("NbPages", "2");
      putMetadata("FormatFichier", "fmt/354");
      putMetadata("DateCreation", "2012-01-01");
      putMetadata("Titre", "Attestation de vigilance");
      putMetadata("TypeHash", "SHA-1");
      putMetadata("Hash", att_hash);
      putMetadata("DateReception", "1999-11-25");

   }

   private void putMetadata(String code, String value) {

      metadatasRef.put(code, ObjectModelFactory.createMetadata(code, value));
   }

   @Test
   @Ignore("Ajouter dans son ecde local un fichier vide attestation_vide.txt")
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureFichierVide()
         throws IOException {

      // appel du service archivage unitaire
      URI urlEcde = URI.create(ECDE_HOST + "attestation_vide.txt");

      try {

         service.archivageUnitairePJ(urlEcde, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "Le fichier à archiver est vide (attestation_vide.txt)",
               "CaptureFichierVide", SoapTestUtils.SAE_NAMESPACE,
               SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureMetadonneesInconnu()
         throws IOException {

      // appel du service archivage unitaire

      putMetadata("metadonnee_inconnu_1", "value_1");
      putMetadata("metadonnee_inconnu_2", "value_2");

      Collection<Metadata> clone = metadatasRef.values();

      try {

         service.archivageUnitairePJ(ECDE_URL, clone);

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "La ou les métadonnées suivantes n'existent pas dans le référentiel des métadonnées : metadonnee_inconnu_1, metadonnee_inconnu_2",
                     "CaptureMetadonneesInconnu", SoapTestUtils.SAE_NAMESPACE,
                     SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureMetadonneesDoublon()
         throws IOException {

      @SuppressWarnings("unchecked")
      Collection<Metadata> clone = CollectionUtils.disjunction(metadatasRef
            .values(), new ArrayList());

      clone.add(ObjectModelFactory.createMetadata("VersionRND", "15.6"));

      clone.add(ObjectModelFactory.createMetadata("CodeOrganismeGestionnaire",
            "UR44"));

      try {

         service.archivageUnitairePJ(ECDE_URL, clone);

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "La ou les métadonnées suivantes sont renseignées plusieurs fois : CodeOrganismeGestionnaire, VersionRND",
                     "CaptureMetadonneesDoublon", SoapTestUtils.SAE_NAMESPACE,
                     SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureMetadonneesFormatTypeNonValide()
         throws IOException {

      putMetadata("DateCreation", "01/01/2012");
      putMetadata("Gel", "value");

      try {

         service.archivageUnitairePJ(ECDE_URL, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "Le type ou le format des métadonnées suivantes n'est pas valide : DateCreation, Gel",
                     "CaptureMetadonneesFormatTypeNonValide",
                     SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureMetadonneesInterdites()
         throws IOException {

      putMetadata("DureeConservation", "1825");
      putMetadata("Gel", "true");

      try {

         service.archivageUnitairePJ(ECDE_URL, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "La ou les métadonnées suivantes ne sont pas autorisées à l'archivage : DureeConservation, Gel",
                     "CaptureMetadonneesInterdites",
                     SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureMetadonneesArchivageObligatoire()
         throws IOException {

      metadatasRef.remove("ApplicationProductrice");
      metadatasRef.remove("CodeRND");

      try {

         service.archivageUnitairePJ(ECDE_URL, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "La ou les métadonnées suivantes, obligatoires lors de l'archivage, ne sont pas renseignées : ApplicationProductrice, CodeRND",
                     "CaptureMetadonneesArchivageObligatoire",
                     SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureCodeRndInterdit()
         throws IOException {

      putMetadata("CodeRND", "toto");

      try {

         service.archivageUnitairePJ(ECDE_URL, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "Le type de document toto n'est pas autorisé à l'archivage.",
               "CaptureCodeRndInterdit", SoapTestUtils.SAE_NAMESPACE,
               SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureHashErreur()
         throws IOException {

      putMetadata("Hash", "toto");

      try {

         service.archivageUnitairePJ(ECDE_URL, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "Le contrôle de l'intégrité du fichier a échoué.",
               "CaptureHashErreur", SoapTestUtils.SAE_NAMESPACE,
               SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureUrlEcdeFichierIntrouvable()
         throws IOException {

      try {

         service
               .archivageUnitairePJ(
                     URI
                           .create("ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation_inconnu.pdf"),
                     metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "Le fichier pointé par l'URL ECDE est introuvable (ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation_inconnu.pdf)",
                     "CaptureUrlEcdeFichierIntrouvable",
                     SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }
   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureUrlEcdeIncorrecte()
         throws IOException {

      try {

         // le DNS n'est pas bon
         service
               .archivageUnitairePJ(
                     URI
                           .create("ecde://ecde.cer70.recouv/DCL001/19991231/3/documents/attestation_inconnu.pdf"),
                     metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "L'URL ECDE est incorrecte (ecde://ecde.cer70.recouv/DCL001/19991231/3/documents/attestation_inconnu.pdf)",
                     "CaptureUrlEcdeIncorrecte", SoapTestUtils.SAE_NAMESPACE,
                     SoapTestUtils.SAE_PREFIX);

      }
   }

   @Test
   public void archivageUnitairePJ_failure_AvecUrlEcde_CaptureMetadonneesVide()
         throws IOException {

      Collection<Metadata> metadatas = new ArrayList<Metadata>();

      try {

         service.archivageUnitairePJ(ECDE_URL, metadatas);

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "La liste des métadonnées est vide.", "CaptureMetadonneesVide",
               SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }
   }
   
   @Test
   public void archivageUnitairePJ_failure_AvecContenu_CaptureFichierVide()
         throws IOException {

      Collection<Metadata> metadatas = metadatasRef.values();

      try {
         String fileName = "CaptureContenuVide";
         
         service.archivageUnitairePJ(fileName, null, metadatas);

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "Le contenu du fichier à archiver est vide.", "CaptureFichierVide",
               SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }
   }
   
   @Test
   public void archivageUnitairePJ_failure_AvecContenu_NomFichierVide_chaineVide()
         throws IOException {

      Collection<Metadata> metadatas = metadatasRef.values();

      try {
         
         byte[] contenu = new byte[20];
         String fileName = "";
         
         service.archivageUnitairePJ(fileName, contenu, metadatas);

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "Le nom du fichier est vide.", "NomFichierVide",
               SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }
   }
   
   @Test
   public void archivageUnitairePJ_failure_AvecContenu_NomFichierVide_chaineAvecDesEspaces()
         throws IOException {

      Collection<Metadata> metadatas = metadatasRef.values();

      try {
         
         byte[] contenu = new byte[20];
         String fileName = "   "; // on met des espaces pour vérifier si le trim est bien fait
         
         service.archivageUnitairePJ(fileName, contenu, metadatas);

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "Le nom du fichier est vide.", "NomFichierVide",
               SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }
   }
   
   @Test
   @Ignore("A mettre à jour une fois la gestion des extensions faite par DFCE.")
   public void archivageUnitairePJ_failure_AvecContenu_CaptureSansExtension()
         throws IOException {

      Collection<Metadata> metadatas = metadatasRef.values();

      byte[] contenu = new byte[20];
      String fileName = "NomFichierSansExtension";
         
      service.archivageUnitairePJ(fileName, contenu, metadatas);

   }
   
   @Test
   public void archivageUnitairePJ_failure_AvecContenu_CaptureFichierVide_null() throws URISyntaxException,
         FileNotFoundException, IOException {

      
      try {
         String fileName = "NomFichier.txt";
         byte[] contenu = null;
         
         List<Metadata> metadatas = new ArrayList<Metadata>();
   
         metadatas.add(ObjectModelFactory.createMetadata("ApplicationProductrice",
               "ADELAIDE"));
         metadatas.add(ObjectModelFactory.createMetadata(
               "CodeOrganismeProprietaire", "CER69"));
         metadatas.add(ObjectModelFactory.createMetadata(
               "CodeOrganismeGestionnaire", "UR750"));
         metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
         metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
         metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
         metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
               "fmt/354"));
         metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
               "2012-01-01"));
         metadatas.add(ObjectModelFactory.createMetadata("Titre",
               "Attestation de vigilance"));
         metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
         metadatas.add(ObjectModelFactory.createMetadata("Hash", ""));
         metadatas.add(ObjectModelFactory.createMetadata("DateReception",
               "1999-11-25"));
         metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
               "2011-09-02"));
   
         
         service
               .archivageUnitairePJ(fileName, contenu, metadatas);
         
         Assert.fail(SoapTestUtils.FAIL_MSG);
   
      }
      catch(AxisFault axisFault) {
         
         SoapTestUtils.assertAxisFault(axisFault,
               "Le contenu du fichier à archiver est vide.",
               "CaptureFichierVide", SoapTestUtils.SAE_NAMESPACE,
               SoapTestUtils.SAE_PREFIX);
         
      }
   }
   
   @Test
   public void archivageUnitairePJ_failure_AvecContenu_CaptureFichierVide_0octet() throws URISyntaxException,
         FileNotFoundException, IOException {

      try {
         String fileName = "NomFichier.txt";
         byte[] contenu = new byte[0];
         String att_hash = DigestUtils.shaHex(contenu);
         List<Metadata> metadatas = new ArrayList<Metadata>();
   
         metadatas.add(ObjectModelFactory.createMetadata("ApplicationProductrice",
               "ADELAIDE"));
         metadatas.add(ObjectModelFactory.createMetadata(
               "CodeOrganismeProprietaire", "CER69"));
         metadatas.add(ObjectModelFactory.createMetadata(
               "CodeOrganismeGestionnaire", "UR750"));
         metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
         metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
         metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
         metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
               "fmt/354"));
         metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
               "2012-01-01"));
         metadatas.add(ObjectModelFactory.createMetadata("Titre",
               "Attestation de vigilance"));
         metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
         metadatas.add(ObjectModelFactory.createMetadata("Hash", att_hash));
         metadatas.add(ObjectModelFactory.createMetadata("DateReception",
               "1999-11-25"));
         metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
               "2011-09-02"));
   
         
         service
               .archivageUnitairePJ(fileName, contenu, metadatas);
   
      }
      catch(AxisFault axisFault) {
         
         SoapTestUtils.assertAxisFault(axisFault,
               "Le contenu du fichier à archiver est vide.",
               "CaptureFichierVide", SoapTestUtils.SAE_NAMESPACE,
               SoapTestUtils.SAE_PREFIX);
      }
      
   }

}
