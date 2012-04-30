package fr.urssaf.image.sae.storage.services.storagedocument;

import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit les services de recherche de document<BR />
 * Ces services sont :
 * <ul>
 * <li>searchStorageDocumentByLuceneCriteria : service qui permet de faire une
 * recherche par une requête lucene.</li>
 * <li>searchStorageDocumentByUUIDCriteria : service qui permet de faire une
 * recherche de document par UUID</li>
 * <li>searchMetaDatasByUUIDCriteria :de faire une recherche des métadonnées par
 * UUID.</li>
 * </ul>
 */
public interface SearchingService {

	/**
	 * Permet de faire une recherche par une requête lucene.
	 * 
	 * @param luceneCriteria
	 *            : La requête Lucene
	 * 
	 * @return Les résultats de la recherche
	 * 
	 * @throws SearchingServiceEx
	 *             Exception lévée lorsque la recherche ne se déroule pas bien.
	 * @throws QueryParseServiceEx Exception levée lorsque du parsing de la requête.
	 */

	StorageDocuments searchStorageDocumentByLuceneCriteria(
			final LuceneCriteria luceneCriteria) throws SearchingServiceEx, QueryParseServiceEx;

	/**
	 * Permet de faire une recherche de document par UUID.
	 * 
	 * @param uuidCriteria
	 *            : L'UUID du document à rechercher
	 * 
	 * @return un strorageDocument
	 * 
	 * @throws SearchingServiceEx
	 *              Exception lévée lorsque la recherche ne se déroule pas bien.
	 */
	StorageDocument searchStorageDocumentByUUIDCriteria(
			final UUIDCriteria uuidCriteria) throws SearchingServiceEx;

	/**
	 * Permet de faire une recherche des métadonnées par UUID.
	 * 
	 * @param uuidCriteria
	 *            : L'UUID du document à rechercher
	 * 
	 * @return Le resultat de la recherche
	 * 
	 * @throws SearchingServiceEx
	 *              Exception lévée lorsque la recherche ne se déroule pas bien.
	 */
	StorageDocument searchMetaDatasByUUIDCriteria(
			final UUIDCriteria uuidCriteria) throws SearchingServiceEx;
	
	/**
	 * 
	 * @param <T> : Le type générique.
	 * @param parameter : Le paramètre du service {@link SearchingService}
	 */
	 <T> void setSearchingServiceParameter(T parameter);
	

}
