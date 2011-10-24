package fr.urssaf.image.sae.storage.bouchon.data.model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant de désérialiser les données définies pour simuler la
 * récupération des métadonnées <li>Attribut document : Map contenant la liste
 * des métadonnées d'un document</li>
 * 
 * @author akenore
 * 
 */
@XStreamAlias(value = "retrieveMetaData")
public class RetrieveMetaData extends AbstractData {
	/**
	 * Constructeur
	 * @param document : La Map contenant la liste des métadonnées d'un document
	 */
	public RetrieveMetaData(final Map<String, String> document) {
		super(document);
		}
	
}
