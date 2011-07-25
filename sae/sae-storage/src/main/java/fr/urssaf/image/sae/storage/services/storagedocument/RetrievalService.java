package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.List;

import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit les services de récupération.<BR />
 * Ces services sont :
 * <ul>
 * <li>retrieveStorageDocumentContentByUUID : service qui permet récupérer le
 * contenu d’un document à partir du critère « UUIDCriteria ».</li>
 * <li>retrieveStorageDocumentMetaDatasByUUID : service qui permet de récupérer
 * les métadonnées d’un document à partir du critère « UUIDCriteria »
 * <li>retrieveStorageDocumentByUUID : service qui de récupérer un document à
 * partir du critère « UUIDCriteria ».</li>
 * </ul>
 */
public interface RetrievalService {

	/**
	 * Permet de récupérer le contenu d’un document à partir du critère «
	 * UUIDCriteria ».
	 * 
	 * @param uuidCriteria
	 *            : L'identifiant unique du document
	 * 
	 * @return Le contenu du document
	 * 
	 * @throws RetrievalServiceEx
	 *             Runtime exception
	 */

	byte[] retrieveStorageDocumentContentByUUID(final UUIDCriteria uuidCriteria)
			throws RetrievalServiceEx;

	/**
	 * Permet de récupérer les métadonnées d’un document à partir du critère «
	 * UUIDCriteria »
	 * 
	 * @param uuidCriteria
	 *            : L'identifiant unique du document
	 * 
	 * @return Une liste de metadonnées
	 * 
	 * @throws RetrievalServiceEx
	 *             Runtime exception
	 */
	List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
			final UUIDCriteria uuidCriteria) throws RetrievalServiceEx;

	/**
	 * Permet de récupérer un document à partir du critère « UUIDCriteria ».
	 * 
	 * @param uuidCriteria
	 *            : L'identifiant universel unique du document
	 * 
	 * @return Le document et ses métas données
	 * 
	 * @throws RetrievalServiceEx
	 *             Runtime exception
	 */
	StorageDocument retrieveStorageDocumentByUUID(
			final UUIDCriteria uuidCriteria) throws RetrievalServiceEx;

	/**
	 * Permet d'initialiser les paramètres dont le service aura besoin
	 * 
	 * @param storageConnectionParameter
	 *            : Les paramètres de connexion à la base de stockage
	 */
	@SuppressWarnings("PMD.LongVariable")
	void setRetrievalServiceParameter(
			final StorageConnectionParameter storageConnectionParameter);

}
