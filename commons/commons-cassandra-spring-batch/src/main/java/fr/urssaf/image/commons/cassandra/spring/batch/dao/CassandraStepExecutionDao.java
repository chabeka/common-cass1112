package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.Collection;

import org.springframework.batch.admin.service.SearchableStepExecutionDao;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daothrift.CassandraStepExecutionDaoThrift;

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

  /**
   * COnstructeur
   * 
   * @param daoCqlparam
   *          DAO parametre pour le CQL
   * @param daoThriftparam
   *          DAO parametre pour le Thrift
   */
  public CassandraStepExecutionDao(final IJobStepExecutionDaoCql daoCqlparam, final CassandraStepExecutionDaoThrift daoThriftparam) {
    super();
    daoCql = daoCqlparam;
    daoThrift = daoThriftparam;
  }

  @Override
  public final void addStepExecutions(final JobExecution jobExecution) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.addStepExecutions(jobExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.addStepExecutions(jobExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public final StepExecution getStepExecution(final JobExecution jobExecution, final Long stepExecutionId) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return daoCql.getStepExecution(jobExecution, stepExecutionId);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return daoThrift.getStepExecution(jobExecution, stepExecutionId);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public final void saveStepExecution(final StepExecution stepExecution) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.saveStepExecution(stepExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.saveStepExecution(stepExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public final void updateStepExecution(final StepExecution stepExecution) {
    // Le nom de la méthode n'est pas super explicite, mais is s'agit
    // d'enregister le stepExecution
    // en base de données.

    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.updateStepExecution(stepExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.updateStepExecution(stepExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  /**
   * Supprime un step de la base de données
   *
   * @param stepExecutionId
   *          : id du step à supprimer
   */
  public final void deleteStepExecution(final Long stepExecutionId) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.deleteStepExecution(stepExecutionId);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.deleteStepExecution(stepExecutionId);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  /**
   * Supprime tous les steps d'un jobExecution donné
   *
   * @param jobExecution
   *          : jobExecution concerné
   */
  public final void deleteStepsOfExecution(final JobExecution jobExecution) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      daoCql.deleteStepsOfExecution(jobExecution);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      daoThrift.deleteStepsOfExecution(jobExecution);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public final int countStepExecutions(final String jobNamePattern, final String stepNamePattern) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return daoCql.countStepExecutions(jobNamePattern, stepNamePattern);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return daoThrift.countStepExecutions(jobNamePattern, stepNamePattern);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return 0;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final Collection<StepExecution> findStepExecutions(final String jobNamePattern, final String stepNamePattern, final int start, final int count) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return daoCql.findStepExecutions(jobNamePattern, stepNamePattern, start, count);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return daoThrift.findStepExecutions(jobNamePattern, stepNamePattern, start, count);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final Collection<String> findStepNamesForJobExecution(final String jobName, final String excludesPattern) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (ModeGestionAPI.MODE_API.DATASTAX.equals(modeApi)) {
      return daoCql.findStepNamesForJobExecution(jobName, excludesPattern);
    } else if (ModeGestionAPI.MODE_API.HECTOR.equals(modeApi)) {
      return daoThrift.findStepNamesForJobExecution(jobName, excludesPattern);
    } else if (ModeGestionAPI.MODE_API.DUAL_MODE.equals(modeApi)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

}
