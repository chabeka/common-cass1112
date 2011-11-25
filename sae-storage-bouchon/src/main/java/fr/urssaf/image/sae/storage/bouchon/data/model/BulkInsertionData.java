package fr.urssaf.image.sae.storage.bouchon.data.model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant de désérialiser les données définies pour  l'insertion en masse
 * <li>Attribut document : Map contenant la liste des métadonnées d'un document</li>
 * 
 * @author akenore
 * 
 */
@XStreamAlias(value = "bulkInsertionData")
public class BulkInsertionData extends AbstractData{
/**
 * 
 * @param document : la map contenant la liste des métadonnées d'un document
 */
	public BulkInsertionData(final Map<String, String> document) {
		super(document);
		
	}

}
