package fr.urssaf.image.sae.commons.support.facade;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.support.ParametersSupport;
import fr.urssaf.image.sae.commons.support.cql.ParametersCqlSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-commons-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ParametersSupportFacadeTest {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ParametersSupportFacadeTest.class);

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private ParametersSupportFacade supportFacade;

  @Autowired
  private ParametersSupport support;

  @Autowired
  private ParametersCqlSupport supportCql;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

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
      LOGGER.error(e.getMessage());
    }
  }

  @Test(expected = ModeGestionAPIUnkownException.class)
  public void testModeAPIInconnu() throws ParameterNotFoundException {
    // On se met sur mode API inconnu
    modeApiSupport.updateModeApi("UNKNOWN", Constantes.CF_PARAMETERS);
    // On attends que le cache soit modifié
    try {
      Thread.sleep(45000);
    }
    catch (final InterruptedException e) {
      e.printStackTrace();
    }
    supportFacade.find(ParameterType.JOURNALISATION_EVT_META_TITRE,
                       ParameterRowType.TRACABILITE);
  }

  @Test
  public void testCreationDualReadThrift() throws ParameterNotFoundException {
    // On se met sur mode dual read thrift
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT, Constantes.CF_PARAMETERS);
    final Parameter parameter = new Parameter(ParameterType.PURGE_CORBEILLE_DUREE, 20);

    // On ajoute un parameter avec la facade sur les tables thrift et cql
    supportFacade.create(parameter, ParameterRowType.CORBEILLE);
    // On supprime le parameter cql
    // supportCql.delete(CODE1);
    // On recherche le parameter avec la facade
    final Parameter parameterFacade = supportFacade.find(ParameterType.PURGE_CORBEILLE_DUREE, ParameterRowType.CORBEILLE);
    // On recherche le parameter thrift
    final Parameter parameterThrift = support.find(ParameterType.PURGE_CORBEILLE_DUREE, ParameterRowType.CORBEILLE);
    // On vérifie que les deux parameter sont bien les mêmes
    Assert.assertEquals(parameterFacade, parameterThrift);
  }

  @Test
  public void testCreationDualReadCql() throws ParameterNotFoundException {
    // On se met sur mode dual read cql
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL, Constantes.CF_PARAMETERS);
    final Parameter parameter = new Parameter(ParameterType.PURGE_CORBEILLE_DUREE, 20);

    // On ajoute un parameter avec la facade sur les tables thrift et cql
    supportFacade.create(parameter, ParameterRowType.CORBEILLE);
    // On supprime le parameter cql
    // supportCql.delete(CODE1);
    // On recherche le parameter avec la facade
    final Parameter parameterFacade = supportFacade.find(ParameterType.PURGE_CORBEILLE_DUREE, ParameterRowType.CORBEILLE);
    // On recherche le parameter thrift
    final Parameter parameterCql = supportCql.find(ParameterType.PURGE_CORBEILLE_DUREE, ParameterRowType.CORBEILLE);
    // On vérifie que les deux parameter sont bien les mêmes
    Assert.assertEquals(parameterFacade, parameterCql);
  }


}
