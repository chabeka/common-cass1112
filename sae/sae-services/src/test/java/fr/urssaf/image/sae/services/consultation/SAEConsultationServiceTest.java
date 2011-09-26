package fr.urssaf.image.sae.services.consultation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEConsultationServiceTest {

   private static final Logger LOG = Logger
         .getLogger(SAEConsultationServiceTest.class);

   @Autowired
   @Qualifier("saeConsultationService")
   private SAEConsultationService service;

   @Autowired
   private SAEServiceTestProvider testProvider;

   private UUID uuid;

   @SuppressWarnings("PMD.NullAssignment")
   @Before
   public void before() {

      // initialisation de l'uuid de l'archive
      uuid = null;
   }

   @After
   public void after() throws ConnectionServiceEx {

      // suppression de l'insertion
      if (uuid != null) {

         testProvider.deleteDocument(uuid);
      }
   }

   private UUID capture() throws IOException, ConnectionServiceEx,
         ParseException {

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      byte[] content = FileUtils.readFileToByteArray(srcFile);

      String[] parsePatterns = new String[] { "yyyy-MM-dd" };

      Map<String, Object> metadatas = new HashMap<String, Object>();
      metadatas.put("APR", "ADELAIDE");
      metadatas.put("COP", "CER69");
      metadatas.put("COG", "UR750");
      metadatas.put("RND", "2.3.1.1.12");
      metadatas.put("VRN", "11.1");
      metadatas.put("DOM", "2");
      metadatas.put("ACT", "3");
      metadatas.put("DDC", DateUtils.parseDate("2013-01-01", parsePatterns));
      metadatas.put("DFC", DateUtils.parseDate("2014-01-01", parsePatterns));
      metadatas.put("NBP", 2);
      metadatas.put("FFI", "fmt/1354");
      metadatas.put("CSE", "ATT_PROD_001");
      metadatas.put("DRE", DateUtils.parseDate("1999-12-30", parsePatterns));

      Date creationDate = DateUtils.parseDate("2012-01-01", parsePatterns);
      String type = "PDF";
      String title = "Attestation de vigilance";
      return testProvider.captureDocument(content, metadatas, title, type,
            creationDate);
   }

   @Test
   public void consultation_success() throws IOException,
         SAEConsultationServiceException, ConnectionServiceEx, ParseException {

      uuid = capture();

      LOG.debug("document archivé dans DFCE:" + uuid);

      UntypedDocument untypedDocument = service.consultation(uuid);

      assertNotNull("idArchive '" + uuid + "' doit être consultable",
            untypedDocument);

      List<UntypedMetadata> metadatas = untypedDocument.getUMetadatas();

      assertNotNull("la liste des metadonnées doit être renseignée", metadatas);

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("Titre", "Attestation de vigilance");
      expectedMetadatas.put("DateCreation", "2012-01-01");
      expectedMetadatas.put("DateReception", "1999-12-29");
      expectedMetadatas.put("CodeOrganismeGestionnaire", "UR750");
      expectedMetadatas.put("CodeOrganismeProprietaire", "CER69");
      expectedMetadatas.put("CodeRND", "2.3.1.1.12");
      expectedMetadatas.put("NomFichier", "");
      expectedMetadatas.put("FormatFichier", "fmt/1354");
      expectedMetadatas.put("ContratDeService", "ATT_PROD_001");
      expectedMetadatas.put("DateArchivage", "2012-01-01");

      for (UntypedMetadata metadata : metadatas) {
         assertMetadata(metadata, expectedMetadatas);
      }

      assertTrue("Des métadonnées '" + expectedMetadatas.keySet()
            + "' sont attendues", expectedMetadatas.isEmpty());

      File expectedContent = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      assertTrue("le contenu n'est pas attendu", IOUtils.contentEquals(
            FileUtils.openInputStream(expectedContent),
            new ByteArrayInputStream(untypedDocument.getContent())));
   }

   private static void assertMetadata(UntypedMetadata metadata,
         Map<String, Object> expectedMetadatas) {

      assertTrue("la metadonnée '" + metadata.getLongCode()
            + "' est inattendue", expectedMetadatas.containsKey(metadata
            .getLongCode()));

      assertEquals("la valeur de la metadonnée '" + metadata.getLongCode()
            + "'est inattendue", expectedMetadatas.get(metadata.getLongCode()),
            metadata.getValue());

      expectedMetadatas.remove(metadata.getLongCode());
   }

}
