package fr.urssaf.astyanaxtest.cleanjob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;

import fr.urssaf.astyanaxtest.dao.JobExecutionCF;
import fr.urssaf.astyanaxtest.dao.JobInstanceCF;
import fr.urssaf.astyanaxtest.dao.JobsDao;
import fr.urssaf.astyanaxtest.dao.StepExecutionCF;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;

/**
 * Permet de purger les CF JobInstance, JobExecution et StepExecution du keyspace Docubase
 */
public class JobCleaner {

   public void purgeOldJobs(final Keyspace keyspace, final int daysToKeep) throws Exception {
      // Calcul de la date limite
      final Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      c.add(Calendar.DATE, -daysToKeep);
      final Date thresholdDate = c.getTime();

      final Map<Long, JobInstance> jobInstances = getJobInstances(keyspace);
      for (final Long jobInstanceId : jobInstances.keySet()) {
         final JobInstance jobInstance = jobInstances.get(jobInstanceId);
         if (jobInstance.maxCreationTime.before(thresholdDate)) {
            final int jobExecutionsCount = jobInstance.jobExecutionIds.size();
            System.out.println(jobInstanceId + " - " + jobExecutionsCount + " - " + jobInstance.maxCreationTime);
            deleteOneJobInstance(keyspace, jobInstanceId);
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
   private Map<Long, JobInstance> getJobInstances(final Keyspace keyspace) throws Exception {
      Rows<Long, String> rows;
      final Map<Long, JobInstance> jobInstances = new HashMap<Long, JobInstance>();
      rows = keyspace.prepareQuery(JobExecutionCF.get())
                     .getAllRows()
                     .setRowLimit(50)
                     .execute()
                     .getResult();
      for (final Row<Long, String> row : rows) {
         final ColumnList<String> columns = row.getColumns();
         // long jobExecutionId = row.getKey();
         final long jobInstanceId = columns.getLongValue("jobInstanceId", 0L);
         final long jobExecutionId = columns.getLongValue("jobExecutionId", 0L);
         final Date creationTime = columns.getDateValue("createTime", new Date(0));
         if (jobInstanceId == 0) {
            continue;
         }
         // System.out.println(jobInstanceId + " - " + jobExecutionId + " - " + createTime);
         if (!jobInstances.containsKey(jobInstanceId)) {
            final ArrayList<Long> jobExecutionIds = new ArrayList<Long>();
            jobExecutionIds.add(jobExecutionId);
            jobInstances.put(jobInstanceId, new JobInstance(jobExecutionIds, creationTime));
         } else {
            final JobInstance jobInstance = jobInstances.get(jobInstanceId);
            final ArrayList<Long> jobExecutionIds = jobInstance.jobExecutionIds;
            jobExecutionIds.add(jobExecutionId);
            if (creationTime.after(jobInstance.maxCreationTime)) {
               jobInstance.maxCreationTime = creationTime;
            }
         }
      }
      return jobInstances;
   }

   public void deleteOneJobInstance(final Keyspace keyspace, final long jobInstanceId) throws Exception {
      final MutationBatch batch = keyspace.prepareMutationBatch();
      try {
         final byte[] jobInstanceKey = getJobInstanceKey(keyspace, jobInstanceId);
         System.out.println("Suppression jobInstance " + ConvertHelper.getReadableUTF8String(jobInstanceKey));
         batch.withRow(JobInstanceCF.get(), jobInstanceKey).delete();
      }
      catch (final Exception e) {
         System.out.println("Clé non trouvée dans jobInstance");
      }

      final List<Long> jobExecuctions = getJobExecutions(keyspace, jobInstanceId);
      for (final Long jobExecutionId : jobExecuctions) {
         System.out.println("Suppression jobExecution " + jobExecutionId);
         batch.withRow(JobExecutionCF.get(), jobExecutionId).delete();

         final List<Long> stepExecutions = getStepExecutions(keyspace, jobExecutionId);
         for (final Long stepExecutionId : stepExecutions) {
            System.out.println("Suppression step " + stepExecutionId);
            batch.withRow(StepExecutionCF.get(), stepExecutionId).delete();
         }
      }
      final OperationResult<Void> result = batch.execute();
      System.out.println("Batch de suppression exécuté en : " + result.getLatency(TimeUnit.MILLISECONDS) + " ms");
   }

   private List<Long> getJobExecutions(final Keyspace keyspace, final long jobInstanceId) throws Exception {
      final List<Long> jobExecutions = new ArrayList<Long>();

      // On parcours JobExecution en utilisant l'index secondaire
      final Rows<Long, String> rows = keyspace.prepareQuery(JobExecutionCF.get())
                                              .searchWithIndex()
                                              .setRowLimit(5000)
                                              .addExpression()
                                              .whereColumn("jobInstanceId")
                                              .equals()
                                              .value(jobInstanceId)
                                              .execute()
                                              .getResult();
      for (final Row<Long, String> row : rows) {
         final long jobExecutionId = row.getKey();
         jobExecutions.add(jobExecutionId);
      }
      return jobExecutions;
   }

   private List<Long> getStepExecutions(final Keyspace keyspace, final long jobExecutionId) throws Exception {
      final List<Long> stepExecutions = new ArrayList<Long>();

      // On parcours StepExecution en utilisant l'index secondaire
      final Rows<Long, String> rows = keyspace.prepareQuery(StepExecutionCF.get())
                                              .searchWithIndex()
                                              .setRowLimit(500)
                                              .addExpression()
                                              .whereColumn("jobExecutionId")
                                              .equals()
                                              .value(jobExecutionId)
                                              .execute()
                                              .getResult();
      for (final Row<Long, String> row : rows) {
         final long stepExecutionId = row.getKey();
         stepExecutions.add(stepExecutionId);
      }
      return stepExecutions;
   }

   private byte[] getJobInstanceKey(final Keyspace keyspace, final long jobInstanceId) throws Exception {

      // On parcours JobInstance en utilisant l'index secondaire
      final Rows<byte[], String> rows = keyspace.prepareQuery(JobInstanceCF.get())
                                                .searchWithIndex()
                                                .setRowLimit(1)
                                                .addExpression()
                                                .whereColumn("jobInstanceId")
                                                .equals()
                                                .value(jobInstanceId)
                                                .execute()
                                                .getResult();
      for (final Row<byte[], String> row : rows) {
         return row.getKey();
      }
      throw new JobInstanceNotFoundException("Clé non trouvée pour jobInstanceId=" + jobInstanceId);
   }

   /**
    * Met la propriété "running" du job à false dans la cf Jobs
    * 
    * @param keyspace
    * @param jobKey
    * @throws Exception
    */
   public void setJobAsNotRunning(final Keyspace keyspace, final String jobKey) throws Exception {
      final JobsDao dao = new JobsDao(keyspace);
      dao.setJobAsNonRunning(jobKey);
   }

   /**
    * Permet de trouver un jobInstance référence dans jobExecution mais qui n'existe pas dans la CF JobInstance
    * 
    * @param keyspace
    * @throws Exception
    */
   public void findNonExistantJobInstances(final Keyspace keyspace) throws Exception {
      System.out.println("Récupération de la liste des jobInstances...");
      final Map<Long, JobInstance> jobInstances = getJobInstances(keyspace);
      System.out.println("Parcours de la liste...");
      final int count = jobInstances.keySet().size();
      int counter = 0;
      int errorCount = 0;
      Date mostRecentError = new SimpleDateFormat("yyyyMMdd").parse("19200520");
      for (final Long jobInstanceId : jobInstances.keySet()) {
         try {
            final byte[] key = getJobInstanceKey(keyspace, jobInstanceId);
         }
         catch (final JobInstanceNotFoundException ex) {
            System.out.println("============= JobInstanceId non trouvé : " + jobInstanceId);

            System.out.println("maxCreationTime : " + jobInstances.get(jobInstanceId).maxCreationTime);
            System.out.println("jobExecutions : " + StringUtils.join(jobInstances.get(jobInstanceId).jobExecutionIds, ","));
            if (mostRecentError.before(jobInstances.get(jobInstanceId).maxCreationTime)) {
               mostRecentError = jobInstances.get(jobInstanceId).maxCreationTime;
            }
            errorCount++;
         }
         counter++;
         if (counter % 200 == 0) {
            System.out.println(counter + " / " + count + " ...");
         }
      }
      System.out.println("Nombre d'erreurs détéctées : " + errorCount);
      System.out.println("mostRecentError : " + mostRecentError);
   }

   class JobInstance {
      public ArrayList<Long> jobExecutionIds;

      public Date maxCreationTime;

      public JobInstance(final ArrayList<Long> jobExecutionIds, final Date maxCreationTime) {
         this.jobExecutionIds = jobExecutionIds;
         this.maxCreationTime = maxCreationTime;
      }
   }
}
