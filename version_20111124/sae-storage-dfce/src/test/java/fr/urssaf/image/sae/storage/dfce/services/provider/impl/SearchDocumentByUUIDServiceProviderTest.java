package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.provider.CommonsServicesProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
/**
 * Classe permettant de tester la recherche d'un document dans la
 * base.
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class SearchDocumentByUUIDServiceProviderTest extends
		CommonsServicesProvider {

	// Ici on test la recherche d'un  document
	@Test
	public final void searchDocument() throws ConnectionServiceEx,
			SearchingServiceEx, InsertionServiceEx {
		// On récupère la connexion
		getServiceProvider().openConnexion();
		// on insert le document.
		StorageDocument document= getServiceProvider().getStorageDocumentService()
				.insertStorageDocument(getStorageDocument());
		// on test ici si on a un UUID
		Assert.assertNotNull(document.getUuid());
		StorageDocument storageDocument = getServiceProvider()
				.getStorageDocumentService()
				.searchStorageDocumentByUUIDCriteria(
						new UUIDCriteria(document.getUuid(), null));
		// ici on vérifie qu'on a bien un contenu
		Assert.assertNotNull(storageDocument.getContent());
		// ici on vérifie qu'on a bien des métadonnées
		Assert.assertTrue(storageDocument.getMetadatas().size() > 3);
	}

}
