package fr.urssaf.image.sae.jobspring;

import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daothrift.CassandraJobExecutionDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobExecution;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Serializer;

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

   /**
    * {@inheritDoc}
    */
   @Override
   public void migrationFromThriftToCql() {

      LOGGER.debug(" migrationFromThriftToCql start");

      // requete pour l'extraction des donnée dans les tables thrift
      final ColumnFamilyTemplate<Long, String> jobExecutionTemplate = new ThriftColumnFamilyTemplate<Long, String>(
                                                                                                                   ccfthrift.getKeyspace(),
                                                                                                                   JOBEXECUTION_CFNAME,
                                                                                                                   LongSerializer.get(),
                                                                                                                   StringSerializer.get());

      // itérateur nous permettant de parcourir toutes les lignes de la CF
      final Iterator<GenericJobExecution> it = genericJobExdao.findAllByCFName(JOBEXECUTION_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
      Long lastKey = null;

      while (it.hasNext()) {
         final Row row = (Row) it.next();

         // extraction de la clé
         final Long key = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
         System.out.println(key);
         if (key != null && !key.equals(lastKey)) {
            // recuperer les colonnes du JobExecution à partir de la clé
            final ColumnFamilyResult<Long, String> result = jobExecutionTemplate.queryColumns(key);

            // mapping de colonne extraite en un objet JobExecutionCql et sauvegarde
            final JobExecutionCql jobcql = getJobExecutionCqlFromResult(result);
            jobdaocql.save(jobcql);
            lastKey = key;
         }
      }

      LOGGER.debug(" migrationFromThriftToCql end");
   }

   public void migrationFromThriftToCql(final CassandraJobExecutionDaoThrift jobExecutionDao) {

      LOGGER.debug(" migrationFromThriftToCql start");

      // itérateur nous permettant de parcourir toutes les lignes de la CF
      final Iterator<GenericJobExecution> it = genericJobExdao.findAllByCFName(JOBEXECUTION_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
      Long lastKey = null;

      while (it.hasNext()) {
         final Row row = (Row) it.next();

         // extraction de la clé
         final Long key = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
         System.out.println(key);

         if (key != null && !key.equals(lastKey)) {
            final JobExecution jobEx = jobExecutionDao.getJobExecution(key);
            final JobExecutionCql jobExCql = JobTranslateUtils.JobExecutionToJobExecutionCql(jobEx);
            jobdaocql.save(jobExCql);
            lastKey = key;
         }
      }

      LOGGER.debug(" migrationFromThriftToCql end");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void migrationFromCqlTothrift() {

      final Iterator<JobExecutionCql> it = jobdaocql.findAllWithMapper();
      while (it.hasNext()) {
         final JobExecution jobexe = JobTranslateUtils.JobExecutionCqlToJobExecution(it.next(), null);
         saveJobExecutionToCassandra(jobexe);
      }

   }

   // ############################################################
   // ################# Methode utilitaire ######################
   // ############################################################
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
   private JobExecutionCql getJobExecutionCqlFromResult(final ColumnFamilyResult<Long, String> result) {
      if (result == null || !result.hasResults()) {
         return null;
      }

      final Serializer<ExecutionContext> oSlz = ExecutionContextSerializer.get();
      final Serializer<Date> dSlz = NullableDateSerializer.get();

      final Long executionId = result.getKey();
      final JobExecution jobExecution = new JobExecution(executionId);

      final Long jobInstanceId = result.getLong(JE_JOB_INSTANCE_ID_COLUMN);
      final String jobName = result.getString(JE_JOBNAME_COLUMN);
      final Date createTime = dSlz.fromBytes(result.getByteArray(JE_CREATION_TIME_COLUMN));
      jobExecution.setCreateTime(createTime);
      if (executionId == 463) {
         System.out.println("");
      }
      final ExecutionContext executionContext = oSlz.fromBytes(result.getByteArray(JE_EXECUTION_CONTEXT_COLUMN));
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
      jobExecution.setJobInstance(new JobInstance(jobInstanceId, null, jobName));
      final JobExecutionCql jobcql = JobTranslateUtils.JobExecutionToJobExecutionCql(jobExecution);
      return jobcql;
   }

   /**
    * Enregistre un jobExecution dans cassandra Le jobExecution doit avoir un id
    * affecté.
    *
    * @param jobExecution
    */
   private void saveJobExecutionToCassandra(final JobExecution jobExecution) {

      final ColumnFamilyTemplate<Long, String> jobExecutionTemplate = new ThriftColumnFamilyTemplate<Long, String>(
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
}
