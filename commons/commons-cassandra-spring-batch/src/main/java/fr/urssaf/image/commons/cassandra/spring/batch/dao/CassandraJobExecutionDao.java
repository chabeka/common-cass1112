/**
 *
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.admin.service.SearchableJobExecutionDao;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraJobExecutionDaoThrift;

/**
 *
 *
 */
public class CassandraJobExecutionDao implements SearchableJobExecutionDao {
  /** Le nom de la table */
  private final String cfName = "jobexecution";

  /** le dao thrift */
  private final CassandraJobExecutionDaoThrift jobExeDaoThrift;

  /** le dao cql */
  private final IJobExecutionDaoCql jobExeDaoCql;

  /**
   * Contructeur
   *
   * @param daoCql
   *           le dao cql
   * @param daoThrift
   *           le dao thrift
   */
  public CassandraJobExecutionDao(final IJobExecutionDaoCql daoCql, final CassandraJobExecutionDaoThrift daoThrift) {
    super();
    jobExeDaoCql = daoCql;
    jobExeDaoThrift = daoThrift;

  }

  @Override
  public void saveJobExecution(final JobExecution jobExecution) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      jobExeDaoCql.saveJobExecution(jobExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      jobExeDaoThrift.saveJobExecution(jobExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }

  }

  @Override
  public void updateJobExecution(final JobExecution jobExecution) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      jobExeDaoCql.updateJobExecution(jobExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      jobExeDaoThrift.updateJobExecution(jobExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }

  }

  @Override
  public List<JobExecution> findJobExecutions(final JobInstance jobInstance) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.findJobExecutions(jobInstance);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.findJobExecutions(jobInstance);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return new ArrayList<>();
  }

  @Override
  public JobExecution getLastJobExecution(final JobInstance jobInstance) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.getLastJobExecution(jobInstance);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.getLastJobExecution(jobInstance);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public Set<JobExecution> findRunningJobExecutions(final String jobName) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.findRunningJobExecutions(jobName);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.findRunningJobExecutions(jobName);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return new HashSet<>();
  }

  @Override
  public JobExecution getJobExecution(final Long executionId) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.getJobExecution(executionId);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.getJobExecution(executionId);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public void synchronizeStatus(final JobExecution jobExecution) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      jobExeDaoCql.synchronizeStatus(jobExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      jobExeDaoThrift.synchronizeStatus(jobExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }

  }

  @Override
  public int countJobExecutions() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.countJobExecutions();
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.countJobExecutions();
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return 0;
  }

  @Override
  public List<JobExecution> getJobExecutions(final String jobName, final int start, final int count) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.getJobExecutions(jobName, start, count);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.getJobExecutions(jobName, start, count);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return new ArrayList<>();
  }

  @Override
  public List<JobExecution> getJobExecutions(final int start, final int count) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.getJobExecutions(start, count);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.getJobExecutions(start, count);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return new ArrayList<>();
  }

  @Override
  public int countJobExecutions(final String jobName) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.countJobExecutions(jobName);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.countJobExecutions(jobName);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return 0;
  }

  @Override
  public Collection<JobExecution> getRunningJobExecutions() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return jobExeDaoCql.getRunningJobExecutions();
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return jobExeDaoThrift.getRunningJobExecutions();
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return new ArrayList<>();
  }

}
