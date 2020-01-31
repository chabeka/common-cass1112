package fr.urssaf.astyanaxtest.cleanjob;

import java.util.List;

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

import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class SAEJobCleanerTest {

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
      // servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
      // servers = "hwi69gincleasaecas1.cer69.recouv:9160";
      // servers = "cnp69gntcas1:9160, cnp69gntcas2:9160, cnp69gntcas3:9160";
      // // Production
      // servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers =
      // "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      // servers = "cer69-saeint3:9160";
      // servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; // Préprod
      // servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160"; // Charge
      servers = "cnp6gntcvecas1.cve.recouv:9160,cnp3gntcvecas1.cve.recouv:9160,cnp7gntcvecas1.cve.recouv:9160"; // Charge GNT
      // servers = "cnp69gingntcas1.cer69.recouv:9160,cnp69gingntcas2.cer69.recouv:9160,cnp69gingntcas3.cer69.recouv:9160";
      // servers = "cnp69intgntc1cas1.gidn.recouv:9160,cnp69intgntc1cas2.gidn.recouv:9160,cnp69intgntc1cas3.gidn.recouv:9160";
      // servers = "cnp69intgntp1cas1.gidn.recouv:9160,cnp69intgntp1cas2.gidn.recouv:9160,cnp69intgntp1cas3.gidn.recouv:9160";
      // servers = "cnp69gingntc1cas1.cer69.recouv:9160,cnp69gingntc1cas2.cer69.recouv:9160,cnp69gingntc1cas3.cer69.recouv:9160";
      // servers = "cnp69gingntp1cas1.cer69.recouv:9160,cnp69gingntp1cas2.cer69.recouv:9160,cnp69gingntp1cas3.cer69.recouv:9160";
      // servers = "cnp69intgntc1cas1.gidn.recouv,cnp69intgntc1cas2.gidn.recouv";

      final AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            "root",
            "regina4932");

      final AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
            .forCluster("SAE")
            .forKeyspace("SAE")
            .withAstyanaxConfiguration(
                  new AstyanaxConfigurationImpl()
                  .setDiscoveryType(NodeDiscoveryType.NONE)
                  .setDefaultReadConsistencyLevel(
                        ConsistencyLevel.CL_QUORUM)
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
   public void jobKeyToJobInstanceIdsTest() throws Exception {
      final SAEJobCleaner cleaner = new SAEJobCleaner();
      final byte[] jobKey = ConvertHelper.hexStringToByteArray("35b5fc77f1137d69a0b2cb64e4dffb24");
      final List<Long> jobInstances = cleaner.jobKeyToJobInstanceIds(keyspace, jobKey);
      System.out.println("Nombre de JobInstanceId : " + jobInstances.size());
      for (final Long jobInstanceId : jobInstances) {
         System.out.println("JobInstanceId : " + jobInstanceId);
      }
   }

   @Test
   public void findJobInstanceByParameterTest() throws Exception {
      final SAEJobCleaner cleaner = new SAEJobCleaner();
      cleaner.findJobInstanceByParameter(keyspace, "20190302/3172/sommaire.xml");
   }

   @Test
   public void findBigExecutionContextsTest() throws Exception {
      final SAEJobCleaner cleaner = new SAEJobCleaner();
      cleaner.findBigExecutionContexts(keyspace);
   }

   @Test
   public void deleteOneJobInstanceTest() throws Exception {
      final SAEJobCleaner cleaner = new SAEJobCleaner();
      final boolean simulationMode = true;
      cleaner.deleteOneJobInstance(keyspace, 5086, simulationMode);
   }

   @Test
   public void purgeOldJobsTest() throws Exception {
      final SAEJobCleaner cleaner = new SAEJobCleaner();
      final boolean simulationMode = true;
      cleaner.purgeOldJobs(keyspace, 50, simulationMode);
   }

}
