package fr.urssaf.image.sae.webservices.skeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ConsultationMTOMFailureTest {

   @Autowired
   private SaeServiceSkeleton skeleton;

   private ConsultationMTOM createConsultationMTOM(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return ConsultationMTOM.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   @Autowired
   private SAEDocumentService documentService;

   @After
   public void after() {
      EasyMock.reset(documentService);
   }

   private static final String AXIS_FAULT = "AxisFault non attendue";

   private static void assertAxisFault(AxisFault axisFault, String expectedMsg,
         String expectedType, String expectedPrefix) {

      Assert.assertEquals(AXIS_FAULT, expectedMsg, axisFault.getMessage());
      Assert.assertEquals(AXIS_FAULT, expectedType, axisFault.getFaultCode()
            .getLocalPart());
      Assert.assertEquals(AXIS_FAULT, expectedPrefix, axisFault.getFaultCode()
            .getPrefix());
   }

   @Test
   public void consultation_failure_RuntimeException()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {

      List<String> metadonnees = new ArrayList<String>();
      metadonnees.add("TypeHash");
      metadonnees.add("NbPages");
      metadonnees.add("FormatFichier");

      ConsultParams consultParams = new ConsultParams(UUID
            .fromString("cc4a5ec1-788d-4b41-baa8-d349947865bf"), metadonnees);

      EasyMock.expect(
            documentService.consultation(ConsultationFailureTest
                  .checkConsult(consultParams))).andThrow(
            new RuntimeException("une runtime exception est levée"));

      EasyMock.replay(documentService);

      try {
         ConsultationMTOM request = createConsultationMTOM("src/test/resources/request/consultationMTOM_success.xml");

         skeleton.consultationMTOMSecure(request).getConsultationMTOMResponse();

         Assert
               .fail("le test doit échouer à cause de la levée d'une exception de type "
                     + SAEConsultationServiceException.class);

      } catch (ConsultationAxisFault fault) {

         assertAxisFault(
               fault,
               "Une erreur interne à l'application est survenue lors de la consultation.",
               "ErreurInterneConsultation", "sae");

      }
   }
}
