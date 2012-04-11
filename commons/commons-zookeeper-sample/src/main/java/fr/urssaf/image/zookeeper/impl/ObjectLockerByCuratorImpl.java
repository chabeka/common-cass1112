package fr.urssaf.image.zookeeper.impl;

import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.retry.ExponentialBackoffRetry;

import fr.urssaf.image.zookeeper.ObjectLocker;
import fr.urssaf.image.zookeeper.exception.ZookeeperEx;

public class ObjectLockerByCuratorImpl implements ObjectLocker {
   private int connectionTimeout;
   private String connectionString;
   private String parentNode;
   private Builder builder = null;
   private CuratorFramework client = null;
   private int lockTimeOut = 20;

   @Override
   public boolean lockObject(String childNode) throws ZookeeperEx {
      return createLockNode(childNode);

   }

   public void init() {
      boolean initDone = false;
      while (!initDone)
         try {
            builder = CuratorFrameworkFactory.builder();
            builder.connectString(getConnectionString()).namespace(
                  getParentNode());
            builder.retryPolicy(new ExponentialBackoffRetry(100, 10));
            client = builder.build();
            client.start();
            initDone = true;
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
   }

   private boolean createLockNode(String childNode) throws ZookeeperEx {
      boolean created = false;
      while (!created)
         try {
            InterProcessMutex mutex = new InterProcessMutex(client, "/"
                  + childNode);
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

   @Override
   public boolean unlockObject(String noeud) {
      return deleteLockNode(noeud);

   }

   private boolean deleteLockNode(String nodePath) {
      boolean deleted = false;
      while (!deleted)
         try {
            InterProcessMutex mutex = new InterProcessMutex(client, "/"
                  + nodePath);
            if (mutex.isAcquiredInThisProcess())
               mutex.release();
            deleted = true;
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      return deleted;
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

}
