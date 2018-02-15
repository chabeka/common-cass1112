package fr.urssaf.image.sae.cassandra.dao.reservation;

import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;
import fr.urssaf.image.sae.cassandra.dao.exception.ZookeeperEx;

public interface ObjectLocker {
   public boolean lockObject(String paramObject) throws CassandraEx, ZookeeperEx;

   public boolean unlockObject(String paramObject);

   public boolean isLockedObject(String paramObject);
}
  