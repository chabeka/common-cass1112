/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.service.SaePagmpService;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * Classe Test de la classe {@link SaePagmpService} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaePagmpCqlServiceTest {
  /**
   * 
   */
  private static final String TYPE_CORRECT = "type de l'exception correcte";

  /**
   * 
   */
  private static final String ERREUR_ATTENDUE = "erreur attendue";
  @Autowired
  private SaePagmpService service;


  public String cfName = Constantes.CF_DROIT_PAGMP;


  @Test
  public void testCreatePagmpObligatoire() {

    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      service.createPagmp(null);
      Assert.fail(ERREUR_ATTENDUE);
    }
    catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT,
                          IllegalArgumentException.class,
                          e.getClass());
      Assert.assertTrue("message de l'exception contient pagm",
                        e
                        .getMessage()
                        .contains("pagmp"));
    }

  }

  @Test
  public void testCreateCodePagmpObligatoire() {

    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      final Pagmp pagmp = new Pagmp();
      pagmp.setDescription("description");
      pagmp.setPrmd("prmd");

      service.createPagmp(pagmp);
      Assert.fail(ERREUR_ATTENDUE);
    }
    catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT,
                          IllegalArgumentException.class,
                          e.getClass());
      Assert.assertTrue("message de l'exception contient code",
                        e
                        .getMessage()
                        .contains("code"));
    }

  }

  @Test
  public void testCreateDescriptionPagmpObligatoire() {

    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      final Pagmp pagmp = new Pagmp();
      pagmp.setCode("code");
      pagmp.setPrmd("prmd");

      service.createPagmp(pagmp);
      Assert.fail(ERREUR_ATTENDUE);
    }
    catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT,
                          IllegalArgumentException.class,
                          e.getClass());
      Assert.assertTrue("message de l'exception contient description",
                        e
                        .getMessage()
                        .contains("description"));
    }

  }

  @Test
  public void testCreatePrmdPagmpObligatoire() {

    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      final Pagmp pagmp = new Pagmp();
      pagmp.setDescription("description");
      pagmp.setCode("code");

      service.createPagmp(pagmp);
      Assert.fail(ERREUR_ATTENDUE);
    }
    catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT,
                          IllegalArgumentException.class,
                          e.getClass());
      Assert.assertTrue("message de l'exception contient prmd",
                        e
                        .getMessage()
                        .contains("prmd"));
    }
  }

}
