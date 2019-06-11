
package fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl;

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
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.google.common.collect.Iterators;

import fr.urssaf.image.commons.cassandra.cql.codec.BytesBlobCodec;
import fr.urssaf.image.commons.cassandra.cql.codec.JsonCodec;
import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsRunningCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionToJobStepDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionsDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionsRunningDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceToJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.IdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.ExecutionContextCodec;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;

@Repository
public class JobExecutionDaoCqlImpl extends GenericDAOImpl<JobExecutionCql, Long> implements IJobExecutionDaoCql {

   /**
    * le nom de la colonne indexée dals la table d'index JobExecutionsRunning
    */
   private static final String JOB_NAME = "jobname";

   /**
    * le nom de la colonne contenant les ids des execution
    */
   private static final String JOBEXECUTIONID = "jobexecutionid";

   private static final String ALL = "all";

   // protected static final String JOBEXECUTIONS_CFNAME = "JobExecutions";

   @Autowired
   @Qualifier("jobexecutionidgeneratorcql")
   private IdGenerator idGenerator;

   @Autowired
   IJobExecutionsDaoCql jobExsDaoCql;

   @Autowired
   IJobExecutionToJobStepDaoCql jobExToJobStepDaoCql;

   @Autowired
   IJobExecutionsRunningDaoCql jobExRunDaoCql;

   @Autowired
   IJobInstanceToJobExecutionDaoCql jobInstToJExDaoCql;

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

   /**
    * {@inheritDoc}
    */
   @Override
   public void saveJobExecution(final JobExecution jobExecution) {
      Assert.notNull(jobExecution, "JobExecution cannot be null.");

      validateJobExecution(jobExecution);
      jobExecution.incrementVersion();

      final long executionId = idGenerator.getNextId();
      jobExecution.setId(executionId);

      saveJobExecutionToCassandra(jobExecution);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void updateJobExecution(final JobExecution jobExecution) {

      // Le nom de la méthode n'est pas super explicite, mais is s'agit
      // d'enregister le jobExecution
      // en base de données.

      Assert.notNull(jobExecution.getId(), "JobExecution ID cannot be null. JobExecution must be saved before it can be updated");
      Assert.notNull(jobExecution.getVersion(), "JobExecution version cannot be null. JobExecution must be saved before it can be updated");

      validateJobExecution(jobExecution);
      jobExecution.incrementVersion();
      saveJobExecutionToCassandra(jobExecution);

   }

   /**
    * Validate JobExecution. At a minimum, JobId, StartTime, EndTime, and Status
    * cannot be null.
    *
    * @param jobExecution
    * @throws IllegalArgumentException
    */
   private void validateJobExecution(final JobExecution jobExecution) {

      Assert.notNull(jobExecution);
      Assert.notNull(jobExecution.getJobId(),
                     "JobExecution Job-Id cannot be null.");
      Assert.notNull(jobExecution.getStatus(),
                     "JobExecution status cannot be null.");
      Assert.notNull(jobExecution.getCreateTime(),
                     "JobExecution create time cannot be null");
   }

   /**
    * Enregistre un jobExecution dans cassandra Le jobExecution doit avoir un id
    * affecté.
    * <br>
    * Alimentation des différents index
    *
    * @param jobExecution
    */
   private void saveJobExecutionToCassandra(final JobExecution jobExecution) {

      // On écrit dans cassandra
      final JobExecutionCql executionCql = JobTranslateUtils.JobExecutionToJobExecutionCql(jobExecution);
      this.saveWithMapper(executionCql);

      // Alimentation des différents index

      // insertion dans JOBINSTANCE_TO_JOBEXECUTION_CFNAME
      // Index permettant de faire la recherche des exécution des jobs en fonction des ids des instances
      final JobInstanceToJobExecutionCql jobIToJEx = new JobInstanceToJobExecutionCql();
      jobIToJEx.setJobExecutionId(jobExecution.getId());
      jobIToJEx.setJobInstanceId(jobExecution.getJobInstance().getId());
      jobInstToJExDaoCql.saveWithMapper(jobIToJEx);

      // insertion dans JOBEXECUTIONS_CFNAME avec la clé jobName
      // Index permettant de faire la recheche d'un job exécution par son nom
      final JobExecutionsCql jobExes = new JobExecutionsCql();
      jobExes.setJobExecutionId(jobExecution.getId());
      jobExes.setJobName(jobExecution.getJobInstance().getJobName());
      // jobExes.setCreationTime(new Date());
      jobExsDaoCql.saveWithMapper(jobExes);

      // insertion dans JOBEXECUTIONS_RUNNING_CFNAME avec la clé jobName
      // Index permettant de faire la recherche d'un job en cours d'exécution par son nom
      final JobExecutionsRunningCql jobRun = new JobExecutionsRunningCql();
      jobRun.setJobName(jobExecution.getJobInstance().getJobName());
      jobRun.setJobExecutionId(jobExecution.getId());
      if (jobExecution.isRunning()) {
         jobExRunDaoCql.saveWithMapper(jobRun);

      } else {
         // suppression de l'index JOBEXECUTIONS_RUNNING_CFNAME avec le second key jobName
         jobExRunDaoCql.deleteWithMapper(jobRun);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<JobExecution> findJobExecutions(final JobInstance jobInstance) {
      Assert.notNull(jobInstance, "Job cannot be null.");
      Assert.notNull(jobInstance.getId(), "Job Id cannot be null.");

      final List<JobExecution> jobsExSpring = new ArrayList<JobExecution>();
      //
      final List<Long> idsJobExe = new ArrayList<Long>();

      final Iterator<JobInstanceToJobExecutionCql> it = jobInstToJExDaoCql.findAllWithMapperById(jobInstance.getId());
      while (it.hasNext()) {
         idsJobExe.add(it.next().getJobExecutionId());
      }

      for (int i = idsJobExe.size() - 1; i >= 0; i--) {
         final Long id = idsJobExe.get(i);
         final Optional<JobExecutionCql> opt = this.findWithMapperById(id);
         if (opt.isPresent()) {
            final JobExecutionCql jobcql = opt.get();
            jobsExSpring.add(JobTranslateUtils.JobExecutionCqlToJobExecution(jobcql, jobInstance));
         }
      }
      /*
       * // Récuperation de la list des jobs ExecutionCql en fonction des ids
       * final Iterator<JobExecutionCql> itJobExe = this.findAllWithMapper();
       * // Transformer la liste de JobExecutionsCql en liste de JobExecution
       * while (itJobExe.hasNext()) {
       * final JobExecutionCql jobcql = itJobExe.next();
       * if (idsJobExe.contains(jobcql.getJobExecutionId())) {
       * jobsExSpring.add(JobTranslateUtils.JobExecutionCqlToJobExecution(jobcql, jobInstance));
       * }
       * }
       */
      return jobsExSpring;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JobExecution getLastJobExecution(final JobInstance jobInstance) {
      Assert.notNull(jobInstance, "Job cannot be null.");

      // Récupération dans JobInstanceToJobExecutionCql, de l'executionId le plus grand

      final JobInstanceToJobExecutionCql jobExeToJInst = jobInstToJExDaoCql.getLastJobInstanceToJobExecution(jobInstance.getId());
      final Long executionId = jobExeToJInst.getJobExecutionId();

      // Récuperation de JobExecutionCql ayant le plus grand executionId
      final Optional<JobExecutionCql> opt = this.findWithMapperById(executionId);
      if (opt.isPresent()) {
         return JobTranslateUtils.JobExecutionCqlToJobExecution(opt.get(), jobInstance);
      }
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Set<JobExecution> findRunningJobExecutions(final String jobName) {

      final Set<JobExecution> set = new HashSet<JobExecution>();
      final Iterator<JobExecutionsRunningCql> it = jobExRunDaoCql.findAllWithMapperById(jobName);

      // Récupération des id des jobExecutions, par ordre décroissant
      while (it.hasNext()) {
         final JobExecutionsRunningCql job = it.next();
         final Optional<JobExecutionCql> opt = this.findWithMapperById(job.getJobExecutionId());
         if (opt.isPresent()) {
            set.add(JobTranslateUtils.JobExecutionCqlToJobExecution(opt.get(), null));
         }
      }
      // Récupération des executions à partir des ids
      /*
       * final Iterator<JobExecutionCql> itJobExe = this.findAllWithMapper();
       * while (itJobExe.hasNext()) {
       * final JobExecutionCql jobcql = itJobExe.next();
       * if (ids.contains(jobcql.getJobExecutionId())) {
       * set.add(JobTranslateUtils.JobExecutionCqlToJobExecution(jobcql, null));
       * }
       * }
       */

      return set;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JobExecution getJobExecution(final Long executionId) {
      Assert.notNull(executionId, "executionId cannot be null.");
      final Optional<JobExecutionCql> opt = this.findWithMapperById(executionId);
      if (opt.isPresent()) {
         return JobTranslateUtils.JobExecutionCqlToJobExecution(opt.get(), null);
      }
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void synchronizeStatus(final JobExecution jobExecution) {
      // On lit le status et la version dans cassandra
      final Optional<JobExecutionCql> opt = this.findWithMapperById(jobExecution.getId());

      if (!opt.isPresent()) {
         return;
      }
      final JobExecutionCql jobExe = opt.get();
      jobExecution.setStatus(jobExe.getStatus());
      jobExecution.setVersion(jobExe.getVersion());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int countJobExecutions() {
      return jobExsDaoCql.count();
   }

   /**
    * {@inheritDoc}
    * D'après l'implémentation JDBC, il faut lier les objets jobExecution à des objets
    * jobInstance contenant un id, un jobName, et des paramètres null
    * Sinon, ça fait planter spring-batch-admin.
    */
   @Override
   public List<JobExecution> getJobExecutions(final int start, final int count) {
      return getJobExecutions(ALL, start, count);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<JobExecution> getJobExecutions(final String jobName, final int start, final int count) {

      final List<JobExecution> list = new ArrayList<JobExecution>();
      final List<JobExecutionsCql> jobExes = new ArrayList<JobExecutionsCql>();

      Iterator<JobExecutionsCql> it;
      if (ALL.equals(jobName)) {
         it = jobExsDaoCql.findAllWithMapper();
      } else {
         it = jobExsDaoCql.findAllWithMapperById(jobName);
      }
      while (it.hasNext()) {
         jobExes.add(it.next());
      }

      int i = 0;
      for (final JobExecutionsCql job : jobExes) {
         if (i >= start && list.size() < count) {
            list.add(getJobExecution(job.getJobExecutionId()));
         }
         i++;
      }
      return list;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int countJobExecutions(final String jobName) {
      // possible car jobName est la clé primaire de la table donc il possible de faire une recherche
      // sur la colonne
      final Iterator<JobExecutionsCql> it = jobExsDaoCql.findAllWithMapperById(jobName);
      final int count = Iterators.size(it);
      return count;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Collection<JobExecution> getRunningJobExecutions() {

      // Récupération des id, par ordre décroissant

      final List<JobExecution> list = new ArrayList<JobExecution>();
      final Iterator<JobExecutionsRunningCql> it = jobExRunDaoCql.findAllWithMapper();
      final List<Long> jobExecutionIds = new ArrayList<Long>();
      while (it.hasNext()) {
         final JobExecutionsRunningCql job = it.next();
         jobExecutionIds.add(job.getJobExecutionId());
      }
      Collections.sort(jobExecutionIds, Collections.reverseOrder());
      for (final Long id : jobExecutionIds) {
         final Optional<JobExecutionCql> opt = this.findWithMapperById(id);
         if (opt.isPresent()) {
            list.add(JobTranslateUtils.JobExecutionCqlToJobExecution(opt.get(), null));
         }
      }
      /*
       * final Iterator<JobExecutionCql> itJobExes = this.findAllWithMapper();
       * while (itJobExes.hasNext()) {
       * final JobExecutionCql job = itJobExes.next();
       * list.add(JobTranslateUtils.JobExecutionCqlToJobExecution(job, null));
       * }
       */
      return list;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteJobExecution(final long jobExecutionId, final String jobName, final IJobStepExecutionDaoCql stepExecutionDao) {

      // Suppression des steps
      final JobExecution jobExecution = getJobExecution(jobExecutionId);
      stepExecutionDao.addStepExecutions(jobExecution);
      stepExecutionDao.deleteStepsOfExecution(jobExecution);

      // Suppression des indexations de jobExecutions
      /*
       * final Iterator<JobExecutionsCql> itJobExes = jobExsDaoCql.findAllWithMapper();
       * while (itJobExes.hasNext()) {
       * final JobExecutionsCql jobExs = itJobExes.next();
       * if (jobExs.getJobExecutionId().equals(jobExecutionId)) {
       * jobExsDaoCql.deleteWithMapper(jobExs);
       * }
       * }
       */
      final Optional<JobExecutionsCql> opt = jobExsDaoCql.findByJobExecutionId(jobExecutionId);
      if (opt.isPresent()) {
         jobExsDaoCql.deleteWithMapper(opt.get());
      }

      // suppression des index JobExecutionToJobStep
      // Dans ce cas on supprime toutes les lignes qui font reference à l'id de jobExecution. On peut en avoir plusieurs...
      final Delete delete1 = QueryBuilder.delete().from(ccf.getKeyspace(), ColumnUtil.getColumnFamily(JobExecutionToJobStepCql.class));
      delete1.where(QueryBuilder.eq(JOBEXECUTIONID, jobExecutionId));
      jobExToJobStepDaoCql.getSession().execute(delete1);

      // Suppression de l'index dans JobExecutionsRunning
      if (jobExecution.isRunning()) {

         final Delete delete2 = QueryBuilder.delete().from(ccf.getKeyspace(), ColumnUtil.getColumnFamily(JobExecutionsRunningCql.class));
         delete2.where(QueryBuilder.eq(JOB_NAME, jobName)).and(QueryBuilder.eq(JOBEXECUTIONID, jobExecutionId));
         jobExRunDaoCql.getSession().execute(delete2);
      }
      // On ne supprime rien dans JobInstanceToJobExecution : ça sera fait lors de la suppression
      // de l'instance

      // Suppression du jobExecution
      this.deleteById(jobExecutionId);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteJobExecutionsOfInstance(final JobInstance jobInstance, final IJobStepExecutionDaoCql stepExecutionDao) {
      Assert.notNull(jobInstance, "JobInstance cannot be null.");
      Assert.notNull(stepExecutionDao, "stepExecutionDao cannot be null.");
      final List<JobExecution> list = findJobExecutions(jobInstance);
      for (final JobExecution jobExecution : list) {
         deleteJobExecution(jobExecution.getId(), jobInstance.getJobName(), stepExecutionDao);
      }

   }

}
