package fr.urssaf.zookeeper;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.test.TestingServer;

import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;

public class SampleZookeeperMutexTest {

   private TestingServer zkServer;
   private CuratorFramework zkClient;

   @Before
   public void init() throws Exception {
      // Connexion à un serveur zookeeper local
      initZookeeperServer();
      String connectionString = zkServer.getConnectString() + ","
            + zkServer.getConnectString();
      
      zkClient = ZookeeperClientFactory.getClient(connectionString, "Batch");
   }

   @After
   public void clean() {
      if (zkServer != null)
         zkServer.close();
   }

   private void initZookeeperServer() throws Exception {
      if (zkServer == null)
         zkServer = new TestingServer();
   }

   @Test
   public void testAcquire() {
      ZookeeperMutex lock = new ZookeeperMutex(zkClient, "/sequences/"
            + UUID.randomUUID());
      boolean acquired = lock.acquire(20, TimeUnit.SECONDS);
      Assert.assertTrue(acquired);
      Assert.assertTrue(lock.isObjectStillLocked(20, TimeUnit.SECONDS));
      lock.release();
   }

   @Test
   public void acquire() throws InterruptedException {
      ZookeeperMutex lock = new ZookeeperMutex(zkClient, "/sequences/"
            + UUID.randomUUID());
      boolean acquired = lock.acquire(20, TimeUnit.SECONDS);
      Assert.assertTrue(acquired);
      Assert.assertTrue(lock.isObjectStillLocked(20, TimeUnit.SECONDS));      
      zkServer.close();
      zkServer = null;
      int compteur = 0;
      while (true) {
         Thread.sleep(1000);
         boolean locked = lock.isObjectStillLocked(20, TimeUnit.MILLISECONDS);
         if (!locked)
            break;
         if (compteur++ == 100)
         {
            break;
         }
      }
   }

   @Test
   public void testDeconnexion() throws Exception {
      ZookeeperMutex lock = new ZookeeperMutex(zkClient, "/sequences/"
            + UUID.randomUUID());
      boolean acquired = lock.acquire(20, TimeUnit.SECONDS);
      Assert.assertTrue(acquired);

      // On stoppe le serveur
      zkServer.close();
      zkServer = null;

      // Normalement, on devrait recevoir un événement de déconnexion et donc
      // perdre le lock
      int compteur = 0;
      while (true) {
         Thread.sleep(100);
         boolean isLock = lock.isObjectStillLocked(20, TimeUnit.MILLISECONDS);
         System.out.println(isLock);
         if (!isLock)
            break;
         if (compteur++ == 100)
            Assert.fail("On aurait déja du avoir perdu le lock");
      }
   }

   @Test
   public void testReconnexion() throws Exception {

      ZookeeperMutex lock = new ZookeeperMutex(zkClient, "/sequences/"
            + UUID.randomUUID());
      boolean acquired = lock.acquire(20, TimeUnit.SECONDS);
      Assert.assertTrue(acquired);

      // On stop le serveur, et on le relance
      new Thread(new Runnable() {
         public void run() {
            zkServer.stop();
            try {
               Thread.sleep(1000);
               zkServer = new TestingServer(zkServer.getPort(), zkServer
                     .getTempDirectory());
            } catch (Exception e) {
               e.printStackTrace();
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
