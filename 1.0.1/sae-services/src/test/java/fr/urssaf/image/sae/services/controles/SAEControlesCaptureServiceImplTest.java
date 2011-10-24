/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.CommonsServices;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
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
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;

/**
 * Classe permettant de tester le service de contrôle.
 * 
 * @author Rhofir
 */
@SuppressWarnings("all")
public class SAEControlesCaptureServiceImplTest extends CommonsServices {
   @Autowired
   @Qualifier("saeControlesCaptureService")
   SAEControlesCaptureService saeControlesCaptureService;

   @Autowired
   @Qualifier("saeEnrichmentMetadataService")
   SAEEnrichmentMetadataService saeEnrichmentMetadataService;

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
    * @return Le service saeControlesCaptureService
    */
   public final SAEControlesCaptureService getSaeControlesCaptureService() {
      return saeControlesCaptureService;
   }

   /**
    * @param saeControlesCaptureService
    *           : Le service saeControlesCaptureService
    */
   public final void setSaeControlesCaptureService(
         SAEControlesCaptureService saeControlesCaptureService) {
      this.saeControlesCaptureService = saeControlesCaptureService;
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkUntypedDocument(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument)}
    * .
    */
   @Test(expected = EmptyDocumentEx.class)
   public final void checkEmptyUntypedDocumentFailed() throws EmptyDocumentEx,
         SAECaptureServiceEx, IOException, ParseException {
      UntypedDocument untypedDocument = getUntypedDocumentMockData();
      untypedDocument.setContent(null);
      saeControlesCaptureService.checkUntypedDocument(untypedDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkUntypedMetadata(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument)}
    * .
    */
   @Test
   public final void checkUntypedMetadata() throws UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         SAECaptureServiceEx, IOException, ParseException, RequiredArchivableMetadataEx {
      UntypedDocument untypedDocument = getUntypedDocumentMockData();
      saeControlesCaptureService.checkUntypedMetadata(untypedDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkUntypedMetadata(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument)}
    * .
    */
   @Test(expected = DuplicatedMetadataEx.class)
   public final void checkDuplicatedMetadataFailed() throws UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         SAECaptureServiceEx, IOException, ParseException, RequiredArchivableMetadataEx {
      UntypedDocument untypedDocument = getUntypedDocumentMockData();
      untypedDocument.getUMetadatas().add(
            new UntypedMetadata("DateCreation", "2012-01-01"));
      saeControlesCaptureService.checkUntypedMetadata(untypedDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkUntypedMetadata(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument)}
    * .
    */
   @Test(expected = UnknownMetadataEx.class)
   public final void checkUnknownMetadataFailed() throws UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         SAECaptureServiceEx, IOException, ParseException, RequiredArchivableMetadataEx {
      UntypedDocument untypedDocument = getUntypedDocumentMockData();
      untypedDocument.getUMetadatas().add(
            new UntypedMetadata("DateCreat", "2012-01-01"));
      saeControlesCaptureService.checkUntypedMetadata(untypedDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkUntypedMetadata(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument)}
    * .
    */
   @Test(expected = InvalidValueTypeAndFormatMetadataEx.class)
   public final void checkInvalidValueTypeAndFormatMetadataFailed()
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, SAECaptureServiceEx, IOException,
         ParseException, RequiredArchivableMetadataEx {
      UntypedDocument untypedDocument = getUntypedDocumentMockData();
      untypedDocument.getUMetadatas().add(
            new UntypedMetadata("DateReception", "12121"));
      saeControlesCaptureService.checkUntypedMetadata(untypedDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkSaeMetadataForCapture(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test
   public final void checkSaeMetadataForCapture()
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx,
         SAECaptureServiceEx, IOException, ParseException {
      SAEDocument saeDocument = getSAEDocumentMockData();
      saeControlesCaptureService.checkSaeMetadataForCapture(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkSaeMetadataForCapture(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test(expected = NotSpecifiableMetadataEx.class)
   public final void notSpecifiableMetadataFailed()
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx,
         SAECaptureServiceEx, IOException, ParseException {
      SAEDocument saeDocument = getSAEDocumentMockData();
      saeDocument.getMetadatas().add(new SAEMetadata("CodeFonction", "100"));
      saeControlesCaptureService.checkSaeMetadataForCapture(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkSaeMetadataForCapture(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test(expected = RequiredArchivableMetadataEx.class)
   public final void requiredArchivableMetadataFailed()
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx,
         SAECaptureServiceEx, IOException, ParseException {
      SAEDocument saeDocument = getSAEDocumentMockData();
      SAEMetadata saeMetadata = null;
      for (SAEMetadata metadata : saeDocument.getMetadatas()) {
         if (StorageTechnicalMetadatas.TITRE.getLongCode().equals(
               metadata.getLongCode())) {
            saeMetadata = metadata;
            break;
         }
      }
      // Suppression de la métadonnée Titre.
      saeDocument.getMetadatas().remove(saeMetadata);
      saeControlesCaptureService.checkSaeMetadataForCapture(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkSaeMetadataForCapture(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test(expected = IllegalArgumentException.class)
   public final void saeCaptureFailed() throws NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, SAECaptureServiceEx, IOException,
         ParseException {
      SAEDocument saeDocument = getSAEDocumentMockData();
      saeDocument.setMetadatas(null);
      saeControlesCaptureService.checkSaeMetadataForCapture(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkSaeMetadataForStorage(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test
   public final void checkSaeMetadataForStorage()
         throws RequiredStorageMetadataEx, SAECaptureServiceEx, IOException,
         ParseException, SAEEnrichmentEx, ReferentialRndException,
         UnknownCodeRndEx {
      SAEDocument saeDocument = getSAEDocumentMockData();
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
      saeControlesCaptureService.checkSaeMetadataForStorage(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkHashCodeMetadataForStorage(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test(expected = UnknownHashCodeEx.class)
   public final void checkHashCodeMetadataForStorageFailed()
         throws SAECaptureServiceEx, IOException, ParseException,
         UnknownHashCodeEx {
      SAEDocument saeDocument = getSAEDocumentMockData();
      for (SAEMetadata saeMetadata : saeDocument.getMetadatas()) {
         if (saeMetadata.getLongCode().equals(
               SAEArchivalMetadatas.HASH_CODE.getLongCode())) {
            saeMetadata.setValue("2121");
            break;
         }
      }
      saeControlesCaptureService.checkHashCodeMetadataForStorage(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkHashCodeMetadataForStorage(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test(expected = UnknownHashCodeEx.class)
   public final void checkAlogoHashMetadataForStorageFailed()
         throws SAECaptureServiceEx, IOException, ParseException,
         UnknownHashCodeEx {
      SAEDocument saeDocument = getSAEDocumentMockData();
      for (SAEMetadata saeMetadata : saeDocument.getMetadatas()) {
         if (saeMetadata.getLongCode().equals(
               SAEArchivalMetadatas.TYPE_HASH.getLongCode())) {
            saeMetadata.setValue("2121");
            break;
         }
      }
      saeControlesCaptureService.checkHashCodeMetadataForStorage(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkHashCodeMetadataForStorage(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test
   public final void checkHashCodeMetadataForStorage()
         throws SAECaptureServiceEx, IOException, ParseException,
         UnknownHashCodeEx {
      SAEDocument saeDocument = getSAEDocumentMockData();
      saeControlesCaptureService.checkHashCodeMetadataForStorage(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkSaeMetadataForStorage(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test(expected = RequiredStorageMetadataEx.class)
   public final void requiredStorageMetadataFailed()
         throws RequiredStorageMetadataEx, SAECaptureServiceEx, IOException,
         ParseException, SAEEnrichmentEx, ReferentialRndException,
         UnknownCodeRndEx {
      SAEDocument saeDocument = getSAEDocumentMockData();
      SAEMetadata saeMetadataToRemove = null;
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
      for (SAEMetadata saeMetadata : saeDocument.getMetadatas()) {
         if (saeMetadata.getLongCode().equals(
               SAEArchivalMetadatas.CODE_RND.getLongCode())) {
            saeMetadataToRemove = saeMetadata;
            break;
         }
      }
      if (null != saeMetadataToRemove) {
         saeDocument.getMetadatas().remove(saeMetadataToRemove);
      }
      saeControlesCaptureService.checkSaeMetadataForStorage(saeDocument);
   }
   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.controles.impl.SAEControlesCaptureServiceImpl#checkSaeMetadataForStorage(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
    * .
    */
   @Test(expected = RequiredStorageMetadataEx.class)
   public final void requiredValueStorageMetadataFailed()
         throws RequiredStorageMetadataEx, SAECaptureServiceEx, IOException,
         ParseException, SAEEnrichmentEx, ReferentialRndException,
         UnknownCodeRndEx {
      SAEDocument saeDocument = getSAEDocumentMockData();
      SAEMetadata saeMetadataToRemove = null;
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
      for (SAEMetadata saeMetadata : saeDocument.getMetadatas()) {
         if (saeMetadata.getLongCode().equals(
               SAEArchivalMetadatas.CODE_RND.getLongCode())) {
            saeMetadataToRemove = saeMetadata;
            saeMetadataToRemove.setValue(null);
            break;
         }
      }
      saeControlesCaptureService.checkSaeMetadataForStorage(saeDocument);
   }
}
