/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class CycleVieSupportTest {

   private static final int MAX_COUNT = 100;

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
   private CycleVieSupport support;

   @Autowired
   private ServiceProviderSupport provider;

   @After
   public void after() throws Exception {
      provider.disconnect();
   }

   @Before
   public void before() {
      provider.connect();
   }

   @Test
   public void testCreateFindSuccess() {

      Date startDate = new Date();
      createTrace();
      Date endDate = new Date();

      Date deb = DateUtils.truncate(startDate, Calendar.DATE);
      Date fin = DateUtils.truncate(DateUtils.addDays(endDate, 1),
            Calendar.DATE);

      List<DfceTraceDoc> values = support.findByDates(deb, fin, MAX_COUNT,
            true);

      boolean found = false;
      int index = 0;
      while (!found && index < values.size()) {
         if (StringUtils.equals(values.get(index).getLogin(), LOGIN)) {
            found = true;
         }
         index++;
      }

      Assert.assertTrue("l'historique doit contenir la trace", found);
   }

   private void createTrace() {
      TraceToCreate trace = new TraceToCreate();
      trace.setAction(ACTION);
      trace.setCodeEvt(CODE_EVT);
      trace.setContrat(CONTRAT);
      trace.setLogin(LOGIN);
      trace.setInfos(INFOS);

      support.create(trace);
   }
}
