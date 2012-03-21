package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.test.TestingServer;

import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobInstanceIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.StepExecutionIdGenerator;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;

public class CassandraStepExecutionDAOTest extends
      AbstractCassandraUnit4TestCase {

   private CassandraJobExecutionDao jobExecutionDao;
   private CassandraJobInstanceDao jobInstanceDao;
   private CassandraStepExecutionDao stepExecutionDao;
   private TestingServer zkServer;

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("dataSet-commons-cassandra-spring-batch.xml");
   }

   @Before
   public void init() throws Exception {
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      CuratorFramework zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");

      // Récupération du keyspace de cassandra-unit, et création des dao
      Keyspace keyspace = getKeyspace();
      jobExecutionDao = new CassandraJobExecutionDao(keyspace, new JobExecutionIdGenerator(keyspace, zkClient));
      jobInstanceDao = new CassandraJobInstanceDao(keyspace, new JobInstanceIdGenerator(keyspace, zkClient));
      stepExecutionDao = new CassandraStepExecutionDao(keyspace, new StepExecutionIdGenerator(keyspace, zkClient));
   }

   @After
   public void clean() {
      zkServer.close();
   }
   
   private void initZookeeperServer() throws Exception {
      if (zkServer == null)
         zkServer = new TestingServer();
   }

   @Test
   public void saveAndAddStepExecutionsTest() {
      CassandraStepExecutionDao dao = stepExecutionDao;

      // Création d'une exécution
      JobExecution jobExecution = get0rCreateTestJobExecution("job");

      // Création de 2 steps
      createTestSteps(jobExecution, 2);

      // Récupération des steps
      Long jobExecutionId = jobExecution.getId();
      JobExecution jobExecution2 = jobExecutionDao
            .getJobExecution(jobExecutionId);
      dao.addStepExecutions(jobExecution2);
      Collection<StepExecution> steps = jobExecution2.getStepExecutions();

      // Vérification des steps
      Assert.assertEquals(2, steps.size());
      for (StepExecution stepExecution : steps) {
         if (stepExecution.getStepName().equals("step1")) {
            Assert.assertEquals(1, stepExecution.getCommitCount());
         } else if (stepExecution.getStepName().equals("step2")) {
            Assert.assertEquals(2, stepExecution.getCommitCount());
         } else {
            Assert.fail("Step inattendu :" + stepExecution);
         }
      }
   }

   /**
    * Création des steps pour le jobExecution passé en paramètre
    * 
    * @param jobExecution
    * @param count   nombre de steps à créer
    */
   private void createTestSteps(JobExecution jobExecution, int count) {
      // Création des steps
      List<StepExecution> steps = new ArrayList<StepExecution>(count);
      for (int i = 1; i <= count; i++) {
         StepExecution step = new StepExecution("step" + i, jobExecution);
         step.setCommitCount(i);
         step.setLastUpdated(new Date(System.currentTimeMillis()));
         // Enregistrement du step
         stepExecutionDao.saveStepExecution(step);
         steps.add(step);
      }
      jobExecution.addStepExecutions(steps);
   }

   @Test
   public void deleteTest() {
      CassandraStepExecutionDao dao = stepExecutionDao;
      // Création d'un jobExecution avec 2 steps
      JobExecution jobExecution = get0rCreateTestJobExecution("job");
      createTestSteps(jobExecution, 2);

      // Suppression des steps
      dao.deleteStepsOfExecution(jobExecution);

      // Chargement des steps
      Long jobExecutionId = jobExecution.getId();
      JobExecution jobExecution2 = jobExecutionDao
            .getJobExecution(jobExecutionId);
      dao.addStepExecutions(jobExecution2);
      Collection<StepExecution> steps = jobExecution2.getStepExecutions();

      // Vérification qu'il n'y a plus de step
      Assert.assertEquals(0, steps.size());
   }
   
   @Test
   public void testCountStepExecutions() {
      CassandraStepExecutionDao dao = stepExecutionDao;
      JobExecution je1 = createJobExecution("job1");
      createTestSteps(je1, 2);
      JobExecution je2 = createJobExecution("job2");
      createTestSteps(je2, 3);
      Assert.assertEquals(1, dao.countStepExecutions("job1", "step1"));
   }

   @Test
   public void testFindStepExecutions() {
      CassandraStepExecutionDao dao = stepExecutionDao;
      JobExecution je1 = createJobExecution("job1");
      createTestSteps(je1, 2);
      JobExecution je2 = createJobExecution("job2");
      createTestSteps(je2, 3);
      Collection<StepExecution> steps = dao.findStepExecutions("job1", "step1", 0, 100);
      Assert.assertEquals(1, steps.size());
      Collection<StepExecution> allSteps = dao.findStepExecutions("job1", "st*ep*", 0, 100);
      Assert.assertEquals(2, allSteps.size());
   }
   
   @Test
   public void testfindStepNamesForJobExecution() {
      CassandraStepExecutionDao dao = stepExecutionDao;
      JobExecution je1 = createJobExecution("job1");
      createTestSteps(je1, 2);
      JobExecution je2 = createJobExecution("job2");
      createTestSteps(je2, 3);
      Collection<String> stepNames = dao.findStepNamesForJobExecution("job1", "toto");
      Assert.assertEquals(2, stepNames.size());
      Assert.assertTrue(stepNames.contains("step1"));
      Assert.assertTrue(stepNames.contains("step2"));
      stepNames = dao.findStepNamesForJobExecution("job2", "toto");
      Assert.assertEquals(3, stepNames.size());
      Assert.assertTrue(stepNames.contains("step3"));
      
      stepNames = dao.findStepNamesForJobExecution("job1", "s*");
      Assert.assertEquals(0, stepNames.size());
   }
   
   private JobExecution get0rCreateTestJobExecution(String jobName) {
      CassandraJobExecutionDao dao = jobExecutionDao;
      JobInstance jobInstance = getTestOrCreateJobInstance(jobName);
      List<JobExecution> list = dao.findJobExecutions(jobInstance);
      if (!list.isEmpty()) return list.get(0);
      return createJobExecution(jobName);
   }

   private JobExecution createJobExecution(String jobName) {
      CassandraJobExecutionDao dao = jobExecutionDao;
      JobInstance jobInstance = getTestOrCreateJobInstance(jobName);
      JobExecution jobExecution = new JobExecution(jobInstance);
      Map<String, Object> mapContext = new HashMap<String, Object>();
      mapContext.put("contexte1", "test1");
      mapContext.put("contexte2", 2);
      ExecutionContext executionContext = new ExecutionContext(mapContext);
      jobExecution.setExecutionContext(executionContext);
      jobExecution.setExitStatus(new ExitStatus("123", "test123"));
      dao.saveJobExecution(jobExecution);
      return jobExecution;
   }
   
   private JobInstance getTestOrCreateJobInstance(String jobName) {
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
