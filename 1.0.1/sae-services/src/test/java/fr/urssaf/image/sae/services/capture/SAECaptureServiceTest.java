package fr.urssaf.image.sae.services.capture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestDocument;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
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
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions", "PMD.ExcessiveImports" })
public class SAECaptureServiceTest {
   @Autowired
   private EcdeTestTools ecdeTestTools;
   private static final Logger LOG = Logger
         .getLogger(SAECaptureServiceTest.class);

   @Autowired
   private SAECaptureService service;

   @Autowired
   private SAEServiceTestProvider testProvider;

   private UUID uuid;

   private static File ecdeRepertory;

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
   }

   @After
   public void after() throws ConnectionServiceEx, DeletionServiceEx {
      // suppression de l'insertion
      // FIXME Attente de la nouvelle version de DFCE
      if (uuid != null) {
         // testProvider.deleteDocument(uuid);
      }
   }

   @Test
   public void capture_success() throws ConnectionServiceEx,
         SAECaptureServiceEx, IOException, SAEEnrichmentEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         MappingFromReferentialException, InvalidSAETypeException,
         UnknownHashCodeEx, NotArchivableMetadataEx, ReferentialRndException,
         UnknownCodeRndEx, SearchingServiceEx {

      EcdeTestDocument ecde = ecdeTestTools.buildEcdeTestDocument("attestation_consultation.pdf");
      
      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument  = ecde.getUrlEcdeDocument ();
      

      LOG.debug("CAPTURE UNITAIRE ECDE TEMP: "
            + repertoireEcde.getAbsoluteFile());
      File fileDoc = new File(repertoireEcde,
            "attestation_consultation.pdf");
      ClassPathResource resDoc = new ClassPathResource(
            "attestation_consultation.pdf");
      FileOutputStream fos = new FileOutputStream(fileDoc);
      IOUtils.copy(resDoc.getInputStream(), fos);

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();

      metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));

      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("VersionRND", "11.1"));

      metadatas.add(new UntypedMetadata("NbPages", "2"));
      //metadatas.add(new UntypedMetadata("NomFichier", "attestation.pdf"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/1354"));
      metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));
      metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));

      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      metadatas.add(new UntypedMetadata("Hash", hash));

      uuid = service.capture(metadatas, urlEcdeDocument);
      LOG.debug("document archivé dans DFCE:" + uuid);
      StorageDocument doc = testProvider.searchDocument(uuid);

      Assert.assertNotNull("l'UUID '" + uuid + "' doit exister dans le SAE",
            doc);
   }
}
