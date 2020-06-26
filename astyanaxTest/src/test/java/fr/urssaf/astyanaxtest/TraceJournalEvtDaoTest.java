package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.util.UUID;

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

import fr.urssaf.astyanaxtest.dao.sae.TraceJournalEvtDao;

public class TraceJournalEvtDaoTest {
   /**
    * Représente le keyspace cassandra sur lequel on travaille
    */
   Keyspace keyspace;

   /**
    * La où on veut dumper
    */
   PrintStream sysout;

   @Before
   public void init() throws Exception {
      String servers;
      // servers =
      // "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      // servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp69saecas4:9160, cnp69saecas5:9160, cnp69saecas6:9160, cnp69saecas7:9160, cnp69saecas8:9160, cnp69saecas9:9160, cnp69saecas10:9160, cnp69saecas11:9160, cnp69saecas12:9160, cnp69saecas13:9160, cnp69saecas14:9160, cnp69saecas15:9160, cnp69saecas16:9160, cnp69saecas17:9160, cnp69saecas18:9160, cnp69saecas19:9160, cnp69saecas20:9160, cnp69saecas21:9160, cnp69saecas22:9160, cnp69saecas23:9160, cnp69saecas24:9160";
      // servers = "cnp69gntcas1:9160, cnp69gntcas2:9160, cnp69gntcas3:9160";

      // GNT Intégration client
      servers = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";

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
      // servers = "cnp69givngntcas1:9160, cnp69givngntcas1:9160, cnp69givngntcas1:9160";

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
                  .setMaxConnsPerHost(2)
                  .setSeeds(servers)
                  .setAuthenticationCredentials(credentials))
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());
      context.start();
      keyspace = context.getClient();

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      // sysout = new PrintStream("d:/temp/out.txt");
      sysout = System.out;

   }

   @Test
   public void testUpdateInfos() throws Exception {
      final TraceJournalEvtDao dao = new TraceJournalEvtDao(keyspace);
      final UUID traceId = UUID.fromString("a7d36900-ce8e-176a-b52b-005056b90926");
      final String infos = "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>saeServeurIP</string><string>10.207.81.92</string></entry><entry><string>saeServeurHostname</string><string>hwi31intgntv6boappli1</string></entry><entry><string>idDoc</string><string>52cc5a62-3efe-44cb-b7c1-6de7bbf138ab</string></entry></map>";
      dao.updateInfos(traceId, infos);
   }

}
