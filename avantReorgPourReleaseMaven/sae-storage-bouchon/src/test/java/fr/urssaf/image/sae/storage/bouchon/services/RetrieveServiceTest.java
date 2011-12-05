package fr.urssaf.image.sae.storage.bouchon.services;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.bouchon.services.providers.AbstractStorageServices;
import fr.urssaf.image.sae.storage.bouchon.services.providers.CompareTo;
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

public class RetrieveServiceTest extends
AbstractStorageServices {
	/**
	 * Test si le contenu du document récupéré n'est pas null<br/>
	 * Test si le document a bien un contenu
	 */
	@Test
	public void retrieveStorageDocumentContentByUUID()
			throws RetrievalServiceEx {
	final 	byte[] retrieveDoc = getRetrievalService()
				.retrieveStorageDocumentContentByUUID(getUuidCriteria());
		Assert.assertNotNull(retrieveDoc);
		Assert.assertTrue(retrieveDoc.length > 0);
	}
	/**
	 * Test si document n'est pas null<br/>
	 * Test si document a bien un contenu<br/>
	 * Test si document a bien des métadonnées qui correspondent bien à ceux
	 * attendus.
	 */
	@Test
	@SuppressWarnings("PMD.LongVariable")
	public void retrieveStorageDocumentByUUID()
			throws RetrievalServiceEx {
		final StorageDocument retrieveDocMetaData = getRetrievalService()
				.retrieveStorageDocumentByUUID(getUuidCriteria());
		Assert.assertNotNull(retrieveDocMetaData);
		Assert.assertNotNull(retrieveDocMetaData.getContent());
		Assert.assertNotNull(retrieveDocMetaData.getContent().length > 0);
		Assert.assertNotNull(retrieveDocMetaData.getMetadatas());
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(retrieveDocMetaData.getMetadatas(),
				CompareTo.METADATA_CODE, CompareTo.THIS_DATA_3));
	}
	/**
	 * Test si les métadonnées ne sont pas null<br/>
	 * Test si les valeurs des métadonnées correspondent bien aux valeurs
	 * attendues.
	 * 
	 */
	@Test
	public void retrieveStorageDocumentMetaDatasByUUID() throws RetrievalServiceEx {
	final 	List<StorageMetadata> retrieveMetaData = getRetrievalService()
				.retrieveStorageDocumentMetaDatasByUUID(getUuidCriteria());
		Assert.assertNotNull(retrieveMetaData);
		Assert.assertEquals(CompareTo.THIS_LIST_OF_METADATA_RETRIEVE_SIZE,
				retrieveMetaData.size());
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(retrieveMetaData,
				CompareTo.METADATA_CODE, CompareTo.THIS_DATA_3));
	}

}
