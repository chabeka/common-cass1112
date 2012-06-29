/**
 * 
 */
package fr.urssaf.image.sae.droit.utils;

import java.util.concurrent.TimeUnit;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

/**
 * 
 * 
 */
public final class ZookeeperUtils {

   /**
    * Time-out du lock, en secondes
    */
   private static final int LOCK_TIME_OUT = 20;

   private ZookeeperUtils() {
   }

   /**
    * Création du mutex ZooKeeper
    * 
    * @param curatorClient
    *           connexion a zookeeper
    * @param name
    *           nom à réserver
    * @return le mutex zookeeper
    */
   public static ZookeeperMutex createMutex(CuratorFramework curatorClient,
         String name) {
      return new ZookeeperMutex(curatorClient, name);
   }

   /**
    * Acquisition du lock
    * 
    * @param mutex
    *           mutex Zookeeper
    * @param name
    *           nom a locker
    * @throws LockTimeoutException
    *            exception levée lorsqu'il est impossible de récupérer le lock
    */
   public static void acquire(ZookeeperMutex mutex, String name) {
      if (!mutex.acquire(LOCK_TIME_OUT, TimeUnit.SECONDS)) {
         throw new DroitRuntimeException(
               "Erreur lors de la tentative d'acquisition du lock pour le contrat "
                     + name + " : impossible d'obtenir le lock au bout de "
                     + LOCK_TIME_OUT + " secondes.");
      }

   }

   /**
    * retourne un booleen pour savoir si l'objet est toujours locké ou non par
    * zookeeper
    * 
    * @param mutex
    *           mutex zookeeper
    * @return objet locké<b>(true)</b> ou non<b>(false)</b>
    */
   public static boolean isLock(ZookeeperMutex mutex) {
      return mutex.isObjectStillLocked(LOCK_TIME_OUT, TimeUnit.SECONDS);
   }

}
