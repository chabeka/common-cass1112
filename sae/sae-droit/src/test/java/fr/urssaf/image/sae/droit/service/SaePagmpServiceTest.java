/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Pagmp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePagmpServiceTest {

  /**
   * 
   */
  private static final String TYPE_CORRECT = "type de l'exception correcte";
  /**
   * 
   */
  private static final String ERREUR_ATTENDUE = "erreur attendue";
  @Autowired
  @Qualifier("saePagmpServiceFacadeImpl")
  private SaePagmpService service;

  @Test
  public void testCreatePagmpObligatoire() {

    try {
      service.createPagmp(null);
      Assert.fail(ERREUR_ATTENDUE);
    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT,
                          IllegalArgumentException.class, e.getClass());
      Assert.assertTrue("message de l'exception contient pagm", e
                        .getMessage().contains("pagmp"));
    }

  }

  @Test
  public void testCreateCodePagmpObligatoire() {

    try {
      final Pagmp pagmp = new Pagmp();
      pagmp.setDescription("description");
      pagmp.setPrmd("prmd");

      service.createPagmp(pagmp);
      Assert.fail(ERREUR_ATTENDUE);
    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT,
                          IllegalArgumentException.class, e.getClass());
      Assert.assertTrue("message de l'exception contient code", e
                        .getMessage().contains("code"));
    }

  }

  @Test
  public void testCreateDescriptionPagmpObligatoire() {

    try {
      final Pagmp pagmp = new Pagmp();
      pagmp.setCode("code");
      pagmp.setPrmd("prmd");

      service.createPagmp(pagmp);
      Assert.fail(ERREUR_ATTENDUE);
    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT,
                          IllegalArgumentException.class, e.getClass());
      Assert.assertTrue("message de l'exception contient description", e
                        .getMessage().contains("description"));
    }

  }

  @Test
  public void testCreatePrmdPagmpObligatoire() {

    try {
      final Pagmp pagmp = new Pagmp();
      pagmp.setDescription("description");
      pagmp.setCode("code");

      service.createPagmp(pagmp);
      Assert.fail(ERREUR_ATTENDUE);
    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT,
                          IllegalArgumentException.class, e.getClass());
      Assert.assertTrue("message de l'exception contient prmd", e
                        .getMessage().contains("prmd"));
    }
  }
}
