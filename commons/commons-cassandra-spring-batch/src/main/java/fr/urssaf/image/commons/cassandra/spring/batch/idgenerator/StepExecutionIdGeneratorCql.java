package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.spring.batch.daocql.ISequencesDaoCql;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;

/**
 * Générateur de stepExecutionId
 */
@Component(value = "stepexecutionidgeneratorcql")
public class StepExecutionIdGeneratorCql implements IdGenerator {

  private final IdGenerator generator;

  /**
   * Constructeur
   *
   * @param keyspace
   *          Keyspace cassandra
   * @param curatorClient
   *          Connexion à zookeeper
   * @param jobClockSupport
   *          support de l'horloge
   */
  @Autowired
  public StepExecutionIdGeneratorCql(final CuratorFramework curatorClient, final JobClockSupport jobClockSupport, final ISequencesDaoCql sequencesdao) {
    generator = new CassandraIdGeneratorCql(curatorClient, "stepExecutionId", sequencesdao);
  }

  @Override
  public final long getNextId() {
    return generator.getNextId();
  }

}
