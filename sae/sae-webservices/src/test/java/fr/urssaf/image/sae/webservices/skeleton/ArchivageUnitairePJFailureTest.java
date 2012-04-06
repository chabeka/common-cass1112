package fr.urssaf.image.sae.webservices.skeleton;

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

import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ArchivageUnitairePJFailureTest {

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

   private ArchivageUnitairePJ createArchivageUnitairePJ(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return ArchivageUnitairePJ.Factory.parse(reader);

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

   // private static void assertAxisFault(AxisFault axisFault, String
   // expectedCode) {
   //
   // Assert.assertEquals(FAIL_SOAPFAULT, expectedCode, axisFault
   // .getFaultCode().getLocalPart());
   //
   // Assert.assertEquals(FAIL_SOAPFAULT, "sae", axisFault.getFaultCode()
   // .getPrefix());
   //
   // Assert.assertEquals(FAIL_SOAPFAULT, "urn:sae:faultcodes", axisFault
   // .getFaultCode().getNamespaceURI());
   // }

   private void callService() throws AxisFault {

      ArchivageUnitairePJ request = createArchivageUnitairePJ("src/test/resources/request/archivageUnitairePJ_file_success.xml");

      skeleton.archivageUnitairePJSecure(request)
            .getArchivageUnitairePJResponse();
   }

   private void mockThrowable(Throwable expectedThrowable) {

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      metadatas.add(EasyMock.anyObject(UntypedMetadata.class));
      byte[] content = EasyMock.notNull();

      try {
         EasyMock.expect(
               captureService.captureBinaire(metadatas, content, EasyMock
                     .anyObject(String.class))).andThrow(expectedThrowable);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(captureService);
   }

   @Test
   public void archivageUnitairePJ_file_failure_SAECaptureServiceEx()
         throws AxisFault {

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
   public void archivageUnitairePJ_file_failure_RuntimeException()
         throws AxisFault {

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
