package fr.urssaf.astyanaxtest.dao;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.ColumnCountQuery;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.ObjectSerializer;
import com.netflix.astyanax.serializers.ShortSerializer;
import com.netflix.astyanax.util.RangeBuilder;

import fr.urssaf.astyanaxtest.helper.ConvertHelper;
import fr.urssaf.astyanaxtest.helper.MetaHelper;

public abstract class TermInfoRangeDao {

	private Keyspace keyspace;
	/**
	 * La méta, qui peut être composite. Exemple : "nce" ou "cot&cag&SM_CREATION_DATE&"
	 */
	public String meta;
	/**
	 * Dans le cas d'un index composite : la liste des métas associées, exemple {"cot", "cag", "SM_CREATION_DATE"}
	 */
	private ArrayList<String> realMetas;
	private UUID baseUUID;
	private IndexReference indexReference;
	protected ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn> cf;
	
	public TermInfoRangeDao(Keyspace keyspace, String meta, UUID baseUUID, IndexReference indexReference) {
		this.keyspace = keyspace;
		this.meta = meta;		
		this.baseUUID = baseUUID;
		this.indexReference = indexReference;
		realMetas = MetaHelper.indexToMetas(meta);
	}
	
	public IndexReference getIndexReference() {
		return indexReference;
	}
	
	public List<String> getRealMetas() {
		return realMetas;
	}
	
	public byte[] getKey_old(int rangeId) {
		String rangeIdAsHex;
		if (rangeId < 128) {
			rangeIdAsHex = "01" + ConvertHelper.bytesToHex(new byte[] {(byte) rangeId});
		}
		else {
			rangeIdAsHex = "02" + ConvertHelper.bytesToHex(ShortSerializer.get().toBytes((short) rangeId));
		}
		String hex = "00" + String.format("%08x", meta.length()) + ConvertHelper.stringToHex(meta)
				+ "000010" + ConvertHelper.UUIDToHexString(baseUUID) + "0000" + rangeIdAsHex + "00"; 
		return ConvertHelper.hexStringToByteArray(hex);
	}
	
	public byte[] getKey_old2(int rangeId) throws UnsupportedEncodingException {
	   int bufferSize = 29 + meta.length();
	   if (rangeId >= 128) bufferSize ++; 
	   ByteBuffer bb = ByteBuffer.wrap(new byte[bufferSize]);
	   bb.put((byte) 0);
	   bb.putInt(meta.length());
	   bb.put(ConvertHelper.stringToBytes(meta));
	   bb.put((byte) 0);
	   bb.putShort((short) 16);
	   bb.putLong(baseUUID.getMostSignificantBits());
	   bb.putLong(baseUUID.getLeastSignificantBits());
	   bb.put((byte) 0);
	   bb.put((byte) 0);
	   if (rangeId < 128) {
		   bb.put((byte) 1);
		   bb.put((byte)rangeId);
	   }
	   else {
		   bb.put((byte) 2);
		   bb.putShort((short)rangeId);
	   }
	   bb.put((byte) 0);
	   return bb.array();
	}
	
	public TermInfoRangeKey getKey(int rangeId) {
		return new TermInfoRangeKey(meta, baseUUID, rangeId);
	}
	
	/**
	 * Pour test : affiche quelques infos d'un document récupéré dans l'index
	 * @param docUUID	: id du document
	 * @param metaValue	: valeur de la méta indexé
	 * @throws Exception
	 */
	public void readForTest(UUID docUUID, String metaValue) throws Exception {
		metaValue = ConvertHelper.normalizeMetaValue(metaValue);
		int rangeId = indexReference.metaToRangeId(metaValue);
		TermInfoRangeKey key = getKey(rangeId);
		TermInfoRangeColumn column = new TermInfoRangeColumn();
		column.setDocumentUUID(docUUID);
		column.setDocumentVersion("0.0.0");
		column.setCategoryValue(metaValue);
		byte[] columnAsBytes = TermInfoRangeCF.columnSerializer.toBytes(column);
		System.out.println("Key : " + ConvertHelper.getReadableUTF8String(TermInfoRangeCF.keySerializer.toBytes(key)));
		System.out.println("Column : " + ConvertHelper.getHexString(columnAsBytes));
		
		OperationResult<ColumnList<TermInfoRangeColumn>> cols = keyspace
				.prepareQuery(cf).getKey(key)
				.withColumnSlice(column)
				.execute();
		ColumnList<TermInfoRangeColumn> result = cols.getResult();
		for (Column<TermInfoRangeColumn> c : result) {
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            //System.out.println("Value - " + metadatas.toString());
            String filename = getMetadataValue(metadatas, "SM_FILENAME");
            String extension = getMetadataValue(metadatas, "SM_EXTENSION");
            String date = getMetadataValue(metadatas, "SM_ARCHIVAGE_DATE");
            String siret = getMetadataValue(metadatas, "srt");
            System.out.println(date + " " + docUUID.toString().toUpperCase() + " " + filename + "." + extension + " " + siret);
		}
	}

	/**
	 * Renvoie les métadonnées d'un document comme elles sont stockées, c'est à dire dans une HashMap
	 * @param docUUID : l'id du document
	 * @param metaValue : la valeur de la méta qui nous sert à trouver le document dans l'index
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, ArrayList<String>> getHashMap(UUID docUUID, String metaValue) throws Exception {
		metaValue = ConvertHelper.normalizeMetaValue(metaValue);
		int rangeId = indexReference.metaToRangeId(metaValue);
		TermInfoRangeKey key = getKey(rangeId);
		TermInfoRangeColumn column = new TermInfoRangeColumn();
		column.setDocumentUUID(docUUID);
		column.setDocumentVersion("0.0.0");
		column.setCategoryValue(metaValue);
		
		OperationResult<ColumnList<TermInfoRangeColumn>> cols = keyspace
				.prepareQuery(cf).getKey(key)
				.withColumnSlice(column)
				.execute();
		ColumnList<TermInfoRangeColumn> result = cols.getResult();
		for (Column<TermInfoRangeColumn> c : result) {
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            return metadatas;
		}
		return null;
	}

	public ArrayList<HashMap<String, ArrayList<String>>> getHashMaps(String metaValue) throws Exception {
		metaValue = ConvertHelper.normalizeMetaValue(metaValue);
		int rangeId = indexReference.metaToRangeId(metaValue);
		TermInfoRangeKey key = getKey(rangeId);
		TermInfoRangeColumn columnMin = new TermInfoRangeColumn();
		columnMin.setDocumentUUID(new UUID(0, 0));
		columnMin.setDocumentVersion("0.0.0");
		columnMin.setCategoryValue(metaValue);
		TermInfoRangeColumn columnMax = new TermInfoRangeColumn();
		columnMax.setDocumentUUID(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"));
		columnMax.setDocumentVersion("0.0.0");
		columnMax.setCategoryValue(metaValue);
		
		OperationResult<ColumnList<TermInfoRangeColumn>> cols = keyspace
				.prepareQuery(cf).getKey(key)
				.withColumnRange(columnMin, columnMax, false, 100)
				.execute();
		ColumnList<TermInfoRangeColumn> result = cols.getResult();
		ArrayList<HashMap<String, ArrayList<String>>> list = new ArrayList<HashMap<String, ArrayList<String>>>();
		for (Column<TermInfoRangeColumn> c : result) {
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            list.add(metadatas);
		}
		return list;
	}
	
	/**
	 * Écrit des entrées dans TermInfoRangeString
	 * @param hashMaps	: liste de hashmaps à écrire. Chaque hashmap correspond aux méta d'un document
	 * @throws Exception
	 * @return : tableau indexé sur rangeId, donnant le nombre d'éléments ajoutés à chaque range
	 */
	public int[] writeHashMaps(ArrayList<HashMap<String, ArrayList<String>>> hashMaps) throws Exception {
		int[] counts = new int[indexReference.getMaxRangeId()+1];
		MutationBatch batch = keyspace.prepareMutationBatch();
		ObjectSerializer serializer = ObjectSerializer.get();
		for (HashMap<String, ArrayList<String>> metadatas : hashMaps) {
			// On récupère la valeur de la méta
			String indexValueInIndexReference = getIndexValueInIndexReference(metadatas);
			String docUUID = getMetadataValue(metadatas, "SM_UUID");
			int rangeId = indexReference.metaToRangeId(indexValueInIndexReference);
			counts[rangeId]++;
			TermInfoRangeKey key = getKey(rangeId);
			TermInfoRangeColumn column = new TermInfoRangeColumn();
			column.setDocumentUUID(UUID.fromString(docUUID));
			column.setDocumentVersion("0.0.0");
			String indexValueInTermInfoRange = getIndexValueInTermInfoRange(metadatas);
			column.setCategoryValue(indexValueInTermInfoRange);
			
			batch.withRow(cf, key).putColumn(column, serializer.toBytes(metadatas), null);
		}
		OperationResult<Void> result = batch.execute();
		return counts;
	}
	
	/**
	 * Renvoie la valeur de l'index (éventuellement composite) à partir des métadonnées du document,
	 * tel qu'il est stocké dans le nom de la colonne de TermInfoRangeXXX
	 * @param metadatas : les métadonnées du document
	 * @return
	 */
	private String getIndexValueInTermInfoRange(HashMap<String, ArrayList<String>> metadatas) {
		if (realMetas.size() == 1) return ConvertHelper.normalizeMetaValue(getMetadataValue(metadatas, meta));
		String result = "";
		for (String realMeta : realMetas) {
			// Dans le nom de la colonne de TermInfoRangeString, ce n'est pas "&" le séparateur, mais "\0"
			result += ConvertHelper.normalizeMetaValue(getMetadataValue(metadatas, realMeta)) + "\0";
		}
		return result;
	}
	
	/**
	 * Renvoie la valeur de l'index (éventuellement composite) à partir des métadonnées du document,
	 * tel qu'il est stocké pour la délimitation des ranges dans la CF IndexReference
	 * @param metadatas : les métadonnées du document
	 * @return
	 */
	private String getIndexValueInIndexReference(HashMap<String, ArrayList<String>> metadatas) {
		if (realMetas.size() == 1) return ConvertHelper.normalizeMetaValue(getMetadataValue(metadatas, meta));
		String result = "";
		for (String realMeta : realMetas) {
			// Dans IndexReference, c'est "&" le séparateur
			result += ConvertHelper.normalizeMetaValue(getMetadataValue(metadatas, realMeta)) + "&";
		}
		return result;
	}
	
	/**
	 * Exemple d'itération sur les documents en utilisant l'index
	 */
	public void iterateForTest(String metaStartValue, String metaEndValue) throws Exception {

		int blocSize = 100; // Nombre de document qu'on ramène à la fois de
							// cassandra
		int hardLimitForTest = 200; // On arrête la boucle une fois ce nombre de
									// documents parcourus
		
		int[] rangeIds = indexReference.metaToRangeIds(metaStartValue, metaEndValue);

		for (int rangeId : rangeIds) {
			System.out.println("RangeId=" + rangeId);
			TermInfoRangeKey key = getKey(rangeId);
			RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
					.prepareQuery(cf)
					.getKey(key)
					.autoPaginate(true)
					.withColumnRange(new RangeBuilder().setLimit(blocSize).build());
	
			ColumnList<TermInfoRangeColumn> columns;
			int compteurLigne = 0;
			boolean shouldStop = false;
			while (!(columns = query.execute().getResult()).isEmpty()
					&& !shouldStop) {
				for (Column<TermInfoRangeColumn> c : columns) {
					TermInfoRangeColumn colName = c
							.getName();
	
					@SuppressWarnings("unchecked")
					HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c
							.getValue(ObjectSerializer.get());
					String uuid = getMetadataValue(metadatas, "SM_UUID");
					System.out.println("uuid : " + uuid);
					//System.out.println("DeseriazedValue : " + metadatas.toString());
					String metaValue1 = colName.getCategoryValueAsString();
					byte[] metaValue2 = colName.getCategoryValue();
					String metaValue3 = getMetadataValue(metadatas, meta);
					System.out.println("metaValue1 : " + metaValue1);
					System.out.println("metaValue2 : " + ConvertHelper.getReadableUTF8String(metaValue2));
					System.out.println("metaValue3 : " + metaValue3);
					
					compteurLigne++;
					if (compteurLigne >= hardLimitForTest) {
						shouldStop = true;
						break;
					}
				}
			}
		}
	}

	/**
	 * Supprime complètement la ligne correspondant au range fourni
	 * @param rangeId : l'id du range
	 * @throws Exception 
	 */
	public void deleteRow(int rangeId) throws Exception {
		MutationBatch batch = keyspace.prepareMutationBatch();
		TermInfoRangeKey key = getKey(rangeId);
		batch.withRow(cf, key).delete();
		OperationResult<Void> result = batch.execute();
	}
	
	/**
	 * Exemple d'itération sur un des ranges de l'index
	 */
	public void iterateOnRange(int rangeId) throws Exception {

		int blocSize = 500; // Nombre de document qu'on ramène à la fois de cassandra
		int hardLimitForTest = 2500000; // On arrête la boucle une fois ce nombre de documents parcourus
		
		System.out.println("RangeId=" + rangeId);
		TermInfoRangeKey key = getKey(rangeId);
		System.out.println("Key=" + ConvertHelper.bytesToHex(TermInfoRangeCF.keySerializer.toBytes(key)));

		RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
				.prepareQuery(cf)
				.getKey(key)
				.autoPaginate(true)
				.withColumnRange(new RangeBuilder().setLimit(blocSize).build());

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date minDate = dateFormat.parse("1998-01-26 11:11:11");
		ColumnList<TermInfoRangeColumn> columns;
		int compteurLigne = 0;
		boolean shouldStop = false;
		while (!(columns = query.execute().getResult()).isEmpty()
				&& !shouldStop) {
			for (Column<TermInfoRangeColumn> c : columns) {
				TermInfoRangeColumn colName = c
						.getName();
				
				Date date = new Date(c.getTimestamp()/1000);
				if (compteurLigne % 10000 == 0 || minDate.after(date)) {
					if (minDate.after(date)) {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					}
					@SuppressWarnings("unchecked")
					HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c
							.getValue(ObjectSerializer.get());
					String uuid = getMetadataValue(metadatas, "SM_UUID");
					System.out.println("rangeId : " + rangeId);
					System.out.println("counter : " + compteurLigne);
					System.out.println("uuid : " + uuid);
					//System.out.println("DeseriazedValue : " + metadatas.toString());
					String metaValue1 = colName.getCategoryValueAsString();
					byte[] metaValue2 = colName.getCategoryValue();
					String metaValue3 = getMetadataValue(metadatas, meta);
					System.out.println("metaValue1 : " + metaValue1);
					//System.out.println("metaValue2 : " + ConvertHelper.getReadableUTF8String(metaValue2));
					System.out.println("metaValue2 : " + ConvertHelper.getHexString(metaValue2));
					System.out.println("metaValue3 : " + metaValue3);
					System.out.println("timestamp : " + dateFormat.format(date));
				}
				compteurLigne++;
				if (compteurLigne >= hardLimitForTest) {
					shouldStop = true;
					break;
				}
			}
		}
		System.out.println("RangeId : " + rangeId + " - counter : " + compteurLigne);
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
	private String getMetadataValue(HashMap<String, ArrayList<String>> metadatas, String metadataCode) {
		if (metadatas.containsKey(metadataCode)) {
			ArrayList<String> valuesList = metadatas.get(metadataCode);
			// catégorie monovaluée -> on renvoie le 1er élément
			return valuesList.get(0);
		}
		return null;
	}
	
	public UUID getBaseUUID() {
		return baseUUID;
	}
	
	/**
	 * Compte le nombre de colonnes pour un range donné. Ne marche pas
	 * @param rangeId
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public int getColumnCount_doNotWork(int rangeId) throws Exception {
		TermInfoRangeKey key = getKey(rangeId);

		ColumnCountQuery query = keyspace.prepareQuery(cf)
			    .getKey(key)
			    .autoPaginate(true)
			    .withColumnRange(new RangeBuilder().setLimit(100000).build())
			    .getCount();
		
		int total = 0;
	    Integer count;
		while ((count = query.execute().getResult()) > 0) {
	        total += count;
	        System.out.println(total);
	    }		
		return total;
	}

	/**
	 * Compte le nombre de colonnes pour un range donné
	 * @param rangeId
	 * @return
	 * @throws Exception
	 */
	public int getColumnCount(int rangeId) throws Exception {

		int blocSize = 5000; // Nombre de documents qu'on ramène à la fois de cassandra
		int displayInterval = 10000;
		TermInfoRangeKey key = getKey(rangeId);
		//System.out.println(key.getCategoryName());
		byte[] keyAsBytes = cf.getKeySerializer().toBytes(key);
		String keyAsReadableString = ConvertHelper.getReadableUTF8String(keyAsBytes);
		System.out.println("Key : " + keyAsReadableString);
		String keyAsHex = ConvertHelper.getHexString(keyAsBytes);
		System.out.println("Key (hex) : " + keyAsHex);
		
		RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
				.prepareQuery(cf)
				.getKey(key)
				.autoPaginate(true)
				.withColumnRange(new RangeBuilder().setLimit(blocSize).build());
		/*
				.withColumnRange(
							TermInfoRangeCF.columnSerializer.makeEndpoint("", Equality.EQUAL).toBytes(),
							TermInfoRangeCF.columnSerializer.makeEndpoint("" + (char)255, Equality.LESS_THAN_EQUALS)
									.toBytes(), false, blocSize);*/

		ColumnList<TermInfoRangeColumn> columns;
		int counter = 0;
		int nextDisplayCount = displayInterval;
		while (!(columns = query.execute().getResult()).isEmpty()) {
			counter+= columns.size();
			if (counter >= nextDisplayCount) {
				System.out.println("RangeId : " + rangeId + " - counter : " + counter);
				//String value = c.getName().getCategoryValueAsString();
				//System.out.println("value : " + value);
				nextDisplayCount += displayInterval;
			}
		}
		return counter;
	}
	
	/**
	 * Compte le nombre de colonnes pour un range donné, ainsi que le nombre de valeurs de méta distinct
	 * @param rangeId
	 * @return deux entiers dans un tableau : nombre total de colonnes, et nombre de valeurs distinctes de méta
	 * @throws Exception
	 */
	public int[] getDistinctColumnCount(int rangeId) throws Exception {

		int blocSize = 1000; // Nombre de documents qu'on ramène à la fois de cassandra
		TermInfoRangeKey key = getKey(rangeId);
		//System.out.println(key.getCategoryName());
		byte[] keyAsBytes = cf.getKeySerializer().toBytes(key);
		String keyAsReadableString = ConvertHelper.getReadableUTF8String(keyAsBytes);
		System.out.println("Key : " + keyAsReadableString);
		
		RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
				.prepareQuery(cf)
				.getKey(key)
				.autoPaginate(true)
				.withColumnRange(new RangeBuilder().setLimit(blocSize).build());
		/*
				.withColumnRange(
							TermInfoRangeCF.columnSerializer.makeEndpoint("", Equality.EQUAL).toBytes(),
							TermInfoRangeCF.columnSerializer.makeEndpoint("" + (char)255, Equality.LESS_THAN_EQUALS)
									.toBytes(), false, blocSize);*/

		ColumnList<TermInfoRangeColumn> columns;
		int counter = 0;
		int distinctCounter = 0;
		String currentValue = "";
		while (!(columns = query.execute().getResult()).isEmpty()) {
			for (Column<TermInfoRangeColumn> c : columns) {
				counter++;
				String value = c.getName().getCategoryValueAsString();
				if (!currentValue.equals(value)) {
					distinctCounter++;
					currentValue = value;
				}
				if (counter % 50000 == 0) {
					System.out.println("RangeId : " + rangeId + " - counter : " + counter + " - distinctCounter=" + distinctCounter);
					System.out.println("value : " + value);
				}
			}
		}		
		return new int[] {counter, distinctCounter};
	}
	
}
