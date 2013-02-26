/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class JournalisationServiceDatasTest {

   private static final Date DATE = new Date();
   private static final String VALUE = "valeur";
   private static final String KEY = "clé";

   private static final String LOGIN = "LE LOGIN";
   private static final String CONTRAT = "contrat de service";
   private static final String CODE_EVT = "code événement";
   private static final String CONTEXTE = "contexte";
   private static final Map<String, Object> INFOS;
   static {
      INFOS = new HashMap<String, Object>();
      INFOS.put(KEY, VALUE);
   }

   @Autowired
   private ParametersService parametersService;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private TraceJournalEvtSupport support;

   @Autowired
   private JournalisationService service;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testRecup0Dates() {

      Date date = new Date();
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_DATE, DateUtils.addDays(date, -5));
      parametersService.saveParameter(parameter);

      List<Date> dates = service
            .recupererDates(JournalisationType.JOURNALISATION_EVT);
      Assert.assertEquals("le nombre d'éléments doit etre correct", 0, dates
            .size());

   }

   @Test
   public void testRecup5Dates() {

      createTraces();

      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_DATE, DateUtils.addDays(DATE, -5));
      parametersService.saveParameter(parameter);

      List<Date> dates = service
            .recupererDates(JournalisationType.JOURNALISATION_EVT);
      Assert.assertEquals("le nombre d'éléments doit etre correct", 5, dates
            .size());

      for (int i = 1; i < 6; i++) {
         Assert.assertTrue("la date j-" + i + "doit etre contenue", dates
               .contains(DateUtils.truncate(DateUtils.addDays(DATE, -i),
                     Calendar.DATE)));
      }

   }

   private void createTraces() {
      createTrace(DATE, " [DATE]");
      createTrace(DateUtils.addDays(DATE, -1), " [DATE_1]");
      createTrace(DateUtils.addDays(DATE, -2), " [DATE_2]");
      createTrace(DateUtils.addDays(DATE, -3), " [DATE_3]");
      createTrace(DateUtils.addDays(DATE, -4), " [DATE_4]");
      createTrace(DateUtils.addDays(DATE, -5), " [DATE_5]");
   }

   private void createTrace(Date date, String suffixe) {
      TraceJournalEvt trace = new TraceJournalEvt();
      trace.setContexte(CONTEXTE + suffixe);
      trace.setCodeEvt(CODE_EVT + suffixe);
      trace.setCs(CONTRAT + suffixe);
      trace.setId(TimeUUIDUtils.getTimeUUID(date.getTime()));
      trace.setLogin(LOGIN + suffixe);
      trace.setTimestamp(date);
      trace.setInfos(INFOS);

      support.create(trace, new Date().getTime());
   }

   // FIXME - FBON - Tester l'export
}