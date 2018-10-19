package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceToJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstancesByNameDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepsDaoCql;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-cassandra-local.xml" })
public class CassandraJobInstanceDoaCqlTest {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraJobInstanceDoaCqlTest.class);

   @Autowired
   IJobInstanceDaoCql jobInstDaoCql;

   @Autowired
   private IJobExecutionDaoCql jobExeDaoCql;

   @Autowired
   IJobInstanceToJobExecutionDaoCql jobInstToJExDaoCql;

   @Autowired
   private IJobStepsDaoCql stepExeDaoCql;

   @Autowired
   private IJobInstancesByNameDaoCql jobInstByNamedaocql;

   private static final String MY_JOB_NAME = "my_job_name";

   private JobParameters myJobParameters;

   private TestingServer zkServer;

   private CuratorFramework zkClient;

   @Autowired
   private CassandraServerBeanCql server;

   @Autowired
   private CassandraCQLClientFactory ccf;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Before
   public void init() throws Exception {

      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");

      // On crée une 1ere instance de test
      myJobParameters = getTestJobParameters();
      jobInstDaoCql.createJobInstance(MY_JOB_NAME, myJobParameters);
   }

   @Test
   public void testDeleteJobInstances() {

      final List<String> jobNames = new ArrayList<String>();
      final List<Long> jobInstIds = new ArrayList<Long>();

      List<JobInstance> jobInstances = jobInstDaoCql.getJobInstances(MY_JOB_NAME, 0, 150);
      Assert.assertEquals(1, jobInstances.size());

      for (final JobInstance jobInstance : jobInstances) {
         jobNames.add(jobInstance.getJobName());
         jobInstIds.add(jobInstance.getId());
      }
      // Verification sur les index
      // JobInstanceByName
      Iterator<JobInstancesByNameCql> jobInstByName = jobInstByNamedaocql.findAllWithMapperById(jobNames.get(0));
      Assert.assertTrue("Un index JobInstancesByNameCql doit être créé", jobInstByName.hasNext());

      // On parcours les instances et on les supprime
      LOG.trace("Suppression des jobs :");
      for (final JobInstance jobInstance : jobInstances) {
         final long id = jobInstance.getId();
         LOG.trace(jobInstance.getId().toString());
         jobInstDaoCql.deleteJobInstance(id);
      }

      // On vérifie qu'il n'y a plus d'instance
      jobInstances = jobInstDaoCql.getJobInstances(MY_JOB_NAME, 0, 150);
      Assert.assertEquals(0, jobInstances.size());

      // Verification sur les index
      jobInstByName = jobInstByNamedaocql.findAllWithMapperById(jobNames.get(0));
      Assert.assertFalse("L'index doit être supprimer", jobInstByName.hasNext());

   }

   // Methode utilitaire

   private void initZookeeperServer() throws Exception {
      if (zkServer == null) {
         zkServer = new TestingServer();
      }
   }

   private JobParameters getTestJobParameters() {
      final Map<String, JobParameter> mapJobParameters = new HashMap<String, JobParameter>();
      mapJobParameters.put("index", new JobParameter(0L));
      mapJobParameters.put("premier_parametre", new JobParameter("test1"));
      mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
      mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
      return new JobParameters(mapJobParameters);
   }
}
