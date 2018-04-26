package fr.urssaf.image.sae.storage.bouchon.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
/**
 * Fournit des méthodes pour valider les services de récupération
 * @author akenore
 *
 */
@Aspect
public class RetrievalServiceValidation {

	/**
	 * Valide l'argument de la méthode retrieveStorageDocumentByUUID.
	 * 
	 * 
	 * @param uUIDCriteria
	 *            : Le critère UUID
	 */
	@Before(value = "execution( fr.urssaf.image.sae.storage.model..StorageDocument  fr.urssaf.image.sae.storage.bouchon..RetrievalServiceImpl.retrieveStorageDocumentByUUID(..)) && args(uUIDCriteria)")
	public final void retrieveStorageDocumentByUUIDValidation(
		final	UUIDCriteria uUIDCriteria) {
		checkNotNull(uUIDCriteria);
	}

	/**
	 * Valide l'argument de la méthode retrieveStorageDocumentContentByUUID.
	 * 
	 * @param uUIDCriteria
	 *            : Le critère UUID
	 */
	@Before(value = "execution( byte[] fr.urssaf.image.sae.storage.bouchon..RetrievalServiceImpl.retrieveStorageDocumentContentByUUID(..)) && args(uUIDCriteria)")
	public final void retrieveStorageDocumentContentByUUIDValidation(
			final UUIDCriteria uUIDCriteria) {
		checkNotNull(uUIDCriteria);
	}

	/**
	 * Valide l'argument de la méthode retrieveStorageDocumentContentByUUID.
	 * 
	 * @param uUIDCriteria
	 *            : Le critère UUID
	 */
	@Before(value = "execution(java.util.List<fr.urssaf.image.sae.storage.model..StorageMetadata> fr.urssaf.image.sae.storage.bouchon..RetrievalServiceImpl.retrieveStorageDocumentMetaDatasByUUID(..)) && args(uUIDCriteria)")
	public final void retrieveStorageDocumentMetaDatasByUUIDValidation(
		final	UUIDCriteria uUIDCriteria) {
		checkNotNull(uUIDCriteria);
	}

	/**
	 * 
	 * @param uUIDCriteria
	 *            : Le critère UUID
	 */
	private void checkNotNull(final UUIDCriteria uUIDCriteria) {
		Validate.notNull(uUIDCriteria,
				Message.RETRIEVE_BY_UUID_NOT_NULL.getMessage());
		if (uUIDCriteria != null) {
			Validate.notNull(uUIDCriteria.getUuid(),
					Message.RETRIEVE_BY_UUID_NOT_NULL.getMessage());
		}

	}
}
