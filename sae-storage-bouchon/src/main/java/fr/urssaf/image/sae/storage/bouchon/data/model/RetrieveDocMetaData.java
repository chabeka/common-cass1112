package fr.urssaf.image.sae.storage.bouchon.data.model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant de désérialiser les données définies pour simuler la
 * récuperation des documents <li>Attribut document : Map contenant la liste des
 * métadonnées d'un document</li>
 * 
 * @author akenore
 * 
 */
@XStreamAlias(value = "retrieveDocMetaData")
public class RetrieveDocMetaData extends AbstractData {
	/**
	 * Constructeur
	 * @param document : La Map contenant la liste des métadonnées d'un document
	 */
	public RetrieveDocMetaData(final Map<String, String> document) {
		super(document);
		
	}
	
}
