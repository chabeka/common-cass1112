package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl.CassandraExecutionContextDaoCqlImpl;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-cassandra-local.xml" })
public class CassandraExecutionContextDAOCqlTest {

   @Autowired
   private IJobExecutionDaoCql jobExecutionDaoCql;

   @Autowired
   private IJobInstanceDaoCql jobInstanceDaoCql;

   @Autowired
   private CassandraExecutionContextDaoCqlImpl executionContextDao;

   private static final String MY_JOB_NAME = "job_test_execution";

   private static final String INDEX = "index";

   private TestingServer zkServer;

   private CuratorFramework zkClient;

   @Autowired
   private CassandraServerBeanCql server;

   @Autowired
   private CassandraCQLClientFactory ccf;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Before
   public void init() throws Exception {
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");

   }

   @After
   public void clean() {
      zkClient.close();
      try {
         zkServer.close();
      }
      catch (final IOException e) {
         e.printStackTrace();
      }
   }

   private void initZookeeperServer() throws Exception {
      if (zkServer == null) {
         zkServer = new TestingServer();
      }
   }

   @Test
   public void updateExecutionContext() {
      // On crée une exécution
      final JobInstance jobInstance = getOrCreateTestJobInstance();
      final JobExecution jobExecution = createJobExecution(jobInstance, 333);
      final Long executionId = jobExecution.getId();

      // On met à jour son contexte
      final Map<String, Object> mapContext = new HashMap<String, Object>();
      mapContext.put("contexte1", "newValue");
      final ExecutionContext executionContext = new ExecutionContext(mapContext);
      jobExecution.setExecutionContext(executionContext);
      executionContextDao.updateExecutionContext(jobExecution);

      // On relit l'exécution
      final JobExecution jobExecution2 = jobExecutionDaoCql.getJobExecution(executionId);
      Assert.assertEquals("newValue", jobExecution2.getExecutionContext().getString("contexte1"));
   }

   private JobInstance getOrCreateTestJobInstance() {
      return getOrCreateTestJobInstance(MY_JOB_NAME);
   }

   private JobExecution createJobExecution(final JobInstance jobInstance, final int index) {
      final JobExecution jobExecution = new JobExecution(jobInstance);
      final Map<String, Object> mapContext = new HashMap<String, Object>();
      mapContext.put("contexte1", "test1");
      mapContext.put("contexte2", 2);
      mapContext.put(INDEX, index);
      final ExecutionContext executionContext = new ExecutionContext(mapContext);
      jobExecution.setExecutionContext(executionContext);
      jobExecution.setExitStatus(new ExitStatus("123", "test123"));
      jobExecutionDaoCql.saveJobExecution(jobExecution);
      return jobExecution;
   }

   private JobInstance getOrCreateTestJobInstance(final String jobName) {
      final Map<String, JobParameter> mapJobParameters = new HashMap<String, JobParameter>();
      mapJobParameters.put("premier_parametre", new JobParameter("test1"));
      mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
      mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
      final JobParameters jobParameters = new JobParameters(mapJobParameters);

      JobInstance jobInstance = jobInstanceDaoCql.getJobInstance(jobName, jobParameters);
      if (jobInstance == null) {
         jobInstance = jobInstanceDaoCql.createJobInstance(jobName, jobParameters);
      }
      return jobInstance;
   }

}
