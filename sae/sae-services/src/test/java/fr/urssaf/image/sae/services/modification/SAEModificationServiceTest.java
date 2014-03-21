package fr.urssaf.image.sae.services.modification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.service.ParametersService;
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
import fr.urssaf.image.sae.services.capture.SAECaptureService;
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
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEModificationServiceTest {

   @Autowired
   private SAEModificationService saeModificationService;

   @Autowired
   private SAESuppressionService saeSuppressionService;

   @Autowired
   private StorageServiceProvider provider;

   @Autowired
   private SAECaptureService insertService;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private SAEServiceTestProvider testProvider;

   @Autowired
   private CassandraServerBean server;
   @Autowired
   private ParametersService parametersService;
   @Autowired
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   private UUID uuid = null;

   @After
   public void end() throws Exception {
      if (uuid != null) {

         try {
            saeSuppressionService.suppression(uuid);

         } catch (SuppressionException exception) {
            exception.printStackTrace();
         }
      }

      AuthenticationContext.setAuthenticationToken(null);

      provider.closeConnexion();

      server.resetData();
   }

   @Before
   public void init() throws ConnectionServiceEx {

      provider.openConnexion();

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
      String[] roles = new String[] { "modification", "archivage_unitaire",
            "suppression" };
      saePrmds.add(saePrmd);

      saeDroits.put("modification", saePrmds);
      saeDroits.put("archivage_unitaire", saePrmds);
      saeDroits.put("suppression", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("5.1.2.1.5");
      typeDocCree.setCodeActivite("1");
      typeDocCree.setCodeFonction("5");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("Libellé 5.1.2.1.5");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());

      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.12");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("1");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("Libellé 2.3.1.1.12");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   @Test
   public void testUuidObligatoire() {
      try {
         saeModificationService.modification(null, null);
         Assert.fail("une IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit etre correct", exception
               .getMessage().contains("identifiant de l'archive"));

      } catch (Exception exception) {
         Assert.fail("une IllegalArgumentException est attendue");
      }
   }

   @Test
   public void testMetasObligatoire() {
      try {
         saeModificationService.modification(UUID.randomUUID(),
               new ArrayList<UntypedMetadata>());
         Assert.fail("une IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit etre correct", exception
               .getMessage().contains("les métadonnées"));

      } catch (Exception exception) {
         Assert.fail("une IllegalArgumentException est attendue");
      }
   }

   // Ne peut pas être réalisé en l'état car le test de l'existence de l'archive se fait désormais avant
   // la vérification des métas en double, il faudrait donc utiliser des mocks.
   @Ignore
   @Test(expected = DuplicatedMetadataEx.class)
   public void testMetasDupliquee() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx, ModificationException,
         ArchiveInexistanteEx, MetadataValueNotInDictionaryEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "NumeroCompteInterne", "123456"), new UntypedMetadata("Titre",
            "ceci est le titre 2"));

      saeModificationService.modification(UUID.randomUUID(), metadatas);
   }
   
   @Test(expected = ArchiveInexistanteEx.class)
   public void testArchiveInexistante() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx, ModificationException,
         ArchiveInexistanteEx, MetadataValueNotInDictionaryEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "NumeroCompteInterne", "123456"));

      saeModificationService.modification(UUID.randomUUID(), metadatas);
   }

   @Test
   public void modificationSucces() throws IOException, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, NotModifiableMetadataEx,
         ModificationException, ArchiveInexistanteEx, ValidationExceptionInvalidFile, UnknownFormatException {
      EcdeTestDocument ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      File repertoireEcde = ecde.getRepEcdeDocuments();
      URI urlEcdeDocument = ecde.getUrlEcdeDocument();

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
      metadatas.add(new UntypedMetadata("Hash", hash.toUpperCase()));
      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));
      metadatas.add(new UntypedMetadata("CodeTraitementV2", "RD75.L02"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2011-09-02"));

      uuid = insertService.capture(metadatas, urlEcdeDocument).getIdDoc();

      Assert.assertNotNull("le document doit être créé", uuid);

      List<UntypedMetadata> calledMetas = Arrays.asList(new UntypedMetadata(
            StorageTechnicalMetadatas.TITRE.getLongCode(), "Titre modifié"),
            new UntypedMetadata(StorageTechnicalMetadatas.TYPE.getLongCode(),
                  "5.1.2.1.5"), new UntypedMetadata("CodeTraitementV2", null));

      saeModificationService.modification(uuid, calledMetas);

      Document document = testProvider.searchDocument(uuid);

      Assert.assertEquals("le titre doit etre correct", "Titre modifié",
            document.getTitle());
      Assert.assertEquals("le code RND doit être correct", "5.1.2.1.5",
            document.getType());
      String codeFonction = (String) document.getSingleCriterion("dom")
            .getWord();
      Criterion codeActiviteV2 = document.getSingleCriterion("ctr");

      String codeActivite = (String) document.getSingleCriterion("act")
            .getWord();
      Assert.assertEquals("le code fonction doit etre correct", "5",
            codeFonction);
      Assert.assertEquals("le code activité doit etre correct", "1",
            codeActivite);
      Assert.assertNull("le criterion CodeActiviteV2 doit etre null",
            codeActiviteV2);

   }
}
