package fr.urssaf.image.sae.storage.bouchon.services;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.bouchon.services.providers.AbstractStorageServices;
import fr.urssaf.image.sae.storage.bouchon.services.providers.CompareTo;
import fr.urssaf.image.sae.storage.bouchon.services.util.BeanHelper;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Classe de test unitaire des services de recherche
 * 
 * @author akenore
 * 
 */

public class SearchServiceTest extends AbstractStorageServices {

	/**
	 * Test si la recherche lucene ne retourne pas une liste null<br/>
	 * Test si la recherche lucene retourne pas une liste de 3 documents<br/>
	 */
	@Test
	public void searchStorageDocumentByLuceneCriteria()
			throws SearchingServiceEx {
	final	StorageDocuments storageDocuments = getSearchingService()
				.searchStorageDocumentByLuceneCriteria(getLuceneCriteria());
		Assert.assertNotNull(storageDocuments);
		Assert.assertEquals(storageDocuments.getListOfStorageDocument().size(),
				CompareTo.THIS_LIST_OF_STORAGEDOCUMENT_SIZE);

	}

	/**
	 * Test si la recherche par uuid ne retourne pas une liste null<br/>
	 * Test si la recherche par uuid retourne les valeurs attendues<br/>
	 */
	@Test
	public void searchStorageDocumentByUUIDCriteria() throws SearchingServiceEx {
	final	StorageDocument storageDocument = getSearchingService()
				.searchStorageDocumentByUUIDCriteria(getUuidCriteria());
		Assert.assertNotNull(storageDocument);
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(
				storageDocument.getMetadatas(), CompareTo.METADATA_CODE,
				CompareTo.THIS_DATA_3));
	}

}
