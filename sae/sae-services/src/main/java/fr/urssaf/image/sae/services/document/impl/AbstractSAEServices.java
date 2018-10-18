package fr.urssaf.image.sae.services.document.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Classe abstraite contenant les attributs communs de toutes les
 * implementations:
 * <ul>
 * <li>{@link fr.urssaf.image.sae.services.document.SAESearchService Recherche}
 * : Implementation de recherche,</li>
 * <li>{@link fr.urssaf.image.sae.services.consultation.SAEConsultationService
 * Consultation} : Implementation de la consultation.</li>
 * </ul>
 */
public abstract class AbstractSAEServices {

	@Autowired
	@Qualifier("storageDocumentService")
	private StorageDocumentService storageDocumentService;

	/**
	 * @return La façade de services Storage DFCE.
	 */
	public final StorageDocumentService getStorageDocumentService() {
		return storageDocumentService;
	}

	/**
	 * @param storageServiceProvider
	 *            : La façade de services Storage DFCE.
	 */
	public final void setStorageServiceProvider(final StorageDocumentService storageDocumentService) {
		this.storageDocumentService = storageDocumentService;
	}

	/**
	 * Contrôle si la liste de métadonnées passée en paramètre contient la
	 * métadonnée gel à true (Document gelé).
	 * 
	 * @param listeStorageMeta
	 *            liste de métadonnées
	 * @return true si le document est gelé
	 * @throws RetrievalServiceEx
	 * @{@link RetrievalServiceEx}
	 * @throws SearchingServiceEx
	 * @{@link SearchingServiceEx}
	 */
	public boolean isFrozenDocument(List<StorageMetadata> listeStorageMeta) throws RetrievalServiceEx {
		if (listeStorageMeta != null && !listeStorageMeta.isEmpty()) {
			for (StorageMetadata meta : listeStorageMeta) {
				if (meta.getShortCode().equals(StorageTechnicalMetadatas.GEL.getShortCode())) {
					if (meta.getValue() == Boolean.TRUE) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
