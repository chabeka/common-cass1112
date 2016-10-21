package fr.urssaf.image.sae.test.dfce17;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public class TermInfoTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(TermInfoTest.class);
   
   // Developpement 
   private String hosts = "cer69imageint10.cer69.recouv";
   private String nomBase = "SAE-INT";
   
   // Recette interne GNT
   //private String hosts = "cnp69devgntcas1.gidn.recouv:9160,cnp69devgntcas2.gidn.recouv:9160";
   //private String nomBase = "GNT-DEV";
   
   // Integration cliente GNS
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   //private String nomBase = "SAE-INT";
   
   // Integration cliente GNT
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   //private String nomBase = "GNT-INT";
   
   // Pre-prod MOE
   //private String hosts = "cnp69pprodsaecas1.cer69.recouv:9160,cnp69pprodsaecas2.cer69.recouv:9160,cnp69pprodsaecas3.cer69.recouv:9160,cnp69pprodsaecas4.cer69.recouv:9160,cnp69pprodsaecas5.cer69.recouv:9160,cnp69pprodsaecas6.cer69.recouv:9160";
   //private String nomBase = "SAE-PROD";
   
   // Prod nationale GNT
   //private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   //private String nomBase = "GNT-PROD";

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
   
   @Test
   public void findDocumentsByUUID() {
      
      long compteur = 0;
      UUID idFileToFind = UUID.fromString("e876fdd1-71f6-4295-8530-17acefa87f0d");
      
      LOGGER.debug("Recuperation du document (Documents)");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("Documents").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(200000);
      QueryResult<OrderedRows<byte[], byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], byte[], byte[]> row = iterateur.next();
            
            String idDoc = new String(row.getKey());
            compteur++;
            
            if (idDoc.equals(idFileToFind.toString())) {
               LOGGER.debug("Row key {}", new String[] { idDoc });
               SliceQuery<byte[], byte[], byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
               queryDocubase.setColumnFamily("Documents");
               queryDocubase.setKey(row.getKey());
               AllColumnsIterator<byte[], byte[]> iterColonne = new AllColumnsIterator<byte[], byte[]>(queryDocubase);
               long nbColonne = 0;
               while (iterColonne.hasNext()) {
                  iterColonne.next();
                  nbColonne++;
               }
               LOGGER.debug("    {} colonnes", nbColonne );
               
               break;
            }
         }
      }
      LOGGER.debug("Nb resultat : {}", compteur);
   }
   
   @Test
   public void findDocInfoByUUID() {
      
      long compteur = 0;
      UUID idDocToFind = UUID.fromString("55c8cbfa-2980-4e8e-b5af-02f1ad121237");
      //UUID idDocToFind = UUID.fromString("e876fdd1-71f6-4295-8530-17acefa87f0d");
      //UUID idDocToFind = UUID.fromString("c7a36368-c3f9-436b-8e94-a1f4076d7dff");
      
      LOGGER.debug("Recuperation de la liste des index (DocInfo)");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("DocInfo").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(200000);
      QueryResult<OrderedRows<Composite, byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<Composite, byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<Composite, byte[], byte[]> row = iterateur.next();
            
            // la key est compose de :
            // - UUID du document
            // - 0.0.0
            
            UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
            String version = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
            
            if (idDocToFind.toString().equals(idDoc.toString())) {
               compteur++;
               LOGGER.debug("Row key {}:{}", new String[] { idDoc.toString(), version });
               SliceQuery<byte[], byte[], byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
               queryDocubase.setColumnFamily("DocInfo");
               queryDocubase.setKey(CompositeSerializer.get().toBytes(row.getKey()));
               AllColumnsIterator<byte[], byte[]> iterColonne = new AllColumnsIterator<byte[], byte[]>(queryDocubase);
               long nbColonne = 0;
               while (iterColonne.hasNext()) {
                  iterColonne.next();
                  nbColonne++;
               }
               LOGGER.debug("    {} colonnes", nbColonne );
               break;
            }
         }
      }
      LOGGER.debug("Nb resultat : {}", compteur);
   }
   
   @Test
   public void findTermInfoByUUID() {
      
      long compteur = 0;
      UUID idDocToFind = UUID.fromString("55c8cbfa-2980-4e8e-b5af-02f1ad121237");
      //UUID idDocToFind = UUID.fromString("87a2e6c3-9f66-455c-b98f-5c870b83bfb8");
      //UUID idDocToFind = UUID.fromString("c7a36368-c3f9-436b-8e94-a1f4076d7dff");
      List<String> listeIndex = new ArrayList<String>();
      
      LOGGER.debug("Recuperation de la liste des index (TermInfo)");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("TermInfo").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(700000);
      QueryResult<OrderedRows<Composite, byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<Composite, byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
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
            
            SliceQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
            queryDocubase.setColumnFamily("TermInfo");
            queryDocubase.setKey(CompositeSerializer.get().toBytes(row.getKey()));
            AllColumnsIterator<Composite, byte[]> iterColonne = new AllColumnsIterator<Composite, byte[]>(queryDocubase);
            long nbColonne = 0;
            while (iterColonne.hasNext()) {
               HColumn<Composite, byte[]> colonne = iterColonne.next();
               
               // Le nom de la colonne est composé de 
               // - l'uuid de la base
               // - l'uuid du document
               // - 0.0.0
               //UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)); 
               UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
               //String version = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(2));
               if (idDocToFind.toString().equals(idDoc.toString())) {
                  listeIndex.add(termField);
               }
               nbColonne++;
            }
            LOGGER.debug("    {} colonnes", nbColonne );
         }
      }
      LOGGER.debug("Nb resultat : {}", compteur);
      
      for (String index : listeIndex) {
         LOGGER.debug("Le document {} est present dans l'index {}", new String[] {idDocToFind.toString(), index});
      }
   }
   
   @Test
   public void findTermInfoRangeByUUID() {
      
      long compteur = 0;
      UUID idDocToFind = UUID.fromString("87a2e6c3-9f66-455c-b98f-5c870b83bfb8");
      //UUID idDocToFind = UUID.fromString("c7a36368-c3f9-436b-8e94-a1f4076d7dff");
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
         List<String> listeIndex = new ArrayList<String>();
         
         LOGGER.debug("Recuperation de la liste des index (" + cfName + ")");
         Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
         RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
         rangeQueryDocubase.setColumnFamily(cfName).setKeys(null, null);
         rangeQueryDocubase.setReturnKeysOnly();
         rangeQueryDocubase.setRowCount(200000);
         QueryResult<OrderedRows<Composite, byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
         if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
            Iterator<Row<Composite, byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
            while (iterateur.hasNext()) {
               Row<Composite, byte[], byte[]> row = iterateur.next();
               
               // la key est compose de :
               // - le nom de l'index
               // - le nom de la categorie
               // - l'uuid de la base
               // - le nombre de row de la categorie
               String indexName = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
               String catName = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
               UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
               byte[] catRowNb = BytesArraySerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(3));
               long nbCatRow = convertByteToLong(catRowNb);
               
               LOGGER.debug("Row key {}:{}:{}:{}", new String[] { indexName, catName, baseId.toString(), Long.toString(nbCatRow) });
               
               SliceQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
               queryDocubase.setColumnFamily(cfName);
               queryDocubase.setKey(CompositeSerializer.get().toBytes(row.getKey()));
               AllColumnsIterator<Composite, byte[]> iterColonne = new AllColumnsIterator<Composite, byte[]>(queryDocubase);
               long nbColonne = 0;
               while (iterColonne.hasNext()) {
                  HColumn<Composite, byte[]> colonne = iterColonne.next();
                  // Le nom de la colonne est composé de 
                  // - l'uuid de la base
                  // - l'uuid du document
                  // - 0.0.0
                  //UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)); 
                  UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
                  //String version = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(2));
                  if (idDocToFind.toString().equals(idDoc.toString())) {
                     listeIndex.add(catName);
                  }
                  nbColonne++;
               }
               LOGGER.debug("    {} colonnes", nbColonne );
            }
         }
         LOGGER.debug("Nb resultat : {}", compteur);
         
         for (String index : listeIndex) {
            LOGGER.debug("Le document {} est present dans l'index {} de la CF {}", new String[] {idDocToFind.toString(), index, cfName});
         }
      }
   }
   
   @Test
   public void findAllIndexReference() {
      
      long compteur = 0;
      
      LOGGER.debug("Recuperation de la liste des index de reference");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("IndexReference").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            LOGGER.debug("Row key {}", new String[] { new String(row.getKey()) });
            SliceQuery<byte[], String, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
            queryDocubase.setColumnFamily("IndexReference");
            queryDocubase.setKey(row.getKey());
            AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
            while (iterColonne.hasNext()) {
               HColumn<String, byte[]> colonne = iterColonne.next();
               if (colonne.getName().equals("baseUUID")) {
                  LOGGER.debug("    {} : {}", new Object[] { colonne.getName(), UUIDSerializer.get().fromBytes(colonne.getValue()).toString() });
               } else if (colonne.getName().equals("distinctIndexUseCount") || colonne.getName().equals("totalIndexUseCount") || colonne.getName().equals("rangeIndexes.size") || colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
                  LOGGER.debug("    {} : {}", new Object[] { colonne.getName(), convertByteToLong(colonne.getValue()).toString() });
               } else {
                  LOGGER.debug("    {} : {}", new Object[] { colonne.getName(), new String(colonne.getValue()) });
               }
            }
            compteur++;
         }
      }
      LOGGER.debug("Nb resultat : {}", compteur);
   }
   
   private void deleteDocument(Keyspace keyspaceDocubase, List<String> listeIdFile) {
      long nbSuppression = 0;
      RangeSlicesQuery<byte[],byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("Documents").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(200000);
      QueryResult<OrderedRows<byte[], byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], byte[], byte[]> row = iterateur.next();
            
            String idFile = new String(row.getKey());
            
            if (listeIdFile.contains(idFile)) {
               SliceQuery<byte[], byte[], byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
               queryDocubase.setColumnFamily("Documents");
               queryDocubase.setKey(row.getKey());
               AllColumnsIterator<byte[], byte[]> iterColonne = new AllColumnsIterator<byte[], byte[]>(queryDocubase);
               long nbColonne = 0;
               while (iterColonne.hasNext()) {
                  iterColonne.next();
                  nbColonne++;
               }
               if (nbColonne > 0) {
                  // supprime la row
                  Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase, BytesArraySerializer.get());
                  mutator.addDeletion(row.getKey(), "Documents");
                  mutator.execute();
                  nbSuppression++;
               }
            }
         }
      }
      LOGGER.debug("Nb Documents supprimés : {}", nbSuppression);
   }
   
   private void deleteDocInfo(Keyspace keyspaceDocubase, List<String> listeIdDoc) {
      long nbSuppression = 0;
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("DocInfo").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(200000);
      QueryResult<OrderedRows<Composite, byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<Composite, byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<Composite, byte[], byte[]> row = iterateur.next();
            
            // la key est compose de :
            // - UUID du document
            // - 0.0.0
            
            UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
            //String version = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
            
            if (listeIdDoc.contains(idDoc.toString())) {
               SliceQuery<byte[], byte[], byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
               queryDocubase.setColumnFamily("DocInfo");
               queryDocubase.setKey(CompositeSerializer.get().toBytes(row.getKey()));
               AllColumnsIterator<byte[], byte[]> iterColonne = new AllColumnsIterator<byte[], byte[]>(queryDocubase);
               long nbColonne = 0;
               while (iterColonne.hasNext()) {
                  iterColonne.next();
                  nbColonne++;
               }
               if (nbColonne > 0) {
                  // supprime la row
                  Mutator<Composite> mutator = HFactory.createMutator(keyspaceDocubase, CompositeSerializer.get());
                  mutator.addDeletion(row.getKey(), "DocInfo");
                  mutator.execute();
                  nbSuppression++;
               }
            }
         }
      }
      LOGGER.debug("Nb DocInfo supprimés : {}", nbSuppression);
   }
   
   private void deleteTermInfo(Keyspace keyspaceDocubase, List<String> listeIdDoc) {
      long nbSuppression = 0;
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("TermInfo").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(700000);
      QueryResult<OrderedRows<Composite, byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<Composite, byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<Composite, byte[], byte[]> row = iterateur.next();
            
            // la key est compose de :
            // - le nom de l'index
            // - le champs d'un term
            // - le texte d'un term
            
            //String indexName = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
            //String termField = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
            //String termText = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
            
            SliceQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
            queryDocubase.setColumnFamily("TermInfo");
            queryDocubase.setKey(CompositeSerializer.get().toBytes(row.getKey()));
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
               if (listeIdDoc.contains(idDoc.toString())) {
                  // supprime la colonne
                  Mutator<Composite> mutator = HFactory.createMutator(keyspaceDocubase, CompositeSerializer.get());
                  mutator.addDeletion(row.getKey(), "TermInfo", colonne.getName(), CompositeSerializer.get());
                  mutator.execute();
                  nbSuppression++;
               }
            }
         }
      }
      LOGGER.debug("Nb TermInfo supprimés : {}", nbSuppression);
   }
   
   private void deleteTermInfoRange(Keyspace keyspaceDocubase, List<String> listeIdDoc) {
      long nbSuppression = 0;
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
         RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
         rangeQueryDocubase.setColumnFamily(cfName).setKeys(null, null);
         rangeQueryDocubase.setReturnKeysOnly();
         rangeQueryDocubase.setRowCount(200000);
         QueryResult<OrderedRows<Composite, byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
         if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
            Iterator<Row<Composite, byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
            while (iterateur.hasNext()) {
               Row<Composite, byte[], byte[]> row = iterateur.next();
               
               // la key est compose de :
               // - le nom de l'index
               // - le nom de la categorie
               // - l'uuid de la base
               // - le nombre de row de la categorie
               //String indexName = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
               //String catName = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
               //UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
               //byte[] catRowNb = BytesArraySerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(3));
               //long nbCatRow = convertByteToLong(catRowNb);
               
               SliceQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
               queryDocubase.setColumnFamily(cfName);
               queryDocubase.setKey(CompositeSerializer.get().toBytes(row.getKey()));
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
                  if (listeIdDoc.contains(idDoc.toString())) {
                     // supprime la colonne
                     Mutator<Composite> mutator = HFactory.createMutator(keyspaceDocubase, CompositeSerializer.get());
                     mutator.addDeletion(row.getKey(), cfName, colonne.getName(), CompositeSerializer.get());
                     mutator.execute();
                     nbSuppression++;
                  }
               }
            }
         }
      }
      LOGGER.debug("Nb TermInfoRange supprimés : {}", nbSuppression);
   }
   
   @Test
   public void supprimeDocs() throws IOException {
      
      //UUID idDocToFind = UUID.fromString("92d7736c-7318-432a-9c25-17969c0b024c");
      //UUID idFileToFind = UUID.fromString("aaafe93e-b40a-41e7-89a5-ed994b688ac2");
      
      List<String> listeIdDoc = new ArrayList<String>();
      List<String> listeIdFile = new ArrayList<String>();
      
      //listeIdDoc.add(idDocToFind.toString());
      //listeIdFile.add(idFileToFind.toString());
      
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/docs-sicomor-" + nomBase + ".csv")));
      
      String line = in.readLine();
      while (line != null) {
         String idDoc = line.split(";")[0];
         String idFile = line.split(";")[1];
         
         listeIdDoc.add(idDoc);
         listeIdFile.add(idFile);
         
         line = in.readLine();
      }
      
      in.close();
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      //deleteDocument(keyspaceDocubase, listeIdFile);
      deleteDocInfo(keyspaceDocubase, listeIdDoc);
      deleteTermInfo(keyspaceDocubase, listeIdDoc);
      deleteTermInfoRange(keyspaceDocubase, listeIdDoc);
      
   }
}

