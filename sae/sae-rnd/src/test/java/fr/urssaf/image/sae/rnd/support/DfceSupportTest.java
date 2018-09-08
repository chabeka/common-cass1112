package fr.urssaf.image.sae.rnd.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import com.docubase.dfce.exception.ObjectAlreadyExistsException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.rnd.exception.DfceRuntimeException;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.utils.SaeLogAppender;
import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.model.reference.LifeCycleStep;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class DfceSupportTest {

   // Mock
   @Autowired
   private DFCEServices dfceServices;

   @Autowired
   private LifeCycleRule lifeCycleRule;

   @Autowired
   private LifeCycleStep lifeCycleStep;

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
      EasyMock.reset(dfceServices, lifeCycleRule, lifeCycleStep);

      logger.detachAppender(logAppender);
   }

   @Test
   public void testUpdateLifeCycleRuleNouveauCode()
         throws DfceRuntimeException, ObjectAlreadyExistsException {

      initComposantsNouveauCode();

      final List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
      final TypeDocument typeDoc1 = new TypeDocument();
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

      EasyMock.verify(dfceServices, lifeCycleRule, lifeCycleStep);

   }

   @Test
   public void testUpdateLifeCycleRuleCodeExistantDureeIdentique()
         throws DfceRuntimeException, ObjectAlreadyExistsException {

      initComposantsCodeExistantDureeIdentique();

      final List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
      final TypeDocument typeDoc1 = new TypeDocument();
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

      EasyMock.verify(dfceServices, lifeCycleRule, lifeCycleStep);

   }

   /**
    * JIRA CRTL-113<br>
    * Ajout d'un log dans le DfceSupport pour savoir sur quel RND l'exception
    * DFCE s'est produite
    */
   @Test
   public void testUpdateLifeCycleRuleCodeExistantDureeDifferenteAvecExceptionDfce()
         throws DfceRuntimeException, ObjectAlreadyExistsException {

      initComposantsCodeExistantDureeDifferenteEtException();

      final List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
      final TypeDocument typeDoc1 = new TypeDocument();
      typeDoc1.setCloture(false);
      typeDoc1.setCode("1.1.1.1.1");
      typeDoc1.setCodeActivite("1");
      typeDoc1.setCodeFonction("1");
      typeDoc1.setDureeConservation(3000);
      typeDoc1.setLibelle("libelle");
      typeDoc1.setType(TypeCode.ARCHIVABLE_AED);

      listeTypeDocs.add(typeDoc1);

      try {
         dfceSupport.updateLifeCycleRule(listeTypeDocs);
         Assert.fail("Une exception DfceRuntimeException aurait dû être levée");

      } catch (final DfceRuntimeException ex) {
         Assert.assertEquals(
                             "Le message de l'exception levée n'est pas celui attendu",
                             "Erreur sur la mise à jour du type de document 1.1.1.1.1 dans DFCE",
                             ex.getMessage());
      }

   }

   @Test
   public void testUpdateLifeCycleRuleCodeExistantDureeDifferente()
         throws DfceRuntimeException, ObjectAlreadyExistsException {

      initComposantsCodeExistantDureeDifferente();

      final List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
      final TypeDocument typeDoc1 = new TypeDocument();
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

      EasyMock.verify(dfceServices, lifeCycleRule, lifeCycleStep);

   }

   private void initComposantsNouveauCode() throws ObjectAlreadyExistsException {
      initStorageAdministrationCreate();
      replay();
   }

   private void initComposantsCodeExistantDureeIdentique() {
      initLifeCycleRule(3000);
      // Réglage storageAdministrationService
      EasyMock
      .expect(
              dfceServices.getLifeCycleRule(EasyMock
                                            .anyObject(String.class))).andReturn(lifeCycleRule)
      .anyTimes();
      replay();
   }

   private void initComposantsCodeExistantDureeDifferente() {
      initLifeCycleRule(5000);
      initStorageAdministrationUpdate();
      replay();
   }

   private void initComposantsCodeExistantDureeDifferenteEtException() {
      initLifeCycleRule(5000);
      initStorageAdministrationUpdateAvecExceptionJiraCRTL113();
      replay();
   }

   private void replay() {
      EasyMock.replay(dfceServices, lifeCycleRule, lifeCycleStep);
   }


   private void initLifeCycleRule(final int duree) {
      // Réglage de lifeCycleStep
      EasyMock.expect(lifeCycleStep.getLength()).andReturn(duree)
      .anyTimes();
      // Réglage de lifeCycleRule
      final List<LifeCycleStep> steps = new ArrayList<LifeCycleStep>();
      steps.add(lifeCycleStep);
      EasyMock.expect(lifeCycleRule.getSteps()).andReturn(steps)
      .anyTimes();
   }

   private void initStorageAdministrationCreate()
         throws ObjectAlreadyExistsException {
      // Réglage storageAdministrationService
      EasyMock
      .expect(
              dfceServices.getLifeCycleRule(EasyMock
                                            .anyObject(String.class))).andReturn(null).anyTimes();

      EasyMock.expect(
                      dfceServices.createNewLifeCycleRule(
                                                          EasyMock.anyObject(LifeCycleRule.class))).andReturn(
                                                                                                              lifeCycleRule);
   }

   private void initStorageAdministrationUpdate() {
      // Réglage storageAdministrationService
      EasyMock
      .expect(
              dfceServices.getLifeCycleRule(EasyMock
                                            .anyObject(String.class))).andReturn(lifeCycleRule)
      .anyTimes();

      EasyMock.expect(
                      dfceServices.updateLifeCycleRule(
                                                       EasyMock.anyObject(LifeCycleRule.class))).andReturn(
                                                                                                           lifeCycleRule);
   }

   private void initStorageAdministrationUpdateAvecExceptionJiraCRTL113() {
      // Réglage storageAdministrationService
      // avec levée d'une exception
      EasyMock
      .expect(
              dfceServices.getLifeCycleRule(EasyMock
                                            .anyObject(String.class))).andReturn(lifeCycleRule)
      .anyTimes();

      EasyMock
      .expect(
              dfceServices.updateLifeCycleRule(
                                               EasyMock.anyObject(LifeCycleRule.class)))
      .andThrow(
                new IllegalStateException(
                                          "no life cycle rule update can be made as long as previous rule history was not handled"));

   }

   private void checkLogsNouveauCode() {
      final List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertTrue(
                        "Message de log d'info incorrect",
                        loggingEvents != null
                        && loggingEvents.size() > 0
                        && "updateLifeCycleRule - Ajout du code : 1.1.1.1.1"
                        .equals(loggingEvents.get(0).getFormattedMessage()));

   }

   private void checkLogsCodeExistantDureeIdentique() {
      final List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertTrue("Message de log d'info doit être vide",
                        loggingEvents != null && compteNbLogInfo(loggingEvents) == 0);

   }

   private void checkLogsCodeExistantDureeDifferente() {
      final List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertTrue(
                        "Message de log d'info incorrect",
                        loggingEvents != null
                        && compteNbLogInfo(loggingEvents) == 1
                        && "updateLifeCycleRule - La durée de conservation du code 1.1.1.1.1 a été modifiée (5000 => 3000) !"
                        .equals(recupPremierLogInfo(loggingEvents)));
   }

   private int compteNbLogInfo(final List<ILoggingEvent> loggingEvents) {

      int comptage = 0;
      if (loggingEvents != null) {
         for (final ILoggingEvent loggingEvent : loggingEvents) {
            if (loggingEvent.getLevel() == Level.INFO) {
               comptage++;
            }
         }
      }
      return comptage;

   }

   private String recupPremierLogInfo(final List<ILoggingEvent> loggingEvents) {
      String result = StringUtils.EMPTY;
      if (loggingEvents != null) {
         for (final ILoggingEvent loggingEvent : loggingEvents) {
            if (loggingEvent.getLevel() == Level.INFO) {
               result = loggingEvent.getFormattedMessage();
               break;
            }
         }
      }
      return result;
   }

}
