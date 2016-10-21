package fr.urssaf.image.sae.test.divers.dfce;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
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
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.test.divers.cassandra.AllColumnsIterator;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
public class JobInstance2Test {

   private static final Logger LOGGER = LoggerFactory.getLogger(JobInstance2Test.class);

   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;

   /**
    * Recuperation
    */
   @Autowired
   private CassandraServerBean cassandraServer;

   private Keyspace getKeyspaceDocubaseFromKeyspace() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            cassandraServer.getHosts());
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
   }

   private String getAttributFromSpringBatch(String summary, String name) {
      int index = summary.indexOf(name);
      String valeur = "";
      if (index > 0) {
         int finIndex = summary.indexOf(",", index + 1);
         if (finIndex < 0) {
            finIndex = summary.indexOf("]", index + 1);
         }
         valeur = summary.substring(index + name.length() + 1, finIndex);
      }
      return valeur;
   }

   private Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }

   private byte[] convertLongToByte(long valeur) {
      return ByteBuffer.allocate(8).putLong(valeur).array();
   }


   public static String bytesToHex(byte[] bytes) {
      final char[] hexArray = "0123456789ABCDEF".toCharArray();
      char[] hexChars = new char[bytes.length * 2];
      for ( int j = 0; j < bytes.length; j++ ) {
         int v = bytes[j] & 0xFF;
         hexChars[j * 2] = hexArray[v >>> 4];
         hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
   }

   @Test
   public void findAllJobInJobInstance() {
      String nomJob = "clearEventJob";
      String[] colonnes = { "jobInstanceId", "jobInstanceName" };
      int compteurJob = 0;
      Map<Long, String> jobs = new HashMap<Long, String>();
      
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobInstance").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            //LOGGER.debug("colonne {} : {}", new String[] { columnName.getName(), new String(columnName.getValue())});
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)) {
               // un job du bon nom a ete trouve
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               Long idJobCourant = convertByteToLong(columnId.getValue());
               //LOGGER.debug("colonne {} : {}", new String[] { columnId.getName(), idJobCourant.toString()});
               if (columnId != null) {
                  String rowKey = bytesToHex(row.getKey());
                  if (jobs.containsKey(idJobCourant)) {
                     LOGGER.debug("Il existe deja un job avec l'id {} : {} (nouveau job avec cette id : {}", new Object[] { idJobCourant, jobs.get(idJobCourant), rowKey });
                  }
                  jobs.put(idJobCourant, rowKey);
               }
            }
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
   }
   
   @Test
   public void findAllJobInJobExecution() {
      //String nomJob = "indexCompositesJob";
      //String nomJob = "clearEventJob";
      String nomJob = "splitRangeIndexJob";
      String[] colonnes = { "jobInstanceId", "jobInstanceName" };
      //Long idJob = Long.valueOf(4050);
      Long idJob = Long.valueOf(4098);
      Long idJobExec = Long.valueOf(-1);
      int compteurJob = 0;
      
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobExecution").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            //LOGGER.debug("colonne {} : {}", new String[] { columnName.getName(), new String(columnName.getValue())});
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)) {
               // un job du bon nom a ete trouve
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               Long idJobCourant = convertByteToLong(columnId.getValue());
               //LOGGER.debug("colonne {} : {}", new String[] { columnId.getName(), idJobCourant.toString()});
               if (columnId != null && idJobCourant.longValue() == idJob.longValue()) {
                  String rowKey = bytesToHex(row.getKey());
                  idJobExec = convertByteToLong(row.getKey());
                  LOGGER.debug("Id job exec {} : {}", new Object[] { idJobExec, rowKey });
               }
            }
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
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
   
   @Test
   public void findIndexReferenceByMeta() throws JSONException {
      
      long compteur = 0;
      String meta = "SM_CREATION_DATE";
      
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
            String rowKey = new String(row.getKey());
            if (rowKey.startsWith(meta)) {
               LOGGER.debug("Row key {}", new String[] { rowKey });
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
                  
                  /*if (colonne.getName().matches("rangeIndexes.[0-9]*.value")) {
                     JSONObject obj = new JSONObject(new String(colonne.getValue()));
                     String id = obj.getString("ID");
                     String minRange = obj.getString("LOWER_BOUND");
                     String maxRange = obj.getString("UPPER_BOUND");
                     String count = obj.getString("COUNT");
                     LOGGER.debug("        {};{};{};{}", new Object[] { id, minRange, maxRange, count });
                  }*/
               }
               compteur++;
            }
         }
      }
      LOGGER.debug("Nb resultat : {}", compteur);
   }
   
   @Test
   public void findTermInfoRangeDatetimeByMeta() {
      
      long compteur = 0;
      String meta = "SM_ARCHIVAGE_DATE";
      
      LOGGER.debug("Recuperation de la liste des index (TermInfoRangeDatetime)");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],Composite,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("TermInfoRangeDatetime").setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], Composite, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], Composite, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], Composite, byte[]> row = iterateur.next();
            
            // la key est compose de :
            // - le type d'index (vide, RB ou SYS)
            // - le nom de la meta
            // - l'uuid de la base
            // - le nombre de row de la meta ??? le numero peut être
            Composite compositeKey = CompositeSerializer.get().fromBytes(row.getKey());
            if (!compositeKey.isEmpty() && compositeKey.size() == 4) {
               String nomMeta = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(1));
               UUID baseUuid = UUIDSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(2));
               byte[] byteNbRow = BytesArraySerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(3));
               Long nbRow = convertByteToLong(byteNbRow);
               
               if (nomMeta.equals(meta)) {
                  LOGGER.debug("Row key {}:{}:{}", new String[] { nomMeta, baseUuid.toString(), nbRow.toString() });
                  if (nbRow.longValue() > 0) {
                     SliceQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
                     queryDocubase.setColumnFamily("TermInfoRangeDatetime");
                     queryDocubase.setKey(row.getKey());
                     AllColumnsIterator<Composite, byte[]> iterColonne = new AllColumnsIterator<Composite, byte[]>(queryDocubase);
                     long nbColonne = 0;
                     while (iterColonne.hasNext()) {
                        iterColonne.next();
                        nbColonne++;
                     }
                     LOGGER.debug("    {} colonnes", nbColonne );
                  }
                  compteur++;
               }
            }
         }
      }
      LOGGER.debug("Nb resultat : {}", compteur);
   }
   
   @Test
   public void findAllIndexCounter() throws IOException {
      
      long compteur = 0;
      
      //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:/tmp/index-counter.csv")));
      
      LOGGER.debug("Recuperation de la liste des index de comptage");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("IndexCounter").setKeys(null, null);
      //rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<Composite, byte[], byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<Composite, byte[], byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<Composite, byte[], byte[]> row = iterateur.next();
            String indexKey = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
            String baseUUID = UUIDSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1)).toString();
            String counterMode = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
            LOGGER.debug("Row key {}|{}|{}", new String[] { indexKey, baseUUID, counterMode });
            
            boolean skipCount = false;
            if (indexKey.equals("SM_UUID") && baseUUID.equals("f573ae93-ac6a-4615-a23b-150fd621b5a0") && counterMode.equals("INSERT")) {
               skipCount = true;
            }
            
            long compteurCol = 0;
            try {
               //if (!skipCount) {
                  SliceQuery<Composite, byte[], byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
                  queryDocubase.setColumnFamily("IndexCounter");
                  queryDocubase.setKey(row.getKey());
                  AllColumnsIterator<byte[], byte[]> iterColonne = new AllColumnsIterator<byte[], byte[]>(queryDocubase);
                  while (iterColonne.hasNext()) {
                     HColumn<byte[], byte[]> colonne = iterColonne.next();
                     //LOGGER.debug("    {} : {}", new Object[] { colonne.getName(), new String(colonne.getValue()) });
                     compteurCol++;
                     if (compteurCol > 1000 && skipCount) {
                        break;
                     }
                  }
                  LOGGER.debug("    {} colonnes", new Object[] { compteurCol });
               //}
            } catch(RuntimeException ex) {
               LOGGER.debug("    {} colonnes", new Object[] { compteurCol });
            }
            
            /*out.write(indexKey + ";");
            out.write(baseUUID + ";");
            out.write(counterMode + ";");
            out.write(compteurCol + ";\n");*/
            
            compteur++;
         }
      }
      LOGGER.debug("Nb resultat : {}", compteur);
      
      //out.close();
   }
   
   @Test
   public void findAllJobInstanceByName() {
      String nomJob = "splitRangeIndexJob";
      String[] colonnes = { "jobInstanceId", "jobInstanceName" };
      int compteurJob = 0;
      
      LOGGER.debug("Recuperation de la liste des jobs");
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("JobInstance").setKeys(null, null);
      rangeQueryDocubase.setColumnNames(colonnes);
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnName = row.getColumnSlice().getColumnByName("jobInstanceName");
            //LOGGER.debug("colonne {} : {}", new String[] { columnName.getName(), new String(columnName.getValue())});
            if (columnName != null && new String(columnName.getValue()).equals(nomJob)) {
               // un job du bon nom a ete trouve
               HColumn<String, byte[]> columnId = row.getColumnSlice().getColumnByName("jobInstanceId");
               Long idJobCourant = convertByteToLong(columnId.getValue());
               //LOGGER.debug("colonne {} : {}", new String[] { columnId.getName(), idJobCourant.toString()});
               if (columnId != null) {
                  LOGGER.debug("idJobInstance: {}", idJobCourant);
               }
            }
            compteurJob++;
         }
      }
      LOGGER.debug("Nombre de jobs parcourus : {}", compteurJob);
   }
}
