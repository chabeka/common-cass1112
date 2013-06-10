package fr.urssaf.image.sae.trace.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

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

   @Autowired
   private TimeUUIDEtTimestampSupport timeUUIDSupport;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testRecup0Dates() {

      Date laDate = DateUtils.addDays(new Date(), -5);

      parametersService.setJournalisationEvtDate(laDate);

      List<Date> dates = service
            .recupererDates(JournalisationType.JOURNALISATION_EVT);

      Assert.assertEquals("le nombre d'éléments doit etre correct", 0, dates
            .size());

   }

   @Test
   public void testRecup5Dates() {

      createTraces();

      Date laDate = DateUtils.addDays(DATE, -5);

      parametersService.setJournalisationEvtDate(laDate);

      List<Date> dates = service
            .recupererDates(JournalisationType.JOURNALISATION_EVT);
      Assert.assertEquals("le nombre d'éléments doit etre correct", 4, dates
            .size());

      for (int i = 1; i < 5; i++) {
         Assert.assertTrue("la date j-" + i + "doit etre contenue", dates
               .contains(DateUtils.truncate(DateUtils.addDays(DATE, -i),
                     Calendar.DATE)));
      }

   }

   @Test
   public void testExport() throws IOException {

      parametersService
            .setJournalisationEvtIdJournPrec("6f5e4930-80cc-11e2-8759-005056c00008");

      parametersService.setJournalisationEvtHashJournPrec("00000");

      Calendar calendar = new GregorianCalendar();
      calendar.set(Calendar.YEAR, 2013);
      calendar.set(Calendar.MONTH, 3);
      calendar.set(Calendar.DATE, 15);
      calendar.set(Calendar.AM_PM, Calendar.AM);
      calendar.set(Calendar.HOUR, 11);
      calendar.set(Calendar.MINUTE, 45);
      calendar.set(Calendar.SECOND, 50);

      createTrace(calendar.getTime(), " [DATE]", UUID
            .fromString("1b163270-80bd-11e2-8148-005056c00008"));

      String dirPath = System.getProperty("java.io.tmpdir");
      File tempDirectory = new File(dirPath);
      File repertoire = new File(tempDirectory, "export");
      repertoire.mkdir();

      String path = service.exporterTraces(
            JournalisationType.JOURNALISATION_EVT,
            repertoire.getAbsolutePath(), calendar.getTime());

      String sha1Attendu = "61d37fcf05a3ea89ad02653a412e5147d8b8b9d6";
      String sha1Obtenu = calculeSha1(new File(path));

      FileUtils.deleteDirectory(repertoire);

      Assert.assertEquals(
            "le sha1 doit etre correct : le fichier doit etre correct",
            sha1Attendu, sha1Obtenu);

   }

   private String calculeSha1(File file) throws IOException {

      FileInputStream fis = new FileInputStream(file);
      try {

         return DigestUtils.shaHex(fis);

      } finally {
         if (fis != null) {
            fis.close();
         }
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
      TraceJournalEvt trace = new TraceJournalEvt(timeUUIDSupport
            .buildUUIDFromDate(date), date);
      trace.setContexte(CONTEXTE + suffixe);
      trace.setCodeEvt(CODE_EVT + suffixe);
      trace.setContratService(CONTRAT + suffixe);
      trace.setLogin(LOGIN + suffixe);
      trace.setInfos(INFOS);

      support.create(trace, new Date().getTime());
   }

   private void createTrace(Date date, String suffixe, UUID id) {
      TraceJournalEvt trace = new TraceJournalEvt(id, date);
      trace.setContexte(CONTEXTE + suffixe);
      trace.setCodeEvt(CODE_EVT + suffixe);
      trace.setContratService(CONTRAT + suffixe);
      trace.setLogin(LOGIN + suffixe);
      trace.setInfos(INFOS);
      trace.setPagms(Arrays.asList("PAGM " + suffixe));

      support.create(trace, new Date().getTime());
   }
}