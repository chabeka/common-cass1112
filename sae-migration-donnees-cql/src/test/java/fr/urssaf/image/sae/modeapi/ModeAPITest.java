/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.modeapi;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;




/**
 * (AC75095351) Classe de test migration des parameters
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class ModeAPITest {

  @Autowired
  private ModeApiCqlSupport modeApiCqlSupport;


  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(ModeAPITest.class);



  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Test init
   */
  @Test
  public void testInitHector() {
    try {

      modeApiCqlSupport.initTables(MODE_API.HECTOR);
      final List<ModeAPI> listmodeAPI = modeApiCqlSupport.findAll();

      Assert.assertTrue(!listmodeAPI.isEmpty());
      boolean allThrift = true;
      for (final ModeAPI modeAPI : listmodeAPI) {
        if (!modeAPI.getMode().equals(MODE_API.HECTOR)) {
          allThrift = false;
          break;
        }
      }
      Assert.assertTrue(allThrift);
      Assert.assertTrue(listmodeAPI.size() == 35);
    }
    catch (final Exception ex) {

      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }
}
