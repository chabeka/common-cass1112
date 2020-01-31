package fr.urssaf.astyanaxtest;

import java.util.UUID;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

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
import fr.urssaf.astyanaxtest.dao.CategoriesReference;
import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDaoFactory;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDatetimeDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeStringDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeUuidDao;
import fr.urssaf.astyanaxtest.spliter.IndexSpliter;
import fr.urssaf.astyanaxtest.spliter.MultiIndexSpliter;

public class Main {
	
	public static void main(String[] args) throws Exception {
		OptionParser parser = new OptionParser();
        parser.accepts("cluster", "Le cluster : CVE|PROD" ).withRequiredArg().required().describedAs("CLUSTER");
        parser.accepts("base", "Le nom de la base. Exemple : SAE-PROD").withRequiredArg().describedAs("BASE").defaultsTo("SAE-PROD");
        parser.accepts("progressFile", "Le chemin du fichier de progression").withRequiredArg().required().describedAs("PROGRESS_FILE");
        OptionSet options;
        try {
        	options = parser.parse(args);
        }
        catch (OptionException e) {
        	System.out.println(e.getMessage());
        	parser.printHelpOn( System.out );
        	return;
        }
        String cluster = (String) options.valueOf("cluster");
        Keyspace keyspace = getKeyspace(cluster, 36);
        MappingUtil mapper = new MappingUtil(keyspace, new MappingCache());
        
        String base = (String) options.valueOf("base");
        String progressFile = (String) options.valueOf("progressFile");
        
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, base);
		
		String[] metas_ori = new String[]{
				//"LOG_ARCHIVE_BEGIN_DATE",
				//"LOG_ARCHIVE_END_DATE",
				//"LOG_ARCHIVE_TYPE",
				//"PREVIOUS_LOG_ARCHIVE_UUID",
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
				//"cot&cop&mch&",		<- DomaineCotisant-CodeOrganismeProprietaire-MontantCheque 
				//"cot&cop&mre&",		<- DomaineCotisant-CodeOrganismeProprietaire-MontantRegle 
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
				//"dmc",							<- type DATE		(date mise en corbeille)
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
		String[] metas = new String[]{
				"SM_MODIFICATION_DATE",
				"SM_ARCHIVAGE_DATE",
				"SM_CREATION_DATE",
				"SM_LIFE_CYCLE_REFERENCE_DATE"
		};
		TermInfoRangeDao[] termInfoRangeDaos = new TermInfoRangeDao[metas.length];
		for (int i = 0; i< metas.length; i++) {
			IndexReference indexReference = new IndexReference();
			indexReference.readIndexReference(keyspace, baseUUID, metas[i], new String[] {"NOMINAL","BUILDING"});
			termInfoRangeDaos[i] = TermInfoRangeDaoFactory.get(keyspace, metas[i], baseUUID, indexReference);
		}
		
		MultiIndexSpliter spliter = new MultiIndexSpliter(keyspace, termInfoRangeDaos);
		//spliter.splitIndex(index, "2058", startToken, endToken, progressFile);
		int blocCount = 36;
		spliter.splitIndex_multithread("2058", keyspace.getPartitioner().getMinToken(), keyspace.getPartitioner().getMaxToken(), blocCount, progressFile);
	}
	
	public static void main_cspp(String[] args) throws Exception {
		OptionParser parser = new OptionParser();
        parser.accepts("cluster", "Le cluster : CVE|PROD" ).withRequiredArg().required().describedAs("CLUSTER");
        parser.accepts("base", "Le nom de la base. Exemple : SAE-PROD").withRequiredArg().describedAs("BASE").defaultsTo("SAE-PROD");
        parser.accepts("indexState", "L'état de l'index : NOMINAL, BUILDING...").withRequiredArg().required().describedAs("STATE");
        parser.accepts("progressFile", "Le chemin du fichier de progression").withRequiredArg().required().describedAs("PROGRESS_FILE");
        OptionSet options;
        try {
        	options = parser.parse(args);
        }
        catch (OptionException e) {
        	System.out.println(e.getMessage());
        	parser.printHelpOn( System.out );
        	return;
        }
        
        String cluster = (String) options.valueOf("cluster");
        Keyspace keyspace = getKeyspace(cluster, 36);
        MappingUtil mapper = new MappingUtil(keyspace, new MappingCache());
        
        String base = (String) options.valueOf("base");
        String indexState = (String) options.valueOf("indexState");
        String progressFile = (String) options.valueOf("progressFile");
        
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, base);
		
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
		TermInfoRangeDao[] termInfoRangeDaos = new TermInfoRangeDao[metas.length];
		for (int i = 0; i< metas.length; i++) {
			IndexReference indexReference = new IndexReference();
			indexReference.readIndexReference(keyspace, baseUUID, metas[i], indexState);
			if (metas[i].contains("DATE")) {
				termInfoRangeDaos[i] = new TermInfoRangeDatetimeDao(keyspace, metas[i], baseUUID, indexReference);
			}
			else {
				termInfoRangeDaos[i] = new TermInfoRangeStringDao(keyspace, metas[i], baseUUID, indexReference);
			}
		}
		
		MultiIndexSpliter spliter = new MultiIndexSpliter(keyspace, termInfoRangeDaos);
		//spliter.splitIndex(index, "2058", startToken, endToken, progressFile);
		int blocCount = 36;
		spliter.splitIndex_multithread("2058", keyspace.getPartitioner().getMinToken(), keyspace.getPartitioner().getMaxToken(), blocCount, progressFile);
	}

	public static void main_mutithread(String[] args) throws Exception {
		OptionParser parser = new OptionParser();
        parser.accepts("cluster", "Le cluster : CVE|PROD" ).withRequiredArg().required().describedAs("CLUSTER");
        parser.accepts("base", "Le nom de la base. Exemple : SAE-PROD").withRequiredArg().describedAs("BASE").defaultsTo("SAE-PROD");
        parser.accepts("indexToSplit", "Le nom de l'index à spliter. Exemple : srt").withRequiredArg().required().describedAs("INDEX");
        parser.accepts("indexState", "L'état de l'index : NOMINAL, BUILDING...").withRequiredArg().required().describedAs("STATE");
        parser.accepts("indexType", "Le type de l'index. STRING|DATETIME|UUID").withRequiredArg().describedAs("TYPE").defaultsTo("STRING");
        parser.accepts("progressFile", "Le chemin du fichier de progression").withRequiredArg().required().describedAs("PROGRESS_FILE");
        OptionSet options;
        try {
        	options = parser.parse(args);
        }
        catch (OptionException e) {
        	System.out.println(e.getMessage());
        	parser.printHelpOn( System.out );
        	return;
        }
        
        String cluster = (String) options.valueOf("cluster");
        Keyspace keyspace = getKeyspace(cluster, 36);
        MappingUtil mapper = new MappingUtil(keyspace, new MappingCache());
        
        String index = (String) options.valueOf("indexToSplit");
        String base = (String) options.valueOf("base");
        String indexState = (String) options.valueOf("indexState");
        String indexType = (String) options.valueOf("indexType");
        String progressFile = (String) options.valueOf("progressFile");
        
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, base);
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, index, indexState);
		TermInfoRangeDao termInfoRangeDao;
		if ("STRING".equals(indexType)) {
			termInfoRangeDao = new TermInfoRangeStringDao(keyspace, index, baseUUID, indexReference);
		}
		else if ("DATETIME".equals(indexType)) {
			termInfoRangeDao = new TermInfoRangeDatetimeDao(keyspace, index, baseUUID, indexReference);
		}
		else  if ("UUID".equals(indexType)) {
			termInfoRangeDao = new TermInfoRangeUuidDao(keyspace, index, baseUUID, indexReference);
		}
		else {
			System.out.println("Valeur du paramètre --indexType incorrect : " + indexType);
			return;
		}
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		//spliter.splitIndex(index, "2058", startToken, endToken, progressFile);
		int blocCount = 36;
		spliter.splitIndex_multithread(index, "2058", keyspace.getPartitioner().getMinToken(), keyspace.getPartitioner().getMaxToken(), blocCount, progressFile);
	}
	
	public static void main_monothread(String[] args) throws Exception {
		OptionParser parser = new OptionParser();
        parser.accepts("cluster", "Le cluster : CVE|PROD" ).withRequiredArg().required().describedAs("CLUSTER");
        parser.accepts("base", "Le nom de la base. Exemple : SAE-PROD").withRequiredArg().describedAs("BASE").defaultsTo("SAE-PROD");
        parser.accepts("indexToSplit", "Le nom de l'index à spliter. Exemple : srt").withRequiredArg().required().describedAs("INDEX");
        parser.accepts("indexState", "L'état de l'index : NOMINAL, BUILDING...").withRequiredArg().required().describedAs("STATE");
        parser.accepts("startToken", "Le token de début").withRequiredArg().describedAs("START_TOKEN");
        parser.accepts("endToken", "Le token de fin").withRequiredArg().describedAs("END_TOKEN");
        parser.accepts("progressFile", "Le chemin du fichier de progression").withRequiredArg().required().describedAs("PROGRESS_FILE");
        OptionSet options;
        try {
        	options = parser.parse(args);
        }
        catch (OptionException e) {
        	System.out.println(e.getMessage());
        	parser.printHelpOn( System.out );
        	return;
        }
        
        String cluster = (String) options.valueOf("cluster");
        Keyspace keyspace = getKeyspace(cluster, 1);
        MappingUtil mapper = new MappingUtil(keyspace, new MappingCache());
        
        String startToken;
        if (options.hasArgument("startToken")) {
        	startToken = (String) options.valueOf("startToken");
        }
        else {
        	startToken = keyspace.getPartitioner().getMinToken();
        }
        String endToken;
        if (options.hasArgument("endToken")) {
        	endToken = (String) options.valueOf("endToken");
        }
        else {
        	endToken = keyspace.getPartitioner().getMaxToken();
        }
        System.out.println("startToken="+startToken);
        System.out.println("endToken="+endToken);
        String index = (String) options.valueOf("indexToSplit");
        String base = (String) options.valueOf("base");
        String indexState = (String) options.valueOf("indexState");
        String progressFile = (String) options.valueOf("progressFile");
        
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, base);
		IndexReference indexReference = new IndexReference();
		indexReference.readIndexReference(keyspace, baseUUID, index, indexState);
		TermInfoRangeStringDao termInfoRangeDao = new TermInfoRangeStringDao(keyspace, index, baseUUID, indexReference);
		IndexSpliter spliter = new IndexSpliter(keyspace, termInfoRangeDao);
		spliter.splitIndex(index, "2058", startToken, endToken, progressFile);
	}

	private static Keyspace getKeyspace(String cluster, int maxConnsPerHost) throws Exception {
		String servers;
		if ("CVE".equals(cluster)) {
			servers = "cnp3gnscvecas01.cve.recouv:9160,cnp6gnscvecas01.cve.recouv:9160,cnp7gnssaecvecas01.cve.recouv:9160,"+
					"cnp3gnscvecas02.cve.recouv:9160,cnp6gnscvecas02.cve.recouv:9160,cnp7gnssaecvecas02.cve.recouv:9160,"+
					"cnp3gnscvecas03.cve.recouv:9160,cnp6gnscvecas03.cve.recouv:9160,cnp7gnssaecvecas03.cve.recouv:9160,"+
					"cnp3gnscvecas04.cve.recouv:9160,cnp6gnscvecas04.cve.recouv:9160,cnp7gnssaecvecas04.cve.recouv:9160,"+
					"cnp3gnscvecas05.cve.recouv:9160,cnp6gnscvecas05.cve.recouv:9160,cnp7gnssaecvecas05.cve.recouv:9160,"+
					"cnp3gnscvecas06.cve.recouv:9160,cnp6gnscvecas06.cve.recouv:9160,cnp7gnssaecvecas06.cve.recouv:9160,"+
					"cnp3gnscvecas07.cve.recouv:9160,cnp6gnscvecas07.cve.recouv:9160,cnp7gnssaecvecas07.cve.recouv:9160,"+
					"cnp3gnscvecas08.cve.recouv:9160,cnp6gnscvecas08.cve.recouv:9160,cnp7gnssaecvecas08.cve.recouv:9160,"+
					"cnp3gnscvecas09.cve.recouv:9160,cnp6gnscvecas09.cve.recouv:9160,cnp7gnssaecvecas09.cve.recouv:9160,"+
					"cnp3gnscvecas10.cve.recouv:9160,cnp6gnscvecas10.cve.recouv:9160,cnp7gnssaecvecas10.cve.recouv:9160,"+
					"cnp3gnscvecas11.cve.recouv:9160,cnp6gnscvecas11.cve.recouv:9160,cnp7gnssaecvecas11.cve.recouv:9160,"+
					"cnp3gnscvecas12.cve.recouv:9160,cnp6gnscvecas12.cve.recouv:9160,cnp7gnssaecvecas12.cve.recouv:9160,"+
					"cnp3gnscvecas13.cve.recouv:9160,cnp6gnscvecas13.cve.recouv:9160,cnp7gnssaecvecas13.cve.recouv:9160,"+
					"cnp3gnscvecas14.cve.recouv:9160,cnp6gnscvecas14.cve.recouv:9160,cnp7gnssaecvecas14.cve.recouv:9160,"+
					"cnp3gnscvecas15.cve.recouv:9160,cnp6gnscvecas15.cve.recouv:9160,cnp7gnssaecvecas15.cve.recouv:9160,"+
					"cnp3gnscvecas16.cve.recouv:9160,cnp6gnscvecas16.cve.recouv:9160,cnp7gnssaecvecas16.cve.recouv:9160,"+
					"cnp3gnscvecas17.cve.recouv:9160,cnp6gnscvecas17.cve.recouv:9160,cnp7gnssaecvecas17.cve.recouv:9160,"+
					"cnp3gnscvecas18.cve.recouv:9160,cnp6gnscvecas18.cve.recouv:9160,cnp7gnssaecvecas18.cve.recouv:9160,"+
					"cnp3gnscvecas19.cve.recouv:9160,cnp6gnscvecas19.cve.recouv:9160,cnp7gnssaecvecas19.cve.recouv:9160,"+
					"cnp3gnscvecas20.cve.recouv:9160,cnp6gnscvecas20.cve.recouv:9160,cnp7gnssaecvecas20.cve.recouv:9160,"+
					"cnp3gnscvecas21.cve.recouv:9160,cnp6gnscvecas21.cve.recouv:9160,cnp7gnssaecvecas21.cve.recouv:9160,"+
					"cnp3gnscvecas22.cve.recouv:9160,cnp6gnscvecas22.cve.recouv:9160,cnp7gnssaecvecas22.cve.recouv:9160,"+
					"cnp3gnscvecas23.cve.recouv:9160,cnp6gnscvecas23.cve.recouv:9160,cnp7gnssaecvecas23.cve.recouv:9160,"+
					"cnp3gnscvecas24.cve.recouv:9160,cnp6gnscvecas24.cve.recouv:9160,cnp7gnssaecvecas24.cve.recouv:9160";
		}
		else {
			throw new Exception ("Cluster inconnu " + cluster);
		}

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
								.setPort(9160).setMaxConnsPerHost(maxConnsPerHost)
								.setSeeds(servers)
								.setAuthenticationCredentials(credentials))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
				.buildKeyspace(ThriftFamilyFactory.getInstance());

		context.start();
		Keyspace keyspace = context.getClient();
		return keyspace;
	}
}
