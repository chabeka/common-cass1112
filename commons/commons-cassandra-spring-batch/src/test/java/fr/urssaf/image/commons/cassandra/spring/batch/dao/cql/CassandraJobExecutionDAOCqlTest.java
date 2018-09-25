package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceToJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-cassandra-local.xml" })
public class CassandraJobExecutionDAOCqlTest {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraJobExecutionDAOCqlTest.class);

   @Autowired
   IJobExecutionDaoCql jobExecutionDaoCQl;

   @Autowired
   IJobInstanceDaoCql jobInstDaoCql;

   @Autowired
   IJobStepExecutionDaoCql stepExeDaoCql;

   @Autowired
   IJobInstanceToJobExecutionDaoCql jobInstToJExDaoCql;

   private static final String MY_JOB_NAME = "job_test_execution";

   private static final String INDEX = "index";

   @Autowired
   private CassandraServerBeanCql server;

   @Autowired
   private CassandraCQLClientFactory ccf;

   private CuratorFramework zkClient;

   private TestingServer zkServer;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Before
   public void init() throws Exception {
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");
      if (!server.getStartLocal()) {
         // TestUltis.truncateBase(jobInstToJExDaoCql.getSession());
      }
   }

   private void initZookeeperServer() throws Exception {
      if (zkServer == null) {
         zkServer = new TestingServer();
      }
   }

   @Test
   public void findJobExecutionsTest() {
      // Création d'une instance
      final JobInstance jobInstance = getOrCreateTestJobInstance();

      LOG.trace("\nJobInstanceId : {}", jobInstance.getId());
      // On vérifie qu'elle n'a aucune exécution
      List<JobExecution> list = jobExecutionDaoCQl.findJobExecutions(jobInstance);
      for (final JobExecution jobExecution : list) {
         LOG.trace("\nJobExecution : {} ...", jobExecution);
      }
      // Assert.assertEquals(0, list.size());
      // Création de 10 exécutions
      for (int i = 10000; i < 10010; i++) {
         createJobExecution(jobInstance, i);
      }
      // On vérifie qu'on les trouve toutes
      list = jobExecutionDaoCQl.findJobExecutions(jobInstance);
      Assert.assertEquals(10, list.size());
   }

   @Test
   public void getLastExecutionTest() throws InterruptedException {
      // Création d'une instance
      final JobInstance jobInstance = getOrCreateTestJobInstance();

      // Création de 2 exécution. On attend 100ms entre les 2 création pour être
      // sur que
      // la 2eme a une date plus récente que la 1ere
      for (int i = 0; i <= 2; i++) {
         createJobExecution(jobInstance, i);
         Thread.sleep(100);
      }
      // On vérifie qu'on trouve bien la plus récente
      final JobExecution jobExecution2 = jobExecutionDaoCQl.getLastJobExecution(jobInstance);
      Assert.assertEquals("test123", jobExecution2.getExitStatus().getExitDescription());
      Assert.assertEquals(2, jobExecution2.getExecutionContext().getInt(INDEX));
   }

   @Test
   public void getExecutionById() {
      final JobInstance jobInstance = getOrCreateTestJobInstance();
      final JobExecution jobExecution = createJobExecution(jobInstance, 333);
      final Long executionId = jobExecution.getId();
      final JobExecution jobExecution2 = jobExecutionDaoCQl.getJobExecution(executionId);
      Assert.assertEquals(333, jobExecution2.getExecutionContext().getInt(INDEX));
   }

   @Test
   public void findRunningJobExecutionsTest() {

      final JobInstance jobInstance = getOrCreateTestJobInstance();

      // On crée 2 exécutions
      final JobExecution jobExecution1 = createJobExecution(jobInstance, 1);
      Assert.assertNotNull(jobExecution1);
      final JobExecution jobExecution2 = createJobExecution(jobInstance, 2);
      Set<JobExecution> set = jobExecutionDaoCQl.findRunningJobExecutions(MY_JOB_NAME);
      Assert.assertEquals(2, set.size());

      // On termine l'exécution de jobExecution2
      jobExecution2.setEndTime(new Date());
      jobExecutionDaoCQl.updateJobExecution(jobExecution2);

      // On vérifie qu'on ne trouve que l'exécution 1
      set = jobExecutionDaoCQl.findRunningJobExecutions(MY_JOB_NAME);
      Assert.assertEquals(1, set.size());
      final JobExecution jobExecution = set.iterator().next();
      Assert.assertEquals(1, jobExecution.getExecutionContext().getInt(INDEX));

      // On vérifie qu'on ne trouve aucun running exécution pour un job qui
      // n'existe pas
      final Set<JobExecution> set2 = jobExecutionDaoCQl.findRunningJobExecutions("je n'existe pas");
      Assert.assertEquals(0, set2.size());
   }

   @Test
   public void testDelete() {

      final JobInstance jobInstance = getOrCreateTestJobInstance();
      // On crée 2 exécutions
      createJobExecution(jobInstance, 1);
      createJobExecution(jobInstance, 2);
      // On vérifie qu'on les trouve
      List<JobExecution> list = jobExecutionDaoCQl.findJobExecutions(jobInstance);
      Assert.assertEquals(2, list.size());
      // On les supprime
      jobExecutionDaoCQl.deleteJobExecutionsOfInstance(jobInstance, stepExeDaoCql);
      // On vérifie qu'on ne les trouve plus
      list = jobExecutionDaoCQl.findJobExecutions(jobInstance);
      Assert.assertEquals(0, list.size());

   }

   @Test
   public void testCountJobExecutions() {
      for (int i = 0; i < 3; i++) {
         final JobInstance jobInstance1 = getOrCreateTestJobInstance("jobName1");
         final JobInstance jobInstance2 = getOrCreateTestJobInstance("jobName2");
         createJobExecution(jobInstance1, i);
         createJobExecution(jobInstance2, i);
      }
      Assert.assertEquals(6, jobExecutionDaoCQl.countJobExecutions());
      Assert.assertEquals(3, jobExecutionDaoCQl.countJobExecutions("jobName1"));
      Assert.assertEquals(3, jobExecutionDaoCQl.countJobExecutions("jobName2"));
   }

   @Test
   public void testGetJobExecutions() {
      for (int i = 0; i < 3; i++) {
         final JobInstance jobInstance1 = getOrCreateTestJobInstance("jobName1");
         final JobInstance jobInstance2 = getOrCreateTestJobInstance("jobName2");
         createJobExecution(jobInstance1, i);
         createJobExecution(jobInstance2, i);
      }
      final Iterator<JobInstanceCql> it = jobInstDaoCql.findAllWithMapper();
      while (it.hasNext()) {
         final String string = it.next().getJobInstanceId().toString();
         LOG.info(string);
      }
      Assert.assertEquals(6, jobExecutionDaoCQl.getJobExecutions(0, 100).size());
      Assert.assertEquals(5, jobExecutionDaoCQl.getJobExecutions(1, 100).size());
      Assert.assertEquals(2, jobExecutionDaoCQl.getJobExecutions(1, 2).size());
   }
   //

   //
   private JobExecution createJobExecution(final JobInstance jobInstance, final int index) {
      final JobExecution jobExecution = new JobExecution(jobInstance);
      final Map<String, Object> mapContext = new HashMap<String, Object>();
      mapContext.put("contexte1", "test1");
      mapContext.put("contexte2", 2);
      mapContext.put(INDEX, index);
      final ExecutionContext executionContext = new ExecutionContext(mapContext);
      jobExecution.setExecutionContext(executionContext);
      jobExecution.setExitStatus(new ExitStatus("123", "test123"));
      jobExecutionDaoCQl.saveJobExecution(jobExecution);
      return jobExecution;
   }

   private JobInstance getOrCreateTestJobInstance() {
      return getOrCreateTestJobInstance(MY_JOB_NAME);
   }

   private JobInstance getOrCreateTestJobInstance(final String jobName) {

      final Map<String, JobParameter> mapJobParameters = new HashMap<String, JobParameter>();
      mapJobParameters.put("premier_parametre", new JobParameter("test1"));
      mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
      mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
      final JobParameters jobParameters = new JobParameters(mapJobParameters);

      // on verifie si le job existe déja ou pas
      JobInstance jobInstance = jobInstDaoCql.getJobInstance(jobName, jobParameters);
      if (jobInstance == null) {
         jobInstance = jobInstDaoCql.createJobInstance(jobName, jobParameters);
      }
      return jobInstance;
   }
}
