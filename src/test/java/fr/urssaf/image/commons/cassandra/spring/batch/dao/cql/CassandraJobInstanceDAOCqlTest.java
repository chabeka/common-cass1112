package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.curator.test.TestingServer;
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

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceToJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstancesByNameDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
public class CassandraJobInstanceDAOCqlTest {

  private static final Logger LOG = LoggerFactory.getLogger(CassandraJobInstanceDAOCqlTest.class);

  @Autowired
  IJobInstanceDaoCql jobInstDaoCql;

  @Autowired
  IJobInstanceToJobExecutionDaoCql jobInstToJExDaoCql;

  @Autowired
  private IJobInstancesByNameDaoCql jobInstByNamedaocql;

  @Autowired
  private CassandraServerBean serverCql;

  @Autowired
  private CassandraClientFactory ccf;

  @Autowired
  private JobClockSupport clockSupport;

  private static final String MY_JOB_NAME = "my_job_name";

  private JobParameters myJobParameters;

  private TestingServer zkServer;

  @Before
  public void before() throws Exception {
    serverCql.resetData(true, MODE_API.DATASTAX);
    init();
  }

  public void init() throws Exception {

    // Connexion à un serveur zookeeper local
    initZookeeperServer();
    ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");

    // On crée une 1ere instance de test
    myJobParameters = getTestJobParameters();
    jobInstDaoCql.createJobInstance(MY_JOB_NAME, myJobParameters);
  }

  @Test
  public void testDeleteJobInstances() {

    final List<String> jobNames = new ArrayList<>();
    final List<Long> jobInstIds = new ArrayList<>();

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

  @Test
  public void testGetUnreservedJobInstances() {
    // Création de 3 instances supplémentaires, soit 4 instances au total
    for (int i = 1; i <= 3; i++) {
      final Map<String, JobParameter> map = new HashMap<>();
      map.put("index", new JobParameter((long) i));
      final JobParameters parameters = new JobParameters(map);
      jobInstDaoCql.createJobInstance(MY_JOB_NAME, parameters);
    }
    // Aucune instance n'est pour le moment réservée
    List<JobInstance> list = jobInstDaoCql.getUnreservedJobInstances();
    Assert.assertEquals(4, list.size());

    // On réserve puis on dé-réserve l'instance 1
    jobInstDaoCql.reserveJob(1, "myServer");
    jobInstDaoCql.reserveJob(1, Constante.UNRESERVED_KEY);
    // On réserve l'instance 2
    jobInstDaoCql.reserveJob(2, "myServer");

    // On vérifie qu'on n'a que 3 instances non réservées
    list = jobInstDaoCql.getUnreservedJobInstances();
    Assert.assertEquals(3, list.size());
    // et que l'instance n°2 n'apparait pas dans la liste
    for (final JobInstance jobInstance : list) {
      Assert.assertTrue(jobInstance.getId() != 2);
    }

    // On réserve tous les jobs
    for (int i = 1; i <= 4; i++) {
      jobInstDaoCql.reserveJob(i, "myServer");
    }
    list = jobInstDaoCql.getUnreservedJobInstances();
    Assert.assertEquals(0, list.size());

  }

  @Test
  public void testReserveJob() {
    final JobInstance jobInstance = jobInstDaoCql.getJobInstance(MY_JOB_NAME, myJobParameters);
    final long instanceId = jobInstance.getId();
    String server = jobInstDaoCql.getReservingServer(instanceId);
    Assert.assertEquals(Constante.UNRESERVED_KEY, server);

    jobInstDaoCql.reserveJob(instanceId, "serveur.domain.com");
    server = jobInstDaoCql.getReservingServer(instanceId);
    Assert.assertEquals("serveur.domain.com", server);

    // On "dé-réserve" le job
    jobInstDaoCql.reserveJob(instanceId, "");
    server = jobInstDaoCql.getReservingServer(instanceId);
    Assert.assertEquals("", server);

    final long nonExistentId = 4654654L;
    server = jobInstDaoCql.getReservingServer(nonExistentId);
    Assert.assertNull(server);

  }
  // Methode utilitaire

  private void initZookeeperServer() throws Exception {
    if (zkServer == null) {
      zkServer = new TestingServer();
    }
  }

  private JobParameters getTestJobParameters() {
    final Map<String, JobParameter> mapJobParameters = new HashMap<>();
    mapJobParameters.put("index", new JobParameter(0L));
    mapJobParameters.put("premier_parametre", new JobParameter("test1"));
    mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
    mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
    return new JobParameters(mapJobParameters);
  }
}
