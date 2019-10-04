/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePrmdServiceDatasTest {

  @Autowired
  @Qualifier("saePrmdServiceFacadeImpl")
  private SaePrmdService service;

  @Autowired
  private PrmdSupport prmdSupport;

  @Autowired
  private JobClockSupport clockSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.HECTOR);
  }

  @Test
  public void testServicePrmdNotExists() {
    final boolean value = service.prmdExists("code");

    Assert.assertFalse("le prmd n'existe pas", value);
  }

  @Test
  public void testServicePrmdExists() {

    final Prmd prmd = new Prmd();

    prmd.setCode("codePrmd");
    prmd.setDescription("description Prmd");
    prmd.setLucene("lucene Prmd");
    prmd.setBean("bean1");
    prmd.setMetadata(new HashMap<String, List<String>>());


    prmdSupport.create(prmd, clockSupport.currentCLock());

    final boolean value = service.prmdExists("codePrmd");

    Assert.assertTrue("le prmd existe", value);
  }

  @Test(expected = DroitRuntimeException.class)
  public void testPrmdExiste() {

    final Prmd prmd = new Prmd();

    prmd.setCode("codePrmd");
    prmd.setDescription("description Prmd");
    prmd.setLucene("lucene Prmd");
    prmd.setBean("bean1");
    prmd.setMetadata(new HashMap<String, List<String>>());

    prmdSupport.create(prmd, clockSupport.currentCLock());

    service.createPrmd(prmd);
  }

  @Test
  public void testSucces() {

    final Prmd prmd = new Prmd();

    prmd.setCode("codePrmd");
    prmd.setDescription("description Prmd");
    prmd.setLucene("lucene Prmd");
    prmd.setBean("bean1");
    final Map<String, List<String>> map = new HashMap<>();
    map.put("cle1", Arrays.asList(new String[]{"valeur1"}));
    prmd.setMetadata(map);

    service.createPrmd(prmd);

    final Prmd storedPrmd = prmdSupport.find("codePrmd");
    Assert.assertEquals("les deux prmds doivent être identiques", storedPrmd,
                        prmd);
  }
}
