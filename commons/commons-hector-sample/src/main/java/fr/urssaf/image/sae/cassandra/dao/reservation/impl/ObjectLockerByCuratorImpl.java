package fr.urssaf.image.sae.cassandra.dao.reservation.impl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;
import org.springframework.util.Assert;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.ExponentialBackoffRetry;

import fr.urssaf.image.sae.cassandra.dao.exception.ZookeeperEx;
import fr.urssaf.image.sae.cassandra.dao.reservation.ObjectLocker;

/**
 * Exemple d'implémentation de Curator pour réserver des job dans Zookeeper.
 * 
 */
public class ObjectLockerByCuratorImpl implements ObjectLocker {
   private int connectionTimeout;
   private String connectionString;
   private String parentNode;
   private Builder builder = null;
   private CuratorFramework client = null;
   private int lockTimeOut = 20;

   public boolean isLockedObject(String noeud) {
      return false;
   }

   public boolean lockObject(String childNode) throws ZookeeperEx {
      String nodePath = createNodePath(childNode);
      return createLockNode(childNode, nodePath);

   }

   public void init() {
      boolean initDone = false;
      while (!initDone)
         try {
            builder = CuratorFrameworkFactory.builder();
            builder.connectString("cer69imageint9.cer69.recouv:2181")
                  .namespace("saeJobs");
            builder.retryPolicy(new ExponentialBackoffRetry(100, 10));
            client = builder.build();
            client.start();
            initDone = true;
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
   }

   private boolean createLockNode(String childNode, String nodePath)
         throws ZookeeperEx {
      boolean created = false;
      // Il faut obtenir un lock avant d'accéder à la séquence
      final LockInfo lockInfo = new LockInfo();

      // Conformément aux recommandations d'utilisation de la classe de lock
      // (https://github.com/Netflix/curator/wiki/Shared-lock)
      // On capte les événements de déconnexion, car en cas de déconnexion, on
      // n'est plus sûr d'avoir le lock
      ConnectionStateListener stateListener = new ConnectionStateListener() {
         @Override
         public void stateChanged(CuratorFramework client,
               ConnectionState newState) {
            switch (newState) {
            case SUSPENDED:
            case LOST:
               lockInfo.lockOk = false;
               break;
            case RECONNECTED:
               lockInfo.lockOk = true;
               break;
            default:
            }
         }
      };
      client.getConnectionStateListenable().addListener(stateListener);
      while (!created)
         try {
            // client.create().forPath(childNode);
            InterProcessMutex mutex = new InterProcessMutex(client,
                  "/sequences/" + childNode);
            if (!mutex.acquire(lockTimeOut, TimeUnit.SECONDS)) {
               throw new ZookeeperEx(
                     "Erreur lors de la tentative d'acquisition du lock pour la séquence "
                           + childNode
                           + " : on n'a pas obtenu le lock au bout de 20 secondes.");
            }

            created = true;
         } catch (KeeperException.NodeExistsException e) {
            throw new ZookeeperEx("le noeud " + childNode
                  + " est réservé par un autre serveur ");
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      return created;
   }

   public boolean unlockObject(String noeud) {
      return deleteLockNode(noeud);
   }

   private boolean deleteLockNode(String nodePath) {
      boolean deleted = false;
      while (!deleted)
         try {
            InterProcessMutex mutex = new InterProcessMutex(client,
                  "/sequences/" + nodePath);
            if (mutex.isAcquiredInThisProcess())
               mutex.release();
            deleted = true;
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      return deleted;
   }

   private String createNodePath(String jobId) {
      Assert.notNull(jobId, "jobId should not be null");
      Assert.notNull(getParentNode(), "the root node should not be null");
      String nodePath = createPath(getParentNode(), jobId);
      return nodePath;
   }

   private String createPath(String mainNode, String subNode) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("/");
      stringBuilder.append(mainNode);
      stringBuilder.append("/");
      stringBuilder.append(subNode);
      return stringBuilder.toString();
   }

   /**
    * @return the connectionTimeout
    */
   public int getConnectionTimeout() {
      return connectionTimeout;
   }

   /**
    * @param connectionTimeout
    *           the connectionTimeout to set
    */
   public void setConnectionTimeout(int connectionTimeout) {
      this.connectionTimeout = connectionTimeout;
   }

   /**
    * @return the connectionString
    */
   public String getConnectionString() {
      return connectionString;
   }

   /**
    * @param connectionString
    *           the connectionString to set
    */
   public void setConnectionString(String connectionString) {
      this.connectionString = connectionString;
   }

   /**
    * @return the parentNode
    */
   public String getParentNode() {
      return parentNode;
   }

   /**
    * @param parentNode
    *           the parentNode to set
    */
   public void setParentNode(String parentNode) {
      this.parentNode = parentNode;
   }

   private static class LockInfo {
      public boolean lockOk = true;
   }
}
