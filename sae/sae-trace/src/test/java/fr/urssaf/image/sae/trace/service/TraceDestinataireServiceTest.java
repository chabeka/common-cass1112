/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class TraceDestinataireServiceTest {

   @Autowired
   private TraceDestinaireService service;

   @Autowired
   private CassandraServerBean server;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testGetCodeEvenementByTypeTrace() {
    
      service.getCodeEvenementByTypeTrace("REG_TECHNIQUE");
   }

 
}
