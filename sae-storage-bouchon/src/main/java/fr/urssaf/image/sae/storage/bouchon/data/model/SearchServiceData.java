package fr.urssaf.image.sae.storage.bouchon.data.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Classe permettant de désérialiser les données définies pour les services de
 * recherches. <li>Attribut searchByLuceneData : Données pour le service de
 * recherche lucene</li> <li>Attribut searchByUUIDData : Données pour le service
 * de recherche par UUID</li>
 * 
 * @author akenore
 * 
 */
@XStreamAlias(value = "searchServiceData")
public class SearchServiceData {

	@XStreamImplicit(itemFieldName = "searchByLuceneData")
	@SuppressWarnings("PMD.LongVariable")
	private List<SearchByLuceneData> searchByLuceneData;
	@XStreamImplicit(itemFieldName = "searchByUUIDData")
	private List<SearchByUUIDData> searchByUUIDData;

	/**
	 * Constructeur
	 * 
	 * @param searchByLuceneData
	 *            : Données pour le service de recherche lucene
	 * @param searchByUUIDData
	 *            : Données pour le service de recherche par UUID
	 */
	@SuppressWarnings("PMD.LongVariable")
	public SearchServiceData(final List<SearchByLuceneData> searchByLuceneData,
			final List<SearchByUUIDData> searchByUUIDData) {
		this.searchByLuceneData = searchByLuceneData;
		this.searchByUUIDData = searchByUUIDData;
	}

	/**
	 * @param searchByLuceneData : La données pour le service de recherche lucene
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setSearchByLuceneData(final List<SearchByLuceneData> searchByLuceneData) {
		this.searchByLuceneData = searchByLuceneData;
	}

	/**
	 * @return La données pour le service de recherche lucene
	 */
	public final List<SearchByLuceneData> getSearchByLuceneData() {
		return searchByLuceneData;
	}

	/**
	 * @param searchByUUIDData : La données pour le service de recherche par UUID
	 */
	public final void setSearchByUUIDData(final List<SearchByUUIDData> searchByUUIDData) {
		this.searchByUUIDData = searchByUUIDData;
	}

	/**
	 * @return La données pour le service de recherche par UUID
	 */
	public final List<SearchByUUIDData> getSearchByUUIDData() {
		return searchByUUIDData;
	}
}
