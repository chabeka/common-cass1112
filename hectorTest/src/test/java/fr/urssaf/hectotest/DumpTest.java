package fr.urssaf.hectotest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.model.thrift.ThriftCountQuery;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Dump de données cassandra
 */
public class DumpTest {

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
      //servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
      //servers = "hwi54saecas1.cve.recouv:9160";  // CNH
      servers = "cer69imageint9.cer69.recouv:9160";
      //servers = "cer69imageint10.cer69.recouv:9160";
      //servers = "10.203.34.39:9160";    // Noufnouf
      //servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      //servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160";
      //servers = "cer69-saeint3.cer69.recouv:9160";
      // servers = "cnp69pprodsaecas1.cer69.recouv:9160,cnp69pprodsaecas2.cer69.recouv:9160,cnp69pprodsaecas3.cer69.recouv:9160";
      
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      cluster = HFactory.getOrCreateCluster("Docubase", hostConfigurator);
      keyspace = HFactory.createKeyspace("Docubase", cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);

      sysout = new PrintStream(System.out, true, "UTF-8");

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      // sysout = new PrintStream("c:/temp/out.txt");
      dumper = new Dumper(keyspace, sysout);
   }

   public static void main(String[] args) throws Exception {
      DumpTest app = new DumpTest();
      app.init();
      /*
       * app.testDumpDocInfo(); app.close();
       */
      app.sysout.println("±ΰσ");
      System.out.println("±ΰσ");
   }

   @After
   public void close() {
      // cluster.getConnectionManager().shutdown();
      HFactory.shutdownCluster(cluster);
   }

   @Test
   public void testDumpDocInfo() throws Exception {
      dumper.printKeyInHex = true;
      dumper.dumpCF("DocInfo", 300);
   }

   @Test
   public void testDocInfoFreezer() throws Exception {
      dumper.printKeyInHex = true;
      dumper.dumpCF("DocInfoFreezer", 150);
   }

   @Test
   public void testExtractOneDocInfo() throws Exception {
      extractOneDocInfo("74b7cde6-01db-4630-a2e2-e8c543286168");
      // extractOneDocInfo ("05A5CB97-196B-423C-9A3C-F438F160DD03");
      // extractOneDocInfo ("3E915A0A-3878-47B4-8225-666F1ECAB779");
      // extractOneDocInfo("1c577d7e-19bf-45b0-ae51-456b3ba084f8");
      // extractOneDocInfo("6fd809ec-8fd7-44f3-9d6f-ee655fa7e54a");
   }

   @Test
   public void testGetArchiveUUID() throws Exception {
      // Récupère récursivement la liste des journaux des événements
      // String uuid = "af135566-f8b8-44dd-aaaa-f5074302efc5";
      String uuid = "2326c0ff-3f13-4ae5-a860-5e494471a5f0";
      while (true) {
         byte[] previousUUID = dumper.getColumnValue("DocInfo",
               uuidToDocInfoKey(uuid), ConvertHelper
                     .stringToBytes("PREVIOUS_LOG_ARCHIVE_UUID"));
         uuid = new String(previousUUID);
         sysout.println("UUID : " + uuid);
         if (previousUUID == null)
            break;
      }
   }

   private void extractOneDocInfo(String uuid) throws Exception {
      /*
       * // Version qui fonctionne avec DFCE 0.9 byte[] key =
       * ConvertHelper.stringToBytesWithDocubaseDelimiter(uuid + "|||0.0.0");
       * dumper.dumpCF("DocInfo",key);
       */

      byte[] key = uuidToDocInfoKey(uuid);
      dumper.dumpCF("DocInfo", key);
   }

   private byte[] uuidToDocInfoKey(String uuid) {
      String startKey = "0010";
      String endKey = "000005" + ConvertHelper.stringToHex("0.0.0") + "00";
      byte[] key = ConvertHelper.hexStringToByteArray(startKey
            + uuid.replace("-", "") + endKey);
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
   private byte[] uuidToFileUUID(String uuid) throws Exception {
      byte[] fileUUID = dumper
            .getColumnValue("DocInfo", uuidToDocInfoKey(uuid), ConvertHelper
                  .stringToBytes("SM_FILE_UUID"));
      return fileUUID;
   }

   @Test
   public void testGetDocCount() throws Exception {
      // Ne compte que 2 blocs sur 50
      // getDocCount(200, 2);

      // Comptage exhaustif, par bloc de 50
      getDocCount(50, 50);
   }

   /**
    * Compte le nombre de documents de la base. Le comptage peut être long s'il
    * y a beaucoup de documents. On compte donc par bloc, pour pouvoir afficher
    * l'avancement du comptage. De plus, on permet de ne compter qu'une partie
    * des documents, et d'évaluer le nombre total par extrapolation
    * 
    * @param blocCount
    *           : l'espace des UUID sera divisé par ce nombre de bloc
    * @param blocsToCount
    *           : nombre de blocs à compter (égal à blocCount si on veut compter
    *           tous les documents)
    * @throws Exception
    */
   private void getDocCount(int blocCount, int blocsToCount) throws Exception {
      /*
       * //Méthode trop lente
       * 
       * int count = dumper.getKeysCount("DocInfo");
       * sysout.println("Nombre de clés dans DocInfo : " + count); count =
       * dumper.getKeysCount("Documents");
       * sysout.println("Nombre de clés dans Documents : " + count);
       */

      int maxKeys = 1000;
      List<byte[]> keys = dumper.getKeys("TermInfoRangeUUID", maxKeys);
      if (keys.size() == maxKeys)
         throw new Exception("Attention : trop de clés");
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

      // byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("\u0000");
      // byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes("\uFFFF");

      // On découpe l'espace des UUID
      String[][] slices = getUUIDSlices(blocCount);

      int maxColPerBloc = 1000000;
      int total = 0;

      // Il faut parcourir plusieurs clés, car DFCE crée une clé par base DFCE,
      // et
      // il y a au moins 2 bases DFCE : la base SAE et la base
      // DAILY_LOG_ARCHIVE_BASE
      for (byte[] key : keys) {
         String displayableKey = ConvertHelper.getReadableUTF8String(key);
         if (displayableKey.contains("SM_UUID")) {
            sysout.println("Nombre de colonnes pour la clé " + displayableKey
                  + " ...");
            ThriftCountQuery<byte[], byte[]> cq = new ThriftCountQuery<byte[], byte[]>(
                  keyspace, bytesSerializer, bytesSerializer);
            cq.setColumnFamily("TermInfoRangeUUID").setKey(key);
            int totalForKey = 0;
            for (int i = 0; i < slices.length; i++) {
               byte[] sliceStart = getTermInfoRangeUUIDSliceBytes(slices[i][0]);
               byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes(slices[i][1]);
               cq.setRange(sliceStart, sliceEnd, maxColPerBloc);
               QueryResult<Integer> r = cq.execute();
               int count = r.get();
               if (count == maxColPerBloc)
                  throw new Exception("Attention : trop de colonnes");
               totalForKey += count;
               sysout.print(" " + totalForKey);
               if (i == blocsToCount - 1)
                  break;
            }
            total += totalForKey;
            sysout.println();
         }
      }
      sysout.println("Nombre total de documents comptés : " + total);
      if (blocsToCount != blocCount) {
         int extrapolation = total * blocCount / blocsToCount;
         sysout.println("Nombre total de documents (extrapolation) : "
               + extrapolation);
      }
   }

   /**
    * Permet de séparer l'espace des UUID en plusieurs tranches. Utile pour
    * faire des traitements par blocs, sur des blocs d'UUID.
    * 
    * @param blocCount
    *           : nombre de tranches à obtenir
    * @return tableau de slices - un slice est un tableau à 2 éléments dont le
    *         1er représente la borne inférieure du slice et le 2ème représente
    *         la borne supérieure.
    * @throws Exception
    */
   private String[][] getUUIDSlices(int blocCount) throws Exception {
      byte[] maxUUID = ConvertHelper
            .hexStringToByteArray("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
      BigInteger max = new BigInteger(1, maxUUID);
      BigInteger delta = max
            .divide(new BigInteger(Integer.toString(blocCount)));
      BigInteger un = new BigInteger("1");
      delta = delta.subtract(un);
      BigInteger b = new BigInteger("0");
      String[][] tab = new String[blocCount][];
      for (int i = 0; i < blocCount; i++) {
         String s1 = ConvertHelper.getHexString(b.toByteArray());
         if (s1.startsWith("00"))
            s1 = s1.substring(2);
         if (s1.equals(""))
            s1 = "00";
         b = b.add(delta);
         if (i == blocCount - 1)
            b = max;
         String s2 = ConvertHelper.getHexString(b.toByteArray());
         if (s2.startsWith("00"))
            s2 = s2.substring(2);
         b = b.add(un);

         // sysout.println(s1 + " - " + s2);
         tab[i] = new String[2];
         tab[i][0] = s1;
         tab[i][1] = s2;
      }
      return tab;
   }

   @Test
   public void testDumpDocuments() throws Exception {
      dumper.dumpCF("Documents", 5);
   }

   @Test
   public void testDumpOneDocument() throws Exception {
      dumper.dumpCF("Documents", "9eba112d-2a44-4d52-ba17-a95cadd4037b");
   }

   @Test
   public void testExtractOneDocument() throws Exception {

      ExtractOneDocument("4a6d4b6a-5fd3-47de-a8bc-b6bfe574cfd3",
            "c:\\divers\\test1.pdf");

      // ExtractOneDocument("2326c0ff-3f13-4ae5-a860-5e494471a5f0",
      // "c:\\temp\\archive_2326c0ff-3f13-4ae5-a860-5e494471a5f0.txt");
      // ExtractOneDocument("74c366f8-39d2-431c-9798-2c1cf64a7f42",
      // "c:\\temp\\archive_74c366f8-39d2-431c-9798-2c1cf64a7f42.txt");
      // ExtractOneDocument("2c2b26b3-f855-4f8c-83d0-29bc02e0b221",
      // "c:\\temp\\archive_2c2b26b3-f855-4f8c-83d0-29bc02e0b221.txt");
      // ExtractOneDocument("cdc7a928-f182-4284-9699-7c670b10aecb",
      // "c:\\temp\\archive_cdc7a928-f182-4284-9699-7c670b10aecb.txt");
      // ExtractOneDocument("74c366f8-39d2-431c-9798-2c1cf64a7f42",
      // "c:\\temp\\archive_74c366f8-39d2-431c-9798-2c1cf64a7f42.txt");
      // ExtractOneDocument("a5cea082-e6c2-487b-95c3-ca20f833cbc8",
      // "c:\\temp\\archive_a5cea082-e6c2-487b-95c3-ca20f833cbc8.txt");
      // ExtractOneDocument("79f7e469-5efd-468e-b8dc-e06daeb27659",
      // "c:\\temp\\archive_79f7e469-5efd-468e-b8dc-e06daeb27659.txt");
      // ExtractOneDocument("720ddb05-da41-496b-aade-1a06c1b47725",
      // "c:\\temp\\archive_720ddb05-da41-496b-aade-1a06c1b47725.txt");
      // ExtractOneDocument("10a3173c-b742-4edb-b77d-3ffdb8f45321",
      // "c:\\temp\\archive_system_last.txt");
      // ExtractOneDocument("747bca57-1d56-4f93-a4ae-36b666a0ba5e",
      // "c:\\temp\\archive_doc_2012-05-24.txt");
      // ExtractOneDocument("346d1ea7-2793-434c-a8f8-40c53ece9ceb",
      // "c:\\temp\\archive_doc_2012-25-25.txt");
      // ExtractOneDocument("264CE288-940E-4E92-8B11-DB02A5CD95EA",
      // "c:\\temp\\test1.pdf");
      // ExtractOneDocument("4989A4D8-DA49-4E48-AB4C-395047EDC5EF",
      // "c:\\temp\\test2.pdf");
      // ExtractOneDocument("5D1D920B-4D97-4ED7-8FF8-B4045EE6F918",
      // "c:\\temp\\test3.pdf");
      // ExtractOneDocument("644BDAAC-6504-4290-80F6-EE07E3D872A9",
      // "c:\\temp\\test4.pdf");
      // ExtractOneDocument("EF639451-15CB-4AB7-94DB-657995C66EC8",
      // "c:\\temp\\test5.pdf");
      // ExtractOneDocument("D1DC3F43-591B-4299-BE87-EE970A62DC90",
      // "c:\\temp\\alex2\\5.pdf");
      // ExtractOneDocument("5F72669A-BECD-4513-849C-ECCEF216001D",
      // "c:\\temp\\alex2\\6.pdf");
      // ExtractOneDocument("017961ff-1899-40a7-8d3d-6d32729780ef",
      // "c:\\temp\\test2.pdf");
      // ExtractOneDocument("0125e102-a096-4db0-9746-edf0c314498a",
      // "c:\\temp\\test3.pdf");
      // ExtractOneDocument("0f5003e0-8698-4405-9804-a098ed6e5575",
      // "c:\\temp\\test4.pdf");
   }

   /***
    * Extrait le corps de la version 1 du document dont l'uuid est passé en
    * paramètre
    * 
    * @param uuid
    *           : uuid du document à extraire
    * @param fileName
    *           : fichier à créer
    * @throws Exception
    */
   private void ExtractOneDocument(String uuid, String fileName)
         throws Exception {
      byte[] fileUuid = uuidToFileUUID(uuid);
      if (fileUuid == null)
         throw new Exception("Pas de fileUUID trouvé pour cet uuid : " + uuid);
      String stringFileUuid = ConvertHelper.getReadableUTF8String(fileUuid);
      ExtractOneDocumentFromFileUUID(stringFileUuid, fileName);
   }

   /***
    * Extrait le corps de la version 1 du document dont l'uuid est passé en
    * paramètre
    * 
    * @param fileUuid
    *           : uuid du fichier à extraire
    * @param fileName
    *           : fichier à créer
    * @throws Exception
    */
   private void ExtractOneDocumentFromFileUUID(String fileUuid, String fileName)
         throws Exception {
      String key = fileUuid.toLowerCase();

      StringSerializer stringSerializer = StringSerializer.get();
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(keyspace, stringSerializer,
                  stringSerializer, bytesSerializer);
      rangeSlicesQuery.setColumnFamily("Documents");
      rangeSlicesQuery.setKeys(key, key);
      rangeSlicesQuery.setRange("chunk_0", "chunk_9", false, 1000);
      QueryResult<OrderedRows<String, String, byte[]>> result = rangeSlicesQuery
            .execute();
      OrderedRows<String, String, byte[]> orderedRows = result.get();

      // On ne reçoit normalement qu'une seule ligne
      Row<String, String, byte[]> row = orderedRows.getByKey(key);
      if (row == null)
         throw new IllegalArgumentException(
               "On n'a pas trouvé de fichier dont l'uuid est " + fileUuid);

      sysout.println("Création du fichier " + fileName + "...");
      File someFile = new File(fileName);
      FileOutputStream fos = new FileOutputStream(someFile);

      ColumnSlice<String, byte[]> columnSlice = row.getColumnSlice();
      List<HColumn<String, byte[]>> columns = columnSlice.getColumns();
      for (HColumn<String, byte[]> column : columns) {
         String name = column.getName();
         sysout.println("Extracting " + name + " ...");
         byte[] value = column.getValue();
         fos.write(value);
      }
      fos.flush();
      fos.close();
   }

   @Test
   public void testDumpTermInfo() throws Exception {
      dumper.printColumnNameInComposite = true;
      dumper.deserializeValue = true;
      dumper.compositeDisplayTypeMapper = new boolean[] { true, true, false };

      // dumper.dumpCF("TermInfo", 10000);

      byte[] startKey = getTermInfoKey("srn", "0123406588");
      dumper.dumpCF_StartKey("TermInfo", startKey, 30);
   }

   @Test
   public void testDumpOneTermInfo() throws Exception {
      dumpOneTermInfo("srn", "0123406588");
      // dumpOneTermInfo("SM_LIFE_CYCLE_REFERENCE_DATE", "20120106124749571");
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
   private byte[] getTermInfoKey(String categorieName, String value)
         throws Exception {
      // Exemple de Clé de terminfo :
      // \x00\x00\x00\x00 \x03 nre \x00\x00 \x04 1234 \x00
      // Les \x03 et \x04 sont les tailles de categorieName et value
      byte[] key = ConvertHelper.hexStringToByteArray("00000000"
            + ConvertHelper.getHexString((byte) categorieName.length())
            + ConvertHelper.stringToHex(categorieName) + "0000"
            + ConvertHelper.getHexString((byte) value.length())
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
   private void dumpOneTermInfo(String categorieName, String value)
         throws Exception {
      byte[] key = getTermInfoKey(categorieName, value);
      dumper.printColumnNameInHex = true;
      dumper.deserializeValue = true;
      dumper.dumpCF("TermInfo", key);
   }

   @Test
   public void testDumpTermInfoRangeDate() throws Exception {
      dumper.deserializeValue = true;
      dumper.printColumnNameInComposite = true;
      dumper.compositeDisplayTypeMapper = new boolean[] { false, true, false };
      dumper.dumpCF("TermInfoRangeDate", 15);
   }

   @Test
   public void testDumpTermInfoRangeDateTime() throws Exception {
      dumper.deserializeValue = true;
      dumper.printColumnNameInComposite = true;
      dumper.compositeDisplayTypeMapper = new boolean[] { false, true, false };
      dumper.dumpCF("TermInfoRangeDatetime", 15);

      /*
       * //SM_LIFE_CYCLE_REFERENCE_DATE byte[] sliceStart =
       * getTermInfoRangeUUIDSliceBytes("20120117000000000"); byte[] sliceEnd =
       * getTermInfoRangeUUIDSliceBytes ("20120518000000000");
       * dumper.dumpCF_slice("TermInfoRangeDatetime", sliceStart, sliceEnd, 50);
       */
   }

   @Test
   public void testDumpTermInfoRangeDouble() throws Exception {
      dumper.dumpCF("TermInfoRangeDouble", 15);
   }

   @Test
   public void testDumpTermInfoRangeFloat() throws Exception {
      dumper.dumpCF("TermInfoRangeFloat", 15);
   }

   @Test
   public void testDumpTermInfoRangeInteger() throws Exception {
      dumper.dumpCF("TermInfoRangeInteger", 15);
   }

   @Test
   public void testDumpTermInfoRangeLong() throws Exception {
      dumper.dumpCF("TermInfoRangeLong", 15);
   }

   @Test
   public void testDumpTermInfoRangeString() throws Exception {
      dumper.deserializeValue = true;
      dumper.printColumnNameInComposite = true;
      dumper.compositeDisplayTypeMapper = new boolean[] { false, true, false };
      dumper.dumpCF("TermInfoRangeString", 30);
   }

   @Test
   public void testDumpTermInfoRangeUUID() throws Exception {
      dumper.deserializeValue = true;
      dumper.printColumnNameInComposite = true;
      dumper.compositeDisplayTypeMapper = new boolean[] { false, true, false };

      // dumper.dumpCF("TermInfoRangeUUID", 15);

      // byte[]sliceStart = new byte[0];
      // byte[]sliceEnd = new byte[0];
      byte[] sliceStart = getTermInfoRangeUUIDSliceBytes("1234");
      byte[] sliceEnd = getTermInfoRangeUUIDSliceBytes("1235");
      dumper.dumpCF_slice("TermInfoRangeUUID", sliceStart, sliceEnd, 15);
   }

   private Composite getTermInfoRangeUUIDSliceComposite(String docUUID) {
      /*
       * La CF TermInfoRangeUUID est déclarée ainsi : create column family
       * TermInfoRangeUUID with comparator = 'CompositeType(UTF8Type, UUIDType,
       * UTF8Type)'
       * 
       * La 1ere partie du composite est l'uuid du document codé en UTF8 La 1ere
       * partie du composite est l'uuid du document codé en bytes !!!! La 1ere
       * partie du composite est une chaîne correspondant à la version du
       * document ("0.0.0")
       */

      Composite c = new Composite();
      c.addComponent(docUUID, StringSerializer.get());
      c.addComponent(ConvertHelper
            .hexStringToByteArray("00000000000000000000000000000000"),
            BytesArraySerializer.get());
      c.addComponent("", StringSerializer.get());
      return c;
   }

   private byte[] getTermInfoRangeUUIDSliceBytes(String docUUID) {
      Composite c = getTermInfoRangeUUIDSliceComposite(docUUID);
      return new CompositeSerializer().toBytes(c);
   }

   @Test
   public void testDumpNotes() throws Exception {
      dumper.dumpCF("Notes", 15);
   }

   @Test
   public void testDumpAnnotations() throws Exception {
      dumper.dumpCF("Annotations", 15);
   }

   @Test
   public void testDumpBaseCategoriesReference() throws Exception {
      dumper.dumpCF("BaseCategoriesReference", 500);
   }

   @Test
   public void testDumpCategoriesReference() throws Exception {
      dumper.dumpCF("CategoriesReference", 100);
   }

   @Test
   public void testDumpBasesReference() throws Exception {
      dumper.dumpCF("BasesReference", 150);
   }

   @Test
   public void testDumpCompositeIndexesReference() throws Exception {
      dumper.dumpCF("CompositeIndexesReference", 15);
   }

   @Test
   public void testDumpIndexReference() throws Exception {
      dumper.dumpCF("IndexReference", 15);
   }

   @Test
   public void testDumpJobs() throws Exception {
      dumper.dumpCF("Jobs", 15);
   }

   @Test
   public void testDumpLifeCycleRules() throws Exception {
      dumper.dumpCF("LifeCycleRules", 2000);
   }

   @Test
   public void testDumpStaticDictionaries() throws Exception {
      dumper.dumpCF("StaticDictionaries", 15);
   }

   @Test
   public void testDumpIndexCounter() throws Exception {
      dumper.dumpCF("IndexCounter", 50);
   }

   @Test
   public void testDumpSystemEventLog() throws Exception {
      dumper.dumpCF("SystemEventLog", 30);
      // dumper.dumpCF("SystemEventLog",
      // "21a80940-d38a-4e49-9aad-9269fd778e08");
   }

   @Test
   public void SystemEventLogByTime() throws Exception {
      dumper.dumpCF("SystemEventLogByTime", 15);
   }

   @Test
   public void testDumpDocEventLog() throws Exception {
      dumper.dumpCF("DocEventLog", 30);
      // dumper.dumpCF("DocEventLog", "41a275d4-4561-437d-a1be-b6287119445b");
      // dumper.dumpCF("DocEventLog", "36f3a4ff-d065-4289-af18-f67482bdf13e");
      // dumper.dumpCF("DocEventLog", "f781b54c-d10a-4931-a9a5-c042eb76c56e");
   }

   @Test
   public void testDumpDocEventLogByTime() throws Exception {
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("DocEventLogByTime", 150);
      // dumper.dumpCF("DocEventLogByTime", "20120524");
   }

   @Test
   public void testDumpUser() throws Exception {
      dumper.dumpCF("User", 15);
   }

   @Test
   public void testDumpUserGroup() throws Exception {
      dumper.dumpCF("UserGroup", 15);
   }

   @Test
   public void testDumpUserSearchFilters() throws Exception {
      dumper.dumpCF("UserSearchFilters", 15);
   }

   @Test
   public void testDumpVersions() throws Exception {
      dumper.dumpCF("Versions", 15);
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
      List<byte[]> keys = dumper.getKeys("TermInfo", 5000);
      for (int i = 0; i < keys.size(); i++) {
         byte[] key = keys.get(i);
         String displayableKey = ConvertHelper.getReadableUTF8String(key);
         if (displayableKey.contains("srn")) {
            // dumper.dumpCF("TermInfo", key);
            BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
            SliceQuery<byte[], byte[], byte[]> query = HFactory
                  .createSliceQuery(keyspace, bytesSerializer, bytesSerializer,
                        bytesSerializer);
            query.setColumnFamily("TermInfo");
            query.setKey(key);
            query.setRange(new byte[0], new byte[0], false, 100);
            QueryResult<ColumnSlice<byte[], byte[]>> result = query.execute();
            ColumnSlice<byte[], byte[]> slice = result.get();
            List<HColumn<byte[], byte[]>> columns = slice.getColumns();
            // sysout.println("Key :" + displayableKey);
            Map<String, List<String>> docs = new HashMap<String, List<String>>();
            for (HColumn<byte[], byte[]> column : columns) {
               byte[] value = column.getValue();
               // La valeur est une map sérialisée. On la désérialise
               ByteArrayInputStream bis = new ByteArrayInputStream(value);
               ObjectInputStream ois = new ObjectInputStream(bis);
               Map<String, ArrayList<String>> map = (Map) ois.readObject();
               // sysout.println(map);
               String siren = (String) map.get("srn").get(0);
               String title = (String) map.get("SM_TITLE").get(0);
               String date = (String) map.get("SM_ARCHIVAGE_DATE").get(0);
               String uuid = (String) map.get("SM_UUID").get(0);
               if (!docs.containsKey(title))
                  docs.put(title, new ArrayList<String>());
               docs.get(title).add(
                     "Siren : " + siren + "  Title : " + title + "  Date : "
                           + date + "  UUID : " + uuid);
            }
            for (Map.Entry<String, List<String>> doc : docs.entrySet()) {
               List<String> list = doc.getValue();
               if (list.size() > 1) {
                  compteurKO++;
                  for (String element : list) {
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
   public void testCQL() throws Exception {
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      CqlQuery<byte[], byte[], byte[]> cqlQuery = new CqlQuery<byte[], byte[], byte[]>(
            keyspace, bytesSerializer, bytesSerializer, bytesSerializer);
      // String query = "select * from DocInfo where Key ='" +
      // ConvertHelper.stringToHex("DOCUBASE") + "efbfbf" +
      // ConvertHelper.stringToHex("dd258958-24ff-486e-84b8-4f6b2714aaff") +
      // "'";
      // String query = "select * from DocInfo where Key ='" +
      // ConvertHelper.stringToHex("DOCUBASE") + "efbfbf" +
      // ConvertHelper.stringToHex("d6db9900-4e83-401f-ac82-21e88b804503") +
      // "'";
      // String query = "select * from BasesReference where Key ='" +
      // ConvertHelper.stringToHex("SAE") + "'";
      String query = "select * from Documents where Key ='"
            + ConvertHelper
                  .stringToHex("dd258958-24ff-486e-84b8-4f6b2714aaff_v1") + "'";
      // String query = "select * from SystemEventLog WHERE KEY >'" +
      // ConvertHelper.stringToHex("20110819000000000") + "' LIMIT 100";
      // String query = "select * from DocEventLogByTime WHERE KEY >'" +
      // ConvertHelper.stringToHex("20110819000000000") + "' LIMIT 100";
      // String query = "select * from DocEventLog WHERE KEY >'" +
      // ConvertHelper.stringToHex("d6db9900-4e83-401f-ac82-21e88b804503") +
      // "' LIMIT 50";
      cqlQuery.setQuery(query);
      QueryResult<CqlRows<byte[], byte[], byte[]>> result = cqlQuery.execute();
      dumper.dumpCqlQueryResult(result);
   }

   @Test
   public void testDelete() throws Exception {
      StringSerializer keySerializer = StringSerializer.get();
      Mutator<String> mutator = HFactory.createMutator(keyspace, keySerializer);
      StringSerializer nameSerializer = keySerializer;
      String cf = "Documents";
      String key = "156432135";
      mutator.delete(key, cf, null, nameSerializer);
   }

   public void testUpdate() {
      ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "myColFamily", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = template
            .createUpdater("a key");
      updater.setString("domain", "www.datastax.com");

   }

   @SuppressWarnings("deprecation")
   @Test
   public void testEntityManager() {

      EntityManagerImpl em = new EntityManagerImpl(keyspace,
            "fr.urssaf.hectotest");

      /*
       * MyPojo pojo1 = new MyPojo(); pojo1.setId(UUID.randomUUID());
       * pojo1.setLongProp1(123L);
       * 
       * em.save(pojo1);
       */

      MyPojo pojo2 = em.load(MyPojo.class, UUID.randomUUID());

      if (pojo2 == null) {
         sysout.println("Entity non trouvée");
      } else {
         sysout.println("Long prop = " + pojo2.getLongProp1());
      }
   }

   @Test
   public void testDumpBatchCounter() throws Exception {
      dumper.dumpCF("BatchCounter", 10);
   }

   @Test
   public void testDumpDocEventLogByTimeSerialized() throws Exception {
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("DocEventLogByTimeSerialized", 150);
   }

   @Test
   public void testDumpDocInfoFreezer() throws Exception {
      dumper.dumpCF("DocInfoFreezer", 50);
   }

   @Test
   public void testDumpJobInstance() throws Exception {
      dumper.dumpCF("JobInstance", 50);
   }

   @Test
   public void testDumpJobExecution() throws Exception {
      dumper.dumpCF("JobExecution", 50);
   }

   @Test
   public void testDumpLocker() throws Exception {
      dumper.dumpCF("Locker", 50);
   }

   @Test
   public void testDumpStepExecution() throws Exception {
      dumper.dumpCF("StepExecution", 100);
   }

   @Test
   public void testDumpSystemEventLogByTime() throws Exception {
      dumper.dumpCF("SystemEventLogByTime", 50);
   }

   @Test
   public void testDumpSystemEventLogByTimeSerialized() throws Exception {
      dumper.printColumnNameInHex = true;
      dumper.dumpCF("SystemEventLogByTimeSerialized", 50);
   }

}
