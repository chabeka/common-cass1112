package fr.urssaf.image.sae.services.document.commons.impl;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.services.CommonsServices;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
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

/***
 * Classe de test pour le service commun de capture en masse et capture
 * unitaire.
 * 
 * @author Rhofir
 */
@SuppressWarnings("all")
public class SAECommonCaptureServiceImplTest extends CommonsServices {
   @Autowired
   @Qualifier("saeCommonCaptureService")
   SAECommonCaptureService saeCommonCaptureService;

   /**
    * @return Le service saeCommonCaptureService
    */
   public final SAECommonCaptureService getSaeCommonCaptureService() {
      return saeCommonCaptureService;
   }

   /**
    * @param saeCommonCaptureService
    *           : Le service saeCommonCaptureService.
    */
   public final void setSaeCommonCaptureService(
         SAECommonCaptureService saeCommonCaptureService) {
      this.saeCommonCaptureService = saeCommonCaptureService;
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService#buildStorageDocumentForCapture(UntypedDocument)}
    * .
    */
   @Test
   public final void buildStorageDocumentForCapture()
         throws SAECaptureServiceEx, IOException, ParseException,
         SAEEnrichmentEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotArchivableMetadataEx,
         NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, MappingFromReferentialException,
         InvalidSAETypeException, UnknownHashCodeEx, ReferentialRndException,
         UnknownCodeRndEx {
      UntypedDocument untypedDocument = getUntypedDocumentMockData();
      saeCommonCaptureService.buildStorageDocumentForCapture(untypedDocument);
   }

}
