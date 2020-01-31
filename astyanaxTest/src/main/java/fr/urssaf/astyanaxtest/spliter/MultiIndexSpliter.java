package fr.urssaf.astyanaxtest.spliter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;

import fr.urssaf.astyanaxtest.dao.DocInfoCF;
import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDao;

/**
 * 
 * Permet de spliter plusieurs index à froid
 *
 */
public class MultiIndexSpliter {

	private Keyspace keyspace;
	private TermInfoRangeDao[] termInfoRangeDaos; 
	private String baseUUID;
	private int indexCount;
	
	public MultiIndexSpliter(Keyspace keyspace, TermInfoRangeDao[] termInfoRangeDaos) {
		this.keyspace = keyspace;
		this.termInfoRangeDaos = termInfoRangeDaos;
		indexCount = termInfoRangeDaos.length;
		this.baseUUID = termInfoRangeDaos[0].getBaseUUID().toString().toLowerCase();
	}
	

	/**
	 * Découpe un index (TermInfoRangeString)
	 * @param maxArchivageDate	: (ex : "20151116222151439") on ignore les doc archivés après cette date 
	 * @throws Exception
	 */
	public void splitIndex(String maxArchivageDate, String progressFile) throws Exception {
		splitIndex(maxArchivageDate, keyspace.getPartitioner().getMinToken(), keyspace.getPartitioner().getMaxToken(), progressFile);
	}
	
	/**
	 * Lance un split mutithreadé. Chaque thread parcours une partie de DocInfo, et, pour chaque document
	 * parcouru, alimente les données dans TermInfoRangeXXX pour chaque index
	 * 
	 * @param maxArchivageDate
	 * @param startToken
	 * @param endToken
	 * @param blocCount
	 * @param progressFile
	 * @return les compteurs, pour chaque (index,  rangeId)
	 * @throws Exception
	 */
	public int[][] splitIndex_multithread(final String maxArchivageDate, String startToken, String endToken, int blocCount, String progressFile) throws Exception {
		BigInteger startTokenAsBig = new BigInteger(startToken);
		BigInteger endTokenAsBig = new BigInteger(endToken);
		BigInteger tokenWidth = endTokenAsBig.subtract(startTokenAsBig);
		BigInteger tokenInterval = tokenWidth.divide(new BigInteger(Integer.toString(blocCount)));
		BigInteger currentToken = startTokenAsBig;

		// Initialisation des compteurs
		final int[][] counters = new int[indexCount][];
		for (int i = 0; i < indexCount; i++) {
			counters[i] = new int[termInfoRangeDaos[i].getIndexReference().getMaxRangeId() + 1];
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(36);
		final ArrayList<Integer> blocsInError = new ArrayList<Integer>();
		
		final class WorkerThread implements Runnable {
			private int blocNumber;
			private String startToken;
			private String endToken;
		    private String progressFile;
		    public WorkerThread(int blocNumber, String startToken, String endToken, String progressFile) {
		    	this.blocNumber = blocNumber;
		    	this.startToken = startToken;
		    	this.endToken = endToken;
		        this.progressFile = progressFile;
		    }

		    public void run() {
		    	int errorCount = 0;
		    	while (true) {
			        try {
			        	int[][] localCounts = splitIndex(maxArchivageDate, startToken, endToken, progressFile);
			        	synchronized (counters) {
			        		for (int i = 0; i < indexCount; i++) {
			        			for (int j = 0; j < counters[i].length; j++) {
					        		counters[i][j] += localCounts[i][j];
			        			}
			        		}
						}
			            return;
			        } catch(Exception ex) {
			            System.out.println("Exception " + errorCount + " pour le bloc " + blocNumber + " : " + ex);
			            ex.printStackTrace();
			            errorCount ++;
			            if (errorCount > 500) {
				            System.out.println("Trop d'erreurs pour le bloc " + blocNumber);
				            synchronized (blocsInError) {
				            	blocsInError.add(blocNumber);
				            }
				            return;
			            }
			            try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}
			        }
		        }
		    }
		}
		
		for (int i = 0; i < blocCount; i++) {
			BigInteger start = currentToken;
			BigInteger end;
			if (i == blocCount - 1) {
				end = endTokenAsBig;
			}
			else {
				end = start.add(tokenInterval);
			}
			System.out.println(start.toString() + " - " + end.toString());
			Runnable worker = new WorkerThread(i, start.toString(), end.toString(), progressFile + "." + i);
			executor.execute(worker);
			currentToken = end.add(new BigInteger("1"));
		}

		executor.shutdown();
        while (!executor.isTerminated()) {
        	Thread.sleep(2000);
	    }
        System.out.println("Tous les blocs sont terminés");
        System.out.println("Blocs en erreur :" + blocsInError);

        // Affichage des compteurs
		for (int i = 0; i < indexCount; i++) {
    		System.out.println("============== META " + termInfoRangeDaos[i].meta);
			for (int rangeId = 0; rangeId < counters[i].length; rangeId++) {
	    		System.out.println("Range " + rangeId + " : " + counters[i][rangeId]);
			}
		}
        // Mise à jours des compteurs dans indexReference
		for (int i = 0; i < indexCount; i++) {
    		IndexReference indexReference = termInfoRangeDaos[i].getIndexReference();
    		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
			int totalIndexUseCount = 0;
			for (int rangeId = 0; rangeId < counters[i].length; rangeId++) {
				if (counters[i][rangeId] > 0) {
					counts.put(rangeId, counters[i][rangeId]);
					totalIndexUseCount += counters[i][rangeId]; 
				}
			}
			indexReference.updateCountInRanges(keyspace, UUID.fromString(baseUUID), termInfoRangeDaos[i].meta, counts);
			indexReference.updateTotalIndexUseCount(keyspace, UUID.fromString(baseUUID), termInfoRangeDaos[i].meta, totalIndexUseCount);
		}
		
    	return counters;
	}
	
	/**
	 * Découpe un index (TermInfoRangeString)
	 * @param maxArchivageDate	: (ex : "20151116222151439") on ignore les doc archivés après cette date 
	 * @param startToken		: le token min, si on ne veut parcourir qu'une partie de l'espace des token
	 * @param endToken			: le token max, si on ne veut parcourir qu'une partie de l'espace des token
	 * @throws Exception
	 */
	public int[][] splitIndex(String maxArchivageDate, String startToken, String endToken, String progressFile) throws Exception {
		
		// Lecture fichier de progression et gestion reprise 
		ProgressFileContent progressFileContent = readProgressFile(progressFile);
		
		final int[][] counts = new int[indexCount][];
		for (int i = 0; i < indexCount; i++) {
			counts[i] = new int[termInfoRangeDaos[i].getIndexReference().getMaxRangeId() + 1];
		}
		
		if (progressFileContent.currentToken !=null) {
			BigInteger currentToken = new BigInteger(progressFileContent.currentToken);
			BigInteger nextToken = currentToken.add(new BigInteger("1"));
			startToken = nextToken.toString();
			System.out.println("Reprise du traitement à partir du token " + startToken);
			HashMap<Integer,HashMap<Integer, Integer>> countsAsMap = progressFileContent.counts;
			Iterator<Entry<Integer, HashMap<Integer, Integer>>> it = countsAsMap.entrySet().iterator();		
			while (it.hasNext()) {
		        Entry<Integer, HashMap<Integer, Integer>> map2 = it.next();
				int metaIndex = map2.getKey(); 
				Iterator<Entry<Integer, Integer>> it2 = map2.getValue().entrySet().iterator();
				while (it2.hasNext()) {
					Entry<Integer, Integer> pair = it2.next();
			        int rangeId = pair.getKey();
			        int count = pair.getValue();
			        counts[metaIndex][rangeId] = count;
				}
			}
			if (currentToken.toString().equals(endToken)) {
				System.out.println("Traitement déja terminé : currentToken="+ currentToken);
				return counts;
			}
		}
		
		int commitInterval = 100;
		OperationResult<Rows<String, String>> rows = keyspace
				.prepareQuery(DocInfoCF.get()).getAllRows()
				.setRowLimit(200)
				// On ne prend pas la colonne \xef\xbf\xbfMETA\xef\xbf\xbf
				.withColumnRange("", String.valueOf((char)(0xef)), false, 1000)
				.forTokenRange(startToken, endToken)
				.execute();

		// Parcours des documents à partir de DocInfo
		int counter = 0;
		Stopwatch watch = new Stopwatch();
		watch.start();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		ArrayList<HashMap<String, ArrayList<String>>>[] hashMaps = new ArrayList[indexCount];
		for (int i = 0; i < indexCount; i++) {
			hashMaps[i] = new ArrayList<HashMap<String,ArrayList<String>>>();
		}
		
		for (Row<String, String> row : rows.getResult()) {
			
			// Est-ce que ce doc nous intéresse ?
			ColumnList<String> columns = row.getColumns();
			if (columns.size() == 0) continue;
			Column<String> colBase = columns.getColumnByName("SM_BASE_UUID");
			if (colBase == null || !colBase.getStringValue().toLowerCase().equals(baseUUID)) continue;
			Column<String> colArchivageDate = columns.getColumnByName("SM_ARCHIVAGE_DATE");
			if (colArchivageDate == null) continue;
			String archivageDate = colArchivageDate.getStringValue();
			if (archivageDate.compareTo(maxArchivageDate) > 0) {
				System.out.println("Date ignorée :" + archivageDate);
				continue;
			}
			
			// Création de la map de métadonnées à partir des colonnes de DocInfo
			HashMap<String, ArrayList<String>> metadatas = new HashMap<String, ArrayList<String>>();
			for (Column<String> col : columns) {
				String name = col.getName();
				String theValue = col.getStringValue();
				ArrayList<String> value = new ArrayList<String>();
				value.add(theValue);
				metadatas.put(name, value);
			}

			// On cherche les index concernés par ce document
			boolean foundAtLeastOneIndex = false;		// Vrai si au moins un index est concerné par le document courant
			for (int i = 0; i < indexCount; i++) {
				boolean isIndexConcerned = true;		// Vrai si cet index est concerné par le document courant
				List<String> realMetas = termInfoRangeDaos[i].getRealMetas();
				for(String realMeta : realMetas) {
					if (columns.getColumnByName(realMeta) == null) {
						isIndexConcerned = false;
						break;
					}
				}
				if (isIndexConcerned) {
					hashMaps[i].add(metadatas);
					foundAtLeastOneIndex = true;
				}
			}
			if (foundAtLeastOneIndex) counter++;

			if (counter % commitInterval == 0) {
				for (int i = 0; i < indexCount; i++) {
					if (hashMaps[i].size() == 0) continue;
					int tryCounter = 0;
					while(true) {
						try {
							int[] added = termInfoRangeDaos[i].writeHashMaps(hashMaps[i]);
							for (int j = 0; j < counts[i].length; j++) {
								counts[i][j] += added[j];
							}
							hashMaps[i].clear();
							break;
						}
						catch (Exception e) {
							System.out.println("Tentative " + tryCounter + " en échec");
							System.out.println(e);
							e.printStackTrace();
							//System.out.println(e.getMessage());
							Thread.sleep(5000);
							tryCounter++;
							if (tryCounter > 50) {
								throw e;
							}
						}
					}
				}
				String currentToken = keyspace.getPartitioner().getTokenForKey(row.getRawKey());
				writeProgressFile(progressFile, currentToken, counts, false);
			}
			if (counter % 10000 == 0) {
				String time = dateFormat.format(new Date());
				String currentToken = keyspace.getPartitioner().getTokenForKey(row.getRawKey());
				System.out.println("token=" + currentToken);
				long speed = counter / watch.elapsed(TimeUnit.SECONDS);
				System.out.println("Time = "  + time + " - Counter=" + counter + " - Speed=" + speed + "doc/s");
			}
			
		}
		for (int i = 0; i < indexCount; i++) {
			if (hashMaps[i].size() == 0) continue;
			int[] added = termInfoRangeDaos[i].writeHashMaps(hashMaps[i]);
			for (int j = 0; j < counts[i].length; j++) {
				counts[i][j] += added[j];
			}
			hashMaps[i].clear();
		}
		writeProgressFile(progressFile, endToken, counts, true);
		
		System.out.println("Counter=" + counter);
		System.out.println("Fini");
		return counts;
		
	}

	
	private void writeProgressFile(String filename, String currentToken, int[][] counts, boolean finished) throws IOException {
		File file = new File(filename);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("CurrentToken:" + currentToken +"\n");
		for (int i = 0; i < indexCount; i++) {
			for (int j = 0; j < counts[i].length; j++) {
				if (counts[i][j] > 0) {
			        writer.write("Count:" + i + ":" + j + ":" + counts[i][j] +"\n");
				}
			}
		}
		if (finished) writer.write("Finished\n");
		writer.close();
	}
	
	private class ProgressFileContent {
		public String currentToken;
		public HashMap<Integer, HashMap<Integer,Integer>> counts = new HashMap<Integer, HashMap<Integer, Integer>>();
		public boolean finished = false;
	}
	
	private ProgressFileContent readProgressFile(String filename) throws IOException {
		ProgressFileContent content = new ProgressFileContent();
		File f = new File(filename);
		if(!f.exists()) return content;
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
	    String line;
	    while ((line = br.readLine()) != null) {
	    	String[] elements = line.split(":");
	    	if ("CurrentToken".equals(elements[0])) {
	    		content.currentToken = elements[1]; 
	    	}
	    	if ("Count".equals(elements[0])) {
	    		int metaIndex = Integer.parseInt(elements[1]);
	    		int splitIndex = Integer.parseInt(elements[2]);
	    		int count = Integer.parseInt(elements[3]);
	    		if(!content.counts.containsKey(metaIndex)) content.counts.put(metaIndex, new HashMap<Integer, Integer>());
	    		content.counts.get(metaIndex).put(splitIndex, count);
	    	}
	    	if ("Finished".equals(elements[0])) {
	    		content.finished = true;
	    	}
	    }
	    br.close();
	    return content;
	}
}
