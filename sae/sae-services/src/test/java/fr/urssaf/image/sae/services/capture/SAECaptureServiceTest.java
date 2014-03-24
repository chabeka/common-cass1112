package fr.urssaf.image.sae.services.capture;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.istack.ByteArrayDataSource;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.commons.utils.InputStreamSource;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestDocument;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
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
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SAECaptureServiceTest {
   @Autowired
   private EcdeTestTools ecdeTestTools;
   private static final Logger LOG = LoggerFactory
         .getLogger(SAECaptureServiceTest.class);

   @Autowired
   private SAECaptureService service;

   @Autowired
   private SAEServiceTestProvider testProvider;

   private UUID uuid;

   private static String path;

   private static File ecdeRepertory;

   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

   @Autowired
   private CassandraServerBean server;
   @Autowired
   private ParametersService parametersService;
   @Autowired
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   @BeforeClass
   public static void beforeClass() throws IOException {

      // mise en place du point dans le répertoire temporaire

      ecdeRepertory = new File(FilenameUtils.concat(SystemUtils
            .getJavaIoTmpDir().getAbsolutePath(), "ecde"));
      path = new ClassPathResource("doc/attestation_consultation.pdf")
            .getFile().getAbsolutePath();
      FileUtils.forceMkdir(ecdeRepertory);
      FileUtils.cleanDirectory(ecdeRepertory);

   }

   @AfterClass
   public static void afterClass() throws IOException {

      FileUtils.cleanDirectory(ecdeRepertory);

   }

   @SuppressWarnings("PMD.NullAssignment")
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
      String[] roles = new String[] { "archivage_unitaire" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
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

      AuthenticationContext.setAuthenticationToken(null);

      server.resetData();
   }

   @Test
   public void captureSuccess() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException {

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

      uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();
      LOG.debug("document archivé dans DFCE:" + uuid);
      Document doc = testProvider.searchDocument(uuid);

      Assert.assertNotNull("l'UUID '" + uuid + "' doit exister dans le SAE",
            doc);

      // test sur les métadonnées techniques
      assertDocument(doc, hash);

      // test sur les autres métadonnées
      List<Criterion> criterions = new ArrayList<Criterion>();
      criterions.addAll(doc.getAllCriterions());
      assertCriterions(criterions);

      // test sur le contenu du document
      Assert.assertTrue("le contenu n'est pas attendu", IOUtils.contentEquals(
            FileUtils.openInputStream(srcFile), testProvider
                  .loadDocumentFile(doc)));
   }

   /**
    * Cas de test : On tente un archivage unitaire en passant dans la métadonnée
    * FormatFichier un identifiant de format qui n'existe pas dans le
    * référentiel des formats.<br>
    * Résultat attendu : Exception de type UnknownFormatException
    * 
    */
   @Test
   public void captureErreurFormat() throws IOException {

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

      try {
         uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();
         Assert
               .fail("On attendait une exception UnknownFormatException alors que l'archivage a fonctionné");

      } catch (SAECaptureServiceEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (ReferentialRndException e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (UnknownCodeRndEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (RequiredStorageMetadataEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (InvalidValueTypeAndFormatMetadataEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (UnknownMetadataEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (DuplicatedMetadataEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (NotSpecifiableMetadataEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (EmptyDocumentEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (RequiredArchivableMetadataEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (NotArchivableMetadataEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (UnknownHashCodeEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (CaptureBadEcdeUrlEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (CaptureEcdeUrlFileNotFoundEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (MetadataValueNotInDictionaryEx e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (ValidationExceptionInvalidFile e) {
         Assert
               .fail("On attendait une exception UnknownFormatException alors qu'on a obtenu : "
                     + e.toString());

      } catch (UnknownFormatException e) {
         Assert
               .assertEquals(
                     "le message d'erreur attendu est incorrect",
                     "Le format du fichier n'existe pas dans le référentiel : fmt/1354",
                     e.getMessage());
      }

   }

   @Test(expected = CaptureEcdeUrlFileNotFoundEx.class)
   public void captureFailed() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException {

      EcdeTestDocument ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();
      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      service.capture(metadatas, urlEcdeDocument);
   }

   private static void assertDocument(Document doc, String expectedHash) {

      // TEST sur métadonnée : Titre
      Assert.assertEquals("la métadonnée 'Titre(sm_title)' est incorrecte",
            "Attestation de vigilance", doc.getTitle());

      // TEST sur métadonnée : DateCreation
      Assert.assertEquals(
            "la métadonnée 'DateCreation(sm_creation_date)' est incorrecte",
            "2012-01-01 00:00:00", DateFormatUtils.formatUTC(doc
                  .getCreationDate(), DATE_FORMAT));

      // TEST sur les métadonnées : DateModification & DateArchivage
      Assert.assertTrue("la métadonnée 'DateArchivage(sm_archivage_date)':"
            + doc.getArchivageDate()
            + " et 'DateModification(sm_modification)':"
            + doc.getModificationDate(), doc.getArchivageDate().equals(
            doc.getModificationDate()));

      // TEST sur métadonnée : DateDebutConservation
      Assert
            .assertEquals(
                  "la métadonnée 'DateDebutConservation(sm_life_cycle_reference_date)' est incorrecte",
                  "2011-09-02 00:00:00", DateFormatUtils.formatUTC(doc
                        .getLifeCycleReferenceDate(), DATE_FORMAT));

      // TEST sur métadonnée : TypeHash
      Assert.assertEquals(
            "la métadonnée 'TypeHash(sm_digest_algorithm)' est incorrecte",
            "SHA-1", doc.getDigestAlgorithm());

      // TEST sur métadonnée : Hash
      Assert.assertEquals("la métadonnée 'Hash(sm_digest)' est incorrecte",
            expectedHash, doc.getDigest());

      // TEST sur métadonnée : NomFichier
      Assert.assertEquals(
            "la métadonnée 'NomFichier(sm_filename)' est incorrecte",
            "attestation_consultation", doc.getFilename());

      Assert.assertEquals(
            "la métadonnée 'NomFichier(sm_extension)' est incorrecte", "pdf",
            doc.getExtension());

   }

   private static <T extends Criterion> void assertCriterions(List<T> criterions) {

      Assert.assertEquals("la nombre de métadonnées est inattendu", 11,
            criterions.size());

      // on trie les métadonnées non typés en fonction de leur code long
      Comparator<T> comparator = new Comparator<T>() {

         @Override
         public int compare(T criterion1, T criterion2) {

            return criterion1.getCategoryName().compareTo(
                  criterion2.getCategoryName());

         }
      };
      Collections.sort(criterions, comparator);

      // TEST sur métadonnée : CodeActivite
      assertMetadata(criterions.get(0), "act", "3");

      // TEST sur métadonnée : ApplicationProductrice
      assertMetadata(criterions.get(1), "apr", "ADELAIDE");

      // TEST sur métadonnée : CodeOrganismeGestionnaire
      assertMetadata(criterions.get(2), "cog", "UR750");

      // TEST sur métadonnée : CodeOrganismeProprietaire
      assertMetadata(criterions.get(3), "cop", "CER69");

      // TEST sur métadonnée : ContratDeService
      assertMetadata(criterions.get(4), "cse", "TESTS_UNITAIRES");

      // TEST sur métadonnée : DateFinConservation
      Assert.assertEquals(
            "le code de la metadonnée est inattendue dans cet ordre", "dfc",
            criterions.get(5).getCategoryName());
      Assert.assertEquals("la valeur de la metadonnée 'dfc'est inattendue",
            "2016-08-31 00:00:00", DateFormatUtils.format((Date) criterions
                  .get(5).getWord(), DATE_FORMAT));

      // TEST sur métadonnée : CodeFonction
      assertMetadata(criterions.get(6), "dom", "2");

      // TEST sur métadonnée : DateReception
      Assert.assertEquals(
            "le code de la metadonnée est inattendue dans cet ordre", "dre",
            criterions.get(7).getCategoryName());
      Assert.assertEquals("la valeur de la metadonnée 'dre'est inattendue",
            "1999-11-25 00:00:00", DateFormatUtils.format((Date) criterions
                  .get(7).getWord(), DATE_FORMAT));

      // TEST sur métadonnée : FormatFichier
      assertMetadata(criterions.get(8), "ffi", "fmt/354");

      // TEST sur métadonnée : NbPages
      assertMetadata(criterions.get(9), "nbp", 2);

      // TEST sur métadonnée : VersionRND par défaut
      assertMetadata(criterions.get(10), "vrn", "11.2");

   }

   private static void assertMetadata(Criterion criterion, String expectedCode,
         Object expectedValue) {

      Assert.assertEquals(
            "le code de la metadonnée est inattendue dans cet ordre",
            expectedCode, criterion.getCategoryName());

      Assert.assertEquals("la valeur de la metadonnée '"
            + criterion.getCategoryName() + "'est inattendue", expectedValue,
            criterion.getWord());

   }

   /**************************************************************************************/
   /**************************************************************************************/
   /***************************
    * CAPTURE UNITAIRE AVEC PJ
    * 
    * @throws IOException
    * @throws FileNotFoundException
    *            *
    **********************************/

   private List<UntypedMetadata> getListMetadata()
         throws FileNotFoundException, IOException {
      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      // liste des métadonnées obligatoires
      metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new UntypedMetadata("NbPages", "2"));
      metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));

      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      return metadatas;
   }

   @Test(expected = EmptyDocumentEx.class)
   public void captureBinaireContentNull() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile {

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");
      List<UntypedMetadata> metadatas = getListMetadata();
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      metadatas.add(new UntypedMetadata("Hash", hash));

      String fileName = "Test fichier contenu vide";

      service.captureBinaire(metadatas, null, fileName);

      fail("Le message d'erreur : Le contenu du fichier à archiver est vide.");
   }

   @Test(expected = EmptyFileNameEx.class)
   public void captureBinaireFileNameEmpty() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile {

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");
      List<UntypedMetadata> metadatas = getListMetadata();
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      metadatas.add(new UntypedMetadata("Hash", hash));

      String fileNameEmpty = null;
      byte[] content = new byte[16384];
      ByteArrayInputStream stream = new ByteArrayInputStream(content);
      InputStreamSource source = new InputStreamSource(stream);
      DataHandler dataHandler = new DataHandler(source);
      service.captureBinaire(metadatas, dataHandler, fileNameEmpty);

      fail("Le message d'erreur : Le nom du fichier est vide.");
   }

   @Test(expected = EmptyFileNameEx.class)
   public void captureBinaireFileNameSpace() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile {

      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");
      List<UntypedMetadata> metadatas = getListMetadata();
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      metadatas.add(new UntypedMetadata("Hash", hash));

      byte[] content = new byte[16384];
      ByteArrayInputStream stream = new ByteArrayInputStream(content);
      InputStreamSource source = new InputStreamSource(stream);
      DataHandler dataHandler = new DataHandler(source);
      String fileNameEmpty = "          ";
      service.captureBinaire(metadatas, dataHandler, fileNameEmpty);

      fail("Le message d'erreur : Le nom du fichier est vide.");
   }

   @Test
   @Ignore("Il manque un contrôle sur les extensions, communs à la capture unitaire et la capture de masse")
   public void captureBinaireFileNameNotExtension() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile {

      List<UntypedMetadata> metadatas = getListMetadata();

      String fileNameEmpty = "NomSansExtension";
      byte[] content = new byte[16384];
      ByteArrayInputStream stream = new ByteArrayInputStream(content);
      InputStreamSource source = new InputStreamSource(stream);
      DataHandler dataHandler = new DataHandler(source);
      String hash = DigestUtils.shaHex(content);
      metadatas.add(new UntypedMetadata("Hash", hash));
      service.captureBinaire(metadatas, dataHandler, fileNameEmpty);

   }

   @Test
   public void captureBinaireFileSuccess() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile {

      List<UntypedMetadata> metadatas = getListMetadata();

      String fileNameEmpty = "NomExtension.pdf";
      byte[] content = new byte[16];
      ByteArrayDataSource source = new ByteArrayDataSource(content,
            "application/octet-stream");
      DataHandler dataHandler = new DataHandler(source);
      String hash = DigestUtils.shaHex(content);
      metadatas.add(new UntypedMetadata("Hash", hash));
      service.captureBinaire(metadatas, dataHandler, fileNameEmpty);

   }

   @Test(expected = EmptyDocumentEx.class)
   public void captureBinaireContent0octet() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile {

      List<UntypedMetadata> metadatas = getListMetadata();

      String fileNameEmpty = "NomExtension.pdf";
      byte[] content = new byte[0];
      ByteArrayInputStream stream = new ByteArrayInputStream(content);
      InputStreamSource source = new InputStreamSource(stream);
      DataHandler dataHandler = new DataHandler(source);
      String hash = DigestUtils.shaHex(content);
      metadatas.add(new UntypedMetadata("Hash", hash));
      service.captureBinaire(metadatas, dataHandler, fileNameEmpty);

   }

   /**
    * Test permetant de vérifier que la capture fonctionne quand on lui passe un
    * emplacement de fichier
    * 
    * @throws UnknownFormatException
    * @throws ValidationExceptionInvalidFile
    */

   @Test
   public void captureFileSuccess() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, IOException, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException {

      File srcFile = new File(path);

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

      uuid = service.captureFichier(metadatas, path).getIdDoc();
      LOG.debug("document archivé dans DFCE:" + uuid);
      Document doc = testProvider.searchDocument(uuid);

      Assert.assertNotNull("l'UUID '" + uuid + "' doit exister dans le SAE",
            doc);

      // test sur les métadonnées techniques
      assertDocument(doc, hash);

      // test sur les autres métadonnées
      List<Criterion> criterions = new ArrayList<Criterion>();
      criterions.addAll(doc.getAllCriterions());
      assertCriterions(criterions);

      // test sur le contenu du document
      Assert.assertTrue("le contenu n'est pas attendu", IOUtils.contentEquals(
            FileUtils.openInputStream(srcFile), testProvider
                  .loadDocumentFile(doc)));
   }

   /**
    * Test permetant de tester la levée d'exception si le fichier est inexistant
    * 
    * @throws UnknownHashCodeEx
    * @throws RequiredArchivableMetadataEx
    * @throws EmptyDocumentEx
    * @throws NotArchivableMetadataEx
    * @throws NotSpecifiableMetadataEx
    * @throws DuplicatedMetadataEx
    * @throws UnknownMetadataEx
    * @throws InvalidValueTypeAndFormatMetadataEx
    * @throws RequiredStorageMetadataEx
    * @throws FileNotFoundException
    * @throws UnknownCodeRndEx
    * @throws ReferentialRndException
    * @throws SAECaptureServiceEx
    * @throws MetadataValueNotInDictionaryEx
    * @throws UnknownFormatException
    * @throws ValidationExceptionInvalidFile
    */
   @Test(expected = FileNotFoundException.class)
   public void fileNotFoundExceptionTest() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, FileNotFoundException,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         NotArchivableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException {
      String path = FileUtils.getTempDirectoryPath();
      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      uuid = service.captureFichier(metadatas,
            path.concat("FichierInexistant.pdf")).getIdDoc();
      LOG.debug("document archivé alors que le fichier est inexistant:" + uuid);
      Assert.fail();
   }

}
