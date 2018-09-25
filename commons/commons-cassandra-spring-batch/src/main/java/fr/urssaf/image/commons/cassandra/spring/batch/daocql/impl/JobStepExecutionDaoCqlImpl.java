
package fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl;

import java.util.ArrayList;
import java.util.Collection;
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

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionToJobStepDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepsDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.helper.CassandraJobHelper;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.IdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.ExecutionContextCodec;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.sae.commons.context.BytesBlobCodec;
import fr.urssaf.image.sae.commons.context.JsonCodec;
import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;

@Repository
public class JobStepExecutionDaoCqlImpl extends GenericDAOImpl<JobStepCql, Long> implements IJobStepExecutionDaoCql {

  @Autowired
  @Qualifier("stepexecutionidgeneratorcql")
  private IdGenerator idGenerator;

  @Autowired
  private IJobExecutionToJobStepDaoCql jobExeToStepDaoCql;

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
    ccf.getCluster().getConfiguration().getCodecRegistry().register(new JsonCodec<BatchStatus>(BatchStatus.class));
    ccf.getCluster().getConfiguration().getCodecRegistry().register(BytesBlobCodec.instance);
    ccf.getCluster().getConfiguration().getCodecRegistry().register(ExecutionContextCodec.instance);

  }

  @Override
  public final void addStepExecutions(final JobExecution jobExecution) {
    Assert.notNull(jobExecution, "JobExecution cannot be null.");
    Assert.notNull(jobExecution.getId(), "JobExecution Id cannot be null.");
    final long jobExecutionId = jobExecution.getId();

    // Récupération des id des steps
    final Iterator<JobExecutionToJobStepCql> it = jobExeToStepDaoCql.findAllWithMapperById(jobExecutionId);

    final List<Long> stepIds = new ArrayList<Long>();
    while (it.hasNext()) {
      stepIds.add(it.next().getJobStepId());
    }

    final Iterator<JobStepCql> itJobSt = this.findAllWithMapper();

    final List<StepExecution> list = new ArrayList<StepExecution>(stepIds.size());
    while (itJobSt.hasNext()) {
      final JobStepCql jobcql = itJobSt.next();
      // Charger les steps dans l'objet parent jobExecution
      list.add(JobTranslateUtils.getStepExecutionFromStpeCql(jobExecution, jobcql));
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
   *          : step à enregistrer
   */
  private void saveStepExecutionToCassandra(final StepExecution stepExecution) {

    // On écrit dans cassandra
    final JobStepCql stepCql = JobTranslateUtils.getStpeCqlFromStepExecution(stepExecution);
    this.save(stepCql);

    // Alimentation des différents index

    // on ecrit Dans JobExecutionToJobStep
    // clé = jobExecutionId
    // Nom de colonne = jobStepId
    // Valeur = vide
    final JobExecutionToJobStepCql jobExToStep = new JobExecutionToJobStepCql();
    jobExToStep.setJobExecutionId(stepExecution.getId());
    jobExToStep.setJobStepId(stepCql.getJobStepExecutionId());
    jobExeToStepDaoCql.save(jobExToStep);

    // on ecrit Dans JobSteps
    // clé = "jobSteps"
    // Nom de colonne = stepId
    // Valeur = composite(jobName, stepName)
    final JobStepsCql jobSteps = new JobStepsCql();
    jobSteps.setJobName(stepExecution.getJobExecution().getJobInstance().getJobName());
    jobSteps.setJobStepId(stepExecution.getId());
    jobSteps.setStepName(stepExecution.getStepName());
    stepsDao.save(jobSteps);

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
   *          : id du step à supprimer
   */
  @Override
  public final void deleteStepExecution(final Long stepExecutionId) {

    // On supprimee dans JobStep
    this.deleteById(stepExecutionId);
  }

  /**
   * Supprime tous les steps d'un jobExecution donné
   *
   * @param jobExecution
   *          : jobExecution concerné
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

    final Iterator<JobStepsCql> it = stepsDao.findAllWithMapper();

    int compteur = 0;
    while (it.hasNext()) {
      final JobStepsCql step = it.next();

      final String jobName = step.getJobName();
      final String stepName = step.getStepName();
      if (CassandraJobHelper.checkPattern(jobNamePattern, jobName)
          && CassandraJobHelper.checkPattern(stepNamePattern, stepName)) {
        compteur++;
      }
    }
    return compteur;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Collection<StepExecution> findStepExecutions(final String jobNamePattern,
                                                            final String stepNamePattern, final int start, final int count) {

    // TODO par ordre décroissant d'ID
    final Iterator<JobStepsCql> it = stepsDao.findAllWithMapper();

    int compteur = 0;
    // recuperation des ids des steps
    final List<Long> stepIds = new ArrayList<Long>(count);
    while (it.hasNext()) {
      final JobStepsCql step = it.next();
      final String jobName = step.getJobName();
      final String stepName = step.getStepName();
      if (CassandraJobHelper.checkPattern(jobNamePattern, jobName) &&
          CassandraJobHelper.checkPattern(stepNamePattern, stepName)) {
        compteur++;
        if (compteur >= start) {
          stepIds.add(step.getJobStepId());
        }
        if (compteur == count + start) {
          break;
        }
      }
    }

    // recuperation des steps en fonction de leurs ids
    final List<JobStepCql> listStep = new ArrayList<JobStepCql>();
    for (final Long id : stepIds) {
      if (this.findWithMapperById(id).isPresent()) {
        listStep.add(this.findWithMapperById(id).get());
      }
    }

    // transformation de JobStepCql en StepExecution
    final List<StepExecution> list = new ArrayList<StepExecution>(stepIds.size());
    for (final JobStepCql step : listStep) {
      list.add(JobTranslateUtils.getStepExecutionFromStpeCql(null, step));
    }

    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Collection<String> findStepNamesForJobExecution(final String jobName,
                                                               final String excludesPattern) {

    final Iterator<JobStepsCql> it = stepsDao.findAllWithMapper();

    final Set<String> stepNames = new HashSet<String>();
    while (it.hasNext()) {
      final JobStepsCql step = it.next();
      final String currentJobName = step.getJobName();
      final String currentStepName = step.getStepName();
      if (currentJobName.equals(jobName)
          && !CassandraJobHelper.checkPattern(excludesPattern, currentStepName)) {
        stepNames.add(currentStepName);
      }
    }
    return stepNames;
  }

}
