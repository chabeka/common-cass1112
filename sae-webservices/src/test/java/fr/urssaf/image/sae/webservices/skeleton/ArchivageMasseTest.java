package fr.urssaf.image.sae.webservices.skeleton;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URI;
import java.util.UUID;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ArchivageMasseTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private SAEControlesCaptureService controlesService;

   @Autowired
   private EcdeServices ecdeServices;

   @Before
   public void before() {

      MockHttpServletResponse response = new MockHttpServletResponse();
      response.setContentType("text/html");

      MessageContext ctx = new MessageContext();
      ctx.setProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE, response);

      MessageContext.setCurrentMessageContext(ctx);

      // indispensable car c'est ainsi que l'identifiant du traitement est
      // calculé
      MDC.put(BuildOrClearMDCAspect.LOG_CONTEXTE, UUID.randomUUID().toString());
   }

   @After
   public void after() {

      EasyMock.reset(controlesService, ecdeServices);
   }

   private ArchivageMasse createArchivageMasse(String filePath) {

      try {
         EasyMock
               .expect(
                     ecdeServices.convertSommaireToFile(EasyMock
                           .anyObject(URI.class)))
               .andReturn(
                     new File(
                           "C:/appl/sae/ecde_local/SAE_INTEGRATION/"
                                 + "20110822/CaptureMasse-201-CaptureMasse-OK-Tor-10/sommaire.xml"));
      } catch (EcdeBadURLException e) {
         throw new NestableRuntimeException(e);
      } catch (EcdeBadURLFormatException e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(ecdeServices);

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return ArchivageMasse.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   @Test
   public void archivageMasse_success() throws AxisFault {

      ArchivageMasse request = createArchivageMasse("src/test/resources/request/archivageMasse_success.xml");

      ArchivageMasseResponse response = skeleton.archivageMasseSecure(request,
            "127.0.0.1");

      assertNotNull("Test de l'archivage masse", response
            .getArchivageMasseResponse());
   }

}
