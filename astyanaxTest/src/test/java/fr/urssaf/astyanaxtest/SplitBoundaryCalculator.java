package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
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
import fr.urssaf.astyanaxtest.dao.CategoriesReference;
import fr.urssaf.astyanaxtest.dao.DocInfoCF;
import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDao;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDaoFactory;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeStringDao;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;
import fr.urssaf.astyanaxtest.helper.MetaHelper;

/**
 * 
 * Permet de calculer rapidement les bornes de split pour un index composite ou non composite.
 * 
 */
public class SplitBoundaryCalculator {

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
	public void testCalculateSplits_SM_ARCHIVAGE_DATE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 2000000;
		final int nbBuckets = 2000000;  
		final int nbSplits = 250;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "SM_ARCHIVAGE_DATE");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_Siret() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 2000000;
		final int nbBuckets = 2000000;  
		final int nbSplits = 300;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "srt");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_Siren() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 84000;
		final int nbBuckets = 8000;  
		final int nbSplits = 28;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "srn");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_NumeroCompte() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 84000;
		final int nbBuckets = 8000;  
		final int nbSplits = 42;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "nce");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_ArchivageDate() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 84000;
		final int nbBuckets = 8000;  
		final int nbSplits = 42;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "SM_ARCHIVAGE_DATE");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_SM_LIFE_CYCLE_REFERENCE_DATE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 84000;
		final int nbBuckets = 16000;  
		final int nbSplits = 42;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "SM_LIFE_CYCLE_REFERENCE_DATE");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_SM_CREATION_DATE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 84000;
		final int nbBuckets = 8000;  
		final int nbSplits = 42;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "SM_CREATION_DATE");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_SM_MODIFICATION_DATE() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 1000000;
		final int nbBuckets =    1000000;  
		final int nbSplits = 300;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "SM_MODIFICATION_DATE");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}
	
	@Test
	public void testCalculateSplits_nce() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 500000;
		final int nbBuckets =    500000;  
		final int nbSplits = 400;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "nce");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_SM_UUID() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 50000;
		final int nbBuckets =    50000;  
		final int nbSplits = 400;
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, "SM_UUID");
		System.out.println("Splits : " + getSplitsAsString(splits));
	}
	
	@Test
	public void testCalculateSplits_Composite() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 50000;
		final int nbBuckets =    50000;  
		final int nbSplits = 400;
		ArrayList<String> metas = new ArrayList<String>();
		metas.add("cot");
		metas.add("cag");
		metas.add("SM_CREATION_DATE");
		String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits, metas);
		System.out.println("Splits : " + getSplitsAsString(splits));
	}

	@Test
	public void testCalculateSplits_cspp() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 10000;
		final int nbBuckets =    10000;  
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
		int[] nbSplits = new int[] {
				10,10,10,10,10,384,137,45,56,293,400,400
		};
		for(int i = 0; i < metas.length; i++) {
			try {
				System.out.println("Calcul des splits pour : " + metas[i]);
				String[] splits = calculateSplits(nbDocsToRead, baseUUID.toString(), nbBuckets, nbSplits[i], metas[i]);
				System.out.println("Splits : " + getSplitsAsString(splits));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void Test() throws Exception {
		String[] metas = new String[]{
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

		TermInfoRangeDao[] termInfoRangeDaos = new TermInfoRangeDao[metas.length];
		for (int i = 0; i< metas.length; i++) {
			String meta = metas[i];
			CategoriesReference cat = new CategoriesReference(keyspace);
			String metaType = cat.getCategoryType(meta);
			System.out.println("Meta : " + meta + " - " + metaType);
		}		

	}
	
	@Test
	/**
	 * Fait le nécessaire pour calculer et créer les ranges dans IndexReference :
	 * 		- pour chaque index :
	 * 			- parcours de DocInfo pour estimer le nombre de ranges nécessaires
	 * 			- parcours de DocInfo pour estimer les bornes des ranges
	 * 			- création de ces bornes dans IndexReference
	 * Cette procédure a été jouée sur l'environnement CSPP GNS, avant purge de 
	 * TermInfoRangeUUID, TermInfoRangeDateTime,  TermInfoRangeString et réindexation.
	 * 
	 * @throws Exception
	 */
	public void createRanges_cspp() throws Exception {
		UUID baseUUID = BasesReferenceDao.getBaseUUID(mapper, "SAE-PROD");
		final int nbDocsToRead = 50000;
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
		int[] counts = calculateSplitsCount(nbDocsToRead, baseUUID.toString(), indexList);
		for (int i = 0; i < indexList.length; i++) {
			int nbSplits = counts[i];
			if (nbSplits > 1) {
				ArrayList<String> realMetas = MetaHelper.indexToMetas(indexList[i]);
				int nbDocsToRead2 = nbSplits * 500;
				int nbBuckets = nbDocsToRead2;		
				System.out.println("Traitement de l'index : " + indexList[i]);
				String[] splits = calculateSplits(nbDocsToRead2 , baseUUID.toString(), nbBuckets, nbSplits, realMetas);
				System.out.println("Splits : " + getSplitsAsString(splits));
				
				// Ecriture des nouveaux ranges dans IndexReference
				IndexReference dao = new IndexReference();
				dao.setRanges(splits);
				int approximateDocCountPerSplit = 1000000;
				dao.writeIndexReference(keyspace, baseUUID, indexList[i], approximateDocCountPerSplit);
			}
		}
	}

	
	@Test
	public void calculateSplitsUUID() {
		BigInteger maxUUID = BigInteger.valueOf(2).pow(128).subtract(BigInteger.valueOf(1));
		System.out.println("maxUUID="+maxUUID);
		long least = bigIntegerToLong(maxUUID, 0, 63);
		System.out.println("least="+least);
		long most = bigIntegerToLong(maxUUID, 64, 127);
		System.out.println("most="+most);
		UUID uuid = new UUID(most, least);
		System.out.println("uuid="+uuid);
		
		int nbSplits = 400;
		BigInteger interval = maxUUID.divide(BigInteger.valueOf(nbSplits));
		BigInteger counter = BigInteger.valueOf(0);
		for (int i = 0; i < nbSplits; i++) {
			counter = counter.add(interval);
			System.out.println(bigIntegerToUUID(counter));
		}
		
	}

	private UUID bigIntegerToUUID(BigInteger big) {
		long least = bigIntegerToLong(big, 0, 63);
		long most = bigIntegerToLong(big, 64, 127);
		UUID uuid = new UUID(most, least);
		return uuid;		
	}

	private long bigIntegerToLong(BigInteger big, int firstBit, int lastBit) {
		long result = 0;
		for (int bit = firstBit; bit <= lastBit; bit++) {
			boolean isSet = big.testBit(bit);
			if (isSet) result = result | (1l << bit-firstBit);
		}
		return result;
	}
	
	private String[] calculateSplits(int nbDocsToRead, String baseUUID, int nbBuckets, int nbSplits, String columnName) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        list.add(columnName);
		return calculateSplits(nbDocsToRead, baseUUID, nbBuckets, nbSplits, list);
	}
	
	/**
	 * Estime nombre de ranges à créer pour chaque index présent dans indexList
	 * @param nbDocsToRead : nombre de documents à lire dans DocInfo
	 * @param baseUUID	: l'UUID de la base
	 * @param indexList	: liste des index qui nous intéressent
	 * @throws Exception
	 */
	public int[] calculateSplitsCount(int nbDocsToRead, String baseUUID, String[] indexList) throws Exception {
		int indexCount = indexList.length;
		ArrayList<String>[] realMetasArray = new ArrayList[indexCount]; 
		for (int i = 0; i < indexCount; i++) {
			realMetasArray[i] = MetaHelper.indexToMetas(indexList[i]);
		}
		int[] counts = new int[indexCount];
		int[] result = new int[indexCount];
		
		OperationResult<Rows<String, String>> rows = keyspace
				.prepareQuery(DocInfoCF.get()).getAllRows()
				.setRowLimit(200)
				// On ne prend pas la colonne \xef\xbf\xbfMETA\xef\xbf\xbf
				.withColumnRange("", String.valueOf((char)(0xef)), false, 1000)
				.execute();

		// Parcours des documents à partir de DocInfo
		int counter = 0;		
		for (Row<String, String> row : rows.getResult()) {
			
			// Est-ce que ce doc nous intéresse ?
			ColumnList<String> columns = row.getColumns();
			if (columns.size() == 0) continue;
			Column<String> colBase = columns.getColumnByName("SM_BASE_UUID");
			if (colBase == null || !colBase.getStringValue().toLowerCase().equals(baseUUID)) continue;
			
			// On cherche les index concernés par ce document
			for (int i = 0; i < indexCount; i++) {
				boolean isIndexConcerned = true;		// Vrai si cet index est concerné par le document courant
				ArrayList<String> realMetas = realMetasArray[i];
				for(String realMeta : realMetas) {
					if (columns.getColumnByName(realMeta) == null) {
						isIndexConcerned = false;
						break;
					}
				}
				if (isIndexConcerned) {
					counts[i]++;
				}
			}
			counter++;
			if (counter % 1000 == 0) {
				System.out.println("counter=" + counter);
			}
			if (counter > nbDocsToRead) {
				break;
			}
		}
		
		// Affichage des compteurs
		for (int i = 0; i < indexCount; i++) {
			String indexName = indexList[i];
			float maxDocForRange = 1000000;
			int totalDocCount = 300000000;
			float nbSplits = (float)(counts[i])/counter * totalDocCount/maxDocForRange;
			if (nbSplits > 1.1) {
				System.out.println(indexName + " : " + nbSplits + " !!!!!!!!!");
				result[i] = (int) Math.ceil(nbSplits);
			}
			else {
				System.out.println(indexName + " : " + nbSplits);
				result[i] = 1;
			}
		}
		return result;
	}
	
	/**
	 * Calcul les ranges des splits pour un index
	 * @param nbDocsToRead	: nombre de documents à lire (taille de l'échantillon)
	 * @param nbBuckets		: nombre de buckets à garder en mémoire (ex : 1000)
	 * @param nbSplits		: nombre de splits à faire
	 * @param columnName	: nom de la colonne correspondant à l'index
	 * @return les splits, par exemple : [min_lower_bound TO 20130124012514529[|[20130124012514529 TO 20131130031701620[|[20131130031701620 TO max_upper_bound]
	 * @throws Exception
	 * @return Tableau donnant la limite basse de chaque split
	 */
	private String[] calculateSplits(int nbDocsToRead, String baseUUID, int nbBuckets, int nbSplits, ArrayList<String> columnNames) throws Exception {
		ArrayList<String> newColumnNames = (ArrayList<String>) columnNames.clone();
		newColumnNames.add("SM_BASE_UUID");
		OperationResult<Rows<String, String>> rows = keyspace
				.prepareQuery(DocInfoCF.get()).getAllRows()
				.setRowLimit(200)
				// This is the page size
				// .withColumnRange(new RangeBuilder().setLimit(10).build())
				.withColumnSlice(newColumnNames)
				.setExceptionCallback(new ExceptionCallback() {
					public boolean onException(ConnectionException e) {
						Assert.fail(e.getMessage());
						return true;
					}
				}).execute();

		int counter = 0;
		String bucketLowerLimits[] = new String[nbBuckets];		// Limite basse incluse du bucket
		int bucketDocCount[] = new int[nbBuckets];
		int columnNamesSize = columnNames.size();
		
		// Parcours des documents. On s'arrête lorsqu'on a atteint nbDocsToRead documents.
		for (Row<String, String> row : rows.getResult()) {
			//String key = row.getKey();
			ColumnList<String> columns = row.getColumns();
			if (columns.size() < columnNamesSize) continue;
			
			Column<String> colBase = columns.getColumnByName("SM_BASE_UUID");
			if (colBase == null || !colBase.getStringValue().toLowerCase().equals(baseUUID)) continue;
			
			String value = getIndexValue(columns, columnNames);
			if (value == null) continue;
			
			// On se sert des premiers documents pour délimiter les buckets
			if (counter < nbBuckets - 1) {
				bucketLowerLimits[counter] = value;
				if (counter == nbBuckets - 2) {
					// On fixe arbitrairement la limite du dernier bucket
					bucketLowerLimits[counter + 1] = "";
					// On trie les buckets
					Arrays.sort(bucketLowerLimits);
					// On vérifie qu'il n'y a pas de doublons
					for(int i = 0; i < nbBuckets - 1; i++) {
						if (bucketLowerLimits[i] == bucketLowerLimits[i+1]) throw new Exception("Doublon de bucket en position " + i);
					}
					// Chaque bucket a déjà un document, sauf le premier
					for(int i = 1; i < nbBuckets; i++) {
						bucketDocCount[i] = 1;
					}
				}
			}
			// On classe les documents suivants dans les buckets
			else {
				// On trouve le bucket concerné par binary search
				int bucket = Arrays.binarySearch(bucketLowerLimits, value);
				if (bucket < 0) bucket = -2 - bucket;
				bucketDocCount[bucket]++;
			}

			counter++;
			if (counter % 10000 == 0) {
				System.out.println("Counter : " + counter);
			}
			
			if (counter > nbDocsToRead) {
				// On a fini le parcours de l'échantillon de documents
				break;
			}
		}
		
		int totalFoundDoc = 0;
		for (int i = 0; i < nbBuckets; i++) totalFoundDoc += bucketDocCount[i];
		System.out.println("Nombre de documents lus : " + totalFoundDoc);
		float maxDocForRange = 1000000;
		int totalDocCount = 300000000;
		System.out.println("Nombre de splits qu'il faut pour " + totalDocCount + " docs : " + (float)totalFoundDoc/counter * totalDocCount/maxDocForRange);
		
		// Le parcours des documents est fini. On agrège les buckets pour former la délimitation des splits.
		String splitLowerLimits[] = new String[nbSplits];		// Limite basse incluse du split
		splitLowerLimits[0] = "";
		float docsPerSplit = (float) nbDocsToRead / nbSplits;
		int nextBucket = 0;
		int currentDocCount = 0;
		for (int i = 1; i < nbSplits; i++) {
			float desiredDocCount = docsPerSplit * i;
			while (true) {
				// On regarde s'il faut inclure le prochain bucket dans le split
				float delta1 = Math.abs(currentDocCount - desiredDocCount); 
				float delta2 = Math.abs(currentDocCount + bucketDocCount[nextBucket] - desiredDocCount);
				if (delta2 <= delta1) {
					// Oui, il faut inclure ce bucket dans le split
					currentDocCount += bucketDocCount[nextBucket];
					nextBucket ++;
				}
				else {
					// Non, on s'arrête ici
					splitLowerLimits[i] = bucketLowerLimits[nextBucket - 1];
					//System.out.println("Split " + i + " : nextBucket = " + nextBucket + " - currentDocCount=" + currentDocCount);
					break;
				}
			}
		}
		
		// On vérifie qu'on n'a pas de doublons dans les limites des splits
		for(int i = 0; i < nbSplits - 1; i++) {
			if (splitLowerLimits[i] == splitLowerLimits[i+1]) throw new Exception("Doublon de split en position " + i);
		}
		
		// Mise en forme des splits
		return splitLowerLimits;
	}

	public String getSplitsAsString(String splitLowerLimits[]) {
		// Mise en forme des splits
		int nbSplits = splitLowerLimits.length;
		String s = "[min_lower_bound TO " + splitLowerLimits[1] + "[";
		for(int i = 1; i < nbSplits - 1; i++) {
			s += "|[" + splitLowerLimits[i] + " TO " + splitLowerLimits[i+1] + "[";
		}
		s += "|[" + splitLowerLimits[nbSplits - 1] + " TO max_upper_bound]";
		return s;		
	}
	
	/**
	 * Renvoie la valeur de l'index
	 * @param columns		: valeur des différentes colonnes
	 * @param columnNames	: nom des colonnes composant l'index
	 * @return
	 */
	private String getIndexValue(ColumnList<String> columns, ArrayList<String> columnNames) {
		if (columnNames.size() == 1) {
			Column<String> col = columns.getColumnByName(columnNames.get(0));
			if (col == null) return null;
			return ConvertHelper.normalizeMetaValue(col.getStringValue());
		}
		String result = "";
		for (String columnName :columnNames) {
			Column<String> col = columns.getColumnByName(columnName);
			if (col == null) return null;
			result += ConvertHelper.normalizeMetaValue(col.getStringValue()) + "&";
		}
		return result;
	}


}
