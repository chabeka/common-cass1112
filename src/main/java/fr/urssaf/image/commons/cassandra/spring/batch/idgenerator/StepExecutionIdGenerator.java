package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import com.netflix.curator.framework.CuratorFramework;

import me.prettyprint.hector.api.Keyspace;

/**
 * Générateur de stepExecutionId
 *
 */
public class StepExecutionIdGenerator implements IdGenerator {

   private final IdGenerator generator;
   
   /**
    * Constructeur 
    * @param keyspace      Keyspace cassandra
    * @param curatorClient Connexion à zookeeper
    */
   public StepExecutionIdGenerator(Keyspace keyspace, CuratorFramework curatorClient) {
      generator = new CassandraIdGenerator(keyspace, curatorClient, "stepExecutionId");
   }

   @Override
   public final long getNextId() {
      return generator.getNextId();
   }

}
