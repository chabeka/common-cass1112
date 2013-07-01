package fr.urssaf.image.sae.rnd.service;

import java.util.Map;
import java.util.TreeMap;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.RndRecuperationException;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.ws.adrn.service.RndRecuperationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class MajCorrespondancesServiceTest {

   @Autowired
   private MajRndService majCorrespondancesService;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Autowired
   private SaeBddSupport saeBddSupport;

   @Autowired
   private RndRecuperationService rndRecuperationService;

   @Before
   public void before() throws SaeBddRuntimeException, RndRecuperationException {

      Map<String, String> listeCorrespondances = new TreeMap<String, String>();
      listeCorrespondances.put("1.1.1.1.1", "2.2.2.2.2");
      saeBddSupport.updateCorrespondances(listeCorrespondances, "11.4");
      
   }

   @After
   public void after() throws Exception {
      EasyMock.reset(rndRecuperationService);
      server.resetData();
   }

   @Ignore
   @Test
   public void testLancer() throws Exception {
      majCorrespondancesService.lancer();

   }

   private void initComposants() throws Exception {

   }

}
