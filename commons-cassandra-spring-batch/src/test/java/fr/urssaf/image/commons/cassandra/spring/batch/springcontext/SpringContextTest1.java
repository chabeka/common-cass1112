package fr.urssaf.image.commons.cassandra.spring.batch.springcontext;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobInstanceDao;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;


/**
 * Test la création de la DAO JobInstance par spring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)      // Pour fermer le serveur zookeeper à la fin de la classe
public class SpringContextTest1 {

   @Autowired
   private CassandraJobInstanceDao jobInstanceDao;

   @Autowired
   private CassandraServerBean cassandraServer;
   
   @After
   public final void init() throws Exception {
      // Après chaque test, on reset les données de cassandra
      cassandraServer.resetData();
   }
   
   @Test
   public final void springContextTest1 () throws Exception {
      int count = jobInstanceDao.countJobInstances("jobName");
      Assert.assertEquals(0, count);
      createJobInstance();
   }
   
   @Test
   public final void springContextTest2 () {
      // Ce 2ème test échouerait si on n'avait pas réinitalisé les données de cassandra
      int count = jobInstanceDao.countJobInstances("jobName");
      Assert.assertEquals(0, count);
      createJobInstance();
   }
   
   private void createJobInstance() {
      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      JobParameters jobParameters = new JobParameters(parameters );
      jobInstanceDao.createJobInstance("jobName", jobParameters );
   }
}
