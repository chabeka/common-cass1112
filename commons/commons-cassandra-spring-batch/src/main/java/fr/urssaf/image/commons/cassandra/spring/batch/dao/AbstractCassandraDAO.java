package fr.urssaf.image.commons.cassandra.spring.batch.dao;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;

/**
 * Classe mère des classes DAO utilisant Cassandra pour spring batch. On définit
 * l'ensemble des CF et des colonnes, et on instancie des templates Hector.
 * 
 * @author Samuel Carrière
 */
@SuppressWarnings({"PMD.LongVariable" })
public class AbstractCassandraDAO {
   protected static final String JOBINSTANCE_CFNAME = "JobInstance";
   protected static final String JOBINSTANCES_BY_NAME_CFNAME = "JobInstancesByName";
   protected static final String JOBINSTANCE_TO_JOBEXECUTION_CFNAME = "JobInstanceToJobExecution";
   protected static final String JOBEXECUTION_CFNAME = "JobExecution";
   protected static final String JOBEXECUTIONS_CFNAME = "JobExecutions";
   protected static final String JOBEXECUTIONS_RUNNING_CFNAME = "JobExecutionsRunning";
   protected static final String JOBEXECUTION_TO_JOBSTEP_CFNAME = "JobExecutionToJobStep";
   protected static final String JOBSTEP_CFNAME = "JobStep";
   protected static final String JOBSTEPS_CFNAME = "JobSteps";
   
   //CHECKSTYLE:OFF   on ne fait pas d'accesseur pour plus de lisibilité et de concision
   protected Keyspace keyspace;
   protected ColumnFamilyTemplate<Long, String> jobInstanceTemplate;
   protected ColumnFamilyTemplate<String, Long> jobInstancesByNameTemplate;
   protected ColumnFamilyTemplate<Long, Long> jobInstanceToJobExecutionTemplate;
   protected ColumnFamilyTemplate<Long, String> jobExecutionTemplate;
   protected ColumnFamilyTemplate<String, Long> jobExecutionsTemplate;
   protected ColumnFamilyTemplate<String, Long> jobExecutionsRunningTemplate;
   protected ColumnFamilyTemplate<Long, Long> jobExecutionToJobStepTemplate;
   protected ColumnFamilyTemplate<Long, String> jobStepTemplate;
   protected ColumnFamilyTemplate<String, Long> jobStepsTemplate;
   //CHECKSTYLE:ON

   // Colonnes de JobInstance
   protected static final String JI_NAME_COLUMN = "name";
   protected static final String JI_PARAMETERS_COLUMN = "parameters";
   protected static final String JI_JOB_KEY_COLUMN = "jobKey";
   protected static final String JI_VERSION = "version";
   protected static final String JI_RESERVED_BY = "reservedBy";

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

   // Clés constantes
   protected static final String ALL_JOBS_KEY = "_all";
   protected static final String JOB_STEPS_KEY = "jobSteps";
   protected static final String UNRESERVED_KEY = "_unreserved";
   
   // Nombre de colonnes retournées par défaut pour les CF d'indexation
   private static final int DEFAULT_MAX_COLS = 500;
   
   /**
    * Constructeur de la DAO
    * 
    * @param keyspace
    *           Keyspace cassandra à utiliser
    */
   public AbstractCassandraDAO(Keyspace keyspace) {
      this.keyspace = keyspace;

      jobInstanceTemplate = new ThriftColumnFamilyTemplate<Long, String>(
            keyspace, JOBINSTANCE_CFNAME, LongSerializer.get(), StringSerializer
                  .get());
      jobInstancesByNameTemplate = new ThriftColumnFamilyTemplate<String, Long>(
            keyspace, JOBINSTANCES_BY_NAME_CFNAME, StringSerializer.get(),
            LongSerializer.get());
      jobInstancesByNameTemplate.setCount(DEFAULT_MAX_COLS);
      jobInstanceToJobExecutionTemplate = new ThriftColumnFamilyTemplate<Long, Long>(
            keyspace, JOBINSTANCE_TO_JOBEXECUTION_CFNAME, LongSerializer.get(),
            LongSerializer.get());
      jobInstanceToJobExecutionTemplate.setCount(DEFAULT_MAX_COLS);
      
      jobExecutionTemplate = new ThriftColumnFamilyTemplate<Long, String>(
            keyspace, JOBEXECUTION_CFNAME, LongSerializer.get(),
            StringSerializer.get());
      jobExecutionsTemplate = new ThriftColumnFamilyTemplate<String, Long>(
            keyspace, JOBEXECUTIONS_CFNAME, StringSerializer.get(),
            LongSerializer.get());
      jobExecutionsTemplate.setCount(DEFAULT_MAX_COLS);
      jobExecutionsRunningTemplate = new ThriftColumnFamilyTemplate<String, Long>(
            keyspace, JOBEXECUTIONS_RUNNING_CFNAME, StringSerializer.get(),
            LongSerializer.get());
      jobExecutionsRunningTemplate.setCount(DEFAULT_MAX_COLS);
      jobExecutionToJobStepTemplate = new ThriftColumnFamilyTemplate<Long, Long>(
            keyspace, JOBEXECUTION_TO_JOBSTEP_CFNAME, LongSerializer.get(),
            LongSerializer.get());
      jobExecutionToJobStepTemplate.setCount(DEFAULT_MAX_COLS);
      
      jobStepTemplate = new ThriftColumnFamilyTemplate<Long, String>(keyspace,
            JOBSTEP_CFNAME, LongSerializer.get(), StringSerializer.get());
      jobStepsTemplate = new ThriftColumnFamilyTemplate<String, Long>(
            keyspace, JOBSTEPS_CFNAME, StringSerializer.get(),
            LongSerializer.get());
      jobStepsTemplate.setCount(DEFAULT_MAX_COLS);
   }

}
