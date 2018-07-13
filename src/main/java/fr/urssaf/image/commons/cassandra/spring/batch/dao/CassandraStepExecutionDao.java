package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.prettyprint.cassandra.model.HSlicePredicate;
import me.prettyprint.cassandra.serializers.*;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.*;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.*;

import org.springframework.batch.admin.service.SearchableStepExecutionDao;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.helper.CassandraJobHelper;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.IdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;

/**
 * Classe implémentant StepExecutionDao, qui utilise cassandra. L'implémentation
 * est inspirée de
 * org.springframework.batch.core.repository.dao.JdbcStepExecutionDao
 * 
 * @see org.springframework.batch.core.repository.dao.JdbcStepExecutionDao
 * @see org.springframework.batch.admin.service.JdbcSearchableStepExecutionDao
 * @author Samuel Carrière
 */
public class CassandraStepExecutionDao extends AbstractCassandraDAO implements
      SearchableStepExecutionDao {

   private final IdGenerator idGenerator;
   private static final int MAX_COLS = 500;

   /**
    * Constructeur
    * @param keyspace : keyspace cassandra 
    * @param idGenerator : générateur d'id pour les stepExecution
    */
   public CassandraStepExecutionDao(Keyspace keyspace, IdGenerator idGenerator) {
      super(keyspace);
      this.idGenerator = idGenerator;
   }

   @Override
   public final void addStepExecutions(JobExecution jobExecution) {
      Assert.notNull(jobExecution, "JobExecution cannot be null.");
      Assert.notNull(jobExecution.getId(), "JobExecution Id cannot be null.");
      long jobExecutionId = jobExecution.getId();

      // Récupération des id des steps
      HSlicePredicate<Long> predicate = new HSlicePredicate<Long>(LongSerializer.get());
      predicate.setReversed(true);
      predicate.setCount(MAX_COLS);
      ColumnFamilyResult<Long, Long> result = jobExecutionToJobStepTemplate.queryColumns(jobExecutionId, predicate);
      Collection<Long> stepIds = result.getColumnNames();

      // Chargement des steps à partir des id trouvés
      getStepExecutionsFromIds(jobExecution, stepIds);
   }

   private Collection<StepExecution> getStepExecutionsFromIds(JobExecution jobExecution, Collection<Long> stepIds) {

      // On fait un multi-get pour récupérer toutes les lignes d'un coup
      MultigetSliceQuery<Long, String, byte[]> query = HFactory.createMultigetSliceQuery(keyspace, LongSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      query.setKeys(stepIds);
      query.setColumnFamily(JOBSTEP_CFNAME);
      query.setRange(null, null, false, MAX_COLS);
      QueryResult<Rows<Long, String, byte[]>> result = query.execute();
      Rows<Long, String, byte[]> rows = result.get();
      
      // On parcours les lignes dans l'ordre des stepIds
      List<StepExecution> list = new ArrayList<StepExecution>(stepIds.size());
      for (Long stepId : stepIds) {
         Row<Long, String, byte[]> row = rows.getByKey(stepId);
         list.add(getStepExecutionFromRow(jobExecution, stepId, row.getColumnSlice()));
      }
      return list;
   }

   /**
    * Méthode utilitaire pour récupérer la valeur d'une colonne
    * @param <T>           type de la valeur
    * @param slice         slice de colonnes contenant la colonne à lire
    * @param colName       nom de la colonne à lire
    * @param serializer    sérialiseur à utiliser    
    * @return
    */
   private <T> T getValue(ColumnSlice<String, byte[]> slice, String colName, Serializer<T> serializer) {
      byte[] bytes = slice.getColumnByName(colName).getValue();
      return serializer.fromBytes(bytes);
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
   private StepExecution getStepExecutionFromRow(JobExecution jobExecution, Long stepId,
         ColumnSlice<String, byte[]> slice) {
      
      Serializer<String> sSlz = StringSerializer.get();
      Serializer<Integer> iSlz = IntegerSerializer.get();
      Serializer<Date> dSlz = NullableDateSerializer.get();
      Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();

      if (slice.getColumns().size() == 0) {
         return null;
      }
      String stepName = getValue(slice, JS_STEP_NAME_COLUMN, sSlz);
      StepExecution step;
      if (jobExecution == null) {
         step = new StepExecution(stepName, null);
      }
      else {
         step = new StepExecution(stepName, jobExecution, stepId);         
      }
      step.setVersion(getValue(slice, JS_VERSION_COLUMN, iSlz));
      step.setStartTime(getValue(slice, JS_START_TIME_COLUMN, dSlz));
      step.setEndTime(getValue(slice, JS_END_TIME_COLUMN, dSlz));
      step.setStatus(BatchStatus.valueOf(getValue(slice, JS_STATUS_COLUMN, sSlz)));
      step.setCommitCount(getValue(slice, JS_COMMITCOUNT_COLUMN, iSlz));
      step.setReadCount(getValue(slice, JS_READCOUNT_COLUMN, iSlz));
      step.setFilterCount(getValue(slice, JS_FILTERCOUNT_COLUMN, iSlz));
      step.setWriteCount(getValue(slice, JS_WRITECOUNT_COLUMN, iSlz));
      step.setReadSkipCount(getValue(slice, JS_READSKIPCOUNT_COLUMN, iSlz));
      step.setWriteSkipCount(getValue(slice, JS_WRITESKIPCOUNT_COLUMN, iSlz));
      step.setProcessSkipCount(getValue(slice, JS_PROCESSSKIPCOUNT_COLUMN, iSlz));
      step.setRollbackCount(getValue(slice, JS_ROLLBACKCOUNT_COLUMN, iSlz));
      String exitCode = getValue(slice, JS_EXIT_CODE_COLUMN, sSlz);
      String exitMessage = getValue(slice, JS_EXIT_MESSAGE_COLUMN, sSlz);
      step.setExitStatus(new ExitStatus(exitCode, exitMessage));
      step.setLastUpdated(getValue(slice, JS_LAST_UPDATED_COLUMN, dSlz));
      ExecutionContext executionContext = getValue(slice, JS_EXECUTION_CONTEXT_COLUMN, oSlz);
      step.setExecutionContext(executionContext);
      return step;
      
   }

   @Override
   public final StepExecution getStepExecution(JobExecution jobExecution,
         Long stepExecutionId) {

      SliceQuery<Long, String, byte[]> query = HFactory.createSliceQuery(keyspace, LongSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      query.setKey(stepExecutionId);
      query.setColumnFamily(JOBSTEP_CFNAME);
      query.setRange(null, null, false, MAX_COLS);
      QueryResult<ColumnSlice<String,byte[]>> result = query.execute();
      ColumnSlice<String, byte[]> slice = result.get();
      if (slice == null) {
         return null;
      }
      return getStepExecutionFromRow(jobExecution, stepExecutionId, slice);
   }

   @Override
   public final void saveStepExecution(StepExecution stepExecution) {
      Assert
            .isNull(stepExecution.getId(),
                  "to-be-saved (not updated) StepExecution can't already have an id assigned");
      Assert
            .isNull(stepExecution.getVersion(),
                  "to-be-saved (not updated) StepExecution can't already have a version assigned");
      validateStepExecution(stepExecution);

      stepExecution.incrementVersion();

      long stepId = idGenerator.getNextId();
      stepExecution.setId(stepId);

      saveStepExecutionToCassandra(stepExecution);
   }

   /**
    * Enregistre un step dans cassandra Le step doit avoir un id affecté.
    * 
    * @param stepExecution
    *           : step à enregistrer
    */
   private void saveStepExecutionToCassandra(StepExecution stepExecution) {
      Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
      Serializer<Date> dSlz = NullableDateSerializer.get();
      Serializer<Long> lSlz = LongSerializer.get();
      Serializer<String> sSlz = StringSerializer.get();
      Serializer<byte[]> bSlz = BytesArraySerializer.get();
      ColumnFamilyUpdater<Long, String> updater = jobStepTemplate
            .createUpdater(stepExecution.getId());

      updater.setLong(JS_JOB_EXECUTION_ID_COLUMN, stepExecution
            .getJobExecutionId());
      updater.setInteger(JS_VERSION_COLUMN, stepExecution.getVersion());
      updater.setString(JS_STEP_NAME_COLUMN, stepExecution.getStepName());
      updater.setByteArray(JS_START_TIME_COLUMN, dSlz.toBytes(stepExecution
            .getStartTime()));
      updater.setByteArray(JS_END_TIME_COLUMN, dSlz.toBytes(stepExecution
            .getEndTime()));
      updater.setString(JS_STATUS_COLUMN, stepExecution.getStatus().name());
      updater.setInteger(JS_COMMITCOUNT_COLUMN, stepExecution.getCommitCount());
      updater.setInteger(JS_READCOUNT_COLUMN, stepExecution.getReadCount());
      updater.setInteger(JS_FILTERCOUNT_COLUMN, stepExecution.getFilterCount());
      updater.setInteger(JS_WRITECOUNT_COLUMN, stepExecution.getWriteCount());
      updater.setInteger(JS_READSKIPCOUNT_COLUMN, stepExecution
            .getReadSkipCount());
      updater.setInteger(JS_WRITESKIPCOUNT_COLUMN, stepExecution
            .getWriteSkipCount());
      updater.setInteger(JS_PROCESSSKIPCOUNT_COLUMN, stepExecution
            .getProcessSkipCount());
      updater.setInteger(JS_ROLLBACKCOUNT_COLUMN, stepExecution
            .getRollbackCount());
      updater.setString(JS_EXIT_CODE_COLUMN, stepExecution.getExitStatus()
            .getExitCode());
      updater.setString(JS_EXIT_MESSAGE_COLUMN, stepExecution.getExitStatus()
            .getExitDescription());
      updater.setByteArray(JS_LAST_UPDATED_COLUMN, dSlz.toBytes(stepExecution
            .getLastUpdated()));
      updater.setByteArray(JS_EXECUTION_CONTEXT_COLUMN, oSlz
            .toBytes(stepExecution.getExecutionContext()));

      // On écrit dans cassandra
      jobExecutionTemplate.update(updater);
      
      // Alimentation des différents index
      Long stepId = stepExecution.getId();
      Long jobExecutionId = stepExecution.getJobExecutionId();
      String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
      final byte[] empty = new byte[0];
      Mutator<byte[]> mutator = HFactory.createMutator(keyspace, bSlz);

      // Dans JobExecutionToJobStep 
      //    clé = jobExecutionId
      //    Nom de colonne = jobStepId
      //    Valeur = vide
      mutator.addInsertion(lSlz.toBytes(jobExecutionId) , JOBEXECUTION_TO_JOBSTEP_CFNAME, 
            HFactory.createColumn(stepId, empty, lSlz, bSlz));
      
      // Dans JobSteps 
      //    clé = "jobSteps"
      //    Nom de colonne = stepId
      //    Valeur = composite(jobName, stepName)
      Composite value = new Composite();
      value.addComponent(jobName, sSlz);
      value.addComponent(stepExecution.getStepName(), sSlz);
      mutator.addInsertion(sSlz.toBytes(JOB_STEPS_KEY) , JOBSTEPS_CFNAME, 
            HFactory.createColumn(stepId, value, lSlz, CompositeSerializer.get()));

      mutator.execute();
      
   }

   /**
    * Validate StepExecution. At a minimum, JobId, StartTime, and Status cannot
    * be null. EndTime can be null for an unfinished job.
    * 
    * @param value
    * @throws IllegalArgumentException
    */
   private void validateStepExecution(StepExecution stepExecution) {
      Assert.notNull(stepExecution);
      Assert.notNull(stepExecution.getStepName(),
            "StepExecution step name cannot be null.");
      Assert.notNull(stepExecution.getStartTime(),
            "StepExecution start time cannot be null.");
      Assert.notNull(stepExecution.getStatus(),
            "StepExecution status cannot be null.");
   }

   @Override
   public final void updateStepExecution(StepExecution stepExecution) {
      // Le nom de la méthode n'est pas super explicite, mais is s'agit
      // d'enregister le stepExecution
      // en base de données.

      validateStepExecution(stepExecution);
      Assert.notNull(stepExecution.getId(),
            "StepExecution Id cannot be null. StepExecution must saved"
                  + " before it can be updated.");
      stepExecution.incrementVersion();
      saveStepExecutionToCassandra(stepExecution);
   }

   /**
    * Supprime un step de la base de données
    * 
    * @param stepExecutionId
    *           : id du step à supprimer
    */
   public final void deleteStepExecution(Long stepExecutionId) {
      jobStepTemplate.deleteRow(stepExecutionId);
      jobStepsTemplate.deleteColumn(JOB_STEPS_KEY, stepExecutionId);
   }

   /**
    * Supprime tous les steps d'un jobExecution donné
    * 
    * @param jobExecution
    *           : jobExecution concerné
    */
   public final void deleteStepsOfExecution(JobExecution jobExecution) {
      Collection<StepExecution> steps = jobExecution.getStepExecutions();
      for (StepExecution stepExecution : steps) {
         deleteStepExecution(stepExecution.getId());
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public final int countStepExecutions(String jobNamePattern, String stepNamePattern) {
      ColumnFamilyResult<String, Long> result = jobStepsTemplate.queryColumns(JOB_STEPS_KEY);
      CompositeSerializer cSlz = CompositeSerializer.get();
      Serializer<String> sSlz = StringSerializer.get();
      
      Collection<Long> colNames = result.getColumnNames();
      int compteur = 0;
      for (Long colName : colNames) {
         byte[] value = result.getByteArray(colName);
         Composite composite = cSlz.fromBytes(value);
         String jobName = (String) composite.getComponent(0).getValue(sSlz);
         String stepName = (String) composite.getComponent(1).getValue(sSlz);
         if (CassandraJobHelper.checkPattern(jobNamePattern, jobName) 
               && CassandraJobHelper.checkPattern(stepNamePattern, stepName)) {
            compteur ++;
         }
      }
      return compteur;
   }

   @SuppressWarnings("unchecked")
   @Override
   public final Collection<StepExecution> findStepExecutions(String jobNamePattern,
         String stepNamePattern, int start, int count) {
      
      CompositeSerializer cSlz = CompositeSerializer.get();
      Serializer<String> sSlz = StringSerializer.get();
      Serializer<Long> lSlz = LongSerializer.get();
      
      SliceQuery<String, Long, Composite> sliceQuery = HFactory.createSliceQuery(
            keyspace, sSlz, lSlz, cSlz);
      sliceQuery.setKey(JOB_STEPS_KEY);
      sliceQuery.setColumnFamily(JOBSTEPS_CFNAME);
      
      ColumnSliceIterator<String, Long, Composite> iterator = new ColumnSliceIterator<String, Long, Composite>(
            sliceQuery, Long.MAX_VALUE, Long.MIN_VALUE, true);

      int compteur = 0;
      List<Long> stepIds = new ArrayList<Long>(count);
      while (iterator.hasNext()) {
         HColumn<Long, Composite> col = iterator.next();
         Long stepId = col.getName();
         Composite composite = col.getValue();
         String jobName = (String) composite.getComponent(0).getValue(sSlz);
         String stepName = (String) composite.getComponent(1).getValue(sSlz);
         if (CassandraJobHelper.checkPattern(jobNamePattern, jobName) && 
               CassandraJobHelper.checkPattern(stepNamePattern, stepName)) {
            compteur ++;
            if (compteur >= start) {
               stepIds.add(stepId);
            }
            if (compteur == count + start) {
               break;
            }
         }
      }
      return getStepExecutionsFromIds(null, stepIds);
   }

   @SuppressWarnings("unchecked")
   @Override
   public final Collection<String> findStepNamesForJobExecution(String jobName,
         String excludesPattern) {
      CompositeSerializer cSlz = CompositeSerializer.get();
      Serializer<String> sSlz = StringSerializer.get();

      ColumnFamilyResult<String, Long> result = jobStepsTemplate
            .queryColumns(JOB_STEPS_KEY);

      Collection<Long> colNames = result.getColumnNames();
      Set<String> stepNames = new HashSet<String>();
      for (Long colName : colNames) {
         byte[] value = result.getByteArray(colName);
         Composite composite = cSlz.fromBytes(value);
         String currentJobName = (String) composite.getComponent(0).getValue(sSlz);
         String currentStepName = (String) composite.getComponent(1).getValue(sSlz);
         if (currentJobName.equals(jobName)
               && !CassandraJobHelper.checkPattern(excludesPattern, currentStepName)) {
            stepNames.add(currentStepName);
         }
      }
      return stepNames;
   }

}
