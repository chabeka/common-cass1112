package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe permettant de test la récupération du contenue d'un document en base.
 */
public class RetrieveContentByUUIDServiceProviderTest extends
		CommonsServicesProvider {

	// Ici on test la recupération du contenu du document
	@Test
	public final void retrieveContent() throws ConnectionServiceEx,
			RetrievalServiceEx, InsertionServiceEx {
		
		// On récupère la connexion
		getServiceProvider().openConnexion();
		// on insert le document.
		StorageDocument document  = getServiceProvider().getStorageDocumentService()
				.insertStorageDocument(getStorageDocument());
		// on test ici si on a un UUID
		Assert.assertNotNull(document.getUuid());
		byte[] content = getServiceProvider().getStorageDocumentService()
				.retrieveStorageDocumentContentByUUID(
						new UUIDCriteria(document.getUuid(), null));
		// ici on vérifie qu'on a bien un contenu
		Assert.assertNotNull(content);
	}
}
