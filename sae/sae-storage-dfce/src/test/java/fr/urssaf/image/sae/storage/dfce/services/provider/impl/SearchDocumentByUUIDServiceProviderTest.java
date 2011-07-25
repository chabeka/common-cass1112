package fr.urssaf.image.sae.storage.dfce.services.provider.impl;

import java.util.UUID;

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
		// initialise les paramètres de connexion
		getServiceProvider().setStorageServiceProviderParameter(
				getStorageConnectionParameter());
		// On récupère la connexion
		getServiceProvider().getStorageConnectionService().openConnection();
		// on insert le document.
		UUID uuid = getServiceProvider().getStorageDocumentService()
				.insertStorageDocument(getStorageDocument());
		// on test ici si on a un UUID
		Assert.assertNotNull(uuid);
		StorageDocument storageDocument = getServiceProvider()
				.getStorageDocumentService()
				.searchStorageDocumentByUUIDCriteria(
						new UUIDCriteria(uuid, null));
		// ici on vérifie qu'on a bien un contenu
		Assert.assertNotNull(storageDocument.getContent());
		// ici on vérifie qu'on a bien des métadonnées
		Assert.assertTrue(storageDocument.getMetadatas().size() > 3);
		// on ferme la connection
		getServiceProvider().getStorageConnectionService().closeConnexion();
	}

}
