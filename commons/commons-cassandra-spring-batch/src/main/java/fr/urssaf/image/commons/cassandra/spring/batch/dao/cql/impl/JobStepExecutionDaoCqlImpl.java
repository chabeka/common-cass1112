
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.CodecNotFoundException;

import fr.urssaf.image.commons.cassandra.cql.codec.BytesBlobCodec;
import fr.urssaf.image.commons.cassandra.cql.codec.JsonCodec;
import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionToJobStepDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepsDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.helper.CassandraJobHelper;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.IdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.ExecutionContextCodec;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;


/**
 * Classe implémentant le DAO  {@link IJobStepExecutionDaoCql}
 */
@Repository
public class JobStepExecutionDaoCqlImpl extends GenericDAOImpl<JobStepCql, Long> implements IJobStepExecutionDaoCql {

	
  /**
   * @param ccf
   */
  public JobStepExecutionDaoCqlImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

   @Autowired
   @Qualifier("stepexecutionidgeneratorcql")
   private IdGenerator idGenerator;

   @Autowired
   private IJobExecutionToJobStepDaoCql jobExeToStepDaoCql;
   
   @Autowired
   private IJobStepDaoCql jobStepDaoCql;

   @Autowired
   IJobStepsDaoCql stepsDao;

   private static final int MAX_COLS = 500;

   /**
    * Cette methode est appelé après l'instanciation de la classe par spring.
    * Grace à l'annotation {@link PostConstruct} on est sur que les dependances
    * son bien injectés ({@link CassandraClientFactory}) et cela nous permet d'enregistrer tous les <b>codec</b> nécessaires
    * aux opérations sur la table (CF) de ce DAO
    */
   @PostConstruct
   public void setRegister() {
	   if(ccf != null) {
      final CodecRegistry registry = ccf.getCluster().getConfiguration().getCodecRegistry();
      registerCodecIfNotFound(registry, new JsonCodec<>(BatchStatus.class));
      registerCodecIfNotFound(registry, BytesBlobCodec.instance);
      registerCodecIfNotFound(registry, ExecutionContextCodec.instance);

      // ccf.getCluster().getConfiguration().getCodecRegistry().register(new JsonCodec<>(BatchStatus.class));
      // ccf.getCluster().getConfiguration().getCodecRegistry().register(BytesBlobCodec.instance);
      // ccf.getCluster().getConfiguration().getCodecRegistry().register(ExecutionContextCodec.instance);
	   }

   }

   @Override
   public final void addStepExecutions(final JobExecution jobExecution) {
      Assert.notNull(jobExecution, "JobExecution cannot be null.");
      Assert.notNull(jobExecution.getId(), "JobExecution Id cannot be null.");
      final long jobExecutionId = jobExecution.getId();

      List<JobStepCql> listJobStep =  jobStepDaoCql.findJobStepByJobExecutionId(jobExecutionId);
      for(JobStepCql stepcql : listJobStep) {
    	  JobTranslateUtils.getStepExecutionFromStpeCql(jobExecution, stepcql);
      }

   }

   @Override
   public final StepExecution getStepExecution(final JobExecution jobExecution,
                                               final Long stepExecutionId) {
      final Optional<JobStepCql> opt = this.findWithMapperById(stepExecutionId);
      if (opt.isPresent()) {
         final JobStepCql stepCql = opt.get();
         return JobTranslateUtils.getStepExecutionFromStpeCql(jobExecution, stepCql);
      }

      return null;
   }

   @Override
   public final void saveStepExecution(final StepExecution stepExecution) {
      Assert.isNull(stepExecution.getId(), "to-be-saved (not updated) StepExecution can't already have an id assigned");
      Assert.isNull(stepExecution.getVersion(), "to-be-saved (not updated) StepExecution can't already have a version assigned");
      validateStepExecution(stepExecution);

      stepExecution.incrementVersion();

      final long stepId = idGenerator.getNextId();
      stepExecution.setId(stepId);

      saveStepExecutionToCassandra(stepExecution);
   }

   /**
    * Enregistre un step dans cassandra Le step doit avoir un id affecté.
    *
    * @param stepExecution
    *           : step à enregistrer
    */
   private void saveStepExecutionToCassandra(final StepExecution stepExecution) {

      // On écrit dans cassandra
      final JobStepCql stepCql = JobTranslateUtils.getStpeCqlFromStepExecution(stepExecution);
      this.saveWithMapper(stepCql);

      // Alimentation des différents index

      // on ecrit Dans JobExecutionToJobStep
      // clé = jobExecutionId
      // Nom de colonne = jobStepId
      // Valeur = vide
      final JobExecutionToJobStepCql jobExToStep = new JobExecutionToJobStepCql();
      jobExToStep.setJobExecutionId(stepExecution.getId());
      jobExToStep.setJobStepId(stepCql.getJobStepExecutionId());
      jobExeToStepDaoCql.saveWithMapper(jobExToStep);

      // on ecrit Dans JobSteps
      // clé = "jobSteps"
      // Nom de colonne = stepId
      // Valeur = composite(jobName, stepName)
      final JobStepsCql jobSteps = new JobStepsCql();
      jobSteps.setJobName(stepExecution.getJobExecution().getJobInstance().getJobName());
      jobSteps.setJobStepId(stepExecution.getId());
      jobSteps.setStepName(stepExecution.getStepName());
      stepsDao.saveWithMapper(jobSteps);

   }

   /**
    * Validate StepExecution. At a minimum, JobId, StartTime, and Status cannot
    * be null. EndTime can be null for an unfinished job.
    *
    * @param value
    * @throws IllegalArgumentException
    */
   private void validateStepExecution(final StepExecution stepExecution) {
      Assert.notNull(stepExecution);
      Assert.notNull(stepExecution.getStepName(), "StepExecution step name cannot be null.");
      Assert.notNull(stepExecution.getStartTime(), "StepExecution start time cannot be null.");
      Assert.notNull(stepExecution.getStatus(), "StepExecution status cannot be null.");
   }

   @Override
   public final void updateStepExecution(final StepExecution stepExecution) {
      // Le nom de la méthode n'est pas super explicite, mais is s'agit
      // d'enregister le stepExecution
      // en base de données.

      validateStepExecution(stepExecution);
      Assert.notNull(stepExecution.getId(), "StepExecution Id cannot be null. StepExecution must saved  before it can be updated.");
      stepExecution.incrementVersion();
      saveStepExecutionToCassandra(stepExecution);
   }

   /**
    * Supprime un step de la base de données
    *
    * @param stepExecutionId
    *           : id du step à supprimer
    */
   @Override
   public final void deleteStepExecution(final Long stepExecutionId) {

      // On supprimee dans JobStep
    deleteById(stepExecutionId);
   }

   /**
    * Supprime tous les steps d'un jobExecution donné
    *
    * @param jobExecution
    *           : jobExecution concerné
    */
   @Override
   public final void deleteStepsOfExecution(final JobExecution jobExecution) {
      final Collection<StepExecution> steps = jobExecution.getStepExecutions();
      for (final StepExecution stepExecution : steps) {
         deleteStepExecution(stepExecution.getId());
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public final int countStepExecutions(final String jobNamePattern, final String stepNamePattern) {

      final List<JobStepsCql> listJobStepsCql = stepsDao.getJobStepsCqlByJobNameAndSetName(jobNamePattern, stepNamePattern);
      return listJobStepsCql.size();
   }

   @Override
   @SuppressWarnings("unchecked")
   public final Collection<StepExecution> findStepExecutions(final String jobNamePattern,
                                                             final String stepNamePattern, final int start, final int count) {

      // TODO par ordre décroissant d'ID
    final List<JobStepsCql> listJobStepsCql = stepsDao.getJobStepsCqlByJobNameAndSetName(jobNamePattern, stepNamePattern);

    Collections.sort(listJobStepsCql);
    
      int compteur = 0;
      // recuperation des ids des steps
    final List<Long> stepIds = new ArrayList<>(count);
      for (JobStepsCql stepcql : listJobStepsCql) {
         compteur++;
         if (compteur >= start) {
            stepIds.add(stepcql.getJobStepId());
         }
         if (compteur == count + start) {
            break;
         }

    }

      // recuperation des steps en fonction de leurs ids
    final List<JobStepCql> listStep = new ArrayList<>();
      for (final Long id : stepIds) {
      if (findWithMapperById(id).isPresent()) {
        listStep.add(findWithMapperById(id).get());
         }
      }

      // transformation de JobStepCql en StepExecution
    final List<StepExecution> list = new ArrayList<>(stepIds.size());
      for (final JobStepCql step : listStep) {
         list.add(JobTranslateUtils.getStepExecutionFromStpeCql(null, step));
      }

      return list;
   }

   @Override
   @SuppressWarnings("unchecked")
   public final Collection<String> findStepNamesForJobExecution(final String jobName,
                                                                final String excludesPattern) {

	   // recherche de tous les steps du job de nom jobName
	   // select * from JobStepsCql where jobname=jobName;
      final List<JobStepsCql> listJobStepsCql = stepsDao.getJobStepsCqlByJobName(jobName);

      final Set<String> stepNames = new HashSet<String>();
      for(JobStepsCql stepscql : listJobStepsCql) {
         final String currentStepName = stepscql.getStepName();
         stepNames.add(currentStepName);
      }
      return stepNames;
   }
   
   
  private void registerCodecIfNotFound(final CodecRegistry registry, final TypeCodec<?> codec) {
    try {
      registry.codecFor(codec.getCqlType(), codec.getJavaType());
    }
    catch (final CodecNotFoundException e) {
      registry.register(codec);
    }
  }
}
