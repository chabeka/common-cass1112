package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionsDaoCql;
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
   IJobExecutionsDaoCql jobExsDao;

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

   private TestingServer zkServer;

   @Before
   public void before() throws Exception {
      server.resetData(true);
      init();
   }

   @Before
   public void init() throws Exception {
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");
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
   public void runningJobExecutionsOrderTest() {
      final JobInstance jobInstance = getOrCreateTestJobInstance();

      // On crée 10 exécutions
      for (int i = 0; i < 10; i++) {
         createJobExecution(jobInstance, i);
      }
      // on s'assure que les 10 executions ont tété créé
      final int count = jobExecutionDaoCQl.count();
      Assert.assertEquals(10, count);

      // on recupère les dix executions
      final List<JobExecution> list = jobExecutionDaoCQl.getJobExecutions(MY_JOB_NAME, 0, 10);
      Assert.assertEquals(10, list.size());

      // on verife l'ordre des lignes qui ont été recupéré
      final Long j1 = list.get(0).getId();
      Assert.assertEquals(j1.intValue(), 10);
      final Long j9 = list.get(9).getId();
      Assert.assertEquals(j9.intValue(), 1);

      // on recupère 5 executions avec une limitation
      final List<JobExecution> list5 = jobExecutionDaoCQl.getJobExecutions(MY_JOB_NAME, 0, 5);
      Assert.assertEquals(5, list5.size());
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
      final JobExecution job1 = createJobExecution(jobInstance, 1);
      final JobExecution job2 = createJobExecution(jobInstance, 2);
      // On vérifie qu'on les trouve
      List<JobExecution> list = jobExecutionDaoCQl.findJobExecutions(jobInstance);
      Assert.assertEquals(2, list.size());

      // on verfie les index
      final Optional<JobExecutionsCql> optIni = jobExsDao.findByJobExecutionId(job1.getId());
      Assert.assertTrue("Un index doit etre associé", optIni.isPresent());

      // On les supprime
      jobExecutionDaoCQl.deleteJobExecutionsOfInstance(jobInstance, stepExeDaoCql);
      // On vérifie qu'on ne les trouve plus
      list = jobExecutionDaoCQl.findJobExecutions(jobInstance);
      Assert.assertEquals(0, list.size());
      // on verifie que tous les index associés ont été supprimés
      // index jobexecutions
      final Optional<JobExecutionsCql> optEnd = jobExsDao.findByJobExecutionId(job1.getId());
      Assert.assertFalse("Aucun index ne doit etre associé", optEnd.isPresent());
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

   @Test
   public void testGetOrderJobExecutions() {

      // creation d'une instance
      final JobInstance jobInstance1 = getOrCreateTestJobInstance("jobName1");
      // creation de 10 exécution liées à l'instance
      for (int i = 0; i < 10; i++) {
         createJobExecution(jobInstance1, i);
      }
      // verification nombre d'execution
      final int exescount = jobExecutionDaoCQl.count();
      Assert.assertTrue("Le nombre d'executions créé doit etre correct", 10 == exescount);
      // verification du nombre d'index
      final int insTOexecount = jobInstToJExDaoCql.count();
      Assert.assertTrue("Le nombre  d'index créé lié à l'execution doit etre correct", 10 == insTOexecount);
      // recupérations des exécutions verification
      final List<JobExecution> exes = jobExecutionDaoCQl.findJobExecutions(jobInstance1);
      Assert.assertEquals(1, exes.get(0).getId().intValue());
      Assert.assertEquals(10, exes.get(9).getId().intValue());
   }

   @Test
   public void getRunningJobExecutions() {

      // On crée 6 executions, dont 1 terminée
      for (int i = 0; i < 3; i++) {
         final JobInstance jobInstance1 = getOrCreateTestJobInstance("jobName1");
         final JobInstance jobInstance2 = getOrCreateTestJobInstance("jobName2");
         createJobExecution(jobInstance1, i);
         final JobExecution jobExecution2 = createJobExecution(jobInstance2, i);
         if (i == 2) {
            // On termine l'exécution de jobExecution2
            jobExecution2.setEndTime(new Date());
            jobExecutionDaoCQl.updateJobExecution(jobExecution2);
         }
      }
      Assert.assertEquals(5, jobExecutionDaoCQl.getRunningJobExecutions().size());
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
