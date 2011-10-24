package fr.urssaf.image.sae.storage.bouchon.data;

import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.storage.bouchon.data.provider.InsertionDataProvider;
import fr.urssaf.image.sae.storage.bouchon.services.providers.CompareTo;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;

/**
 * Tests Unitaires du fournisseur de données des services d'insertion <Li>
 * Attribut uuid : L'identifiant unique</Li> <Li>Attribut bulkInsertionResults :
 * résultat de l'insertion en masse</Li>
 * 
 * @author akenore
 * 
 */
public class InsertionDataProviderTest {
	private UUID uuid;
	@SuppressWarnings("PMD.LongVariable")
	private BulkInsertionResults bulkInsertionResults;

	/**
	 * @return L'identifiant unique
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuid : L'identifiant unique
	 */
	public void setUuid(final UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return Le résultat de l'insertion en masse
	 */
	public BulkInsertionResults getBulkInsertionResults() {
		return bulkInsertionResults;
	}

	/**
	 * @param bulkInsertionResults : Le résultat de l'insertion en masse
	 */
	@SuppressWarnings("PMD.LongVariable")
	public void setBulkInsertionResults(final BulkInsertionResults bulkInsertionResults) {
		this.bulkInsertionResults = bulkInsertionResults;
	}

	/**
	 * Initialise les données de tests
	 */
	@Before
	public void initialize() {
		uuid = InsertionDataProvider.getInsertionData();
		bulkInsertionResults = InsertionDataProvider.getBulkInsertionData();
	}

	/**
	 * Test si l'uuid n'est pas null
	 * 
	 */
	@Test
	public void insertionDataNotNull() {
		Assert.assertNotNull(uuid);
	}

	/**
	 * Test si l'uuid correspond bien à celui attendu
	 * 
	 * 
	 */
	@Test
	public void getDesiredUUID() {
		Assert.assertEquals(UUID.fromString(CompareTo.THIS_UUID), uuid);
	}

	/**
	 * Test si les uuids retournés dans la cadre de l'insertion en masse ne sont
	 * pas null
	 * 
	 */
	@Test
	public void bulkInsertionDataNotNull() {
		Assert.assertNotNull(bulkInsertionResults);
	}

	/**
	 * Test si le nombre des uuids retournés dans la cadre de l'insertion en
	 * masse est égal à 3
	 * 
	 */
	@Test
	public void getDesiredUUIDsSizeFromBulkInsertion() {
		Assert.assertEquals(CompareTo.THIS_LIST_OF_STORAGEDOCUMENT_SIZE,
				bulkInsertionResults.getStorageDocuments()
						.getListOfStorageDocument().size());
	}

	/**
	 * Test si les valeurs des uuids retournés dans la cadre de l'insertion en
	 * masse correspondent bien à ceux attendus.
	 * 
	 */
	@Test
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void getDesiredUUIDsFromBulkInsertion() {
		boolean find = true;
		final List<UUID> uuids = bulkInsertionResults.getUuids();
		for (String uuid : CompareTo.THESE_UUIDS) {
			find = find && uuids.contains(UUID.fromString(uuid));
		}
		Assert.assertTrue(find);
	}

}
