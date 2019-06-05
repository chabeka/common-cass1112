/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.utils;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.helper.CassandraJobHelper;

/**
 * TODO (AC75095028) Description du type
 */
public class JobTranslateUtils {

   public static JobExecutionCql JobExecutionToJobExecutionCql(final JobExecution jobExecution) {
      Assert.notNull(jobExecution, "Job cannot be null.");

      final JobExecutionCql exCql = new JobExecutionCql();

      if (jobExecution == null) {
         return null;
      }
      exCql.setJobExecutionId(jobExecution.getId());
      if (jobExecution.getJobInstance() != null) {
         exCql.setJobInstanceId(jobExecution.getJobInstance().getId());
      }
      exCql.setCreationTime(jobExecution.getCreateTime());
      exCql.setEndTime(jobExecution.getEndTime());
      exCql.setExitCode(jobExecution.getExitStatus().getExitCode());
      exCql.setExitMessage(jobExecution.getExitStatus().getExitDescription());
      exCql.setJobName(jobExecution.getJobInstance().getJobName());
      exCql.setLastUpdated(jobExecution.getLastUpdated());
      exCql.setStartTime(jobExecution.getStartTime());
      exCql.setStatus(jobExecution.getStatus());
      exCql.setVersion(jobExecution.getVersion());
      exCql.setExecutionContext(jobExecution.getExecutionContext());
      return exCql;
   }

   /**
    * Crée un objet {@link JobExecution} à partir d'un {@link JobExecutionsCql}.
    *
    * @param result
    *           Données de cassandra
    * @param jobInstance
    *           Si non nul : jobInstance lié au jobExecution à renvoyé
    *           Si nul, on instanciera un jobInstance "minimal"
    * @return le jobExecution
    */
   public static JobExecution JobExecutionCqlToJobExecution(final JobExecutionCql jobExCql, final JobInstance jobInstance) {

      final JobExecution JobSpripng = new JobExecution(jobExCql.getJobExecutionId());

      final Long jobInstanceId = jobExCql.getJobInstanceId();

      final String jobName = jobExCql.getJobName();

      JobSpripng.setCreateTime(jobExCql.getCreationTime());
      JobSpripng.setEndTime(jobExCql.getEndTime());
      JobSpripng.setLastUpdated(jobExCql.getLastUpdated());
      JobSpripng.setStartTime(jobExCql.getStartTime());
      JobSpripng.setStatus(jobExCql.getStatus());
      JobSpripng.setVersion(jobExCql.getVersion());
      JobSpripng.setExecutionContext(jobExCql.getExecutionContext());
      JobSpripng.setExitStatus(new ExitStatus(jobExCql.getExitCode(), jobExCql.getExitMessage()));

      if (jobInstance == null) {
         // On fait comme dans l'implémentation JDBC : on instancie une instance
         // avec des paramètres nuls
         JobSpripng.setJobInstance(new JobInstance(jobInstanceId, null, jobName));
      } else {
         JobSpripng.setJobInstance(jobInstance);
      }
      return JobSpripng;
   }

   /**
    * Transforme un {@link JobInstance} en {@link JobInstanceCql}
    *
    * @param instance
    * @return
    */
   public static JobInstanceCql getJobInstanceCqlToJobInstance(final JobInstance instance) {
      final JobInstanceCql job = new JobInstanceCql();
      job.setJobInstanceId(instance.getId());
      final byte[] jobKey = CassandraJobHelper.createJobKey(instance.getJobName(), instance
                                                                                           .getJobParameters());
      job.setJobKey(jobKey);
      job.setJobparameters(instance.getJobParameters());
      job.setReservedBy("");
      job.setVersion(instance.getVersion());
      job.setJobName(instance.getJobName());
      return job;
   }

   /**
    * transforme un {@link JobInstance} en {@link JobInstanceCql}
    *
    * @param jobCql
    * @return
    */
   public static JobInstance getJobInstanceToJobInstanceCql(final JobInstanceCql jobCql) {
      if (jobCql == null) {
         return null;
      }
      final JobInstance job = new JobInstance(jobCql.getJobInstanceId(), jobCql.getJobparameters(), jobCql.getJobName());
      job.setVersion(jobCql.getVersion());
      return job;
   }

   /**
    * transforme un {@link JobInstanceCql} en {@link JobInstancesByNameCql}
    *
    * @param jobCql
    * @return
    */
   public static JobInstancesByNameCql getJobInstancesByNameCqlToJobInstance(final JobInstanceCql jobCql) {
      final JobInstancesByNameCql job = new JobInstancesByNameCql();
      job.setJobName(jobCql.getJobName());
      job.setJobInstanceId(jobCql.getJobInstanceId());
      // job.setReservedBy(jobCql.getReservedBy());
      return job;
   }

   /**
    * Crée un objet {@link JobStepCql} à partir d'un objet {@link StepExecution}
    *
    * @param {@link
    *           StepExecution}
    *           : le {@link StepExecution} référencé par le step à créer (éventuellement null)
    * @return Le {@link JobStepCql} à enregistrer dans cassandra
    */
   public static JobStepCql getStpeCqlFromStepExecution(final StepExecution stepExecution) {

      if (stepExecution == null) {
         return null;
      }
      final JobStepCql step = new JobStepCql();

      step.setJobStepExecutionId(stepExecution.getId());
      step.setJobExecutionId(stepExecution.getJobExecutionId());
      final String stepName = stepExecution.getStepName();
      step.setName(stepName);
      step.setVersion(stepExecution.getVersion());
      step.setStartTime(stepExecution.getStartTime());
      step.setEndTime(stepExecution.getEndTime());
      step.setStatus(stepExecution.getStatus());
      step.setCommitCount(stepExecution.getCommitCount());
      step.setReadCount(stepExecution.getReadCount());
      step.setFilterCount(stepExecution.getFilterCount());
      step.setWriteCount(stepExecution.getWriteCount());
      step.setReadSkipCount(stepExecution.getReadSkipCount());
      step.setWriteSkipCount(stepExecution.getWriteSkipCount());
      step.setProcessSkipCount(stepExecution.getProcessSkipCount());
      step.setRollbackCount(stepExecution.getRollbackCount());
      final String exitCode = stepExecution.getExitStatus().getExitCode();
      final String exitMessage = stepExecution.getExitStatus().getExitDescription();
      step.setExitCode(exitCode);
      step.setExitMessage(exitMessage);
      step.setLastUpdated(stepExecution.getLastUpdated());
      final ExecutionContext executionContext = stepExecution.getExecutionContext();
      step.setExecutionContext(executionContext);
      return step;

   }

   /**
    * Crée un objet StepExecution à partir d'une ligne lue de cassandra
    *
    * @param jobExecution
    *           : le jobExecution référencé par le step à créer (éventuellement null)
    * @param result
    *           : données cassandra
    * @return
    */
   public static StepExecution getStepExecutionFromStpeCql(final JobExecution jobExecution, final JobStepCql stepCql) {

      if (stepCql == null) {
         return null;
      }
      final String stepName = stepCql.getName();
      StepExecution step;
      if (jobExecution == null) {
         step = new StepExecution(stepName, null);
         step.setId(stepCql.getJobStepExecutionId());
      } else {
         step = new StepExecution(stepName, jobExecution, stepCql.getJobStepExecutionId());
      }

      step.setVersion(stepCql.getVersion());
      step.setStartTime(stepCql.getStartTime());
      step.setEndTime(stepCql.getEndTime());
      step.setStatus(stepCql.getStatus());
      step.setCommitCount(stepCql.getCommitCount());
      step.setReadCount(stepCql.getReadCount());
      step.setFilterCount(stepCql.getFilterCount());
      step.setWriteCount(stepCql.getWriteCount());
      step.setReadSkipCount(stepCql.getReadSkipCount());
      step.setWriteSkipCount(stepCql.getWriteSkipCount());
      step.setProcessSkipCount(stepCql.getProcessSkipCount());
      step.setRollbackCount(stepCql.getRollbackCount());
      final String exitCode = stepCql.getExitCode();
      final String exitMessage = stepCql.getExitMessage();
      step.setExitStatus(new ExitStatus(exitCode, exitMessage));
      step.setLastUpdated(stepCql.getLastUpdated());
      final ExecutionContext executionContext = stepCql.getExecutionContext();
      step.setExecutionContext(executionContext);
      return step;

   }
}
