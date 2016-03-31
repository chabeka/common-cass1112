package fr.urssaf.image.sae.trace.executable.support;

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
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
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
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableRuntimeException;
import fr.urssaf.image.sae.trace.executable.service.TraitementService;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.StatusService;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-executable-test.xml" })
public class JournalisationSupportTest {

   private static final String CODE_EVT = "TEST_JOURNALISATION_SUPPORT";

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

   @After
   public void after() throws Exception {
      serverBean.resetData();
   }

   @Test
   public void testJournalisationDejaEnCours() {

      statusService.updateJournalisationStatus(
            JournalisationType.JOURNALISATION_EVT, Boolean.TRUE);

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
