package fr.urssaf.astyanaxtest.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.TreeMap;
import java.util.UUID;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.ShortSerializer;

import fr.urssaf.astyanaxtest.helper.ConvertHelper;

/**
 * Classe qui permet de lire la définition d'un index dans IndexReference, et qui est capable
 * de dire sur quel range il faut chercher pour une valeur de métadonnée
 *
 */
public class IndexReference {
	
	private String boundaries[];
	private int boundaryIndexToRangeId[];
	private int maxRangeId;
	
	Map<Integer, RangeIndexEntity> entitiesById = new HashMap<Integer, RangeIndexEntity>(); 
	Map<Integer, Integer> rangeIdToIndex = new HashMap<Integer, Integer>(); 
	private int rangeIndexSize;
	
	public int getMaxRangeId() {
		return maxRangeId;
	}
	
	public static byte[] getKey(UUID baseUUID, String meta) throws Exception {
		String keyAsString = meta + "\\xef\\xbf\\xbf" + baseUUID.toString().toLowerCase();
		byte[] key =  ConvertHelper.getBytesFromReadableUTF8String(keyAsString);
		return key;
	}
	
	public void readIndexReference(Keyspace keyspace, UUID baseUUID, String meta, String state) throws Exception {
		readIndexReference(keyspace, baseUUID, meta, new String[] {state});
	}
	
	public void readIndexReference(Keyspace keyspace, UUID baseUUID, String meta, String[] states) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		
		OperationResult<ColumnList<String>> cols = keyspace
				.prepareQuery(IndexReferenceCF.get()).getKey(key)
				.execute();

		ColumnList<String> result = cols.getResult();
		byte[] sizeAsBytes = result.getByteArrayValue("rangeIndexes.size", new byte[0]);
		if (sizeAsBytes.length == 1) {
			rangeIndexSize = sizeAsBytes[0];
		}
		else if (sizeAsBytes.length == 2) {
			rangeIndexSize = ShortSerializer.get().fromBytes(sizeAsBytes);
		}
		else {
			System.out.println("Key : " + ConvertHelper.getReadableUTF8String(key));
			throw new Exception("Taille inattendue : " + ConvertHelper.getHexString(sizeAsBytes));
		}
		
		maxRangeId = 0;
		ObjectMapper jsonMapper = new ObjectMapper();
		// On crée une collection d'entitées (range) triée par la borne inférieure du range
		TreeMap<String, RangeIndexEntity> entities = new TreeMap<String, RangeIndexEntity>(); 
		for (int i = 0; i < rangeIndexSize; i++) {
			String json = result.getStringValue("rangeIndexes." + i + ".value", "");
			if (json == null || json.isEmpty()) continue;
			//System.out.println(json);
			RangeIndexEntity entity = jsonMapper.readValue(json, RangeIndexEntity.class);
			entitiesById.put(entity.getId(), entity.clone());
			rangeIdToIndex.put(entity.getId(), i);
			if (Arrays.asList(states).contains(entity.getSTATE())) {
				if ("min_lower_bound".equals(entity.getLOWER_BOUND())) {
					entity.setLOWER_BOUND("");
				}
				entities.put(entity.getLOWER_BOUND(), entity);
				if (entity.getId() > maxRangeId) maxRangeId = entity.getId();  
			}
		}
		
		// On vérifie que tout est ok
		Iterator<Entry<String, RangeIndexEntity>> it1 = entities.entrySet().iterator();
		RangeIndexEntity previousEntity = null;
		RangeIndexEntity lastEntity = null;
	    while (it1.hasNext()) {
	        Entry<String, RangeIndexEntity> pair = it1.next();
	        RangeIndexEntity entity = pair.getValue();
	        System.out.println(entity.getLOWER_BOUND() + " - " + entity.getUPPER_BOUND());
	        if (previousEntity == null) {
	        	if(!"".equals(entity.getLOWER_BOUND())) {
	    	    	throw new Exception("Première borne min pas bonne : " + entity.getLOWER_BOUND());
	        	}
	        }
	        else {
	        	// on vérifie que les bornes sont contiguës
	        	if (!previousEntity.getUPPER_BOUND().equals(entity.getLOWER_BOUND())) {
	    	    	throw new Exception("Bornes non contiguës : " + previousEntity.getUPPER_BOUND() + " et " + entity.getLOWER_BOUND());
	        	}
	        }
	        previousEntity = entity;
	        lastEntity = entity;
	    }
	    if (!"max_upper_bound".equals(lastEntity.getUPPER_BOUND())) {
	    	throw new Exception("Dernière borne max pas bonne : " + lastEntity.getUPPER_BOUND());
	    }
	    
		// Création des bornes
		boundaries = new String[entities.size()];
		boundaryIndexToRangeId = new int[entities.size()];
		Iterator<Entry<String, RangeIndexEntity>> it = entities.entrySet().iterator();
		int currentRangeIndex = 0;
	    while (it.hasNext()) {
	        Entry<String, RangeIndexEntity> pair = it.next();
	        RangeIndexEntity entity = pair.getValue();
	        boundaries[currentRangeIndex] = entity.getLOWER_BOUND();
	        boundaryIndexToRangeId[currentRangeIndex] = entity.getId();
	        currentRangeIndex++;
	    }
	    
	}

   public class Tuple<X, Y> {
      public final X x;
      public final Y y;

      public Tuple(X x, Y y) {
         this.x = x;
         this.y = y;
      }
   } 
   /**
    * Permet de lire un IndexReference cassé
    * @param keyspace
    * @param baseUUID
    * @param meta
    * @param states
    * @throws Exception
    */
   public void readBrokenIndexReference(Keyspace keyspace, UUID baseUUID, String meta) throws Exception {
      byte[] key = getKey(baseUUID, meta);

      OperationResult<ColumnList<String>> cols = keyspace.prepareQuery(IndexReferenceCF.get()).getKey(key).execute();

      ColumnList<String> result = cols.getResult();
      byte[] sizeAsBytes = result.getByteArrayValue("rangeIndexes.size", new byte[0]);
      if (sizeAsBytes.length == 1) {
         rangeIndexSize = sizeAsBytes[0];
      } else if (sizeAsBytes.length == 2) {
         rangeIndexSize = ShortSerializer.get().fromBytes(sizeAsBytes);
      } else {
         throw new Exception("Taille inattendue : " + sizeAsBytes);
      }
      System.out.println("rangeIndexSize=" + rangeIndexSize);
      
      int maxIdToScan = 500;
      maxRangeId = 0;
      ObjectMapper jsonMapper = new ObjectMapper();
      // On crée une collection d'entitées (range) triée par la borne inférieure du range
      TreeMap<String, Tuple<Integer,RangeIndexEntity>> entities = new TreeMap<String, Tuple<Integer,RangeIndexEntity>>();
      List<String> linesByLowerBound = new ArrayList<String>();
      for (int i = 0; i < maxIdToScan; i++) {
         String json = result.getStringValue("rangeIndexes." + i + ".value", "");
         if (json == null || json.isEmpty()) continue;
         RangeIndexEntity entity = jsonMapper.readValue(json, RangeIndexEntity.class);
         entitiesById.put(entity.getId(), entity.clone());
         rangeIdToIndex.put(entity.getId(), i);
         if ("min_lower_bound".equals(entity.getLOWER_BOUND())) {
            entity.setLOWER_BOUND("");
         }
         entities.put(entity.getLOWER_BOUND(), new Tuple<Integer,RangeIndexEntity>(i, entity));
         linesByLowerBound.add(entity.getLOWER_BOUND() + " - index= " + String.format("%03d", i) + " - " + json); 
         if (entity.getId() > maxRangeId) maxRangeId = entity.getId();
      }
      
      // Dump avec doublons
      Collections.sort(linesByLowerBound);
      String currentBound = "";
      for (String line: linesByLowerBound) {
         String[] parts = line.split("-");
         String bound = parts[0];
         if (bound.equals(currentBound)) {
             System.out.println(line + " !!!!!!!!!!!!!!!!!!!!!!!");
         }
         else {
             System.out.println(line);
         }
         currentBound = bound;
      }
      
      // Affichage des ranges manquants
      System.out.println();
      System.out.println("Range ID manquants :");
      for (int rangeId = 1; rangeId <=maxRangeId; rangeId++) {
         if (!rangeIdToIndex.containsKey(rangeId)) System.out.println(rangeId);
      }
      
      // Dump
      System.out.println();
      Iterator<Entry<String, Tuple<Integer,RangeIndexEntity>>> it1 = entities.entrySet().iterator();
      while (it1.hasNext()) {
         Entry<String, Tuple<Integer,RangeIndexEntity>> pair = it1.next();
         Tuple<Integer,RangeIndexEntity> tuple = pair.getValue();
         Integer index = tuple.x;
         RangeIndexEntity entity = tuple.y;
         System.out.println(index.toString() + " - " + entity.getLOWER_BOUND() + " - " + entity.getUPPER_BOUND());
      }
      
      // On vérifie que tout est ok
      it1 = entities.entrySet().iterator();
      RangeIndexEntity previousEntity = null;
      RangeIndexEntity lastEntity = null;
      while (it1.hasNext()) {
         Entry<String, Tuple<Integer,RangeIndexEntity>> pair = it1.next();
         Tuple<Integer,RangeIndexEntity> tuple = pair.getValue();
         RangeIndexEntity entity = tuple.y;
         // System.out.println(entity.getLOWER_BOUND() + " - " +
         // entity.getUPPER_BOUND());
         if (previousEntity == null) {
            if (!"".equals(entity.getLOWER_BOUND())) {
               System.out.println("Première borne min pas bonne : " + entity.getLOWER_BOUND());
            }
         } else {
            // on vérifie que les bornes sont contiguës
            if (!previousEntity.getUPPER_BOUND()
                  .equals(entity.getLOWER_BOUND())) {
               System.out.println("Bornes non contiguës : " + previousEntity.getUPPER_BOUND() + " et " + entity.getLOWER_BOUND());
            }
         }
         previousEntity = entity;
         lastEntity = entity;
      }
      if (lastEntity == null) {
         System.out.println("Aucune borne trouvée");
      }
      else if (!"max_upper_bound".equals(lastEntity.getUPPER_BOUND())) {
         System.out.println("Dernière borne max pas bonne : " + lastEntity.getUPPER_BOUND());
      }
   }

	
	/**
	 * Création d'un seul range
	 */
	public void resetRanges() {
		// Création d'une seule borne
		boundaries = new String[1];
		boundaryIndexToRangeId = new int[1];
		boundaries[0] = "";
		boundaryIndexToRangeId[0] = 0;
		maxRangeId = 0;
	}
	
	/**
	 * Positionne les ranges.
	 * @param newBoundaries. La 1ere borne doit être une chaine vide.
	 */
	public void setRanges(String newBoundaries[]) {
		boundaries = newBoundaries;
		int boundariesCount = boundaries.length;
		
		boundaryIndexToRangeId = new int[boundariesCount];
		for (int i = 0; i < boundariesCount; i++) {
			boundaryIndexToRangeId[i] = i;
		}
		maxRangeId = boundariesCount - 1;
	}

	/**
	 * Met à jour la propriété "COUNT" dans le json pour différents ranges
	 * @param keyspace
	 * @param baseUUID
	 * @param meta
	 * @param counts	: Map rangeId=>count
	 * @throws Exception
	 */
	public void updateCountInRanges(Keyspace keyspace, UUID baseUUID, String meta, Map<Integer,Integer> counts) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		MutationBatch batch = keyspace.prepareMutationBatch();
		
		ObjectMapper jsonMapper = new ObjectMapper();
	    Iterator<Entry<Integer, Integer>> it = counts.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer, Integer> pair = it.next();
	        int rangeId = pair.getKey();
	        int count = pair.getValue();
	        RangeIndexEntity entity = entitiesById.get(rangeId).clone();
	        entity.setCOUNT(count);
	        String json = jsonMapper.writeValueAsString(entity);
	        int index = rangeIdToIndex.get(rangeId);
	        String colName = "rangeIndexes." + index + ".value";
	        System.out.println(colName + "=" + json);
			//batch.withRow(IndexReferenceCF.get(), key).putColumn(colName, json, null);
	        batch.withRow(IndexReferenceCF.get(), key).putColumn(colName, json);
	    }		
		OperationResult<Void> result = batch.execute();
		System.out.println("Batch exécuté en : " + result.getLatency(TimeUnit.MILLISECONDS) + " ms");
	}
	
	

	/**
	 * Met à jour la propriété "totalIndexUseCount" pour un index
	 * @param keyspace
	 * @param baseUUID
	 * @param meta		: le nom de l'index
	 * @param count	    : le nombre à écrire
	 * @throws Exception
	 */
	public void updateTotalIndexUseCount (Keyspace keyspace, UUID baseUUID, String meta, int count) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		MutationBatch batch = keyspace.prepareMutationBatch();
        batch.withRow(IndexReferenceCF.get(), key).putColumn("totalIndexUseCount", count);
		OperationResult<Void> result = batch.execute();
	}

	/**
	 * Met à jour la propriété "distinctIndexUseCount" pour un index
	 * @param keyspace
	 * @param baseUUID
	 * @param meta		: le nom de l'index
	 * @param count	    : le nombre à écrire
	 * @throws Exception
	 */
	public void updateDistinctIndexUseCount (Keyspace keyspace, UUID baseUUID, String meta, int count) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		MutationBatch batch = keyspace.prepareMutationBatch();
        batch.withRow(IndexReferenceCF.get(), key).putColumn("distinctIndexUseCount", count);
		OperationResult<Void> result = batch.execute();
	}

	/**
	 * Met à jour la propriété "STATE" pour un range d'un index
	 * @param keyspace
	 * @param baseUUID
	 * @param meta		: le nom de l'index
	 * @param rangeId	: l'id du range
	 * @param state	    : la valeur du l'état à positionner ("NOMINAL", "BUILDING", "SPLITTING")
	 * @throws Exception
	 */
	public void updateState(Keyspace keyspace, UUID baseUUID, String meta, int rangeId, String state) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		ObjectMapper jsonMapper = new ObjectMapper();
		RangeIndexEntity entity = entitiesById.get(rangeId).clone();
		entity.setSTATE(state);
        String json = jsonMapper.writeValueAsString(entity);
        int index = rangeIdToIndex.get(rangeId);
        String colName = "rangeIndexes." + index + ".value";
        System.out.println(colName + "=" + json);
		MutationBatch batch = keyspace.prepareMutationBatch();
        batch.withRow(IndexReferenceCF.get(), key).putColumn(colName, json);
        OperationResult<Void> result = batch.execute();
	}
	
	/**
	 * Annule un split avorté.
	 * Les ranges qui sont à l'état SPLITTING sont remis à l'état NOMINAL
	 */
	public void cancelSplit(Keyspace keyspace, UUID baseUUID, String meta) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		ObjectMapper jsonMapper = new ObjectMapper();
		int maxIndex = 0;
		for (Integer rangeId : entitiesById.keySet()) {			
			RangeIndexEntity entity = entitiesById.get(rangeId).clone();
			if (entity.getSTATE().equals("SPLITTING")) {
				entity.setSTATE("NOMINAL");
				String json = jsonMapper.writeValueAsString(entity);
				int index = rangeIdToIndex.get(rangeId);
				String colName = "rangeIndexes." + index + ".value";
				System.out.println(colName + "=" + json);
				MutationBatch batch = keyspace.prepareMutationBatch();
				batch.withRow(IndexReferenceCF.get(), key).putColumn(colName, json);
				OperationResult<Void> result = batch.execute();
			}
			if (entity.getSTATE().equals("SPLITTING") || entity.getSTATE().equals("NOMINAL")) {
				int index = rangeIdToIndex.get(rangeId);
				maxIndex = Math.max(maxIndex, index);
			}
		}
		writeSize(keyspace, baseUUID, meta, maxIndex + 1);
	}
	
	public RangeIndexEntity getEntityById(int id) {
		return entitiesById.get(id).clone();
	}

	/**
	 * Écrit la colonne "rangeIndexes.size"
	 * @param keyspace
	 * @param baseUUID
	 * @param meta
	 * @param size
	 * @throws Exception
	 */
	public void writeSize(Keyspace keyspace, UUID baseUUID, String meta, int size) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		
		MutationBatch batch = keyspace.prepareMutationBatch();
		// La taille est sérialisée de manière bizarre (un seul octet si < 128)
		byte[] sizeAsBytes = getSizeAsBytes(size);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		OperationResult<Void> result = batch.execute();
	}

	/**
	 * Déclare que le range 0 est à l'état "SPLITTING", et crée les ranges à l'état "BUILDING"
	 * @param keyspace
	 * @param baseUUID
	 * @param meta
	 * @param rangeId	forcément 0
	 * @param rangesAsString	Exemple : "[min_lower_bound TO 316[|[316 TO 330[|[330 TO 400[|[400 TO max_upper_bound]"
	 * @throws Exception 
	 */
	public void startSplitting(Keyspace keyspace, UUID baseUUID, String meta, int rangeId, String rangesAsString) throws Exception {
		Assert.assertEquals(0, rangeId);
		Assert.assertEquals(1, rangeIndexSize);
		RangeIndexEntity firstEntity = entitiesById.get(0);
		Assert.assertEquals("NOMINAL", firstEntity.getSTATE());
		Iterable<String> ranges = Splitter.on('|').split(rangesAsString);
		ObjectMapper jsonMapper = new ObjectMapper();
		
		byte[] key =  getKey(baseUUID, meta);
		MutationBatch batch = keyspace.prepareMutationBatch();
		
		// On met le 1er range à l'état SPLITTING
		firstEntity.setSTATE("SPLITTING");
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.0.value", jsonMapper.writeValueAsString(firstEntity), null);
		
		int index = 0;
		for(String range : ranges) {
			range = range.trim().substring(1, range.length() - 1);
			String[] bounds = range.split(" TO ");
			System.out.println(bounds[0] + " => " + bounds[1]);
			index ++;

			String json;
			RangeIndexEntity entity = new RangeIndexEntity();
			entity.setId(index);
			entity.setCOUNT(0);
			entity.setLOWER_BOUND(bounds[0]);
			entity.setUPPER_BOUND(bounds[1]);
			entity.setSTATE("BUILDING");
			json = jsonMapper.writeValueAsString(entity);

			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".key", index, null);
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".value", json, null);
		}
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", getSizeAsBytes(index+1), null);
		OperationResult<Void> result = batch.execute();
	}

	/**
	 * Permet de déclarer un range en "SPLITTING", et crée les ranges associés à l'état "BUILDING"
	 * @param keyspace
	 * @param baseUUID
	 * @param meta
	 * @param rangeId	pas forcément 0
	 * @param rangesAsString	Exemple : "[min_lower_bound TO 316[|[316 TO 330[|[330 TO 400[|[400 TO max_upper_bound]"
	 * @throws Exception 
	 */
	public void startSplitting_new(Keyspace keyspace, UUID baseUUID, String meta, int rangeId, String rangesAsString) throws Exception {
		RangeIndexEntity firstEntity = entitiesById.get(rangeId);
		Assert.assertEquals("NOMINAL", firstEntity.getSTATE());
		Iterable<String> ranges = Splitter.on('|').split(rangesAsString);
		ObjectMapper jsonMapper = new ObjectMapper();
		int firstEntityIndex = rangeIdToIndex.get(rangeId);
		
		byte[] key =  getKey(baseUUID, meta);
		MutationBatch batch = keyspace.prepareMutationBatch();
		
		// On met le 1er range à l'état SPLITTING
		firstEntity.setSTATE("SPLITTING");
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." +  firstEntityIndex + ".value", jsonMapper.writeValueAsString(firstEntity), null);
		
		int nextIndex = rangeIndexSize;
		int nextRangeId = maxRangeId + 1;
		for(String range : ranges) {
			range = range.trim().substring(1, range.length() - 1);
			String[] bounds = range.split(" TO ");
			System.out.println(bounds[0] + " => " + bounds[1]);

			String json;
			RangeIndexEntity entity = new RangeIndexEntity();
			entity.setId(nextRangeId);
			entity.setCOUNT(0);
			entity.setLOWER_BOUND(bounds[0]);
			entity.setUPPER_BOUND(bounds[1]);
			entity.setSTATE("BUILDING");
			json = jsonMapper.writeValueAsString(entity);

			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + nextIndex + ".key", nextRangeId, null);
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + nextIndex + ".value", json, null);
			
			nextIndex++;
			nextRangeId++;
		}
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", getSizeAsBytes(nextIndex), null);
		OperationResult<Void> result = batch.execute();
	}

	/**
	 * Permet de déclarer un range en "SPLITTING", et crée les ranges associés à l'état "BUILDING"
	 * @param keyspace
	 * @param baseUUID
	 * @param meta
	 * @param rangeToSplit	le range à spliter au format texte : par exemple : [51175 TO 51400[ 
	 * @param rangesAsString	Exemple : "[min_lower_bound TO 316[|[316 TO 330[|[330 TO 400[|[400 TO max_upper_bound]"
	 * @throws Exception 
	 */
	public void startSplitting_new(Keyspace keyspace, UUID baseUUID, String meta, String rangeToSplit, String rangesAsString) throws Exception {
		int rangeId = findRangeId(rangeToSplit);
		startSplitting_new(keyspace, baseUUID, meta, rangeId, rangesAsString);
	}
	
	/**
	 * Trouve l'id du range
	 * @param rangeToSplit range au format texte, par exemple : [51175 TO 51400[
	 * @return
	 * @throws Exception 
	 */
	private int findRangeId(String range) throws Exception {
		range = range.trim().substring(1, range.length() - 1);
		String[] bounds = range.split(" TO ");
		String lowerBound = bounds[0];
		String upperBound = bounds[1];
		for (Entry<Integer, RangeIndexEntity> entry : entitiesById.entrySet())
		{
		    RangeIndexEntity entity = entry.getValue();
		    if (entity.getLOWER_BOUND().equals(lowerBound) && entity.getUPPER_BOUND().equals(upperBound)) {
			    int rangeId = entry.getKey();
		    	return rangeId; 
		    }
		}
		throw new Exception("Range non trouvé : " + range);
	}

	/**
	 * Les ranges délimités par "boundarie" sont écrits dans cassandra.
	 * Les ranges sont écrits en mode "nominal". Les éventuelles autres ranges présents dans cassandra sont écrasés.
	 * Attention : à ne surtout pas utiliser en dehors du cas particulier du 1er split.
	 * Sert à remplacer le range "SPLITTING" par les ranges "BUILDING" et en les mettant en état "NOMINAL" 
	 * @param keyspace
	 * @param baseUUID
	 * @param meta
	 * @param approximateDocCountPerSplit : le nombre de documents sur chaque split
	 * @throws Exception
	 */
	public void writeIndexReference(Keyspace keyspace, UUID baseUUID, String meta, int approximateDocCountPerSplit) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		// Pour test :
		//key =  ConvertHelper.getBytesFromReadableUTF8String("test");
		
		MutationBatch batch = keyspace.prepareMutationBatch();
		int size = boundaries.length;
		ObjectMapper jsonMapper = new ObjectMapper();
		for (int i = 0; i < size; i++) {
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + i + ".key", boundaryIndexToRangeId[i], null);
			String json;
			RangeIndexEntity entity = new RangeIndexEntity();
			entity.setId(boundaryIndexToRangeId[i]);
			entity.setCOUNT(approximateDocCountPerSplit);
			String lowerBound = "min_lower_bound";
			if (!boundaries[i].isEmpty()) lowerBound = boundaries[i];
			entity.setLOWER_BOUND(lowerBound);
			String upperBound = "max_upper_bound";
			if (i < size - 1) upperBound = boundaries[i+1];
			entity.setUPPER_BOUND(upperBound);
			entity.setSTATE("NOMINAL");
			json = jsonMapper.writeValueAsString(entity);
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes."+ i + ".value", json, null);
		}
		// La taille est sérialisée de manière bizarre (un seul octet si < 128)
		byte[] sizeAsBytes = getSizeAsBytes(size);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		OperationResult<Void> result = batch.execute();
	}

	/**
	 * Ré-écrit les ranges qui sont en mémoire, en gardant la propriété COUNT, et en passant l'état à NOMINAL.
	 * Ca peut permettre de remettre d'aplomb en split "planté", en passant les ranges "BUILDIND" en "NOMINAL", et
	 * et supprimant les ranges "SPLITING".
	 * @param keyspace
	 * @param baseUUID
	 * @param meta
	 * @throws Exception
	 */
	public void reWriteIndexReference(Keyspace keyspace, UUID baseUUID, String meta) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		
		MutationBatch batch = keyspace.prepareMutationBatch();
		int size = boundaries.length;
		ObjectMapper jsonMapper = new ObjectMapper();
		for (int i = 0; i < size; i++) {
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + i + ".key", boundaryIndexToRangeId[i], null);
			System.out.println("rangeIndexes." + i + ".key" + ":"+ boundaryIndexToRangeId[i]);
			int rangeId = boundaryIndexToRangeId[i];
			String json;
			RangeIndexEntity entity = getEntityById(rangeId);
			entity.setSTATE("NOMINAL");
			json = jsonMapper.writeValueAsString(entity);
			batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes."+ i + ".value", json, null);
			System.out.println("rangeIndexes."+ i + ".value" + ":"+ json);
		}
		// La taille est sérialisée de manière bizarre (un seul octet si < 128)
		byte[] sizeAsBytes = getSizeAsBytes(size);
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);
		System.out.println("rangeIndexes.size" +":"+ sizeAsBytes);
		OperationResult<Void> result = batch.execute();
	}

	
	/**
	 * Écrit une colonne "rangeIndexes.x.key"
	 * @param keyspace
	 * @param baseUUID
	 * @param meta
	 * @param rangeIndex
	 * @param rangeKey
	 * @throws Exception
	 */
	public static void writeRangeKey(Keyspace keyspace, UUID baseUUID, String meta, int rangeIndex, int rangeKey) throws Exception {
		byte[] key =  getKey(baseUUID, meta);
		
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + rangeIndex + ".key", rangeKey, null);
		OperationResult<Void> result = batch.execute();
	}

	
	public static byte[] getSizeAsBytes(int size) {
		byte[] sizeAsBytes;
		if (size < 128) {
			sizeAsBytes = new byte[1];
			sizeAsBytes[0] = (byte)size;
		}
		else {
			sizeAsBytes = ShortSerializer.get().toBytes((short)size);
		}
		return sizeAsBytes;
	}
	
	/**
	 * Renvoie l'id du range qui contient la valeur de la méta passée en paramètre
	 * @param metaValue
	 * @return
	 */
	public int metaToRangeId(String metaValue) {
		int index = Arrays.binarySearch(boundaries, metaValue);
		if (index < 0) index = -2 - index;
		return boundaryIndexToRangeId[index];
	}

	/**
	 * Retourne l'id de l'ensemble des ranges sur lesquels il faut requêter pour trouver les documents dont la méta indexée
	 * est entre metaStartValue et metaEndValue
	 * @param metaStartValue
	 * @param metaEndValue
	 * @return Les id des ranges
	 */
	public int[] metaToRangeIds(String metaStartValue, String metaEndValue) {
		int index1 = Arrays.binarySearch(boundaries, metaStartValue);
		if (index1 < 0) index1 = -2 - index1;
		int index2 = Arrays.binarySearch(boundaries, metaEndValue);
		if (index2 < 0) index2 = -2 - index2;
		int[] result = new int[index2 - index1 + 1]; 
		for (int index = index1; index<=index2; index++) {
			result[index - index1] = boundaryIndexToRangeId[index];
		}
		return result;
	}
	
	public int[] getRangeIds() {
		return boundaryIndexToRangeId.clone();
	}
}
