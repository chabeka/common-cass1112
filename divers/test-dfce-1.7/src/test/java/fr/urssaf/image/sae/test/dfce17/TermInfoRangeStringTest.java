package fr.urssaf.image.sae.test.dfce17;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public class TermInfoRangeStringTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(TermInfoRangeStringTest.class);
   
   // Integration cliente GNT
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   //private String nomBase = "GNT-INT";
   
   // Integration cliente GNS
   private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   private String nomBase = "SAE-INT";
   
   // Pre-prod GNS
   //private String hosts = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas2.cer69.recouv,cnp69pregnscas3.cer69.recouv,cnp69pregnscas4.cer69.recouv,cnp69pregnscas5.cer69.recouv,cnp69pregnscas6.cer69.recouv";
   //private String nomBase = "SAE-PROD";
   
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
   
   @Test
   public void findTermInfoRangeStringSiretFiltered() {
      
      String cfName = "TermInfoRangeString";
      String metaRecherchee = "srt";
      String rangeRecherche = "5516";
      UUID idBaseDocubase = null;
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // recupere l'id de la base
      idBaseDocubase = getBaseId(keyspaceDocubase);
      
      byte[] zero = new byte[1];
      zero[0] = 0;
      
      LOGGER.debug("Recuperation de la liste des index (" + cfName + ")");
      
      Composite compositeKey = new Composite();
      compositeKey.add(StringSerializer.get().toByteBuffer(""));
      compositeKey.add(StringSerializer.get().toByteBuffer(metaRecherchee));
      compositeKey.add(UUIDSerializer.get().toByteBuffer(idBaseDocubase));
      compositeKey.add(BytesArraySerializer.get().toByteBuffer(zero));
      
      Composite compositeCol = new Composite();
      compositeCol.add(StringSerializer.get().toByteBuffer(rangeRecherche));
      
      SliceQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily(cfName);
      queryDocubase.setKey(CompositeSerializer.get().toBytes(compositeKey));
      AllColumnsIterator<Composite, byte[]> iterColonne = new AllColumnsIterator<Composite, byte[]>(queryDocubase);
      long nbColonne = 0;
      while (iterColonne.hasNext()) {
         HColumn<Composite, byte[]> colonne = iterColonne.next();
         // Le nom de la colonne est composé de 
         // - siret
         // - l'uuid du document
         // - 0.0.0
         String siret = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
         UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
         String numero = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(2));
         if (siret.startsWith(rangeRecherche)) {
            LOGGER.debug("        {}:{}:{}", new String[] { siret, idDoc.toString(), numero});
            nbColonne++;
         }
      }
      LOGGER.debug("    {} colonnes", nbColonne );
   }

   private UUID getBaseId(Keyspace keyspaceDocubase) {
      UUID idBaseDocubase = null;
      LOGGER.debug("Recuperation de l'identifiant de la base");
      SliceQuery<String,String,byte[]> sliceQueryBase= HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      sliceQueryBase.setColumnFamily("BasesReference").setKey(nomBase);
      sliceQueryBase.setColumnNames("uuid");
      QueryResult<ColumnSlice<String, byte[]>> sliceResultBase = sliceQueryBase.execute();
      if (sliceResultBase != null && sliceResultBase.get() != null) {
         ColumnSlice<String, byte[]> columns = sliceResultBase.get();
         HColumn<String, byte[]> colonne = columns.getColumnByName("uuid");
         if (colonne != null) {
            UUID idBase = UUIDSerializer.get().fromBytes(colonne.getValue());
            idBaseDocubase = idBase;
            LOGGER.debug("Id Base {}", new String[] { idBase.toString() });
         }
      }
      return idBaseDocubase;
   }
   
   @Test
   public void findTermInfoRangeStringSiret() {
      
      String cfName = "TermInfoRangeString";
      String metaRecherchee = "srt";
      String rangeRecherche = "5516";
      UUID idBaseDocubase = null;
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // recupere l'id de la base
      idBaseDocubase = getBaseId(keyspaceDocubase);
      
      byte[] zero = new byte[1];
      zero[0] = 0;
      
      LOGGER.debug("Recuperation de la liste des index (" + cfName + ")");
      
      Composite compositeKey = new Composite();
      compositeKey.add(StringSerializer.get().toByteBuffer(""));
      compositeKey.add(StringSerializer.get().toByteBuffer(metaRecherchee));
      compositeKey.add(UUIDSerializer.get().toByteBuffer(idBaseDocubase));
      compositeKey.add(BytesArraySerializer.get().toByteBuffer(zero));
      
      LOGGER.debug("{}", rangeRecherche.getBytes());
      
      Composite compositeRangeMin = new Composite();
      compositeRangeMin.addComponent(0, StringSerializer.get().toByteBuffer(StringUtils.rightPad(rangeRecherche, 14, '0')), ComponentEquality.EQUAL);
      //compositeRangeMin.add(UUIDSerializer.get().toByteBuffer(UUID.fromString("00000000-0000-0000-0000-000000000000")));
      //compositeRangeMin.add(StringSerializer.get().toByteBuffer("0.0.0"));
      byte[] rangeColMin = CompositeSerializer.get().toBytes(compositeRangeMin);
      
      Composite compositeRangeMax = new Composite();
      compositeRangeMax.addComponent(0, StringSerializer.get().toByteBuffer(StringUtils.rightPad(rangeRecherche, 14, '9')), ComponentEquality.GREATER_THAN_EQUAL);
      //compositeRangeMax.add(UUIDSerializer.get().toByteBuffer(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")));
      //compositeRangeMax.add(StringSerializer.get().toByteBuffer("0.0.0"));
      byte[] rangeColMax = CompositeSerializer.get().toBytes(compositeRangeMax);
      
      SliceQuery<byte[], byte[], byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily(cfName);
      queryDocubase.setKey(CompositeSerializer.get().toBytes(compositeKey));
      queryDocubase.setRange(rangeColMin, rangeColMax, false, 1000);
      QueryResult<ColumnSlice<byte[], byte[]>> resultat = queryDocubase.execute();
      Iterator<HColumn<byte[], byte[]>> iterColonne = resultat.get().getColumns().iterator();
      long nbColonne = 0;
      while (iterColonne.hasNext()) {
         HColumn<byte[], byte[]> colonne = iterColonne.next();
         // Le nom de la colonne est composé de 
         // - siret
         // - l'uuid du document
         // - 0.0.0
         Composite compositeCol = CompositeSerializer.get().fromBytes(colonne.getName());
         String siret = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeCol.get(0));
         UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) compositeCol.get(1));
         String numero = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeCol.get(2));
         LOGGER.debug("        {}:{}:{}", new String[] { siret, idDoc.toString(), numero});
         LOGGER.debug("        {}", colonne.getName());
         nbColonne++;
      }
      LOGGER.debug("    {} colonnes", nbColonne );
   }
}

