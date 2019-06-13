/**
 *
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.List;

import org.springframework.batch.admin.service.SearchableJobInstanceDao;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daothrift.CassandraJobInstanceDaoThrift;

/**
 * Classe implémentant JobInstanceDao, qui utilise cassandra. L'implémentation
 * est inspirée de
 * org.springframework.batch.core.repository.dao.JdbcJobInstanceDao
 *
 * @see org.springframework.batch.core.repository.dao.JdbcJobInstanceDao
 * @author Samuel Carrière
 */
public class CassandraJobInstanceDao implements SearchableJobInstanceDao {

  /** Le nom de la table */
  private final String cfName = "jobinstance";

  /** Le dao cql */
  private final IJobInstanceDaoCql jobInstanceDaoCql;

  /** Le dao thrift */
  private final CassandraJobInstanceDaoThrift jobInstanceDaoThrift;

  /**
   * Constructeur
   * 
   * @param daoCql
   *          DAO pour le CQL
   * @param daoThrift
   *          DAO pour le Thrift
   */
  public CassandraJobInstanceDao(final IJobInstanceDaoCql daoCql, final CassandraJobInstanceDaoThrift daoThrift) {
    super();
    jobInstanceDaoCql = daoCql;
    jobInstanceDaoThrift = daoThrift;
  }

  @Override
  public JobInstance createJobInstance(final String jobName, final JobParameters jobParameters) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.createJobInstance(jobName, jobParameters);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.createJobInstance(jobName, jobParameters);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public JobInstance getJobInstance(final String jobName, final JobParameters jobParameters) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.getJobInstance(jobName, jobParameters);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.getJobInstance(jobName, jobParameters);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public JobInstance getJobInstance(final Long instanceId) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.getJobInstance(instanceId);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.getJobInstance(instanceId);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public JobInstance getJobInstance(final JobExecution jobExecution) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.getJobInstance(jobExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.getJobInstance(jobExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<JobInstance> getJobInstances(final String jobName, final int start, final int count) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.getJobInstances(jobName, start, count);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.getJobInstances(jobName, start, count);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<String> getJobNames() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.getJobNames();
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.getJobNames();
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public int countJobInstances(final String name) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.countJobInstances(name);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.countJobInstances(name);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return 0;
  }

  /**
   * Réserve un job, c'est à dire inscrit un nom de serveur dans la colonne
   * "reservedBy" de l'instance de job
   *
   * @param instanceId
   *          Id de l'instance de job à réserver
   * @param serverName
   *          Nom du serveur qui réserve le job
   */
  public void reserveJob(final long instanceId, final String serverName) {

    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      jobInstanceDaoCql.reserveJob(instanceId, serverName);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      jobInstanceDaoThrift.reserveJob(instanceId, serverName);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  /**
   * Renvoie le nom du serveur qui réserve l'instance de job
   *
   * @param instanceId
   *          Id de l'instance
   * @return Nom du serveur qui réserve l'instance de job, ou vide si aucun serveur
   *         ne réserve le job, ou null si l'instance n'existe pas
   */
  public final String getReservingServer(final long instanceId) {

    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.getReservingServer(instanceId);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.getReservingServer(instanceId);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  /**
   * Renvoie la liste des jobs non réservés
   *
   * @return Liste des jobs non réservés
   */
  public final List<JobInstance> getUnreservedJobInstances() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobInstanceDaoCql.getUnreservedJobInstances();
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobInstanceDaoThrift.getUnreservedJobInstances();
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

}
