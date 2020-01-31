package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

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
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.model.Equality;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.RangeBuilder;

import fr.urssaf.astyanaxtest.dao.BaseCategoriesReferenceDao;
import fr.urssaf.astyanaxtest.dao.BasesReferenceDao;
import fr.urssaf.astyanaxtest.dao.IndexCounterCF;
import fr.urssaf.astyanaxtest.dao.IndexCounterKey;
import fr.urssaf.astyanaxtest.dao.IndexReferenceCF;
import fr.urssaf.astyanaxtest.dao.SystemEventLogByTimeSerializedCF;
import fr.urssaf.astyanaxtest.dao.SystemEventLogByTimeSerializedCompositeColumnDefinition;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class IndexCounterTest {
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
	public void testReadRow() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexCounterKey key = new IndexCounterKey("SM_UUID", baseUUID, "INSERT");
		
		RowQuery<IndexCounterKey, byte[]> query = keyspace
			.prepareQuery(IndexCounterCF.get())
			.getKey(key)
			.withColumnRange(new RangeBuilder().setLimit(10).build());
		ColumnList<byte[]> columns = query.execute().getResult();
        for (Column<byte[]> c : columns) {
        	sysout.println("Name :" + ConvertHelper.getReadableUTF8String(c.getName()));
        	sysout.println("Value :" + c.getStringValue());
        }
	}
	
	@Test
	public void testDeleteRow() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		IndexCounterKey key = new IndexCounterKey("SM_UUID", baseUUID, "INSERT");
		
		sysout.println("Suppression...");
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexCounterCF.get(), key).delete();
		batch.execute();
		sysout.println("Fini");
	}
}