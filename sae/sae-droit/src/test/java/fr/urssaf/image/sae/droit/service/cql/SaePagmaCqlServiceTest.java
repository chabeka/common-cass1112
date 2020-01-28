/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.service.SaePagmaService;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * Classe Test de la classe {@link SaePagmaService} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaePagmaCqlServiceTest {

  @Autowired
  private SaePagmaService service;


  public String cfName = Constantes.CF_DROIT_PAGMA;


  @Test
  public void testPagmaObligatoire() throws Exception {


    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      service.createPagma(null);
      Assert.fail("erreur attendue");
    }
    catch (final Exception e) {
      Assert.assertEquals("type de l'exception correcte",
                          IllegalArgumentException.class,
                          e.getClass());
      Assert.assertTrue("message de l'exception contient pagma",
                        e
                        .getMessage()
                        .contains("pagma"));
    }

  }

  @Test
  public void testCodePagmaObligatoire() throws Exception {

    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      final Pagma pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] {"action1"}));

      service.createPagma(pagma);
      Assert.fail("erreur attendue");
    }
    catch (final Exception e) {
      Assert.assertEquals("type de l'exception correcte",
                          IllegalArgumentException.class,
                          e.getClass());
      Assert.assertTrue("message de l'exception contient code",
                        e
                        .getMessage()
                        .contains("code"));
    }

  }

  @Test
  public void testActionsUnitairesPagmaObligatoire() throws Exception {

    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      final Pagma pagma = new Pagma();
      pagma.setCode("code");

      service.createPagma(pagma);
      Assert.fail("erreur attendue");
    }
    catch (final Exception e) {
      Assert.assertEquals("type de l'exception correcte",
                          IllegalArgumentException.class,
                          e.getClass());
      Assert.assertTrue("message de l'exception contient actions unitaires",
                        e.getMessage().contains("actions unitaires"));
    }

  }



}
