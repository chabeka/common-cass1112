package fr.urssaf.image.sae.storage.bouchon.data.model;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant  de désérialiser les données définies pour simuler la recherche
 * <li>Attribut document : Map contenant la liste des métadonnées d'un document</li>
 * @author akenore
 *
 */
@XStreamAlias(value = "searchByLuceneData")
public class SearchByLuceneData extends AbstractData{
	/**
	 * Constructeur
	 * @param document : La Map contenant la liste des métadonnées d'un document
	 */
	public SearchByLuceneData(final Map<String, String> document) {
		super(document);
		
	}
	
}
