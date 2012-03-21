package fr.urssaf.image.sae.services.document;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-bulkcapture-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEBulkCaptureServiceTest {

   private static final String TRAITEMENT_REF = "DCL001/19991231/3";

   @Autowired
   private SAEBulkCaptureService bulkCapture;

   @Autowired
   private EcdeSource ecdeSource;

   @Autowired
   private SAEServiceTestProvider testProvider;

   private String urlSommaire;

   private String idTreatement;

   @Before
   public void before() throws IOException {

      FileUtils.cleanDirectory(ecdeSource.getBasePath());

      // copie du fichier sommaire dans ECDE
      File sommaire = new File(
            "src/test/resources/sommaire/sommaire_success.xml");
      File ecdeSommaire = new File(ecdeSource.getBasePath(), TRAITEMENT_REF
            + "/sommaire.xml");
      FileUtils.copyFile(sommaire, ecdeSommaire, false);

      // copie du fichier attestation dans documents
      File attestation = new File("src/test/resources/doc/attestation.pdf");

      // attestation 1
      File ecdeAttestation1 = new File(ecdeSource.getBasePath(), TRAITEMENT_REF
            + "/documents/attestation1.pdf");
      FileUtils.copyFile(attestation, ecdeAttestation1, false);

      // attestation 2
      File ecdeAttestation2 = new File(ecdeSource.getBasePath(), TRAITEMENT_REF
            + "/documents/attestation2.pdf");
      FileUtils.copyFile(attestation, ecdeAttestation2, false);

      // attestation 3
      File ecdeAttestation3 = new File(ecdeSource.getBasePath(), TRAITEMENT_REF
            + "/documents/attestation3.pdf");
      FileUtils.copyFile(attestation, ecdeAttestation3, false);

      // création de l'URL ECDE pour le sommaire
      urlSommaire = "ecde://" + ecdeSource.getHost() + "/" + TRAITEMENT_REF
            + "/sommaire.xml";

      // instanciation de l'identifiant du traitement de masse
      idTreatement = ObjectUtils.toString(UUID.randomUUID());
      MDC.put("log_contexte_uuid", idTreatement);
   }

   private void assertFinTraitementFlag() {

      // vérification de la présence du fichier fin_traitement.flag
      File finTraitement = new File(ecdeSource.getBasePath(), TRAITEMENT_REF
            + "/fin_traitement.flag");
      Assert.assertTrue("le fichier fin_traitement.flag doit être présent",
            finTraitement.isFile());
   }

   private void assertResultats(String expectedResultats) throws IOException {

      // vérification de la présence du fichier resultats.xml
      File resultats = new File(ecdeSource.getBasePath(), TRAITEMENT_REF
            + "/resultats.xml");
      Assert.assertTrue("le fichier resultats.xml doit être présent", resultats
            .isFile());

      // vérification du contenu de resultats.xml
      File expectedResultat = new File(expectedResultats);
      Assert.assertEquals(
            "le checksum du fichier resultats.xml n'est pas celui attendu",
            FileUtils.checksumCRC32(expectedResultat), FileUtils
                  .checksumCRC32(resultats));
   }

   @Test
   public void bulkCapture_success() throws CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, CaptureEcdeWriteFileEx, IOException {

      // appel du service de capture en masse
      bulkCapture.bulkCapture(urlSommaire);

      SearchResult results = testProvider.searchDocuments(idTreatement, 10000);
      List<Document> documents = results.getDocuments();

      // on vérifie que les documents avant l'échec de l'interruption sont bien
      // insérés
      Assert.assertEquals("le nombre de documents inséré est inattendu", 3,
            documents.size());

      // on test le fichier de fin de traitement
      assertFinTraitementFlag();

      // on test le fichier de resultats
      assertResultats("src/test/resources/resultats/resultats_success.xml");

   }

}
