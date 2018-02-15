package fr.urssaf.image.sae.services.transfert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.note.Note;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAETransfertServiceTest {

   @Autowired
   private StorageTransfertService storageTransfertService;

   @Autowired
   private StorageServiceProvider storageServiceProvider;

   @Autowired
   private SAETransfertService saeTransfertService;

   @Autowired
   private StorageServiceProvider provider;

   @Autowired
   private StorageTransfertService transfertService;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private ParametersService parametersService;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Autowired
   private ServiceProviderSupport traceServiceSupport;

   @Autowired
   @Qualifier("SAEServiceTestProvider")
   private SAEServiceTestProvider testProviderGNT;

   @Autowired
   @Qualifier("saeServiceTestProviderTransfert")
   private SAEServiceTestProvider testProviderGNS;

   private UUID uidDocGNT;
   private UUID uidDocGNS;

   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

   private static File srcFile = new File(
         "src/test/resources/doc/attestation_consultation.pdf");

   @After
   public void end() throws Exception {
      server.resetData();

      if (uidDocGNT != null) {
         testProviderGNT.deleteDocument(uidDocGNT);
      }
      if (uidDocGNS != null) {
         testProviderGNS.deleteDocument(uidDocGNS);
      }

      AuthenticationContext.setAuthenticationToken(null);
   }

   @Before
   public void init() throws Exception {

      server.resetData();

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
      String[] roles = new String[] { "modification", "recherche",
            "suppression", "transfert", "archivage_unitaire", "transfert_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("suppression", saePrmds);
      saeDroits.put("modification", saePrmds);
      saeDroits.put("recherche", saePrmds);
      saeDroits.put("transfert", saePrmds);
      saeDroits.put("archivage_unitaire", saePrmds);
      saeDroits.put("transfert_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);

      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.12");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("2");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("Libellé 2.3.1.1.12");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   @Test
   public void testUuidObligatoire() {
      try {
         saeTransfertService.transfertDoc(null);
         Assert.fail("une IllegalArgumentException est attendue");

      } catch (IllegalArgumentException e) {
         Assert.assertTrue("le message doit etre correct", e.getMessage()
               .contains("identifiant de l'archive"));

      } catch (Exception e) {
         Assert.fail("une IllegalArgumentException est attendue");
      }
   }

   private UUID insertDoc(SAEServiceTestProvider testProvider)
         throws IOException, ConnectionServiceEx, ParseException {

      byte[] content = FileUtils.readFileToByteArray(srcFile);

      String parsePatterns = new String("yyyy-MM-dd");
      Map<String, Object> metadatas = new HashMap<String, Object>();

      DateTimeFormatter formatter = DateTimeFormat.forPattern(parsePatterns)
            .withZoneUTC();
      DateTime dt = formatter.parseDateTime("2014-10-28");
      Date date = dt.toDate();

      metadatas.put("apr", "ADELAIDE");
      metadatas.put("cop", "CER69");
      metadatas.put("cog", "UR750");
      metadatas.put("vrn", "11.1");
      metadatas.put("dom", "2");
      metadatas.put("act", "3");
      metadatas.put("nbp", "2");
      metadatas.put("ffi", "fmt/1354");
      metadatas.put("cse", "ATT_PROD_001");
      metadatas.put("dre", date);// date réception
      metadatas.put("dfc", date);// date fin conservation
      metadatas.put("cot", Boolean.TRUE);

      String documentTitle = "attestation_transfert";
      String documentType = "pdf";
      String codeRND = "2.3.1.1.12";
      String title = "Attestation de transfert";

      return testProvider.captureDocument(content, metadatas, documentTitle,
            documentType, date, date, codeRND, title, null);
   }

   @Test
   // @Ignore("Mis en commentaire le temps de la release")
   public void testArchiveInexistante() throws TransfertException,
         ArchiveAlreadyTransferedException, InsertionIdGedExistantEx {

      // -- Appel méthode de transfert sur un doc déjà transféré
      try {

         UUID uuid = UUID.randomUUID();
         saeTransfertService.transfertDoc(uuid);
         Assert.fail("une ArchiveInexistanteEx est attendue");

      } catch (ArchiveInexistanteEx e) {
         // On a la bonne exception
      }
   }

   @Test
   // @Ignore("Mis en commentaire le temps de la release")
   public void testArchiveDejaTransferee() throws ConnectionServiceEx,
         IOException, ParseException {

      // -- Insertion d'un document de test sur la GNS
      uidDocGNS = insertDoc(testProviderGNS);

      // -- Recherche du document inséré
      Document doc = testProviderGNS.searchDocument(uidDocGNS);

      Assert.assertNotNull(
            "l'UUID '" + uidDocGNS + "' doit exister sur la GNS", doc);

      // -- Appel méthode de transfert sur un doc déjà transféré
      try {
         saeTransfertService.transfertDoc(uidDocGNS);
         Assert.fail("une ArchiveAlreadyTransferedException est attendue");

      } catch (ArchiveAlreadyTransferedException e) {
         // On a la bonne exception
      } catch (Exception e) {
         Assert.fail("une ArchiveAlreadyTransferedException est attendue: "
               + e.getMessage());
      }
   }

   @Test
   public void testModifierMeta() throws ConnectionServiceEx, IOException,
         ParseException, ArchiveAlreadyTransferedException,
         ArchiveInexistanteEx, TransfertException {

      String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";
      // -- Insertion d'un document de test sur la GNT
      UUID idArchive = insertDoc(testProviderGNT);

      List<StorageMetadata> listeMeta = new ArrayList<StorageMetadata>();
      listeMeta.add(new StorageMetadata("SM_TITLE",
            "Attestation de transfert test"));
      // Le codeRND peut ne pas être autorisé et bloquer le test
      // listeMeta.add(new StorageMetadata("SM_DOCUMENT_TYPE", "7.4.1.2.4"));

      try {
         saeTransfertService.controleDroitTransfert(idArchive);

         StorageDocument document = saeTransfertService
               .recupererDocMetaTransferable(idArchive);

         StorageDocument documentGNS = saeTransfertService
               .transfertControlePlateforme(document, idArchive, false, null);

         if (documentGNS == null) {
            document = saeTransfertService.updateMetaDocumentForTransfertMasse(
                  document, listeMeta, UUID.randomUUID());
            saeTransfertService.transfertDocument(document);
         } else {
            // -- Le document existe sur la GNS et sur la GNT
            String uuid = idArchive.toString();
            String message = "Le document {0} est anormalement présent en GNT et en GNS. Une intervention est nécessaire.";
            throw new ArchiveAlreadyTransferedException(StringUtils.replace(
                  message, "{0}", uuid));
         }

         saeTransfertService.deleteDocApresTransfert(idArchive);

         // -- Vérification présence fichier transféré
         Document doc = testProviderGNS.searchDocument(idArchive);
         Assert.assertNotNull("l'UUID '" + idArchive
               + "' doit exister dans la GNS", doc);

         // TEST sur métadonnée : Titre
         Assert.assertEquals("la métadonnée 'Titre(sm_title)' est incorrecte",
               "Attestation de transfert test", doc.getTitle());

      } catch (ConnectionServiceEx ex) {
         throw new TransfertException(erreur, ex);
      } catch (SearchingServiceEx ex) {
         throw new TransfertException(erreur, ex);
      } catch (ReferentialException ex) {
         throw new TransfertException(erreur, ex);
      } catch (InvalidSAETypeException ex) {
         throw new TransfertException(erreur, ex);
      } catch (MappingFromReferentialException ex) {
         throw new TransfertException(erreur, ex);
      } catch (RetrievalServiceEx ex) {
         throw new TransfertException(erreur, ex);
      } catch (TraitementRepriseAlreadyDoneException ex) {
         throw new TransfertException(erreur, ex);
      } catch (UnknownCodeRndEx e) {
         throw new TransfertException(erreur, e);
      }
   }

   @Test
   // @Ignore("Mis en commentaire le temps de la release")
   public void testSuccess() throws ConnectionServiceEx, IOException,
         ParseException, TransfertException, ArchiveAlreadyTransferedException,
         ArchiveInexistanteEx, SAECaptureServiceEx, ReferentialRndException,
         UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, InsertionIdGedExistantEx {

      // -- Insertion d'un document de test sur la GNT
      uidDocGNT = insertDoc(testProviderGNT);

      // -- Transfert du document vers la GNS
      saeTransfertService.transfertDoc(uidDocGNT);

      // -- Vérification présence fichier transféré
      Document doc = testProviderGNS.searchDocument(uidDocGNT);
      Assert.assertNotNull("l'UUID '" + uidDocGNT
            + "' doit exister dans la GNS", doc);

      // recupere l'identifiant du document que l'on a transfere en GNS
      // pour pouvoir le supprimer a la fin du test
      uidDocGNS = doc.getUuid();

      // le doc à été supprimé par transferDoc()
      // ne pas le re-suppr. dans "@After" erreur dfce.
      uidDocGNT = null;

      // test sur les métadonnées techniques
      assertDocument(doc);
   }

   // /**
   // * Attribut de {@link SAETransfertServiceImpl} injecté en mock
   // * pour les besoin des test (Cf. applicationContext-properties-test.xml)
   // */
   // @Autowired
   // private StorageTransfertService mockStorageTransfertService;
   //
   // @DirtiesContext
   // @Test(expected=TransfertException.class)
   // public void testInsertGNS_failure() throws ConnectionServiceEx,
   // IOException, ParseException, TransfertException,
   // ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
   // SAECaptureServiceEx, ReferentialRndException,
   // UnknownCodeRndEx, RequiredStorageMetadataEx,
   // InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
   // DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
   // RequiredArchivableMetadataEx, NotArchivableMetadataEx,
   // UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
   // MetadataValueNotInDictionaryEx,
   // ValidationExceptionInvalidFile, UnknownFormatException, InsertionServiceEx
   // {
   //
   // //-- Insertion d'un document de test sur la GNT
   // uidDocGNT = insertDoc(testProviderGNT);
   //
   // EasyMock.expect(mockStorageTransfertService.insertBinaryStorageDocument(EasyMock.anyObject(StorageDocument.class)))
   // .andThrow(new InsertionServiceEx("test unitaire")).once();
   // EasyMock.replay(mockStorageTransfertService);
   //
   // //-- Transfert du document vers la GNS
   // saeTransfertService.transfertDoc(uidDocGNT);
   //
   // EasyMock.reset(mockStorageTransfertService);
   // }

   private static void assertDocument(Document doc)
         throws FileNotFoundException, IOException {

      // TEST sur métadonnée : Titre
      Assert.assertEquals("la métadonnée 'Titre(sm_title)' est incorrecte",
            "Attestation de transfert", doc.getTitle());

      // TEST sur métadonnée : DateCreation
      Assert.assertEquals(
            "la métadonnée 'DateCreation(sm_creation_date)' est incorrecte",
            "2014-10-28 00:00:00",
            DateFormatUtils.formatUTC(doc.getCreationDate(), DATE_FORMAT));

      // TEST sur les métadonnées : DateModification & DateArchivage
      Assert.assertTrue(
            "la métadonnée 'DateArchivage(sm_archivage_date)':"
                  + doc.getArchivageDate()
                  + " et 'DateModification(sm_modification)':"
                  + doc.getModificationDate(),
            doc.getArchivageDate().equals(doc.getModificationDate()));

      // TEST sur métadonnée : DateDebutConservation
      Assert.assertEquals(
            "la métadonnée 'DateDebutConservation(sm_life_cycle_reference_date)' est incorrecte",
            "2014-10-28 00:00:00", DateFormatUtils.formatUTC(
                  doc.getLifeCycleReferenceDate(), DATE_FORMAT));

      // TEST sur métadonnée : TypeHash
      Assert.assertEquals(
            "la métadonnée 'TypeHash(sm_digest_algorithm)' est incorrecte",
            "SHA-1", doc.getDigestAlgorithm());

      // TEST sur métadonnée : Hash
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      Assert.assertEquals("la métadonnée 'Hash(sm_digest)' est incorrecte",
            hash, doc.getDigest());

      // TEST sur métadonnée : NomFichier
      Assert.assertEquals(
            "la métadonnée 'NomFichier(sm_filename)' est incorrecte",
            "attestation_transfert", doc.getFilename());

      Assert.assertEquals(
            "la métadonnée 'NomFichier(sm_extension)' est incorrecte", "pdf",
            doc.getExtension());
   }

   @Test
   // @Ignore("Mis en commentaire le temps de la release")
   public void testTransfertNoteSuccess() throws ConnectionServiceEx,
         IOException, ParseException, TransfertException,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException, InsertionIdGedExistantEx {

      // -- Insertion d'un document de test sur la GNT
      uidDocGNT = insertDoc(testProviderGNT);

      // -- Ajout d'une note au document
      testProviderGNT.addNoteDocument(uidDocGNT, "Contenu de la note");

      // -- Transfert du document vers la GNS
      saeTransfertService.transfertDoc(uidDocGNT);

      // -- Vérification présence fichier transféré
      Document doc = testProviderGNS.searchDocument(uidDocGNT);
      Assert.assertNotNull("l'UUID '" + uidDocGNT
            + "' doit exister dans la GNS", doc);

      // recupere l'identifiant du document que l'on a transfere en GNS
      // pour pouvoir le supprimer a la fin du test
      uidDocGNS = doc.getUuid();

      // le doc à été supprimé par transferDoc()
      // ne pas le re-suppr. dans "@After" erreur dfce.
      uidDocGNT = null;

      // test sur le bon transfert de la note
      List<Note> listeNotes = testProviderGNS.getNoteDocument(uidDocGNS);

      if (listeNotes.size() > 0) {
         Assert.assertEquals("Le contenu de la note est incorrect",
               "Contenu de la note", listeNotes.get(0).getContent());
      } else {
         Assert.fail("Une note doit être présente sur le document");
      }

   }
}