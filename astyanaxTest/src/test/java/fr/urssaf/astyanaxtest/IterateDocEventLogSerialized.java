package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
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
import com.netflix.astyanax.serializers.ObjectSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.astyanaxtest.dao.DocEventLogByTimeSerializedCF;
import fr.urssaf.astyanaxtest.dao.DocEventLogByTimeSerializedCompositeColumnDefinition;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class IterateDocEventLogSerialized {

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
		//servers = "cer69-saeint3:9160";
		//servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; // Préprod

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
	/**
	 * Exemple d'itération sur les événements sérialisés
	 */
	public void testIterateOverEvents() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = sdf.parse("2015-07-07 16:30:00");
		Date end = sdf.parse("2015-07-07 18:10:10");
		String key = "20150707";

		int blocSize = 500; // Nombre de colonnes qu'on ramène à la fois de
							// cassandra
		int hardLimitForTest = 1000; // On arrête la boucle une fois ce nombre de
									// documents parcourus

		RowQuery<String, DocEventLogByTimeSerializedCompositeColumnDefinition> query = keyspace
				.prepareQuery(DocEventLogByTimeSerializedCF.cf)
				.getKey(key)
				.autoPaginate(true)
				.withColumnRange(
						DocEventLogByTimeSerializedCF.columnSerializer
								.makeEndpoint(start, Equality.EQUAL).toBytes(),
						DocEventLogByTimeSerializedCF.columnSerializer
								.makeEndpoint(end, Equality.LESS_THAN_EQUALS)
								.toBytes(), false, blocSize);

		ColumnList<DocEventLogByTimeSerializedCompositeColumnDefinition> columns;
		int compteurLigne = 0;
		int compteurDoc = 0;
		int maxDoc = 0;
		int minDoc = 0;
		boolean shouldStop = false;
		Stopwatch chrono = new Stopwatch();
		chrono.start();
		while (!(columns = query.execute().getResult()).isEmpty()
				&& !shouldStop) {
			for (Column<DocEventLogByTimeSerializedCompositeColumnDefinition> c : columns) {
				DocEventLogByTimeSerializedCompositeColumnDefinition colName = c
						.getName();

				{
					UUID eventUUID = colName.getEventUUID();
					Date date = colName.getDateValue();
							
					HashMap<String, List<String>> metadatas = (HashMap<String, List<String>>) c
							.getValue(ObjectSerializer.get());
					
					String docUuid = getMetadataValue(metadatas, "docUUID");
					String eventType = getMetadataValue(metadatas, "eventType");
					
					if (eventType.equals("CREATE_DOCUMENT")) {
						//sysout.println("date : " + sdf.format(date));
						//sysout.println("Doc uuid : " + docUuid);
						//sysout.println("Event type : " + eventType);
						sysout.println(sdf.format(date) + " " + docUuid);
					}
					//sysout.println("DeseriazedValue : "+ metadatas.toString());
				}

				compteurLigne++;
				compteurDoc++;
				if (compteurLigne >= hardLimitForTest) {
					shouldStop = true;
					break;
				}
			}
		}
		chrono.stop();
		System.out.println("Temps de traitement : " + chrono.toString());
		System.out.println(compteurLigne + " lignes affichées");
		System.out.println("Nombre max de doc : " + maxDoc);
		System.out.println("Nombre min de doc : " + minDoc);
	}

	/**
	 * Récupère la valeur d'une métadonnée mono-valuée
	 * 
	 * @param metadatas
	 *            : Hashmap contenant les métadonnée
	 * @param metadataCode
	 *            : Code de la métadonnée à lire
	 * @return
	 */
	private String getMetadataValue(
			HashMap<String, List<String>> metadatas, String metadataCode) {
		if (metadatas.containsKey(metadataCode)) {
			
			List<String> valuesList = metadatas.get(metadataCode);
			// catégorie monovaluée -> on renvoie le 1er élément
			return valuesList.get(0);
		}
		return null;
	}

	@Test
	public void wattTest() {
		String hex = "aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400064f534952495378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000027400055043313643740005504331364478\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504332354178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504332354178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005514335314178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005514335314178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504335314178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504335314178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43363078\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000027400055043313642740005504331364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f4000000000000174000650433337414378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f4000000000000174000650433337414478\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400045043323478\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504332354278\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43313878\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43333178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43313178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43333678\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43313278\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43383178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43383078\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43313778\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43363578\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43323178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43313978\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43353878\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43343878\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43323978\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400044e43353978\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400054e4332314178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f4000000000000174000650433337414378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f4000000000000174000650433337414478\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504336364178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f4000000000000174000650433337414378\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504337374178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504337374178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504337374178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504337374178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400034c303078\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f4000000000000174000650433337414478\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f40000000000001740005504337374178\r\n" + 
				"aced0005737200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000017400045043333778";
		String[] elements = hex.split("\r\n");
		ObjectSerializer serializer = ObjectSerializer.get();
		for(String element : elements) {
			byte[] bytes = ConvertHelper.hexStringToByteArray(element);
			Object object = serializer.fromBytes(bytes);
			System.out.println(object);
		}
	}
}
