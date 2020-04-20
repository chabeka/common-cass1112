/**
 * AC75095351
 */
package fr.urssaf.image.sae.droit.dao.support.facade;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.droit.utils.Constantes;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PagmaSupportFacadeTest {


  private static final String CODE1 = "code1";

  private static final String[] ACTIONS1 = new String[] { "action1", "action2" };

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private PagmaSupportFacade supportFacade;

  @Autowired
  private PagmaSupport support;

  @Autowired
  private PagmaCqlSupport supportCql;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @After
  public void end() throws Exception {
    cassandraServer.resetData();
  }

  @Ignore
  @Test(expected = ModeGestionAPIUnkownException.class)
  public void testModeAPIInconnu() throws InterruptedException {
    // On se met sur mode API inconnu
    modeApiSupport.updateModeApi("UNKNOWN", Constantes.CF_DROIT_PAGMA);
    Thread.sleep(30000);
    supportFacade.find("TEST");
  }

  @Test
  public void testCreationDualReadThrift() {
    // On se met sur mode dual read thrift
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT, Constantes.CF_DROIT_PAGMA);
    final List<String> listeAu = Arrays.asList(ACTIONS1);
    final Pagma pagma = new Pagma();
    pagma.setActionUnitaires(listeAu);
    pagma.setCode(CODE1);
    // On ajoute un pagm avec la facade sur les tables thrift et cql
    supportFacade.create(pagma);
    // On supprime le pagm cql
    supportCql.delete(CODE1);
    // On recherche le pagm avec la facade
    final Pagma pagmaFacade = supportFacade.find(CODE1);
    // On recherche le pagm thrift
    final Pagma pagmThrift = support.find(CODE1);
    // On vérifie que les deux pagm sont bien les mêmes
    Assert.assertEquals(pagmThrift, pagmaFacade);
  }

  @Test
  public void testCreationDualReadCql() {
    // On se met sur mode dual read cql
    modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL, Constantes.CF_DROIT_PAGMA);
    final List<String> listeAu = Arrays.asList(ACTIONS1);
    final Pagma pagma = new Pagma();
    pagma.setActionUnitaires(listeAu);
    pagma.setCode(CODE1);
    // On ajoute un pagm avec la facade sur les tables thrift et cql
    supportFacade.create(pagma);
    // On supprime le pagm thrift
    support.delete(CODE1, new Date().getTime());
    // On recherche le pagm avec la facade
    final Pagma pagmaFacade = supportFacade.find(CODE1);
    // On recherche le pagm cql
    final Pagma pagmCql = supportCql.find(CODE1);
    // On vérifie que les deux pagm sont bien les mêmes
    Assert.assertEquals(pagmCql, pagmaFacade);
  }

}
