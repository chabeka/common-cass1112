package fr.urssaf.image.sae.storage.bouchon.services.impl.storagedocument;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.bouchon.data.provider.RetrieveDataProvider;
import fr.urssaf.image.sae.storage.bouchon.model.AbstractDfceService;
import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageBase;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;

/**
 * Implémente les services de l'interface RetrievalService.
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("retrievalService")
public class RetrievalServiceImpl extends AbstractDfceService implements
		RetrievalService {
	/**
	 * Constructeur de la classe
	 * 
	 * @param storageBase
	 *            : La base de stockage
	 */
	public RetrievalServiceImpl(final StorageBase storageBase) {
		super(storageBase);
	}

	/**
	 * Construction par défaut
	 **/
	public RetrievalServiceImpl() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StorageDocument retrieveStorageDocumentByUUID(
		final	UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
		try {
			return RetrieveDataProvider
					.retrieveStorageDocumentByUUIDCriteriaData(0);
		} catch (RuntimeException runtimeException) {
			throw new RetrievalServiceEx(Message.RETRIEVE_FAILED.getMessage(),
					runtimeException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final byte[] retrieveStorageDocumentContentByUUID(final UUIDCriteria uUIDCriteria)
			throws RetrievalServiceEx {

		try {
			return RetrieveDataProvider
					.retrieveStorageContentByUUIDCriteriaData(0);
		} catch (RuntimeException runtimeException) {
			throw new RetrievalServiceEx(Message.RETRIEVE_FAILED.getMessage(),
					runtimeException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
		try {
			return RetrieveDataProvider
					.retrieveStorageMetaDataByUUIDCriteriaData(0);
		} catch (RuntimeException runtimeException) {
			throw new RetrievalServiceEx(Message.RETRIEVE_FAILED.getMessage(),
					runtimeException);
		}
	}

}
