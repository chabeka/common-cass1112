/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.docubase.toolkit.model.recordmanager.RMSystemEvent;

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
      createTraces();
      Date endDate = new Date();

      // on fixe les bornes inférieure à la première trace de la journée
      Date dateStart = DateUtils.addDays(startDate, 1);
      Date dateFin = DateUtils.addDays(endDate, 2);

      List<RMSystemEvent> result = service
            .lecture(dateStart, dateFin, 10, true);
      Assert.assertNull("il ne doit y avoir aucun résultat", result);
   }

   @Test
   public void testRetourUnSeulElementLimite() {
      Date dateDebut = new Date();
      createTraces();
      Date dateFin = new Date();

      List<RMSystemEvent> result = service.lecture(dateDebut, dateFin, 1, true);

      Assert.assertNotNull("il doit y avoir un résultat");
      Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
            1, result.size());
   }

   @Test
   public void testRetour3ElementsMemeJour() {
      createTraces();

      Date dateStart = DATE_INF;
      Date dateEnd = DATE_SUP;

      List<RMSystemEvent> result = service.lecture(dateStart, dateEnd, 3, true);
      Assert.assertNotNull("il doit y avoir un résultat");
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

   private void createTrace(Date date, String suffixe) {

      TraceToCreate trace = new TraceToCreate();
      trace.setAction(ACTION + suffixe);
      trace.setCodeEvt(CODE_EVT + suffixe);
      trace.setContrat(CONTRAT + suffixe);
      trace.setLogin(LOGIN + suffixe);
      trace.setTimestamp(date);
      trace.setInfos(INFOS);

      support.create(trace);
   }
}
