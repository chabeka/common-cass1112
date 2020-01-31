package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

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
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.mapping.MappingCache;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.astyanaxtest.dao.BasesReferenceDao;
import fr.urssaf.astyanaxtest.dao.DocInfoCF;
import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;
import fr.urssaf.astyanaxtest.helper.MetaHelper;
import fr.urssaf.astyanaxtest.repair.IndexReferenceRepair;

/**
 * 
 * Permet de réparer IndexReference
 * 
 */
public class IndexReferenceRepairTest {

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
		sysout = new PrintStream("d:/temp/out.txt");

	}


	@Test
	public void repair_cspp() throws Exception {
		IndexReferenceRepair repairer = new IndexReferenceRepair();		
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String file = "D:\\temp\\IndexReference-CSPP-GNS-2017-10-04.txt";
		//repairer.cleanFromTextFile(keyspace, baseUUID, "nci", file);
		
		String[] indexList = new String[]{
				"LOG_ARCHIVE_BEGIN_DATE",
				"LOG_ARCHIVE_END_DATE",
				"LOG_ARCHIVE_TYPE",
				"PREVIOUS_LOG_ARCHIVE_UUID",
				"SM_ARCHIVAGE_DATE",
				"SM_CREATION_DATE",
				//"SM_FINAL_DATE",			<- ne pas indexer
				//"SM_IS_FROZEN",			<- ne pas indexer
				"SM_LIFE_CYCLE_REFERENCE_DATE",
				"SM_MODIFICATION_DATE",
				"SM_UUID",
				"cct",
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
				"dar&cop&SM_ARCHIVAGE_DATE&",
				"den",
				"dmc",
				"drh&cop&SM_CREATION_DATE&",
				"drh&cop&nma&",
				"drh&cop&nma&frd&",
				"drh&cop&pag&SM_CREATION_DATE&",
				"isi",
				"iti",
				"itm",
				"naw",
				"nce",
				"nci",
				"nic",
				"nid",
				"nis",
				"nne",
				"nns",
				"npe",
				"nsa",
				"psi",
				"rdu",
				"red",
				"rum",
				"srn",
				"srt",
		};

		
		for (String indexName : indexList) {
			System.out.println("Réparation de " + indexName + "...");
			repairer.cleanFromTextFile(keyspace, baseUUID, indexName, file);
		}
		
	}

	@Test
	public void repair_cspp2() throws Exception {
		IndexReferenceRepair repairer = new IndexReferenceRepair();		
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		String file = "D:\\temp\\IndexRefTest.txt";
		repairer.cleanFromTextFile(keyspace, baseUUID, "SM_ARCHIVAGE_DATE", file);
	}

}
