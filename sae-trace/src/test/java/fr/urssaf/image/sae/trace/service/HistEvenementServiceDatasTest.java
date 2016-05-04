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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.dao.support.HistEvenementSupport;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;
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
   private ServiceProviderSupport serviceSupport;

   @Autowired
   private HistEvenementSupport support;

   @Before
   public void before() {
      serviceSupport.connect();
   }

   @After
   public void after() throws Exception {
      serviceSupport.disconnect();
   }

   @Test
   public void testAucunRetourBorneInferieure() {
      Date startDate = new Date();
      
      createTrace(UUID.randomUUID(), "");
      Date endDate = new Date();

      // on fixe les bornes inférieure à la première trace de la journée
      Date dateStart = DateUtils.addDays(startDate, 1);
      Date dateFin = DateUtils.addDays(endDate, 2);

      List<DfceTraceSyst> result = service
            .lecture(dateStart, dateFin, 10, true);
      Assert.assertNull("il ne doit y avoir aucun résultat", result);
   }

   @Test
   public void testRetourUnSeulElementLimite() {
      Date dateDebut = new Date();
      UUID uuid = UUID.randomUUID();
      createTrace(uuid, "");
      Date dateFin = new Date();

      dateDebut = DateUtils.truncate(dateDebut, Calendar.DATE);
      dateFin = DateUtils.addDays(dateFin, 1);
      dateFin = DateUtils.truncate(dateFin, Calendar.DATE);
      
      List<DfceTraceSyst> result = service.lecture(dateDebut, dateFin, 10, true);

      Assert.assertNotNull("il doit y avoir un résultat", result);
      boolean traceOK = false;
      for (DfceTraceSyst dfceTraceSys : result) {
         if (dfceTraceSys.getLogin() != null) {
            if (dfceTraceSys.getLogin().equals(uuid.toString())) {
               traceOK = true;
            }
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
   private void createTrace(UUID uuid, String suffixe) {

      TraceToCreate trace = new TraceToCreate();
      trace.setAction(ACTION + suffixe);
      trace.setCodeEvt(CODE_EVT + suffixe);
      trace.setContrat(CONTRAT + suffixe);
      trace.setInfos(INFOS);
      trace.setLogin(uuid.toString());

      support.create(trace);
   }
}
