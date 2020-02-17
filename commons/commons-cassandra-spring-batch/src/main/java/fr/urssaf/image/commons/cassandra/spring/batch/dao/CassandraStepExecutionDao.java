package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.Collection;

import org.springframework.batch.admin.service.SearchableStepExecutionDao;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeAPIService;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraStepExecutionDaoThrift;

/**
 * Classe permettant de faire le sxitch entre le dao thrift et cql
 * Classe implémentant StepExecutionDao, qui utilise cassandra. L'implémentation
 * est inspirée de
 * org.springframework.batch.core.repository.dao.JdbcStepExecutionDao
 *
 * @see org.springframework.batch.core.repository.dao.JdbcStepExecutionDao
 * @see org.springframework.batch.admin.service.JdbcSearchableStepExecutionDao
 */
public class CassandraStepExecutionDao implements SearchableStepExecutionDao {

  private final String cfName = "jobstep";

  private final CassandraStepExecutionDaoThrift daoThrift;

  private final IJobStepExecutionDaoCql daoCql;

  private final ModeAPIService modeApiService;

  /**
   * COnstructeur
   * 
   * @param daoCqlparam
   *          DAO parametre pour le CQL
   * @param daoThriftparam
   *          DAO parametre pour le Thrift
   */
  public CassandraStepExecutionDao(final IJobStepExecutionDaoCql daoCqlparam,
                                   final CassandraStepExecutionDaoThrift daoThriftparam,
                                   final ModeAPIService modeApiService) {
    super();
    daoCql = daoCqlparam;
    daoThrift = daoThriftparam;
    this.modeApiService = modeApiService;
  }

  @Override
  public final void addStepExecutions(final JobExecution jobExecution) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.addStepExecutions(jobExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.addStepExecutions(jobExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)) {
      daoCql.addStepExecutions(jobExecution);
      daoThrift.addStepExecutions(jobExecution);
    }
  }

  @Override
  public final StepExecution getStepExecution(final JobExecution jobExecution, final Long stepExecutionId) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)) {
      return daoCql.getStepExecution(jobExecution, stepExecutionId);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      return daoThrift.getStepExecution(jobExecution, stepExecutionId);
    }
    return null;
  }

  @Override
  public final void saveStepExecution(final StepExecution stepExecution) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.saveStepExecution(stepExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.saveStepExecution(stepExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      daoCql.saveStepExecution(stepExecution);
      daoThrift.saveStepExecution(stepExecution);
    }
  }

  @Override
  public final void updateStepExecution(final StepExecution stepExecution) {
    // Le nom de la méthode n'est pas super explicite, mais is s'agit
    // d'enregister le stepExecution
    // en base de données.

    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.updateStepExecution(stepExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.updateStepExecution(stepExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      daoCql.updateStepExecution(stepExecution);
      daoThrift.saveStepExecution(stepExecution);
    }
  }

  /**
   * Supprime un step de la base de données
   *
   * @param stepExecutionId
   *          : id du step à supprimer
   */
  public final void deleteStepExecution(final Long stepExecutionId) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.deleteStepExecution(stepExecutionId);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.deleteStepExecution(stepExecutionId);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      daoCql.deleteStepExecution(stepExecutionId);
      daoThrift.deleteStepExecution(stepExecutionId);
    }
  }

  /**
   * Supprime tous les steps d'un jobExecution donné
   *
   * @param jobExecution
   *          : jobExecution concerné
   */
  public final void deleteStepsOfExecution(final JobExecution jobExecution) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.deleteStepsOfExecution(jobExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.deleteStepsOfExecution(jobExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      daoCql.deleteStepsOfExecution(jobExecution);
      daoThrift.deleteStepsOfExecution(jobExecution);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public final int countStepExecutions(final String jobNamePattern, final String stepNamePattern) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi) ||
        ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)) {
      return daoCql.countStepExecutions(jobNamePattern, stepNamePattern);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      return daoThrift.countStepExecutions(jobNamePattern, stepNamePattern);
    }
    return 0;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final Collection<StepExecution> findStepExecutions(final String jobNamePattern, final String stepNamePattern, final int start, final int count) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)) {
      return daoCql.findStepExecutions(jobNamePattern, stepNamePattern, start, count);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      return daoThrift.findStepExecutions(jobNamePattern, stepNamePattern, start, count);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final Collection<String> findStepNamesForJobExecution(final String jobName, final String excludesPattern) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL.equals(modeApi)) {
      return daoCql.findStepNamesForJobExecution(jobName, excludesPattern);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)
        || ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT.equals(modeApi)) {
      return daoThrift.findStepNamesForJobExecution(jobName, excludesPattern);
    }
    return null;
  }

}
