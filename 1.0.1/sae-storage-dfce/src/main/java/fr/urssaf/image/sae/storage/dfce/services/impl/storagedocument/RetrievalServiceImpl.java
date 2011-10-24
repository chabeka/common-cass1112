package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

/**
 * Implémente les services de l'interface {@link RetrievalService}.
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("retrievalService")
public class RetrievalServiceImpl extends AbstractServices implements
		RetrievalService {
	@Autowired
	@Qualifier("searchingService")
	private SearchingService searchingService;


	/**
	 * {@inheritDoc}
	 */
	@Loggable(LogLevel.TRACE)
	@ServiceChecked
	public final StorageDocument retrieveStorageDocumentByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
		try {
			searchingService.setSearchingServiceParameter(getDfceService());
			return searchingService
					.searchStorageDocumentByUUIDCriteria(uUIDCriteria);
		} catch (SearchingServiceEx srcSerEx) {
			throw new RetrievalServiceEx(
					StorageMessageHandler.getMessage(Constants.RTR_CODE_ERROR),
					srcSerEx.getMessage(), srcSerEx);
		} catch (Exception exc) {
			throw new RetrievalServiceEx(
					StorageMessageHandler.getMessage(Constants.RTR_CODE_ERROR),
					exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Loggable(LogLevel.TRACE)
	@ServiceChecked
	public final byte[] retrieveStorageDocumentContentByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
		try {
			final Document docDfce = getDfceService().getSearchService()
					.getDocumentByUUID(getBaseDFCE(), uUIDCriteria.getUuid());
			final InputStream docContent = getDfceService().getStoreService()
					.getDocumentFile(docDfce);
			return IOUtils.toByteArray(docContent);
		} catch (IOException except) {
			throw new RetrievalServiceEx(
					StorageMessageHandler.getMessage(Constants.RTR_CODE_ERROR),
					except.getMessage(), except);
		} catch (Exception except) {
			throw new RetrievalServiceEx(
					StorageMessageHandler.getMessage(Constants.SRH_CODE_ERROR),
					except.getMessage(), except);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Loggable(LogLevel.TRACE)
	@ServiceChecked
	public final List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
			final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
		try {
			searchingService.setSearchingServiceParameter(getDfceService());
			return searchingService.searchMetaDatasByUUIDCriteria(uUIDCriteria)
					.getMetadatas();
		} catch (SearchingServiceEx srcSerEx) {
			throw new RetrievalServiceEx(srcSerEx.getMessage(), srcSerEx);
		} catch (Exception except) {
			throw new RetrievalServiceEx(
					StorageMessageHandler.getMessage(Constants.RTR_CODE_ERROR),
					except.getMessage(), except);
		}
	}

	/**
	 * @param searchingService
	 *            the searchingService to set
	 */
	public final void setSearchingService(
			final SearchingService searchingService) {
		this.searchingService = searchingService;
	}

	/**
	 * @return the searchingService
	 */
	public final SearchingService getSearchingService() {
		return searchingService;
	}

	/**
	 * {@inheritDoc}
	 */
	public final <T> void setRetrievalServiceParameter(final T parameter) {
		setDfceService((ServiceProvider) parameter);
	}

}
