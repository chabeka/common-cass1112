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
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.service.SaePrmdService;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * Classe Test de la classe {@link SaePrmdService} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePrmdCqlServiceTest {
  /**
   * 
   */
  private static final String TYPE_CORRECTE = "type de l'exception correcte";

  /**
   * 
   */
  private static final String ERREUR_ATTENDUE = "erreur attendue";
  @Autowired
  private SaePrmdService service;

  @Autowired
  private CassandraServerBean cassandraServer;

  public String cfName = Constantes.CF_DROIT_PRMD;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testCreatePrmdObligatoire() {
    try {
      modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DATASTAX, cfName);
      service.createPrmd(null);
      Assert.fail(ERREUR_ATTENDUE);
    }
    catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECTE,
                          IllegalArgumentException.class,
                          e
                          .getClass());
      Assert.assertTrue("message de l'exception contient prmd",
                        e
                        .getMessage()
                        .contains("prmd"));
    }

  }

  @Test
  public void testCreateCodePrmdObligatoire() {
    try {
      modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DATASTAX, cfName);
      final Prmd prmd = new Prmd();
      prmd.setDescription("description");
      prmd.setLucene("lucène");

      service.createPrmd(prmd);
      Assert.fail(ERREUR_ATTENDUE);
    }
    catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECTE,
                          IllegalArgumentException.class,
                          e
                          .getClass());
      Assert.assertTrue("message de l'exception contient code",
                        e
                        .getMessage()
                        .contains("code"));
    }

  }

  @Test
  public void testCreateDescriptionObligatoire() {
    try {
      modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DATASTAX, cfName);
      final Prmd prmd = new Prmd();
      prmd.setCode("code");
      prmd.setLucene("lucène");

      service.createPrmd(prmd);
      Assert.fail(ERREUR_ATTENDUE);
    }
    catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECTE,
                          IllegalArgumentException.class,
                          e
                          .getClass());
      Assert.assertTrue("message de l'exception contient description",
                        e
                        .getMessage()
                        .contains("description"));
    }
  }

  @Test
  public void testCheckCodeObligatoire() {
    try {
      modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DATASTAX, cfName);
      service.prmdExists(null);
      Assert.fail(ERREUR_ATTENDUE);
    }
    catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECTE,
                          IllegalArgumentException.class,
                          e
                          .getClass());
      Assert.assertTrue("message de l'exception contient code",
                        e
                        .getMessage()
                        .contains("code"));
    }
  }

  @Test
  public void getPrmdTest() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DATASTAX, cfName);
    final Prmd prmd = new Prmd();

    prmd.setCode("codePrmd");
    prmd.setDescription("description Prmd");
    prmd.setLucene("lucene Prmd");
    prmd.setBean("bean1");
    final Map<String, List<String>> map = new HashMap<>();
    map.put("cle1", Arrays.asList(new String[]{"valeur1"}));
    prmd.setMetadata(map);

    service.createPrmd(prmd);

    final Prmd storedPrmd = service.getPrmd("codePrmd");
    Assert.assertEquals("les deux prmds doivent être identiques", storedPrmd,
                        prmd);
  }
}
