package fr.urssaf.astyanaxtest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.mapping.MappingCache;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.serializers.UUIDSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.astyanaxtest.dao.BasesReferenceCF;
import fr.urssaf.astyanaxtest.dao.BasesReferenceDao;
import fr.urssaf.astyanaxtest.dao.BasesReferenceEntity;
import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.dao.IndexReferenceCF;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDao;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class IndexReferenceTest {
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
		//servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160"; //GIVN
		//servers = "cnp69gntcas1:9160, cnp69gntcas2:9160, cnp69gntcas3:9160";
		servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
		// servers = "cer69imageint9.cer69.recouv:9160";
		// servers = "cer69imageint10.cer69.recouv:9160";
		// servers = "10.203.34.39:9160"; // Noufnouf
		// servers =
		// "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
		// servers = "hwi69ginsaecas2.cer69.recouv:9160";
		// servers = "cer69-saeint3:9160";
		//servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; // Préprod
		//servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160";	// Charge
		//servers = "cnp3gntcvecas1.cve.recouv:9160,cnp6gntcvecas1.cve.recouv:9160,cnp7gntcvecas1.cve.recouv:9160";	// Charge GNT
		
		AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
				"root", "regina4932");

		AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
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
								.setPort(9160).setMaxConnsPerHost(1)
								.setSeeds(servers)
								.setAuthenticationCredentials(credentials))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
				.buildKeyspace(ThriftFamilyFactory.getInstance());

		context.start();
		keyspace = context.getEntity();
		mapper = new MappingUtil(keyspace, new MappingCache());

		// Pour dumper sur un fichier plutôt que sur la sortie standard
		//sysout = new PrintStream("d:/temp/out.txt");
		sysout= System.out;

	}


	@Test
	public void readTest() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		
		Assert.assertEquals(14, ref.metaToRangeId("426"));
		Assert.assertEquals(14, ref.metaToRangeId("42469972600016"));
		Assert.assertEquals(1, ref.metaToRangeId(""));
		Assert.assertEquals(1, ref.metaToRangeId("007"));
		Assert.assertEquals(41, ref.metaToRangeId("804"));
		Assert.assertEquals(42, ref.metaToRangeId("820"));
	}

	@Test
	public void readTestSM_CREATION_DATE() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", "NOMINAL");
		//ref.readIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", new String[]{"NOMINAL", "BUILDING"});
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			String state = ref.getEntityById(id).getSTATE();
			sysout.println("Id : " + id + " - state : " + state);
			
		}
	}

	@Test
	public void reWriteIndexReference_SM_CREATION_DATE() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String meta = "SM_CREATION_DATE";
		ref.readIndexReference(keyspace, baseUUID, meta , new String[]{"NOMINAL", "BUILDING"});
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			String state = ref.getEntityById(id).getSTATE();
			sysout.println("Id : " + id + " - state : " + state);
			
		}
		ref.reWriteIndexReference(keyspace, baseUUID, meta);
	}

	@Test
	public void reWriteIndexReference_SM_ARCHIVAGE_DATE() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String meta = "SM_ARCHIVAGE_DATE";
		ref.readIndexReference(keyspace, baseUUID, meta , new String[]{"NOMINAL", "BUILDING"});
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			String state = ref.getEntityById(id).getSTATE();
			sysout.println("Id : " + id + " - state : " + state);
			
		}
		ref.reWriteIndexReference(keyspace, baseUUID, meta);
	}

	
	@Test
	public void readBroken() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		//ref.readBrokenIndexReference(keyspace, baseUUID, "SM_CREATION_DATE");
		//ref.readBrokenIndexReference(keyspace, baseUUID, "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&");
		//ref.readBrokenIndexReference(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE");
		//ref.readBrokenIndexReference(keyspace, baseUUID, "SM_MODIFICATION_DATE");
		//ref.readBrokenIndexReference(keyspace, baseUUID, "srn");
		//ref.readBrokenIndexReference(keyspace, baseUUID, "SM_ARCHIVAGE_DATE");
		//ref.readBrokenIndexReference(keyspace, baseUUID, "srt");
		ref.readBrokenIndexReference(keyspace, baseUUID, "cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&");
	}
	
	
	@Test
	public void readTestSM_LIFE_CYCLE_REFERENCE_DATE() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", "NOMINAL");
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			sysout.println("Id : " + id);
		}
	}


	@Test
	public void readTestSiret() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			sysout.println("Id : " + id);
		}
	}

	@Test
	public void readTestComposite() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String meta = "cot&apr&atr&ame&SM_ARCHIVAGE_DATE&";
		byte[] key = IndexReference.getKey(baseUUID, meta);
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		ref.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			sysout.println("Id : " + id);
		}
	}

	@Test
	public void readTestComposite2() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String meta = "cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
		byte[] key = IndexReference.getKey(baseUUID, meta);
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		ref.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			sysout.println("Id : " + id);
		}
	}
	
	
	@Test
	public void readTestSiren() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "srn", "NOMINAL");
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			sysout.println("Id : " + id);
		}
	}

	@Test
	public void readTestSM_ARCHIVAGE_DATE() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "SM_ARCHIVAGE_DATE", "NOMINAL");
		//ref.readIndexReference(keyspace, baseUUID, "SM_ARCHIVAGE_DATE", new String[]{"NOMINAL", "BUILDING"});
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			sysout.println("Id : " + id);
		}
	}

	@Test
	public void readTestSM_MODIFICATION_DATE() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "SM_MODIFICATION_DATE", "NOMINAL");
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			sysout.println("Id : " + id);
		}
	}

	@Test
	public void readTestNce() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "nce", "NOMINAL");
		//Assert.assertEquals(37, ref.metaToRangeId("937000002004270161"));
		int[] ids = ref.getRangeIds();
		for (int id : ids) {
			sysout.println("Id : " + id);
		}
	}
	
	@Test
	public void readTestNceBuilding() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "nce", "BUILDING");
	}
	
	@Test
	public void readAllTest() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		new IndexReference().readIndexReference(keyspace, baseUUID, "SM_ARCHIVAGE_DATE", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "nci", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "den", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "npe", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", "NOMINAL");
		//new IndexReference().readIndexReference(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", new String[]{"NOMINAL", "BUILDING"});
		new IndexReference().readIndexReference(keyspace, baseUUID, "cot&apr&atr&ame&SM_ARCHIVAGE_DATE&", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "SM_MODIFICATION_DATE", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "nce", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "srn", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "rum", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		//new IndexReference().readIndexReference(keyspace, baseUUID, "iti", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "SM_UUID", "NOMINAL");
		new IndexReference().readIndexReference(keyspace, baseUUID, "nne", "NOMINAL");
	}

	
	@Test
	public void finishSplittingNceTest() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "nce", "BUILDING");
		ref.writeIndexReference(keyspace, baseUUID, "nce", 1000000);
	}
	
	@Test
	public void updateStateTest() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String meta = "SM_LIFE_CYCLE_REFERENCE_DATE";
		ref.readIndexReference(keyspace, baseUUID, meta, new String[]{"NOMINAL", "SPLITTING"});
		ref.updateState(keyspace, baseUUID, meta, 6, "NOMINAL");
		ref.updateState(keyspace, baseUUID, meta, 7, "NOMINAL");
		ref.updateState(keyspace, baseUUID, meta, 8, "NOMINAL");
	}
	
	@Test
	public void updateState2Test() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String meta = "SM_LIFE_CYCLE_REFERENCE_DATE";
		ref.readIndexReference(keyspace, baseUUID, meta, new String[]{"NOMINAL", "BUILDING"});
		ref.updateState(keyspace, baseUUID, meta, 97, "NOMINAL");
	}
	
	@Test
	public void cancelSplitTest() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		//String meta = "cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
		String meta = "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
		ref.readIndexReference(keyspace, baseUUID, meta, new String[]{"NOMINAL", "SPLITTING"});
		ref.cancelSplit(keyspace, baseUUID, meta);
	}
	
	
	@Test
	public void finishSplitting_SM_MODIFICATION_DATETest() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "SM_MODIFICATION_DATE", "BUILDING");
		ref.writeIndexReference(keyspace, baseUUID, "SM_MODIFICATION_DATE", 1000000);
	}

	@Test
	public void cleanTest() throws Exception {
		byte[] key = ConvertHelper.getBytesFromReadableUTF8String("indexName\\xef\\xbf\\xbff573ae93-ac6a-4615-a23b-150fd621b5a0");
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).delete();
		batch.execute();
	}

	@Test
	public void cleanTest2() throws Exception {
		byte[] key = ConvertHelper.getBytesFromReadableUTF8String("SM_CREATION_DATE\\xef\\xbf\\xbff573ae93-ac6a-4615-a23b-150fd621b5a0");
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.150.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.150.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.204.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.204.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.201.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.201.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.275.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.275.value");
		batch.execute();
	}

	@Test
	public void cleanTest3() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-GIVN");
		byte[] key = IndexReference.getKey(baseUUID, "SM_CREATION_DATE");
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.0.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.0.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.1.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.1.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.2.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.2.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.3.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.3.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.4.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.4.value");		
		batch.execute();
		IndexReference ref = new IndexReference();
		ref.resetRanges();
		ref.writeIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", 30000000);
	}

	@Test
	public void cleanTest4() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "srt");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.key", 1, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.value", "{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"0134449q01\",\"COUNT\":173052,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.key", 2, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.value", "{\"ID\":2,\"LOWER_BOUND\":\"0134449q01\",\"UPPER_BOUND\":\"0180424n01\",\"COUNT\":143865,\"STATE\":\"NOMINAL\"}", null);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(302);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();

		/*
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.0.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.0.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.1.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.1.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.2.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.2.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.3.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.3.value");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.4.key");
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexes.4.value");		
		batch.execute();
		IndexReference ref = new IndexReference();
		ref.resetRanges();
		ref.writeIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", 30000000);
		*/
	}
	
	@Test
	public void cleanTest5() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "cot&apr&atr&ame&SM_ARCHIVAGE_DATE&");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(3);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}
	
	@Test
	public void cleanTest6() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "srn");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.key", 1, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.value", "{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"210800116\",\"COUNT\":204175,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.key", 2, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.value", "{\"ID\":2,\"LOWER_BOUND\":\"210800116\",\"UPPER_BOUND\":\"219100419\",\"COUNT\":210698,\"STATE\":\"NOMINAL\"}", null);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(202);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest7() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "SM_ARCHIVAGE_DATE");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.key", 1, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.value", "{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"20120126235111669\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.key", 2, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.value", "{\"ID\":2,\"LOWER_BOUND\":\"20120126235111669\",\"UPPER_BOUND\":\"20120313142159784\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.2.key", 3, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.2.value", "{\"ID\":3,\"LOWER_BOUND\":\"20120313142159784\",\"UPPER_BOUND\":\"20120817073731973\",\"COUNT\":771729,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.3.key", 4, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.3.value", "{\"ID\":4,\"LOWER_BOUND\":\"20120817073731973\",\"UPPER_BOUND\":\"20130115014252651\",\"COUNT\":768048,\"STATE\":\"NOMINAL\"}", null);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(99);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest8() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(3);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest9() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "SM_MODIFICATION_DATE");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.key", 2, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.value", "{\"ID\":2,\"LOWER_BOUND\":\"20150504185118619\",\"UPPER_BOUND\":\"20150507090424461\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.2.key", 3, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.2.value", "{\"ID\":3,\"LOWER_BOUND\":\"20150507090424461\",\"UPPER_BOUND\":\"20150507141010583\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.3.key", 4, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.3.value", "{\"ID\":4,\"LOWER_BOUND\":\"20150507141010583\",\"UPPER_BOUND\":\"20150507202737900\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.4.key", 5, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.4.value", "{\"ID\":5,\"LOWER_BOUND\":\"20150507202737900\",\"UPPER_BOUND\":\"20150508124343998\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.5.key", 6, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.5.value", "{\"ID\":6,\"LOWER_BOUND\":\"20150508124343998\",\"UPPER_BOUND\":\"20150510124851896\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.6.key", 7, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.6.value", "{\"ID\":7,\"LOWER_BOUND\":\"20150510124851896\",\"UPPER_BOUND\":\"20150510175328396\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.7.key", 8, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.7.value", "{\"ID\":8,\"LOWER_BOUND\":\"20150510175328396\",\"UPPER_BOUND\":\"20150513083016536\",\"COUNT\":777355,\"STATE\":\"NOMINAL\"}", null);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(90);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest10() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "SM_CREATION_DATE");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		
		String rangesAsString = 
				"{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"20120126000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":2,\"LOWER_BOUND\":\"20120126000000000\",\"UPPER_BOUND\":\"20120306000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":3,\"LOWER_BOUND\":\"20120306000000000\",\"UPPER_BOUND\":\"20120731000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":4,\"LOWER_BOUND\":\"20120731000000000\",\"UPPER_BOUND\":\"20130114000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":5,\"LOWER_BOUND\":\"20130114000000000\",\"UPPER_BOUND\":\"20130118000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":6,\"LOWER_BOUND\":\"20130118000000000\",\"UPPER_BOUND\":\"20130209000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":7,\"LOWER_BOUND\":\"20130209000000000\",\"UPPER_BOUND\":\"20130409000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":8,\"LOWER_BOUND\":\"20130409000000000\",\"UPPER_BOUND\":\"20130808000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":9,\"LOWER_BOUND\":\"20130808000000000\",\"UPPER_BOUND\":\"20131004000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":10,\"LOWER_BOUND\":\"20131004000000000\",\"UPPER_BOUND\":\"20131016000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":11,\"LOWER_BOUND\":\"20131016000000000\",\"UPPER_BOUND\":\"20131203000000000\",\"COUNT\":1141046,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":12,\"LOWER_BOUND\":\"20131203000000000\",\"UPPER_BOUND\":\"20131214000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":13,\"LOWER_BOUND\":\"20131214000000000\",\"UPPER_BOUND\":\"20131216000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":14,\"LOWER_BOUND\":\"20131216000000000\",\"UPPER_BOUND\":\"20140117000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":15,\"LOWER_BOUND\":\"20140117000000000\",\"UPPER_BOUND\":\"20140123000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":16,\"LOWER_BOUND\":\"20140123000000000\",\"UPPER_BOUND\":\"20140202000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":17,\"LOWER_BOUND\":\"20140202000000000\",\"UPPER_BOUND\":\"20140228000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":18,\"LOWER_BOUND\":\"20140228000000000\",\"UPPER_BOUND\":\"20140328000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":19,\"LOWER_BOUND\":\"20140328000000000\",\"UPPER_BOUND\":\"20140404000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":20,\"LOWER_BOUND\":\"20140404000000000\",\"UPPER_BOUND\":\"20140410000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":21,\"LOWER_BOUND\":\"20140410000000000\",\"UPPER_BOUND\":\"20140414000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":22,\"LOWER_BOUND\":\"20140414000000000\",\"UPPER_BOUND\":\"20140416000000000\",\"COUNT\":1262039,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":23,\"LOWER_BOUND\":\"20140416000000000\",\"UPPER_BOUND\":\"20140418000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":24,\"LOWER_BOUND\":\"20140418000000000\",\"UPPER_BOUND\":\"20140429000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":25,\"LOWER_BOUND\":\"20140429000000000\",\"UPPER_BOUND\":\"20140523000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":26,\"LOWER_BOUND\":\"20140523000000000\",\"UPPER_BOUND\":\"20140717000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":27,\"LOWER_BOUND\":\"20140717000000000\",\"UPPER_BOUND\":\"20140908000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":28,\"LOWER_BOUND\":\"20140908000000000\",\"UPPER_BOUND\":\"20141007000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":29,\"LOWER_BOUND\":\"20141007000000000\",\"UPPER_BOUND\":\"20141021000000000\",\"COUNT\":1074885,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":30,\"LOWER_BOUND\":\"20141021000000000\",\"UPPER_BOUND\":\"20141127000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":31,\"LOWER_BOUND\":\"20141127000000000\",\"UPPER_BOUND\":\"20141211000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":32,\"LOWER_BOUND\":\"20141211000000000\",\"UPPER_BOUND\":\"20141212000000000\",\"COUNT\":737031,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":33,\"LOWER_BOUND\":\"20141212000000000\",\"UPPER_BOUND\":\"20150114000000000\",\"COUNT\":1300911,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":34,\"LOWER_BOUND\":\"20150114000000000\",\"UPPER_BOUND\":\"20150123000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":35,\"LOWER_BOUND\":\"20150123000000000\",\"UPPER_BOUND\":\"20150206000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":36,\"LOWER_BOUND\":\"20150206000000000\",\"UPPER_BOUND\":\"20150227000000000\",\"COUNT\":719490,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":37,\"LOWER_BOUND\":\"20150227000000000\",\"UPPER_BOUND\":\"20150304000000000\",\"COUNT\":736658,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":38,\"LOWER_BOUND\":\"20150304000000000\",\"UPPER_BOUND\":\"20150307000000000\",\"COUNT\":852816,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":39,\"LOWER_BOUND\":\"20150307000000000\",\"UPPER_BOUND\":\"20150310000000000\",\"COUNT\":448066,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":40,\"LOWER_BOUND\":\"20150310000000000\",\"UPPER_BOUND\":\"20150316000000000\",\"COUNT\":725424,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":41,\"LOWER_BOUND\":\"20150316000000000\",\"UPPER_BOUND\":\"20150319000000000\",\"COUNT\":1168550,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":42,\"LOWER_BOUND\":\"20150319000000000\",\"UPPER_BOUND\":\"20150327000000000\",\"COUNT\":777590,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":43,\"LOWER_BOUND\":\"20150327000000000\",\"UPPER_BOUND\":\"20150408000000000\",\"COUNT\":767446,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":44,\"LOWER_BOUND\":\"20150408000000000\",\"UPPER_BOUND\":\"20150504000000000\",\"COUNT\":796209,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":45,\"LOWER_BOUND\":\"20150504000000000\",\"UPPER_BOUND\":\"20150519000000000\",\"COUNT\":728929,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":46,\"LOWER_BOUND\":\"20150519000000000\",\"UPPER_BOUND\":\"20150602000000000\",\"COUNT\":817727,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":47,\"LOWER_BOUND\":\"20150602000000000\",\"UPPER_BOUND\":\"20150615000000000\",\"COUNT\":783071,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":48,\"LOWER_BOUND\":\"20150615000000000\",\"UPPER_BOUND\":\"20150625000000000\",\"COUNT\":447419,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":49,\"LOWER_BOUND\":\"20150625000000000\",\"UPPER_BOUND\":\"20150720000000000\",\"COUNT\":1133629,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":50,\"LOWER_BOUND\":\"20150720000000000\",\"UPPER_BOUND\":\"20150820000000000\",\"COUNT\":800557,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":51,\"LOWER_BOUND\":\"20150820000000000\",\"UPPER_BOUND\":\"20150919000000000\",\"COUNT\":692836,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":52,\"LOWER_BOUND\":\"20150919000000000\",\"UPPER_BOUND\":\"20151002000000000\",\"COUNT\":909125,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":53,\"LOWER_BOUND\":\"20151002000000000\",\"UPPER_BOUND\":\"20151028000000000\",\"COUNT\":810023,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":54,\"LOWER_BOUND\":\"20151028000000000\",\"UPPER_BOUND\":\"20151130000000000\",\"COUNT\":881317,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":55,\"LOWER_BOUND\":\"20151130000000000\",\"UPPER_BOUND\":\"20151222000000000\",\"COUNT\":809140,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":56,\"LOWER_BOUND\":\"20151222000000000\",\"UPPER_BOUND\":\"20160108000000000\",\"COUNT\":548902,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":57,\"LOWER_BOUND\":\"20160108000000000\",\"UPPER_BOUND\":\"20160114000000000\",\"COUNT\":1068641,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":58,\"LOWER_BOUND\":\"20160114000000000\",\"UPPER_BOUND\":\"20160119000000000\",\"COUNT\":648294,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":59,\"LOWER_BOUND\":\"20160119000000000\",\"UPPER_BOUND\":\"20160205000000000\",\"COUNT\":1032048,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":60,\"LOWER_BOUND\":\"20160205000000000\",\"UPPER_BOUND\":\"20160304000000000\",\"COUNT\":831906,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":61,\"LOWER_BOUND\":\"20160304000000000\",\"UPPER_BOUND\":\"20160317000000000\",\"COUNT\":566168,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":62,\"LOWER_BOUND\":\"20160317000000000\",\"UPPER_BOUND\":\"20160321000000000\",\"COUNT\":955976,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":63,\"LOWER_BOUND\":\"20160321000000000\",\"UPPER_BOUND\":\"20160323000000000\",\"COUNT\":632706,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":64,\"LOWER_BOUND\":\"20160323000000000\",\"UPPER_BOUND\":\"20160325000000000\",\"COUNT\":762493,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":65,\"LOWER_BOUND\":\"20160325000000000\",\"UPPER_BOUND\":\"20160401000000000\",\"COUNT\":934963,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":66,\"LOWER_BOUND\":\"20160401000000000\",\"UPPER_BOUND\":\"20160405000000000\",\"COUNT\":458190,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":67,\"LOWER_BOUND\":\"20160405000000000\",\"UPPER_BOUND\":\"20160407000000000\",\"COUNT\":1062066,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":68,\"LOWER_BOUND\":\"20160407000000000\",\"UPPER_BOUND\":\"20160420000000000\",\"COUNT\":937676,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":69,\"LOWER_BOUND\":\"20160420000000000\",\"UPPER_BOUND\":\"20160506000000000\",\"COUNT\":852598,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":71,\"LOWER_BOUND\":\"20160506000000000\",\"UPPER_BOUND\":\"20160526000000000\",\"COUNT\":1053685,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":72,\"LOWER_BOUND\":\"20160526000000000\",\"UPPER_BOUND\":\"20160609000000000\",\"COUNT\":1039959,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":73,\"LOWER_BOUND\":\"20160609000000000\",\"UPPER_BOUND\":\"20160621000000000\",\"COUNT\":1353793,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":74,\"LOWER_BOUND\":\"20160621000000000\",\"UPPER_BOUND\":\"20160721000000000\",\"COUNT\":1159626,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":76,\"LOWER_BOUND\":\"20160721000000000\",\"UPPER_BOUND\":\"20160819000000000\",\"COUNT\":1065177,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":77,\"LOWER_BOUND\":\"20160819000000000\",\"UPPER_BOUND\":\"20160908000000000\",\"COUNT\":1022622,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":78,\"LOWER_BOUND\":\"20160908000000000\",\"UPPER_BOUND\":\"20160921000000000\",\"COUNT\":1247470,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":80,\"LOWER_BOUND\":\"20160921000000000\",\"UPPER_BOUND\":\"20161013000000000\",\"COUNT\":1056231,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":82,\"LOWER_BOUND\":\"20161013000000000\",\"UPPER_BOUND\":\"20161029000000000\",\"COUNT\":1038387,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":83,\"LOWER_BOUND\":\"20161029000000000\",\"UPPER_BOUND\":\"20161122000000000\",\"COUNT\":1257951,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":85,\"LOWER_BOUND\":\"20161122000000000\",\"UPPER_BOUND\":\"20161211000000000\",\"COUNT\":1023243,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":86,\"LOWER_BOUND\":\"20161211000000000\",\"UPPER_BOUND\":\"20161222000000000\",\"COUNT\":1139059,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":88,\"LOWER_BOUND\":\"20161222000000000\",\"UPPER_BOUND\":\"20170118000000000\",\"COUNT\":1047556,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":90,\"LOWER_BOUND\":\"20170118000000000\",\"UPPER_BOUND\":\"20170208000000000\",\"COUNT\":1012802,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":92,\"LOWER_BOUND\":\"20170208000000000\",\"UPPER_BOUND\":\"20170301000000000\",\"COUNT\":1027151,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":93,\"LOWER_BOUND\":\"20170301000000000\",\"UPPER_BOUND\":\"20170316000000000\",\"COUNT\":1021125,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":95,\"LOWER_BOUND\":\"20170316000000000\",\"UPPER_BOUND\":\"20170404000000000\",\"COUNT\":1023131,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":96,\"LOWER_BOUND\":\"20170404000000000\",\"UPPER_BOUND\":\"20170407000000000\",\"COUNT\":1124849,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":98,\"LOWER_BOUND\":\"20170407000000000\",\"UPPER_BOUND\":\"20170421000000000\",\"COUNT\":1074423,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":99,\"LOWER_BOUND\":\"20170421000000000\",\"UPPER_BOUND\":\"20170505000000000\",\"COUNT\":1096262,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":101,\"LOWER_BOUND\":\"20170505000000000\",\"UPPER_BOUND\":\"20170624000000000\",\"COUNT\":1345576,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":102,\"LOWER_BOUND\":\"20170624000000000\",\"UPPER_BOUND\":\"20170714000000000\",\"COUNT\":1050322,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":104,\"LOWER_BOUND\":\"20170714000000000\",\"UPPER_BOUND\":\"20170801000000000\",\"COUNT\":1047481,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":105,\"LOWER_BOUND\":\"20170801000000000\",\"UPPER_BOUND\":\"20170820000000000\",\"COUNT\":1013370,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":106,\"LOWER_BOUND\":\"20170820000000000\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":2891560,\"STATE\":\"NOMINAL\"}"; 
		
		Iterable<String> lines = Splitter.on("\r\n").split(rangesAsString);
		int index = 0;
		MutationBatch batch = keyspace.prepareMutationBatch();
		for(String line : lines) {
			System.out.println(index + " : " + line);
			int start = line.indexOf(':');
			int end = line.indexOf(',');			
			int id = Integer.parseInt(line.substring(start + 1, end));
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".key", id, null);
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".value", line, null);
			index++;
		}
		System.out.println("size=" + index);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(index);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest11() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		
		String rangesAsString = 
				"{\"ID\":28,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"true&ur117&2.1.1.2.1&20161221023345774&\",\"COUNT\":1058901,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":29,\"LOWER_BOUND\":\"true&ur117&2.1.1.2.1&20161221023345774&\",\"UPPER_BOUND\":\"true&ur117&2.3.1.1.9&20170405142216446&\",\"COUNT\":1297528,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":30,\"LOWER_BOUND\":\"true&ur117&2.3.1.1.9&20170405142216446&\",\"UPPER_BOUND\":\"true&ur117&3.1.2.1.1&20161031232250411&\",\"COUNT\":47989,\"STATE\":\"NOMINAL\"}\r\n" +
				"{\"ID\":16,\"LOWER_BOUND\":\"true&ur117&3.1.2.1.1&20161031232250411&\",\"UPPER_BOUND\":\"true&ur117&3.d.x.x.x&20161110095307748&\",\"COUNT\":1140367,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":17,\"LOWER_BOUND\":\"true&ur117&3.d.x.x.x&20161110095307748&\",\"UPPER_BOUND\":\"true&ur227&8.4.3.2.1&20170411201154372&\",\"COUNT\":1211493,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":18,\"LOWER_BOUND\":\"true&ur227&8.4.3.2.1&20170411201154372&\",\"UPPER_BOUND\":\"true&ur237&2.1.4.5.1&20161117223417846&\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":10,\"LOWER_BOUND\":\"true&ur237&2.1.4.5.1&20161117223417846&\",\"UPPER_BOUND\":\"true&ur237&3.1.2.2.1&20161110205448702&\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":19,\"LOWER_BOUND\":\"true&ur237&3.1.2.2.1&20161110205448702&\",\"UPPER_BOUND\":\"true&ur267&3.3.1.1.2&20161208211654534&\",\"COUNT\":1244703,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":20,\"LOWER_BOUND\":\"true&ur267&3.3.1.1.2&20161208211654534&\",\"UPPER_BOUND\":\"true&ur427&1.2.2.4.12&20170102110046936&\",\"COUNT\":1226224,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":21,\"LOWER_BOUND\":\"true&ur427&1.2.2.4.12&20170102110046936&\",\"UPPER_BOUND\":\"true&ur427&2.1.1.2.1&20161221062911760&\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":22,\"LOWER_BOUND\":\"true&ur427&2.1.1.2.1&20161221062911760&\",\"UPPER_BOUND\":\"true&ur527&3.1.2.3.1&20170111214325490&\",\"COUNT\":1292394,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":23,\"LOWER_BOUND\":\"true&ur527&3.1.2.3.1&20170111214325490&\",\"UPPER_BOUND\":\"true&ur595&3.3.1.1.2&20161024175545729&\",\"COUNT\":1253891,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":24,\"LOWER_BOUND\":\"true&ur595&3.3.1.1.2&20161024175545729&\",\"UPPER_BOUND\":\"true&ur727&1.2.2.4.13&20161205125229269&\",\"COUNT\":105525,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":13,\"LOWER_BOUND\":\"true&ur727&1.2.2.4.13&20161205125229269&\",\"UPPER_BOUND\":\"true&ur747&1.2.2.4.12&20170108081607984&\",\"COUNT\":1343422,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":14,\"LOWER_BOUND\":\"true&ur747&1.2.2.4.12&20170108081607984&\",\"UPPER_BOUND\":\"true&ur827&3.1.2.1.2&20161108023253792&\",\"COUNT\":1338786,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":25,\"LOWER_BOUND\":\"true&ur827&3.1.2.1.2&20161108023253792&\",\"UPPER_BOUND\":\"true&ur917&2.1.1.2.1&20150616171013773&\",\"COUNT\":1153319,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":26,\"LOWER_BOUND\":\"true&ur917&2.1.1.2.1&20150616171013773&\",\"UPPER_BOUND\":\"true&ur937&2.1.1.2.1&20161024070934374&\",\"COUNT\":1152498,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":27,\"LOWER_BOUND\":\"true&ur937&2.1.1.2.1&20161024070934374&\",\"UPPER_BOUND\":\"true&ur937&2.1.1.2.1&20161221094259133&\",\"COUNT\":44616,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":7,\"LOWER_BOUND\":\"true&ur937&2.1.1.2.1&20161221094259133&\",\"UPPER_BOUND\":\"true&ur937&2.1.3.2.8&20161026191407020&\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":31,\"LOWER_BOUND\":\"true&ur937&2.1.3.2.8&20161026191407020&\",\"UPPER_BOUND\":\"true&ur937&3.3.1.1.2&20170722002342227&\",\"COUNT\":1154552,\"STATE\":\"NOMINAL\"}\r\n" +
				"{\"ID\":32,\"LOWER_BOUND\":\"true&ur937&3.3.1.1.2&20170722002342227&\",\"UPPER_BOUND\":\"true&ur974&3.1.2.2.1&20170427002751413&\",\"COUNT\":1193750,\"STATE\":\"NOMINAL\"}\r\n" +
				"{\"ID\":33,\"LOWER_BOUND\":\"true&ur974&3.1.2.2.1&20170427002751413&\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":150769,\"STATE\":\"NOMINAL\"}"; 
		Iterable<String> lines = Splitter.on("\r\n").split(rangesAsString);
		int index = 0;
		MutationBatch batch = keyspace.prepareMutationBatch();
		for(String line : lines) {
			System.out.println(index + " : " + line);
			int start = line.indexOf(':');
			int end = line.indexOf(',');
			int id = Integer.parseInt(line.substring(start + 1, end));
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".key", id, null);
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".value", line, null);
			index++;
		}
		System.out.println("size=" + index);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(index);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest12() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		
		String rangesAsString = 
				"{\"ID\":2,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"20120611000000000\",\"COUNT\":1122675,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":3,\"LOWER_BOUND\":\"20120611000000000\",\"UPPER_BOUND\":\"20130308000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":6,\"LOWER_BOUND\":\"20130308000000000\",\"UPPER_BOUND\":\"20130802000000000\",\"COUNT\":1033336,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":7,\"LOWER_BOUND\":\"20130802000000000\",\"UPPER_BOUND\":\"20131007221006998\",\"COUNT\":1001431,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":8,\"LOWER_BOUND\":\"20131007221006998\",\"UPPER_BOUND\":\"20131110044847402\",\"COUNT\":1005108,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":9,\"LOWER_BOUND\":\"20131110044847402\",\"UPPER_BOUND\":\"20131214061041535\",\"COUNT\":1003182,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":10,\"LOWER_BOUND\":\"20131214061041535\",\"UPPER_BOUND\":\"20131217050706881\",\"COUNT\":1004284,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":11,\"LOWER_BOUND\":\"20131217050706881\",\"UPPER_BOUND\":\"20140201000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":13,\"LOWER_BOUND\":\"20140201000000000\",\"UPPER_BOUND\":\"20140227215150339\",\"COUNT\":1019329,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":14,\"LOWER_BOUND\":\"20140227215150339\",\"UPPER_BOUND\":\"20140401000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":15,\"LOWER_BOUND\":\"20140401000000000\",\"UPPER_BOUND\":\"20140409000000000\",\"COUNT\":1051164,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":16,\"LOWER_BOUND\":\"20140409000000000\",\"UPPER_BOUND\":\"20140415000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":17,\"LOWER_BOUND\":\"20140415000000000\",\"UPPER_BOUND\":\"20140417000000000\",\"COUNT\":1148009,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":18,\"LOWER_BOUND\":\"20140417000000000\",\"UPPER_BOUND\":\"20140423000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":19,\"LOWER_BOUND\":\"20140423000000000\",\"UPPER_BOUND\":\"20140520020303772\",\"COUNT\":1273192,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":20,\"LOWER_BOUND\":\"20140520020303772\",\"UPPER_BOUND\":\"20140722000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":21,\"LOWER_BOUND\":\"20140722000000000\",\"UPPER_BOUND\":\"20140930000000000\",\"COUNT\":1006748,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":22,\"LOWER_BOUND\":\"20140930000000000\",\"UPPER_BOUND\":\"20141018123618472\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":23,\"LOWER_BOUND\":\"20141018123618472\",\"UPPER_BOUND\":\"20141201000000000\",\"COUNT\":1002219,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":24,\"LOWER_BOUND\":\"20141201000000000\",\"UPPER_BOUND\":\"20141212063756457\",\"COUNT\":1012834,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":25,\"LOWER_BOUND\":\"20141212063756457\",\"UPPER_BOUND\":\"20150107000000000\",\"COUNT\":1004841,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":26,\"LOWER_BOUND\":\"20150107000000000\",\"UPPER_BOUND\":\"20150122000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":27,\"LOWER_BOUND\":\"20150122000000000\",\"UPPER_BOUND\":\"20150211012635836\",\"COUNT\":1061305,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":28,\"LOWER_BOUND\":\"20150211012635836\",\"UPPER_BOUND\":\"20150303000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":29,\"LOWER_BOUND\":\"20150303000000000\",\"UPPER_BOUND\":\"20150306000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":30,\"LOWER_BOUND\":\"20150306000000000\",\"UPPER_BOUND\":\"20150311000000000\",\"COUNT\":1097800,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":31,\"LOWER_BOUND\":\"20150311000000000\",\"UPPER_BOUND\":\"20150317211234547\",\"COUNT\":1112527,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":32,\"LOWER_BOUND\":\"20150317211234547\",\"UPPER_BOUND\":\"20150324201148739\",\"COUNT\":1008482,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":33,\"LOWER_BOUND\":\"20150324201148739\",\"UPPER_BOUND\":\"20150409042353396\",\"COUNT\":1012347,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":34,\"LOWER_BOUND\":\"20150409042353396\",\"UPPER_BOUND\":\"20150511000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":35,\"LOWER_BOUND\":\"20150511000000000\",\"UPPER_BOUND\":\"20150527200326392\",\"COUNT\":1010281,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":36,\"LOWER_BOUND\":\"20150527200326392\",\"UPPER_BOUND\":\"20150612160254874\",\"COUNT\":1002521,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":37,\"LOWER_BOUND\":\"20150612160254874\",\"UPPER_BOUND\":\"20150626043139378\",\"COUNT\":1007030,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":38,\"LOWER_BOUND\":\"20150626043139378\",\"UPPER_BOUND\":\"20150727000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":39,\"LOWER_BOUND\":\"20150727000000000\",\"UPPER_BOUND\":\"20150910000000000\",\"COUNT\":1009461,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":40,\"LOWER_BOUND\":\"20150910000000000\",\"UPPER_BOUND\":\"20150928000000000\",\"COUNT\":1007843,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":41,\"LOWER_BOUND\":\"20150928000000000\",\"UPPER_BOUND\":\"20151112214443095\",\"COUNT\":1019283,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":42,\"LOWER_BOUND\":\"20151112214443095\",\"UPPER_BOUND\":\"20151208145619296\",\"COUNT\":1006988,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":43,\"LOWER_BOUND\":\"20151208145619296\",\"UPPER_BOUND\":\"20160108210624406\",\"COUNT\":1003473,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":44,\"LOWER_BOUND\":\"20160108210624406\",\"UPPER_BOUND\":\"20160114000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":45,\"LOWER_BOUND\":\"20160114000000000\",\"UPPER_BOUND\":\"20160121000000000\",\"COUNT\":1042224,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":46,\"LOWER_BOUND\":\"20160121000000000\",\"UPPER_BOUND\":\"20160224000000000\",\"COUNT\":1051062,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":47,\"LOWER_BOUND\":\"20160224000000000\",\"UPPER_BOUND\":\"20160317213953625\",\"COUNT\":1013640,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":48,\"LOWER_BOUND\":\"20160317213953625\",\"UPPER_BOUND\":\"20160322000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":49,\"LOWER_BOUND\":\"20160322000000000\",\"UPPER_BOUND\":\"20160324000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":50,\"LOWER_BOUND\":\"20160324000000000\",\"UPPER_BOUND\":\"20160331000000000\",\"COUNT\":1100846,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":51,\"LOWER_BOUND\":\"20160331000000000\",\"UPPER_BOUND\":\"20160406025748098\",\"COUNT\":1248743,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":52,\"LOWER_BOUND\":\"20160406025748098\",\"UPPER_BOUND\":\"20160413103714947\",\"COUNT\":1017627,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":53,\"LOWER_BOUND\":\"20160413103714947\",\"UPPER_BOUND\":\"20160502000000000\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":55,\"LOWER_BOUND\":\"20160502000000000\",\"UPPER_BOUND\":\"20160521043545662\",\"COUNT\":1000060,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":56,\"LOWER_BOUND\":\"20160521043545662\",\"UPPER_BOUND\":\"20160608102342125\",\"COUNT\":1000086,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":57,\"LOWER_BOUND\":\"20160608102342125\",\"UPPER_BOUND\":\"20160620222317627\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":58,\"LOWER_BOUND\":\"20160620222317627\",\"UPPER_BOUND\":\"20160704014412888\",\"COUNT\":1008544,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":59,\"LOWER_BOUND\":\"20160704014412888\",\"UPPER_BOUND\":\"20160728000719830\",\"COUNT\":1004306,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":61,\"LOWER_BOUND\":\"20160728000719830\",\"UPPER_BOUND\":\"20160822223418550\",\"COUNT\":1000110,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":62,\"LOWER_BOUND\":\"20160822223418550\",\"UPPER_BOUND\":\"20160915000015574\",\"COUNT\":1004165,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":63,\"LOWER_BOUND\":\"20160915000015574\",\"UPPER_BOUND\":\"20160922004139704\",\"COUNT\":1000880,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":65,\"LOWER_BOUND\":\"20160922004139704\",\"UPPER_BOUND\":\"20161013175905677\",\"COUNT\":1000183,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":66,\"LOWER_BOUND\":\"20161013175905677\",\"UPPER_BOUND\":\"20161029000018903\",\"COUNT\":1001799,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":68,\"LOWER_BOUND\":\"20161029000018903\",\"UPPER_BOUND\":\"20161122013134884\",\"COUNT\":1000222,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":69,\"LOWER_BOUND\":\"20161122013134884\",\"UPPER_BOUND\":\"20161207000032611\",\"COUNT\":1006654,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":71,\"LOWER_BOUND\":\"20161207000032611\",\"UPPER_BOUND\":\"20161220214309844\",\"COUNT\":1000539,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":72,\"LOWER_BOUND\":\"20161220214309844\",\"UPPER_BOUND\":\"20161228135222823\",\"COUNT\":1000558,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":74,\"LOWER_BOUND\":\"20161228135222823\",\"UPPER_BOUND\":\"20170110203645014\",\"COUNT\":1000109,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":75,\"LOWER_BOUND\":\"20170110203645014\",\"UPPER_BOUND\":\"20170201000651617\",\"COUNT\":1013820,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":77,\"LOWER_BOUND\":\"20170201000651617\",\"UPPER_BOUND\":\"20170220003410648\",\"COUNT\":1020167,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":79,\"LOWER_BOUND\":\"20170220003410648\",\"UPPER_BOUND\":\"20170308222534013\",\"COUNT\":1000281,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":80,\"LOWER_BOUND\":\"20170308222534013\",\"UPPER_BOUND\":\"20170325033850545\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":82,\"LOWER_BOUND\":\"20170325033850545\",\"UPPER_BOUND\":\"20170405083625350\",\"COUNT\":1000662,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":83,\"LOWER_BOUND\":\"20170405083625350\",\"UPPER_BOUND\":\"20170412000054798\",\"COUNT\":1108628,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":85,\"LOWER_BOUND\":\"20170412000054798\",\"UPPER_BOUND\":\"20170427000043741\",\"COUNT\":1025853,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":86,\"LOWER_BOUND\":\"20170427000043741\",\"UPPER_BOUND\":\"20170510234941026\",\"COUNT\":1000958,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":88,\"LOWER_BOUND\":\"20170510234941026\",\"UPPER_BOUND\":\"20170522071839755\",\"COUNT\":1000027,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":89,\"LOWER_BOUND\":\"20170522071839755\",\"UPPER_BOUND\":\"20170601152155104\",\"COUNT\":1000032,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":90,\"LOWER_BOUND\":\"20170601152155104\",\"UPPER_BOUND\":\"20170614083102301\",\"COUNT\":1000082,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":91,\"LOWER_BOUND\":\"20170614083102301\",\"UPPER_BOUND\":\"20170624012747615\",\"COUNT\":1000151,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":92,\"LOWER_BOUND\":\"20170624012747615\",\"UPPER_BOUND\":\"20170706000004337\",\"COUNT\":1023932,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":94,\"LOWER_BOUND\":\"20170706000004337\",\"UPPER_BOUND\":\"20170725000103419\",\"COUNT\":1005320,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":95,\"LOWER_BOUND\":\"20170725000103419\",\"UPPER_BOUND\":\"20170811115630823\",\"COUNT\":1001266,\"STATE\":\"NOMINAL\"}\r\n" + 
				"{\"ID\":96,\"LOWER_BOUND\":\"20170811115630823\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":3092816,\"STATE\":\"NOMINAL\"}"; 
		
		Iterable<String> lines = Splitter.on("\r\n").split(rangesAsString);
		int index = 0;
		MutationBatch batch = keyspace.prepareMutationBatch();
		for(String line : lines) {
			System.out.println(index + " : " + line);
			int start = line.indexOf(':');
			int end = line.indexOf(',');
			int id = Integer.parseInt(line.substring(start + 1, end));
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".key", id, null);
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".value", line, null);
			index++;
		}
		System.out.println("size=" + index);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(index);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest13() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "nce");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.key", 1, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.value", "{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"117000001502317873\",\"COUNT\":304110,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.key", 2, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.value", "{\"ID\":2,\"LOWER_BOUND\":\"117000001502317873\",\"UPPER_BOUND\":\"117000001503694197\",\"COUNT\":241081,\"STATE\":\"NOMINAL\"}", null);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(300);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest14() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "SM_CREATION_DATE");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		
		String rangesAsString = "{\"ID\":1,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"20120618220000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":2,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20120618220000000\",\"UPPER_BOUND\":\"20121205230000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":3,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20121205230000000\",\"UPPER_BOUND\":\"20130524000000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":4,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20130524000000000\",\"UPPER_BOUND\":\"20131110000000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":5,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20131110000000000\",\"UPPER_BOUND\":\"20140430000000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":6,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20140430000000000\",\"UPPER_BOUND\":\"20141018000000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":7,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20141018000000000\",\"UPPER_BOUND\":\"20150407000000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":8,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20150407000000000\",\"UPPER_BOUND\":\"20150924000000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":9,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20150924000000000\",\"UPPER_BOUND\":\"20160313000000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":10,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20160313000000000\",\"UPPER_BOUND\":\"20160830000000000\",\"COUNT\":987881}\r\n" + 
				"{\"ID\":11,\"STATE\":\"NOMINAL\",\"LOWER_BOUND\":\"20160830000000000\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":987881}";
		
		Iterable<String> lines = Splitter.on("\r\n").split(rangesAsString);
		int index = 0;
		MutationBatch batch = keyspace.prepareMutationBatch();
		for(String line : lines) {
			System.out.println(index + " : " + line);
			int start = line.indexOf(':');
			int end = line.indexOf(',');
			int id = Integer.parseInt(line.substring(start + 1, end));
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".key", id, null);
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".value", line, null);
			index++;
		}
		System.out.println("size=" + index);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(index);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest15() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "nci");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.key", 1, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.value", "{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"0018146\",\"COUNT\":337503,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.key", 2, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.1.value", "{\"ID\":2,\"LOWER_BOUND\":\"0018146\",\"UPPER_BOUND\":\"0034534\",\"COUNT\":297184,\"STATE\":\"NOMINAL\"}", null);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(150);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest16() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "nne");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.key", 1, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.value", "{\"ID\":1,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"1840574256071\",\"COUNT\":1000074,\"STATE\":\"NOMINAL\"}", null);
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(3);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest17() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "SM_CREATION_DATE");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.104.value", "{\"ID\":127,\"LOWER_BOUND\":\"20171221000000000\",\"UPPER_BOUND\":\"20171228000000000\",\"COUNT\":1000000,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.112.value", "{\"ID\":128,\"LOWER_BOUND\":\"20171228000000000\",\"UPPER_BOUND\":\"20180105000000000\",\"COUNT\":1000000,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.113.value", "{\"ID\":128,\"LOWER_BOUND\":\"20171228000000000\",\"UPPER_BOUND\":\"20180105000000000\",\"COUNT\":1000000,\"STATE\":\"NOMINAL\"}", null);
		batch.execute();
	}

	@Test
	public void cleanTest18() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "SM_ARCHIVAGE_DATE");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.10.value", "{\"ID\":11,\"LOWER_BOUND\":\"20161013071647339\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":3439585,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.6.value", "{\"ID\":7,\"LOWER_BOUND\":\"20161012015241149\",\"UPPER_BOUND\":\"20161012034540950\",\"COUNT\":1990861,\"STATE\":\"NOMINAL\"}", null);
		batch.execute();
	}

	@Test
	public void cleanTest19() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "SM_ARCHIVAGE_DATE");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(304);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}

	@Test
	public void cleanTest20() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		byte[] key = IndexReference.getKey(baseUUID, "srt");
		System.out.println(ConvertHelper.getReadableUTF8String(key));
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.key", 0, null);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.value", "{\"ID\":0,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":83432,\"STATE\":\"NOMINAL\"}", null);
		batch.withRow(IndexReferenceCF.get(), key).deleteColumn("rangeIndexNb");
		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(1);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		batch.execute();
	}


	
	@Test
	public void writeSizeTest() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.writeSize(keyspace, baseUUID, "nce", 401);
	}
	
	@Test
	public void writeSize_SM_MODIFICATION_DATE_Test() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.writeSize(keyspace, baseUUID, "SM_MODIFICATION_DATE", 301);
	}
	
	@Test
	public void writeRangeKey_cspp() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference.writeRangeKey(keyspace, baseUUID, "SM_MODIFICATION_DATE", 0, 1);
		IndexReference.writeRangeKey(keyspace, baseUUID, "den", 0, 1);
		IndexReference.writeRangeKey(keyspace, baseUUID, "SM_CREATION_DATE", 0, 1);
		
		IndexReference.writeRangeKey(keyspace, baseUUID, "den", 1, 2);
		IndexReference.writeRangeKey(keyspace, baseUUID, "SM_CREATION_DATE", 1, 2);
		
		IndexReference.writeRangeKey(keyspace, baseUUID, "SM_CREATION_DATE", 2, 3);
		IndexReference.writeRangeKey(keyspace, baseUUID, "SM_CREATION_DATE", 3, 4);
		IndexReference.writeRangeKey(keyspace, baseUUID, "SM_CREATION_DATE", 4, 5);
	}
	
	
	@Test
	public void updateCountInRangesTest() throws Exception {
		String indexName = "SM_ARCHIVAGE_DATE";
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, indexName, "NOMINAL");
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		String rangesAsString = "Range 0 : 0\r\n" + 
				"Range 1 : 843289\r\n" + 
				"Range 2 : 844126\r\n" + 
				"Range 3 : 816451\r\n" + 
				"Range 4 : 831394\r\n" + 
				"Range 5 : 849917\r\n" + 
				"Range 6 : 828382\r\n" + 
				"Range 7 : 824150\r\n" + 
				"Range 8 : 834653\r\n" + 
				"Range 9 : 832005\r\n" + 
				"Range 10 : 833257\r\n" + 
				"Range 11 : 817564\r\n" + 
				"Range 12 : 841929\r\n" + 
				"Range 13 : 825434\r\n" + 
				"Range 14 : 830946\r\n" + 
				"Range 15 : 830861\r\n" + 
				"Range 16 : 829146\r\n" + 
				"Range 17 : 838008\r\n" + 
				"Range 18 : 836362\r\n" + 
				"Range 19 : 831373\r\n" + 
				"Range 20 : 827202\r\n" + 
				"Range 21 : 823278\r\n" + 
				"Range 22 : 829646\r\n" + 
				"Range 23 : 839541\r\n" + 
				"Range 24 : 827253\r\n" + 
				"Range 25 : 841224\r\n" + 
				"Range 26 : 820529\r\n" + 
				"Range 27 : 854870\r\n" + 
				"Range 28 : 832268\r\n" + 
				"Range 29 : 840657\r\n" + 
				"Range 30 : 842366\r\n" + 
				"Range 31 : 844415\r\n" + 
				"Range 32 : 840084\r\n" + 
				"Range 33 : 840343\r\n" + 
				"Range 34 : 845615\r\n" + 
				"Range 35 : 826303\r\n" + 
				"Range 36 : 824502\r\n" + 
				"Range 37 : 829520\r\n" + 
				"Range 38 : 832432\r\n" + 
				"Range 39 : 818656\r\n" + 
				"Range 40 : 823106\r\n" + 
				"Range 41 : 841747\r\n" + 
				"Range 42 : 837396\r\n" + 
				"Range 43 : 835102\r\n" + 
				"Range 44 : 848160\r\n" + 
				"Range 45 : 840879\r\n" + 
				"Range 46 : 837293\r\n" + 
				"Range 47 : 840379\r\n" + 
				"Range 48 : 838113\r\n" + 
				"Range 49 : 847280\r\n" + 
				"Range 50 : 824787\r\n" + 
				"Range 51 : 836336\r\n" + 
				"Range 52 : 833554\r\n" + 
				"Range 53 : 813736\r\n" + 
				"Range 54 : 834549\r\n" + 
				"Range 55 : 834332\r\n" + 
				"Range 56 : 821789\r\n" + 
				"Range 57 : 829026\r\n" + 
				"Range 58 : 832389\r\n" + 
				"Range 59 : 818640\r\n" + 
				"Range 60 : 822304\r\n" + 
				"Range 61 : 832265\r\n" + 
				"Range 62 : 829094\r\n" + 
				"Range 63 : 838582\r\n" + 
				"Range 64 : 842027\r\n" + 
				"Range 65 : 840951\r\n" + 
				"Range 66 : 840513\r\n" + 
				"Range 67 : 829471\r\n" + 
				"Range 68 : 838769\r\n" + 
				"Range 69 : 829026\r\n" + 
				"Range 70 : 830977\r\n" + 
				"Range 71 : 834196\r\n" + 
				"Range 72 : 842500\r\n" + 
				"Range 73 : 826449\r\n" + 
				"Range 74 : 840699\r\n" + 
				"Range 75 : 835414\r\n" + 
				"Range 76 : 831251\r\n" + 
				"Range 77 : 844636\r\n" + 
				"Range 78 : 818790\r\n" + 
				"Range 79 : 840968\r\n" + 
				"Range 80 : 835262\r\n" + 
				"Range 81 : 822486\r\n" + 
				"Range 82 : 831249\r\n" + 
				"Range 83 : 818832\r\n" + 
				"Range 84 : 854101\r\n" + 
				"Range 85 : 823048\r\n" + 
				"Range 86 : 826622\r\n" + 
				"Range 87 : 824610\r\n" + 
				"Range 88 : 844351\r\n" + 
				"Range 89 : 830185\r\n" + 
				"Range 90 : 848819\r\n" + 
				"Range 91 : 825575\r\n" + 
				"Range 92 : 829605\r\n" + 
				"Range 93 : 827112\r\n" + 
				"Range 94 : 835990\r\n" + 
				"Range 95 : 821435\r\n" + 
				"Range 96 : 837842\r\n" + 
				"Range 97 : 839224\r\n" + 
				"Range 98 : 824301\r\n" + 
				"Range 99 : 836542\r\n" + 
				"Range 100 : 853504\r\n" + 
				"Range 101 : 835213\r\n" + 
				"Range 102 : 833570\r\n" + 
				"Range 103 : 829569\r\n" + 
				"Range 104 : 838893\r\n" + 
				"Range 105 : 847334\r\n" + 
				"Range 106 : 826389\r\n" + 
				"Range 107 : 840535\r\n" + 
				"Range 108 : 831688\r\n" + 
				"Range 109 : 829967\r\n" + 
				"Range 110 : 838807\r\n" + 
				"Range 111 : 824512\r\n" + 
				"Range 112 : 811327\r\n" + 
				"Range 113 : 832613\r\n" + 
				"Range 114 : 840028\r\n" + 
				"Range 115 : 825190\r\n" + 
				"Range 116 : 809515\r\n" + 
				"Range 117 : 819322\r\n" + 
				"Range 118 : 852840\r\n" + 
				"Range 119 : 831459\r\n" + 
				"Range 120 : 831961\r\n" + 
				"Range 121 : 833063\r\n" + 
				"Range 122 : 831283\r\n" + 
				"Range 123 : 815867\r\n" + 
				"Range 124 : 822498\r\n" + 
				"Range 125 : 822982\r\n" + 
				"Range 126 : 832147\r\n" + 
				"Range 127 : 857710\r\n" + 
				"Range 128 : 842171\r\n" + 
				"Range 129 : 826087\r\n" + 
				"Range 130 : 829056\r\n" + 
				"Range 131 : 837840\r\n" + 
				"Range 132 : 834334\r\n" + 
				"Range 133 : 848632\r\n" + 
				"Range 134 : 841418\r\n" + 
				"Range 135 : 838007\r\n" + 
				"Range 136 : 833473\r\n" + 
				"Range 137 : 834684\r\n" + 
				"Range 138 : 825234\r\n" + 
				"Range 139 : 834885\r\n" + 
				"Range 140 : 840211\r\n" + 
				"Range 141 : 845536\r\n" + 
				"Range 142 : 835788\r\n" + 
				"Range 143 : 845953\r\n" + 
				"Range 144 : 820676\r\n" + 
				"Range 145 : 834771\r\n" + 
				"Range 146 : 826559\r\n" + 
				"Range 147 : 838123\r\n" + 
				"Range 148 : 825280\r\n" + 
				"Range 149 : 830603\r\n" + 
				"Range 150 : 832717\r\n" + 
				"Range 151 : 846914\r\n" + 
				"Range 152 : 838130\r\n" + 
				"Range 153 : 840726\r\n" + 
				"Range 154 : 821066\r\n" + 
				"Range 155 : 834053\r\n" + 
				"Range 156 : 842555\r\n" + 
				"Range 157 : 838478\r\n" + 
				"Range 158 : 831737\r\n" + 
				"Range 159 : 834060\r\n" + 
				"Range 160 : 830307\r\n" + 
				"Range 161 : 837928\r\n" + 
				"Range 162 : 821215\r\n" + 
				"Range 163 : 833900\r\n" + 
				"Range 164 : 833080\r\n" + 
				"Range 165 : 827702\r\n" + 
				"Range 166 : 848474\r\n" + 
				"Range 167 : 848333\r\n" + 
				"Range 168 : 823088\r\n" + 
				"Range 169 : 841331\r\n" + 
				"Range 170 : 832116\r\n" + 
				"Range 171 : 837441\r\n" + 
				"Range 172 : 827330\r\n" + 
				"Range 173 : 841035\r\n" + 
				"Range 174 : 832397\r\n" + 
				"Range 175 : 827010\r\n" + 
				"Range 176 : 829431\r\n" + 
				"Range 177 : 851346\r\n" + 
				"Range 178 : 837725\r\n" + 
				"Range 179 : 839092\r\n" + 
				"Range 180 : 839420\r\n" + 
				"Range 181 : 828078\r\n" + 
				"Range 182 : 847389\r\n" + 
				"Range 183 : 824930\r\n" + 
				"Range 184 : 827541\r\n" + 
				"Range 185 : 843681\r\n" + 
				"Range 186 : 816393\r\n" + 
				"Range 187 : 842253\r\n" + 
				"Range 188 : 816742\r\n" + 
				"Range 189 : 834480\r\n" + 
				"Range 190 : 827630\r\n" + 
				"Range 191 : 831969\r\n" + 
				"Range 192 : 823207\r\n" + 
				"Range 193 : 835254\r\n" + 
				"Range 194 : 836060\r\n" + 
				"Range 195 : 829636\r\n" + 
				"Range 196 : 834402\r\n" + 
				"Range 197 : 841097\r\n" + 
				"Range 198 : 840714\r\n" + 
				"Range 199 : 838649\r\n" + 
				"Range 200 : 839916\r\n" + 
				"Range 201 : 866910\r\n" + 
				"Range 202 : 824238\r\n" + 
				"Range 203 : 831202\r\n" + 
				"Range 204 : 825206\r\n" + 
				"Range 205 : 827847\r\n" + 
				"Range 206 : 830175\r\n" + 
				"Range 207 : 844593\r\n" + 
				"Range 208 : 831970\r\n" + 
				"Range 209 : 839241\r\n" + 
				"Range 210 : 820000\r\n" + 
				"Range 211 : 830813\r\n" + 
				"Range 212 : 835373\r\n" + 
				"Range 213 : 824049\r\n" + 
				"Range 214 : 837116\r\n" + 
				"Range 215 : 821162\r\n" + 
				"Range 216 : 849980\r\n" + 
				"Range 217 : 840134\r\n" + 
				"Range 218 : 830992\r\n" + 
				"Range 219 : 844207\r\n" + 
				"Range 220 : 839843\r\n" + 
				"Range 221 : 835995\r\n" + 
				"Range 222 : 831351\r\n" + 
				"Range 223 : 823943\r\n" + 
				"Range 224 : 826491\r\n" + 
				"Range 225 : 833649\r\n" + 
				"Range 226 : 827447\r\n" + 
				"Range 227 : 838524\r\n" + 
				"Range 228 : 848900\r\n" + 
				"Range 229 : 836825\r\n" + 
				"Range 230 : 844061\r\n" + 
				"Range 231 : 839430\r\n" + 
				"Range 232 : 830331\r\n" + 
				"Range 233 : 833575\r\n" + 
				"Range 234 : 842123\r\n" + 
				"Range 235 : 834474\r\n" + 
				"Range 236 : 824417\r\n" + 
				"Range 237 : 825127\r\n" + 
				"Range 238 : 855058\r\n" + 
				"Range 239 : 828407\r\n" + 
				"Range 240 : 821785\r\n" + 
				"Range 241 : 842663\r\n" + 
				"Range 242 : 831286\r\n" + 
				"Range 243 : 831371\r\n" + 
				"Range 244 : 827578\r\n" + 
				"Range 245 : 826455\r\n" + 
				"Range 246 : 831291\r\n" + 
				"Range 247 : 834403\r\n" + 
				"Range 248 : 832443\r\n" + 
				"Range 249 : 833727\r\n" + 
				"Range 250 : 0\r\n" + 
				"Range 251 : 991271\r\n" + 
				"Range 252 : 1027778\r\n" + 
				"Range 253 : 890041\r\n" + 
				"Range 254 : 974019\r\n" + 
				"Range 255 : 975645\r\n" + 
				"Range 256 : 1014107\r\n" + 
				"Range 257 : 1014155\r\n" + 
				"Range 258 : 942452\r\n" + 
				"Range 259 : 935398\r\n" + 
				"Range 260 : 975063\r\n" + 
				"Range 261 : 999816\r\n" + 
				"Range 262 : 920176\r\n" + 
				"Range 263 : 992462\r\n" + 
				"Range 264 : 994694\r\n" + 
				"Range 265 : 954009\r\n" + 
				"Range 266 : 977548\r\n" + 
				"Range 267 : 957338\r\n" + 
				"Range 268 : 964031\r\n" + 
				"Range 269 : 997705\r\n" + 
				"Range 270 : 1021762\r\n" + 
				"Range 271 : 1019435\r\n" + 
				"Range 272 : 942565\r\n" + 
				"Range 273 : 947908\r\n" + 
				"Range 274 : 965105\r\n" + 
				"Range 275 : 903780\r\n" + 
				"Range 276 : 988935\r\n" + 
				"Range 277 : 959933\r\n" + 
				"Range 278 : 1002104\r\n" + 
				"Range 279 : 950174\r\n" + 
				"Range 280 : 903375\r\n" + 
				"Range 281 : 963547\r\n" + 
				"Range 282 : 934941\r\n" + 
				"Range 283 : 1023631\r\n" + 
				"Range 284 : 944447\r\n" + 
				"Range 285 : 1014597\r\n" + 
				"Range 286 : 973725\r\n" + 
				"Range 287 : 972254\r\n" + 
				"Range 288 : 935694\r\n" + 
				"Range 289 : 974529\r\n" + 
				"Range 290 : 957429\r\n" + 
				"Range 291 : 973239\r\n" + 
				"Range 292 : 903103\r\n" + 
				"Range 293 : 937446\r\n" + 
				"Range 294 : 1003391\r\n" + 
				"Range 295 : 975383\r\n" + 
				"Range 296 : 1001833\r\n" + 
				"Range 297 : 972613\r\n" + 
				"Range 298 : 957126\r\n" + 
				"Range 299 : 1009186\r\n" + 
				"Range 300 : 968426\r\n" + 
				"Range 301 : 969094\r\n" + 
				"Range 302 : 943379\r\n" + 
				"Range 303 : 932567\r\n" + 
				"Range 304 : 968653\r\n" + 
				"Range 305 : 995847\r\n" + 
				"Range 306 : 977430\r\n" + 
				"Range 307 : 927580\r\n" + 
				"Range 308 : 957378\r\n" + 
				"Range 309 : 957238\r\n" + 
				"Range 310 : 1063767\r\n" + 
				"Range 311 : 1035667\r\n" + 
				"Range 312 : 987087\r\n" + 
				"Range 313 : 949855\r\n" + 
				"Range 314 : 998208\r\n" + 
				"Range 315 : 957035\r\n" + 
				"Range 316 : 930549\r\n" + 
				"Range 317 : 930804\r\n" + 
				"Range 318 : 954669\r\n" + 
				"Range 319 : 1042753\r\n" + 
				"Range 320 : 994513\r\n" + 
				"Range 321 : 995342\r\n" + 
				"Range 322 : 963762\r\n" + 
				"Range 323 : 966773\r\n" + 
				"Range 324 : 984840\r\n" + 
				"Range 325 : 968841\r\n" + 
				"Range 326 : 1005424\r\n" + 
				"Range 327 : 931816\r\n" + 
				"Range 328 : 968362\r\n" + 
				"Range 329 : 978970\r\n" + 
				"Range 330 : 1002433\r\n" + 
				"Range 331 : 1010977\r\n" + 
				"Range 332 : 928673\r\n" + 
				"Range 333 : 982835\r\n" + 
				"Range 334 : 1023451\r\n" + 
				"Range 335 : 974948\r\n" + 
				"Range 336 : 925706\r\n" + 
				"Range 337 : 998528\r\n" + 
				"Range 338 : 1056888\r\n" + 
				"Range 339 : 963428\r\n" + 
				"Range 340 : 1052900\r\n" + 
				"Range 341 : 1003478\r\n" + 
				"Range 342 : 943444\r\n" + 
				"Range 343 : 993573\r\n" + 
				"Range 344 : 938909\r\n" + 
				"Range 345 : 977200\r\n" + 
				"Range 346 : 942477\r\n" + 
				"Range 347 : 950392\r\n" + 
				"Range 348 : 1001607\r\n" + 
				"Range 349 : 1020165\r\n" + 
				"Range 350 : 939988\r\n" + 
				"Range 351 : 974615\r\n" + 
				"Range 352 : 995957\r\n" + 
				"Range 353 : 941450\r\n" + 
				"Range 354 : 959770\r\n" + 
				"Range 355 : 993827";
		Iterable<String> ranges = Splitter.on("\r\n").split(rangesAsString);
		for(String range : ranges) {
			String[] elements = range.split(":");
			int rangeId = Integer.parseInt(elements[0].replace("Range", "").trim());
			int count = Integer.parseInt(elements[1].trim());
			if (count > 0) {
				System.out.println(rangeId + " - " + count);
				counts.put(rangeId, count);
			}
		}
		
		ref.updateCountInRanges(keyspace, baseUUID, indexName, counts);
	}

	@Test
	public void startSplitting_nce() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "nce", "NOMINAL");
		String rangesAsString = "[min_lower_bound TO 105225586052983999[|[105225586052983999 TO 110415414907038212[|[110415414907038212 TO 115315744560211896[|[115315744560211896 TO 117000001502041952[|[117000001502041952 TO 117000001503794369[|[117000001503794369 TO 117000001504507216[|[117000001504507216 TO 117000001505439799[|[117000001505439799 TO 117000001506017206[|[117000001506017206 TO 117000001506750723[|[117000001506750723 TO 117000001508076655[|[117000001508076655 TO 117000001508786899[|[117000001508786899 TO 117000001509244385[|[117000001509244385 TO 117000001512080008[|[117000001512080008 TO 117000001513328760[|[117000001513328760 TO 117000001514212294[|[117000001514212294 TO 117000001515026669[|[117000001515026669 TO 117000001515499783[|[117000001515499783 TO 117000001515890411[|[117000001515890411 TO 117000001517453580[|[117000001517453580 TO 117000001518337550[|[117000001518337550 TO 117000001518870493[|[117000001518870493 TO 117000001519279223[|[117000001519279223 TO 117000001523096472[|[117000001523096472 TO 117000001524601809[|[117000001524601809 TO 117000001525565763[|[117000001525565763 TO 117000001526068932[|[117000001526068932 TO 117000001527518372[|[117000001527518372 TO 117000001528423267[|[117000001528423267 TO 117000001528937696[|[117000001528937696 TO 117000001530258735[|[117000001530258735 TO 117000001533792334[|[117000001533792334 TO 117000001535412329[|[117000001535412329 TO 117000001536053312[|[117000001536053312 TO 117000001536656189[|[117000001536656189 TO 117000001537143195[|[117000001537143195 TO 117000001537770534[|[117000001537770534 TO 117000001543295351[|[117000001543295351 TO 117000001544821296[|[117000001544821296 TO 117000001545326816[|[117000001545326816 TO 117000001546169496[|[117000001546169496 TO 117000001547131107[|[117000001547131107 TO 117000001548073605[|[117000001548073605 TO 117000001548655088[|[117000001548655088 TO 117000001549112881[|[117000001549112881 TO 117442071624100208[|[117442071624100208 TO 122645232314243912[|[122645232314243912 TO 127666098764166236[|[127666098764166236 TO 132332334015518428[|[132332334015518428 TO 137352598225697872[|[137352598225697872 TO 142427154537290336[|[142427154537290336 TO 147387602133676408[|[147387602133676408 TO 152605766011402008[|[152605766011402008 TO 157695848960429432[|[157695848960429432 TO 162798221455886960[|[162798221455886960 TO 167941715149208904[|[167941715149208904 TO 173011500900611280[|[173011500900611280 TO 177857722435146576[|[177857722435146576 TO 183048905804753296[|[183048905804753296 TO 188133853161707520[|[188133853161707520 TO 193149103317409760[|[193149103317409760 TO 198433916317299008[|[198433916317299008 TO 200000010000848894[|[200000010000848894 TO 201021414017304784[|[201021414017304784 TO 206099068280309440[|[206099068280309440 TO 210915688704699280[|[210915688704699280 TO 215816665440797808[|[215816665440797808 TO 217000001101573443[|[217000001101573443 TO 217000001120010401[|[217000001120010401 TO 217000001130975015[|[217000001130975015 TO 219205328077077872[|[219205328077077872 TO 224105236120522016[|[224105236120522016 TO 227000000800852632[|[227000000800852632 TO 227000000810226967[|[227000000810226967 TO 227000000812059606[|[227000000812059606 TO 227000000820663753[|[227000000820663753 TO 227000000830124806[|[227000000830124806 TO 229820699151605360[|[229820699151605360 TO 234668057272210720[|[234668057272210720 TO 237000001900508119[|[237000001900508119 TO 237000001901636836[|[237000001901636836 TO 237000001902190684[|[237000001902190684 TO 237000001910891588[|[237000001910891588 TO 237000001912848867[|[237000001912848867 TO 240490077342838048[|[240490077342838048 TO 245693833986297248[|[245693833986297248 TO 247000001701061357[|[247000001701061357 TO 247000001702796969[|[247000001702796969 TO 247000001711197670[|[247000001711197670 TO 247000001721164744[|[247000001721164744 TO 247000001723453186[|[247000001723453186 TO 247000001731726896[|[247000001731726896 TO 247000001742316984[|[247000001742316984 TO 247000001752429867[|[247000001752429867 TO 249755249544978144[|[249755249544978144 TO 254726376943290240[|[254726376943290240 TO 257000000701391820[|[257000000701391820 TO 257000000704120879[|[257000000704120879 TO 257000000720224275[|[257000000720224275 TO 257000000730054954[|[257000000730054954 TO 259018595749512320[|[259018595749512320 TO 264139685034751904[|[264139685034751904 TO 267000001600841643[|[267000001600841643 TO 267000001602717437[|[267000001602717437 TO 267000001620322665[|[267000001620322665 TO 267000001622655880[|[267000001622655880 TO 267000001631436868[|[267000001631436868 TO 269887978304177536[|[269887978304177536 TO 274958163872361184[|[274958163872361184 TO 280200766026973728[|[280200766026973728 TO 285077055543661120[|[285077055543661120 TO 290014942828565824[|[290014942828565824 TO 295023798849433664[|[295023798849433664 TO 300164554174989472[|[300164554174989472 TO 305263302335515616[|[305263302335515616 TO 310241740010678784[|[310241740010678784 TO 315106756705790752[|[315106756705790752 TO 317000001001165687[|[317000001001165687 TO 317000001002970622[|[317000001002970622 TO 317000001004871505[|[317000001004871505 TO 317000001005725049[|[317000001005725049 TO 317000001010204386[|[317000001010204386 TO 317000001011862315[|[317000001011862315 TO 317000001013352182[|[317000001013352182 TO 317000001013927959[|[317000001013927959 TO 317000001020250294[|[317000001020250294 TO 317761052725836640[|[317761052725836640 TO 323074137466028320[|[323074137466028320 TO 328087526839226496[|[328087526839226496 TO 333037452632561312[|[333037452632561312 TO 338106412347406144[|[338106412347406144 TO 343196862423792480[|[343196862423792480 TO 348360728751868000[|[348360728751868000 TO 353562264610081920[|[353562264610081920 TO 358469750732183456[|[358469750732183456 TO 363643707614392032[|[363643707614392032 TO 368697689287364480[|[368697689287364480 TO 373640552069991840[|[373640552069991840 TO 378668308444321152[|[378668308444321152 TO 383518959395587456[|[383518959395587456 TO 388581248372793216[|[388581248372793216 TO 393359300727024704[|[393359300727024704 TO 398382612597197312[|[398382612597197312 TO 403523433301597824[|[403523433301597824 TO 408365017548203456[|[408365017548203456 TO 413434642367064960[|[413434642367064960 TO 417000000400296541[|[417000000400296541 TO 417000000402083384[|[417000000402083384 TO 417000000410350403[|[417000000410350403 TO 417000000421669726[|[417000000421669726 TO 417000000430413728[|[417000000430413728 TO 417000000440162519[|[417000000440162519 TO 419185694120824320[|[419185694120824320 TO 424049912858754368[|[424049912858754368 TO 427000000300389635[|[427000000300389635 TO 427000000302037661[|[427000000302037661 TO 427000000304688180[|[427000000304688180 TO 427000000311269834[|[427000000311269834 TO 427000000320030448[|[427000000320030448 TO 428587205521762368[|[428587205521762368 TO 433696501469239616[|[433696501469239616 TO 437000001800499194[|[437000001800499194 TO 437000001810456929[|[437000001810456929 TO 437000001830011654[|[437000001830011654 TO 437582065118476736[|[437582065118476736 TO 442294436041265728[|[442294436041265728 TO 447295038402080512[|[447295038402080512 TO 452406861912459136[|[452406861912459136 TO 457423140527680512[|[457423140527680512 TO 462559733819216512[|[462559733819216512 TO 467567332973703744[|[467567332973703744 TO 472435474023222912[|[472435474023222912 TO 477941019833087936[|[477941019833087936 TO 481336363265290880[|[481336363265290880 TO 486291337059810752[|[486291337059810752 TO 491394041059538752[|[491394041059538752 TO 496259214496240000[|[496259214496240000 TO 501224478334188480[|[501224478334188480 TO 506065922183915968[|[506065922183915968 TO 511020354926586176[|[511020354926586176 TO 515915050683543104[|[515915050683543104 TO 520905619068071232[|[520905619068071232 TO 526042961701750784[|[526042961701750784 TO 527000000201479989[|[527000000201479989 TO 527000000203255825[|[527000000203255825 TO 527000000204063855[|[527000000204063855 TO 527000000211385937[|[527000000211385937 TO 527000000220796413[|[527000000220796413 TO 527000000231919996[|[527000000231919996 TO 527000000241477290[|[527000000241477290 TO 527000000250104858[|[527000000250104858 TO 527000000250527546[|[527000000250527546 TO 527000000250962768[|[527000000250962768 TO 527834376832470272[|[527834376832470272 TO 533167463727295424[|[533167463727295424 TO 537000000500367268[|[537000000500367268 TO 537000000502523504[|[537000000502523504 TO 537000000503319902[|[537000000503319902 TO 537000000511336187[|[537000000511336187 TO 537000000520601688[|[537000000520601688 TO 537000000522539654[|[537000000522539654 TO 537000000530138051[|[537000000530138051 TO 537000000531875537[|[537000000531875537 TO 537000000540074721[|[537000000540074721 TO 537000000540602869[|[537000000540602869 TO 540529967797920128[|[540529967797920128 TO 545685573760420096[|[545685573760420096 TO 547000001301767110[|[547000001301767110 TO 547000001302663003[|[547000001302663003 TO 547000001320003539[|[547000001320003539 TO 547000001322144240[|[547000001322144240 TO 547000001340022555[|[547000001340022555 TO 547524731792509568[|[547524731792509568 TO 552387766586616640[|[552387766586616640 TO 557367900758981696[|[557367900758981696 TO 562336860178038464[|[562336860178038464 TO 567398021370172480[|[567398021370172480 TO 572292012209072704[|[572292012209072704 TO 577403132896870400[|[577403132896870400 TO 582478710124269120[|[582478710124269120 TO 587510812515392896[|[587510812515392896 TO 592663463018834560[|[592663463018834560 TO 597395096812397248[|[597395096812397248 TO 602269799215719104[|[602269799215719104 TO 607359206164255744[|[607359206164255744 TO 612176529411226496[|[612176529411226496 TO 617220692941918976[|[617220692941918976 TO 622269827779382464[|[622269827779382464 TO 627140956977382272[|[627140956977382272 TO 632272045873105536[|[632272045873105536 TO 637366735097020864[|[637366735097020864 TO 642429548781365184[|[642429548781365184 TO 647614161577075712[|[647614161577075712 TO 652708924561738944[|[652708924561738944 TO 657850003009662016[|[657850003009662016 TO 662821138789877312[|[662821138789877312 TO 667657037591561664[|[667657037591561664 TO 672595272725448000[|[672595272725448000 TO 677617161767557248[|[677617161767557248 TO 682394737610593408[|[682394737610593408 TO 687383306492120064[|[687383306492120064 TO 692159039992839040[|[692159039992839040 TO 696546365320682496[|[696546365320682496 TO 701940840436145664[|[701940840436145664 TO 707115144748240768[|[707115144748240768 TO 712004859140142848[|[712004859140142848 TO 717161011183634432[|[717161011183634432 TO 722213247744366464[|[722213247744366464 TO 727000000600041160[|[727000000600041160 TO 727000000602347276[|[727000000602347276 TO 727000000604137055[|[727000000604137055 TO 727000000605198197[|[727000000605198197 TO 727000000605735626[|[727000000605735626 TO 727000000610797355[|[727000000610797355 TO 727000000620887592[|[727000000620887592 TO 727000000622203392[|[727000000622203392 TO 727000000630294367[|[727000000630294367 TO 727000000640011611[|[727000000640011611 TO 727000000642257295[|[727000000642257295 TO 727000000650359599[|[727000000650359599 TO 727000000650894462[|[727000000650894462 TO 730469949590042240[|[730469949590042240 TO 735651074675843072[|[735651074675843072 TO 737000000101855717[|[737000000101855717 TO 737000000103723673[|[737000000103723673 TO 737000000104683967[|[737000000104683967 TO 737000000111128188[|[737000000111128188 TO 737000000130453385[|[737000000130453385 TO 737000000150479260[|[737000000150479260 TO 737000000161010054[|[737000000161010054 TO 737000000180036030[|[737000000180036030 TO 737000000180448151[|[737000000180448151 TO 737000000180841769[|[737000000180841769 TO 737000000181366261[|[737000000181366261 TO 740900631714612224[|[740900631714612224 TO 745865393895655808[|[745865393895655808 TO 747000000901827906[|[747000000901827906 TO 747000000911773496[|[747000000911773496 TO 748335918784141568[|[748335918784141568 TO 753157701203599616[|[753157701203599616 TO 758243026584386816[|[758243026584386816 TO 763079949654638720[|[763079949654638720 TO 768269192613661312[|[768269192613661312 TO 772602880885824512[|[772602880885824512 TO 777605769829824512[|[777605769829824512 TO 782102146651595776[|[782102146651595776 TO 787054664967581568[|[787054664967581568 TO 791986204637214592[|[791986204637214592 TO 797018335526809088[|[797018335526809088 TO 802246048068627712[|[802246048068627712 TO 807151974737644160[|[807151974737644160 TO 812235626671463296[|[812235626671463296 TO 817261965665966336[|[817261965665966336 TO 822271404229104512[|[822271404229104512 TO 827000002100014555[|[827000002100014555 TO 827000002100795427[|[827000002100795427 TO 827000002101645621[|[827000002101645621 TO 827000002102508273[|[827000002102508273 TO 827000002103067394[|[827000002103067394 TO 827000002103532587[|[827000002103532587 TO 827000002103910767[|[827000002103910767 TO 827000002110511558[|[827000002110511558 TO 827000002120344206[|[827000002120344206 TO 827000002122275564[|[827000002122275564 TO 827000002123597487[|[827000002123597487 TO 827000002124149494[|[827000002124149494 TO 827000002130491518[|[827000002130491518 TO 827000002132383911[|[827000002132383911 TO 827000002140516346[|[827000002140516346 TO 827000002142406728[|[827000002142406728 TO 827000002144007466[|[827000002144007466 TO 827000002150958404[|[827000002150958404 TO 827000002152890118[|[827000002152890118 TO 827000002161246591[|[827000002161246591 TO 827000002170011804[|[827000002170011804 TO 827000002172070741[|[827000002172070741 TO 827000002172693658[|[827000002172693658 TO 827000002180544526[|[827000002180544526 TO 829077242640778368[|[829077242640778368 TO 833831477398052864[|[833831477398052864 TO 837000000000458927[|[837000000000458927 TO 837000000002257939[|[837000000002257939 TO 837000000020242442[|[837000000020242442 TO 837000000040118036[|[837000000040118036 TO 837757509713992448[|[837757509713992448 TO 842674215510487552[|[842674215510487552 TO 847851880546659200[|[847851880546659200 TO 852707460476085504[|[852707460476085504 TO 857621263200417152[|[857621263200417152 TO 862597349332645504[|[862597349332645504 TO 867472783476114304[|[867472783476114304 TO 872586926678195584[|[872586926678195584 TO 877499957848340224[|[877499957848340224 TO 882506227632984576[|[882506227632984576 TO 887613369012251520[|[887613369012251520 TO 892545053269714176[|[892545053269714176 TO 897594872489571584[|[897594872489571584 TO 902690150961279872[|[902690150961279872 TO 907490515941753984[|[907490515941753984 TO 911851402651518592[|[911851402651518592 TO 916667953925207296[|[916667953925207296 TO 917000001201721224[|[917000001201721224 TO 917000001203252640[|[917000001203252640 TO 917000001203891793[|[917000001203891793 TO 917000001210526119[|[917000001210526119 TO 917000001220086567[|[917000001220086567 TO 917000001221657127[|[917000001221657127 TO 917000001222244057[|[917000001222244057 TO 917000001232006579[|[917000001232006579 TO 917000001240212777[|[917000001240212777 TO 917000001240668028[|[917000001240668028 TO 919519581319764224[|[919519581319764224 TO 923686824692413184[|[923686824692413184 TO 928584970021620352[|[928584970021620352 TO 930883872002001003[|[930883872002001003 TO 935686213523149440[|[935686213523149440 TO 937000002000607481[|[937000002000607481 TO 937000002001496736[|[937000002001496736 TO 937000002002514701[|[937000002002514701 TO 937000002003508777[|[937000002003508777 TO 937000002004045332[|[937000002004045332 TO 937000002004502548[|[937000002004502548 TO 937000002004878104[|[937000002004878104 TO 937000002010321115[|[937000002010321115 TO 937000002021305768[|[937000002021305768 TO 937000002023427131[|[937000002023427131 TO 937000002024542284[|[937000002024542284 TO 937000002025173006[|[937000002025173006 TO 937000002025682188[|[937000002025682188 TO 937000002030330302[|[937000002030330302 TO 937000002041047267[|[937000002041047267 TO 937000002043148634[|[937000002043148634 TO 937000002044162113[|[937000002044162113 TO 937000002044813384[|[937000002044813384 TO 937000002050292481[|[937000002050292481 TO 937000002051930766[|[937000002051930766 TO 937000002052417672[|[937000002052417672 TO 937000002060762214[|[937000002060762214 TO 940694814734160896[|[940694814734160896 TO 945627062255516672[|[945627062255516672 TO 950431382290001011[|[950431382290001011 TO 955044033238664320[|[955044033238664320 TO 960028525162488192[|[960028525162488192 TO 964926663786172928[|[964926663786172928 TO 967830013579001011[|[967830013579001011 TO 971000002845640362[|[971000002845640362 TO 971000009868757141[|[971000009868757141 TO 971990203235290320[|[971990203235290320 TO 972000000206274863[|[972000000206274863 TO 972030000300000791[|[972030000300000791 TO 973000004100002766[|[973000004100002766 TO 974000000000794446[|[974000000000794446 TO 974000000002069144[|[974000000002069144 TO 974000000002739910[|[974000000002739910 TO 974000000003172343[|[974000000003172343 TO 976000000031053622[|[976000000031053622 TO 980121530825272192[|[980121530825272192 TO 984966948116198144[|[984966948116198144 TO 990038173459470336[|[990038173459470336 TO 995048961509019136[|[995048961509019136 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "nce", 0, rangesAsString );
	}
	
	@Test
	public void startSplitting_SM_MODIFICATION_DATE() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "SM_MODIFICATION_DATE", "NOMINAL");
		String rangesAsString = "[min_lower_bound TO 20130103113223965[|[20130103113223965 TO 20130112013207698[|[20130112013207698 TO 20130205221442178[|[20130205221442178 TO 20130703115620621[|[20130703115620621 TO 20131008002342329[|[20131008002342329 TO 20131120103919380[|[20131120103919380 TO 20131214164239236[|[20131214164239236 TO 20131227155951963[|[20131227155951963 TO 20131227182236175[|[20131227182236175 TO 20131227203758995[|[20131227203758995 TO 20131227232138107[|[20131227232138107 TO 20131228042756003[|[20131228042756003 TO 20140120012858217[|[20140120012858217 TO 20140129024202655[|[20140129024202655 TO 20140219223247297[|[20140219223247297 TO 20140326165248762[|[20140326165248762 TO 20140404182811993[|[20140404182811993 TO 20140411032547381[|[20140411032547381 TO 20140415020303448[|[20140415020303448 TO 20140417041754303[|[20140417041754303 TO 20140423121529405[|[20140423121529405 TO 20140503101630334[|[20140503101630334 TO 20140618010928607[|[20140618010928607 TO 20140819213712657[|[20140819213712657 TO 20140829163946959[|[20140829163946959 TO 20140829195605608[|[20140829195605608 TO 20140829235917676[|[20140829235917676 TO 20140830033726714[|[20140830033726714 TO 20140830070837441[|[20140830070837441 TO 20140919072227233[|[20140919072227233 TO 20141008055715390[|[20141008055715390 TO 20141028202514748[|[20141028202514748 TO 20141126221432338[|[20141126221432338 TO 20141212013106358[|[20141212013106358 TO 20141212092409593[|[20141212092409593 TO 20150109203256631[|[20150109203256631 TO 20150212125806872[|[20150212125806872 TO 20150212160748059[|[20150212160748059 TO 20150213130832112[|[20150213130832112 TO 20150213193733175[|[20150213193733175 TO 20150214014750380[|[20150214014750380 TO 20150214090626266[|[20150214090626266 TO 20150216190800928[|[20150216190800928 TO 20150216224710072[|[20150216224710072 TO 20150217030149890[|[20150217030149890 TO 20150217055302403[|[20150217055302403 TO 20150217095653647[|[20150217095653647 TO 20150217124212661[|[20150217124212661 TO 20150217153400971[|[20150217153400971 TO 20150217182754960[|[20150217182754960 TO 20150218090639226[|[20150218090639226 TO 20150218130257849[|[20150218130257849 TO 20150218155511918[|[20150218155511918 TO 20150218185254868[|[20150218185254868 TO 20150218215209003[|[20150218215209003 TO 20150219021527060[|[20150219021527060 TO 20150219050951060[|[20150219050951060 TO 20150219080350022[|[20150219080350022 TO 20150219105126521[|[20150219105126521 TO 20150219133820115[|[20150219133820115 TO 20150219163457890[|[20150219163457890 TO 20150219192312245[|[20150219192312245 TO 20150219222935371[|[20150219222935371 TO 20150220093910007[|[20150220093910007 TO 20150220133946704[|[20150220133946704 TO 20150220170718164[|[20150220170718164 TO 20150220200416335[|[20150220200416335 TO 20150220230031507[|[20150220230031507 TO 20150221032902955[|[20150221032902955 TO 20150221064052494[|[20150221064052494 TO 20150221095843911[|[20150221095843911 TO 20150303134434756[|[20150303134434756 TO 20150304164141124[|[20150304164141124 TO 20150304192150593[|[20150304192150593 TO 20150306100720557[|[20150306100720557 TO 20150306124454507[|[20150306124454507 TO 20150306152738872[|[20150306152738872 TO 20150306180358960[|[20150306180358960 TO 20150306204224868[|[20150306204224868 TO 20150307011114068[|[20150307011114068 TO 20150307040314379[|[20150307040314379 TO 20150307065512003[|[20150307065512003 TO 20150307094031858[|[20150307094031858 TO 20150307122921468[|[20150307122921468 TO 20150307151725519[|[20150307151725519 TO 20150307175608165[|[20150307175608165 TO 20150307203450714[|[20150307203450714 TO 20150307234046825[|[20150307234046825 TO 20150308034321102[|[20150308034321102 TO 20150308063045024[|[20150308063045024 TO 20150308090722609[|[20150308090722609 TO 20150308120932515[|[20150308120932515 TO 20150308145150371[|[20150308145150371 TO 20150308173609842[|[20150308173609842 TO 20150308202315650[|[20150308202315650 TO 20150308233439622[|[20150308233439622 TO 20150309034449590[|[20150309034449590 TO 20150309062613642[|[20150309062613642 TO 20150309090815898[|[20150309090815898 TO 20150309115703888[|[20150309115703888 TO 20150309181414245[|[20150309181414245 TO 20150309205518949[|[20150309205518949 TO 20150309233957580[|[20150309233957580 TO 20150310040059854[|[20150310040059854 TO 20150310065309266[|[20150310065309266 TO 20150310094035174[|[20150310094035174 TO 20150310123031926[|[20150310123031926 TO 20150310151440283[|[20150310151440283 TO 20150310175457112[|[20150310175457112 TO 20150310204339992[|[20150310204339992 TO 20150310233441349[|[20150310233441349 TO 20150311043425876[|[20150311043425876 TO 20150311072426690[|[20150311072426690 TO 20150311141805510[|[20150311141805510 TO 20150311170458023[|[20150311170458023 TO 20150311194823952[|[20150311194823952 TO 20150311223312929[|[20150311223312929 TO 20150312024244212[|[20150312024244212 TO 20150312053050495[|[20150312053050495 TO 20150312082102525[|[20150312082102525 TO 20150312110946146[|[20150312110946146 TO 20150312135644071[|[20150312135644071 TO 20150312164157558[|[20150312164157558 TO 20150312195138045[|[20150312195138045 TO 20150312224601122[|[20150312224601122 TO 20150313024955227[|[20150313024955227 TO 20150313054020661[|[20150313054020661 TO 20150313084404242[|[20150313084404242 TO 20150313171557254[|[20150313171557254 TO 20150313200839264[|[20150313200839264 TO 20150313225756872[|[20150313225756872 TO 20150314034622076[|[20150314034622076 TO 20150314062319929[|[20150314062319929 TO 20150314090643463[|[20150314090643463 TO 20150314115238416[|[20150314115238416 TO 20150314144648459[|[20150314144648459 TO 20150314180518517[|[20150314180518517 TO 20150314211103876[|[20150314211103876 TO 20150315020921027[|[20150315020921027 TO 20150315054426819[|[20150315054426819 TO 20150315090019466[|[20150315090019466 TO 20150315121724010[|[20150315121724010 TO 20150315160308305[|[20150315160308305 TO 20150318100815913[|[20150318100815913 TO 20150318130635530[|[20150318130635530 TO 20150318160108024[|[20150318160108024 TO 20150318185836828[|[20150318185836828 TO 20150318215433745[|[20150318215433745 TO 20150319021621977[|[20150319021621977 TO 20150319051249042[|[20150319051249042 TO 20150319081324788[|[20150319081324788 TO 20150319111048130[|[20150319111048130 TO 20150319141037311[|[20150319141037311 TO 20150319170906463[|[20150319170906463 TO 20150320111219675[|[20150320111219675 TO 20150320140507648[|[20150320140507648 TO 20150320170453389[|[20150320170453389 TO 20150320200929011[|[20150320200929011 TO 20150320231006645[|[20150320231006645 TO 20150321032500741[|[20150321032500741 TO 20150321062830279[|[20150321062830279 TO 20150321092752639[|[20150321092752639 TO 20150321122658340[|[20150321122658340 TO 20150321152534130[|[20150321152534130 TO 20150321182931789[|[20150321182931789 TO 20150321213027145[|[20150321213027145 TO 20150322015901144[|[20150322015901144 TO 20150322050011435[|[20150322050011435 TO 20150322075406400[|[20150322075406400 TO 20150322105545909[|[20150322105545909 TO 20150322134807278[|[20150322134807278 TO 20150322163910590[|[20150322163910590 TO 20150322193614464[|[20150322193614464 TO 20150322223346919[|[20150322223346919 TO 20150323025502806[|[20150323025502806 TO 20150323061459084[|[20150323061459084 TO 20150323092608510[|[20150323092608510 TO 20150323122619554[|[20150323122619554 TO 20150323152450345[|[20150323152450345 TO 20150323182548652[|[20150323182548652 TO 20150323212524105[|[20150323212524105 TO 20150324014653487[|[20150324014653487 TO 20150324044654747[|[20150324044654747 TO 20150324075638223[|[20150324075638223 TO 20150325191116167[|[20150325191116167 TO 20150325223653307[|[20150325223653307 TO 20150326032603273[|[20150326032603273 TO 20150326065633722[|[20150326065633722 TO 20150327121001653[|[20150327121001653 TO 20150327142726315[|[20150327142726315 TO 20150327165304065[|[20150327165304065 TO 20150327191619644[|[20150327191619644 TO 20150327213942336[|[20150327213942336 TO 20150328012316678[|[20150328012316678 TO 20150328035036399[|[20150328035036399 TO 20150328062211200[|[20150328062211200 TO 20150328085735341[|[20150328085735341 TO 20150328113149042[|[20150328113149042 TO 20150328140139444[|[20150328140139444 TO 20150328163402239[|[20150328163402239 TO 20150328190938879[|[20150328190938879 TO 20150328213852229[|[20150328213852229 TO 20150329012844304[|[20150329012844304 TO 20150329035305816[|[20150329035305816 TO 20150329061717239[|[20150329061717239 TO 20150329084652534[|[20150329084652534 TO 20150329111108969[|[20150329111108969 TO 20150329133423499[|[20150329133423499 TO 20150330100146902[|[20150330100146902 TO 20150330125249134[|[20150330125249134 TO 20150330154547937[|[20150330154547937 TO 20150330182900399[|[20150330182900399 TO 20150330210351585[|[20150330210351585 TO 20150331005739693[|[20150331005739693 TO 20150331035159680[|[20150331035159680 TO 20150331065253251[|[20150331065253251 TO 20150331092654789[|[20150331092654789 TO 20150331115403496[|[20150331115403496 TO 20150331143037120[|[20150331143037120 TO 20150331170110625[|[20150331170110625 TO 20150331192648980[|[20150331192648980 TO 20150331220540767[|[20150331220540767 TO 20150401015152842[|[20150401015152842 TO 20150401044159992[|[20150401044159992 TO 20150401072352518[|[20150401072352518 TO 20150401100801380[|[20150401100801380 TO 20150401124353898[|[20150401124353898 TO 20150401151732034[|[20150401151732034 TO 20150401180113995[|[20150401180113995 TO 20150401203459850[|[20150401203459850 TO 20150402003550987[|[20150402003550987 TO 20150402032707216[|[20150402032707216 TO 20150402062250072[|[20150402062250072 TO 20150402085835957[|[20150402085835957 TO 20150402112355753[|[20150402112355753 TO 20150402135836503[|[20150402135836503 TO 20150402162458609[|[20150402162458609 TO 20150402184906964[|[20150402184906964 TO 20150402212343565[|[20150402212343565 TO 20150403011621819[|[20150403011621819 TO 20150403071553810[|[20150403071553810 TO 20150403094808721[|[20150403094808721 TO 20150403121248822[|[20150403121248822 TO 20150403144114330[|[20150403144114330 TO 20150403170613825[|[20150403170613825 TO 20150403193733805[|[20150403193733805 TO 20150403220525518[|[20150403220525518 TO 20150404020910165[|[20150404020910165 TO 20150404044824312[|[20150404044824312 TO 20150404073244634[|[20150404073244634 TO 20150404102502418[|[20150404102502418 TO 20150404132923184[|[20150404132923184 TO 20150404163528416[|[20150404163528416 TO 20150404193807009[|[20150404193807009 TO 20150404223941497[|[20150404223941497 TO 20150405090459461[|[20150405090459461 TO 20150405115232058[|[20150405115232058 TO 20150406160240503[|[20150406160240503 TO 20150406184753496[|[20150406184753496 TO 20150406214044655[|[20150406214044655 TO 20150407014828909[|[20150407014828909 TO 20150407043352795[|[20150407043352795 TO 20150407071346406[|[20150407071346406 TO 20150407095357703[|[20150407095357703 TO 20150407125310299[|[20150407125310299 TO 20150407153338036[|[20150407153338036 TO 20150407182358245[|[20150407182358245 TO 20150407210149938[|[20150407210149938 TO 20150408010143334[|[20150408010143334 TO 20150408035530747[|[20150408035530747 TO 20150408063841745[|[20150408063841745 TO 20150408092419541[|[20150408092419541 TO 20150408125710887[|[20150408125710887 TO 20150408165122192[|[20150408165122192 TO 20150408205855942[|[20150408205855942 TO 20150409005434420[|[20150409005434420 TO 20150409033624946[|[20150409033624946 TO 20150409063204278[|[20150409063204278 TO 20150409090922651[|[20150409090922651 TO 20150409114500875[|[20150409114500875 TO 20150409141703395[|[20150409141703395 TO 20150409165234337[|[20150409165234337 TO 20150409192335524[|[20150409192335524 TO 20150409214952900[|[20150409214952900 TO 20150410014946858[|[20150410014946858 TO 20150410043539782[|[20150410043539782 TO 20150410073726995[|[20150410073726995 TO 20150410103025354[|[20150410103025354 TO 20150410131911913[|[20150410131911913 TO 20150410161034652[|[20150410161034652 TO 20150410191337586[|[20150410191337586 TO 20150410222533929[|[20150410222533929 TO 20150411025842193[|[20150411025842193 TO 20150411061914564[|[20150411061914564 TO 20150423142056455[|[20150423142056455 TO 20150902135345158[|[20150902135345158 TO 20150918113050349[|[20150918113050349 TO 20160321140653900[|[20160321140653900 TO 20160322122557342[|[20160322122557342 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "SM_MODIFICATION_DATE", 0, rangesAsString );
	}

	@Test
	public void startSplitting_SM_UUID() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		ref.readIndexReference(keyspace, baseUUID, "SM_UUID", "NOMINAL");
		String rangesAsString = "[min_lower_bound TO 007fe76c-6fb7-4517-b51d-87a438fd022b[|[007fe76c-6fb7-4517-b51d-87a438fd022b TO 0119b260-4983-4b95-ac41-6a14883419cd[|[0119b260-4983-4b95-ac41-6a14883419cd TO 01ec1c13-92a3-4233-b79d-5630ec065475[|[01ec1c13-92a3-4233-b79d-5630ec065475 TO 0280c7b2-314e-4eb6-aa2c-7896f28be6db[|[0280c7b2-314e-4eb6-aa2c-7896f28be6db TO 031d5f2b-40c6-4b00-9cce-c1d80e739cb2[|[031d5f2b-40c6-4b00-9cce-c1d80e739cb2 TO 03d431aa-734c-4064-9020-644cc9888ad6[|[03d431aa-734c-4064-9020-644cc9888ad6 TO 0473435f-eef9-4b85-8b1d-bc7600737623[|[0473435f-eef9-4b85-8b1d-bc7600737623 TO 0510d98a-7f49-4277-9a8c-2295c99745af[|[0510d98a-7f49-4277-9a8c-2295c99745af TO 05b61b1e-5c19-418b-91d7-67894fdf558d[|[05b61b1e-5c19-418b-91d7-67894fdf558d TO 06544dcd-5ca9-4429-a48b-2bb3ce3130ee[|[06544dcd-5ca9-4429-a48b-2bb3ce3130ee TO 06f73c6f-dbc2-4df6-b272-20ff7f61d219[|[06f73c6f-dbc2-4df6-b272-20ff7f61d219 TO 07986715-af26-4fe0-873a-581c3652fde8[|[07986715-af26-4fe0-873a-581c3652fde8 TO 0842f4f8-0109-4d84-8f00-6f7ed538d9d2[|[0842f4f8-0109-4d84-8f00-6f7ed538d9d2 TO 08f10f94-bde8-430f-8dcf-8acb1e5b484e[|[08f10f94-bde8-430f-8dcf-8acb1e5b484e TO 098957c3-0ca6-4992-935a-9c3229257f88[|[098957c3-0ca6-4992-935a-9c3229257f88 TO 0a3d3a6a-ae83-406d-9870-b708f2496e26[|[0a3d3a6a-ae83-406d-9870-b708f2496e26 TO 0ad863db-c2be-4cdd-b63a-0b025a27a1aa[|[0ad863db-c2be-4cdd-b63a-0b025a27a1aa TO 0b702f93-0850-4158-a6bb-5e15c7c6c169[|[0b702f93-0850-4158-a6bb-5e15c7c6c169 TO 0bfa68b4-5d0d-4cb5-8d9c-24fa513455fd[|[0bfa68b4-5d0d-4cb5-8d9c-24fa513455fd TO 0c923db5-77e5-402b-8cb2-ece4037bbd07[|[0c923db5-77e5-402b-8cb2-ece4037bbd07 TO 0d4f55e1-7334-45f4-ad94-a3e25f8d2f7f[|[0d4f55e1-7334-45f4-ad94-a3e25f8d2f7f TO 0e202c59-b738-4d63-bb74-0d98f4e577d0[|[0e202c59-b738-4d63-bb74-0d98f4e577d0 TO 0ebe4216-7b23-4042-a264-ac8799c9cc4a[|[0ebe4216-7b23-4042-a264-ac8799c9cc4a TO 0f647aa8-f209-4fe8-8c74-2c5020b2abcf[|[0f647aa8-f209-4fe8-8c74-2c5020b2abcf TO 100c4792-33fb-4bbd-b0b7-abc5e0a1fd53[|[100c4792-33fb-4bbd-b0b7-abc5e0a1fd53 TO 10cd3cde-1ea6-4417-a741-adf9446b97e5[|[10cd3cde-1ea6-4417-a741-adf9446b97e5 TO 117af119-cbb1-4dd0-9935-23f552a09ee5[|[117af119-cbb1-4dd0-9935-23f552a09ee5 TO 1218f816-5d5c-44ed-90c3-61d22877ecfd[|[1218f816-5d5c-44ed-90c3-61d22877ecfd TO 12ae393e-1c28-4d33-9e1d-86289473a0b3[|[12ae393e-1c28-4d33-9e1d-86289473a0b3 TO 1363d4d5-2b46-42f9-be35-0530e1555aa7[|[1363d4d5-2b46-42f9-be35-0530e1555aa7 TO 13fa9ccf-5633-4d45-b61d-9fde20e42428[|[13fa9ccf-5633-4d45-b61d-9fde20e42428 TO 14938bf9-628d-4f8a-868d-9b926f26ee67[|[14938bf9-628d-4f8a-868d-9b926f26ee67 TO 153c3931-b9cc-47ca-9899-087b2a1bfca3[|[153c3931-b9cc-47ca-9899-087b2a1bfca3 TO 15c7b237-29d6-41ca-af68-4314fc83b28b[|[15c7b237-29d6-41ca-af68-4314fc83b28b TO 166d9b37-b31d-40af-be3f-f1e36add433a[|[166d9b37-b31d-40af-be3f-f1e36add433a TO 171b97ea-1b0d-4e27-901e-ab2cae3ee0f6[|[171b97ea-1b0d-4e27-901e-ab2cae3ee0f6 TO 17b7075b-9144-4228-b095-2c06c43f382d[|[17b7075b-9144-4228-b095-2c06c43f382d TO 18487a19-d8bd-4e95-b076-ae196b2ba1d5[|[18487a19-d8bd-4e95-b076-ae196b2ba1d5 TO 18d743af-2008-47fe-a700-4ea5de516ed2[|[18d743af-2008-47fe-a700-4ea5de516ed2 TO 1978d498-f7a3-4abb-8888-d58edd9f62ee[|[1978d498-f7a3-4abb-8888-d58edd9f62ee TO 1a27ca12-642b-4768-bbdd-bf8dd12f891f[|[1a27ca12-642b-4768-bbdd-bf8dd12f891f TO 1ab6d133-d0ad-437c-887c-86da9b2f7d96[|[1ab6d133-d0ad-437c-887c-86da9b2f7d96 TO 1b51263e-0390-4029-b107-4ce72721b638[|[1b51263e-0390-4029-b107-4ce72721b638 TO 1beb4b4c-7107-49b1-80cf-3bd50c604387[|[1beb4b4c-7107-49b1-80cf-3bd50c604387 TO 1c9a6afa-ce16-4c45-8827-ebd3df785ad0[|[1c9a6afa-ce16-4c45-8827-ebd3df785ad0 TO 1d29eea2-b426-47e4-a096-fc229eb78eb0[|[1d29eea2-b426-47e4-a096-fc229eb78eb0 TO 1dd03110-0358-4840-8a50-9671d4fe8023[|[1dd03110-0358-4840-8a50-9671d4fe8023 TO 1e60887c-87a2-4256-8d84-d6dc2cb66852[|[1e60887c-87a2-4256-8d84-d6dc2cb66852 TO 1f11229e-5ce8-492e-a2c8-4b729cf19937[|[1f11229e-5ce8-492e-a2c8-4b729cf19937 TO 1fc518de-413d-4ec3-8e75-dab40a413155[|[1fc518de-413d-4ec3-8e75-dab40a413155 TO 2064e3ae-2dc8-4be4-8898-b498f073086b[|[2064e3ae-2dc8-4be4-8898-b498f073086b TO 20ff6f78-6e1c-4d20-8919-f52d1329337f[|[20ff6f78-6e1c-4d20-8919-f52d1329337f TO 21a2da00-c277-4497-909a-9a525291278c[|[21a2da00-c277-4497-909a-9a525291278c TO 2239fc1e-ec1b-41bc-8593-16fc68cf04c3[|[2239fc1e-ec1b-41bc-8593-16fc68cf04c3 TO 22cbbe3e-86e7-45ad-bd76-a36248c3da4c[|[22cbbe3e-86e7-45ad-bd76-a36248c3da4c TO 235ae509-79f7-4b5b-891f-3418b8f93057[|[235ae509-79f7-4b5b-891f-3418b8f93057 TO 2403ef98-132d-43f4-bd0b-ad6ce3406bc5[|[2403ef98-132d-43f4-bd0b-ad6ce3406bc5 TO 249f8d8c-8078-45e8-86ea-9b4ed1442c9b[|[249f8d8c-8078-45e8-86ea-9b4ed1442c9b TO 256fe537-fa8e-4688-bfe6-024a5402e835[|[256fe537-fa8e-4688-bfe6-024a5402e835 TO 26205776-82c5-4dd8-802b-b2d195da9d73[|[26205776-82c5-4dd8-802b-b2d195da9d73 TO 26cd1b53-0035-4d88-ae50-6b7e03ed5b92[|[26cd1b53-0035-4d88-ae50-6b7e03ed5b92 TO 2762e0e3-d27b-4d73-b35e-92d23afb4da2[|[2762e0e3-d27b-4d73-b35e-92d23afb4da2 TO 28058bd4-9522-45a3-9d94-75bf2055bc7e[|[28058bd4-9522-45a3-9d94-75bf2055bc7e TO 28a6ba31-334c-4c0b-b430-aae9ffac012a[|[28a6ba31-334c-4c0b-b430-aae9ffac012a TO 295a525c-541c-4bef-83d1-a0fe8b6c61cd[|[295a525c-541c-4bef-83d1-a0fe8b6c61cd TO 29f11e47-4d69-4658-b6b2-f60924fae692[|[29f11e47-4d69-4658-b6b2-f60924fae692 TO 2a89b579-06ec-451a-a9e4-943e87216194[|[2a89b579-06ec-451a-a9e4-943e87216194 TO 2b2711f4-dac6-4091-b793-4403519229c8[|[2b2711f4-dac6-4091-b793-4403519229c8 TO 2bcd1017-daa6-4578-92c3-c4707905b854[|[2bcd1017-daa6-4578-92c3-c4707905b854 TO 2c773cd4-be72-41db-ac8a-c6826211a83a[|[2c773cd4-be72-41db-ac8a-c6826211a83a TO 2d0b7d33-d755-4ed5-ac31-022ad3091209[|[2d0b7d33-d755-4ed5-ac31-022ad3091209 TO 2daf0a12-335e-430c-8bf8-19e55412f375[|[2daf0a12-335e-430c-8bf8-19e55412f375 TO 2e70543e-ca0f-422d-a7f5-434ff91f840e[|[2e70543e-ca0f-422d-a7f5-434ff91f840e TO 2efe41e8-afc9-48d3-a334-ce66bd6720f0[|[2efe41e8-afc9-48d3-a334-ce66bd6720f0 TO 2f92fd2b-e557-4d5d-ab8d-085d8ec55d1b[|[2f92fd2b-e557-4d5d-ab8d-085d8ec55d1b TO 302f1bb9-a1b4-4172-a2b1-e58fa3f9c193[|[302f1bb9-a1b4-4172-a2b1-e58fa3f9c193 TO 30cda124-ab91-4b83-b422-36fd1fbfdc0f[|[30cda124-ab91-4b83-b422-36fd1fbfdc0f TO 3173bd23-e5f7-4222-b1ab-02f9297a2adc[|[3173bd23-e5f7-4222-b1ab-02f9297a2adc TO 32292088-a90e-4061-b549-1287b80670ea[|[32292088-a90e-4061-b549-1287b80670ea TO 32b6208c-3bc2-4ab7-806c-3565d5bb1157[|[32b6208c-3bc2-4ab7-806c-3565d5bb1157 TO 334739da-4c88-470e-bc64-acde7972e1fe[|[334739da-4c88-470e-bc64-acde7972e1fe TO 34028332-afe0-4cbe-924c-45960f6ce6e3[|[34028332-afe0-4cbe-924c-45960f6ce6e3 TO 34a71fc3-8bd2-4efb-a519-86007a070fc8[|[34a71fc3-8bd2-4efb-a519-86007a070fc8 TO 35451c7e-96d4-4e80-aa87-9b05b3a75f1f[|[35451c7e-96d4-4e80-aa87-9b05b3a75f1f TO 35fc17bd-c0f2-452c-8402-0d505371d5f6[|[35fc17bd-c0f2-452c-8402-0d505371d5f6 TO 36b5e8d5-f590-4b73-b0d0-d4cdaba9a6e7[|[36b5e8d5-f590-4b73-b0d0-d4cdaba9a6e7 TO 375b58a6-fa23-46fd-894e-4d83fc79bffb[|[375b58a6-fa23-46fd-894e-4d83fc79bffb TO 37f341a5-9d78-4e3d-a2f3-ccdf6b27d16f[|[37f341a5-9d78-4e3d-a2f3-ccdf6b27d16f TO 38a82919-772e-437e-9499-9937ef0dbfd1[|[38a82919-772e-437e-9499-9937ef0dbfd1 TO 39452b92-927c-4fd0-9285-1ef37463c1cb[|[39452b92-927c-4fd0-9285-1ef37463c1cb TO 39ee1f60-ed5c-4d18-bc0f-9fcb39772098[|[39ee1f60-ed5c-4d18-bc0f-9fcb39772098 TO 3a8519f7-e39c-40b3-bd4a-6d728e445c9c[|[3a8519f7-e39c-40b3-bd4a-6d728e445c9c TO 3b1707e1-9742-4858-9b96-b1507f75d97e[|[3b1707e1-9742-4858-9b96-b1507f75d97e TO 3bc57d35-b449-4100-9436-63e80ec291e1[|[3bc57d35-b449-4100-9436-63e80ec291e1 TO 3c5808ab-0105-4354-8c4d-5284f3075df6[|[3c5808ab-0105-4354-8c4d-5284f3075df6 TO 3d068ead-6303-43f1-8612-2232d0569e9c[|[3d068ead-6303-43f1-8612-2232d0569e9c TO 3da6491a-6ff7-4aec-b128-d8a58148fcd1[|[3da6491a-6ff7-4aec-b128-d8a58148fcd1 TO 3e44179c-dc7c-48ce-b5ba-044d4c0a8e4f[|[3e44179c-dc7c-48ce-b5ba-044d4c0a8e4f TO 3ee634e8-2490-4204-b11e-e124f54ef20b[|[3ee634e8-2490-4204-b11e-e124f54ef20b TO 3f8911a8-c44a-4c0c-858b-31f8caad59b3[|[3f8911a8-c44a-4c0c-858b-31f8caad59b3 TO 4039ea36-aa2b-46ad-9046-64087c84b4c8[|[4039ea36-aa2b-46ad-9046-64087c84b4c8 TO 40de888d-6ae3-4877-b5cd-594e5038f031[|[40de888d-6ae3-4877-b5cd-594e5038f031 TO 4191ce5b-9eff-427d-835b-a4f48fbb6f89[|[4191ce5b-9eff-427d-835b-a4f48fbb6f89 TO 422c5597-85bc-48a7-982c-613064aeb947[|[422c5597-85bc-48a7-982c-613064aeb947 TO 42e4e273-a35f-4684-ac63-3c0da9c315a7[|[42e4e273-a35f-4684-ac63-3c0da9c315a7 TO 437892bc-9d6d-4416-b9f6-c2fa12106748[|[437892bc-9d6d-4416-b9f6-c2fa12106748 TO 441bc68d-9052-4955-b3fe-585e76aa2376[|[441bc68d-9052-4955-b3fe-585e76aa2376 TO 44da1667-a9b2-4683-af0a-6f84a49f0753[|[44da1667-a9b2-4683-af0a-6f84a49f0753 TO 457c7c4b-023e-45c6-b972-64bcf65851b4[|[457c7c4b-023e-45c6-b972-64bcf65851b4 TO 460f9234-b19f-4e29-8410-e2de3348f924[|[460f9234-b19f-4e29-8410-e2de3348f924 TO 4694e8a8-5283-438e-8c45-f849320d0d7d[|[4694e8a8-5283-438e-8c45-f849320d0d7d TO 474bf2c0-04ac-474d-998b-0a52ba92dcce[|[474bf2c0-04ac-474d-998b-0a52ba92dcce TO 47faaed7-686b-4394-bd80-04606ae4d6a2[|[47faaed7-686b-4394-bd80-04606ae4d6a2 TO 48a7d5e5-fb6f-4992-bd88-5bcf5b35b069[|[48a7d5e5-fb6f-4992-bd88-5bcf5b35b069 TO 49516a19-b365-45ef-8a40-ffe8e0c4f5d2[|[49516a19-b365-45ef-8a40-ffe8e0c4f5d2 TO 4a0abab0-3e1e-4b9c-9f3a-c1c3f28b6a03[|[4a0abab0-3e1e-4b9c-9f3a-c1c3f28b6a03 TO 4ab04d3d-16d4-4f6b-8438-3fc171a10a65[|[4ab04d3d-16d4-4f6b-8438-3fc171a10a65 TO 4b55e198-cc97-4c54-bf8d-dabb38a9a14c[|[4b55e198-cc97-4c54-bf8d-dabb38a9a14c TO 4bf6dda1-5c0d-45dd-a6cc-40eb0776147a[|[4bf6dda1-5c0d-45dd-a6cc-40eb0776147a TO 4c8b84cd-7989-4433-94b4-158d28cd7eed[|[4c8b84cd-7989-4433-94b4-158d28cd7eed TO 4d2b97be-2d22-439e-8984-b764513e8c6c[|[4d2b97be-2d22-439e-8984-b764513e8c6c TO 4dd6be06-0feb-41ee-baef-8d9ae130e86d[|[4dd6be06-0feb-41ee-baef-8d9ae130e86d TO 4e664231-12d0-4f9a-be9c-2888829efdb0[|[4e664231-12d0-4f9a-be9c-2888829efdb0 TO 4ef85759-a583-451b-b54a-7cdd7a24ac6a[|[4ef85759-a583-451b-b54a-7cdd7a24ac6a TO 4fa3c6b5-0ce2-4a30-9228-297b9c3746bf[|[4fa3c6b5-0ce2-4a30-9228-297b9c3746bf TO 5042fbb7-6782-49d1-8d38-133d70f2dbb9[|[5042fbb7-6782-49d1-8d38-133d70f2dbb9 TO 50ecfd85-4f9b-4b16-a378-2172c0f292af[|[50ecfd85-4f9b-4b16-a378-2172c0f292af TO 518bbcea-2909-4836-87e7-e3e7e9c24d96[|[518bbcea-2909-4836-87e7-e3e7e9c24d96 TO 521c48c7-3a07-4715-ada6-087a051f07fc[|[521c48c7-3a07-4715-ada6-087a051f07fc TO 52c43557-2385-4c3f-9244-301c91bcbc5e[|[52c43557-2385-4c3f-9244-301c91bcbc5e TO 5350c249-0321-4e49-b91f-a53cf894c796[|[5350c249-0321-4e49-b91f-a53cf894c796 TO 5410932e-3d72-4a8a-b974-cce8c3354602[|[5410932e-3d72-4a8a-b974-cce8c3354602 TO 54adafb7-ab99-49d7-93b3-a881921786c7[|[54adafb7-ab99-49d7-93b3-a881921786c7 TO 5547c450-db3c-4b8c-a323-890b6f5aa9d9[|[5547c450-db3c-4b8c-a323-890b6f5aa9d9 TO 55e53136-b194-46bf-935f-a43e58918393[|[55e53136-b194-46bf-935f-a43e58918393 TO 56857d19-aef5-41ed-8a45-ba250dda7169[|[56857d19-aef5-41ed-8a45-ba250dda7169 TO 5743fe55-a996-4156-a267-054fb0d6cb99[|[5743fe55-a996-4156-a267-054fb0d6cb99 TO 57f4d596-a2d6-4201-8246-6e0aeed801c4[|[57f4d596-a2d6-4201-8246-6e0aeed801c4 TO 589ceb71-8feb-4611-b416-698b96000307[|[589ceb71-8feb-4611-b416-698b96000307 TO 59385a82-fbe9-41ef-b666-cfd56e9532b7[|[59385a82-fbe9-41ef-b666-cfd56e9532b7 TO 59e3df6b-efb0-4050-ad77-715b7a534e3e[|[59e3df6b-efb0-4050-ad77-715b7a534e3e TO 5a7ee9cf-acee-4f52-a679-a69311ea9288[|[5a7ee9cf-acee-4f52-a679-a69311ea9288 TO 5b3500fb-0646-40ba-95b9-604c8d138c80[|[5b3500fb-0646-40ba-95b9-604c8d138c80 TO 5bc67c68-585c-44b6-8f3d-ca778f460e13[|[5bc67c68-585c-44b6-8f3d-ca778f460e13 TO 5c7a4ef7-8017-46e4-8f3c-612630acd94c[|[5c7a4ef7-8017-46e4-8f3c-612630acd94c TO 5d1c28d3-f035-4c1c-a5dc-eb192462f73d[|[5d1c28d3-f035-4c1c-a5dc-eb192462f73d TO 5dc33fad-de91-4f08-90db-80a697a00424[|[5dc33fad-de91-4f08-90db-80a697a00424 TO 5e7331c6-ecea-4458-9a63-189e6477f490[|[5e7331c6-ecea-4458-9a63-189e6477f490 TO 5f1d0a65-ad4f-4407-87be-5ff759679832[|[5f1d0a65-ad4f-4407-87be-5ff759679832 TO 5fddaa2d-3a8f-4c97-a580-45c14610d00e[|[5fddaa2d-3a8f-4c97-a580-45c14610d00e TO 60876c25-c2bb-4591-8093-feca83d4bd15[|[60876c25-c2bb-4591-8093-feca83d4bd15 TO 612722c5-76d1-450d-9642-b44e35f42977[|[612722c5-76d1-450d-9642-b44e35f42977 TO 61c65261-6a4e-4d51-afa1-8d94a2176c87[|[61c65261-6a4e-4d51-afa1-8d94a2176c87 TO 62751db8-e5bc-4253-a8cc-4ad3d5c9797f[|[62751db8-e5bc-4253-a8cc-4ad3d5c9797f TO 634123fe-6911-4088-939a-37298b82d1fd[|[634123fe-6911-4088-939a-37298b82d1fd TO 63e75380-2237-4e83-aa9b-01890a020e8b[|[63e75380-2237-4e83-aa9b-01890a020e8b TO 646df192-3fbb-405f-a1b5-743d8eccf841[|[646df192-3fbb-405f-a1b5-743d8eccf841 TO 6544182f-d815-4d1d-9334-33f9f4d4651e[|[6544182f-d815-4d1d-9334-33f9f4d4651e TO 66090f7f-5e9b-4466-862f-296fef9ad757[|[66090f7f-5e9b-4466-862f-296fef9ad757 TO 66aa3430-883b-4c48-a7f5-ab09fe8e72bd[|[66aa3430-883b-4c48-a7f5-ab09fe8e72bd TO 6751f679-1dd1-4dbe-a8ae-134c60964b7c[|[6751f679-1dd1-4dbe-a8ae-134c60964b7c TO 67ddfa4b-a471-46e7-a20a-d4c71c8a351c[|[67ddfa4b-a471-46e7-a20a-d4c71c8a351c TO 68786727-a395-4d71-9e3d-c1645f084348[|[68786727-a395-4d71-9e3d-c1645f084348 TO 69286bf3-d6fc-4e14-bbf4-dc81b90ad73d[|[69286bf3-d6fc-4e14-bbf4-dc81b90ad73d TO 69ced6a2-8ee8-4b78-9caa-82b137c36e23[|[69ced6a2-8ee8-4b78-9caa-82b137c36e23 TO 6a90bd6b-5d7d-4145-a8c1-0087e4787022[|[6a90bd6b-5d7d-4145-a8c1-0087e4787022 TO 6b3c469b-5113-46d4-9a87-84b2feeb907b[|[6b3c469b-5113-46d4-9a87-84b2feeb907b TO 6bf6e6ab-c069-4a49-8aaf-94ff22472328[|[6bf6e6ab-c069-4a49-8aaf-94ff22472328 TO 6c8890c8-0d91-4e7e-b1dc-297bd032a40b[|[6c8890c8-0d91-4e7e-b1dc-297bd032a40b TO 6d38294b-ac22-495d-ac41-e6411d0a493d[|[6d38294b-ac22-495d-ac41-e6411d0a493d TO 6de1c0c1-a877-4e17-b94d-99bd91edc92b[|[6de1c0c1-a877-4e17-b94d-99bd91edc92b TO 6e7b111d-1308-41ac-bd92-0d458fb85a92[|[6e7b111d-1308-41ac-bd92-0d458fb85a92 TO 6f1bdeea-f9a2-4c4d-a1d7-a44c6faf0979[|[6f1bdeea-f9a2-4c4d-a1d7-a44c6faf0979 TO 6fd0b663-8914-4cc5-b452-15e786eef5ba[|[6fd0b663-8914-4cc5-b452-15e786eef5ba TO 7073cd87-75be-4759-b68a-249305876f67[|[7073cd87-75be-4759-b68a-249305876f67 TO 71116c4c-7ee5-4c7a-b1d9-1c53843d9aec[|[71116c4c-7ee5-4c7a-b1d9-1c53843d9aec TO 71a8cd2e-c224-4e5d-b899-f152ca31e902[|[71a8cd2e-c224-4e5d-b899-f152ca31e902 TO 724fe0db-0791-4d2a-a516-b0bfbff11213[|[724fe0db-0791-4d2a-a516-b0bfbff11213 TO 72f7f572-d856-4876-83c6-e1ec326c8058[|[72f7f572-d856-4876-83c6-e1ec326c8058 TO 73a9e847-7f76-42da-9df9-e6fcc99ae6de[|[73a9e847-7f76-42da-9df9-e6fcc99ae6de TO 74466e0e-ffb3-45d3-a0da-ff8fe7ff104a[|[74466e0e-ffb3-45d3-a0da-ff8fe7ff104a TO 74e5c523-987f-4e01-8657-ab4b1a90524e[|[74e5c523-987f-4e01-8657-ab4b1a90524e TO 758fc38f-404e-4ae2-9648-c35a942a5614[|[758fc38f-404e-4ae2-9648-c35a942a5614 TO 762d2e36-1b53-4049-900e-dcd37ab7b4a4[|[762d2e36-1b53-4049-900e-dcd37ab7b4a4 TO 76cb1acc-ff10-40b7-a380-9537e92cb46c[|[76cb1acc-ff10-40b7-a380-9537e92cb46c TO 77751f4f-c71e-4e55-900d-b1919150c43d[|[77751f4f-c71e-4e55-900d-b1919150c43d TO 78294a6a-4314-4516-811e-e71adaefcbc4[|[78294a6a-4314-4516-811e-e71adaefcbc4 TO 78e54e0f-0d05-4176-b948-1b0a70221e45[|[78e54e0f-0d05-4176-b948-1b0a70221e45 TO 79956ede-5ab1-4f9f-9e01-051de1294741[|[79956ede-5ab1-4f9f-9e01-051de1294741 TO 7a421c58-8d74-4bbd-9570-93cba1baea45[|[7a421c58-8d74-4bbd-9570-93cba1baea45 TO 7b035311-7c27-4562-af15-a39c69f3c813[|[7b035311-7c27-4562-af15-a39c69f3c813 TO 7b911da6-a98b-47c7-b35c-1ab568ad9b4c[|[7b911da6-a98b-47c7-b35c-1ab568ad9b4c TO 7c1ca7b2-6e01-4dd4-b127-6ba59b99aa84[|[7c1ca7b2-6e01-4dd4-b127-6ba59b99aa84 TO 7cb85f4e-3ce9-4e54-8145-ed5a3903722e[|[7cb85f4e-3ce9-4e54-8145-ed5a3903722e TO 7d4b68be-0a9d-45be-8266-a7fe820369a9[|[7d4b68be-0a9d-45be-8266-a7fe820369a9 TO 7dd1734a-d4e7-4170-a5ab-a3cdf1ee4534[|[7dd1734a-d4e7-4170-a5ab-a3cdf1ee4534 TO 7e8362ea-1ede-42e7-8e7a-61d08328f692[|[7e8362ea-1ede-42e7-8e7a-61d08328f692 TO 7f3f39ad-35d1-42b1-a731-d9e18c6e6255[|[7f3f39ad-35d1-42b1-a731-d9e18c6e6255 TO 7fd318ca-b0d3-4821-a3d6-48f2414e55de[|[7fd318ca-b0d3-4821-a3d6-48f2414e55de TO 8082bab1-1c48-4a46-b57a-8cc77ec7cf71[|[8082bab1-1c48-4a46-b57a-8cc77ec7cf71 TO 8122bf95-ef94-4235-b328-f81bce802685[|[8122bf95-ef94-4235-b328-f81bce802685 TO 81cc88ce-d90f-4623-8a7c-3f6acb5f1abc[|[81cc88ce-d90f-4623-8a7c-3f6acb5f1abc TO 825d3431-45a4-494a-b026-79aa698d61ac[|[825d3431-45a4-494a-b026-79aa698d61ac TO 82f992a0-cc7d-4e77-afcf-d5317c3fe94b[|[82f992a0-cc7d-4e77-afcf-d5317c3fe94b TO 8392f4ad-f8cb-44f0-bc13-8875e7daf720[|[8392f4ad-f8cb-44f0-bc13-8875e7daf720 TO 84416cc0-4b11-46c5-be1c-a0e1cbd27aa4[|[84416cc0-4b11-46c5-be1c-a0e1cbd27aa4 TO 84e76db5-e22c-4ea0-8c93-be4d9da03260[|[84e76db5-e22c-4ea0-8c93-be4d9da03260 TO 8586d508-111e-4f24-bb16-d1cf0614d516[|[8586d508-111e-4f24-bb16-d1cf0614d516 TO 8629af76-206d-4ba0-b1a3-24b16e905a31[|[8629af76-206d-4ba0-b1a3-24b16e905a31 TO 86be235b-faac-438d-a833-0792186f9b2b[|[86be235b-faac-438d-a833-0792186f9b2b TO 876e2b1a-7828-44f9-b7d6-d0049b67a0ec[|[876e2b1a-7828-44f9-b7d6-d0049b67a0ec TO 880e7975-1ab7-4095-8931-f6e0fbc30cee[|[880e7975-1ab7-4095-8931-f6e0fbc30cee TO 88c081aa-826c-4052-8e86-a7dc965b0483[|[88c081aa-826c-4052-8e86-a7dc965b0483 TO 8969c302-90c6-4a0a-b046-a29cd10c4706[|[8969c302-90c6-4a0a-b046-a29cd10c4706 TO 8a0f1666-47e2-4173-b25c-80836aa7e4f0[|[8a0f1666-47e2-4173-b25c-80836aa7e4f0 TO 8ac5791f-e8a2-4356-b781-7325270f64f4[|[8ac5791f-e8a2-4356-b781-7325270f64f4 TO 8b636e80-24b1-4d6a-a859-aeb237f248bd[|[8b636e80-24b1-4d6a-a859-aeb237f248bd TO 8bfe163b-96f9-4b0f-b1d6-8c62ecdc1801[|[8bfe163b-96f9-4b0f-b1d6-8c62ecdc1801 TO 8cad2935-9286-4f02-b3c5-34bc1757b4e8[|[8cad2935-9286-4f02-b3c5-34bc1757b4e8 TO 8d316bbf-3222-496a-8ee7-67e760ecb3ff[|[8d316bbf-3222-496a-8ee7-67e760ecb3ff TO 8dd96aab-a1b9-49b2-82f5-3fd913e76420[|[8dd96aab-a1b9-49b2-82f5-3fd913e76420 TO 8e758c34-2e2e-4a97-8ad3-b41c869da373[|[8e758c34-2e2e-4a97-8ad3-b41c869da373 TO 8f374d41-d17d-433c-9a8c-51dbd6a88c28[|[8f374d41-d17d-433c-9a8c-51dbd6a88c28 TO 8fc82d7b-bd3a-4c2d-bf72-3f547a03901a[|[8fc82d7b-bd3a-4c2d-bf72-3f547a03901a TO 9070b79f-115b-4e0f-8a33-72e5ac405a86[|[9070b79f-115b-4e0f-8a33-72e5ac405a86 TO 90fb078e-753a-4c1b-acdd-273b5b2f389a[|[90fb078e-753a-4c1b-acdd-273b5b2f389a TO 91a028ee-fd99-45ba-aa38-e2a2e4a8eac2[|[91a028ee-fd99-45ba-aa38-e2a2e4a8eac2 TO 92459b97-eb45-431a-ba98-f62abb68deac[|[92459b97-eb45-431a-ba98-f62abb68deac TO 92f86e95-49ee-49f5-a495-1f99dd858573[|[92f86e95-49ee-49f5-a495-1f99dd858573 TO 93aa6475-6b8e-48a6-8092-66b00c67cc4b[|[93aa6475-6b8e-48a6-8092-66b00c67cc4b TO 944ef7ea-cddd-476d-90de-f5555b34d059[|[944ef7ea-cddd-476d-90de-f5555b34d059 TO 94f32ad2-bc84-4212-bb77-9f3943bbfb54[|[94f32ad2-bc84-4212-bb77-9f3943bbfb54 TO 959d349e-ef46-40b7-a401-f33952112810[|[959d349e-ef46-40b7-a401-f33952112810 TO 963b6831-b655-4001-b997-3fef8d022f1e[|[963b6831-b655-4001-b997-3fef8d022f1e TO 96da6df9-9121-4a49-9c09-e08b54a4b4a6[|[96da6df9-9121-4a49-9c09-e08b54a4b4a6 TO 97717c72-cd31-43bd-93d8-4433846c7752[|[97717c72-cd31-43bd-93d8-4433846c7752 TO 981d66c4-00a9-4ca6-9fa8-3e88ba859eab[|[981d66c4-00a9-4ca6-9fa8-3e88ba859eab TO 98bf41c0-bd6a-4b37-bb0a-3e5fce0a2852[|[98bf41c0-bd6a-4b37-bb0a-3e5fce0a2852 TO 994be97d-82ff-4b78-9eee-309c304cd9cf[|[994be97d-82ff-4b78-9eee-309c304cd9cf TO 99d919eb-ecf4-4643-8635-e3a2fd741173[|[99d919eb-ecf4-4643-8635-e3a2fd741173 TO 9a7c15ec-7dd3-46a8-8fc4-99b7ee95eccd[|[9a7c15ec-7dd3-46a8-8fc4-99b7ee95eccd TO 9b2b93f5-c044-47d8-b836-eeb3ce5ad582[|[9b2b93f5-c044-47d8-b836-eeb3ce5ad582 TO 9bd485fd-9b84-4cee-be28-aee77752f556[|[9bd485fd-9b84-4cee-be28-aee77752f556 TO 9c69ca92-738d-4d58-b9ee-727b08e6392e[|[9c69ca92-738d-4d58-b9ee-727b08e6392e TO 9cffc0cc-3f90-4f37-a529-b50de271b5d0[|[9cffc0cc-3f90-4f37-a529-b50de271b5d0 TO 9d8842c5-e914-4d45-9766-1745ae092c1a[|[9d8842c5-e914-4d45-9766-1745ae092c1a TO 9e45dc30-d082-47e2-9d53-24edb7ee3f5a[|[9e45dc30-d082-47e2-9d53-24edb7ee3f5a TO 9ee1141f-4d22-498a-b23b-c88b78d18100[|[9ee1141f-4d22-498a-b23b-c88b78d18100 TO 9f668fcd-9858-4c0d-b14d-a7939175236e[|[9f668fcd-9858-4c0d-b14d-a7939175236e TO a01e07a8-6d63-48aa-8d8e-2d5b503a0758[|[a01e07a8-6d63-48aa-8d8e-2d5b503a0758 TO a0caaa41-0709-4779-8ea1-9ceec6594393[|[a0caaa41-0709-4779-8ea1-9ceec6594393 TO a16ff8ad-06d7-4e1c-a186-78e448d31f6f[|[a16ff8ad-06d7-4e1c-a186-78e448d31f6f TO a22729b0-1d6a-4d69-9524-3ef49845ea6b[|[a22729b0-1d6a-4d69-9524-3ef49845ea6b TO a2c01034-73e4-4d1c-898d-ab70e43cf82d[|[a2c01034-73e4-4d1c-898d-ab70e43cf82d TO a3527f4f-c673-4d5c-897a-53a04455eef4[|[a3527f4f-c673-4d5c-897a-53a04455eef4 TO a40a535c-c75b-4d7f-b984-27795de190b3[|[a40a535c-c75b-4d7f-b984-27795de190b3 TO a4bee3a7-0027-4e47-ba22-5eb94d036518[|[a4bee3a7-0027-4e47-ba22-5eb94d036518 TO a555efe9-bb7f-40d3-85b7-d2094a5c1cdf[|[a555efe9-bb7f-40d3-85b7-d2094a5c1cdf TO a5dbd913-d27b-4887-b1af-506c9838eadf[|[a5dbd913-d27b-4887-b1af-506c9838eadf TO a67a1507-8c44-4e14-a6b7-c0982c5419bd[|[a67a1507-8c44-4e14-a6b7-c0982c5419bd TO a7351962-308f-43f0-8c19-c506df0e1a4d[|[a7351962-308f-43f0-8c19-c506df0e1a4d TO a7bbe34a-9d38-4181-a72c-c3b4de676abc[|[a7bbe34a-9d38-4181-a72c-c3b4de676abc TO a850189d-b3c9-424a-967c-15746f55c82b[|[a850189d-b3c9-424a-967c-15746f55c82b TO a903b412-25f8-460b-a120-8ebed291e324[|[a903b412-25f8-460b-a120-8ebed291e324 TO a9a44bbb-cac4-464e-800c-ce88a3480067[|[a9a44bbb-cac4-464e-800c-ce88a3480067 TO aa5b7165-3d57-4e64-b6b1-5d806ea755d7[|[aa5b7165-3d57-4e64-b6b1-5d806ea755d7 TO aaf47bc9-f792-47dd-860d-265ada837044[|[aaf47bc9-f792-47dd-860d-265ada837044 TO aba0208b-fb61-4733-86da-6d069a52054b[|[aba0208b-fb61-4733-86da-6d069a52054b TO ac248dc4-8f2c-4a8a-845b-1d395c9c5439[|[ac248dc4-8f2c-4a8a-845b-1d395c9c5439 TO acc6f357-771b-4649-a419-0aece6ed2463[|[acc6f357-771b-4649-a419-0aece6ed2463 TO ad572bbd-d39a-4dd7-a2f9-315db10cda58[|[ad572bbd-d39a-4dd7-a2f9-315db10cda58 TO adee929d-fb77-44aa-8713-d80382fdeee3[|[adee929d-fb77-44aa-8713-d80382fdeee3 TO ae85fc51-611b-4b2b-b4c6-ced00119446f[|[ae85fc51-611b-4b2b-b4c6-ced00119446f TO af235a8f-ee5e-4818-81a8-5b13726c08f3[|[af235a8f-ee5e-4818-81a8-5b13726c08f3 TO afbe4900-5cc2-434d-81a7-a65f3f095d58[|[afbe4900-5cc2-434d-81a7-a65f3f095d58 TO b07331ab-bc84-4428-a11e-f93383df391b[|[b07331ab-bc84-4428-a11e-f93383df391b TO b1098421-5068-4478-b5ad-e31641a13af9[|[b1098421-5068-4478-b5ad-e31641a13af9 TO b1b89178-b8c3-4edc-9723-02d0e959c828[|[b1b89178-b8c3-4edc-9723-02d0e959c828 TO b2639965-6de1-44c2-be3a-662617b5e828[|[b2639965-6de1-44c2-be3a-662617b5e828 TO b2f46aa4-7445-433a-a76d-8a7cf81ed9c9[|[b2f46aa4-7445-433a-a76d-8a7cf81ed9c9 TO b38baeb4-41a8-47d9-96e9-9bf2e1c887bd[|[b38baeb4-41a8-47d9-96e9-9bf2e1c887bd TO b435b416-5e88-4bac-8b90-389d978427c9[|[b435b416-5e88-4bac-8b90-389d978427c9 TO b4e1d351-5a0b-454e-9dec-1ae0eb50a237[|[b4e1d351-5a0b-454e-9dec-1ae0eb50a237 TO b5824bdc-1e06-44da-9c4c-68ed579f84f9[|[b5824bdc-1e06-44da-9c4c-68ed579f84f9 TO b640dfbe-835e-4866-9a7d-cbdf62311932[|[b640dfbe-835e-4866-9a7d-cbdf62311932 TO b6f01520-232b-4e0f-aad4-011ce5ad8ae7[|[b6f01520-232b-4e0f-aad4-011ce5ad8ae7 TO b7ae4d50-50aa-4fe0-bb4b-b543022492b8[|[b7ae4d50-50aa-4fe0-bb4b-b543022492b8 TO b84a9798-63b1-41a5-9703-5c994eb1959d[|[b84a9798-63b1-41a5-9703-5c994eb1959d TO b8fb4426-fab0-47f3-8d58-6d2302da032d[|[b8fb4426-fab0-47f3-8d58-6d2302da032d TO b9b3d9ec-f81c-4fba-9d15-952fdc5b3ead[|[b9b3d9ec-f81c-4fba-9d15-952fdc5b3ead TO ba4c33f4-b920-46be-bbe4-69cf6ac82b19[|[ba4c33f4-b920-46be-bbe4-69cf6ac82b19 TO bacfc103-af01-4048-8955-ec7abde9c2f1[|[bacfc103-af01-4048-8955-ec7abde9c2f1 TO bb895922-c53b-4611-bcd6-6e2a964dcee9[|[bb895922-c53b-4611-bcd6-6e2a964dcee9 TO bc20c7bd-111f-401d-9fc7-6678d0545fa7[|[bc20c7bd-111f-401d-9fc7-6678d0545fa7 TO bcc280ac-01f3-4796-875d-1332e4a20e19[|[bcc280ac-01f3-4796-875d-1332e4a20e19 TO bd8445ca-c61c-431c-b1ca-9947f39ce221[|[bd8445ca-c61c-431c-b1ca-9947f39ce221 TO be0f6974-655c-4445-8710-729161d3b82c[|[be0f6974-655c-4445-8710-729161d3b82c TO bec0d16c-ee8b-423a-97db-570621b66d63[|[bec0d16c-ee8b-423a-97db-570621b66d63 TO bf559f89-4869-4254-abf8-7aa7c4bdedf1[|[bf559f89-4869-4254-abf8-7aa7c4bdedf1 TO bfff18e3-7ce1-45c8-80a5-c6163073d64c[|[bfff18e3-7ce1-45c8-80a5-c6163073d64c TO c09da6df-5735-493c-b245-8b1e87e823c2[|[c09da6df-5735-493c-b245-8b1e87e823c2 TO c12cae5d-0b1b-45d9-b42c-8911962b7f29[|[c12cae5d-0b1b-45d9-b42c-8911962b7f29 TO c1e141d0-4969-42b2-b213-97d3e55e2b13[|[c1e141d0-4969-42b2-b213-97d3e55e2b13 TO c28aeec1-1ae7-41e5-9016-6ec0e4892ee6[|[c28aeec1-1ae7-41e5-9016-6ec0e4892ee6 TO c3320dd8-57b8-4b24-a35c-3e5711a01744[|[c3320dd8-57b8-4b24-a35c-3e5711a01744 TO c3da14a3-1cd9-4e10-9032-ed03d25277ab[|[c3da14a3-1cd9-4e10-9032-ed03d25277ab TO c47d673a-57f9-4dbb-ba9e-858ba35c6af5[|[c47d673a-57f9-4dbb-ba9e-858ba35c6af5 TO c51bb33e-4694-45f6-8e96-fcac1f9c3a37[|[c51bb33e-4694-45f6-8e96-fcac1f9c3a37 TO c5c23a8b-40b7-493d-8ff9-9314d00d8f49[|[c5c23a8b-40b7-493d-8ff9-9314d00d8f49 TO c66ce6c1-756a-4850-a138-daeafb4b1240[|[c66ce6c1-756a-4850-a138-daeafb4b1240 TO c708b7e2-74c9-4d71-ae3e-7c95308b686d[|[c708b7e2-74c9-4d71-ae3e-7c95308b686d TO c7a4e9eb-0d0f-44bc-a2cf-992c5d254a8c[|[c7a4e9eb-0d0f-44bc-a2cf-992c5d254a8c TO c841da60-7d37-4191-8997-acf8124b5b6e[|[c841da60-7d37-4191-8997-acf8124b5b6e TO c8c787cc-7e45-4130-82ab-2eda10712a8e[|[c8c787cc-7e45-4130-82ab-2eda10712a8e TO c9593924-5030-4ef2-869c-2b9e6211f323[|[c9593924-5030-4ef2-869c-2b9e6211f323 TO c9f2da4f-cf92-4320-bfe2-77cfe2094dbb[|[c9f2da4f-cf92-4320-bfe2-77cfe2094dbb TO ca9fc39f-e5d1-4d55-a489-8b91139ee8c6[|[ca9fc39f-e5d1-4d55-a489-8b91139ee8c6 TO cb37a747-269e-432e-b635-fa7886a66dba[|[cb37a747-269e-432e-b635-fa7886a66dba TO cbcdbe82-044a-44a2-865a-cca8f184a939[|[cbcdbe82-044a-44a2-865a-cca8f184a939 TO cc79ee3b-ecc7-48bc-99b9-b2fcc3e36c38[|[cc79ee3b-ecc7-48bc-99b9-b2fcc3e36c38 TO cd07a0c4-2b0a-4009-8f62-595c3b90838a[|[cd07a0c4-2b0a-4009-8f62-595c3b90838a TO cda75a46-2c33-40d4-94bc-28ac5a526bb4[|[cda75a46-2c33-40d4-94bc-28ac5a526bb4 TO ce33c121-265e-4af4-85fd-d35cd750be74[|[ce33c121-265e-4af4-85fd-d35cd750be74 TO ced91a23-3cb7-45aa-8ef8-c4e23f1e85b8[|[ced91a23-3cb7-45aa-8ef8-c4e23f1e85b8 TO cf895f95-b67a-4a09-b52b-36889f2007ad[|[cf895f95-b67a-4a09-b52b-36889f2007ad TO d0195217-c320-40ff-9ac4-94581d6e7ea2[|[d0195217-c320-40ff-9ac4-94581d6e7ea2 TO d0ab77ff-fbdd-4080-a464-9ac34abcb439[|[d0ab77ff-fbdd-4080-a464-9ac34abcb439 TO d13de5a2-a952-4a90-8d05-9f0486ea97d3[|[d13de5a2-a952-4a90-8d05-9f0486ea97d3 TO d1e28cc9-eb0a-40ad-8334-15b630e93324[|[d1e28cc9-eb0a-40ad-8334-15b630e93324 TO d281662d-ffbc-47a3-acc5-70fc6cc4ad52[|[d281662d-ffbc-47a3-acc5-70fc6cc4ad52 TO d3136cf9-b12e-40d9-b3c8-bc0fc74361ba[|[d3136cf9-b12e-40d9-b3c8-bc0fc74361ba TO d3c4033d-c7c7-4611-8c71-dfb74d84b74b[|[d3c4033d-c7c7-4611-8c71-dfb74d84b74b TO d46bd2ce-e10f-443b-9db6-b6f9e35bee0b[|[d46bd2ce-e10f-443b-9db6-b6f9e35bee0b TO d527333a-a4fb-4937-a0c0-d7e69f9bc405[|[d527333a-a4fb-4937-a0c0-d7e69f9bc405 TO d5cf6a4e-8878-467f-b85c-8163a6433617[|[d5cf6a4e-8878-467f-b85c-8163a6433617 TO d68383da-4958-48f0-a5f3-c8d332ca6629[|[d68383da-4958-48f0-a5f3-c8d332ca6629 TO d7245e5a-87a9-432a-93bc-fce2b3adf30d[|[d7245e5a-87a9-432a-93bc-fce2b3adf30d TO d7d7bbac-c53f-4815-8cfc-b987b8ddb546[|[d7d7bbac-c53f-4815-8cfc-b987b8ddb546 TO d896466a-a075-4307-8a03-aed024ca62b9[|[d896466a-a075-4307-8a03-aed024ca62b9 TO d93ce136-ad28-4fde-be3e-58b780bef0ed[|[d93ce136-ad28-4fde-be3e-58b780bef0ed TO d9d93e21-8281-473b-8e9b-8635b3073645[|[d9d93e21-8281-473b-8e9b-8635b3073645 TO da845567-0aeb-4809-823f-6d616e36bed2[|[da845567-0aeb-4809-823f-6d616e36bed2 TO db122de3-f3a9-4aa7-bd0d-7f20515cd712[|[db122de3-f3a9-4aa7-bd0d-7f20515cd712 TO dba8eb53-9c4e-450c-9a64-68c9cdc6fc9a[|[dba8eb53-9c4e-450c-9a64-68c9cdc6fc9a TO dc5e6b96-b33a-4a31-9380-fdb745b5c0f8[|[dc5e6b96-b33a-4a31-9380-fdb745b5c0f8 TO dd122e9d-a724-4de7-a2a0-5852dbbccb9a[|[dd122e9d-a724-4de7-a2a0-5852dbbccb9a TO ddc08643-115e-4bfb-b6a2-309a1067ea5b[|[ddc08643-115e-4bfb-b6a2-309a1067ea5b TO de6db8a0-b22a-4b83-8769-1d36d1e66330[|[de6db8a0-b22a-4b83-8769-1d36d1e66330 TO df1d064e-6ffe-41f8-a9b8-a0b0dcd01c63[|[df1d064e-6ffe-41f8-a9b8-a0b0dcd01c63 TO dfc125bd-a378-47e9-a573-b5a873f968f9[|[dfc125bd-a378-47e9-a573-b5a873f968f9 TO e0729617-1c33-45fe-8996-1955709d3fe1[|[e0729617-1c33-45fe-8996-1955709d3fe1 TO e10705b0-4fc0-4d19-85d1-e03f588edb79[|[e10705b0-4fc0-4d19-85d1-e03f588edb79 TO e1c540de-b606-487c-bf61-57dd38a8323c[|[e1c540de-b606-487c-bf61-57dd38a8323c TO e272db6c-8874-4e86-a825-f543affb0a15[|[e272db6c-8874-4e86-a825-f543affb0a15 TO e30acf0e-c250-4e43-9414-3c9e06a70dd0[|[e30acf0e-c250-4e43-9414-3c9e06a70dd0 TO e3af25f8-14af-400d-b085-09f711950935[|[e3af25f8-14af-400d-b085-09f711950935 TO e43f242f-7dad-4fc4-bc2a-025dc910113e[|[e43f242f-7dad-4fc4-bc2a-025dc910113e TO e4dc770f-b5a6-43b0-baea-18e825743026[|[e4dc770f-b5a6-43b0-baea-18e825743026 TO e5815e60-dbf9-490c-a6be-c9f7eed034c0[|[e5815e60-dbf9-490c-a6be-c9f7eed034c0 TO e61599cf-2c2a-46df-9a28-71db4ac0a85e[|[e61599cf-2c2a-46df-9a28-71db4ac0a85e TO e6c6ef14-b9e1-44cb-98b1-6c990343230e[|[e6c6ef14-b9e1-44cb-98b1-6c990343230e TO e7594090-fde3-4a06-ae98-c68a3e3b36e8[|[e7594090-fde3-4a06-ae98-c68a3e3b36e8 TO e7fcb65f-3930-4494-927e-a59e806c9215[|[e7fcb65f-3930-4494-927e-a59e806c9215 TO e8a3ac40-84d2-4f09-b352-759859f6f9ac[|[e8a3ac40-84d2-4f09-b352-759859f6f9ac TO e94bacda-bb1b-460b-af3b-a09a3bc18f3a[|[e94bacda-bb1b-460b-af3b-a09a3bc18f3a TO e9e7b281-8430-4831-8d39-d9eaf9c85fd0[|[e9e7b281-8430-4831-8d39-d9eaf9c85fd0 TO ea8e17a7-7fa1-41bb-af7c-3af0c61b7893[|[ea8e17a7-7fa1-41bb-af7c-3af0c61b7893 TO eb31d3c2-5dac-47ee-ad6c-adcf67756938[|[eb31d3c2-5dac-47ee-ad6c-adcf67756938 TO ebe33699-20ce-4e09-9f78-e008376b8b1b[|[ebe33699-20ce-4e09-9f78-e008376b8b1b TO ec8c82a7-5a04-46e1-b4e3-7a74a2b090bd[|[ec8c82a7-5a04-46e1-b4e3-7a74a2b090bd TO ed2136ea-3c97-4bca-a5de-743435be9439[|[ed2136ea-3c97-4bca-a5de-743435be9439 TO edc1f6c2-6d51-49d6-a47d-e8cbe4d9171c[|[edc1f6c2-6d51-49d6-a47d-e8cbe4d9171c TO ee7eccd8-9613-4276-b1de-d80654a41b14[|[ee7eccd8-9613-4276-b1de-d80654a41b14 TO ef2297c3-760f-426f-b67d-4dc7ca4a06cb[|[ef2297c3-760f-426f-b67d-4dc7ca4a06cb TO efc39117-a073-4705-a460-a34dabcae682[|[efc39117-a073-4705-a460-a34dabcae682 TO f06a299e-cf58-446f-a684-56cae7557ac9[|[f06a299e-cf58-446f-a684-56cae7557ac9 TO f0fdc1c3-039f-4cb6-a662-f3bdd6357acd[|[f0fdc1c3-039f-4cb6-a662-f3bdd6357acd TO f1a96199-7866-4106-b095-4a8dbc7b04ed[|[f1a96199-7866-4106-b095-4a8dbc7b04ed TO f253b959-424a-4ac6-8924-df9a71bffc24[|[f253b959-424a-4ac6-8924-df9a71bffc24 TO f3003132-dc27-434f-bcc7-dfc356e52b01[|[f3003132-dc27-434f-bcc7-dfc356e52b01 TO f3bcdf02-1d10-4e8c-997e-b7ff38f30858[|[f3bcdf02-1d10-4e8c-997e-b7ff38f30858 TO f4716002-a78f-4221-b9bc-820700144c2c[|[f4716002-a78f-4221-b9bc-820700144c2c TO f51daf81-c014-4b03-af4f-f45139b5bbac[|[f51daf81-c014-4b03-af4f-f45139b5bbac TO f5b89f23-11d0-4d06-bcc4-e9ccd8f02752[|[f5b89f23-11d0-4d06-bcc4-e9ccd8f02752 TO f669316e-74d1-4a35-baed-8a4a1d6b9c51[|[f669316e-74d1-4a35-baed-8a4a1d6b9c51 TO f70e01bd-0751-4d19-9bb1-3d8313755dce[|[f70e01bd-0751-4d19-9bb1-3d8313755dce TO f79cba60-ba39-4afe-b706-46c4785f277b[|[f79cba60-ba39-4afe-b706-46c4785f277b TO f84d0927-ebea-4edc-a504-b6e4024e395f[|[f84d0927-ebea-4edc-a504-b6e4024e395f TO f8fc9383-7aec-4ed8-b89a-bdc8657c05c9[|[f8fc9383-7aec-4ed8-b89a-bdc8657c05c9 TO f98d0c73-8d6c-4e86-bab6-ec6b834e47ea[|[f98d0c73-8d6c-4e86-bab6-ec6b834e47ea TO fa349d6e-7d2f-417d-90bc-e82bd27e48d8[|[fa349d6e-7d2f-417d-90bc-e82bd27e48d8 TO fad58fad-a0a0-4f43-8477-a942ce7bedfd[|[fad58fad-a0a0-4f43-8477-a942ce7bedfd TO fb74b7dd-7526-4039-bb52-9b34fe5878c6[|[fb74b7dd-7526-4039-bb52-9b34fe5878c6 TO fc039fba-cd37-440f-9640-47d326f2acf7[|[fc039fba-cd37-440f-9640-47d326f2acf7 TO fc9bae09-c587-438b-9a30-1a13f0839b81[|[fc9bae09-c587-438b-9a30-1a13f0839b81 TO fd310498-23ea-4bf9-81f5-f34801378969[|[fd310498-23ea-4bf9-81f5-f34801378969 TO fddb77ec-233d-4999-90ba-ad4f9fdbe2c9[|[fddb77ec-233d-4999-90ba-ad4f9fdbe2c9 TO fe88b61a-cd2f-45a7-ab46-a0018a6588a6[|[fe88b61a-cd2f-45a7-ab46-a0018a6588a6 TO ff5e4aaf-4ab0-47e1-aac7-7dff11fed472[|[ff5e4aaf-4ab0-47e1-aac7-7dff11fed472 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "SM_UUID", 0, rangesAsString );
	}

	@Test
	public void startSplitting_cspp() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		
		ref.readIndexReference(keyspace, baseUUID, "mi1", "NOMINAL");
		String rangesAsString = "[min_lower_bound TO 6cts27lfugra[|[6cts27lfugra TO azhlsqg1517bazt58t[|[azhlsqg1517bazt58t TO eb4jz1cszeemlk4ahfjt[|[eb4jz1cszeemlk4ahfjt TO hhrqnvs17h[|[hhrqnvs17h TO kny2lbnkhjxlryy1[|[kny2lbnkhjxlryy1 TO nqhfsioxqwdzhxirpfvi[|[nqhfsioxqwdzhxirpfvi TO qtujf2dodlplrfvk[|[qtujf2dodlplrfvk TO ttvratl1g[|[ttvratl1g TO wy82n96wxiaa5[|[wy82n96wxiaa5 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "mi1", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "mi2", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 6dogkh0tts6imdjsy[|[6dogkh0tts6imdjsy TO b5zcua2fsp[|[b5zcua2fsp TO eeja1cdmv9[|[eeja1cdmv9 TO hg0m6rsupld[|[hg0m6rsupld TO kiwgebnqckol0[|[kiwgebnqckol0 TO nocp2if5jdxn[|[nocp2if5jdxn TO qspfdbli26[|[qspfdbli26 TO ts99xaalyfnf[|[ts99xaalyfnf TO wvwemt4utdn1vw[|[wvwemt4utdn1vw TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "mi2", 0, rangesAsString);		
		ref.readIndexReference(keyspace, baseUUID, "mi3", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 5sbdj0pn5sdg[|[5sbdj0pn5sdg TO aykjyylo[|[aykjyylo TO e5gzwdjrshce[|[e5gzwdjrshce TO hbpzm9[|[hbpzm9 TO k80uh[|[k80uh TO ncqdwnaysdta88[|[ncqdwnaysdta88 TO qf7am9yfeznlwci[|[qf7am9yfeznlwci TO tgaebnpsu3iacff5b[|[tgaebnpsu3iacff5b TO wtvvgfpi4uvfxu6[|[wtvvgfpi4uvfxu6 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "mi3", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "mi4", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 5y0ozaq8gqkcnf0ux[|[5y0ozaq8gqkcnf0ux TO b3l6n8lp9sezbvegl1bt[|[b3l6n8lp9sezbvegl1bt TO ee63v[|[ee63v TO hh7zvynm[|[hh7zvynm TO kkf2p6hrhocxnhizpu[|[kkf2p6hrhocxnhizpu TO nrs6bddswsxwi[|[nrs6bddswsxwi TO qt2qdcwgf[|[qt2qdcwgf TO txaduz5sq9u5ne[|[txaduz5sq9u5ne TO wxgd2d5usbvid9ugc[|[wxgd2d5usbvid9ugc TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "mi4", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "mi5", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 6igopbp4h[|[6igopbp4h TO bc0mkds[|[bc0mkds TO eib8nw[|[eib8nw TO hmpvrwhszyne[|[hmpvrwhszyne TO ko4gsmyknc9dhftwd7[|[ko4gsmyknc9dhftwd7 TO nr2psy32oigug[|[nr2psy32oigug TO qtumadvzxukyqqizw[|[qtumadvzxukyqqizw TO tvkyoy0rulptun6cel[|[tvkyoy0rulptun6cel TO wzfvtb68qniezsg5ae4[|[wzfvtb68qniezsg5ae4 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "mi5", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "iti", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 008d8d60-ded3-11e4-ab5b-005056bf32d7[|[008d8d60-ded3-11e4-ab5b-005056bf32d7 TO 00fb5030-dfcc-11e4-b3d1-005056bf53ee[|[00fb5030-dfcc-11e4-b3d1-005056bf53ee TO 018c2bd0-ddda-11e4-9d3e-005056bf0df0[|[018c2bd0-ddda-11e4-9d3e-005056bf0df0 TO 02455750-cfb0-11e4-9023-005056bf2081[|[02455750-cfb0-11e4-9023-005056bf2081 TO 02e66c90-d6f3-11e4-805e-005056bf53ee[|[02e66c90-d6f3-11e4-805e-005056bf53ee TO 0398fcb0-c7ea-11e4-8db1-005056bf5463[|[0398fcb0-c7ea-11e4-8db1-005056bf5463 TO 046d3ee0-c735-11e4-9625-005056bf53ee[|[046d3ee0-c735-11e4-9625-005056bf53ee TO 04fcede2-768e-4c38-91a1-38f0119bb18b[|[04fcede2-768e-4c38-91a1-38f0119bb18b TO 056aaab0-b66b-11e4-aa50-005056bf2081[|[056aaab0-b66b-11e4-aa50-005056bf2081 TO 060efbb0-cdfe-11e4-83ca-005056bf5463[|[060efbb0-cdfe-11e4-83ca-005056bf5463 TO 06a750e0-d70e-11e4-9348-005056bf6d47[|[06a750e0-d70e-11e4-9348-005056bf6d47 TO 075826c0-d1de-11e4-b13c-005056bf12e1[|[075826c0-d1de-11e4-b13c-005056bf12e1 TO 07c86c80-da28-11e4-a69d-005056bf6d47[|[07c86c80-da28-11e4-a69d-005056bf6d47 TO 0863dbd0-da6d-11e4-aea1-005056bf32d7[|[0863dbd0-da6d-11e4-aea1-005056bf32d7 TO 094b4000-c5b0-11e4-a036-005056bf32d7[|[094b4000-c5b0-11e4-a036-005056bf32d7 TO 09e4f150-d91f-11e4-9e7b-005056bf6d47[|[09e4f150-d91f-11e4-9e7b-005056bf6d47 TO 0a5fe7d0-d568-11e4-b0c3-005056bf5463[|[0a5fe7d0-d568-11e4-b0c3-005056bf5463 TO 0b1651e0-deb0-11e4-b527-005056bf5463[|[0b1651e0-deb0-11e4-b527-005056bf5463 TO 0bbe8c10-cdd6-11e4-85cf-005056bf12e1[|[0bbe8c10-cdd6-11e4-85cf-005056bf12e1 TO 0c634790-b61c-11e4-b771-005056bf6d47[|[0c634790-b61c-11e4-b771-005056bf6d47 TO 0d1416e0-da40-11e4-a245-005056bf5463[|[0d1416e0-da40-11e4-a245-005056bf5463 TO 0d9c1fc0-d718-11e4-9348-005056bf6d47[|[0d9c1fc0-d718-11e4-9348-005056bf6d47 TO 0e193ae0-b69e-11e4-af20-005056bf32d7[|[0e193ae0-b69e-11e4-af20-005056bf32d7 TO 0ea04200-cf04-11e4-b43c-005056bf53ee[|[0ea04200-cf04-11e4-b43c-005056bf53ee TO 0f293eb0-b777-11e4-97f9-005056bf5463[|[0f293eb0-b777-11e4-97f9-005056bf5463 TO 0fdc2320-d1d2-11e4-8ab3-005056bf1bac[|[0fdc2320-d1d2-11e4-8ab3-005056bf1bac TO 103f9e80-ddc8-11e4-92a8-005056bf53ee[|[103f9e80-ddc8-11e4-92a8-005056bf53ee TO 10e8b980-3777-4731-b01c-228180aea33a[|[10e8b980-3777-4731-b01c-228180aea33a TO 11bd9bf0-dd18-11e4-8c2f-005056bf2081[|[11bd9bf0-dd18-11e4-8c2f-005056bf2081 TO 120debf0-b3f1-11e4-b795-005056bf5463[|[120debf0-b3f1-11e4-b795-005056bf5463 TO 12c5ec00-d066-11e4-9337-005056bf53ee[|[12c5ec00-d066-11e4-9337-005056bf53ee TO 13669970-d764-11e4-902a-005056bf53ee[|[13669970-d764-11e4-902a-005056bf53ee TO 143bdc10-b7ad-11e4-881e-005056bf1bac[|[143bdc10-b7ad-11e4-881e-005056bf1bac TO 15362ab0-61c1-11e2-a54d-005056920243[|[15362ab0-61c1-11e2-a54d-005056920243 TO 15e0aff0-db2f-11e4-a1d5-005056bf53ee[|[15e0aff0-db2f-11e4-a1d5-005056bf53ee TO 16ae1410-c2af-11e4-9713-005056bf53ee[|[16ae1410-c2af-11e4-9713-005056bf53ee TO 17037a80-c535-11e4-bbce-005056bf5463[|[17037a80-c535-11e4-bbce-005056bf5463 TO 17a31fe0-d0fc-11e4-bcad-005056bf2081[|[17a31fe0-d0fc-11e4-bcad-005056bf2081 TO 183a5ae0-d00c-11e4-ae03-005056bf6d47[|[183a5ae0-d00c-11e4-ae03-005056bf6d47 TO 18c9b520-cad3-11e4-9762-005056bf12e1[|[18c9b520-cad3-11e4-9762-005056bf12e1 TO 196ef580-dfbe-11e4-8dde-005056bf32d7[|[196ef580-dfbe-11e4-8dde-005056bf32d7 TO 1a1b16b0-cb07-11e4-9c6a-005056bf5463[|[1a1b16b0-cb07-11e4-9c6a-005056bf5463 TO 1af87790-5df5-11e5-9fa4-005056bf4abb[|[1af87790-5df5-11e5-9fa4-005056bf4abb TO 1b83c3a0-b81d-11e4-8887-005056bf2081[|[1b83c3a0-b81d-11e4-8887-005056bf2081 TO 1c52b320-b86f-11e4-8887-005056bf2081[|[1c52b320-b86f-11e4-8887-005056bf2081 TO 1cc91000-cf36-11e4-b5e3-005056bf0df0[|[1cc91000-cf36-11e4-b5e3-005056bf0df0 TO 1dad0cd0-d849-11e4-ad6f-005056bf53ee[|[1dad0cd0-d849-11e4-ad6f-005056bf53ee TO 1e1ec3d0-d231-11e3-86d3-005056920241[|[1e1ec3d0-d231-11e3-86d3-005056920241 TO 1ed011d0-d7c1-11e4-b4f8-005056bf2081[|[1ed011d0-d7c1-11e4-b4f8-005056bf2081 TO 1f7144a0-d470-11e4-880c-005056bf12e1[|[1f7144a0-d470-11e4-880c-005056bf12e1 TO 2014ac70-dd77-11e4-9b7e-005056bf6d47[|[2014ac70-dd77-11e4-9b7e-005056bf6d47 TO 20d8e2e0-ce39-11e4-9c5a-005056bf0df0[|[20d8e2e0-ce39-11e4-9c5a-005056bf0df0 TO 219b51f0-cd5c-11e4-8fa6-005056bf2081[|[219b51f0-cd5c-11e4-8fa6-005056bf2081 TO 2233e4e0-d6d2-11e4-a996-005056bf0df0[|[2233e4e0-d6d2-11e4-a996-005056bf0df0 TO 23145240-ef73-11e5-b87a-005056bf4abb[|[23145240-ef73-11e5-b87a-005056bf4abb TO 23a39e50-d6ce-11e4-805e-005056bf53ee[|[23a39e50-d6ce-11e4-805e-005056bf53ee TO 249cab10-4e68-11e4-a482-005056bf2e59[|[249cab10-4e68-11e4-a482-005056bf2e59 TO 25161160-d35d-11e4-bfe6-005056bf2081[|[25161160-d35d-11e4-bfe6-005056bf2081 TO 25bdb920-64d1-11e3-803e-005056b20326[|[25bdb920-64d1-11e3-803e-005056b20326 TO 26a02070-c7af-11e4-a160-005056bf4abb[|[26a02070-c7af-11e4-a160-005056bf4abb TO 271bbd70-b927-11e4-886e-005056bf4abb[|[271bbd70-b927-11e4-886e-005056bf4abb TO 27bbd5d0-2f99-11e3-99e3-005056b20326[|[27bbd5d0-2f99-11e3-99e3-005056b20326 TO 287bb580-c502-11e4-a2f7-005056bf53ee[|[287bb580-c502-11e4-a2f7-005056bf53ee TO 293877e0-cdec-11e4-9489-005056bf32d7[|[293877e0-cdec-11e4-9489-005056bf32d7 TO 29e3a370-b7dc-11e4-ae1e-005056bf0df0[|[29e3a370-b7dc-11e4-ae1e-005056bf0df0 TO 2ac87780-c5b9-11e4-937b-005056bf4abb[|[2ac87780-c5b9-11e4-937b-005056bf4abb TO 2b82b420-5c85-11e5-b4bd-005056bf53ee[|[2b82b420-5c85-11e5-b4bd-005056bf53ee TO 2c3977a0-ddaa-11e4-84d4-005056bf5463[|[2c3977a0-ddaa-11e4-84d4-005056bf5463 TO 2ceb2e10-ca67-11e4-ac9f-005056bf12e1[|[2ceb2e10-ca67-11e4-ac9f-005056bf12e1 TO 2d9d3820-c455-11e4-9a8f-005056bf4abb[|[2d9d3820-c455-11e4-9a8f-005056bf4abb TO 2e5adf90-b66f-11e4-b7de-005056bf4abb[|[2e5adf90-b66f-11e4-b7de-005056bf4abb TO 2f18cb80-d533-11e4-b0c3-005056bf5463[|[2f18cb80-d533-11e4-b0c3-005056bf5463 TO 2fbf9670-b771-11e4-97f9-005056bf5463[|[2fbf9670-b771-11e4-97f9-005056bf5463 TO 306e2280-da3a-11e4-b68d-005056bf0df0[|[306e2280-da3a-11e4-b68d-005056bf0df0 TO 30f9c2a0-dfca-11e4-b3d1-005056bf53ee[|[30f9c2a0-dfca-11e4-b3d1-005056bf53ee TO 321ca8e0-c78e-11e4-bba1-005056bf1bac[|[321ca8e0-c78e-11e4-bba1-005056bf1bac TO 32a639d0-d769-11e4-a575-005056bf6d47[|[32a639d0-d769-11e4-a575-005056bf6d47 TO 339fe2b0-de25-11e4-92a8-005056bf53ee[|[339fe2b0-de25-11e4-92a8-005056bf53ee TO 342b0f50-cf09-11e4-8484-005056bf2081[|[342b0f50-cf09-11e4-8484-005056bf2081 TO 34990a00-b6b8-11e4-aa50-005056bf2081[|[34990a00-b6b8-11e4-aa50-005056bf2081 TO 352f8ff0-d1db-11e4-b13c-005056bf12e1[|[352f8ff0-d1db-11e4-b13c-005056bf12e1 TO 359d0bb0-cf52-11e4-b43c-005056bf53ee[|[359d0bb0-cf52-11e4-b43c-005056bf53ee TO 367b7280-c7b7-11e4-8112-005056bf2081[|[367b7280-c7b7-11e4-8112-005056bf2081 TO 36eb1480-d8df-11e4-b3ea-005056bf2081[|[36eb1480-d8df-11e4-b3ea-005056bf2081 TO 37888320-df18-11e4-a5fc-005056bf0df0[|[37888320-df18-11e4-a5fc-005056bf0df0 TO 3854d410-d4b5-11e4-b5c0-005056bf53ee[|[3854d410-d4b5-11e4-b5c0-005056bf53ee TO 38b0f400-81ed-11e3-bceb-005056920242[|[38b0f400-81ed-11e3-bceb-005056920242 TO 393319c0-d4ef-11e4-ab0f-005056bf53ee[|[393319c0-d4ef-11e4-ab0f-005056bf53ee TO 3a51c180-c7fc-11e4-a1d6-005056bf53ee[|[3a51c180-c7fc-11e4-a1d6-005056bf53ee TO 3acfe0b0-b66d-11e4-8689-005056bf0df0[|[3acfe0b0-b66d-11e4-8689-005056bf0df0 TO 3b5aa440-b65c-11e4-9b46-005056bf6d47[|[3b5aa440-b65c-11e4-9b46-005056bf6d47 TO 3c46da30-b8e7-11e4-886e-005056bf4abb[|[3c46da30-b8e7-11e4-886e-005056bf4abb TO 3d12f7b0-c999-11e4-941c-005056bf5463[|[3d12f7b0-c999-11e4-941c-005056bf5463 TO 3db1dd80-d098-11e3-9ae6-005056920242[|[3db1dd80-d098-11e3-9ae6-005056920242 TO 3e7e3bc0-c49c-11e4-a7d4-005056bf6d47[|[3e7e3bc0-c49c-11e4-a7d4-005056bf6d47 TO 3f3e0270-d207-11e4-b23d-005056bf6d47[|[3f3e0270-d207-11e4-b23d-005056bf6d47 TO 3feea1e0-b9aa-11e4-a08e-005056bf2081[|[3feea1e0-b9aa-11e4-a08e-005056bf2081 TO 40c6bd30-cf05-11e4-829e-005056bf6d47[|[40c6bd30-cf05-11e4-829e-005056bf6d47 TO 41808240-d321-11e4-9535-005056bf0df0[|[41808240-d321-11e4-9535-005056bf0df0 TO 421a23d0-da6c-11e4-ba27-005056bf1bac[|[421a23d0-da6c-11e4-ba27-005056bf1bac TO 42dcd9f0-d5be-11e4-b2ff-005056bf5463[|[42dcd9f0-d5be-11e4-b2ff-005056bf5463 TO 43a79330-b952-11e4-b88f-005056bf5463[|[43a79330-b952-11e4-b88f-005056bf5463 TO 443b76c0-d164-11e4-b83e-005056bf5463[|[443b76c0-d164-11e4-b83e-005056bf5463 TO 45001bf0-4606-11e3-a01b-005056bf2e5e[|[45001bf0-4606-11e3-a01b-005056bf2e5e TO 45b3edf0-dfb2-11e4-92bf-005056bf1bac[|[45b3edf0-dfb2-11e4-92bf-005056bf1bac TO 4674be50-d46c-11e4-82d8-005056bf1bac[|[4674be50-d46c-11e4-82d8-005056bf1bac TO 471f6180-dec5-11e4-96c2-005056bf4abb[|[471f6180-dec5-11e4-96c2-005056bf4abb TO 47bf32d0-d373-11e4-b1a8-005056bf0df0[|[47bf32d0-d373-11e4-b1a8-005056bf0df0 TO 48471fc0-da7f-11e4-988b-005056bf2081[|[48471fc0-da7f-11e4-988b-005056bf2081 TO 48f68b60-d1f4-11e4-8ae2-005056bf2081[|[48f68b60-d1f4-11e4-8ae2-005056bf2081 TO 49e23da0-ddaa-11e4-93f1-005056bf12e1[|[49e23da0-ddaa-11e4-93f1-005056bf12e1 TO 4a9d49a0-d95f-11e4-af18-005056bf12e1[|[4a9d49a0-d95f-11e4-af18-005056bf12e1 TO 4b4491f0-e009-11e4-9bed-005056bf6d47[|[4b4491f0-e009-11e4-9bed-005056bf6d47 TO 4c0c9f50-d5b5-11e4-b2ff-005056bf5463[|[4c0c9f50-d5b5-11e4-b2ff-005056bf5463 TO 4cb000f0-c3fe-11e4-bbf0-005056bf12e1[|[4cb000f0-c3fe-11e4-bbf0-005056bf12e1 TO 4dd7fef0-c4ea-11e4-a423-005056bf12e1[|[4dd7fef0-c4ea-11e4-a423-005056bf12e1 TO 4e97dce0-c6f5-11e4-a61e-005056bf4abb[|[4e97dce0-c6f5-11e4-a61e-005056bf4abb TO 4f65f090-d5c2-11e4-b2ff-005056bf5463[|[4f65f090-d5c2-11e4-b2ff-005056bf5463 TO 4fe31140-d7ae-11e4-9e1e-005056bf5463[|[4fe31140-d7ae-11e4-9e1e-005056bf5463 TO 5097c010-cfff-11e4-a429-005056bf53ee[|[5097c010-cfff-11e4-a429-005056bf53ee TO 5135fae0-d9ce-11e4-b68d-005056bf0df0[|[5135fae0-d9ce-11e4-b68d-005056bf0df0 TO 51cb58e0-d011-11e4-ae03-005056bf6d47[|[51cb58e0-d011-11e4-ae03-005056bf6d47 TO 526880e0-b773-11e4-afb2-005056bf32d7[|[526880e0-b773-11e4-afb2-005056bf32d7 TO 52ee2010-d73a-11e4-827b-005056bf32d7[|[52ee2010-d73a-11e4-827b-005056bf32d7 TO 53dcb680-cf7e-11e4-9d59-005056bf4abb[|[53dcb680-cf7e-11e4-9d59-005056bf4abb TO 54ab23f0-d148-11e4-b83e-005056bf5463[|[54ab23f0-d148-11e4-b83e-005056bf5463 TO 55736940-d185-11e4-b913-005056bf1bac[|[55736940-d185-11e4-b913-005056bf1bac TO 563dd932-ba1d-4ac3-b7e6-9bf0935cf3d2[|[563dd932-ba1d-4ac3-b7e6-9bf0935cf3d2 TO 56a8ade0-d507-11e4-8051-005056bf1bac[|[56a8ade0-d507-11e4-8051-005056bf1bac TO 578b89c0-c522-11e4-88c5-005056bf5463[|[578b89c0-c522-11e4-88c5-005056bf5463 TO 58759350-d583-11e4-8051-005056bf1bac[|[58759350-d583-11e4-8051-005056bf1bac TO 59153300-b6a8-11e4-b4e2-005056bf5463[|[59153300-b6a8-11e4-b4e2-005056bf5463 TO 599f96f0-d36b-11e4-90e2-005056bf1bac[|[599f96f0-d36b-11e4-90e2-005056bf1bac TO 5a4ad3f0-c85b-11e4-9dd7-005056bf4abb[|[5a4ad3f0-c85b-11e4-9dd7-005056bf4abb TO 5b3b7f60-d0c8-11e4-9410-005056bf0df0[|[5b3b7f60-d0c8-11e4-9410-005056bf0df0 TO 5baf3aa0-b6af-11e4-9140-005056bf53ee[|[5baf3aa0-b6af-11e4-9140-005056bf53ee TO 5c73da90-c8c0-11e4-8a56-005056bf5463[|[5c73da90-c8c0-11e4-8a56-005056bf5463 TO 5d228210-cf0b-11e4-bab9-005056bf12e1[|[5d228210-cf0b-11e4-bab9-005056bf12e1 TO 5dd3f400-d85c-11e4-8dc6-005056bf0df0[|[5dd3f400-d85c-11e4-8dc6-005056bf0df0 TO 5e5b6870-c882-11e4-a0dc-005056bf2081[|[5e5b6870-c882-11e4-a0dc-005056bf2081 TO 5f3e62c0-b6d9-11e4-9140-005056bf53ee[|[5f3e62c0-b6d9-11e4-9140-005056bf53ee TO 5fcc30e0-c42c-11e4-bbf0-005056bf12e1[|[5fcc30e0-c42c-11e4-bbf0-005056bf12e1 TO 6066fed0-cd83-11e4-beb9-005056bf1bac[|[6066fed0-cd83-11e4-beb9-005056bf1bac TO 614aa860-d102-11e4-a737-005056bf4abb[|[614aa860-d102-11e4-a737-005056bf4abb TO 62152990-ded2-11e4-96c2-005056bf4abb[|[62152990-ded2-11e4-96c2-005056bf4abb TO 62898fe0-d506-11e4-ab0f-005056bf53ee[|[62898fe0-d506-11e4-ab0f-005056bf53ee TO 62d96770-b60a-11e3-bc8a-005056920242[|[62d96770-b60a-11e3-bc8a-005056920242 TO 63796830-cac0-11e4-9528-005056bf6d47[|[63796830-cac0-11e4-9528-005056bf6d47 TO 643812b0-df58-11e4-8dde-005056bf32d7[|[643812b0-df58-11e4-8dde-005056bf32d7 TO 64e9e8a0-b86b-11e4-aec4-005056bf1bac[|[64e9e8a0-b86b-11e4-aec4-005056bf1bac TO 656c30f0-d1e6-11e4-adf5-005056bf53ee[|[656c30f0-d1e6-11e4-adf5-005056bf53ee TO 65ea7680-6451-11e3-a08e-005056bf2e59[|[65ea7680-6451-11e3-a08e-005056bf2e59 TO 668f1600-d6dc-11e4-9348-005056bf6d47[|[668f1600-d6dc-11e4-9348-005056bf6d47 TO 673551f0-c421-11e4-a03a-005056bf53ee[|[673551f0-c421-11e4-a03a-005056bf53ee TO 67da0100-d475-11e4-8ea3-005056bf5463[|[67da0100-d475-11e4-8ea3-005056bf5463 TO 68730bf0-dda5-11e4-be10-005056bf6d47[|[68730bf0-dda5-11e4-be10-005056bf6d47 TO 69184510-b8e4-11e4-886e-005056bf4abb[|[69184510-b8e4-11e4-886e-005056bf4abb TO 69dd2b70-d890-11e4-b597-005056bf5463[|[69dd2b70-d890-11e4-b597-005056bf5463 TO 6a7f5d40-d19d-11e4-bcad-005056bf2081[|[6a7f5d40-d19d-11e4-bcad-005056bf2081 TO 6b43c3f0-b85d-11e4-af50-005056bf5463[|[6b43c3f0-b85d-11e4-af50-005056bf5463 TO 6c0ad170-b781-11e4-ad9a-005056bf2081[|[6c0ad170-b781-11e4-ad9a-005056bf2081 TO 6cd201b0-c6da-11e4-a61e-005056bf4abb[|[6cd201b0-c6da-11e4-a61e-005056bf4abb TO 6d63dd50-d9a5-11e4-ac04-005056bf0df0[|[6d63dd50-d9a5-11e4-ac04-005056bf0df0 TO 6e00f9b0-ce0a-11e4-9f23-005056bf53ee[|[6e00f9b0-ce0a-11e4-9f23-005056bf53ee TO 6eb7e0c0-c82f-11e4-8db1-005056bf5463[|[6eb7e0c0-c82f-11e4-8db1-005056bf5463 TO 6f52d170-3b5e-11e3-abd5-005056bf2e5b[|[6f52d170-3b5e-11e3-abd5-005056bf2e5b TO 6fdf2ac0-c713-11e4-9625-005056bf53ee[|[6fdf2ac0-c713-11e4-9625-005056bf53ee TO 708d3b50-b83c-11e4-af50-005056bf5463[|[708d3b50-b83c-11e4-af50-005056bf5463 TO 713263a0-db28-11e4-a504-005056bf12e1[|[713263a0-db28-11e4-a504-005056bf12e1 TO 721c6f30-5f98-11e2-9354-005056920241[|[721c6f30-5f98-11e2-9354-005056920241 TO 72a1cd40-d9d7-11e4-b68d-005056bf0df0[|[72a1cd40-d9d7-11e4-b68d-005056bf0df0 TO 733b4710-dae8-11e4-8f14-005056bf5463[|[733b4710-dae8-11e4-8f14-005056bf5463 TO 73d853b0-de04-11e4-9105-005056bf4abb[|[73d853b0-de04-11e4-9105-005056bf4abb TO 74b8f1d0-ded7-11e4-93f7-005056bf53ee[|[74b8f1d0-ded7-11e4-93f7-005056bf53ee TO 75632150-c5b9-11e4-bbce-005056bf5463[|[75632150-c5b9-11e4-bbce-005056bf5463 TO 75d37a80-c565-11e4-9099-005056bf12e1[|[75d37a80-c565-11e4-9099-005056bf12e1 TO 7663e840-dc9e-11e4-b856-005056bf5463[|[7663e840-dc9e-11e4-b856-005056bf5463 TO 774a7c40-d0ac-11e4-9fc0-005056bf1bac[|[774a7c40-d0ac-11e4-9fc0-005056bf1bac TO 7803ce10-b82a-11e4-ae1e-005056bf0df0[|[7803ce10-b82a-11e4-ae1e-005056bf0df0 TO 7845db40-ca61-11e4-86ba-005056bf32d7[|[7845db40-ca61-11e4-86ba-005056bf32d7 TO 78d31fe0-d9f3-11e4-9151-005056bf12e1[|[78d31fe0-d9f3-11e4-9151-005056bf12e1 TO 799de1f0-d569-11e4-8adc-005056bf6d47[|[799de1f0-d569-11e4-8adc-005056bf6d47 TO 7a38e510-c434-11e4-b830-005056bf5463[|[7a38e510-c434-11e4-b830-005056bf5463 TO 7ade9510-d8e7-11e4-b3ea-005056bf2081[|[7ade9510-d8e7-11e4-b3ea-005056bf2081 TO 7b9b2000-b645-11e4-b7de-005056bf4abb[|[7b9b2000-b645-11e4-b7de-005056bf4abb TO 7c3b2740-df1b-11e4-92bf-005056bf1bac[|[7c3b2740-df1b-11e4-92bf-005056bf1bac TO 7d1bc2f0-c814-11e4-a1d6-005056bf53ee[|[7d1bc2f0-c814-11e4-a1d6-005056bf53ee TO 7dab6a30-d585-11e4-b820-005056bf12e1[|[7dab6a30-d585-11e4-b820-005056bf12e1 TO 7e825920-c435-11e4-a191-005056bf2081[|[7e825920-c435-11e4-a191-005056bf2081 TO 7f5fd780-d5b2-11e4-b23e-005056bf32d7[|[7f5fd780-d5b2-11e4-b23e-005056bf32d7 TO 802903d0-d5c6-11e4-84c2-005056bf2081[|[802903d0-d5c6-11e4-84c2-005056bf2081 TO 80c808f0-b838-11e4-ba8a-005056bf12e1[|[80c808f0-b838-11e4-ba8a-005056bf12e1 TO 8130b780-c82b-11e4-a160-005056bf4abb[|[8130b780-c82b-11e4-a160-005056bf4abb TO 81bf1010-b6ac-11e4-9b46-005056bf6d47[|[81bf1010-b6ac-11e4-9b46-005056bf6d47 TO 82849760-d031-11e4-9410-005056bf0df0[|[82849760-d031-11e4-9410-005056bf0df0 TO 835d7520-dcbc-11e4-83b0-005056bf1bac[|[835d7520-dcbc-11e4-83b0-005056bf1bac TO 840efa70-d46f-11e4-880c-005056bf12e1[|[840efa70-d46f-11e4-880c-005056bf12e1 TO 84df32d0-de1f-11e4-84d4-005056bf5463[|[84df32d0-de1f-11e4-84d4-005056bf5463 TO 857428b0-b834-11e4-aec4-005056bf1bac[|[857428b0-b834-11e4-aec4-005056bf1bac TO 86609650-b749-11e4-afb2-005056bf32d7[|[86609650-b749-11e4-afb2-005056bf32d7 TO 86ef38b0-d5bc-11e4-8ff2-005056bf53ee[|[86ef38b0-d5bc-11e4-8ff2-005056bf53ee TO 87abcb80-ddcb-11e4-be10-005056bf6d47[|[87abcb80-ddcb-11e4-be10-005056bf6d47 TO 88500b90-ce45-11e4-9489-005056bf32d7[|[88500b90-ce45-11e4-9489-005056bf32d7 TO 89148cf0-6455-11e3-b775-005056920242[|[89148cf0-6455-11e3-b775-005056920242 TO 89a99e90-ce39-11e4-bbf9-005056bf1bac[|[89a99e90-ce39-11e4-bbf9-005056bf1bac TO 8a344fc0-d5f7-11e4-b23e-005056bf32d7[|[8a344fc0-d5f7-11e4-b23e-005056bf32d7 TO 8af1a7d0-c8bb-11e4-a0dc-005056bf2081[|[8af1a7d0-c8bb-11e4-a0dc-005056bf2081 TO 8b7681d0-c753-11e4-a61e-005056bf4abb[|[8b7681d0-c753-11e4-a61e-005056bf4abb TO 8c47f400-5d2f-11e5-bce7-005056bf4abb[|[8c47f400-5d2f-11e5-bce7-005056bf4abb TO 8ce73bf0-d12f-11e4-b83e-005056bf5463[|[8ce73bf0-d12f-11e4-b83e-005056bf5463 TO 8d583e20-d5cd-11e4-84c2-005056bf2081[|[8d583e20-d5cd-11e4-84c2-005056bf2081 TO 8de415b0-c76f-11e4-a3cf-005056bf5463[|[8de415b0-c76f-11e4-a3cf-005056bf5463 TO 8ea1c7c0-dd65-11e4-b0bc-005056bf12e1[|[8ea1c7c0-dd65-11e4-b0bc-005056bf12e1 TO 8f474440-c724-11e4-a3cf-005056bf5463[|[8f474440-c724-11e4-a3cf-005056bf5463 TO 8fe31b80-d4cc-11e4-b725-005056bf6d47[|[8fe31b80-d4cc-11e4-b725-005056bf6d47 TO 909770c0-d563-11e4-b0c3-005056bf5463[|[909770c0-d563-11e4-b0c3-005056bf5463 TO 91484e70-d498-11e4-b725-005056bf6d47[|[91484e70-d498-11e4-b725-005056bf6d47 TO 91dd2f30-c4a4-11e4-9efb-005056bf1bac[|[91dd2f30-c4a4-11e4-9efb-005056bf1bac TO 9254c420-d492-11e4-b894-005056bf0df0[|[9254c420-d492-11e4-b894-005056bf0df0 TO 9302d590-c738-11e4-b291-005056bf0df0[|[9302d590-c738-11e4-b291-005056bf0df0 TO 9375b150-da20-11e4-9f46-005056bf2081[|[9375b150-da20-11e4-9f46-005056bf2081 TO 9428f660-deef-11e4-b0ac-005056bf6d47[|[9428f660-deef-11e4-b0ac-005056bf6d47 TO 94a3bdc0-d237-11e3-86d3-005056920241[|[94a3bdc0-d237-11e3-86d3-005056920241 TO 95256b80-cd9b-11e4-ba3e-005056bf4abb[|[95256b80-cd9b-11e4-ba3e-005056bf4abb TO 95c7cc40-cf2b-11e4-b5e3-005056bf0df0[|[95c7cc40-cf2b-11e4-b5e3-005056bf0df0 TO 967d1000-da43-11e4-ab25-005056bf4abb[|[967d1000-da43-11e4-ab25-005056bf4abb TO 96f00160-dd49-11e4-83b0-005056bf1bac[|[96f00160-dd49-11e4-83b0-005056bf1bac TO 979c1960-d9e7-11e4-ab25-005056bf4abb[|[979c1960-d9e7-11e4-ab25-005056bf4abb TO 98279ed0-c944-11e4-b7e4-005056bf0df0[|[98279ed0-c944-11e4-b7e4-005056bf0df0 TO 990dec40-dd31-11e4-b0bc-005056bf12e1[|[990dec40-dd31-11e4-b0bc-005056bf12e1 TO 99d1e110-d384-11e4-b1a8-005056bf0df0[|[99d1e110-d384-11e4-b1a8-005056bf0df0 TO 9a4dc8d0-dfae-11e4-9b6f-005056bf2081[|[9a4dc8d0-dfae-11e4-9b6f-005056bf2081 TO 9ad93ea0-d758-11e4-909c-005056bf0df0[|[9ad93ea0-d758-11e4-909c-005056bf0df0 TO 9bbfcd10-c8bd-11e4-bdea-005056bf12e1[|[9bbfcd10-c8bd-11e4-bdea-005056bf12e1 TO 9c52f6a0-df09-11e4-b0ac-005056bf6d47[|[9c52f6a0-df09-11e4-b0ac-005056bf6d47 TO 9cf02ee0-cee0-11e4-b5e4-005056bf4abb[|[9cf02ee0-cee0-11e4-b5e4-005056bf4abb TO 9dc63d00-bd43-11e3-b308-005056bf2e5e[|[9dc63d00-bd43-11e3-b308-005056bf2e5e TO 9e6f2830-b3cd-11e4-9e8d-005056bf2081[|[9e6f2830-b3cd-11e4-9e8d-005056bf2081 TO 9ec9fc50-cefe-11e4-b5e3-005056bf0df0[|[9ec9fc50-cefe-11e4-b5e3-005056bf0df0 TO 9f5dd5c0-df42-11e4-8cca-005056bf4abb[|[9f5dd5c0-df42-11e4-8cca-005056bf4abb TO 9ff26bd0-bd43-11e3-a8cf-005056bf2e59[|[9ff26bd0-bd43-11e3-a8cf-005056bf2e59 TO a09c2a60-cfa5-11e4-ae03-005056bf6d47[|[a09c2a60-cfa5-11e4-ae03-005056bf6d47 TO a1266f90-d725-11e4-a996-005056bf0df0[|[a1266f90-d725-11e4-a996-005056bf0df0 TO a19eaa10-cf54-11e4-8484-005056bf2081[|[a19eaa10-cf54-11e4-8484-005056bf2081 TO a223d560-cdf0-11e4-9489-005056bf32d7[|[a223d560-cdf0-11e4-9489-005056bf32d7 TO a2a0d370-ef4c-11e5-a2cd-005056bf2081[|[a2a0d370-ef4c-11e5-a2cd-005056bf2081 TO a30f1a20-c729-11e4-a2e2-005056bf32d7[|[a30f1a20-c729-11e4-a2e2-005056bf32d7 TO a3b54e60-d591-11e4-ab0f-005056bf53ee[|[a3b54e60-d591-11e4-ab0f-005056bf53ee TO a46d6e60-c578-11e4-a036-005056bf32d7[|[a46d6e60-c578-11e4-a036-005056bf32d7 TO a5450ce0-d1b1-11e4-b913-005056bf1bac[|[a5450ce0-d1b1-11e4-b913-005056bf1bac TO a616f440-d9f3-11e4-aef4-005056bf1bac[|[a616f440-d9f3-11e4-aef4-005056bf1bac TO a6b82a60-d03c-11e4-b7a2-005056bf6d47[|[a6b82a60-d03c-11e4-b7a2-005056bf6d47 TO a7876550-c588-11e4-9099-005056bf12e1[|[a7876550-c588-11e4-9099-005056bf12e1 TO a809acc0-e9bb-11e4-83dd-005056bf1bac[|[a809acc0-e9bb-11e4-83dd-005056bf1bac TO a8af6c30-dd92-11e4-be10-005056bf6d47[|[a8af6c30-dd92-11e4-be10-005056bf6d47 TO a95aef10-c4d3-11e4-bfc2-005056bf32d7[|[a95aef10-c4d3-11e4-bfc2-005056bf32d7 TO a9e21f90-d46d-11e4-b5c0-005056bf53ee[|[a9e21f90-d46d-11e4-b5c0-005056bf53ee TO aa90b720-b605-11e4-b771-005056bf6d47[|[aa90b720-b605-11e4-b771-005056bf6d47 TO ab4619b0-da7e-11e4-b8e6-005056bf53ee[|[ab4619b0-da7e-11e4-b8e6-005056bf53ee TO abcab470-d517-11e4-ab0f-005056bf53ee[|[abcab470-d517-11e4-ab0f-005056bf53ee TO ac480760-d920-11e4-b3ea-005056bf2081[|[ac480760-d920-11e4-b3ea-005056bf2081 TO ad0986e0-d855-11e4-b053-005056bf2081[|[ad0986e0-d855-11e4-b053-005056bf2081 TO adc8a9fa-b76c-4697-88fc-2fb5eeed682d[|[adc8a9fa-b76c-4697-88fc-2fb5eeed682d TO ae545490-c779-11e4-a2e2-005056bf32d7[|[ae545490-c779-11e4-a2e2-005056bf32d7 TO af04c3b0-de81-11e4-bd18-005056bf2081[|[af04c3b0-de81-11e4-bd18-005056bf2081 TO af8b0370-b85d-11e4-aec4-005056bf1bac[|[af8b0370-b85d-11e4-aec4-005056bf1bac TO b04b18d0-b791-11e4-9e52-005056bf0df0[|[b04b18d0-b791-11e4-9e52-005056bf0df0 TO b0e048c0-d04f-11e4-9337-005056bf53ee[|[b0e048c0-d04f-11e4-9337-005056bf53ee TO b19610f0-c90c-11e4-a0dc-005056bf2081[|[b19610f0-c90c-11e4-a0dc-005056bf2081 TO b2737fb0-b6e4-11e4-af20-005056bf32d7[|[b2737fb0-b6e4-11e4-af20-005056bf32d7 TO b30994f0-d7f0-11e4-95ca-005056bf12e1[|[b30994f0-d7f0-11e4-95ca-005056bf12e1 TO b3db82b0-ca41-11e4-8c84-005056bf53ee[|[b3db82b0-ca41-11e4-8c84-005056bf53ee TO b4750430-c77b-11e4-85b7-005056bf12e1[|[b4750430-c77b-11e4-85b7-005056bf12e1 TO b516ab90-ce43-11e4-89f3-005056bf4abb[|[b516ab90-ce43-11e4-89f3-005056bf4abb TO b5eb1060-c6e1-11e4-bec4-005056bf1bac[|[b5eb1060-c6e1-11e4-bec4-005056bf1bac TO b6a0e870-b923-11e4-b88f-005056bf5463[|[b6a0e870-b923-11e4-b88f-005056bf5463 TO b78de2b0-10bd-11e4-bb51-005056bf2e5e[|[b78de2b0-10bd-11e4-bb51-005056bf2e5e TO b849e730-f027-11e5-a655-005056bf4abb[|[b849e730-f027-11e5-a655-005056bf4abb TO b8b8ef00-8822-11e3-adfb-005056920243[|[b8b8ef00-8822-11e3-adfb-005056920243 TO b948ca40-b36d-11e4-a6f3-005056bf12e1[|[b948ca40-b36d-11e4-a6f3-005056bf12e1 TO ba19fb50-d119-11e4-9bd6-005056bf6d47[|[ba19fb50-d119-11e4-9bd6-005056bf6d47 TO baf20150-c1ae-11e4-8665-005056bf32d7[|[baf20150-c1ae-11e4-8665-005056bf32d7 TO bbaa9cc0-c6ad-11e4-b563-005056bf4abb[|[bbaa9cc0-c6ad-11e4-b563-005056bf4abb TO bc49b920-d857-11e4-8539-005056bf12e1[|[bc49b920-d857-11e4-8539-005056bf12e1 TO bcf31410-db6e-11e4-a451-005056bf4abb[|[bcf31410-db6e-11e4-a451-005056bf4abb TO bda338b0-d996-11e4-8e35-005056bf12e1[|[bda338b0-d996-11e4-8e35-005056bf12e1 TO be2ef410-c283-11e4-8842-005056bf6d47[|[be2ef410-c283-11e4-8842-005056bf6d47 TO beea9e30-d5a3-11e4-8051-005056bf1bac[|[beea9e30-d5a3-11e4-8051-005056bf1bac TO bf9a2d20-cf16-11e4-938c-005056bf32d7[|[bf9a2d20-cf16-11e4-938c-005056bf32d7 TO c04e0b50-c959-11e4-b12a-005056bf6d47[|[c04e0b50-c959-11e4-b12a-005056bf6d47 TO c1333990-c748-11e4-aae2-005056bf2081[|[c1333990-c748-11e4-aae2-005056bf2081 TO c20202a0-dd3e-11e4-b9df-005056bf0df0[|[c20202a0-dd3e-11e4-b9df-005056bf0df0 TO c2b13300-c935-11e4-bf9c-005056bf2081[|[c2b13300-c935-11e4-bf9c-005056bf2081 TO c39d10f0-d94c-11e4-b3ea-005056bf2081[|[c39d10f0-d94c-11e4-b3ea-005056bf2081 TO c47ba390-c5e4-11e4-9099-005056bf12e1[|[c47ba390-c5e4-11e4-9099-005056bf12e1 TO c5142040-323a-11e4-a7d9-005056bf2e59[|[c5142040-323a-11e4-a7d9-005056bf2e59 TO c5d99960-d75d-11e4-a575-005056bf6d47[|[c5d99960-d75d-11e4-a575-005056bf6d47 TO c6ac39c0-5df3-11e5-9ede-005056bf6d47[|[c6ac39c0-5df3-11e5-9ede-005056bf6d47 TO c75b3bae-7344-40cb-9c53-3d4e4347425e[|[c75b3bae-7344-40cb-9c53-3d4e4347425e TO c808ef90-caf3-11e4-9513-005056bf32d7[|[c808ef90-caf3-11e4-9513-005056bf32d7 TO c8bc1fa0-b83f-11e4-ba6b-005056bf53ee[|[c8bc1fa0-b83f-11e4-ba6b-005056bf53ee TO c9559ef0-d527-11e4-b0c3-005056bf5463[|[c9559ef0-d527-11e4-b0c3-005056bf5463 TO c9d57960-b9b4-11e4-a08e-005056bf2081[|[c9d57960-b9b4-11e4-a08e-005056bf2081 TO caa2b510-ca76-11e4-93ee-005056bf1bac[|[caa2b510-ca76-11e4-93ee-005056bf1bac TO cb3827c0-c5be-11e3-bd99-005056920243[|[cb3827c0-c5be-11e3-bd99-005056920243 TO cbcdd480-d6e0-11e4-9027-005056bf12e1[|[cbcdd480-d6e0-11e4-9027-005056bf12e1 TO cc778480-df50-11e4-b036-005056bf5463[|[cc778480-df50-11e4-b036-005056bf5463 TO cd06c4b0-de6a-11e4-b527-005056bf5463[|[cd06c4b0-de6a-11e4-b527-005056bf5463 TO cda78130-c442-11e4-9a8f-005056bf4abb[|[cda78130-c442-11e4-9a8f-005056bf4abb TO ce6fc880-df44-11e4-92bf-005056bf1bac[|[ce6fc880-df44-11e4-92bf-005056bf1bac TO cf213060-ca0a-11e4-9de0-005056bf2081[|[cf213060-ca0a-11e4-9de0-005056bf2081 TO cfb36d90-c802-11e4-8db1-005056bf5463[|[cfb36d90-c802-11e4-8db1-005056bf5463 TO d0b7a3b0-b6d6-11e4-825b-005056bf1bac[|[d0b7a3b0-b6d6-11e4-825b-005056bf1bac TO d156bee0-d97d-11e4-a1fa-005056bf32d7[|[d156bee0-d97d-11e4-a1fa-005056bf32d7 TO d22aac60-b66d-11e4-8689-005056bf0df0[|[d22aac60-b66d-11e4-8689-005056bf0df0 TO d2aa9730-6485-11e3-a2d3-005056920242[|[d2aa9730-6485-11e3-a2d3-005056920242 TO d31111c0-87d2-11e3-adfb-005056920243[|[d31111c0-87d2-11e3-adfb-005056920243 TO d3b022c0-d97e-11e4-af18-005056bf12e1[|[d3b022c0-d97e-11e4-af18-005056bf12e1 TO d49073e0-cefa-11e4-8484-005056bf2081[|[d49073e0-cefa-11e4-8484-005056bf2081 TO d5272d90-c652-11e4-bfb8-005056bf53ee[|[d5272d90-c652-11e4-bfb8-005056bf53ee TO d5af20e0-caf5-11e4-980b-005056bf4abb[|[d5af20e0-caf5-11e4-980b-005056bf4abb TO d6172dd0-dd46-11e4-b0bc-005056bf12e1[|[d6172dd0-dd46-11e4-b0bc-005056bf12e1 TO d6a3e780-c61b-11e4-bfb8-005056bf53ee[|[d6a3e780-c61b-11e4-bfb8-005056bf53ee TO d7608d70-d806-11e4-8dc6-005056bf0df0[|[d7608d70-d806-11e4-8dc6-005056bf0df0 TO d8135c70-b1d7-11e4-b47f-005056bf32d7[|[d8135c70-b1d7-11e4-b47f-005056bf32d7 TO d8aab3a0-d959-11e4-b3ea-005056bf2081[|[d8aab3a0-d959-11e4-b3ea-005056bf2081 TO d967f390-c278-11e4-a47b-005056bf5463[|[d967f390-c278-11e4-a47b-005056bf5463 TO da0f2dc0-cdb5-11e4-b4b6-005056bf5463[|[da0f2dc0-cdb5-11e4-b4b6-005056bf5463 TO da9f82c0-ca87-11e4-93ee-005056bf1bac[|[da9f82c0-ca87-11e4-93ee-005056bf1bac TO db5cb910-b940-11e4-a677-005056bf1bac[|[db5cb910-b940-11e4-a677-005056bf1bac TO dbee8d80-dfbb-11e4-8cca-005056bf4abb[|[dbee8d80-dfbb-11e4-8cca-005056bf4abb TO dcc020e0-da82-11e4-988b-005056bf2081[|[dcc020e0-da82-11e4-988b-005056bf2081 TO dd1521c0-cd68-11e4-8fa6-005056bf2081[|[dd1521c0-cd68-11e4-8fa6-005056bf2081 TO ddcdfe10-d606-11e4-ab68-005056bf12e1[|[ddcdfe10-d606-11e4-ab68-005056bf12e1 TO de75b640-cac2-11e4-9762-005056bf12e1[|[de75b640-cac2-11e4-9762-005056bf12e1 TO df259010-daee-11e4-b8e6-005056bf53ee[|[df259010-daee-11e4-b8e6-005056bf53ee TO dfc645a0-db2a-11e4-a1d5-005056bf53ee[|[dfc645a0-db2a-11e4-a1d5-005056bf53ee TO e0c73650-6023-11e2-a5e1-005056920242[|[e0c73650-6023-11e2-a5e1-005056920242 TO e16eeac0-b937-11e4-886e-005056bf4abb[|[e16eeac0-b937-11e4-886e-005056bf4abb TO e20eee30-c2a1-11e4-989c-005056bf1bac[|[e20eee30-c2a1-11e4-989c-005056bf1bac TO e2c25110-d4a7-11e4-9489-005056bf4abb[|[e2c25110-d4a7-11e4-9489-005056bf4abb TO e37f0580-dd67-11e4-8f5c-005056bf53ee[|[e37f0580-dd67-11e4-8f5c-005056bf53ee TO e4342b80-c6fc-11e4-9625-005056bf53ee[|[e4342b80-c6fc-11e4-9625-005056bf53ee TO e4ce0200-b6cb-11e4-825b-005056bf1bac[|[e4ce0200-b6cb-11e4-825b-005056bf1bac TO e5848750-c80f-11e4-a1d6-005056bf53ee[|[e5848750-c80f-11e4-a1d6-005056bf53ee TO e649dde0-d1f9-11e4-9fb2-005056bf0df0[|[e649dde0-d1f9-11e4-9fb2-005056bf0df0 TO e6df73a0-6472-11e3-adf7-005056bf2e5b[|[e6df73a0-6472-11e3-adf7-005056bf2e5b TO e767e6b0-81d9-11e4-b8ec-005056b20325[|[e767e6b0-81d9-11e4-b8ec-005056b20325 TO e814df70-d4a2-11e4-880c-005056bf12e1[|[e814df70-d4a2-11e4-880c-005056bf12e1 TO e8799750-ca73-11e4-ac9f-005056bf12e1[|[e8799750-ca73-11e4-ac9f-005056bf12e1 TO e92323b0-de7d-11e4-9552-005056bf12e1[|[e92323b0-de7d-11e4-9552-005056bf12e1 TO e9d668b0-dcbd-11e4-b0bc-005056bf12e1[|[e9d668b0-dcbd-11e4-b0bc-005056bf12e1 TO ea793cf0-df71-11e4-8cca-005056bf4abb[|[ea793cf0-df71-11e4-8cca-005056bf4abb TO eb1f3300-d583-11e4-8051-005056bf1bac[|[eb1f3300-d583-11e4-8051-005056bf1bac TO ebc7d220-c59f-11e3-b33c-005056920241[|[ebc7d220-c59f-11e3-b33c-005056920241 TO ec425580-c8ba-11e4-8a56-005056bf5463[|[ec425580-c8ba-11e4-8a56-005056bf5463 TO ed316da0-3dd7-11e4-b4b3-005056920243[|[ed316da0-3dd7-11e4-b4b3-005056920243 TO edcfa8d0-df4e-11e4-b0e4-005056bf6d47[|[edcfa8d0-df4e-11e4-b0e4-005056bf6d47 TO eea61460-dd19-11e4-b0bc-005056bf12e1[|[eea61460-dd19-11e4-b0bc-005056bf12e1 TO ef543c00-d8b6-11e4-9a89-005056bf32d7[|[ef543c00-d8b6-11e4-9a89-005056bf32d7 TO efe5dc50-cfb0-11e4-9a27-005056bf1bac[|[efe5dc50-cfb0-11e4-9a27-005056bf1bac TO f1069f00-d84a-11e4-a311-005056bf1bac[|[f1069f00-d84a-11e4-a311-005056bf1bac TO f191b450-d552-11e4-9487-005056bf0df0[|[f191b450-d552-11e4-9487-005056bf0df0 TO f233feb0-d519-11e4-b820-005056bf12e1[|[f233feb0-d519-11e4-b820-005056bf12e1 TO f2db7650-d5ea-11e4-b484-005056bf4abb[|[f2db7650-d5ea-11e4-b484-005056bf4abb TO f37a2710-b45b-11e4-8f26-005056bf12e1[|[f37a2710-b45b-11e4-8f26-005056bf12e1 TO f401d450-cddb-11e4-85cf-005056bf12e1[|[f401d450-cddb-11e4-85cf-005056bf12e1 TO f4f11e80-d019-11e4-9345-005056bf0df0[|[f4f11e80-d019-11e4-9345-005056bf0df0 TO f5a9f350-d560-11e4-8051-005056bf1bac[|[f5a9f350-d560-11e4-8051-005056bf1bac TO f6a9e870-df77-11e4-b036-005056bf5463[|[f6a9e870-df77-11e4-b036-005056bf5463 TO f70abc00-d0a3-11e4-9fc0-005056bf1bac[|[f70abc00-d0a3-11e4-9fc0-005056bf1bac TO f7af62d0-d0d7-11e4-9337-005056bf53ee[|[f7af62d0-d0d7-11e4-9337-005056bf53ee TO f8713a90-dcbc-11e4-8c2f-005056bf2081[|[f8713a90-dcbc-11e4-8c2f-005056bf2081 TO f96cf5c0-d784-11e4-9398-005056bf4abb[|[f96cf5c0-d784-11e4-9398-005056bf4abb TO fa25b330-b8df-11e4-a677-005056bf1bac[|[fa25b330-b8df-11e4-a677-005056bf1bac TO fb14af70-bae9-11e3-af7b-005056920243[|[fb14af70-bae9-11e3-af7b-005056920243 TO fb828b50-d53f-11e4-8623-005056bf4abb[|[fb828b50-d53f-11e4-8623-005056bf4abb TO fbfbb350-c954-11e4-85f4-005056bf4abb[|[fbfbb350-c954-11e4-85f4-005056bf4abb TO fca6f990-b860-11e4-8dff-005056bf4abb[|[fca6f990-b860-11e4-8dff-005056bf4abb TO fdaf3f40-d553-11e4-8051-005056bf1bac[|[fdaf3f40-d553-11e4-8051-005056bf1bac TO fe484140-cf7c-11e4-9a27-005056bf1bac[|[fe484140-cf7c-11e4-9a27-005056bf1bac TO feb20db0-de71-11e4-bd18-005056bf2081[|[feb20db0-de71-11e4-bd18-005056bf2081 TO ff4aab10-d5e8-11e4-84c2-005056bf2081[|[ff4aab10-d5e8-11e4-84c2-005056bf2081 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "iti", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "srn", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 0094318893[|[0094318893 TO 0184089288[|[0184089288 TO 0262995034[|[0262995034 TO 0371384119[|[0371384119 TO 0461388982[|[0461388982 TO 0558545225[|[0558545225 TO 0655905316[|[0655905316 TO 0750573224[|[0750573224 TO 0845700517[|[0845700517 TO 0930906369[|[0930906369 TO 1024524452[|[1024524452 TO 1122688394[|[1122688394 TO 1212584077[|[1212584077 TO 1301971181[|[1301971181 TO 1405565985[|[1405565985 TO 1501852058[|[1501852058 TO 1571418416[|[1571418416 TO 1648056342[|[1648056342 TO 1753672470[|[1753672470 TO 1837153539[|[1837153539 TO 1922246296[|[1922246296 TO 2001948256[|[2001948256 TO 2090749314[|[2090749314 TO 2156605291[|[2156605291 TO 2245031390[|[2245031390 TO 2348586347[|[2348586347 TO 2461857747[|[2461857747 TO 2536777570[|[2536777570 TO 2640669686[|[2640669686 TO 2732653032[|[2732653032 TO 2830949487[|[2830949487 TO 2922042493[|[2922042493 TO 301251906[|[301251906 TO 3086592972[|[3086592972 TO 314467960[|[314467960 TO 3211884451[|[3211884451 TO 3267299318[|[3267299318 TO 331777490[|[331777490 TO 337692479[|[337692479 TO 341895779[|[341895779 TO 3473843157[|[3473843157 TO 352087134[|[352087134 TO 3589969607[|[3589969607 TO 3700130349[|[3700130349 TO 379476666[|[379476666 TO 3843315499[|[3843315499 TO 389498353[|[389498353 TO 394189369[|[394189369 TO 3992152927[|[3992152927 TO 403471964[|[403471964 TO 409884673[|[409884673 TO 4141251744[|[4141251744 TO 419416474[|[419416474 TO 422882464[|[422882464 TO 4283330528[|[4283330528 TO 432134534[|[432134534 TO 435405402[|[435405402 TO 440118669[|[440118669 TO 442810032[|[442810032 TO 448040816[|[448040816 TO 451411664[|[451411664 TO 4538898389[|[4538898389 TO 4619719362[|[4619719362 TO 4721151066[|[4721151066 TO 4796085650[|[4796085650 TO 484038492[|[484038492 TO 488533688[|[488533688 TO 491408787[|[491408787 TO 4933799684[|[4933799684 TO 4978257611[|[4978257611 TO 500384193[|[500384193 TO 503616328[|[503616328 TO 5070446644[|[5070446644 TO 510240021[|[510240021 TO 5125751355[|[5125751355 TO 5152971461[|[5152971461 TO 519652192[|[519652192 TO 522185339[|[522185339 TO 524632551[|[524632551 TO 528441959[|[528441959 TO 5314426135[|[5314426135 TO 5343705483[|[5343705483 TO 538552217[|[538552217 TO 5441557982[|[5441557982 TO 5538891124[|[5538891124 TO 5637533458[|[5637533458 TO 5734419799[|[5734419799 TO 5818829263[|[5818829263 TO 5910709018[|[5910709018 TO 6028094552[|[6028094552 TO 6125416183[|[6125416183 TO 6205872971[|[6205872971 TO 6285030497[|[6285030497 TO 6379148458[|[6379148458 TO 6469285743[|[6469285743 TO 6563250001[|[6563250001 TO 6675338283[|[6675338283 TO 6750597526[|[6750597526 TO 6835251679[|[6835251679 TO 6951413523[|[6951413523 TO 7042233042[|[7042233042 TO 7145093255[|[7145093255 TO 7240711140[|[7240711140 TO 7347438114[|[7347438114 TO 7433069580[|[7433069580 TO 750405391[|[750405391 TO 753016815[|[753016815 TO 7586856287[|[7586856287 TO 7664852190[|[7664852190 TO 7754564482[|[7754564482 TO 7818831300[|[7818831300 TO 789416518[|[789416518 TO 792040651[|[792040651 TO 794939140[|[794939140 TO 798856241[|[798856241 TO 8034690957[|[8034690957 TO 8112643865[|[8112643865 TO 8203338319[|[8203338319 TO 8309868006[|[8309868006 TO 8404135366[|[8404135366 TO 8494167427[|[8494167427 TO 8605129575[|[8605129575 TO 8688833246[|[8688833246 TO 8774889448[|[8774889448 TO 8875244198[|[8875244198 TO 8969081501[|[8969081501 TO 9049669348[|[9049669348 TO 9153231101[|[9153231101 TO 9244346794[|[9244346794 TO 9338118117[|[9338118117 TO 9435892832[|[9435892832 TO 9513002412[|[9513002412 TO 9607248443[|[9607248443 TO 9703953400[|[9703953400 TO 9801330665[|[9801330665 TO 9906357510[|[9906357510 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "srn", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "npe", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 0285467[|[0285467 TO 0567920[|[0567920 TO 0864105[|[0864105 TO 1038263[|[1038263 TO 1163617[|[1163617 TO 1329051[|[1329051 TO 1517056[|[1517056 TO 1710955[|[1710955 TO 1880627[|[1880627 TO 20379[|[20379 TO 2181338[|[2181338 TO 2358095[|[2358095 TO 2547704[|[2547704 TO 275269[|[275269 TO 2933652[|[2933652 TO 3065884[|[3065884 TO 3248762[|[3248762 TO 3472234[|[3472234 TO 3690066[|[3690066 TO 3907423[|[3907423 TO 4063456[|[4063456 TO 4251561[|[4251561 TO 4421752[|[4421752 TO 4638486[|[4638486 TO 4901011[|[4901011 TO 5106388[|[5106388 TO 5307858[|[5307858 TO 556776[|[556776 TO 5804523[|[5804523 TO 6046777[|[6046777 TO 6271590[|[6271590 TO 6556288[|[6556288 TO 6820367[|[6820367 TO 7070544[|[7070544 TO 73075[|[73075 TO 76026[|[76026 TO 7862002[|[7862002 TO 8126625[|[8126625 TO 8373379[|[8373379 TO 8655587[|[8655587 TO 8905932[|[8905932 TO 9173705[|[9173705 TO 9434225[|[9434225 TO 9721376[|[9721376 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "npe", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "nci", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 0067870[|[0067870 TO 0130962[|[0130962 TO 0181009[|[0181009 TO 0224643[|[0224643 TO 0271840[|[0271840 TO 0327399[|[0327399 TO 0388364[|[0388364 TO 0472870[|[0472870 TO 0572765[|[0572765 TO 0754232[|[0754232 TO 0948507[|[0948507 TO 1068962[|[1068962 TO 1144641[|[1144641 TO 1271351[|[1271351 TO 1456381[|[1456381 TO 1657871[|[1657871 TO 1886170[|[1886170 TO 2019426[|[2019426 TO 2110014[|[2110014 TO 2209505[|[2209505 TO 2323727[|[2323727 TO 2491466[|[2491466 TO 2652755[|[2652755 TO 2881842[|[2881842 TO 3035532[|[3035532 TO 3144484[|[3144484 TO 3307620[|[3307620 TO 3527786[|[3527786 TO 3755356[|[3755356 TO 4001439[|[4001439 TO 4065073[|[4065073 TO 4214964[|[4214964 TO 4417005[|[4417005 TO 4568003[|[4568003 TO 4803052[|[4803052 TO 5002174[|[5002174 TO 5123875[|[5123875 TO 5288014[|[5288014 TO 5561378[|[5561378 TO 5823706[|[5823706 TO 6046708[|[6046708 TO 6268904[|[6268904 TO 6590820[|[6590820 TO 6884501[|[6884501 TO 7154104[|[7154104 TO 7353303[|[7353303 TO 7643007[|[7643007 TO 7938768[|[7938768 TO 8096949[|[8096949 TO 8306246[|[8306246 TO 8571182[|[8571182 TO 8857309[|[8857309 TO 9107105[|[9107105 TO 9407149[|[9407149 TO 9708914[|[9708914 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "nci", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "den", "NOMINAL");
		rangesAsString = "[min_lower_bound TO a g s energies[|[a g s energies TO ab der halden maxime[|[ab der halden maxime TO accord production[|[accord production TO adam florin[|[adam florin TO aef servi sud du piscenois[|[aef servi sud du piscenois TO aia studio paysages[|[aia studio paysages TO ald[|[ald TO alpha mediations[|[alpha mediations TO amatsigroup[|[amatsigroup TO amphenol air lb[|[amphenol air lb TO anpel[|[anpel TO aquitaine equip occase[|[aquitaine equip occase TO arrieta jean guy[|[arrieta jean guy TO asclepios[|[asclepios TO ass comite d animation[|[ass comite d animation TO ass la sierra prod[|[ass la sierra prod TO ass union musicale de marcillac[|[ass union musicale de marcillac TO atelier de la boiserie[|[atelier de la boiserie TO au gres du vent[|[au gres du vent TO auto flash 91[|[auto flash 91 TO ayisso-kauffmann magalutche[|[ayisso-kauffmann magalutche TO badiali olivier jean mi[|[badiali olivier jean mi TO barat william franck[|[barat william franck TO base[|[base TO baudrillon fabrice laurent[|[baudrillon fabrice laurent TO becognee christine[|[becognee christine TO beltran andre[|[beltran andre TO benzouaoui salima[|[benzouaoui salima TO bernier guillaume[|[bernier guillaume TO betourne laurent desire[|[betourne laurent desire TO birene richard[|[birene richard TO bluett alexander[|[bluett alexander TO bonnamy philippe[|[bonnamy philippe TO boucheron claude raymond[|[boucheron claude raymond TO bourahla m hamed[|[bourahla m hamed TO bouvet christian[|[bouvet christian TO breteault decors[|[breteault decors TO brunet valerie[|[brunet valerie TO burin dominique[|[burin dominique TO cabinet d avocats[|[cabinet d avocats TO calabre jean pierre guy[|[calabre jean pierre guy TO cappitta silvia[|[cappitta silvia TO carrelage region tourangell[|[carrelage region tourangell TO casino bourbonne les bains[|[casino bourbonne les bains TO cb2r[|[cb2r TO centre formation continue[|[centre formation continue TO chamalieres montferrand[|[chamalieres montferrand TO charlet florent[|[charlet florent TO chaumes en brie-commune de[|[chaumes en brie-commune de TO chhim[|[chhim TO ci[|[ci TO clerf louis bernard b[|[clerf louis bernard b TO co prop imm r leblanc 39[|[co prop imm r leblanc 39 TO college mozart[|[college mozart TO communale saulces monclin[|[communale saulces monclin TO compagnie art vos souhaits[|[compagnie art vos souhaits TO conseil depart saintvincent[|[conseil depart saintvincent TO copr 17 rue vauthier[|[copr 17 rue vauthier TO copro 5 rue ernest duchesne[|[copro 5 rue ernest duchesne TO cosinus[|[cosinus TO couvent soeurs providence[|[couvent soeurs providence TO crouineau patricia michel[|[crouineau patricia michel TO cviklinski luc[|[cviklinski luc TO dacau industries[|[dacau industries TO darlene[|[darlene TO de fremond de la merveiller[|[de fremond de la merveiller TO decolletage du rosemont[|[decolletage du rosemont TO delaurier paul[|[delaurier paul TO delya[|[delya TO dequier geraldine[|[dequier geraldine TO desvignes denis jacques r[|[desvignes denis jacques r TO didienne franck bernard[|[didienne franck bernard TO distribtion et prestations[|[distribtion et prestations TO dorca renov[|[dorca renov TO dress code paris[|[dress code paris TO dubois isabelle marie-[|[dubois isabelle marie- TO dulimbert cecile[|[dulimbert cecile TO durou jean gaston[|[durou jean gaston TO eckly tania[|[eckly tania TO eg2b 89[|[eg2b 89 TO electromer[|[electromer TO entreprise d isolation[|[entreprise d isolation TO eric leignel conseil[|[eric leignel conseil TO essemtec france[|[essemtec france TO ets vogel[|[ets vogel TO eurl dejean jean pierre[|[eurl dejean jean pierre TO eurl marc manut amenag renov cre[|[eurl marc manut amenag renov cre TO euroflaco dijon[|[euroflaco dijon TO fabb[|[fabb TO fauron ginette[|[fauron ginette TO ferbal[|[ferbal TO ficarra josette marie c[|[ficarra josette marie c TO flandres.protect incendie[|[flandres.protect incendie TO forez plaquiste associes[|[forez plaquiste associes TO francaise de financemen[|[francaise de financemen TO free cadre[|[free cadre TO futur digital[|[futur digital TO gallego bernard[|[gallego bernard TO garbati marc louis[|[garbati marc louis TO gatt xavier philippe[|[gatt xavier philippe TO gca genie civil d armor[|[gca genie civil d armor TO geraud christine[|[geraud christine TO giennoise de chaudronnerie[|[giennoise de chaudronnerie TO global services[|[global services TO gonthier perrier anne[|[gonthier perrier anne TO grand garage maritime[|[grand garage maritime TO grollemund jean paul[|[grollemund jean paul TO gs services[|[gs services TO guiheneuf laurence[|[guiheneuf laurence TO gym et loisirs de preaux[|[gym et loisirs de preaux TO hamon marchand[|[hamon marchand TO hebinger camille antoine[|[hebinger camille antoine TO hestia immobilier[|[hestia immobilier TO holuigue richard hugues[|[holuigue richard hugues TO houlmedis[|[houlmedis TO hydropale[|[hydropale TO immobilier de moyens[|[immobilier de moyens TO initiatives&developpement[|[initiatives&developpement TO irc cod etiq[|[irc cod etiq TO jacob da moura elizete[|[jacob da moura elizete TO javaux thomas yvon mic[|[javaux thomas yvon mic TO jlm pack[|[jlm pack TO jsa international[|[jsa international TO kanka[|[kanka TO king center pneu occasion[|[king center pneu occasion TO kuhner alain[|[kuhner alain TO l r t[|[l r t TO la mulatine[|[la mulatine TO lafarge michel andre[|[lafarge michel andre TO lamiral gilles[|[lamiral gilles TO laran eric henri[|[laran eric henri TO laurentin installations[|[laurentin installations TO le breton emmanuel jean f[|[le breton emmanuel jean f TO le galet[|[le galet TO le real[|[le real TO lecacheux jean[|[lecacheux jean TO legoff frederic pierre[|[legoff frederic pierre TO lepvrier marc georges ro[|[lepvrier marc georges ro TO les halles de pertuis[|[les halles de pertuis TO lewicki catherine[|[lewicki catherine TO limprost[|[limprost TO logivie[|[logivie TO loutski corinne[|[loutski corinne TO lyonnaz polyfusion[|[lyonnaz polyfusion TO maconnerie annecienne[|[maconnerie annecienne TO maingault stephane herve[|[maingault stephane herve TO mairie de essey les nancy[|[mairie de essey les nancy TO mairie de thuilley aux gros[|[mairie de thuilley aux gros TO mairie mormaison[|[mairie mormaison TO maison emploi sud vaucluse[|[maison emploi sud vaucluse TO mantrans juvisy[|[mantrans juvisy TO marechal virginie[|[marechal virginie TO martin-vitel alexandra paul[|[martin-vitel alexandra paul TO mathe et knecht[|[mathe et knecht TO mazat thierry[|[mazat thierry TO medocaine electro service[|[medocaine electro service TO menuiserie albert thierry[|[menuiserie albert thierry TO messeant dominique[|[messeant dominique TO michel premat[|[michel premat TO minisini romain[|[minisini romain TO mle amar sandrine isabel[|[mle amar sandrine isabel TO mle debeaurain caroline franci[|[mle debeaurain caroline franci TO mle israel karine jacqueli[|[mle israel karine jacqueli TO mle reiser sylvie gabriell[|[mle reiser sylvie gabriell TO mme abderrahmane sihame[|[mme abderrahmane sihame TO mme bonnet marie helene[|[mme bonnet marie helene TO mme david bellouard valerie[|[mme david bellouard valerie TO mme elvee carmen elisabet[|[mme elvee carmen elisabet TO mme girault florence josephe fl[|[mme girault florence josephe fl TO mme juif noemie[|[mme juif noemie TO mme martinez cecile[|[mme martinez cecile TO mme noisette marcelle maryse[|[mme noisette marcelle maryse TO mme roux odette[|[mme roux odette TO mme vergnes carole[|[mme vergnes carole TO monnier emmanuel andre[|[monnier emmanuel andre TO morel olivier alain m[|[morel olivier alain m TO mouneyres celine[|[mouneyres celine TO mr akkaya fuat[|[mr akkaya fuat TO mr baj serge jackie[|[mr baj serge jackie TO mr beslier georges yvan gi[|[mr beslier georges yvan gi TO mr boyjonauth prasram[|[mr boyjonauth prasram TO mr caylak murat[|[mr caylak murat TO mr clemenceau didier[|[mr clemenceau didier TO mr dal negro michael[|[mr dal negro michael TO mr deniau matthieu samuel[|[mr deniau matthieu samuel TO mr duru dominique desir[|[mr duru dominique desir TO mr fourgoux claude[|[mr fourgoux claude TO mr gourama joseph mario[|[mr gourama joseph mario TO mr huffer stephen[|[mr huffer stephen TO mr lala jean philippe[|[mr lala jean philippe TO mr lejosne jean marc[|[mr lejosne jean marc TO mr marc remy paul andre[|[mr marc remy paul andre TO mr moncomble fabien guy guis[|[mr moncomble fabien guy guis TO mr ortes lionel[|[mr ortes lionel TO mr poma frederic[|[mr poma frederic TO mr rochet blanc gerald[|[mr rochet blanc gerald TO mr schrapp thomas[|[mr schrapp thomas TO mr thueux cyrille christi[|[mr thueux cyrille christi TO mr voukassovitc michel[|[mr voukassovitc michel TO muntlak monelle[|[muntlak monelle TO nathy[|[nathy TO newcom l'objet pub[|[newcom l'objet pub TO nna koum cyrille[|[nna koum cyrille TO novelis[|[novelis TO ogec joyeux bearn[|[ogec joyeux bearn TO opale d transports[|[opale d transports TO osiris plus[|[osiris plus TO p.c.b.[|[p.c.b. TO paquelet gerald roland g[|[paquelet gerald roland g TO pascal basseuil[|[pascal basseuil TO payre-ficot serge[|[payre-ficot serge TO peratou sebastien[|[peratou sebastien TO perrotey marie laure[|[perrotey marie laure TO pharmacie uxol[|[pharmacie uxol TO pierre negroni et cie[|[pierre negroni et cie TO planete securite privee[|[planete securite privee TO poinapin gael jean danie[|[poinapin gael jean danie TO ponta michel galisto[|[ponta michel galisto TO prantl julien jean fra[|[prantl julien jean fra TO pro 41[|[pro 41 TO pruvot jean pierre[|[pruvot jean pierre TO quillec maurice[|[quillec maurice TO ragueneau jonathan jean-l[|[ragueneau jonathan jean-l TO reboux didier yves hen[|[reboux didier yves hen TO repesse olivier[|[repesse olivier TO reynaud francois andre[|[reynaud francois andre TO rivault celine fabienne[|[rivault celine fabienne TO rollet elise pascale e[|[rollet elise pascale e TO roussel[|[roussel TO s b c g informatique[|[s b c g informatique TO sa dav[|[sa dav TO sahel[|[sahel TO saliou delphine[|[saliou delphine TO saone vallee proximite[|[saone vallee proximite TO sarl afm peinture[|[sarl afm peinture TO sarl anis optic[|[sarl anis optic TO sarl balara[|[sarl balara TO sarl cap amo et co[|[sarl cap amo et co TO sarl clikeco[|[sarl clikeco TO sarl decor et peinture[|[sarl decor et peinture TO sarl em deco[|[sarl em deco TO sarl fox automobiles[|[sarl fox automobiles TO sarl gsafi[|[sarl gsafi TO sarl iso raval oi[|[sarl iso raval oi TO sarl l entreprise de proprete et[|[sarl l entreprise de proprete et TO sarl le notable[|[sarl le notable TO sarl malissa[|[sarl malissa TO sarl n j com[|[sarl n j com TO sarl petri[|[sarl petri TO sarl restaurant salon de the[|[sarl restaurant salon de the TO sarl slimane[|[sarl slimane TO sarl taxis agnes[|[sarl taxis agnes TO sarl yana[|[sarl yana TO sas anonyme sas d architecture[|[sas anonyme sas d architecture TO sas d dunoyer[|[sas d dunoyer TO sas l ambiance 2[|[sas l ambiance 2 TO sas perstorp france[|[sas perstorp france TO sasu ab telecom[|[sasu ab telecom TO sautron monique[|[sautron monique TO scf wagon i. & b. gheco[|[scf wagon i. & b. gheco TO scotte clement[|[scotte clement TO seid sn[|[seid sn TO serrano joel marie clau[|[serrano joel marie clau TO shahmirian isabelle olga m[|[shahmirian isabelle olga m TO sivom le merlan rauze du[|[sivom le merlan rauze du TO smr europe[|[smr europe TO societe antunes[|[societe antunes TO societe peinture couleur[|[societe peinture couleur TO soledad invest[|[soledad invest TO sourzat regis[|[sourzat regis TO star gt holdco iv[|[star gt holdco iv TO stores menuiseries sud[|[stores menuiseries sud TO sushi gourmet strasbourg[|[sushi gourmet strasbourg TO syndicat eaux du grandvaux[|[syndicat eaux du grandvaux TO taieb alexandre prosp[|[taieb alexandre prosp TO team art services la pie[|[team art services la pie TO telecom reseaux services[|[telecom reseaux services TO teyssier[|[teyssier TO tiatia teiva francois[|[tiatia teiva francois TO toscani chape et carrelage[|[toscani chape et carrelage TO transports besombes[|[transports besombes TO tremoureux pierre marie[|[tremoureux pierre marie TO tuffou fabien jean mar[|[tuffou fabien jean mar TO union patronale charente[|[union patronale charente TO vallat thierry[|[vallat thierry TO vastel laurent[|[vastel laurent TO verlet helene[|[verlet helene TO vignoles daniel francis[|[vignoles daniel francis TO vitale stephanie[|[vitale stephanie TO wailly mathieu bertran[|[wailly mathieu bertran TO wipco latchan[|[wipco latchan TO yigit tamer[|[yigit tamer TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "den", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 20120107000000000[|[20120107000000000 TO 20120112000000000[|[20120112000000000 TO 20120117000000000[|[20120117000000000 TO 20120120000000000[|[20120120000000000 TO 20120123000000000[|[20120123000000000 TO 20120126000000000[|[20120126000000000 TO 20120130000000000[|[20120130000000000 TO 20120201000000000[|[20120201000000000 TO 20120207000000000[|[20120207000000000 TO 20120211000000000[|[20120211000000000 TO 20120216000000000[|[20120216000000000 TO 20120219000000000[|[20120219000000000 TO 20120224000000000[|[20120224000000000 TO 20120229000000000[|[20120229000000000 TO 20120307000000000[|[20120307000000000 TO 20120312000000000[|[20120312000000000 TO 20120318000000000[|[20120318000000000 TO 20120321000000000[|[20120321000000000 TO 20120326000000000[|[20120326000000000 TO 20120330000000000[|[20120330000000000 TO 20120406000000000[|[20120406000000000 TO 20120411000000000[|[20120411000000000 TO 20120415000000000[|[20120415000000000 TO 20120418000000000[|[20120418000000000 TO 20120423000000000[|[20120423000000000 TO 20120429000000000[|[20120429000000000 TO 20120503000000000[|[20120503000000000 TO 20120508000000000[|[20120508000000000 TO 20120514000000000[|[20120514000000000 TO 20120517000000000[|[20120517000000000 TO 20120521000000000[|[20120521000000000 TO 20120527000000000[|[20120527000000000 TO 20120601000000000[|[20120601000000000 TO 20120605000000000[|[20120605000000000 TO 20120610000000000[|[20120610000000000 TO 20120614000000000[|[20120614000000000 TO 20120620000000000[|[20120620000000000 TO 20120625000000000[|[20120625000000000 TO 20120630000000000[|[20120630000000000 TO 20120705000000000[|[20120705000000000 TO 20120710000000000[|[20120710000000000 TO 20120713000000000[|[20120713000000000 TO 20120718000000000[|[20120718000000000 TO 20120723000000000[|[20120723000000000 TO 20120728000000000[|[20120728000000000 TO 20120801000000000[|[20120801000000000 TO 20120807000000000[|[20120807000000000 TO 20120810000000000[|[20120810000000000 TO 20120814000000000[|[20120814000000000 TO 20120820000000000[|[20120820000000000 TO 20120825000000000[|[20120825000000000 TO 20120901000000000[|[20120901000000000 TO 20120908000000000[|[20120908000000000 TO 20120912000000000[|[20120912000000000 TO 20120915000000000[|[20120915000000000 TO 20120920000000000[|[20120920000000000 TO 20120925000000000[|[20120925000000000 TO 20120930000000000[|[20120930000000000 TO 20121005000000000[|[20121005000000000 TO 20121009000000000[|[20121009000000000 TO 20121012000000000[|[20121012000000000 TO 20121017000000000[|[20121017000000000 TO 20121022000000000[|[20121022000000000 TO 20121027000000000[|[20121027000000000 TO 20121031000000000[|[20121031000000000 TO 20121104000000000[|[20121104000000000 TO 20121111000000000[|[20121111000000000 TO 20121115000000000[|[20121115000000000 TO 20121119000000000[|[20121119000000000 TO 20121122000000000[|[20121122000000000 TO 20121125000000000[|[20121125000000000 TO 20121201000000000[|[20121201000000000 TO 20121205000000000[|[20121205000000000 TO 20121210000000000[|[20121210000000000 TO 20121215000000000[|[20121215000000000 TO 20121218000000000[|[20121218000000000 TO 20121222000000000[|[20121222000000000 TO 20121228000000000[|[20121228000000000 TO 20130103000000000[|[20130103000000000 TO 20130107000000000[|[20130107000000000 TO 20130111000000000[|[20130111000000000 TO 20130115000000000[|[20130115000000000 TO 20130116000000000[|[20130116000000000 TO 20130119000000000[|[20130119000000000 TO 20130123000000000[|[20130123000000000 TO 20130126000000000[|[20130126000000000 TO 20130130000000000[|[20130130000000000 TO 20130204000000000[|[20130204000000000 TO 20130209000000000[|[20130209000000000 TO 20130213000000000[|[20130213000000000 TO 20130217000000000[|[20130217000000000 TO 20130221000000000[|[20130221000000000 TO 20130226000000000[|[20130226000000000 TO 20130302000000000[|[20130302000000000 TO 20130310000000000[|[20130310000000000 TO 20130316000000000[|[20130316000000000 TO 20130321000000000[|[20130321000000000 TO 20130323000000000[|[20130323000000000 TO 20130329000000000[|[20130329000000000 TO 20130404000000000[|[20130404000000000 TO 20130408000000000[|[20130408000000000 TO 20130413000000000[|[20130413000000000 TO 20130418000000000[|[20130418000000000 TO 20130423000000000[|[20130423000000000 TO 20130426000000000[|[20130426000000000 TO 20130501000000000[|[20130501000000000 TO 20130506000000000[|[20130506000000000 TO 20130511000000000[|[20130511000000000 TO 20130515000000000[|[20130515000000000 TO 20130518000000000[|[20130518000000000 TO 20130523000000000[|[20130523000000000 TO 20130529000000000[|[20130529000000000 TO 20130602000000000[|[20130602000000000 TO 20130609000000000[|[20130609000000000 TO 20130613000000000[|[20130613000000000 TO 20130618000000000[|[20130618000000000 TO 20130622000000000[|[20130622000000000 TO 20130627000000000[|[20130627000000000 TO 20130702000000000[|[20130702000000000 TO 20130708000000000[|[20130708000000000 TO 20130712000000000[|[20130712000000000 TO 20130716000000000[|[20130716000000000 TO 20130722000000000[|[20130722000000000 TO 20130725000000000[|[20130725000000000 TO 20130728000000000[|[20130728000000000 TO 20130802000000000[|[20130802000000000 TO 20130808000000000[|[20130808000000000 TO 20130812000000000[|[20130812000000000 TO 20130818000000000[|[20130818000000000 TO 20130821000000000[|[20130821000000000 TO 20130826000000000[|[20130826000000000 TO 20130901000000000[|[20130901000000000 TO 20130905000000000[|[20130905000000000 TO 20130911000000000[|[20130911000000000 TO 20130913000000000[|[20130913000000000 TO 20130920000000000[|[20130920000000000 TO 20130924000000000[|[20130924000000000 TO 20130927000000000[|[20130927000000000 TO 20131001000000000[|[20131001000000000 TO 20131005000000000[|[20131005000000000 TO 20131009000000000[|[20131009000000000 TO 20131015000000000[|[20131015000000000 TO 20131016000000000[|[20131016000000000 TO 20131018000000000[|[20131018000000000 TO 20131022000000000[|[20131022000000000 TO 20131028000000000[|[20131028000000000 TO 20131102000000000[|[20131102000000000 TO 20131106000000000[|[20131106000000000 TO 20131112000000000[|[20131112000000000 TO 20131117000000000[|[20131117000000000 TO 20131120000000000[|[20131120000000000 TO 20131126000000000[|[20131126000000000 TO 20131130000000000[|[20131130000000000 TO 20131204000000000[|[20131204000000000 TO 20131208000000000[|[20131208000000000 TO 20131213000000000[|[20131213000000000 TO 20131214000000000[|[20131214000000000 TO 20131214000000000[|[20131214000000000 TO 20131216000000000[|[20131216000000000 TO 20131219000000000[|[20131219000000000 TO 20131224000000000[|[20131224000000000 TO 20131228000000000[|[20131228000000000 TO 20140102000000000[|[20140102000000000 TO 20140108000000000[|[20140108000000000 TO 20140113000000000[|[20140113000000000 TO 20140116000000000[|[20140116000000000 TO 20140118000000000[|[20140118000000000 TO 20140120000000000[|[20140120000000000 TO 20140122000000000[|[20140122000000000 TO 20140124000000000[|[20140124000000000 TO 20140128000000000[|[20140128000000000 TO 20140131000000000[|[20140131000000000 TO 20140202000000000[|[20140202000000000 TO 20140204000000000[|[20140204000000000 TO 20140209000000000[|[20140209000000000 TO 20140212000000000[|[20140212000000000 TO 20140216000000000[|[20140216000000000 TO 20140220000000000[|[20140220000000000 TO 20140224000000000[|[20140224000000000 TO 20140301000000000[|[20140301000000000 TO 20140305000000000[|[20140305000000000 TO 20140310000000000[|[20140310000000000 TO 20140315000000000[|[20140315000000000 TO 20140320000000000[|[20140320000000000 TO 20140324000000000[|[20140324000000000 TO 20140327000000000[|[20140327000000000 TO 20140401000000000[|[20140401000000000 TO 20140403000000000[|[20140403000000000 TO 20140405000000000[|[20140405000000000 TO 20140408000000000[|[20140408000000000 TO 20140410000000000[|[20140410000000000 TO 20140414000000000[|[20140414000000000 TO 20140414000000000[|[20140414000000000 TO 20140415000000000[|[20140415000000000 TO 20140417000000000[|[20140417000000000 TO 20140417000000000[|[20140417000000000 TO 20140420000000000[|[20140420000000000 TO 20140423000000000[|[20140423000000000 TO 20140425000000000[|[20140425000000000 TO 20140428000000000[|[20140428000000000 TO 20140502000000000[|[20140502000000000 TO 20140505000000000[|[20140505000000000 TO 20140510000000000[|[20140510000000000 TO 20140513000000000[|[20140513000000000 TO 20140517000000000[|[20140517000000000 TO 20140519000000000[|[20140519000000000 TO 20140523000000000[|[20140523000000000 TO 20140527000000000[|[20140527000000000 TO 20140602000000000[|[20140602000000000 TO 20140607000000000[|[20140607000000000 TO 20140612000000000[|[20140612000000000 TO 20140619000000000[|[20140619000000000 TO 20140623000000000[|[20140623000000000 TO 20140626000000000[|[20140626000000000 TO 20140630000000000[|[20140630000000000 TO 20140705000000000[|[20140705000000000 TO 20140708000000000[|[20140708000000000 TO 20140712000000000[|[20140712000000000 TO 20140716000000000[|[20140716000000000 TO 20140719000000000[|[20140719000000000 TO 20140725000000000[|[20140725000000000 TO 20140729000000000[|[20140729000000000 TO 20140803000000000[|[20140803000000000 TO 20140808000000000[|[20140808000000000 TO 20140813000000000[|[20140813000000000 TO 20140819000000000[|[20140819000000000 TO 20140823000000000[|[20140823000000000 TO 20140829000000000[|[20140829000000000 TO 20140902000000000[|[20140902000000000 TO 20140907000000000[|[20140907000000000 TO 20140909000000000[|[20140909000000000 TO 20140913000000000[|[20140913000000000 TO 20140917000000000[|[20140917000000000 TO 20140920000000000[|[20140920000000000 TO 20140924000000000[|[20140924000000000 TO 20140927000000000[|[20140927000000000 TO 20141001000000000[|[20141001000000000 TO 20141007000000000[|[20141007000000000 TO 20141008000000000[|[20141008000000000 TO 20141013000000000[|[20141013000000000 TO 20141017000000000[|[20141017000000000 TO 20141020000000000[|[20141020000000000 TO 20141023000000000[|[20141023000000000 TO 20141027000000000[|[20141027000000000 TO 20141031000000000[|[20141031000000000 TO 20141104000000000[|[20141104000000000 TO 20141108000000000[|[20141108000000000 TO 20141113000000000[|[20141113000000000 TO 20141117000000000[|[20141117000000000 TO 20141121000000000[|[20141121000000000 TO 20141125000000000[|[20141125000000000 TO 20141130000000000[|[20141130000000000 TO 20141205000000000[|[20141205000000000 TO 20141210000000000[|[20141210000000000 TO 20141211000000000[|[20141211000000000 TO 20141212000000000[|[20141212000000000 TO 20141212000000000[|[20141212000000000 TO 20141215000000000[|[20141215000000000 TO 20141219000000000[|[20141219000000000 TO 20141222000000000[|[20141222000000000 TO 20141228000000000[|[20141228000000000 TO 20150101000000000[|[20150101000000000 TO 20150105000000000[|[20150105000000000 TO 20150110000000000[|[20150110000000000 TO 20150115000000000[|[20150115000000000 TO 20150119000000000[|[20150119000000000 TO 20150127000000000[|[20150127000000000 TO 20150202000000000[|[20150202000000000 TO 20150208000000000[|[20150208000000000 TO 20150213000000000[|[20150213000000000 TO 20150218000000000[|[20150218000000000 TO 20150223000000000[|[20150223000000000 TO 20150228000000000[|[20150228000000000 TO 20150304000000000[|[20150304000000000 TO 20150311000000000[|[20150311000000000 TO 20150317000000000[|[20150317000000000 TO 20150321000000000[|[20150321000000000 TO 20150327000000000[|[20150327000000000 TO 20150401000000000[|[20150401000000000 TO 20150407000000000[|[20150407000000000 TO 20150412000000000[|[20150412000000000 TO 20150419000000000[|[20150419000000000 TO 20150423000000000[|[20150423000000000 TO 20150429000000000[|[20150429000000000 TO 20150503000000000[|[20150503000000000 TO 20150507000000000[|[20150507000000000 TO 20150512000000000[|[20150512000000000 TO 20150518000000000[|[20150518000000000 TO 20150524000000000[|[20150524000000000 TO 20150530000000000[|[20150530000000000 TO 20150604000000000[|[20150604000000000 TO 20150611000000000[|[20150611000000000 TO 20150614000000000[|[20150614000000000 TO 20150619000000000[|[20150619000000000 TO 20150624000000000[|[20150624000000000 TO 20150629000000000[|[20150629000000000 TO 20150704000000000[|[20150704000000000 TO 20150710000000000[|[20150710000000000 TO 20150714000000000[|[20150714000000000 TO 20150721000000000[|[20150721000000000 TO 20150728000000000[|[20150728000000000 TO 20150801000000000[|[20150801000000000 TO 20150808000000000[|[20150808000000000 TO 20150813000000000[|[20150813000000000 TO 20150819000000000[|[20150819000000000 TO 20150823000000000[|[20150823000000000 TO 20150827000000000[|[20150827000000000 TO 20150831000000000[|[20150831000000000 TO 20150907000000000[|[20150907000000000 TO 20150911000000000[|[20150911000000000 TO 20150917000000000[|[20150917000000000 TO 20150923000000000[|[20150923000000000 TO 20151001000000000[|[20151001000000000 TO 20151006000000000[|[20151006000000000 TO 20151012000000000[|[20151012000000000 TO 20151018000000000[|[20151018000000000 TO 20151024000000000[|[20151024000000000 TO 20151028000000000[|[20151028000000000 TO 20151104000000000[|[20151104000000000 TO 20151109000000000[|[20151109000000000 TO 20151113000000000[|[20151113000000000 TO 20151118000000000[|[20151118000000000 TO 20151124000000000[|[20151124000000000 TO 20151127000000000[|[20151127000000000 TO 20151202000000000[|[20151202000000000 TO 20151206000000000[|[20151206000000000 TO 20151211000000000[|[20151211000000000 TO 20151216000000000[|[20151216000000000 TO 20151222000000000[|[20151222000000000 TO 20151228000000000[|[20151228000000000 TO 20160102000000000[|[20160102000000000 TO 20160106000000000[|[20160106000000000 TO 20160113000000000[|[20160113000000000 TO 20160117000000000[|[20160117000000000 TO 20160123000000000[|[20160123000000000 TO 20160127000000000[|[20160127000000000 TO 20160203000000000[|[20160203000000000 TO 20160207000000000[|[20160207000000000 TO 20160214000000000[|[20160214000000000 TO 20160219000000000[|[20160219000000000 TO 20160225000000000[|[20160225000000000 TO 20160229000000000[|[20160229000000000 TO 20160304000000000[|[20160304000000000 TO 20160310000000000[|[20160310000000000 TO 20160315000000000[|[20160315000000000 TO 20160321000000000[|[20160321000000000 TO 20160327000000000[|[20160327000000000 TO 20160401000000000[|[20160401000000000 TO 20160406000000000[|[20160406000000000 TO 20160412000000000[|[20160412000000000 TO 20160416000000000[|[20160416000000000 TO 20160422000000000[|[20160422000000000 TO 20160427000000000[|[20160427000000000 TO 20160502000000000[|[20160502000000000 TO 20160507000000000[|[20160507000000000 TO 20160512000000000[|[20160512000000000 TO 20160518000000000[|[20160518000000000 TO 20160524000000000[|[20160524000000000 TO 20160531000000000[|[20160531000000000 TO 20160605000000000[|[20160605000000000 TO 20160609000000000[|[20160609000000000 TO 20160614000000000[|[20160614000000000 TO 20160619000000000[|[20160619000000000 TO 20160624000000000[|[20160624000000000 TO 20160629000000000[|[20160629000000000 TO 20160705000000000[|[20160705000000000 TO 20160711000000000[|[20160711000000000 TO 20160716000000000[|[20160716000000000 TO 20160721000000000[|[20160721000000000 TO 20160727000000000[|[20160727000000000 TO 20160801000000000[|[20160801000000000 TO 20160806000000000[|[20160806000000000 TO 20160812000000000[|[20160812000000000 TO 20160817000000000[|[20160817000000000 TO 20160823000000000[|[20160823000000000 TO 20160827000000000[|[20160827000000000 TO 20160902000000000[|[20160902000000000 TO 20160906000000000[|[20160906000000000 TO 20160912000000000[|[20160912000000000 TO 20160918000000000[|[20160918000000000 TO 20160923000000000[|[20160923000000000 TO 20160927000000000[|[20160927000000000 TO 20161002000000000[|[20161002000000000 TO 20161006000000000[|[20161006000000000 TO 20161012000000000[|[20161012000000000 TO 20161018000000000[|[20161018000000000 TO 20161023000000000[|[20161023000000000 TO 20161027000000000[|[20161027000000000 TO 20161101000000000[|[20161101000000000 TO 20161106000000000[|[20161106000000000 TO 20161112000000000[|[20161112000000000 TO 20161117000000000[|[20161117000000000 TO 20161123000000000[|[20161123000000000 TO 20161128000000000[|[20161128000000000 TO 20161201000000000[|[20161201000000000 TO 20161206000000000[|[20161206000000000 TO 20161213000000000[|[20161213000000000 TO 20161220000000000[|[20161220000000000 TO 20161225000000000[|[20161225000000000 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "SM_CREATION_DATE", 0, rangesAsString);
		ref.readIndexReference(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", "NOMINAL");
		rangesAsString = "[min_lower_bound TO 20120124000000000[|[20120124000000000 TO 20120202000000000[|[20120202000000000 TO 20120529091102320[|[20120529091102320 TO 20120910000000000[|[20120910000000000 TO 20130104000000000[|[20130104000000000 TO 20130116000000000[|[20130116000000000 TO 20130118000000000[|[20130118000000000 TO 20130131000000000[|[20130131000000000 TO 20130222000000000[|[20130222000000000 TO 20130423000000000[|[20130423000000000 TO 20130725195138270[|[20130725195138270 TO 20130829000000000[|[20130829000000000 TO 20131007100027504[|[20131007100027504 TO 20131017003901378[|[20131017003901378 TO 20131118105819109[|[20131118105819109 TO 20131213210321870[|[20131213210321870 TO 20131214054444081[|[20131214054444081 TO 20131214210930773[|[20131214210930773 TO 20131217051234754[|[20131217051234754 TO 20140116060227029[|[20140116060227029 TO 20140120000000000[|[20140120000000000 TO 20140124000000000[|[20140124000000000 TO 20140129000000000[|[20140129000000000 TO 20140211000000000[|[20140211000000000 TO 20140306110307984[|[20140306110307984 TO 20140327000000000[|[20140327000000000 TO 20140403000000000[|[20140403000000000 TO 20140407000000000[|[20140407000000000 TO 20140411000000000[|[20140411000000000 TO 20140415000000000[|[20140415000000000 TO 20140415024921047[|[20140415024921047 TO 20140417000000000[|[20140417000000000 TO 20140418000000000[|[20140418000000000 TO 20140423000000000[|[20140423000000000 TO 20140430000000000[|[20140430000000000 TO 20140520002443745[|[20140520002443745 TO 20140607000000000[|[20140607000000000 TO 20140715000000000[|[20140715000000000 TO 20140901000000000[|[20140901000000000 TO 20140925205213681[|[20140925205213681 TO 20141008054114623[|[20141008054114623 TO 20141028212137068[|[20141028212137068 TO 20141121201644513[|[20141121201644513 TO 20141209014846395[|[20141209014846395 TO 20141212015430292[|[20141212015430292 TO 20141212084354664[|[20141212084354664 TO 20141215140731087[|[20141215140731087 TO 20150209145709992[|[20150209145709992 TO 20150211152741909[|[20150211152741909 TO 20150212150936904[|[20150212150936904 TO 20150213105527587[|[20150213105527587 TO 20150213133357113[|[20150213133357113 TO 20150213185907794[|[20150213185907794 TO 20150213224310252[|[20150213224310252 TO 20150214031954297[|[20150214031954297 TO 20150214122737321[|[20150214122737321 TO 20150216182913262[|[20150216182913262 TO 20150216211358335[|[20150216211358335 TO 20150217020319396[|[20150217020319396 TO 20150217035332247[|[20150217035332247 TO 20150217054113479[|[20150217054113479 TO 20150217074055706[|[20150217074055706 TO 20150217111727425[|[20150217111727425 TO 20150217134832932[|[20150217134832932 TO 20150217160233880[|[20150217160233880 TO 20150217175043327[|[20150217175043327 TO 20150217195519462[|[20150217195519462 TO 20150218091041244[|[20150218091041244 TO 20150218112411022[|[20150218112411022 TO 20150218134610870[|[20150218134610870 TO 20150218155209989[|[20150218155209989 TO 20150218174545129[|[20150218174545129 TO 20150218192711031[|[20150218192711031 TO 20150218204637890[|[20150218204637890 TO 20150218231909946[|[20150218231909946 TO 20150219024639849[|[20150219024639849 TO 20150219055742254[|[20150219055742254 TO 20150219075325873[|[20150219075325873 TO 20150219102145058[|[20150219102145058 TO 20150219123816662[|[20150219123816662 TO 20150219145608227[|[20150219145608227 TO 20150219174002133[|[20150219174002133 TO 20150219194709373[|[20150219194709373 TO 20150219223632185[|[20150219223632185 TO 20150220082151008[|[20150220082151008 TO 20150220105337174[|[20150220105337174 TO 20150220142224671[|[20150220142224671 TO 20150220171617509[|[20150220171617509 TO 20150220195209836[|[20150220195209836 TO 20150220220858721[|[20150220220858721 TO 20150221013951593[|[20150221013951593 TO 20150221044136089[|[20150221044136089 TO 20150221064636620[|[20150221064636620 TO 20150221092132487[|[20150221092132487 TO 20150221115521061[|[20150221115521061 TO 20150304142931807[|[20150304142931807 TO 20150304164107915[|[20150304164107915 TO 20150304191633812[|[20150304191633812 TO 20150304213510053[|[20150304213510053 TO 20150306113935114[|[20150306113935114 TO 20150306133418336[|[20150306133418336 TO 20150306161024933[|[20150306161024933 TO 20150306174832687[|[20150306174832687 TO 20150306194528102[|[20150306194528102 TO 20150306222116473[|[20150306222116473 TO 20150307013807026[|[20150307013807026 TO 20150307040609875[|[20150307040609875 TO 20150307071452392[|[20150307071452392 TO 20150307090120635[|[20150307090120635 TO 20150307115533411[|[20150307115533411 TO 20150307143143887[|[20150307143143887 TO 20150307155640105[|[20150307155640105 TO 20150307175910958[|[20150307175910958 TO 20150307195905122[|[20150307195905122 TO 20150307223735247[|[20150307223735247 TO 20150308012154819[|[20150308012154819 TO 20150308031006154[|[20150308031006154 TO 20150308053537383[|[20150308053537383 TO 20150308074634345[|[20150308074634345 TO 20150308101825515[|[20150308101825515 TO 20150308125609487[|[20150308125609487 TO 20150308153558359[|[20150308153558359 TO 20150308174715376[|[20150308174715376 TO 20150308194940921[|[20150308194940921 TO 20150308222501082[|[20150308222501082 TO 20150309012521773[|[20150309012521773 TO 20150309033808292[|[20150309033808292 TO 20150309054908391[|[20150309054908391 TO 20150309081607246[|[20150309081607246 TO 20150309094824771[|[20150309094824771 TO 20150309122903618[|[20150309122903618 TO 20150309184641346[|[20150309184641346 TO 20150309205815770[|[20150309205815770 TO 20150309225141239[|[20150309225141239 TO 20150310021820050[|[20150310021820050 TO 20150310044158758[|[20150310044158758 TO 20150310064356150[|[20150310064356150 TO 20150310092023681[|[20150310092023681 TO 20150310112908700[|[20150310112908700 TO 20150310134011635[|[20150310134011635 TO 20150310152320272[|[20150310152320272 TO 20150310171024185[|[20150310171024185 TO 20150310185425688[|[20150310185425688 TO 20150310211603199[|[20150310211603199 TO 20150311011402894[|[20150311011402894 TO 20150311041032302[|[20150311041032302 TO 20150311071011362[|[20150311071011362 TO 20150311124541944[|[20150311124541944 TO 20150311150702903[|[20150311150702903 TO 20150311161524065[|[20150311161524065 TO 20150311175520970[|[20150311175520970 TO 20150311201854518[|[20150311201854518 TO 20150311223152394[|[20150311223152394 TO 20150312014837683[|[20150312014837683 TO 20150312033541861[|[20150312033541861 TO 20150312054425973[|[20150312054425973 TO 20150312074350708[|[20150312074350708 TO 20150312092425738[|[20150312092425738 TO 20150312112958968[|[20150312112958968 TO 20150312130747572[|[20150312130747572 TO 20150312144208085[|[20150312144208085 TO 20150312162522033[|[20150312162522033 TO 20150312180357849[|[20150312180357849 TO 20150312200034455[|[20150312200034455 TO 20150312220657840[|[20150312220657840 TO 20150312230555525[|[20150312230555525 TO 20150313022847460[|[20150313022847460 TO 20150313044950505[|[20150313044950505 TO 20150313070641871[|[20150313070641871 TO 20150313085422530[|[20150313085422530 TO 20150313165310119[|[20150313165310119 TO 20150313191552272[|[20150313191552272 TO 20150313205648363[|[20150313205648363 TO 20150314011631689[|[20150314011631689 TO 20150314041459595[|[20150314041459595 TO 20150314060829699[|[20150314060829699 TO 20150314075851334[|[20150314075851334 TO 20150314094354936[|[20150314094354936 TO 20150314112803438[|[20150314112803438 TO 20150314131651940[|[20150314131651940 TO 20150314155537304[|[20150314155537304 TO 20150314174924096[|[20150314174924096 TO 20150314190858260[|[20150314190858260 TO 20150314204713954[|[20150314204713954 TO 20150314234306900[|[20150314234306900 TO 20150315040229745[|[20150315040229745 TO 20150315055527988[|[20150315055527988 TO 20150315075711276[|[20150315075711276 TO 20150315095153505[|[20150315095153505 TO 20150315124230565[|[20150315124230565 TO 20150315152057332[|[20150315152057332 TO 20150317111308963[|[20150317111308963 TO 20150318114303424[|[20150318114303424 TO 20150318142254200[|[20150318142254200 TO 20150318172806205[|[20150318172806205 TO 20150318194216135[|[20150318194216135 TO 20150318215931576[|[20150318215931576 TO 20150319012700472[|[20150319012700472 TO 20150319033534753[|[20150319033534753 TO 20150319053647120[|[20150319053647120 TO 20150319080549198[|[20150319080549198 TO 20150319102004710[|[20150319102004710 TO 20150319115119414[|[20150319115119414 TO 20150319140512520[|[20150319140512520 TO 20150319171031005[|[20150319171031005 TO 20150320100919468[|[20150320100919468 TO 20150320121853790[|[20150320121853790 TO 20150320143201251[|[20150320143201251 TO 20150320174444170[|[20150320174444170 TO 20150320194920874[|[20150320194920874 TO 20150320220524548[|[20150320220524548 TO 20150320233655213[|[20150320233655213 TO 20150321023903483[|[20150321023903483 TO 20150321055304331[|[20150321055304331 TO 20150321074907869[|[20150321074907869 TO 20150321095459595[|[20150321095459595 TO 20150321122131749[|[20150321122131749 TO 20150321141745651[|[20150321141745651 TO 20150321160000851[|[20150321160000851 TO 20150321183050174[|[20150321183050174 TO 20150321204811028[|[20150321204811028 TO 20150321224714145[|[20150321224714145 TO 20150322024950943[|[20150322024950943 TO 20150322044350424[|[20150322044350424 TO 20150322065257084[|[20150322065257084 TO 20150322093100528[|[20150322093100528 TO 20150322120235187[|[20150322120235187 TO 20150322141634063[|[20150322141634063 TO 20150322163332004[|[20150322163332004 TO 20150322193515220[|[20150322193515220 TO 20150322211423535[|[20150322211423535 TO 20150322232003544[|[20150322232003544 TO 20150323025736824[|[20150323025736824 TO 20150323054133525[|[20150323054133525 TO 20150323080334130[|[20150323080334130 TO 20150323105725568[|[20150323105725568 TO 20150323124722321[|[20150323124722321 TO 20150323144947957[|[20150323144947957 TO 20150323183150350[|[20150323183150350 TO 20150323203029545[|[20150323203029545 TO 20150323230336997[|[20150323230336997 TO 20150324025508660[|[20150324025508660 TO 20150324051352237[|[20150324051352237 TO 20150324073409294[|[20150324073409294 TO 20150324131906922[|[20150324131906922 TO 20150325195630616[|[20150325195630616 TO 20150325221601615[|[20150325221601615 TO 20150326015354596[|[20150326015354596 TO 20150326042127280[|[20150326042127280 TO 20150326065706165[|[20150326065706165 TO 20150327112824828[|[20150327112824828 TO 20150327125203393[|[20150327125203393 TO 20150327145144425[|[20150327145144425 TO 20150327161029744[|[20150327161029744 TO 20150327181819826[|[20150327181819826 TO 20150327200710147[|[20150327200710147 TO 20150327215845871[|[20150327215845871 TO 20150327234121156[|[20150327234121156 TO 20150328025859122[|[20150328025859122 TO 20150328044415607[|[20150328044415607 TO 20150328063915798[|[20150328063915798 TO 20150328083229879[|[20150328083229879 TO 20150328102456872[|[20150328102456872 TO 20150328123701995[|[20150328123701995 TO 20150328143305837[|[20150328143305837 TO 20150328161522213[|[20150328161522213 TO 20150328184842993[|[20150328184842993 TO 20150328212342112[|[20150328212342112 TO 20150328233029298[|[20150328233029298 TO 20150329021809582[|[20150329021809582 TO 20150329035025228[|[20150329035025228 TO 20150329052057344[|[20150329052057344 TO 20150329064426238[|[20150329064426238 TO 20150329085403936[|[20150329085403936 TO 20150329110930713[|[20150329110930713 TO 20150329123924823[|[20150329123924823 TO 20150330073415158[|[20150330073415158 TO 20150330100333168[|[20150330100333168 TO 20150330120104489[|[20150330120104489 TO 20150330135905789[|[20150330135905789 TO 20150330161417887[|[20150330161417887 TO 20150330183545617[|[20150330183545617 TO 20150330205947320[|[20150330205947320 TO 20150330222236144[|[20150330222236144 TO 20150331014540598[|[20150331014540598 TO 20150331034859891[|[20150331034859891 TO 20150331060541224[|[20150331060541224 TO 20150331083644148[|[20150331083644148 TO 20150331103557205[|[20150331103557205 TO 20150331115908131[|[20150331115908131 TO 20150331132928227[|[20150331132928227 TO 20150331160146534[|[20150331160146534 TO 20150331182240424[|[20150331182240424 TO 20150331201539264[|[20150331201539264 TO 20150331215733840[|[20150331215733840 TO 20150401005011697[|[20150401005011697 TO 20150401025307307[|[20150401025307307 TO 20150401045224204[|[20150401045224204 TO 20150401071543838[|[20150401071543838 TO 20150401085752704[|[20150401085752704 TO 20150401105811020[|[20150401105811020 TO 20150401130844563[|[20150401130844563 TO 20150401151932523[|[20150401151932523 TO 20150401180828249[|[20150401180828249 TO 20150401195835592[|[20150401195835592 TO 20150401220100745[|[20150401220100745 TO 20150402010245107[|[20150402010245107 TO 20150402035701910[|[20150402035701910 TO 20150402064153328[|[20150402064153328 TO 20150402081846092[|[20150402081846092 TO 20150402094829875[|[20150402094829875 TO 20150402111722593[|[20150402111722593 TO 20150402125557363[|[20150402125557363 TO 20150402145601541[|[20150402145601541 TO 20150402162544623[|[20150402162544623 TO 20150402182213649[|[20150402182213649 TO 20150402200718703[|[20150402200718703 TO 20150402220037870[|[20150402220037870 TO 20150403013322750[|[20150403013322750 TO 20150403070729291[|[20150403070729291 TO 20150403090910938[|[20150403090910938 TO 20150403110138833[|[20150403110138833 TO 20150403125302998[|[20150403125302998 TO 20150403150318906[|[20150403150318906 TO 20150403170631705[|[20150403170631705 TO 20150403190930574[|[20150403190930574 TO 20150403213332088[|[20150403213332088 TO 20150403224828463[|[20150403224828463 TO 20150404022850208[|[20150404022850208 TO 20150404042250109[|[20150404042250109 TO 20150404054431515[|[20150404054431515 TO 20150404080516575[|[20150404080516575 TO 20150404103046038[|[20150404103046038 TO 20150404124045478[|[20150404124045478 TO 20150404145649596[|[20150404145649596 TO 20150404172329583[|[20150404172329583 TO 20150404194341758[|[20150404194341758 TO 20150404205914356[|[20150404205914356 TO 20150405004026523[|[20150405004026523 TO 20150405090233245[|[20150405090233245 TO 20150405110658067[|[20150405110658067 TO 20150406135835719[|[20150406135835719 TO 20150406154114134[|[20150406154114134 TO 20150406180518947[|[20150406180518947 TO 20150406195614809[|[20150406195614809 TO 20150406214700578[|[20150406214700578 TO 20150407003937096[|[20150407003937096 TO 20150407025722677[|[20150407025722677 TO 20150407044447915[|[20150407044447915 TO 20150407065420765[|[20150407065420765 TO 20150407093632380[|[20150407093632380 TO 20150407112603853[|[20150407112603853 TO 20150407134053680[|[20150407134053680 TO 20150407154433342[|[20150407154433342 TO 20150407175146792[|[20150407175146792 TO 20150407203934568[|[20150407203934568 TO 20150407223929423[|[20150407223929423 TO 20150408013930668[|[20150408013930668 TO 20150408034354325[|[20150408034354325 TO 20150408054420519[|[20150408054420519 TO 20150408074127595[|[20150408074127595 TO 20150408092610052[|[20150408092610052 TO 20150408124902999[|[20150408124902999 TO 20150408161735799[|[20150408161735799 TO 20150408195906430[|[20150408195906430 TO 20150409003028202[|[20150409003028202 TO 20150409031302308[|[20150409031302308 TO 20150409050747058[|[20150409050747058 TO 20150409070740142[|[20150409070740142 TO 20150409091714249[|[20150409091714249 TO 20150409110022646[|[20150409110022646 TO 20150409121853095[|[20150409121853095 TO 20150409134400673[|[20150409134400673 TO 20150409152011981[|[20150409152011981 TO 20150409170557900[|[20150409170557900 TO 20150409193713964[|[20150409193713964 TO 20150409213158079[|[20150409213158079 TO 20150410005947554[|[20150410005947554 TO 20150410023108233[|[20150410023108233 TO 20150410054627170[|[20150410054627170 TO 20150410080335760[|[20150410080335760 TO 20150410095416126[|[20150410095416126 TO 20150410120918215[|[20150410120918215 TO 20150410133659950[|[20150410133659950 TO 20150410152644045[|[20150410152644045 TO 20150410175029407[|[20150410175029407 TO 20150410195849247[|[20150410195849247 TO 20150410221909576[|[20150410221909576 TO 20150411013356575[|[20150411013356575 TO 20150411041659636[|[20150411041659636 TO 20150414120239011[|[20150414120239011 TO 20150423141059650[|[20150423141059650 TO 20150702020519574[|[20150702020519574 TO 20150902143251043[|[20150902143251043 TO 20150917113514699[|[20150917113514699 TO 20160317152131126[|[20160317152131126 TO 20160321142441958[|[20160321142441958 TO 20160322100314652[|[20160322100314652 TO 20160329145255326[|[20160329145255326 TO max_upper_bound]";
		ref.startSplitting(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", 0, rangesAsString);

	}

	@Test
	public void startSplitting_cspp2() throws Exception {
		IndexReference ref = new IndexReference();
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		
		ref.readIndexReference(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", new String[] {"NOMINAL", "BUILDING"});
		String rangesAsString = "[20150917113514699 TO 20150927220000000[|[20150927220000000 TO 20151014220000000[|[20151014220000000 TO 20151031230000000[|[20151031230000000 TO 20151118230000000[|[20151118230000000 TO 20151205230000000[|[20151205230000000 TO 20151223230000000[|[20151223230000000 TO 20160109230000000[|[20160109230000000 TO 20160127230000000[|[20160127230000000 TO 20160213230000000[|[20160213230000000 TO 20160302230000000[|[20160302230000000 TO 20160317152131126[";
		//ref.startSplitting_new(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", 396, rangesAsString);

		//ref.readIndexReference(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", new String[] {"NOMINAL", "BUILDING"});
		//rangesAsString = "[20160329145255326 TO 20160408220000000[|[20160408220000000 TO 20160425220000000[|[20160425220000000 TO 20160513220000000[|[20160513220000000 TO 20160531220000000[|[20160531220000000 TO 20160618220000000[|[20160618220000000 TO 20160706220000000[|[20160706220000000 TO 20160724220000000[|[20160724220000000 TO 20160810220000000[|[20160810220000000 TO 20160828220000000[|[20160828220000000 TO 20160914220000000[|[20160914220000000 TO 20161002220000000[|[20161002220000000 TO 20161020220000000[|[20161020220000000 TO 20161107230000000[|[20161107230000000 TO 20161124230000000[|[20161124230000000 TO 20161212230000000[|[20161212230000000 TO max_upper_bound]";
		//ref.startSplitting_new(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", 400, rangesAsString);

		/*
		ref.readIndexReference(keyspace, baseUUID, "rib", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[min_lower_bound TO 2012-02-22[|[2012-02-22 TO 2012-04-14[|[2012-04-14 TO 2012-06-06[|[2012-06-06 TO 2012-07-27[|[2012-07-27 TO 2012-09-18[|[2012-09-18 TO 2012-11-09[|[2012-11-09 TO 2012-12-31[|[2012-12-31 TO 2013-02-21[|[2013-02-21 TO 2013-04-14[|[2013-04-14 TO 2013-06-05[|[2013-06-05 TO 2013-07-27[|[2013-07-27 TO 2013-09-18[|[2013-09-18 TO 2013-11-09[|[2013-11-09 TO 2013-12-31[|[2013-12-31 TO 2014-02-21[|[2014-02-21 TO 2014-04-15[|[2014-04-15 TO 2014-06-06[|[2014-06-06 TO 2014-07-28[|[2014-07-28 TO 2014-09-18[|[2014-09-18 TO 2014-11-10[|[2014-11-10 TO 2015-01-01[|[2015-01-01 TO 2015-02-22[|[2015-02-22 TO 2015-04-15[|[2015-04-15 TO 2015-06-06[|[2015-06-06 TO 2015-07-29[|[2015-07-29 TO 2015-09-19[|[2015-09-19 TO 2015-11-11[|[2015-11-11 TO 2016-01-02[|[2016-01-02 TO 2016-02-23[|[2016-02-23 TO 2016-04-15[|[2016-04-15 TO 2016-06-07[|[2016-06-07 TO 2016-07-29[|[2016-07-29 TO 2016-09-19[|[2016-09-19 TO 2016-11-10[|[2016-11-10 TO max_upper_bound]";
		ref.startSplitting_new(keyspace, baseUUID, "rib", 0, rangesAsString);

		ref.readIndexReference(keyspace, baseUUID, "nic", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[min_lower_bound TO 037059[|[037059 TO 074432[|[074432 TO 111755[|[111755 TO 148823[|[148823 TO 186239[|[186239 TO 223049[|[223049 TO 260159[|[260159 TO 297246[|[297246 TO 334142[|[334142 TO 371323[|[371323 TO 408015[|[408015 TO 445582[|[445582 TO 482859[|[482859 TO 520162[|[520162 TO 557821[|[557821 TO 594597[|[594597 TO 631385[|[631385 TO 668191[|[668191 TO 705466[|[705466 TO 742000[|[742000 TO 778399[|[778399 TO 815481[|[815481 TO 852745[|[852745 TO 889340[|[889340 TO 926228[|[926228 TO 962347[|[962347 TO max_upper_bound]";
		ref.startSplitting_new(keyspace, baseUUID, "nic", 0, rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "rdo", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[min_lower_bound TO 02851865583[|[02851865583 TO 05706272961992518950[|[05706272961992518950 TO 08564[|[08564 TO 1140411445034882560820[|[1140411445034882560820 TO 142672[|[142672 TO 17197212268500351950149216008[|[17197212268500351950149216008 TO 20050951469268830626383[|[20050951469268830626383 TO 229214847333110015979513719546[|[229214847333110015979513719546 TO 257602878569962[|[257602878569962 TO 2854986[|[2854986 TO 314380058704726088673025077913[|[314380058704726088673025077913 TO 343042[|[343042 TO 371823071506[|[371823071506 TO 400112156340[|[400112156340 TO 429106689575070744550723922446[|[429106689575070744550723922446 TO 457715528939259328390[|[457715528939259328390 TO 48629643331331814023765406953[|[48629643331331814023765406953 TO 515835204044805095068282265[|[515835204044805095068282265 TO 544780995820110230666715510[|[544780995820110230666715510 TO 57416989641782156602791359993[|[57416989641782156602791359993 TO 602433748572118251167134381051[|[602433748572118251167134381051 TO 63103070287236480265[|[63103070287236480265 TO 65957522125203[|[65957522125203 TO 68806535633998257645598318985[|[68806535633998257645598318985 TO 71682276273029972[|[71682276273029972 TO 74459[|[74459 TO 772984523383281403260438734[|[772984523383281403260438734 TO 8014531134[|[8014531134 TO 82962492[|[82962492 TO 85813632762671209711858[|[85813632762671209711858 TO 88655210229384557737542[|[88655210229384557737542 TO 91484241011329535125815[|[91484241011329535125815 TO 942941529567[|[942941529567 TO 97106057053746630519621[|[97106057053746630519621 TO max_upper_bound]";
		ref.startSplitting_new(keyspace, baseUUID, "rdo", 0, rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "SM_MODIFICATION_DATE", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[20160322122557342 TO 20160727142721311[|[20160727142721311 TO 20160728092307869[|[20160728092307869 TO 20160728113515115[|[20160728113515115 TO 20160728134835479[|[20160728134835479 TO 20160729092157409[|[20160729092157409 TO 20160729112805400[|[20160729112805400 TO 20160729133504457[|[20160729133504457 TO 20160729154338858[|[20160729154338858 TO 20160729175405634[|[20160729175405634 TO 20160729195544142[|[20160729195544142 TO 20160729220846881[|[20160729220846881 TO 20160730002136798[|[20160730002136798 TO 20160730023352078[|[20160730023352078 TO 20160730044412144[|[20160730044412144 TO 20160730064555698[|[20160730064555698 TO 20160730084124432[|[20160730084124432 TO 20160730105054145[|[20160730105054145 TO 20160730125835065[|[20160730125835065 TO 20160730151143456[|[20160730151143456 TO 20160730171327554[|[20160730171327554 TO 20160730192137150[|[20160730192137150 TO 20160730212149023[|[20160730212149023 TO 20160730232150167[|[20160730232150167 TO 20160731012739708[|[20160731012739708 TO 20160731033720926[|[20160731033720926 TO 20160731054837535[|[20160731054837535 TO 20160731075439722[|[20160731075439722 TO 20160731095543818[|[20160731095543818 TO 20160731115311740[|[20160731115311740 TO 20160731140023798[|[20160731140023798 TO 20160731160622062[|[20160731160622062 TO 20160731181015295[|[20160731181015295 TO 20160731202201337[|[20160731202201337 TO 20160731223000718[|[20160731223000718 TO 20160801003856239[|[20160801003856239 TO 20160801024612240[|[20160801024612240 TO 20160801044522791[|[20160801044522791 TO 20160801065655861[|[20160801065655861 TO 20160801085236379[|[20160801085236379 TO 20160801110438506[|[20160801110438506 TO 20160801130410461[|[20160801130410461 TO 20160801150622553[|[20160801150622553 TO 20160801171903578[|[20160801171903578 TO 20160801192607054[|[20160801192607054 TO 20160801213638378[|[20160801213638378 TO 20160801234022008[|[20160801234022008 TO 20160802015135043[|[20160802015135043 TO 20160802040743407[|[20160802040743407 TO 20160802061614017[|[20160802061614017 TO 20160802082820116[|[20160802082820116 TO 20160802103037387[|[20160802103037387 TO 20160802123123444[|[20160802123123444 TO 20160802144051637[|[20160802144051637 TO 20160802165434500[|[20160802165434500 TO 20160802190353254[|[20160802190353254 TO 20160802210706818[|[20160802210706818 TO 20160802231440300[|[20160802231440300 TO 20160803012450263[|[20160803012450263 TO 20160803034203353[|[20160803034203353 TO 20160803055733461[|[20160803055733461 TO 20160803080720960[|[20160803080720960 TO 20160803101544048[|[20160803101544048 TO 20160803122837900[|[20160803122837900 TO 20160803143704682[|[20160803143704682 TO 20160803163933071[|[20160803163933071 TO 20160803183753165[|[20160803183753165 TO 20160803204407397[|[20160803204407397 TO 20160803225607648[|[20160803225607648 TO 20160804010557092[|[20160804010557092 TO 20160804031822370[|[20160804031822370 TO 20160804052425307[|[20160804052425307 TO 20160804073219036[|[20160804073219036 TO 20160804093806670[|[20160804093806670 TO 20160804114103236[|[20160804114103236 TO 20160804134758668[|[20160804134758668 TO 20160804155658958[|[20160804155658958 TO 20160804175500602[|[20160804175500602 TO 20160804200215212[|[20160804200215212 TO 20160804220938054[|[20160804220938054 TO 20160805001511939[|[20160805001511939 TO 20160805022708261[|[20160805022708261 TO 20160805043317891[|[20160805043317891 TO 20160805063608920[|[20160805063608920 TO 20160805084227300[|[20160805084227300 TO 20160805104551749[|[20160805104551749 TO 20160805124650076[|[20160805124650076 TO 20160805150020941[|[20160805150020941 TO 20160805170406763[|[20160805170406763 TO 20160805191021284[|[20160805191021284 TO 20160805211953005[|[20160805211953005 TO 20160805231927220[|[20160805231927220 TO 20160806011849817[|[20160806011849817 TO 20160806032843867[|[20160806032843867 TO 20160806053046150[|[20160806053046150 TO 20160806072442710[|[20160806072442710 TO 20160806092911804[|[20160806092911804 TO 20160806112403845[|[20160806112403845 TO 20160806133351937[|[20160806133351937 TO 20160806155004747[|[20160806155004747 TO 20160806175139548[|[20160806175139548 TO 20160806200230037[|[20160806200230037 TO 20160806221326385[|[20160806221326385 TO 20160807003540815[|[20160807003540815 TO 20160807030430931[|[20160807030430931 TO max_upper_bound]"; 
		ref.startSplitting_new(keyspace, baseUUID, "SM_MODIFICATION_DATE", 300, rangesAsString);
		
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[51175004400030 TO 51231429500019[|[51231429500019 TO 51288953600011[|[51288953600011 TO 51345665700024[|[51345665700024 TO 51400002500023["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[51175004400030 TO 51400002500023[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[75025000300019 TO 75082701600015[|[75082701600015 TO 75138644200014[|[75138644200014 TO 75193178300011[|[75193178300011 TO 75250002690590["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[75025000300019 TO 75250002690590[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[75250002690590 TO 75294327400015[|[75294327400015 TO 75337453700011[|[75337453700011 TO 75376419000017[|[75376419000017 TO 75475015258416["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[75250002690590 TO 75475015258416[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[52750003700017 TO 52803364000012[|[52803364000012 TO 52860636100012[|[52860636100012 TO 52919786500015[|[52919786500015 TO 52975000200014["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[52750003700017 TO 52975000200014[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[52075000100015 TO 52131273271515[|[52131273271515 TO 52186862000019[|[52186862000019 TO 52242738921195[|[52242738921195 TO 52300003200020["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[52075000100015 TO 52300003200020[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[52300003200020 TO 52359259000013[|[52359259000013 TO 52417010700014[|[52417010700014 TO 52472083600021[|[52472083600021 TO 52525000700024["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[52300003200020 TO 52525000700024[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[49825004200013 TO 49884499432519[|[49884499432519 TO 49940362400012[|[49940362400012 TO 49994819800019[|[49994819800019 TO 50050002000010["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[49825004200013 TO 50050002000010[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[50050002000010 TO 50114923100018[|[50114923100018 TO 50168562290050[|[50168562290050 TO 50219662900013[|[50219662900013 TO 50275011000020["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[50050002000010 TO 50275011000020[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[50725003300010 TO 50788132400028[|[50788132400028 TO 50841602100022[|[50841602100022 TO 50894291900012[|[50894291900012 TO 50950002100019["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[50725003300010 TO 50950002100019[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[50950002100019 TO 51008013800017[|[51008013800017 TO 51066691000011[|[51066691000011 TO 51119666900028[|[51119666900028 TO 51175004400030["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[50950002100019 TO 51175004400030[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[50275011000020 TO 50331240700011[|[50331240700011 TO 50386400100014[|[50386400100014 TO 50444203900039[|[50444203900039 TO 50500004200018["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[50275011000020 TO 50500004200018[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[53875000100017 TO 53913455100016[|[53913455100016 TO 53949771900044[|[53949771900044 TO 53985541100010[|[53985541100010 TO 54100019005127["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[53875000100017 TO 54100019005127[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[53200001500018 TO 53256390500019[|[53256390500019 TO 53313145400019[|[53313145400019 TO 53369914600015[|[53369914600015 TO 53425002200025["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[53200001500018 TO 53425002200025[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[52975000200014 TO 53033526400012[|[53033526400012 TO 53090293100019[|[53090293100019 TO 53146650190465[|[53146650190465 TO 53200001500018["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[52975000200014 TO 53200001500018[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[53650014195591 TO 53761736700017[|[53761736700017 TO 53797663100029[|[53797663100029 TO 53836088400018[|[53836088400018 TO 53875000100017["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[53650014195591 TO 53875000100017[", rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "srt", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[53425002200025 TO 53456437200017[|[53456437200017 TO 53488605600017[|[53488605600017 TO 53520892150700[|[53520892150700 TO 53650014195591["; 
		ref.startSplitting_new(keyspace, baseUUID, "srt", "[53425002200025 TO 53650014195591[", rangesAsString);
		*/
		
		ref.readIndexReference(keyspace, baseUUID, "rum", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[min_lower_bound TO 02523271696960041345453272156091039[|[02523271696960041345453272156091039 TO 042077cew20140228a000164412[|[042077cew20140228a000164412 TO 04969601636420886528370877325504222[|[04969601636420886528370877325504222 TO 07515504153486936634897328598506775[|[07515504153486936634897328598506775 TO 10063800058633847219265543539629651[|[10063800058633847219265543539629651 TO 12628504474565157397269656211947398[|[12628504474565157397269656211947398 TO 15215668032109350155185721528360157[|[15215668032109350155185721528360157 TO 17818480651641456521026522340278410[|[17818480651641456521026522340278410 TO 20384510076474436267060509926502949[|[20384510076474436267060509926502949 TO 22963325463867736920824334164462803[|[22963325463867736920824334164462803 TO 25555163444212144400158259082488598[|[25555163444212144400158259082488598 TO 28111420924921345779853422093841580[|[28111420924921345779853422093841580 TO 30682388312369762340000149732377071[|[30682388312369762340000149732377071 TO 33273028487814487222645605901191987[|[33273028487814487222645605901191987 TO 35864048415174958397621374825796293[|[35864048415174958397621374825796293 TO 38416564671530473503798678532692019[|[38416564671530473503798678532692019 TO 41038102779171913394336666603901068[|[41038102779171913394336666603901068 TO 43598688695489686287153115155980621[|[43598688695489686287153115155980621 TO 46102802290669152965298745700565516[|[46102802290669152965298745700565516 TO 48616952075686783030019617901543954[|[48616952075686783030019617901543954 TO 51227656521527760967369534622638080[|[51227656521527760967369534622638080 TO 53782452676046430124754859355742537[|[53782452676046430124754859355742537 TO 56359961555461439232248289967183326[|[56359961555461439232248289967183326 TO 58894401415716685327712294851288180[|[58894401415716685327712294851288180 TO 61456459320827581523808125230389313[|[61456459320827581523808125230389313 TO 63972095213340868967031760602408658[|[63972095213340868967031760602408658 TO 66536450234444331065539896005770148[|[66536450234444331065539896005770148 TO 69130583452364371550770880801658618[|[69130583452364371550770880801658618 TO 71697582360278455082349425212128282[|[71697582360278455082349425212128282 TO 74254883751191984388277231872657539[|[74254883751191984388277231872657539 TO 76806745009237321087007801925170036[|[76806745009237321087007801925170036 TO 79391256201149579674156349089065146[|[79391256201149579674156349089065146 TO 81942934087393380493002591579167269[|[81942934087393380493002591579167269 TO 84520337807558892151731184775114658[|[84520337807558892151731184775114658 TO 87103699306672237438468600769314296[|[87103699306672237438468600769314296 TO 89677065721674315636713356566984012[|[89677065721674315636713356566984012 TO 92264446215728837198808381399361056[|[92264446215728837198808381399361056 TO 94855351357885284617296159164957533[|[94855351357885284617296159164957533 TO 97410102994708428943000162836019666[|[97410102994708428943000162836019666 TO max_upper_bound]";
		ref.startSplitting_new(keyspace, baseUUID, "rum", 0, rangesAsString);

		ref.readIndexReference(keyspace, baseUUID, "nre", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[min_lower_bound TO 037041293355039[|[037041293355039 TO 073690754480058[|[073690754480058 TO 110747402670190[|[110747402670190 TO 147739226789470[|[147739226789470 TO 184666646536749[|[184666646536749 TO 221719395078708[|[221719395078708 TO 258262266421928[|[258262266421928 TO 295076312241371[|[295076312241371 TO 332463421045104[|[332463421045104 TO 369612587422405[|[369612587422405 TO 406641655663795[|[406641655663795 TO 443683291924182[|[443683291924182 TO 480924983291089[|[480924983291089 TO 517803226699877[|[517803226699877 TO 554844668053186[|[554844668053186 TO 592219734674179[|[592219734674179 TO 629136544558307[|[629136544558307 TO 666183989000964[|[666183989000964 TO 703386737541855[|[703386737541855 TO 740817217923616[|[740817217923616 TO 777850689293850[|[777850689293850 TO 815561834829181[|[815561834829181 TO 852333197093975[|[852333197093975 TO 889110082116100[|[889110082116100 TO 925959549560124[|[925959549560124 TO 963162220942452[|[963162220942452 TO max_upper_bound]";
		ref.startSplitting_new(keyspace, baseUUID, "nre", 0, rangesAsString);

		ref.readIndexReference(keyspace, baseUUID, "psi", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[min_lower_bound TO 01221642764964[|[01221642764964 TO 02437515901329[|[02437515901329 TO 03696319623226[|[03696319623226 TO 04948288729199[|[04948288729199 TO 06218750088941[|[06218750088941 TO 07468433211176[|[07468433211176 TO 08718327875332[|[08718327875332 TO 09954399192699[|[09954399192699 TO 11221606743568[|[11221606743568 TO 12461860646516[|[12461860646516 TO 13716335234381[|[13716335234381 TO 14935602840587[|[14935602840587 TO 16168254867359[|[16168254867359 TO 17429757231263[|[17429757231263 TO 18643244644958[|[18643244644958 TO 19884594090871[|[19884594090871 TO 21097910929562[|[21097910929562 TO 22305996036183[|[22305996036183 TO 23526501370224[|[23526501370224 TO 24741900999688[|[24741900999688 TO 25977942887311[|[25977942887311 TO 27210965023243[|[27210965023243 TO 28432451343516[|[28432451343516 TO 29660767866929[|[29660767866929 TO 30894556829464[|[30894556829464 TO 32126755254054[|[32126755254054 TO 33370824002793[|[33370824002793 TO 34598386563364[|[34598386563364 TO 35829481145863[|[35829481145863 TO 37060394397644[|[37060394397644 TO 38311093184872[|[38311093184872 TO 39544362128610[|[39544362128610 TO 40795771729611[|[40795771729611 TO 42033141339142[|[42033141339142 TO 43288056301525[|[43288056301525 TO 44545994769781[|[44545994769781 TO 45800899364584[|[45800899364584 TO 47042755466935[|[47042755466935 TO 48262523965113[|[48262523965113 TO 49511119300146[|[49511119300146 TO 50741384631162[|[50741384631162 TO 51972173447235[|[51972173447235 TO 53187207657710[|[53187207657710 TO 54408641938875[|[54408641938875 TO 55619985680051[|[55619985680051 TO 56832700978424[|[56832700978424 TO 58054718680816[|[58054718680816 TO 59281278903968[|[59281278903968 TO 60544170886050[|[60544170886050 TO 61773498960539[|[61773498960539 TO 63010527488322[|[63010527488322 TO 64241594117468[|[64241594117468 TO 65474052330669[|[65474052330669 TO 66717339969869[|[66717339969869 TO 67934347662605[|[67934347662605 TO 69193116486282[|[69193116486282 TO 70419262526126[|[70419262526126 TO 71664074196811[|[71664074196811 TO 72908434383203[|[72908434383203 TO 74154689333065[|[74154689333065 TO 75393134203303[|[75393134203303 TO 76625429120987[|[76625429120987 TO 77860161721197[|[77860161721197 TO 79108192063873[|[79108192063873 TO 80328558429023[|[80328558429023 TO 81567021626980[|[81567021626980 TO 82805281351249[|[82805281351249 TO 84058490460548[|[84058490460548 TO 85307546380222[|[85307546380222 TO 86520410874267[|[86520410874267 TO 87741447784017[|[87741447784017 TO 88998250068631[|[88998250068631 TO 90237503576087[|[90237503576087 TO 91465624210438[|[91465624210438 TO 92727576981364[|[92727576981364 TO 93934524821715[|[93934524821715 TO 95167303945652[|[95167303945652 TO 96404429041748[|[96404429041748 TO 97670323645149[|[97670323645149 TO 98869586518369[|[98869586518369 TO y1333723380006[|[y1333723380006 TO max_upper_bound]";
		ref.startSplitting_new(keyspace, baseUUID, "psi", 0, rangesAsString);
		
		ref.readIndexReference(keyspace, baseUUID, "nne", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[min_lower_bound TO 071290201189265[|[071290201189265 TO 142463020678764[|[142463020678764 TO 214109031226427[|[214109031226427 TO 286034130584596[|[286034130584596 TO 357205644116156[|[357205644116156 TO 428430498301228[|[428430498301228 TO 499527227533211[|[499527227533211 TO 570865659790272[|[570865659790272 TO 642433817690804[|[642433817690804 TO 713406977759832[|[713406977759832 TO 784650021388008[|[784650021388008 TO 856260147514717[|[856260147514717 TO 928119598301503[|[928119598301503 TO max_upper_bound]";
		ref.startSplitting_new(keyspace, baseUUID, "nne", 0, rangesAsString);

		ref.readIndexReference(keyspace, baseUUID, "SM_ARCHIVAGE_DATE", new String[] {"NOMINAL", "BUILDING"});
		rangesAsString = "[20150902135428761 TO 20160317151300174[|[20160317151300174 TO 20160322120156837[|[20160322120156837 TO 20160727141305153[|[20160727141305153 TO 20160728091306886[|[20160728091306886 TO 20160728112452462[|[20160728112452462 TO 20160728134130053[|[20160728134130053 TO 20160729091806947[|[20160729091806947 TO 20160729112746212[|[20160729112746212 TO 20160729133715863[|[20160729133715863 TO 20160729154734116[|[20160729154734116 TO 20160729180233836[|[20160729180233836 TO 20160729200721133[|[20160729200721133 TO 20160729222204147[|[20160729222204147 TO 20160730004027567[|[20160730004027567 TO 20160730025133632[|[20160730025133632 TO 20160730050128195[|[20160730050128195 TO 20160730070438667[|[20160730070438667 TO 20160730090716391[|[20160730090716391 TO 20160730111631308[|[20160730111631308 TO 20160730133004268[|[20160730133004268 TO 20160730154401680[|[20160730154401680 TO 20160730174720687[|[20160730174720687 TO 20160730195452062[|[20160730195452062 TO 20160730220204503[|[20160730220204503 TO 20160731000227430[|[20160731000227430 TO 20160731021636313[|[20160731021636313 TO 20160731042546959[|[20160731042546959 TO 20160731064020430[|[20160731064020430 TO 20160731084612405[|[20160731084612405 TO 20160731104825060[|[20160731104825060 TO 20160731125431433[|[20160731125431433 TO 20160731150157876[|[20160731150157876 TO 20160731171634048[|[20160731171634048 TO 20160731192052522[|[20160731192052522 TO 20160731213739366[|[20160731213739366 TO 20160731234711295[|[20160731234711295 TO 20160801020045076[|[20160801020045076 TO 20160801040603797[|[20160801040603797 TO 20160801061514315[|[20160801061514315 TO 20160801081845236[|[20160801081845236 TO 20160801102851727[|[20160801102851727 TO 20160801122928158[|[20160801122928158 TO 20160801143439717[|[20160801143439717 TO 20160801165017949[|[20160801165017949 TO 20160801185946737[|[20160801185946737 TO 20160801211145094[|[20160801211145094 TO 20160801232151126[|[20160801232151126 TO 20160802013006286[|[20160802013006286 TO 20160802034804037[|[20160802034804037 TO 20160802060031375[|[20160802060031375 TO 20160802081519268[|[20160802081519268 TO 20160802102105839[|[20160802102105839 TO 20160802122501551[|[20160802122501551 TO 20160802143651834[|[20160802143651834 TO 20160802165133514[|[20160802165133514 TO 20160802190347119[|[20160802190347119 TO 20160802210805524[|[20160802210805524 TO 20160802231910423[|[20160802231910423 TO 20160803012906067[|[20160803012906067 TO 20160803035232357[|[20160803035232357 TO 20160803061131623[|[20160803061131623 TO 20160803082312161[|[20160803082312161 TO 20160803103228511[|[20160803103228511 TO 20160803124612317[|[20160803124612317 TO 20160803145640095[|[20160803145640095 TO 20160803170122669[|[20160803170122669 TO 20160803190408061[|[20160803190408061 TO 20160803211120846[|[20160803211120846 TO 20160803233048218[|[20160803233048218 TO 20160804013829763[|[20160804013829763 TO 20160804035434403[|[20160804035434403 TO 20160804060635137[|[20160804060635137 TO 20160804081435654[|[20160804081435654 TO 20160804102253968[|[20160804102253968 TO 20160804122956730[|[20160804122956730 TO 20160804144252186[|[20160804144252186 TO 20160804164444627[|[20160804164444627 TO 20160804185340833[|[20160804185340833 TO 20160804210300295[|[20160804210300295 TO 20160804231138927[|[20160804231138927 TO 20160805012703395[|[20160805012703395 TO 20160805033216015[|[20160805033216015 TO 20160805054117767[|[20160805054117767 TO 20160805074924647[|[20160805074924647 TO 20160805095618731[|[20160805095618731 TO 20160805115644121[|[20160805115644121 TO 20160805140509004[|[20160805140509004 TO 20160805162214065[|[20160805162214065 TO 20160805182550367[|[20160805182550367 TO 20160805204038401[|[20160805204038401 TO 20160805224543424[|[20160805224543424 TO 20160806004541002[|[20160806004541002 TO 20160806025753001[|[20160806025753001 TO 20160806050049149[|[20160806050049149 TO 20160806070329745[|[20160806070329745 TO 20160806090211597[|[20160806090211597 TO 20160806110551380[|[20160806110551380 TO 20160806131330158[|[20160806131330158 TO 20160806152851396[|[20160806152851396 TO 20160806173744209[|[20160806173744209 TO 20160806195147652[|[20160806195147652 TO 20160806220707642[|[20160806220707642 TO 20160807003127609[|[20160807003127609 TO 20160807030226125[|[20160807030226125 TO max_upper_bound]";
		ref.startSplitting_new(keyspace, baseUUID, "SM_ARCHIVAGE_DATE", "[20150902135428761 TO max_upper_bound]", rangesAsString);

	}
	
	@Test
	public void finishSplitting_cspp() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		/*
		String[] metas = new String[]{
				"mi1",
				"mi2",
				"mi3",
				"mi4",
				"mi5",
				"iti",
				"srn",
				"npe",
				"nci",
				"den",
				"SM_CREATION_DATE",
				"SM_LIFE_CYCLE_REFERENCE_DATE"};
		*/
		String[] metas = new String[]{
				"SM_UUID"};		
		for (int i = 0; i< metas.length; i++) {
			System.out.println(metas[i]);
			IndexReference ref = new IndexReference();
			ref.readIndexReference(keyspace, baseUUID, metas[i], "BUILDING");
			ref.writeIndexReference(keyspace, baseUUID, metas[i], 1000000);
		}		
		
	}

	@Test
	public void finishSplitting_cspp2() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String[] metas = new String[]{
				"SM_LIFE_CYCLE_REFERENCE_DATE",
				"rib",
				"nic",
				"rdo",
				"srt",
				"rum",
				"nre",
				"psi",
				"nne",
				"SM_ARCHIVAGE_DATE"};
		for (int i = 0; i< metas.length; i++) {
			System.out.println(metas[i]);
			IndexReference ref = new IndexReference();
			ref.readIndexReference(keyspace, baseUUID, metas[i], new String[] {"NOMINAL", "BUILDING"});
			ref.writeIndexReference(keyspace, baseUUID, metas[i], 1000000);
		}
		
	}
	
	@Test
	public void updateDistinctUseCountTest() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		String meta = "nce";
		IndexReference ref = new IndexReference();
		//ref.readBrokenIndexReference(keyspace, baseUUID, meta);
		ref.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
		ref.updateDistinctIndexUseCount(keyspace, baseUUID, meta, 83432);
		ref.updateTotalIndexUseCount(keyspace, baseUUID, meta, 83432);
	}

}
