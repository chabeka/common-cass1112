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
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
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
public class CassandraTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(CassandraTest.class);
   
   // Developpement 
   //private String hosts = "cer69imageint9.cer69.recouv";
   //private String hosts = "cer69imageint10.cer69.recouv";
   
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
   
   // Pre-prod nationale GNT
   private String hosts = "cnp69pregntcas1.cer69.recouv:9160,cnp69pregntcas2.cer69.recouv:9160,cnp69pregntcas3.cer69.recouv:9160";
   
   // Pre-prod nationale GNS
   //private String hosts = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas2.cer69.recouv,cnp69pregnscas3.cer69.recouv,cnp69pregnscas4.cer69.recouv,cnp69pregnscas5.cer69.recouv,cnp69pregnscas6.cer69.recouv";
   
   // Prod nationale GNT
   //private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   
   // Prod nationale GNS
   //private String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
   
   private Cluster getCluster() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            hosts);
      return HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator, credentials);
   }
   
   @Test
   public void verifConfColumnsFamilies() {
      Cluster cluster = getCluster();
      
      String[] keyspaces = { "Docubase", "SAE" };
      boolean verifAll = true;
      
      for (String keyspace : keyspaces) {
         LOGGER.debug("Vérifie le keyspace {}", keyspace);
         KeyspaceDefinition definition = cluster.describeKeyspace(keyspace);
         List<ColumnFamilyDefinition> listeCfs = definition.getCfDefs();
         for (ColumnFamilyDefinition cfDef : listeCfs) {
            int nbMissing = 0;
            StringBuffer buffer = new StringBuffer();
            if (verifAll) {
               if (!cfDef.getCompactionStrategy().equals("org.apache.cassandra.db.compaction.LeveledCompactionStrategy")) {
                  if (nbMissing  > 0) {
                     buffer.append("\n");
                  }
                  buffer.append("CompactionStrategy: ");
                  buffer.append(cfDef.getCompactionStrategy());
                  nbMissing++;
               }
               if (cfDef.getCompressionOptions().get("sstable_compression") == null || (!cfDef.getCompressionOptions().get("sstable_compression").equals("org.apache.cassandra.io.compress.SnappyCompressor"))) {
                  if (nbMissing  > 0) {
                     buffer.append("\n");
                  }
                  buffer.append("CompressionOptions: ");
                  buffer.append(cfDef.getCompressionOptions());
                  nbMissing++;
               }
            }
            if (cfDef.getCompactionStrategyOptions().get("sstable_size_in_mb") == null || (!cfDef.getCompactionStrategyOptions().get("sstable_size_in_mb").equals("200"))) {
               if (nbMissing  > 0) {
                  buffer.append("\n");
               }
               buffer.append("CompactionStrategyOptions: ");
               buffer.append(cfDef.getCompactionStrategyOptions());
               nbMissing++;
            }
            if (nbMissing > 0) {
               LOGGER.debug("    {} : manque {}", cfDef.getName(), buffer.toString());
            }
         }
      }
   }
   
   
   
   
   
   
}

