/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.service.SaeActionUnitaireService;
import fr.urssaf.image.sae.droit.utils.Constantes;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
/**
 * Classe Test de la classe {@link ActionUnitaireService} en mode Cql
 */
public class SaeActionUnitaireCqlServiceTest {

  @Autowired
  private SaeActionUnitaireService service;


  public String cfName = Constantes.CF_DROIT_ACTION_UNITAIRE;



  @Test
  public void testActionUnitaireObligatoire() {

    try {

      GestionModeApiUtils.setModeApiCql(cfName);
      service.createActionUnitaire(null);
      Assert.fail("exception attendue");
    }
    catch (final Exception e) {

      Assert.assertEquals("type de l'exception correcte",
                          IllegalArgumentException.class,
                          e.getClass());
    }

  }

  @Test
  public void testCodeObligatoire() {

    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      final ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setDescription("test création");

      service.createActionUnitaire(actionUnitaire);
      Assert.fail("exception attendue");
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
  public void testDescriptionObligatoire() {

    try {
      GestionModeApiUtils.setModeApiCql(cfName);
      final ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("test1");

      service.createActionUnitaire(actionUnitaire);
      Assert.fail("exception attendue");
    }
    catch (final Exception e) {

      Assert.assertEquals("type de l'exception correcte",
                          IllegalArgumentException.class,
                          e.getClass());
      Assert.assertTrue("message de l'exception contient description",
                        e
                        .getMessage()
                        .contains("description"));
    }

  }


}
