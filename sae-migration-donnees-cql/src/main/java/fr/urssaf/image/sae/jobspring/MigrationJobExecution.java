package fr.urssaf.image.sae.jobspring;

import java.nio.ByteBuffer;
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
import fr.urssaf.image.sae.trace.dao.IJobExecutionCqlForMigDao;
import fr.urssaf.image.sae.trace.model.JobExecutionCqlForMig;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
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
   
   @Autowired
   IJobExecutionCqlForMigDao jobdaocqlForMig;

   /**
    * Migration thrift vers cql
    */
   @Override
   public void migrationFromThriftToCql() {

      LOGGER.debug(" migrationFromThriftToCql start");

      // itérateur nous permettant de parcourir toutes les lignes de la CF
      final Iterator<GenericJobExecution> it = genericJobExdao.findAllByCFName(JOBEXECUTION_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
      Long lastKey = null;
      JobExecutionCqlForMig jobExecutionCql = new JobExecutionCqlForMig();
      
      while (it.hasNext()) {
         final Row row = (Row) it.next();

         // extraction de la clé
         final Long key = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
         final String colName = StringSerializer.get().fromByteBuffer(row.getBytes("column1"));
         if(lastKey == null) {
        	 lastKey = key;
         }
         // Creation des colonne        
         final Long executionId = key;
         jobExecutionCql.setJobExecutionId(executionId);
         
         if(JE_JOB_INSTANCE_ID_COLUMN.equals(colName)) {
        	 Long jobInstanceId = LongSerializer.get().fromByteBuffer(row.getBytes("value"));
        	 jobExecutionCql.setJobInstanceId(jobInstanceId);
         }
         
         if(JE_JOBNAME_COLUMN.equals(colName)) {
        	 String jobName = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
        	 jobExecutionCql.setJobName(jobName);
         }
         
         if(JE_CREATION_TIME_COLUMN.equals(colName)) {
        	 Date createTime = NullableDateSerializer.get().fromByteBuffer(row.getBytes("value"));
        	 jobExecutionCql.setCreationTime(createTime);
         }
         
         if(JE_EXECUTION_CONTEXT_COLUMN.equals(colName)) {
        	 ByteBuffer executionContext = row.getBytes("value");
        	 jobExecutionCql.setExecutionContext(executionContext);
         }
         
         if(JE_VERSION_COLUMN.equals(colName)) {
        	 int version = IntegerSerializer.get().fromByteBuffer(row.getBytes("value"));
             jobExecutionCql.setVersion(version);
         }
         
         if(JE_START_TIME_COLUMN.equals(colName)) {
        	 Date startDate = NullableDateSerializer.get().fromByteBuffer(row.getBytes("value"));
        	 jobExecutionCql.setStartTime(startDate);
         }
         
         if(JE_END_TIME_COLUMN.equals(colName)) {
        	 Date endDate = NullableDateSerializer.get().fromByteBuffer(row.getBytes("value"));
        	 jobExecutionCql.setEndTime(endDate);
         }
         
         if(JE_LAST_UPDATED_COLUMN.equals(colName)) {
        	 Date lastDate = NullableDateSerializer.get().fromByteBuffer(row.getBytes("value"));
        	 jobExecutionCql.setLastUpdated(lastDate);
         }
         
         if(JE_STATUS_COLUMN.equals(colName)) {
        	 String status = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
             jobExecutionCql.setStatus(BatchStatus.valueOf(status));
         }
         
         if(JE_EXIT_CODE_COLUMN.equals(colName)) {
        	 String exitCode = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
             jobExecutionCql.setExitCode(exitCode);
         }

         if(JE_EXIT_MESSAGE_COLUMN.equals(colName)) {
        	 String exitMessage = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
             jobExecutionCql.setExitMessage(exitMessage);
         }
         
         if (key != null && !key.equals(lastKey)) {
        	 jobdaocqlForMig.save(jobExecutionCql);
            lastKey = key;
            jobExecutionCql = new JobExecutionCqlForMig();
         }
      }

      LOGGER.debug(" migrationFromThriftToCql end");
   }

   /**
    * Migration cql vers Thrift
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
