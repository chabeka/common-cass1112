package fr.urssaf.astyanaxtest.repair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;

import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.dao.IndexReferenceCF;
import fr.urssaf.astyanaxtest.dao.RangeIndexEntity;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;

/**
 * Classe qui permet d'aider à la réparation des données de la CF IndexReference, qui ont tendance à sauter.
 *
 */
public class IndexReferenceRepair {


	/**
	 * Permet de parser un fichier généré par HectoTest.DumpTest.testDumpIndexReference, et de ré-appliquer une partie de son
	 * contenu (rangeIndexes.xx.key, rangeIndexes.xx.value, et rangeIndexes.size) dans IndexReference 
	 * @param keyspace
	 * @param baseUUID
	 * @param indexName		: nom de l'index à réparer
	 * @param dumpFilename	: fichier généré par HectoTest.DumpTest.testDumpIndexReference, à réappliquer
	 * @throws Exception
	 */
	public void cleanFromTextFile(Keyspace keyspace, UUID baseUUID, String indexName, String dumpFilename) throws Exception {
		
		IndexReference ref = new IndexReference();
		byte[] key = IndexReference.getKey(baseUUID, indexName);
		String firstLine = "Key : " + ConvertHelper.getReadableUTF8String(key);
		
		// Préparation du batch cassandra
		MutationBatch batch = keyspace.prepareMutationBatch();		
		
		// Parcours du fichier : lecture ligne par ligne
		FileInputStream fstream = new FileInputStream(dumpFilename);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String line;
		boolean hasBegan = false;
		while ((line = br.readLine()) != null) {
			if (line.equals(firstLine)) {
				hasBegan = true;
			}
		    if (hasBegan) {
		    	if (line.equals("")) break;
		    	if (line.contains(".value")) {
		    		
		    		// récupération de l'index		    		
		    		int start = "Name : rangeIndexes.".length();
		    		int end = line.indexOf(".", start);
		    		String s = line.substring(start, end);
		    		int index = Integer.parseInt(s);

		    		// récupération de la chaine json
		    		start = line.indexOf("{");
		    		end = line.indexOf("}", start) + 1;
		    		String json = line.substring(start, end);
		    		
		    		// récupération du rangeId
		    		ObjectMapper jsonMapper = new ObjectMapper();
		    		RangeIndexEntity entity = jsonMapper.readValue(json, RangeIndexEntity.class);
					int rangeId = entity.getId();

		    		System.out.println("indice="+index);
		    		System.out.println("json="+json);
		    		System.out.println("rangeId="+rangeId);

					batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".key", rangeId, null);
					batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes." + index + ".value", json, null);
		    	}
		    	if (line.contains("rangeIndexes.size")) {
		    		String sizeAsHex = StringUtils.substringBetween(line, "hexValue : ", " - clock :");
		    		System.out.println("sizeAsHex="+sizeAsHex);
		    		int sizeAsInt = Integer.parseInt(sizeAsHex, 16);
		    		System.out.println("sizeAsInt="+sizeAsInt);		    		
		    		byte[] sizeAsBytes = IndexReference.getSizeAsBytes(sizeAsInt);
		    		batch.withRow(IndexReferenceCF.get(), key).putColumn("rangeIndexes.size", sizeAsBytes, null);		    		
		    	}
		    }
		}		
		br.close();
		fstream.close();
		// Exécution du batch
		batch.execute();
	}
	
}
