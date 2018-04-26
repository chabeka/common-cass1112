package fr.urssaf.image.sae.storage.bouchon.services.providers;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.bouchon.services.util.BeanHelper;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Classe de test unitaire des services d'insertion
 * 
 * @author akenore
 * 
 */

public class SearchServiceProviderTest extends
		AbstractServiceProviderTest {

	@Test
	public void searchStorageDocumentByLuceneCriteria()
			throws SearchingServiceEx {
	final 	StorageDocuments storageDocuments = getStorageServiceProvider()
				.searchStorageDocumentByLuceneCriteria(getLuceneCriteria());
		Assert.assertNotNull(storageDocuments);
		Assert.assertEquals(storageDocuments.getListOfStorageDocument()
				.size(), CompareTo.THIS_LIST_OF_STORAGEDOCUMENT_SIZE);
		
	}

	@Test
	public void searchStorageDocumentByUUIDCriteria() throws SearchingServiceEx {
		final 	StorageDocument  storageDocument =getStorageServiceProvider()
		.searchStorageDocumentByUUIDCriteria(getUuidCriteria());
		Assert.assertNotNull(storageDocument);
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(
				storageDocument.getMetadatas(), CompareTo.METADATA_CODE,
				CompareTo.THIS_DATA_3));
	}

}
