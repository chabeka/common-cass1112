package fr.urssaf.astyanaxtest.cleanjob;

import org.junit.Before;
import org.junit.Test;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class JobCleanerTest {

   /**
    * Représente le keyspace cassandra sur lequel on travaille
    */
   Keyspace keyspace;

   @Before
   public void init() throws Exception {
      String servers;
      // servers =
      // "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      // //GIVN
      servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
      // servers = "cnp69gincleagntcas1.cer69.recouv:9160";
      // servers = "cnp69gntcas1:9160, cnp69gntcas2:9160, cnp69gntcas3:9160";
      // // Production
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      // servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers =
      // "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      // servers = "cer69-saeint3:9160";
      // servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; // Préprod
      // servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160"; // Charge
      // servers = "cnp6gntcvecas1.cve.recouv:9160,cnp3gntcvecas1.cve.recouv:9160,cnp7gntcvecas1.cve.recouv:9160"; // Charge GNT
      // servers = "cnp69gingntcas1.cer69.recouv:9160,cnp69gingntcas2.cer69.recouv:9160,cnp69gingntcas3.cer69.recouv:9160";
      // servers = "cnp69intgntc1cas1.gidn.recouv:9160,cnp69intgntc1cas2.gidn.recouv:9160,cnp69intgntc1cas3.gidn.recouv:9160";
      // servers = "cnp69intgntp1cas1.gidn.recouv:9160,cnp69intgntp1cas2.gidn.recouv:9160,cnp69intgntp1cas3.gidn.recouv:9160";
      // servers = "cnp69gingntc1cas1.cer69.recouv:9160,cnp69gingntc1cas2.cer69.recouv:9160,cnp69gingntc1cas3.cer69.recouv:9160";
      // servers = "cnp69gingntp1cas1.cer69.recouv:9160,cnp69gingntp1cas2.cer69.recouv:9160,cnp69gingntp1cas3.cer69.recouv:9160";

      final AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
                                                                                        "root",
                                                                                        "regina4932");

      final AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
                                                                             .forCluster("Docubase")
                                                                             .forKeyspace("Docubase")
                                                                             .withAstyanaxConfiguration(
                                                                                                        new AstyanaxConfigurationImpl()
                                                                                                                                       .setDiscoveryType(NodeDiscoveryType.NONE)
                                                                                                                                       .setDefaultReadConsistencyLevel(
                                                                                                                                                                       ConsistencyLevel.CL_ONE)
                                                                                                                                       .setDefaultWriteConsistencyLevel(
                                                                                                                                                                        ConsistencyLevel.CL_QUORUM))
                                                                             .withConnectionPoolConfiguration(
                                                                                                              new ConnectionPoolConfigurationImpl("MyConnectionPool")
                                                                                                                                                                     .setPort(9160)
                                                                                                                                                                     .setMaxConnsPerHost(1)
                                                                                                                                                                     .setSeeds(servers)
                                                                                                                                                                     .setAuthenticationCredentials(credentials))
                                                                             .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                                                                             .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();
      keyspace = context.getClient();
   }

   @Test
   public void purgeOldJobsTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      final int daysToKeep = 8;
      cleaner.purgeOldJobs(keyspace, daysToKeep);
   }

   @Test
   public void deleteOneJobInstanceTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      // Attention : c'est le jobInstanceId qu'il faut mettre, pas le jobExecutionId affiché dans DFCE Control Center
      final int jobInstanceId = 7417;
      cleaner.deleteOneJobInstance(keyspace, jobInstanceId);
   }

   @Test
   public void setJobAsNotRunningTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      // String jobKey = "MANAGE_RANGE_INDEX_JOB|GNT-PROD|cot&apr&atr&ame&sm_document_type&sm_archivage_date&";
      final String jobKey = "MANAGE_RANGE_INDEX_JOB|GNT-PROD|cot&cop&sm_document_type&sm_archivage_date&";
      cleaner.setJobAsNotRunning(keyspace, jobKey);
   }

   @Test
   public void findNonExistantJobInstancesTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      cleaner.findNonExistantJobInstances(keyspace);
   }

}
