package fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria;

import java.util.List;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Classe concrète contenant les critères de recherche lucene.<BR />
 * Elle contient les attributs :
 * <ul>
 * <li>
 * luceneQuery : La requête Lucene.</li>
 * <li>
 * limit : Le nombre maximum de documents à retourner.</li>
 * </ul>
 */
public class LuceneCriteria extends AbstractCriteria {
	// Les attributs
	private String luceneQuery;
	private int limit;

	/**
	 * Retourne la requête lucene.
	 * 
	 * @return La requête lucene
	 */
	public final String getLuceneQuery() {
		return luceneQuery;
	}

	/**
	 * Initialise la requête lucene
	 * 
	 * @param luceneQuery
	 *            : Reaquête Lucene
	 */
	public final void setLuceneQuery(final String luceneQuery) {
		this.luceneQuery = luceneQuery;
	}

	/**
	 * Retourne la valeur maximum du nombre de document à retourner dans le
	 * cadre d’une recherche
	 * 
	 * @return Le maximum de documents à retourner
	 */
	public final Integer getLimit() {
		return limit;
	}

	/**
	 * Initialise la valeur limite du nombre limite de document à retourner dans
	 * le cadre d’une recherche
	 * 
	 * @param limit
	 *            : Le nombre maximum de documents à retourner dans une
	 *            recherche
	 */
	public final void setLimit(final int limit) {
		this.limit = limit;
	}

	/**
	 * Construit un {@link LuceneCriteria }
	 * 
	 * @param luceneQuery
	 *            : La requête lucene
	 * @param limit
	 *            : Le nombre maximum de documents retournés
	 * @param desiredStorageMetadatas
	 *            : Les métadonnées désirées
	 */
	@SuppressWarnings("PMD.LongVariable")
	public LuceneCriteria(final String luceneQuery, final int limit,
			final List<StorageMetadata> desiredStorageMetadatas) {
		super(desiredStorageMetadatas);
		this.luceneQuery = luceneQuery;
		this.limit = limit;
	}

}
