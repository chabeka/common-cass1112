package fr.urssaf.image.sae.webservices.skeleton;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
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
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions", "PMD.TooManyMethods" })
public class ArchivageUnitaireFailureTest {

   private static final String FAIL_MSG = "le test doit échouer";

   private static final String FAIL_SOAPFAULT = "SOAP FAULT non attendu";

   @Autowired
   private SaeServiceSkeleton skeleton;

   @Autowired
   private SAECaptureService captureService;

   @After
   public void after() {
      EasyMock.reset(captureService);
   }

   private ArchivageUnitaire createArchivageMasseResponse(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return ArchivageUnitaire.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   private static void assertAxisFault(AxisFault axisFault,
         String expectedCode, String expectedMessage) {

      Assert.assertEquals(FAIL_SOAPFAULT, expectedCode, axisFault
            .getFaultCode().getLocalPart());

      Assert.assertEquals(FAIL_SOAPFAULT, expectedMessage, axisFault
            .getMessage());

      Assert.assertEquals(FAIL_SOAPFAULT, "sae", axisFault.getFaultCode()
            .getPrefix());

      Assert.assertEquals(FAIL_SOAPFAULT, "urn:sae:faultcodes", axisFault
            .getFaultCode().getNamespaceURI());
   }

   private static void assertAxisFault(AxisFault axisFault, String expectedCode) {

      Assert.assertEquals(FAIL_SOAPFAULT, expectedCode, axisFault
            .getFaultCode().getLocalPart());

      Assert.assertEquals(FAIL_SOAPFAULT, "sae", axisFault.getFaultCode()
            .getPrefix());

      Assert.assertEquals(FAIL_SOAPFAULT, "urn:sae:faultcodes", axisFault
            .getFaultCode().getNamespaceURI());
   }

   private void callService() throws AxisFault {

      ArchivageUnitaire request = createArchivageMasseResponse("src/test/resources/request/archivageUnitaire_success.xml");

      skeleton.archivageUnitaireSecure(request).getArchivageUnitaireResponse();
   }

   private void mockThrowable(Throwable expectedThrowable) {

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      metadatas.add(EasyMock.anyObject(UntypedMetadata.class));

      try {
         EasyMock
               .expect(
                     captureService.capture(metadatas, EasyMock
                           .anyObject(URI.class))).andThrow(expectedThrowable);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(captureService);
   }

   @Test
   public void archivageUnitaire_failure_SAECaptureServiceEx() {

      mockThrowable(new SAECaptureServiceEx());

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "ErreurInterneCapture",
               "Une erreur interne à l'application est survenue lors de la capture.");
      }
   }

   @Test
   public void archivageUnitaire_failure_RequiredStorageMetadataEx() {

      mockThrowable(new RequiredStorageMetadataEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "ErreurInterneCapture");
      }
   }

   @Test
   public void archivageUnitaire_failure_InvalidValueTypeAndFormatMetadataEx() {

      mockThrowable(new InvalidValueTypeAndFormatMetadataEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureMetadonneesFormatTypeNonValide");
      }
   }

   @Test
   public void archivageUnitaire_failure_UnknownMetadataEx() {

      mockThrowable(new UnknownMetadataEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureMetadonneesInconnu");
      }
   }

   @Test
   public void archivageUnitaire_failure_DuplicatedMetadataEx() {

      mockThrowable(new DuplicatedMetadataEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureMetadonneesDoublon");
      }
   }

   @Test
   public void archivageUnitaire_failure_EmptyDocumentEx() {

      mockThrowable(new EmptyDocumentEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureFichierVide");
      }
   }

   @Test
   public void archivageUnitaire_failure_RequiredArchivableMetadataEx() {

      mockThrowable(new RequiredArchivableMetadataEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureMetadonneesArchivageObligatoire");
      }
   }

   @Test
   public void archivageUnitaire_failure_NotArchivableMetadataEx() {

      mockThrowable(new NotArchivableMetadataEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "ErreurInterneCapture",
               "Une erreur interne à l'application est survenue lors de la capture.");
      }
   }

   @Test
   public void archivageUnitaire_failure_NotSpecifiableMetadataEx() {

      mockThrowable(new NotSpecifiableMetadataEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureMetadonneesInterdites");
      }
   }

   @Test
   public void archivageUnitaire_failure_UnknownCodeRndEx() {

      mockThrowable(new UnknownCodeRndEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureCodeRndInterdit");
      }
   }

   @Test
   public void archivageUnitaire_failure_UnknownHashCodeEx() {

      mockThrowable(new UnknownHashCodeEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureHashErreur");
      }
   }

   @Test
   public void archivageUnitaire_failure_ReferentialRndException() {

      mockThrowable(new ReferentialRndException(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "ErreurInterne",
               "Une erreur interne à l'application est survenue lors de la capture.");
      }
   }

   @Test
   public void archivageUnitaire_failure_CaptureBadEcdeUrlEx() {

      mockThrowable(new CaptureBadEcdeUrlEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureUrlEcdeIncorrecte");
      }
   }

   @Test
   public void archivageUnitaire_failure_CaptureEcdeUrlFileNotFoundEx() {

      mockThrowable(new CaptureEcdeUrlFileNotFoundEx(null));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureUrlEcdeFichierIntrouvable");
      }
   }

   @Test
   public void archivageUnitaire_failure_RuntimeException() throws AxisFault {

      mockThrowable(new RuntimeException("une runtime exception est levée"));

      try {

         callService();

         Assert.fail(FAIL_MSG);

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "ErreurInterneCapture",
               "Une erreur interne à l'application est survenue lors de la capture.");
      }
   }

}
