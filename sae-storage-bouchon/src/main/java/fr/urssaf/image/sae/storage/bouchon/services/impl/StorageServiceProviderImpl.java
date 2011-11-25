package fr.urssaf.image.sae.storage.bouchon.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.storage.bouchon.model.AbstractConnectionServiceProvider;
import fr.urssaf.image.sae.storage.bouchon.services.impl.connection.StorageConnectionServiceImpl;
import fr.urssaf.image.sae.storage.bouchon.services.impl.storagedocument.StorageDocumentServiceImpl;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.connection.StorageConnectionService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Fournit la façade des implementations des services
 * {@link StorageDocumentService } , {@link StorageConnectionService}. <li>
 * Attribut StorageDocumentService : l’ensemble des services d’insertion, de
 * recherche, de récupération.</li> <li>Attribut storageConnectionService :le
 * service de connexion à la base de stockage</li>
 * 
 * @author akenore
 * 
 */
@Service
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Qualifier("storageServiceProvider")
public class StorageServiceProviderImpl extends
		AbstractConnectionServiceProvider implements StorageServiceProvider {
	@SuppressWarnings("PMD.LongVariable")
	@Autowired
	@Qualifier("storageDocumentService")
	private StorageDocumentService storageDocumentService;

	@SuppressWarnings("PMD.LongVariable")
	@Autowired
	@Qualifier("storageConnectionService")
	private StorageConnectionService storageConnectionService;

	/**
	 * 
	 * @return les services d'insertion ,de recherche,récupération
	 */
	public final StorageDocumentService getStorageDocumentService() {
		return storageDocumentService;
	}

	/**
	 * 
	 * @return l'interface des services de connexion
	 */
	public final StorageConnectionService getStorageConnectionService() {
		return storageConnectionService;
	}

	/**
	 * Initialise les services d'insertion ,de recherche,récupération
	 * 
	 * @param storageDocumentService
	 *            : la façade des services d'insertion ,de
	 *            recherche,récupération
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setStorageDocumentService(
			final StorageDocumentServiceImpl storageDocumentService) {
		Assert.notNull(storageDocumentService,
				"storageDocumentService is required");
		this.storageDocumentService = storageDocumentService;
	}

	/**
	 * Initialise les services de connexion
	 * 
	 * @param storageConnectionService
	 *            : L'interface des services de connexion
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setStorageConnectionService(
			final StorageConnectionServiceImpl storageConnectionService) {
		this.storageConnectionService = storageConnectionService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final UUID insertStorageDocument(
			final StorageDocument storageDocument) throws InsertionServiceEx {

		return storageDocumentService.insertStorageDocument(storageDocument);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BulkInsertionResults bulkInsertStorageDocument(
			final StorageDocuments storageDocuments, final boolean allOrNothing) {

		return storageDocumentService.bulkInsertStorageDocument(
				storageDocuments, allOrNothing);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StorageDocuments searchStorageDocumentByLuceneCriteria(
			final LuceneCriteria luceneCriteria) throws SearchingServiceEx {

		return storageDocumentService
				.searchStorageDocumentByLuceneCriteria(luceneCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StorageDocument searchStorageDocumentByUUIDCriteria(
			final UUIDCriteria uuidCriteria) throws SearchingServiceEx {

		return storageDocumentService
				.searchStorageDocumentByUUIDCriteria(uuidCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final byte[] retrieveStorageDocumentContentByUUID(
			final UUIDCriteria uuidCriteria) throws RetrievalServiceEx {

		return storageDocumentService
				.retrieveStorageDocumentContentByUUID(uuidCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
			final UUIDCriteria uuidCriteria) throws RetrievalServiceEx {
		return storageDocumentService
				.retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StorageDocument retrieveStorageDocumentByUUID(
			final UUIDCriteria uuidCriteria) throws RetrievalServiceEx {

		return storageDocumentService
				.retrieveStorageDocumentByUUID(uuidCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void openConnection() throws ConnectionServiceEx {
		storageConnectionService.openConnection();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void closeConnexion() {
		storageConnectionService.closeConnexion();
	}

	/**
	 * Constructeur par défaut
	 */
	public StorageServiceProviderImpl() {
		super();
	}

	/**
	 * Constructeur
	 * 
	 * @param storageConnectionParameter
	 *            : les paramètres de connexion à la base de stockage
	 */
	@SuppressWarnings("PMD.LongVariable")
	public StorageServiceProviderImpl(
			final StorageConnectionParameter storageConnectionParameter) {
		super(storageConnectionParameter);
	}

}
