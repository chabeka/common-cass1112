package fr.urssaf.image.commons.cassandra.spring.batch.daothrift;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobInstanceIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.StepExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.support.JobClockSupportFactory;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import junit.framework.Assert;
import me.prettyprint.hector.api.Keyspace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-cassandra-local.xml" })
public class CassandraStepExecutionDAOThriftTest {

   private CassandraJobExecutionDaoThrift jobExecutionDao;

   private CassandraJobInstanceDaoThrift jobInstanceDao;

   private CassandraStepExecutionDaoThrift stepExecutionDao;

   private TestingServer zkServer;

   private CuratorFramework zkClient;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private CassandraClientFactory ccf;

   @Before
   public void before() throws Exception {
      server.resetData();
      init();
   }
  
   public void init() throws Exception {
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");

      // Récupération du keyspace de cassandra-unit, et création des dao
      final Keyspace keyspace = ccf.getKeyspace();
      final JobClockSupport clockSupport = JobClockSupportFactory.createJobClockSupport(keyspace);
      jobExecutionDao = new CassandraJobExecutionDaoThrift(keyspace, new JobExecutionIdGenerator(keyspace, zkClient, clockSupport));
      jobInstanceDao = new CassandraJobInstanceDaoThrift(keyspace, new JobInstanceIdGenerator(keyspace, zkClient, clockSupport));
      stepExecutionDao = new CassandraStepExecutionDaoThrift(keyspace, new StepExecutionIdGenerator(keyspace, zkClient, clockSupport));
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
   public void saveAndAddStepExecutionsTest() {
      final CassandraStepExecutionDaoThrift dao = stepExecutionDao;

      // Création d'une exécution
      final JobExecution jobExecution = get0rCreateTestJobExecution("job");

      // Création de 2 steps
      createTestSteps(jobExecution, 2);

      // Récupération des steps
      final Long jobExecutionId = jobExecution.getId();
      final JobExecution jobExecution2 = jobExecutionDao
                                                        .getJobExecution(jobExecutionId);
      dao.addStepExecutions(jobExecution2);
      final Collection<StepExecution> steps = jobExecution2.getStepExecutions();

      // Vérification des steps
      Assert.assertEquals(2, steps.size());
      for (final StepExecution stepExecution : steps) {
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
    * @param count
    *           nombre de steps à créer
    */
   private void createTestSteps(final JobExecution jobExecution, final int count) {
      // Création des steps
      final List<StepExecution> steps = new ArrayList<StepExecution>(count);
      for (int i = 1; i <= count; i++) {
         final StepExecution step = new StepExecution("step" + i, jobExecution);
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
      final CassandraStepExecutionDaoThrift dao = stepExecutionDao;
      // Création d'un jobExecution avec 2 steps
      final JobExecution jobExecution = get0rCreateTestJobExecution("job");
      createTestSteps(jobExecution, 2);

      // Suppression des steps
      dao.deleteStepsOfExecution(jobExecution);

      // Chargement des steps
      final Long jobExecutionId = jobExecution.getId();
      final JobExecution jobExecution2 = jobExecutionDao
                                                        .getJobExecution(jobExecutionId);
      dao.addStepExecutions(jobExecution2);
      final Collection<StepExecution> steps = jobExecution2.getStepExecutions();

      // Vérification qu'il n'y a plus de step
      Assert.assertEquals(0, steps.size());
   }

   @Test
   public void testCountStepExecutions() {
      final CassandraStepExecutionDaoThrift dao = stepExecutionDao;
      final JobExecution je1 = createJobExecution("job1");
      createTestSteps(je1, 2);
      final JobExecution je2 = createJobExecution("job2");
      createTestSteps(je2, 3);
      Assert.assertEquals(1, dao.countStepExecutions("job1", "step1"));
   }

   @Test
   public void testFindStepExecutions() {
      final CassandraStepExecutionDaoThrift dao = stepExecutionDao;
      final JobExecution je1 = createJobExecution("job1");
      createTestSteps(je1, 2);
      final JobExecution je2 = createJobExecution("job2");
      createTestSteps(je2, 3);
      final Collection<StepExecution> steps = dao.findStepExecutions("job1", "step1", 0, 100);
      Assert.assertEquals(1, steps.size());
      final Collection<StepExecution> allSteps = dao.findStepExecutions("job1", "st*ep*", 0, 100);
      Assert.assertEquals(2, allSteps.size());
   }

   @Test
   public void testfindStepNamesForJobExecution() {
      final CassandraStepExecutionDaoThrift dao = stepExecutionDao;
      final JobExecution je1 = createJobExecution("job1");
      createTestSteps(je1, 2);
      final JobExecution je2 = createJobExecution("job2");
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

   private JobExecution get0rCreateTestJobExecution(final String jobName) {
      final CassandraJobExecutionDaoThrift dao = jobExecutionDao;
      final JobInstance jobInstance = getTestOrCreateJobInstance(jobName);
      final List<JobExecution> list = dao.findJobExecutions(jobInstance);
      if (!list.isEmpty()) {
         return list.get(0);
      }
      return createJobExecution(jobName);
   }

   private JobExecution createJobExecution(final String jobName) {
      final CassandraJobExecutionDaoThrift dao = jobExecutionDao;
      final JobInstance jobInstance = getTestOrCreateJobInstance(jobName);
      final JobExecution jobExecution = new JobExecution(jobInstance);
      final Map<String, Object> mapContext = new HashMap<String, Object>();
      mapContext.put("contexte1", "test1");
      mapContext.put("contexte2", 2);
      final ExecutionContext executionContext = new ExecutionContext(mapContext);
      jobExecution.setExecutionContext(executionContext);
      jobExecution.setExitStatus(new ExitStatus("123", "test123"));
      dao.saveJobExecution(jobExecution);
      return jobExecution;
   }

   private JobInstance getTestOrCreateJobInstance(final String jobName) {
      final CassandraJobInstanceDaoThrift dao = jobInstanceDao;
      final Map<String, JobParameter> mapJobParameters = new HashMap<String, JobParameter>();
      mapJobParameters.put("premier_parametre", new JobParameter("test1"));
      mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
      mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
      final JobParameters jobParameters = new JobParameters(mapJobParameters);

      JobInstance jobInstance = dao.getJobInstance(jobName, jobParameters);
      if (jobInstance == null) {
         jobInstance = dao.createJobInstance(jobName, jobParameters);
      }
      return jobInstance;
   }

}
