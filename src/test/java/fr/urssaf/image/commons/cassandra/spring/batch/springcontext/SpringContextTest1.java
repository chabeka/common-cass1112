package fr.urssaf.image.commons.cassandra.spring.batch.springcontext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobInstanceDao;


/**
 * Test la création de la DAO JobInstance par spring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
public class SpringContextTest1 {

   @Autowired
   private CassandraJobInstanceDao jobInstanceDao;
   
   @Test
   public final void springContextTest () {
      int count = jobInstanceDao.countJobInstances("toto");
      Assert.assertEquals(0, count);
   }
}
