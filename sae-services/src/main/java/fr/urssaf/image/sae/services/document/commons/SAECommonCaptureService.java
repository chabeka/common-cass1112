package fr.urssaf.image.sae.services.document.commons;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Service commun pour l’archivage unitaire et l’archivage en masse.
 */
public interface SAECommonCaptureService {

   /**
    * Cette méthode permet de construire un StorageDocument à partir d’un
    * UntypedDocument. Cette méthode fait appel dans l’ordre à :<br>
    * <lu><br>
    * <li>checkUntypedDocument</li><br>
    * <li>checkUntypedMetadata</li><br>
    * <li>checkSaeMetadataForCapture</li><br>
    * <li>enrichmentMetadata</li><br>
    * <li>checkSaeMetadataForStorage</li><br>
    * </lu>
    * 
    * @param untypedDocument
    *           : Classe représentant un document non typé
    *           {@link StorageDocument}
    * @param captureResult
    *           résultat de la capture
    * @return Classe représentant un document archivable.
    * @throws RequiredStorageMetadataEx
    *            {@link RequiredStorageMetadataEx}.
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            {@link InvalidValueTypeAndFormatMetadataEx}.
    * @throws UnknownMetadataEx
    *            {@link UnknownMetadataEx}.
    * @throws DuplicatedMetadataEx
    *            {@link DuplicatedMetadataEx}.
    * @throws NotArchivableMetadataEx
    *            {@link NotArchivableMetadataEx}.
    * @throws NotSpecifiableMetadataEx
    *            {@link NotSpecifiableMetadataEx}.
    *@throws EmptyDocumentEx
    *            {@link EmptyDocumentEx}.
    *@throws RequiredArchivableMetadataEx
    *            {@link RequiredArchivableMetadataEx}.
    * @throws SAEEnrichmentEx
    *            {@link SAEEnrichmentEx}.
    * @throws UnknownHashCodeEx
    *            {@link UnknownHashCodeEx}.
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}.
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}.
    * @throws SAECaptureServiceEx
    *            {@link SAECaptureServiceEx}.
    * @throws MetadataValueNotInDictionaryEx
    *            {@link MetadataValueNotInDictionaryEx}.
    * @throws UnknownFormatException
    *            {@link UnknownFormatException}
    * @throws ValidationExceptionInvalidFile
    *            {@link ValidationExceptionInvalidFile}
    */
   StorageDocument buildStorageDocumentForCapture(
         UntypedDocument untypedDocument, CaptureResult captureResult)
         throws RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotArchivableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx, SAEEnrichmentEx,
         UnknownHashCodeEx, ReferentialRndException, UnknownCodeRndEx,
         NotSpecifiableMetadataEx, SAECaptureServiceEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile, UnexpectedDomainException, 
         InvalidPagmsCombinaisonException;

   /**
    * Cette méthode permet de construire un StorageDocument à partir d’un
    * UntypedDocument pour un fichier dont le contenu binaire a été transmis.
    * <lu><br>
    * <li>checkUntypedDocument</li><br>
    * <li>checkUntypedMetadata</li><br>
    * <li>checkSaeMetadataForCapture</li><br>
    * <li>enrichmentMetadata</li><br>
    * <li>checkSaeMetadataForStorage</li><br>
    * </lu>
    * 
    * @param untypedDocument
    *           : Classe représentant un document non typé
    *           {@link StorageDocument}
    * @param captureResult
    *           résultat de la capture
    * @return Classe représentant un document archivable.
    * @throws RequiredStorageMetadataEx
    *            {@link RequiredStorageMetadataEx}.
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            {@link InvalidValueTypeAndFormatMetadataEx}.
    * @throws UnknownMetadataEx
    *            {@link UnknownMetadataEx}.
    * @throws DuplicatedMetadataEx
    *            {@link DuplicatedMetadataEx}.
    * @throws NotArchivableMetadataEx
    *            {@link NotArchivableMetadataEx}.
    * @throws NotSpecifiableMetadataEx
    *            {@link NotSpecifiableMetadataEx}.
    *@throws EmptyDocumentEx
    *            {@link EmptyDocumentEx}.
    *@throws RequiredArchivableMetadataEx
    *            {@link RequiredArchivableMetadataEx}.
    * @throws SAEEnrichmentEx
    *            {@link SAEEnrichmentEx}.
    * @throws UnknownHashCodeEx
    *            {@link UnknownHashCodeEx}.
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}.
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}.
    * @throws SAECaptureServiceEx
    *            {@link SAECaptureServiceEx}.
    * @throws EmptyFileNameEx
    *            {@link EmptyFileNameEx}
    * @throws MetadataValueNotInDictionaryEx
    *            {@link MetadataValueNotInDictionaryEx}
    * @throws UnknownFormatException
    *            {@link UnknownFormatException}
    * @throws ValidationExceptionInvalidFile
    *            {@link ValidationExceptionInvalidFile}
    * 
    */
   StorageDocument buildBinaryStorageDocumentForCapture(
         UntypedDocument untypedDocument, CaptureResult captureResult)
         throws RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, EmptyFileNameEx, DuplicatedMetadataEx,
         NotArchivableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, SAEEnrichmentEx, UnknownHashCodeEx,
         ReferentialRndException, UnknownCodeRndEx, NotSpecifiableMetadataEx,
         SAECaptureServiceEx, MetadataValueNotInDictionaryEx,
         UnknownFormatException, ValidationExceptionInvalidFile, UnexpectedDomainException, 
         InvalidPagmsCombinaisonException;

}