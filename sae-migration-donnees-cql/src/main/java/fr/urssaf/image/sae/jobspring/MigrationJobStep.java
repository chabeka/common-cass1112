package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.utils.CompareUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

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

  @Autowired
  CassandraClientFactory ccf;

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

    LOG.info("MigrationJobStep - migrationFromThriftToCql - DEBUT");

    final Serializer<String> stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final LongSerializer longSerializer = LongSerializer.get();

    final RangeSlicesQuery<Long, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccf.getKeyspace(),
                                longSerializer,
                                stringSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JOBSTEP_CFNAME);

    final int blockSize = 1000;
    Long startKey = null;
    int totalKey = 1;
    int count;

    // Map contenant key = (numero d'iteration)
    // value=(liste des UUID des objets de l'iteration)
    final Map<Integer, List<Long>> lastIterationMap = new HashMap<>();

    // Numero d'itération
    int iterationNB = 0;

    do {
      rangeSlicesQuery.setRange(null, null, false, blockSize);
      rangeSlicesQuery.setKeys(startKey, null);
      rangeSlicesQuery.setRowCount(blockSize);
      final QueryResult<OrderedRows<Long, String, byte[]>> result0 = rangeSlicesQuery.execute();

      final OrderedRows<Long, String, byte[]> orderedRows = result0.get();
      count = orderedRows.getCount();
      // On enlève 1, car sinon à chaque itération, la startKey serait
      // comptée deux fois.
      totalKey += count - 1;
      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<Long, String, byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      // Liste des ids de l'iteration n-1 (null si au debut)
      final List<Long> lastlistUUID = lastIterationMap.get(iterationNB - 1);

      // Liste des ids de l'iteration courante
      final List<Long> currentlistUUID = new ArrayList<>();

      for (final me.prettyprint.hector.api.beans.Row<Long, String, byte[]> row : orderedRows) {
        final StepExecution stepThrift = getStepExecutionFromRow(row.getColumnSlice());
        final long key = row.getKey();
        final JobStepCql stepcql = JobTranslateUtils.getStpeCqlFromStepExecution(stepThrift);
        final Long jobExecutionId = getValue(row.getColumnSlice(), JS_JOB_EXECUTION_ID_COLUMN, LongSerializer.get());
        stepcql.setJobExecutionId(jobExecutionId);
        stepcql.setJobStepExecutionId(key);

        currentlistUUID.add(key);
        // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
        if (lastlistUUID == null || !lastlistUUID.contains(key)) {
          jobdaocql.saveWithMapper(stepcql);
        }
      }

      // remettre à jour la map
      lastIterationMap.put(iterationNB, currentlistUUID);
      lastIterationMap.remove(iterationNB - 1);
      iterationNB++;

    } while (count == blockSize);

    LOG.debug(" Nb total de cle dans la CF: " + totalKey);
    LOG.info("MigrationJobStep - migrationFromThriftToCql - FIN");
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

  // ############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobStepCql() {

    // liste d'objet cql venant de la base thrift après transformation
    final List<JobStepCql> listJobThrift = getStepExecutionsFromThrift();

    // liste venant de la base cql
    final List<JobStepCql> listJobCql = new ArrayList<>();
    final Iterator<JobStepCql> it = jobdaocql.findAllWithMapper();
    while (it.hasNext()) {
      final JobStepCql jobExToJR = it.next();
      listJobCql.add(jobExToJR);
    }

    // comparaison de deux listes
    final boolean isEqList = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (isEqList) {
      LOG.info("MIGRATION_JobStepCql -- Les listes metadata sont identiques");
    } else {
      LOG.warn("MIGRATION_JobStepCql -- ATTENTION: Les listes metadata sont différentes ");
    }

    return isEqList;
  }

  // ########################################################################
  // ########################## Methodes utilitares ########################
  // ########################################################################

  public List<JobStepCql> getStepExecutionsFromThrift() {

    final List<JobStepCql> list = new ArrayList<>();

    final Serializer<String> stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final LongSerializer longSerializer = LongSerializer.get();

    final RangeSlicesQuery<Long, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccf.getKeyspace(),
                                longSerializer,
                                stringSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JOBSTEP_CFNAME);

    final int blockSize = 1000;
    Long startKey = null;
    int count;

    // Map contenant key = (numero d'iteration)
    // value=(liste des UUID des objets de l'iteration)
    final Map<Integer, List<Long>> lastIteartionMap = new HashMap<>();

    // Numero d'itération
    int iterationNB = 0;

    do {
      rangeSlicesQuery.setRange(null, null, false, blockSize);
      rangeSlicesQuery.setKeys(startKey, null);
      rangeSlicesQuery.setRowCount(blockSize);
      final QueryResult<OrderedRows<Long, String, byte[]>> result0 = rangeSlicesQuery.execute();

      final OrderedRows<Long, String, byte[]> orderedRows = result0.get();
      count = orderedRows.getCount();
      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<Long, String, byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      // Liste des ids de l'iteration n-1 (null si au debut)
      final List<Long> lastlistUUID = lastIteartionMap.get(iterationNB - 1);

      // Liste des ids de l'iteration courante
      final List<Long> currentlistUUID = new ArrayList<>();

      for (final me.prettyprint.hector.api.beans.Row<Long, String, byte[]> row : orderedRows) {
        final StepExecution stepThrift = getStepExecutionFromRow(row.getColumnSlice());
        final long key = row.getKey();
        final JobStepCql stepcql = JobTranslateUtils.getStpeCqlFromStepExecution(stepThrift);
        final Long jobExecutionId = getValue(row.getColumnSlice(), JS_JOB_EXECUTION_ID_COLUMN, LongSerializer.get());
        stepcql.setJobExecutionId(jobExecutionId);
        stepcql.setJobStepExecutionId(key);

        currentlistUUID.add(key);
        // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
        if (lastlistUUID == null || !lastlistUUID.contains(key)) {
          list.add(stepcql);
        }
      }

      // remettre à jour la map
      lastIteartionMap.put(iterationNB, currentlistUUID);
      lastIteartionMap.remove(iterationNB - 1);
      iterationNB++;

    } while (count == blockSize);

    return list;
  }
  /**
   * Méthode utilitaire pour récupérer la valeur d'une colonne
   * 
   * @param <T>
   *          type de la valeur
   * @param slice
   *          slice de colonnes contenant la colonne à lire
   * @param colName
   *          nom de la colonne à lire
   * @param serializer
   *          sérialiseur à utiliser
   * @return
   */
  private <T> T getValue(final ColumnSlice<String, byte[]> slice, final String colName, final Serializer<T> serializer) {
    final byte[] bytes = slice.getColumnByName(colName).getValue();
    return serializer.fromBytes(bytes);
  }

  /**
   * Enregistre un step dans cassandra Le step doit avoir un id affecté.
   *
   * @param stepExecution
   *          : step à enregistrer
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

  /**
   * Crée un objet StepExecution à partir d'une ligne lue de cassandra
   * 
   * @param jobExecution
   *          : le jobExecution référencé par le step à créer (éventuellement null)
   * @param result
   *          : données cassandra
   * @return
   */
  private StepExecution getStepExecutionFromRow(final ColumnSlice<String, byte[]> slice) {

    final Serializer<String> sSlz = StringSerializer.get();
    final Serializer<Integer> iSlz = IntegerSerializer.get();
    final Serializer<Date> dSlz = NullableDateSerializer.get();
    final Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();

    if (slice.getColumns().size() == 0) {
      return null;
    }
    final String stepName = getValue(slice, JS_STEP_NAME_COLUMN, sSlz);
    final StepExecution step = new StepExecution(stepName, null);

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
    final String exitCode = getValue(slice, JS_EXIT_CODE_COLUMN, sSlz);
    final String exitMessage = getValue(slice, JS_EXIT_MESSAGE_COLUMN, sSlz);
    step.setExitStatus(new ExitStatus(exitCode, exitMessage));
    step.setLastUpdated(getValue(slice, JS_LAST_UPDATED_COLUMN, dSlz));
    final ExecutionContext executionContext = getValue(slice, JS_EXECUTION_CONTEXT_COLUMN, oSlz);
    step.setExecutionContext(executionContext);
    return step;

  }


}