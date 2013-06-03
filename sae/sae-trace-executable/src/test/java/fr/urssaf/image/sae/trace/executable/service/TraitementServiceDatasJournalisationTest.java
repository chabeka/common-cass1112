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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
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
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegExploitationSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableException;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableRuntimeException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.StatusService;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-executable-test.xml" })
public class TraitementServiceDatasJournalisationTest {

   private static final String CODE_EVT = "TEST_TRAITEMENT_SERVICE";
   private static final Date DATE = new Date();

   @Autowired
   private ParametersService paramService;

   @Autowired
   private StatusService statusService;

   @Autowired
   private TraitementService traitementService;

   @Autowired
   private TraceDestinataireSupport traceSupport;

   @Autowired
   private CassandraServerBean serverBean;

   @Autowired
   private TraceRegExploitationSupport exploitationSupport;

   @Autowired
   private TraceRegSecuriteSupport securiteSupport;

   @Autowired
   private TraceRegTechniqueSupport techniqueSupport;

   @Autowired
   private TraceJournalEvtSupport evtSupport;

   @Autowired
   private TimeUUIDEtTimestampSupport timeUUIDSupport;
   
   @Autowired
   private ParametersService parametersService;
   @Autowired 
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   @After
   public void after() throws Exception {
      serverBean.resetData();
   }

   @Test
   public void testJournalisationDejaEnCours() {

      paramService.setJournalisationEvtIsRunning(Boolean.TRUE);

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
         checkExceptionParametreInexistant(exception, "JOURNALISATION_EVT_DATE");

      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }

   }

   private void checkExceptionParametreInexistant(
         TraceRuntimeException exception, String parametre) {

      Assert.assertEquals(
            "le message d'erreur d'origine doit etre un parametre non trouvé",
            ParameterNotFoundException.class, exception.getCause().getClass());

      String messageAttendu = "le paramètre " + parametre + " n'existe pas";
      String messageObtenu = exception.getCause().getMessage();
      Assert.assertEquals("le message d'erreur doit etre correct",
            messageAttendu, messageObtenu);

   }

   @Test
   public void testJournalisationIdJournalPrecedentNonRenseignee() {

      createTraces();

      paramService.setJournalisationEvtDate(DateUtils.addDays(new Date(), -2));

      try {

         traitementService.journaliser(JournalisationType.JOURNALISATION_EVT);
         Assert.fail("une exception TraceRuntimeException est attendue");

      } catch (TraceRuntimeException exception) {
         checkExceptionParametreInexistant(exception,
               "JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT");

      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }

   }

   @Test
   public void testJournalisationHashJournalPrecedentNonRenseignee() {

      createTraces();

      paramService.setJournalisationEvtDate(DateUtils.addDays(new Date(), -2));
      paramService
            .setJournalisationEvtIdJournPrec(UUID.randomUUID().toString());

      try {
         traitementService.journaliser(JournalisationType.JOURNALISATION_EVT);
         Assert.fail("une exception TraceRuntimeException est attendue");

      } catch (TraceRuntimeException exception) {
         checkExceptionParametreInexistant(exception,
               "JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT");

      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }
   }

   @Test
   public void testSucces() throws IOException, TraceExecutableException,
         SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         NotArchivableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         ParameterNotFoundException {

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");
      
      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("7.7.8.8.1");
      typeDocCree.setCodeActivite("7");
      typeDocCree.setCodeFonction("7");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("ATTESTATION DE VIGILANCE");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);
      
      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
      
      create4Traces();

      paramService.setJournalisationEvtDate(DateUtils.addDays(DATE, -3));
      paramService
            .setJournalisationEvtIdJournPrec(UUID.randomUUID().toString());
      paramService
            .setJournalisationEvtHashJournPrec("bca50fcd3aa69f927df1a0775fe63e6882dc8913");
      paramService.setJournalisationEvtMetaTitre("JournalisationTest");
      paramService.setJournalisationEvtMetaApplProd("SAE");
      paramService.setJournalisationEvtMetaApplTrait("SAET");
      paramService.setJournalisationEvtMetaCodeOrga("UR750");
      paramService.setJournalisationEvtMetaCodeRnd("7.7.8.8.1");

      traitementService.journaliser(JournalisationType.JOURNALISATION_EVT);

      Date date = paramService.getJournalisationEvtDate();
      Assert.assertTrue("le dernier jour stocké doit etre à -1", DateUtils
            .isSameDay(DateUtils.addDays(new Date(), -1), date));

      Boolean isRunning = statusService
            .isJournalisationRunning(JournalisationType.JOURNALISATION_EVT);
      Assert.assertEquals("le traitement doit etre arrete", Boolean.FALSE,
            isRunning);

   }

   private void createTraces() {
      TraceDestinataire destinataire = new TraceDestinataire();
      destinataire.setCodeEvt(CODE_EVT);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_JOURN_EVT, null);
      destinataire.setDestinataires(map);

      traceSupport.create(destinataire, new Date().getTime());

      createTrace("[JOUR J]", 0, destinataire);
      createTrace("[JOUR J-1]", -1, destinataire);
      createTrace("[JOUR J-2]", -2, destinataire);
      createTrace("[JOUR J-3]", -3, destinataire);
      createTrace("[JOUR J-4]", -4, destinataire);
      createTrace("[JOUR J-5]", -5, destinataire);
   }

   private void create4Traces() {
      TraceDestinataire destinataire = new TraceDestinataire();
      destinataire.setCodeEvt(CODE_EVT);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_JOURN_EVT, null);
      destinataire.setDestinataires(map);

      traceSupport.create(destinataire, new Date().getTime());

      createTrace("[JOUR J]", 0, destinataire);
      createTrace("[JOUR J-1]", -1, destinataire);
      createTrace("[JOUR J-2]", -2, destinataire);
      createTrace("[JOUR J-3]", -3, destinataire);
   }

   private void createTrace(String suffix, int decalage,
         TraceDestinataire destinataire) {
      TraceToCreate trace = new TraceToCreate();
      trace.setAction("action " + suffix);
      trace.setCodeEvt(CODE_EVT);
      trace.setContexte("contexte " + suffix);
      trace.setContrat("contrat " + suffix);
      trace.setInfos(null);
      trace.setLogin("login " + suffix);
      trace.setStracktrace("stackTrace " + suffix);
      for (String dest : destinataire.getDestinataires().keySet()) {
         createTraces(trace, dest, DateUtils.addDays(DATE, decalage));
      }
   }

   private void createTraces(TraceToCreate traceToCreate, String nomDestinaire,
         Date date) {

      long timestamp = timeUUIDSupport.getTimestampFromDate(date);
      UUID idTrace = timeUUIDSupport.buildUUIDFromTimestamp(timestamp);
      Date timestampTrace = timeUUIDSupport.getDateFromTimestamp(timestamp);

      if (TraceDestinataireDao.COL_REG_EXPLOIT.equalsIgnoreCase(nomDestinaire)) {
         exploitationSupport.create(new TraceRegExploitation(traceToCreate,
               null, idTrace, timestampTrace), timestampTrace.getTime());
      } else if (TraceDestinataireDao.COL_REG_SECURITE
            .equalsIgnoreCase(nomDestinaire)) {
         securiteSupport.create(new TraceRegSecurite(traceToCreate, null,
               idTrace, timestampTrace), timestampTrace.getTime());
      } else if (TraceDestinataireDao.COL_REG_TECHNIQUE
            .equalsIgnoreCase(nomDestinaire)) {
         techniqueSupport.create(new TraceRegTechnique(traceToCreate, null,
               idTrace, timestampTrace), timestampTrace.getTime());
      } else if (TraceDestinataireDao.COL_JOURN_EVT
            .equalsIgnoreCase(nomDestinaire)) {
         evtSupport.create(new TraceJournalEvt(traceToCreate, null, idTrace,
               timestampTrace), timestampTrace.getTime());
      }
   }

}
