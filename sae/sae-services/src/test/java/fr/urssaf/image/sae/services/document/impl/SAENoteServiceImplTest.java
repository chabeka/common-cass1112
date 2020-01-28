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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
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
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.commons.utils.ModeApiAllUtils;
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
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
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
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
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
  @Qualifier("storageDocumentService")
  private StorageDocumentService storageDocumentService;

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
    ModeApiAllUtils.setAllModeAPIThrift();
  }

  @Before
  public void before() throws Exception {

    // initialisation de l'uuid de l'archive
    uuid = null;

    // initialisation du contexte de sécurité
    final VIContenuExtrait viExtrait = new VIContenuExtrait();
    viExtrait.setCodeAppli("TESTS_UNITAIRES");
    viExtrait.setIdUtilisateur("UTILISATEUR TEST");

    final SaeDroits saeDroits = new SaeDroits();
    final List<SaePrmd> saePrmds = new ArrayList<>();
    final SaePrmd saePrmd = new SaePrmd();
    saePrmd.setValues(new HashMap<String, String>());
    final Prmd prmd = new Prmd();
    prmd.setBean("permitAll");
    prmd.setCode("default");
    saePrmd.setPrmd(prmd);
    final String[] roles = new String[] { "ROLE_archivage_unitaire", "ROLE_ajout_note",
    "ROLE_consultation" };
    saePrmds.add(saePrmd);

    saeDroits.put("archivage_unitaire", saePrmds);
    saeDroits.put("ajout_note", saePrmds);
    saeDroits.put("consultation", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(), viExtrait, roles);
    AuthenticationContext.setAuthenticationToken(token);

    // Paramétrage du RND

    parametersService.setVersionRndDateMaj(new Date());
    parametersService.setVersionRndNumero("11.2");

    final TypeDocument typeDocCree = new TypeDocument();
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

    AuthenticationContext.setAuthenticationToken(null);

    server.resetData();

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
  MetaDataUnauthorizedToConsultEx, ArchiveInexistanteEx, CaptureExistingUuuidException {

    ecde = ecdeTestTools
        .buildEcdeTestDocument("attestation_consultation.pdf");

    final File repertoireEcde = ecde.getRepEcdeDocuments();
    final URI urlEcdeDocument = ecde.getUrlEcdeDocument();

    // copie le fichier attestation_consultation.pdf
    // dans le repertoire de l'ecde
    LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
        + repertoireEcde.getAbsoluteFile());
    final File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
    final ClassPathResource resDoc = new ClassPathResource(
        "doc/attestation_consultation.pdf");
    final FileOutputStream fos = new FileOutputStream(fileDoc);
    IOUtils.copy(resDoc.getInputStream(), fos);
    resDoc.getInputStream().close();
    fos.close();

    final File srcFile = new File(
        "src/test/resources/doc/attestation_consultation.pdf");

    final List<UntypedMetadata> metadatas = new ArrayList<>();

    // liste des métadonnées obligatoires
    metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
    metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
    metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
    metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
    metadatas.add(new UntypedMetadata("NbPages", "2"));
    metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
    metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
    final String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
    metadatas.add(new UntypedMetadata("Hash", StringUtils.upperCase(hash)));
    metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
    metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));

    // liste des métadonnées non obligatoires
    metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
    metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

    uuid = captureService.capture(metadatas, urlEcdeDocument).getIdDoc();
    LOG.debug("document archivé dans DFCE:" + uuid);

    noteService.addDocumentNote(uuid, "Contenu de la note", "login");
    final List<String> listeMeta = new ArrayList<>();
    listeMeta.add("Note");
    final ConsultParams consultParams = new ConsultParams(uuid, listeMeta);
    final UntypedDocument uDoc = consultationService.consultation(consultParams);

    if (uDoc.getUMetadatas().size() > 0) {
      // rq : login n'est pas remplacé, DFCE écrase par _ADMIN

      final String noteContent = uDoc.getUMetadatas().get(0).getValue();
      validateNoteContent(noteContent);

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

    final UUID uuid = UUID.randomUUID();

    try {
      noteService.addDocumentNote(uuid, "Contenu de la note", "login");
      fail("Une exception devrait être renvoyée car le document n'existe pas");
    } catch (final ArchiveInexistanteEx e) {
      final String message = "Il n'existe aucun document pour l'identifiant d'archivage '"
          + uuid + "'";
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
  MetaDataUnauthorizedToConsultEx, ArchiveInexistanteEx, CaptureExistingUuuidException {

    ecde = ecdeTestTools
        .buildEcdeTestDocument("attestation_consultation.pdf");

    final File repertoireEcde = ecde.getRepEcdeDocuments();
    final URI urlEcdeDocument = ecde.getUrlEcdeDocument();

    // copie le fichier attestation_consultation.pdf
    // dans le repertoire de l'ecde
    LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
        + repertoireEcde.getAbsoluteFile());
    final File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
    final ClassPathResource resDoc = new ClassPathResource(
        "doc/attestation_consultation.pdf");
    final FileOutputStream fos = new FileOutputStream(fileDoc);
    IOUtils.copy(resDoc.getInputStream(), fos);
    resDoc.getInputStream().close();
    fos.close();

    final File srcFile = new File(
        "src/test/resources/doc/attestation_consultation.pdf");

    final List<UntypedMetadata> metadatas = new ArrayList<>();

    // liste des métadonnées obligatoires
    metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
    metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
    metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
    metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
    metadatas.add(new UntypedMetadata("NbPages", "2"));
    metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
    metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
    final String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
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
    } catch (final IllegalArgumentException aExp) {
      assert aExp.getMessage().contains("Le contenu de la note est null");
    }

    try {
      noteService.addDocumentNote(uuid, null, "login");
      fail("Une exception devrait être renvoyée car le contenu de la note ne doit pas être null");
    } catch (final IllegalArgumentException aExp) {
      assert aExp.getMessage().contains("Le contenu de la note est null");
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
      // noteService.addDocumentNote(null, "Contenu de la note", "login");
      // Dans la couche service, on est trop haut pour faire le test d'id
      // null
      // puisque dans cette couche, on va rechercher le document par id, et
      // détecter
      // que l'id est null. Dans ce cas, on remonte une erreur sur la
      // recherche et non
      // sur l'ajout de note
      storageDocumentService.addDocumentNote(null,
                                             "Contenu de la note", "login");
      fail("Une exception devrait être renvoyée car l'UUID du document ne doit pas être null");
    } catch (final IllegalArgumentException aExp) {
      assert aExp.getMessage()
      .contains("L'identifiant du document est null");
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
  ArchiveInexistanteEx, CaptureExistingUuuidException {

    ecde = ecdeTestTools
        .buildEcdeTestDocument("attestation_consultation.pdf");

    final File repertoireEcde = ecde.getRepEcdeDocuments();
    final URI urlEcdeDocument = ecde.getUrlEcdeDocument();

    // copie le fichier attestation_consultation.pdf
    // dans le repertoire de l'ecde
    LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
        + repertoireEcde.getAbsoluteFile());
    final File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
    final ClassPathResource resDoc = new ClassPathResource(
        "doc/attestation_consultation.pdf");
    final FileOutputStream fos = new FileOutputStream(fileDoc);
    IOUtils.copy(resDoc.getInputStream(), fos);
    resDoc.getInputStream().close();
    fos.close();

    final File srcFile = new File(
        "src/test/resources/doc/attestation_consultation.pdf");

    final List<UntypedMetadata> metadatas = new ArrayList<>();

    // liste des métadonnées obligatoires
    metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
    metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
    metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
    metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
    metadatas.add(new UntypedMetadata("NbPages", "2"));
    metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
    metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
    final String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
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

    final List<String> listeMeta = new ArrayList<>();
    listeMeta.add("Note");
    final ConsultParams consultParams = new ConsultParams(uuid, listeMeta);
    final UntypedDocument uDoc = consultationService.consultation(consultParams);

    if (uDoc.getUMetadatas().size() > 0) {
      /*
       * assertEquals( "Contenu de la note invalide",
       * "[{\"contenu\":\"Contenu de la note 1\",\"dateCreation\":\"" +
       * dateString +
       * "\",\"auteur\":\"_ADMIN\"},{\"contenu\":\"Contenu de la note 2\",\"dateCreation\":\""
       * + dateString + "\",\"auteur\":\"_ADMIN\"}]", uDoc
       * .getUMetadatas().get(0).getValue());
       */
      final String contenu = uDoc.getUMetadatas().get(0).getValue();

      final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      final Date todayDate = new Date();
      final String todayString= dateFormat.format(todayDate);
      final Calendar c = Calendar.getInstance();
      c.setTime(todayDate);
      c.add(Calendar.DATE, 1);
      final Date tomorowDate = c.getTime();
      final String tomorowString = dateFormat.format(tomorowDate);

      Assert.assertThat(contenu, JUnitMatchers.containsString("[{\"contenu\":\"Contenu de la note 1\""));
      Assert.assertThat(contenu, JUnitMatchers.containsString("\",\"auteur\":\"login\"},{\"contenu\":\"Contenu de la note 2\""));
      Assert.assertThat(contenu, JUnitMatchers.either(JUnitMatchers.containsString(todayString))
                        .or(JUnitMatchers.containsString(tomorowString)));
    } else {
      fail("Une note devrait être rattachée au document");
    }

    // supprime le fichier attestation_consultation.pdf sur le repertoire
    // de
    // l'ecde
    fileDoc.delete();
  }

  @Test(expected = AccessDeniedException.class)
  public void addDocumentNote_accessDenied() throws IOException,
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
  ArchiveInexistanteEx, CaptureExistingUuuidException {

    final VIContenuExtrait viExtrait = new VIContenuExtrait();
    viExtrait.setCodeAppli("TESTS_UNITAIRES");
    viExtrait.setIdUtilisateur("UTILISATEUR TEST");

    final SaeDroits saeDroits = new SaeDroits();
    final List<SaePrmd> saePrmds = new ArrayList<>();
    final SaePrmd saePrmd = new SaePrmd();
    saePrmd.setValues(new HashMap<String, String>());
    final Prmd prmd = new Prmd();
    prmd.setBean("permitAll");
    prmd.setCode("default");
    saePrmd.setPrmd(prmd);
    final String[] roles = new String[] { "ROLE_recherche" };
    saePrmds.add(saePrmd);

    saeDroits.put("recherche", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(), viExtrait, roles);
    AuthenticationContext.setAuthenticationToken(token);

    ecde = ecdeTestTools
        .buildEcdeTestDocument("attestation_consultation.pdf");

    final File repertoireEcde = ecde.getRepEcdeDocuments();
    final URI urlEcdeDocument = ecde.getUrlEcdeDocument();

    // copie le fichier attestation_consultation.pdf
    // dans le repertoire de l'ecde
    LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
        + repertoireEcde.getAbsoluteFile());
    final File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
    final ClassPathResource resDoc = new ClassPathResource(
        "doc/attestation_consultation.pdf");
    final FileOutputStream fos = new FileOutputStream(fileDoc);
    IOUtils.copy(resDoc.getInputStream(), fos);
    resDoc.getInputStream().close();
    fos.close();

    final File srcFile = new File(
        "src/test/resources/doc/attestation_consultation.pdf");

    final List<UntypedMetadata> metadatas = new ArrayList<>();

    // liste des métadonnées obligatoires
    metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
    metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
    metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
    metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
    metadatas.add(new UntypedMetadata("NbPages", "2"));
    metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
    metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
    final String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
    metadatas.add(new UntypedMetadata("Hash", StringUtils.upperCase(hash)));
    metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
    metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));

    // liste des métadonnées non obligatoires
    metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
    metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

    uuid = captureService.capture(metadatas, urlEcdeDocument).getIdDoc();
    LOG.debug("document archivé dans DFCE:" + uuid);

    noteService.addDocumentNote(uuid, "Contenu de la note 1", "login");

    Assert.fail("exception attendue");
  }

  /**
   * Vérifie le contenu de la note
   * @param noteContent : contenu de la note
   */
  private void validateNoteContent(final String contenu) {
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    final Date todayDate = new Date();
    final String todayString= dateFormat.format(todayDate);
    final Calendar c = Calendar.getInstance();
    c.setTime(todayDate);
    c.add(Calendar.DATE, 1);
    final Date tomorowDate = c.getTime();
    final String tomorowString = dateFormat.format(tomorowDate);

    Assert.assertThat(contenu, JUnitMatchers.containsString("[{\"contenu\":\"Contenu de la note\""));
    Assert.assertThat(contenu, JUnitMatchers.either(JUnitMatchers.containsString(todayString))
                      .or(JUnitMatchers.containsString(tomorowString)));
    Assert.assertThat(contenu, JUnitMatchers.containsString("\"auteur\":\"login\"}]"));
  }
}
