package fr.urssaf.image.sae.jobspring;

import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Serializer;

/**
 * Classe permettant de faire la grigration de données de la table {@link JobStep}
 * de thrift vers cql ou de cql vers thrift
 */
@Component
public class MigrationJobStep extends MigrationJob implements IMigration {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationJobStep.class);

  //

  @Autowired
  IJobStepExecutionDaoCql jobdaocql;

  protected static final String JOBSTEP_CFNAME = "JobStep";

  //
  // Colonnes de JobStep
  protected static final String JS_JOB_EXECUTION_ID_COLUMN = "jobExecutionId";

  protected static final String JS_VERSION_COLUMN = "version";

  protected static final String JS_STEP_NAME_COLUMN = "name";

  protected static final String JS_START_TIME_COLUMN = "startTime";

  protected static final String JS_END_TIME_COLUMN = "endTime";

  protected static final String JS_STATUS_COLUMN = "status";

  protected static final String JS_COMMITCOUNT_COLUMN = "commitCount";

  protected static final String JS_READCOUNT_COLUMN = "readCount";

  protected static final String JS_FILTERCOUNT_COLUMN = "filterCount";

  protected static final String JS_WRITECOUNT_COLUMN = "writeCount";

  protected static final String JS_READSKIPCOUNT_COLUMN = "readSkipCount";

  protected static final String JS_WRITESKIPCOUNT_COLUMN = "writeSkipCount";

  protected static final String JS_PROCESSSKIPCOUNT_COLUMN = "processSkipCount";

  protected static final String JS_ROLLBACKCOUNT_COLUMN = "rollbackCount";

  protected static final String JS_EXIT_CODE_COLUMN = "exitCode";

  protected static final String JS_EXIT_MESSAGE_COLUMN = "exitMessage";

  protected static final String JS_LAST_UPDATED_COLUMN = "lastUpdated";

  protected static final String JS_EXECUTION_CONTEXT_COLUMN = "executionContext";

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromThriftToCql() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("MigrationJobStep - migrationFromThriftToCql - DEBUT");
    }

    final Serializer<String> sSlz = StringSerializer.get();
    final Serializer<Integer> iSlz = IntegerSerializer.get();
    final Serializer<Date> dSlz = NullableDateSerializer.get();
    final Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(JOBSTEP_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

    Long lastKey = null;

    // initialisation des colonnes
    String exitMessage = null;
    String exitCode = null;
    Long jobStepExecutionId = null;
    String stepName = null;
    StepExecution jobStep = null;
    while (it.hasNext()) {
      // Extraction de la clé
      final Row row = (Row) it.next();
      final Long key = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
      if(lastKey == null) {
        lastKey = key;
      }
      if (jobStep == null) {
        // le vrai nom sera fixé après
        jobStep = new StepExecution("aaa", null);
      }
      // compare avec la derniere clé qui a été extraite
      // Si different, cela veut dire qu'on passe sur des colonnes avec une nouvelle clé
      // alors on enrgistre celui qui vient d'être traité
      if (key != null && !key.equals(lastKey)) {
        jobStep.setExitStatus(new ExitStatus(exitCode, exitMessage));
        final JobStepCql stepcql = JobTranslateUtils.getStpeCqlFromStepExecution(jobStep);
        stepcql.setJobStepExecutionId(lastKey);
        stepcql.setJobExecutionId(jobStepExecutionId);
        stepcql.setName(stepName);
        jobdaocql.save(stepcql);

        lastKey = key;
        // réinitialisation
        jobStep = new StepExecution("aaa", null);
      }
      // extraction des colonnes
      final String columnName = StringSerializer.get().fromByteBuffer(row.getBytes("column1"));

      if (JS_STEP_NAME_COLUMN.equals(columnName)) {
        stepName = getValue(row, JS_STEP_NAME_COLUMN, sSlz);
      }
      if (JS_JOB_EXECUTION_ID_COLUMN.equals(columnName)) {
        jobStepExecutionId = LongSerializer.get().fromByteBuffer(row.getBytes("value"));
      }

      if (JS_VERSION_COLUMN.equals(columnName)) {
        jobStep.setVersion(getValue(row, JS_VERSION_COLUMN, iSlz));
      }
      if (JS_START_TIME_COLUMN.equals(columnName)) {
        jobStep.setStartTime(getValue(row, JS_START_TIME_COLUMN, dSlz));
      }
      if (JS_END_TIME_COLUMN.equals(columnName)) {
        jobStep.setEndTime(getValue(row, JS_END_TIME_COLUMN, dSlz));
      }
      if (JS_STATUS_COLUMN.equals(columnName)) {
        jobStep.setStatus(BatchStatus.valueOf(getValue(row, JS_STATUS_COLUMN, sSlz)));
      }
      if (JS_COMMITCOUNT_COLUMN.equals(columnName)) {
        jobStep.setCommitCount(getValue(row, JS_COMMITCOUNT_COLUMN, iSlz));
      }
      if (JS_READCOUNT_COLUMN.equals(columnName)) {
        jobStep.setReadCount(getValue(row, JS_READCOUNT_COLUMN, iSlz));
      }
      if (JS_FILTERCOUNT_COLUMN.equals(columnName)) {
        jobStep.setFilterCount(getValue(row, JS_FILTERCOUNT_COLUMN, iSlz));
      }
      if (JS_WRITECOUNT_COLUMN.equals(columnName)) {
        jobStep.setWriteCount(getValue(row, JS_WRITECOUNT_COLUMN, iSlz));
      }
      if (JS_READSKIPCOUNT_COLUMN.equals(columnName)) {
        jobStep.setReadSkipCount(getValue(row, JS_READSKIPCOUNT_COLUMN, iSlz));
      }
      if (JS_WRITESKIPCOUNT_COLUMN.equals(columnName)) {
        jobStep.setWriteSkipCount(getValue(row, JS_WRITESKIPCOUNT_COLUMN, iSlz));
      }
      if (JS_PROCESSSKIPCOUNT_COLUMN.equals(columnName)) {
        jobStep.setProcessSkipCount(getValue(row, JS_PROCESSSKIPCOUNT_COLUMN, iSlz));
      }
      if (JS_ROLLBACKCOUNT_COLUMN.equals(columnName)) {
        jobStep.setRollbackCount(getValue(row, JS_ROLLBACKCOUNT_COLUMN, iSlz));
      }
      if (JS_EXIT_CODE_COLUMN.equals(columnName)) {
        exitCode = getValue(row, JS_EXIT_CODE_COLUMN, sSlz);
        jobStep.setExitStatus(new ExitStatus(exitCode, exitMessage));
      }
      if (JS_EXIT_MESSAGE_COLUMN.equals(columnName)) {
        exitMessage = getValue(row, JS_EXIT_MESSAGE_COLUMN, sSlz);
      }
      if (JS_LAST_UPDATED_COLUMN.equals(columnName)) {
        jobStep.setLastUpdated(getValue(row, JS_LAST_UPDATED_COLUMN, dSlz));
      }
      if (JS_EXECUTION_CONTEXT_COLUMN.equals(columnName)) {
        final ExecutionContext executionContext = getValue(row, JS_EXECUTION_CONTEXT_COLUMN, oSlz);
        jobStep.setExecutionContext(executionContext);
      }

    }

    // traiter le dernier cas
    jobStep.setExitStatus(new ExitStatus(exitCode, exitMessage));
    final JobStepCql stepcql = JobTranslateUtils.getStpeCqlFromStepExecution(jobStep);
    stepcql.setJobStepExecutionId(lastKey);
    stepcql.setJobExecutionId(jobStepExecutionId);
    stepcql.setName(stepName);
    jobdaocql.save(stepcql);

    if (LOG.isDebugEnabled()) {
      LOG.debug("MigrationJobStep - migrationFromThriftToCql - FIN");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromCqlTothrift() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("MigrationJobStep - migrationFromCqlTothrift - DEBUT");
    }

    final Iterator<JobStepCql> it = jobdaocql.findAllWithMapper();
    while (it.hasNext()) {
      final JobStepCql stepCql = it.next();
      final StepExecution stepEx = JobTranslateUtils.getStepExecutionFromStpeCql(null, stepCql);
      saveStepExecutionToCassandra(stepEx, stepCql.getJobExecutionId());
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("MigrationJobStep - migrationFromCqlTothrift - DEBUT");
    }
  }

  // ########################################################################
  // ########################## Methodes utilitares ########################
  // ########################################################################
  /**
   * Méthode utilitaire pour récupérer la valeur d'une colonne
   *
   * @param <T>
   *           type de la valeur
   * @param row
   *           row de colonnes contenant la colonne à lire
   * @param colName
   *           nom de la colonne à lire
   * @param serializer
   *           sérialiseur à utiliser
   * @return
   */
  private <T> T getValue(final Row row, final String colName, final Serializer<T> serializer) {
    final byte[] bytes = row.getBytes("value").array();
    return serializer.fromBytes(bytes);
  }

  /**
   * Enregistre un step dans cassandra Le step doit avoir un id affecté.
   *
   * @param stepExecution
   *           : step à enregistrer
   */
  private void saveStepExecutionToCassandra(final StepExecution stepExecution, final Long executionId) {

    final ColumnFamilyTemplate<Long, String> jobStepTemplate = new ThriftColumnFamilyTemplate<>(ccfthrift.getKeyspace(),
        JOBSTEP_CFNAME,
        LongSerializer.get(),
        StringSerializer.get());
    final Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
    final Serializer<Date> dSlz = NullableDateSerializer.get();
    final ColumnFamilyUpdater<Long, String> updater = jobStepTemplate.createUpdater(stepExecution.getId());

    updater.setLong(JS_JOB_EXECUTION_ID_COLUMN, executionId);
    updater.setInteger(JS_VERSION_COLUMN, stepExecution.getVersion());
    updater.setString(JS_STEP_NAME_COLUMN, stepExecution.getStepName());
    updater.setByteArray(JS_START_TIME_COLUMN, dSlz.toBytes(stepExecution.getStartTime()));
    updater.setByteArray(JS_END_TIME_COLUMN, dSlz.toBytes(stepExecution.getEndTime()));
    updater.setString(JS_STATUS_COLUMN, stepExecution.getStatus().name());
    updater.setInteger(JS_COMMITCOUNT_COLUMN, stepExecution.getCommitCount());
    updater.setInteger(JS_READCOUNT_COLUMN, stepExecution.getReadCount());
    updater.setInteger(JS_FILTERCOUNT_COLUMN, stepExecution.getFilterCount());
    updater.setInteger(JS_WRITECOUNT_COLUMN, stepExecution.getWriteCount());
    updater.setInteger(JS_READSKIPCOUNT_COLUMN, stepExecution.getReadSkipCount());
    updater.setInteger(JS_WRITESKIPCOUNT_COLUMN, stepExecution.getWriteSkipCount());
    updater.setInteger(JS_PROCESSSKIPCOUNT_COLUMN, stepExecution.getProcessSkipCount());
    updater.setInteger(JS_ROLLBACKCOUNT_COLUMN, stepExecution.getRollbackCount());
    updater.setString(JS_EXIT_CODE_COLUMN, stepExecution.getExitStatus().getExitCode());
    updater.setString(JS_EXIT_MESSAGE_COLUMN, stepExecution.getExitStatus().getExitDescription());
    updater.setByteArray(JS_LAST_UPDATED_COLUMN, dSlz.toBytes(stepExecution.getLastUpdated()));
    updater.setByteArray(JS_EXECUTION_CONTEXT_COLUMN, oSlz.toBytes(stepExecution.getExecutionContext()));

    // On écrit dans cassandra
    jobStepTemplate.update(updater);

  }
}