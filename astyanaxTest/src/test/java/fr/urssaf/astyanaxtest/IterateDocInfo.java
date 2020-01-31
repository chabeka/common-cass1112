package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.SortedMap;

import javax.annotation.Nullable;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.ExceptionCallback;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.mapping.MappingCache;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.CheckpointManager;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.RangeBuilder;

import fr.urssaf.astyanaxtest.dao.DocInfoCF;

public class IterateDocInfo {

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
		//servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
		//servers = "cnp69gntcas1:9160, cnp69gntcas2:9160, cnp69gntcas3:9160";
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
		servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160";	// Charge

		AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
				"root", "regina4932");

		AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
				.forCluster("Docubase")
				.forKeyspace("Docubase")
				.withAstyanaxConfiguration(
						new AstyanaxConfigurationImpl()
						//.setDiscoveryType(NodeDiscoveryType.NONE)
						.setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
						.setDefaultReadConsistencyLevel(ConsistencyLevel.CL_ONE)
						.setDefaultWriteConsistencyLevel(ConsistencyLevel.CL_QUORUM)
						.setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE)
						)
				.withConnectionPoolConfiguration(
						new ConnectionPoolConfigurationImpl("MyConnectionPool")
								.setPort(9160).setMaxConnsPerHost(1)
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
	/**
	 * Exemple d'itération sur les lignes de DocInfo : comptage des documents
	 */
	public void testCountDocuments() throws Exception {
		OperationResult<Rows<String, String>> rows = keyspace
				.prepareQuery(DocInfoCF.get()).getAllRows()
				.setRowLimit(100)
				// This is the page size
				// .withColumnRange(new RangeBuilder().setLimit(10).build())
				.withColumnSlice("SM_UUID", "SM_BASE_ID")
				.setExceptionCallback(new ExceptionCallback() {
					public boolean onException(ConnectionException e) {
						Assert.fail(e.getMessage());
						return true;
					}
				}).execute();

		int counter = 0;
		BigInteger startToken = null;
		BigInteger endToken = null;
		for (Row<String, String> row : rows.getResult()) {
			String key = row.getKey();
			//System.out.println("ROW: " + key + " " + row.getColumns().size());
			//System.out.println(KeyToToken(row.getRawKey()));
			endToken = KeyToToken(row.getRawKey());
			//String endTokenAsString = keyspace.getPartitioner().getTokenForKey(row.getRawKey());
			//System.out.println("endTokenAsString="+endTokenAsString);
			if (startToken == null) startToken = endToken;
			ColumnList<String> columns = row.getColumns();
			String base = columns.getColumnByName("SM_BASE_ID").getStringValue();
			if (base.equals("SAE-PROD")) {
				counter++;
			}
			if (counter > 100000)
				break;
		}
		System.out.println("Counter=" + counter);
		System.out.println("startToken=" + startToken);
		System.out.println("endToken=" + endToken);
		BigInteger diffToken = endToken.subtract(startToken);
		System.out.println("diffToken=" + diffToken);
		BigInteger maxToken = BigInteger.valueOf(2).pow(127).subtract(BigInteger.valueOf(1));
		System.out.println("maxToken=" + maxToken);
		BigInteger facteur = maxToken.divide(diffToken);
		System.out.println("Facteur=" + facteur);
		System.out.println("NbDoc=" + facteur.multiply(BigInteger.valueOf(counter)));
		
	}

	@Test
	/**
	 * Exemple d'itération sur les lignes de DocInfo : comptage des documents
	 */
	public void testCountMetasForSplit() throws Exception {
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
		int metaCount = metas.length;
		int[] counters = new int[metaCount];
		
		OperationResult<Rows<String, String>> rows = keyspace
				.prepareQuery(DocInfoCF.get()).getAllRows()
				.setRowLimit(100)
				// This is the page size
				.withColumnRange(new RangeBuilder().setLimit(100).build())
				.setExceptionCallback(new ExceptionCallback() {
					public boolean onException(ConnectionException e) {
						Assert.fail(e.getMessage());
						return true;
					}
				}).execute();

		int counter = 0;
		BigInteger startToken = null;
		BigInteger endToken = null;
		for (Row<String, String> row : rows.getResult()) {
			String key = row.getKey();
			endToken = KeyToToken(row.getRawKey());
			if (startToken == null) startToken = endToken;
			ColumnList<String> columns = row.getColumns();
			String base = columns.getColumnByName("SM_BASE_ID").getStringValue();
			if (base.equals("SAE-PROD")) {
				for (int i = 0; i < metaCount; i++) {
					if (columns.getColumnByName(metas[i]) != null) counters[i]++;
				}
				counter++;
			}
			if (counter > 50000)
				break;
		}
		System.out.println("Counter=" + counter);
		System.out.println("startToken=" + startToken);
		System.out.println("endToken=" + endToken);
		BigInteger diffToken = endToken.subtract(startToken);
		System.out.println("diffToken=" + diffToken);
		BigInteger maxToken = BigInteger.valueOf(2).pow(127).subtract(BigInteger.valueOf(1));
		System.out.println("maxToken=" + maxToken);
		BigInteger facteur = maxToken.divide(diffToken);
		System.out.println("Facteur=" + facteur);
		int nbDocs = facteur.multiply(BigInteger.valueOf(counter)).intValue();
		System.out.println("NbDoc=" + nbDocs);
		float f = nbDocs / counter;
				
		for (int i = 0; i < metaCount; i++) {
			String meta = metas[i];
			int count = (int) (counters[i] * f);
			int splitCount = (int) (400 * counters[i] / counter);
			System.out.println("Meta " + meta + " : count=" + count + " - splits=" + splitCount);
		}
		
	}
	
	public static BigInteger KeyToToken(ByteBuffer key) {
		byte[] result = DigestUtils.md5(key.array());
		BigInteger hash = new BigInteger(result);
		return hash.abs();
	}
	
	@Test
	/**
	 * Exemple d'itération sur les lignes de DocInfo : trouver des documents par filtrage
	 */
	public void testFindBigDocuments() throws Exception {
		OperationResult<Rows<String, String>> rows = keyspace
				.prepareQuery(DocInfoCF.get()).getAllRows()
				.setRowLimit(200)
				// This is the page size
				// .withColumnRange(new RangeBuilder().setLimit(10).build())
				.withColumnSlice("SM_UUID", "SM_SIZE")
				.setExceptionCallback(new ExceptionCallback() {
					public boolean onException(ConnectionException e) {
						Assert.fail(e.getMessage());
						return true;
					}
				}).execute();

		int counter = 0;
		for (Row<String, String> row : rows.getResult()) {
			ColumnList<String> columns = row.getColumns();
			String key = row.getKey();
			String uuid = columns.getColumnByName("SM_UUID").getStringValue();
			String sizeAsString = columns.getColumnByName("SM_SIZE").getStringValue();
			int size = Integer.parseInt(sizeAsString);
			if (size > 2000000 && size < 6000000) {
				System.out.println("uuid: " + uuid + " - " + size);
			}
			counter++;
			if (counter % 100000 == 0) {
				System.out.println("Counter : " + counter);
			}
		}
	}

	@Test
	/**
	 * Exemple d'itération sur les lignes de DocInfo : comptage des documents
	 */
	public void testGetDocumentSize() throws Exception {
		OperationResult<Rows<String, String>> rows = keyspace
				.prepareQuery(DocInfoCF.get()).getAllRows()
				.setRowLimit(100)
				// This is the page size
				// .withColumnRange(new RangeBuilder().setLimit(10).build())
				.withColumnSlice("SM_UUID", "SM_BASE_ID", "SM_SIZE")
				.setExceptionCallback(new ExceptionCallback() {
					public boolean onException(ConnectionException e) {
						Assert.fail(e.getMessage());
						return true;
					}
				}).execute();

		int counterGNT = 0;
		int counterOther = 0;
		long sizeGNT = 0;
		long sizeOther = 0;
		for (Row<String, String> row : rows.getResult()) {
			String key = row.getKey();
			//System.out.println("ROW: " + key + " " + row.getColumns().size());
			//System.out.println(KeyToToken(row.getRawKey()));
			//String endTokenAsString = keyspace.getPartitioner().getTokenForKey(row.getRawKey());
			//System.out.println("endTokenAsString="+endTokenAsString);
			ColumnList<String> columns = row.getColumns();
			String base = columns.getColumnByName("SM_BASE_ID").getStringValue();
			String sizeAsString = columns.getColumnByName("SM_SIZE").getStringValue();
			long size = Long.parseLong(sizeAsString);
			if (base.equals("SAE-PROD")) {
				counterGNT++;
				sizeGNT += size;
			}
			else {
				counterOther++;
				sizeOther += size;
			}
			if (counterGNT >= 100000)
				break;
		}
		System.out.println("CounterGNT=" + counterGNT);
		System.out.println("sizeGNT=" + sizeGNT);
		System.out.println("sizeGNT/doc=" + sizeGNT/counterGNT);
		System.out.println("CounterOther=" + counterOther);
		System.out.println("sizeOther=" + sizeOther);
		System.out.println("sizeOther/doc=" + sizeOther/counterOther);
	}
}
