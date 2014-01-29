package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe permettant de test la récupération des métadonnées d'un document en
 * base.
 */
public class RetrieveMetadatasByUUIDServiceProviderTest extends
		CommonsServicesProvider {

	// Ici on test la récupération du document
	@Test
	public final void retrieveMetadatas() throws ConnectionServiceEx,
			RetrievalServiceEx, InsertionServiceEx {
		// On récupère la connexion
		getServiceProvider().openConnexion();
		// on insert le document.
		StorageDocument document= getServiceProvider().getStorageDocumentService()
				.insertStorageDocument(getStorageDocument());
		// on test ici si on a un UUID
		Assert.assertNotNull(document.getUuid());
		List<StorageMetadata> metadatas = getServiceProvider()
				.getStorageDocumentService()
				.retrieveStorageDocumentMetaDatasByUUID(
						new UUIDCriteria(document.getUuid(), null));
		// ici on vérifie qu'on a bien des métadonnées
		Assert.assertTrue(metadatas.size() > 3);
	}
}
