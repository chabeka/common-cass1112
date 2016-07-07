package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.cassandra.serializers.ObjectSerializer;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SaeUpdaterTest {

   Keyspace keyspace;
   Cluster cluster;
   PrintStream sysout;
   Updater updater;

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

      // servers =
      // "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp31saecas1.cer31.recouv:9160";
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      // servers = "cer69imageint9.cer69.recouv:9160";
      servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers =
      // "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      // servers =
      // "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      // servers = "cer69-saeint3.cer69.recouv:9160";
      // servers =
      // "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";

      // servers = "cnp69devgntcas1.gidn.recouv:9160";
      // servers = "hwi69devsaecas1.cer69.recouv:9160";

      // INTEGRATION CLIENTE
      // -------------------
      // servers =
      // "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
      // servers =
      // "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";

      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      cluster = HFactory.getOrCreateCluster("SAE", hostConfigurator);
      keyspace = HFactory.createKeyspace("SAE", cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);

      sysout = new PrintStream(System.out, true, "UTF-8");

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      // sysout = new PrintStream("c:/temp/out.txt");
      updater = new Updater(keyspace, sysout);
   }

   @After
   public void close() {
      // cluster.getConnectionManager().shutdown();
      HFactory.shutdownCluster(cluster);
   }

   @Test
   public void testUpdateParametersVersionBDD() throws Exception {
      updater.updateColumn("Parameters", "parameters", "versionBDD",
            Long.valueOf(17));
   }

   @Test
   public void testUpdateParametersIsRunningCorbeille() throws Exception {
      updater.updateColumn("Parameters", "parametresCorbeille",
            "PURGE_CORBEILLE_IS_RUNNING", Boolean.FALSE, ObjectSerializer.get());
   }

   @Test
   public void testUpdateParametersDateDebutCorbeille() throws Exception {
      updater.updateColumn("Parameters", "parametresCorbeille",
            "PURGE_CORBEILLE_DATE_DEBUT_PURGE",
            DateUtils.addDays(new Date(), -20), ObjectSerializer.get());
   }
   
   @Test
   public void testUpdateParametersDureeRetentionCorbeille() throws Exception {
      updater.updateColumn("Parameters", "parametresCorbeille",
            "PURGE_CORBEILLE_DUREE", 0, ObjectSerializer.get());
   }

}
