package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.util.HashMap;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Dump des données de spring batch
 * 
 */
public class BatchDumpTest {
   Keyspace keyspace;
   Cluster cluster;
   PrintStream sysout;
   Dumper dumper;

   @SuppressWarnings("serial")
   @Before
   public void init() throws Exception {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>() {
         {
            put("username", "root");
         }
         {
            put("password", "regina4932");
         }
      };
      String servers;

      // servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp31saecas1.cer31.recouv:9160";
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      // servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      cluster = HFactory.getOrCreateCluster("Docubase", hostConfigurator);
      keyspace = HFactory.createKeyspace("Batch", cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);

      sysout = new PrintStream(System.out, true, "UTF-8");

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      // sysout = new PrintStream("c:/temp/out.txt");
      dumper = new Dumper(keyspace, sysout);
   }

   @Test
   public void testDumpJobInstance() throws Exception {
      dumper.printKeyInLong = true;
      dumper.dumpCF("JobInstance", 25);
   }

   @Test
   public void testDumpJobInstancesByName() throws Exception {
      dumper.printKeyInLong = false;
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("JobInstancesByName", 25);
   }

   @Test
   public void testDumpJobExecution() throws Exception {
      dumper.printKeyInLong = true;
      dumper.printColumnNameInHex = false;
      dumper.dumpCF("JobExecution", 25);
   }

   @Test
   public void testDumpJobStep() throws Exception {
      dumper.maxValueLenght = 10000;
      dumper.printKeyInLong = true;
      dumper.dumpCF("JobStep", 25);
   }

   @Test
   public void testDumpJobInstanceToJobExecution() throws Exception {
      dumper.printKeyInLong = true;
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("JobInstanceToJobExecution", 25);
   }

   @Test
   public void testDumpJobExecutions() throws Exception {
      dumper.printKeyInLong = false;
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("JobExecutions", 25);
   }

   @Test
   public void testDumpJobExecutionsRunning() throws Exception {
      dumper.printKeyInLong = false;
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("JobExecutionsRunning", 25);
   }

   @Test
   public void testDumpJobExecutionToJobStep() throws Exception {
      dumper.printKeyInLong = true;
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("JobExecutionToJobStep", 25);
   }

   @Test
   public void testDumpJobSteps() throws Exception {
      dumper.printKeyInLong = false;
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("JobSteps", 25);
   }

   @Test
   public void testDumpSequences() throws Exception {
      dumper.printKeyInLong = false;
      dumper.printColumnNameInHex = false;
      dumper.dumpCF("Sequences", 25);
   }

   @Test
   public void testDumpAll() throws Exception {
      sysout.println("\nJobInstance");
      testDumpJobInstance();
      sysout.println("\nJobInstancesByName");
      testDumpJobInstancesByName();
      sysout.println("\nJobExecution");
      testDumpJobExecution();
      sysout.println("\nJobStep");
      testDumpJobStep();
   }

   @Test
   @Ignore
   /**
    * Attention : supprime toutes les données
    */
   public void truncate() throws Exception {
      truncate("JobExecution");
      truncate("JobExecutions");
      truncate("JobExecutionsRunning");
      truncate("JobExecutionToJobStep");
      truncate("JobInstance");
      truncate("JobInstancesByName");
      truncate("JobInstanceToJobExecution");
      truncate("JobStep");
      truncate("JobSteps");
      truncate("Sequences");
   }

   /**
    * Vide le contenu d'une famille de colonnes
    * 
    * @param cfName
    *           Nom de la famille de colonnes à vider
    * @throws Exception
    */
   private void truncate(String cfName) throws Exception {
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      CqlQuery<byte[], byte[], byte[]> cqlQuery = new CqlQuery<byte[], byte[], byte[]>(
            keyspace, bytesSerializer, bytesSerializer, bytesSerializer);
      String query = "truncate " + cfName;
      cqlQuery.setQuery(query);
      QueryResult<CqlRows<byte[], byte[], byte[]>> result = cqlQuery.execute();
      dumper.dumpCqlQueryResult(result);
   }
}
