package fr.urssaf.image.sae.storage.bouchon.data.model;

import java.util.Map;

/**
 * Classe abstraite contenant les données communes de la désérialisation <li>
 * Attribut document : Map contenant la liste des métadonnées d'un document</li>
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD")
public abstract class AbstractData {

	private Map<String, String> document;

	/**
	 * Initialise la map contenant la liste des métadonnées d'un document
	 * 
	 * @param document
	 *            :Map contenant la liste des métadonnées d'un document
	 */
	public final void setDocument(final Map<String, String> document) {
		this.document = document;
	}

	/**
	 * @return La Map contenant la liste des métadonnées d'un document
	 */
	public final Map<String, String> getDocument() {
		return document;
	}
	/**
	 * Constructeur
	 * @param document : La Map contenant la liste des métadonnées d'un document
	 */
	public AbstractData(final Map<String, String> document){
		this.document =document;
	}
}
