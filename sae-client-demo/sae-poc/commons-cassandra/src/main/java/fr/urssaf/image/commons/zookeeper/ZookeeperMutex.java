package fr.urssaf.image.commons.zookeeper;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;

/**
 * Classe Utilitaire pour créer un mutex.
 * On utilise l'InterProcessMutex de Curator, mais on capte en plus les événements
 * de déconnexion, pour être sûr qu'on ne perd pas le lock
 * @see com.netflix.curator.framework.recipes.locks.InterProcessMutex
 */
public class ZookeeperMutex {
   private static final Logger LOG = LoggerFactory
         .getLogger(ZookeeperMutex.class);
   private LockInfo lockInfo = new LockInfo();
   private final InterProcessMutex mutex;
   private ConnectionStateListener stateListener;
   private final CuratorFramework curatorClient;
   private String mutexPath;
   private volatile ConnectionState connectionState;
   
   /**
    * Constructeur
    * @param curatorClient client curator
    * @param mutexPath the path to lock
    */
   public ZookeeperMutex(final CuratorFramework curatorClient, final String mutexPath) {
      Assert.notNull(curatorClient);
      Assert.notNull(mutexPath);
      this.curatorClient = curatorClient;
      this.mutexPath = mutexPath;
      stateListener = new ConnectionStateListener() {
         @Override
         public void stateChanged(CuratorFramework client,
               ConnectionState newState) {
            connectionState = newState;
            switch (newState) {
            case SUSPENDED:
            case LOST:
               lockInfo.lockOk = false;
               break;
            case RECONNECTED:
               lockInfo.lockOk = true;
               notifyFromWatcher();
               break;
            default:
            }
         }
      };
      // Conformément aux recommandations d'utilisation de la classe de lock
      // (https://github.com/Netflix/curator/wiki/Shared-lock)
      // On capte les événements de déconnexion, car en cas de déconnexion, on
      // n'est plus sûr d'avoir le lock
      curatorClient.getConnectionStateListenable().addListener(stateListener);      
      mutex = new InterProcessMutex(curatorClient, mutexPath);
   }

   /**
    * Acquire the mutex - blocks until it's available or the given time expires.
    * Attention : ce mutex n'est pas ré-entrant.
    * Attention, même un fois acquis, un lock peut être perdu en cas de déconnexion.
    *
    * @param timeOut time to wait
    * @param timeOutUnit time unit
    * @return true if the mutex was acquired, false if not
    */
   public final boolean acquire(long timeOut, TimeUnit timeOutUnit) {
      try {
         return mutex.acquire(timeOut, timeOutUnit);
      } catch (Exception e) {
         LOG.error("Erreur lors de la tentative de lock sur le chemin " + mutexPath, e);
         return false;
      }
   }

   /**
    * Vérifie qu'on a toujours le lock.
    * 
    * @param timeOut       combien de temps on attend dans le cas où on est en train d'être reconnecté
    * @param timeOutUnit   time unit
    * @return boolean      vrai si on a encore le lock
    */
   public final synchronized boolean isObjectStillLocked(long timeOut, TimeUnit timeOutUnit) {
      if (connectionState == ConnectionState.SUSPENDED) {
         try {
            this.wait(timeOutUnit.toMillis(timeOut));
         } catch (InterruptedException e) {
            throw new RuntimeException(e);
         }
      }
      return lockInfo.lockOk;
   }

   /**
    * Lâche le lock
    * 
    */
   public final void release() {
      try {
         if (mutex.isAcquiredInThisProcess())
            mutex.release();

      } catch (Exception e) {
         LOG.error("Erreur lors de la libération du lock pour le chemin " 
               + mutexPath, e);
      } finally {
         curatorClient.getConnectionStateListenable().removeListener(
               stateListener);
         stateListener = null;
      }
   }

   private synchronized void notifyFromWatcher()
   {
       notifyAll();
   }
   
   /**
    * Juste une feinte pour pouvoir modifier le booléen depuis une classe anonyme
    * (java doesn't support true closures)
    */
   private static class LockInfo {
      // CHECKSTYLE:OFF    Pas d'accesseurs : c'est une classe privée interne
      public volatile boolean lockOk = true;
      // CHECKSTYLE:ON
   }
}