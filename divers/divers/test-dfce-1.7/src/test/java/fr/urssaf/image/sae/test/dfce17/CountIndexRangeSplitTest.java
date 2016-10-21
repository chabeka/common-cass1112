package fr.urssaf.image.sae.test.dfce17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.CountQuery;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.codec.Hex;

@RunWith(BlockJUnit4ClassRunner.class)
public class CountIndexRangeSplitTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(CountIndexRangeSplitTest.class);
   
   // Integration cliente GNS
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   //private String nomBase = "SAE-INT";
   
   // Validation nationale GNS
   //private String hosts = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
   //private String nomBase = "SAE-GIVN";
   
   // GIIN ISO PROD
   //private String hosts = "hwi69gincleasaecas1.cer69.recouv:9160,hwi69gincleasaecas2.cer69.recouv:9160,hwi69gincleasaecas3.cer69.recouv:9160,hwi69gincleasaecas4.cer69.recouv:9160,hwi69gincleasaecas5.cer69.recouv:9160,hwi69gincleasaecas6.cer69.recouv:9160";
   //private String nomBase = "SAE-PROD";
   
   // Pre-prod MOE
   //private String hosts = "cnp69pprodsaecas1.cer69.recouv:9160,cnp69pprodsaecas2.cer69.recouv:9160,cnp69pprodsaecas3.cer69.recouv:9160,cnp69pprodsaecas4.cer69.recouv:9160,cnp69pprodsaecas5.cer69.recouv:9160,cnp69pprodsaecas6.cer69.recouv:9160";
   //private String nomBase = "SAE-PROD";
   
   // CSPP
   //private String hosts = "cnp3saecvecas1.cve.recouv:9160,cnp3saecvecas2.cve.recouv:9160,cnp3saecvecas3.cve.recouv:9160,cnp3saecvecas4.cve.recouv:9160,cnp3saecvecas5.cve.recouv:9160,cnp3saecvecas6.cve.recouv:9160,cnp3saecvecas7.cve.recouv:9160,cnp3saecvecas8.cve.recouv:9160,cnp3saecvecas9.cve.recouv:9160,cnp3saecvecas10.cve.recouv:9160,cnp3saecvecas11.cve.recouv:9160,cnp3saecvecas12.cve.recouv:9160,cnp6saecvecas1.cve.recouv:9160,cnp6saecvecas2.cve.recouv:9160,cnp6saecvecas3.cve.recouv:9160,cnp6saecvecas4.cve.recouv:9160,cnp6saecvecas5.cve.recouv:9160,cnp6saecvecas6.cve.recouv:9160,cnp6saecvecas7.cve.recouv:9160,cnp6saecvecas8.cve.recouv:9160,cnp6saecvecas9.cve.recouv:9160,cnp6saecvecas10.cve.recouv:9160,cnp6saecvecas11.cve.recouv:9160,cnp6saecvecas12.cve.recouv:9160,cnp7saecvecas1.cve.recouv:9160,cnp7saecvecas2.cve.recouv:9160,cnp7saecvecas3.cve.recouv:9160,cnp7saecvecas4.cve.recouv:9160,cnp7saecvecas5.cve.recouv:9160,cnp7saecvecas6.cve.recouv:9160,cnp7saecvecas7.cve.recouv:9160,cnp7saecvecas8.cve.recouv:9160,cnp7saecvecas9.cve.recouv:9160,cnp7saecvecas10.cve.recouv:9160,cnp7saecvecas11.cve.recouv:9160,cnp7saecvecas12.cve.recouv:9160";
   //private String nomBase = "SAE-PROD";
   
   // Prod nationale GNS
   private String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
   private String nomBase = "SAE-PROD";
 
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
   
   private UUID getBaseUUIDByName(String nomBase, Keyspace keyspaceDocubase) {
      UUID idBase = null;
      
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
   
   private Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }
   
   @Test
   public void viewIndexReferenceByIndexName() throws Exception {
      
      String indexName = "SM_ARCHIVAGE_DATE";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      System.out.println(Hex.encode(StringSerializer.get().toBytes(buffer.toString()))); 
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
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
   }
   
   @Test
   public void viewIndexReferenceByIndexNameOrderedByNumRange() throws Exception {
      
      String indexName = "srt";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      System.out.println(Hex.encode(StringSerializer.get().toBytes(buffer.toString()))); 
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      
      Map<Long, List<String>> ranges = new TreeMap<Long, List<String>>();
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.value")) {
            String valeur = StringSerializer.get().fromBytes(colonne.getValue());
            JSONObject object = new JSONObject(valeur);
            Long id = object.getLong("ID");
            if (!ranges.containsKey(id)) {
               ranges.put(id, new ArrayList<String>());
            }
            ranges.get(id).add(valeur);
         }
      }
      
      for (Long numRow : ranges.keySet()) {
         for (String range : ranges.get(numRow)) {
            LOGGER.debug("{}", range);
         }
      }
   }
   
   @Test
   public void checkIndexReferenceBeforeUpdate() throws Exception {
      
      String indexName = "SM_LIFE_CYCLE_REFERENCE_DATE";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      System.out.println(Hex.encode(StringSerializer.get().toBytes(buffer.toString()))); 
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      
      Map<Long, Long> ranges = new TreeMap<Long, Long>();
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
            String[] colonneName = colonne.getName().split("\\.");
            Long key = Long.valueOf(colonneName[1]);
            Long numRow = convertByteToLong(colonne.getValue());
            ranges.put(key, numRow);
         }
      }
      
      // update des keys
      ranges.put(Long.valueOf(1), Long.valueOf(1));
      ranges.put(Long.valueOf(9), Long.valueOf(2));
      ranges.put(Long.valueOf(400), Long.valueOf(4));
      ranges.put(Long.valueOf(401), Long.valueOf(5));
      ranges.put(Long.valueOf(402), Long.valueOf(6));
      ranges.put(Long.valueOf(403), Long.valueOf(7));
      ranges.put(Long.valueOf(404), Long.valueOf(8));
      ranges.put(Long.valueOf(405), Long.valueOf(9));
      ranges.put(Long.valueOf(406), Long.valueOf(10));
      ranges.put(Long.valueOf(407), Long.valueOf(11));
      
      for (Long key : ranges.keySet()) {
         LOGGER.debug("{} -> {}", key, ranges.get(key));
      }
      
      for (int index = 0; index < 408; index++) {
         if (!ranges.containsKey(Long.valueOf(index))) {
            LOGGER.debug("key {} : manquante", index);
         }
      }
      
      for (int index = 1; index < 400; index++) {
         if (!ranges.containsValue(Long.valueOf(index))) {
            LOGGER.debug("valeur {} : manquante", index);
         }
      }
      
      /*for (Long numRow : ranges.keySet()) {
         for (String range : ranges.get(numRow)) {
            LOGGER.debug("{}", range);
         }
      }*/
   }
   
   private List<Long> getIndexReferenceByIndexName(String indexName, UUID baseId, Keyspace keyspaceDocubase) {
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(baseId.toString());
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      
      List<Long> listNumRow = new ArrayList<Long>();
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
            listNumRow.add(convertByteToLong(colonne.getValue()));
         } 
      }
      
      // tri la liste des numero de row
      Collections.sort(listNumRow);
      
      return listNumRow;
   }
   
   @Test
   public void listIndexByName() {
      
      String indexName = "srt";
      String cfName = "TermInfoRangeString";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
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
            // - le numero de row de la categorie
            Composite compositeKey = CompositeSerializer.get().fromBytes(row.getKey());
            String value1 = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(0));
            String catName = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(1));
            UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(2));
            byte[] nbRow = BytesArraySerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(3));
            
            boolean skip = false;
            if ((!idBase.toString().equals(baseId.toString())) || (!catName.equals(indexName))) {
               // pas la bonne base ou pas le bon index, on skip
               skip = true;
            } 
            
            if (!skip) {
               // on a trouve une bonne row
               LOGGER.info("Index trouvé : {}|{}|{}|{}", new String[] { value1, catName, baseId.toString(), new String(nbRow) });
            }
         }
      } 
   }
   
   @Test
   public void countByIndexName() {
      
      //String indexName = "nce";
      //String cfName = "TermInfoRangeString";
      
      //String indexName = "SM_UUID";
      //String cfName = "TermInfoRangeUUID";
      
      String indexName = "SM_CREATION_DATE";
      String cfName = "TermInfoRangeDatetime";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      // On recupere le numero des rows indexees
      List<Long> numRows = getIndexReferenceByIndexName(indexName, idBase, keyspaceDocubase);
      numRows = new ArrayList<Long>();
      numRows.add(Long.valueOf(0));
      
      LOGGER.info("Lancement du comptage de l'index {} pour la base {} ({})", new String[] { indexName, nomBase, idBase.toString() });
      
      long total = 0;
      for (Long numRow : numRows) {
         LOGGER.debug("Traitement de la row : {}", numRow);
         // l'index a au moins une valeur
         // la cle de l'index est compose de :
         // - le nom de l'index
         // - le nom de la categorie
         // - l'uuid de la base
         // - le numero de row de la categorie
         Composite compositeKey = new Composite();
         compositeKey.add(0, "");
         compositeKey.add(1, indexName);
         compositeKey.add(2, idBase);
         compositeKey.add(3, toByteArray(numRow));
         
         // on va verifie l'indexation des documents
         SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubaseTerm.setColumnFamily(cfName);
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
         AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm, 500);
         long nbCompteur = 0;
         while (iterColonneTerm.hasNext()) {
            iterColonneTerm.next();
            nbCompteur++;
            /*if (nbCompteur % 10000 == 0) {
               LOGGER.debug("En cours : {}", new String[] { Long.toString(nbCompteur) });
            }*/
            if (nbCompteur > 1000000) {
               break;
            }
         }
         
         LOGGER.info("{} docs indexés sur la row {} de l'index {}", new String[] { Long.toString(nbCompteur), numRow.toString(), indexName });
         total += nbCompteur;
      }
      
      LOGGER.info("{} docs indexés au total sur l'index {}", new String[] { Long.toString(total), indexName });
   }
   
   @Test
   public void countByIndexNameOptimized() {
      
      //String indexName = "nci";
      //String cfName = "TermInfoRangeString";
      
      String indexName = "SM_MODIFICATION_DATE";
      String cfName = "TermInfoRangeDatetime";
      
      //String indexName = "SM_UUID";
      //String cfName = "TermInfoRangeUUID";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      // On recupere le numero des rows indexees
      List<Long> numRows = getIndexReferenceByIndexName(indexName, idBase, keyspaceDocubase);
      
      LOGGER.info("Lancement du comptage de l'index {} pour la base {} ({})", new String[] { indexName, nomBase, idBase.toString() });
      
      long total = 0;
      for (Long numRow : numRows) {
         LOGGER.debug("Traitement de la row : {}", numRow);
         
         // l'index a au moins une valeur
         // la cle de l'index est compose de :
         // - le nom de l'index
         // - le nom de la categorie
         // - l'uuid de la base
         // - le numero de row de la categorie
         Composite compositeKey = new Composite();
         compositeKey.add(0, "");
         compositeKey.add(1, indexName);
         compositeKey.add(2, idBase);
         compositeKey.add(3, toByteArray(numRow));
         
         // on va verifie l'indexation des documents
         CountQuery<byte[], Composite> queryDocubaseTerm = HFactory.createCountQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get());
         queryDocubaseTerm.setColumnFamily(cfName);
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
         
         queryDocubaseTerm.setRange(null, null, 1000000000);
         
         Integer nbCompteur = queryDocubaseTerm.execute().get();
         
         LOGGER.info("{} docs indexés sur la row {} de l'index {}", new String[] { Long.toString(nbCompteur), numRow.toString(), indexName });
         total += nbCompteur;
      }
      
      LOGGER.info("{} docs indexés au total sur l'index {}", new String[] { Long.toString(total), indexName });
   }
   
   @Test
   public void verifyTermInfoRangeByName() throws Exception {
      
      //String indexName = "SM_LIFE_CYCLE_REFERENCE_DATE";
      //String cfName = "TermInfoRangeDatetime";
      
      String indexName = "srt";
      String cfName = "TermInfoRangeString";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      // On recupere le numero des rows indexees
      //List<Long> numRows = getIndexReferenceByIndexName(indexName, idBase, keyspaceDocubase);
      List<Long> numRows = new ArrayList<Long>();
      for (int index = 0; index < 400; index++) {
         numRows.add(Long.valueOf(index+1));
      }
      //numRows.remove(2);
      numRows.remove(Long.valueOf(170));
      
      LOGGER.info("Analyse du contenu de la table {} pour l'index {}", cfName, indexName);
      
      String bornePrecedente = "";
      Long numRowPrecedent = null;
      Map<String, Long> bornesMin = new TreeMap<String, Long>();
      Map<Long, String[]> bornesReels = new TreeMap<Long, String[]>();
      for (Long numRow : numRows) {
         LOGGER.debug("Recuperation de la row {} pour l'index {} de {}", new Object[] { numRow, indexName, cfName });
         byte[] nbRow = toByteArray(numRow);
         // l'index a au moins une valeur
         // la cle de l'index est compose de :
         // - le nom de l'index
         // - le nom de la categorie
         // - l'uuid de la base
         // - le numero de row de la categorie
         Composite compositeKey = new Composite();
         compositeKey.add(0, "");
         compositeKey.add(1, indexName);
         compositeKey.add(2, idBase);
         compositeKey.add(3, nbRow);
         
         // on va verifie l'indexation des documents
         SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubaseTerm.setColumnFamily(cfName);
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
         AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm, 100);
         boolean hasColumn = false;
         while (iterColonneTerm.hasNext()) {
            HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
            // Le nom de la colonne est composé de 
            // - la valeur
            // - l'uuid du document
            // - 0.0.0
            String valeur = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
            //UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
            //LOGGER.info("({}|{})", new String[] { valeur, idDoc.toString() });
            bornesMin.put(valeur, numRow);
            /*if (!bornePrecedente.equals("")) {
               //LOGGER.info("[{} TO {}[", new String[] { bornePrecedente, valeur });
               if (bornesReels.isEmpty()) {
                  bornesReels.put(numRowPrecedent, new String[] { "min_lower_bound", valeur });
               } else {
                  bornesReels.put(numRowPrecedent, new String[] { bornePrecedente, valeur });
               }
            }
            bornePrecedente = valeur;
            numRowPrecedent = numRow;
            */
            hasColumn = true;
            break;
         }
         if (!hasColumn) {
            LOGGER.info("row vide {}", numRow);
         } 
      }
      
      for (String borne : bornesMin.keySet()) {
         if (!bornePrecedente.equals("")) {
            if (bornesReels.isEmpty()) {
               bornesReels.put(numRowPrecedent, new String[] { "min_lower_bound", borne });
            } else {
               bornesReels.put(numRowPrecedent, new String[] { bornePrecedente, borne });
            }
         }
         bornePrecedente = borne;
         numRowPrecedent = bornesMin.get(borne);
      }
      
      
      
      bornesReels.put(numRowPrecedent, new String[] { bornePrecedente, "max_upper_bound" });
      //LOGGER.info("[{} TO {}[", new String[] { bornePrecedente, "max_upper_bound" });
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      LOGGER.info("Analyse du contenu de la table IndexReference pour l'index {}", indexName);
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      
      Map<Long, List<String>> ranges = new TreeMap<Long, List<String>>();
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.value")) {
            String valeur = StringSerializer.get().fromBytes(colonne.getValue());
            JSONObject object = new JSONObject(valeur);
            Long id = object.getLong("ID");
            if (!ranges.containsKey(id)) {
               ranges.put(id, new ArrayList<String>());
            }
            ranges.get(id).add(valeur);
         }
      }
      
      LOGGER.info("Comparaison des résultats : ");
      for (Long numRow : bornesReels.keySet()) {
         if (ranges.get(numRow) == null || ranges.get(numRow).size() == 0) {
            LOGGER.error("Le range {} avec les bornes {} TO {} n'est pas reference dans la table IndexReference", new Object[] {numRow, bornesReels.get(numRow)[0], bornesReels.get(numRow)[1]});
         } else if (ranges.get(numRow).size() == 1) {
            // on a qu'une seule reference a ce range
            JSONObject object = new JSONObject(ranges.get(numRow).get(0));
            String borneMin = object.getString("LOWER_BOUND");
            String borneMax = object.getString("UPPER_BOUND");
            
            if ((!bornesReels.get(numRow)[0].equals(borneMin)) || (!bornesReels.get(numRow)[1].equals(borneMax))) {
               LOGGER.error("Un des bornes du range {} n'a pas les bonnes bornes (bornesMinReel={} / {} et bornesMaxReel={} / {})", new Object[] {numRow, bornesReels.get(numRow)[0], borneMin, bornesReels.get(numRow)[1], borneMax});
            }
         } else {
            LOGGER.error("Le range {} est déclaré {} fois", new Object[] {numRow, ranges.get(numRow).size()});
            for (String valeur : ranges.get(numRow)) {
               JSONObject object = new JSONObject(valeur);
               String borneMin = object.getString("LOWER_BOUND");
               String borneMax = object.getString("UPPER_BOUND");
               
               if ((!bornesReels.get(numRow)[0].equals(borneMin)) || (!bornesReels.get(numRow)[1].equals(borneMax))) {
                  LOGGER.error("Un des bornes du range {} n'a pas les bonnes bornes (bornesMinReel={} / {} et bornesMaxReel={} / {})", new Object[] {numRow, bornesReels.get(numRow)[0], borneMin, bornesReels.get(numRow)[1], borneMax});
               }
            }
         }
      }
   }
   
   @Test
   public void recalculateIndexReferenceToCsv_srt() throws Exception {
      
      String indexName = "srt";
      String cfName = "TermInfoRangeString";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      // On recupere le numero des rows indexees
      //List<Long> numRows = getIndexReferenceByIndexName(indexName, idBase, keyspaceDocubase);
      List<Long> numRows = new ArrayList<Long>();
      for (int index = 0; index < 400; index++) {
         numRows.add(Long.valueOf(index+1));
      }
      // range n'existant plus
      numRows.remove(Long.valueOf(170));
      numRows.remove(Long.valueOf(171));
      numRows.remove(Long.valueOf(174));
      numRows.remove(Long.valueOf(175));
      numRows.remove(Long.valueOf(187));
      numRows.remove(Long.valueOf(307));
      numRows.remove(Long.valueOf(308));
      numRows.remove(Long.valueOf(309));
      numRows.remove(Long.valueOf(311));
      
      LOGGER.info("Analyse du contenu de la table {} pour l'index {}", cfName, indexName);
      
      String bornePrecedente = "";
      Long numRowPrecedent = null;
      Map<String, Long> bornesMin = new TreeMap<String, Long>();
      Map<Long, String[]> bornesReels = new TreeMap<Long, String[]>();
      for (Long numRow : numRows) {
         LOGGER.debug("Recuperation de la row {} pour l'index {} de {}", new Object[] { numRow, indexName, cfName });
         byte[] nbRow = toByteArray(numRow);
         // l'index a au moins une valeur
         // la cle de l'index est compose de :
         // - le nom de l'index
         // - le nom de la categorie
         // - l'uuid de la base
         // - le numero de row de la categorie
         Composite compositeKey = new Composite();
         compositeKey.add(0, "");
         compositeKey.add(1, indexName);
         compositeKey.add(2, idBase);
         compositeKey.add(3, nbRow);
         
         // on va verifie l'indexation des documents
         SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubaseTerm.setColumnFamily(cfName);
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
         AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm, 100);
         boolean hasColumn = false;
         while (iterColonneTerm.hasNext()) {
            HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
            // Le nom de la colonne est composé de 
            // - la valeur
            // - l'uuid du document
            // - 0.0.0
            String valeur = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
            //UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
            //LOGGER.info("({}|{})", new String[] { valeur, idDoc.toString() });
            bornesMin.put(valeur, numRow);
            hasColumn = true;
            break;
         }
         if (!hasColumn) {
            LOGGER.info("row vide {}", numRow);
         } 
      }
      
      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File("c:/divers");
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(rep, "cspp_rebuild_srt.csv");
      FileWriter writer = new FileWriter(fichier);
      
      int index = 0;
      // calcule les ranges reel
      for (String borne : bornesMin.keySet()) {
         if (!bornePrecedente.equals("")) {
            if (bornesReels.isEmpty()) {
               bornesReels.put(numRowPrecedent, new String[] { "min_lower_bound", borne });
               //LOGGER.warn("{} TO {}", "min_lower_bound", borne);
               
               // numero de la key
               writer.write(Integer.toString(index));
               writer.write(';');
               
               // numero de la row
               writer.write(numRowPrecedent.toString());
               writer.write(';');
               
               writer.write("min_lower_bound");
               writer.write(';');
               writer.write(borne);
               writer.write('\n');
               
            } else {
               bornesReels.put(numRowPrecedent, new String[] { bornePrecedente, borne });
               //LOGGER.warn("{} TO {}", bornePrecedente, borne);
               
               // numero de la key
               writer.write(Integer.toString(index));
               writer.write(';');
               
               // numero de la row
               writer.write(numRowPrecedent.toString());
               writer.write(';');
               
               writer.write(bornePrecedente);
               writer.write(';');
               writer.write(borne);
               writer.write('\n');
            }
            index++;
         }
         bornePrecedente = borne;
         numRowPrecedent = bornesMin.get(borne);
      }
      bornesReels.put(numRowPrecedent, new String[] { bornePrecedente, "max_upper_bound" });
      
      // numero de la key
      writer.write(Integer.toString(index));
      writer.write(';');
      
      // numero de la row
      writer.write(numRowPrecedent.toString());
      writer.write(';');
      
      writer.write(bornePrecedente);
      writer.write(';');
      writer.write("max_upper_bound");
      writer.write('\n');
      
      try {
         if (writer != null) {
            writer.close();
         }
      } catch (IOException exception) {
         System.err.println("impossible de fermer le flux");
      }
      
      //LOGGER.warn("{} TO {}", bornePrecedente, "max_upper_bound");
   }
   
   @Test
   public void viewNValueOfARow() {
      
      //String indexName = "SM_LIFE_CYCLE_REFERENCE_DATE";
      //String cfName = "TermInfoRangeDatetime";
      String indexName = "srt";
      String cfName = "TermInfoRangeString";
      Long numRow = Long.valueOf(402);
      long nbValueToSee = 1;
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      LOGGER.debug("Recuperation de la row {} pour l'index {} de {}", new Object[] { numRow, indexName, cfName });
      byte[] nbRow = toByteArray(numRow);
      // l'index a au moins une valeur
      // la cle de l'index est compose de :
      // - le nom de l'index
      // - le nom de la categorie
      // - l'uuid de la base
      // - le numero de row de la categorie
      Composite compositeKey = new Composite();
      compositeKey.add(0, "");
      compositeKey.add(1, indexName);
      compositeKey.add(2, idBase);
      compositeKey.add(3, nbRow);
      
      // on va verifie l'indexation des documents
      SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
      queryDocubaseTerm.setColumnFamily(cfName);
      queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
      AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm, 500);
      long nbCompteur = 0;
      while (iterColonneTerm.hasNext()) {
         HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
         // Le nom de la colonne est composé de 
         // - la valeur
         // - l'uuid du document
         // - 0.0.0
         String valeur = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
         UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
         LOGGER.info("({}|{})", new String[] { valeur, idDoc.toString() });
         nbCompteur++;
         if (nbCompteur >= nbValueToSee) {
            break;
         }
      }
   }
   
   @Test
   public void viewBornesRangeOfARow() {
      
      String indexName = "SM_LIFE_CYCLE_REFERENCE_DATE";
      String cfName = "TermInfoRangeDatetime";
      
      //String indexName = "den";
      //String cfName = "TermInfoRangeString";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      // On recupere le numero des rows indexees
      //List<Long> numRows = getIndexReferenceByIndexName(indexName, idBase, keyspaceDocubase);
      List<Long> numRows = new ArrayList<Long>();
      /*for (int index = 0; index < 5; index++) {
         numRows.add(Long.valueOf(index+1));
      }*/
      numRows.add(Long.valueOf(1));
      numRows.add(Long.valueOf(2));
      numRows.add(Long.valueOf(4));
      numRows.add(Long.valueOf(5));
      numRows.add(Long.valueOf(6));
      numRows.add(Long.valueOf(7));
      numRows.add(Long.valueOf(8));
      numRows.add(Long.valueOf(9));
      numRows.add(Long.valueOf(10));
      numRows.add(Long.valueOf(11));
      
      LOGGER.info("Recuperation des bornes reels de l'index {} pour la base {} ({})", new String[] { indexName, nomBase, idBase.toString() });
      
      for (Long numRow : numRows) {
         // l'index a au moins une valeur
         // la cle de l'index est compose de :
         // - le nom de l'index
         // - le nom de la categorie
         // - l'uuid de la base
         // - le numero de row de la categorie
         Composite compositeKey = new Composite();
         compositeKey.add(0, "");
         compositeKey.add(1, indexName);
         compositeKey.add(2, idBase);
         compositeKey.add(3, toByteArray(numRow));
         
         // on va verifie l'indexation des documents
         SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubaseTerm.setColumnFamily(cfName);
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
         AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm, 200);
         
         long nbCompteur = 0;
         String borneMin = "";
         String borneMax = "";
         boolean firstValue = true;
         
         while (iterColonneTerm.hasNext()) {
            HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
            // Le nom de la colonne est composé de 
            // - la valeur
            // - l'uuid du document
            // - 0.0.0
            String valeur = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
            if (firstValue) {
               borneMin = valeur;
            }
            borneMax = valeur;
            nbCompteur++;
            firstValue = false;
         }
         
         LOGGER.info("Traitement de la row {} : de {} a {} : {} docs", new String[] { numRow.toString(), borneMin, borneMax, Long.toString(nbCompteur) });
      }
   }
   
   /**
    * 
    *  @deprecated Ne tient pas compte de l'ordre des bornes </br>
    *              utiliser la methode {@link #checkIndexInIndexReferenceOrderByBornes()} à la place.  
    */
   @Test
   @Deprecated
   public void checkIndexInIndexReference() throws JSONException {
      
      String indexName = "srt";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      
      Long nbRange = null;
      List<Long> listNumRow = new ArrayList<Long>();
      Map<Long, String[]> bornes = new TreeMap<Long, String[]>();
      List<Long> rangesClosed = new ArrayList<Long>();
      Long maxKey = Long.valueOf(-1);
      Long numRowMaxKey = Long.valueOf(-1);
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
            listNumRow.add(convertByteToLong(colonne.getValue()));
            
            Long key = Long.valueOf(colonne.getName().split("\\.")[1]);
            if (key.longValue() > maxKey.longValue()) {
               maxKey = key;
               numRowMaxKey = convertByteToLong(colonne.getValue());
            }
            
         } else if (colonne.getName().equals("rangeIndexes.size")) {
            nbRange = convertByteToLong(colonne.getValue());
         } else if (colonne.getName().matches("rangeIndexes.[0-9]*.value")) {
            JSONObject object = new JSONObject(StringSerializer.get().fromBytes(colonne.getValue()));
            Long id = object.getLong("ID");
            String borneMin = object.getString("LOWER_BOUND");
            String borneMax = object.getString("UPPER_BOUND");
            String etat = object.getString("STATE"); 
            if (!etat.equals("CLOSED")) {
               bornes.put(id, new String[] {borneMin, borneMax} );
            } else {
               rangesClosed.add(id);
            }
         } 
      }
      
      // suppression des ranges closed
      for (Long numRow : rangesClosed) {
         LOGGER.warn("Ne tient pas compte du range {} fermé", numRow);
         listNumRow.remove(numRow);
      }
      
      if (maxKey.longValue() > 0) {
         // on est sur d'avoir plusieurs ranges, donc d'avoir splitter
         // on ignore la numRow de la max key
         LOGGER.warn("Ne tient pas compte du range {} de la toute dernière key", numRowMaxKey);
         listNumRow.remove(numRowMaxKey);
         if (!listNumRow.contains(numRowMaxKey)) {
            LOGGER.warn("Le range {} n'est plus reference, donc on supprime les bornes", numRowMaxKey);
            bornes.remove(numRowMaxKey);
         }
      }
      
      // verifie les doublons
      Map<Long, Long> doublons = new TreeMap<Long, Long>();
      for (Long numRow : listNumRow) {
         if (doublons.containsKey(numRow)) {
            doublons.put(numRow, doublons.get(numRow) + 1);
         } else {
            doublons.put(numRow, Long.valueOf(1));
         }
      }
      for (Long numRow : doublons.keySet()) {
         int index = 0;
         if (doublons.get(numRow) == 2) {
            LOGGER.warn("Doublon du range {} pour l'index {}", numRow, indexName);
            index = 1;
         } else if (doublons.get(numRow) > 2) {
            LOGGER.error("Trop de ranges({}) pour la row {} pour l'index {}", new Object[] { doublons.get(numRow), numRow, indexName });
            index = doublons.get(numRow).intValue() - 1;
         }
         // elimination des doublons
         while (index >= 1) {
            listNumRow.remove(numRow);
            index--;
         }
      }
      
      // tri la liste des numero de row
      Collections.sort(listNumRow);
      
      // verifie le nombre de range
      if (nbRange < listNumRow.size()) {
         LOGGER.error("Le nombre de range n'est pas correct pour l'index {}: {} au lieu de {}", new Object[] { indexName, nbRange, listNumRow.size() });
      }
      
      boolean first = true;
      String bornePrecedente = "";
      // verifie les bornes
      for (Long numRow : listNumRow) {
         if (first) {
            // verifie la borne min
            if (!bornes.get(numRow)[0].equalsIgnoreCase("min_lower_bound")) {
               LOGGER.error("Pas de borne min : {}", bornes.get(numRow)[0]);
            }
            first = false;
         } else {
            if (!bornes.get(numRow)[0].equalsIgnoreCase(bornePrecedente)) {
               LOGGER.error("Valeur differente entre la borne precdente et le nouveau range : {} - {}", bornePrecedente, bornes.get(numRow)[0]);
            }
         }
         bornePrecedente = bornes.get(numRow)[1];
      }
      // verifie la derniere borne
      if (!bornePrecedente.equalsIgnoreCase("max_upper_bound")) {
         LOGGER.error("Pas de borne max : {}", bornePrecedente);
      }
   }
   
   @Test
   public void checkIndexInIndexReferenceOrderByBornes() throws JSONException {
      
      String indexName = "SM_MODIFICATION_DATE";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      
      Long nbRange = null;
      List<Long> listNumRow = new ArrayList<Long>();
      Map<String, String[]> bornes = new TreeMap<String, String[]>();
      List<Long> rangesClosed = new ArrayList<Long>();
      String[] firstBorne = null;
      Long maxKey = Long.valueOf(-1);
      Long numRowMaxKey = Long.valueOf(-1);
      Map<Long, String> bornesMin = new TreeMap<Long, String>();
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
            listNumRow.add(convertByteToLong(colonne.getValue()));
            
            Long key = Long.valueOf(colonne.getName().split("\\.")[1]);
            if (key.longValue() > maxKey.longValue()) {
               maxKey = key;
               numRowMaxKey = convertByteToLong(colonne.getValue());
            }
            
         } else if (colonne.getName().equals("rangeIndexes.size")) {
            nbRange = convertByteToLong(colonne.getValue());
         } else if (colonne.getName().matches("rangeIndexes.[0-9]*.value")) {
            JSONObject object = new JSONObject(StringSerializer.get().fromBytes(colonne.getValue()));
            Long id = object.getLong("ID");
            String borneMin = object.getString("LOWER_BOUND");
            String borneMax = object.getString("UPPER_BOUND");
            String etat = object.getString("STATE"); 
            if (!etat.equals("CLOSED")) {
               if (borneMin.equals("min_lower_bound")) {
                  firstBorne = new String[] {borneMin, borneMax};
               } else {
                  bornes.put(borneMin, new String[] {borneMin, borneMax} );
               }
               bornesMin.put(id, borneMin);
            } else {
               rangesClosed.add(id);
            }
         } 
      }
      
      // suppression des ranges closed
      for (Long numRow : rangesClosed) {
         LOGGER.warn("Ne tient pas compte du range {} fermé", numRow);
         listNumRow.remove(numRow);
      }
      
      if (maxKey.longValue() > 0) {
         // on est sur d'avoir plusieurs ranges, donc d'avoir splitter
         // on ignore la numRow de la max key
         LOGGER.warn("Ne tient pas compte du range {} de la toute dernière key", numRowMaxKey);
         listNumRow.remove(numRowMaxKey);
         if (!listNumRow.contains(numRowMaxKey)) {
            LOGGER.warn("Le range {} n'est plus reference, donc on supprime les bornes", numRowMaxKey);
            if (bornesMin.get(numRowMaxKey) != null) {
               bornes.remove(bornesMin.get(numRowMaxKey));
            }
         }
      }
      
      // verifie les doublons
      Map<Long, Long> doublons = new TreeMap<Long, Long>();
      for (Long numRow : listNumRow) {
         if (doublons.containsKey(numRow)) {
            doublons.put(numRow, doublons.get(numRow) + 1);
         } else {
            doublons.put(numRow, Long.valueOf(1));
         }
      }
      for (Long numRow : doublons.keySet()) {
         int index = 0;
         if (doublons.get(numRow) == 2) {
            LOGGER.warn("Doublon du range {} pour l'index {}", numRow, indexName);
            index = 1;
         } else if (doublons.get(numRow) > 2) {
            LOGGER.error("Trop de ranges({}) pour la row {} pour l'index {}", new Object[] { doublons.get(numRow), numRow, indexName });
            index = doublons.get(numRow).intValue() - 1;
         }
         // elimination des doublons
         while (index >= 1) {
            listNumRow.remove(numRow);
            index--;
         }
      }
      
      // tri la liste des numero de row
      Collections.sort(listNumRow);
      
      // verifie le nombre de range
      if (nbRange < listNumRow.size()) {
         LOGGER.error("Le nombre de range n'est pas correct pour l'index {}: {} au lieu de {}", new Object[] { indexName, nbRange, listNumRow.size() });
      }
      
      String bornePrecedente = "";
      if (firstBorne == null) {
         LOGGER.error("Pas de borne min");
      } else {
         bornePrecedente = firstBorne[1];
      }
      for (String borneMin : bornes.keySet()) {
         if (!bornePrecedente.equals("")) {
            if (!bornes.get(borneMin)[0].equalsIgnoreCase(bornePrecedente)) {
               LOGGER.error("Valeur differente entre la borne precdente et le nouveau range : {} - {}", bornePrecedente, bornes.get(borneMin)[0]);
            }
         }
         bornePrecedente = bornes.get(borneMin)[1];
      }
      if (!bornePrecedente.equalsIgnoreCase("max_upper_bound")) {
         LOGGER.error("Pas de borne max : {}", bornePrecedente);
      }
   }
   
   /**
    * 
    *  @deprecated Ne tient pas compte de l'ordre des bornes </br>
    *              utiliser la methode {@link #checkAllIndexReferenceOrderByBornes()} à la place.  
    */
   @Test
   @Deprecated
   public void checkAllIndexReference() throws JSONException {
      
      boolean activeLog = false;
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      Map<String, byte[]> indexes = new HashMap<String, byte[]>();
      
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("IndexReference");
      rangeQueryDocubase.setReturnKeysOnly();
      
      
      AllRowsIterator<byte[],String,byte[]> iterRows = new AllRowsIterator<byte[], String, byte[]>(rangeQueryDocubase);
      while (iterRows.hasNext()) {
         Row<byte[], String, byte[]> row = iterRows.next();
         String rowKey = StringSerializer.get().fromBytes(row.getKey());
         String indexName = rowKey.substring(0, rowKey.indexOf((char) 65535));
         String base = rowKey.substring(rowKey.indexOf((char) 65535) + 1);
         if (base.equals(idBase.toString())) {
            indexes.put(indexName, row.getKey());
         }
      }
      
      for (String indexName : indexes.keySet()) {
         
         LOGGER.info("Analyse de l'index {}", indexName);
         boolean error = false;
         
         SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
         queryDocubase.setColumnFamily("IndexReference");
         queryDocubase.setKey(indexes.get(indexName));
         AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
         
         Long nbRange = null;
         List<Long> listNumRow = new ArrayList<Long>();
         Map<Long, String[]> bornes = new TreeMap<Long, String[]>();
         List<Long> rangesClosed = new ArrayList<Long>();
         Long maxKey = Long.valueOf(-1);
         Long numRowMaxKey = Long.valueOf(-1);
         while (iterColonne.hasNext()) {
            HColumn<String, byte[]> colonne = iterColonne.next();
            if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
               listNumRow.add(convertByteToLong(colonne.getValue()));
               
               Long key = Long.valueOf(colonne.getName().split("\\.")[1]);
               if (key.longValue() > maxKey.longValue()) {
                  maxKey = key;
                  numRowMaxKey = convertByteToLong(colonne.getValue());
               }
               
            } else if (colonne.getName().equals("rangeIndexes.size")) {
               nbRange = convertByteToLong(colonne.getValue());
            } else if (colonne.getName().matches("rangeIndexes.[0-9]*.value")) {
               JSONObject object = new JSONObject(StringSerializer.get().fromBytes(colonne.getValue()));
               Long id = object.getLong("ID");
               String borneMin = object.getString("LOWER_BOUND");
               String borneMax = object.getString("UPPER_BOUND");
               String etat = object.getString("STATE"); 
               if (!etat.equals("CLOSED")) {
                  bornes.put(id, new String[] {borneMin, borneMax} );
               } else {
                  rangesClosed.add(id);
               }
            } 
         }
         
         // gere le cas bizarre des index sans taille de range
         if (nbRange == null) {
            continue;
         }
         
         // suppression des ranges closed
         for (Long numRow : rangesClosed) {
            if (activeLog) {
               LOGGER.warn("Ne tient pas compte du range {} fermé", numRow);
            }
            listNumRow.remove(numRow);
         }
         
         if (maxKey.longValue() > 0) {
            // on est sur d'avoir plusieurs ranges, donc d'avoir splitter
            // on ignore la numRow de la max key
            if (activeLog) {
               LOGGER.warn("Ne tient pas compte du range {} de la toute dernière key", numRowMaxKey);
            }
            listNumRow.remove(numRowMaxKey);
            if (!listNumRow.contains(numRowMaxKey)) {
               if (activeLog) {
                  LOGGER.warn("Le range {} n'est plus reference, donc on supprime les bornes", numRowMaxKey);
               }
               bornes.remove(numRowMaxKey);
            }
         }
         
         // verifie les doublons
         Map<Long, Long> doublons = new TreeMap<Long, Long>();
         for (Long numRow : listNumRow) {
            if (doublons.containsKey(numRow)) {
               doublons.put(numRow, doublons.get(numRow) + 1);
            } else {
               doublons.put(numRow, Long.valueOf(1));
            }
         }
         for (Long numRow : doublons.keySet()) {
            int index = 0;
            if (doublons.get(numRow) == 2) {
               if (activeLog) {
                  LOGGER.warn("Doublon du range {} pour l'index {}", numRow, indexName);
               }
               index = 1;
            } else if (doublons.get(numRow) > 2) {
               if (activeLog) {
                  LOGGER.error("Trop de ranges({}) pour la row {} pour l'index {}", new Object[] { doublons.get(numRow), numRow, indexName });
               }
               index = doublons.get(numRow).intValue() - 1;
               error = true;
            }
            // elimination des doublons
            while (index >= 1) {
               listNumRow.remove(numRow);
               index--;
            }
         }
         
         // tri la liste des numero de row
         Collections.sort(listNumRow);
         
         // verifie le nombre de range
         if (nbRange < listNumRow.size()) {
            if (activeLog) {
               LOGGER.error("Le nombre de range n'est pas correct pour l'index {}: {} au lieu de {}", new Object[] { indexName, nbRange, listNumRow.size() });
            }
            error = true;
         }
         
         boolean first = true;
         String bornePrecedente = "";
         // verifie les bornes
         for (Long numRow : listNumRow) {
            if (first) {
               // verifie la borne min
               if (!bornes.get(numRow)[0].equalsIgnoreCase("min_lower_bound")) {
                  if (activeLog) {
                     LOGGER.error("Pas de borne min : {}", bornes.get(numRow)[0]);
                  }
                  error = true;
               }
               first = false;
            } else {
               if (!bornes.get(numRow)[0].equalsIgnoreCase(bornePrecedente)) {
                  if (activeLog) {
                     LOGGER.error("Valeur differente entre la borne precdente et le nouveau range : {} - {}", bornePrecedente, bornes.get(numRow)[0]);
                  }
                  error = true;
               }
            }
            bornePrecedente = bornes.get(numRow)[1];
         }
         // verifie la derniere borne
         if (!bornePrecedente.equalsIgnoreCase("max_upper_bound")) {
            if (activeLog) {
               LOGGER.error("Pas de borne max : {}", bornePrecedente);
            }
            error = true;
         }
         if (error) {
            LOGGER.error("erreur sur l'index {}", indexName);
         }
      }
   }
   
   @Test
   public void checkAllIndexReferenceOrderByBornes() throws JSONException {
      
      boolean activeLog = false;
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      Map<String, byte[]> indexes = new HashMap<String, byte[]>();
      
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("IndexReference");
      rangeQueryDocubase.setReturnKeysOnly();
      
      
      AllRowsIterator<byte[],String,byte[]> iterRows = new AllRowsIterator<byte[], String, byte[]>(rangeQueryDocubase);
      while (iterRows.hasNext()) {
         Row<byte[], String, byte[]> row = iterRows.next();
         String rowKey = StringSerializer.get().fromBytes(row.getKey());
         String indexName = rowKey.substring(0, rowKey.indexOf((char) 65535));
         String base = rowKey.substring(rowKey.indexOf((char) 65535) + 1);
         if (base.equals(idBase.toString())) {
            indexes.put(indexName, row.getKey());
         }
      }
      
      for (String indexName : indexes.keySet()) {
         
         LOGGER.info("Analyse de l'index {}", indexName);
         boolean error = false;
         
         SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
         queryDocubase.setColumnFamily("IndexReference");
         queryDocubase.setKey(indexes.get(indexName));
         AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
         
         Long nbRange = null;
         List<Long> listNumRow = new ArrayList<Long>();
         Map<String, String[]> bornes = new TreeMap<String, String[]>();
         List<Long> rangesClosed = new ArrayList<Long>();
         String[] firstBorne = null;
         Long maxKey = Long.valueOf(-1);
         Long numRowMaxKey = Long.valueOf(-1);
         Map<Long, String> bornesMin = new TreeMap<Long, String>();
         while (iterColonne.hasNext()) {
            HColumn<String, byte[]> colonne = iterColonne.next();
            if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
               listNumRow.add(convertByteToLong(colonne.getValue()));
               
               Long key = Long.valueOf(colonne.getName().split("\\.")[1]);
               if (key.longValue() > maxKey.longValue()) {
                  maxKey = key;
                  numRowMaxKey = convertByteToLong(colonne.getValue());
               }
               
            } else if (colonne.getName().equals("rangeIndexes.size")) {
               nbRange = convertByteToLong(colonne.getValue());
            } else if (colonne.getName().matches("rangeIndexes.[0-9]*.value")) {
               JSONObject object = new JSONObject(StringSerializer.get().fromBytes(colonne.getValue()));
               Long id = object.getLong("ID");
               String borneMin = object.getString("LOWER_BOUND");
               String borneMax = object.getString("UPPER_BOUND");
               String etat = object.getString("STATE"); 
               if (!etat.equals("CLOSED")) {
                  if (borneMin.equals("min_lower_bound")) {
                     firstBorne = new String[] {borneMin, borneMax};
                  } else {
                     bornes.put(borneMin, new String[] {borneMin, borneMax} );
                  }
                  bornesMin.put(id, borneMin);
               } else {
                  rangesClosed.add(id);
               }
            } 
         }
         
         // gere le cas bizarre des index sans taille de range
         if (nbRange == null) {
            continue;
         }
         
         // suppression des ranges closed
         for (Long numRow : rangesClosed) {
            if (activeLog) {
               LOGGER.warn("Ne tient pas compte du range {} fermé", numRow);
            }
            listNumRow.remove(numRow);
         }
         
         if (maxKey.longValue() > 0) {
            // on est sur d'avoir plusieurs ranges, donc d'avoir splitter
            // on ignore la numRow de la max key
            if (activeLog) {
               LOGGER.warn("Ne tient pas compte du range {} de la toute dernière key", numRowMaxKey);
            }
            listNumRow.remove(numRowMaxKey);
            if (!listNumRow.contains(numRowMaxKey)) {
               if (activeLog) {
                  LOGGER.warn("Le range {} n'est plus reference, donc on supprime les bornes", numRowMaxKey);
               }
               if (bornesMin.get(numRowMaxKey) != null) {
                  bornes.remove(bornesMin.get(numRowMaxKey));
               }
            }
         }
         
         // verifie les doublons
         Map<Long, Long> doublons = new TreeMap<Long, Long>();
         for (Long numRow : listNumRow) {
            if (doublons.containsKey(numRow)) {
               doublons.put(numRow, doublons.get(numRow) + 1);
            } else {
               doublons.put(numRow, Long.valueOf(1));
            }
         }
         for (Long numRow : doublons.keySet()) {
            int index = 0;
            if (doublons.get(numRow) == 2) {
               if (activeLog) {
                  LOGGER.warn("Doublon du range {} pour l'index {}", numRow, indexName);
               }
               index = 1;
            } else if (doublons.get(numRow) > 2) {
               if (activeLog) {
                  LOGGER.error("Trop de ranges({}) pour la row {} pour l'index {}", new Object[] { doublons.get(numRow), numRow, indexName });
               }
               index = doublons.get(numRow).intValue() - 1;
               error = true;
            }
            // elimination des doublons
            while (index >= 1) {
               listNumRow.remove(numRow);
               index--;
            }
         }
         
         // tri la liste des numero de row
         Collections.sort(listNumRow);
         
         // verifie le nombre de range
         if (nbRange < listNumRow.size()) {
            if (activeLog) {
               LOGGER.error("Le nombre de range n'est pas correct pour l'index {}: {} au lieu de {}", new Object[] { indexName, nbRange, listNumRow.size() });
            }
            error = true;
         }
         
         String bornePrecedente = "";
         if (firstBorne == null) {
            if (activeLog) {
               LOGGER.error("Pas de borne min");
            }
            error = true;
         } else {
            bornePrecedente = firstBorne[1];
         }
         for (String borneMin : bornes.keySet()) {
            if (!bornePrecedente.equals("")) {
               if (!bornes.get(borneMin)[0].equalsIgnoreCase(bornePrecedente)) {
                  if (activeLog) {
                     LOGGER.error("Valeur differente entre la borne precdente et le nouveau range : {} - {}", bornePrecedente, bornes.get(borneMin)[0]);
                  }
                  error = true;
               }
            }
            bornePrecedente = bornes.get(borneMin)[1];
         }
         // verifie la derniere borne
         if (!bornePrecedente.equalsIgnoreCase("max_upper_bound")) {
            if (activeLog) {
               LOGGER.error("Pas de borne max : {}", bornePrecedente);
            }
            error = true;
         }
         if (error) {
            LOGGER.error("erreur sur l'index {}", indexName);
         }
      }
   }
   
   @Test
   public void calculateRangeIndexName() {
      
      //String indexName = "den";
      //String cfName = "TermInfoRangeString";
      
      String indexName = "SM_CREATION_DATE";
      String cfName = "TermInfoRangeDatetime";
      
      long tailleRange = 1000000;
      List<String[]> ranges = new ArrayList<String[]>();
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      // On recupere le numero des rows indexees
      List<Long> numRows = getIndexReferenceByIndexName(indexName, idBase, keyspaceDocubase);
      numRows.clear();
      numRows.add(Long.valueOf(75));
      
      LOGGER.info("Lancement du comptage de l'index {} pour la base {} ({})", new String[] { indexName, nomBase, idBase.toString() });
      
      long total = 0;
      for (Long numRow : numRows) {
         LOGGER.debug("Traitement de la row : {}", numRow);
         // l'index a au moins une valeur
         // la cle de l'index est compose de :
         // - le nom de l'index
         // - le nom de la categorie
         // - l'uuid de la base
         // - le numero de row de la categorie
         Composite compositeKey = new Composite();
         compositeKey.add(0, "");
         compositeKey.add(1, indexName);
         compositeKey.add(2, idBase);
         compositeKey.add(3, toByteArray(numRow));
         
         String startRange = "";
         String endRange = "";
         
         // on va verifie l'indexation des documents
         SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubaseTerm.setColumnFamily(cfName);
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
         AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm, 500);
         long nbCompteur = 0;
         while (iterColonneTerm.hasNext()) {
            HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
            if (StringUtils.isEmpty(startRange)) {
               startRange = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
            }
            endRange = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
            nbCompteur++;
            if (nbCompteur % 10000 == 0) {
               LOGGER.debug("En cours : {}", new String[] { Long.toString(nbCompteur) });
            }
            if (nbCompteur % tailleRange == 0) {
               ranges.add(new String[] { startRange, endRange });
               startRange = endRange;
            } 
         }
         
         // ajout du dernier range
         ranges.add(new String[] { startRange, endRange });
         
         LOGGER.info("{} docs indexés sur la row {} de l'index {}", new String[] { Long.toString(nbCompteur), numRow.toString(), indexName });
         total += nbCompteur;
      }
      
      for (String[] range : ranges) {
         LOGGER.debug("range: {} -> {}", range[0], range[1]);
      }
      
      LOGGER.info("{} docs indexés au total sur l'index {}", new String[] { Long.toString(total), indexName });
   }
   
   @Test
   public void getKeyIndexReference() {
      
      String indexName = "nid";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      String valeurHexa = new String(Hex.encode(buffer.toString().getBytes())); 
      
      LOGGER.info("key de l'index {} : {}", indexName, valeurHexa);
   }
   
   @Test
   public void getKeyBaseCategoriesReference() {
      
      String indexName = "nid";
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(nomBase);
      buffer.append((char) 65535);
      buffer.append(indexName);
      
      String valeurHexa = new String(Hex.encode(buffer.toString().getBytes())); 
      
      LOGGER.info("key de l'index {} : {}", indexName, valeurHexa);
   }
   
   private byte[] toByteArray(long valeur) {
      int nbOctet;
      if (valeur <= Byte.MAX_VALUE) {
         nbOctet = 1;
      } else if (valeur > Byte.MAX_VALUE && valeur <= Short.MAX_VALUE) {
         nbOctet = 2;
      } else if (valeur > Short.MAX_VALUE && valeur <= Integer.MAX_VALUE) {
         nbOctet = 4;
      } else if (valeur > Integer.MAX_VALUE && valeur <= Long.MAX_VALUE) {
         nbOctet = 8;
      } else {
         throw new IllegalArgumentException("valeur trop grande");
      }
      byte[] result = new byte[nbOctet];
      for (int i = nbOctet - 1; i >= 0; i--) {
        result[i] = (byte) (valeur & 0xffL);
        valeur >>= 8;
      }
      return result;
   }
   
   @Test
   @Ignore
   public void updateIndexReference_cspp_sm_archivage_date() {
      
      String indexName = "SM_ARCHIVAGE_DATE";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase, BytesArraySerializer.get());
      
      HColumn<String, byte[]> columnKey0 = HFactory.createColumn("rangeIndexes.0.key", 
            toByteArray(Long.valueOf(1)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey0);
      
      HColumn<String, byte[]> columnValue0 = HFactory.createColumn("rangeIndexes.0.value", 
            StringSerializer.get().toBytes("{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"20120127211654625\",\"COUNT\":843289,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue0);
      
      HColumn<String, byte[]> columnValue239 = HFactory.createColumn("rangeIndexes.239.value", 
            StringSerializer.get().toBytes("{\"ID\":250,\"LOWER_BOUND\":\"20150902135428761\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":90766757,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue239);
      
      HColumn<String, byte[]> columnRangeSize = HFactory.createColumn("rangeIndexes.size", 
            toByteArray(Long.valueOf(250)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnRangeSize);
      
      mutator.execute();
   }
   
   @Test
   @Ignore
   public void updateIndexReference_cspp_sm_creation_date() {
      
      String indexName = "SM_CREATION_DATE";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase, BytesArraySerializer.get());
      
      HColumn<String, byte[]> columnKey0 = HFactory.createColumn("rangeIndexes.0.key", 
            toByteArray(Long.valueOf(1)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey0);
      
      HColumn<String, byte[]> columnValue0 = HFactory.createColumn("rangeIndexes.0.value", 
            StringSerializer.get().toBytes("{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"20120107000000000\",\"COUNT\":656457,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue0);
      
      HColumn<String, byte[]> columnKey1 = HFactory.createColumn("rangeIndexes.1.key", 
            toByteArray(Long.valueOf(2)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey1);
      
      HColumn<String, byte[]> columnValue1 = HFactory.createColumn("rangeIndexes.1.value", 
            StringSerializer.get().toBytes("{\"ID\":2,\"LOWER_BOUND\":\"20120107000000000\",\"UPPER_BOUND\":\"20120112000000000\",\"COUNT\":542310,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue1);
      
      HColumn<String, byte[]> columnKey2 = HFactory.createColumn("rangeIndexes.2.key", 
            toByteArray(Long.valueOf(3)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey2);
      
      HColumn<String, byte[]> columnValue2 = HFactory.createColumn("rangeIndexes.2.value", 
            StringSerializer.get().toBytes("{\"ID\":3,\"LOWER_BOUND\":\"20120112000000000\",\"UPPER_BOUND\":\"20120117000000000\",\"COUNT\":541960,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue2);
      
      HColumn<String, byte[]> columnKey3 = HFactory.createColumn("rangeIndexes.3.key", 
            toByteArray(Long.valueOf(4)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey3);
      
      HColumn<String, byte[]> columnValue3 = HFactory.createColumn("rangeIndexes.3.value", 
            StringSerializer.get().toBytes("{\"ID\":4,\"LOWER_BOUND\":\"20120117000000000\",\"UPPER_BOUND\":\"20120120000000000\",\"COUNT\":373763,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue3);
      
      HColumn<String, byte[]> columnKey4 = HFactory.createColumn("rangeIndexes.4.key", 
            toByteArray(Long.valueOf(5)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey4);
      
      HColumn<String, byte[]> columnValue4 = HFactory.createColumn("rangeIndexes.4.value", 
            StringSerializer.get().toBytes("{\"ID\":5,\"LOWER_BOUND\":\"20120120000000000\",\"UPPER_BOUND\":\"20120123000000000\",\"COUNT\":592322,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue4);
      
      HColumn<String, byte[]> columnValue80 = HFactory.createColumn("rangeIndexes.80.value", 
            StringSerializer.get().toBytes("{\"ID\":84,\"LOWER_BOUND\":\"20130116000000000\",\"UPPER_BOUND\":\"20130119000000000\",\"COUNT\":1159125,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue80);
      
      HColumn<String, byte[]> columnValue149 = HFactory.createColumn("rangeIndexes.149.value", 
            StringSerializer.get().toBytes("{\"ID\":159,\"LOWER_BOUND\":\"20131214000000000\",\"UPPER_BOUND\":\"20131216000000000\",\"COUNT\":1146966,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue149);
      
      HColumn<String, byte[]> columnValue198 = HFactory.createColumn("rangeIndexes.198.value", 
            StringSerializer.get().toBytes("{\"ID\":197,\"LOWER_BOUND\":\"20140417000000000\",\"UPPER_BOUND\":\"20140420000000000\",\"COUNT\":1188429,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue198);
      
      HColumn<String, byte[]> columnValue268 = HFactory.createColumn("rangeIndexes.268.value", 
            StringSerializer.get().toBytes("{\"ID\":258,\"LOWER_BOUND\":\"20141212000000000\",\"UPPER_BOUND\":\"20141215000000000\",\"COUNT\":1158651,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue268);
      
      HColumn<String, byte[]> columnValue379 = HFactory.createColumn("rangeIndexes.379.value", 
            StringSerializer.get().toBytes("{\"ID\":400,\"LOWER_BOUND\":\"20161225000000000\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":1035761,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue379);
            
      HColumn<String, byte[]> columnValue392 = HFactory.createColumn("rangeIndexes.392.value", 
            StringSerializer.get().toBytes("{\"ID\":391,\"LOWER_BOUND\":\"20161106000000000\",\"UPPER_BOUND\":\"20161112000000000\",\"COUNT\":540785,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue392);
      
      HColumn<String, byte[]> columnValue393 = HFactory.createColumn("rangeIndexes.393.value", 
            StringSerializer.get().toBytes("{\"ID\":390,\"LOWER_BOUND\":\"20161101000000000\",\"UPPER_BOUND\":\"20161106000000000\",\"COUNT\":649701,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue393);
      
      HColumn<String, byte[]> columnValue394 = HFactory.createColumn("rangeIndexes.394.value", 
            StringSerializer.get().toBytes("{\"ID\":389,\"LOWER_BOUND\":\"20161027000000000\",\"UPPER_BOUND\":\"20161101000000000\",\"COUNT\":541466,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue394);
      
      HColumn<String, byte[]> columnValue395 = HFactory.createColumn("rangeIndexes.395.value", 
            StringSerializer.get().toBytes("{\"ID\":388,\"LOWER_BOUND\":\"20161023000000000\",\"UPPER_BOUND\":\"20161027000000000\",\"COUNT\":323597,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue395);
      
      HColumn<String, byte[]> columnKey396 = HFactory.createColumn("rangeIndexes.396.key", 
            toByteArray(Long.valueOf(384)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey396);
      
      HColumn<String, byte[]> columnValue396 = HFactory.createColumn("rangeIndexes.396.value", 
            StringSerializer.get().toBytes("{\"ID\":384,\"LOWER_BOUND\":\"20161002000000000\",\"UPPER_BOUND\":\"20161006000000000\",\"COUNT\":412558,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue396);
      
      mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", "rangeIndexes.397.key", StringSerializer.get());
      mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", "rangeIndexes.397.value", StringSerializer.get());
      
      mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", "rangeIndexes.398.key", StringSerializer.get());
      mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", "rangeIndexes.398.value", StringSerializer.get());
      
      mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", "rangeIndexes.399.key", StringSerializer.get());
      mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", "rangeIndexes.399.value", StringSerializer.get());
      
      mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", "rangeIndexes.400.key", StringSerializer.get());
      mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", "rangeIndexes.400.value", StringSerializer.get());
      
      HColumn<String, byte[]> columnRangeSize = HFactory.createColumn("rangeIndexes.size", 
            toByteArray(Long.valueOf(396)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnRangeSize);
      
      mutator.execute();
   }
   
   @Test
   @Ignore
   public void updateIndexReference_cspp_sm_modification_date() {
      
      String indexName = "SM_MODIFICATION_DATE";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase, BytesArraySerializer.get());
      
      HColumn<String, byte[]> columnKey0 = HFactory.createColumn("rangeIndexes.0.key", 
            toByteArray(Long.valueOf(1)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey0);
      
      HColumn<String, byte[]> columnValue0 = HFactory.createColumn("rangeIndexes.0.value", 
            StringSerializer.get().toBytes("{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"20130103113223965\",\"COUNT\":409144,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue0);
      
      HColumn<String, byte[]> columnValue299 = HFactory.createColumn("rangeIndexes.299.value", 
            StringSerializer.get().toBytes("{\"ID\":300,\"LOWER_BOUND\":\"20160322122557342\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":91770791,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue299);
      
      HColumn<String, byte[]> columnValue300 = HFactory.createColumn("rangeIndexes.300.value", 
            StringSerializer.get().toBytes("{\"ID\":300,\"LOWER_BOUND\":\"20160322122557342\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":91770791,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue300);
      
      HColumn<String, byte[]> columnRangeSize = HFactory.createColumn("rangeIndexes.size", 
            toByteArray(Long.valueOf(300)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnRangeSize);
      
      mutator.execute();
   }
   
   @Test
   @Ignore
   public void updateIndexReference_cspp_den() {
      
      String indexName = "den";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase, BytesArraySerializer.get());
      
      HColumn<String, byte[]> columnKey0 = HFactory.createColumn("rangeIndexes.0.key", 
            toByteArray(Long.valueOf(1)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey0);
      
      HColumn<String, byte[]> columnValue0 = HFactory.createColumn("rangeIndexes.0.value", 
            StringSerializer.get().toBytes("{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"a g s energies\",\"COUNT\":769203,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue0);
      
      HColumn<String, byte[]> columnKey1 = HFactory.createColumn("rangeIndexes.1.key", 
            toByteArray(Long.valueOf(2)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey1);
      
      HColumn<String, byte[]> columnValue1 = HFactory.createColumn("rangeIndexes.1.value", 
            StringSerializer.get().toBytes("{\"ID\":2,\"LOWER_BOUND\":\"a g s energies\",\"UPPER_BOUND\":\"ab der halden maxime\",\"COUNT\":563745,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue1);
      
      HColumn<String, byte[]> columnValue171 = HFactory.createColumn("rangeIndexes.171.value", 
            StringSerializer.get().toBytes("{\"ID\":167,\"LOWER_BOUND\":\"mme bonnet marie helene\",\"UPPER_BOUND\":\"mme david bellouard valerie\",\"COUNT\":1159373,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue171);
      
      HColumn<String, byte[]> columnValue265 = HFactory.createColumn("rangeIndexes.265.value", 
            StringSerializer.get().toBytes("{\"ID\":281,\"LOWER_BOUND\":\"toscani chape et carrelage\",\"UPPER_BOUND\":\"transports besombes\",\"COUNT\":1174223,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue265);
      
      HColumn<String, byte[]> columnValue292 = HFactory.createColumn("rangeIndexes.292.value", 
            StringSerializer.get().toBytes("{\"ID\":293,\"LOWER_BOUND\":\"yigit tamer\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":937201,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue292);
      
      HColumn<String, byte[]> columnValue293 = HFactory.createColumn("rangeIndexes.293.value", 
            StringSerializer.get().toBytes("{\"ID\":293,\"LOWER_BOUND\":\"yigit tamer\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":937201,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue293);
      
      HColumn<String, byte[]> columnRangeSize = HFactory.createColumn("rangeIndexes.size", 
            toByteArray(Long.valueOf(293)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnRangeSize);
      
      mutator.execute();
   }
   
   @Test
   @Ignore
   public void updateIndexReference_cspp_sm_life_cycle_reference_date() {
      
      String indexName = "SM_LIFE_CYCLE_REFERENCE_DATE";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase, BytesArraySerializer.get());
      
      HColumn<String, byte[]> columnKey1 = HFactory.createColumn("rangeIndexes.1.key", 
            toByteArray(Long.valueOf(1)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey1);
      
      HColumn<String, byte[]> columnValue1 = HFactory.createColumn("rangeIndexes.1.value", 
            StringSerializer.get().toBytes("{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"20120124000000000\",\"COUNT\":594358,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue1);
      
      HColumn<String, byte[]> columnKey9 = HFactory.createColumn("rangeIndexes.9.key", 
            toByteArray(Long.valueOf(2)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey9);
      
      HColumn<String, byte[]> columnValue9 = HFactory.createColumn("rangeIndexes.9.value", 
            StringSerializer.get().toBytes("{\"ID\":2,\"LOWER_BOUND\":\"20120124000000000\",\"UPPER_BOUND\":\"20120202000000000\",\"COUNT\":744007,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue9);
      
      HColumn<String, byte[]> columnKey400 = HFactory.createColumn("rangeIndexes.400.key", 
            toByteArray(Long.valueOf(4)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey400);
      
      HColumn<String, byte[]> columnValue400 = HFactory.createColumn("rangeIndexes.400.value", 
            StringSerializer.get().toBytes("{\"ID\":4,\"LOWER_BOUND\":\"20120529091102320\",\"UPPER_BOUND\":\"20120910000000000\",\"COUNT\":1072659,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue400);
      
      HColumn<String, byte[]> columnKey401 = HFactory.createColumn("rangeIndexes.401.key", 
            toByteArray(Long.valueOf(5)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey401);
      
      HColumn<String, byte[]> columnValue401 = HFactory.createColumn("rangeIndexes.401.value", 
            StringSerializer.get().toBytes("{\"ID\":5,\"LOWER_BOUND\":\"20120910000000000\",\"UPPER_BOUND\":\"20130104000000000\",\"COUNT\":1125684,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue401);
      
      HColumn<String, byte[]> columnKey402 = HFactory.createColumn("rangeIndexes.402.key", 
            toByteArray(Long.valueOf(6)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey402);
      
      HColumn<String, byte[]> columnValue402 = HFactory.createColumn("rangeIndexes.402.value", 
            StringSerializer.get().toBytes("{\"ID\":6,\"LOWER_BOUND\":\"20130104000000000\",\"UPPER_BOUND\":\"20130116000000000\",\"COUNT\":501816,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue402);
      
      HColumn<String, byte[]> columnKey403 = HFactory.createColumn("rangeIndexes.403.key", 
            toByteArray(Long.valueOf(7)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey403);
      
      HColumn<String, byte[]> columnValue403 = HFactory.createColumn("rangeIndexes.403.value", 
            StringSerializer.get().toBytes("{\"ID\":7,\"LOWER_BOUND\":\"20130116000000000\",\"UPPER_BOUND\":\"20130118000000000\",\"COUNT\":515451,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue403);
      
      HColumn<String, byte[]> columnKey404 = HFactory.createColumn("rangeIndexes.404.key", 
            toByteArray(Long.valueOf(8)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey404);
      
      HColumn<String, byte[]> columnValue404 = HFactory.createColumn("rangeIndexes.404.value", 
            StringSerializer.get().toBytes("{\"ID\":8,\"LOWER_BOUND\":\"20130118000000000\",\"UPPER_BOUND\":\"20130131000000000\",\"COUNT\":661549,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue404);
      
      HColumn<String, byte[]> columnKey405 = HFactory.createColumn("rangeIndexes.405.key", 
            toByteArray(Long.valueOf(9)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey405);
      
      HColumn<String, byte[]> columnValue405 = HFactory.createColumn("rangeIndexes.405.value", 
            StringSerializer.get().toBytes("{\"ID\":9,\"LOWER_BOUND\":\"20130131000000000\",\"UPPER_BOUND\":\"20130222000000000\",\"COUNT\":727128,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue405);
      
      HColumn<String, byte[]> columnKey406 = HFactory.createColumn("rangeIndexes.406.key", 
            toByteArray(Long.valueOf(10)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey406);
      
      HColumn<String, byte[]> columnValue406 = HFactory.createColumn("rangeIndexes.406.value", 
            StringSerializer.get().toBytes("{\"ID\":10,\"LOWER_BOUND\":\"20130222000000000\",\"UPPER_BOUND\":\"20130423000000000\",\"COUNT\":898586,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue406);
      
      HColumn<String, byte[]> columnKey407 = HFactory.createColumn("rangeIndexes.407.key", 
            toByteArray(Long.valueOf(400)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey407);
      
      HColumn<String, byte[]> columnValue407 = HFactory.createColumn("rangeIndexes.407.value", 
            StringSerializer.get().toBytes("{\"ID\":400,\"LOWER_BOUND\":\"20160329145255326\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":13890165,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue407);
      
      HColumn<String, byte[]> columnKey383 = HFactory.createColumn("rangeIndexes.383.key", 
            toByteArray(Long.valueOf(11)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey383);
      
      HColumn<String, byte[]> columnValue383 = HFactory.createColumn("rangeIndexes.383.value", 
            StringSerializer.get().toBytes("{\"ID\":11,\"LOWER_BOUND\":\"20130423000000000\",\"UPPER_BOUND\":\"20130725195138270\",\"COUNT\":1112598,\"STATE\":\"NOMINAL\"}"), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue383);
      
      HColumn<String, byte[]> columnRangeSize = HFactory.createColumn("rangeIndexes.size", 
            toByteArray(Long.valueOf(407)), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnRangeSize);
      
      mutator.execute();
   }
   
   @Test
   @Ignore
   public void updateIndexReferenceFromCsv_cspp_srt() throws IOException {
      
      String indexName = "srt";
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/divers/cspp_rebuild_srt.csv")));
      
      Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase, BytesArraySerializer.get());
      
      Long key = Long.valueOf(-1);
      Long numRow = Long.valueOf(-1);
      String rangeInfo = "";
      // boucle de verif
      String line = in.readLine();
      while (line != null) {
         String[] valeurs = line.split(";"); 
         
         key = Long.valueOf(valeurs[0]);
         
         numRow = Long.valueOf(valeurs[1]);
         
         String borneMin = valeurs[2];
         
         String borneMax = valeurs[3];
         
         String keyRange = String.format("rangeIndexes.%d.key", key);
         String valueRange = String.format("rangeIndexes.%d.value", key);
         
         rangeInfo = String.format("{\"ID\":%d,\"LOWER_BOUND\":\"%s\",\"UPPER_BOUND\":\"%s\",\"COUNT\":0,\"STATE\":\"NOMINAL\"}", numRow, borneMin, borneMax);
         
      
         //LOGGER.info("{} - {} - {}", new Object[] { keyRange, numRow, rangeInfo });
         
         HColumn<String, byte[]> columnKey = HFactory.createColumn(keyRange, 
               toByteArray(numRow), StringSerializer.get(), BytesArraySerializer.get());
         mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey);
         
         HColumn<String, byte[]> columnValue = HFactory.createColumn(valueRange, 
               StringSerializer.get().toBytes(rangeInfo), StringSerializer.get(), BytesArraySerializer.get());
         mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue);
         
         line = in.readLine();
      }
      
      in.close();
      
      // creation du doublon de la derniere ligne
      key = key.longValue() + 1;
      String keyRange = String.format("rangeIndexes.%d.key", key);
      String valueRange = String.format("rangeIndexes.%d.value", key);
      
      //LOGGER.info("{} - {} - {}", new Object[] { keyRange, numRow, rangeInfo });
      
      HColumn<String, byte[]> columnKey = HFactory.createColumn(keyRange, 
            toByteArray(numRow), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnKey);
      
      HColumn<String, byte[]> columnValue = HFactory.createColumn(valueRange, 
            StringSerializer.get().toBytes(rangeInfo), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnValue);
      
      HColumn<String, byte[]> columnRangeSize = HFactory.createColumn("rangeIndexes.size", 
            toByteArray(key), StringSerializer.get(), BytesArraySerializer.get());
      mutator.addInsertion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", columnRangeSize);
      
      for (long index = key.longValue() + 1; index <= 400; index++) {
         keyRange = String.format("rangeIndexes.%d.key", index);
         valueRange = String.format("rangeIndexes.%d.value", index);
         
         //LOGGER.info("delete du range {} - {}", new Object[] { keyRange, valueRange });
         
         mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", keyRange, StringSerializer.get());
         mutator.addDeletion(StringSerializer.get().toBytes(buffer.toString()), "IndexReference", valueRange, StringSerializer.get());
      }
      
      mutator.execute();
   }
}
