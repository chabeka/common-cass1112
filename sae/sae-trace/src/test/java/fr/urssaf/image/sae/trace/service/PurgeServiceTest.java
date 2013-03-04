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
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
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
public class PurgeServiceTest {

   private static final Date DATE = new Date();

   private static final String CODE_EVT = "TEST_PURGE_SERVICE";

   @Autowired
   private ParametersService paramService;

   @Autowired
   private PurgeService purgeService;

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
   private TimeUUIDEtTimestampSupport timeUUIDSupport;

   @After
   public void after() throws Exception {
      serverBean.resetData();
   }

   @Test
   public void testPurgeDureeRetentionObligatoire() {

      try {
         purgeService.purgerRegistre(PurgeType.PURGE_EXPLOITATION);
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
      createParameter(ParameterType.PURGE_EXPLOIT_IS_RUNNING, Boolean.TRUE);

      try {
         purgeService.purgerRegistre(PurgeType.PURGE_EXPLOITATION);

      } catch (TraceRuntimeException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "La purge des registres d'exploitation est déjà en cours",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("une exception TraceRuntimeException est attendue");
      }

   }

   @Test
   public void testPurge() {
      createParameters();
      createTraces();

      purgeService.purgerRegistre(PurgeType.PURGE_EXPLOITATION);
      checkPurgeExploit();

      purgeService.purgerRegistre(PurgeType.PURGE_SECURITE);
      checkPurgeSecurite();

      purgeService.purgerRegistre(PurgeType.PURGE_TECHNIQUE);
      checkPurgetechnique();

      checkParameters();
   }
   
   @Test
   public void testPurgeNonLancée() {
      createParametersEquals();
      createTraces();
      
      purgeService.purgerRegistre(PurgeType.PURGE_EXPLOITATION);
      List<TraceRegExploitationIndex> traces = exploitService.lecture(DateUtils
            .addMonths(DATE, -1), DateUtils.addMinutes(DATE, 1), 10, false);
      Assert.assertEquals("les 6 traces doivent etre présentes", 6, traces.size());
      
      purgeService.purgerRegistre(PurgeType.PURGE_SECURITE);
      List<TraceRegSecuriteIndex> tracesSecu = secuService.lecture(DateUtils
            .addMonths(DATE, -1), DateUtils.addMinutes(DATE, 1), 10, false);
      Assert.assertEquals("les 6 traces doivent etre présentes", 6, tracesSecu.size());

      purgeService.purgerRegistre(PurgeType.PURGE_TECHNIQUE);
      List<TraceRegTechniqueIndex> tracesTech = techService.lecture(DateUtils
            .addMonths(DATE, -1), DateUtils.addMinutes(DATE, 1), 10, false);
      Assert.assertEquals("les 6 traces doivent etre présentes", 6, tracesTech.size());
   }
   
   private void createParametersEquals() {
      Date lastDate = DateUtils.addDays(DATE, -1);
      createParameter(ParameterType.PURGE_EXPLOIT_DATE, lastDate);
      createParameter(ParameterType.PURGE_SECU_DATE, lastDate);
      createParameter(ParameterType.PURGE_TECH_DATE, lastDate);

      createParameter(ParameterType.PURGE_EXPLOIT_DUREE, Integer.valueOf(5));
      createParameter(ParameterType.PURGE_SECU_DUREE, Integer.valueOf(5));
      createParameter(ParameterType.PURGE_TECH_DUREE, Integer.valueOf(5));
   }

   private void createParameters() {
      Date lastDate = DateUtils.addMonths(DATE, -1);
      createParameter(ParameterType.PURGE_EXPLOIT_DATE, lastDate);
      createParameter(ParameterType.PURGE_SECU_DATE, lastDate);
      createParameter(ParameterType.PURGE_TECH_DATE, lastDate);

      createParameter(ParameterType.PURGE_EXPLOIT_DUREE, Integer.valueOf(3));
      createParameter(ParameterType.PURGE_SECU_DUREE, Integer.valueOf(4));
      createParameter(ParameterType.PURGE_TECH_DUREE, Integer.valueOf(1));
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
      // trace.setTimestamp(DateUtils.addDays(DATE, decalage));
      for (String dest : destinataire.getDestinataires().keySet()) {
         createTraces(trace, dest, DateUtils.addDays(DATE, decalage));
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

   private void checkParameters() {
      checkDates();
      checkExecutions();
   }

   private void checkDates() {
      Date maxDate = DateUtils.addDays(DATE, -3);
      maxDate = DateUtils.truncate(maxDate, Calendar.DATE);
      checkDate(ParameterType.PURGE_EXPLOIT_DATE, maxDate);

      maxDate = DateUtils.addDays(DATE, -4);
      maxDate = DateUtils.truncate(maxDate, Calendar.DATE);
      checkDate(ParameterType.PURGE_SECU_DATE, maxDate);

      maxDate = DateUtils.addDays(DATE, -1);
      maxDate = DateUtils.truncate(maxDate, Calendar.DATE);
      checkDate(ParameterType.PURGE_TECH_DATE, maxDate);
   }

   private void checkDate(ParameterType parameterType, Date value) {
      try {
         Parameter param = paramService.loadParameter(parameterType);
         Assert.assertEquals("la date doit etre correcte", value, (Date) param
               .getValue());

      } catch (ParameterNotFoundException exception) {
         Assert.fail("le paramètre doit etre renseigné");
      }
   }

   private void checkExecutions() {
      checkExecution(ParameterType.PURGE_EXPLOIT_IS_RUNNING);
      checkExecution(ParameterType.PURGE_SECU_IS_RUNNING);
      checkExecution(ParameterType.PURGE_TECH_IS_RUNNING);
   }

   private void checkExecution(ParameterType parameterType) {
      try {
         Parameter param = paramService.loadParameter(parameterType);
         Assert.assertEquals("le boolean doit etre correcte", Boolean.FALSE,
               (Boolean) param.getValue());

      } catch (ParameterNotFoundException exception) {
         Assert.fail("le paramètre doit etre renseigné");
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
      }
   }
}
