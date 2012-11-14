/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.support;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.bean.CassandraConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
public class DocInfoServiceTest {

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private CassandraConfig cassandraConfig;

   @After
   public void end() {
      cassandraSupport.disconnect();
   }

   @Test
   public void testConnect() throws ConnectionException {

      cassandraSupport.connect();

      Assert.assertEquals("le keySpace doit etre correct", cassandraConfig
            .getKeyspace(), cassandraSupport.getKeySpace().getKeyspaceName());

   }

}
