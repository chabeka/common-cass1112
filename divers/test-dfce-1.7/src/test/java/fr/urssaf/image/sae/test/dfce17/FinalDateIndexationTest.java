package fr.urssaf.image.sae.test.dfce17;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public class FinalDateIndexationTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(FinalDateIndexationTest.class);
   
   // Développement
   //private String hosts = "cer69imageint10.cer69.recouv:9160";
   
   // Integration cliente GNT
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   
   // Integration cliente GNS
   private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   
   // Prod nationale GNT
   //private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   
   // Prod nationale GNS
   //private String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";

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
   
   private List<UUID> getBases(Keyspace keyspaceDocubase) {
      List<UUID> basesId = new ArrayList<UUID>();
      
      RangeSlicesQuery<String, String, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("BasesReference");
      queryDocubase.setKeys(null, null);
      queryDocubase.setRowCount(1000);
      queryDocubase.setColumnNames("uuid");
      AllRowsIterator<String, String, byte[]> iterRow = new AllRowsIterator<String, String, byte[]>(queryDocubase);
      while (iterRow.hasNext()) {
         Row<String, String, byte[]> row = iterRow.next();
         if (!row.getColumnSlice().getColumns().isEmpty() && row.getColumnSlice().getColumnByName("uuid") != null) {
            basesId.add(UUIDSerializer.get().fromBytes(row.getColumnSlice().getColumnByName("uuid").getValue()));
         }
      }
      return basesId;
   }
   
   @Test
   public void getTermInfoToDelete() {
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
            
      // l'index a au moins une valeur
      // la cle de cette index est composee de :
      // - une chaine vide
      // - le nom de l'index
      // - la valeur de l'index
      Composite compositeKey = new Composite();
      compositeKey.add(0, "");
      compositeKey.add(1, "SM_FINAL_DATE");
      compositeKey.add(2, "");
      
      Map<String, Long> mapColsToDel = new TreeMap<String, Long>();
      
      SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
      queryDocubaseTerm.setColumnFamily("TermInfo");
      queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
      AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm);
      while (iterColonneTerm.hasNext()) {
         HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
         
         // Le nom de la colonne est composé de 
         // - l'uuid de la base
         // - l'uuid du document
         // - 0.0.0
         UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
         
         if (!mapColsToDel.containsKey(baseId.toString())) {
            mapColsToDel.put(baseId.toString(), Long.valueOf(1));
         } else {
            mapColsToDel.put(baseId.toString(), mapColsToDel.get(baseId.toString()) + 1);
         }
      }
      
      for (String base : mapColsToDel.keySet()) {
         LOGGER.debug("{} columns to delete in TermInfo for baseId {}", new String[] { mapColsToDel.get(base).toString(), base });
      }
   }
   
   @Test
   public void getTermInfoRangeToDelete() throws IOException {
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      List<UUID> basesId = getBases(keyspaceDocubase);
      
      for (UUID baseId : basesId) {
      
         byte[] nbRow = new byte[] { 0 };
         // l'index a au moins une valeur
         // la cle de l'index est compose de :
         // - le nom de l'index
         // - le nom de la categorie
         // - l'uuid de la base
         // - le nombre de row de la categorie
         Composite compositeKey = new Composite();
         compositeKey.add(0, "");
         compositeKey.add(1, "SM_FINAL_DATE");
         compositeKey.add(2, baseId);
         compositeKey.add(3, nbRow);
         
         Map<String, Long> mapColsToDel = new TreeMap<String, Long>();
         // on va verifie l'indexation des documents
         
         SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubaseTerm.setColumnFamily("TermInfoRangeDatetime");
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
         AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm);
         while (iterColonneTerm.hasNext()) {
            HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
            
            // Le nom de la colonne est composé de 
            // - la valeur
            // - l'uuid du document
            // - 0.0.0
            String valeur;
            // la valeur est serialiser en string
            valeur = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)); 
            
            boolean skip = true;
            if (StringUtils.isEmpty(valeur)) {
               // on cherche les final date vide
               skip = false;
            }
            
            if (!skip) {
               //UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
               
               if (!mapColsToDel.containsKey(baseId.toString())) {
                  mapColsToDel.put(baseId.toString(), Long.valueOf(1));
               } else {
                  mapColsToDel.put(baseId.toString(), mapColsToDel.get(baseId.toString()) + 1);
               }
            }
         }
         
         for (String base : mapColsToDel.keySet()) {
            LOGGER.debug("{} columns to delete in {} for baseId {}", new String[] { mapColsToDel.get(base).toString(), "TermInfoRangeDatetime", base });
         }
      }
   }
}

