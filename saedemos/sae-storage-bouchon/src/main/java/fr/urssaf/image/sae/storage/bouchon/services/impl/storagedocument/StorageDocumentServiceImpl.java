package fr.urssaf.image.sae.storage.bouchon.services.impl.storagedocument;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.bouchon.model.AbstractServices;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageBase;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;


/**
 * 
 * @author akenore
 *
 */
@Service
@Qualifier("storageDocumentService")
public class StorageDocumentServiceImpl extends AbstractServices implements
		StorageDocumentService {
	@Autowired
	@Qualifier("insertionService")
	private InsertionService insertionService;
	@Autowired
	@Qualifier("searchingService")
	private SearchingService searchingService;
	@Autowired
	@Qualifier("retrievalService")
	private RetrievalService retrievalService;

	/**
	 * Constructeur de la classe
	 * 
	 * @param storageBase
	 *            : La base de stockage
	 */
	public StorageDocumentServiceImpl(final StorageBase storageBase) {
		super(storageBase);
	}

	/**
	 * Constructeur par défaut
	 */
	public StorageDocumentServiceImpl() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final UUID insertStorageDocument(final StorageDocument storageDocument)
			throws InsertionServiceEx {
		return insertionService.insertStorageDocument(storageDocument);
	}

	/**
	 * 
	 * @return Les services d'insertions
	 */
	public final InsertionService getInsertionService() {
		return insertionService;
	}

	/**
	 * 
	 * @return les services de recherche
	 */
	public final SearchingService getSearchingService() {
		return searchingService;
	}

	/**
	 * 
	 * @return les services récupération
	 */
	public final RetrievalService getRetrievalService() {
		return retrievalService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StorageDocuments searchStorageDocumentByLuceneCriteria(
			final LuceneCriteria luceneCriteria) throws SearchingServiceEx {

		return searchingService
				.searchStorageDocumentByLuceneCriteria(luceneCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StorageDocument searchStorageDocumentByUUIDCriteria(
			final UUIDCriteria uUIDCriteria) throws SearchingServiceEx {

		return searchingService
				.searchStorageDocumentByUUIDCriteria(uUIDCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	public final StorageDocument retrieveStorageDocumentByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {

		return retrievalService.retrieveStorageDocumentByUUID(uUIDCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final byte[] retrieveStorageDocumentContentByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {

		return retrievalService
				.retrieveStorageDocumentContentByUUID(uUIDCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {

		return retrievalService
				.retrieveStorageDocumentMetaDatasByUUID(uUIDCriteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BulkInsertionResults bulkInsertStorageDocument(
			final StorageDocuments storageDocuments, final boolean allOrNothing) {

		return insertionService.bulkInsertStorageDocument(storageDocuments,
				allOrNothing);
	}

	/**
	 * Initialise les services d'insertion
	 * 
	 * @param insertionService
	 *            : les services d'insertions
	 */
	public final void setInsertionService(final InsertionService insertionService) {
		this.insertionService = insertionService;
	}

	/**
	 * Initialise les services de recherche
	 * 
	 * @param searchingService : Le service de recherche
	 */
	public final void setSearchingService(final SearchingService searchingService) {
		this.searchingService = searchingService;
	}

	/**
	 * Initialise les services de récupération
	 * 
	 * @param retrievalService
	 *            : les services de récupération
	 */
	public final void setRetrievalService(final RetrievalService retrievalService) {
		this.retrievalService = retrievalService;
	}

}
