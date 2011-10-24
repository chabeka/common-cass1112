package fr.urssaf.image.sae.storage.bouchon.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * 
 * @author akenore
 * 
 */
@Aspect
public class SearchingServiceValidation {
	/**
	 * Valide l'argument de la méthode searchStorageDocumentByLuceneCriteria.
	 * 
	 * 
	 * @param luceneCriteria
	 *            : Le critère lucene de recherche
	 */
	@Before(value = "execution( fr.urssaf.image.sae.storage.model..StorageDocuments  fr.urssaf.image.sae.storage.bouchon..SearchingServiceImpl.searchStorageDocumentByLuceneCriteria(..)) && args(luceneCriteria)")
	public final void searchStorageDocumentByLuceneCriteriaValidation(
		final	LuceneCriteria luceneCriteria) {
		Validate.notNull(luceneCriteria,
				Message.SEARCHING_BY_LUCENE_CRITERIA_NOT_NULL.getMessage());
		if (luceneCriteria != null) {
			Validate.notNull(luceneCriteria.getLuceneQuery(),
					Message.SEARCHING_BY_LUCENE_CRITERIA_NOT_NULL.getMessage());
		}
	}

	/**
	 * Valide l'argument de la méthode searchStorageDocumentByUUIDCriteria
	 * 
	 * @param uUIDCriteria
	 *            : Le critère uuid
	 * 
	 */
	@Before(value = "execution( fr.urssaf.image.sae.storage.model..StorageDocument  fr.urssaf.image.sae.storage.bouchon..SearchingServiceImpl.searchStorageDocumentByUUIDCriteria(..)) && args(uUIDCriteria)")
	public final void searchStorageDocumentByUUIDCriteriaValidation(
		final 	UUIDCriteria uUIDCriteria) {
		Validate.notNull(uUIDCriteria,
				Message.SEARCHING_BY_UUID_NOT_NULL.getMessage());
		if (uUIDCriteria != null) {
			Validate.notNull(uUIDCriteria.getUuid(),
					Message.SEARCHING_BY_UUID_NOT_NULL.getMessage());
		}
	}
}
