/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePrmdServiceDatasTest {

   @Autowired
   private SaePrmdService service;

   @Autowired
   private PrmdSupport prmdSupport;

   @Autowired
   private JobClockSupport clockSupport;

   @Autowired
   private CassandraServerBean cassandraServer;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test
   public void testServicePrmdNotExists() {
      boolean value = service.prmdExists("code");
      
      Assert.assertFalse("le prmd n'existe pas", value);
   }

   @Test
   public void testServicePrmdExists() {
      
      Prmd prmd = new Prmd();

      prmd.setCode("codePrmd");
      prmd.setDescription("description Prmd");
      prmd.setLucene("lucene Prmd");
      prmd.setBean("bean1");
      prmd.setMetadata(new HashMap<String, String>());

      prmdSupport.create(prmd, clockSupport.currentCLock());
      
      boolean value = service.prmdExists("codePrmd");
      
      Assert.assertTrue("le prmd existe", value);
   }
   
   @Test(expected = DroitRuntimeException.class)
   public void testPrmdExiste() {

      Prmd prmd = new Prmd();

      prmd.setCode("codePrmd");
      prmd.setDescription("description Prmd");
      prmd.setLucene("lucene Prmd");
      prmd.setBean("bean1");
      prmd.setMetadata(new HashMap<String, String>());

      prmdSupport.create(prmd, clockSupport.currentCLock());

      service.createPrmd(prmd);
   }

   @Test
   public void testSucces() {

      Prmd prmd = new Prmd();

      prmd.setCode("codePrmd");
      prmd.setDescription("description Prmd");
      prmd.setLucene("lucene Prmd");
      prmd.setBean("bean1");
      Map<String, String> map = new HashMap<String, String>();
      map.put("cle1", "valeur1");
      prmd.setMetadata(map);

      service.createPrmd(prmd);

      Prmd storedPrmd = prmdSupport.find("codePrmd");
      Assert.assertEquals("les deux prmds doivent être identiques", storedPrmd,
            prmd);
   }
}
