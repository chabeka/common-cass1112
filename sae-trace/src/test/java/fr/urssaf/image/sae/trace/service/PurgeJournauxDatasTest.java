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
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
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
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
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

      paramService.setPurgeEvtIsRunning(Boolean.TRUE);

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

   /**
    * <b><u>Explication du test</u></b><br>
    * <br>
    * Soit DATE = 07/05/2013 11h30<br>
    * <br>
    * On créé 10 traces dans le journal des événements SAE (notamment) à :<br>
    * <table border="1">
    * <tr>
    * <td>DATE</td>
    * <td>07/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -1 jour</td>
    * <td>06/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -2 jours</td>
    * <td>05/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -3 jours</td>
    * <td>04/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -4 jours</td>
    * <td>03/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -5 jours</td>
    * <td>02/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -6 jours</td>
    * <td>01/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -7 jours</td>
    * <td>30/04/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -8 jours</td>
    * <td>29/04/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -9 jours</td>
    * <td>28/04/2013 11h30</td>
    * </tr>
    * </table>
    * <br>
    * On indique dans les paramètres :<br>
    * <ul>
    * <li>que les traces antérieures ou "égales" à DATE - 10 jours (27/04/2013
    * dans l'exemple) ont été purgées</li>
    * <li>que les traces antérieures ou "égales" à DATE - 10 jours (27/04/2013
    * dans l'exemple) ont été journalisées</li>
    * <li>que la durée de rétention des traces avant purge dans le journal des
    * événements est de 5 jours</li>
    * </ul>
    * La purge va donc commencer dans l'exemple par la journée du 28/04/2013.<br>
    * Or aucune des 10 traces à partir du 28/04 n'a été journalisée.<br>
    * <br>
    * <b>On doit donc retrouver, après la purge, l'ensemble des 10 traces.</b>
    * 
    */
   @Test
   public void testPurgeJournalMemeDateJournalisation() {

      Date date = DateUtils.addDays(DATE, -10);

      paramService.setJournalisationEvtDate(date);
      paramService.setPurgeEvtDate(date);
      paramService.setPurgeEvtDuree(5);

      createTraces();

      purgeService.purgerJournal(PurgeType.PURGE_EVT);

      List<TraceJournalEvtIndex> traces = evtService.lecture(date, DateUtils
            .addDays(DATE, 10), 20, false);

      Assert.assertEquals("toutes les traces doivent etre présentes", 10,
            traces.size());
   }

   /**
    * <b><u>Explication du test</u></b><br>
    * <br>
    * Soit DATE = 07/05/2013 11h30<br>
    * <br>
    * On créé 10 traces dans le journal des événements SAE (notamment) à :<br>
    * <table border="1">
    * <tr>
    * <td>DATE</td>
    * <td>07/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -1 jour</td>
    * <td>06/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -2 jours</td>
    * <td>05/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -3 jours</td>
    * <td>04/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -4 jours</td>
    * <td>03/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -5 jours</td>
    * <td>02/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -6 jours</td>
    * <td>01/05/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -7 jours</td>
    * <td>30/04/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -8 jours</td>
    * <td>29/04/2013 11h30</td>
    * </tr>
    * <tr>
    * <td>DATE -9 jours</td>
    * <td>28/04/2013 11h30</td>
    * </tr>
    * </table>
    * <br>
    * On indique dans les paramètres :<br>
    * <ul>
    * <li>que les traces antérieures ou "égales" à DATE - 10 jours (27/04/2013
    * dans l'exemple) ont été purgées</li>
    * <li>que les traces antérieures ou "égales" à DATE - 6 jours (01/05/2013
    * dans l'exemple) ont été journalisées</li>
    * <li>que la durée de rétention des traces avant purge dans le journal des
    * événements est de 5 jours</li>
    * </ul>
    * La purge va donc commencer dans l'exemple par la journée du 28/04/2013.<br>
    * Les traces du 28/04, 29/04, 30/04 et 01/05 ont été journalisées selon les
    * paramètres.<br>
    * Elles seront donc purgées. <br>
    * <b>On doit donc retrouver, après la purge, 6 traces.</b>
    * 
    */
   @Test
   public void testPurgeJournalPosterieureJournalisation()
         throws ParameterNotFoundException {

      Date date = DateUtils.addDays(DATE, -10);
      paramService.setPurgeEvtDate(date);

      date = DateUtils.addDays(DATE, -6);
      paramService.setJournalisationEvtDate(date);

      paramService.setPurgeEvtDuree(5);
      createTraces();

      purgeService.purgerJournal(PurgeType.PURGE_EVT);

      date = DateUtils.addDays(DATE, -10);
      List<TraceJournalEvtIndex> traces = evtService.lecture(date, DateUtils
            .addDays(DATE, 10), 20, false);
      Assert.assertEquals("toutes les traces doivent etre présentes", 6, traces
            .size());

      Date dateAttendue = DateUtils.addDays(DATE, -6);
      dateAttendue = DateUtils.truncate(dateAttendue, Calendar.DATE);

      Date dateObtenue = paramService.getPurgeEvtDate();

      Assert.assertEquals("la date stockée doit etre correcte", dateAttendue,
            dateObtenue);
   }

   @Test
   public void testPurgeJournalAntérieureJournalisation()
         throws ParameterNotFoundException {

      Date date = DateUtils.addDays(DATE, -10);
      paramService.setPurgeEvtDate(date);

      date = DateUtils.addDays(DATE, -1);
      paramService.setJournalisationEvtDate(date);

      paramService.setPurgeEvtDuree(5);
      createTraces();

      purgeService.purgerJournal(PurgeType.PURGE_EVT);

      date = DateUtils.addDays(DATE, -10);
      List<TraceJournalEvtIndex> traces = evtService.lecture(date, DateUtils
            .addDays(DATE, 10), 20, false);
      Assert.assertEquals("toutes les traces doivent etre présentes", 5, traces
            .size());

      Date dateAttendue = DateUtils.addDays(DATE, -5);
      dateAttendue = DateUtils.truncate(dateAttendue, Calendar.DATE);

      Date dateObtenue = paramService.getPurgeEvtDate();

      Assert.assertEquals("la date stockée doit etre correcte", dateAttendue,
            dateObtenue);
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
