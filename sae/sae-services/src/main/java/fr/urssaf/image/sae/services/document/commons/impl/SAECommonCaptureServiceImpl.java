package fr.urssaf.image.sae.services.document.commons.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.dispatchers.SAEServiceDispatcher;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Classe concrète pour le service commun pour les services de Capture unitaire
 * et Capture en masse.
 * 
 * @author rhofir.
 */
@Service
@Qualifier("saeCommonCaptureService")
public class SAECommonCaptureServiceImpl implements SAECommonCaptureService {

	@Autowired
	@Qualifier("saeControlesCaptureService")
	private SAEControlesCaptureService cntrolesService;
	@Autowired
	@Qualifier("saeServiceDispatcher")
	private SAEServiceDispatcher serviceDispatcher;
	@Autowired
	@Qualifier("mappingDocumentService")
	private MappingDocumentService mappingService;
	@Autowired
	@Qualifier("saeEnrichmentMetadataService")
	private SAEEnrichmentMetadataService enrichmentService;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	@Override
	public final StorageDocument buildStorageDocumentForCapture(
			UntypedDocument untypedDocument) throws RequiredStorageMetadataEx,
			InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
			DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
			RequiredArchivableMetadataEx, SAEEnrichmentEx, UnknownHashCodeEx,
			ReferentialRndException, UnknownCodeRndEx, SAECaptureServiceEx {
		SAEDocument saeDocument = null;
		StorageDocument storageDocument = null;
		try {
			cntrolesService.checkUntypedDocument(untypedDocument);
			cntrolesService.checkUntypedMetadata(untypedDocument);
			saeDocument = mappingService
					.untypedDocumentToSaeDocument(untypedDocument);
			if (saeDocument != null) {
				cntrolesService.checkSaeMetadataForCapture(saeDocument);
				cntrolesService.checkHashCodeMetadataForStorage(saeDocument);
				enrichmentService.enrichmentMetadata(saeDocument);
				cntrolesService.checkSaeMetadataForStorage(saeDocument);
				storageDocument = mappingService
						.saeDocumentToStorageDocument(saeDocument);
			}
		} catch (InvalidSAETypeException e) {
			serviceDispatcher.dispatch(new SAECaptureServiceEx(e));
		} catch (MappingFromReferentialException e) {
			serviceDispatcher.dispatch(new SAECaptureServiceEx(e));
		}
		return storageDocument;

	}

	/**
	 * @param serviceDispatcher
	 *            : Le dispatcher
	 */
	public final void setServiceDispatcher(
			final SAEServiceDispatcher serviceDispatcher) {
		this.serviceDispatcher = serviceDispatcher;
	}

	/**
	 * @return Le dispatcher.
	 */
	public final SAEServiceDispatcher getSaeServiceDispatcher() {
		return serviceDispatcher;
	}

}
