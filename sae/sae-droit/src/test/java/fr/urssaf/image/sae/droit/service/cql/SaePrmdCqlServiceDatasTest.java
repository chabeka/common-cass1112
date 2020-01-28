/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.service.SaePrmdService;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * Classe Test de la classe {@link SaePrmdService} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePrmdCqlServiceDatasTest {

  @Autowired
  private SaePrmdService service;

  @Autowired
  private PrmdCqlSupport prmdCqlSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  public String cfName = Constantes.CF_DROIT_PRMD;

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testServicePrmdNotExists() throws Exception {

    GestionModeApiUtils.setModeApiCql(cfName);
    final boolean value = service.prmdExists("code");

    Assert.assertFalse("le prmd n'existe pas", value);
  }

  @Test
  public void testServicePrmdExists() {
    GestionModeApiUtils.setModeApiCql(cfName);
    final Prmd prmd = new Prmd();

    prmd.setCode("codePrmd");
    prmd.setDescription("description Prmd");
    prmd.setLucene("lucene Prmd");
    prmd.setBean("bean1");
    prmd.setMetadata(new HashMap<String, List<String>>());

    prmdCqlSupport.create(prmd);

    final boolean value = service.prmdExists("codePrmd");

    Assert.assertTrue("le prmd existe", value);
  }

  @Test(expected = DroitRuntimeException.class)
  public void testPrmdExiste() throws Exception {

    GestionModeApiUtils.setModeApiCql(cfName);
    final Prmd prmd = new Prmd();

    prmd.setCode("codePrmd");
    prmd.setDescription("description Prmd");
    prmd.setLucene("lucene Prmd");
    prmd.setBean("bean1");
    prmd.setMetadata(new HashMap<String, List<String>>());

    prmdCqlSupport.create(prmd);

    service.createPrmd(prmd);
  }

  @Test
  public void testSucces() throws Exception {

    GestionModeApiUtils.setModeApiCql(cfName);
    final Prmd prmd = new Prmd();

    prmd.setCode("codePrmd");
    prmd.setDescription("description Prmd");
    prmd.setLucene("lucene Prmd");
    prmd.setBean("bean1");
    final Map<String, List<String>> map = new HashMap<>();
    map.put("cle1", Arrays.asList(new String[]{"valeur1"}));
    prmd.setMetadata(map);

    service.createPrmd(prmd);

    final Prmd storedPrmd = prmdCqlSupport.find("codePrmd");
    Assert.assertEquals("les deux prmds doivent être identiques", storedPrmd,
                        prmd);
  }
}
