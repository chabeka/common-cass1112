package fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import fr.urssaf.image.commons.cassandra.spring.batch.support.JobClockSupportFactory;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import junit.framework.Assert;
import me.prettyprint.hector.api.Keyspace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
public class CassandraExecutionContextDAOThriftTest {

  private CassandraJobExecutionDaoThrift jobExecutionDao;

  private CassandraJobInstanceDaoThrift jobInstanceDao;

  private CassandraExecutionContextDaoThrift executionContextDao;

  private static final String MY_JOB_NAME = "job_test_execution";

  private static final String INDEX = "index";

  private TestingServer zkServer;

  private CuratorFramework zkClient;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(CassandraExecutionContextDAOThriftTest.class);

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
    executionContextDao = new CassandraExecutionContextDaoThrift(keyspace);
  }

  @After
  public void clean() {
    zkClient.close();
    try {
      zkServer.close();
    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
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
    final Map<String, Object> mapContext = new HashMap<>();
    mapContext.put("contexte1", "newValue");
    final ExecutionContext executionContext = new ExecutionContext(mapContext);
    jobExecution.setExecutionContext(executionContext);
    executionContextDao.updateExecutionContext(jobExecution);

    // On relit l'exécution
    final JobExecution jobExecution2 = jobExecutionDao.getJobExecution(executionId);
    Assert.assertEquals("newValue", jobExecution2.getExecutionContext().getString("contexte1"));
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
