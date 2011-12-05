package fr.urssaf.image.sae.storage.bouchon.data;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.storage.bouchon.data.provider.RetrieveDataProvider;
import fr.urssaf.image.sae.storage.bouchon.services.providers.CompareTo;
import fr.urssaf.image.sae.storage.bouchon.services.util.BeanHelper;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Tests Unitaires du fournisseur de données des services de récupération. <Li>
 * Attribut retrieveDoc : Le flux binaire</Li> <Li>Attribut retrieveDocMetaData
 * : Le flux binaire et les métadonnées</Li> <Li>Attribut retrieveMetaData : Les
 * métadonnées</Li>
 * 
 * @author akenore
 * 
 */
public class RetrieveDataProviderTest {

	private byte[] retrieveDoc = null;
	@SuppressWarnings("PMD.LongVariable")
	private StorageDocument retrieveDocMetaData = null;
	@SuppressWarnings("PMD.LongVariable")
	private List<StorageMetadata> retrieveMetaData = null;

	/**
	 * 
	 * @return Le flux binaire
	 */
	@SuppressWarnings("PMD.MethodReturnsInternalArray")
	public byte[] getRetrieveDoc() {
		return retrieveDoc;
	}
	
	/**
	 *
	 * @param retrieveDoc
	 *            : Le flux binaire
	 */
	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public void setRetrieveDoc(final byte[] retrieveDoc) {
		this.retrieveDoc = retrieveDoc;
	}

	/**
	 * 
	 * @return Le flux binaire et les métadonnées
	 */
	public StorageDocument getRetrieveDocMetaData() {
		return retrieveDocMetaData;
	}

	/**
	 * 
	 * @param retrieveDocMetaData
	 *            : Le flux binaire et les métadonnées
	 */
	@SuppressWarnings("PMD.LongVariable")
	public void setRetrieveDocMetaData(final StorageDocument retrieveDocMetaData) {
		this.retrieveDocMetaData = retrieveDocMetaData;
	}

	/**
	 * 
	 * @return Les métadonnées
	 */
	public List<StorageMetadata> getRetrieveMetaData() {
		return retrieveMetaData;
	}

	/**
	 * 
	 * @param retrieveMetaData
	 *            : Les métadonnées
	 */
	public void setRetrieveMetaData(final List<StorageMetadata> retrieveMetaData) {
		this.retrieveMetaData = retrieveMetaData;
	}

	/**
	 * Initialise les données de tests
	 */
	@Before
	public void initialize() {
		retrieveDoc = RetrieveDataProvider
				.retrieveStorageContentByUUIDCriteriaData(0);
		retrieveDocMetaData = RetrieveDataProvider
				.retrieveStorageDocumentByUUIDCriteriaData(0);
		retrieveMetaData = RetrieveDataProvider
				.retrieveStorageMetaDataByUUIDCriteriaData(0);
	}

	/**
	 * Test si le contenu du document récupéré n'est pas null
	 * 
	 */
	@Test
	public void retrieveStorageDocumentContentNotNull() {
		Assert.assertNotNull(retrieveDoc);
	}

	/**
	 * Test si le document a bien un contenu
	 * 
	 */
	public void retrieveStorageDocumentContentSize() {
		Assert.assertTrue(retrieveDoc.length > 0);
	}

	/**
	 * Test si les métadonnées ne sont pas null
	 * 
	 */
	@Test
	public void retrieveStorageDocumentMetaDataNotNull() {
		Assert.assertNotNull(retrieveMetaData);
	}

	/**
	 * Test si le nombre de métadonnées correspond bien au nombre attendu
	 * 
	 */

	@Test
	public void retrieveStorageDocumentMetaDataSize() {
		Assert.assertEquals(CompareTo.THIS_LIST_OF_METADATA_RETRIEVE_SIZE,
				retrieveMetaData.size());
	}

	/**
	 * Test si les valeurs des métadonnées correspondent bien aux valeurs
	 * attendues.
	 * 
	 */
	@Test
	public void retrieveStorageDocumentMetaDataContent() {
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(retrieveMetaData,
				CompareTo.METADATA_CODE, CompareTo.THIS_DATA_3));
	}

	/**
	 * Test si document n'est pas null
	 * 
	 */
	@Test
	public void retrieveStorageDocumentByUUID() {
		Assert.assertNotNull(retrieveDocMetaData);
	}

	/**
	 * Test si document a bien un contenu
	 * 
	 */
	@Test
	public void retrieveStorageDocumentContentByUUID() {
		Assert.assertNotNull(retrieveDocMetaData.getContent());
		Assert.assertNotNull(retrieveDocMetaData.getContent().length > 0);
	}

	/**
	 * Test si document a bien des métadonnées qui correspondent bien à ceux
	 * attendus.
	 * 
	 */
	@Test
	public void retrieveStorageDocumentMetadataByUUID() {
		Assert.assertNotNull(retrieveDocMetaData.getMetadatas());
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(
				retrieveDocMetaData.getMetadatas(), CompareTo.METADATA_CODE,
				CompareTo.THIS_DATA_3));
	}

}
