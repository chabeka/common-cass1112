package fr.urssaf.image.sae.services.enrichment;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.CommonsServices;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

public class SAEEnrichmentMetadataServiceImplTest extends CommonsServices {
   @Autowired
   @Qualifier("saeEnrichmentMetadataService")
   SAEEnrichmentMetadataService saeEnrichmentMetadataService;
   @Autowired
   @Qualifier("mappingDocumentService")
   private MappingDocumentService mappingService;

   /**
    * @return Le service de mappingService
    */
   public final MappingDocumentService getMappingService() {
      return mappingService;
   }

   /**
    * @param mappingService
    *           : Le service de mappingService.
    */
   public final void setMappingService(MappingDocumentService mappingService) {
      this.mappingService = mappingService;
   }

   /**
    * @return Le service d'enrichment des metadonnées.
    */
   public final SAEEnrichmentMetadataService getSaeEnrichmentMetadataService() {
      return saeEnrichmentMetadataService;
   }

   /**
    * @param saeEnrichmentMetadataService
    *           the saeEnrichmentMetadataService to set
    */
   public final void setSaeEnrichmentMetadataService(
         SAEEnrichmentMetadataService saeEnrichmentMetadataService) {
      this.saeEnrichmentMetadataService = saeEnrichmentMetadataService;
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.enrichment.impl.SAEEnrichmentMetadataServiceImpl#enrichmentMetadata(SAEDocument)}
    * .
    */
   @Test
   public final void enrichmentMetadata() throws SAECaptureServiceEx,
         IOException, ParseException, SAEEnrichmentEx, InvalidSAETypeException,
         MappingFromReferentialException, ReferentialRndException, UnknownCodeRndEx {
      SAEDocument saeDocument = mappingService
            .untypedDocumentToSaeDocument(getUntypedDocumentMockData());
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
      Assert.assertNotNull(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.enrichment.impl.SAEEnrichmentMetadataServiceImpl#enrichmentMetadata(SAEDocument)}
    * .
    */
   @Test(expected = UnknownCodeRndEx.class)
   public final void enrichmentMetadataFailed() throws SAECaptureServiceEx,
         IOException, ParseException, SAEEnrichmentEx, ReferentialRndException, UnknownCodeRndEx {
      SAEDocument saeDocument = getSAEDocumentMockData();
      for (SAEMetadata saeMetadata : saeDocument.getMetadatas()) {
         if (saeMetadata.getLongCode().equals(
               SAEArchivalMetadatas.CODERND.getLongCode())) {
            saeMetadata.setValue("121212");
            break;
         }
      }
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
   }
}
