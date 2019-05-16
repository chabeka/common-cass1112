package fr.urssaf.javaDriverTest.cleanjob;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.db.Keyspace;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * Permet de purger les CF relatives aux executions de jobs du keyspace dfce
 */
public class JobCleaner {

   final ObjectMapper jsonMapper;

   public JobCleaner() {
      jsonMapper = jsonCqlMapper();
   }

   public void purgeOldJobs(final CqlSession session, final int daysToKeep) throws Exception {
      // Calcul de la date limite
      final Instant thresholdDate = Instant.now().minusSeconds(24 * 3600 * daysToKeep);

      final Map<Long, JobInstance> jobInstances = getJobInstances(session);
      for (final Long jobInstanceId : jobInstances.keySet()) {
         final JobInstance jobInstance = jobInstances.get(jobInstanceId);
         if (jobInstance.maxCreationTime.isBefore(thresholdDate)) {
            final int jobExecutionsCount = jobInstance.jobExecutionIds.size();
            System.out.println("delete " + jobInstanceId + " - " + jobExecutionsCount + " - " + jobInstance.maxCreationTime);
            deleteOneJobInstance(session, jobInstanceId);
         } else {
            final int jobExecutionsCount = jobInstance.jobExecutionIds.size();
            System.out.println("keep " + jobInstanceId + " - " + jobExecutionsCount + " - " + jobInstance.maxCreationTime);
         }
      }
   }

   /**
    * Parcours la CF JobExecutions, et renvoie un dictionnaire JobInstanceId => JobInstance
    * JobInstance contenant la liste des jobExecution et la date de la plus récente exécution
    * 
    * @param keyspace
    * @return
    * @throws Exception
    */
   private Map<Long, JobInstance> getJobInstances(final CqlSession session) throws Exception {
      final Map<Long, JobInstance> jobInstances = new HashMap<>();

      final ResultSet rs = session.execute("select * from dfce.job_execution_by_id");
      for (final Row row : rs) {

         // long jobExecutionId = row.getKey();
         final long jobInstanceId = row.getLong("job_instance_id");
         final long jobExecutionId = row.getLong("id");
         final Instant creationTime = row.getInstant("create_time");
         if (jobInstanceId == 0) {
            continue;
         }
         // System.out.println(jobInstanceId + " - " + jobExecutionId + " - " + createTime);
         if (!jobInstances.containsKey(jobInstanceId)) {
            final ArrayList<Long> jobExecutionIds = new ArrayList<>();
            jobExecutionIds.add(jobExecutionId);
            jobInstances.put(jobInstanceId, new JobInstance(jobExecutionIds, creationTime));
         } else {
            final JobInstance jobInstance = jobInstances.get(jobInstanceId);
            final ArrayList<Long> jobExecutionIds = jobInstance.jobExecutionIds;
            jobExecutionIds.add(jobExecutionId);
            if (creationTime.isAfter(jobInstance.maxCreationTime)) {
               jobInstance.maxCreationTime = creationTime;
            }
         }
      }
      return jobInstances;
   }

   public void deleteOneJobInstance(final CqlSession session, final long jobInstanceId) throws Exception {
      final BatchStatementBuilder batchBuilder = BatchStatement.builder(DefaultBatchType.LOGGED);

      try {
         final JobInstanceInfo jobInstanceInfo = getJobInstanceInfo(session, jobInstanceId);
         System.out.println("Suppression jobInstance " + jobInstanceId);
         batchBuilder.addStatement(SimpleStatement.newInstance("delete from dfce.job_instance_by_id where id=?", jobInstanceId));
         batchBuilder.addStatement(SimpleStatement.newInstance("delete from dfce.job_instance_by_name_and_id where job_name=? and id=?",
                                                               jobInstanceInfo.jobName,
                                                               jobInstanceId));
         batchBuilder.addStatement(SimpleStatement.newInstance("delete from dfce.job_instance_by_name_and_parameters where job_name = ? and job_parameters_key = ?",
                                                               jobInstanceInfo.jobName,
                                                               jobInstanceInfo.jobKey));
      }
      catch (final Exception e) {
         System.out.println("Clé non trouvée dans jobInstance");
      }

      final List<Long> jobExecuctions = getJobExecutions(session, jobInstanceId);
      for (final Long jobExecutionId : jobExecuctions) {
         System.out.println("Suppression jobExecution " + jobExecutionId);
         batchBuilder.addStatement(SimpleStatement.newInstance("delete from dfce.job_execution_by_id where id=?", jobExecutionId));
         batchBuilder.addStatement(SimpleStatement.newInstance("delete from dfce.job_execution_by_instance where job_instance_id=? and id=?",
                                                               jobInstanceId,
                                                               jobExecutionId));

         final List<Long> stepExecutions = getStepExecutions(session, jobExecutionId);
         for (final Long stepExecutionId : stepExecutions) {
            System.out.println("Suppression step " + stepExecutionId);
            batchBuilder.addStatement(SimpleStatement.newInstance("delete from dfce.step_execution_by_id where id=?", jobExecutionId));
            batchBuilder.addStatement(SimpleStatement.newInstance("delete from dfce.step_execution_by_job_execution where job_execution_id=? and id=?",
                                                                  jobExecutionId,
                                                                  stepExecutionId));
         }
      }
      final BatchStatement batch = batchBuilder.build();
      final ResultSet result = session.execute(batch);
      System.out.println("Errors=" + result.getExecutionInfo().getErrors().toString());
   }

   public static String createJobKey(final JobParameters jobParameters) {
      final Map<String, JobParameter> props = jobParameters.getParameters();
      final StringBuilder stringBuilder = new StringBuilder();
      final List<String> keys = new ArrayList<>(props.keySet());
      Collections.sort(keys);
      for (final String key : keys) {
         stringBuilder.append(key).append("=").append(props.get(key).toString()).append(";");
      }
      MessageDigest digest;
      try {
         digest = MessageDigest.getInstance("MD5");
      }
      catch (final NoSuchAlgorithmException e) {

         throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
      }
      try {
         final byte[] bytes = digest.digest(stringBuilder.toString().getBytes("UTF-8"));
         return String.format("%032x", new BigInteger(1, bytes));
      }
      catch (final UnsupportedEncodingException e) {
         throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
      }
   }

   private List<Long> getJobExecutions(final CqlSession session, final long jobInstanceId) throws Exception {
      final List<Long> jobExecutions = new ArrayList<>();

      final SimpleStatement query = SimpleStatement.newInstance("select id from dfce.job_execution_by_instance where job_instance_id=?", jobInstanceId);
      final ResultSet rs = session.execute(query);
      for (final Row row : rs) {
         final long jobExecutionId = row.getLong("id");
         jobExecutions.add(jobExecutionId);
      }
      return jobExecutions;
   }

   private List<Long> getStepExecutions(final CqlSession session, final long jobExecutionId) throws Exception {
      final List<Long> stepExecutions = new ArrayList<>();

      final SimpleStatement query = SimpleStatement.newInstance("select id from dfce.step_execution_by_job_execution where job_execution_id=?", jobExecutionId);
      final ResultSet rs = session.execute(query);
      for (final Row row : rs) {
         final long stepId = row.getLong("id");
         stepExecutions.add(stepId);
      }

      return stepExecutions;
   }

   public JobInstanceInfo getJobInstanceInfo(final CqlSession session, final long jobInstanceId) throws Exception {

      final SimpleStatement query = SimpleStatement.newInstance("select * from dfce.job_instance_by_id where id=?", jobInstanceId);
      final Row row = session.execute(query).one();
      final String parametersAsJson = row.getString("job_parameters");
      final JobParameters jobParameters = jsonMapper.readValue(parametersAsJson, JobParameters.class);
      final String jobKey = createJobKey(jobParameters);
      return new JobInstanceInfo(row.getString("job_name"), jobKey);
   }

   public static ObjectMapper jsonCqlMapper() {
      final ObjectMapper objectMapper = new ObjectMapper();
      final SimpleModule simpleModule = new SimpleModule("myModule", org.codehaus.jackson.Version.unknownVersion());
      simpleModule.addDeserializer(JobParameter.class, new JobParameterDeserializer());
      objectMapper.registerModule(simpleModule);
      /*
      objectMapper.addMixIn(JobParameter.class, JobParameterMixin.class);
      objectMapper.addMixIn(JobParameters.class, JobParametersMixin.class);
      */
      return objectMapper;
   }

   /**
    * Met la propriété "running" du job à false dans la cf Jobs
    * 
    * @param keyspace
    * @param jobKey
    * @throws Exception
    */
   public void setJobAsNotRunning(final Keyspace keyspace, final String jobKey) throws Exception {
      throw new Exception("TODO");
      // final JobsDao dao = new JobsDao(keyspace);
      // dao.setJobAsNonRunning(jobKey);
   }

   /**
    * Permet de trouver un jobInstance référence dans jobExecution mais qui n'existe pas dans la CF JobInstance
    * 
    * @param keyspace
    * @throws Exception
    */
   public void findNonExistantJobInstances(final CqlSession session) throws Exception {
      System.out.println("Récupération de la liste des jobInstances...");
      final Map<Long, JobInstance> jobInstances = getJobInstances(session);
      System.out.println("Parcours de la liste...");
      final int count = jobInstances.keySet().size();
      int counter = 0;
      int errorCount = 0;
      Instant mostRecentError = Instant.parse("1920-05-13T10:15:30.00Z");
      for (final Long jobInstanceId : jobInstances.keySet()) {
         try {
            final JobInstanceInfo jobInstanceInfo = getJobInstanceInfo(session, jobInstanceId);
         }
         catch (final JobInstanceNotFoundException ex) {
            System.out.println("============= JobInstanceId non trouvé : " + jobInstanceId);

            System.out.println("maxCreationTime : " + jobInstances.get(jobInstanceId).maxCreationTime);
            System.out.println("jobExecutions : " + StringUtils.join(jobInstances.get(jobInstanceId).jobExecutionIds, ","));
            if (mostRecentError.isBefore(jobInstances.get(jobInstanceId).maxCreationTime)) {
               mostRecentError = jobInstances.get(jobInstanceId).maxCreationTime;
            }
            errorCount++;
         }
         counter++;
         if (counter % 200 == 0) {
            System.out.println(counter + " / " + count + " ...");
         }
      }
      System.out.println("Nombre d'erreurs détectées : " + errorCount);
      System.out.println("mostRecentError : " + mostRecentError);
   }

   public class JobInstanceInfo {
      public String jobName;

      public String jobKey;

      public JobInstanceInfo(final String jobName, final String jobKey) {
         this.jobName = jobName;
         this.jobKey = jobKey;
      }
   }

   class JobInstance {
      public ArrayList<Long> jobExecutionIds;

      public Instant maxCreationTime;

      public JobInstance(final ArrayList<Long> jobExecutionIds, final Instant maxCreationTime) {
         this.jobExecutionIds = jobExecutionIds;
         this.maxCreationTime = maxCreationTime;
      }
   }
}
