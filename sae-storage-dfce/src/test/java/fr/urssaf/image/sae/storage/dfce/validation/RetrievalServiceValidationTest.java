package fr.urssaf.image.sae.storage.dfce.validation;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;

/**
 * Test les aspects pour la validation.
 * 
 * @author rhofir, kenore.
 * 
 */
public class RetrievalServiceValidationTest extends StorageServices {
	/**
	 * {@link fr.urssaf.image.sae.storage.dfce.RetrievalServiceValidation#retrieveStorageDocumentByUUID(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
	 * retrieveStorageDocumentContentByUUID} <br>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void retrieveStorageDocumentByUUID() throws RetrievalServiceEx {
		getRetrievalService().retrieveStorageDocumentByUUID(null);
	}

	/**
	 * {@link fr.urssaf.image.sae.storage.dfce.RetrievalServiceValidation#retrieveStorageDocumentContentByUUID(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
	 * retrieveStorageDocumentContentByUUID} <br>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void retrieveStorageDocumentContentByUUID()
			throws RetrievalServiceEx {
		getRetrievalService().retrieveStorageDocumentContentByUUID(null);
	}

	/**
	 * {@link fr.urssaf.image.sae.storage.dfce.RetrievalServiceValidation#retrieveStorageDocumentMetaDatasByUUID(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
	 * retrieveStorageDocumentMetaDatasByUUID} <br>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void retrieveStorageDocumentMetaDatasByUUID()
			throws RetrievalServiceEx {
		getRetrievalService().retrieveStorageDocumentMetaDatasByUUID(null);
	}
}
