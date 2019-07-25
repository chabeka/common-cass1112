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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
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
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
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
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
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
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEModificationServiceTest {

   @Autowired
   private SAEModificationService saeModificationService;

   @Autowired
   private SAESuppressionService saeSuppressionService;

   @Autowired
   private SAECaptureService insertService;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   @Qualifier("SAEServiceTestProvider")
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

   private EcdeTestDocument ecde;

   @After
   public void end() throws Exception {
      if (uuid != null) {

         try {
            saeSuppressionService.suppression(uuid);

         } catch (final SuppressionException exception) {
            exception.printStackTrace();
         }
      }

      AuthenticationContext.setAuthenticationToken(null);

      server.resetData(true, MODE_API.HECTOR);

      if (ecde != null) {
         // supprime le repertoire ecde
         ecdeTestTools.cleanEcdeTestDocument(ecde);
         Thread.sleep(500);
      }
   }

   @Before
   public void init() throws Exception {

      server.resetData(true, MODE_API.HECTOR);

      final VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      final SaeDroits saeDroits = new SaeDroits();
      final List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      final SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      final Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      final String[] roles = new String[] { "ROLE_modification", "ROLE_archivage_unitaire",
      "ROLE_suppression" };
      saePrmds.add(saePrmd);

      saeDroits.put("modification", saePrmds);
      saeDroits.put("archivage_unitaire", saePrmds);
      saeDroits.put("suppression", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                   viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");

      final TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("5.1.2.1.5");
      typeDocCree.setCodeActivite("1");
      typeDocCree.setCodeFonction("5");
      typeDocCree.setDureeConservation(1095);
      typeDocCree.setLibelle("Libellé 5.1.2.1.5");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());

      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.12");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("1");
      typeDocCree.setDureeConservation(1095);
      typeDocCree.setLibelle("Libellé 2.3.1.1.12");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   @Test
   public void testUuidObligatoire() {
      try {
         saeModificationService.modification(null, null);
         Assert.fail("une IllegalArgumentException est attendue");

      } catch (final IllegalArgumentException exception) {
         Assert.assertTrue("le message doit etre correct", exception
                           .getMessage().contains("identifiant de l'archive"));

      } catch (final Exception exception) {
         Assert.fail("une IllegalArgumentException est attendue");
      }
   }

   @Test
   public void testMetasObligatoire() {
      try {
         saeModificationService.modification(UUID.randomUUID(),
                                             new ArrayList<UntypedMetadata>());
         Assert.fail("une IllegalArgumentException est attendue");

      } catch (final IllegalArgumentException exception) {
         Assert.assertTrue("le message doit etre correct", exception
                           .getMessage().contains("les métadonnées"));

      } catch (final Exception exception) {
         Assert.fail("une IllegalArgumentException est attendue");
      }
   }

   // Ne peut pas être réalisé en l'état car le test de l'existence de l'archive
   // se fait désormais avant
   // la vérification des métas en double, il faudrait donc utiliser des mocks.
   @Ignore
   @Test(expected = DuplicatedMetadataEx.class)
   public void testMetasDupliquee() throws ReferentialRndException,
   UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
   UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
   RequiredArchivableMetadataEx, NotArchivableMetadataEx,
   UnknownHashCodeEx, NotModifiableMetadataEx, ModificationException,
   ArchiveInexistanteEx, MetadataValueNotInDictionaryEx, ReferentialException, RetrievalServiceEx {
      final List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
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
   ArchiveInexistanteEx, MetadataValueNotInDictionaryEx, ReferentialException, RetrievalServiceEx {
      final List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
                                                                                "Titre", "ceci est le titre"), new UntypedMetadata(
                                                                                                                                   "NumeroCompteInterne", "123456"));

      saeModificationService.modification(UUID.randomUUID(), metadatas);
   }

   /**
    *
    * NB: A date du commit 16/09/2014, il y a un souci sur ce test, car :
    * <ul>
    * <li>
    * d'une part, côté DFCE, la date de fin de conservation n'est pas calculée
    * au moment du stockage. Il est donc techniquement possible d'archiver un
    * document dont la date de fin de conservation se situe dans le passé</li>
    * <li>
    * d'autre part, côté DFCE, lors de l'appel à l'API de changement de type de
    * document, il y a un calcul de la date de fin de conservation, et une
    * erreur est levée si cette date est dans le passé"java.lang.IllegalArgumentException: new final date has to be in the future, actual : Mon Sep 01 02:00:00 CEST 2014"
    * </li>
    * </ul>
    * Donc pour que ce TU passe, il faut pour le moment s'assurer que la mise à
    * jour du code RND donne une date de fin de conservation dans le futur.
    * @throws InvalidPagmsCombinaisonException
    * @throws UnexpectedDomainException
    * @throws CaptureExistingUuuidException
 * @throws RetrievalServiceEx 
 * @throws ReferentialException 
    *
    */
   @Test
   public void modificationSucces() throws IOException, SAECaptureServiceEx,
   ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
   InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
   DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
   RequiredArchivableMetadataEx, NotArchivableMetadataEx,
   UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
   MetadataValueNotInDictionaryEx, NotModifiableMetadataEx,
   ModificationException, ArchiveInexistanteEx,
   ValidationExceptionInvalidFile, UnknownFormatException,
   UnexpectedDomainException, InvalidPagmsCombinaisonException,
   CaptureExistingUuuidException, ReferentialException, RetrievalServiceEx {

      ecde = ecdeTestTools
            .buildEcdeTestDocument("attestation_consultation.pdf");

      final File repertoireEcde = ecde.getRepEcdeDocuments();
      final URI urlEcdeDocument = ecde.getUrlEcdeDocument();

      // copie le fichier attestation_consultation.pdf
      // dans le repertoire de l'ecde
      final File fileDoc = new File(repertoireEcde, "attestation_consultation.pdf");
      final ClassPathResource resDoc = new ClassPathResource(
            "doc/attestation_consultation.pdf");
      final FileOutputStream fos = new FileOutputStream(fileDoc);
      IOUtils.copy(resDoc.getInputStream(), fos);
      resDoc.getInputStream().close();
      fos.close();

      final File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      final List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();

      // liste des métadonnées obligatoires
      metadatas.add(new UntypedMetadata("ApplicationProductrice", "ADELAIDE"));
      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "CER69"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
      final String hash = DigestUtils.shaHex(new FileInputStream(srcFile));
      metadatas.add(new UntypedMetadata("Hash", hash.toUpperCase()));
      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("Titre", "Attestation de vigilance"));
      metadatas.add(new UntypedMetadata("CodeTraitementV2", "RD75.L02"));
      metadatas.add(new UntypedMetadata("NbPages", "10"));

      // liste des métadonnées non obligatoires
      metadatas.add(new UntypedMetadata("DateReception", "1999-11-25"));
      metadatas.add(new UntypedMetadata("DateDebutConservation", "2013-09-02"));

      uuid = insertService.capture(metadatas, urlEcdeDocument).getIdDoc();

      Assert.assertNotNull("le document doit être créé", uuid);

      // List<UntypedMetadata> calledMetas = Arrays.asList(new UntypedMetadata(
      // StorageTechnicalMetadatas.TITRE.getLongCode(), "Titre modifié"),
      // new UntypedMetadata(StorageTechnicalMetadatas.TYPE.getLongCode(),
      // "5.1.2.1.5"), new UntypedMetadata("CodeTraitementV2", null));
      final List<UntypedMetadata> calledMetas = Arrays.asList(new UntypedMetadata(
                                                                                  StorageTechnicalMetadatas.TITRE.getLongCode(), "Titre modifié"),
                                                              new UntypedMetadata(StorageTechnicalMetadatas.TYPE.getLongCode(),
                                                                    "5.1.2.1.5"), new UntypedMetadata("CodeTraitementV2", null),
                                                              new UntypedMetadata("DateReception", ""));

      saeModificationService.modification(uuid, calledMetas);

      final Document document = testProvider.searchDocument(uuid);

      Assert.assertEquals("le titre doit etre correct", "Titre modifié",
                          document.getTitle());
      Assert.assertEquals("le code RND doit être correct", "5.1.2.1.5",
                          document.getType());
      final String codeFonction = (String) document.getSingleCriterion("dom")
            .getWord();
      final Criterion codeActiviteV2 = document.getSingleCriterion("ctr");
      final Criterion dateReception = document.getSingleCriterion("dre");

      final String codeActivite = (String) document.getSingleCriterion("act")
            .getWord();
      Assert.assertEquals("le code fonction doit etre correct", "5",
                          codeFonction);
      Assert.assertEquals("le code activité doit etre correct", "1",
                          codeActivite);
      Assert.assertNull("le criterion CodeActiviteV2 doit etre null",
                        codeActiviteV2);
      Assert.assertNull("le criterion DateReception doit etre null",
                        dateReception);

      // supprime le fichier attestation_consultation.pdf sur le repertoire de l'ecde
      fileDoc.delete();
   }
}
