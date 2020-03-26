/**
 *
 */
package fr.urssaf.image.sae.jobspring;

import java.io.PrintStream;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionsDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionsRunningDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceToJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstancesByNameDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraJobExecutionDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraJobInstanceDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobInstanceIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import fr.urssaf.image.sae.support.JobClockSupportFactory;
import fr.urssaf.image.sae.testutils.TestUtils;
import fr.urssaf.image.sae.utils.Dumper;
import me.prettyprint.hector.api.Keyspace;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
public class MigrationJobExecutionTest {

  /**
   * le +1 vient de la clé qui regroupe toutes les colonnes de la CF<br>
   * _all : pour JobExecutions qui permet de parcourir les exécutions tout job confondu<br>
   * _unreserved : pour jobInstanceByRunning qui permet de parcourir les job non reservés
   */
  private static final int CENT_UN = 101;

  private CassandraJobExecutionDaoThrift jobExecutionDao;

  private CassandraJobInstanceDaoThrift jobInstanceDao;

  private static final String MY_JOB_NAME = "job_test_execution";

  private static int NB_ROWS = 100;

  private TestingServer zkServer;

  private CuratorFramework zkClient;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  // @Qualifier("CassandraClientFactory")
  private CassandraClientFactory ccf;

  @Autowired
  MigrationJobInstance migJobInst;

  @Autowired
  MigrationJobInstancesByName migJobInstByNameIndex;

  @Autowired
  MigrationJobExecution migJobExe;

  @Autowired
  MigrationJobExecutions migJobExesIndex;

  @Autowired
  MigrationJobExecutionsRunning migJobRunIndex;

  @Autowired
  MigrationJobinstanceToJobExecution migJobInstToExeIndex;

  @Autowired
  IJobInstanceDaoCql daoinstcql;

  @Autowired
  IJobInstancesByNameDaoCql daoinstbynamecql;

  @Autowired
  IJobExecutionDaoCql daoExecql;

  @Autowired
  IJobExecutionsDaoCql daoExescql;

  @Autowired
  IJobExecutionsRunningDaoCql daoExRunningcql;

  @Autowired
  IJobInstanceToJobExecutionDaoCql daoInstToExecql;

  @Autowired
  private IJobInstanceDaoCql jobInstanceDaocql;

  Dumper dumper;

  PrintStream sysout;

  @After
  public void after() throws Exception {
    server.resetData(true, MODE_API.HECTOR);
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

    sysout = new PrintStream(System.out, true, "UTF-8");

    // Pour dumper sur un fichier plutôt que sur la sortie standard
    // sysout = new PrintStream("c:/temp/out.txt");
    dumper = new Dumper(keyspace, sysout);
  }

  private void initZookeeperServer() throws Exception {
    if (zkServer == null) {
      zkServer = new TestingServer();
    }
  }

  @Test
  public void migrationFromThriftToCql() throws Exception {
    populateTableThrift();

    // migration de la table JobExecution
    migJobInst.migrationFromThriftToCql();
    final boolean isJonInst = migJobInst.compareJobInstance();

    migJobInstByNameIndex.migrationFromThriftToCql();
    final boolean isJobInstByName = migJobInstByNameIndex.compareJobInstanceByName();

    // JOBEXECUTION
    migJobExe.migrationFromThriftToCql();
    final boolean isJobExe = migJobExe.compareJobExecution();

    // JOBEXECUTIONS
    migJobExesIndex.migrationFromThriftToCql();
    final boolean isJobExes = migJobExesIndex.compareJobExecutions();

    // JobInstanceToJobExecution
    migJobInstToExeIndex.migrationFromThriftToCql();
    final boolean isJobInstToExe = migJobInstToExeIndex.compareJobInstanceToExecution();

    // JOBEXECUTIONS_RUNNING_CFNAME
    migJobRunIndex.migrationFromThriftToCql();
    final boolean isJobRun = migJobRunIndex.compareJobExecutionsRunning();

    // final JobInstance inst = TestUtils.getOrCreateTestJobInstanceCql(MY_JOB_NAME + 1000, jobInstanceDaocql);
    //migJobExe.migrationFromThriftToCql();

  }

  @Test
  public void migrationFromCqlTothrift() throws Exception {

    populateTableCql();

    // JOBINSTANCE
    // migration de la table
    migJobInst.migrationFromCqlTothrift();

    final int nb_keyInst = dumper.getKeysCount(Constante.JOBINSTANCE_CFNAME);
    Assert.assertEquals(NB_ROWS, nb_keyInst);

    // JOBINSTANCE_BY_NAME
    // migration de la table
    migJobInstByNameIndex.migrationFromCqlTothrift();

    final int nb_keyInstByName = dumper.getKeysCount(Constante.JOBINSTANCES_BY_NAME_CFNAME);
    Assert.assertEquals(NB_ROWS, nb_keyInstByName);

    // JOBEXECUTION
    // migration de la table
    migJobExe.migrationFromCqlTothrift();

    final int nb_key = dumper.getKeysCount(Constante.JOBEXECUTION_CFNAME);
    Assert.assertEquals(NB_ROWS, nb_key);

    // JOBEXECUTIONS
    // migration de la table
    migJobExesIndex.migrationFromCqlTothrift();

    final int nb_key1 = dumper.getKeysCount(Constante.JOBEXECUTIONS_CFNAME);
    Assert.assertEquals(NB_ROWS + 1, nb_key1);

    // JOBINSTANCE_TO_JOBEXECUTION_CFNAME
    // migration de la table
    migJobInstToExeIndex.migrationFromCqlTothrift();

    final int nb_key2 = dumper.getKeysCount(Constante.JOBINSTANCE_TO_JOBEXECUTION_CFNAME);
    Assert.assertEquals(NB_ROWS, nb_key2);

    // JOBEXECUTIONS_RUNNING_CFNAME
    // migration de la table
    migJobRunIndex.migrationFromCqlTothrift();

    //
    final int nb_key3 = dumper.getKeysCount(Constante.JOBEXECUTIONS_RUNNING_CFNAME);
    Assert.assertEquals(NB_ROWS + 1, nb_key3);
  }

  // Methode UTILITAIRE

  public void populateTableCql() {

    for (int i = 0; i < NB_ROWS; i++) {
      final JobInstance inst = TestUtils.getOrCreateTestJobInstanceCql(MY_JOB_NAME + i, jobInstanceDaocql);
      final JobExecution exe = TestUtils.saveJobExecutionCql(inst, i, daoExecql);
    }
  }

  private void populateTableThrift() {

    for (int i = 0; i < NB_ROWS; i++) {
      final JobInstance inst = TestUtils.getOrCreateTestJobInstance(MY_JOB_NAME + i, jobInstanceDao);
      final JobExecution exe = TestUtils.saveJobExecutionThrift(inst, i, jobExecutionDao);
    }
  }

}
