/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import java.util.HashMap;
import java.util.List;

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
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmpCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;
import fr.urssaf.image.sae.droit.service.SaePagmpService;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.ModeAPIDroitUtils;

/**
 * Classe Test de la classe {@link SaePagmpService} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
// @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaePagmpCqlServiceDatasTest {

  @Autowired
  private SaePagmpService service;

  @Autowired
  private PagmpCqlSupport pagmpCqlSupport;

  @Autowired
  private PrmdCqlSupport prmdCqlSupport;

  @Autowired
  private CassandraServerBean cassandraServer;



  public String cfName = Constantes.CF_DROIT_PAGMP;

  @Before
  public void start() throws Exception {
    ModeAPIDroitUtils.setAllDroitsModeAPICql();
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


  @Test(expected = PagmpReferenceException.class)
  public void testPagmpDejaExistant() throws Exception {

    final Pagmp pagmp = new Pagmp();
    pagmp.setCode("codePagmp");
    pagmp.setDescription("description Pagmp");
    pagmp.setPrmd("prmd");
    pagmpCqlSupport.create(pagmp);

    service.createPagmp(pagmp);


  }

  @Test(expected = PrmdReferenceException.class)
  public void testPrmdInexistante() throws Exception {

    final Pagmp pagmp = new Pagmp();
    pagmp.setCode("codePagmp");
    pagmp.setDescription("description Pagmp");
    pagmp.setPrmd("prmd");

    service.createPagmp(pagmp);



  }

  @Test
  public void testSucces() throws Exception {
    // cassandraServer.resetData(true, MODE_API.DATASTAX);

    final Prmd prmd = new Prmd();
    prmd.setCode("prmd");
    prmd.setDescription("description");
    prmd.setLucene("lucene");
    prmd.setBean("bean1");
    prmd.setMetadata(new HashMap<String, List<String>>());

    prmdCqlSupport.create(prmd);

    final Pagmp pagmp = new Pagmp();
    pagmp.setCode("codePagmp");
    pagmp.setDescription("description Pagmp");
    pagmp.setPrmd("prmd");

    service.createPagmp(pagmp);

    final Pagmp storePagmp = pagmpCqlSupport.findById("codePagmp");
    Assert.assertEquals("le pagmp doit être créé correctement",
                        pagmp,
                        storePagmp);


  }



}
