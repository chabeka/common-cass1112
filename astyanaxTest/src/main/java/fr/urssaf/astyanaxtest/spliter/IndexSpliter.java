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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Splitter;
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
import fr.urssaf.astyanaxtest.dao.TermInfoRangeDao;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;
import fr.urssaf.astyanaxtest.helper.MetaHelper;

/**
 * 
 * Permet de spliter un index à froid
 *
 */
public class IndexSpliter {

	private Keyspace keyspace;
	private TermInfoRangeDao termInfoRangeDao; 
	private String baseUUID;
	
	public IndexSpliter(Keyspace keyspace, TermInfoRangeDao termInfoRangeStringDao) {
		this.keyspace = keyspace;
		this.termInfoRangeDao = termInfoRangeStringDao;
		this.baseUUID = termInfoRangeStringDao.getBaseUUID().toString().toLowerCase();
	}
	
	/**
	 * Vérifie l'indexation d'une méta dans TermInfoRangeString
	 * @param meta	 			: le nom de la méta
	 * @param maxArchivageDate	: (ex : "20151116222151439") on ignore les doc archivés après cette date 
	 * @throws Exception
	 */
	public void verifyIndex(String meta, String maxArchivageDate) throws Exception {
		verifyIndex(meta, maxArchivageDate, keyspace.getPartitioner().getMinToken(), keyspace.getPartitioner().getMaxToken());
	}
	
	/**
	 * Vérifie l'indexation d'une méta dans TermInfoRangeString
	 * @param index	 			: le nom de l'index, éventuellement composite (ex: "nce" ou "cot&cag&SM_CREATION_DATE&")
	 * @param maxArchivageDate	: (ex : "20151116222151439") on ignore les doc archivés après cette date 
	 * @param startToken		: le token min, si on ne veut parcourir qu'une partie de l'espace des token
	 * @param endToken			: le token max, si on ne veut parcourir qu'une partie de l'espace des token
	 * @throws Exception
	 */
	public void verifyIndex(String index, String maxArchivageDate, String startToken, String endToken) throws Exception {
		
		ArrayList<String> realMetas = MetaHelper.indexToMetas(index);
		
		OperationResult<Rows<String, String>> rows = keyspace
				.prepareQuery(DocInfoCF.get()).getAllRows()
				.setRowLimit(200)
				// On ne prend pas la colonne \xef\xbf\xbfMETA\xef\xbf\xbf
				.withColumnRange("", String.valueOf((char)(0xef)), false, 1000)
				.forTokenRange(startToken, endToken)
				.execute();

		// On ignore certaines différences
		TreeMap<String, ArrayList<String>> ignoreRight = new TreeMap<String, ArrayList<String>>();
		ignoreRight.put("SM_KEY_REFERENCE_UUID", new ArrayList<String>());
		ArrayList<String> cassandraRepo = new ArrayList<String>(); cassandraRepo.add("cassandra");
		ignoreRight.put("SM_REPOSITORY_NAME", cassandraRepo);
		ignoreRight.put("SM_INDEX_CODE", new ArrayList<String>());
		ignoreRight.put("SM_IS_FROZEN", new ArrayList<String>());

		
		// Parcours des documents à partir de DocInfo
		int counter = 0;
		int goodCounter = 0;
		int badCounter = 0;
		int notPresentCounter = 0;
		for (Row<String, String> row : rows.getResult()) {
			
			// Est-ce que ce doc nous intéresse ?
			ColumnList<String> columns = row.getColumns();
			if (columns.size() == 0) continue;
			String indexValue = getIndexValue(columns, realMetas);
			if (indexValue == null) continue;
			Column<String> colBase = columns.getColumnByName("SM_BASE_UUID");
			if (colBase == null || !colBase.getStringValue().toLowerCase().equals(baseUUID)) continue;
			Column<String> colArchivageDate = columns.getColumnByName("SM_ARCHIVAGE_DATE");
			if (colArchivageDate == null) continue;
			String archivageDate = colArchivageDate.getStringValue();
			if (archivageDate.compareTo(maxArchivageDate) > 0) {
				System.out.println("Date ignorée :" + archivageDate);
				continue;
			}
			
			// Création de la map à partir des colonnes de DocInfo			
			HashMap<String, ArrayList<String>> metadatas = new HashMap<String, ArrayList<String>>();
			for (Column<String> col : columns) {
				String name = col.getName();
				String theValue = col.getStringValue();
				ArrayList<String> value = new ArrayList<String>();
				value.add(theValue);
				metadatas.put(name, value);
			}
			
			// On récupère la map dans TermInfoRangeString 
			UUID docUUID = UUID.fromString(columns.getColumnByName("SM_UUID").getStringValue());
			//TODO : metaValue avec des null à la place de &
			HashMap<String, ArrayList<String>> metadatas2 = termInfoRangeDao.getHashMap(docUUID , indexValue);
			
			if (metadatas.equals(metadatas2)) {
				//System.out.println("Equal");
				//System.out.println(metadatas);
				goodCounter++;
			}
			else {
				if (metadatas2 == null) {
					notPresentCounter++;
					/*
					System.out.println(docUUID);
					System.out.println(metaValue);
					System.out.println("metadatas2 is null");
					*/
				}
				else {
					MapDifference<String, ArrayList<String>> diff = Maps.difference(metadatas, metadatas2);
					boolean ko1 = diff.entriesDiffering().size() > 0;
					boolean ko2 = diff.entriesOnlyOnLeft().size() > 0 && "{SM_ANNOTATION_UUID=[]".equals(diff.entriesOnlyOnLeft().toString());
					TreeMap<String, ArrayList<String>> onlyRight = new TreeMap<String, ArrayList<String>>(diff.entriesOnlyOnRight());
					boolean ko3 = diff.entriesOnlyOnRight().size() > 0 && onlyRight.equals(ignoreRight);
					
					if (ko1 || ko2 || ko3) {
						badCounter++;
						System.out.println(docUUID);
						System.out.println(indexValue);
						System.out.println("Differing :" + diff.entriesDiffering());
						System.out.println("OnlyLeft :" + diff.entriesOnlyOnLeft());
						System.out.println("OnlyRight :" + onlyRight);
						System.out.println("ko1 : " + ko1);
						System.out.println("ko2 : " + ko2);
						System.out.println("ko3 : " + ko3);
					}
					else {
						goodCounter++;
					}
				}
			}
			
			counter++;
			if (counter % 100 == 0) {
				System.out.println("docUUID="+docUUID);
				System.out.println("token=" + keyspace.getPartitioner().getTokenForKey(row.getRawKey()));
				System.out.println("goodCounter=" + goodCounter + "-badCounter=" + badCounter + "-notPresentCounter="+notPresentCounter);
			}
		}		
	}

	/**
	 * Découpe un index (TermInfoRangeString)
	 * @param meta	 			: le nom de la méta
	 * @param maxArchivageDate	: (ex : "20151116222151439") on ignore les doc archivés après cette date 
	 * @throws Exception
	 */
	public void splitIndex(String meta, String maxArchivageDate, String progressFile) throws Exception {
		splitIndex(meta, maxArchivageDate, keyspace.getPartitioner().getMinToken(), keyspace.getPartitioner().getMaxToken(), progressFile);
	}
	
	public void splitIndex_multithread(final String meta, final String maxArchivageDate, String startToken, String endToken, int blocCount, String progressFile) throws Exception {
		BigInteger startTokenAsBig = new BigInteger(startToken);
		BigInteger endTokenAsBig = new BigInteger(endToken);
		BigInteger tokenWidth = endTokenAsBig.subtract(startTokenAsBig);
		BigInteger tokenInterval = tokenWidth.divide(new BigInteger(Integer.toString(blocCount)));
		BigInteger currentToken = startTokenAsBig;
		
		final int maxRangeId = termInfoRangeDao.getIndexReference().getMaxRangeId();
		final int[] counters = new int[maxRangeId + 1];
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
			        	int[] localCounts = splitIndex(meta, maxArchivageDate, startToken, endToken, progressFile);
			        	synchronized (counters) {
				        	for (int i = 0; i <= maxRangeId; i++) {
				        		counters[i] += localCounts[i];
				        	}
						}
			            return;
			        } catch(Exception ex) {
			            System.out.println("Exception " + errorCount + " pour le bloc " + blocNumber + " : " + ex);
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
        
    	for (int i = 0; i <= maxRangeId; i++) {
    		System.out.println("Range " + i + " : " + counters[i]);
    	}
	}
	
	/**
	 * Découpe un index (TermInfoRangeString)
	 * @param meta	 			: le nom de la méta
	 * @param maxArchivageDate	: (ex : "20151116222151439") on ignore les doc archivés après cette date 
	 * @param startToken		: le token min, si on ne veut parcourir qu'une partie de l'espace des token
	 * @param endToken			: le token max, si on ne veut parcourir qu'une partie de l'espace des token
	 * @throws Exception
	 */
	public int[] splitIndex(String meta, String maxArchivageDate, String startToken, String endToken, String progressFile) throws Exception {
		
		// Lecture fichier de progression et gestion reprise 
		ProgressFileContent progressFileContent = readProgressFile(progressFile);
		
		int maxRangeId = termInfoRangeDao.getIndexReference().getMaxRangeId();
		int[] counts = new int[maxRangeId + 1];
		
		if (progressFileContent.currentToken !=null) {
			BigInteger currentToken = new BigInteger(progressFileContent.currentToken);
			BigInteger nextToken = currentToken.add(new BigInteger("1"));
			startToken = nextToken.toString();
			System.out.println("Reprise du traitement sur à partir du token " + startToken);
			HashMap<Integer, Integer> countsAsMap = progressFileContent.counts;
			Iterator<Entry<Integer, Integer>> it = countsAsMap.entrySet().iterator();		
			while (it.hasNext()) {
		        Entry<Integer, Integer> pair = it.next();
		        int rangeId = pair.getKey();
		        int count = pair.getValue();
		        counts[rangeId] = count;
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
		ArrayList<HashMap<String, ArrayList<String>>> hashMaps = new ArrayList<HashMap<String,ArrayList<String>>>();		
		for (Row<String, String> row : rows.getResult()) {
			
			// Est-ce que ce doc nous intéresse ?
			ColumnList<String> columns = row.getColumns();
			if (columns.size() == 0) continue;
			Column<String> colMeta = columns.getColumnByName(meta);
			if (colMeta == null) continue;
			Column<String> colBase = columns.getColumnByName("SM_BASE_UUID");
			if (colBase == null || !colBase.getStringValue().toLowerCase().equals(baseUUID)) continue;
			Column<String> colArchivageDate = columns.getColumnByName("SM_ARCHIVAGE_DATE");
			if (colArchivageDate == null) continue;
			String archivageDate = colArchivageDate.getStringValue();
			if (archivageDate.compareTo(maxArchivageDate) > 0) {
				System.out.println("Date ignorée :" + archivageDate);
				continue;
			}
			
			// Création de la map à partir des colonnes de DocInfo
			HashMap<String, ArrayList<String>> metadatas = new HashMap<String, ArrayList<String>>();
			for (Column<String> col : columns) {
				String name = col.getName();
				String theValue = col.getStringValue();
				ArrayList<String> value = new ArrayList<String>();
				value.add(theValue);
				metadatas.put(name, value);
			}
			hashMaps.add(metadatas);
			counter++;
			if (counter % commitInterval == 0 && hashMaps.size() > 0) {
				int tryCounter = 0;
				while(true) {
					try {
						int[] added = termInfoRangeDao.writeHashMaps(hashMaps);
						for (int i = 0; i <= maxRangeId; i++) {
							counts[i] += added[i];
						}
						hashMaps.clear();
						String currentToken = keyspace.getPartitioner().getTokenForKey(row.getRawKey());
						writeProgressFile(progressFile, currentToken, counts, false);
						break;
					}
					catch (Exception e) {
						System.out.println("Tentative " + tryCounter + " en échec");
						System.out.println(e.getMessage());
						Thread.sleep(5000);
						tryCounter++;
						if (tryCounter > 50) {
							throw e;
						}
					}
				}
			}
			if (counter % 10000 == 0) {
				String time = dateFormat.format(new Date());
				String currentToken = keyspace.getPartitioner().getTokenForKey(row.getRawKey());
				System.out.println("token=" + currentToken);
				long speed = counter / watch.elapsed(TimeUnit.SECONDS);
				System.out.println("Time = "  + time + " - Counter=" + counter + " - Speed=" + speed + "doc/s");
			}
			
		}
		int[] added = termInfoRangeDao.writeHashMaps(hashMaps);
		for (int i = 0; i <= maxRangeId; i++) {
			counts[i] += added[i];
		}
		hashMaps.clear();
		writeProgressFile(progressFile, endToken, counts, true);
		
		System.out.println("Counter=" + counter);
		System.out.println("Fini");
		return counts;
		
	}

	private void writeProgressFile(String filename, String currentToken, int[] counts, boolean finished) throws IOException {
		File file = new File(filename);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("CurrentToken:" + currentToken +"\n");
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > 0) {
		        writer.write("Count:" + i + ":" + counts[i] +"\n");
			}
		}
		if (finished) writer.write("Finished\n");
		writer.close();
	}
	
	private class ProgressFileContent {
		public String currentToken;
		public HashMap<Integer,Integer> counts = new HashMap<Integer, Integer>();
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
	    		content.counts.put(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
	    	}
	    	if ("Finished".equals(elements[0])) {
	    		content.finished = true;
	    	}
	    }
	    br.close();
	    return content;
	}

	/**
	 * Renvoie la valeur de l'index tel qu'il est stocké dans la termInfoRangeString
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
			result += ConvertHelper.normalizeMetaValue(col.getStringValue()) + "\0";
		}
		return result;
	}

}
