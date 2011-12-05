package fr.urssaf.image.sae.services.document.commons.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
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
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAECommonCaptureServiceImpl.class);
   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesService;

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
      // Traces debug - entrée méthode
      String prefixeTrc = "buildStorageDocumentForCapture()";
      LOGGER.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
      SAEDocument saeDocument = null;
      StorageDocument storageDocument = null;
      try {
      // on ne contrôle pas la taille du document
         LOGGER
               .debug(
                     "{} - Début des contrôles sur (UntypedDocument et UntypedMetadata)",
                     prefixeTrc);
         controlesService.checkUntypedDocument(untypedDocument);
         controlesService.checkUntypedMetadata(untypedDocument);
         LOGGER
               .debug(
                     "{} - Fin des contrôles sur (UntypedDocument et UntypedMetadata)",
                     prefixeTrc);
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
