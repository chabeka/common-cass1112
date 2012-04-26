package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import me.prettyprint.hector.api.Keyspace;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.test.TestingServer;

import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.JobInstanceIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.StepExecutionIdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.support.JobClockSupportFactory;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;

public class CassandraJobInstanceDoaTest extends
      AbstractCassandraUnit4TestCase {

   private static final Logger LOG = LoggerFactory
         .getLogger(CassandraJobExecutionDAOTest.class);
   private CassandraJobExecutionDao jobExecutionDao;
   private CassandraJobInstanceDao jobInstanceDao;
   private CassandraStepExecutionDao stepExecutionDao;
   private static final String MY_JOB_NAME = "my_job_name";
   private JobParameters myJobParameters;
   private TestingServer zkServer;
   private CuratorFramework zkClient;
   
   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("dataSet-commons-cassandra-spring-batch.xml");
   }

   @Before
   public void init() throws Exception {
      
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");
      
      // Récupération du keyspace de cassandra-unit et création des dao
      Keyspace keyspace = getKeyspace();
      JobClockSupport clockSupport =  JobClockSupportFactory.createJobClockSupport(keyspace);
      jobExecutionDao = new CassandraJobExecutionDao(keyspace, new JobExecutionIdGenerator(keyspace, zkClient,clockSupport));
      jobInstanceDao = new CassandraJobInstanceDao(keyspace, new JobInstanceIdGenerator(keyspace, zkClient,clockSupport));
      stepExecutionDao = new CassandraStepExecutionDao(keyspace, new StepExecutionIdGenerator(keyspace, zkClient,clockSupport));

      // On crée une 1ere instance de test
      myJobParameters = getTestJobParameters();
      jobInstanceDao.createJobInstance(MY_JOB_NAME, myJobParameters);
   }

   @After
   public void clean() {
      zkClient.close();
      zkServer.close();
   }
   
   private void initZookeeperServer() throws Exception {
      if (zkServer == null)
         zkServer = new TestingServer();
   }
   
   @Test
   public void testGetJobInstanceByNameAndParameters() {
      // On vérifie qu'on trouve l'instance de test connaissant son nom et ses
      // paramètres
      JobInstance jobInstance = jobInstanceDao.getJobInstance(MY_JOB_NAME,
            myJobParameters);
      LOG.trace("Instance trouvée :" + jobInstance);
      validateJobInstance(jobInstance);
   }

   @Test
   public void testGetJobInstanceById() {
      JobInstance jobInstance = jobInstanceDao.getJobInstance(MY_JOB_NAME,
            myJobParameters);
      long jobInstanceId = jobInstance.getId();

      // On vérifie qu'on trouve l'instance de test par son id
      JobInstance jobInstance2 = jobInstanceDao.getJobInstance(jobInstanceId);
      validateJobInstance(jobInstance2);
   }

   @Test
   public void testGetJobInstanceByNonExistentName() {
      JobInstance jobInstance3 = jobInstanceDao.getJobInstance(MY_JOB_NAME,
            myJobParameters);
      validateJobInstance(jobInstance3);

      JobInstance jobInstance4 = jobInstanceDao.getJobInstance(
            "job qui n'existe pas", myJobParameters);
      Assert.assertNull(jobInstance4);
   }

   @Test
   @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
   public void testGetJobInstances() {

      // On vérifie d'abord qu'on ne retrouve que la 1ere instance de test
      List<JobInstance> jobInstances = jobInstanceDao.getJobInstances(
            MY_JOB_NAME, 0, 150);
      LOG.trace("Liste des jobs :");
      for (JobInstance jobInstance : jobInstances) {
         LOG.trace(jobInstance.getId().toString());
      }
      validateJobInstance(jobInstances.get(0));
      Assert.assertEquals(1, jobInstances.size());

      // Création de 10 instances supplémentaires, soit 11 instances au total
      for (int i = 1; i <= 10; i++) {
         Map<String, JobParameter> map = new HashMap<String, JobParameter>();
         map.put("index", new JobParameter((long) i));
         JobParameters parameters = new JobParameters(map);
         jobInstanceDao.createJobInstance(MY_JOB_NAME, parameters);
      }

      // On vérifie qu'on arrive bien à récupérer les 11 instances, en paginant
      List<JobInstance> jobInstances1 = jobInstanceDao.getJobInstances(
            MY_JOB_NAME, 0, 5);
      List<JobInstance> jobInstances2 = jobInstanceDao.getJobInstances(
            MY_JOB_NAME, 5, 150);
      Assert.assertEquals(5, jobInstances1.size());
      Assert.assertEquals(6, jobInstances2.size());
      jobInstances1.addAll(jobInstances2);
      Set<Long> set = new HashSet<Long>();
      for (JobInstance jobInstance : jobInstances1) {
         set.add(jobInstance.getJobParameters().getLong("index"));
      }
      Assert.assertEquals(11, set.size());
   }

   @Test
   public void testDeleteJobInstances() {
      CassandraJobInstanceDao dao = jobInstanceDao;

      // On parcours les instances et on les supprime
      List<JobInstance> jobInstances = dao.getJobInstances(MY_JOB_NAME, 0, 150);
      LOG.trace("Suppression des jobs :");
      for (JobInstance jobInstance : jobInstances) {
         long id = jobInstance.getId();
         LOG.trace(jobInstance.getId().toString());
         dao.deleteJobInstance(id, jobExecutionDao, stepExecutionDao);
      }

      // On vérifie qu'il n'y a plus d'instance
      jobInstances = dao.getJobInstances(MY_JOB_NAME, 0, 150);
      Assert.assertEquals(0, jobInstances.size());
   }

   @Test
   public void testGetJobNames() {
      CassandraJobInstanceDao dao = jobInstanceDao;

      // On vérifie qu'on ne trouve qu'un seul jobname, et le bon
      List<String> jobNames = dao.getJobNames();
      LOG.trace("Liste des jobNames :");
      for (String name : jobNames) {
         LOG.trace(name);
      }
      Assert.assertEquals(1, jobNames.size());
      Assert.assertEquals(MY_JOB_NAME, jobNames.get(0));

      // Création de 10 instances supplémentaires, avec des noms différents
      for (int i = 0; i < 10; i++) {
         dao.createJobInstance(MY_JOB_NAME + i, myJobParameters);
      }
      // On vérifie qu'on trouve bien 11 jobnames
      jobNames = dao.getJobNames();
      Assert.assertEquals(11, jobNames.size());
   }
   
   @Test
   public void testCountJobInstances() {
      // Création de 3 instances supplémentaires, soit 4 instances au total
      for (int i = 1; i <= 3; i++) {
         Map<String, JobParameter> map = new HashMap<String, JobParameter>();
         map.put("index", new JobParameter((long) i));
         JobParameters parameters = new JobParameters(map);
         jobInstanceDao.createJobInstance(MY_JOB_NAME, parameters);
      }
      Assert.assertEquals(4, jobInstanceDao.countJobInstances(MY_JOB_NAME));
   }

   @Test
   public void testReserveJob() {
      JobInstance jobInstance = jobInstanceDao.getJobInstance(MY_JOB_NAME,
            myJobParameters);
      long instanceId = jobInstance.getId();
      String server = jobInstanceDao.getReservingServer(instanceId);
      Assert.assertEquals("", server);
      
      jobInstanceDao.reserveJob(instanceId, "serveur.domain.com");
      server = jobInstanceDao.getReservingServer(instanceId);
      Assert.assertEquals("serveur.domain.com", server);
      
      // On "dé-réserve" le job
      jobInstanceDao.reserveJob(instanceId, "");
      server = jobInstanceDao.getReservingServer(instanceId);
      Assert.assertEquals("", server);

      long nonExistentId = 4654654L;
      server = jobInstanceDao.getReservingServer(nonExistentId);
      Assert.assertNull(server);

   }
   
   @Test
   public void testGetUnreservedJobInstances() {
      // Création de 3 instances supplémentaires, soit 4 instances au total
      for (int i = 1; i <= 3; i++) {
         Map<String, JobParameter> map = new HashMap<String, JobParameter>();
         map.put("index", new JobParameter((long) i));
         JobParameters parameters = new JobParameters(map);
         jobInstanceDao.createJobInstance(MY_JOB_NAME, parameters);
      }
      // Aucune instance n'est pour le moment réservée
      List<JobInstance> list = jobInstanceDao.getUnreservedJobInstances();
      Assert.assertEquals(4, list.size());
      // On vérifie qu'on a les instances dans le bon ordre
      for (int i=0; i < 4; i++) {
         Assert.assertEquals(i, list.get(i).getJobParameters().getLong("index"));
      }
      
      // On réserve puis on dé-réserve l'instance 1
      jobInstanceDao.reserveJob(1, "myServer");
      jobInstanceDao.reserveJob(1, "");
      // On réserve l'instance 2
      jobInstanceDao.reserveJob(2, "myServer");

      // On vérifie qu'on n'a que 3 instances non réservées 
      list = jobInstanceDao.getUnreservedJobInstances();
      Assert.assertEquals(3, list.size());
      // et que l'instance n°2 n'apparait pas dans la liste
      for (JobInstance jobInstance : list) {
         Assert.assertTrue(jobInstance.getId() != 2);
      }
      
      // On réserve tous les jobs
      for (int i = 1; i <= 4; i++) {
         jobInstanceDao.reserveJob(i, "myServer");
      }
      list = jobInstanceDao.getUnreservedJobInstances();
      Assert.assertEquals(0, list.size());
      
   }
   
   private JobParameters getTestJobParameters() {
      Map<String, JobParameter> mapJobParameters = new HashMap<String, JobParameter>();
      mapJobParameters.put("index", new JobParameter(0L));
      mapJobParameters.put("premier_parametre", new JobParameter("test1"));
      mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
      mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
      return new JobParameters(mapJobParameters);
   }

   private void validateJobInstance(JobInstance jobInstance) {
      long jobInstanceId = jobInstance.getId();
      Assert.assertTrue(jobInstanceId != 0);
      Assert.assertEquals(MY_JOB_NAME, jobInstance.getJobName());
      Assert.assertEquals("test1", jobInstance.getJobParameters().getString(
            "premier_parametre"));
      Assert.assertEquals("test2", jobInstance.getJobParameters().getString(
            "deuxieme_parametre"));
      Assert.assertEquals(122L, jobInstance.getJobParameters().getLong(
            "troisieme_parametre"));
   }

}
