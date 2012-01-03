package fr.urssaf.image.sae.webservices.skeleton;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
import fr.urssaf.image.sae.webservices.service.support.LauncherSupport;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml"   
                                  })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ArchivageMasseFailureTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private LauncherSupport captureLauncher;

   @Autowired
   private SAEControlesCaptureService controlesService;

   @Before
   public void before() {

     EasyMock.expect(captureLauncher.isLaunched()).andReturn(false);

      captureLauncher.launch(EasyMock.anyObject(Object.class), EasyMock
            .anyObject(Object.class), EasyMock.anyObject(Object.class));

      EasyMock.replay(captureLauncher);
   }
   
   @After
   public void after(){
      
      EasyMock.reset(captureLauncher);
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

   private static void assertAxisFault(AxisFault axisFault, String expectedCode) {

      Assert.assertEquals("SOAP FAULT non attendu", expectedCode, axisFault
            .getFaultCode().getLocalPart());

      Assert.assertEquals("SOAP FAULT non attendu", "sae", axisFault
            .getFaultCode().getPrefix());

      Assert.assertEquals("SOAP FAULT non attendu", "urn:sae:faultcodes",
            axisFault.getFaultCode().getNamespaceURI());
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

      mockThrowable(new CaptureBadEcdeUrlEx(null));

      try {

         callService();

         Assert
               .fail("l'appel de la capture en masse doit lever une exception CaptureBadEcdeUrlEx");

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureUrlEcdeIncorrecte");
      }
   }

   @Test
   public void archivageMasse_failure_CaptureEcdeUrlFileNotFoundEx()
         throws AxisFault {

      mockThrowable(new CaptureEcdeUrlFileNotFoundEx(null));

      try {

         callService();

         Assert
               .fail("l'appel de la capture en masse doit lever une exception CaptureEcdeUrlFileNotFoundEx");

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureUrlEcdeFichierIntrouvable");
      }

   }

   @Test
   public void archivageMasse_failure_CaptureEcdeWriteFileEx() throws AxisFault {

      mockThrowable(new CaptureEcdeWriteFileEx(null));

      try {

         callService();

         Assert
               .fail("l'appel de la capture en masse doit lever une exception CaptureEcdeWriteFileEx");

      } catch (AxisFault axisFault) {

         assertAxisFault(axisFault, "CaptureEcdeDroitEcriture");
      }
   }

}
