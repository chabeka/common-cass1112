package fr.urssaf.image.sae.mapping.services.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.SAEError;
import fr.urssaf.image.sae.bo.model.bo.SAEDocumentOnError;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Classe qui fournit des services de conversion entre objet du modèle et objet
 * technique de stockage.
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("mappingDocumentOnErrorService")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class MappingDocumentOnErrorServiceImpl extends
		AbstractMappingDocumentService implements MappingDocumentOnErrorService {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public UntypedDocumentOnError saeDocumentOnErrorToUntypedDocumentOnError(
			final SAEDocumentOnError saeDocOnError)
			throws InvalidSAETypeException, MappingFromReferentialException {
		final List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
		for (SAEMetadata metadata : Utils.nullSafeIterable(saeDocOnError
				.getMetadatas())) {
			try {
				final MetadataReference reference = getReferenceDAO()
						.getByLongCode(metadata.getLongCode());
				metadatas.add(new UntypedMetadata(metadata.getLongCode(), Utils
						.convertToString(metadata.getValue(), reference)));
			} catch (ParseException parseExcept) {
				throw new InvalidSAETypeException(parseExcept);
			} catch (ReferentialException refExcpt) {
				throw new MappingFromReferentialException(refExcpt);
			}
		}
		final List<MetadataError> errors = new ArrayList<MetadataError>();
		for (SAEError saeError : Utils.nullSafeIterable(saeDocOnError
				.getErrors())) {
			MetadataError error = new MetadataError();
			error.setCode(saeError.getCode());
			error.setMessage(saeError.getMessage());
			errors.add(error);
		}
		return new UntypedDocumentOnError(saeDocOnError.getContent(),
				metadatas, errors);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@SuppressWarnings({ "PMD.AvoidInstantiatingObjectsInLoops",
			"PMD.DataflowAnomalyAnalysis" })
	public SAEDocumentOnError storageDocumentOnErrorToSaeDocumentOnError(
			final StorageDocumentOnError storageDocOnError)
			throws InvalidSAETypeException, MappingFromReferentialException {
		final SAEDocumentOnError saeDocOnError = new SAEDocumentOnError();
		final List<SAEMetadata> metadatas = new ArrayList<SAEMetadata>();
		final List<SAEError> errors = new ArrayList<SAEError>();
		saeDocOnError.setContent(storageDocOnError.getContent());
		errors.add(new SAEError(storageDocOnError.getCodeError(),
				storageDocOnError.getMessageError()));
		saeDocOnError.setErrors(errors);
		try {
			for (StorageMetadata sMetadata : Utils
					.nullSafeIterable(storageDocOnError.getMetadatas())) {

				final MetadataReference reference = getReferenceDAO()
						.getByShortCode(sMetadata.getShortCode());
				metadatas.add(new SAEMetadata(reference.getLongCode(),
						sMetadata.getShortCode(), sMetadata.getValue()));
			}
		} catch (ReferentialException refExcpt) {
			throw new MappingFromReferentialException(refExcpt);
		}
		saeDocOnError.setMetadatas(metadatas);
		return saeDocOnError;

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public UntypedDocumentOnError storageDocumentOnErrorToUntypedDocumentOnError(
			final StorageDocumentOnError storageDocOnError)
			throws InvalidSAETypeException {
		final List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
		for (StorageMetadata metadata : Utils
				.nullSafeIterable(storageDocOnError.getMetadatas())) {
			try {
				final MetadataReference reference = getReferenceDAO()
						.getByShortCode(metadata.getShortCode());
				metadatas.add(new UntypedMetadata(metadata.getShortCode(),
						Utils.convertToString(metadata.getValue(), reference)));
			} catch (ParseException parseExcept) {
				throw new InvalidSAETypeException(parseExcept);
			} catch (ReferentialException referentielExcept) {
				throw new InvalidSAETypeException(referentielExcept);
			}
		}
		UntypedDocumentOnError untypedDocumentOnError = new UntypedDocumentOnError(storageDocOnError.getContent(),
            metadatas, null);		
		untypedDocumentOnError.setFilePath(storageDocOnError.getFilePath());
		return untypedDocumentOnError;
	}

}
