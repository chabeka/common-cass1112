package fr.urssaf.image.sae.rnd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.RndRecuperationException;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;
import fr.urssaf.image.sae.rnd.utils.SaeLogAppender;
import fr.urssaf.image.sae.rnd.ws.adrn.service.RndRecuperationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class MajRndServiceTest {

   @Autowired
   private MajRndService majRndService;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Autowired
   private SaeBddSupport saeBddSupport;

   // Mocks
   @Autowired
   private RndRecuperationService rndRecuperationService;

   // Mocks
   @Autowired
   private ServiceProvider serviceProvider;

   // Mocks
   @Autowired
   private DFCEConnectionService dfceConnectionService;

   // Mocks
   @Autowired
   private StorageAdministrationService storageAdministrationService;

   // Mocks
   @Autowired
   private LifeCycleRule lifeCycleRule;

   private Logger logger;

   private SaeLogAppender logAppender;

   @Before
   public void before() throws SaeBddRuntimeException, RndRecuperationException {

      logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      logAppender = new SaeLogAppender(Level.INFO, "fr.urssaf.image.sae");
      logger.addAppender(logAppender);

      VersionRnd version = new VersionRnd();
      version.setDateMiseAJour(new Date());
      version.setVersionEnCours("11.4");
      saeBddSupport.updateVersionRnd(version);
   }

   @After
   public void after() throws Exception {
      EasyMock.reset(rndRecuperationService);
      server.resetData();
      logger.detachAppender(logAppender);
   }

   @Test
   public void testLancer() throws Exception {

      initComposantsADRN();
      initComposantsDFCE();

      majRndService.lancer();

      checkLogs();

      // On vérifie que les codes "1.1.1.1.1", "2.1.1.1.1", "3.1.1.1.1" et
      // "0.0.0.0.0" existe bien dans la base
      TypeDocument typeDoc1 = rndSupport.getRnd("1.1.1.1.1");
      Assert.assertNotNull(typeDoc1);

      TypeDocument typeDoc2 = rndSupport.getRnd("2.1.1.1.1");
      Assert.assertNotNull(typeDoc2);

      TypeDocument typeDoc3 = rndSupport.getRnd("3.1.1.1.1");
      Assert.assertNotNull(typeDoc3);

      // On vérifie que la durée de conservation du 1er doc a été passée à 300
      Assert.assertEquals(
            "Le type de doc 1.1.1.1.1 doit avoir une durée de conservation à 300",
            300, typeDoc1.getDureeConservation());

      // On vérifie que le code "1.1.1.1.1" a été passé à clôturé suite à la
      // mise à jour des correspondances
      Assert.assertEquals("Le type de doc 1.1.1.1.1 doit être clôturé", true,
            typeDoc1.isCloture());

   }

   private void initComposantsADRN() throws Exception {

      // Version du RND dans l'ADRN
      // --------------------------
      EasyMock.expect(rndRecuperationService.getVersionCourante())
            .andReturn("11.5").anyTimes();

      // Liste des types de documents de l'ADRN
      // --------------------------------------
      List<TypeDocument> listeTypeDoc = new ArrayList<TypeDocument>();
      TypeDocument typeDoc1 = new TypeDocument();
      typeDoc1.setCloture(false);
      typeDoc1.setCode("1.1.1.1.1");
      typeDoc1.setCodeActivite("1");
      typeDoc1.setCodeFonction("1");
      typeDoc1.setDureeConservation(300);
      typeDoc1.setLibelle("Libellé 1.1.1.1.1");
      typeDoc1.setType(TypeCode.ARCHIVABLE_AED);
      listeTypeDoc.add(typeDoc1);
      TypeDocument typeDoc2 = new TypeDocument();
      typeDoc2.setCloture(false);
      typeDoc2.setCode("2.1.1.1.1");
      typeDoc2.setCodeActivite("1");
      typeDoc2.setCodeFonction("2");
      typeDoc2.setDureeConservation(400);
      typeDoc2.setLibelle("Libellé 2.1.1.1.1");
      typeDoc2.setType(TypeCode.ARCHIVABLE_AED);
      listeTypeDoc.add(typeDoc2);
      TypeDocument typeDoc3 = new TypeDocument();
      typeDoc3.setCloture(false);
      typeDoc3.setCode("3.1.1.1.1");
      typeDoc3.setCodeActivite("1");
      typeDoc3.setCodeFonction("3");
      typeDoc3.setDureeConservation(500);
      typeDoc3.setLibelle("Libellé 3.1.1.1.1");
      typeDoc3.setType(TypeCode.ARCHIVABLE_AED);
      listeTypeDoc.add(typeDoc3);

      EasyMock
            .expect(
                  rndRecuperationService.getListeRnd(EasyMock
                        .anyObject(String.class))).andReturn(listeTypeDoc)
            .anyTimes();

      // Le 1er document sera déjà dans le RND du SAE mais avec la propriété
      // sur la durée de conservation différente, il sera donc ajouté (écrase)
      TypeDocument typeDoc1bis = new TypeDocument();
      typeDoc1bis.setCloture(false);
      typeDoc1bis.setCode("1.1.1.1.1");
      typeDoc1bis.setCodeActivite("1");
      typeDoc1bis.setCodeFonction("1");
      typeDoc1bis.setDureeConservation(3000);
      typeDoc1bis.setLibelle("Libellé 1.1.1.1.1");
      typeDoc1bis.setType(TypeCode.ARCHIVABLE_AED);
      rndSupport.ajouterRnd(typeDoc1bis, jobClockSupport.currentCLock());
      // Le 2ème document n'est pas dans le RND du SAE, il sera donc ajouté
      // Le 3ème document sera déjà dans le RND du SAE donc il ne sera pas
      // ajouté (toutes propriétés identiques)
      rndSupport.ajouterRnd(typeDoc3, jobClockSupport.currentCLock());

      // Liste des correspondances
      // -------------------------
      Map<String, String> listeCorrespondances = new TreeMap<String, String>();
      listeCorrespondances.put("1.1.1.1.1", "2.2.2.2.2");
      EasyMock
            .expect(
                  rndRecuperationService.getListeCorrespondances(EasyMock
                        .anyObject(String.class)))
            .andReturn(listeCorrespondances).anyTimes();

      // Liste des codes temporaires
      // ---------------------------
      List<TypeDocument> listeCodesTempo = new ArrayList<TypeDocument>();
      TypeDocument typeDoc4 = new TypeDocument();
      typeDoc4.setCloture(false);
      typeDoc4.setCode("0.0.0.0.0");
      typeDoc4.setDureeConservation(3000);
      typeDoc4.setLibelle("Libellé 0.0.0.0.0");
      typeDoc4.setType(TypeCode.TEMPORAIRE);
      rndSupport.ajouterRnd(typeDoc4, jobClockSupport.currentCLock());
      EasyMock.expect(rndRecuperationService.getListeCodesTemporaires())
            .andReturn(listeCodesTempo);

      EasyMock.replay(rndRecuperationService);
   }

   private void initComposantsDFCE() throws ObjectAlreadyExistsException {
      // Réglage dfce
      serviceProvider.connect(EasyMock.anyObject(String.class),
            EasyMock.anyObject(String.class), EasyMock.anyObject(String.class),
            EasyMock.anyInt());
      EasyMock.expectLastCall().anyTimes();
      serviceProvider.disconnect();
      EasyMock.expectLastCall().anyTimes();
      EasyMock.expect(serviceProvider.getStorageAdministrationService())
            .andReturn(storageAdministrationService).anyTimes();
      EasyMock.expect(dfceConnectionService.openConnection())
            .andReturn(serviceProvider).anyTimes();

      // 1er appel, on retourne une durée de conservation différente de celle du
      // type de doc 1 à mettre à jour donc la méthode updateLifeCycleRule doit
      // être appelée
      EasyMock.expect(lifeCycleRule.getLifeCycleLength()).andReturn(3000)
            .times(1);

      // 2ème appel, on retourne une durée de conservation égale à celle
      // du type de doc 2 à mettre à jour donc rien ne doit être fait
      EasyMock.expect(lifeCycleRule.getLifeCycleLength()).andReturn(400)
            .times(2);

      // Il n'y aura pas de 3ème appel car le 3ème doc ne sera pas présent dans
      // la CF LifeCycleRule

      // Les 2 premiers appels on retourne un lifeCylcleRule
      EasyMock
            .expect(
                  storageAdministrationService.getLifeCycleRule(EasyMock
                        .anyObject(String.class))).andReturn(lifeCycleRule)
            .times(1);
      EasyMock
            .expect(
                  storageAdministrationService.getLifeCycleRule(EasyMock
                        .anyObject(String.class))).andReturn(lifeCycleRule)
            .times(1);

      // Le 3ème appel, on retourne null, donc la méthode createNewLifeCycleRule
      // doit être appelée
      EasyMock
            .expect(
                  storageAdministrationService.getLifeCycleRule(EasyMock
                        .anyObject(String.class))).andReturn(null).times(1);

      EasyMock.expect(
            storageAdministrationService.createNewLifeCycleRule(
                  EasyMock.anyObject(LifeCycleRule.class))).andReturn(
            lifeCycleRule);

      EasyMock
            .expect(
                  storageAdministrationService.updateLifeCycleRule(
                        EasyMock.anyObject(LifeCycleRule.class)))
            .andReturn(lifeCycleRule).anyTimes();

      EasyMock.replay(serviceProvider, dfceConnectionService,
            storageAdministrationService, lifeCycleRule);
   }

   private void checkLogs() {
      List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertTrue("Message de log d'info incorrect",
            loggingEvents != null && loggingEvents.size() > 0);

      Assert.assertTrue(logContains(loggingEvents,
            "ajouterRnd - Ajout du code : 1.1.1.1.1"));

      Assert.assertTrue(logContains(loggingEvents,
            "ajouterRnd - Ajout du code : 2.1.1.1.1"));

      Assert.assertTrue(logContains(
            loggingEvents,
            "updateLifeCycleRule - La durée de conservation du code 1.1.1.1.1 a été modifiée (3000 => 300) !"));

      Assert.assertTrue(logContains(loggingEvents,
            "updateLifeCycleRule - Ajout du code : 3.1.1.1.1"));

      Assert.assertTrue(logContains(loggingEvents,
            "ajouterCorrespondance - Ajout de la correspondance : 1.1.1.1.1 / 2.2.2.2.2"));

   }

   private boolean logContains(List<ILoggingEvent> loggingEvents, String message) {

      boolean result = false;

      if (!CollectionUtils.isEmpty(loggingEvents)) {
         for (ILoggingEvent loggingEvent : loggingEvents) {
            if (loggingEvent.getFormattedMessage().equals(message)) {
               result = true;
               break;
            }
         }
      }

      return result;

   }

}
