package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe de test du service
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DeletionServiceImpl
 * DeletionService}
 * 
 * @author rhofir, kenore.
 */
public class DeletionServiceTest extends StorageServices {
	/**
	 * Test du service :
	 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DeletionServiceImpl#deleteStorageDocument(fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria)
	 * deleteStorageDocument}
	 * 
	 * @throws ConnectionServiceEx
	 *             ConnectionServiceEx Exception lévée lorsque la connexion
	 *             n'aboutie pas.
	 * @throws RetrievalServiceEx 
	 */
	@Test
	public void deleteStorageDocument() throws InsertionServiceEx, IOException,
			ParseException,  RetrievalServiceEx {
		
		// Initialisation des jeux de données UUID
		final StorageDocument storageDoc = getMockData(getInsertionService());
		final UUIDCriteria uuidCriteria = new UUIDCriteria(
				storageDoc.getUuid(), new ArrayList<StorageMetadata>());
		try {
			getDeletionService().deleteStorageDocument(uuidCriteria);
		} catch (DeletionServiceEx e) {
			Assert.assertTrue("La suppression a échoué " + e.getMessage(), true);
		}
		Assert.assertNull(getRetrievalService().retrieveStorageDocumentByUUID(uuidCriteria));
	}

	/**
	 * Test du service :
	 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DeletionServiceImpl#rollBack(java.lang.String)
	 * rollBack}.
	 * 
	 * @throws ConnectionServiceEx
	 */
	@Test
	@Ignore("034c37dd-7b45-4b11-be63-fe639d90df68 car ce document est gele A revoir")
	public void rollBack() throws InsertionServiceEx, IOException,
			ParseException, DeletionServiceEx {
		getMockData(getInsertionService());
		try {
			// ID process.
			getDeletionService().rollBack(Constants.ID_PROCESS_TEST);

		} catch (DeletionServiceEx e) {
			Assert.fail("La suppression a échoué " + e.getMessage());
		} catch (Exception e) {
			Assert.fail("Aucune recherche ne correspond à l'indentifiant passé en paramétre. "
					+ e.getMessage());
		}
		Assert.assertTrue("La suppression a réussi : ", true);
	}

}
