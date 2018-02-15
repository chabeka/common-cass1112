package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;

import me.prettyprint.hector.api.Keyspace;

/**
 * Générateur de jobInstanceId
 *
 */
public class JobInstanceIdGenerator implements IdGenerator {

   private final IdGenerator generator;

   /**
    * Constructeur 
    * @param keyspace   Keyspace cassandra
    * @param curatorClient Connexion à zookeeper
    * @param jobClockSupport support de l'horloge
    */
   public JobInstanceIdGenerator(Keyspace keyspace, CuratorFramework curatorClient,JobClockSupport jobClockSupport) {
      generator = new CassandraIdGenerator(keyspace, curatorClient, "jobInstanceId",jobClockSupport);
   }

   @Override
   public final long getNextId() {
      return generator.getNextId();
   }

}
