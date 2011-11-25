package fr.urssaf.image.sae.storage.bouchon.services.providers;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

/**
 * Classe de tests unitaires des services d'insertion
 * 
 * @author akenore
 * 
 */

public class InsertServiceProviderTest extends
		AbstractServiceProviderTest {

	@Test
	public void insertionDataNotNull() throws InsertionServiceEx {
	final	UUID uuid = getStorageServiceProvider()
				.insertStorageDocument(getStorageDocument());
		Assert.assertNotNull(uuid);
	}

	@Test
	public void getDesiredUUID() throws InsertionServiceEx {
		final 	UUID uuid = getStorageServiceProvider()
				.insertStorageDocument(getStorageDocument());
		Assert.assertEquals(uuid, UUID.fromString(CompareTo.THIS_UUID));
	}

	@Test
	public void bulkInsertionDataNotNull() {
		Assert.assertNotNull(getStorageServiceProvider().bulkInsertStorageDocument(
				getStorageDocuments(), true).getUuids());
	}

	@Test
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void getDesiredUUIDsFromBulkInsertion() {
		boolean find = true;
		for (String uuid : CompareTo.THESE_UUIDS) {
			find=find && getStorageServiceProvider()
					.bulkInsertStorageDocument(getStorageDocuments(), true)
					.getUuids().contains(UUID.fromString(uuid));
		}
		Assert.assertTrue(find);
	}
	
	

}
