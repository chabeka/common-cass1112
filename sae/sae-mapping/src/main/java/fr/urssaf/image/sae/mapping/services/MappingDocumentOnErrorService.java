package fr.urssaf.image.sae.mapping.services;

import fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;

/**
 * Interface qui fournit des services de conversion entre objet du modèle et
 * objet technique de stockage.
 * 
 * @author akenore
 * 
 */

public interface MappingDocumentOnErrorService {

	/**
	 * Service de conversion d’un objet de type {@link SAEDocumentOnError} vers
	 * un objet de type {@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError}.
	 * 
	 * @param saeDocOnError
	 *            : Le document métier.
	 * @return un objet de type {@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError}
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion ne se passe pas bien.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	UntypedDocumentOnError saeDocumentOnErrorToUntypedDocumentOnError(
			final SAEDocumentOnError saeDocOnError)
			throws InvalidSAETypeException, MappingFromReferentialException;

	/**
	 * Service de conversion d’un objet de type {@link fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError}
	 * vers un objet de type {@link fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError} .
	 * 
	 * @param storageDocOnError
	 *            : Le document technique en erreur
	 * @return Un objet de type {@link fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError}.
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	SAEDocumentOnError storageDocumentOnErrorToSaeDocumentOnError(
			final StorageDocumentOnError storageDocOnError)
			throws InvalidSAETypeException, MappingFromReferentialException;

	/**
	 * Service de conversion d’un objet de type {@link fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError}
	 * vers un objet de type {{@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError}.
	 * 
	 * @param storageDocOnError
	 *            : Le document métier.
	 * @return un objet de type {@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError}
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion ne se passe pas bien.
	 */
	UntypedDocumentOnError storageDocumentOnErrorToUntypedDocumentOnError(
			final StorageDocumentOnError storageDocOnError)
			throws InvalidSAETypeException;
}
