/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.dao.support.HistEvenementSupport;
import fr.urssaf.image.sae.trace.model.DfceTraceSyst;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class HistEvenementServiceDatasTest {

   private static final Date DATE = new Date();
   private static final Date DATE_INF = DateUtils.addHours(DATE, -1);
   private static final Date DATE_SUP = DateUtils.addHours(DATE, 1);
   private static final Date DATE_JOUR_SUIVANT = DateUtils.addDays(DATE, 1);
   private static final Date DATE_JOUR_PRECEDENT = DateUtils.addDays(DATE, -1);

   private static final String VALUE = "valeur";
   private static final String KEY = "clé";

   private static final String KEY_UUID = "UUID";

   private static final String USERNAME = "_ADMIN";
   private static final String LOGIN = "LE LOGIN";
   private static final String CONTRAT = "contrat de service";
   private static final String CODE_EVT = "code événement";
   private static final String ACTION = "action";
   private static final Map<String, Object> INFOS;
   static {
      INFOS = new HashMap<String, Object>();
      INFOS.put(KEY, VALUE);
   }

   @Autowired
   private HistEvenementService service;

   @Autowired
   private HistEvenementSupport support;

   @Test
   public void testAucunRetourBorneInferieure() {
      final Date startDate = new Date();

      createTrace(UUID.randomUUID(), "");
      final Date endDate = new Date();

      // on fixe les bornes inférieure à la première trace de la journée
      final Date dateStart = DateUtils.addDays(startDate, 1);
      final Date dateFin = DateUtils.addDays(endDate, 2);

      final List<DfceTraceSyst> result = service
            .lecture(dateStart, dateFin, 10, true);
      Assert.assertNull("il ne doit y avoir aucun résultat", result);
   }

   @Test
   public void testRetourUnSeulElementLimite() {

     // Date de début : date courante moins 30 secondes
     final Calendar calendar = Calendar.getInstance();
     final long t = calendar.getTimeInMillis();
     final Date dateDebut = new Date(t - 30000);

     // Création d'une trace
      final UUID uuid = UUID.randomUUID();
      createTrace(uuid, "");
      
      Date dateFin = new Date();
      dateFin = DateUtils.addDays(dateFin, 1);
      dateFin = DateUtils.truncate(dateFin, Calendar.DATE);

      final List<DfceTraceSyst> result = service.lecture(dateDebut, dateFin, 100, true);

      Assert.assertNotNull("il doit y avoir un résultat", result);

      boolean traceOK = false;
      for (final DfceTraceSyst dfceTraceSys : result) {
         final List<String> arrays = Arrays.asList(dfceTraceSys.getTypeEvt().split(";"));
         final String stringToCompare = KEY_UUID + ":" + uuid;
         if (arrays.contains(stringToCompare)) {
            traceOK = true;
         }
      }

      Assert.assertEquals("La trace insérée doit être trouvée", true, traceOK);
   }

   /*
   @Test
   public void testRetour3ElementsMemeJour() {
      createTraces();

      Date dateStart = DATE_INF;
      Date dateEnd = DATE_SUP;

      List<DfceTraceSyst> result = service.lecture(dateStart, dateEnd, 3, true);
      Assert.assertNotNull("il doit y avoir un résultat", result);
      Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
            3, result.size());

   }

   private void createTraces() {
      createTrace(DATE, " [DATE]");
      createTrace(DATE_INF, " [DATE_INF]");
      createTrace(DATE_SUP, " [DATE_SUP]");
      createTrace(DATE_JOUR_SUIVANT, " [DATE_JOUR_SUIVANT]");
      createTrace(DATE_JOUR_PRECEDENT, " [DATE_JOUR_PRECEDENT]");
   }
    */
   private void createTrace(final UUID uuid, final String suffixe) {
      INFOS.put(KEY_UUID, uuid);
      final TraceToCreate trace = new TraceToCreate();
      trace.setAction(ACTION + suffixe);
      trace.setCodeEvt(CODE_EVT + suffixe);
      trace.setContrat(CONTRAT + suffixe);
      trace.setInfos(INFOS);

      support.create(trace);
   }
}
