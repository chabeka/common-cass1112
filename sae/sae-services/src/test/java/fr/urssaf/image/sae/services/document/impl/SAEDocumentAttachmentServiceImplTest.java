package fr.urssaf.image.sae.services.document.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDeniedException;
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
import fr.urssaf.image.sae.services.document.SAEDocumentAttachmentService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentAttachmentEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
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
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEDocumentAttachmentServiceImplTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAECaptureServiceTest.class);

   @Autowired
   @Qualifier("saeDocumentAttachmentService")
   private SAEDocumentAttachmentService docAttService;

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
      String[] roles = new String[] { "archivage_unitaire",
            "ajout_doc_attache", "consultation" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
      saeDroits.put("ajout_doc_attache", saePrmds);
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
   public void ajoutDocumentAttacheUrlTestSuccess()
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx, EmptyDocumentEx,
         EmptyFileNameEx, IOException, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException {

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

      docAttService.addDocumentAttachmentUrl(uuid, urlEcdeDocument);

      StorageDocumentAttachment storagedocAtt = docAttService
            .getDocumentAttachment(uuid);

      if (storagedocAtt != null) {
         assertEquals("UUID du document incorrect", uuid,
               storagedocAtt.getDocUuid());
         assertEquals("L'extension est invalide", "pdf",
               storagedocAtt.getExtension());
         assertEquals("Le nom est invalide", "attestation_consultation",
               storagedocAtt.getName());
         assertEquals("Le hash est invalide", StringUtils.upperCase(hash),
               StringUtils.upperCase(storagedocAtt.getHash()));

      } else {
         fail("Un document attaché devrait être trouvé");
      }

      // supprime le fichier attestation_consultation.pdf sur le repertoire
      // de
      // l'ecde
      fileDoc.delete();

   }

   @Test
   public void ajoutDocumentAttacheBinaireTestSuccess()
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx, EmptyDocumentEx,
         EmptyFileNameEx, IOException, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException {

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

      DataSource fds = new FileDataSource(
            "src/test/resources/doc/attestation_consultation.pdf");
      DataHandler contenu = new DataHandler(fds);
      docAttService.addDocumentAttachmentBinaire(uuid,
            "attestation_consultation", "pdf", contenu);

      StorageDocumentAttachment storagedocAtt = docAttService
            .getDocumentAttachment(uuid);

      if (storagedocAtt != null) {
         assertEquals("UUID du document incorrect", uuid,
               storagedocAtt.getDocUuid());
         assertEquals("L'extension est invalide", "pdf",
               storagedocAtt.getExtension());
         assertEquals("Le nom est invalide", "attestation_consultation",
               storagedocAtt.getName());
         assertEquals("Le hash est invalide", StringUtils.upperCase(hash),
               StringUtils.upperCase(storagedocAtt.getHash()));

      } else {
         fail("Un document attaché devrait être trouvé");
      }

      // supprime le fichier attestation_consultation.pdf sur le repertoire
      // de
      // l'ecde
      fileDoc.delete();

   }

   /**
    * Test qu'une exception est bien levée si le document auquel on souhaite
    * rattacher un document n'existe pas
    * 
    * @throws EmptyFileNameEx
    * @throws EmptyDocumentEx
    * @throws SAEDocumentAttachmentEx
    * @throws CaptureBadEcdeUrlEx 
    */
   @Test
   public void ajoutDocAttacheTestDocUuidInexistant()
         throws SAEDocumentAttachmentEx, EmptyDocumentEx, EmptyFileNameEx, CaptureBadEcdeUrlEx {

      UUID uuid = UUID.randomUUID();
      try {
         ecde = ecdeTestTools
               .buildEcdeTestDocument("attestation_consultation.pdf");
         URI urlEcdeDocument = ecde.getUrlEcdeDocument();
         docAttService.addDocumentAttachmentUrl(uuid, urlEcdeDocument);
         fail("Une exception devrait être renvoyée car le document n'existe pas");
      } catch (ArchiveInexistanteEx e) {
         String message = "Il n'existe aucun document pour l'identifiant d'archivage '"
               + uuid + "'";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }
   }

   /**
    * Test qu'une exception est bien levée si l'UUID est null
    * 
    * @throws EmptyFileNameEx
    * @throws EmptyDocumentEx
    * @throws SAEDocumentAttachmentEx
    * @throws ArchiveInexistanteEx
    * @throws CaptureBadEcdeUrlEx 
    */
   @Test
   public void ajoutDocAttacheUrlDocUuidNullTest() throws SAEDocumentAttachmentEx,
         EmptyDocumentEx, EmptyFileNameEx, ArchiveInexistanteEx, CaptureBadEcdeUrlEx {

      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();
      try {
         docAttService.addDocumentAttachmentUrl(null, urlEcdeDocument);
         fail("Une exception devrait être renvoyée car l'UUID du document est nul");
      } catch (IllegalArgumentException e) {
         String message = "L'argument ''UUID du document parent'' doit être renseigné ou être non null.";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }

   }
   
   /**
    * Test qu'une exception est bien levée si l'UUID est null
    * 
    * @throws EmptyFileNameEx
    * @throws EmptyDocumentEx
    * @throws SAEDocumentAttachmentEx
    * @throws ArchiveInexistanteEx
    */
   @Test
   public void ajoutDocAttacheBinaireDocUuidNullTest() throws SAEDocumentAttachmentEx,
         EmptyDocumentEx, EmptyFileNameEx, ArchiveInexistanteEx {

      try {
         docAttService.addDocumentAttachmentBinaire(null, "docName", "extension", null);
         fail("Une exception devrait être renvoyée car l'UUID du document est nul");
      } catch (IllegalArgumentException e) {
         String message = "L'argument ''UUID du document parent'' doit être renseigné ou être non null.";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }

   }

   /**
    * Test qu'une exception est bien levée si l'url est null
    * 
    * @throws EmptyFileNameEx
    * @throws EmptyDocumentEx
    * @throws SAEDocumentAttachmentEx
    * @throws ArchiveInexistanteEx
    * @throws IOException
    * @throws CaptureExistingUuuidException
    * @throws InvalidPagmsCombinaisonException
    * @throws UnexpectedDomainException
    * @throws UnknownFormatException
    * @throws ValidationExceptionInvalidFile
    * @throws MetadataValueNotInDictionaryEx
    * @throws CaptureEcdeUrlFileNotFoundEx
    * @throws CaptureBadEcdeUrlEx
    * @throws UnknownHashCodeEx
    * @throws NotArchivableMetadataEx
    * @throws RequiredArchivableMetadataEx
    * @throws NotSpecifiableMetadataEx
    * @throws DuplicatedMetadataEx
    * @throws UnknownMetadataEx
    * @throws InvalidValueTypeAndFormatMetadataEx
    * @throws RequiredStorageMetadataEx
    * @throws UnknownCodeRndEx
    * @throws ReferentialRndException
    * @throws SAECaptureServiceEx
    */
   @Test
   public void ajoutDocAttacheTestUrlNull() throws SAEDocumentAttachmentEx,
         EmptyDocumentEx, EmptyFileNameEx, ArchiveInexistanteEx, IOException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException {

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
         docAttService.addDocumentAttachmentUrl(uuid, null);
         fail("Une exception devrait être renvoyée car l'url du document est nul");
      } catch (IllegalArgumentException e) {
         String message = "L'argument ''URL du document attaché'' doit être renseigné ou être non null.";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }

   }

   @Test
   public void ajoutDocumentAttacheBinaireArgumentNullTest()
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx, EmptyDocumentEx,
         EmptyFileNameEx, IOException, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException {

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

      DataSource fds = new FileDataSource(
            "src/test/resources/doc/attestation_consultation.pdf");
      DataHandler contenu = new DataHandler(fds);

      try {
         docAttService.addDocumentAttachmentBinaire(uuid,
              null, "pdf", contenu);
      } catch (IllegalArgumentException e) {
         String message = "L'argument ''Nom du document attaché'' doit être renseigné ou être non null.";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }
      
      try {
         docAttService.addDocumentAttachmentBinaire(uuid,
              "", "pdf", contenu);
      } catch (IllegalArgumentException e) {
         String message = "L'argument ''Nom du document attaché'' doit être renseigné ou être non null.";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }
     
      try {
         docAttService.addDocumentAttachmentBinaire(uuid,
              "docName", null, contenu);
      } catch (IllegalArgumentException e) {
         String message = "L'argument ''Extension du document attaché'' doit être renseigné ou être non null.";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }

      try {
         docAttService.addDocumentAttachmentBinaire(uuid,
              "docName", "", contenu);
      } catch (IllegalArgumentException e) {
         String message = "L'argument ''Extension du document attaché'' doit être renseigné ou être non null.";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }
      
      try {
         docAttService.addDocumentAttachmentBinaire(uuid,
              "docName", "pdf", null);
      } catch (IllegalArgumentException e) {
         String message = "L'argument ''Contenu du document attaché'' doit être renseigné ou être non null.";
         assertEquals("Erreur message attendu", message, e.getMessage());
      }
      
      // supprime le fichier attestation_consultation.pdf sur le repertoire
      // de
      // l'ecde
      fileDoc.delete();

   }

   @Test(expected = AccessDeniedException.class)
   public void addDocumentAttacheUrl_accessDenied() throws IOException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         CaptureExistingUuuidException, SAEDocumentAttachmentEx,
         ArchiveInexistanteEx, EmptyFileNameEx {

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
      String[] roles = new String[] { "recherche" };
      saePrmds.add(saePrmd);

      saeDroits.put("recherche", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

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

      docAttService.addDocumentAttachmentUrl(uuid, urlEcdeDocument);

      Assert.fail("exception attendue");
   }
   
   
   @Test(expected = AccessDeniedException.class)
   public void addDocumentAttacheBinaire_accessDenied() throws IOException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         CaptureExistingUuuidException, SAEDocumentAttachmentEx,
         ArchiveInexistanteEx, EmptyFileNameEx {

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
      String[] roles = new String[] { "recherche" };
      saePrmds.add(saePrmd);

      saeDroits.put("recherche", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

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

      DataSource fds = new FileDataSource(
            "src/test/resources/doc/attestation_consultation.pdf");
      DataHandler contenu = new DataHandler(fds);
      docAttService.addDocumentAttachmentBinaire(uuid,
            "attestation_consultation", "pdf", contenu);
      
      docAttService.addDocumentAttachmentBinaire(uuid, "attestation_consultation", "pdf", contenu);

      Assert.fail("exception attendue");
   }
}
