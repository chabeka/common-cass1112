package fr.urssaf.image.sae.webservices.skeleton;

import static org.junit.Assert.assertNotNull;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.webservices.service.support.LauncherSupport;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml"   
                                  })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ArchivageMasseTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private LauncherSupport captureLauncher;

   @Autowired
   private SAEControlesCaptureService controlesService;

   @Before
   public void before() {

      MockHttpServletResponse response = new MockHttpServletResponse();
      response.setContentType("text/html");

      MessageContext ctx = new MessageContext();
      ctx.setProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE, response);

      MessageContext.setCurrentMessageContext(ctx);
   }

   @After
   public void after() {

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

   private void initCaptureLauncher(boolean isLaunched) {

      EasyMock.expect(captureLauncher.isLaunched()).andReturn(isLaunched);

      captureLauncher.launch(EasyMock.anyObject(Object.class), EasyMock
            .anyObject(Object.class), EasyMock.anyObject(Object.class),
            EasyMock.anyObject(Object.class));

      EasyMock.replay(captureLauncher);
   }

   private static void assertAxisFault(AxisFault axisFault,
         String expectedCode, String expectedMessage) {

      Assert.assertEquals("SOAP FAULT localPart non attendu", expectedCode,
            axisFault.getFaultCode().getLocalPart());

      Assert.assertEquals("SOAP FAULT prefix non attendu", "sae", axisFault
            .getFaultCode().getPrefix());

      Assert.assertEquals("SOAP FAULT namespaceURI non attendu",
            "urn:sae:faultcodes", axisFault.getFaultCode().getNamespaceURI());

      Assert.assertEquals("SOAP FAULT message non attendu", expectedMessage,
            axisFault.getMessage());
   }

   @Test
   public void archivageMasse_success() throws AxisFault {

      initCaptureLauncher(false);

      ArchivageMasse request = createArchivageMasse("src/test/resources/request/archivageMasse_success.xml");

      ArchivageMasseResponse response = skeleton.archivageMasseSecure(request);

      assertNotNull("Test de l'archivage masse", response
            .getArchivageMasseResponse());
   }

   @Test
   public void archivageMasse_failure_CaptureMasseRefusee() throws AxisFault {

      initCaptureLauncher(true);

      ArchivageMasse request = createArchivageMasse("src/test/resources/request/archivageMasse_success.xml");

      try {

         skeleton.archivageMasseSecure(request);

         Assert
               .fail("l'appel de la capture en masse doit lever une exception CaptureMasseRefusee");

      } catch (AxisFault axisFault) {

         assertAxisFault(
               axisFault,
               "CaptureMasseRefusee",
               "L'archivage de masse ne peut pas être exécuté sur ce serveur car un traitement de masse est déjà en cours.");
      }

   }

}
