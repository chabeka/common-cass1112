/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class TraceDestinataireServiceTest {

  @Autowired
  private TraceDestinaireService service;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @Before
  public void start() throws Exception {
    modeApiSupport.initTables(ModeGestionAPI.MODE_API.HECTOR);
  }

  @After
  public void after() throws Exception {
    server.resetDataOnly();
  }

  @Test
  public void testGetCodeEvenementByTypeTrace() {

    final List<String> list = service.getCodeEvenementByTypeTrace("REG_TECHNIQUE");
    Assert.assertTrue(list != null && !list.isEmpty());
  }

}
