package fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.admin.service.SearchableJobExecutionDao;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.AbstractCassandraDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.IdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;
import me.prettyprint.cassandra.model.HSlicePredicate;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Classe implémentant JobExecutionDao et SearchableJobExecutionDao,
 * qui utilise cassandra.
 * L'implémentation est inspirée de
 * org.springframework.batch.core.repository.dao.JdbcJobExecutionDao
 *
 * @see org.springframework.batch.core.repository.dao.JdbcJobExecutionDao
 * @see org.springframework.batch.admin.service.JdbcSearchableJobExecutionDao
 * @author Samuel Carrière
 */
@SuppressWarnings("PMD.TooManyMethods") // On implémente toutes les méthodes de l'interface
public class CassandraJobExecutionDaoThrift extends AbstractCassandraDAO implements
      SearchableJobExecutionDao {

   private final IdGenerator idGenerator;

   private static final int MAX_COLS = 500;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           : keyspace cassandra
    * @param idGenerator
    *           : générateur d'id pour les jobExecution
    */
   public CassandraJobExecutionDaoThrift(final Keyspace keyspace, final IdGenerator idGenerator) {
      super(keyspace);
      this.idGenerator = idGenerator;
   }

   @Override
   public final List<JobExecution> findJobExecutions(final JobInstance jobInstance) {
      Assert.notNull(jobInstance, "Job cannot be null.");
      Assert.notNull(jobInstance.getId(), "Job Id cannot be null.");
      final long jobInstanceId = jobInstance.getId();

      // Récupération des id des jobExecutions, par ordre décroissant
      final HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(MAX_COLS);
      final ColumnFamilyResult<Long, Long> result1 = jobInstanceToJobExecutionTemplate.queryColumns(jobInstanceId, predicate);
      final Collection<Long> jobExecutionIds = result1.getColumnNames();

      return getJobExecutionsFromIds(jobExecutionIds, jobInstance);

   }

   /**
    * Récupère une liste de JobExecution à partir d'une liste d'id
    * On fait en sorte de renvoyer les jobExecution dans le même ordre que la liste des id
    * 
    * @param jobExecutionIds
    *           Liste des id des jobExecution
    * @param jobInstance
    *           jobInstance lié au jobExecution (éventuellement nul)
    * @return
    */
   private List<JobExecution> getJobExecutionsFromIds(final Collection<Long> jobExecutionIds, final JobInstance jobInstance) {
      final ColumnFamilyResult<Long, String> result = jobExecutionTemplate.queryColumns(jobExecutionIds);
      final Map<Long, JobExecution> map = new HashMap<Long, JobExecution>(jobExecutionIds.size());
      final HectorIterator<Long, String> resultIterator = new HectorIterator<Long, String>(result);
      for (final ColumnFamilyResult<Long, String> row : resultIterator) {
         final JobExecution jobExecution = getJobExecutionFromResult(row, jobInstance);
         map.put(row.getKey(), jobExecution);
      }

      // On renvoie les jobExecution dans l'ordre des jobExecutionIds
      final List<JobExecution> list = new ArrayList<JobExecution>(jobExecutionIds.size());
      for (final Long jobExecutionId : jobExecutionIds) {
         if (map.containsKey(jobExecutionId)) {
            list.add(map.get(jobExecutionId));
         }
      }
      return list;
   }

   /**
    * Supprime un jobExecution
    *
    * @param jobExecutionId
    *           id du jobExecution à supprimer
    * @param jobName
    *           nom du job
    * @param stepExecutionDao
    *           DAO permettant de supprimer les steps de l'instance
    */
   public final void deleteJobExecution(final long jobExecutionId, final String jobName,
                                        final CassandraStepExecutionDaoThrift stepExecutionDao) {
      // Suppression des steps
      final JobExecution jobExecution = getJobExecution(jobExecutionId);
      stepExecutionDao.addStepExecutions(jobExecution);
      stepExecutionDao.deleteStepsOfExecution(jobExecution);
      // Suppression des indexations de jobExecution
      jobExecutionsTemplate.deleteColumn(jobName, jobExecutionId);
      jobExecutionsTemplate.deleteColumn(ALL_JOBS_KEY, jobExecutionId);
      jobExecutionToJobStepTemplate.deleteRow(jobExecutionId);
      if (jobExecution.isRunning()) {
         jobExecutionsRunningTemplate.deleteColumn(jobName, jobExecutionId);
         jobExecutionsRunningTemplate.deleteColumn(ALL_JOBS_KEY, jobExecutionId);
      }
      // On ne supprime rien dans JobInstanceToJobExecution : ça sera fait lors de la suppression
      // de l'instance

      // Suppression du jobExecution
      jobExecutionTemplate.deleteRow(jobExecutionId);
   }

   /**
    * Supprime tous les jobExecutions relatif à une instance de job donnée
    *
    * @param jobInstance
    *           jobInstance concerné
    * @param stepExecutionDao
    *           DAO permettant de supprimer les steps de l'instance
    */
   public final void deleteJobExecutionsOfInstance(final JobInstance jobInstance,
                                                   final CassandraStepExecutionDaoThrift stepExecutionDao) {
      Assert.notNull(jobInstance, "JobInstance cannot be null.");
      Assert.notNull(stepExecutionDao, "stepExecutionDao cannot be null.");
      final List<JobExecution> list = findJobExecutions(jobInstance);
      for (final JobExecution jobExecution : list) {
         deleteJobExecution(jobExecution.getId(), jobInstance.getJobName(), stepExecutionDao);
      }
   }

   /**
    * Crée un objet JobExecution à partir de données lues de cassandra.
    * 
    * @param result
    *           Données de cassandra
    * @param jobInstance
    *           Si non nul : jobInstance lié au jobExecution à renvoyé
    *           Si nul, on instanciera un jobInstance "minimal"
    * @return le jobExecution
    */
   private JobExecution getJobExecutionFromResult(
                                                  final ColumnFamilyResult<Long, String> result, final JobInstance jobInstance) {
      if (result == null || !result.hasResults()) {
         return null;
      }

      final Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
      final Serializer<Date> dSlz = NullableDateSerializer.get();

      final Long executionId = result.getKey();
      final JobExecution jobExecution = new JobExecution(executionId);

      final Long jobInstanceId = result.getLong(JE_JOB_INSTANCE_ID_COLUMN);
      final String jobName = result.getString(JE_JOBNAME_COLUMN);
      final Date createTime = dSlz.fromBytes(result
                                                   .getByteArray(JE_CREATION_TIME_COLUMN));
      jobExecution.setCreateTime(createTime);
      final ExecutionContext executionContext = oSlz.fromBytes(result
                                                                     .getByteArray(JE_EXECUTION_CONTEXT_COLUMN));
      jobExecution.setExecutionContext(executionContext);
      final int version = result.getInteger(JE_VERSION_COLUMN);
      jobExecution.setVersion(version);
      final Date startDate = dSlz.fromBytes(result.getByteArray(JE_START_TIME_COLUMN));
      jobExecution.setStartTime(startDate);
      final Date endDate = dSlz.fromBytes(result.getByteArray(JE_END_TIME_COLUMN));
      jobExecution.setEndTime(endDate);
      final Date lastDate = dSlz.fromBytes(result.getByteArray(JE_LAST_UPDATED_COLUMN));
      jobExecution.setLastUpdated(lastDate);
      final String status = result.getString(JE_STATUS_COLUMN);
      jobExecution.setStatus(BatchStatus.valueOf(status));
      final String exitCode = result.getString(JE_EXIT_CODE_COLUMN);
      final String exitMessage = result.getString(JE_EXIT_MESSAGE_COLUMN);
      jobExecution.setExitStatus(new ExitStatus(exitCode, exitMessage));

      if (jobInstance == null) {
         // On fait comme dans l'implémentation JDBC : on instancie une instance
         // avec des paramètres nuls
         jobExecution.setJobInstance(new JobInstance(jobInstanceId, null, jobName));
      } else {
         jobExecution.setJobInstance(jobInstance);
      }
      return jobExecution;
   }

   @Override
   public final Set<JobExecution> findRunningJobExecutions(final String jobName) {
      final Set<JobExecution> set = new HashSet<JobExecution>();

      // Récupération des id des jobExecutions, par ordre décroissant
      final HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setCount(MAX_COLS);
      predicate.setReversed(true);
      final ColumnFamilyResult<String, Long> result1 = jobExecutionsRunningTemplate.queryColumns(jobName, predicate);
      final Collection<Long> jobExecutionIds = result1.getColumnNames();

      // Récupération des executions à partir des ids
      final ColumnFamilyResult<Long, String> result = jobExecutionTemplate.queryColumns(jobExecutionIds);

      final HectorIterator<Long, String> resultIterator = new HectorIterator<Long, String>(result);
      for (final ColumnFamilyResult<Long, String> row : resultIterator) {
         final JobExecution jobExecution = getJobExecutionFromResult(row, null);
         set.add(jobExecution);
      }

      return set;
   }

   @Override
   public final JobExecution getJobExecution(final Long executionId) {
      Assert.notNull(executionId, "executionId cannot be null.");
      final ColumnFamilyResult<Long, String> result = jobExecutionTemplate
                                                                          .queryColumns(executionId);
      return getJobExecutionFromResult(result, null);
   }

   @Override
   public final JobExecution getLastJobExecution(final JobInstance jobInstance) {

      // Récupération dans jobExecutions, de l'executionId le plus grand
      final HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(1);
      final ColumnFamilyResult<Long, Long> result1 = jobInstanceToJobExecutionTemplate.queryColumns(jobInstance.getId(), predicate);
      final Collection<Long> jobExecutionIds = result1.getColumnNames();
      if (jobExecutionIds.isEmpty()) {
         return null;
      }
      final Long jobExecutionId = jobExecutionIds.iterator().next();
      return getJobExecution(jobExecutionId);
   }

   @Override
   public final void saveJobExecution(final JobExecution jobExecution) {
      validateJobExecution(jobExecution);
      jobExecution.incrementVersion();

      final long executionId = idGenerator.getNextId();
      jobExecution.setId(executionId);

      saveJobExecutionToCassandra(jobExecution);
   }

   /**
    * Enregistre un jobExecution dans cassandra Le jobExecution doit avoir un id
    * affecté.
    *
    * @param jobExecution
    */
   private void saveJobExecutionToCassandra(final JobExecution jobExecution) {
      final Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
      final NullableDateSerializer dSlz = NullableDateSerializer.get();
      final Serializer<String> sSlz = StringSerializer.get();
      final Serializer<Long> lSlz = LongSerializer.get();
      final Serializer<byte[]> bSlz = BytesArraySerializer.get();
      final Long jobInstanceId = jobExecution.getJobId();
      final Long jobExecutionId = jobExecution.getId();
      final String jobName = jobExecution.getJobInstance().getJobName();

      final ColumnFamilyUpdater<Long, String> updater = jobExecutionTemplate
                                                                            .createUpdater(jobExecution.getId());

      updater.setLong(JE_JOB_INSTANCE_ID_COLUMN, jobInstanceId);
      updater.setString(JE_JOBNAME_COLUMN, jobName);
      updater.setByteArray(JE_CREATION_TIME_COLUMN, dSlz.toBytes(jobExecution
                                                                             .getCreateTime()));
      updater.setByteArray(JE_EXECUTION_CONTEXT_COLUMN, oSlz.toBytes(jobExecution
                                                                                 .getExecutionContext()));
      updater.setInteger(JE_VERSION_COLUMN, jobExecution.getVersion());
      updater.setByteArray(JE_START_TIME_COLUMN, dSlz.toBytes(jobExecution
                                                                          .getStartTime()));
      updater.setByteArray(JE_END_TIME_COLUMN, dSlz.toBytes(jobExecution
                                                                        .getEndTime()));
      updater.setString(JE_STATUS_COLUMN, jobExecution.getStatus().name());
      updater.setString(JE_EXIT_CODE_COLUMN, jobExecution.getExitStatus()
                                                         .getExitCode());
      updater.setString(JE_EXIT_MESSAGE_COLUMN, jobExecution.getExitStatus()
                                                            .getExitDescription());
      updater.setByteArray(JE_LAST_UPDATED_COLUMN, dSlz.toBytes(jobExecution
                                                                            .getLastUpdated()));

      // On écrit dans cassandra
      jobExecutionTemplate.update(updater);

      // Alimentation des différents index
      final byte[] empty = new byte[0];
      final Mutator<byte[]> mutator = HFactory.createMutator(keyspace, bSlz);
      mutator.addInsertion(lSlz.toBytes(jobInstanceId),
                           JOBINSTANCE_TO_JOBEXECUTION_CFNAME,
                           HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
      mutator.addInsertion(sSlz.toBytes(jobName),
                           JOBEXECUTIONS_CFNAME,
                           HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
      mutator.addInsertion(sSlz.toBytes(ALL_JOBS_KEY),
                           JOBEXECUTIONS_CFNAME,
                           HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));

      if (jobExecution.isRunning()) {
         mutator.addInsertion(sSlz.toBytes(jobName),
                              JOBEXECUTIONS_RUNNING_CFNAME,
                              HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
         mutator.addInsertion(sSlz.toBytes(ALL_JOBS_KEY),
                              JOBEXECUTIONS_RUNNING_CFNAME,
                              HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
      } else {
         mutator.addDeletion(sSlz.toBytes(jobName), JOBEXECUTIONS_RUNNING_CFNAME, jobExecutionId, lSlz);
         mutator.addDeletion(sSlz.toBytes(ALL_JOBS_KEY), JOBEXECUTIONS_RUNNING_CFNAME, jobExecutionId, lSlz);
      }
      mutator.execute();
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

   @Override
   public final void synchronizeStatus(final JobExecution jobExecution) {
      // On lit le status et la version dans cassandra
      final ColumnFamilyResult<Long, String> result = jobExecutionTemplate
                                                                          .queryColumns(jobExecution.getId());
      if (result == null || !result.hasResults()) {
         return;
      }
      final String status = result.getString(JE_STATUS_COLUMN);
      jobExecution.setStatus(BatchStatus.valueOf(status));
      final int version = result.getInteger(JE_VERSION_COLUMN);
      jobExecution.setVersion(version);
   }

   @Override
   public final void updateJobExecution(final JobExecution jobExecution) {
      // Le nom de la méthode n'est pas super explicite, mais is s'agit
      // d'enregister le jobExecution
      // en base de données.

      Assert
            .notNull(
                     jobExecution.getId(),
                     "JobExecution ID cannot be null. JobExecution must be saved before it can be updated");

      Assert
            .notNull(
                     jobExecution.getVersion(),
                     "JobExecution version cannot be null. JobExecution must be saved before it can be updated");

      validateJobExecution(jobExecution);
      jobExecution.incrementVersion();
      saveJobExecutionToCassandra(jobExecution);
   }

   @Override
   public final int countJobExecutions() {
      return jobExecutionsTemplate.countColumns(ALL_JOBS_KEY);
   }

   @Override
   public final int countJobExecutions(final String jobName) {
      return jobExecutionsTemplate.countColumns(jobName);
   }

   @Override
   /**
    * {@inheritDoc}
    * D'après l'implémentation JDBC, il faut lier les objets jobExecution à des objets
    * jobInstance contenant un id, un jobName, et des paramètres null
    * Sinon, ça fait planter spring-batch-admin.
    */
   public final List<JobExecution> getJobExecutions(final int start, final int count) {
      return getJobExecutions(ALL_JOBS_KEY, start, count);
   }

   @Override
   public final List<JobExecution> getJobExecutions(final String jobName, final int start,
                                                    final int count) {
      // Récupération des id, par ordre décroissant
      final HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(start + count);
      final ColumnFamilyResult<String, Long> result = jobExecutionsTemplate.queryColumns(jobName, predicate);
      final Collection<Long> ids = result.getColumnNames();
      // On ignore les start premiers ids
      final List<Long> jobExecutionIds = new ArrayList<Long>(count);
      final Iterator<Long> iterator = ids.iterator();
      for (int i = 0; i < ids.size(); i++) {
         final Long executionId = iterator.next();
         if (i >= start) {
            jobExecutionIds.add(executionId);
         }
      }
      return getJobExecutionsFromIds(jobExecutionIds, null);
   }

   @Override
   public final Collection<JobExecution> getRunningJobExecutions() {
      // Récupération des id, par ordre décroissant
      final HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(MAX_COLS);
      final ColumnFamilyResult<String, Long> result = jobExecutionsRunningTemplate.queryColumns(ALL_JOBS_KEY, predicate);
      final Collection<Long> jobExecutionIds = result.getColumnNames();
      return getJobExecutionsFromIds(jobExecutionIds, null);
   }

   /**
    * @return le keyspace utilise
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }
}
