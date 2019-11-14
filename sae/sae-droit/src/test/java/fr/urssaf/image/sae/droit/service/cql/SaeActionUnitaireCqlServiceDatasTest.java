/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.service.SaeActionUnitaireService;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * (AC75095351) Classe Test de la classe {@link SaeActionUnitaire} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaeActionUnitaireCqlServiceDatasTest {

  @Autowired
  private SaeActionUnitaireService service;

  @Autowired
  private ActionUnitaireCqlSupport actionCqlSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  public String cfName = Constantes.CF_DROIT_ACTION_UNITAIRE;

  @Before
  public void setup() throws Exception {

    GestionModeApiUtils.setModeApiCql(cfName);
  }
  @After
  public void end() throws Exception {
    cassandraServer.resetDataOnly();
  }

  @Test
  public void init() {
    try {
      if (cassandraServer.isCassandraStarted()) {
        cassandraServer.resetData();
      }
      Assert.assertTrue(true);
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }
  @Test(expected = DroitRuntimeException.class)
  public void testActionUnitaireExistante() throws Exception {
    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode("codeAction");
    actionUnitaire.setDescription("description action");

    actionCqlSupport.create(actionUnitaire);

    service.createActionUnitaire(actionUnitaire);

  }

  @Test
  public void testActionUnitaireSucces() throws Exception {
    // cassandraServercql.resetData(true);
    GestionModeApiUtils.setModeApiCql(cfName);

    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode("codeAction");
    actionUnitaire.setDescription("description action");
    service.createActionUnitaire(actionUnitaire);

    final ActionUnitaire storedAction = actionCqlSupport.find("codeAction");

    Assert.assertEquals("les deux actions doivent etre identiques",
                        actionUnitaire, storedAction);

  }

}
