package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.Copie;
import fr.cirtil.www.saeservice.CopieResponse;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
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
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.copie.SAECopieServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.webservices.exception.CopieAxisFault;

public interface WSCopieService {

   CopieResponse copie(Copie request) throws CopieAxisFault,
         ArchiveInexistanteEx, SAEConsultationServiceException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         ReferentialException, SAECopieServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException;
}
