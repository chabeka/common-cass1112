/**
 * 
 */
package fr.urssaf.image.sae.droit.service.cql;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmfCqlSupport;
import fr.urssaf.image.sae.droit.exception.PagmfNotFoundException;
import fr.urssaf.image.sae.droit.service.SaePagmfService;
import fr.urssaf.image.sae.droit.utils.Constantes;

/**
 * Classe Test de la classe {@link SaePagmfService} en mode Cql
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaePagmfCqlServiceDatasTest {

  @Autowired
  private SaePagmfService service;

  @Autowired
  private PagmfCqlSupport pagmfCqlSupport;




  @Autowired
  private CassandraServerBean cassandraServer;



  public String cfName = Constantes.CF_DROIT_PAGMF;



  @Test(expected = PagmfNotFoundException.class)
  public void testPagmfInexistant() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
    GestionModeApiUtils.setModeApiCql(cfName);
    service.getPagmf("test");



  }


  @Test
  public void testSucces() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
    GestionModeApiUtils.setModeApiCql(cfName);

    final Pagmf pagmf = new Pagmf();
    pagmf.setCodePagmf("codePagmf");
    pagmf.setDescription("description Pagmf");
    pagmf.setCodeFormatControlProfil("codeformat");

    service.addPagmf(pagmf);

    final Pagmf storePagmf = pagmfCqlSupport.findById("codePagmf");
    Assert.assertEquals("le pagmf doit être créé correctement",
                        pagmf,
                        storePagmf);


  }

}
