package fr.urssaf.image.zookeeper;

import fr.urssaf.image.zookeeper.exception.ZookeeperEx;

public interface ObjectLocker {
   public boolean lockObject(String noeud) throws ZookeeperEx;

   public boolean unlockObject(String noeud);
}
