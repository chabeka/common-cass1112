package fr.urssaf.hectotest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.model.IndexedSlicesQuery;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import me.prettyprint.hom.EntityManagerImpl;

/**
 * Dump de données cassandra
 */
public class DfceDumpTest {

   Keyspace systemKeyspace;

   Keyspace keyspace;

   Cluster cluster;

   PrintStream sysout;

   Dumper dumper;

   Dumper systemDumper;

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
      // servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
      // servers = "cnp69saecas4.cer69.recouv:9160, cnp69saecas5.cer69.recouv:9160, cnp69saecas6.cer69.recouv:9160";
      // servers = "cnp69gntcas1:9160, cnp69gntcas2:9160, cnp69gntcas3:9160";
      // servers = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
      // servers = "cnp69pregntcas1:9160, cnp69pregntcas2:9160";
      // servers = "cnp69givngntcas1:9160, cnp69givngntcas2:9160";
      // servers = "hwi69gincleasaecas1.cer69.recouv:9160,hwi69gincleasaecas2.cer69.recouv:9160";
      // servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; //Préprod
      // servers = "cnp69pprodsaecas6:9160"; //Préprod
      // servers = "cnp69pregnscas1.cer69.recouv:9160,cnp69pregnscas1.cer69.recouv:9160,cnp69pregnscas1.cer69.recouv:9160"; // Vrai préprod
      // servers = "10.213.82.56:9160";
      // servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160"; // Charge
      // servers = "cnp3gntcvecas1.cve.recouv:9160,cnp6gntcvecas1.cve.recouv:9160,cnp7gntcvecas1.cve.recouv:9160"; // Charge GNT
      // servers = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
      // servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.207.81.29:9160";
      // servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      // servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      // servers = "cer69-saeint3:9160";
      // servers = "cnp69devgntcas1.gidn.recouv:9160, cnp69devgntcas2.gidn.recouv:9160";
      // servers = "cnp69dev2gntcas1.gidn.recouv:9160, cnp69dev2gntcas2.gidn.recouv:9160";
      // servers = "cnp69miggntcas1.gidn.recouv:9160,cnp69miggntcas2.gidn.recouv:9160"; // Migration cassandra V2
      // servers = "cnp69dev2gntcas1.gidn.recouv:9160";
      // servers = "cnp69gingntcas1.cer69.recouv:9160,cnp69gingntcas2.cer69.recouv:9160,cnp69gingntcas3.cer69.recouv:9160";
      // servers = "cnp69intgntc1cas1.gidn.recouv:9160,cnp69intgntc1cas2.gidn.recouv:9160,cnp69intgntc1cas3.gidn.recouv:9160";
      // servers = "cnp69gingntc1cas1.cer69.recouv:9160,cnp69gingntc1cas2.cer69.recouv:9160,cnp69gingntc1cas3.cer69.recouv:9160";
      // servers = "cnp69gingntp1cas1.cer69.recouv:9160,cnp69gingntp1cas2.cer69.recouv:9160,cnp69gingntp1cas3.cer69.recouv:9160";
      servers = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";

      final CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      cluster = HFactory.getOrCreateCluster("myCluster", hostConfigurator);
      keyspace = HFactory.createKeyspace("dfce", cluster, ccl, FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);
      systemKeyspace = HFactory.createKeyspace("system", cluster, ccl, FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);

      sysout = new PrintStream(System.out, true, "UTF-8");

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("d:/temp/out.txt");
      dumper = new Dumper(keyspace, sysout);
      systemDumper = new Dumper(systemKeyspace, sysout);
   }

   @After
   public void close() {
      // cluster.getConnectionManager().shutdown();
      HFactory.shutdownCluster(cluster);
   }

   @Test
   public void testDumpAcl() throws Exception {
      dumper.dumpCF("acl", 100);
   }

   @Test
   public void testDumpActionByDay() throws Exception {
      dumper.dumpCF("action_by_day", 100);
   }

   @Test
   public void testDumpActiveSession() throws Exception {
      dumper.dumpCF("active_session", 100);
   }

   @Test
   public void testDumpAnnotations() throws Exception {
      dumper.dumpCF("annotations", 100);
   }

   @Test
   public void testDumpBase() throws Exception {
      dumper.dumpCF("base", 100);
   }

   @Test
   public void testDumpBatchCounter() throws Exception {
      dumper.dumpCF("batch_counter", 100);
      // Ne marche pas
   }

   @Test
   public void testDumpCompositeIndex() throws Exception {
      dumper.dumpCF("composite_index", 100);
   }

   @Test
   public void testDumpContentRepository() throws Exception {
      dumper.dumpCF("content_repository", 100);
   }

   @Test
   public void testDumpDictionary() throws Exception {
      dumper.dumpCF("dictionary", 100);
   }

   @Test
   public void testDumpDocEventByDate() throws Exception {
      dumper.dumpCF("doc_event_by_date", 50);
   }

   @Test
   public void testDumpDocEventByRegisterDate() throws Exception {
      dumper.dumpCF("doc_event_by_register_date", 50);
   }

   @Test
   public void testDumpDocInfo() throws Exception {
      dumper.printKeyInHex = true;
      dumper.dumpCF("doc_info", 100);
   }

   @Test
   public void testExtractOneDocInfo() throws Exception {
      // extractOneDocInfo("7ea35959-f23e-4563-8519-d4a7a23c807b");
      extractOneDocInfo("ff19c966-4ee2-4954-83ef-b46dee66763b");
      // extractOneDocInfo("645D0CBB-35EF-4AED-B380-F5BBB947AEF0");
      // extractOneDocInfo ("05A5CB97-196B-423C-9A3C-F438F160DD03");
      // extractOneDocInfo ("3E915A0A-3878-47B4-8225-666F1ECAB779");
      // extractOneDocInfo("1c577d7e-19bf-45b0-ae51-456b3ba084f8");
      // extractOneDocInfo("6fd809ec-8fd7-44f3-9d6f-ee655fa7e54a");
   }

   @Test
   public void testDumpDocStatistics() throws Exception {
      dumper.dumpCF("doc_statistics", 100);
   }

   @Test
   public void testDumpDocTimeSeries() throws Exception {
      dumper.dumpCF("doc_time_series", 150);
   }

   @Test
   public void testDumpDocumentLifeCycle() throws Exception {
      dumper.dumpCF("document_life_cycle", 150);
   }

   @Test
   public void testDumpDocuments() throws Exception {
      dumper.dumpCF("documents", 5);
   }

   @Test
   public void testDumpDocVersion() throws Exception {
      dumper.dumpCF("doc_version", 150);
   }

   @Test
   public void testDumpEncryptedKey() throws Exception {
      dumper.dumpCF("encrypted_key", 150);
   }

   @Test
   public void testDumpEvent() throws Exception {
      dumper.printKeyInHex = true;
      dumper.dumpCF("event", 150);
   }

   @Test
   public void testDumpExternalEvent() throws Exception {
      dumper.dumpCF("external_event", 100);
   }

   @Test
   public void testDumpGroup() throws Exception {
      dumper.dumpCF("group", 100);
   }

   @Test
   public void testDumpIndexableEntityEvent() throws Exception {
      dumper.dumpCF("indexable_entity_event", 100);
   }

   @Test
   public void testDumpIndexCounter() throws Exception {
      // dumper.dumpCF("index_counter", 2);
      // byte[] key = ConvertHelper.getBytesFromReadableUTF8String("\\x00\\x07SM_UUID\\x00\\x00\\x10u\\x05\\x93s9}F\\xf7\\x8eϷ\\xfe\\xa6#\\xea?\\x00\\x00\\x06INSERT\\x00");

      // C'est la clé correspondant à SM_UUID en prod et CSPP. En CSPP, la ligne est trop grosse.
      final byte[] key = ConvertHelper.getBytesFromReadableUTF8String("\\x00\\x07SM_UUID\\x00\\x00\\x10\\xf5s\\xae\\x93\\xacjF\\x15\\xa2;\\x15\\x0f\\xd6!\\xb5\\xa0\\x00\\x00\\x06INSERT\\x00");
      // byte[] key = ConvertHelper.hexStringToByteArray("0007534d5f55554944000010f573ae93ac6a4615a23b150fd621b5a0000006494e5345525400");
      // dumper.dumpCF("index_counter", key, 20);
      dumper.dumpCF("index_counter", 10);
   }

   @Test
   public void testDumpIndexReference() throws Exception {
      dumper.dumpCF("index_reference", 2000);
   }

   @Test
   public void testDump_job_execution_by_id() throws Exception {
      dumper.dumpCF("job_execution_by_id", 1000);
   }

   @Test
   public void testDump_job_execution_by_instance() throws Exception {
      dumper.dumpCF("job_execution_by_instance", 100);
   }

   @Test
   public void testDump_job() throws Exception {
      dumper.dumpCF("job", 100);
   }

   @Test
   public void testDump_job_instance_by_id() throws Exception {
      dumper.dumpCF("job_instance_by_id", 100);
   }

   @Test
   public void testDump_job_instance_by_name_and_id() throws Exception {
      dumper.dumpCF("job_instance_by_name_and_id", 100);
   }

   @Test
   public void testDump_job_instance_by_name_and_parameters() throws Exception {
      dumper.dumpCF("job_instance_by_name_and_parameters", 9000);
   }

   @Test
   public void testDump_key_reference() throws Exception {
      dumper.dumpCF("key_reference", 100);
   }

   @Test
   public void testDump_life_cycle_rule() throws Exception {
      dumper.dumpCF("life_cycle_rule", 100);
   }

   @Test
   public void testDump_life_cycle_step_history() throws Exception {
      dumper.dumpCF("life_cycle_step_history", 100);
   }

   @Test
   public void testDump_lock() throws Exception {
      dumper.dumpCF("lock", 100);
   }

   @Test
   public void testDump_metadata() throws Exception {
      dumper.dumpCF("metadata", 100);
   }

   @Test
   public void testDump_metadata_translation() throws Exception {
      dumper.dumpCF("metadata_translation", 100);
   }

   @Test
   public void testDump_note() throws Exception {
      dumper.dumpCF("note", 100);
   }

   @Test
   public void testDump_note_index() throws Exception {
      dumper.dumpCF("note_index", 100);
   }

   @Test
   public void testDump_running_job_executions() throws Exception {
      dumper.dumpCF("running_job_executions", 100);
   }

   @Test
   public void testDump_stat_history() throws Exception {
      dumper.dumpCF("stat_history", 100);
   }

   @Test
   public void testDump_stat_last_execution() throws Exception {
      dumper.dumpCF("stat_last_execution", 100);
   }

   @Test
   public void testDump_stat_series() throws Exception {
      dumper.dumpCF("stat_series", 100);
   }

   @Test
   public void testDump_step_execution_by_id() throws Exception {
      dumper.dumpCF("step_execution_by_id", 100);
   }

   @Test
   public void testDump_step_execution_by_job_execution() throws Exception {
      dumper.dumpCF("step_execution_by_job_execution", 100);
   }

   @Test
   public void testDump_sys_event_by_date() throws Exception {
      dumper.dumpCF("sys_event_by_date", 100);
   }

   @Test
   public void testDump_sys_event_by_register_date() throws Exception {
      dumper.dumpCF("sys_event_by_register_date", 100);
   }

   @Test
   public void testDump_temporary_token() throws Exception {
      dumper.dumpCF("temporary_token", 100);
   }

   @Test
   public void testGetArchiveUUID() throws Exception {
      // Récupère récursivement la liste des journaux des événements
      // String uuid = "af135566-f8b8-44dd-aaaa-f5074302efc5";
      // String uuid = "2326c0ff-3f13-4ae5-a860-5e494471a5f0";
      String uuid = "e46e2f91-3add-4ef4-bacb-a4da5de78008";

      while (true) {
         final byte[] previousUUID = dumper.getColumnValue("doc_info",
                                                           uuidToDocInfoKey(uuid),
                                                           ConvertHelper
                                                                        .stringToBytes("PREVIOUS_LOG_ARCHIVE_UUID"));
         if (previousUUID == null) {
            break;
         }
         uuid = new String(previousUUID);
         sysout.println("UUID : " + uuid);
      }
   }

   @Test
   public void testGetAllJournals() throws Exception {
      // String uuid = "e46e2f91-3add-4ef4-bacb-a4da5de78008"; // System
      String uuid = "98ab1acd-d800-4e1c-86ef-1d9cac9f8e35"; // Document

      int compteur = 0;
      while (true) {
         sysout.println("UUID : " + uuid);
         // Extraction du document
         final String filename = "c:\\temp\\journaux_doc\\" + String.format("%03d", compteur) + "_" + uuid + ".txt";
         if (!new File(filename).exists()) {
            ExtractOneDocument(uuid, filename);
         }
         sysout.println(FileHelper.head(filename));
         sysout.println(FileHelper.tail(new File(filename), 2));
         // Thread.sleep(150);
         // if (compteur == 0) break;

         final byte[] previousUUID = dumper.getColumnValue("doc_info",
                                                           uuidToDocInfoKey(uuid),
                                                           ConvertHelper
                                                                        .stringToBytes("PREVIOUS_LOG_ARCHIVE_UUID"));
         if (previousUUID == null) {
            break;
         }
         uuid = new String(previousUUID);
         compteur++;
      }
   }

   private void extractOneDocInfo(final String uuid) throws Exception {
      final byte[] key = uuidToDocInfoKey(uuid);
      dumper.dumpCF("doc_info", key);
   }

   private byte[] uuidToDocInfoKey(final String uuid) {
      final String startKey = "0010";
      final String endKey = "000005" + ConvertHelper.stringToHex("0.0.0") + "00";
      final String keyAsHex = startKey + uuid.replace("-", "") + endKey;
      sysout.println("Key en hexa : " + keyAsHex);
      final byte[] key = ConvertHelper.hexStringToByteArray(keyAsHex);
      return key;
   }

   /**
    * Renvoie un FILE_UUID à partir d'un UUID
    * 
    * @param uuid
    *           : uuid du document
    * @return file_uuid : uuid du fichier
    * @throws Exception
    */
   private byte[] uuidToFileUUID(final String uuid) throws Exception {
      final byte[] fileUUID = dumper.getColumnValue("doc_info",
                                                    uuidToDocInfoKey(uuid),
                                                    ConvertHelper.stringToBytes("SM_FILE_UUID"));
      return fileUUID;
   }

   @Test
   public void testDumpOneDocument() throws Exception {
      dumper.dumpCF("documents", "f3347dee-fa1b-4568-9363-6ed6cbbe7b23");
   }

   @Test
   public void testExtractOneDocument() throws Exception {
      ExtractOneDocument("c088b447-f501-4888-b6ae-a6705b2e4573", "c:\\temp\\doc.txt");

      // ExtractOneDocumentFromFileUUID("630a3663-df91-4166-adbc-c9a4a94d4a0f", "c:\\temp\\test.pdf");
      // ExtractOneDocument("8a43184c-81b1-4695-95b8-611734ac261d", "c:\\temp\\journal.txt");
      // ExtractOneDocument("3fe079a1-ae5e-4e67-8b68-0c4f71e80f38", "c:\\temp\\archive_3fe079a1-ae5e-4e67-8b68-0c4f71e80f38.2.txt");
      // ExtractOneDocument("cd7a8d60-a73a-4da9-8100-0f2e9b7667e1", "c:\\temp\\archive_cd7a8d60-a73a-4da9-8100-0f2e9b7667e1.txt");
      // ExtractOneDocument("2326c0ff-3f13-4ae5-a860-5e494471a5f0", "c:\\temp\\archive_2326c0ff-3f13-4ae5-a860-5e494471a5f0.txt");
      // ExtractOneDocument("74c366f8-39d2-431c-9798-2c1cf64a7f42", "c:\\temp\\archive_74c366f8-39d2-431c-9798-2c1cf64a7f42.txt");
      // ExtractOneDocument("2c2b26b3-f855-4f8c-83d0-29bc02e0b221", "c:\\temp\\archive_2c2b26b3-f855-4f8c-83d0-29bc02e0b221.txt");
      // ExtractOneDocument("cdc7a928-f182-4284-9699-7c670b10aecb", "c:\\temp\\archive_cdc7a928-f182-4284-9699-7c670b10aecb.txt");
      // ExtractOneDocument("74c366f8-39d2-431c-9798-2c1cf64a7f42", "c:\\temp\\archive_74c366f8-39d2-431c-9798-2c1cf64a7f42.txt");
      // ExtractOneDocument("a5cea082-e6c2-487b-95c3-ca20f833cbc8", "c:\\temp\\archive_a5cea082-e6c2-487b-95c3-ca20f833cbc8.txt");
      // ExtractOneDocument("79f7e469-5efd-468e-b8dc-e06daeb27659", "c:\\temp\\archive_79f7e469-5efd-468e-b8dc-e06daeb27659.txt");
      // ExtractOneDocument("720ddb05-da41-496b-aade-1a06c1b47725", "c:\\temp\\archive_720ddb05-da41-496b-aade-1a06c1b47725.txt");
      // ExtractOneDocument("10a3173c-b742-4edb-b77d-3ffdb8f45321", "c:\\temp\\archive_system_last.txt");
      // ExtractOneDocument("747bca57-1d56-4f93-a4ae-36b666a0ba5e", "c:\\temp\\archive_doc_2012-05-24.txt");
      // ExtractOneDocument("346d1ea7-2793-434c-a8f8-40c53ece9ceb", "c:\\temp\\archive_doc_2012-25-25.txt");
      // ExtractOneDocument("264CE288-940E-4E92-8B11-DB02A5CD95EA", "c:\\temp\\test1.pdf");
      // ExtractOneDocument("4989A4D8-DA49-4E48-AB4C-395047EDC5EF", "c:\\temp\\test2.pdf");
      // ExtractOneDocument("5D1D920B-4D97-4ED7-8FF8-B4045EE6F918", "c:\\temp\\test3.pdf");
      // ExtractOneDocument("644BDAAC-6504-4290-80F6-EE07E3D872A9", "c:\\temp\\test4.pdf");
      // ExtractOneDocument("EF639451-15CB-4AB7-94DB-657995C66EC8", "c:\\temp\\test5.pdf");
      // ExtractOneDocument("D1DC3F43-591B-4299-BE87-EE970A62DC90", "c:\\temp\\alex2\\5.pdf");
      // ExtractOneDocument("5F72669A-BECD-4513-849C-ECCEF216001D", "c:\\temp\\alex2\\6.pdf");
      // ExtractOneDocument("017961ff-1899-40a7-8d3d-6d32729780ef", "c:\\temp\\test2.pdf");
      // ExtractOneDocument("0125e102-a096-4db0-9746-edf0c314498a", "c:\\temp\\test3.pdf");
      // ExtractOneDocument("0f5003e0-8698-4405-9804-a098ed6e5575", "c:\\temp\\test4.pdf");
   }

   /***
    * Extrait le corps de la version 1 du document dont l'uuid est passé en paramètre
    * 
    * @param uuid
    *           : uuid du document à extraire
    * @param fileName
    *           : fichier à créer
    * @throws Exception
    */
   private void ExtractOneDocument(final String uuid, final String fileName) throws Exception {
      final byte[] fileUuid = uuidToFileUUID(uuid);
      if (fileUuid == null) {
         throw new Exception("Pas de fileUUID trouvé pour cet uuid : " + uuid);
      }
      final String stringFileUuid = ConvertHelper.getReadableUTF8String(fileUuid);
      // ExtractOneDocumentFromFileUUID(stringFileUuid, fileName);
      DocumentDao.ExtractOneDocumentFromFileUUID(keyspace, stringFileUuid, fileName);
   }

   /***
    * Extrait le corps de la version 1 du document dont l'uuid est passé en paramètre
    * 
    * @param fileUuid
    *           : uuid du fichier à extraire
    * @param fileName
    *           : fichier à créer
    * @throws Exception
    */
   private void ExtractOneDocumentFromFileUUID(final String fileUuid, final String fileName) throws Exception {
      final String key = fileUuid.toLowerCase();

      final StringSerializer stringSerializer = StringSerializer.get();
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
                                                                                .createRangeSlicesQuery(keyspace,
                                                                                                        stringSerializer,
                                                                                                        stringSerializer,
                                                                                                        bytesSerializer);
      rangeSlicesQuery.setColumnFamily("documents");
      rangeSlicesQuery.setKeys(key, key);
      rangeSlicesQuery.setRange("chunk_0", "chunk_9", false, 200);
      final QueryResult<OrderedRows<String, String, byte[]>> result = rangeSlicesQuery
                                                                                      .execute();
      final OrderedRows<String, String, byte[]> orderedRows = result.get();

      // On ne reçoit normalement qu'une seule ligne
      final Row<String, String, byte[]> row = orderedRows.getByKey(key);
      if (row == null) {
         throw new IllegalArgumentException("On n'a pas trouvé de fichier dont l'uuid est " + fileUuid);
      }

      // sysout.println("Création du fichier " + fileName + "...");
      final File someFile = new File(fileName);
      final FileOutputStream fos = new FileOutputStream(someFile);

      final ColumnSlice<String, byte[]> columnSlice = row.getColumnSlice();
      final List<HColumn<String, byte[]>> columns = columnSlice.getColumns();
      for (final HColumn<String, byte[]> column : columns) {
         final String name = column.getName();
         // sysout.println("Extracting " + name + " ...");
         final byte[] value = column.getValue();
         fos.write(value);
      }
      fos.flush();
      fos.close();
   }

   @Test
   public void testDumpTermInfo() throws Exception {
      // dumper.printColumnNameInComposite = true;
      dumper.printColumnNameInHex = true;
      dumper.deserializeValue = true;
      dumper.compositeDisplayTypeMapper = new boolean[] {true, true, false};

      // dumper.dumpCF("TermInfo", 10000);

      // byte[] startKey = getTermInfoKey("srn", "0123406588");
      // byte[] startKey = getTermInfoKey("nce", "260002400002811882");
      // byte[] startKey = getTermInfoKey("srt", "21260005000018");
      // byte[] startKey = ConvertHelper.hexStringToByteArray("000000000d534d5f46494e414c5f4441544500000000");
      final byte[] startKey = ConvertHelper.hexStringToByteArray("0000000011534d5f4152434849564147455f44415445000011323031333032303930323230333338333600");
      // dumper.dumpCF_StartKey("TermInfo", startKey, 100);
      dumper.dumpCF_StartKey("term_info", startKey, 50);
   }

   @Test
   public void testDumpOneTermInfo() throws Exception {
      // dumpOneTermInfo("srn", "0123406588");
      // dumpOneTermInfo("srn", "489393652");
      // dumpOneTermInfo("itm", "2014-12-30");
      // dumpOneTermInfo("SM_LIFE_CYCLE_REFERENCE_DATE", "20150101013717518");
      // dumpOneTermInfo("SM_LIFE_CYCLE_REFERENCE_DATE", "20120106124749571");
      // dumpOneTermInfo("itm", "2014-12-30");
      // dumpOneTermInfo("cpt&sac&SM_DOCUMENT_TYPE&nor&", "true1906.1.3.1.13939");
      dumpOneTermInfo("SM_FINAL_DATE", "");
   }

   @Test
   public void testDumpBigTermInfo() throws Exception {
      final byte[] key = getTermInfoKey("SM_FINAL_DATE", "");
      final String docUUID = "f60b96a7-49bc-46d2-88b0-7575b222fa1a";
      final byte[] searchedCol = getTermInfoColumn("SAE-PROD", docUUID);
      final byte[] endCol = getTermInfoColumn("SAE-PROD", "ffffffff-ffff-ffff-ffff-ffffffffffff");
      sysout.println("colonne cherchée :" + ConvertHelper.getHexString(searchedCol));
      dumper.printColumnNameInHex = true;
      dumper.deserializeValue = true;
      dumper.dumpCF_slice("term_info", key, searchedCol, endCol, 10, false);
   }

   /**
    * Calcule une clé pour la CF TermInfo
    * 
    * @param categorieName
    *           : nom de la catégorie (ex : nre)
    * @param value
    *           : valeur de la catégorie
    * @throws Exception
    */
   private byte[] getTermInfoKey(final String categorieName, final String value) throws Exception {
      // Exemple de Clé de terminfo :
      // \x00\x00\x00\x00 \x03 nre \x00\x00 \x04 1234 \x00
      // Les \x03 et \x04 sont les tailles de categorieName et value
      final byte[] key = ConvertHelper.hexStringToByteArray("00000000" + ConvertHelper.getHexString((byte) categorieName.length())
            + ConvertHelper.stringToHex(categorieName)
            + "0000" + ConvertHelper.getHexString((byte) value.length())
            + ConvertHelper.stringToHex(value) + "00");
      return key;
   }

   /**
    * Dump une ligne de la CF TermInfo
    * 
    * @param categorieName
    *           : nom de la catégorie (ex : nre)
    * @param value
    *           : valeur de la catégorie
    * @throws Exception
    */
   private void dumpOneTermInfo(final String categorieName, final String value) throws Exception {
      final byte[] key = getTermInfoKey(categorieName, value);
      dumper.printColumnNameInHex = true;
      dumper.deserializeValue = true;
      dumper.dumpCF("term_info", key);
   }

   @Test
   public void testDumpTermInfoRangeDate() throws Exception {
      dumper.deserializeValue = true;
      // dumper.printColumnNameInComposite = true;
      // dumper.compositeDisplayTypeMapper = new boolean[]{false, true, false};
      dumper.dumpCF("term_info_range_date", 15);
   }

   @Test
   public void testDumpTermInfoRangeDateTime() throws Exception {
      dumper.deserializeValue = true;
      dumper.printColumnNameInHex = true;
      // dumper.printColumnNameInComposite = true;
      // dumper.compositeDisplayTypeMapper = new boolean[]{false, true, false};
      // dumper.dumpCF("TermInfoRangeDatetime", 10, 2);
      // byte[] key = ConvertHelper.hexStringToByteArray("000000000d534d5f46494e414c5f44415445000010f573ae93ac6a4615a23b150fd621b5a00000010000");
      // byte[] key = ConvertHelper.hexStringToByteArray("0000000011534D5F4152434849564147455F44415445000010F573AE93AC6A4615A23B150FD621B5A00000010000");
      // byte[] key = ConvertHelper.hexStringToByteArray("0000000011534D5F4152434849564147455F44415445000010F573AE93AC6A4615A23B150FD621B5A00000010100");
      final byte[] key = ConvertHelper.hexStringToByteArray("0000000014534D5F4D4F44494649434154494F4E5F44415445000010F573AE93AC6A4615A23B150FD621B5A00000014C00");
      dumper.dumpCF("term_info_range_datetime", key, 2);
   }

   @Test
   public void testDumpTermInfoRangeDateTime_sliceFinalDate() throws Exception {
      dumper.deserializeValue = true;
      dumper.printColumnNameInHex = true;
      // Clé correspondant à "SM_FINAL_DATE"
      final byte[] key = ConvertHelper.hexStringToByteArray("000000000d534d5f46494e414c5f44415445000010f573ae93ac6a4615a23b150fd621b5a00000010000");

      final Composite start = new Composite();
      start.addComponent("", StringSerializer.get());
      start.addComponent(UUID.fromString("f60b96a7-49bc-46d2-88b0-7575b222fa1a"), UUIDSerializer.get());
      start.addComponent("", StringSerializer.get());
      final byte[] slice_start = new CompositeSerializer().toBytes(start);
      final Composite stop = new Composite();
      stop.addComponent("", StringSerializer.get());
      stop.addComponent(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"), UUIDSerializer.get());
      stop.addComponent("", StringSerializer.get());
      final byte[] slice_stop = new CompositeSerializer().toBytes(stop);

      dumper.dumpCF_slice("term_info_range_datetime", key, slice_start, slice_stop, 50, false);
   }

   @Test
   public void testDumpTermInfoRangeDateTime_slice() throws Exception {
      dumper.deserializeValue = true;
      // dumper.printColumnNameInComposite = true;
      // dumper.compositeDisplayTypeMapper = new boolean[]{false, true, false};
      // dumper.dumpCF("TermInfoRangeDatetime", 15);

      // SM_LIFE_CYCLE_REFERENCE_DATE
      final byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("20160402");
      final byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes("20160403");
      dumper.dumpCF_slice("term_info_range_datetime", sliceStart, sliceEnd, 500);

   }

   @Test
   public void testDumpTermInfoRangeDouble() throws Exception {
      dumper.dumpCF("term_info_range_double", 15);
   }

   @Test
   public void testDumpTermInfoRangeFloat() throws Exception {
      dumper.dumpCF("term_info_range_float", 15);
   }

   @Test
   public void testDumpTermInfoRangeInteger() throws Exception {
      dumper.dumpCF("term_info_range_integer", 15);
   }

   @Test
   public void testDumpTermInfoRangeLong() throws Exception {
      dumper.dumpCF("term_info_range_long", 15);
   }

   @Test
   public void testDumpTermInfoRangeString() throws Exception {
      // dumper.deserializeValue = true;
      // dumper.printColumnNameInComposite = true;
      // dumper.compositeDisplayTypeMapper = new boolean[]{false, true, false};
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("term_info_range_string", 100, 2);
   }

   @Test
   public void testDumpTermInfoRangeStringSlice() throws Exception {
      dumper.deserializeValue = true;
      // dumper.printColumnNameInComposite = true;
      // dumper.compositeDisplayTypeMapper = new boolean[]{false, true, false};
      // Clé pour nce
      // byte[] key = ConvertHelper.getBytesFromReadableUTF8String("\\x00ά\\xed\\x00\\x05ur\\x00\\x13[Ljava.lang.Object;\\x90\\xceX\\x9f\\x10s)l\\x02\\x00\\x00xp\\x00\\x00\\x00\\x04t\\x00\\x00t\\x00\\x03ncesr\\x00\\x0ejava.util.UUID\\xbc\\x99\\x03\\xf7\\x98m\\x85/\\x02\\x00\\x02J\\x00\\x0cleastSigBitsJ\\x00\\x0bmostSigBitsxp\\xa2;\\x15\\x0f\\xd6!\\xb5\\xa0\\xf5s\\xae\\x93\\xacjF\\x15sr\\x00\\x11java.lang.Integer\\x12\\xe2\\xa0\\xa4\\xf7\\x81\\x878\\x02\\x00\\x01I\\x00\\x05valuexr\\x00\\x10java.lang.Number\\x86\\xac\\x95\\x1d\\x0b\\x94\\xe0\\x8b\\x02\\x00\\x00xp\\x00\\x00\\x00\\x00\\x00");
      // byte[] key = ConvertHelper.getBytesFromReadableUTF8String("\\x00\\x00\\x00\\x00\\x03nce\\x00\\x00\\x10\\xf5s\\xae\\x93\\xacjF\\x15\\xa2;\\x15\\x0f\\xd6!\\xb5\\xa0\\x00\\x00\\x01\\x00\\x00");
      final byte[] key = ConvertHelper.getBytesFromReadableUTF8String("\\x00\\x00\\x00\\x00\\x03itm\\x00\\x00\\x10\\xf5s\\xae\\x93\\xacjF\\x15\\xa2;\\x15\\x0f\\xd6!\\xb5\\xa0\\x00\\x00\\x01\\x00\\x00");
      // byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("537000000521244");
      // byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes ("537000000521244999");
      // byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("827000002150000736");
      // byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes ("827000002150000737");
      final byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("2014-12-30");
      final byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes("2014-12-308");
      dumper.dumpCF_slice("term_info_range_string", key, sliceStart, sliceEnd, 150, false);
      // dumper.dumpCF("term_info_range_string", key, false);
   }

   @Test
   public void testDumpTermInfoRangeStringSlice_RefDossier() throws Exception {
      dumper.deserializeValue = true;
      // dumper.printColumnNameInComposite = true;
      // dumper.compositeDisplayTypeMapper = new boolean[]{false, true, false};
      // Clé pour red (ReferenceDossier)
      final byte[] key = ConvertHelper.getBytesFromReadableUTF8String("\\x00\\x00\\x00\\x00\\x03red\\x00\\x00\\x10\\xe7+u%\\xa9aIǹ\\x03\\xd6\\xf2m*\\xce3\\x00\\x00\\x01\\x00\\x00");
      final byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("444403");
      final byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes("444404");
      dumper.dumpCF_slice("term_info_range_string", key, sliceStart, sliceEnd, 150, false);
      // dumper.dumpCF("term_info_range_string", key, false);
   }

   @Test
   public void testDumpTermInfoRangeUUID() throws Exception {
      dumper.deserializeValue = true;
      // dumper.printColumnNameInComposite = true;
      // dumper.compositeDisplayTypeMapper = new boolean[]{false, true, false};

      dumper.dumpCF("term_info_range_uuid", 30);

      // byte[]sliceStart = new byte[0];
      // byte[]sliceEnd = new byte[0];
      // byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("1234");
      // byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes("1235");
      // dumper.dumpCF_slice("TermInfoRangeUUID", sliceStart, sliceEnd, 15);
   }

   @Test
   public void testDumpTermInfoRangeUUIDByKey() throws Exception {
      dumper.deserializeValue = true;
      // dumper.printColumnNameInHex = true;
      // dumper.printColumnNameInComposite = true;
      // dumper.compositeDisplayTypeMapper = new boolean[]{false, true, false};

      // dumper.dumpCF("TermInfoRangeUUID", 15);

      // byte[]sliceStart = new byte[0];
      // byte[]sliceEnd = new byte[0];
      final byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("1234");
      final byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes("1235");
      final byte[] key = ConvertHelper.getBytesFromReadableUTF8String("\\x00\\x00\\x00\\x00\\x07SM_UUID\\x00\\x00\\x10\\xf5s\\xae\\x93\\xacjF\\x15\\xa2;\\x15\\x0f\\xd6!\\xb5\\xa0\\x00\\x00\\x01\\x00\\x00");
      final Stopwatch chrono = Stopwatch.createStarted();
      dumper.dumpCF_slice("term_info_range_uuid", key, sliceStart, sliceEnd, 1, false);
      chrono.stop();
      System.out.println("Durée : " + chrono.elapsed(TimeUnit.MILLISECONDS) + " ms");
   }

   @Test
   public void testDumpTermInfoRangeUUIDByKey2() throws Exception {
      dumper.deserializeValue = true;
      dumper.printColumnNameInHex = true;
      final byte[] key = ConvertHelper.getBytesFromReadableUTF8String("\\x00\\x00\\x00\\x00\\x07SM_UUID\\x00\\x00\\x10\\xf5s\\xae\\x93\\xacjF\\x15\\xa2;\\x15\\x0f\\xd6!\\xb5\\xa0\\x00\\x00\\x01\\x00\\x00");
      final byte[] columnName = ConvertHelper.hexStringToByteArray("002431323334303036352d346137382d346133612d386265342d626464336537393564613830000010123400654a784a3a8be4bdd3e795da80000005302e302e3000");
      final Stopwatch chrono = Stopwatch.createStarted();
      final byte[] columnValue = dumper.getColumnValue("term_info_range_uuid", key, columnName);
      chrono.stop();
      final String displayValue = ConvertHelper.getReadableUTF8String(columnValue);
      System.out.println("Value : " + displayValue);
      System.out.println("Durée : " + chrono.elapsed(TimeUnit.MILLISECONDS) + " ms");
   }

   private Composite getTermInfoRangeUUIDSliceComposite(final String docUUID) {
      /*
       * La CF TermInfoRangeUUID est déclarée ainsi :
       * create column family TermInfoRangeUUID with comparator = 'CompositeType(UTF8Type, UUIDType, UTF8Type)'
       * La 1ere partie du composite est l'uuid du document codé en UTF8
       * La 2eme partie du composite est l'uuid du document codé en bytes !!!!
       * La 3eme partie du composite est une chaîne correspondant à la version du document ("0.0.0")
       */

      final Composite c = new Composite();
      c.addComponent(docUUID, StringSerializer.get());
      c.addComponent(ConvertHelper.hexStringToByteArray("00000000000000000000000000000000"), BytesArraySerializer.get());
      c.addComponent("", StringSerializer.get());
      return c;
   }

   private byte[] getTermInfoRangeUUIDSliceBytes(final String docUUID) {
      final Composite c = getTermInfoRangeUUIDSliceComposite(docUUID);
      return new CompositeSerializer().toBytes(c);
   }

   private byte[] getTermInfoColumn(final String baseName, final String docUUID) throws Exception {
      final UUID baseUUID = getBaseUUID(baseName);
      final Composite c = new Composite();
      c.addComponent(baseUUID, UUIDSerializer.get());
      c.addComponent(UUID.fromString(docUUID), UUIDSerializer.get());
      c.addComponent("0.0.0", StringSerializer.get());
      return new CompositeSerializer().toBytes(c);
   }

   @Test
   public void testDump_thumbnail() throws Exception {
      dumper.dumpCF("thumbnail", 15);
   }

   @Test
   public void testDump_translation_code() throws Exception {
      dumper.dumpCF("translation_code", 15);
   }

   @Test
   public void testDump_user() throws Exception {
      dumper.dumpCF("user", 15);
   }

   @Test
   public void testDump_user_preferences() throws Exception {
      dumper.dumpCF("user_preferences", 15);
   }

   @Test
   public void testDump_users_by_group() throws Exception {
      dumper.dumpCF("users_by_group", 15);
   }

   @SuppressWarnings("unchecked")
   @Test
   /**
    * Recherche de "doublons" par échantillonnage.
    * On parcours 5000 clés de TermInfo. Si la clé concerne une index sur siren (srn), on
    * compte le nombre de documents
    */
   public void testLookForDoublons() throws Exception {
      int compteurKO = 0;
      int compteurOK = 0;
      final List<byte[]> keys = dumper.getKeys("term_info", 5000);
      for (int i = 0; i < keys.size(); i++) {
         final byte[] key = keys.get(i);
         final String displayableKey = ConvertHelper.getReadableUTF8String(key);
         if (displayableKey.contains("srn")) {
            // dumper.dumpCF("TermInfo", key);
            final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
            final SliceQuery<byte[], byte[], byte[]> query = HFactory.createSliceQuery(keyspace, bytesSerializer, bytesSerializer, bytesSerializer);
            query.setColumnFamily("TermInfo");
            query.setKey(key);
            query.setRange(new byte[0], new byte[0], false, 100);
            final QueryResult<ColumnSlice<byte[], byte[]>> result = query.execute();
            final ColumnSlice<byte[], byte[]> slice = result.get();
            final List<HColumn<byte[], byte[]>> columns = slice.getColumns();
            // sysout.println("Key :" + displayableKey);
            final Map<String, List<String>> docs = new HashMap<>();
            for (final HColumn<byte[], byte[]> column : columns) {
               final byte[] value = column.getValue();
               // La valeur est une map sérialisée. On la désérialise
               final ByteArrayInputStream bis = new ByteArrayInputStream(value);
               final ObjectInputStream ois = new ObjectInputStream(bis);
               final Map<String, ArrayList<String>> map = (Map) ois.readObject();
               // sysout.println(map);
               final String siren = map.get("srn").get(0);
               final String title = map.get("SM_TITLE").get(0);
               final String date = map.get("SM_ARCHIVAGE_DATE").get(0);
               final String uuid = map.get("SM_UUID").get(0);
               if (!docs.containsKey(title)) {
                  docs.put(title, new ArrayList<String>());
               }
               docs.get(title).add("Siren : " + siren + "  Title : " + title + "  Date : " + date + "  UUID : " + uuid);
            }
            for (final Map.Entry<String, List<String>> doc : docs.entrySet()) {
               final List<String> list = doc.getValue();
               if (list.size() > 1) {
                  compteurKO++;
                  for (final String element : list) {
                     sysout.println(element);
                  }
                  sysout.println();
               } else {
                  compteurOK++;
               }
            }
         }
      }
      sysout.println("Nombre de OK : " + compteurOK);
      sysout.println("Nombre de KO : " + compteurKO);
   }

   @Test
   public void testDelete() throws Exception {
      final StringSerializer keySerializer = StringSerializer.get();
      final Mutator<String> mutator = HFactory.createMutator(keyspace, keySerializer);
      final StringSerializer nameSerializer = keySerializer;
      final String cf = "documents";
      final String key = "156432135";
      mutator.delete(key, cf, null, nameSerializer);
   }

   public void testUpdate() {
      final ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<>(keyspace,
                                                                                                           "myColFamily",
                                                                                                           StringSerializer.get(),
                                                                                                           StringSerializer.get());
      final ColumnFamilyUpdater<String, String> updater = template.createUpdater("a key");
      updater.setString("domain", "www.datastax.com");

   }

   @Test
   public void testEntityManager() {

      final EntityManagerImpl em = new EntityManagerImpl(keyspace,
                                                         "fr.urssaf.hectotest");

      /*
       * MyPojo pojo1 = new MyPojo(); pojo1.setId(UUID.randomUUID());
       * pojo1.setLongProp1(123L);
       * em.save(pojo1);
       */

      final MyPojo pojo2 = em.load(MyPojo.class, UUID.randomUUID());

      if (pojo2 == null) {
         sysout.println("Entity non trouvée");
      } else {
         sysout.println("Long prop = " + pojo2.getLongProp1());
      }
   }

   @Test
   public void testDumpStepExecution2() throws Exception {
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final SliceQuery<byte[], byte[], byte[]> query = HFactory.createSliceQuery(keyspace, bytesSerializer, bytesSerializer, bytesSerializer);

      query.setColumnFamily("StepExecution");
      final byte[] key = ConvertHelper.hexStringToByteArray("00000000000070e5");
      query.setKey(key);
      final int count = 100;
      query.setRange(new byte[0], new byte[0], false, count);
      final QueryResult<ColumnSlice<byte[], byte[]>> result = query.execute();
      dumper.dumpColumns(result.get().getColumns());
   }

   @Test
   public void testDumpStepExecution3() throws Exception {
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final IndexedSlicesQuery<Long, String, byte[]> query = HFactory.createIndexedSlicesQuery(keyspace,
                                                                                               LongSerializer.get(),
                                                                                               StringSerializer.get(),
                                                                                               bytesSerializer);
      /*
       * query.setColumnFamily("StepExecution");
       * query.set
       * byte[] key = ConvertHelper.hexStringToByteArray("00000000000070e5");
       * query.setKey(key);
       * int count = 100;
       * query.setRange(new byte[0], new byte[0], false, count );
       * QueryResult<ColumnSlice<byte[], byte[]>> result = query.execute();
       * dumper.dumpColumns(result.get().getColumns());
       */
   }

   @Test
   public void testDumpSystemEventLogByTime() throws Exception {
      // dumper.dumpCF("SystemEventLogByTime", 50);
      dumper.dumpCF_slice("SystemEventLogByTime", "20120303".getBytes(), new byte[0], new byte[0], 10000, true);
   }

   @Test
   public void testDumpSystemEventLogByTimeSerialized() throws Exception {
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("SystemEventLogByTimeSerialized", 5);
   }

   @Test
   public void testDumpColumnfamilies() throws Exception {
      systemDumper.printKeyInHex = false;
      systemDumper.dumpCF("schema_columnfamilies", 1000);
   }

   @Test
   public void testDumpColumns() throws Exception {
      systemDumper.printKeyInHex = false;
      systemDumper.dumpCF("schema_columns", 1000);
   }

   @Test
   public void testProvoquerSoapFaultErreurInterneConsultation() throws Exception {

      // Suppression du contenu du document dans la CF "Documents"
      // pour provoquer une erreur DFCE non gérée par le SAE
      // et donc pour provoquer la SoapFault SAE d'erreur interne à la consultation

      // Modifier ici l'UUID du document
      final String uuid = "1F86D287-E0C5-4857-9C49-06AF584E1199";

      final byte[] fileUuid = uuidToFileUUID(uuid);
      if (fileUuid == null) {
         throw new Exception("Pas de fileUUID trouvé pour cet uuid : " + uuid);
      }
      final String stringFileUuid = ConvertHelper.getReadableUTF8String(fileUuid);

      final String key = stringFileUuid.toLowerCase();
      System.out.println("fileUUID key: " + key);

      final StringSerializer stringSerializer = StringSerializer.get();
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
                                                                                .createRangeSlicesQuery(keyspace,
                                                                                                        stringSerializer,
                                                                                                        stringSerializer,
                                                                                                        bytesSerializer);
      rangeSlicesQuery.setColumnFamily("documents");
      rangeSlicesQuery.setKeys(key, key);
      rangeSlicesQuery.setRange("chunk_0", "chunk_9", false, 1000);
      final QueryResult<OrderedRows<String, String, byte[]>> result = rangeSlicesQuery
                                                                                      .execute();
      final OrderedRows<String, String, byte[]> orderedRows = result.get();

      // On ne reçoit normalement qu'une seule ligne
      final Row<String, String, byte[]> row = orderedRows.getByKey(key);
      if (row == null) {
         throw new IllegalArgumentException(
                                            "On n'a pas trouvé de fichier dont l'uuid est " + fileUuid);
      }

      // Suppression de la ligne
      final Mutator<String> mutator = HFactory.createMutator(keyspace,
                                                             StringSerializer.get());
      mutator.addDeletion(key, "Documents");
      mutator.execute();

   }

   /**
    * Renvoie l'UUID d'une base DFCE
    * 
    * @param baseName
    *           nom de la base
    * @return UUID de la base
    * @throws Exception
    */
   public UUID getBaseUUID(final String baseName) throws Exception {
      final byte[] uuidAsBytes = dumper.getColumnValue("base",
                                                       ConvertHelper.stringToBytes(baseName),
                                                       ConvertHelper.stringToBytes("uuid"));
      return ConvertHelper.byteArrayToUUID(uuidAsBytes);
   }

}
