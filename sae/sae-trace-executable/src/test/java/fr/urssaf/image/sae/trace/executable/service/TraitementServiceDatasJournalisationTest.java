/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
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
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableException;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableRuntimeException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.trace.service.ParametersService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-executable-test.xml" })
public class TraitementServiceDatasJournalisationTest {

   private static final String CODE_EVT = "TEST_TRAITEMENT_SERVICE";

   @Autowired
   private ParametersService paramService;

   @Autowired
   private TraitementService traitementService;

   @Autowired
   private TraceDestinataireSupport traceSupport;

   @Autowired
   private DispatcheurService dispatcheurService;

   @Autowired
   private CassandraServerBean serverBean;

   @After
   public void after() throws Exception {
      serverBean.resetData();
   }

   @Test
   public void testJournalisationDejaEnCours() {
      createParameter(ParameterType.JOURNALISATION_EVT_IS_RUNNING, Boolean.TRUE);

      try {
         traitementService.journaliser(JournalisationType.JOURNALISATION_EVT);
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");

      } catch (TraceExecutableRuntimeException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct", exception
               .getMessage(),
               "la journalisation JOURNALISATION_EVT est déjà en cours");

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");
      }

   }

   @Test
   public void testJournalisationDateNonRenseignee() {

      try {
         traitementService.journaliser(JournalisationType.JOURNALISATION_EVT);
         Assert.fail("une exception TraceRuntimeException est attendue");

      } catch (TraceRuntimeException exception) {
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     exception.getCause().getClass(),
                     ParameterNotFoundException.class);
         Assert.assertEquals("le message d'erreur doit etre correct", exception
               .getCause().getMessage(), "le paramètre "
               + ParameterType.JOURNALISATION_EVT_DATE.toString()
               + " n'existe pas");

      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }

   }

   @Test
   @Ignore("initialisation du TimeStamp désactivé pour cause de doublons")
   public void testJournalisationIdJournalPrecedentNonRenseignee() {

      createTraces();

      createParameter(ParameterType.JOURNALISATION_EVT_DATE, DateUtils.addDays(
            new Date(), -2));

      try {
         traitementService.journaliser(JournalisationType.JOURNALISATION_EVT);
         Assert.fail("une exception TraceRuntimeException est attendue");

      } catch (TraceRuntimeException exception) {
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     exception.getCause().getClass(),
                     ParameterNotFoundException.class);
         Assert.assertEquals("le message d'erreur doit etre correct", exception
               .getCause().getMessage(), "le paramètre "
               + ParameterType.JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT
                     .toString() + " n'existe pas");

      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }

   }

   @Test
   @Ignore("initialisation du TimeStamp désactivé pour cause de doublons")
   public void testJournalisationHashJournalPrecedentNonRenseignee() {

      createTraces();

      createParameter(ParameterType.JOURNALISATION_EVT_DATE, DateUtils.addDays(
            new Date(), -2));
      createParameter(ParameterType.JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT,
            UUID.randomUUID().toString());

      try {
         traitementService.journaliser(JournalisationType.JOURNALISATION_EVT);
         Assert.fail("une exception TraceRuntimeException est attendue");

      } catch (TraceRuntimeException exception) {
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     exception.getCause().getClass(),
                     ParameterNotFoundException.class);
         Assert.assertEquals("le message d'erreur doit etre correct", exception
               .getCause().getMessage(), "le paramètre "
               + ParameterType.JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT
                     .toString() + " n'existe pas");

      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }
   }

   @Test
   @Ignore("initialisation du TimeStamp désactivé pour cause de doublons")
   public void testSucces() throws IOException, TraceExecutableException,
         SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         NotArchivableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         ParameterNotFoundException {

      create4Traces();

      createParameter(ParameterType.JOURNALISATION_EVT_DATE, DateUtils.addDays(
            new Date(), -3));
      createParameter(ParameterType.JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT,
            UUID.randomUUID().toString());
      createParameter(ParameterType.JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT,
            "bca50fcd3aa69f927df1a0775fe63e6882dc8913");

      createParameter(ParameterType.JOURNALISATION_EVT_META_TITRE,
            "JournalisationTest");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            "SAE");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_ORGA, "UR750");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_RND,
            "7.7.8.8.1");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT,
            "SAET");

      traitementService.journaliser(JournalisationType.JOURNALISATION_EVT);

      Date date = (Date) paramService.loadParameter(
            ParameterType.JOURNALISATION_EVT_DATE).getValue();
      Assert.assertTrue("le dernier jour stocké doit etre à -1", DateUtils
            .isSameDay(DateUtils.addDays(new Date(), -1), date));

      Boolean isRunning = (Boolean) paramService.loadParameter(
            ParameterType.JOURNALISATION_EVT_IS_RUNNING).getValue();
      Assert.assertEquals("le traitement doit etre arrete", Boolean.FALSE,
            isRunning);

   }

   private void createParameter(ParameterType type, Object value) {
      Parameter parameter = new Parameter(type, value);
      paramService.saveParameter(parameter);
   }

   private void createTraces() {
      TraceDestinataire destinataire = new TraceDestinataire();
      destinataire.setCodeEvt(CODE_EVT);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_JOURN_EVT, null);
      destinataire.setDestinataires(map);

      traceSupport.create(destinataire, new Date().getTime());

      createTrace("[JOUR J]", 0);
      createTrace("[JOUR J-1]", -1);
      createTrace("[JOUR J-2]", -2);
      createTrace("[JOUR J-3]", -3);
      createTrace("[JOUR J-4]", -4);
      createTrace("[JOUR J-5]", -5);
   }

   private void create4Traces() {
      TraceDestinataire destinataire = new TraceDestinataire();
      destinataire.setCodeEvt(CODE_EVT);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_JOURN_EVT, null);
      destinataire.setDestinataires(map);

      traceSupport.create(destinataire, new Date().getTime());

      createTrace("[JOUR J]", 0);
      createTrace("[JOUR J-1]", -1);
      createTrace("[JOUR J-2]", -2);
      createTrace("[JOUR J-3]", -3);
   }

   private void createTrace(String suffix, int decalage) {
      TraceToCreate trace = new TraceToCreate();
      trace.setAction("action " + suffix);
      trace.setCodeEvt(CODE_EVT);
      trace.setContexte("contexte " + suffix);
      trace.setContrat("contrat " + suffix);
      trace.setInfos(null);
      trace.setLogin("login " + suffix);
      trace.setStracktrace("stackTrace " + suffix);
      dispatcheurService.ajouterTrace(trace);
   }

}
