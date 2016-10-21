package fr.urssaf.image.sae.test.dfce17;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public class DesindexMontantSicomorTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(DesindexMontantSicomorTest.class);
   
   // Developpement 
   //private String hosts = "cer69imageint10.cer69.recouv";
   
   // Developpement 
   //private String hosts = "cer69imageint9.cer69.recouv";
   
   // Recette interne GNT
   //private String hosts = "cnp69devgntcas1.gidn.recouv:9160,cnp69devgntcas2.gidn.recouv:9160";
   
   // Recette interne GNS
   //private String hosts = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
   
   // Integration cliente GNT
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   
   // Integration cliente GNS
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   
   // Integration nationale GNT
   //private String hosts = "cnp69gingntcas1.cer69.recouv:9160,cnp69gingntcas2.cer69.recouv:9160";
   
   // Integration nationale GNS
   //private String hosts = "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160";
   
   // Validation nationale GNT
   //private String hosts = "cnp69givngntcas1.cer69.recouv:9160,cnp69givngntcas2.cer69.recouv:9160,cnp69givngntcas3.cer69.recouv:9160";
   
   // Validation nationale GNS
   //private String hosts = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
   
   // Pre-prod MOE
   //private String hosts = "cnp69pprodsaecas1.cer69.recouv:9160,cnp69pprodsaecas2.cer69.recouv:9160,cnp69pprodsaecas3.cer69.recouv:9160,cnp69pprodsaecas4.cer69.recouv:9160,cnp69pprodsaecas5.cer69.recouv:9160,cnp69pprodsaecas6.cer69.recouv:9160";
   
   // Prod nationale GNT
   //private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   
   // Prod nationale GNS
   private String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";

   private Keyspace getKeyspaceDocubaseFromKeyspace() {
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
   
   private List<String> getIndexedMetadatas(Keyspace keyspaceDocubase) {
      
      List<String> indexedMetadatas = new ArrayList<String>();
      
      LOGGER.info("Recuperation de la liste des categories indexées");
      RangeSlicesQuery<byte[], String, byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("BaseCategoriesReference");
      rangeQueryDocubase.setKeys(null, null);
      rangeQueryDocubase.setColumnNames("indexed", "computed", "categoryReference");
      rangeQueryDocubase.setRowCount(1000);
      
      AllRowsIterator<byte[], String, byte[]> iterateur = new AllRowsIterator<byte[], String, byte[]>(rangeQueryDocubase);
      while (iterateur.hasNext()) {
         Row<byte[], String, byte[]> row = iterateur.next();
         
         HColumn<String, byte[]> isIndexed = row.getColumnSlice().getColumnByName("indexed");
         if (isIndexed != null) {
            boolean valeur = BooleanSerializer.get().fromBytes(isIndexed.getValue());
            if (!valeur) {
               continue;
            }
         }
         HColumn<String, byte[]> isComputed = row.getColumnSlice().getColumnByName("computed");
         if (isComputed != null) {
            boolean valeur = BooleanSerializer.get().fromBytes(isComputed.getValue());
            if (!valeur) {
               continue;
            }
         }
         HColumn<String, byte[]> categoryReference = row.getColumnSlice().getColumnByName("categoryReference");
         indexedMetadatas.add(StringSerializer.get().fromBytes(categoryReference.getValue()));
      }
      
      return indexedMetadatas;
   }
   
   private List<String> getCompositeIndexesComputed(Keyspace keyspaceDocubase) {
      
      List<String> computedCompositeIndexes = new ArrayList<String>();
      
      LOGGER.info("Recuperation de la liste des index composites indexés");
      RangeSlicesQuery<String, String, byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("CompositeIndexesReference");
      rangeQueryDocubase.setKeys(null, null);
      rangeQueryDocubase.setColumnNames("computed");
      rangeQueryDocubase.setRowCount(1000);
      
      AllRowsIterator<String, String, byte[]> iterateur = new AllRowsIterator<String, String, byte[]>(rangeQueryDocubase);
      while (iterateur.hasNext()) {
         Row<String, String, byte[]> row = iterateur.next();
         
         HColumn<String, byte[]> isComputed = row.getColumnSlice().getColumnByName("computed");
         if (isComputed != null) {
            boolean valeur = BooleanSerializer.get().fromBytes(isComputed.getValue());
            if (!valeur) {
               continue;
            }
         }
         computedCompositeIndexes.add(row.getKey());
      }
      
      return computedCompositeIndexes;
   }
   
   @Test
   public void listerIndex() {
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      List<String> indexedMetadatas = getIndexedMetadatas(keyspaceDocubase);
      for (String metadata : indexedMetadatas) {
         LOGGER.info("{}", metadata);
      }
      List<String> computedCompositeIndexes = getCompositeIndexesComputed(keyspaceDocubase);
      for (String indexComposite : computedCompositeIndexes) {
         LOGGER.info("{}", indexComposite);
      }
   }
   
   @Test
   public void findTermInfoNonIndexed() {
      
      long compteur = 0;
      long compteurDoc = 0;
      List<String> listeIndexNonIndexed = new ArrayList<String>();
      
      String[] metaSysteme = new String[] {
            "SM_MODIFICATION_DATE",
            "SM_LIFE_CYCLE_REFERENCE_DATE",
            "SM_UUID",
            "SM_ARCHIVAGE_DATE",
            "SM_CREATION_DATE",
            "SM_FINAL_DATE",
            "SM_IS_FROZEN"
      };
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      // recupere la liste des index
      List<String> indexedMetadatas = getIndexedMetadatas(keyspaceDocubase);
      List<String> computedCompositeIndexes = getCompositeIndexesComputed(keyspaceDocubase);
      List<String> allIndex = new ArrayList<String>(indexedMetadatas);
      allIndex.addAll(computedCompositeIndexes);
      allIndex.addAll(Arrays.asList(metaSysteme));
      
      LOGGER.debug("Recuperation de la liste des index (TermInfo)");
      
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("TermInfo").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(10000);
      
      AllRowsIterator<Composite, byte[], byte[]> iterateur = new AllRowsIterator<Composite, byte[], byte[]>(rangeQueryDocubase);
      
      while (iterateur.hasNext()) {
         Row<Composite, byte[], byte[]> row = iterateur.next();
         
         // la key est compose de :
         // - le nom de l'index
         // - le champs d'un term
         // - le texte d'un term
         
         String indexName = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
         String termField = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
         String termText = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
         LOGGER.debug("Row key {}:{}:{}", new String[] { indexName, termField, termText });
         
         boolean verif = true;
         for (String meta : allIndex) {
            if (termField.equals(meta)) {
               verif = false;
               break;
            }
         }
         
         if (verif) {
            if (!listeIndexNonIndexed.contains(termField)) {
               listeIndexNonIndexed.add(termField);
            }
         }
         compteur++;
         if (termField.equals("SM_UUID")) {
            compteurDoc++;
         }
      }
      LOGGER.debug("Nb resultat : {}, nb docs {}", compteur, compteurDoc);
      
      for (String index : listeIndexNonIndexed) {
         LOGGER.debug("L'index {} ne devrait pas être présent dans TermInfo", new String[] {index });
      }
   }
   
   @Test
   public void findTermInfoByIndexName() {
      
      long compteur = 0;
      long compteurDoc = 0;
      boolean deleteIndex = false;
      ConcurrentHashMap<String, Long> mapIndex = new ConcurrentHashMap<String, Long>();
      String[] containIndex = new String[] {
            "mde",
            "mre"
      };
      
      ExecutorService executor = Executors.newFixedThreadPool(5);
      
      LOGGER.debug("Recuperation de la liste des index (TermInfo)");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("TermInfo").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(1000);
      
      AllRowsIterator<Composite, byte[], byte[]> iterateur = new AllRowsIterator<Composite, byte[], byte[]>(rangeQueryDocubase);
      
      while (iterateur.hasNext()) {
         Row<Composite, byte[], byte[]> row = iterateur.next();
         
         // la key est compose de :
         // - le nom de l'index
         // - le champs d'un term
         // - le texte d'un term
         
         String indexName = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
         String termField = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
         String termText = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
         //LOGGER.debug("Row key {}:{}:{}", new String[] { indexName, termField, termText });
         
         boolean verif = false;
         for (String meta : containIndex) {
            if (termField.contains(meta)) {
               verif = true;
               break;
            }
         }
         
         if (verif) {
            
            GetDataTermInfo getData = new GetDataTermInfo(keyspaceDocubase, row.getKey(), mapIndex, deleteIndex);
            executor.execute(getData);
         }
         compteur++;
         if (compteur % 100000 == 0) {
            LOGGER.info("{} index analysés", compteur);
         }
         if (termField.equals("SM_UUID")) {
            compteurDoc++;
         }
      }
      LOGGER.debug("Nb resultat : {}, nb docs {}", compteur, compteurDoc);
      executor.shutdown();
      while (!executor.isTerminated()) {
      }
      
      for (String index : mapIndex.keySet()) {
         LOGGER.debug("L'index {} contient {} docs", new String[] {index, mapIndex.get(index).toString() });
      }
   }
   
   @Test
   public void findTermInfoRangeKeyBizarre() throws IOException {
      
      String[] cfRanges = {
            "TermInfoRangeDate",
            "TermInfoRangeDatetime", 
            "TermInfoRangeDouble",
            "TermInfoRangeFloat",
            "TermInfoRangeInteger",
            "TermInfoRangeLong",
            "TermInfoRangeString",
            "TermInfoRangeUUID",
      };
      
      for (String cfName : cfRanges) {
         
         Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
         RangeSlicesQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubase.setColumnFamily(cfName);
         queryDocubase.setRange(null, null, false, 1);
         AllRowsIterator<byte[], Composite, byte[]> iterRow = new AllRowsIterator<byte[], Composite, byte[]>(queryDocubase);
         while (iterRow.hasNext()) {
            Row<byte[], Composite, byte[]> row = iterRow.next();

            // les rows sans colonnes sont des rows "supprimees"
            if (!row.getColumnSlice().getColumns().isEmpty()) {
               
               // l'index a au moins une valeur
               // la cle de l'index est compose de :
               // - le nom de l'index
               // - le nom de la categorie
               // - l'uuid de la base
               // - le nombre de row de la categorie
               Composite compositeKey = CompositeSerializer.get().fromBytes(row.getKey());
               
               if (compositeKey.size() < 4) {
                  byte[] rowKey = BytesArraySerializer.get().fromBytes(row.getKey());
                  LOGGER.info("Cle a supprimer dans la CF {} :  {}", cfName, bytesToHex(rowKey));
               } 
            }
         }
      }
   }
   
   @Test
   public void findTermInfoRangeKeyBizarre2() throws IOException {
      
      String[] cfRanges = {
            /*"TermInfoRangeDate",
            "TermInfoRangeDatetime",
            "TermInfoRangeDouble",
            "TermInfoRangeFloat",
            "TermInfoRangeInteger",
            "TermInfoRangeLong",*/
            "TermInfoRangeString"/*,
            "TermInfoRangeUUID",*/
      };
      
      for (String cfName : cfRanges) {
         
         Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
         RangeSlicesQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubase.setColumnFamily(cfName);
         queryDocubase.setReturnKeysOnly();
         AllRowsIterator<byte[], Composite, byte[]> iterRow = new AllRowsIterator<byte[], Composite, byte[]>(queryDocubase);
         while (iterRow.hasNext()) {
            Row<byte[], Composite, byte[]> row = iterRow.next();

            // l'index a au moins une valeur
            // la cle de l'index est compose de :
            // - le nom de l'index
            // - le nom de la categorie
            // - l'uuid de la base
            // - le nombre de row de la categorie
            Composite compositeKey = CompositeSerializer.get().fromBytes(row.getKey());
            
            if (compositeKey.size() < 4) {
               byte[] rowKey = BytesArraySerializer.get().fromBytes(row.getKey());
               
               Object[] key = (Object[]) ObjectSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(0));
               
               LOGGER.info("Cle a supprimer dans la CF {} ({}):  {}", new String[] { cfName, key[1].toString(), bytesToHex(rowKey) });
            }
         }
      }
   }
   
   @Test
   public void writeTombstone() throws IOException {
      
      //String cfName = "TermInfoRangeUUID";
      //String hexaKey = "00D2ACED0005757200135B4C6A6176612E6C616E672E4F626A6563743B90CE589F1073296C020000787000000004740000740007534D5F555549447372000E6A6176612E7574696C2E55554944BC9903F7986D852F0200024A000C6C65617374536967426974734A000B6D6F73745369674269747378708D9F30DF291386BBD08FD434B7684FAD737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000";
      
      String cfName = "TermInfoRangeString";
      String hexaKey = "00CEACED0005757200135B4C6A6176612E6C616E672E4F626A6563743B90CE589F1073296C02000078700000000474000074000373726E7372000E6A6176612E7574696C2E55554944BC9903F7986D852F0200024A000C6C65617374536967426974734A000B6D6F7374536967426974737870BAA7EBAEE6667FCD5871886A1041422E737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000";
      
      //String cfName = "TermInfoRangeDatetime";
      //String hexaKey = "00E1ACED0005757200135B4C6A6176612E6C616E672E4F626A6563743B90CE589F1073296C0200007870000000047400007400164C4F475F415243484956455F424547494E5F444154457372000E6A6176612E7574696C2E55554944BC9903F7986D852F0200024A000C6C65617374536967426974734A000B6D6F73745369674269747378708D9F30DF291386BBD08FD434B7684FAD737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000";
      
      byte[] key = new BigInteger(hexaKey,16).toByteArray();
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      LOGGER.info("{}", hexaKey);
      
      for (int index = 0; index < 100000; index++) {
      
         // creation d'un nom de colonnes
         Composite columnName = new Composite();
         columnName.add(UUID.randomUUID().toString());
         columnName.add(UUID.randomUUID());
         columnName.add("9.9.9");
         
         Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase, BytesArraySerializer.get());
         mutator.delete(key, cfName, columnName, CompositeSerializer.get());
         
         if (index % 10000 == 0) {
            LOGGER.info("{} tombstone(s) ajoute", index);
         }
      }
   }

   class GetDataTermInfo implements Runnable {
      
      private Keyspace keyspaceDocubase;
      private Composite key;
      private ConcurrentHashMap<String, Long> mapIndex;
      private boolean deleteIndex;
      
      public GetDataTermInfo(Keyspace keyspace, Composite rowKey, ConcurrentHashMap<String, Long> mapIndex, boolean deleteIndex) {
         this.keyspaceDocubase = keyspace;
         this.key = rowKey;
         this.mapIndex = mapIndex;
         this.deleteIndex = deleteIndex;
      }

      @Override
      public void run() {
         
         String termField = StringSerializer.get().fromByteBuffer((ByteBuffer) key.get(1));
         
         SliceQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubase.setColumnFamily("TermInfo");
         queryDocubase.setKey(CompositeSerializer.get().toBytes(key));
         AllColumnsIterator<Composite, byte[]> iterColonne = new AllColumnsIterator<Composite, byte[]>(queryDocubase);
         while (iterColonne.hasNext()) {
            HColumn<Composite, byte[]> colonne = iterColonne.next();
            
            // Le nom de la colonne est composé de 
            // - l'uuid de la base
            // - l'uuid du document
            // - 0.0.0
            //UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)); 
            UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
            //String version = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(2));
            if (!mapIndex.containsKey(termField)) {
               mapIndex.put(termField, Long.valueOf(1));
            } else {
               mapIndex.put(termField, mapIndex.get(termField) + 1);
            }
         }
         // on teste si on doit supprimer toute la row
         if (deleteIndex) {
            Mutator<Composite> mutator = HFactory.createMutator(keyspaceDocubase, CompositeSerializer.get());
            mutator.addDeletion(key, "TermInfo");
            mutator.execute();
            
            String typeStockage = StringSerializer.get().fromByteBuffer((ByteBuffer) key.get(0));
            String indexName = StringSerializer.get().fromByteBuffer((ByteBuffer) key.get(1));
            String valeurIndex = StringSerializer.get().fromByteBuffer((ByteBuffer) key.get(2));
            LOGGER.debug("Suppression de la row {}:{}:{}", new String[] { typeStockage, indexName, valeurIndex });
         }
      }
      
   }
}

