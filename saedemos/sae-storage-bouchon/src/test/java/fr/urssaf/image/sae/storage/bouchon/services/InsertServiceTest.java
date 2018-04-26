package fr.urssaf.image.sae.storage.bouchon.services;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.bouchon.services.providers.AbstractStorageServices;
import fr.urssaf.image.sae.storage.bouchon.services.providers.CompareTo;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

/**
 * Classe de tests unitaires des services d'insertion
 * 
 * @author akenore
 * 
 */

public class InsertServiceTest extends AbstractStorageServices {
	/**
	 * Test si le service d'insertion ne retourne pas un uuid null
	 * 
	 */
	@Test
	public void insertionDataNotNull() throws InsertionServiceEx {
	final	UUID uuid = getInsertionService().insertStorageDocument(getStorageDocument());
		Assert.assertNotNull(uuid);
	}

	/**
	 * Test si le service d'insertion retourne bien l'uuid attendu
	 * 
	 * 
	 */
	@Test
	public void getDesiredUUID() throws InsertionServiceEx {
		final	UUID uuid = getInsertionService().insertStorageDocument(getStorageDocument());
		Assert.assertEquals(uuid, UUID.fromString(CompareTo.THIS_UUID));
	}

	/**
	 * Test si le service d'insertion en masse retourne bien des uuids non null
	 * 
	 */
	@Test
	public void bulkInsertionDataNotNull() {
		Assert.assertNotNull(getInsertionService().bulkInsertStorageDocument(
				getStorageDocuments(), true).getUuids());
	}

	/**
	 * Test si le service d'insertion en masse retourne bien des uuids attendus
	 * 
	 * 
	 */
	@Test
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void getDesiredUUIDsFromBulkInsertion() {
		boolean find = true;
		for (String uuid : CompareTo.THESE_UUIDS) {
			find = find
					&& getInsertionService()
							.bulkInsertStorageDocument(getStorageDocuments(), true)
							.getUuids().contains(UUID.fromString(uuid));
		}
		Assert.assertTrue(find);
	}

}
