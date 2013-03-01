/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Calendar;
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
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegExploitationSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class PurgeJournauxDatasTest {

   private static final Date DATE = new Date();

   private static final String CODE_EVT = "TEST_PURGE_SERVICE_JOURNAUX";

   @Autowired
   private ParametersService paramService;

   @Autowired
   private PurgeService purgeService;

   @Autowired
   private TraceDestinataireSupport traceSupport;

   @Autowired
   private JournalEvtService evtService;

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
   public void testPurgeDureeRetentionObligatoire() {

      try {
         purgeService.purgerJournal(PurgeType.PURGE_EVT);
      } catch (TraceRuntimeException exception) {
         Assert.assertEquals("l'exception mère doit avoir le bon type",
               ParameterNotFoundException.class, exception.getCause()
                     .getClass());
      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }
   }

   @Test
   public void testPurgeDejaEnCours() {
      createParameter(ParameterType.PURGE_EVT_IS_RUNNING, Boolean.TRUE);

      try {
         purgeService.purgerJournal(PurgeType.PURGE_EVT);

      } catch (TraceRuntimeException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "La purge du journal des événements est déjà en cours",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }

   }

   @Test
   public void testPurgeJournalMemeDateJournalisation() {

      Date date = DateUtils.addDays(DATE, -10);

      createParameter(ParameterType.JOURNALISATION_EVT_DATE, date);
      createParameter(ParameterType.PURGE_EVT_DATE, date);
      createParameter(ParameterType.PURGE_EVT_DUREE, 5);
      createTraces();

      purgeService.purgerJournal(PurgeType.PURGE_EVT);

      List<TraceJournalEvtIndex> traces = evtService.lecture(date, DateUtils
            .addDays(DATE, 10), 20, false);
      Assert.assertEquals("toutes les traces doivent etre présentes", 10,
            traces.size());
   }

   @Test
   public void testPurgeJournalPosterieureJournalisation()
         throws ParameterNotFoundException {

      Date date = DateUtils.addDays(DATE, -10);
      createParameter(ParameterType.PURGE_EVT_DATE, date);

      date = DateUtils.addDays(DATE, -6);
      createParameter(ParameterType.JOURNALISATION_EVT_DATE, date);

      createParameter(ParameterType.PURGE_EVT_DUREE, 5);
      createTraces();

      purgeService.purgerJournal(PurgeType.PURGE_EVT);

      date = DateUtils.addDays(DATE, -10);
      List<TraceJournalEvtIndex> traces = evtService.lecture(date, DateUtils
            .addDays(DATE, 10), 20, false);
      Assert.assertEquals("toutes les traces doivent etre présentes", 6, traces
            .size());

      date = DateUtils.addDays(DATE, -6);
      Assert.assertEquals("la date stockée doit etre correcte", DateUtils
            .truncate(date, Calendar.DATE), paramService.loadParameter(
            ParameterType.PURGE_EVT_DATE).getValue());
   }

   @Test
   public void testPurgeJournalAntérieureJournalisation()
         throws ParameterNotFoundException {

      Date date = DateUtils.addDays(DATE, -10);
      createParameter(ParameterType.PURGE_EVT_DATE, date);

      date = DateUtils.addDays(DATE, -1);
      createParameter(ParameterType.JOURNALISATION_EVT_DATE, date);

      createParameter(ParameterType.PURGE_EVT_DUREE, 5);
      createTraces();

      purgeService.purgerJournal(PurgeType.PURGE_EVT);

      date = DateUtils.addDays(DATE, -10);
      List<TraceJournalEvtIndex> traces = evtService.lecture(date, DateUtils
            .addDays(DATE, 10), 20, false);
      Assert.assertEquals("toutes les traces doivent etre présentes", 5, traces
            .size());

      date = DateUtils.addDays(DATE, -5);
      Assert.assertEquals("la date stockée doit etre correcte", DateUtils
            .truncate(date, Calendar.DATE), paramService.loadParameter(
            ParameterType.PURGE_EVT_DATE).getValue());
   }

   private void createParameter(ParameterType type, Object value) {
      Parameter parameter = new Parameter(type, value);
      paramService.saveParameter(parameter);
   }

   private void createTraces() {
      TraceDestinataire destinataire = new TraceDestinataire();
      destinataire.setCodeEvt(CODE_EVT);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_REG_EXPLOIT, null);
      map.put(TraceDestinataireDao.COL_REG_SECURITE, null);
      map.put(TraceDestinataireDao.COL_REG_TECHNIQUE, null);
      map.put(TraceDestinataireDao.COL_JOURN_EVT, null);
      destinataire.setDestinataires(map);

      traceSupport.create(destinataire, new Date().getTime());

      createTrace("[JOUR J]", 0, destinataire);
      createTrace("[JOUR J-1]", -1, destinataire);
      createTrace("[JOUR J-2]", -2, destinataire);
      createTrace("[JOUR J-3]", -3, destinataire);
      createTrace("[JOUR J-4]", -4, destinataire);
      createTrace("[JOUR J-5]", -5, destinataire);
      createTrace("[JOUR J-6]", -6, destinataire);
      createTrace("[JOUR J-7]", -7, destinataire);
      createTrace("[JOUR J-8]", -8, destinataire);
      createTrace("[JOUR J-9]", -9, destinataire);

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
      // trace.setTimestamp(DateUtils.addDays(DATE, decalage));

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
