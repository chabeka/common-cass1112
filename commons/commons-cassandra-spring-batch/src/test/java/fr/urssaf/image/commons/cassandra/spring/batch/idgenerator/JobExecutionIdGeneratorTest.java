package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.spring.batch.support.JobClockSupportFactory;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
public class JobExecutionIdGeneratorTest {

  private TestingServer zkServer;

  private CuratorFramework zkClient;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private CassandraClientFactory ccf;

  @Before
  public void before() throws Exception {
    server.resetData(true, MODE_API.HECTOR);
    init();
  }

  public void init() throws Exception {
    // Connexion à un serveur zookeeper local
    initZookeeperServer();
    zkClient = ZookeeperClientFactory.getClient(zkServer.getConnectString(), "Batch");
  }

  @After
  public void clean() {
    try {
      zkServer.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
    try {
      server.resetData(true, MODE_API.HECTOR);
    }
    catch (final Exception e) {
      e.printStackTrace();
    } // EC
  }

  private void initZookeeperServer() throws Exception {
    if (zkServer == null) {
      zkServer = new TestingServer();
    }
  }

  @Test
  public void testMonoThread() {

    final JobClockSupport clockSupport = JobClockSupportFactory.createJobClockSupport(ccf.getKeyspace());

    final IdGenerator generator = new JobExecutionIdGenerator(ccf.getKeyspace(), zkClient, clockSupport);
    for (int i = 1; i < 5; i++) {
      Assert.assertEquals(i, generator.getNextId());
    }
  }

  @Test
  public void testMultiThread() throws InterruptedException {

    final JobClockSupport clockSupport = JobClockSupportFactory.createJobClockSupport(ccf.getKeyspace());

    final IdGenerator generator = new JobExecutionIdGenerator(ccf.getKeyspace(), zkClient, clockSupport);
    final Map<Long, Long> map = new ConcurrentHashMap<>();
    final SimpleThread[] threads = new SimpleThread[10];
    for (int i = 0; i < 10; i++) {
      threads[i] = new SimpleThread(generator, map);
      threads[i].start();
    }
    for (int i = 0; i < 10; i++) {
      threads[i].join();
    }
    Assert.assertEquals(50, map.size());
    for (final Entry<Long, Long> entry : map.entrySet()) {
      System.out.print(entry.getKey() + " ");
    }
  }

  private class SimpleThread extends Thread {

    IdGenerator generator;

    Map<Long, Long> map;

    public SimpleThread(final IdGenerator generator, final Map<Long, Long> map) {
      super();
      this.generator = generator;
      this.map = map;
    }

    @Override
    public void run() {
      for (int i = 0; i < 5; i++) {
        final long id = generator.getNextId();
        map.put(id, id);
      }
    }
  }

}
