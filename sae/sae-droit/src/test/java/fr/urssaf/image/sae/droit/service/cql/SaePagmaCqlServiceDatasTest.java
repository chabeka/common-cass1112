/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import java.util.Arrays;
import java.util.HashMap;

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
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.droit.service.SaePagmaService;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * Classe Test de la classe {@link SaePagmaService} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-droit-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaePagmaCqlServiceDatasTest {

  @Autowired
  private SaePagmaService service;

  @Autowired
  private PagmaCqlSupport pagmaCqlSupport;

  @Autowired
  private ActionUnitaireCqlSupport actionUnitaireCqlSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  public String cfName = Constantes.CF_DROIT_PAGMA;

  @Before
  public void start() throws Exception {
    initModeAPI();
  }

  @After
  public void end() throws Exception {
    cassandraServer.clearAndLoad();
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


  @Test(expected = PagmaReferenceException.class)
  public void testPagmaDejaExistant() throws Exception {
    final Pagma pagma = new Pagma();
    pagma.setCode("codePagma");
    pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));
    pagmaCqlSupport.create(pagma);

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
    actionUnitaireCqlSupport.create(actionUnitaire);

    final Pagma pagma = new Pagma();
    pagma.setCode("codePagma");
    pagma.setActionUnitaires(Arrays.asList(new String[] { "action1" }));

    service.createPagma(pagma);

    final Pagma storePagma = pagmaCqlSupport.findById("codePagma");
    Assert.assertEquals("le pagma doit être créé correctement", pagma,
                        storePagma);


  }

  /**
   * 
   */
  private void initModeAPI() {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(Constantes.CF_DROIT_PAGMA, MODE_API.DATASTAX);
    modesApiTest.put(Constantes.CF_DROIT_ACTION_UNITAIRE, MODE_API.DATASTAX);
    ModeGestionAPI.setListeCfsModes(modesApiTest);
  }

}
