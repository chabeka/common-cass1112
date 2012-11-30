package fr.urssaf.image.sae.webservices.skeleton;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.xml.stream.XMLStreamReader;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ArchivageMasseAvecHash;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ArchivageMasseAvecHashTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private SAEControlesCaptureService controlesService;
   
   @Autowired
   private SAEControleSupportService supportService;

   @Autowired
   private EcdeServices ecdeServices;

   private static final String EXCEPTION_MSG_TYPE_ERROR = "Le type de hash type hash error nest pas autorisé";
   private static final String EXCEPTION_MSG_HASH_ERROR = "Le hash du fichier sommaire.xml attendu h1 est différent de celui obtenu h2 (type de hash type)";
   private static final String EXCEPTION_MSG_FILE_ERROR = "Impossible de lire le fichier";
   private static final String AXIS_FAULT = "SOAP FAULT non attendu";
   
   @Before
   public void init(){
      String contexteLog = TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString();
      MDC.put("log_contexte_uuid", contexteLog);
   }

   @After
   public void after() {

      EasyMock.reset(controlesService, ecdeServices);

      SecurityContextHolder.getContext().setAuthentication(null);
   }

   private ArchivageMasseAvecHash createArchivageMasse(String filePath) {

      try {
         EasyMock
               .expect(
                     ecdeServices.convertSommaireToFile(EasyMock
                           .anyObject(URI.class)))
               .andReturn(new ClassPathResource("sommaire.xml").getFile()).times(2);
      } catch (EcdeBadURLException e) {
         throw new NestableRuntimeException(e);
      } catch (EcdeBadURLFormatException e) {
         throw new NestableRuntimeException(e);
      } catch (IOException e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(ecdeServices);

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return ArchivageMasseAvecHash.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }
   
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

   
   /**
    * On vérifie qu'on obitient bien un axisFault TypeHashSommaireIncorrect quand le type du has est incorrect
    * @throws CaptureMasseSommaireHashException
    * @throws CaptureMasseSommaireTypeHashException
    */
   @Test
   public void archivageMasseAvecHash_FailureTypeError() throws CaptureMasseSommaireHashException, CaptureMasseSommaireTypeHashException {

      ArchivageMasseAvecHash request = createArchivageMasse("src/test/resources/request/archivageMasseAvecHash_FailureNoParam.xml");
      supportService.checkHash(EasyMock.anyObject(File.class), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class));
      EasyMock.expectLastCall().andThrow(new CaptureMasseSommaireTypeHashException("type hash error"));
      EasyMock.replay(supportService);

      try {
         skeleton.archivageMasseAvecHashSecure(request,"127.0.0.1");
      } catch (AxisFault e) {
         assertAxisFault(e,"TypeHashSommaireIncorrect",EXCEPTION_MSG_TYPE_ERROR);
      }


   }

   /**
    * On vérifie qu'on a bien un AxisFault HashSommaireIncorrect quand les hash ne correspondent pas
    * @throws CaptureMasseSommaireHashException
    * @throws CaptureMasseSommaireTypeHashException
    */
   @Test
   public void archivageMasseAvecHash_FailureHashError() throws CaptureMasseSommaireHashException, CaptureMasseSommaireTypeHashException {

      ArchivageMasseAvecHash request = createArchivageMasse("src/test/resources/request/archivageMasseAvecHash_FailureNoParam.xml");
      supportService.checkHash(EasyMock.anyObject(File.class), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class));
      EasyMock.expectLastCall().andThrow(new CaptureMasseSommaireHashException("h1","h2", "type"));
      EasyMock.replay(supportService);

      try {
         skeleton.archivageMasseAvecHashSecure(request,"127.0.0.1");
      } catch (AxisFault e) {
         assertAxisFault(e,"HashSommaireIncorrect",EXCEPTION_MSG_HASH_ERROR);
      }

     
   }
   /**
    * On vérifie qu'on a un AxisFault CaptureUrlEcdeFichierIntrouvable si on n'arrive pas a lire le fichier sommaire.xml.
    * @throws CaptureMasseSommaireHashException
    * @throws CaptureMasseSommaireTypeHashException
    */
   @Test
   public void archivageMasseAvecHash_FailureFileReadError() throws CaptureMasseSommaireHashException, CaptureMasseSommaireTypeHashException {

      ArchivageMasseAvecHash request = createArchivageMasse("src/test/resources/request/archivageMasseAvecHash_FailureNoParam.xml");
      supportService.checkHash(EasyMock.anyObject(File.class), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class));
      EasyMock.expectLastCall().andThrow(new CaptureMasseRuntimeException("Impossible de lire le fichier"));
      EasyMock.replay(supportService);

      try {
         skeleton.archivageMasseAvecHashSecure(request,"127.0.0.1");
      } catch (AxisFault e) {
         assertAxisFault(e,"CaptureUrlEcdeFichierIntrouvable",EXCEPTION_MSG_FILE_ERROR);
      }

      
   }
}
