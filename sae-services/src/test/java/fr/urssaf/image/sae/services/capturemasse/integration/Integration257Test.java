/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.utils.LogUtils;
import fr.urssaf.image.sae.utils.SaeLogAppender;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-test.xml",
      "/applicationContext-sae-services-integration-test.xml" })
public class Integration257Test {

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

   private static final String LOG_WARN = "Le type ou le format des "
         + "métadonnées suivantes n'est pas valide : "
         + "CodeSousCategorieV2, DateCreation, DateReception, NbPages";

   private Logger logger;

   private SaeLogAppender logAppender;

   @Before
   public void init() {
      logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      logAppender = new SaeLogAppender(Level.WARN, "fr.urssaf.image.sae");
      logger.addAppender(logAppender);

      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      logger.debug("initialisation du répertoire de traitetement :"
            + ecdeTestSommaire.getRepEcde());

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
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }

      EasyMock.reset(provider, storageDocumentService);

      AuthenticationContext.setAuthenticationToken(null);

      logger.detachAppender(logAppender);
   }

   @Test
   @DirtiesContext
   public void testLancement() throws ConnectionServiceEx, DeletionServiceEx,
         InsertionServiceEx, IOException {
      initComposants();
      initDatas();

      ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
            .getUrlEcde(), UUID.randomUUID());

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur", exitStatus
            .isSucces());

      checkFiles();

      checkLogs();

   }

   private void initComposants() throws ConnectionServiceEx, DeletionServiceEx,
         InsertionServiceEx {

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

      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
            .andReturn(storageDocument).anyTimes();

      EasyMock.replay(provider, storageDocumentService);
   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/257/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/257/documents/doc132.PDF");
      File fileAttestation1 = new File(repEcde, "doc132.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

      resAttestation1 = new ClassPathResource(
            "testhautniveau/257/documents/doc70.PDF");
      fileAttestation1 = new File(repEcde, "doc70.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

      resAttestation1 = new ClassPathResource(
            "testhautniveau/257/documents/doc77.PDF");
      fileAttestation1 = new File(repEcde, "doc77.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

   }

   private void checkFiles() throws IOException {

      File repTraitement = ecdeTestSommaire.getRepEcde();
      File debut = new File(repTraitement, "debut_traitement.flag");
      File fin = new File(repTraitement, "fin_traitement.flag");
      File resultats = new File(repTraitement, "resultats.xml");

      Assert.assertTrue("le fichier debut_traitement.flag doit exister", debut
            .exists());
      Assert.assertTrue("le fichier fin_traitement.flag doit exister", fin
            .exists());
      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
            .exists());

      String sha1Resultat = calculeSha1(resultats);
      String sha1Attendu = "ba7bcc31417bb7f93e1fe223914b0ada7eab9428";

      Assert.assertEquals(
            "le sha1 attendu et de résultat doivent etre identiques",
            sha1Attendu, sha1Resultat);

   }

   private String calculeSha1(File file) throws IOException {

      FileInputStream fis = new FileInputStream(file);
      try {

         return DigestUtils.shaHex(fis);

      } finally {
         if (fis != null) {
            fis.close();
         }
      }

   }

   private void checkLogs() {
      List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertNotNull("liste des messages non null", loggingEvents);

      Assert.assertEquals("un message attendu", 1, loggingEvents.size());

      ILoggingEvent event = loggingEvents.get(0);

      Assert.assertEquals("le log doit être de niveau WARN", Level.WARN, event
            .getLevel());

      boolean messageFound = LogUtils.logContainsMessage(event, LOG_WARN);
      Assert.assertTrue("le message d'erreur attendu doit être correct",
            messageFound);
   }

}
