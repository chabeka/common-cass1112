package fr.urssaf.image.sae.mapping.validation;

import org.junit.Test;

import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;

/**
 * 
 * @author akenore
 *
 */
public class MappingOnErrorDocumentServiceValidationTest extends AbstractDocumentOnErrorService{
	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService#saeDocumentOnErrorToUntypedDocumentOnError(fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError)
	 * saeDocumentOnErrorToUntypedDocumentOnError}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saeDocumentOnErrorToUntypedDocumentOnError() throws InvalidSAETypeException, MappingFromReferentialException {
		getDocOnErrorService().saeDocumentOnErrorToUntypedDocumentOnError(null);
	}

	
	
	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService# storageDocumentOnErrorToSaeDocumentOnError(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError)
	 * storageDocumentOnErrorToSaeDocumentOnError}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void storageDocumentOnErrorToSaeDocumentOnError() throws InvalidSAETypeException, MappingFromReferentialException {
		getDocOnErrorService().storageDocumentOnErrorToSaeDocumentOnError(null);
	}
	
	
	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService#storageDocumentOnErrorToUntypedDocumentOnError(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError)
	 * storageDocumentOnErrorToUntypedDocumentOnError}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void storageDocumentOnErrorToUntypedDocumentOnError() throws InvalidSAETypeException, MappingFromReferentialException {
		getDocOnErrorService().storageDocumentOnErrorToUntypedDocumentOnError(null);
	}
	
	
}
