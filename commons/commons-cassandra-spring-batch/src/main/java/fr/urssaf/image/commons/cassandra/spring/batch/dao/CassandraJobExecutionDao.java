package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import org.springframework.batch.admin.service.SearchableJobExecutionDao;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.IdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;

/**
 * Classe implémentant JobExecutionDao et SearchableJobExecutionDao,
 * qui utilise cassandra.
 * L'implémentation est inspirée de
 * org.springframework.batch.core.repository.dao.JdbcJobExecutionDao
 * 
 * @see org.springframework.batch.core.repository.dao.JdbcJobExecutionDao
 * @see org.springframework.batch.admin.service.JdbcSearchableJobExecutionDao
 * @author Samuel Carrière
 * 
 */
public class CassandraJobExecutionDao extends AbstractCassandraDAO implements
   SearchableJobExecutionDao {

   private final IdGenerator idGenerator;

   /**
    * Constructeur
    * @param keyspace : keyspace cassandra
    * @param idGenerator : générateur d'id pour les jobExecution
    */
   public CassandraJobExecutionDao(Keyspace keyspace, IdGenerator idGenerator) {
      super(keyspace);
      this.idGenerator = idGenerator;
   }

   /** {@inheritDoc} */
   public List<JobExecution> findJobExecutions(JobInstance jobInstance) {
      Assert.notNull(jobInstance, "Job cannot be null.");
      Assert.notNull(jobInstance.getId(), "Job Id cannot be null.");
      long jobInstanceId = jobInstance.getId();

      // Récupération des id des jobExecutions, par ordre décroissant
      HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(500);
      ColumnFamilyResult<Long, Long> result1 = jobInstanceToJobExecutionTemplate.queryColumns(jobInstanceId, predicate);
      Collection<Long> jobExecutionIds = result1.getColumnNames();

      return getJobExecutionsFromIds(jobExecutionIds, jobInstance);
      
   }

   /**
    * Récupère une liste de JobExecution à partir d'une liste d'id
    * On fait en sorte de renvoyer les jobExecution dans le même ordre que la liste des id
    * @param jobExecutionIds     Liste des id des jobExecution
    * @param jobInstance         jobInstance lié au jobExecution (éventuellement nul)
    * @return
    */
   private List<JobExecution> getJobExecutionsFromIds(Collection<Long> jobExecutionIds, JobInstance jobInstance) {
      ColumnFamilyResult<Long, String> result = jobExecutionTemplate.queryColumns(jobExecutionIds);
      Map<Long, JobExecution> map = new HashMap<Long, JobExecution>(jobExecutionIds.size());
      while (true) {
         if (result.hasResults()) {
            JobExecution jobExecution = getJobExecutionFromResult(result, jobInstance);
            map.put(result.getKey(), jobExecution);
         }
         if (result.hasNext()) result = result.next(); else break;
      }
      // On renvoie les jobExecution dans l'ordre des jobExecutionIds
      List<JobExecution> list = new ArrayList<JobExecution>(jobExecutionIds.size());
      for (Long jobExecutionId : jobExecutionIds) {
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
   public void deleteJobExecution(long jobExecutionId, String jobName,
         CassandraStepExecutionDao stepExecutionDao) {
      // Suppression des steps
      JobExecution jobExecution = getJobExecution(jobExecutionId);
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
   public void deleteJobExecutionsOfInstance(JobInstance jobInstance,
         CassandraStepExecutionDao stepExecutionDao) {
      Assert.notNull(jobInstance, "JobInstance cannot be null.");
      Assert.notNull(stepExecutionDao, "stepExecutionDao cannot be null.");
      List<JobExecution> list = findJobExecutions(jobInstance);
      for (JobExecution jobExecution : list) {
         deleteJobExecution(jobExecution.getId(), jobInstance.getJobName(),  stepExecutionDao);
      }
   }

   /**
    * Crée un objet JobExecution à partir de données lues de cassandra.
    * @param result        Données de cassandra
    * @param jobInstance   Si non nul : jobInstance lié au jobExecution à renvoyé
    *                      Si nul, on instanciera un jobInstance "minimal"
    * @return  le jobExecution
    */
   private JobExecution getJobExecutionFromResult(
         ColumnFamilyResult<Long, String> result, JobInstance jobInstance) {
      if (result == null || !result.hasResults()) return null;

      Serializer<ExecutionContext> os = ExecutionContextSerializer.get();
      Serializer<Date> ds = NullableDateSerializer.get();

      Long executionId = result.getKey();
      JobExecution jobExecution = new JobExecution(executionId);

      Long jobInstanceId = result.getLong(JE_JOB_INSTANCE_ID_COLUMN);
      String jobName = result.getString(JE_JOBNAME_COLUMN);
      Date createTime = ds.fromBytes(result
            .getByteArray(JE_CREATION_TIME_COLUMN));
      jobExecution.setCreateTime(createTime);
      ExecutionContext executionContext = os.fromBytes(result
            .getByteArray(JE_EXECUTION_CONTEXT_COLUMN));
      jobExecution.setExecutionContext(executionContext);
      int version = result.getInteger(JE_VERSION_COLUMN);
      jobExecution.setVersion(version);
      Date startDate = ds.fromBytes(result.getByteArray(JE_START_TIME_COLUMN));
      jobExecution.setStartTime(startDate);
      Date endDate = ds.fromBytes(result.getByteArray(JE_END_TIME_COLUMN));
      jobExecution.setEndTime(endDate);
      Date lastDate = ds.fromBytes(result.getByteArray(JE_LAST_UPDATED_COLUMN));
      jobExecution.setLastUpdated(lastDate);
      String status = result.getString(JE_STATUS_COLUMN);
      jobExecution.setStatus(BatchStatus.valueOf(status));
      String exitCode = result.getString(JE_EXIT_CODE_COLUMN);
      String exitMessage = result.getString(JE_EXIT_MESSAGE_COLUMN);
      jobExecution.setExitStatus(new ExitStatus(exitCode, exitMessage));

      if (jobInstance == null) {
         // On fait comme dans l'implémentation JDBC : on instancie une instance
         // avec des paramètres nuls
         jobExecution.setJobInstance(new JobInstance(jobInstanceId, null, jobName));
      }
      else {
         jobExecution.setJobInstance(jobInstance);
      }
      return jobExecution;
   }

   /** {@inheritDoc} */
   public Set<JobExecution> findRunningJobExecutions(String jobName) {
      final Set<JobExecution> set = new HashSet<JobExecution>();

      // Récupération des id des jobExecutions, par ordre décroissant
      HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setCount(500);
      predicate.setReversed(true);
      ColumnFamilyResult<String, Long> result1 = jobExecutionsRunningTemplate.queryColumns(jobName, predicate);
      Collection<Long> jobExecutionIds = result1.getColumnNames();

      // Récupération des executions à partir des ids
      ColumnFamilyResult<Long, String> result = jobExecutionTemplate.queryColumns(jobExecutionIds);

      while (true) {
         if (result.hasResults()) {
            JobExecution jobExecution = getJobExecutionFromResult(result, null);
            set.add(jobExecution);
         }
         if (result.hasNext()) result = result.next(); else break;
      }
      return set;
   }

   /** {@inheritDoc} */
   public JobExecution getJobExecution(Long executionId) {
      Assert.notNull(executionId, "executionId cannot be null.");
      ColumnFamilyResult<Long, String> result = jobExecutionTemplate
            .queryColumns(executionId);
      return getJobExecutionFromResult(result, null);
   }

   /** {@inheritDoc} */
   public JobExecution getLastJobExecution(JobInstance jobInstance) {

      // Récupération dans jobExecutions, de l'executionId le plus grand
      HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(1);
      ColumnFamilyResult<Long, Long> result1 = jobInstanceToJobExecutionTemplate.queryColumns(jobInstance.getId(), predicate);
      Collection<Long> jobExecutionIds = result1.getColumnNames();
      if (jobExecutionIds.size() == 0) return null;
      Long jobExecutionId = jobExecutionIds.iterator().next();
      return getJobExecution(jobExecutionId);
   }


   /** {@inheritDoc} */
   public void saveJobExecution(JobExecution jobExecution) {
      validateJobExecution(jobExecution);
      jobExecution.incrementVersion();

      long executionId = idGenerator.getNextId();
      jobExecution.setId(executionId);

      saveJobExecutionToCassandra(jobExecution);
   }

   /**
    * Enregistre un jobExecution dans cassandra Le jobExecution doit avoir un id
    * affecté.
    * 
    * @param jobExecution
    */
   private void saveJobExecutionToCassandra(JobExecution jobExecution) {
      Serializer<ExecutionContext> os = ExecutionContextSerializer.get();
      NullableDateSerializer ds = NullableDateSerializer.get();
      Serializer<String> ss = StringSerializer.get();
      Serializer<Long> ls = LongSerializer.get();      
      Serializer<byte[]> bs = BytesArraySerializer.get();
      Long jobInstanceId = jobExecution.getJobId();
      Long jobExecutionId = jobExecution.getId();
      String jobName = jobExecution.getJobInstance().getJobName();
      
      ColumnFamilyUpdater<Long, String> updater = jobExecutionTemplate
            .createUpdater(jobExecution.getId());

      updater.setLong(JE_JOB_INSTANCE_ID_COLUMN, jobInstanceId);
      updater.setString(JE_JOBNAME_COLUMN, jobName);
      updater.setByteArray(JE_CREATION_TIME_COLUMN, ds.toBytes(jobExecution
            .getCreateTime()));
      updater.setByteArray(JE_EXECUTION_CONTEXT_COLUMN, os.toBytes(jobExecution
            .getExecutionContext()));
      updater.setInteger(JE_VERSION_COLUMN, jobExecution.getVersion());
      updater.setByteArray(JE_START_TIME_COLUMN, ds.toBytes(jobExecution
            .getStartTime()));
      updater.setByteArray(JE_END_TIME_COLUMN, ds.toBytes(jobExecution
            .getEndTime()));
      updater.setString(JE_STATUS_COLUMN, jobExecution.getStatus().name());
      updater.setString(JE_EXIT_CODE_COLUMN, jobExecution.getExitStatus()
            .getExitCode());
      updater.setString(JE_EXIT_MESSAGE_COLUMN, jobExecution.getExitStatus()
            .getExitDescription());
      updater.setByteArray(JE_LAST_UPDATED_COLUMN, ds.toBytes(jobExecution
            .getLastUpdated()));

      // On écrit dans cassandra
      jobExecutionTemplate.update(updater);
      
      // Alimentation des différents index
      final byte[] EMPTY = new byte[0];
      Mutator<byte[]> mutator = HFactory.createMutator(keyspace, bs);
      mutator.addInsertion(ls.toBytes(jobInstanceId) , JOBINSTANCE_TO_JOBEXECUTION_CFNAME, 
            HFactory.createColumn(jobExecutionId, EMPTY, ls, bs));
      mutator.addInsertion(ss.toBytes(jobName) , JOBEXECUTIONS_CFNAME,
            HFactory.createColumn(jobExecutionId, EMPTY, ls, bs));
      mutator.addInsertion(ss.toBytes(ALL_JOBS_KEY) , JOBEXECUTIONS_CFNAME,
            HFactory.createColumn(jobExecutionId, EMPTY, ls, bs));

      if (jobExecution.isRunning()) {
         mutator.addInsertion(ss.toBytes(jobName) , JOBEXECUTIONS_RUNNING_CFNAME,
               HFactory.createColumn(jobExecutionId, EMPTY, ls, bs));
         mutator.addInsertion(ss.toBytes(ALL_JOBS_KEY) , JOBEXECUTIONS_RUNNING_CFNAME,
               HFactory.createColumn(jobExecutionId, EMPTY, ls, bs));
      }
      else {
         mutator.addDeletion(ss.toBytes(jobName) , JOBEXECUTIONS_RUNNING_CFNAME, jobExecutionId, ls);
         mutator.addDeletion(ss.toBytes(ALL_JOBS_KEY) , JOBEXECUTIONS_RUNNING_CFNAME, jobExecutionId, ls);
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
   private void validateJobExecution(JobExecution jobExecution) {

      Assert.notNull(jobExecution);
      Assert.notNull(jobExecution.getJobId(),
            "JobExecution Job-Id cannot be null.");
      Assert.notNull(jobExecution.getStatus(),
            "JobExecution status cannot be null.");
      Assert.notNull(jobExecution.getCreateTime(),
            "JobExecution create time cannot be null");
   }

   /** {@inheritDoc} */
   public void synchronizeStatus(JobExecution jobExecution) {
      // On lit le status et la version dans cassandra
      ColumnFamilyResult<Long, String> result = jobExecutionTemplate
            .queryColumns(jobExecution.getId());
      if (result == null || !result.hasResults()) return;
      String status = result.getString(JE_STATUS_COLUMN);
      jobExecution.setStatus(BatchStatus.valueOf(status));
      int version = result.getInteger(JE_VERSION_COLUMN);
      jobExecution.setVersion(version);
   }

   /** {@inheritDoc} */
   public void updateJobExecution(JobExecution jobExecution) {
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
   /** {@inheritDoc} */
   public int countJobExecutions() {
      return jobExecutionsTemplate.countColumns(ALL_JOBS_KEY);
   }

   @Override
   /** {@inheritDoc} */
   public int countJobExecutions(String jobName) {
      return jobExecutionsTemplate.countColumns(jobName);
   }

   @Override
   /** {@inheritDoc}
    *  
    * D'après l'implémentation JDBC, il faut lier les objets jobExecution à des objets
    * jobInstance contenant un id, un jobName, et des paramètres null
    * Sinon, ça fait planter spring-batch-admin.
    * */
   public List<JobExecution> getJobExecutions(int start, int count) {
      return getJobExecutions(ALL_JOBS_KEY, start, count);
   }


   @Override
   /** {@inheritDoc} */
   public List<JobExecution> getJobExecutions(String jobName, int start,
         int count) {
      // Récupération des id, par ordre décroissant
      HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(start + count);
      ColumnFamilyResult<String, Long> result = jobExecutionsTemplate.queryColumns(jobName, predicate);
      Collection<Long> ids = result.getColumnNames();
      // On ignore les start premiers ids
      List<Long> jobExecutionIds = new ArrayList<Long>(count);
      Iterator<Long> iterator = ids.iterator();
      for (int i = 0; i < ids.size(); i++) {
         Long id = iterator.next();
         if (i >= start) jobExecutionIds.add(id);
      }
      return getJobExecutionsFromIds(jobExecutionIds, null);
   }

   @Override
   /** {@inheritDoc} */
   public Collection<JobExecution> getRunningJobExecutions() {
      // Récupération des id, par ordre décroissant
      HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(500);
      ColumnFamilyResult<String, Long> result = jobExecutionsRunningTemplate.queryColumns(ALL_JOBS_KEY, predicate);
      Collection<Long> jobExecutionIds = result.getColumnNames();
      return getJobExecutionsFromIds(jobExecutionIds, null);
   }

}
