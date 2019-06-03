/**
 *
 */
package fr.urssaf.image.sae.jobspring;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.test.TestingServer;
import org.junit.After;
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
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionToJobStepDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepsDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daothrift.CassandraJobExecutionDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.daothrift.CassandraJobInstanceDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.daothrift.CassandraStepExecutionDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobInstanceIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.StepExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import fr.urssaf.image.sae.jobspring.dao.IGenericJobSpringDAO;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import fr.urssaf.image.sae.support.JobClockSupportFactory;
import fr.urssaf.image.sae.testutils.TestUtils;
import fr.urssaf.image.sae.utils.Dumper;
import junit.framework.Assert;
import me.prettyprint.hector.api.Keyspace;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
public class MigrationJobStepTest {

   /**
    * le +1 vient de la clé qui regroupe toutes les colonnes de la CF<br>
    * _all : pour JobExecutions qui permet de parcourir les exécutions tout job confondu<br>
    * _unreserved : pour jobInstanceByRunning qui permet de parcourir les job non reservés
    */

   private static final String MY_JOB_NAME = "job_test_execution";

   private Dumper dumper;

   private PrintStream sysout;

   private static int NB_STEPS = 10;

   private TestingServer zkServer;

   private CuratorFramework zkClient;

   private CassandraJobInstanceDaoThrift jobInstanceDao;

   private CassandraJobExecutionDaoThrift jobExecutionDao;

   private CassandraStepExecutionDaoThrift stepExecutionDao;

   @Autowired
   IJobStepsDaoCql stepDao;

   @Autowired
   IJobExecutionDaoCql daocql;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private CassandraServerBeanCql servercql;

   @Autowired
   private CassandraClientFactory ccf;

   @Autowired
   protected IGenericJobSpringDAO genericdao;

   @Autowired
   private IJobInstanceDaoCql jobInstanceDaocql;

   @Autowired
   private IJobStepExecutionDaoCql stepExecutionDaocql;

   @Autowired
   private IJobExecutionToJobStepDaoCql stepExecutionToStepDaocql;

   @Autowired
   MigrationJobStep migJobStep;

   @Autowired
   MigrationJobSteps migJobSteps;

   @Autowired
   MigrationJobExecutionToJobStep migJobExeToSteps;

   @After
   public void after() throws Exception {
      server.resetData(true);
      servercql.resetData();
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
   public void migrationFromThriftToCql() throws Exception {
      populateTableThrift();
      // JOBSTEP

      // migration de la table JOBSTEP
      migJobStep.migrationFromThriftToCql();
      // verification de la table cql
      final Iterator<JobStepCql> it = stepExecutionDaocql.findAllWithMapper();
      final List<JobStepCql> nb_Rows = Lists.newArrayList(it);
      Assert.assertEquals(100, nb_Rows.size());

      // verification de la table thrift
      final int nb_keyStepCql = dumper.getKeysCount(Constante.JOBSTEP_CFNAME);
      Assert.assertEquals(100, nb_keyStepCql);

      // JOBSTEPS

      // migration de la table JOBSTEPS
      migJobSteps.migrationFromThriftToCql();

      // verification de La table thrift
      final int nb_keySteps = dumper.getKeysCount(Constante.JOBSTEPS_CFNAME);
      Assert.assertEquals(1, nb_keySteps);

      final Iterator<GenericJobSpring> itNbTotal = genericdao.findAllByCFName(Constante.JOBSTEPS_CFNAME, ccf.getKeyspace().getKeyspaceName());
      final List<GenericJobSpring> list = Lists.newArrayList(itNbTotal);
      Assert.assertEquals(100, list.size());

      // verification de La table cql
      final Iterator<JobStepsCql> itSteps = stepDao.findAllWithMapper();
      final List<JobStepsCql> nb_Steps = Lists.newArrayList(itSteps);
      Assert.assertEquals(100, nb_Steps.size());

      // JOBJOBEXECUTIONTOSTEP

      // migration de la table JOBJOBEXECUTIONTOSTEP
      migJobExeToSteps.migrationFromThriftToCql();

      // La table cql
      final Iterator<JobExecutionToJobStepCql> itExeToSptep = stepExecutionToStepDaocql.findAllWithMapper();
      final List<JobExecutionToJobStepCql> nb_ExeToSptep = Lists.newArrayList(itExeToSptep);
      Assert.assertEquals(100, nb_ExeToSptep.size());

      // La table thrift
      final int nb_keyJobExToSteps = dumper.getKeysCount(Constante.JOBEXECUTION_TO_JOBSTEP_CFNAME);

      // on a 10 jobExecutions, la partion key de la table JOBEXECUTION_TO_JOBSTEP_CFNAME est jobexecutionid
      Assert.assertEquals(10, nb_keyJobExToSteps);

   }

   @Test
   public void migrationFromCqlTothrift() throws Exception {
      populateTableCql();

      // JOBSTEP

      // migration de la table JOBSTEP
      migJobStep.migrationFromCqlTothrift();

      final Iterator<JobStepCql> it = stepExecutionDaocql.findAllWithMapper();
      final List<JobStepCql> nb_Rows = Lists.newArrayList(it);
      Assert.assertEquals(100, nb_Rows.size());

      final int nb_keyStepCql = dumper.getKeysCount(Constante.JOBSTEP_CFNAME);
      Assert.assertEquals(100, nb_keyStepCql);

      // JOBSTEPS

      // migration de la table JOBSTEPS
      migJobSteps.migrationFromCqlTothrift();

      // verification de La table thrift
      final int nb_keySteps = dumper.getKeysCount(Constante.JOBSTEPS_CFNAME);
      Assert.assertEquals(1, nb_keySteps);

      final Iterator<GenericJobSpring> itNbTotal = genericdao.findAllByCFName(Constante.JOBSTEPS_CFNAME, ccf.getKeyspace().getKeyspaceName());
      final List<GenericJobSpring> list = Lists.newArrayList(itNbTotal);
      Assert.assertEquals(100, list.size());

      // JOBJOBEXECUTIONTOSTEP

      // migration de la table JOBSTEPS
      migJobExeToSteps.migrationFromCqlTothrift();

      // verification de La table cql
      final Iterator<JobExecutionToJobStepCql> itExeToSptep = stepExecutionToStepDaocql.findAllWithMapper();
      final List<JobExecutionToJobStepCql> nb_ExeToSptep = Lists.newArrayList(itExeToSptep);
      Assert.assertEquals(100, nb_ExeToSptep.size());

      // verification de La table thrift
      final int nb_keyJobExToSteps = dumper.getKeysCount(Constante.JOBEXECUTION_TO_JOBSTEP_CFNAME);
      Assert.assertEquals(100, nb_keyJobExToSteps);

   }

   public void populateTableCql() {
      // creation d'une instance de job cql
      final JobInstance inst = TestUtils.getOrCreateTestJobInstanceCql(MY_JOB_NAME, jobInstanceDaocql);
      // creation de 10 executions cql
      for (int i = 0; i < 10; i++) {
         final JobExecution exe = TestUtils.saveJobExecutionCql(inst, i, daocql);
         // ceation de 10 steps par execution
         createTestStepsCql(exe, i);
      }

   }

   private void populateTableThrift() {

      // creation d'une instance de job thrift
      final JobInstance inst = TestUtils.getOrCreateTestJobInstance(MY_JOB_NAME, jobInstanceDao);
      // creation de 10 executions thrift
      for (int i = 0; i < 10; i++) {
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
    *           nombre de steps à créer
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

   /**
    * Création des steps pour le jobExecution passé en paramètre
    *
    * @param jobExecution
    * @param count
    *           nombre de steps à créer
    */
   private void createTestStepsCql(final JobExecution jobExecution, final int index) {
      // Création des steps
      for (int i = 1; i <= NB_STEPS; i++) {
         final StepExecution step = new StepExecution("step" + index + i, jobExecution);
         step.setCommitCount(i);
         step.setLastUpdated(new Date(System.currentTimeMillis()));
         // Enregistrement du step
         stepExecutionDaocql.saveStepExecution(step);
      }
   }
}
