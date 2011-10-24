package fr.urssaf.image.sae.webservices.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.EcdeManager;
import fr.urssaf.image.sae.webservices.configuration.SecurityConfiguration;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModelFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;
import fr.urssaf.image.sae.webservices.util.AuthenticateUtils;
import fr.urssaf.image.sae.webservices.util.SoapTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.VariableNamingConventions" })
public class ArchivageUnitaireFailureTest {

   private static final Logger LOG = Logger
         .getLogger(ArchivageUnitaireFailureTest.class);

   @Autowired
   private ArchivageUnitaireService service;

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

      AuthenticateUtils.authenticate("ROLE_TOUS");

      metadatasRef = new HashMap<String, Metadata>();

      putMetadata("CodeRND", "2.3.1.1.12");
      putMetadata("ApplicationProductrice", "ADELAIDE");
      putMetadata("CodeOrganismeProprietaire", "CER69");
      putMetadata("CodeOrganismeGestionnaire", "UR750");
      putMetadata("VersionRND", "11.1");
      putMetadata("NbPages", "2");
      putMetadata("NomFichier", "empty.pdf");
      putMetadata("FormatFichier", "fmt/1354");
      putMetadata("DateCreation", "2012-01-01");
      putMetadata("Titre", "Attestation de vigilance");
      putMetadata("TypeHash", "SHA-1");
      putMetadata("Hash", att_hash);
      putMetadata("DateReception", "1999-11-25");

   }

   private void putMetadata(String code, String value) {

      metadatasRef.put(code, ObjectModelFactory.createMetadata(code, value));
   }

   @After
   public final void after() {

      SecurityConfiguration.cleanSecurityContext();
   }

   @Test
   public void archivageUnitaire_failure_CaptureFichierVide()
         throws IOException {

      // appel du service archivage unitaire
      URI urlEcde = URI.create(ECDE_HOST + "attestation_vide.txt");

      try {

         service.archivageUnitaire(urlEcde, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "Le fichier à archiver est vide (attestation_vide.txt)",
               "CaptureFichierVide", SoapTestUtils.SAE_NAMESPACE,
               SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitaire_failure_CaptureMetadonneesInconnu()
         throws IOException {

      // appel du service archivage unitaire

      putMetadata("metadonnee_inconnu_1", "value_1");
      putMetadata("metadonnee_inconnu_2", "value_2");

      Collection<Metadata> clone = metadatasRef.values();

      try {

         service.archivageUnitaire(ECDE_URL, clone);

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
   public void archivageUnitaire_failure_CaptureMetadonneesDoublon()
         throws IOException {

      @SuppressWarnings("unchecked")
      Collection<Metadata> clone = CollectionUtils.disjunction(metadatasRef
            .values(), new ArrayList());

      clone.add(ObjectModelFactory.createMetadata("VersionRND", "15.6"));

      clone.add(ObjectModelFactory.createMetadata("CodeOrganismeGestionnaire",
            "UR44"));

      try {

         service.archivageUnitaire(ECDE_URL, clone);

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
   public void archivageUnitaire_failure_CaptureMetadonneesFormatTypeNonValide()
         throws IOException {

      putMetadata("DateCreation", "01/01/2012");
      putMetadata("Gel", "value");

      try {

         service.archivageUnitaire(ECDE_URL, metadatasRef.values());

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
   public void archivageUnitaire_failure_CaptureMetadonneesInterdites()
         throws IOException {

      putMetadata("DureeConservation", "1825");
      putMetadata("Gel", "true");

      try {

         service.archivageUnitaire(ECDE_URL, metadatasRef.values());

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
   public void archivageUnitaire_failure_CaptureMetadonneesArchivageObligatoire()
         throws IOException {

      metadatasRef.remove("ApplicationProductrice");
      metadatasRef.remove("CodeRND");

      try {

         service.archivageUnitaire(ECDE_URL, metadatasRef.values());

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
   public void archivageUnitaire_failure_CaptureCodeRndInterdit()
         throws IOException {

      putMetadata("CodeRND", "toto");

      try {

         service.archivageUnitaire(ECDE_URL, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "Le type de document toto n'est pas autorisé à l'archivage.",
               "CaptureCodeRndInterdit", SoapTestUtils.SAE_NAMESPACE,
               SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitaire_failure_ErreurInterneCapture_Hash()
         throws IOException {

      putMetadata("Hash", "toto");

      try {

         service.archivageUnitaire(ECDE_URL, metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "Une erreur interne à l'application est survenue lors de la capture.",
                     "ErreurInterneCapture", SoapTestUtils.SAE_NAMESPACE,
                     SoapTestUtils.SAE_PREFIX);

      }

   }

   @Test
   public void archivageUnitaire_failure_ErreurInterneCapture_FileNotFound()
         throws IOException {

      try {

         service
               .archivageUnitaire(
                     URI
                           .create("ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation_inconnu.pdf"),
                     metadatasRef.values());

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "Une erreur interne à l'application est survenue lors de la capture.",
                     "ErreurInterneCapture", SoapTestUtils.SAE_NAMESPACE,
                     SoapTestUtils.SAE_PREFIX);

      }
   }

   @Test
   public void archivageUnitaire_failure_CaptureMetadonneesVide()
         throws IOException {

      Collection<Metadata> metadatas = new ArrayList<Metadata>();

      try {

         service.archivageUnitaire(ECDE_URL, metadatas);

         Assert.fail(SoapTestUtils.FAIL_MSG);

      } catch (AxisFault fault) {

         SoapTestUtils.assertAxisFault(fault,
               "La liste des métadonnées est vide.", "CaptureMetadonneesVide",
               SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      }
   }

}
