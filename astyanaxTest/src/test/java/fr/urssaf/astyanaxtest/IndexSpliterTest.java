package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.mapping.MappingCache;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.astyanaxtest.dao.BasesReferenceDao;
import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDatetimeDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeStringDao;
import fr.urssaf.astyanaxtest.spliter.IndexSpliter;

public class IndexSpliterTest {

	/**
	 * Représente le keyspace cassandra sur lequel on travaille
	 */
	Keyspace keyspace;

	/**
	 * Facilite le mapping cassandra<->entité
	 */
	MappingUtil mapper;

	/**
	 * La où on veut dumper
	 */
	PrintStream sysout;

	@Before
	public void init() throws Exception {
		String servers;
		// servers =
		// "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
		// //GIVN
		servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
		// // Production
		// servers = "hwi54saecas1.cve.recouv:9160"; // CNH
		// servers = "cer69imageint9.cer69.recouv:9160";
		// servers = "cer69imageint10.cer69.recouv:9160";
		// servers = "10.203.34.39:9160"; // Noufnouf
		// servers =
		// "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
		// servers = "hwi69ginsaecas2.cer69.recouv:9160";
		// servers = "cer69-saeint3:9160";
		//servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; // Préprod
		//servers = "cnp6saecvecas1.cve.recouv:9160,cnp3saecvecas1.cve.recouv:9160,cnp7saecvecas1.cve.recouv:9160";	// Charge
		//servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
		
		AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
				"root", "regina4932");

		AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
				.forCluster("Docubase")
				.forKeyspace("Docubase")
				.withAstyanaxConfiguration(
						new AstyanaxConfigurationImpl()
								//.setDiscoveryType(NodeDiscoveryType.NONE)
								.setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
								.setDefaultReadConsistencyLevel(ConsistencyLevel.CL_QUORUM)
								.setDefaultWriteConsistencyLevel(ConsistencyLevel.CL_QUORUM)
								.setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE)
								)
				.withConnectionPoolConfiguration(
						new ConnectionPoolConfigurationImpl("MyConnectionPool")
								.setPort(9160).setMaxConnsPerHost(36)
								.setSeeds(servers)
								.setAuthenticationCredentials(credentials))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
				.buildKeyspace(ThriftFamilyFactory.getInstance());

		context.start();
		keyspace = context.getClient();
		mapper = new MappingUtil(keyspace, new MappingCache());

		// Pour dumper sur un fichier plutôt que sur la sortie standard
		sysout = new PrintStream("d:/temp/out.txt");

	}
	
	@Test
	public void testVerifySrt() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		spliter.verifyIndex("srt", "20151116222151439");
	}
	
	@Test
	public void testVerifySrtBuilding() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "BUILDING");
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		//spliter.verifyIndex("srt", "2058");
		spliter.verifyIndex("srt", "2058", "73179109545284736770675729543511796315", keyspace.getPartitioner().getMaxToken());
	}
	
	@Test
	public void testRepairSrtBuilding() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "BUILDING");
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		//spliter.splitIndex("srt", "2058");
		spliter.splitIndex("srt", "2058", "73442196815408752076015959885187265421", keyspace.getPartitioner().getMaxToken(), "c:\\temp\\repairSrt.log");
	}
	
	@Test
	public void testRepairSrt() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		//spliter.splitIndex("srt", "2058");
		spliter.splitIndex("srt", "2058", "73442196815408752076015959885187265421", "73449999999999999999999999999999999999", "c:\\temp\\repairSrt.log");
	}
	
	@Test
	public void testVerifyNci() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "nci", "NOMINAL");
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, "nci", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		spliter.verifyIndex("nci", "2058");
	}
	
	@Test
	public void testVerifyNce() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "nce", "NOMINAL");
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, "nce", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		spliter.verifyIndex("nce", "2058");
	}
	
	@Test
	public void testRepairNce() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "nce", "NOMINAL");
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, "nce", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		spliter.splitIndex("nce", "2058", "c:\\temp\\repairNce.log");
	}

	@Test
	public void testVerifyArchivageDate() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		//UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-GIVN");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "SM_ARCHIVAGE_DATE", "NOMINAL");
		TermInfoRangeDatetimeDao termInfoRangeDao = new TermInfoRangeDatetimeDao(keyspace, "SM_ARCHIVAGE_DATE", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		spliter.verifyIndex("SM_ARCHIVAGE_DATE", "2058");
	}

	@Test
	public void testVerifyCreationDate() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		//UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-GIVN");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", "NOMINAL");
		TermInfoRangeDatetimeDao termInfoRangeDao = new TermInfoRangeDatetimeDao(keyspace, "SM_CREATION_DATE", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		spliter.verifyIndex("SM_CREATION_DATE", "2058");
	}
	
	@Test
	public void testGetMaxToken() throws Exception {
		System.out.println(keyspace.getPartitioner().getMinToken() + " - " + keyspace.getPartitioner().getMaxToken());
	}
	
	@Test
	public void splitIndex_multithread() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "nce", "BUILDING");
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, "nce", baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		int blocCount = 36;
		spliter.splitIndex_multithread("nce", "2058", keyspace.getPartitioner().getMinToken(), keyspace.getPartitioner().getMaxToken(), blocCount, "c:\\temp\\splitNce.log");
		//spliter.splitIndex_multithread("nce", "2058", "73442196815408752076015959885187265421", "73499999999999999999999999999999999999", blocCount, "c:\\temp\\splitNce.log");
	}
}
