package fr.urssaf.image.sae.services.capture;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestDocument;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
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
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SAECaptureServiceDroitsTest {
   @Autowired
   private EcdeTestTools ecdeTestTools;
   private static final Logger LOG = LoggerFactory
         .getLogger(SAECaptureServiceDroitsTest.class);

   @Autowired
   private SAECaptureService service;

   @Autowired
   private SAEServiceTestProvider testProvider;

   private UUID uuid;

   private static File ecdeRepertory;

   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

   @BeforeClass
   public static void beforeClass() throws IOException {

      // mise en place du point dans le répertoire temporaire

      ecdeRepertory = new File(FilenameUtils.concat(SystemUtils
            .getJavaIoTmpDir().getAbsolutePath(), "ecde"));

      FileUtils.forceMkdir(ecdeRepertory);
      FileUtils.cleanDirectory(ecdeRepertory);
   }

   @AfterClass
   public static void afterClass() throws IOException {

      FileUtils.cleanDirectory(ecdeRepertory);
   }

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
      Map<String, List<String>> metadata = new HashMap<String, List<String>>();
      metadata.put("CodeOrganismeGestionnaire", Arrays
            .asList(new String[] { "UR690" }));
      prmd.setMetadata(metadata);
      prmd.setCode("CER75");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_unitaire" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles, viExtrait
                  .getSaeDroits());
      AuthenticationContext.setAuthenticationToken(token);
   }

   @After
   public void after() throws ConnectionServiceEx, DeletionServiceEx {
      // suppression de l'insertion
      if (uuid != null) {
         testProvider.deleteDocument(uuid);
      }

      AuthenticationContext.setAuthenticationToken(null);
   }

   @Test(expected=AccessDeniedException.class)
   public void captureAccessDenied() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx {

      EcdeTestDocument ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
            + repertoireEcde.getAbsoluteFile());
      File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
      ClassPathResource resDoc = new ClassPathResource(
            "doc/attestation_consultation.pdf");
      FileOutputStream fos = new FileOutputStream(fileDoc);
      IOUtils.copy(resDoc.getInputStream(), fos);

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();

      // liste des métadonnées obligatoires
      metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/1354"));
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

      uuid = service.capture(metadatas, urlEcdeDocument);
      
      Assert.fail("exception attendue");
   }

}
