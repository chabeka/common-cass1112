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
import com.netflix.astyanax.MutationBatch;
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
import com.netflix.astyanax.model.Equality;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.DateSerializer;
import com.netflix.astyanax.serializers.ObjectSerializer;
import com.netflix.astyanax.serializers.UUIDSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.astyanaxtest.dao.BasesReferenceCF;
import fr.urssaf.astyanaxtest.dao.SystemEventLogByTimeSerializedCF;
import fr.urssaf.astyanaxtest.dao.SystemEventLogByTimeSerializedCompositeColumnDefinition;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class IterateSystemEventLogSerialized {

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
		//servers = "cer69-saeint3:9160";
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
	/**
	 * Exemple d'itération sur les événements sérialisés
	 */
	public void testIterateOverEvents() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = sdf.parse("2016-04-29 00:00:00");
		Date end = sdf.parse("2019-04-29 23:59:59");
		String key = "20180212";
		//
		//20180624
		//20180511
		//20180422

		int blocSize = 500; // Nombre de colonnes qu'on ramène à la fois de
							// cassandra
		int hardLimitForTest = 2000000; // On arrête la boucle une fois ce nombre de
									// documents parcourus

		RowQuery<String, SystemEventLogByTimeSerializedCompositeColumnDefinition> query = keyspace
				.prepareQuery(SystemEventLogByTimeSerializedCF.cf)
				.getKey(key)
				.autoPaginate(true)
				.withColumnRange(
						SystemEventLogByTimeSerializedCF.columnSerializer
								.makeEndpoint(start, Equality.EQUAL).toBytes(),
						SystemEventLogByTimeSerializedCF.columnSerializer
								.makeEndpoint(end, Equality.LESS_THAN_EQUALS)
								.toBytes(), false, blocSize);

		ColumnList<SystemEventLogByTimeSerializedCompositeColumnDefinition> columns;
		int compteurLigne = 0;
		int compteurDoc = 0;
		int maxDoc = 0;
		int minDoc = 0;
		boolean shouldStop = false;
		Stopwatch chrono = new Stopwatch();
		chrono.start();
		while (!(columns = query.execute().getResult()).isEmpty()
				&& !shouldStop) {
			for (Column<SystemEventLogByTimeSerializedCompositeColumnDefinition> c : columns) {
				SystemEventLogByTimeSerializedCompositeColumnDefinition colName = c
						.getName();

				{
					UUID eventUUID = colName.getEventUUID();
					Date date = colName.getDateValue();
					sysout.println("eventUUID : " +eventUUID.toString());
					HashMap<String, List<String>> metadatas = (HashMap<String, List<String>>) c
							.getValue(ObjectSerializer.get());
					
					/*
					if (eventUUID.toString().equals("c5e293fa-23c4-4d7c-a112-b948925475a6")) {
						sysout.println("date="+date);
						sysout.println("dateAsBytes=" + ConvertHelper.bytesToHex(DateSerializer.get().toBytes(date)));						
						sysout.println(metadatas.toString());
						
						// ATTENTION : Suppression de l'événement...
						MutationBatch batch = keyspace.prepareMutationBatch();
						SystemEventLogByTimeSerializedCompositeColumnDefinition columnName = new SystemEventLogByTimeSerializedCompositeColumnDefinition(date, eventUUID);
						batch.withRow(SystemEventLogByTimeSerializedCF.cf, key).deleteColumn(columnName);
						OperationResult<Void> result = batch.execute();
						
						shouldStop = true;
						break;
					}
					*/
					
					String docUuid = getMetadataValue(metadatas, "docUUID");
					String eventType = getMetadataValue(metadatas, "eventType");
					//String eventDescription = getMetadataValue(metadatas, "eventDescription");
					//sysout.println("Event descr : " + eventDescription);
					
					if (eventType != null && eventType.equals("CREATE_DOCUMENT")) {
						sysout.println("date : " + sdf.format(date));
						sysout.println("Doc uuid : " + docUuid);
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
	public void deleteOneEventTest() throws Exception {
		MutationBatch batch = keyspace.prepareMutationBatch();
		Date dateValue = DateSerializer.get().fromBytes(ConvertHelper.hexStringToByteArray("0000015BBEB18D1B"));
		UUID eventUUID = UUID.fromString("dca338f5-2163-4c3d-932b-b485585e54d1");
		SystemEventLogByTimeSerializedCompositeColumnDefinition columnName = new SystemEventLogByTimeSerializedCompositeColumnDefinition(dateValue, eventUUID);
		batch.withRow(SystemEventLogByTimeSerializedCF.cf, "20170430").deleteColumn(columnName );
		OperationResult<Void> result = batch.execute();
	}
}
