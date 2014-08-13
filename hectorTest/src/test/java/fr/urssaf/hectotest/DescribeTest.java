package fr.urssaf.hectotest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.Before;
import org.junit.Test;

public class DescribeTest {

   private Cluster cluster;
   
   
   @Before
   public void init() throws Exception {
      
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      
      HashMap<String, String> credentials = new HashMap<String, String>() {

         private static final long serialVersionUID = 4939505918052681809L;
         
         {
            put("username", "root");
         }
         {
            put("password", "regina4932");
         }
      };
      
      String servers = "cer69imageint9.cer69.recouv:9160";
//      String servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
//      String servers = "cer69-saeint3.cer69.recouv:9160";
//      String servers = "cnp69pprodsaecas1.cer69.recouv:9160,cnp69pprodsaecas2.cer69.recouv:9160,cnp69pprodsaecas3.cer69.recouv:9160";
//      String servers = "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160";
//      String servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
//      String servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp31saecas1.cer31.recouv:9160";

      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      
      cluster = HFactory.getOrCreateCluster("Cluster", hostConfigurator, credentials);
      
   }
   
   
   private void describeKeyspace(String nomKeyspace, boolean avecCf) {
      
      KeyspaceDefinition ksDef = cluster.describeKeyspace(nomKeyspace);
      describeKeyspace(ksDef, avecCf);
   }
   
   
   private void describeKeyspace(KeyspaceDefinition ksDef, boolean avecCf) {
   
      System.out.println("Keyspace: " + ksDef.getName());
      
      System.out.println("Replication Factor: " + ksDef.getReplicationFactor());
      
      System.out.println("Strategy Class: " + ksDef.getStrategyClass());
      if (ksDef.getStrategyOptions().isEmpty()) {
         System.out.println("Strategy Options : aucune");
      } else {
         for (Map.Entry<String,String> entry: ksDef.getStrategyOptions().entrySet()) {
            System.out.println("Strategy Options :");
            System.out.println("  " + entry.getKey() + "=" + entry.getValue());
         }
      }

      // ksDef.isDurableWrites()      
      
      if (avecCf) {
         System.out.println("Column Families : ");
         for(ColumnFamilyDefinition cfDef: ksDef.getCfDefs()) {
            describeColumnFamily(cfDef);
         }
      }
      
   }
   
   
   private void describeColumnFamily(ColumnFamilyDefinition cfDef) {
      System.out.println("CF \"" + cfDef.getName() + "\"");
      System.out.println("  Comment: " + cfDef.getComment());
      System.out.println("  KeyValidationClass: " + cfDef.getKeyValidationClass());
      System.out.println("  ComparatorType: " + cfDef.getComparatorType().getClassName());
      // System.out.println("  ComparatorTypeAlias: " + cfDef.getComparatorTypeAlias());
      System.out.println("  CompactionStrategy: " + cfDef.getCompactionStrategy());
      if (cfDef.getCompactionStrategyOptions().isEmpty()) {
         System.out.println("  CompactionStrategyOptions: aucune");
      } else {
         for (Map.Entry<String,String> entry: cfDef.getCompactionStrategyOptions().entrySet()) {
            System.out.println("  CompactionStrategyOptions :");
            System.out.println("    " + entry.getKey() + "=" + entry.getValue());
         }
      }
      System.out.println("  GC Grace Seconds: " + cfDef.getGcGraceSeconds());
      System.out.println("  Read Repair Chance: " + cfDef.getReadRepairChance());
   }
   
   
   private void describeKeyspaces(boolean avecCf) {
      List<KeyspaceDefinition> ksDefs = cluster.describeKeyspaces();
      for(KeyspaceDefinition ksDef: ksDefs) {
         describeKeyspace(ksDef, avecCf);
         System.out.println();
      }
   }
   
   
   @Test
   public void describeKeyspacesSansCf() {
      describeKeyspaces(Boolean.FALSE);
   }

   
   @Test
   public void describeKeyspacesAvecCf() {
      describeKeyspaces(Boolean.TRUE);
   }

   
   @Test
   public void describeKeyspaceSAE() {
      describeKeyspace("SAE", Boolean.TRUE);
   }

   
   @Test
   public void describeKeyspaceDocubase() {
      describeKeyspace("Docubase", Boolean.TRUE);
   }
   
}
