package fr.urssaf.image.sae.trace.executable.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.exception.ExceptionUtils;
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
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegExploitationSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.RegExploitationService;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.service.StatusService;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-executable-test.xml" })
public class TraitementServiceDatasTest {

   private static final Date DATE = new Date();

   private static final String CODE_EVT = "TEST_PURGE_SERVICE";

   @Autowired
   private ParametersService paramService;

   @Autowired
   private StatusService statusService;

   @Autowired
   private TraitementService traitementService;

   @Autowired
   private TraceDestinataireSupport traceSupport;

   @Autowired
   private RegExploitationService exploitService;

   @Autowired
   private RegSecuriteService secuService;

   @Autowired
   private RegTechniqueService techService;

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
         traitementService.purger(PurgeType.PURGE_EXPLOITATION);
      } catch (TraceRuntimeException exception) {
         Assert.assertEquals("l'exception mère doit avoir le bon type",
               ParameterNotFoundException.class, exception.getCause()
                     .getClass());
      } catch (Exception exception) {
         Assert
               .fail("Une exception TraceRuntimeException est attendue, alors qu'on a obtenue l'exception : \r\n"
                     + ExceptionUtils.getFullStackTrace(exception));
      }
   }

   @Test
   public void testPurgeDejaEnCours() {

      statusService.updatePurgeStatus(PurgeType.PURGE_EXPLOITATION,
            Boolean.TRUE);

      try {

         traitementService.purger(PurgeType.PURGE_EXPLOITATION);

      } catch (TraceRuntimeException exception) {

         Assert.assertEquals("le message d'erreur doit etre correct",
               "La purge des registres d'exploitation est déjà en cours",
               exception.getMessage());

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceRuntimeException est attendue, alors qu'on a obtenue l'exception : \r\n"
                     + ExceptionUtils.getFullStackTrace(exception));
      }

   }

   @Test
   public void testPurge() throws ParameterNotFoundException {
      createParameters();
      createTraces();

      traitementService.purger(PurgeType.PURGE_EXPLOITATION);
      checkPurgeExploit();

      traitementService.purger(PurgeType.PURGE_SECURITE);
      checkPurgeSecurite();

      traitementService.purger(PurgeType.PURGE_TECHNIQUE);
      checkPurgetechnique();

      checkParameters();
   }

   private void createParameters() {

      Date lastDate = DateUtils.addMonths(DATE, -1);

      paramService.setPurgeExploitDate(lastDate);
      paramService.setPurgeSecuDate(lastDate);
      paramService.setPurgeTechDate(lastDate);

      paramService.setPurgeExploitDuree(3);
      paramService.setPurgeSecuDuree(4);
      paramService.setPurgeTechDuree(1);

   }

   private void createTraces() {
      TraceDestinataire destinataire = new TraceDestinataire();
      destinataire.setCodeEvt(CODE_EVT);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_REG_EXPLOIT, null);
      map.put(TraceDestinataireDao.COL_REG_SECURITE, null);
      map.put(TraceDestinataireDao.COL_REG_TECHNIQUE, null);
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

   private void checkPurgeExploit() {
      List<TraceRegExploitationIndex> traces = exploitService.lecture(DateUtils
            .addMonths(DATE, -1), DateUtils.addMinutes(DATE, 1), 10, false);
      Assert.assertNotNull("il doit y avoir des traces restantes", traces);

      Assert.assertEquals("le nombre de traces doit etre correct", 3, traces
            .size());
      Assert.assertTrue("le plus ancien doit avoir le bon nom : "
            + "[JOUR J-2]", traces.get(0).getAction().contains("[JOUR J-2]"));
   }

   private void checkPurgeSecurite() {
      List<TraceRegSecuriteIndex> traces = secuService.lecture(DateUtils
            .addMonths(DATE, -1), DateUtils.addMinutes(DATE, 1), 10, false);
      Assert.assertNotNull("il doit y avoir des traces restantes", traces);

      Assert.assertEquals("le nombre de traces doit etre correct", 4, traces
            .size());
      Assert.assertTrue("le plus ancien doit avoir le bon nom : "
            + "[JOUR J-2]", traces.get(0).getContexte().contains("[JOUR J-3]"));
   }

   private void checkPurgetechnique() {
      List<TraceRegTechniqueIndex> traces = techService.lecture(DateUtils
            .addMonths(DATE, -1), DateUtils.addMinutes(DATE, 1), 10, false);
      Assert.assertNotNull("il doit y avoir des traces restantes", traces);

      Assert.assertEquals("le nombre de traces doit etre correct", 1, traces
            .size());
      Assert.assertTrue("le plus ancien doit avoir le bon nom : "
            + "[JOUR J-2]", traces.get(0).getContexte().contains("[JOUR J]"));
   }

   private void checkParameters() throws ParameterNotFoundException {
      checkDates();
      checkExecutions();
   }

   private void checkDates() throws ParameterNotFoundException {

      Date maxDate = DateUtils.addDays(DATE, -3);
      maxDate = DateUtils.truncate(maxDate, Calendar.DATE);
      Assert.assertEquals("la date doit etre correcte", maxDate, paramService
            .getPurgeExploitDate());

      maxDate = DateUtils.addDays(DATE, -4);
      maxDate = DateUtils.truncate(maxDate, Calendar.DATE);
      Assert.assertEquals("la date doit etre correcte", maxDate, paramService
            .getPurgeSecuDate());

      maxDate = DateUtils.addDays(DATE, -1);
      maxDate = DateUtils.truncate(maxDate, Calendar.DATE);
      Assert.assertEquals("la date doit etre correcte", maxDate, paramService
            .getPurgeTechDate());

   }

   private void checkExecutions() {

      Assert.assertEquals("le boolean doit etre correcte", Boolean.FALSE,
            statusService.isPurgeRunning(PurgeType.PURGE_EXPLOITATION));

      Assert.assertEquals("le boolean doit etre correcte", Boolean.FALSE,
            statusService.isPurgeRunning(PurgeType.PURGE_SECURITE));

      Assert.assertEquals("le boolean doit etre correcte", Boolean.FALSE,
            statusService.isPurgeRunning(PurgeType.PURGE_TECHNIQUE));

   }

}
