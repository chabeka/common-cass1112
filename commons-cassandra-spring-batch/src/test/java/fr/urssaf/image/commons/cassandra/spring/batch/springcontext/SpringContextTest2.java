package fr.urssaf.image.commons.cassandra.spring.batch.springcontext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobExecutionDao;


/**
 * Test la création de la DAO JobExecution par spring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)      // Pour fermer le serveur zookeeper à la fin de la classe
public class SpringContextTest2 {

   @Autowired
   private CassandraJobExecutionDao jobExecutionDao;
   
   @Test
   public final void springContextTest () {
      int count = jobExecutionDao.countJobExecutions();
      Assert.assertEquals(0, count);
   }
}
