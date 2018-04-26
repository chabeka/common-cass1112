package fr.urssaf.image.sae.storage.bouchon.services.impl.storagedocument;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.bouchon.data.provider.SearchDataProvider;
import fr.urssaf.image.sae.storage.bouchon.model.AbstractDfceService;
import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageBase;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

/**
 * Implémente les services de l'interface SearchingService.
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("searchingService")
public class SearchingServiceImpl extends AbstractDfceService implements
		SearchingService {

	/**
	 * Constructeur de la classe
	 * 
	 * @param storageBase
	 *            : La base de stockage
	 */
	public SearchingServiceImpl(final StorageBase storageBase) {
		super(storageBase);
	}

	/**
	 * Construction par défaut
	 **/
	public SearchingServiceImpl() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StorageDocuments searchStorageDocumentByLuceneCriteria(
			final LuceneCriteria luceneCriteria) throws SearchingServiceEx {
		try {
			return SearchDataProvider.getSearchStorageDocumentByLuceneCriteriaData();
		} catch (RuntimeException runtimeException) {
			throw new SearchingServiceEx(Message.SEARCHING_FAILED.getMessage(), runtimeException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StorageDocument searchStorageDocumentByUUIDCriteria(
			final UUIDCriteria uUIDCriteria) throws SearchingServiceEx {

		try {
			return SearchDataProvider.getSearchStorageDocumentByUUIDCriteria(0);
		} catch (RuntimeException runtimeException) {
			throw new SearchingServiceEx(Message.SEARCHING_FAILED.getMessage(), runtimeException);
		}
	}

}
