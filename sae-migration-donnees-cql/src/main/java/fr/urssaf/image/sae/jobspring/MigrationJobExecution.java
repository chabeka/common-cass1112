package fr.urssaf.image.sae.jobspring;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraJobInstanceDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobExecution;
import fr.urssaf.image.sae.trace.dao.IJobExecutionCqlForMigDao;
import fr.urssaf.image.sae.trace.model.JobExecutionCqlForMig;
import fr.urssaf.image.sae.utils.CompareUtils;
import fr.urssaf.image.sae.utils.RowUtils;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Serializer;

/**
 * Classe permettant de faire la migration de données de la table {@link JobExecution}
 * de thrift vers cql ou de cql vers thrift
 */
@Component
public class MigrationJobExecution extends MigrationJob implements IMigration {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobExecution.class);

  protected static final String JOBEXECUTION_CFNAME = "JobExecution";

  // Colonnes de JobExecution
  protected static final String JE_JOB_INSTANCE_ID_COLUMN = "jobInstanceId";

  protected static final String JE_JOBNAME_COLUMN = "jobName";

  protected static final String JE_CREATION_TIME_COLUMN = "creationTime";

  protected static final String JE_EXECUTION_CONTEXT_COLUMN = "executionContext";

  protected static final String JE_VERSION_COLUMN = "version";

  protected static final String JE_START_TIME_COLUMN = "startTime";

  protected static final String JE_END_TIME_COLUMN = "endTime";

  protected static final String JE_STATUS_COLUMN = "status";

  protected static final String JE_EXIT_CODE_COLUMN = "exitCode";

  protected static final String JE_EXIT_MESSAGE_COLUMN = "exitMessage";

  protected static final String JE_LAST_UPDATED_COLUMN = "lastUpdated";

  //

  @Autowired
  IJobExecutionDaoCql jobdaocql;

  @Autowired
  IJobExecutionCqlForMigDao jobdaocqlForMig;

  @Autowired
  CassandraJobInstanceDaoThrift jobInstanceDaoThrift;


  /**
   * Migration thrift vers cql
   */
  @Override
  public void migrationFromThriftToCql() {
    LOGGER.info(" MigrationJobExecution- migrationFromThriftToCql start");

    // itérateur nous permettant de parcourir toutes les lignes de la CF
    final Iterator<GenericJobExecution> it = genericJobExdao.findAllByCFName(JOBEXECUTION_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    Long lastKey = null;
    Long key = null;
    JobExecutionCqlForMig jobExecutionCql = new JobExecutionCqlForMig();

    int nbRow = 0;
    while (it.hasNext()) {
      final Row row = (Row) it.next();

      // extraction de la clé
      key = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (lastKey == null) {
        lastKey = key;
      }

      if (key != null && !key.equals(lastKey)) {
        // TODO supprimer log
        LOGGER.info("Infos:{}",
                    jobExecutionCql.getJobName() + "/" + jobExecutionCql.getJobExecutionId() + "/"
                        + jobExecutionCql.getExecutionContext().capacity());
        // sauvegarde de l'objet traité
        try {
          if (jobExecutionCql.getExecutionContext().capacity() < RowUtils.MAX_SIZE_COLUMN) {
            jobdaocqlForMig.saveWithMapper(jobExecutionCql);
          } else {
            LOGGER.warn("Contexte trop gros/Infos:{}",
                        jobExecutionCql.getJobName() + "/"
                            + jobExecutionCql.getJobExecutionId() + "/" + jobExecutionCql.getExecutionContext().capacity());

          }
        }
        catch (final Exception e) {
          LOGGER.error("Exception: ", e);
          LOGGER.error("Exception {}", jobExecutionCql.getJobName() + "/" + jobExecutionCql.getJobExecutionId());
          throw e;
        }

        // reinitialisation
        lastKey = key;
        jobExecutionCql = new JobExecutionCqlForMig();
        nbRow++;
        if (nbRow % 1000 == 0) {
          LOGGER.info(" Nb rows : " + nbRow);
        }
      } else {
        // construction de l'objet cql
        getJobExecutionFromResult(row, jobExecutionCql);
      }

    }
    // ajout du dernier cas traité
    if (jobExecutionCql != null) {
      try {
        jobdaocqlForMig.saveWithMapper(jobExecutionCql);
      }
      catch (final Exception e) {
        LOGGER.error("Exception", e);
        LOGGER.error("Exception", jobExecutionCql.getJobName() + "/" + jobExecutionCql.getJobExecutionId());
        throw e;
      }

      // jobdaocqlForMig.saveWithMapper(jobExecutionCql);
      nbRow++;
    }
    LOGGER.info("MigrationJobExecution- migrationFromThriftToCql end");
    LOGGER.info("MigrationJobExecution- migrationFromThriftToCql total :{}", nbRow);
  }

  /**
   * Migration cql vers Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {
    LOGGER.info(" MigrationJobExecution- migrationFromCqlTothrift start");
    final Iterator<JobExecutionCql> it = jobdaocql.findAllWithMapper();
    while (it.hasNext()) {
      final JobExecution jobexe = JobTranslateUtils.JobExecutionCqlToJobExecution(it.next(), null);
      saveJobExecutionToCassandra(jobexe);
    }
    final List<JobExecutionCql> listCql = new ArrayList<>();
    it.forEachRemaining(listCql::add);
    LOGGER.info("MigrationJobExecution- migrationFromCqlTothrift end");
    LOGGER.info("MigrationJobExecution- migrationFromCqlTothrift total :{}", listCql.size());
  }

  // ############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobExecution() {
    // recuperer un iterateur sur la table cql
    // Parcourir les elements et pour chaque element 
    // recuperer un ensemble de X elements dans la table thrift
    // chercher l'element cql dans les X elements
    // si trouvé, on passe à l'element suivant cql
    // sinon on recupère les X element suivant dans la table thrift puis on fait une nouvelle recherche
    // si on en recupère  moins de X et qu'on ne trouve pas l'element cql alors == > echec de comparaison

    // liste venant de la base thrift
    final List<JobExecutionCqlForMig> listJobCql = getListJobExeThrift();

    // liste venant de la base cql
    final List<JobExecutionCqlForMig> listJobThrift = new ArrayList<>();
    final Iterator<JobExecutionCql> it = jobdaocql.findAllWithMapper();
    int nbRow = 0;
    while (it.hasNext()) {
      final JobExecutionCql cqlJob = it.next();
      final JobExecutionCqlForMig jobExFMig = getJobExecutionJobExeCql(cqlJob);
      listJobThrift.add(jobExFMig);
      nbRow++;

    }

    final boolean isListEq = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (isListEq) {
      LOGGER.info("MIGRATION_JobExecution -- Les listes JobExecution sont identiques, nb=" + listJobThrift.size());
    } else {
      LOGGER.warn("MIGRATION_JobExecution -- ATTENTION: Les listes JobExecution sont différentes ");
    }

    return isListEq;
  }





  // ############################################################
  // ################# Methode utilitaire ######################
  // ############################################################

  private JobExecutionCqlForMig getJobExecutionJobExeCql(final JobExecutionCql cqlJob) {

    final JobExecutionCqlForMig jobExecutionCql = new JobExecutionCqlForMig();

    final Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
    // Creation des colonne        

    jobExecutionCql.setJobExecutionId(cqlJob.getJobExecutionId());
    jobExecutionCql.setJobInstanceId(cqlJob.getJobInstanceId());
    jobExecutionCql.setJobName(cqlJob.getJobName());    
    jobExecutionCql.setCreationTime(cqlJob.getCreationTime());      
    jobExecutionCql.setExecutionContext(oSlz.toByteBuffer(cqlJob.getExecutionContext()));
    jobExecutionCql.setVersion(cqlJob.getVersion());
    jobExecutionCql.setStartTime(cqlJob.getStartTime());
    jobExecutionCql.setEndTime(cqlJob.getEndTime());
    jobExecutionCql.setLastUpdated(cqlJob.getLastUpdated());
    jobExecutionCql.setStatus(cqlJob.getStatus());
    jobExecutionCql.setExitCode(cqlJob.getExitCode());
    jobExecutionCql.setExitMessage(cqlJob.getExitMessage());

    return jobExecutionCql;
  }

  private void getJobExecutionFromResult(final Row row, final JobExecutionCqlForMig jobExecutionCql) {

    // final JobExecutionCqlForMig jobExecutionCql = new JobExecutionCqlForMig();

    final String colName = StringSerializer.get().fromByteBuffer(row.getBytes("column1"));

    // Creation des colonne        
    final Long executionId = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
    jobExecutionCql.setJobExecutionId(executionId);

    if(JE_JOB_INSTANCE_ID_COLUMN.equals(colName)) {
      final Long jobInstanceId = LongSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setJobInstanceId(jobInstanceId);
    }

    if(JE_JOBNAME_COLUMN.equals(colName)) {
      final String jobName = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setJobName(jobName);
    }

    if(JE_CREATION_TIME_COLUMN.equals(colName)) {
      final Date createTime = NullableDateSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setCreationTime(createTime);
    }

    if(JE_EXECUTION_CONTEXT_COLUMN.equals(colName)) {
      final ByteBuffer executionContext = row.getBytes("value");
      jobExecutionCql.setExecutionContext(executionContext);
    }

    if(JE_VERSION_COLUMN.equals(colName)) {
      final int version = IntegerSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setVersion(version);
    }

    if(JE_START_TIME_COLUMN.equals(colName)) {
      final Date startDate = NullableDateSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setStartTime(startDate);
    }

    if(JE_END_TIME_COLUMN.equals(colName)) {
      final Date endDate = NullableDateSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setEndTime(endDate);
    }

    if(JE_LAST_UPDATED_COLUMN.equals(colName)) {
      final Date lastDate = NullableDateSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setLastUpdated(lastDate);
    }

    if(JE_STATUS_COLUMN.equals(colName)) {
      final String status = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setStatus(BatchStatus.valueOf(status));
    }

    if(JE_EXIT_CODE_COLUMN.equals(colName)) {
      final String exitCode = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setExitCode(exitCode);
    }

    if(JE_EXIT_MESSAGE_COLUMN.equals(colName)) {
      final String exitMessage = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      jobExecutionCql.setExitMessage(exitMessage);
    }
  }

  /**
   * Enregistre un jobExecution dans cassandra Le jobExecution doit avoir un id
   * affecté.
   *
   * @param jobExecution
   */
  private void saveJobExecutionToCassandra(final JobExecution jobExecution) {

    final ColumnFamilyTemplate<Long, String> jobExecutionTemplate = new ThriftColumnFamilyTemplate<>(
        ccfthrift.getKeyspace(),
        JOBEXECUTION_CFNAME,
        LongSerializer.get(),
        StringSerializer.get());

    final Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
    final NullableDateSerializer dSlz = NullableDateSerializer.get();
    final Long jobInstanceId = jobExecution.getJobId();
    final String jobName = jobExecution.getJobInstance().getJobName();

    final ColumnFamilyUpdater<Long, String> updater = jobExecutionTemplate.createUpdater(jobExecution.getId());

    updater.setLong(JE_JOB_INSTANCE_ID_COLUMN, jobInstanceId);
    updater.setString(JE_JOBNAME_COLUMN, jobName);
    updater.setByteArray(JE_CREATION_TIME_COLUMN, dSlz.toBytes(jobExecution.getCreateTime()));
    updater.setByteArray(JE_EXECUTION_CONTEXT_COLUMN, oSlz.toBytes(jobExecution.getExecutionContext()));
    updater.setInteger(JE_VERSION_COLUMN, jobExecution.getVersion());
    updater.setByteArray(JE_START_TIME_COLUMN, dSlz.toBytes(jobExecution.getStartTime()));
    updater.setByteArray(JE_END_TIME_COLUMN, dSlz.toBytes(jobExecution.getEndTime()));
    updater.setString(JE_STATUS_COLUMN, jobExecution.getStatus().name());
    updater.setString(JE_EXIT_CODE_COLUMN, jobExecution.getExitStatus().getExitCode());
    updater.setString(JE_EXIT_MESSAGE_COLUMN, jobExecution.getExitStatus().getExitDescription());
    updater.setByteArray(JE_LAST_UPDATED_COLUMN, dSlz.toBytes(jobExecution.getLastUpdated()));

    // On écrit dans cassandra
    jobExecutionTemplate.update(updater);

  }

  public List<JobExecutionCqlForMig> getListJobExeThrift() {

    final List<JobExecutionCqlForMig> listJob = new ArrayList<>();

    // itérateur nous permettant de parcourir toutes les lignes de la CF
    final Iterator<GenericJobExecution> it = genericJobExdao.findAllByCFName(JOBEXECUTION_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    Long lastKey = null;
    Long key = null;
    JobExecutionCqlForMig jobExecutionCql = new JobExecutionCqlForMig();

    while (it.hasNext()) {
      final Row row = (Row) it.next();

      // extraction de la clé
      key = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (lastKey == null) {
        lastKey = key;
      }

      // Pour chaque passage à une clé différente, on enregiste la dernière clé traitée
      if (key != null && !key.equals(lastKey)) {
        listJob.add(jobExecutionCql);

        // reinitialion
        lastKey = key;
        jobExecutionCql = new JobExecutionCqlForMig();
      } else {
        // construction de l'objet cql
        getJobExecutionFromResult(row, jobExecutionCql);
      }
    }

    // ajout du dernier cas traité
    if (jobExecutionCql != null) {
      listJob.add(jobExecutionCql);
    }

    return listJob;
  }

  /*
   * ATTENTION POUR TEST
   * A supprimer test executionContext
   */
  /*
   * public void testExecutionContext() {
   * final Map<String, JobParameter> mapJobParameters = new HashMap<>();
   * mapJobParameters.put("testp1", new JobParameter("test1"));
   * mapJobParameters.put("testp2", new JobParameter("test2"));
   * mapJobParameters.put("testp3", new JobParameter("test3"));
   * final JobParameters jobParameters = new JobParameters(mapJobParameters);
   * final JobInstance jobInstance = jobInstanceDaoThrift.createJobInstance("TestContexte70000", jobParameters);
   * final JobExecution jobExecution = new JobExecution(jobInstance, new Long(1));
   * final Map<String, Object> mapContext = new HashMap<>();
   * final StringBuilder sb = new StringBuilder();
   * Contextes trop gros:11453,11452,11451,11455,11454,11457,11450 (CSPP)
   * // 100000KO 50000OK 60000OK(13489049) 65000OK(14614049) 67500OK(15176549) 68750OK(15457799) 69375OK(15598424) 70000KO
   * for (int i = 0; i < 70000; i++) {
   * sb.append("Etre ou ne pas être, c'est là la question.Y a-t-il plus de noblesse d'âme à subir la fronde"
   * + " et les flèches de la fortune outrageante, ou bien à s'armer contre une mer de douleurs et à "
   * + "l'arrêter par une révolte ?" + i);
   * }
   * mapContext.put("contexteTest", sb.toString());
   * mapContext.put("index", 1);
   * final ExecutionContext executionContext = new ExecutionContext(mapContext);
   * jobExecution.setStartTime(Calendar.getInstance().getTime());
   * jobExecution.setEndTime(Calendar.getInstance().getTime());
   * jobExecution.setStatus(BatchStatus.ABANDONED);
   * jobExecution.setCreateTime(Calendar.getInstance().getTime());
   * jobExecution.setLastUpdated(Calendar.getInstance().getTime());
   * jobExecution.setVersion(1);
   * jobExecution.setExecutionContext(executionContext);
   * saveJobExecutionToCassandra(jobExecution);
   * }
   */
}
