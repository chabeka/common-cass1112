package fr.urssaf.image.sae.services.capture.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
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
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SAECaptureServiceValidationTest {

   private SAECaptureService service;

   private static List<UntypedMetadata> metadatas;

   private static URI ecdeURL;

   @BeforeClass
   public static void beforeClass() {

      ecdeURL = URI
            .create("ecde://cer69-ecde.cer69.recouv/DCL001/19991231/3/documents/attestation.pdf");

      metadatas = new ArrayList<UntypedMetadata>();
      metadatas.add(new UntypedMetadata("test", "test"));

   }

   @Before
   public void before() {

      service = new SAECaptureService() {

         @Override
         public CaptureResult capture(List<UntypedMetadata> metadatas,
               URI ecdeURL) {

            return null;
         }

         @Override
         public CaptureResult captureBinaire(List<UntypedMetadata> metadatas,
               DataHandler content, String fileName) {

            return null;
         }

         @Override
         public CaptureResult captureFichier(List<UntypedMetadata> metadatas,
               String path) {
            return null;
         }
      };
   }

   @Test
   public void capture_success() throws SAECaptureServiceEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException {

      try {
         service.capture(metadatas, ecdeURL);

      } catch (IllegalArgumentException e) {
         fail("les arguments en entrée doivent être valides");
      }

   }

   @Test
   public void capture_failure_metadatas_null() throws SAECaptureServiceEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException {

      assertCapture_failure_metadatas(service, null);
      assertCapture_failure_metadatas(service, new ArrayList<UntypedMetadata>());

   }

   private static void assertCapture_failure_metadatas(
         SAECaptureService service, List<UntypedMetadata> metadatas)
         throws SAECaptureServiceEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx,
         CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException {

      try {

         service.capture(metadatas, ecdeURL);

         fail("l'argument metadatas ne doit pas être renseigné");
      } catch (IllegalArgumentException e) {
         assertEquals("message d'exception non attendu",
               "L'argument 'metadatas' doit être renseigné ou être non null.",
               e.getMessage());
      }

   }

   @Test
   public void capture_failure_ecdeUrl_null() throws SAECaptureServiceEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException {

      try {

         service.capture(metadatas, null);

         fail("l'argument ecdeURL ne doit pas être renseigné");
      } catch (IllegalArgumentException e) {
         assertEquals("message d'exception non attendu",
               "L'argument 'ecdeURL' doit être renseigné ou être non null.", e
                     .getMessage());
      }

   }

}
