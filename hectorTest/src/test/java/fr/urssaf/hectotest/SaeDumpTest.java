package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;

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
      final ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      final HashMap<String, String> credentials = new HashMap<String, String>() {
         {
            put("username", "root");
         }
         {
            put("password", "regina4932");
         }
      };
      String servers;

      // servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp31saecas1.cer31.recouv:9160";
      // servers = "cnp69pregnscas1:9160, cnp69pregnscas2:9160, cnp69pregnscas3:9160";
      // servers = "cer69-saeint3.cer69.recouv";
      // servers = "cer69-saeint3.cer69.recouv";

      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      // servers = "cer69imageint9.cer69.recouv:9160";
      // INTEGRATION CLIENTE
      // -------------------
      servers = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
      // servers = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";

      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      // servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160";
      // servers = "cer69-saeint3.cer69.recouv:9160";
      // servers = "cnp69devgntcas1.gidn.recouv:9160";
      // servers = "cnp69pregntcas1:9160, cnp69pregntcas2:9160";
      // servers = "hwi69dev2saecas1.gidn.recouv:9160, hwi69dev2saecas2.gidn.recouv:9160";
      // servers = "cnp69dev2gntcas1.gidn.recouv:9160, cnp69dev2gntcas1.gidn.recouv:9160";
      // servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160"; // Charge
      // servers = "cnp69miggntcas1.gidn.recouv:9160"; // Migration
      // servers = "cnp6gntcvecas1.cve.recouv:9160,cnp3gntcvecas1.cve.recouv:9160,cnp7gntcvecas1.cve.recouv:9160"; // Charge GNT

      // GIIN GNS
      // --------
      // servers = "hwi31ginsaecas1.cer31.recouv:9160,hwi31ginsaecas2.cer31.recouv:9160";

      // GIIN GNT
      // --------
      // servers = "cnp31gingntcas1.cer31.recouv:9160";

      // CSPP GNS
      // --------
      // servers = "cnp3saecvecas1.cve.recouv";

      final CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
                                                                                       servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      cluster = HFactory.getOrCreateCluster("SAE", hostConfigurator);
      keyspace = HFactory.createKeyspace("SAE",
                                         cluster,
                                         ccl,
                                         FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE,
                                         credentials);

      sysout = new PrintStream(System.out, true, "UTF-8");

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("d:/temp/out.txt");
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

      // truncate("TraceRegTechnique");
      // truncate("TraceRegTechniqueIndex");
      // truncate("TraceRegExploitation");
      // truncate("TraceRegExploitationIndex");
      // truncate("TraceRegSecurite");
      // truncate("TraceRegSecuriteIndex");
      // truncate("TraceJournalEvt");
      // truncate("TraceJournalEvtIndex");

      // truncate("Rnd");

   }

   /**
    * Vide le contenu d'une famille de colonnes
    * 
    * @param cfName
    *           Nom de la famille de colonnes à vider
    * @throws Exception
    */
   protected void truncate(final String cfName) throws Exception {
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final CqlQuery<byte[], byte[], byte[]> cqlQuery = new CqlQuery<>(
                                                                                             keyspace,
                                                                                             bytesSerializer,
                                                                                             bytesSerializer,
                                                                                             bytesSerializer);
      final String query = "truncate " + cfName;
      cqlQuery.setQuery(query);
      final QueryResult<CqlRows<byte[], byte[], byte[]>> result = cqlQuery.execute();
      dumper.dumpCqlQueryResult(result);
   }

   @Test
   public void testDumpParameters() throws Exception {
      dumper.printKeyInHex = false;
      dumper.deserializeValue = true;
      dumper.dumpCF("Parameters", 50);
   }

   @Test
   public void testUpdateParameters() throws Exception {
      System.out.println("Début");

      final ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<>(keyspace,
                                                                                                           "Parameters",
                                                                                                           StringSerializer.get(),
                                                                                                           StringSerializer.get());
      final ColumnFamilyUpdater<String, String> updater = template.createUpdater("parametresTracabilite");
      updater.setValue("PURGE_EVT_IS_RUNNING", false, ObjectSerializer.get());
      template.update(updater);

      /*
       * final ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<String, String>(keyspace,
       * "Parameters",
       * StringSerializer.get(),
       * StringSerializer.get());
       * final ColumnFamilyUpdater<String, String> updater = template.createUpdater("parametresTracabilite");
       * final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("14/11/2018");
       * updater.setValue("JOURNALISATION_EVT_DATE", date, ObjectSerializer.get());
       * updater.setValue("JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT", "c58c2de929d94171716e0114f81642e2bc169e10", ObjectSerializer.get());
       * updater.setValue("JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT", "460831ca-f58c-446d-8ca5-3f20cece8c21", ObjectSerializer.get());
       * template.update(updater);
       */

      /*
       * final String key = "parametresTracabilite";
       * final String cf = "Parameters";
       * final Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
       * final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("29/12/2014");
       * System.out.println("Date=" + date);
       * final HColumn<String, Object> col1 = HFactory.createColumn("JOURNALISATION_EVT_DATE", date, StringSerializer.get(), ObjectSerializer.get());
       * mutator.addInsertion(key, cf, col1);
       * final HColumn<String, Object> col2 = HFactory.createColumn("JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT",
       * "dbf6e57795a46bbc9c5a31f9edb7b9c57fc94e77",
       * StringSerializer.get(),
       * ObjectSerializer.get());
       * mutator.addInsertion(key, cf, col2);
       * final HColumn<String, Object> col3 = HFactory.createColumn("JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT",
       * "c7b3e63a-79df-40c6-adfa-33b79fb79cd5",
       * StringSerializer.get(),
       * ObjectSerializer.get());
       * mutator.addInsertion(key, cf, col3);
       * mutator.execute();
       * System.out.println("Ok");
       */
   }

   @Test
   public void testDumpCorrespondancesRnd() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("CorrespondancesRnd", 50);
   }

   @Test
   public void testDumpDictionary() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("Dictionary", 50);
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
   public void testDumpDroitFormatControlProfil() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitFormatControlProfil", 50);
   }

   @Test
   public void testDumpDroitPagm() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagm", 50);
   }

   @Test
   public void testDumpDroitPagm_saturne() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagm", "CS_SATURNE");
   }

   @Test
   public void testDumpDroitPagma() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagma", 2000);
   }

   @Test
   public void testDumpDroitPagmf() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagmf", 50);
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
   public void testDumpJobExecutions() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobExecutions", 500);
   }

   @Test
   public void testDumpJobExecutionsRunning() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobExecutionsRunning", 500);
   }

   @Test
   public void testDumpJobExecutionToJobStep() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobExecutionToJobStep", 500);
   }

   @Test
   public void testDumpJobHistory() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobHistory", 500);
   }

   @Test
   public void testDumpJobInstance() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobInstance", 500);
   }

   @Test
   public void testDumpJobInstanceToJobExecution() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobInstanceToJobExecution", 500);
   }

   @Test
   public void testDumpJobInstancesByName() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("JobInstancesByName", 500);
   }

   @Test
   public void testDumpJobRequest() throws Exception {
      dumper.printKeyInHex = true;
      dumper.dumpCF("JobRequest", 500, true);
   }

   @Test
   public void testDumpJobStep() throws Exception {
      dumper.printKeyInHex = true;
      dumper.dumpCF("JobStep", 500, true);
   }

   @Test
   public void testDumpMetadata() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("Metadata", 200, true);
   }

   @Test
   public void testDumpReferentielFormat() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("ReferentielFormat", 200, true);
   }

   @Test
   public void testDumpSequences() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("Sequences", 200, true);
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
   public void testDumpTraceTraceJournalEvtIndexDoc() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("TraceJournalEvtIndexDoc", 500, true);
   }

   @Test
   public void testDumpRnd() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("Rnd", 2500, true);
   }

   @Test
   public void testDumpOneJobHistory() throws Exception {
      dumper.printKeyInHex = true;
      dumper.printColumnNameInHex = true;
      // byte[] key = ConvertHelper.hexStringToByteArray("b2737fb0-b6e4-11e4-af20-005056bf32d7".replace("-", ""));
      // byte[] key = ConvertHelper.hexStringToByteArray("80296690-b749-11e4-881e-005056bf1bac".replace("-", ""));
      final byte[] key = ConvertHelper.hexStringToByteArray("9773e750d87b11e4b053005056bf2081".replace("-", ""));
      dumper.dumpCF("JobHistory", key);
   }

   @Test
   public void testDumpJobsQueue() throws Exception {
      dumper.printKeyInHex = false;
      dumper.printColumnNameInHex = true;
      // dumper.dumpCF("JobsQueue", 300, false);
      final byte[] key = ConvertHelper.getBytesFromReadableUTF8String("jobsWaiting");
      dumper.dumpCF("JobsQueue", key, 70000);
   }

   @Test
   public void testDumpJobsQueueSlice() throws Exception {
      dumper.printKeyInHex = false;
      dumper.printColumnNameInHex = true;
      final UUID start = TimeUUIDUtils.getTimeUUID(new SimpleDateFormat("yyyy-mm-dd").parse("2015-02-20").getTime());
      final UUID end = TimeUUIDUtils.getTimeUUID(new SimpleDateFormat("yyyy-mm-dd").parse("2036-02-18").getTime());
      dumper.dumpCF_slice("JobsQueue", TimeUUIDUtils.asByteArray(start), TimeUUIDUtils.asByteArray(end), 500);
   }

   @Test
   public void deleteJobDansJobRequest() {

      final UUID idJob = UUID.fromString("9965db20-68b7-11e3-a563-000c29aa49ae");

      final Mutator<UUID> mutator = HFactory.createMutator(keyspace,
                                                           UUIDSerializer
                                                                         .get());
      mutator.addDeletion(idJob, "JobRequest");
      mutator.execute();

   }

   @Test
   public void deleteDansJobsQueue() {

      final Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
      mutator.addDeletion("hwi3gnscveappli1", "JobsQueue");
      mutator.addDeletion("hwi3gnscveappli2", "JobsQueue");
      mutator.addDeletion("hwi3gnscveappli3", "JobsQueue");
      mutator.addDeletion("hwi6gnscveappli1", "JobsQueue");
      mutator.addDeletion("hwi6gnscveappli2", "JobsQueue");
      mutator.addDeletion("hwi6gnscveappli3", "JobsQueue");
      mutator.addDeletion("hwi7gnscveappli1", "JobsQueue");
      mutator.addDeletion("hwi7gnscveappli2", "JobsQueue");
      mutator.addDeletion("hwi7gnscveappli3", "JobsQueue");
      mutator.execute();
   }

   @Test
   public void deleteDansJobsQueue_Paris() {

      final Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
      mutator.addDeletion("hwi75saeappli1", "JobsQueue");
      mutator.addDeletion("hwi75saeappli2", "JobsQueue");
      mutator.addDeletion("hwi75saeappli3", "JobsQueue");
      mutator.execute();
   }

   @Test
   public void deleteWaitingJob() {
      final Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
      mutator.addDeletion("jobsWaiting", "JobsQueue", ConvertHelper.hexStringToByteArray("a70309c0de5811e496c2005056bf4abb"), BytesArraySerializer.get());
      mutator.addDeletion("jobsWaiting", "JobsQueue", ConvertHelper.hexStringToByteArray("d43cf310de5811e496c2005056bf4abb"), BytesArraySerializer.get());
      // mutator.addDeletion("jobsWaiting", "JobsQueue");
      mutator.execute();
   }

}
