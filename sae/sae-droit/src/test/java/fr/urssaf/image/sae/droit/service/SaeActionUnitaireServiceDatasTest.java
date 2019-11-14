/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

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
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.utils.Constantes;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SaeActionUnitaireServiceDatasTest {

  @Autowired
  private SaeActionUnitaireService service;

  @Autowired
  private ActionUnitaireSupport actionSupport;

  @Autowired
  private JobClockSupport clockSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  private final String cfName = Constantes.CF_DROIT_ACTION_UNITAIRE;

  @Before
  public void start() throws Exception {
    GestionModeApiUtils.setModeApiThrift(cfName);
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
  public void testActionUnitaireExistante() {

    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode("codeAction");
    actionUnitaire.setDescription("description action");

    actionSupport.create(actionUnitaire, clockSupport.currentCLock());

    service.createActionUnitaire(actionUnitaire);

  }

  @Test
  public void testActionUnitaireSucces() {

    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode("codeAction");
    actionUnitaire.setDescription("description action");

    service.createActionUnitaire(actionUnitaire);

    final ActionUnitaire storedAction = actionSupport.find("codeAction");

    Assert.assertEquals("les deux actions doivent etre identiques",
                        actionUnitaire, storedAction);

  }

}
