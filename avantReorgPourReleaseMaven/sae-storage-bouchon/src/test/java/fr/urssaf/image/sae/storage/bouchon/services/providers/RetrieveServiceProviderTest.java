package fr.urssaf.image.sae.storage.bouchon.services.providers;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.bouchon.services.util.BeanHelper;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Classe de tests unitaires des services de récupération
 * 
 * @author akenore
 * 
 */

public class RetrieveServiceProviderTest extends
		AbstractServiceProviderTest {

	@Test
	public  void retrieveStorageDocumentContentByUUID()
			throws RetrievalServiceEx {
	final	byte[] retrieveDoc = getStorageServiceProvider()
				.retrieveStorageDocumentContentByUUID(getUuidCriteria());
		Assert.assertNotNull(retrieveDoc);
		Assert.assertTrue(retrieveDoc.length > 0);
	}

	@Test
	@SuppressWarnings("PMD.LongVariable")
	public void retrieveStorageDocumentByUUID()
			throws RetrievalServiceEx {
	final	StorageDocument retrieveDocMetaData = getStorageServiceProvider()
				.retrieveStorageDocumentByUUID(getUuidCriteria());
		Assert.assertNotNull(retrieveDocMetaData);
		Assert.assertNotNull(retrieveDocMetaData.getContent());
		Assert.assertNotNull(retrieveDocMetaData.getContent().length > 0);
		Assert.assertNotNull(retrieveDocMetaData.getMetadatas());
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(retrieveDocMetaData.getMetadatas(),
				CompareTo.METADATA_CODE, CompareTo.THIS_DATA_3));
	}

	@Test
	public void retrieveStorageDocumentMetaDatasByUUID() throws RetrievalServiceEx {
		final List<StorageMetadata> retrieveMetaData = getStorageServiceProvider()
				.retrieveStorageDocumentMetaDatasByUUID(getUuidCriteria());
		Assert.assertNotNull(retrieveMetaData);
		Assert.assertEquals(CompareTo.THIS_LIST_OF_METADATA_RETRIEVE_SIZE,
				retrieveMetaData.size());
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(retrieveMetaData,
				CompareTo.METADATA_CODE, CompareTo.THIS_DATA_3));
	}

}
