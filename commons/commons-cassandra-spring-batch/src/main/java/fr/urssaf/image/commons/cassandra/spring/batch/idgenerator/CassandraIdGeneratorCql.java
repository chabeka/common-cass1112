package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Result;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.ISequencesDaoCql;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;

/**
 * Générateur d'id utilisant cassandra et zookeeper La valeur du dernier id
 * utilisé est stockée dans cassandra On utilise zookeeper pour accéder de
 * manière exclusive à la séquence
 */
public class CassandraIdGeneratorCql implements IdGenerator {

  private ISequencesDaoCql sequencesdao;

  private static final String SEQUENCE_CF = "sequences";

  private String sequenceName = null;

  private CuratorFramework curatorClient = null;

  private static final int DEFAULT_TIME_OUT = 20;

  private int lockTimeOut = DEFAULT_TIME_OUT;

  private JobClockSupport jobClockSupport;

  /**
   *
   */
  public CassandraIdGeneratorCql() {

  }

  /**
   * Constructeur
   *
   * @param curatorClient
   *          Connexion à zookeeper
   * @param sequenceName
   *          Nom de la séquence (doit identifier la séquence de manière unique)
   * @param sequencesdao
   *          DAO de sequences
   */
  public CassandraIdGeneratorCql(final CuratorFramework curatorClient, final String sequenceName, final ISequencesDaoCql sequencesdao,
                                 final JobClockSupport jobClockSupport) {
    this.sequenceName = sequenceName;
    this.curatorClient = curatorClient;
    this.sequencesdao = sequencesdao;
    this.jobClockSupport = jobClockSupport;
  }

  @Override
  public final long getNextId() {

    final ZookeeperMutex mutex = new ZookeeperMutex(curatorClient, "/sequences/" + sequenceName);
    try {
      if (!mutex.acquire(lockTimeOut, TimeUnit.SECONDS)) {
        throw new IdGeneratorException("Erreur lors de la tentative d'acquisition du lock pour la séquence " + sequenceName
                                       + " : on n'a pas obtenu le lock au bout de 20 secondes.");
      }
      // On a le lock.
      // On lit la valeur courante de la séquence
      final SequencesCql seqclq = readCurrentSequence();

      // On écrit dans cassandra la valeur incrémentée
      final long newValue = incrementSequenceValue(seqclq, sequenceName);

      // On vérifie qu'on a toujours le lock. Si oui, on peut utiliser la
      // séquence.
      if (mutex.isObjectStillLocked(lockTimeOut, TimeUnit.SECONDS)) {
        // On peut utiliser la valeur incrémentée
        return newValue;
      } else {
        throw new IdGeneratorException("Erreur lors de la tentative d'acquisition du lock pour la séquence " + sequenceName
                                       + ". Problème de connexion zookeeper ?");
      }

    } finally {
      mutex.release();
    }
  }

  /**
   * Lit la valeur de la séquence dans cassandra
   *
   * @return colonne lue
   */
  private SequencesCql readCurrentSequence() {
    final Select select = QueryBuilder.select().from(SEQUENCE_CF);
    select.where(QueryBuilder.eq("jobidname", sequenceName));
    final Result<SequencesCql> seqcql = sequencesdao.getMapper().map(sequencesdao.getSession().execute(select));
    if (seqcql != null) {
      return seqcql.one();
    }
    return null;
  }

  /**
   * Incrémente la séquence, et la fait persister dans cassandra
   *
   * @param currentSequence
   *          la {@link SequencesCql} dans laquelle on incremente la valeur
   * @return nouvelle valeur de la séquence
   */
  private long incrementSequenceValue(SequencesCql currentSequence, final String sequenceName) {
    long currentValue;

    long colunmTimeStamp;

    if (currentSequence == null) {
      currentValue = 0;
      currentSequence = new SequencesCql();
      currentSequence.setJobIdName(sequenceName);
      colunmTimeStamp = jobClockSupport.currentCLock();
    } else {
      currentValue = currentSequence.getValue();
      // calcul du clock
      final long actualServerClock = jobClockSupport.currentCLock();
      final long columnClock = sequencesdao.getColunmClock(currentSequence.getJobIdName());
      colunmTimeStamp = jobClockSupport.getClock(actualServerClock, columnClock);
    }

    final long newValue = currentValue + 1;
    currentSequence.setValue(newValue);
    sequencesdao.saveWithMapper(currentSequence, colunmTimeStamp);
    return newValue;
  }

  /**
   * Spécifie le timeout du lock, en seconde (par défaut : 20s)
   *
   * @param lockTimeOut
   *          timeout, en seconde
   */
  public final void setLockTimeOut(final int lockTimeOut) {
    this.lockTimeOut = lockTimeOut;
  }

  /**
   * Récupère la valeur du timeout pour le lock
   *
   * @return timeout, en seconde
   */
  public final int getLockTimeOut() {
    return lockTimeOut;
  }

}
