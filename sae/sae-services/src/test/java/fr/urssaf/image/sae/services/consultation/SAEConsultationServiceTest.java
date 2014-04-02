package fr.urssaf.image.sae.services.consultation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEConsultationServiceTest {

   private static final Logger LOG = LoggerFactory
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

      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "consultation" };
      saePrmds.add(saePrmd);

      saeDroits.put("consultation", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }

   @After
   public void after() throws ConnectionServiceEx {

      // suppression de l'insertion
      if (uuid != null) {

         testProvider.deleteDocument(uuid);
      }

      // on vide le contexte de sécurité
      AuthenticationContext.setAuthenticationToken(null);
   }

   private UUID capture() throws IOException, ConnectionServiceEx,
         ParseException {
      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      byte[] content = FileUtils.readFileToByteArray(srcFile);

      String[] parsePatterns = new String[] { "yyyy-MM-dd" };
      Map<String, Object> metadatas = new HashMap<String, Object>();

      metadatas.put("apr", "ADELAIDE");
      metadatas.put("cop", "CER69");
      metadatas.put("cog", "UR750");
      metadatas.put("vrn", "11.1");
      metadatas.put("dom", "2");
      metadatas.put("act", "3");
      metadatas.put("nbp", "2");
      metadatas.put("ffi", "fmt/1354");
      metadatas.put("cse", "ATT_PROD_001");
      metadatas.put("dre", DateUtils.parseDate("1999-12-30", parsePatterns));
      metadatas.put("dfc", DateUtils.parseDate("2012-01-01", parsePatterns));

      Date creationDate = DateUtils.parseDate("2012-01-01", parsePatterns);
      Date dateDebutConservation = DateUtils.parseDate("2013-01-01",
            parsePatterns);
      String documentTitle = "attestation_consultation";
      String documentType = "pdf";
      String codeRND = "2.3.1.1.12";
      String title = "Attestation de vigilance";
      return testProvider.captureDocument(content, metadatas, documentTitle,
            documentType, creationDate, dateDebutConservation, codeRND, title);
   }

   @Test
   public void consultation_success() throws IOException,
         SAEConsultationServiceException, ConnectionServiceEx, ParseException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {

      uuid = capture();

      LOG.debug("document archivé dans DFCE:" + uuid);

      UntypedDocument untypedDocument = service.consultation(uuid);
      checkValues(untypedDocument);
   }

   @Test
   public void consultation_success_consultParam() throws IOException,
         SAEConsultationServiceException, ConnectionServiceEx, ParseException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {

      uuid = capture();

      LOG.debug("document archivé dans DFCE:" + uuid);

      UntypedDocument untypedDocument = service.consultation(new ConsultParams(
            uuid));
      checkValues(untypedDocument);
   }

   @Test
   public void consultation_success_codes_fournis() throws IOException,
         SAEConsultationServiceException, ConnectionServiceEx, ParseException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {

      uuid = capture();

      LOG.debug("document archivé dans DFCE:" + uuid);

      UntypedDocument untypedDocument = service.consultation(new ConsultParams(
            uuid, Arrays.asList(new String[] { "CodeOrganismeGestionnaire",
                  "ContratDeService" })));
      List<UntypedMetadata> metadatas = untypedDocument.getUMetadatas();
      // on trie les métadonnées non typés en fonction de leur code long
      Comparator<UntypedMetadata> comparator = new Comparator<UntypedMetadata>() {
         @Override
         public int compare(UntypedMetadata untypedMetadata1,
               UntypedMetadata untypedMetadata2) {

            return untypedMetadata1.getLongCode().compareTo(
                  untypedMetadata2.getLongCode());

         }
      };
      Collections.sort(metadatas, comparator);

      assertMetadata(metadatas.get(0), "CodeOrganismeGestionnaire", "UR750");
      assertMetadata(metadatas.get(1), "ContratDeService", "ATT_PROD_001");
   }

   @Test
   public void consultationFailureCodeNotExists() throws ConnectionServiceEx,
         IOException, ParseException {

      uuid = capture();
      List<String> listCode = Arrays.asList(new String[] { "Siret",
            "codeInexistant" });
      ConsultParams consultParams = new ConsultParams(uuid, listCode);

      LOG.debug("document archivé dans DFCE:" + uuid);

      try {
         service.consultation(consultParams);
      } catch (SAEConsultationServiceException e) {
         fail("C'est l'exception UnknowDesiredMetadataEx qui est attendue");
      } catch (UnknownDesiredMetadataEx e) {
         String message = "La ou les métadonnées suivantes, "
               + "demandées dans les critères de consultation, "
               + "n'existent pas dans le référentiel des métadonnées : "
               + "codeInexistant";
         assertEquals(
               "le message d'erreur signifiant que le code n'existe pas",
               message, e.getMessage());
      } catch (MetaDataUnauthorizedToConsultEx e) {
         fail("C'est l'exception UnknowDesiredMetadataEx qui est attendue");
      }
   }

   @Test
   public void consultationFailureCodeNoConsult() throws ConnectionServiceEx,
         IOException, ParseException {

      uuid = capture();
      List<String> listCode = Arrays
            .asList(new String[] { "Siret", "StartPage" });
      ConsultParams consultParams = new ConsultParams(uuid, listCode);

      LOG.debug("document archivé dans DFCE:" + uuid);

      try {
         service.consultation(consultParams);
      } catch (SAEConsultationServiceException e) {
         fail("C'est l'exception MetaDataUnauthorizedToConsultEx qui est attendue");
      } catch (UnknownDesiredMetadataEx e) {
         fail("C'est l'exception MetaDataUnauthorizedToConsultEx qui est attendue");
      } catch (MetaDataUnauthorizedToConsultEx e) {
         String message = "La ou les métadonnées suivantes, "
               + "demandées dans les critères de consultation, "
               + "ne sont pas consultables : StartPage";
         assertEquals(
               "le message d'erreur signifiant que le code n'est pas consultable",
               message, e.getMessage());
      }
   }

   private void checkValues(UntypedDocument untypedDocument) throws IOException {
      assertNotNull("idArchive '" + uuid + "' doit être consultable",
            untypedDocument);

      List<UntypedMetadata> metadatas = untypedDocument.getUMetadatas();

      assertNotNull("la liste des metadonnées doit être renseignée", metadatas);
      assertEquals(
            "la nombre de métadonnées consultables par défaut est inattendu",
            12, metadatas.size());

      // on trie les métadonnées non typés en fonction de leur code long
      Comparator<UntypedMetadata> comparator = new Comparator<UntypedMetadata>() {
         @Override
         public int compare(UntypedMetadata untypedMetadata1,
               UntypedMetadata untypedMetadata2) {

            return untypedMetadata1.getLongCode().compareTo(
                  untypedMetadata2.getLongCode());

         }
      };
      Collections.sort(metadatas, comparator);

      assertMetadata(metadatas.get(0), "CodeOrganismeGestionnaire", "UR750");
      assertMetadata(metadatas.get(1), "CodeOrganismeProprietaire", "CER69");
      assertMetadata(metadatas.get(2), "CodeRND", "2.3.1.1.12");
      assertMetadata(metadatas.get(3), "ContratDeService", "ATT_PROD_001");

      assertEquals("le code de la metadonnée est inattendue dans cet ordre",
            "DateArchivage", metadatas.get(4).getLongCode());

      assertMetadata(metadatas.get(5), "DateCreation", "2012-01-01");
      assertMetadata(metadatas.get(6), "DateReception", "1999-12-30");
      assertMetadata(metadatas.get(7), "FormatFichier", "fmt/1354");
      assertMetadata(metadatas.get(8), "Hash",
            "4bf2ddbd82d5fd38e821e6aae434ac989972a043");
      assertMetadata(metadatas.get(9), "NomFichier",
            "attestation_consultation.pdf");
      assertMetadata(metadatas.get(10), "TailleFichier", "73791");
      assertMetadata(metadatas.get(11), "Titre", "Attestation de vigilance");

      File expectedContent = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      assertTrue("le contenu n'est pas attendu", IOUtils.contentEquals(
            FileUtils.openInputStream(expectedContent), untypedDocument
                  .getContent().getInputStream()));
   }

   private static void assertMetadata(UntypedMetadata metadata,
         String expectedCode, String expectedValue) {

      assertEquals("le code de la metadonnée est inattendue dans cet ordre",
            expectedCode, metadata.getLongCode());

      assertEquals("la valeur de la metadonnée '" + metadata.getLongCode()
            + "'est inattendue", expectedValue, metadata.getValue());

   }

}
