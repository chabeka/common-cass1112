/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

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

import fr.urssaf.image.sae.trace.dao.support.CycleVieSupport;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class CycleVieServiceDatasTest {

   private static final String VALUE = "valeur";
   private static final String KEY = "clé";

   private static final String CONTRAT = "contrat de service";
   private static final String CODE_EVT = "code événement";
   private static final String ACTION = "action";
   private static final Map<String, Object> INFOS;
   static {
      INFOS = new HashMap<String, Object>();
      INFOS.put(KEY, VALUE);
   }

   @Autowired
   private CycleVieService service;

   @Autowired
   private ServiceProviderSupport serviceSupport;

   @Autowired
   private CycleVieSupport support;

   @Before
   public void before() {
      serviceSupport.connect();
   }

   @After
   public void after() throws Exception {
      serviceSupport.disconnect();
   }

   @Test
   public void testCreation() {
      Date startDate = new Date();
   
      UUID uuid = UUID.randomUUID();
      createTrace(uuid);
      Date endDate = new Date();

      // on fixe les bornes inférieure à la première trace de la journée
      Date dateStart = DateUtils.addMinutes(startDate, -5);
      Date dateFin = DateUtils.addMinutes(endDate, 5);

      List<DfceTraceDoc> result = service
            .lecture(dateStart, dateFin, 10, true);
      
      Assert.assertNotNull("il doit y avoir un résultat", result);
      
      boolean traceOK = false;
      for (DfceTraceDoc dfceTraceDoc : result) {
         if (dfceTraceDoc.getLogin() != null) {
            if (dfceTraceDoc.getLogin().equals(uuid.toString())) {
               traceOK = true;
            }
         }
      }
      
      Assert.assertEquals("La trace insérée doit être trouvée", true, traceOK);
   }



   private void createTrace(UUID uuid) {

      TraceToCreate trace = new TraceToCreate();
      trace.setAction(ACTION);
      trace.setCodeEvt(CODE_EVT);
      trace.setContrat(CONTRAT);
      trace.setLogin(uuid.toString());
      trace.setInfos(INFOS);

      support.create(trace);
   }
}
