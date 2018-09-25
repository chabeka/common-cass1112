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
    * @param daoCql
    * @param daoThrift
    */
   public CassandraJobInstanceDao(final IJobInstanceDaoCql daoCql, final CassandraJobInstanceDaoThrift daoThrift) {
      super();
      this.jobInstanceDaoCql = daoCql;
      this.jobInstanceDaoThrift = daoThrift;
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
}
