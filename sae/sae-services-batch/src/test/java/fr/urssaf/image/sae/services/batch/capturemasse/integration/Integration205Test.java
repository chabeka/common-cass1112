/**
 *
 */
package fr.urssaf.image.sae.services.batch.capturemasse.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.TraceAssertUtils;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.utils.SaeLogAppender;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                                   "/applicationContext-sae-services-batch-test.xml",
"/applicationContext-sae-services-capturemasse-test-integration.xml" })
public class Integration205Test {

   @Autowired
   private SAECaptureMasseService service;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider provider;

   private EcdeTestSommaire ecdeTestSommaire;

   private Logger logger;

   private SaeLogAppender logAppender;

   @Autowired
   private TraceAssertUtils traceAssertUtils;

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
      logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      logAppender = new SaeLogAppender(Level.WARN, "fr.urssaf.image.sae");
      logger.addAppender(logAppender);

      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      logger.debug("initialisation du répertoire de traitetement :"
            + ecdeTestSommaire.getRepEcde());

      // initialisation du contexte de sécurité
      final VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));

      final SaeDroits saeDroits = new SaeDroits();
      final List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      final SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      final Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      final String[] roles = new String[] { "archivage_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
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
   public void end() throws Exception {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (final IOException e) {
         // rien a faire
      }

      EasyMock.reset(provider, storageDocumentService);

      AuthenticationContext.setAuthenticationToken(null);

      logger.detachAppender(logAppender);
      server.resetData(true);
   }

   @Test
   @DirtiesContext
   public void testLancement() throws ConnectionServiceEx, DeletionServiceEx,
   InsertionServiceEx, IOException, InsertionIdGedExistantEx {
      initComposants();
      initDatas();

      final ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
                                                             .getUrlEcde(), UUID.randomUUID());

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertTrue("le traitement doit etre un succes", exitStatus
                        .isSucces());

      checkFiles();

      checkLogs();

      checkTracabilite();

   }

   private void initComposants() throws ConnectionServiceEx, DeletionServiceEx,
   InsertionServiceEx, InsertionIdGedExistantEx {

      // règlage provider
      provider.openConnexion();
      EasyMock.expectLastCall().anyTimes();
      provider.closeConnexion();
      EasyMock.expectLastCall().anyTimes();
      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
                                                                      storageDocumentService).anyTimes();

      // règlage storageDocumentService
      storageDocumentService.deleteStorageDocument(EasyMock
                                                   .anyObject(UUID.class));
      EasyMock.expectLastCall().anyTimes();

      final StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());

      EasyMock.expect(
                      storageDocumentService.insertStorageDocument(EasyMock
                                                                   .anyObject(StorageDocument.class)))
      .andReturn(storageDocument).anyTimes();

      EasyMock.replay(provider, storageDocumentService);
   }

   private void initDatas() throws IOException {
      final File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      final ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/205/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File origine = new File(ecdeTestSommaire.getRepEcde(), "documents");
      int i = 1;
      File dest, attestation;
      String resourceString = "testhautniveau/205/documents/";
      ClassPathResource resource;
      while (i < 11) {

         dest = new File(origine, String.valueOf(i));
         resourceString = resourceString + i + File.separator;
         resource = new ClassPathResource(resourceString + "doc" + i + ".PDF");
         attestation = new File(dest, "doc" + i + ".PDF");
         FileUtils.copyURLToFile(resource.getURL(), attestation);

         origine = dest;
         i++;
      }
   }

   private void checkFiles() throws IOException {

      final File repTraitement = ecdeTestSommaire.getRepEcde();
      final File debut = new File(repTraitement, "debut_traitement.flag");
      final File fin = new File(repTraitement, "fin_traitement.flag");
      final File resultats = new File(repTraitement, "resultats.xml");

      Assert.assertTrue("le fichier debut_traitement.flag doit exister", debut
                        .exists());
      Assert.assertTrue("le fichier fin_traitement.flag doit exister", fin
                        .exists());
      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
                        .exists());

      final String sha1Resultat = calculeSha1(resultats);
      final String sha1Attendu = "3eaec9be41810c7f431c43557b2f0aa388596f1c";

      Assert.assertEquals(
                          "le sha1 attendu et de résultat doivent etre identiques",
                          sha1Attendu, sha1Resultat);

   }

   private String calculeSha1(final File file) throws IOException {

      final FileInputStream fis = new FileInputStream(file);
      try {

         return DigestUtils.shaHex(fis);

      } finally {
         if (fis != null) {
            fis.close();
         }
      }

   }

   private void checkLogs() {
      final List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertTrue("aucun message d'erreur ou warn attendu",
                        loggingEvents == null || loggingEvents.isEmpty());

   }

   private void checkTracabilite() {

      traceAssertUtils.verifieAucuneTraceDansRegistres();

   }

}
