package fr.urssaf.image.sae.storage.bouchon.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * 
 * @author akenore
 * 
 */
@Aspect
public class InsertionServiceValidation {
	/**
	 * Valide l'argument de la méthode insertStorageDocument.
	 * 
	 * 
	 * @param storageDocument
	 *            : Le document à insérer
	 */
	@Before(value = "execution( java.util.UUID  fr.urssaf.image.sae.storage.bouchon..InsertionServiceImpl.insertStorageDocument(..)) && args(storageDocument)")
	public final void insertStorageDocumentValidation(
			final StorageDocument storageDocument) {
		// ici on valide que le document n'est pas null
		Validate.notNull(storageDocument,
				Message.INSERTION_STORAGEDOCUMENT_NOT_NULL.getMessage());
		// ici on vérifie que tous les composants du document ne sont pas null.

		if (storageDocument != null
				&& storageDocument.getMetadatas() == null
				&& (storageDocument.getContent() == null && storageDocument
						.getFilePath() == null)) {
			Validate.notNull(storageDocument.getMetadatas(),
					Message.INSERTION_STORAGEDOCUMENT_COMPONENTS_NOT_NULL
							.getMessage());
		}

	}

	/**
	 * Valide l'argument de la méthode bulkInsertStorageDocument
	 * 
	 * @param storageDocuments
	 *            : La liste des documents
	 * @param allOrNothing
	 *            : Paramètre qui indique si l'insertion sera réalisée en mode
	 *            "Tout ou rien".
	 */
	@Before(value = "execution( fr.urssaf.image..BulkInsertionResults  fr.urssaf.image.sae.storage.bouchon..InsertionServiceImpl.bulkInsertStorageDocument(..)) && args(storageDocuments,allOrNothing)")
	public final void bulkInsertStorageDocumentValidation(
			final StorageDocuments storageDocuments, final boolean allOrNothing) {
		// ici on valide que le document n'est pas null
		Validate.notNull(storageDocuments,
				Message.INSERTION_STORAGEDOCUMENT_NOT_NULL.getMessage());
		// ici on vérifie que tous les composants du document ne sont pas null.
		if (storageDocuments != null) {
			Validate.notNull(storageDocuments.getListOfStorageDocument(),
					Message.BULK_INSERTION_STORAGEDOCUMENTS_NOT_NULL
							.getMessage());
		}
	}
}
