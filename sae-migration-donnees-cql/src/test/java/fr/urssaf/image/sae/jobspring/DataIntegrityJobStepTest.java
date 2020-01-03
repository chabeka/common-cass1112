/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.jobspring;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraJobExecutionDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraJobInstanceDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraStepExecutionDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobInstanceIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.StepExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import fr.urssaf.image.sae.support.JobClockSupportFactory;
import fr.urssaf.image.sae.testutils.TestUtils;
import fr.urssaf.image.sae.utils.Dumper;
import me.prettyprint.hector.api.Keyspace;

/**
 * TODO (AC75095028) Description du type
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
public class DataIntegrityJobStepTest {

  /**
   * le +1 vient de la clé qui regroupe toutes les colonnes de la CF<br>
   * _all : pour JobExecutions qui permet de parcourir les exécutions tout job confondu<br>
   * _unreserved : pour jobInstanceByRunning qui permet de parcourir les job non reservés
   */

  private static final String MY_JOB_NAME = "job_test_execution";

  private Dumper dumper;

  private PrintStream sysout;

  private static int NB_STEPS = 10;

  private static int NB_STEPEXECUTION = 100;

  private TestingServer zkServer;

  private CuratorFramework zkClient;

  private CassandraJobInstanceDaoThrift jobInstanceDao;

  private CassandraJobExecutionDaoThrift jobExecutionDao;

  private CassandraStepExecutionDaoThrift stepExecutionDao;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private CassandraClientFactory ccf;


  @Autowired
  MigrationJobStep migJobStep;

  @Autowired
  MigrationJobSteps migJobSteps;

  @Autowired
  MigrationJobExecutionToJobStep migJobExeToSteps;

  @After
  public void after() throws Exception {
    server.resetData(true, MODE_API.HECTOR);
    server.resetData(false, MODE_API.DATASTAX);
  }

  @Before
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

    sysout = new PrintStream(System.out, true, "UTF-8");

    // Pour dumper sur un fichier plutôt que sur la sortie standard
    // sysout = new PrintStream("c:/temp/out.txt");
    dumper = new Dumper(keyspace, sysout);
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
  public void sliceQueryTest() throws Exception {

    // JOBSTEPS
    populateTableThrift();
    migJobStep.migrationFromThriftToCql();
    final boolean isEqBaseSteps = migJobStep.compareJobStepCql();
    Assert.assertTrue("les elements des table JobStep cql et thrift doivent être egaux", isEqBaseSteps);
    // JOBSTEPS

    migJobSteps.migrationFromThriftToCql();
    final boolean isEqBaseStepsIndex0 = migJobSteps.compareJobStepsCql();
    Assert.assertTrue("les elements des tables JobSteps cql et thrift doivent être egaux", isEqBaseStepsIndex0);

    // JOBJOBEXECUTIONTOSTEP

    migJobExeToSteps.migrationFromThriftToCql();
    final boolean isEqBaseSeptIndex1 = migJobExeToSteps.compareJobExecutionsToStep();
    Assert.assertTrue("les elements des tables JobExecutionsToStep cql et thrift doivent être egaux", isEqBaseSeptIndex1);

  }

  private void populateTableThrift() {

    // creation d'une instance de job thrift
    final JobInstance inst = TestUtils.getOrCreateTestJobInstance(MY_JOB_NAME, jobInstanceDao);
    // creation de 10 executions thrift
    for (int i = 0; i < NB_STEPEXECUTION; i++) {
      final JobExecution exe = TestUtils.saveJobExecutionThrift(inst, i, jobExecutionDao);
      // ceation de 10 steps par execution
      createTestStepsThrift(exe, i);
    }
  }

  /**
   * Création des steps pour le jobExecution passé en paramètre
   *
   * @param jobExecution
   * @param count
   *          nombre de steps à créer
   */
  private void createTestStepsThrift(final JobExecution jobExecution, final int index) {
    // Création des steps
    for (int i = 1; i <= NB_STEPS; i++) {
      final StepExecution step = new StepExecution("step" + index + i, jobExecution);
      step.setCommitCount(i);
      step.setLastUpdated(new Date(System.currentTimeMillis()));
      // Enregistrement du step
      stepExecutionDao.saveStepExecution(step);
      // sysout.println("step" + index + i);
    }
  }

}
