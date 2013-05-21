package fr.urssaf.image.sae.rnd.support;

import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.reference.LifeCycleLengthUnit;
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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.rnd.exception.DfceRuntimeException;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.utils.SaeLogAppender;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class DfceSupportTest {

   @Autowired
   private ServiceProvider serviceProvider;

   @Autowired
   private DFCEConnectionService dfceConnectionService;

   @Autowired
   private StorageAdministrationService storageAdministrationService;

   @Autowired
   private LifeCycleRule lifeCycleRule;

   @Autowired
   private DfceSupport dfceSupport;

   private Logger logger;

   private SaeLogAppender logAppender;

   @Before
   public void init() {
      logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      logAppender = new SaeLogAppender(Level.INFO, "fr.urssaf.image.sae");
      logger.addAppender(logAppender);
   }

   @After
   public void after() throws Exception {
      EasyMock.reset(serviceProvider, dfceConnectionService,
            storageAdministrationService, lifeCycleRule);

      logger.detachAppender(logAppender);
   }

   @Test
   public void testUpdateLifeCycleRuleNouveauCode()
         throws DfceRuntimeException, ObjectAlreadyExistsException {

      initComposantsNouveauCode();

      List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
      TypeDocument typeDoc1 = new TypeDocument();
      typeDoc1.setCloture(false);
      typeDoc1.setCode("1.1.1.1.1");
      typeDoc1.setCodeActivite("1");
      typeDoc1.setCodeFonction("1");
      typeDoc1.setDureeConservation(3000);
      typeDoc1.setLibelle("libelle");
      typeDoc1.setType(TypeCode.ARCHIVABLE_AED);

      listeTypeDocs.add(typeDoc1);

      dfceSupport.updateLifeCycleRule(listeTypeDocs);

      checkLogsNouveauCode();

      EasyMock.verify(serviceProvider, dfceConnectionService,
            storageAdministrationService, lifeCycleRule);

   }

   @Test
   public void testUpdateLifeCycleRuleCodeExistantDureeIdentique()
         throws DfceRuntimeException, ObjectAlreadyExistsException {

      initComposantsCodeExistantDureeIdentique();

      List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
      TypeDocument typeDoc1 = new TypeDocument();
      typeDoc1.setCloture(false);
      typeDoc1.setCode("1.1.1.1.1");
      typeDoc1.setCodeActivite("1");
      typeDoc1.setCodeFonction("1");
      typeDoc1.setDureeConservation(3000);
      typeDoc1.setLibelle("libelle");
      typeDoc1.setType(TypeCode.ARCHIVABLE_AED);

      listeTypeDocs.add(typeDoc1);

      dfceSupport.updateLifeCycleRule(listeTypeDocs);

      checkLogsCodeExistantDureeIdentique();

      EasyMock.verify(serviceProvider, dfceConnectionService,
            storageAdministrationService, lifeCycleRule);

   }

   @Test
   public void testUpdateLifeCycleRuleCodeExistantDureeDifferente()
         throws DfceRuntimeException, ObjectAlreadyExistsException {

      initComposantsCodeExistantDureeDifferente();

      List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
      TypeDocument typeDoc1 = new TypeDocument();
      typeDoc1.setCloture(false);
      typeDoc1.setCode("1.1.1.1.1");
      typeDoc1.setCodeActivite("1");
      typeDoc1.setCodeFonction("1");
      typeDoc1.setDureeConservation(3000);
      typeDoc1.setLibelle("libelle");
      typeDoc1.setType(TypeCode.ARCHIVABLE_AED);

      listeTypeDocs.add(typeDoc1);

      dfceSupport.updateLifeCycleRule(listeTypeDocs);

      checkLogsCodeExistantDureeDifferente();

      EasyMock.verify(serviceProvider, dfceConnectionService,
            storageAdministrationService, lifeCycleRule);

   }

   private void initComposantsNouveauCode() throws ObjectAlreadyExistsException {
      initDfce();
      initStorageAdministrationCreate();
      replay();
   }

   private void initComposantsCodeExistantDureeIdentique() {
      initDfce();
      initLifeCycleRule(3000);
      // Réglage storageAdministrationService
      EasyMock.expect(
            storageAdministrationService.getLifeCycleRule(EasyMock
                  .anyObject(String.class))).andReturn(lifeCycleRule)
            .anyTimes();
      replay();
   }

   private void initComposantsCodeExistantDureeDifferente() {
      initDfce();
      initLifeCycleRule(5000);
      initStorageAdministrationUpdate();
      replay();
   }

   private void replay() {
      EasyMock.replay(serviceProvider, dfceConnectionService,
            storageAdministrationService, lifeCycleRule);
   }

   private void initDfce() {
      // Réglage dfce
      serviceProvider.connect(EasyMock.anyObject(String.class), EasyMock
            .anyObject(String.class), EasyMock.anyObject(String.class));
      EasyMock.expectLastCall().anyTimes();
      serviceProvider.disconnect();
      EasyMock.expectLastCall().anyTimes();
      EasyMock.expect(serviceProvider.getStorageAdministrationService())
            .andReturn(storageAdministrationService).anyTimes();
      EasyMock.expect(dfceConnectionService.openConnection()).andReturn(
            serviceProvider).anyTimes();
   }

   private void initLifeCycleRule(int duree) {
      // Réglage de lifeCycleRule
      EasyMock.expect(lifeCycleRule.getLifeCycleLength()).andReturn(duree)
            .anyTimes();
   }

   private void initStorageAdministrationCreate()
         throws ObjectAlreadyExistsException {
      // Réglage storageAdministrationService
      EasyMock.expect(
            storageAdministrationService.getLifeCycleRule(EasyMock
                  .anyObject(String.class))).andReturn(null).anyTimes();

      EasyMock.expect(
            storageAdministrationService.createNewLifeCycleRule(EasyMock
                  .anyObject(String.class), EasyMock.anyInt(), EasyMock
                  .anyObject(LifeCycleLengthUnit.class))).andReturn(
            lifeCycleRule);
   }

   private void initStorageAdministrationUpdate() {
      // Réglage storageAdministrationService
      EasyMock.expect(
            storageAdministrationService.getLifeCycleRule(EasyMock
                  .anyObject(String.class))).andReturn(lifeCycleRule)
            .anyTimes();

      EasyMock.expect(
            storageAdministrationService.updateLifeCycleRule(EasyMock
                  .anyObject(String.class), EasyMock.anyInt(), EasyMock
                  .anyObject(LifeCycleLengthUnit.class))).andReturn(
            lifeCycleRule);
   }

   private void checkLogsNouveauCode() {
      List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertTrue("Message de log d'info incorrect",
            loggingEvents != null
                  && loggingEvents.size() > 0
                  && "updateLifeCycleRule - Ajout du code : 1.1.1.1.1"
                        .equals(loggingEvents.get(0).getFormattedMessage()));

   }

   private void checkLogsCodeExistantDureeIdentique() {
      List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertTrue("Message de log d'info doit être vide",
            loggingEvents != null && loggingEvents.size() == 0);

   }

   private void checkLogsCodeExistantDureeDifferente() {
      List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert
            .assertTrue(
                  "Message de log d'info incorrect",
                  loggingEvents != null
                        && loggingEvents.size() > 0
                        && "updateLifeCycleRule - La durée de conservation du code 1.1.1.1.1 a été modifiée (5000 => 3000) !"
                              .equals(loggingEvents.get(0)
                                    .getFormattedMessage()));
   }
}
