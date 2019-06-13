package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

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

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
public class CassandraStepExecutionDAOCqlTest {

  @Autowired
  private IJobExecutionDaoCql jobExecutionDao;

  @Autowired
  private IJobInstanceDaoCql jobInstanceDao;

  @Autowired
  private IJobStepExecutionDaoCql stepExecutionDao;

  private TestingServer zkServer;

  private CuratorFramework zkClient;

  @Autowired
  private CassandraServerBeanCql server;

  @Before
  public void before() throws Exception {
    server.resetData(true);
    init();
  }

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
      server.destroy();
    } catch (final Exception e) {
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
    final IJobStepExecutionDaoCql dao = stepExecutionDao;

    // Création d'une exécution
    final JobExecution jobExecution = get0rCreateTestJobExecution("job");

    // Création de 2 steps
    createTestSteps(jobExecution, 2);

    // Récupération des steps
    final Long jobExecutionId = jobExecution.getId();
    final JobExecution jobExecution2 = jobExecutionDao.getJobExecution(jobExecutionId);
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
   *          nombre de steps à créer
   */
  private void createTestSteps(final JobExecution jobExecution, final int count) {
    // Création des steps
    final List<StepExecution> steps = new ArrayList<>(count);
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
    final IJobStepExecutionDaoCql dao = stepExecutionDao;
    // Création d'un jobExecution avec 2 steps
    final JobExecution jobExecution = get0rCreateTestJobExecution("job");
    createTestSteps(jobExecution, 2);

    // Suppression des steps
    dao.deleteStepsOfExecution(jobExecution);

    // Chargement des steps
    final Long jobExecutionId = jobExecution.getId();
    final JobExecution jobExecution2 = jobExecutionDao.getJobExecution(jobExecutionId);
    dao.addStepExecutions(jobExecution2);
    final Collection<StepExecution> steps = jobExecution2.getStepExecutions();

    // Vérification qu'il n'y a plus de step
    Assert.assertEquals(0, steps.size());
  }

  @Test
  public void testCountStepExecutions() {
    final IJobStepExecutionDaoCql dao = stepExecutionDao;
    final JobExecution je1 = createJobExecution("job1");
    createTestSteps(je1, 2);
    final JobExecution je2 = createJobExecution("job2");
    createTestSteps(je2, 3);
    Assert.assertEquals(1, dao.countStepExecutions("job1", "step1"));
  }

  @Test
  public void testFindStepExecutions() {
    final JobExecution je1 = createJobExecution("job1");
    createTestSteps(je1, 2);
    final JobExecution je2 = createJobExecution("job2");
    createTestSteps(je2, 3);
    final Collection<StepExecution> steps = stepExecutionDao.findStepExecutions("job1", "step1", 0, 100);
    Assert.assertEquals(1, steps.size());
    final Collection<StepExecution> allSteps = stepExecutionDao.findStepExecutions("job1", "st*ep*", 0, 100);
    Assert.assertEquals(2, allSteps.size());
  }

  @Test
  public void testfindStepNamesForJobExecution() {
    final JobExecution je1 = createJobExecution("job1");
    createTestSteps(je1, 2);
    final JobExecution je2 = createJobExecution("job2");
    createTestSteps(je2, 3);
    Collection<String> stepNames = stepExecutionDao.findStepNamesForJobExecution("job1", "toto");
    Assert.assertEquals(2, stepNames.size());
    Assert.assertTrue(stepNames.contains("step1"));
    Assert.assertTrue(stepNames.contains("step2"));
    stepNames = stepExecutionDao.findStepNamesForJobExecution("job2", "toto");
    Assert.assertEquals(3, stepNames.size());
    Assert.assertTrue(stepNames.contains("step3"));

    stepNames = stepExecutionDao.findStepNamesForJobExecution("job1", "s*");
    Assert.assertEquals(0, stepNames.size());
  }

  private JobExecution get0rCreateTestJobExecution(final String jobName) {
    final JobInstance jobInstance = getTestOrCreateJobInstance(jobName);
    final List<JobExecution> list = jobExecutionDao.findJobExecutions(jobInstance);
    if (!list.isEmpty()) {
      return list.get(0);
    }
    return createJobExecution(jobName);
  }

  private JobExecution createJobExecution(final String jobName) {
    final JobInstance jobInstance = getTestOrCreateJobInstance(jobName);
    final JobExecution jobExecution = new JobExecution(jobInstance);
    final Map<String, Object> mapContext = new HashMap<>();
    mapContext.put("contexte1", "test1");
    mapContext.put("contexte2", 2);
    final ExecutionContext executionContext = new ExecutionContext(mapContext);
    jobExecution.setExecutionContext(executionContext);
    jobExecution.setExitStatus(new ExitStatus("123", "test123"));
    jobExecutionDao.saveJobExecution(jobExecution);
    return jobExecution;
  }

  private JobInstance getTestOrCreateJobInstance(final String jobName) {
    final Map<String, JobParameter> mapJobParameters = new HashMap<>();
    mapJobParameters.put("premier_parametre", new JobParameter("test1"));
    mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
    mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
    final JobParameters jobParameters = new JobParameters(mapJobParameters);

    JobInstance jobInstance = jobInstanceDao.getJobInstance(jobName, jobParameters);
    if (jobInstance == null) {
      jobInstance = jobInstanceDao.createJobInstance(jobName, jobParameters);
    }
    return jobInstance;
  }

}
