package fr.urssaf.image.sae.storage.dfce.data.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Cette classe contient les éléments nécessaires pour réaliser les tests
 * unitaires du service de recherche de lucene.
 * 
 * @author akenore
 * 
 */
@XStreamAlias("luceneQuery")
public class LuceneQuery {
	private String query;
	private String desiredMetadata;
	private String queryId;
	private String queryType;

	/**
	 * @return La requête.
	 */
	public final String getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            : La requête
	 */
	public final void setQuery(final String query) {
		this.query = query;
	}


	/**
	 * @return Les métadonnées désirées.
	 */
	public final String getDesiredMetadata() {
		return desiredMetadata;
	}

	/**
	 * @param desiredMetadata
	 *            : Les métadonnées désirées.
	 */
	public final void setDesiredMetadata(final String desiredMetadata) {
		this.desiredMetadata = desiredMetadata;
	}

	/**
	 * @return L'identifiant de la requête.
	 */
	public final String getQueryId() {
		return queryId;
	}

	/**
	 * @param queryId
	 *            : L'identifiant de la requête.
	 */
	public final void setQueryId(final String queryId) {
		this.queryId = queryId;
	}

	/**
	 * @return Le type de la requête.
	 */
	public final String getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType
	 *            : Le type de la requête.
	 */
	public final void setQueryType(final String queryType) {
		this.queryType = queryType;
	}

}
