package fr.urssaf.image.sae.test.divers.dfce;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.JobAdministrationService;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.batch.launch.DfceJobExecutionNotRunningException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobExecutionException;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.test.divers.cassandra.AllRowsIterator;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-cspp-gns.xml" })
public class JobInstanceTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(JobInstanceTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   /**
    * Recuperation
    */
   @Autowired
   private CassandraServerBean cassandraServer;

   private Keyspace getKeyspaceDocubaseFromKeyspace() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            cassandraServer.getHosts());
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   private Keyspace getKeyspaceSaeFromKeyspace() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            cassandraServer.getHosts());
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("SAE", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   private String getAttributFromSpringBatch(String summary, String name) {
      int index = summary.indexOf(name);
      String valeur = "";
      if (index > 0) {
         int finIndex = summary.indexOf(",", index + 1);
         if (finIndex < 0) {
            finIndex = summary.indexOf("]", index + 1);
         }
         valeur = summary.substring(index + name.length() + 1, finIndex);
      }
      return valeur;
   }
   
   private Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }
   
   private byte[] convertLongToByte(long valeur) {
      return ByteBuffer.allocate(8).putLong(valeur).array();
  }
   
   
   public static String bytesToHex(byte[] bytes) {
      final char[] hexArray = "0123456789ABCDEF".toCharArray();
      char[] hexChars = new char[bytes.length * 2];
      for ( int j = 0; j < bytes.length; j++ ) {
          int v = bytes[j] & 0xFF;
          hexChars[j * 2] = hexArray[v >>> 4];
          hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
  }
   
   @Test
   public void getStateJobInstance() throws NoSuchDfceJobExecutionException {
      Long idJob = Long.valueOf(6112);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      JobAdministrationService jobAdminService = serviceProvider.getJobAdministrationService();
      
      LOGGER.debug("Recupere l'etat du job : {}", idJob);
      String resultatJob = jobAdminService.getSummary(idJob);
      LOGGER.debug("Resultat du job : {}", resultatJob);
      LOGGER.debug("Statut du job : {}", getAttributFromSpringBatch(resultatJob, "status"));
      LOGGER.debug("Type de job : {}", getAttributFromSpringBatch(resultatJob, "Job="));
      
      Map<Long, String> steps = jobAdminService.getStepExecutionSummaries(idJob);
      for (Long idStep : steps.keySet()) {
         LOGGER.debug("{} : {}", idStep, steps.get(idStep));
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void stopJob() {
      Long idJob = Long.valueOf(3330);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      JobAdministrationService jobAdminService = serviceProvider.getJobAdministrationService();
      
      try {
         jobAdminService.stop(idJob);
      } catch (NoSuchDfceJobExecutionException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      } catch (DfceJobExecutionNotRunningException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void findLastJobInJobInstance() {
      //String nomJob = "indexCompositesJob";
      //String nomJob = "clearEventJob";
      String nomJob = "indexCounterJob";
      String[] colonnes = { "jobInstanceId", "jobInstanceName" };
      Long idJob = Long.valueOf(-1);
      String rowKey = "";
      int compteurJob = 0;
      
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobInstance").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(10000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            //LOGGER.debug("colonne {} : {}", new String[] { columnName.getName(), new String(columnName.getValue())});
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)) {
               // un job du bon nom a ete trouve
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               Long idJobCourant = convertByteToLong(columnId.getValue());
               //LOGGER.debug("colonne {} : {}", new String[] { columnId.getName(), idJobCourant.toString()});
               if ((columnId != null) && (idJob < idJobCourant)) {
                  idJob = idJobCourant;
                  rowKey = bytesToHex(row.getKey());
               }
            }
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
      LOGGER.debug("Id du job trouve : {}", idJob);
      LOGGER.debug("Key de la row trouve : {}", rowKey);
   }
   
   @Test
   public void findLast5JobInJobInstance() {
      //String nomJob = "indexCompositesJob";
      //String nomJob = "systemLogsArchiveJob";
      String nomJob = "clearEventJob";
      String[] colonnes = { "jobInstanceId", "jobInstanceName" };
      int compteurJob = 0;
      List<Long> idJobs = new ArrayList<Long>();
      
      
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobInstance").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            //LOGGER.debug("colonne {} : {}", new String[] { columnName.getName(), new String(columnName.getValue())});
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)) {
               // un job du bon nom a ete trouve
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               Long idJobCourant = convertByteToLong(columnId.getValue());
               //LOGGER.debug("colonne {} : {}", new String[] { columnId.getName(), idJobCourant.toString()});
               if (columnId != null) {
                  idJobs.add(idJobCourant);
               }
            }
            compteurJob++;
         }
      }
      
      Collections.sort(idJobs, new Comparator<Long>() {
         @Override
         public int compare(Long o1, Long o2) {
            return o2.compareTo(o1);
         }
         
      });
      int compteurResultat = 0;
      for (Long id : idJobs) {
         LOGGER.debug("Id du job trouve : {}", id);
         compteurResultat++;
         if (compteurResultat > 4) {
            break;
         }
      }
      
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
   }
   
   @Test
   public void getJobInstanceById() {
      String[] colonnes = { "jobInstanceId", "jobInstanceName" };
      Long idJob = Long.valueOf(6099);
      String rowKey = "";
      String jobName = "";
      int compteurJob = 0;
      
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobInstance").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(10000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            //LOGGER.debug("colonne {} : {}", new String[] { columnName.getName(), new String(columnName.getValue())});
            HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
            Long idJobCourant = convertByteToLong(columnId.getValue());
            //LOGGER.debug("colonne {} : {}", new String[] { columnId.getName(), idJobCourant.toString()});
            if ((columnId != null) && (idJob.longValue() == idJobCourant.longValue())) {
               idJob = idJobCourant;
               rowKey = bytesToHex(row.getKey());
               jobName = StringSerializer.get().fromBytes(columnName.getValue());
               //break;
               LOGGER.debug("Id du job trouve : {}", idJob);
               LOGGER.debug("Job trouve : {}", jobName);
               LOGGER.debug("Key de la row trouve : {}", rowKey);
            }
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
      //LOGGER.debug("Id du job trouve : {}", idJob);
      //LOGGER.debug("Job trouve : {}", jobName);
      //LOGGER.debug("Key de la row trouve : {}", rowKey);
   }
   
   @Test
   public void findLastJobInJobExecution() {
      //String nomJob = "indexCompositesJob";
      //String nomJob = "documentLogsArchiveJob";
      String nomJob = "indexCounterJob";
      String[] colonnes = { "jobInstanceId", "jobInstanceName" };
      Long idJob = Long.valueOf(-1);
      String rowKey = "";
      Long idJobExec = Long.valueOf(-1);
      int compteurJob = 0;
      
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
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
            //LOGGER.debug("colonne {} : {}", new String[] { columnName.getName(), new String(columnName.getValue())});
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)) {
               // un job du bon nom a ete trouve
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               Long idJobCourant = convertByteToLong(columnId.getValue());
               //LOGGER.debug("colonne {} : {}", new String[] { columnId.getName(), idJobCourant.toString()});
               if ((columnId != null) && (idJob < idJobCourant)) {
                  idJob = idJobCourant;
                  rowKey = bytesToHex(row.getKey());
                  idJobExec = convertByteToLong(row.getKey());
               }
            }
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
      LOGGER.debug("Id de l'instance du job trouve : {}", idJob);
      LOGGER.debug("Key de la row trouve : {}", rowKey);
      LOGGER.debug("Id de l'execution du job trouve : {}", idJobExec);
   }
   
   @Test
   public void getJobExecutionById() {
      String[] colonnes = { "jobInstanceId", "jobInstanceName", "exitCode", "exitMessage", "createTime", "endTime", "executionContext" };
      int[] convertion = { 0, 1, 1, 1, 2, 2, 1 };
      Long idJobExec = Long.valueOf(6112);
            
      byte[] idJobAChercher = convertLongToByte(idJobExec);
      
      LOGGER.debug("Recuperation du job {}", idJobExec);
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
       SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("JobExecution").setKey(idJobAChercher);
      queryDocubase.setColumnNames(colonnes);
      QueryResult<ColumnSlice<String, byte[]>> resultDocubase = queryDocubase.execute();
      if (resultDocubase != null && resultDocubase.get() != null) {
         ColumnSlice<String, byte[]> columnSlice = resultDocubase.get();
         if (columnSlice != null && columnSlice.getColumns() != null && !columnSlice.getColumns().isEmpty()) {
            int compteur = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            for (String nomColonne : colonnes) {
               HColumn<String, byte[]> column = columnSlice.getColumnByName(nomColonne);
               String valeur;
               if (convertion[compteur] == 0) {
                  valeur = bytesToHex(column.getValue());
               } else if (convertion[compteur] == 1) {
                  valeur = new String(column.getValue());
               } else {
                  Long valLong = convertByteToLong(column.getValue());
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTimeInMillis(valLong);
                  valeur = formatter.format(calendar.getTime());
               }
               LOGGER.debug("{} : {}", new Object[] { column.getName(), valeur});
               compteur++;
            }
         } else {
            LOGGER.debug("Le job {} n'a pas ete trouve dans la liste des jobs execute", idJobExec);
         }
      }
   }
   
   @Test
   public void getStepsExecutionByIdJobExec() {
      String nomJob = "indexCompositesJob";
      String[] colonnes = { "jobExecutionId", "jobInstanceName", "stepId", "stepName", "exitCode", "exitMessage", "executionContext", "startTime", "endTime" };
      int[] convertion = { 3, 1, 3, 1, 1, 1, 1, 2, 2 };
      
      Long idJobExec = Long.valueOf(2928);
      // TODO : bizarre l'id du job execution du step execution n'est pas le meme que celui du jobexecution ou du jobinstance
      
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("StepExecution").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            //HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobExecutionId");
            //LOGGER.debug("{} - {}", convertByteToLong(columnId.getValue()), idJobExec);
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)
                  /*&& columnId != null && convertByteToLong(columnId.getValue()).equals(idJobExec)*/) {
               int compteur = 0;
               SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
               for (String nomColonne : colonnes) {
                  HColumn<String, byte[]> column = row.getColumnSlice().getColumnByName(nomColonne);
                  String valeur;
                  if (column != null) {
                     if (convertion[compteur] == 0) {
                        valeur = bytesToHex(column.getValue());
                     } else if (convertion[compteur] == 1) {
                        valeur = new String(column.getValue());
                     } else if (convertion[compteur] == 2) {
                        Long valLong = convertByteToLong(column.getValue());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(valLong);
                        valeur = formatter.format(calendar.getTime());
                     } else {
                        Long valLong = convertByteToLong(column.getValue());
                        valeur = valLong.toString();
                     }
                  } else { 
                     valeur = "colonne non présente";
                  }
                  LOGGER.debug("{} : {}", new Object[] { nomColonne, valeur});
                  compteur++;
               }
            }
         }
      }
   }
   
   @Test
   public void getStatusJobsWithDfce() throws NoSuchDfceJobException, NoSuchDfceJobExecutionException {
      //String nomJob = "indexCompositesJob";
      //String nomJob = "systemLogsArchiveJob";
      String nomJob = "clearEventJob";
      //String nomJob = "indexCounterJob";
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      JobAdministrationService jobAdminService = serviceProvider.getJobAdministrationService();
      
      List<Long> jobs = jobAdminService
         .getJobInstances(nomJob, 0, 5);
      
      List<Long> jobsExec = new ArrayList<Long>();
      
      // recupere 5000 jobs dans la cf jobexecution
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobExecution").setKeys(null, null);
      rangeQueryDocubase.setColumnNames("jobInstanceId");
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[],String,byte[]>> resultDocubase = rangeQueryDocubase.execute();
      
      for (Long idJob : jobs) {
         Long idJobExec = null;
         // recherche de l'id du job (job execution) par l'id du job
         LOGGER.debug("Recherche de l'execution du job {}", idJob);
         if (resultDocubase != null && resultDocubase.get() != null) {
            Iterator<Row<byte[], String, byte[]>> iterateur = resultDocubase.get().iterator();
            while (iterateur.hasNext()) {
               Row<byte[], String, byte[]> row = iterateur.next();
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               if (columnId != null) {
                  Long idJobInstance = convertByteToLong(columnId.getValue());
                  if (idJobInstance.longValue() == idJob.longValue()) {
                     idJobExec = convertByteToLong(row.getKey());
                  }
               }
            } 
         }
         
         if (idJobExec == null) {
            LOGGER.debug("Job non trouve {}", idJob);
         }
         
         jobsExec.add(idJobExec);
      }
   
      for (Long idJobExec : jobsExec) {
         if (idJobExec != null) {
            String resultatJob = jobAdminService.getSummary(idJobExec.longValue());
            LOGGER.debug("Resultat du job : {}", resultatJob);
            LOGGER.debug("Statut du job : {}", getAttributFromSpringBatch(resultatJob, "status"));
         }
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void getRowKeyCassandra() {
      Long idJobExec = Long.valueOf(4105);
      Long idJobInstance = Long.valueOf(4098);
      String[] colonnes = { "jobInstanceId", "jobInstanceName" };
      
      String rowKeyJobExec = bytesToHex(convertLongToByte(idJobExec));
      
      String rowKeyJobInstance = "";
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobInstance").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
            if (columnId != null) {
               Long idJobCourant = convertByteToLong(columnId.getValue());
               //LOGGER.debug("colonne {} : {}", new String[] { columnName.getName(), new String(columnName.getValue())});
               if (idJobCourant.longValue() == idJobInstance.longValue()) {
                  rowKeyJobInstance = bytesToHex(row.getKey());
               }
            }
         }
      }
      
      LOGGER.debug("Row JobExecution : {}", rowKeyJobExec);
      LOGGER.debug("Row JobInstance : {}", rowKeyJobInstance);
   }
   
   @Test
   public void getStateDailyJobsDfce() {
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      RangeSlicesQuery<String,String,Date> queryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), DateSerializer.get());
      queryDocubase.setColumnFamily("Jobs");
      queryDocubase.setColumnNames("lastSuccessfullRunDate", "launchDate");
      queryDocubase.setKeys(null, null);
      QueryResult<OrderedRows<String, String, Date>> resultDocubase = queryDocubase.execute();
      if (resultDocubase != null && resultDocubase.get() != null) {
         Iterator<Row<String, String, Date>> iterateur = resultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<String, String, Date> row = iterateur.next();
            
            Date dateLastRun = null;
            Date dateLastRunWithTime = null;
            if (row.getColumnSlice() != null && row.getColumnSlice().getColumnByName("lastSuccessfullRunDate") != null) {
               HColumn<String, Date> colonne = row.getColumnSlice().getColumnByName("lastSuccessfullRunDate");
               dateLastRun = new DateTime(colonne.getValue().getTime()).withTimeAtStartOfDay().toDate();
               dateLastRunWithTime = new DateTime(colonne.getValue().getTime()).toDate();
            }
            
            String typeJob = "";
            int nbJour = 0;
            boolean enRetard = false;
            if ("DOCUMENT_DAILY_LOG_JOB".equals(row.getKey())) {
               typeJob = "Archivage des événements sur les documents";
               nbJour = 1;
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               enRetard = dateLastRun.before(date);
            } else if ("SYSTEM_DAILY_LOG_JOB".equals(row.getKey())) {
               typeJob = "Archivage des événements système";
               nbJour = 1;
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               enRetard = dateLastRun.before(date);
            } else if ("DOCUMENT_EVENTS_PURGE_JOB".equals(row.getKey())) {
               typeJob = "Purges des événements sur les documents";
               nbJour = 30;
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               enRetard = dateLastRun.before(date);
            } else if ("SYSTEM_EVENTS_PURGE_JOB".equals(row.getKey())) {
               typeJob = "Purges des événements système";
               nbJour = 30;
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               enRetard = dateLastRun.before(date);
            } else if ("LIFE_CYCLE_JOB".equals(row.getKey())) {
               typeJob = "Traitement du cycle de vie";
               // pas de gestion de la frequence de ce job
               enRetard = false;
            } else if ("INDEX_COUNTER_JOB".equals(row.getKey())) {
               typeJob = "Mise à jour des compteurs d'index";
               nbJour = 1;
               enRetard = false;
            } else if ("PROCESS_DOC_STATISTICS_JOB".equals(row.getKey())) {
               typeJob = "Calcul des statistiques des documents";
               nbJour = 1;
               enRetard = false;
            } else if (row.getKey().startsWith("MANAGE_RANGE_INDEX_JOB")) {
               /*typeJob = "Gestion des index de type range";
               if (row.getKey().indexOf("|") != -1) {
                  String indexName = row.getKey().split("\\|")[2];
                  typeJob += " (" + indexName + ")";
               }
               // pas de gestion de la frequence de ce job
               enRetard = false;*/
               
               // on ne va pas tracer les jobs de splits
               continue;
            }
            
            if (!enRetard) {
               LOGGER.info("{} : {}", new String[] { typeJob, formatter.format(dateLastRunWithTime) });
            } else {
               
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               Duration difference = new Duration(new DateTime(dateLastRun), new DateTime(date));
               LOGGER.error("{} : {} -> en retard de {} jours", new String[] { typeJob, formatter.format(dateLastRun), Long.toString(difference.getStandardDays())});
            }
         }
         
         // gestion du cas particulier du job indexCounterJob
         /*RangeSlicesQuery<String,String,Date> queryJobInstance = HFactory.createRangeSlicesQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), DateSerializer.get());
         queryJobInstance.setColumnFamily("JobInstance");
         queryJobInstance.setColumnNames("jobInstanceId");
         queryJobInstance.setKeys(null, null);
         queryJobInstance.setRowCount(10000);
         QueryResult<OrderedRows<String, String, Date>> resultJobInstance = queryJobInstance.execute();
         if (resultJobInstance != null && resultJobInstance.get() != null) {
            Iterator<Row<String, String, Date>> iterateurJobInstance = resultJobInstance.get().iterator();
            
            Row<String, String, Date> row = null;
            Date dateLastRun = null;
            while (iterateurJobInstance.hasNext()) {
               row = iterateurJobInstance.next();
               if (row.getKey().contains("indexCounterJob") && row.getColumnSlice() != null && row.getColumnSlice().getColumnByName("jobInstanceId")!=null) {
                  // recupere la colonne
                  HColumn<String, Date> colonne = row.getColumnSlice().getColumnByName("jobInstanceId");
                  Date dateJob = new DateTime(colonne.getClock() / 1000).withTimeAtStartOfDay().toDate();
                  if (dateLastRun == null || dateJob.after(dateLastRun)) {
                     dateLastRun = (Date) dateJob.clone();
                  }
               }
            }
            
            
            if (dateLastRun != null)  {
               String typeJob = "Mise à jour des compteurs d'index";
               int nbJour = 2;
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               boolean enRetard = dateLastRun.before(date);
               
               if (!enRetard) {
                  LOGGER.info("{} : {}", new String[] { typeJob, formatter.format(dateLastRun) });
               } else {
                  
                  long ecart = new DateTime().withTimeAtStartOfDay().toDate().getTime() - dateLastRun.getTime() - (nbJour * 24 * 60 * 60 * 1000);
                  Duration difference = new Duration(ecart);
                  LOGGER.error("{} : {} -> en retard de {} jours", new String[] { typeJob, formatter.format(dateLastRun), Long.toString(difference.getStandardDays())});
               }
            }
         }*/
      }
   }
   
   @Test
   public void getStateDailyJobsSae() {
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      Keyspace keyspaceSAE = getKeyspaceSaeFromKeyspace();
      
      SliceQuery<String,String,byte[]> querySAE = HFactory.createSliceQuery(keyspaceSAE, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      querySAE.setColumnFamily("Parameters");
      querySAE.setKey("parametresTracabilite");
      querySAE.setRange(null, null, false, 100); // slice sur maximum 100 colonnes
      QueryResult<ColumnSlice<String, byte[]>> resultSAE = querySAE.execute();
      if (resultSAE != null && resultSAE.get() != null) {
         
         Map<String, Object[]> jobsSAE = new HashMap<String, Object[]>();
         for (HColumn<String, byte[]> colonne : resultSAE.get().getColumns()) {
            if (colonne.getName().startsWith("PURGE_")) {
               String jobName = colonne.getName().substring(0, colonne.getName().lastIndexOf("_"));
               if (!jobsSAE.containsKey(jobName)) {
                  jobsSAE.put(jobName, new Object[2]);
               }
               if (colonne.getName().endsWith("_DATE")) {
                  Date lastRun = (Date) ObjectSerializer.get().fromBytes(colonne.getValue());
                  jobsSAE.get(jobName)[0] = lastRun;
               } else if (colonne.getName().endsWith("_DUREE")) {
                  Integer duree = (Integer) ObjectSerializer.get().fromBytes(colonne.getValue());
                  jobsSAE.get(jobName)[1] = duree;
               }
               
            } else if ("JOURNALISATION_EVT_DATE".equals(colonne.getName())) {
               Date lastRun = (Date) ObjectSerializer.get().fromBytes(colonne.getValue());
               jobsSAE.put("JOURNALISATION_EVT", new Object[] { lastRun, Integer.valueOf(1) });
            }
            
         }
         
         
         for (String jobName : jobsSAE.keySet()) {   
            Date valeur = null;
            String typeJob = "";
            int nbJour = 0;
            boolean enRetard = false;
            if ("JOURNALISATION_EVT".equals(jobName)) {
               typeJob = "Journalisation des événements du SAE";
               nbJour = (Integer) jobsSAE.get(jobName)[1];
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               valeur = (Date) jobsSAE.get(jobName)[0];
               enRetard = valeur.before(date);
            } else if ("PURGE_EVT".equals(jobName)) {
               typeJob = "Purge du journal des événements du SAE";
               nbJour = (Integer) jobsSAE.get(jobName)[1];
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               valeur = (Date) jobsSAE.get(jobName)[0];
               enRetard = valeur.before(date);
            } else if ("PURGE_EXPLOIT".equals(jobName)) {
               typeJob = "Purge du registre d'exploitation";
               nbJour = (Integer) jobsSAE.get(jobName)[1];
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               valeur = (Date) jobsSAE.get(jobName)[0];
               enRetard = valeur.before(date);
            } else if ("PURGE_SECU".equals(jobName)) {
               typeJob = "Purges du registre de sécurité";
               nbJour = (Integer) jobsSAE.get(jobName)[1];
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               valeur = (Date) jobsSAE.get(jobName)[0];
               enRetard = valeur.before(date);
            } else if ("PURGE_TECH".equals(jobName)) {
               typeJob = "Purge du registre de surveillance technique";
               nbJour = (Integer) jobsSAE.get(jobName)[1];
               Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
               valeur = (Date) jobsSAE.get(jobName)[0];
               enRetard = valeur.before(date);
            } 
            
            if (StringUtils.isNotEmpty(typeJob)) {
               if (!enRetard) {
                  LOGGER.info("{} : {}", new String[] { typeJob, formatter.format(valeur) });
               } else {
                  
                  Date date = new DateTime().withTimeAtStartOfDay().minusDays(nbJour).toDate();
                  Duration difference = new Duration(new DateTime(valeur), new DateTime(date));
                  LOGGER.error("{} : {} -> en retard de {} jours", new String[] { typeJob, formatter.format(valeur), Long.toString(difference.getStandardDays())});
               }
            }
         }
      }
      
      // verifie la purge de la pile des travaux
      String typeJob = "Purge de la pile des travaux";
      RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspaceSAE, UUIDSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeSlicesQuery.setColumnFamily("JobRequest");
      rangeSlicesQuery.setRange("", "", false, 100);
      rangeSlicesQuery.setRowCount(200000);
      QueryResult<OrderedRows<UUID, String, byte[]>> queryResult = rangeSlicesQuery
            .execute();
      Date minDate = new DateTime().toDate();
      int compteurJobs = 0;
      for (Row<UUID, String, byte[]> row : queryResult.get().getList()) {
         if (!row.getColumnSlice().getColumns().isEmpty()) {
            HColumn<String, byte[]> colDateCreation = row.getColumnSlice().getColumnByName("creationDate");
            Date dateCreation = DateSerializer.get().fromBytes(colDateCreation.getValue());
            if (dateCreation.before(minDate)) {
               minDate = (Date) dateCreation.clone();
            }
            compteurJobs++;
         }
      }
      Date date = new DateTime().withTimeAtStartOfDay().minusDays(30).toDate();
      if (minDate.before(date)) {
         Duration difference = new Duration(new DateTime(minDate), new DateTime(date));
         LOGGER.error("{} : {} -> en retard de {} jours (reste {} jobs)", new String[] { typeJob, formatter.format(minDate), Long.toString(difference.getStandardDays()) , Integer.toString(compteurJobs) });
      } else {
         LOGGER.info("{} : {} -> reste {} jobs", new String[] { typeJob, formatter.format(minDate), Integer.toString(compteurJobs) });
      }
   }
   
   @Test
   public void getJobRequestASupprimer() throws IOException {
      
      FileOutputStream out = new FileOutputStream(new File("c:/divers/jobs-piles-travaux-prod.csv"));
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date maxDate = new DateTime().withTimeAtStartOfDay().minusDays(31).toDate();
      
      Keyspace keyspaceSAE = getKeyspaceSaeFromKeyspace();
      
      RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspaceSAE, UUIDSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeSlicesQuery.setColumnFamily("JobRequest");
      rangeSlicesQuery.setRange("", "", false, 100);
      AllRowsIterator<UUID, String, byte[]> rowIterator = new AllRowsIterator<UUID, String, byte[]>(rangeSlicesQuery);
      
      Date minDate = new DateTime().toDate();
      int compteurJobs = 0;
      int compteurJobsASuppr = 0;
      int compteurTombstone = 0;
      while (rowIterator.hasNext()) {
         Row<UUID, String, byte[]> row = rowIterator.next();
         if (!row.getColumnSlice().getColumns().isEmpty()) {
            HColumn<String, byte[]> colDateCreation = row.getColumnSlice().getColumnByName("creationDate");
            Date dateCreation = DateSerializer.get().fromBytes(colDateCreation.getValue());
            if (dateCreation.before(minDate)) {
               minDate = (Date) dateCreation.clone();
            }
            if (dateCreation.before(maxDate)) {
               compteurJobsASuppr++;
            }
            compteurJobs++;
            
            HColumn<String, byte[]> colDateFin = row.getColumnSlice().getColumnByName("endingDate");
            HColumn<String, byte[]> colEtat = row.getColumnSlice().getColumnByName("state");
            
            StringBuffer buffer = new StringBuffer();
            buffer.append(row.getKey());
            buffer.append(';');
            buffer.append(formatter.format(dateCreation));
            buffer.append(';');
            if (colDateFin != null && colDateFin.getValue() != null) {
               buffer.append(formatter.format(DateSerializer.get().fromBytes(colDateFin.getValue())));
            } else {
               buffer.append("null");
            }
            buffer.append(';');
            buffer.append(StringSerializer.get().fromBytes(colEtat.getValue()));
            buffer.append("\n");
            
            out.write(buffer.toString().getBytes());
         } else {
            compteurTombstone++;
         }
      }
      
      System.out.println("minDate: " + formatter.format(minDate));
      System.out.println("compteurJobs: " + compteurJobs);
      System.out.println("compteurJobsASuppr: " + compteurJobsASuppr);
      System.out.println("compteurJobsTombstoné: " + compteurTombstone);
      
      out.close();
   }
}
