package fr.urssaf.image.sae.services.copie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.copie.SAECopieServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAECopieServiceTest {

   @Autowired
   @Qualifier("saeCopieService")
   private SAECopieService service;

   @Autowired
   @Qualifier("SAEServiceTestProvider")
   private SAEServiceTestProvider testProvider;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private ParametersService parametersService;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private JobClockSupport jobClockSupport;

   private UUID uuid;

   @Before
   public void before() throws Exception {

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
      String[] roles = new String[] { "copie" };
      saePrmds.add(saePrmd);

      saeDroits.put("copie", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Paramétrage du RND

      server.resetData();
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.12");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("2");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("ATTESTATION DE VIGILANCE");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   @After
   public void after() throws Exception {

      // suppression de l'insertion
      if (uuid != null) {

         testProvider.deleteDocument(uuid);
      }

      // on vide le contexte de sécurité
      AuthenticationContext.setAuthenticationToken(null);

      server.resetData();
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
      metadatas.put("ffi", "fmt/354");
      metadatas.put("cse", "ATT_PROD_001");
      metadatas.put("dre", DateUtils.parseDate("1999-12-30", parsePatterns));
      metadatas.put("dfc", DateUtils.parseDate("2012-01-01", parsePatterns));
      metadatas.put("cot", Boolean.TRUE);

      Date creationDate = DateUtils.parseDate("2012-01-01", parsePatterns);
      Date dateDebutConservation = DateUtils.parseDate("2013-01-01",
            parsePatterns);
      String documentTitle = "attestation_consultation";
      String documentType = "pdf";
      String codeRND = "7.7.8.8.1";
      String title = "Attestation de vigilance";
      String note = "note du document";
      return testProvider.captureDocument(content, metadatas, documentTitle,
            documentType, creationDate, dateDebutConservation, codeRND, title,
            note);
   }

   @Test
   public void copie_success() throws IOException,
         SAEConsultationServiceException, ConnectionServiceEx, ParseException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         ReferentialException, SAECopieServiceException,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException,
         ArchiveInexistanteEx {

      uuid = capture();

      //
      String[] parsePatterns = new String[] { "yyyy-MM-dd" };
      Map<String, Object> metadatas = new HashMap<String, Object>();

      metadatas.put("apr", "ADELAIDE");
      metadatas.put("cop", "CER69");
      metadatas.put("cog", "UR750");
      metadatas.put("vrn", "11.1");
      metadatas.put("dom", "2");
      metadatas.put("act", "3");
      metadatas.put("nbp", "8");
      metadatas.put("ffi", "fmt/354");
      metadatas.put("cse", "ATT_PROD_002");
      metadatas.put("dre", DateUtils.parseDate("1999-12-30", parsePatterns));
      metadatas.put("dfc", DateUtils.parseDate("2012-01-01", parsePatterns));
      metadatas.put("cot", Boolean.TRUE);
      metadatas.put("CodeRND", "2.3.1.1.12");

      List<UntypedMetadata> fin = new ArrayList<UntypedMetadata>();

      fin.add(new UntypedMetadata("NbPages", "4"));
      fin.add(new UntypedMetadata("ApplicationProductrice", "Ada"));
      fin.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));

      UUID res = service.copie(uuid, fin);
      checkValues(res);
   }

   private void checkValues(UUID untypedDocument) throws IOException {
      assertNotNull("idCopie '" + uuid + "' doit être existant",
            untypedDocument);
   }

   @Test
   public void copieFailureDocnotExist() throws IOException,
         SAEConsultationServiceException, ConnectionServiceEx, ParseException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {

      uuid = UUID.fromString("C675CED1-6ACE-463E-BA58-725A103A320A");

      String[] parsePatterns = new String[] { "yyyy-MM-dd" };
      Map<String, Object> metadatas = new HashMap<String, Object>();

      metadatas.put("apr", "ADELAIDE");
      metadatas.put("cop", "CER69");
      metadatas.put("cog", "UR750");
      metadatas.put("vrn", "11.1");
      metadatas.put("dom", "2");
      metadatas.put("act", "3");
      metadatas.put("nbp", "8");
      metadatas.put("ffi", "fmt/354");
      metadatas.put("cse", "ATT_PROD_002");
      metadatas.put("dre", DateUtils.parseDate("1999-12-30", parsePatterns));
      metadatas.put("dfc", DateUtils.parseDate("2012-01-01", parsePatterns));
      metadatas.put("cot", Boolean.TRUE);
      metadatas.put("CodeRND", "2.3.1.1.12");

      List<UntypedMetadata> fin = new ArrayList<UntypedMetadata>();

      fin.add(new UntypedMetadata("NbPages", "4"));
      fin.add(new UntypedMetadata("ApplicationProductrice", "Ada"));
      fin.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));

      try {
         UUID res = service.copie(uuid, fin);
      } catch (SAECaptureServiceEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (ReferentialRndException e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (UnknownCodeRndEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (ReferentialException e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (SAECopieServiceException e) {
         String message = "Il n'existe aucun document pour l'identifiant d'archivage 'c675ced1-6ace-463e-ba58-725a103a320a'";
         assertEquals(
               "le message d'erreur signifiant que le document n'existe pas",
               message, e.getMessage());
         uuid = null;
      } catch (RequiredStorageMetadataEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (InvalidValueTypeAndFormatMetadataEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (UnknownMetadataEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (DuplicatedMetadataEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (NotSpecifiableMetadataEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (EmptyDocumentEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (RequiredArchivableMetadataEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (NotArchivableMetadataEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (UnknownHashCodeEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (EmptyFileNameEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (MetadataValueNotInDictionaryEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (UnknownFormatException e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (ValidationExceptionInvalidFile e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (UnexpectedDomainException e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (InvalidPagmsCombinaisonException e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (CaptureExistingUuuidException e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      } catch (ArchiveInexistanteEx e) {
         fail("C'est l'exception SAECopieServiceException qui est attendue");
      }

   }
}
