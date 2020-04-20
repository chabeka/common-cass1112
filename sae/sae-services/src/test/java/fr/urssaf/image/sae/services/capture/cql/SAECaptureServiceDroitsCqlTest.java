package fr.urssaf.image.sae.services.capture.cql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDeniedException;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestDocument;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.AbstractServiceCqlTest;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
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
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)*/
public class SAECaptureServiceDroitsCqlTest extends AbstractServiceCqlTest {

  @Autowired
  private EcdeTestTools ecdeTestTools;

  private static final Logger LOG = LoggerFactory
      .getLogger(SAECaptureServiceDroitsCqlTest.class);

  @Autowired
  private SAECaptureService service;

  @Autowired
  @Qualifier("SAEServiceTestProvider")
  private SAEServiceTestProvider testProvider;

  private UUID uuid;

  private EcdeTestDocument ecde;

  private File fileDoc;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;
  /*
   * @BeforeClass
   * public static void beforeClass() throws IOException {
   * ModeApiAllUtils.setAllModeAPICql();
   * }
   */

  @Before
  public void before() throws InterruptedException, Exception {

    initMetadata();
    modeApiSupport.initTables(ModeGestionAPI.MODE_API.DATASTAX);
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
    final Map<String, List<String>> metadata = new HashMap<>();
    metadata.put("CodeOrganismeGestionnaire", Arrays
                 .asList(new String[] { "UR690" }));
    prmd.setMetadata(metadata);
    prmd.setCode("CER75");
    saePrmd.setPrmd(prmd);
    final String[] roles = new String[] { "ROLE_archivage_unitaire" };
    saePrmds.add(saePrmd);

    saeDroits.put("archivage_unitaire", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(), viExtrait, roles);
    AuthenticationContext.setAuthenticationToken(token);
  }


  @After
  public void after() throws ConnectionServiceEx, DeletionServiceEx, IOException, InterruptedException {
    // suppression de l'insertion
    if (uuid != null) {
      testProvider.deleteDocument(uuid);
    }

    AuthenticationContext.setAuthenticationToken(null);
    server.clearTables();
    if (fileDoc != null) {
      // supprime le fichier attestation_consultation.pdf sur le repertoire de l'ecde
      fileDoc.delete();
    }

    if (ecde != null) {
      // supprime le repertoire ecde
      ecdeTestTools.cleanEcdeTestDocument(ecde);
    }
  }

  @Test(expected = AccessDeniedException.class)
  public void captureAccessDenied() throws SAECaptureServiceEx,
  ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
  InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
  DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
  RequiredArchivableMetadataEx, NotArchivableMetadataEx,
  UnknownHashCodeEx, IOException, CaptureBadEcdeUrlEx,
  CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
  ValidationExceptionInvalidFile, UnknownFormatException, 
  UnexpectedDomainException, InvalidPagmsCombinaisonException, 
  CaptureExistingUuuidException {

    ecde = ecdeTestTools
        .buildEcdeTestDocument("attestation_consultation.pdf");

    final File repertoireEcde = ecde.getRepEcdeDocuments();
    final URI urlEcdeDocument = ecde.getUrlEcdeDocument();

    // copie le fichier attestation_consultation.pdf
    // dans le repertoire de l'ecde
    LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
        + repertoireEcde.getAbsoluteFile());
    fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
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
    metadatas.add(new UntypedMetadata("FormatFichier", "fmt/1354"));
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

    uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();

    Assert.fail("exception attendue");
  }

}
