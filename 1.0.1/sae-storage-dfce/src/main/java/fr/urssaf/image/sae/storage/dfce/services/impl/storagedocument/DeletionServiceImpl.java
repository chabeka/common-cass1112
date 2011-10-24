package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.ArrayList;

import net.docubase.toolkit.exception.ged.FrozenDocumentException;
import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

/**
 * Implémente les services de l'interface {@link DeletionService}.
 * 
 * @author Akenore, Rhofir
 * 
 */
@Service
@Qualifier("deletionService")
public class DeletionServiceImpl extends AbstractServices implements
		DeletionService {
	@Autowired
	@Qualifier("searchingService")
	private SearchingService searchingService;

	/**
	 * @param searchingService
	 *            : Le service de recherche.
	 */
	public final void setSearchingService(
			final SearchingService searchingService) {
		this.searchingService = searchingService;
	}

	/**
	 * @return Le service de recherche.
	 */
	public final SearchingService getSearchingService() {
		return searchingService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Loggable(LogLevel.TRACE)
	@ServiceChecked
	public final void deleteStorageDocument(final UUIDCriteria uuidCriteria)
			throws DeletionServiceEx {
		try {
			getDfceService().getStoreService().deleteDocument(
					uuidCriteria.getUuid());
		} catch (FrozenDocumentException frozenExcept) {
			throw new DeletionServiceEx(
					StorageMessageHandler.getMessage(Constants.DEL_CODE_ERROR),
					frozenExcept.getMessage(), frozenExcept);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Loggable(LogLevel.TRACE)
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	@ServiceChecked
	public final void rollBack(final String processId) throws DeletionServiceEx {
		final String lucene = String.format("%s:%s", "itm", processId);
		StorageDocuments storageDocuments;
		try {
			storageDocuments = searchingService
					.searchStorageDocumentByLuceneCriteria(new LuceneCriteria(
							lucene, Integer.parseInt(StorageMessageHandler
									.getMessage("max.lucene.results")), null));
			for (StorageDocument storageDocument : storageDocuments
					.getAllStorageDocuments()) {
				deleteStorageDocument(new UUIDCriteria(
						storageDocument.getUuid(),
						new ArrayList<StorageMetadata>()));
			}
		} catch (NumberFormatException numberExcept) {
			throw new DeletionServiceEx(
					StorageMessageHandler.getMessage(Constants.DEL_CODE_ERROR),
					numberExcept.getMessage(), numberExcept);
		} catch (SearchingServiceEx searchingExcept) {
			 new DeletionServiceEx(
					StorageMessageHandler.getMessage(Constants.DEL_CODE_ERROR),
					searchingExcept.getMessage(), searchingExcept);
		} catch (QueryParseServiceEx searchingExcept) {
		   throw new DeletionServiceEx(
               StorageMessageHandler.getMessage(Constants.DEL_CODE_ERROR),
               searchingExcept.getMessage(), searchingExcept);
      }
	}

	 /**
	    * {@inheritDoc}
	    */
	public final <T> void setDeletionServiceParameter(final T parameter) {
		setDfceService((ServiceProvider) parameter);
	}
}
