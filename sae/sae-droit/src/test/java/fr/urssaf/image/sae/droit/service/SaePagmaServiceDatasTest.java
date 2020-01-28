/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
// @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaePagmaServiceDatasTest {

  @Autowired
  private SaePagmaService service;

  @Autowired
  private PagmaSupport pagmaSupport;

  @Autowired
  private ActionUnitaireSupport actionSupport;

  @Autowired
  private JobClockSupport clockSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  private final String cfName = Constantes.CF_DROIT_PAGMA;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SaePagmaServiceDatasTest.class);

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
      LOGGER.error("Une erreur s'est produite lors du resetData de cassandra: {}", e.getMessage());
    }
  }
  @Test(expected = PagmaReferenceException.class)
  public void testPagmaDejaExistant() throws Exception {

    final Pagma pagma = new Pagma();
    pagma.setCode("codePagma");
    pagma.setActionUnitaires(Arrays.asList(new String[] {"action1"}));

    pagmaSupport.create(pagma, clockSupport.currentCLock());

    service.createPagma(pagma);


  }

  @Test(expected = ActionUnitaireReferenceException.class)
  public void testActionInexistante() throws Exception {

    final Pagma pagma = new Pagma();
    pagma.setCode("codePagma");
    pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));

    service.createPagma(pagma);

  }

  @Test
  public void testCreationSucces() throws Exception {

    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode("action1");
    actionUnitaire.setDescription("description");
    actionSupport.create(actionUnitaire, clockSupport.currentCLock());

    final Pagma pagma = new Pagma();
    pagma.setCode("codePagma");
    pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));

    service.createPagma(pagma);

    final Pagma storePagma = pagmaSupport.find("codePagma");
    Assert.assertEquals("le pagma doit être créé correctement", pagma,
                        storePagma);

  }

}
