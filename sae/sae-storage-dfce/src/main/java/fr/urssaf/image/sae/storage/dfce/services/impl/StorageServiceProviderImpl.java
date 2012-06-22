package fr.urssaf.image.sae.storage.dfce.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.model.AbstractServiceProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Fournit la façade des implementations des services 
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.StorageDocumentServiceImpl}
 */
@Service
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Qualifier("storageServiceProvider")
public class StorageServiceProviderImpl extends AbstractServiceProvider
		implements StorageServiceProvider {
	@SuppressWarnings("PMD.LongVariable")
	@Autowired
	@Qualifier("storageDocumentService")
	private StorageDocumentService storageDocumentService;

	/**
	 * @return Les services d'insertion ,de recherche, de suppression,de
	 *         récupération
	 */
	public final StorageDocumentService getStorageDocumentService() {
		return storageDocumentService;
	}

	/**
	 * Initialise la façade des services d'insertion ,de recherche,récupération
	 * 
	 * @param storageDocumentService
	 *            : la façade des services d'insertion ,de
	 *            recherche,récupération
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setStorageDocumentService(
			final StorageDocumentService storageDocumentService) {
		this.storageDocumentService = storageDocumentService;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void openConnexion() throws ConnectionServiceEx {
		getDfceServicesManager().getConnection();
		storageDocumentService
				.setStorageDocumentServiceParameter(getDfceServicesManager()
						.getDFCEService());
	}

	/**
	 * {@inheritDoc}
	 */
	public final void closeConnexion() {
		getDfceServicesManager().closeConnection();
		
	}

}
