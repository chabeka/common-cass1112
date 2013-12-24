/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentNotFoundException;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class CaptureMasseControleSupportTest {

   @Autowired
   private CaptureMasseControleSupport support;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;
   
   @Autowired
   private CassandraServerBean server;
   @Autowired
   private ParametersService parametersService;
   @Autowired 
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
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
      String[] roles = new String[] { "archivage_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles, viExtrait
                  .getSaeDroits());
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
      typeDocCree.setLibelle("ATTESTATION DE VIGILANCE");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);
      
      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   @After
   public void end() throws Exception {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }

      AuthenticationContext.setAuthenticationToken(null);
      
      server.resetData();
   }

   @Test(expected = EmptyDocumentEx.class)
   public void testControleSAEDocumentFileEmpty() throws IOException,
         UnknownCodeRndEx, CaptureMasseSommaireDocumentNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repEcde = new File(repertoireEcdeTraitement, "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("docVide.pdf");
      File fileAttestation1 = new File(repEcde, "docVide.pdf");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("docVide.pdf");
      support.controleSAEDocument(document, repEcde.getParentFile());

      Assert.fail();
   }

   @Test(expected = UnknownMetadataEx.class)
   public void testControleSAEDocumentUnknownMetaData() throws IOException,
         UnknownCodeRndEx, CaptureMasseSommaireDocumentNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repEcde = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repEcde, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repDocuments = new File(repEcde, "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc1.PDF");
      document.setUMetadatas(getUntypedMetaData());
      document.getUMetadatas().add(
            new UntypedMetadata("CodeInconnu", "ValeurInconnue"));

      support.controleSAEDocument(document, repDocuments.getParentFile());

      Assert.fail();
   }

   @Test(expected = DuplicatedMetadataEx.class)
   public void testControleSAEDocumentDuplicateMetaData() throws IOException,
         UnknownCodeRndEx, CaptureMasseSommaireDocumentNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repEcde = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repEcde, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repEcde, "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc1.PDF");
      document.setUMetadatas(getUntypedMetaData());
      document.getUMetadatas().add(document.getUMetadatas().get(0));

      support.controleSAEDocument(document, repertoireEcdeDocuments
            .getParentFile());

      Assert.fail();
   }

   @Test(expected = RequiredArchivableMetadataEx.class)
   public void testControleSAEDocumentRequiredMetaData() throws IOException,
         UnknownCodeRndEx, CaptureMasseSommaireDocumentNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc1.PDF");
      document.setUMetadatas(getUntypedMetaData());
      document.getUMetadatas().remove(3);

      support.controleSAEDocument(document, repertoireEcdeDocuments
            .getParentFile());

      Assert.fail();
   }

   @Test(expected = CaptureMasseSommaireDocumentNotFoundException.class)
   public void testControleSAEDocumentDocumentNotFound() throws IOException,
         UnknownCodeRndEx, CaptureMasseSommaireDocumentNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("docVide.PDF");
      document.setUMetadatas(getUntypedMetaData());
      document.getUMetadatas().remove(3);

      support.controleSAEDocument(document, repertoireEcdeDocuments
            .getParentFile());

      Assert.fail();
   }

   @Test(expected = InvalidValueTypeAndFormatMetadataEx.class)
   public void testControleSAEDocumentTypeAndFormatMetaData()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc1.PDF");
      document.setUMetadatas(getUntypedMetaData());
      document.getUMetadatas().get(0).setValue("");

      support.controleSAEDocument(document, repertoireEcdeDocuments
            .getParentFile());

      Assert.fail();
   }

   @Test(expected = NotSpecifiableMetadataEx.class)
   public void testControleSAEDocumentNotSpecifiableMetadata()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc1.PDF");
      document.setUMetadatas(getUntypedMetaData());
      document.getUMetadatas().add(
            new UntypedMetadata("DureeConservation", "25"));

      support.controleSAEDocument(document, repertoireEcdeDocuments
            .getParentFile());

      Assert.fail();
   }

   @Test(expected = UnknownCodeRndEx.class)
   public void testControleSAEDocumentUnknownCodeRnd() throws IOException,
         UnknownCodeRndEx, CaptureMasseSommaireDocumentNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc1.PDF");
      document.setUMetadatas(getUntypedMetaData());
      document.getUMetadatas().get(3).setValue("0.1.1.12");

      support.controleSAEDocument(document, repertoireEcdeDocuments
            .getParentFile());

      Assert.fail();
   }

   @Test(expected = UnknownHashCodeEx.class)
   public void testControleSAEDocumentUnknownHashCode() throws IOException,
         UnknownCodeRndEx, CaptureMasseSommaireDocumentNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc1.PDF");
      document.setUMetadatas(getUntypedMetaData());
      document.getUMetadatas().get(4).setValue(
            "a2f93f1f121ebba0faef2c0596f2f126eacae76b");

      support.controleSAEDocument(document, repertoireEcdeDocuments
            .getParentFile());

      Assert.fail();
   }

   @Test
   public void testControleSAEDocumentSuccess()
         throws MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile {

      try {
         File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
         File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(fileSommaire);

         IOUtils.copy(resSommaire.getInputStream(), fos);
         File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
               "documents");
         ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
         File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
         fos = new FileOutputStream(fileAttestation1);
         IOUtils.copy(resAttestation1.getInputStream(), fos);

         UntypedDocument document = new UntypedDocument();
         document.setFilePath("doc1.PDF");
         document.setUMetadatas(getUntypedMetaData());

         support.controleSAEDocument(document, repertoireEcdeDocuments
               .getParentFile());

      } catch (FileNotFoundException e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (UnknownCodeRndEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (IOException e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (CaptureMasseSommaireDocumentNotFoundException e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (EmptyDocumentEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (UnknownMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (DuplicatedMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (InvalidValueTypeAndFormatMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (NotSpecifiableMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (RequiredArchivableMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (UnknownHashCodeEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      }

   }

   /**
    * @return
    */
   private List<UntypedMetadata> getUntypedMetaData() {
      List<UntypedMetadata> list = new ArrayList<UntypedMetadata>();

      list.add(new UntypedMetadata("SiteAcquisition", "CER69"));
      list.add(new UntypedMetadata("Titre",
            "NOTIFICATIONS DE REMBOURSEMENT du 41882050200023"));
      list.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      list.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      list.add(new UntypedMetadata("Hash",
            "a2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      list.add(new UntypedMetadata("TypeHash", "SHA-1"));
      list.add(new UntypedMetadata("TracabilitePreArchivage", "P"));
      list.add(new UntypedMetadata("ApplicationProductrice", "GED"));
      list.add(new UntypedMetadata("FormatFichier", "fmt/18"));
      list.add(new UntypedMetadata("NbPages", "2"));
      list.add(new UntypedMetadata("CodeOrganismeProprietaire", "UR030"));
      list.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR030"));

      return list;
   }

   /*
    * =====================================================================
    * tests documents virtuels
    * =====================================================================
    */

   @Test(expected = UnknownMetadataEx.class)
   public void testControleVirtualSAEDocumentUnknownMetaData()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx {

      File repEcde = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repEcde, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repDocuments = new File(repEcde, "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedVirtualDocument document = new UntypedVirtualDocument();
      document.setReference(new VirtualReferenceFile());
      document.setuMetadatas(getUntypedMetaData());
      document.getuMetadatas().add(
            new UntypedMetadata("CodeInconnu", "ValeurInconnue"));

      support.controleSAEMetadatas(document);

      Assert.fail();
   }

   @Test(expected = DuplicatedMetadataEx.class)
   public void testControleSAEVirtualDocumentDuplicateMetaData()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx {

      File repEcde = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repEcde, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repEcde, "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedVirtualDocument document = new UntypedVirtualDocument();
      VirtualReferenceFile reference = new VirtualReferenceFile();
      reference.setHash("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      document.setReference(reference);
      document.setuMetadatas(getUntypedMetaData());
      document.getuMetadatas().add(document.getuMetadatas().get(0));

      support.controleSAEMetadatas(document);

      Assert.fail();
   }

   @Test(expected = RequiredArchivableMetadataEx.class)
   public void testControleSAEVirtualDocumentRequiredMetaData()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedVirtualDocument document = new UntypedVirtualDocument();
      VirtualReferenceFile reference = new VirtualReferenceFile();
      reference.setHash("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      document.setReference(reference);
      document.setuMetadatas(getUntypedMetaData());
      document.getuMetadatas().remove(3);

      support.controleSAEMetadatas(document);

      Assert.fail();
   }

   @Test(expected = InvalidValueTypeAndFormatMetadataEx.class)
   public void testControleSAEVirtualDocumentTypeAndFormatMetaData()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedVirtualDocument document = new UntypedVirtualDocument();
      VirtualReferenceFile reference = new VirtualReferenceFile();
      reference.setHash("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      document.setReference(reference);
      document.setuMetadatas(getUntypedMetaData());
      document.getuMetadatas().get(0).setValue("");

      support.controleSAEMetadatas(document);

      Assert.fail();
   }

   @Test(expected = NotSpecifiableMetadataEx.class)
   public void testControleSAEVirtualDocumentNotSpecifiableMetadata()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedVirtualDocument document = new UntypedVirtualDocument();
      VirtualReferenceFile reference = new VirtualReferenceFile();
      reference.setHash("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      document.setReference(reference);
      document.setuMetadatas(getUntypedMetaData());
      document.getuMetadatas().add(
            new UntypedMetadata("DureeConservation", "25"));

      support.controleSAEMetadatas(document);

      Assert.fail();
   }

   @Test(expected = UnknownCodeRndEx.class)
   public void testControleSAEVirtualDocumentUnknownCodeRnd()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedVirtualDocument document = new UntypedVirtualDocument();
      VirtualReferenceFile reference = new VirtualReferenceFile();
      reference.setHash("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      document.setReference(reference);
      document.setuMetadatas(getUntypedMetaData());
      document.getuMetadatas().get(3).setValue("0.1.1.12");

      support.controleSAEMetadatas(document);

      Assert.fail();
   }

   @Test(expected = UnknownHashCodeEx.class)
   public void testControleSAEVirtualDocumentUnknownHashCode()
         throws IOException, UnknownCodeRndEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         MetadataValueNotInDictionaryEx {

      File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
      File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(fileSommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      UntypedVirtualDocument document = new UntypedVirtualDocument();
      VirtualReferenceFile reference = new VirtualReferenceFile();
      reference.setHash("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      document.setReference(reference);
      document.setuMetadatas(getUntypedMetaData());
      document.getuMetadatas().get(4).setValue(
            "a2f93f1f121ebba0faef2c0596f2f126eacae76b");

      support.controleSAEMetadatas(document);

      Assert.fail();
   }

   @Test
   public void testControleSAVirtualEDocumentSuccess()
         throws MetadataValueNotInDictionaryEx {

      try {
         File repertoireEcdeTraitement = ecdeTestSommaire.getRepEcde();
         File fileSommaire = new File(repertoireEcdeTraitement, "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(fileSommaire);

         IOUtils.copy(resSommaire.getInputStream(), fos);
         File repertoireEcdeDocuments = new File(repertoireEcdeTraitement,
               "documents");
         ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
         File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
         fos = new FileOutputStream(fileAttestation1);
         IOUtils.copy(resAttestation1.getInputStream(), fos);

         UntypedVirtualDocument document = new UntypedVirtualDocument();
         VirtualReferenceFile reference = new VirtualReferenceFile();
         reference.setHash("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
         document.setReference(reference);
         document.setuMetadatas(getUntypedMetaData());

         support.controleSAEMetadatas(document);

      } catch (FileNotFoundException e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (UnknownCodeRndEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (IOException e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (UnknownMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (DuplicatedMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (InvalidValueTypeAndFormatMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (NotSpecifiableMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (RequiredArchivableMetadataEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      } catch (UnknownHashCodeEx e) {
         Assert.fail("traitement complet attendu sans erreur");
      }
   }
}
