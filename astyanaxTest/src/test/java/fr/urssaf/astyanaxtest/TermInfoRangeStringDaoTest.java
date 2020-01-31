package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import com.netflix.astyanax.serializers.BigDecimalSerializer;
import com.netflix.astyanax.serializers.DoubleSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.astyanaxtest.dao.BasesReferenceDao;
import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeCF;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDaoFactory;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDatetimeDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeKey;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeStringDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeUuidDao;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class TermInfoRangeStringDaoTest {
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
		//servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160, cnp69saecas4:9160, cnp69saecas5:9160, cnp69saecas6:9160, cnp69saecas7:9160, cnp69saecas8:9160, cnp69saecas9:9160, cnp69saecas10:9160, cnp69saecas11:9160, cnp69saecas12:9160, cnp69saecas13:9160, cnp69saecas14:9160, cnp69saecas15:9160, cnp69saecas16:9160, cnp69saecas17:9160, cnp69saecas18:9160, cnp69saecas19:9160, cnp69saecas20:9160, cnp69saecas21:9160, cnp69saecas22:9160, cnp69saecas23:9160, cnp69saecas24:9160";
		servers = "cnp69gntcas1:9160, cnp69gntcas2:9160, cnp69gntcas3:9160";
		// servers = "hwi54saecas1.cve.recouv:9160"; // CNH
		// servers = "cer69imageint9.cer69.recouv:9160";
		// servers = "cer69imageint10.cer69.recouv:9160";
		// servers = "10.203.34.39:9160"; // Noufnouf
		// servers =
		// "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
		// servers = "hwi69ginsaecas2.cer69.recouv:9160";
		// servers = "cer69-saeint3:9160";
		// servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; // Préprod
		 //servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160";	// Charge
		//servers = "cnp6gntcvecas1.cve.recouv:9160,cnp3gntcvecas1.cve.recouv:9160,cnp7gntcvecas1.cve.recouv:9160";	// Charge GNT
		//servers = "cnp69givngntcas1:9160, cnp69givngntcas1:9160, cnp69givngntcas1:9160";
		
		AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
				"root", "regina4932");

		AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
				.forCluster("Docubase")
				.forKeyspace("Docubase")
				.withAstyanaxConfiguration(
						new AstyanaxConfigurationImpl()
								.setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
								//.setDiscoveryType(NodeDiscoveryType.NONE)
								.setDefaultReadConsistencyLevel(
										ConsistencyLevel.CL_ONE)
								.setDefaultWriteConsistencyLevel(ConsistencyLevel.CL_QUORUM)
								.setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE)
								)
				.withConnectionPoolConfiguration(
						new ConnectionPoolConfigurationImpl("MyConnectionPool")
								.setPort(9160).setMaxConnsPerHost(500)
								.setMaxTimeoutCount(100).setBlockedThreadThreshold(500)
								.setSeeds(servers)
								.setAuthenticationCredentials(credentials))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
				.buildKeyspace(ThriftFamilyFactory.getInstance());

		context.start();
		keyspace = context.getClient();
		mapper = new MappingUtil(keyspace, new MappingCache());

		// Pour dumper sur un fichier plutôt que sur la sortie standard
		//sysout = new PrintStream("d:/temp/out.txt");
		sysout= System.out;

	}

	@Test
	public void testKey() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, null);
		byte[] key1 = dao.getKey_old(128);
		byte[] key2 = dao.getKey_old2(128);
		System.out.println(ConvertHelper.getReadableUTF8String(key1));
		Assert.assertArrayEquals(key1, key2);
	}
	
	@Test
	public void testKeySM_ARCHIVAGE_DATE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "SM_ARCHIVAGE_DATE", baseUUID, null);
		TermInfoRangeKey key = dao.getKey(2);
		System.out.println("Key=" + ConvertHelper.bytesToHex(TermInfoRangeCF.keySerializer.toBytes(key)));
		System.out.println("Key=" + ConvertHelper.getReadableUTF8String(TermInfoRangeCF.keySerializer.toBytes(key)));
	}
	
	@Test
	public void testKeySM_MODIFICATION_DATE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "SM_MODIFICATION_DATE", baseUUID, null);
		for (int i = 76; i < 77; i++) {
			TermInfoRangeKey key = dao.getKey(i);
			System.out.println("Key" + i + "=" + ConvertHelper.bytesToHex(TermInfoRangeCF.keySerializer.toBytes(key)));
			System.out.println("Key" + i + "=" + ConvertHelper.getReadableUTF8String(TermInfoRangeCF.keySerializer.toBytes(key)));
		}
	}

	@Test
	/**
	 * Récupération des clé des index composites dans TermInfoRangeString
	 * 
	 * @throws Exception
	 */
	public void getKeysCompositeIndex() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String[] indexList = new String[]{
				"cop&nlo&",
				"cot&apr&atr&ame&SM_ARCHIVAGE_DATE&",
				"cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&",
				"cot&cag&SM_CREATION_DATE&",
				"cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&",
				"cot&cop&cdo&SM_ARCHIVAGE_DATE&",
				"cot&cop&dpa&",
				"cot&cop&mch&",
				"cot&cop&mre&",
				"cot&cop&ncb&",
				"cot&cop&nch&",
				"cot&cop&nst&",
				"cpt&sco&SM_DOCUMENT_TYPE&",
				"cpt&sco&SM_DOCUMENT_TYPE&dli&",
				"cpt&sco&SM_DOCUMENT_TYPE&dre&",
				"cpt&sco&SM_DOCUMENT_TYPE&nbl&",
				"cpt&sco&SM_DOCUMENT_TYPE&nco&",
				"cpt&sco&SM_DOCUMENT_TYPE&ndf&",
				"cpt&sco&SM_DOCUMENT_TYPE&nds&",
				"cpt&sco&SM_DOCUMENT_TYPE&nfo&",
				"cpt&sco&SM_DOCUMENT_TYPE&nfs&",
				"cpt&sco&SM_DOCUMENT_TYPE&nor&",
				"cpt&sco&SM_DOCUMENT_TYPE&nti&",
				//"dar&cop&SM_ARCHIVAGE_DATE&",
				"drh&cop&SM_CREATION_DATE&",
				"drh&cop&nma&",
				"drh&cop&nma&frd&",
				"drh&cop&pag&SM_CREATION_DATE&",
		};
		for(String index : indexList) {
			TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, index, baseUUID, null);
			int rangeId = 0;
			TermInfoRangeKey key = dao.getKey(rangeId);
			System.out.println(ConvertHelper.bytesToHex(TermInfoRangeCF.keySerializer.toBytes(key)));
		}
	}

	
	@Test
	public void testRead() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		dao.readForTest(UUID.fromString("2182464A-DD8B-4E58-97B1-2F560D35245B"), "10000001700010");
	}

	@Test
	public void testGetHashMapsSrt() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		//indexReference.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		//indexReference.readIndexReference(keyspace, baseUUID, "srt", "BUILDING");
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "SPLITTING");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		//ArrayList<HashMap<String, ArrayList<String>>> maps = dao.getHashMaps("10000001700010");
		//ArrayList<HashMap<String, ArrayList<String>>> maps = dao.getHashMaps("0378645W01");
		ArrayList<HashMap<String, ArrayList<String>>> maps = dao.getHashMaps("89995571011677");
		for (HashMap<String, ArrayList<String>> map :maps) {
			System.out.println(map);
		}
	}
	
	@Test
	public void testGetHashMapsNce() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "nce", "NOMINAL");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "nce", baseUUID, indexReference);
		ArrayList<HashMap<String, ArrayList<String>>> maps = dao.getHashMaps("247000001720208211");
		//ArrayList<HashMap<String, ArrayList<String>>> maps = dao.getHashMaps("257000000700337337");
		for (HashMap<String, ArrayList<String>> map :maps) {
			System.out.println(map);
		}
	}
	
	@Test
	public void testIterateNce() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "nce", "NOMINAL");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "nce", baseUUID, indexReference);
		dao.iterateForTest("247000001720000000", "247000001720999999");
	}

	@Test
	public void testIterateOnRange() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		for (int rangeId : indexReference.getRangeIds()) {
			System.out.println();
			System.out.println("RangeId : " + rangeId);
			dao.iterateOnRange(rangeId);
		}
	}
	
	@Test
	public void testIterateOnRange2() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		//indexReference.readBrokenIndexReference(keyspace, baseUUID, "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&", "NOMINAL");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&", baseUUID, indexReference);
		int rangeId = 32;
		dao.iterateOnRange(rangeId);
	}
	
	@Test
	public void testIterateOnRange3() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		// Index composite DomaineCotisant-CodeOrganismeProprietaire-MontantRegle 
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "cot&cop&mre&", baseUUID, indexReference);
		int rangeId = 0;
		dao.iterateOnRange(rangeId);
		double value = 15734.0;
		byte[] valueAsBytes = DoubleSerializer.get().toBytes(value);
		System.out.println("valueAsBytes : " + ConvertHelper.getHexString(valueAsBytes));
		
		byte[] bytes = ConvertHelper.hexStringToByteArray("200140672e600000");
		double newValue = DoubleSerializer.get().fromBytes(bytes);
		System.out.println("newValue : " + newValue);
		
		BigDecimal value2 = new BigDecimal(15734.0);
		byte[] bytes2 = BigDecimalSerializer.get().toBytes(value2);
		System.out.println("bytes2 : " + ConvertHelper.getHexString(bytes2));
		// Conclusion : je ne sais pas comment est encodé le montant
	}

	@Test
	public void testIterateOnRange4() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		// DocumentArchivable-CodeOrganismeProprietaire-DateArchivage
		// Pour voir comment la date est stockée
		// On voit que la date est stockée en chaine : exemple : true \0 cer69 \0 20170721143032042 \0
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "dar&cop&SM_ARCHIVAGE_DATE&", baseUUID, indexReference);
		int rangeId = 0;
		dao.iterateOnRange(rangeId);
	}

	@Test
	public void testIterateOnRange5() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "nne", baseUUID, indexReference);
		int rangeId = 4;
		dao.iterateOnRange(rangeId);
	}
	
	@Test
	public void testIterateOnRange6() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&", baseUUID, indexReference);
		int rangeId = 10;
		dao.iterateOnRange(rangeId);
	}
	
	
	@Test
	public void testIterateOnRange_DAILY_LOG_ARCHIVE_BASE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "DAILY_LOG_ARCHIVE_BASE");
		IndexReference indexReference = new IndexReference();
		TermInfoRangeStringDao dao1 = new TermInfoRangeStringDao(keyspace, "LOG_ARCHIVE_TYPE", baseUUID, indexReference);
		int rangeId = 0;
		dao1.iterateOnRange(rangeId);
		TermInfoRangeUuidDao dao2 = new TermInfoRangeUuidDao(keyspace, "SM_UUID", baseUUID, indexReference);
		dao2.iterateOnRange(rangeId);
		TermInfoRangeDatetimeDao dao3 = new TermInfoRangeDatetimeDao(keyspace, "SM_MODIFICATION_DATE", baseUUID, indexReference);
		dao3.iterateOnRange(rangeId);
		TermInfoRangeStringDao dao4 = new TermInfoRangeStringDao(keyspace, "dmc", baseUUID, indexReference);
		dao4.iterateOnRange(rangeId);
		TermInfoRangeStringDao dao5 = new TermInfoRangeStringDao(keyspace, " SM_IS_FROZEN", baseUUID, indexReference);
		dao5.iterateOnRange(rangeId);
		TermInfoRangeDatetimeDao dao6 = new TermInfoRangeDatetimeDao(keyspace, " SM_FINAL_DATE", baseUUID, indexReference);
		dao6.iterateOnRange(rangeId);
	}

	@Test
	public void testIterateOnRangeDateTime() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", "NOMINAL");
		TermInfoRangeDatetimeDao dao = new TermInfoRangeDatetimeDao(keyspace, "SM_CREATION_DATE", baseUUID, indexReference);
		/*
		dao.iterateOnRange(0);
		for (int rangeId : indexReference.getRangeIds()) {
			//if (rangeId != 128) continue;
			System.out.println();
			System.out.println("RangeId : " + rangeId);
			dao.iterateOnRange(rangeId);
		}
		*/
		for (int rangeId = 0; rangeId < 16; rangeId++) {
			System.out.println();
			System.out.println("RangeId : " + rangeId);
			dao.iterateOnRange(rangeId);
		}
	}
	
	@Test
	public void testIterateOnRangeCreationDate() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "SM_CREATION_DATE", "NOMINAL");
		TermInfoRangeDatetimeDao dao = new TermInfoRangeDatetimeDao(keyspace, "SM_CREATION_DATE", baseUUID, indexReference);
		dao.iterateOnRange(128);
	}
	
	@Test
	public void testIterateOnRangeSM_LIFE_CYCLE_REFERENCE_DATE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "SM_LIFE_CYCLE_REFERENCE_DATE", new String[]{"NOMINAL", "BUILDING"});
		TermInfoRangeDatetimeDao dao = new TermInfoRangeDatetimeDao(keyspace, "SM_LIFE_CYCLE_REFERENCE_DATE", baseUUID, indexReference);
		dao.iterateOnRange(99);
	}

	@Test
	public void testIterateOnRange_multithread() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		final TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (final int rangeId : indexReference.getRangeIds()) {
			if (rangeId > 139 && rangeId < 160) {
				Thread thread = new Thread() {
				    public void run() {
				        try {
							dao.iterateOnRange(rangeId);
				        } catch(Exception ex) {
				            System.out.println("RangeId " + rangeId + " : " + ex);
				        }
				    }  
				};
				thread.start();
				threads.add(thread);
			}
		}
		for(Thread thread : threads) {
			thread.join();
		}
	}
	
	@Ignore
	@Test
	public void testdeleteRowSrt() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "srt", "NOMINAL");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "srt", baseUUID, indexReference);
		int rangeId = 0;
		dao.deleteRow(rangeId);
	}

	@Ignore
	@Test
	public void testdeleteRowNce() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "nce", "NOMINAL");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "nce", baseUUID, indexReference);
		//for (int rangeId = 0; rangeId <= 42; rangeId++) {
		//	dao.deleteRow(rangeId);
		//}
		int rangeId = 0;
		dao.deleteRow(rangeId);
	}
	
	@Test
	public void testdeleteRowSM_MODIFICATION_DATE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "SM_MODIFICATION_DATE", "SPLITTING");
		TermInfoRangeStringDao dao = new TermInfoRangeStringDao(keyspace, "SM_MODIFICATION_DATE", baseUUID, indexReference);
		//for (int rangeId = 0; rangeId <= 42; rangeId++) {
		//	dao.deleteRow(rangeId);
		//}
		int rangeId = 0;
		dao.deleteRow(rangeId);
	}

	@Test
	public void testdeleteRowSM_UUID() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, "SM_UUID", "SPLITTING");
		TermInfoRangeUuidDao dao = new TermInfoRangeUuidDao(keyspace, "SM_UUID", baseUUID, indexReference);
		int rangeId = 0;
		dao.deleteRow(rangeId);
	}

	
	@Test
	public void testDelete_cspp() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		
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
		
		for (int i = 0; i< metas.length; i++) {
			String meta = metas[i];
			IndexReference indexReference = new IndexReference();
			indexReference.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
			final TermInfoRangeDao dao;
			if (meta.contains("DATE")) {
				dao = new TermInfoRangeDatetimeDao(keyspace, meta, baseUUID, indexReference);
			}
			else {
				dao = new TermInfoRangeStringDao(keyspace, meta, baseUUID, indexReference);
			}
			System.out.println("Suppression pour " + meta + "...");
			int rangeId = 0;
			dao.deleteRow(rangeId);
		}
        System.out.println("Fini");
	}


	@Test
	public void testDelete_cspp2() throws Exception {
		// Suppression des SM_UUID injectés par erreur sur TermInfoRangeDatetime
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String meta = "SM_UUID";
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, meta, "BUILDING");
		final TermInfoRangeDao dao = new TermInfoRangeDatetimeDao(keyspace, meta, baseUUID, indexReference);
		
		for (int rangeId = 1; rangeId <= 400; rangeId++) {
			System.out.println("Suppression pour " + rangeId + "...");
			dao.deleteRow(rangeId);
		}
        System.out.println("Fini");
	}


	@Test
	public void testDelete_cspp3() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		
		String[] metas = new String[]{
				"SM_LIFE_CYCLE_REFERENCE_DATE", "0",
				"SM_LIFE_CYCLE_REFERENCE_DATE", "3",
				"SM_LIFE_CYCLE_REFERENCE_DATE", "396",
				"SM_LIFE_CYCLE_REFERENCE_DATE", "400", 
				"rib", "0",
				"nic", "170",
				"rdo", "171",
				"srt", "174",
				"srt", "175",
				"srt", "178",
				"srt", "179",
				"srt", "180",
				"srt", "182",
				"srt", "183",
				"srt", "184",
				"srt", "187",
				"srt", "188",
				"srt", "189",
				"srt", "191",
				"srt", "192",
				"srt", "193",
				"srt", "194",
				"srt", "195",
				"srt", "196",
				"srt", "290",
				"srt", "291",
				"srt", "307",
				"srt", "308",
				"srt", "309",
				"srt", "311",
				"rum", "0",
				"nre", "0",
				"psi", "0",
				"nne", "0",
				"SM_ARCHIVAGE_DATE", "0",
				"SM_ARCHIVAGE_DATE", "250"
				};
		
		for (int i = 0; i< metas.length; i+=2) {
			String meta = metas[i];
			int rangeId = Integer.parseInt(metas[i+1]);
			IndexReference indexReference = new IndexReference();
			indexReference.readIndexReference(keyspace, baseUUID, meta, new String[] {"NOMINAL", "BUILDING"});
			final TermInfoRangeDao dao;
			if (meta.contains("DATE")) {
				dao = new TermInfoRangeDatetimeDao(keyspace, meta, baseUUID, indexReference);
			}
			else {
				dao = new TermInfoRangeStringDao(keyspace, meta, baseUUID, indexReference);
			}
			System.out.println("Suppression pour " + meta + " et range " + rangeId + " ...");
			dao.deleteRow(rangeId);
		}
        System.out.println("Fini");
	}

	
	@Test
	public void testGetColumnCount() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		IndexReference indexReference = new IndexReference();
		//String meta = "srn";
		//String meta = "cot&cop&swa&SM_ARCHIVAGE_DATE&";
		//String meta = "SM_ARCHIVAGE_DATE";
		String meta = "cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
		indexReference.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
		final TermInfoRangeDao dao = TermInfoRangeDaoFactory.get(keyspace, meta, baseUUID, indexReference);
		final int[] rangeIds = indexReference.getRangeIds();
		int maxRangeId = Collections.max(Arrays.asList(ArrayUtils.toObject(rangeIds)));
		final int[] counters = new int[maxRangeId+1];
		ExecutorService executor = Executors.newFixedThreadPool(36);
		//ExecutorService executor = Executors.newFixedThreadPool(9);
		
		final class WorkerThread implements Runnable {
		    private int rangeId;
		    public WorkerThread(int rangeId){
		        this.rangeId = rangeId;
		    }

		    public void run() {
		        try {
					int count = dao.getColumnCount(rangeId);
					System.out.println("RangeId " + rangeId + " : count=" + count);
					counters[rangeId] = count;
		        } catch(Exception ex) {
		            System.out.println("RangeId " + rangeId + " : " + ex);
		        }
		    }
		}
		
		for (final int rangeId : rangeIds) {
			//if (rangeId == 139 || rangeId >= 160) {
				Runnable worker = new WorkerThread(rangeId);
				executor.execute(worker);
			//}
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        	Thread.sleep(2000);
	    }
        System.out.println("Fini");
		for (final int rangeId : rangeIds) {
			int count = counters[rangeId];
			if (count > 0) {
				System.out.println("RangeId " + rangeId + " : count=" + count);
			}
		}
        
	}
	
	@Test
	public void testGetOneColumnCount() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		//String meta = "cpt&sco&SM_DOCUMENT_TYPE&";
		String meta = "SM_ARCHIVAGE_DATE";
		int rangeId = 299;
		indexReference.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
		final TermInfoRangeDao dao = TermInfoRangeDaoFactory.get(keyspace, meta, baseUUID, indexReference);
		int count = dao.getColumnCount(rangeId);
		System.out.println("Meta " + meta + " - RangeId " + rangeId + " : count=" + count);
	}
	
	@Test
	public void updateOneCount() throws Exception {
		String meta = "nci";
		int rangeId = 134;
		int count = 952970;
		
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		counts.put(rangeId, count);
		indexReference.updateCountInRanges(keyspace, baseUUID, meta, counts);
	}
	
	@Test
	public void updateOneCount_PseudoSiret() throws Exception {
		String meta = "psi";
		int rangeId = 0;
		int count = 5000;
		
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		counts.put(rangeId, count);
		indexReference.updateCountInRanges(keyspace, baseUUID, meta, counts);
		indexReference.updateTotalIndexUseCount(keyspace, baseUUID, meta, count);
		indexReference.updateDistinctIndexUseCount(keyspace, baseUUID, meta, count);
	}

	

	@Test
	public void countAndUpdateColumnCount_cspp() throws Exception {
		//countAndUpdateColumnCount("SAE-PROD", "mi1", "BUILDING");
		//countAndUpdateColumnCount("GNT-PROD", "SM_CREATION_DATE", "NOMINAL");
		//countAndUpdateColumnCount("GNT-PROD", "cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&", "NOMINAL");
		//countAndUpdateColumnCount("GNT-PROD", "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&", "NOMINAL");
		countAndUpdateColumnCount("SAE-PROD", "nce", "NOMINAL");
	}
	
	public void countAndUpdateColumnCount(String baseName, String meta, String indexState) throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, baseName);
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, meta, indexState);
		final TermInfoRangeDao dao = TermInfoRangeDaoFactory.get(keyspace, meta, baseUUID, indexReference);
		
		final int[] rangeIds = indexReference.getRangeIds();
		int maxRangeId = Collections.max(Arrays.asList(ArrayUtils.toObject(rangeIds)));
		final int[] counters = new int[maxRangeId+1];
		ExecutorService executor = Executors.newFixedThreadPool(36);
		
		final class WorkerThread implements Runnable {
		    private int rangeId;
		    public WorkerThread(int rangeId){
		        this.rangeId = rangeId;
		    }

		    public void run() {
		        try {
					int count = dao.getColumnCount(rangeId);
					System.out.println("RangeId " + rangeId + " : count=" + count);
					counters[rangeId] = count;
		        } catch(Exception ex) {
		            System.out.println("RangeId " + rangeId + " : " + ex);
		        }
		    }
		}
		
		for (final int rangeId : rangeIds) {
			Runnable worker = new WorkerThread(rangeId);
			executor.execute(worker);
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        	Thread.sleep(2000);
	    }
        System.out.println("Fini");
		// Mise à jour
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		for (final int rangeId : rangeIds) {
			int count = counters[rangeId];
			System.out.println("RangeId " + rangeId + " : count=" + count);
			counts.put(rangeId, count);
		}
		indexReference.updateCountInRanges(keyspace, baseUUID, meta, counts);
	}

	@Test
	public void countAndUpdateColumnCount_extended_cspp() throws Exception {
		boolean dryRun = false;
		countAndUpdateColumnCount_extended("SAE-PROD", "srn", "NOMINAL", 36, dryRun);
	}
	
	@Test
	public void countAndUpdateColumnCount_extended2() throws Exception {
		boolean dryRun = false;
		//countAndUpdateColumnCount_extended("GNT-PROD", "drh&cop&SM_CREATION_DATE&", "NOMINAL", 1, dryRun);
		countAndUpdateColumnCount_extended("GNT-PROD", "SM_CREATION_DATE", "NOMINAL", 1, dryRun);
	}

	public void countAndUpdateColumnCount_extended(String baseName, String meta, String indexState, int poolSize, boolean dryRun) throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, baseName);
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, meta, indexState);
		final TermInfoRangeDao dao = TermInfoRangeDaoFactory.get(keyspace, meta, baseUUID, indexReference);
		
		final int[] rangeIds = indexReference.getRangeIds();
		int maxRangeId = Collections.max(Arrays.asList(ArrayUtils.toObject(rangeIds)));
		final int[] counters = new int[maxRangeId+1];
		final int[] distinctCounters = new int[maxRangeId+1];
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);
		
		final class WorkerThread implements Runnable {
		    private int rangeId;
		    public WorkerThread(int rangeId){
		        this.rangeId = rangeId;
		    }

		    public void run() {
		        try {
					int[] count = dao.getDistinctColumnCount(rangeId);
					System.out.println("RangeId " + rangeId + " : count=" + count[0] + "," + count[1]);
					counters[rangeId] = count[0];
					distinctCounters[rangeId] = count[1];
		        } catch(Exception ex) {
		            System.out.println("RangeId " + rangeId + " : " + ex);
		        }
		    }
		}
		
		for (final int rangeId : rangeIds) {
			Runnable worker = new WorkerThread(rangeId);
			executor.execute(worker);
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        	Thread.sleep(2000);
	    }
        System.out.println("Fini");
		// Mise à jour du count de chaque range
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		for (final int rangeId : rangeIds) {
			int count = counters[rangeId];
			System.out.println("RangeId " + rangeId + " : count=" + count);
			counts.put(rangeId, count);
		}
		if (!dryRun) indexReference.updateCountInRanges(keyspace, baseUUID, meta, counts);
		
		// Mise à jour des totaux
        int totalCount = 0, totalDistinctCount = 0;
		for (final int rangeId : rangeIds) {
			totalCount += counters[rangeId];
			totalDistinctCount += distinctCounters[rangeId];
		}
		System.out.println("totalCount = " + totalCount);
		System.out.println("totalDistinctCount = " + totalDistinctCount);
		if (!dryRun) {
			indexReference.updateTotalIndexUseCount(keyspace, baseUUID, meta, totalCount);
			indexReference.updateDistinctIndexUseCount(keyspace, baseUUID, meta, totalDistinctCount);
		}
	}
	
	@Test
	public void count_cspp() throws Exception {
		//UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "GNT-PROD");
		
		String[] metas = new String[]{
				//"mi1",
				//"mi2",
				//"mi3",
				//"mi4",
				//"mi5",
				//"iti",
				"srn",
				"nce",
				"npe",
				"nci",
				"den",
				"SM_CREATION_DATE",
				"SM_LIFE_CYCLE_REFERENCE_DATE",
				"SM_ARCHIVAGE_DATE",
				"SM_MODIFICATION_DATE"
				};
		final int[] counters = new int[metas.length];
		ExecutorService executor = Executors.newFixedThreadPool(36);

		final class WorkerThread implements Runnable {
			private int rangeId;
		    private TermInfoRangeDao dao;
		    private int counterIndex;
		    public WorkerThread(int rangeId, TermInfoRangeDao dao, int counterIndex){
		        this.rangeId = rangeId;
		        this.dao = dao;
		        this.counterIndex = counterIndex;
		    }

		    public void run() {
		        try {
					int count = dao.getColumnCount(rangeId);
					System.out.println("RangeId " + rangeId + " : count=" + count);
					counters[counterIndex] = count;
		        } catch(Exception ex) {
		            System.out.println("RangeId " + rangeId + " : " + ex);
		        }
		    }
		}
		
		for (int i = 0; i< metas.length; i++) {
			String meta = metas[i];
			IndexReference indexReference = new IndexReference();
			indexReference.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
			final TermInfoRangeDao dao = TermInfoRangeDaoFactory.get(keyspace, meta, baseUUID, indexReference);
			
			final int[] rangeIds = indexReference.getRangeIds();
			int maxRangeId = Collections.max(Arrays.asList(ArrayUtils.toObject(rangeIds)));
			Runnable worker = new WorkerThread(maxRangeId, dao, i);
			executor.execute(worker);
		}
		
		executor.shutdown();
        while (!executor.isTerminated()) {
        	Thread.sleep(2000);
	    }
        System.out.println("Fini");
        for (int i = 0; i< metas.length; i++) {
			int count = counters[i];
			System.out.println("RangeId " + metas[i] + " : count=" + count);
		}
	}

	
}
