package fr.urssaf.image.sae.mapping.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError;
import fr.urssaf.image.sae.mapping.messages.MappingMessageHandler;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;

/**
 * Fournit des méthodes de validation des arguments des services de l'interface
 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService
 * MappingOnErrorDocumentService}
 * 
 * @author akenore
 * 
 */
@Aspect
public class MappingOnErrorDocumentServiceValidation {
	/**
	 * 
	 * Valide l'argument de la méthode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService#saeDocumentOnErrorToUntypedDocumentOnError(fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError)
	 * saeDocumentOnErrorToUntypedDocumentOnError}.<br>
	 * 
	 * @param saeDocOnError
	 *            : Le document métier en erreur.
	 */
	@Before(value = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError  fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService.saeDocumentOnErrorToUntypedDocumentOnError(..)) && args(saeDocOnError)")
	public final void saeDocumentOnErrorToUntypedDocumentOnErrorValidation(
			final SAEDocumentOnError saeDocOnError) {
		Validate.notNull(saeDocOnError,
				MappingMessageHandler.getMessage("mapping.document.required",
						SAEDocumentOnError.class.getName()));
		Validate.notNull(saeDocOnError.getErrors(), MappingMessageHandler
				.getMessage("mapping.document.errors.required",
						SAEDocumentOnError.class.getName()));
		Validate.notNull(saeDocOnError.getMetadatas(), MappingMessageHandler
				.getMessage("mapping.document.metadata.required",
						SAEDocumentOnError.class.getName()));
		Validate.notNull(saeDocOnError.getContent(), MappingMessageHandler
				.getMessage("mapping.document.content.required",
						SAEDocumentOnError.class.getName()));

	}

	/**
	 * 
	 * Valide l'argument de la méthode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService#storageDocumentOnErrorToSaeDocumentOnError(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError)
	 * storageDocumentOnErrorToSaeDocumentOnError}.<br>
	 * 
	 * @param storageOnError
	 *            : Le document de stockage en erreur.
	 */
	@Before(value = "execution(fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError  fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService.storageDocumentOnErrorToSaeDocumentOnError(..)) && args(storageOnError)")
	public final void storageDocumentOnErrorToSaeDocumentOnErrorErrorValidation(
			final StorageDocumentOnError storageOnError) {
		validateStorageDocumentOnError(storageOnError);

	}

	/**
	 * 
	 * Valide l'argument de la méthode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService#storageDocumentOnErrorToUntypedDocumentOnError(StorageDocumentOnError)
	 * storageDocumentOnErrorToSaeDocumentOnError}.<br>
	 * 
	 * @param storageOnError
	 *            : Le document de stockage en erreur.
	 */
	@Before(value = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError  fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService.storageDocumentOnErrorToUntypedDocumentOnError(..)) && args(storageOnError)")
	public final void storageDocumentOnErrorToUntypedDocumentOnErrorValidation(
			final StorageDocumentOnError storageOnError) {
		validateStorageDocumentOnError(storageOnError);

	}

	/**
	 * Permet de valider le paramètre d'entrée
	 * 
	 * @param storageOnError
	 *            : Le document de stockage en erreur.
	 */
	private void validateStorageDocumentOnError(
			final StorageDocumentOnError storageOnError) {
		Validate.notNull(storageOnError, MappingMessageHandler.getMessage(
				"mapping.document.required",
				StorageDocumentOnError.class.getName()));
		Validate.notNull(storageOnError.getCodeError(), MappingMessageHandler
				.getMessage("mapping.document.errors.required",
						SAEDocumentOnError.class.getName()));
		Validate.notNull(storageOnError.getMessageError(),
				MappingMessageHandler.getMessage(
						"mapping.document.message.error.required",
						SAEDocumentOnError.class.getName()));
		Validate.notNull(storageOnError.getMetadatas(), MappingMessageHandler
				.getMessage("mapping.document.metadata.required",
						SAEDocumentOnError.class.getName()));
		Validate.notNull(storageOnError.getContent(), MappingMessageHandler
				.getMessage("mapping.document.content.required",
						SAEDocumentOnError.class.getName()));
	}
}
