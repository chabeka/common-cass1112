package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.UUID;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Dump de données Cassandra du Keyspace SAE
 */
public class SaeDumpTest {

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

      //servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp31saecas1.cer31.recouv:9160";
      //servers = "cer69-saeint3.cer69.recouv";
      
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      servers = "cer69imageint9.cer69.recouv:9160";
      
      // INTEGRATION CLIENTE
      // -------------------
      //servers = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
      //servers = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";

      //servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
       //servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      // servers = "cer69-saeint3.cer69.recouv:9160";
      //servers = "cnp69devgntcas1.gidn.recouv:9160";
      
      // GIIN GNS
      // --------
      //servers = "hwi31ginsaecas1.cer31.recouv:9160,hwi31ginsaecas2.cer31.recouv:9160";
      
      // GIIN GNT
      // --------
      //servers = "cnp31gingntcas1.cer31.recouv:9160";
      
      // CSPP GNS
      // --------
      //servers = "cnp3saecvecas1.cve.recouv";
      
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      cluster = HFactory.getOrCreateCluster("SAE", hostConfigurator);
      keyspace = HFactory.createKeyspace("SAE", cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);

      sysout = new PrintStream(System.out, true, "UTF-8");

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      // sysout = new PrintStream("c:/temp/out.txt");
      dumper = new Dumper(keyspace, sysout);
   }

   @After
   public void close() {
      // cluster.getConnectionManager().shutdown();
      HFactory.shutdownCluster(cluster);
   }

   @Test
   @Ignore
   /**
    * Attention : supprime toutes les données
    */
   public void truncate() throws Exception {

      // truncate("JobRequest");
      // truncate("JobsQueue");
      // truncate("JobHistory");

      // truncate("Parameters");

      // truncate("TraceDestinataire");

//      truncate("TraceRegTechnique");
//      truncate("TraceRegTechniqueIndex");
//      truncate("TraceRegExploitation");
//      truncate("TraceRegExploitationIndex");
//      truncate("TraceRegSecurite");
//      truncate("TraceRegSecuriteIndex");
//      truncate("TraceJournalEvt");
//      truncate("TraceJournalEvtIndex");
      
//      truncate("Rnd");

   }

   /**
    * Vide le contenu d'une famille de colonnes
    * 
    * @param cfName
    *           Nom de la famille de colonnes à vider
    * @throws Exception
    */
   protected void truncate(String cfName) throws Exception {
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      CqlQuery<byte[], byte[], byte[]> cqlQuery = new CqlQuery<byte[], byte[], byte[]>(
            keyspace, bytesSerializer, bytesSerializer, bytesSerializer);
      String query = "truncate " + cfName;
      cqlQuery.setQuery(query);
      QueryResult<CqlRows<byte[], byte[], byte[]>> result = cqlQuery.execute();
      dumper.dumpCqlQueryResult(result);
   }

   @Test
   public void testDumpParameters() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("Parameters", 50);
   }

   @Test
   public void testDumpDroitActionUnitaire() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitActionUnitaire", 50);
   }

   @Test
   public void testDumpDroitContratService() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitContratService", 50);
   }

   @Test
   public void testDumpDroitPagm() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagm", 50);
   }

   @Test
   public void testDumpDroitPagma() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagma", 50);
   }

   @Test
   public void testDumpDroitPagmp() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagmp", 50);
   }

   @Test
   public void testDumpDroitPrmd() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPrmd", 50);
   }

   @Test
   public void testDumpJobExecution() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobExecution", 500);
   }

   @Test
   public void testDumpJobExecutionToJobStep() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobExecutionToJobStep", 500);
   }

   @Test
   public void testDumpJobRequest() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobRequest", 500, true);
   }

   @Test
   public void testDumpMetadatas() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("Metadata", 500);
   }
   
   @Test
   public void testDumpTraceDestinataire() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceDestinataire", 500);
   }

   @Test
   public void testDumpTraceRegExploitation() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceRegExploitation", 5000, true);
   }

   @Test
   public void testDumpTraceRegExploitationIndex() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceRegExploitationIndex", 1000, true);
   }

   @Test
   public void testDumpTraceRegSecurite() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceRegSecurite", 5000, true);
   }

   @Test
   public void testDumpTraceRegSecuriteIndex() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceRegSecuriteIndex", 1000, true);
   }

   @Test
   public void testDumpTraceRegTechnique() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceRegTechnique", 5000, true);
   }

   @Test
   public void testDumpTraceRegTechniqueIndex() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceRegTechniqueIndex", 1000, true);
   }

   @Test
   public void testDumpTraceJournalEvt() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceJournalEvt", 5000, true);
   }

   @Test
   public void testDumpTraceJournalEvtIndex() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceJournalEvtIndex", 5000, true);
   }
   
   @Test
   public void testDumpRnd() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("Rnd", 2500, true);
   }
   
   
   @Test
   public void deleteJobDansJobRequest() {
      
      UUID idJob = UUID.fromString("9965db20-68b7-11e3-a563-000c29aa49ae");
      
      Mutator<UUID> mutator = HFactory.createMutator(keyspace, UUIDSerializer
            .get());
      mutator.addDeletion(idJob, "JobRequest");
      mutator.execute();
      
   }

}
