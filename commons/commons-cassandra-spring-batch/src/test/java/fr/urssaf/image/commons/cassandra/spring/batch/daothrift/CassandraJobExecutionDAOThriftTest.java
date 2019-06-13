package fr.urssaf.image.commons.cassandra.spring.batch.daothrift;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
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
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
public class CassandraJobExecutionDAOThriftTest {

  private static final Logger LOG = LoggerFactory.getLogger(CassandraJobExecutionDAOThriftTest.class);

  private CassandraJobExecutionDaoThrift jobExecutionDao;

  private CassandraJobInstanceDaoThrift jobInstanceDao;

  private CassandraStepExecutionDaoThrift stepExecutionDao;

  private static final String MY_JOB_NAME = "job_test_execution";

  private static final String INDEX = "index";

  private TestingServer zkServer;

  private CuratorFramework zkClient;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private CassandraClientFactory ccf;

  @Before
  public void after() throws Exception {
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
    } catch (final IOException e) {
      e.printStackTrace();
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
    List<JobExecution> list = jobExecutionDao.findJobExecutions(jobInstance);
    for (final JobExecution jobExecution : list) {
      LOG.trace("\nJobExecution : {} ...", jobExecution);
    }
    Assert.assertEquals(0, list.size());
    // Création de 10 exécutions
    for (int i = 0; i < 10; i++) {
      createJobExecution(jobInstance, i);
    }
    // On vérifie qu'on les trouve toutes
    list = jobExecutionDao.findJobExecutions(jobInstance);
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
    final JobExecution jobExecution2 = jobExecutionDao.getLastJobExecution(jobInstance);
    Assert.assertEquals("test123", jobExecution2.getExitStatus().getExitDescription());
    Assert.assertEquals(2, jobExecution2.getExecutionContext().getInt(INDEX));
  }

  @Test
  public void getExecutionById() {
    final JobInstance jobInstance = getOrCreateTestJobInstance();
    final JobExecution jobExecution = createJobExecution(jobInstance, 333);
    final Long executionId = jobExecution.getId();
    final JobExecution jobExecution2 = jobExecutionDao.getJobExecution(executionId);
    Assert.assertEquals(333, jobExecution2.getExecutionContext().getInt(INDEX));
  }

  @Test
  public void findRunningJobExecutionsTest() {

    final CassandraJobExecutionDaoThrift dao = jobExecutionDao;
    final JobInstance jobInstance = getOrCreateTestJobInstance();

    // On crée 2 exécutions
    final JobExecution jobExecution1 = createJobExecution(jobInstance, 1);
    Assert.assertNotNull(jobExecution1);
    final JobExecution jobExecution2 = createJobExecution(jobInstance, 2);

    // On termine l'exécution de jobExecution2
    jobExecution2.setEndTime(new Date());
    dao.updateJobExecution(jobExecution2);

    // On vérifie qu'on ne trouve que l'exécution 1
    final Set<JobExecution> set = dao.findRunningJobExecutions(MY_JOB_NAME);
    Assert.assertEquals(1, set.size());
    final JobExecution jobExecution = set.iterator().next();
    Assert.assertEquals(1, jobExecution.getExecutionContext().getInt(INDEX));

    // On vérifie qu'on ne trouve aucun running exécution pour un job qui
    // n'existe pas
    final Set<JobExecution> set2 = dao.findRunningJobExecutions("je n'existe pas");
    Assert.assertEquals(0, set2.size());
  }

  @Test
  public void testDelete() {
    final CassandraJobExecutionDaoThrift dao = jobExecutionDao;
    final JobInstance jobInstance = getOrCreateTestJobInstance();
    // On crée 2 exécutions
    createJobExecution(jobInstance, 1);
    createJobExecution(jobInstance, 2);
    // On vérifie qu'on les trouve
    List<JobExecution> list = dao.findJobExecutions(jobInstance);
    Assert.assertEquals(2, list.size());
    list.size();
    // On les supprime
    dao.deleteJobExecutionsOfInstance(jobInstance, stepExecutionDao);
    // On vérifie qu'on ne les trouve plus
    list = dao.findJobExecutions(jobInstance);
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
    Assert.assertEquals(6, jobExecutionDao.countJobExecutions());
    Assert.assertEquals(3, jobExecutionDao.countJobExecutions("jobName1"));
    Assert.assertEquals(3, jobExecutionDao.countJobExecutions("jobName2"));
  }

  @Test
  public void testGetJobExecutions() {
    for (int i = 0; i < 3; i++) {
      final JobInstance jobInstance1 = getOrCreateTestJobInstance("jobName1");
      final JobInstance jobInstance2 = getOrCreateTestJobInstance("jobName2");
      createJobExecution(jobInstance1, i);
      createJobExecution(jobInstance2, i);
    }
    Assert.assertEquals(6, jobExecutionDao.getJobExecutions(0, 100).size());
    Assert.assertEquals(5, jobExecutionDao.getJobExecutions(1, 100).size());
    Assert.assertEquals(2, jobExecutionDao.getJobExecutions(1, 2).size());
  }

  @Test
  public void getRunningJobExecutions() {
    final CassandraJobExecutionDaoThrift dao = jobExecutionDao;

    // On crée 6 executions, dont 1 terminée
    for (int i = 0; i < 3; i++) {
      final JobInstance jobInstance1 = getOrCreateTestJobInstance("jobName1");
      final JobInstance jobInstance2 = getOrCreateTestJobInstance("jobName2");
      createJobExecution(jobInstance1, i);
      final JobExecution jobExecution2 = createJobExecution(jobInstance2, i);
      if (i == 2) {
        // On termine l'exécution de jobExecution2
        jobExecution2.setEndTime(new Date());
        dao.updateJobExecution(jobExecution2);
      }
    }
    Assert.assertEquals(5, jobExecutionDao.getRunningJobExecutions().size());
  }

  private JobInstance getOrCreateTestJobInstance() {
    return getOrCreateTestJobInstance(MY_JOB_NAME);
  }

  private JobExecution createJobExecution(final JobInstance jobInstance, final int index) {
    final JobExecution jobExecution = new JobExecution(jobInstance);
    final Map<String, Object> mapContext = new HashMap<>();
    mapContext.put("contexte1", "test1");
    mapContext.put("contexte2", 2);
    mapContext.put(INDEX, index);
    final ExecutionContext executionContext = new ExecutionContext(mapContext);
    jobExecution.setExecutionContext(executionContext);
    jobExecution.setExitStatus(new ExitStatus("123", "test123"));
    jobExecutionDao.saveJobExecution(jobExecution);
    return jobExecution;
  }

  private JobInstance getOrCreateTestJobInstance(final String jobName) {
    final CassandraJobInstanceDaoThrift dao = jobInstanceDao;
    final Map<String, JobParameter> mapJobParameters = new HashMap<>();
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
