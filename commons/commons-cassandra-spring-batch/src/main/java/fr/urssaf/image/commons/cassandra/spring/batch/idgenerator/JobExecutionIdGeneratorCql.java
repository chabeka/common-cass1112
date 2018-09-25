package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.spring.batch.daocql.ISequencesDaoCql;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import me.prettyprint.hector.api.Keyspace;

/**
 * Générateur de jobExecutionId
 */
@Component(value = "jobexecutionidgeneratorcql")
public class JobExecutionIdGeneratorCql implements IdGenerator {

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
  public JobExecutionIdGeneratorCql(final Keyspace keyspace, final CuratorFramework curatorClient, final JobClockSupport jobClockSupport,
                                    final ISequencesDaoCql sequencesdao) {
    generator = new CassandraIdGeneratorCql(curatorClient, "jobExecutionId", sequencesdao);
  }

  @Override
  public final long getNextId() {
    return generator.getNextId();
  }

}
