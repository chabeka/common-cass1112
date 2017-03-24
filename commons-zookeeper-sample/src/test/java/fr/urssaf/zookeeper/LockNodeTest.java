package fr.urssaf.zookeeper;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.zookeeper.exception.ZookeeperEx;
import fr.urssaf.image.zookeeper.impl.ObjectLockerByCuratorImpl;

/**
 * Exemple d'utilisation de l'API Curator pour locker un objet dans Zoopeeper.
 * Le Zookeeper utilisé est : cer69imageint9.cer69.recouv:2181
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-curator.xml" })
public class LockNodeTest {
   @Autowired
   ObjectLockerByCuratorImpl objectLockerByCuratorImpl;

   @Test
   public void lockObjectByCurator() throws Exception {
      Assert.assertTrue("Création du noeud a échoué !",
            objectLockerByCuratorImpl
                  .lockObject("4feb05f0-4376-11e1-897d-005056c00008"));
      Assert.assertTrue("Suppression du noeud a échoué !",
            objectLockerByCuratorImpl
                  .unlockObject("4feb05f0-4376-11e1-897d-005056c00008"));

   }

}
