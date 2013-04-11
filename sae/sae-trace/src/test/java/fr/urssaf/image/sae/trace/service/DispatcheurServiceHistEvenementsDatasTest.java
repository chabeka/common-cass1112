/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.model.DfceTraceSyst;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class DispatcheurServiceHistEvenementsDatasTest {

   private static final String ACTION = "action";
   private static final String CONTRAT_DE_SERVICE = "contrat de service";
   private static final String IP = "ip";
   private static final String IP_VALUE = "127.0.0.1";
   private static final String MESSAGE = "message";
   private static final String MESSAGE_VALUE = "le message est ici";
   private static final String VI = "vi";
   private static final String VI_VALUE = "<vi><valeur>La valeur du vi</valeur></vi>";
   private static final Map<String, Object> INFOS = new HashMap<String, Object>();
   static {
      INFOS.put(IP, IP_VALUE);
      INFOS.put(MESSAGE, MESSAGE_VALUE);
      INFOS.put(VI, VI_VALUE);
   }

   private static final String ARCHIVAGE_UNITAIRE = "ARCHIVAGE_UNITAIRE_EVT";

   @Autowired
   private DispatcheurService service;

   @Autowired
   private HistEvenementService histService;

   @Autowired
   private TraceDestinataireSupport destSupport;

   @Autowired
   private ServiceProviderSupport support;

   @After
   public void after() throws Exception {
      support.disconnect();
   }

   @Before
   public void init() {
      support.connect();
   }

   @Test
   public void testCreationHistoriqueEvtSucces() {
      Date startDate = new Date();
      createDestinataireExploitation();

      TraceToCreate traceToCreate = new TraceToCreate();
      traceToCreate.setCodeEvt(ARCHIVAGE_UNITAIRE);
      traceToCreate.setAction(ACTION);
      traceToCreate.setContrat(CONTRAT_DE_SERVICE);
      traceToCreate.setInfos(INFOS);

      service.ajouterTrace(traceToCreate);
      
      // Ajout des 5 min car décalage entre les postes et le serveur...
      startDate = DateUtils.addMinutes(startDate, -5);
      Date endDate = DateUtils.addMinutes(startDate, 5);

      List<DfceTraceSyst> events = histService.lecture(startDate, endDate, 1,
            false);
      Assert.assertNotNull("on doit avoir des événements", events);
      Assert.assertEquals("on doit avoir un nombre correct d'événements", 1,
            events.size());

   }

   private void createDestinataireExploitation() {
      TraceDestinataire trace = new TraceDestinataire();
      trace.setCodeEvt(ARCHIVAGE_UNITAIRE);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_HIST_EVT, null);
      trace.setDestinataires(map);
      destSupport.create(trace, new Date().getTime());
   }
}
