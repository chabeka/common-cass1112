package fr.urssaf.image.sae.services.search;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestDocument;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
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
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.services.exception.search.DoublonFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAESearchServiceImplDatasTest {

   @Autowired
   private SAECaptureService service;

   @Autowired
   private SAESuppressionService serviceSuppression;

   @Autowired
   @Qualifier("SAEServiceTestProvider")
   private SAEServiceTestProvider testProvider;

   @Autowired
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;
   @Autowired
   private SaeBddSupport saeBddSupport;

   private UUID uuid;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   @Qualifier("saeSearchService")
   private SAESearchService saeSearchService;

   @Autowired
   private CassandraServerBean bean;

   private EcdeTestDocument ecde;

   @Before
   public void createCaptureContexte() throws Exception {

      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("1.2.1.1.1");
      typeDocCree.setCodeActivite("2");
      typeDocCree.setCodeFonction("1");
      typeDocCree.setDureeConservation(300);
      typeDocCree.setLibelle("Libellé 1.2.1.1.1");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      VersionRnd version = new VersionRnd();
      version.setDateMiseAJour(new Date());
      version.setVersionEnCours("11.4");
      saeBddSupport.updateVersionRnd(version);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      prmd.setLucene("Siret:12345678901234");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "recherche_iterateur",
            "archivage_unitaire", "recherche", "suppression" };
      saePrmds.add(saePrmd);

      saeDroits.put("recherche_iterateur", saePrmds);
      saeDroits.put("archivage_unitaire", saePrmds);
      saeDroits.put("recherche", saePrmds);
      saeDroits.put("suppression", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }

   @After
   public void end() throws IOException {
      try {
         bean.resetData();
      } catch (Exception e) {
         e.printStackTrace();
      }

      AuthenticationContext.setAuthenticationToken(null);

      if (ecde != null) {
         ecdeTestTools.cleanEcdeTestDocument(ecde);
      }
   }

   @Test
   public final void searchSuccessWithoutResult() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, IOException {

      List<UntypedDocument> documents = saeSearchService.search(
            "CodeOrganismeGestionnaire:CER75", new ArrayList<String>());

      Assert.assertTrue("pas d'UntypedDocuments attendus", documents.isEmpty());
   }

   @Test
   public final void searchSuccessResult() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, IOException,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, SuppressionException, ArchiveInexistanteEx,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         CaptureExistingUuuidException {

      // insertion d'un document
      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
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

      String titreUnique = "Titre " + UUID.randomUUID().toString();

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
      metadatas.add(new UntypedMetadata("CodeRND", "1.2.1.1.1"));
      metadatas.add(new UntypedMetadata("Titre", titreUnique));
      metadatas.add(new UntypedMetadata("Siret", "12345678901234"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();
      Document doc = testProvider.searchDocument(uuid);

      Assert.assertNotNull("l'UUID '" + uuid + "' doit exister dans le SAE",
            doc);

      List<UntypedDocument> documents = saeSearchService
            .search("CodeOrganismeGestionnaire:UR750 AND Titre:\""
                  + titreUnique + "\"", new ArrayList<String>());

      Assert.assertEquals("1 document attendu", 1, documents.size());

      serviceSuppression.suppression(uuid);

      // supprime le fichier attestation_consultation.pdf sur le repertoire de
      // l'ecde
      fileDoc.delete();
   }

   /**
    * Cas de test: Appel du service de recherche avec une requête Lucene<br>
    * contenant uniquement ll'identifiantged d'un document<br>
    * Résultat attendu: La recherche ramène un unique document
    */
   @Test
   public final void searchByIdGedSuccess() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, IOException,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, SuppressionException, ArchiveInexistanteEx,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         CaptureExistingUuuidException {

      // insertion d'un document
      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
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

      String titreUnique = "Titre " + UUID.randomUUID().toString();

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
      metadatas.add(new UntypedMetadata("CodeRND", "1.2.1.1.1"));
      metadatas.add(new UntypedMetadata("Titre", titreUnique));
      metadatas.add(new UntypedMetadata("Siret", "12345678901234"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();
      Document doc = testProvider.searchDocument(uuid);

      Assert.assertNotNull("l'UUID '" + uuid + "' doit exister dans le SAE",
            doc);

      List<UntypedDocument> documents = saeSearchService.search(
            "IdGed:" + uuid, new ArrayList<String>());

      Assert.assertEquals("1 document attendu", 1, documents.size());

      serviceSuppression.suppression(uuid);

      // supprime le fichier attestation_consultation.pdf sur le repertoire de
      // l'ecde
      fileDoc.delete();
   }

   @Test
   public final void searchSuccessResultWithMetaRequestedTwice()
         throws SAESearchServiceEx, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         IOException, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException,
         SuppressionException, ArchiveInexistanteEx, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      // insertion d'un document
      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
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

      String titreUnique = "Titre " + UUID.randomUUID().toString();

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
      metadatas.add(new UntypedMetadata("CodeRND", "1.2.1.1.1"));
      metadatas.add(new UntypedMetadata("Titre", titreUnique));
      metadatas.add(new UntypedMetadata("Siret", "12345678901234"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();
      Document doc = testProvider.searchDocument(uuid);

      Assert.assertNotNull("l'UUID '" + uuid + "' doit exister dans le SAE",
            doc);

      List<String> desiredMetadatas = new ArrayList<String>();
      desiredMetadatas.add("Titre");
      desiredMetadatas.add("Siret");
      desiredMetadatas.add("CodeRND");
      desiredMetadatas.add("Titre");
      desiredMetadatas.add("Siret");
      desiredMetadatas.add("CodeRND");
      desiredMetadatas.add("Titre");
      desiredMetadatas.add("Siret");
      desiredMetadatas.add("CodeRND");

      // Test sur la recherche classique
      List<UntypedDocument> documents = saeSearchService
            .search("CodeOrganismeGestionnaire:UR750 AND Titre:\""
                  + titreUnique + "\"", desiredMetadatas);

      Assert.assertEquals("1 document attendu", 1, documents.size());

      // Test sur la recherche paginée
      List<UntypedMetadata> fixedMetadatas = new ArrayList<UntypedMetadata>();
      UntypedMetadata uMeta = new UntypedMetadata("Titre", titreUnique);
      fixedMetadatas.add(uMeta);

      Calendar aujourdhui = Calendar.getInstance();
      aujourdhui.add(Calendar.DATE, -1);
      String dateMin = Integer.toString(aujourdhui.get(Calendar.YEAR))
            + String.format("%02d", aujourdhui.get(Calendar.MONTH) + 1)
            + String.format("%02d", aujourdhui.get(Calendar.DATE)) + "000000000";
      aujourdhui.add(Calendar.DATE, 2);
      String dateMax = Integer.toString(aujourdhui.get(Calendar.YEAR))
            + String.format("%02d", aujourdhui.get(Calendar.MONTH) + 1)
            + String.format("%02d", aujourdhui.get(Calendar.DATE)) + "000000000";
      UntypedRangeMetadata varyingMetadata = new UntypedRangeMetadata(
            "DateArchivage", dateMin, dateMax);

      int nbDocumentsParPage = 10;
      UUID lastIdDoc = null;

      PaginatedUntypedDocuments documents2 = saeSearchService.searchPaginated(
            fixedMetadatas, varyingMetadata, new ArrayList<AbstractMetadata>(),
            new ArrayList<AbstractMetadata>(), nbDocumentsParPage, lastIdDoc,
            desiredMetadatas);

      Assert.assertEquals("1 document attendu", 1, documents2.getDocuments()
            .size());

      serviceSuppression.suppression(uuid);

      // supprime le fichier attestation_consultation.pdf sur le repertoire de
      // l'ecde
      fileDoc.delete();
   }

   /**
    * Cas de test: Appel du service de recherche avec une requête Lucene<br>
    * dont la valeur commence par une étoile<br>
    * Résultat attendu: La recherche se passe bien mais ne ramène aucun document
    */
   @Test
   @Ignore("Ce test provoque une erreur de timeout, mais ce n'est pas systématique.")
   public final void searchSuccessWithoutResultReqEtoile()
         throws SAESearchServiceEx, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx {

      String requete = "Siret:*987654321";

      List<String> listMetaDesiree = Arrays.asList("Titre");

      List<UntypedDocument> documents = saeSearchService.search(requete,
            listMetaDesiree);

      Assert.assertTrue("pas d'UntypedDocuments attendus", documents.isEmpty());
   }

   /**
    * Test de la recherche par itérateur
    * 
    * @throws DoublonFiltresMetadataEx
    * 
    */
   @Test
   public final void rechercheParIterateurSucces() throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, IOException, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, SuppressionException, ArchiveInexistanteEx,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         CaptureExistingUuuidException, DoublonFiltresMetadataEx {

      // insertion d'un document
      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
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

      String titreUnique = "Titre " + UUID.randomUUID().toString();

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
      metadatas.add(new UntypedMetadata("CodeRND", "1.2.1.1.1"));
      metadatas.add(new UntypedMetadata("Titre", titreUnique));
      metadatas.add(new UntypedMetadata("Siret", "12345678901234"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();
      Document doc = testProvider.searchDocument(uuid);

      Assert.assertNotNull("l'UUID '" + uuid + "' doit exister dans le SAE",
            doc);

      List<UntypedMetadata> fixedMetadatas = new ArrayList<UntypedMetadata>();
      UntypedMetadata uMeta = new UntypedMetadata("Siret", "12345678901234");
      fixedMetadatas.add(uMeta);
      UntypedRangeMetadata varyingMetadata = new UntypedRangeMetadata(
            "DateCreation", "20120101", "20120101");
      List<AbstractMetadata> filters = new ArrayList<AbstractMetadata>();
      UntypedMetadata metaFiltre = new UntypedMetadata("Titre", titreUnique);
      filters.add(metaFiltre);

      int nbDocumentsParPage = 10;
      UUID lastIdDoc = null;
      List<String> listeDesiredMetadata = new ArrayList<String>();

      PaginatedUntypedDocuments documents = saeSearchService.searchPaginated(
            fixedMetadatas, varyingMetadata, filters,
            new ArrayList<AbstractMetadata>(), nbDocumentsParPage, lastIdDoc,
            listeDesiredMetadata);

      Assert.assertEquals("1 document attendu", 1, documents.getDocuments()
            .size());

      Assert.assertEquals("UUID attendu incorect", uuid, documents
            .getDocuments().get(0).getUuid());

      serviceSuppression.suppression(uuid);

      // supprime le fichier attestation_consultation.pdf sur le repertoire de
      // l'ecde
      fileDoc.delete();
   }

   /**
    * Test de la recherche par itérateur
    * 
    * @throws DoublonFiltresMetadataEx
    * 
    */
   @Test
   public final void rechercheParIterateurAvecNoteSucces()
         throws SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException, IOException,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownLuceneMetadataEx, SAESearchServiceEx, SyntaxLuceneEx,
         UnknownDesiredMetadataEx, UnknownFiltresMetadataEx,
         SuppressionException, ArchiveInexistanteEx, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException,
         DoublonFiltresMetadataEx {

      // insertion d'un document
      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
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

      String titreUnique = "Titre " + UUID.randomUUID().toString();

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
      metadatas.add(new UntypedMetadata("CodeRND", "1.2.1.1.1"));
      metadatas.add(new UntypedMetadata("Titre", titreUnique));
      metadatas.add(new UntypedMetadata("Siret", "12345678901234"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));
      metadatas.add(new UntypedMetadata("Note", "contenu de la note"));

      uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();
      Document doc = testProvider.searchDocument(uuid);

      Assert.assertNotNull("l'UUID '" + uuid + "' doit exister dans le SAE",
            doc);

      List<UntypedMetadata> fixedMetadatas = new ArrayList<UntypedMetadata>();
      UntypedMetadata uMeta = new UntypedMetadata("Siret", "12345678901234");
      fixedMetadatas.add(uMeta);
      UntypedRangeMetadata varyingMetadata = new UntypedRangeMetadata(
            "DateCreation", "20120101", "20120101");
      List<AbstractMetadata> filters = new ArrayList<AbstractMetadata>();
      UntypedMetadata metaFiltre = new UntypedMetadata("Titre", titreUnique);
      filters.add(metaFiltre);

      int nbDocumentsParPage = 10;
      UUID lastIdDoc = null;
      List<String> listeDesiredMetadata = new ArrayList<String>();
      listeDesiredMetadata.add("Note");

      PaginatedUntypedDocuments documents = saeSearchService.searchPaginated(
            fixedMetadatas, varyingMetadata, filters,
            new ArrayList<AbstractMetadata>(), nbDocumentsParPage, lastIdDoc,
            listeDesiredMetadata);

      Assert.assertEquals("1 document attendu", 1, documents.getDocuments()
            .size());

      Assert.assertEquals("UUID attendu incorect", uuid, documents
            .getDocuments().get(0).getUuid());

      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date dateCourante = new Date();
      String dateString = dateFormat.format(dateCourante);

      String contenu = documents.getDocuments().get(0).getUMetadatas().get(0)
            .getValue();
      String[] splitContenu = contenu.split(dateString);

      assertEquals(
            "Contenu de la note invalide",
            "[{\"contenu\":\"contenu de la note\",\"dateCreation\":\"\",\"auteur\":\"_ADMIN\"}]",
            splitContenu[0] + splitContenu[1].substring(9));

      serviceSuppression.suppression(uuid);

      // supprime le fichier attestation_consultation.pdf sur le repertoire de
      // l'ecde
      fileDoc.delete();
   }

   /**
    * Test de la recherche par itérateur
    * 
    * @throws DoublonFiltresMetadataEx
    * 
    */
   @Test
   public final void rechercheParIterateurErreurFiltreDoublon()
         throws SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException, IOException,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownLuceneMetadataEx, SAESearchServiceEx, SyntaxLuceneEx,
         UnknownDesiredMetadataEx, UnknownFiltresMetadataEx,
         SuppressionException, ArchiveInexistanteEx, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException,
         DoublonFiltresMetadataEx {

      List<UntypedMetadata> fixedMetadatas = new ArrayList<UntypedMetadata>();
      UntypedMetadata uMeta = new UntypedMetadata("Siret", "12345678901234");
      fixedMetadatas.add(uMeta);
      UntypedRangeMetadata varyingMetadata = new UntypedRangeMetadata(
            "DateCreation", "20120101", "20120101");
      List<AbstractMetadata> filters = new ArrayList<AbstractMetadata>();
      UntypedMetadata metaFiltre = new UntypedMetadata("Titre",
            "titre_a_chercher");

      // Ajout du filtre en double
      filters.add(metaFiltre);
      filters.add(metaFiltre);

      int nbDocumentsParPage = 10;
      UUID lastIdDoc = null;
      List<String> listeDesiredMetadata = new ArrayList<String>();

      try {
         PaginatedUntypedDocuments documents = saeSearchService
               .searchPaginated(fixedMetadatas, varyingMetadata, filters,
                     new ArrayList<AbstractMetadata>(), nbDocumentsParPage,
                     lastIdDoc, listeDesiredMetadata);
         Assert
               .fail("L'exception de type DoublonFiltresMetadataEx est attendue");
      } catch (DoublonFiltresMetadataEx e) {
         Assert
               .assertEquals(
                     "Le message attendu est incorrect",
                     "La ou les métadonnées suivantes, utilisées dans le filtre de la requête de recherche par itérateur, sont en doublon : Titre",
                     e.getMessage());
      }

   }

   @Test
   public final void searchWithNoteSuccessResult() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, IOException,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, SuppressionException, ArchiveInexistanteEx,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         CaptureExistingUuuidException {

      // insertion d'un document
      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
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

      String titreUnique = "Titre " + UUID.randomUUID().toString();

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
      metadatas.add(new UntypedMetadata("CodeRND", "1.2.1.1.1"));
      metadatas.add(new UntypedMetadata("Titre", titreUnique));
      metadatas.add(new UntypedMetadata("Siret", "12345678901234"));
      metadatas.add(new UntypedMetadata("Note", "contenu de la note"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = service.capture(metadatas, urlEcdeDocument).getIdDoc();

      String requete = "Titre:\"" + titreUnique + "\"";
      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add("Note");
      List<UntypedDocument> listeDocs = saeSearchService.search(requete,
            listMetaDesired);

      Assert.assertEquals("La recherche doit remonter un seul résultat", 1,
            listeDocs.size());
      Assert.assertEquals("Un seule métadonnée attendue : Note", listeDocs.get(
            0).getUMetadatas().size(), 1);

      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date dateCourante = new Date();
      String dateString = dateFormat.format(dateCourante);

      String contenu = listeDocs.get(0).getUMetadatas().get(0).getValue();
      String[] splitContenu = contenu.split(dateString);

      assertEquals(
            "Contenu de la note invalide",
            "[{\"contenu\":\"contenu de la note\",\"dateCreation\":\"\",\"auteur\":\"_ADMIN\"}]",
            splitContenu[0] + splitContenu[1].substring(9));

      serviceSuppression.suppression(uuid);

      // supprime le fichier attestation_consultation.pdf sur le repertoire de
      // l'ecde
      fileDoc.delete();
   }

}