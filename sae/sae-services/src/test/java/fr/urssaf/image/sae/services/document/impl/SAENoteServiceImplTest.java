package fr.urssaf.image.sae.services.document.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestDocument;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.capture.SAECaptureServiceTest;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAENoteService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentNoteException;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
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
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAENoteServiceImplTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAECaptureServiceTest.class);

   @Autowired
   @Qualifier("saeNoteService")
   private SAENoteService noteService;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider provider;

   private UUID uuid;

   private static String path;

   private EcdeTestDocument ecde;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private ParametersService parametersService;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Autowired
   @Qualifier("SAEServiceTestProvider")
   private SAEServiceTestProvider testProvider;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private SAECaptureService captureService;

   @Autowired
   @Qualifier("saeConsultationService")
   private SAEConsultationService consultationService;

   @BeforeClass
   public static void beforeClass() throws IOException {
      path = new ClassPathResource("doc/attestation_consultation.pdf")
            .getFile().getAbsolutePath();
   }

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
      String[] roles = new String[] { "archivage_unitaire", "ajoutNote",
            "consultation" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
      saeDroits.put("ajoutNote", saePrmds);
      saeDroits.put("consultation", saePrmds);
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

      provider.openConnexion();
   }

   @After
   public void after() throws Exception {
      // suppression de l'insertion
      if (uuid != null) {
         testProvider.deleteDocument(uuid);
      }

      AuthenticationContext.setAuthenticationToken(null);

      server.resetData();
      provider.closeConnexion();

      if (ecde != null) {
         // supprime le repertoire ecde
         ecdeTestTools.cleanEcdeTestDocument(ecde);
      }
   }

   @Test
   public void ajoutNoteTestSuccess() throws IOException, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, SAEDocumentNoteException,
         SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, ArchiveInexistanteEx {

      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
      LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
            + repertoireEcde.getAbsoluteFile());
      File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
      ClassPathResource resDoc = new ClassPathResource(
            "doc/attestation_consultation.pdf");
      FileOutputStream fos = new FileOutputStream(fileDoc);
      IOUtils.copy(resDoc.getInputStream(), fos);
      resDoc.getInputStream().close();
      fos.close();

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();

      // liste des métadonnées obligatoires
      metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new UntypedMetadata("NbPages", "2"));
      metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      metadatas.add(new UntypedMetadata("Hash", StringUtils.upperCase(hash)));
      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = captureService.capture(metadatas, urlEcdeDocument).getIdDoc();
      LOG.debug("document archivé dans DFCE:" + uuid);

      noteService.addDocumentNote(uuid, "Contenu de la note", "login");
      List<String> listeMeta = new ArrayList<String>();
      listeMeta.add("Note");
      ConsultParams consultParams = new ConsultParams(uuid, listeMeta);
      UntypedDocument uDoc = consultationService.consultation(consultParams);

      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date dateCourante = new Date();
      String dateString = dateFormat.format(dateCourante);

      if (uDoc.getUMetadatas().size() > 0) {
         // rq : login n'est pas remplacé, DFCE écrase pas _ADMIN 
         
         assertEquals("Contenu de la note invalide",
               "[{\"contenu\":\"Contenu de la note\",\"dateCreation\":\""
                     + dateString + "\",\"auteur\":\"_ADMIN\"}]", uDoc
                     .getUMetadatas().get(0).getValue());
         
      } else {
         fail("Une note devrait être rattachée au document");
      }

      // supprime le fichier attestation_consultation.pdf sur le repertoire
      // de
      // l'ecde
      fileDoc.delete();

   }

   /**
    * Test qu'une exception est bien levée si le document auquel on souhaite
    * ajouter une note n'existe pas
    * 
    * @throws SAEDocumentNoteException
    */
   @Test
   public void ajoutNoteTestDocUuidInexistant() throws IOException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, SAEDocumentNoteException {

      UUID uuid = UUID.randomUUID();

      try {
         noteService.addDocumentNote(uuid, "Contenu de la note", "login");
         fail("Une exception devrait être renvoyée car le document n'existe pas");
      } catch (ArchiveInexistanteEx e) {
         String message = "Il n'existe aucun document pour l'identifiant d'archivage "
               + uuid;
         assertEquals("Erreur message attendu", message, e.getMessage());
      }

   }

   @Test
   public void ajoutNoteContenuVide() throws IOException, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, SAEDocumentNoteException,
         SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, ArchiveInexistanteEx {

      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
      LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
            + repertoireEcde.getAbsoluteFile());
      File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
      ClassPathResource resDoc = new ClassPathResource(
            "doc/attestation_consultation.pdf");
      FileOutputStream fos = new FileOutputStream(fileDoc);
      IOUtils.copy(resDoc.getInputStream(), fos);
      resDoc.getInputStream().close();
      fos.close();

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();

      // liste des métadonnées obligatoires
      metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new UntypedMetadata("NbPages", "2"));
      metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      metadatas.add(new UntypedMetadata("Hash", StringUtils.upperCase(hash)));
      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = captureService.capture(metadatas, urlEcdeDocument).getIdDoc();
      LOG.debug("document archivé dans DFCE:" + uuid);

      try {
         noteService.addDocumentNote(uuid, "", "login");
         fail("Une exception devrait être renvoyée car le contenu de la note ne doit pas être vide");
      } catch (IllegalArgumentException aExp) {
         assert (aExp.getMessage().contains("Le contenu de la note est null"));
      }

      try {
         noteService.addDocumentNote(uuid, null, "login");
         fail("Une exception devrait être renvoyée car le contenu de la note ne doit pas être null");
      } catch (IllegalArgumentException aExp) {
         assert (aExp.getMessage().contains("Le contenu de la note est null"));
      }

      // supprime le fichier attestation_consultation.pdf sur le repertoire
      // de
      // l'ecde
      fileDoc.delete();

   }

   /**
    * Test qu'une exception est bien levée si le document auquel on souhaite
    * ajouter une note n'existe pas
    * 
    * @throws SAEDocumentNoteException
    * @throws ArchiveInexistanteEx
    */
   @Test
   public void ajoutNoteUUIDNull() throws DocumentNoteServiceEx {

      try {
         //noteService.addDocumentNote(null, "Contenu de la note", "login");
         // Dans la couche service, on est trop haut pour faire le test d'id null 
         // puisque dans cette couche, on va rechercher le document par id, et détecter
         // que l'id est null. Dans ce cas, on remonte une erreur sur la recherche et non 
         // sur l'ajout de note
         provider.getStorageDocumentService().addDocumentNote(null, "Contenu de la note", "login");
         fail("Une exception devrait être renvoyée car l'UUID du document ne doit pas être null");
      } catch (IllegalArgumentException aExp) {
         assert (aExp.getMessage().contains("L'identifiant du document est null"));
      }

   }

   @Test
   public void ajoutPlusieurNotesTestSuccess() throws IOException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         SAEDocumentNoteException, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         ArchiveInexistanteEx {

      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
      LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
            + repertoireEcde.getAbsoluteFile());
      File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
      ClassPathResource resDoc = new ClassPathResource(
            "doc/attestation_consultation.pdf");
      FileOutputStream fos = new FileOutputStream(fileDoc);
      IOUtils.copy(resDoc.getInputStream(), fos);
      resDoc.getInputStream().close();
      fos.close();

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();

      // liste des métadonnées obligatoires
      metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new UntypedMetadata("NbPages", "2"));
      metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      metadatas.add(new UntypedMetadata("Hash", StringUtils.upperCase(hash)));
      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = captureService.capture(metadatas, urlEcdeDocument).getIdDoc();
      LOG.debug("document archivé dans DFCE:" + uuid);

      noteService.addDocumentNote(uuid, "Contenu de la note 1", "login");
      noteService.addDocumentNote(uuid, "Contenu de la note 2", "login");

      List<String> listeMeta = new ArrayList<String>();
      listeMeta.add("Note");
      ConsultParams consultParams = new ConsultParams(uuid, listeMeta);
      UntypedDocument uDoc = consultationService.consultation(consultParams);

      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date dateCourante = new Date();
      String dateString = dateFormat.format(dateCourante);

      if (uDoc.getUMetadatas().size() > 0) {
         assertEquals(
               "Contenu de la note invalide",
               "[{\"contenu\":\"Contenu de la note 1\",\"dateCreation\":\""
                     + dateString
                     + "\",\"auteur\":\"_ADMIN\"},{\"contenu\":\"Contenu de la note 2\",\"dateCreation\":\""
                     + dateString + "\",\"auteur\":\"_ADMIN\"}]", uDoc
                     .getUMetadatas().get(0).getValue());
      } else {
         fail("Une note devrait être rattachée au document");
      }

      // supprime le fichier attestation_consultation.pdf sur le repertoire
      // de
      // l'ecde
      fileDoc.delete();

   }
}
