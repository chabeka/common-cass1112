/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.droit.dao.model.Pagma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePagmaServiceTest {
  @Autowired
  private CassandraServerBean cassandraServer;
  @Autowired
  // @Qualifier("saePagmaServiceFacadeImpl")
  private SaePagmaService service;

  @Test
  public void testPagmaObligatoire() throws Exception {
    cassandraServer.resetData(true, MODE_API.HECTOR);
    try {
      service.createPagma(null);
      Assert.fail("erreur attendue");
    } catch (final Exception e) {
      Assert.assertEquals("type de l'exception correcte",
                          IllegalArgumentException.class, e.getClass());
      Assert.assertTrue("message de l'exception contient pagma", e
                        .getMessage().contains("pagma"));
    }

  }

  @Test
  public void testCodePagmaObligatoire() throws Exception {
    cassandraServer.resetData(true, MODE_API.HECTOR);
    try {
      final Pagma pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));

      service.createPagma(pagma);
      Assert.fail("erreur attendue");
    } catch (final Exception e) {
      Assert.assertEquals("type de l'exception correcte",
                          IllegalArgumentException.class, e.getClass());
      Assert.assertTrue("message de l'exception contient code", e
                        .getMessage().contains("code"));
    }

  }

  @Test
  public void testActionsUnitairesPagmaObligatoire() throws Exception {
    cassandraServer.resetData(true, MODE_API.HECTOR);
    try {
      final Pagma pagma = new Pagma();
      pagma.setCode("code");

      service.createPagma(pagma);
      Assert.fail("erreur attendue");
    } catch (final Exception e) {
      Assert.assertEquals("type de l'exception correcte",
                          IllegalArgumentException.class, e.getClass());
      Assert.assertTrue("message de l'exception contient actions unitaires",
                        e.getMessage().contains("actions unitaires"));
    }

  }

}
