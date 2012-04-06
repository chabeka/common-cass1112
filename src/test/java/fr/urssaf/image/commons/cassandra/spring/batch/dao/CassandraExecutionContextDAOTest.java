package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import me.prettyprint.hector.api.Keyspace;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ExecutionContext;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.test.TestingServer;

import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobInstanceIdGenerator;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;

public class CassandraExecutionContextDAOTest extends
      AbstractCassandraUnit4TestCase {

   private CassandraJobExecutionDao jobExecutionDao;
   private CassandraJobInstanceDao jobInstanceDao;
   private CassandraExecutionContextDao executionContextDao;
   private static final String MY_JOB_NAME = "job_test_execution";
   private static final String INDEX = "index";
   private TestingServer zkServer;
   private CuratorFramework zkClient;

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("dataSet-commons-cassandra-spring-batch.xml");
   }

   @Before
   public void init() throws Exception {
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");
      
      // Récupération du keyspace de cassandra-unit, et création des dao
      Keyspace keyspace = getKeyspace();
      jobExecutionDao = new CassandraJobExecutionDao(keyspace, new JobExecutionIdGenerator(keyspace, zkClient));
      jobInstanceDao = new CassandraJobInstanceDao(keyspace, new JobInstanceIdGenerator(keyspace, zkClient));
      executionContextDao = new CassandraExecutionContextDao(keyspace);
   }
   
   @After
   public void clean() {
      zkClient.close();
      zkServer.close();
   }
   
   private void initZookeeperServer() throws Exception {
      if (zkServer == null)
         zkServer = new TestingServer();
   }


   @Test
   public void updateExecutionContext() {
      // On crée une exécution
      JobInstance jobInstance = getOrCreateTestJobInstance();
      JobExecution jobExecution = createJobExecution(jobInstance, 333);
      Long executionId = jobExecution.getId();

      // On met à jour son contexte
      Map<String, Object> mapContext = new HashMap<String, Object>();
      mapContext.put("contexte1", "newValue");
      ExecutionContext executionContext = new ExecutionContext(mapContext);
      jobExecution.setExecutionContext(executionContext);
      executionContextDao.updateExecutionContext(jobExecution);

      // On relit l'exécution
      JobExecution jobExecution2 = jobExecutionDao.getJobExecution(executionId);
      Assert.assertEquals("newValue", jobExecution2.getExecutionContext().getString("contexte1"));
   }


   private JobInstance getOrCreateTestJobInstance() {
      return getOrCreateTestJobInstance(MY_JOB_NAME);
   }

   private JobExecution createJobExecution(JobInstance jobInstance, int index) {
      JobExecution jobExecution = new JobExecution(jobInstance);
      Map<String, Object> mapContext = new HashMap<String, Object>();
      mapContext.put("contexte1", "test1");
      mapContext.put("contexte2", 2);
      mapContext.put(INDEX, index);
      ExecutionContext executionContext = new ExecutionContext(mapContext);
      jobExecution.setExecutionContext(executionContext);
      jobExecution.setExitStatus(new ExitStatus("123", "test123"));
      jobExecutionDao.saveJobExecution(jobExecution);
      return jobExecution;
   }

   private JobInstance getOrCreateTestJobInstance(String jobName) {
      CassandraJobInstanceDao dao = jobInstanceDao;
      Map<String, JobParameter> mapJobParameters = new HashMap<String, JobParameter>();
      mapJobParameters.put("premier_parametre", new JobParameter("test1"));
      mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
      mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
      JobParameters jobParameters = new JobParameters(mapJobParameters);

      JobInstance jobInstance = dao.getJobInstance(jobName, jobParameters);
      if (jobInstance == null) {
         jobInstance = dao.createJobInstance(jobName, jobParameters);
      }
      return jobInstance;
   }

}
