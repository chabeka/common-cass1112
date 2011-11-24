package fr.urssaf.image.sae.mapping.validation;

import org.junit.Test;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Cette classe permet de tester la validation des paramètres des services de
 * l'interface {@link MappingDocumentService}
 * 
 * @author akenore
 * 
 */

public class MappingDocumentServiceValidationTest extends AbstractDocumentService{
	

	
	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#saeDocumentToStorageDocument(fr.urssaf.image.sae.bo.model.bo.SAEDocument)
	 * saeDocumentToStorageDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             xception levée lorsque la conversion n’aboutit pas.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saeDocumentToStorageDocument() throws InvalidSAETypeException {
		getMappingDocService().saeDocumentToStorageDocument(null);
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#saeDocumentToStorageDocument(fr.urssaf.image.sae.bo.model.bo.SAEDocument)
	 * saeDocumentToStorageDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saeDocumentToStorageDocumentMetadata()
			throws InvalidSAETypeException {
		getMappingDocService().saeDocumentToStorageDocument(new SAEDocument());
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#saeDocumentToUntypedDocument(SAEDocument)
	 * saeDocumentToUntypedDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saeDocumentToUntypedDocumentMetadata()
			throws InvalidSAETypeException, MappingFromReferentialException {
		getMappingDocService().saeDocumentToUntypedDocument(new SAEDocument());
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#saeDocumentToUntypedDocument(SAEDocument)
	 * saeDocumentToUntypedDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saeDocumentToUntypedDocument() throws InvalidSAETypeException,
			MappingFromReferentialException {
		getMappingDocService().saeDocumentToUntypedDocument(null);
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#storageDocumentToSaeDocument(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
	 * storageDocumentToSaeDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void storageDocumentToSaeDocument() throws InvalidSAETypeException,
			MappingFromReferentialException {
		getMappingDocService().storageDocumentToSaeDocument(null);
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#storageDocumentToSaeDocument(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
	 * storageDocumentToSaeDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void storageDocumentToSaeDocumentMetadata()
			throws InvalidSAETypeException, MappingFromReferentialException {
		final StorageDocument storageDoc = new StorageDocument();
		storageDoc.setMetadatas(null);
		getMappingDocService().storageDocumentToSaeDocument(storageDoc);
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#storageDocumentToUntypedDocument(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
	 * storageDocumentToUntypedDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void storageDocumentToUntypedDocument()
			throws InvalidSAETypeException, MappingFromReferentialException {
		getMappingDocService().storageDocumentToUntypedDocument(null);
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#storageDocumentToUntypedDocument(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
	 * storageDocumentToUntypedDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void storageDocumentToUntypedDocumentMetadata()
			throws InvalidSAETypeException, MappingFromReferentialException {
		final StorageDocument storageDoc = new StorageDocument();
		storageDoc.setMetadatas(null);
		getMappingDocService().storageDocumentToUntypedDocument(storageDoc);
	}
	
	
	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#untypedDocumentToSaeDocument(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument)
	 * untypedDocumentToSaeDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 * @throws UnKnowSAETypeException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void untypedDocumentToSaeDocument()
			throws InvalidSAETypeException, MappingFromReferentialException {
		getMappingDocService().untypedDocumentToSaeDocument(null);
	}
	
	
	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#untypedDocumentToSaeDocument(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument)
	 * untypedDocumentToSaeDocument}
	 * 
	 * @throws InvalidSAETypeException
	 *             Exception levée lorsque la conversion n’aboutit pas.
	 * @throws MappingFromReferentialException
	 *             Exception levée lorsque la récupération de la métadata du
	 *             référentiel n'abouti pas
	 * @throws UnKnowSAETypeException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void untypedDocumentToSaeDocumentMetadata()
			throws InvalidSAETypeException, MappingFromReferentialException {
		getMappingDocService().untypedDocumentToSaeDocument(new UntypedDocument());
	}
	
}
