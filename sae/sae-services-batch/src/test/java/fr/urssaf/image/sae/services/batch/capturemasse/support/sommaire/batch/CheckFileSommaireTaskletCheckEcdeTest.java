/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.batch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.utils.LogUtils;
import fr.urssaf.image.sae.utils.SaeLogAppender;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
      "/applicationContext-sae-services-capturemasse-test.xml",
      "/applicationContext-sae-services-capturemasse-test-mock-CaptureSupportService.xml" })
public class CheckFileSommaireTaskletCheckEcdeTest {

   @Autowired
   private SAECaptureMasseService service;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private SAEControleSupportService controleService;

   private EcdeTestSommaire ecdeTestSommaire;

   private Logger logger;

   private SaeLogAppender logAppender;

   @Before
   public void init() {

      logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      logAppender = new SaeLogAppender(Level.ALL, "fr.urssaf.image.sae");
      logger.addAppender(logAppender);

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
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }

      logger.detachAppender(logAppender);

      EasyMock.reset(controleService);
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testUriDomaineConnuFichierExistantEcritureImpossible()
         throws Exception {

      initDatas();

      controleService.checkEcdeWrite(EasyMock.anyObject(File.class));
      EasyMock.expectLastCall().andThrow(
            new CaptureMasseRuntimeException("Erreur technique")).times(3);
      EasyMock.replay(controleService);

      service.captureMasse(ecdeTestSommaire.getUrlEcde(), UUID.randomUUID());

      EasyMock.verify(controleService);

      List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      boolean logErrorExists = LogUtils.logExists(loggingEvents, Level.ERROR,
            "Erreur technique");

      Assert.assertTrue("le log erreur doit exister", logErrorExists);

      List<ILoggingEvent> errorLogs = LogUtils.getLogsByLevel(loggingEvents,
            Level.ERROR);
      boolean foundNullPointer = false;
      int index = 0;

      while (!foundNullPointer && index < errorLogs.size()) {
         if (errorLogs.get(index).getMessage().contains("SOMMAIRE_FILE")
               && errorLogs.get(index).getMessage().contains(
                     "java.lang.ClassCastException")) {
            foundNullPointer = true;
         }
         index++;
      }

      Assert.assertFalse("l'erreur NullPointer ne doit pas etre présente",
            foundNullPointer);

      checkFiles();

   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/checkEcde/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/checkEcde/documents/doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

   }

   private void checkFiles() throws IOException {

      File repTraitement = ecdeTestSommaire.getRepEcde();
      File debut = new File(repTraitement, "debut_traitement.flag");
      File fin = new File(repTraitement, "fin_traitement.flag");
      File resultats = new File(repTraitement, "resultats.xml");

      //FileUtils.copyFile(resultats, new File("c:/resultats.xml"));

      Assert.assertTrue("le fichier debut_traitement.flag doit exister", debut
            .exists());
      Assert.assertTrue("le fichier fin_traitement.flag doit exister", fin
            .exists());
      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
            .exists());

      Assert.assertTrue("l'erreur doit être une SAE-CA-BUL001", FileUtils
            .readFileToString(resultats).contains("SAE-CA-BUL001"));

   }
}
