package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.util.HashMap;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ExhaustedPolicy;
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
public class JobRequestDumpTest {
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
      servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160";	// Charge
      //servers = "localhost:9160";
      //servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp31saecas1.cer31.recouv:9160";
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      //servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      //servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";

      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
            
      cluster = HFactory.getOrCreateCluster("Docubase", hostConfigurator);
      keyspace = HFactory.createKeyspace("SAE", cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);

      sysout = new PrintStream(System.out, true, "UTF-8");

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("c:/temp/out.txt");
      dumper = new Dumper(keyspace, sysout);
   }

   @Test
   public void testDumpJobRequest() throws Exception {
      dumper.printKeyInHex = true;
      dumper.dumpCF("JobRequest", 15000);
   }

   @Test
   public void testDumpJobsQueue() throws Exception {
      dumper.printKeyInHex = false;
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("JobsQueue", 250);
   }
   
   @Test
   public void testDumpJobHistory() throws Exception {
      dumper.printKeyInHex = true;
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("JobHistory", 250);
   }
   
   @Test
   public void testDumpParameters() throws Exception {
      dumper.printKeyInHex = false;
      dumper.printColumnNameInHex = false;
      dumper.dumpCF("Parameters", 25);
   }


   @Test
   public void testDumpAll() throws Exception {
      sysout.println("\nJobRequest");
      testDumpJobRequest();
      sysout.println("\nJobsQueue");
      testDumpJobsQueue();
      sysout.println("\nJobHistory");
      testDumpJobHistory();
   }

   @Test
   @Ignore
   /**
    * Attention : supprime toutes les données
    */
   public void truncate() throws Exception {
      truncate("JobRequest");
      truncate("JobsQueue");
   }
   
   /**
    * Vide le contenu d'une famille de colonnes
    * @param cfName  Nom de la famille de colonnes à vider
    * @throws Exception
    */
   private void truncate(String cfName) throws Exception {
      BytesArraySerializer  bytesSerializer = BytesArraySerializer.get();
      CqlQuery<byte[],byte[],byte[]> cqlQuery = new CqlQuery<byte[],byte[],byte[]>(keyspace, bytesSerializer, bytesSerializer, bytesSerializer);
      String query = "truncate " + cfName;
      cqlQuery.setQuery(query);
      QueryResult<CqlRows<byte[],byte[],byte[]>> result = cqlQuery.execute();
      dumper.dumpCqlQueryResult(result);
   }
}
