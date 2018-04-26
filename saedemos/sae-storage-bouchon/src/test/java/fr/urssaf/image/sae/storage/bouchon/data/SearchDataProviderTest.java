package fr.urssaf.image.sae.storage.bouchon.data;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.storage.bouchon.data.provider.SearchDataProvider;
import fr.urssaf.image.sae.storage.bouchon.services.providers.CompareTo;
import fr.urssaf.image.sae.storage.bouchon.services.util.BeanHelper;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Tests Unitaires du fournisseur de données des services de récupération. <Li>
 * Attribut storageDocuments :La liste des documents</Li> <Li>Attribut
 * storageDocument : Le document</Li>
 * 
 * @author akenore
 * 
 */
public class SearchDataProviderTest {
	private StorageDocuments storageDocuments = null;
	private StorageDocument storageDocument = null;

	/**
	 * Initialise les données de tests
	 */
	@Before
	public void initialize() {
		storageDocuments = SearchDataProvider
				.getSearchStorageDocumentByLuceneCriteriaData();
		storageDocument = SearchDataProvider
				.getSearchStorageDocumentByUUIDCriteria(0);
	}

	/**
	 * 
	 * @return La liste des documents
	 */
	public StorageDocuments getStorageDocuments() {
		return storageDocuments;
	}

	/**
	 * 
	 * @param storageDocuments
	 *            : La liste des documents
	 */
	public void setStorageDocuments(final StorageDocuments storageDocuments) {
		this.storageDocuments = storageDocuments;
	}

	/**
	 * 
	 * @return Le document
	 */
	public StorageDocument getStorageDocument() {
		return storageDocument;
	}

	/**
	 * 
	 * @param storageDocument
	 *            : Le documen
	 */
	public void setStorageDocument(final StorageDocument storageDocument) {
		this.storageDocument = storageDocument;
	}

	/**
	 * Test si la recherche lucene ne retourne pas une liste null
	 */
	@Test
	public void searchByLuceneResultNotNull() {
		Assert.assertNotNull(storageDocuments);
	}

	/**
	 * Test si la recherche lucene retourne pas une liste de 3 documents
	 */
	@Test
	public void searchByLuceneResultSize() {
		Assert.assertEquals(storageDocuments.getListOfStorageDocument().size(),
				CompareTo.THIS_LIST_OF_STORAGEDOCUMENT_SIZE);
	}

	/**
	 * Test si la recherche par uuid ne retourne pas une liste null
	 */
	@Test
	public void searchByUUIDResultNotNull() {
		Assert.assertNotNull(storageDocument);
	}

	/**
	 * Test si la recherche par uuid retourne les valeurs attendues
	 */
	@Test
	public void searchByUUIDResultContent() {
		Assert.assertTrue(BeanHelper.checkMetaDatasValues(
				storageDocument.getMetadatas(), CompareTo.METADATA_CODE,
				CompareTo.THIS_DATA_3));
	}

}
