package fr.urssaf.image.commons.zookeeper;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperMutexTest {

  private TestingServer zkServer;
  private CuratorFramework zkClient;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ZookeeperMutexTest.class);

  @Before
  public void init() throws Exception {
    // Connexion à un serveur zookeeper local
    initZookeeperServer();
    final String connectionString = zkServer.getConnectString() + "," + zkServer.getConnectString();
    zkClient = ZookeeperClientFactory.getClient(connectionString, "Batch");
  }

  @After
  public void clean() {
    if (zkServer != null) {
      try {
        zkServer.close();
      }
      catch (final IOException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }

  private void initZookeeperServer() throws Exception {
    if (zkServer == null) {
      zkServer = new TestingServer();
    }
  }

  @Test
  public void testAcquire() {
    final ZookeeperMutex lock = new ZookeeperMutex(zkClient, "/sequences/" + UUID.randomUUID());
    final boolean acquired = lock.acquire(20, TimeUnit.SECONDS);
    Assert.assertTrue(acquired);
    Assert.assertTrue(lock.isObjectStillLocked(20, TimeUnit.SECONDS));
    lock.release();
  }

  @Test
  public void testDeconnexion() throws Exception {
    final ZookeeperMutex lock = new ZookeeperMutex(zkClient, "/sequences/" + UUID.randomUUID());
    final boolean acquired = lock.acquire(20, TimeUnit.SECONDS);
    Assert.assertTrue(acquired);

    // On stoppe le serveur
    zkServer.close();
    zkServer = null;

    // Normalement, on devrait recevoir un événement de déconnexion et donc
    // perdre le lock
    int compteur = 0;
    while (true) {
      Thread.sleep(100);
      if (!lock.isObjectStillLocked(20, TimeUnit.MILLISECONDS)) {
        break;
      }
      if (compteur++ == 100) {
        Assert.fail("On aurait déja du avoir perdu le lock");
      }
    }
  }

  @Test
  public void testReconnexion() throws Exception {

    final ZookeeperMutex lock = new ZookeeperMutex(zkClient, "/sequences/" + UUID.randomUUID());
    final boolean acquired = lock.acquire(20, TimeUnit.SECONDS);
    Assert.assertTrue(acquired);

    // On stop le serveur, et on le relance
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          zkServer.stop();
        }
        catch (final IOException e1) {
          throw new RuntimeException(e1.getMessage());
        }
        try {
          Thread.sleep(1000);
          zkServer = new TestingServer(zkServer.getPort(), zkServer.getTempDirectory());
        } catch (final Exception e) {
          throw new RuntimeException(e.getMessage());
        }
      }
    }).start();
    // Normalement, on devrait recevoir un événement de suspension, puis un
    // événement de reconnexion
    // perdre le lock
    Thread.sleep(800);
    // On n'attend pas assez longtemps pour être reconnecté
    boolean locked = lock.isObjectStillLocked(1, TimeUnit.MILLISECONDS);
    Assert.assertEquals(false, locked);

    // On attend la reconnexion
    locked = lock.isObjectStillLocked(20, TimeUnit.SECONDS);
    Assert.assertEquals(true, locked);
  }


}
