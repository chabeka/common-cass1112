package fr.urssaf.image.sae.webservices.skeleton;

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

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ArchivageMasseFailureTest {
   
   private static final String EXCEPTION_MSG = "une exception est levée";

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private SAEControlesCaptureService controlesService;

   @After
   public void after() {

      EasyMock.reset(controlesService);
   }

   private ArchivageMasse createArchivageMasse(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return ArchivageMasse.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   private void callService() throws AxisFault {

      ArchivageMasse request = createArchivageMasse("src/test/resources/request/archivageMasse_success.xml");

      skeleton.archivageMasseSecure(request);
   }

   private static final String AXIS_FAULT = "SOAP FAULT non attendu";

   private static void assertAxisFault(AxisFault axisFault,
         String expectedCode,String expectedMsg) {

      Assert.assertEquals(AXIS_FAULT, expectedCode, axisFault.getFaultCode()
            .getLocalPart());

      Assert.assertEquals(AXIS_FAULT, "sae", axisFault.getFaultCode()
            .getPrefix());

      Assert.assertEquals(AXIS_FAULT, "urn:sae:faultcodes", axisFault
            .getFaultCode().getNamespaceURI());

      Assert.assertEquals(AXIS_FAULT, expectedMsg, axisFault.getMessage());
   }

   private void mockThrowable(Throwable expectedThrowable) {

      try {

         controlesService.checkBulkCaptureEcdeUrl(EasyMock
               .anyObject(String.class));

         EasyMock.expectLastCall().andThrow(expectedThrowable);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(controlesService);
   }

   @Test
   public void archivageMasse_failure_CaptureBadEcdeUrlEx() throws AxisFault {

      mockThrowable(new CaptureBadEcdeUrlEx(EXCEPTION_MSG));

      try {

         callService();

         Assert
               .fail("l'appel de la capture en masse doit lever une exception CaptureBadEcdeUrlEx");

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault,"CaptureUrlEcdeIncorrecte",EXCEPTION_MSG);
      }
   }

   @Test
   public void archivageMasse_failure_CaptureEcdeUrlFileNotFoundEx()
         throws AxisFault {

      mockThrowable(new CaptureEcdeUrlFileNotFoundEx(EXCEPTION_MSG));

      try {

         callService();

         Assert
               .fail("l'appel de la capture en masse doit lever une exception CaptureEcdeUrlFileNotFoundEx");

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault,"CaptureUrlEcdeFichierIntrouvable",EXCEPTION_MSG);
      }

   }

   @Test
   public void archivageMasse_failure_CaptureEcdeWriteFileEx() throws AxisFault {

      mockThrowable(new CaptureEcdeWriteFileEx(EXCEPTION_MSG));

      try {

         callService();

         Assert
               .fail("l'appel de la capture en masse doit lever une exception CaptureEcdeWriteFileEx");

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureEcdeDroitEcriture",EXCEPTION_MSG);
      }
   }

   @Test
   public void archivageMasse_failure_RuntimeException() throws AxisFault {

      mockThrowable(new RuntimeException("une runtime exception est levée"));

      try {

         callService();

         Assert
               .fail("l'appel de la capture en masse doit lever une exception CaptureEcdeWriteFileEx");

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "ErreurInterneCapture",
               "Une erreur interne à l'application est survenue lors de la capture.");
      }
   }

}
