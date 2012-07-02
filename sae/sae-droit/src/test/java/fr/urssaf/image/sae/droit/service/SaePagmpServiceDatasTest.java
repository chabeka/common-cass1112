/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
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
      cassandraServer.resetData();
   }

   @Test(expected = DroitRuntimeException.class)
   public void testPagmpExiste() {

      Pagmp pagmp = new Pagmp();
      pagmp.setCode("codePagmp");
      pagmp.setDescription("description Pagmp");
      pagmp.setPrmd("prmd");

      pagmpSupport.create(pagmp, clockSupport.currentCLock());

      service.createPagmp(pagmp);

   }

   @Test(expected = PrmdReferenceException.class)
   public void testPrmdInexistant() {

      Pagmp pagmp = new Pagmp();
      pagmp.setCode("codePagmp");
      pagmp.setDescription("description Pagmp");
      pagmp.setPrmd("prmd");

      service.createPagmp(pagmp);
   }

   @Test
   public void testSucces() {

      Prmd prmd = new Prmd();
      prmd.setCode("prmd");
      prmd.setDescription("description");
      prmd.setLucene("lucene");
      prmd.setBean("bean1");
      prmd.setMetadata(new HashMap<String, String>());

      prmdSupport.create(prmd, clockSupport.currentCLock());

      Pagmp pagmp = new Pagmp();
      pagmp.setCode("codePagmp");
      pagmp.setDescription("description Pagmp");
      pagmp.setPrmd("prmd");

      service.createPagmp(pagmp);

      Pagmp storedPamp = pagmpSupport.find("codePagmp");

      Assert.assertEquals("les deux objets pagmp doivent être identiques",
            pagmp, storedPamp);
   }
}
