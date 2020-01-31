package fr.urssaf.astyanaxtest.cleanjob;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import com.netflix.astyanax.ExceptionCallback;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.exceptions.NotFoundException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.util.RangeBuilder;

import fr.urssaf.astyanaxtest.dao.sae.JobExecutionCF;
import fr.urssaf.astyanaxtest.dao.sae.JobExecutionToJobStepCF;
import fr.urssaf.astyanaxtest.dao.sae.JobExecutionsCF;
import fr.urssaf.astyanaxtest.dao.sae.JobInstanceCF;
import fr.urssaf.astyanaxtest.dao.sae.JobInstanceToJobExecutionCF;
import fr.urssaf.astyanaxtest.dao.sae.JobInstancesByNameCF;
import fr.urssaf.astyanaxtest.dao.sae.JobStepCF;
import fr.urssaf.astyanaxtest.dao.sae.JobStepsCF;

/**
 * Permet de purger, dans le keyspace SAE, les CF relatives à spring batch
 * (JobInstance, JobExecution et JobStep, ...°
 */
public class SAEJobCleaner {

   /**
    * Renvoie le liste des jobInstances pour le jobKey donné
    * Normalement, il y a au maximum un élément dans la liste renvoyée
    * 
    * @param jobKey
    *           clé du job
    * @return
    */
   public List<Long> jobKeyToJobInstanceIds(final Keyspace keyspace, final byte[] jobKey) throws Exception {

      final List<Long> jobInstances = new ArrayList<Long>();

      // On parcours JobInstance en utilisant l'index secondaire sur jobKey
      final Rows<Long, String> rows = keyspace.prepareQuery(JobInstanceCF.get())
            .searchWithIndex()
            .setRowLimit(5000)
            .addExpression()
            .whereColumn("jobKey")
            .equals()
            .value(jobKey)
            .execute()
            .getResult();
      for (final Row<Long, String> row : rows) {
         final long jobExecutionId = row.getKey();
         jobInstances.add(jobExecutionId);
      }
      return jobInstances;
   }

   /**
    * Parcours la CF JobInstance, et affiche les instances dont le paramètre contient whatToFind
    * 
    * @param keyspace
    * @param whatToFind
    *           Chaîne de caractères à chercher dans les parameters des jobs
    * @throws Exception
    */
   public void findJobInstanceByParameter(final Keyspace keyspace, final String whatToFind) throws Exception {
      final OperationResult<Rows<Long, String>> rows = keyspace
            .prepareQuery(JobInstanceCF.get())
            .getAllRows()
            .setRowLimit(100)
            // This is the page size
            .withColumnRange(new RangeBuilder().setLimit(100).build())
            .setExceptionCallback(new ExceptionCallback() {
               public boolean onException(final ConnectionException e) {
                  Assert.fail(e.getMessage());
                  return true;
               }
            })
            .execute();

      int counter = 0;
      for (final Row<Long, String> row : rows.getResult()) {
         final Long key = row.getKey();
         final ColumnList<String> columns = row.getColumns();
         for (final Column<String> column : columns) {
            final String name = column.getName();
            // System.out.println("Name :" + ConvertHelper.getReadableUTF8String(name));
            if ("parameters".equals(name)) {
               final String parameters = column.getStringValue();
               if (parameters.contains(whatToFind)) {
                  System.out.println("key=" + key);
                  System.out.println("parameters=" + parameters);
               }
            }
         }
         counter++;
         if (counter % 10000 == 0) {
            System.out.println(counter);
         }
      }
   }

   /**
    * Affiche les jobExecutions dont la colonne "executionContext" est de taille importante
    * 
    * @param keyspace
    * @throws Exception
    */
   public void findBigExecutionContexts(final Keyspace keyspace) throws Exception {
      final OperationResult<Rows<Long, String>> rows = keyspace
            .prepareQuery(JobExecutionCF.get())
            .getAllRows()
            .setRowLimit(100)
            // This is the page size
            .withColumnRange(new RangeBuilder().setLimit(100).build())
            .setExceptionCallback(new ExceptionCallback() {
               public boolean onException(final ConnectionException e) {
                  Assert.fail(e.getMessage());
                  return true;
               }
            })
            .execute();

      int counter = 0;
      long maxLength = 0;
      for (final Row<Long, String> row : rows.getResult()) {
         final Long key = row.getKey();
         final ColumnList<String> columns = row.getColumns();
         final String executionContext = columns.getStringValue("executionContext", "");
         final int length = executionContext.length();
         if (length > maxLength) {
            maxLength = length;
            final Date lastUpdated = columns.getDateValue("lastUpdated", null);
            System.out.println("key=" + key + " length=" + length + " Date=" + lastUpdated);
         }
         counter++;
         if (counter % 10000 == 0) {
            System.out.println("counter=" + counter + " maxLength=" + maxLength);
         }
      }
      System.out.println("counter=" + counter + " maxLength=" + maxLength);
   }

   /**
    * Lance la purge des vieux jobs.
    * 
    * @param keyspace
    * @param daysToKeep
    *           Nombre de jours de rétention
    * @throws Exception
    */
   public void purgeOldJobs(final Keyspace keyspace, final int daysToKeep, final boolean simulationMode) throws Exception {
      // Calcul de la date limite
      final Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      c.add(Calendar.DATE, -daysToKeep);
      final Date thresholdDate = c.getTime();

      System.out.println("Récupération de la liste des jobInstance...");
      final Map<Long, JobInstance> jobInstances = getJobInstances(keyspace);
      System.out.println("Nombre de jobInstance : " + jobInstances.size());
      int toDeleteCount = 0;
      for (final Long jobInstanceId : jobInstances.keySet()) {
         final JobInstance jobInstance = jobInstances.get(jobInstanceId);
         if (jobInstance.maxCreationTime.before(thresholdDate)) {
            toDeleteCount++;
         }
      }
      System.out.println("Nombre de jobInstance à supprimer : " + toDeleteCount);

      int deleteCounter = 0;
      for (final Long jobInstanceId : jobInstances.keySet()) {
         final JobInstance jobInstance = jobInstances.get(jobInstanceId);
         if (jobInstance.maxCreationTime.before(thresholdDate)) {
            deleteCounter++;
            System.out.println(
                  "==== JobInstance : " + jobInstanceId + " (" + deleteCounter + "/" + toDeleteCount + ") - maxCreationTime : " + jobInstance.maxCreationTime);
            deleteOneJobInstance(keyspace, jobInstanceId, simulationMode);
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
         final long jobExecutionId = row.getKey();
         final long jobInstanceId = columns.getLongValue("jobInstanceId", 0L);
         final Date creationTime = columns.getDateValue("creationTime", new Date(0));
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
            final List<Long> jobExecutionIds = jobInstance.jobExecutionIds;
            jobExecutionIds.add(jobExecutionId);
            if (creationTime.after(jobInstance.maxCreationTime)) {
               jobInstance.maxCreationTime = creationTime;
            }
         }
      }
      return jobInstances;
   }

   public void deleteOneJobInstance(final Keyspace keyspace, final long jobInstanceId, final boolean simulationMode) throws Exception {
      final MutationBatch batch = keyspace.prepareMutationBatch();

      final String jobName = getJobName(keyspace, jobInstanceId);
      if ("".equals(jobName)) {
         throw new Exception("JobInstance non trouvé : " + jobInstanceId);
      }
      System.out.println("Suppression jobInstance " + jobInstanceId);
      batch.withRow(JobInstanceCF.get(), jobInstanceId).delete();
      batch.withRow(JobInstancesByNameCF.get(), jobName).deleteColumn(jobInstanceId);
      if (isUnreserved(keyspace, jobInstanceId)) {
         // System.out.println("isUnreserved=true");
         batch.withRow(JobInstancesByNameCF.get(), "_unreserved").deleteColumn(jobInstanceId);
      } else {
         // System.out.println("isUnreserved=false");
      }

      final Collection<Long> jobExecuctions = getJobExecutions(keyspace, jobInstanceId);
      for (final Long jobExecutionId : jobExecuctions) {
         System.out.println("Suppression jobExecution " + jobExecutionId);
         batch.withRow(JobExecutionCF.get(), jobExecutionId).delete();
         batch.withRow(JobExecutionsCF.get(), jobName).deleteColumn(jobExecutionId);
         batch.withRow(JobExecutionsCF.get(), "_all").deleteColumn(jobExecutionId);

         final Collection<Long> stepExecutions = getStepExecutions(keyspace, jobExecutionId);
         for (final Long stepExecutionId : stepExecutions) {
            System.out.println("Suppression step " + stepExecutionId);
            batch.withRow(JobStepCF.get(), stepExecutionId).delete();
            batch.withRow(JobStepsCF.get(), "jobSteps").deleteColumn(stepExecutionId);
         }
         batch.withRow(JobExecutionToJobStepCF.get(), jobExecutionId).delete();
      }
      batch.withRow(JobInstanceToJobExecutionCF.get(), jobInstanceId).delete();

      if (!simulationMode) {
         final OperationResult<Void> result = batch.execute();
         System.out.println("Batch de suppression exécuté en : " + result.getLatency(TimeUnit.MILLISECONDS) + " ms");
      }
   }

   private boolean isUnreserved(final Keyspace keyspace, final long jobInstanceId) throws Exception {
      try {
         final OperationResult<Column<Long>> col = keyspace
               .prepareQuery(JobInstancesByNameCF.get())
               .getKey("_unreserved")
               .getColumn(jobInstanceId)
               .execute();
         final byte[] value = col.getResult().getByteArrayValue();
         return true;
      }
      catch (final NotFoundException e) {
         return false;
      }
   }

   private Collection<Long> getJobExecutions(final Keyspace keyspace, final long jobInstanceId) throws Exception {
      final OperationResult<ColumnList<Long>> cols = keyspace
            .prepareQuery(JobInstanceToJobExecutionCF.get())
            .getKey(jobInstanceId)
            .execute();
      final ColumnList<Long> result = cols.getResult();
      return result.getColumnNames();
   }

   private Collection<Long> getStepExecutions(final Keyspace keyspace, final long jobExecutionId) throws Exception {
      final OperationResult<ColumnList<Long>> cols = keyspace
            .prepareQuery(JobExecutionToJobStepCF.get())
            .getKey(jobExecutionId)
            .execute();
      final ColumnList<Long> result = cols.getResult();
      return result.getColumnNames();
   }

   public String getJobName(final Keyspace keyspace, final long jobInstanceId) throws Exception {
      final OperationResult<ColumnList<String>> cols = keyspace
            .prepareQuery(JobInstanceCF.get())
            .getKey(jobInstanceId)
            .execute();
      final ColumnList<String> result = cols.getResult();
      return result.getStringValue("name", "");
   }


   class JobInstance {
      public List<Long> jobExecutionIds;

      public Date maxCreationTime;

      public JobInstance(final List<Long> jobExecutionIds, final Date maxCreationTime) {
         this.jobExecutionIds = jobExecutionIds;
         this.maxCreationTime = maxCreationTime;
      }
   }
}
