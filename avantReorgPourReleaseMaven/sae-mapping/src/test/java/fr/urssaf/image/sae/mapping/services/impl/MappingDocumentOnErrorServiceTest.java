package fr.urssaf.image.sae.mapping.services.impl;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.bo.model.SAEError;
import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService;
import fr.urssaf.image.sae.mapping.test.constants.Constants;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;

/**
 * Classe qui permet de faire les tests sur les services
 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService
 * MappingDocumentService}
 * 
 * @author akenore
 * 
 */

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class MappingDocumentOnErrorServiceTest extends AbstractMappingService {
	@Autowired
	@Qualifier("mappingDocumentOnErrorService")
	private MappingDocumentOnErrorService docOnErrorService;

	/**
	 * Test de la méthode saeDocumentToStorageDocument
	 * 
	 * @throws FileNotFoundException
	 *             Exception lever lorqu'il y'a un dysfonctionnement.
	 * @throws InvalidSAETypeException
	 *             Exception lever lorqu'il y'a un dysfonctionnement.
	 * @throws MappingFromReferentialException
	 *             Exception lever lorqu'il y'a un dysfonctionnement.
	 * @throws ParseException
	 *             Exception lever lorqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void saeDocumentOnErrorToUntypedDocumentOnError()
			throws FileNotFoundException, InvalidSAETypeException,
			MappingFromReferentialException, ParseException {
		final UntypedDocument untyped = getUntypedDocument(Constants.MAPPING_FILE_1);
		final SAEDocument saeDoc = getMappingService()
				.untypedDocumentToSaeDocument(untyped);
		final List<SAEError> errors = new ArrayList<SAEError>();
		errors.add(new SAEError("TEST", "TEST"));
		final SAEDocumentOnError saeDocOnError = new SAEDocumentOnError();
		saeDocOnError.setContent(saeDoc.getContent());
		saeDocOnError.setErrors(errors);
		saeDocOnError.setMetadatas(saeDoc.getMetadatas());
		final UntypedDocumentOnError untypedOnError = docOnErrorService
				.saeDocumentOnErrorToUntypedDocumentOnError(saeDocOnError);
		Assert.assertNotNull(untypedOnError);
		Assert.assertNotNull(untypedOnError.getErrors());
		Assert.assertNotNull(untypedOnError.getUMetadatas());
		for (UntypedMetadata metadata : Utils.nullSafeIterable(untypedOnError
				.getUMetadatas())) {
			if (metadata.getLongCode().equals("DateArchivage")) {
				Assert.assertTrue(metadata.getValue().equals("2011-06-03"));
			}

			if (metadata.getLongCode().equals("DateCreation")) {
				Assert.assertTrue(metadata.getValue().equals("2011-06-03"));
			}
			if (metadata.getLongCode().equals("VersionNumber")) {
				Assert.assertTrue(metadata.getValue().equals("120"));
			}
			if (metadata.getLongCode().equals("CodeRND")) {
				Assert.assertTrue(metadata.getValue().equals("3.1.3.1.1"));
			}
		}

	}

	/**
	 * Test de la méthode storageDocumentOnErrorToSaeDocumentOnError
	 * 
	 * @throws FileNotFoundException
	 *             Exception lever lorqu'il y'a un dysfonctionnement.
	 * @throws InvalidSAETypeException
	 *             Exception lever lorqu'il y'a un dysfonctionnement.
	 * @throws MappingFromReferentialException
	 *             Exception lever lorqu'il y'a un dysfonctionnement.
	 * @throws ParseException
	 *             Exception lever lorqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void storageDocumentOnErrorToSaeDocumentOnError()
			throws FileNotFoundException, InvalidSAETypeException,
			MappingFromReferentialException, ParseException {
		final UntypedDocument untyped = getUntypedDocument(Constants.MAPPING_FILE_1);
		final SAEDocument saeDoc = getMappingService()
				.untypedDocumentToSaeDocument(untyped);
		final StorageDocument storage = getMappingService()
				.saeDocumentToStorageDocument(saeDoc);
		final StorageDocumentOnError storageDocOnError = new StorageDocumentOnError(
				storage.getMetadatas(), storage.getContent(),
				storage.getFilePath(), "TEST");
		storageDocOnError.setMessageError("Test");
		final SAEDocumentOnError saeDocOnError = docOnErrorService
				.storageDocumentOnErrorToSaeDocumentOnError(storageDocOnError);
		Assert.assertNotNull(saeDocOnError);
		Assert.assertNotNull(saeDocOnError.getErrors());
		Assert.assertNotNull(saeDocOnError.getMetadatas().size() == 6);
	}

	/**
	 * @param docOnErrorService
	 *            : Le service de mapping des document en erreurs
	 */
	public void setDocOnErrorService(
			final MappingDocumentOnErrorService docOnErrorService) {
		this.docOnErrorService = docOnErrorService;
	}

	/**
	 * @return Le service de mapping des document en erreurs
	 */
	public MappingDocumentOnErrorService getDocOnErrorService() {
		return docOnErrorService;
	}

}
