package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.List;

import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit l’ensemble des services : <BR />
 * <ul>
 * <li>
 * {@link InsertionService } : services d’insertion.</li>
 * <li>
 * {@link SearchingService} : services de recherche.</li>
 * <li>
 * {@link RetrievalService} : services de récupération.</li>
 * <li>
 * {@link DeletionService} : services de suppression.</li>
 * </ul>
 */
public interface StorageDocumentService {
	/**
	 * Permet d'insérer un document unique
	 * 
	 * @param storageDocument
	 *            : Le document à stocker
	 * 
	 * @return Le document
	 * 
	 * @throws InsertionServiceEx
	 *             Exception lévée lorsque l'insertion d'un document ne se
	 *             déroule pas bien.
	 */
	StorageDocument insertStorageDocument(final StorageDocument storageDocument)
			throws InsertionServiceEx;

	/**
	 * Permet de réaliser une insertion en masse de documents
	 * 
	 * @param storageDocuments
	 *            : Les documents à stocker
	 * @param allOrNothing
	 *            : boolean qui permet de faire une insertion en masse en mode
	 *            tout ou rien
	 * @return Le résultat des insertions réussies et échouées
	 * @throws InsertionServiceEx
	 *             : Exception lévée lors de l'insertion en masse.
	 */
	BulkInsertionResults bulkInsertStorageDocument(
			final StorageDocuments storageDocuments, final boolean allOrNothing)
			throws InsertionServiceEx;

	/**
	 * Permet de faire une recherche des métadonnées par UUID.
	 * 
	 * @param uuidCriteria
	 *            : L'UUID du document à rechercher
	 * 
	 * @return Le resultat de la recherche
	 * 
	 * @throws SearchingServiceEx
	 *             Exception lévée lorsque la recherche ne se déroule pas bien.
	 */
	StorageDocument searchMetaDatasByUUIDCriteria(
			final UUIDCriteria uuidCriteria) throws SearchingServiceEx;

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
	 * @throws QueryParseServiceEx   Exception levée lorsque du parsing de la requête.
	 */
	StorageDocuments searchStorageDocumentByLuceneCriteria(
			final LuceneCriteria luceneCriteria) throws SearchingServiceEx, QueryParseServiceEx;

	/**
	 * Permet de faire une recherche de document par UUID.
	 * 
	 * @param uUIDCriteria
	 *            : L'UUID du document à rechercher
	 * 
	 * @return un strorageDocument
	 * 
	 * @throws SearchingServiceEx
	 *             Exception lévée lorsque la recherche ne se déroule pas bien.
	 */
	StorageDocument searchStorageDocumentByUUIDCriteria(
			final UUIDCriteria uUIDCriteria) throws SearchingServiceEx;

	/**
	 * Permet de récupérer un document à partir du critère « UUIDCriteria ».
	 * 
	 * @param uUIDCriteria
	 *            : L'identifiant universel unique du document
	 * 
	 * @return Le document et ses métadonnées
	 * 
	 * @throws RetrievalServiceEx
	 *             Exception lévée lorsque la consultation ne se déroule pas
	 *             bien.
	 */
	StorageDocument retrieveStorageDocumentByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx;

	/**
	 * Permet de récupérer le contenu d’un document à partir du critère «
	 * UUIDCriteria ».
	 * 
	 * @param uUIDCriteria
	 *            : L'identifiant unique du document
	 * 
	 * @return Le contenu du document
	 * 
	 * @throws RetrievalServiceEx
	 *             Exception lévée lorsque la consultation ne se déroule pas
	 *             bien
	 * 
	 */
	byte[] retrieveStorageDocumentContentByUUID(final UUIDCriteria uUIDCriteria)
			throws RetrievalServiceEx;

	/**
	 * Permet de récupérer les métadonnées d’un document à partir du critère «
	 * UUIDCriteria »
	 * 
	 * @param uUIDCriteria
	 *            : L'identifiant unique du document
	 * 
	 * @return Une liste de metadonnées
	 * 
	 * @throws RetrievalServiceEx
	 *             Exception lévée lorsque la consultation ne se déroule pas
	 *             bien
	 */
	List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx;

	/**
	 * Permet de supprimer un StorageDocument à partir du critère « UUIDCriteria
	 * 
	 * 
	 * @param uuidCriteria
	 *            : L'identifiant unique du document
	 * 
	 * 
	 * 
	 *    @throws  DeletionServiceEx Exception lévée lorsque la suppression ne se
	 *            réalise pas correctement
	 */
	void deleteStorageDocument(final UUIDCriteria uuidCriteria)
			throws DeletionServiceEx;

	/**
	 * Permet de faire un rollback à partir d'un identifiant de traitement.
	 * 
	 * 
	 * @param processId
	 *            : L'identifiant du traitement
	 * 
	 * @throws DeletionServiceEx
	 *             Exception lévée lorsque la suppression ne se réalise pas
	 *             correctement
	 * 
	 */

	void rollBack(final String processId) throws DeletionServiceEx;

	/**
	 * 
	 * @param <T>
	 *            : Le type générique.
	 * @param parameter
	 *            : Le paramètre du service {@link StorageDocumentService}
	 */
	<T> void setStorageDocumentServiceParameter(T parameter);

}
