package fr.urssaf.image.sae.test.dfce17;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobExecutionException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobInstanceException;

@RunWith(BlockJUnit4ClassRunner.class)
public class JobAdminTest {

   private static final Logger LOGGER = LoggerFactory
   .getLogger(JobAdminTest.class);
   
   @Test
   public void getJobsInstanceByJobName() throws NoSuchDfceJobException, NoSuchDfceJobInstanceException, NoSuchDfceJobExecutionException {
      String jobName = "splitRangeIndexJob";
      
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      provider.connect("_ADMIN", "DOCUBASE", "  http://hwi69pprodsaeapp.cer69.recouv:8080/dfce-webapp/toolkit/", 3 * 60 * 1000);
      
      List<Long> jobsInstance = provider.getJobAdministrationService().getJobInstances(jobName, 0, 100);
      for (Long idJobInstance : jobsInstance) {
         List<Long> jobsExecution = provider.getJobAdministrationService().getExecutions(idJobInstance);
         for (Long idJobExec :  jobsExecution) {
            String parametres = provider.getJobAdministrationService().getParameters(idJobExec);
            int indexNamePosDeb = parametres.indexOf("index.name");
            int indexNamePosFin = parametres.indexOf("\n", indexNamePosDeb);
            String indexName = parametres.substring(indexNamePosDeb + 11,  indexNamePosFin);
            LOGGER.debug("Execution {} pour l'instance {} du job {} : {}", new Object[] {idJobExec, idJobInstance, jobName, indexName });
         }
      }
      
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void getSummaryAndStepSummaryForPreprod() throws NoSuchDfceJobExecutionException {
      Long idJobExecution = Long.valueOf(6069);
      
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      //provider.connect("_ADMIN", "DOCUBASE", "  http://hwi69pprodsaeapp.cer69.recouv:8080/dfce-webapp/toolkit/", 3 * 60 * 1000);
      provider.connect("_ADMIN", "DOCUBASE", "  http://hwi69saeappli1.cer69.recouv:8080/dfce-webapp/toolkit/", 3 * 60 * 1000);
      
      String summary = provider.getJobAdministrationService().getSummary(idJobExecution);
      LOGGER.debug("Summary : {}", summary);
      
      Map<Long, String> stepSummaries = provider.getJobAdministrationService().getStepExecutionSummaries(idJobExecution);
      for (Map.Entry<Long, String> entry : stepSummaries.entrySet()) {
         LOGGER.debug("{}", entry.getValue());
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   private byte[] convertLongToByte(long valeur) {
      return ByteBuffer.allocate(8).putLong(valeur).array();
   }
   
   public static String bytesToHex(byte[] bytes) {
      final char[] hexArray = "0123456789ABCDEF".toCharArray();
      char[] hexChars = new char[bytes.length * 2];
      for (int j = 0; j < bytes.length; j++) {
         int v = bytes[j] & 0xFF;
         hexChars[j * 2] = hexArray[v >>> 4];
         hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
   }
   
   @Test
   public void convertIdJobExec() {
      Long idJobExecution = Long.valueOf(4105);
      
      LOGGER.debug("Row key : {}", bytesToHex(convertLongToByte(idJobExecution)));
   }
   
   private Keyspace getKeyspaceDocubaseFromKeyspace(String hosts) {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            hosts);
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   private Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }
   
   @Test
   public void getSummaryAndStepSummaryForCSPP() throws NoSuchDfceJobExecutionException, NoSuchDfceJobException {
      String nomJob = "splitRangeIndexJob";
      String[] colonnes = { "jobInstanceId", "jobInstanceName", "status", "exitCode", "endTime" };
      Long idJobExec = Long.valueOf(-1);
      Long endTime = Long.valueOf(-1);
      String status = "";
      String exitCode = "";
      int compteurJob = 0;
      
      String hosts = "cnp6saecvecas1.cve.recouv:9160,cnp6saecvecas2.cve.recouv:9160,cnp6saecvecas3.cve.recouv:9160,cnp6saecvecas4.cve.recouv:9160,cnp6saecvecas5.cve.recouv:9160,cnp6saecvecas6.cve.recouv:9160";
      
      LOGGER.debug("Recherche de la derniere execution du job {}", nomJob);
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hosts);
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobExecution").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(10000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)) {
               // un job du bon nom a ete trouve
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               HColumn<String, byte[]> columnStatus = row.getColumnSlice().getColumnByName("status");
               HColumn<String, byte[]> columnExitCode = row.getColumnSlice().getColumnByName("exitCode");
               HColumn<String, byte[]> columnEndTime = row.getColumnSlice().getColumnByName("endTime");
               Long idJobCourant = convertByteToLong(row.getKey());
               if ((columnId != null) && (idJobExec <= idJobCourant)) {
                  idJobExec = idJobCourant;
                  status = new String(columnStatus.getValue());
                  exitCode = new String(columnExitCode.getValue());
                  if (columnEndTime != null && columnEndTime.getValue() != null) {
                     endTime = convertByteToLong(columnEndTime.getValue());
                  } else  {
                     endTime = Long.valueOf(-1);
                  }
                    
               } 
            }
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
      LOGGER.debug("Id de l'execution du job trouve : {}", idJobExec);
      if (!(status.equals("FAILED") || status.equals("COMPLETED"))) {
         LOGGER.debug("Le statut de l'execution du job {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idJobExec.toString(), status});
      }
      if (!(exitCode.equals("FAILED") || exitCode.equals("COMPLETED"))) {
         LOGGER.debug("L'exitCode de l'execution du job {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idJobExec.toString(), exitCode});
      }
      if (endTime.longValue() == -1) {
         LOGGER.debug("L'endTime n'est pas renseigne pour l'execution du job {}, il faut le renseigner", new String[] { idJobExec.toString()});
      } else {
         LOGGER.debug("L'endTime est deja renseigne, rien a faire {}", new String[] { endTime.toString() });
      }
      
      String[] colonnesStep = { "jobExecutionId", "stepId", "stepName", "status", "commitCount" };
      Map<Long, String> stepExecution = new TreeMap<Long, String>();
      LOGGER.debug("Recuperation la liste des steps de l'execution du job {}", idJobExec);
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubaseStep = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubaseStep.setColumnFamily("StepExecution").setKeys(null, null);
      rangeQueryDocubaseStep.setColumnNames(colonnesStep);
      rangeQueryDocubaseStep.setRowCount(50000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubaseStep = rangeQueryDocubaseStep.execute();
      if (rangeResultDocubaseStep != null && rangeResultDocubaseStep.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubaseStep.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobExecutionId");
            Long idJobCourant = convertByteToLong(columnId.getValue());
            if (columnId != null && idJobCourant.equals(idJobExec)) {
               HColumn<String, byte[]> columnStepId = row.getColumnSlice().getColumnByName("stepId");
               Long idStepCourant = convertByteToLong(columnStepId.getValue());
               HColumn<String, byte[]> columnStepName = row.getColumnSlice().getColumnByName("stepName");
               String stepName = new String(columnStepName.getValue());
               HColumn<String, byte[]> columnStatus = row.getColumnSlice().getColumnByName("status");
               String stepStatus = new String(columnStatus.getValue());
               if (columnStepId != null) {
                  stepExecution.put(idStepCourant, stepName + " -> " + stepStatus); 
               }
               HColumn<String, byte[]> columnCommitCount = row.getColumnSlice().getColumnByName("commitCount");
               if (columnCommitCount != null && endTime == -1) {
                  LOGGER.debug("Le step {} a ete mise a jour a {}", new Long[] { idStepCourant, columnCommitCount.getClock() / 1000});
               }
            }
         }
         Long idStepExecution = Long.valueOf(-1);
         String statusStepExecution = "";
         for (Entry<Long, String> entry : stepExecution.entrySet()) {
            LOGGER.debug("{} : {}", new Object[] {entry.getKey(), entry.getValue()});
            idStepExecution = entry.getKey();
            statusStepExecution = entry.getValue().split(" ")[2];
         }
         if (!(statusStepExecution.equals("FAILED") || statusStepExecution.equals("COMPLETED"))) {
            LOGGER.debug("Le statut du step {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idStepExecution.toString(), statusStepExecution});
         }
      }
      LOGGER.debug("Une fois les mises à jour faite, relancer l'execution du job {}", idJobExec);
   }
   
   
   @Test
   public void getSummaryAndStepSummaryForProdGns() throws NoSuchDfceJobExecutionException, NoSuchDfceJobException {
      String nomJob = "clearEventJob";
      String[] colonnes = { "jobInstanceId", "jobInstanceName", "status", "exitCode", "endTime" };
      Long idJobExec = Long.valueOf(-1);
      Long endTime = Long.valueOf(-1);
      String status = "";
      String exitCode = "";
      int compteurJob = 0;
      
      String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
      
      LOGGER.debug("Recherche de la derniere execution du job {}", nomJob);
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hosts);
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobExecution").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(10000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)) {
               // un job du bon nom a ete trouve
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               HColumn<String, byte[]> columnStatus = row.getColumnSlice().getColumnByName("status");
               HColumn<String, byte[]> columnExitCode = row.getColumnSlice().getColumnByName("exitCode");
               HColumn<String, byte[]> columnEndTime = row.getColumnSlice().getColumnByName("endTime");
               Long idJobCourant = convertByteToLong(row.getKey());
               if ((columnId != null) && (idJobExec <= idJobCourant)) {
                  idJobExec = idJobCourant;
                  status = new String(columnStatus.getValue());
                  exitCode = new String(columnExitCode.getValue());
                  if (columnEndTime != null && columnEndTime.getValue() != null) {
                     endTime = convertByteToLong(columnEndTime.getValue());
                  }
               } 
            }
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
      LOGGER.debug("Id de l'execution du job trouve : {}", idJobExec);
      if (!(status.equals("FAILED") || status.equals("COMPLETED"))) {
         LOGGER.debug("Le statut de l'execution du job {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idJobExec.toString(), status});
      }
      if (!(exitCode.equals("FAILED") || exitCode.equals("COMPLETED"))) {
         LOGGER.debug("L'exitCode de l'execution du job {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idJobExec.toString(), exitCode});
      }
      if (endTime.longValue() == -1) {
         LOGGER.debug("L'endTime n'est pas renseigne pour l'execution du job {}, il faut le renseigner", new String[] { idJobExec.toString()});
      } else {
         LOGGER.debug("L'endTime est deja renseigne, rien a faire {}", new String[] { endTime.toString() });
      }
      
      String[] colonnesStep = { "jobExecutionId", "stepId", "stepName", "status" };
      Map<Long, String> stepExecution = new TreeMap<Long, String>();
      LOGGER.debug("Recuperation la liste des steps de l'execution du job {}", idJobExec);
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubaseStep = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubaseStep.setColumnFamily("StepExecution").setKeys(null, null);
      rangeQueryDocubaseStep.setColumnNames(colonnesStep);
      rangeQueryDocubaseStep.setRowCount(50000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubaseStep = rangeQueryDocubaseStep.execute();
      if (rangeResultDocubaseStep != null && rangeResultDocubaseStep.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubaseStep.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobExecutionId");
            Long idJobCourant = convertByteToLong(columnId.getValue());
            if (columnId != null && idJobCourant.equals(idJobExec)) {
               HColumn<String, byte[]> columnStepId = row.getColumnSlice().getColumnByName("stepId");
               Long idStepCourant = convertByteToLong(columnStepId.getValue());
               HColumn<String, byte[]> columnStepName = row.getColumnSlice().getColumnByName("stepName");
               String stepName = new String(columnStepName.getValue());
               HColumn<String, byte[]> columnStatus = row.getColumnSlice().getColumnByName("status");
               String stepStatus = new String(columnStatus.getValue());
               if (columnStepId != null) {
                  stepExecution.put(idStepCourant, stepName + " -> " + stepStatus); 
               }
            }
         }
         Long idStepExecution = Long.valueOf(-1);
         String statusStepExecution = "";
         for (Entry<Long, String> entry : stepExecution.entrySet()) {
            LOGGER.debug("{} : {}", new Object[] {entry.getKey(), entry.getValue()});
            idStepExecution = entry.getKey();
            statusStepExecution = entry.getValue().split(" ")[2];
         }
         if (!(statusStepExecution.equals("FAILED") || statusStepExecution.equals("COMPLETED"))) {
            LOGGER.debug("Le statut du step {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idStepExecution.toString(), statusStepExecution});
         }
      }
      LOGGER.debug("Une fois les mises à jour faite, relancer l'execution du job {}", idJobExec);
   }
   
   @Test
   public void getSummaryAndStepSummaryByIdForProdGns() throws NoSuchDfceJobExecutionException, NoSuchDfceJobException {
      String[] colonnes = { "jobInstanceId", "jobInstanceName", "status", "exitCode", "endTime" };
      Long idJobExec = Long.valueOf(-1);
      Long endTime = Long.valueOf(-1);
      String status = "";
      String exitCode = "";
      int compteurJob = 0;
      Long idJobExecSearch = Long.valueOf(6425); 
      
      String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
      
      LOGGER.debug("Recherche de l'execution du job {}", idJobExecSearch);
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hosts);
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobExecution").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(10000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
            HColumn<String, byte[]> columnStatus = row.getColumnSlice().getColumnByName("status");
            HColumn<String, byte[]> columnExitCode = row.getColumnSlice().getColumnByName("exitCode");
            HColumn<String, byte[]> columnEndTime = row.getColumnSlice().getColumnByName("endTime");
            Long idJobCourant = convertByteToLong(row.getKey());
            if (idJobExecSearch.longValue() == idJobCourant.longValue()) {
               idJobExec = idJobCourant;
               status = new String(columnStatus.getValue());
               exitCode = new String(columnExitCode.getValue());
               if (columnEndTime != null && columnEndTime.getValue() != null) {
                  endTime = convertByteToLong(columnEndTime.getValue());
               }
               break;
            } 
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
      LOGGER.debug("Id de l'execution du job trouve : {}", idJobExec);
      if (!(status.equals("FAILED") || status.equals("COMPLETED"))) {
         LOGGER.debug("Le statut de l'execution du job {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idJobExec.toString(), status});
      }
      if (!(exitCode.equals("FAILED") || exitCode.equals("COMPLETED"))) {
         LOGGER.debug("L'exitCode de l'execution du job {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idJobExec.toString(), exitCode});
      }
      if (endTime.longValue() == -1) {
         LOGGER.debug("L'endTime n'est pas renseigne pour l'execution du job {}, il faut le renseigner", new String[] { idJobExec.toString()});
      } else {
         LOGGER.debug("L'endTime est deja renseigne, rien a faire {}", new String[] { endTime.toString() });
      }
      
      String[] colonnesStep = { "jobExecutionId", "stepId", "stepName", "status", "commitCount" };
      Map<Long, String> stepExecution = new TreeMap<Long, String>();
      LOGGER.debug("Recuperation la liste des steps de l'execution du job {}", idJobExec);
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubaseStep = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubaseStep.setColumnFamily("StepExecution").setKeys(null, null);
      rangeQueryDocubaseStep.setColumnNames(colonnesStep);
      rangeQueryDocubaseStep.setRowCount(50000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubaseStep = rangeQueryDocubaseStep.execute();
      if (rangeResultDocubaseStep != null && rangeResultDocubaseStep.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubaseStep.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobExecutionId");
            Long idJobCourant = convertByteToLong(columnId.getValue());
            if (columnId != null && idJobCourant.equals(idJobExec)) {
               HColumn<String, byte[]> columnStepId = row.getColumnSlice().getColumnByName("stepId");
               Long idStepCourant = convertByteToLong(columnStepId.getValue());
               HColumn<String, byte[]> columnStepName = row.getColumnSlice().getColumnByName("stepName");
               String stepName = new String(columnStepName.getValue());
               HColumn<String, byte[]> columnStatus = row.getColumnSlice().getColumnByName("status");
               String stepStatus = new String(columnStatus.getValue());
               if (columnStepId != null) {
                  stepExecution.put(idStepCourant, stepName + " -> " + stepStatus); 
               }
               HColumn<String, byte[]> columnCommitCount = row.getColumnSlice().getColumnByName("commitCount");
               if (columnCommitCount != null && endTime == -1) {
                  LOGGER.debug("Le step {} a ete mise a jour a {}", new Long[] { idStepCourant, columnCommitCount.getClock() / 1000});
               }
            }
         }
         Long idStepExecution = Long.valueOf(-1);
         String statusStepExecution = "";
         for (Entry<Long, String> entry : stepExecution.entrySet()) {
            LOGGER.debug("{} : {}", new Object[] {entry.getKey(), entry.getValue()});
            idStepExecution = entry.getKey();
            statusStepExecution = entry.getValue().split(" ")[2];
         }
         if (!(statusStepExecution.equals("FAILED") || statusStepExecution.equals("COMPLETED"))) {
            LOGGER.debug("Le statut du step {} est a l'etat {}, le mettre a l'etat FAILED", new String[] { idStepExecution.toString(), statusStepExecution});
         }
      }
      LOGGER.debug("Une fois les mises à jour faite, relancer l'execution du job {}", idJobExec);
   }
   
   @Test
   public void getInfoFromJobProdGns() throws NoSuchDfceJobException, NoSuchDfceJobInstanceException, NoSuchDfceJobExecutionException {
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      provider.connect("_ADMIN", "DOCUBASE", "  http://hwi69saeappli1.cer69.recouv:8080/dfce-webapp/toolkit/", 3 * 60 * 1000);
      Map<Long, Long> viewDuree = new TreeMap<Long, Long>();
      
      List<Long> jobsInstance = provider.getJobAdministrationService().getJobInstances("processDocStatisticsJob", 0, 500);
      for (Long idJobInstance : jobsInstance) {
         Long idJobExec = provider.getJobAdministrationService().getExecutions(idJobInstance).get(0);
         Map<String, String> infos = provider.getJobAdministrationService().getSummaryAsMap(idJobExec);
         long duree = Long.valueOf(infos.get("endTime")) - Long.valueOf(infos.get("startTime"));  
         viewDuree.put(idJobInstance, duree);
      }
      
      for (Long idJobInstance : viewDuree.keySet()) {
         LOGGER.debug("{} : {}", new String[] {idJobInstance.toString(), convertDuree(viewDuree.get(idJobInstance))});
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   private String convertDuree(final Long duree) {
      StringBuffer buffer = new StringBuffer();
      long resteDuree = duree;
      if (resteDuree / (60 * 60 * 1000) > 0) {
         long nbHeure = resteDuree / (60 * 60 * 1000);
         resteDuree = resteDuree % (60 * 60 * 1000);
         buffer.append(nbHeure);
         buffer.append("h");
      }
      if (resteDuree / (60 * 1000) > 0) {
         long nbMinute = resteDuree / (60 * 1000);
         resteDuree = resteDuree % (60 * 1000);
         buffer.append(nbMinute);
         buffer.append("min");
      }
      if (resteDuree / (1000) > 0) {
         long nbSeconde = resteDuree / (1000);
         resteDuree = resteDuree % (1000);
         buffer.append(nbSeconde);
         buffer.append("s");
      }
      return buffer.toString();
   }
   
   @Test
   public void getSummaryAndStepSummaryForCSPP2() throws NoSuchDfceJobExecutionException {
      Long idJobExecution = Long.valueOf(4942);
      
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      provider.connect("_ADMIN", "DOCUBASE", "http://hwi3saecveappli1.cve.recouv:8080/dfce-webapp/toolkit/", 3 * 60 * 1000);
      
      String summary = provider.getJobAdministrationService().getSummary(idJobExecution);
      LOGGER.debug("Summary : {}", summary);
      
      Map<Long, String> stepSummaries = provider.getJobAdministrationService().getStepExecutionSummaries(idJobExecution);
      for (Map.Entry<Long, String> entry : stepSummaries.entrySet()) {
         LOGGER.debug("{}", entry.getValue());
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
}
