package fr.urssaf.image.sae.storage.bouchon.services.providers;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Classe abstraite qui contient les éléments commun à tous les services
 * proposés par cette façade
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-sae-storage-bouchon.xml"})
public abstract class AbstractServiceProviderTest extends
		AbstractServiceParameters {
	@Autowired
	@SuppressWarnings("PMD.LongVariable")
	private StorageServiceProvider storageServiceProvider;

	/**
	 * @param storageServiceProvider : La façade de services
	 *           
	 */
	@SuppressWarnings("PMD.LongVariable")
	public void setStorageServiceProvider(
			final StorageServiceProvider storageServiceProvider) {
		this.storageServiceProvider = storageServiceProvider;
	}

	/**
	 * @return La façade de services
	 */
	public StorageServiceProvider getStorageServiceProvider() {
		return storageServiceProvider;
	}

}
