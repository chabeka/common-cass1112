/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaePagmpServiceDatasTest {

  @Autowired
  private SaePagmpService service;

  @Autowired
  private JobClockSupport clockSupport;

  @Autowired
  private PagmpSupport pagmpSupport;

  @Autowired
  private PrmdSupport prmdSupport;

  @Autowired
  private CassandraServerBean cassandraServer;

  @After
  public void end() throws Exception {
    cassandraServer.resetDataOnly();
    ;
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
  public void testPagmpExiste() {

    final Pagmp pagmp = new Pagmp();
    pagmp.setCode("codePagmp");
    pagmp.setDescription("description Pagmp");
    pagmp.setPrmd("prmd");

    pagmpSupport.create(pagmp, clockSupport.currentCLock());

    service.createPagmp(pagmp);

  }

  @Test(expected = PrmdReferenceException.class)
  public void testPrmdInexistant() {

    final Pagmp pagmp = new Pagmp();
    pagmp.setCode("codePagmp");
    pagmp.setDescription("description Pagmp");
    pagmp.setPrmd("prmd");

    service.createPagmp(pagmp);
  }

  @Test
  public void testSucces() {

    final Prmd prmd = new Prmd();
    prmd.setCode("prmd");
    prmd.setDescription("description");
    prmd.setLucene("lucene");
    prmd.setBean("bean1");
    prmd.setMetadata(new HashMap<String, List<String>>());

    prmdSupport.create(prmd, clockSupport.currentCLock());

    final Pagmp pagmp = new Pagmp();
    pagmp.setCode("codePagmp");
    pagmp.setDescription("description Pagmp");
    pagmp.setPrmd("prmd");

    service.createPagmp(pagmp);

    final Pagmp storedPamp = pagmpSupport.find("codePagmp");

    Assert.assertEquals("les deux objets pagmp doivent être identiques",
                        pagmp, storedPamp);
  }
}
