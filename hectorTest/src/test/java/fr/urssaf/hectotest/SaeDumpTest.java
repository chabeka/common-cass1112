package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.util.HashMap;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Dump de données Cassandra du Keyspace SAE
 */
public class SaeDumpTest
{

	Keyspace keyspace;
	Cluster cluster;
	PrintStream sysout;
	Dumper dumper;
	
	@SuppressWarnings("serial")
   @Before  
	public void init() throws Exception {
		ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
		ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
		ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
		HashMap<String,String> credentials = new HashMap<String, String>() {{ put("username", "root");}{ put("password", "regina4932");}};
		String servers;
		//servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp31saecas1.cer31.recouv:9160";
		//servers = "hwi54saecas1.cve.recouv:9160";	// CNH
		//servers = "cer69imageint9.cer69.recouv:9160";
		//servers = "cer69imageint10.cer69.recouv:9160";
		//servers = "10.203.34.39:9160";		// Noufnouf
		//servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      //servers = "hwi69ginsaecas2.cer69.recouv:9160";
      //servers = "cer69-saeint3:9160";
		
		CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(servers);
		hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
		cluster = HFactory.getOrCreateCluster("SAE", hostConfigurator);
		keyspace = HFactory.createKeyspace("SAE", cluster, ccl, FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);
		
		sysout = new PrintStream(System.out, true, "UTF-8");

		// Pour dumper sur un fichier plutôt que sur la sortie standard
		//sysout = new PrintStream("c:/temp/out.txt");
		dumper = new Dumper(keyspace, sysout);
    }
    
	 
	
	@After
	public void close() {
		//cluster.getConnectionManager().shutdown();
		HFactory.shutdownCluster(cluster);
	}
	
	@Test
   public void testDumpParameters() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("Parameters", 50);
   }
	
	@Test
	public void testDumpDroitActionUnitaire() throws Exception {
		dumper.printKeyInHex = false;
		dumper.dumpCF("DroitActionUnitaire", 50);
	}
	
	@Test
   public void testDumpDroitContratService() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitContratService", 50);
   }
	
	@Test
   public void testDumpDroitPagm() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagm", 50);
   }
	
	@Test
   public void testDumpDroitPagma() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagma", 50);
   }
	
	@Test
   public void testDumpDroitPagmp() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPagmp", 50);
   }
	
	@Test
   public void testDumpDroitPrmd() throws Exception {
      dumper.printKeyInHex = false;
      dumper.dumpCF("DroitPrmd", 50);
   }
		
}
