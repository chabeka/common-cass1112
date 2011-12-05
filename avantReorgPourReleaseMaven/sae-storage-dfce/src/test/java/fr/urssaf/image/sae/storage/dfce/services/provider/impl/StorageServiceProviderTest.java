package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

/**
 * Classe permettant de tester la validation par aspect du paramètre du service
 * d'insertion.
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class StorageServiceProviderTest extends CommonsServicesProvider {
	/**
	 * Test de validation par aspect du paramètre du service d'insertion.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void storageServiceProvider() throws ConnectionServiceEx,
			InsertionServiceEx {
		getServiceProvider().openConnexion();
		getServiceProvider().getStorageDocumentService().insertStorageDocument(
				null);
	}

}
