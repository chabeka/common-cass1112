package fr.urssaf.image.sae.test.dfce17;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import me.prettyprint.hector.api.beans.AbstractComposite.ComponentEquality;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.SliceQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public class ViewTimestampDocTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(ViewTimestampDocTest.class);
   
   private SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss,SSS");
   
   // Integration cliente GNT
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   //private String nomBase = "GNT-INT";
   
   // Integration cliente GNS
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   //private String nomBase = "SAE-INT";
   
   // Developpement 
   //private String hosts = "cer69imageint10.cer69.recouv";
   //private String nomBase = "SAE-INT";
   
   // Pre-prod MOE
   private String hosts = "cnp69pprodsaecas1.cer69.recouv:9160,cnp69pprodsaecas2.cer69.recouv:9160,cnp69pprodsaecas3.cer69.recouv:9160,cnp69pprodsaecas4.cer69.recouv:9160,cnp69pprodsaecas5.cer69.recouv:9160,cnp69pprodsaecas6.cer69.recouv:9160";
   private String nomBase = "SAE-PROD";
   
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
      @SuppressWarnings("rawtypes")
      FailoverPolicy failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
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
   
   private byte[] convertLongToByte(long valeur, int nbBytes) {
      byte[] value = ByteBuffer.allocate(8).putLong(valeur).array();
      return ByteBuffer.allocate(nbBytes).put(value, 0, nbBytes).array();
   }
   
   private String getDate(long value) {
      Date date = new Date(value / 1000);
      return FORMATTER.format(date);
   }
   
   private UUID getBaseUUIDByName(String nomBase) {
      UUID idBase = null;
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<String, String, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("BasesReference");
      queryDocubase.setKey(nomBase);
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if ("uuid".equals(colonne.getName())) {
            idBase = UUIDSerializer.get().fromBytes(colonne.getValue());
         }
      }
      return idBase;
   }
   
   private Map<String, String[]> getDocInfoById(UUID idDocToFind) {
      Map<String, String[]> indexes = new HashMap<String, String[]>();

      // recupere la liste des indexes
      Composite keyToFind = new Composite();
      keyToFind.addComponent(0, UUIDSerializer.get().toByteBuffer(idDocToFind), ComponentEquality.EQUAL);
      keyToFind.addComponent(1, StringSerializer.get().toByteBuffer("0.0.0"), ComponentEquality.EQUAL);
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      SliceQuery<byte[], String, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("DocInfo");
      queryDocubase.setKey(CompositeSerializer.get().toBytes(keyToFind));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         indexes.put(colonne.getName(), new String[] { StringSerializer.get().fromBytes(colonne.getValue()), getDate(colonne.getClock()) });
      }
      return indexes;
   }
   
   private Map<String, String[]> getIndexCounterByIdAndMode(UUID idDocToFind, Set<String> metas, UUID baseUUID, String mode) {
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      Map<String, String[]> retour = new HashMap<String, String[]>();
      
      for (String indexName : metas) {
         
         // genere la cle a chercher
         Composite keyIndexCounterInsert = new Composite();
         keyIndexCounterInsert.addComponent(0, StringSerializer.get().toByteBuffer(indexName), ComponentEquality.EQUAL);
         keyIndexCounterInsert.addComponent(1, UUIDSerializer.get().toByteBuffer(baseUUID), ComponentEquality.EQUAL);
         keyIndexCounterInsert.addComponent(2, StringSerializer.get().toByteBuffer(mode), ComponentEquality.EQUAL);
      
         //LOGGER.debug("Recuperation de l'index {} en INSERT (IndexCounter)", indexName);
         SliceQuery<byte[], Composite, byte[]> queryDocubaseIndex = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubaseIndex.setColumnFamily("IndexCounter");
         queryDocubaseIndex.setKey(CompositeSerializer.get().toBytes(keyIndexCounterInsert));
         AllColumnsIterator<Composite, byte[]> iterColonneIndex = new AllColumnsIterator<Composite, byte[]>(queryDocubaseIndex);
         while (iterColonneIndex.hasNext()) {
            HColumn<Composite, byte[]> colonne = iterColonneIndex.next();
            
            // Le nom de la colonne est composé de 
            // - valeur
            // - l'uuid du document
            String valeur = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)); 
            UUID idDoc = UUID.fromString(StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1)));
            if (idDocToFind.toString().equals(idDoc.toString())) {
               retour.put(indexName, new String[] { valeur, getDate(colonne.getClock()) });
            }
         }
      }
      return retour;
   }
   
   private Map<String, String> getTermInfoById(UUID idDocToFind, Map<String, String[]> indexes) {
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      Map<String, String> retour = new HashMap<String, String>();
      
      Iterator<Entry<String, String[]>> iterateur = indexes.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<String, String[]> entry = iterateur.next();
         
         Composite keyToFindTerm = new Composite();
         keyToFindTerm.addComponent(0, StringSerializer.get().toByteBuffer(""), ComponentEquality.EQUAL);
         keyToFindTerm.addComponent(1, StringSerializer.get().toByteBuffer(entry.getKey()), ComponentEquality.EQUAL);
         keyToFindTerm.addComponent(2, StringSerializer.get().toByteBuffer(entry.getValue()[0]), ComponentEquality.EQUAL);
         
         SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubaseTerm.setColumnFamily("TermInfo");
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(keyToFindTerm));
         AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm);
         while (iterColonneTerm.hasNext()) {
            HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
            
            // Le nom de la colonne est composé de 
            // - l'uuid de la base
            // - l'uuid du document
            // - 0.0.0
            //UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)); 
            UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
            //String version = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(2));
            if (idDocToFind.toString().equals(idDoc.toString())) {
               retour.put(entry.getKey(), getDate(colonne.getClock()));
            }
         }
      }
      return retour;
   }
   
   @Test
   public void findBaseByName() {
      LOGGER.debug("Recuperation de l'id de la base {}", nomBase);
      UUID baseUUID = getBaseUUIDByName(nomBase);
      LOGGER.debug("Id de la base : {}", baseUUID.toString());
   }
   
   @Test
   public void findDocInfoByUUID() {
      
      UUID idDocToFind = UUID.fromString("f24f8ead-ef67-4dcc-8826-221f784851a8");
      Map<String, String[]> indexes = getDocInfoById(idDocToFind);

      long nbColonne = 0;
      Iterator<Entry<String, String[]>> iterateur = indexes.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<String, String[]> entry = iterateur.next();
         LOGGER.debug("{} => {}", entry.getKey(), entry.getValue()[1]);
         nbColonne++;
      }
      LOGGER.debug("Nb resultat : {}", nbColonne);
   }
   
   @Test
   public void findIndexCounterByUUID() {
      
      UUID idDocToFind = UUID.fromString("A48BB999-7785-4416-8A43-A0CC6229B21B");
      UUID baseUUID = null;
      
      Map<String, String[]> mapIndexes = getDocInfoById(idDocToFind);
      
      if (mapIndexes.containsKey("SM_BASE_UUID")) {
         baseUUID = UUID.fromString(mapIndexes.get("SM_BASE_UUID")[0]);
      }
            
      if (baseUUID != null) {
         LOGGER.debug("Recherche des infos pour le doc {} dans la base {}", idDocToFind.toString(), baseUUID.toString());
         
         // recupere les insert et remove
         Map<String, String[]> indexCounteurInsert = getIndexCounterByIdAndMode(idDocToFind, mapIndexes.keySet(), baseUUID, "INSERT");
         Map<String, String[]> indexCounteurRemove = getIndexCounterByIdAndMode(idDocToFind, mapIndexes.keySet(), baseUUID, "REMOVE");
         
         for (String indexName : mapIndexes.keySet()) {
            
            if (indexCounteurInsert.containsKey(indexName)) {
               LOGGER.debug("INSERT {} => {}", indexName, indexCounteurInsert.get(indexName)[1]);
            }
            if (indexCounteurRemove.containsKey(indexName)) {
               LOGGER.debug("REMOVE {} => {}", indexName, indexCounteurRemove.get(indexName)[1]);
            }
         }
      } else {
         LOGGER.debug("Le doc {} n'existe plus", idDocToFind.toString());
      }
   }
   
   @Test
   public void findTermInfoByUUID() {
      
      UUID idDocToFind = UUID.fromString("f24f8ead-ef67-4dcc-8826-221f784851a8");
      
      Map<String, String[]> indexes = getDocInfoById(idDocToFind);
      
      Map<String, String> terms = getTermInfoById(idDocToFind, indexes);
      
      Iterator<Entry<String, String>> iterateur = terms.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<String, String> entry = iterateur.next();
         
        LOGGER.debug("{} => {}", entry.getKey(), entry.getValue());
      }
   }
   
   @Test
   public void findTermInfoRangeByUUID() {
      
      UUID idDocToFind = UUID.fromString("A48BB999-7785-4416-8A43-A0CC6229B21B");
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
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      UUID baseUUID = null;
      
      Map<String, String[]> mapIndexes = getDocInfoById(idDocToFind);
      
      if (mapIndexes.containsKey("SM_BASE_UUID")) {
         baseUUID = UUID.fromString(mapIndexes.get("SM_BASE_UUID")[0]);
      }
      
      if (baseUUID != null) {
         for (String indexName : mapIndexes.keySet()) {
            for (String cfName : cfRanges) {
               
               Composite keyToFind = new Composite();
               keyToFind.addComponent(0, StringSerializer.get().toByteBuffer(""), ComponentEquality.EQUAL);
               keyToFind.addComponent(1, StringSerializer.get().toByteBuffer(indexName), ComponentEquality.EQUAL);
               keyToFind.addComponent(2, UUIDSerializer.get().toByteBuffer(baseUUID), ComponentEquality.EQUAL);
               keyToFind.addComponent(3, BytesArraySerializer.get().toByteBuffer(convertLongToByte(0, 1)), ComponentEquality.EQUAL);
               
               SliceQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
               queryDocubase.setColumnFamily(cfName);
               queryDocubase.setKey(CompositeSerializer.get().toBytes(keyToFind));
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
                  if (idDocToFind.toString().equals(idDoc.toString())) {
                     LOGGER.debug("{} : {} => {}", new String[] { cfName, indexName, getDate(colonne.getClock())});
                  }
               }
            }
         }
      } else {
         LOGGER.debug("Le doc {} n'existe plus", idDocToFind.toString());
      }
   }
   
   @Test
   public void findAllInfos() {
      UUID idDocToFind = UUID.fromString("A48BB999-7785-4416-8A43-A0CC6229B21B");
      
      // recupere l'id de la base
      UUID baseUUID = getBaseUUIDByName(nomBase);
      
      // recupere la liste des metas
      Map<String, String[]> metas = getDocInfoById(idDocToFind);
      
      // desindexation
      Map<String, String[]> indexCounteurRemove = getIndexCounterByIdAndMode(idDocToFind, metas.keySet(), baseUUID, "REMOVE");
      //Map<String, String> termsInfoRemove = getTermInfoById(idDocToFind, indexCounteurRemove);
      for (String indexName : metas.keySet()) {
         
         if (indexCounteurRemove.containsKey(indexName)) {
            LOGGER.debug("REMOVE INDEX COUNTER {} => {}", indexName, indexCounteurRemove.get(indexName)[1]);
            /*if (termsInfoRemove.containsKey(indexName)) {
               LOGGER.debug("Ancien TermInfo {} : {} => {}", new String[] { indexName, indexCounteurRemove.get(indexName)[0], termsInfoRemove.get(indexName) });
            }*/
         }
      }
      
      LOGGER.debug("---------------------------------------------------------------------------------");
      
      // indexation
      Map<String, String[]> indexCounteurInsert = getIndexCounterByIdAndMode(idDocToFind, metas.keySet(), baseUUID, "INSERT");
      for (String indexName : metas.keySet()) {
         
         if (indexCounteurInsert.containsKey(indexName)) {
            LOGGER.debug("INSERT INDEX COUNTER {} => {}", indexName, indexCounteurInsert.get(indexName)[1]);
         }
      }
      
      LOGGER.debug("---------------------------------------------------------------------------------");
      
      Map<String, String> termsInfoInsert = getTermInfoById(idDocToFind, metas);
      for (String indexName : metas.keySet()) {
         
         if (termsInfoInsert.containsKey(indexName)) {
            LOGGER.debug("Nouveau TermInfo {} : {} => {}", new String[] { indexName, indexCounteurInsert.get(indexName)[0], termsInfoInsert.get(indexName) });
         }
      }
   }
}

