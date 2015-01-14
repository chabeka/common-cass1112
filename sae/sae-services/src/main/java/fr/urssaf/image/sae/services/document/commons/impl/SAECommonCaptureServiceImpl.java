package fr.urssaf.image.sae.services.document.commons.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.controles.model.ControleFormatSucces;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
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
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe concrète pour le service commun pour les services de Capture unitaire
 * et Capture en masse.
 */
@Service
@Qualifier("saeCommonCaptureService")
public class SAECommonCaptureServiceImpl implements SAECommonCaptureService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAECommonCaptureServiceImpl.class);

   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesService;

   @Autowired
   private MappingDocumentService mappingService;

   @Autowired
   @Qualifier("saeEnrichmentMetadataService")
   private SAEEnrichmentMetadataService enrichmentService;

   @Autowired
   private PrmdService prmdService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument buildStorageDocumentForCapture(
         UntypedDocument untypedDocument, CaptureResult captureResult)
         throws RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx, SAEEnrichmentEx,
         UnknownHashCodeEx, ReferentialRndException, UnknownCodeRndEx,
         SAECaptureServiceEx, MetadataValueNotInDictionaryEx,
         UnknownFormatException, ValidationExceptionInvalidFile {
      // Traces debug - entrée méthode
      String prefixeTrc = "buildStorageDocumentForCapture()";
      LOGGER.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // on ne contrôle pas la taille du document
      LOGGER
            .debug(
                  "{} - Début des contrôles sur (UntypedDocument et UntypedMetadata)",
                  prefixeTrc);
      controlesService.checkUntypedDocument(untypedDocument);

      StorageDocument storageDocument = buildStorageDocument(untypedDocument,
            captureResult);

      return storageDocument;

   }

   /**
    * {@inheritDoc}
    * 
    * @throws ValidationExceptionInvalidFile
    * @throws UnknownFormatException
    */
   @Override
   public final StorageDocument buildBinaryStorageDocumentForCapture(
         UntypedDocument untypedDocument, CaptureResult captureResult)
         throws RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, EmptyFileNameEx, DuplicatedMetadataEx,
         NotArchivableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, SAEEnrichmentEx, UnknownHashCodeEx,
         ReferentialRndException, UnknownCodeRndEx, NotSpecifiableMetadataEx,
         SAECaptureServiceEx, MetadataValueNotInDictionaryEx,
         UnknownFormatException, ValidationExceptionInvalidFile {

      // Traces debug - entrée méthode
      String prefixeTrc = "buildBinaryStorageDocumentForCapture()";
      LOGGER.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // on ne contrôle pas la taille du document
      LOGGER
            .debug("{} - Début des contrôles sur (UntypedDocument)", prefixeTrc);
      controlesService.checkUntypedBinaryDocument(untypedDocument);

      StorageDocument storageDocument = buildStorageDocument(untypedDocument,
            captureResult);

      return storageDocument;
   }

   private StorageDocument buildStorageDocument(
         UntypedDocument untypedDocument, CaptureResult captureResult)
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, SAEEnrichmentEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx,
         RequiredStorageMetadataEx, SAECaptureServiceEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile {

      SAEDocument saeDocument = null;
      StorageDocument storageDocument = null;
      String prefixeTrc = "buildStorageDocument()";

      try {

         controlesService.checkUntypedMetadata(untypedDocument);
         LOGGER
               .debug(
                     "{} - Fin des contrôles sur (UntypedDocument et UntypedMetadata)",
                     prefixeTrc);

         LOGGER.debug("{} - Début de vérification des droits", prefixeTrc);

         AuthenticationToken token = (AuthenticationToken) AuthenticationContext
               .getAuthenticationToken();

         List<SaePrmd> prmds = token.getSaeDroits().get("archivage_unitaire");
         
         //-- Affectation du domaine en fonction du contrat de service
         prmdService.addDomaine(untypedDocument.getUMetadatas(), prmds);
         
         boolean isPermitted = prmdService.isPermitted(untypedDocument
               .getUMetadatas(), prmds);

         if (!isPermitted) {
            throw new AccessDeniedException(
                  "Le document est refusé à l'arhivage car les droits sont insuffisants");
         }

         LOGGER.debug("{} - Fin de vérification des droits", prefixeTrc);
         LOGGER
               .debug(
                     "{} - Début de la conversion de UntypedDocument vers SaeDocument",
                     prefixeTrc);
         saeDocument = mappingService
               .untypedDocumentToSaeDocument(untypedDocument);
         LOGGER.debug(
               "{} - Fin de la conversion de UntypedDocument vers SaeDocument",
               prefixeTrc);

         if (saeDocument != null) {
            LOGGER.debug("{} - Début des contrôles sur le format", prefixeTrc);

            // Vérification de la validation des formats.
            // récupération de la liste des profils de contrôle
            List<FormatControlProfil> listFormatControlProfil = token
                  .getListFormatControlProfil();

            ControleFormatSucces retour = controlesService.checkFormat(
                  "captureUnitaire", saeDocument, listFormatControlProfil);

            // récupération du retour du checkFormat
            captureResult.setIdentificationActivee(retour
                  .isIdentificationActivee());
            captureResult.setIdentificationEchecMonitor(retour
                  .isIdentificationEchecMonitor());
            captureResult.setIdFormatReconnu(retour.getIdFormatReconnu());
            captureResult.setValidationActivee(retour.isValidationActivee());
            captureResult.setValidationEchecMonitor(retour
                  .isValidationEchecMonitor());
            captureResult.setDetailEchecValidation(retour
                  .getDetailEchecValidation());

            LOGGER.debug("{} - Fin des contrôles sur le format", prefixeTrc);

            LOGGER.debug(
                  "{} - Début des contrôles sur (SaeDocument  et SaeMetadata)",
                  prefixeTrc);
            controlesService.checkSaeMetadataForCapture(saeDocument);
            controlesService.checkHashCodeMetadataForStorage(saeDocument);
            enrichmentService.enrichmentMetadata(saeDocument);
            controlesService.checkSaeMetadataForStorage(saeDocument);
            LOGGER.debug(
                  "{} - Fin des contrôles sur (SaeDocument  et SaeMetadata)",
                  prefixeTrc);

            LOGGER
                  .debug(
                        "{} - Début de la conversion de SaeDocument vers StorageDocument",
                        prefixeTrc);
            storageDocument = mappingService
                  .saeDocumentToStorageDocument(saeDocument);
            LOGGER
                  .debug(
                        "{} - Fin de la conversion de SaeDocument vers StorageDocument",
                        prefixeTrc);
         }
      } catch (InvalidSAETypeException e) {
         throw new SAECaptureServiceEx(e);
      } catch (MappingFromReferentialException e) {
         throw new SAECaptureServiceEx(e);
      }
      return storageDocument;

   }

}
