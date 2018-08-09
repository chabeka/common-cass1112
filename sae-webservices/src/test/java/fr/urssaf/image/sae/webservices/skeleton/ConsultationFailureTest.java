package fr.urssaf.image.sae.webservices.skeleton;

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

import fr.cirtil.www.saeservice.Consultation;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.webservices.comparator.ConsultParamComparator;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;
import fr.urssaf.image.sae.webservices.security.exception.SaeAccessDeniedAxisFault;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ConsultationFailureTest {

   @Autowired
   private SaeServiceSkeleton skeleton;

   private Consultation createConsultationResponseType(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return Consultation.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

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

   @Autowired
   private SAEDocumentService documentService;

   @After
   public void after() {
      EasyMock.reset(documentService);
   }

   @Test
   public void consultation_failure_uuidNotFound()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, SaeAccessDeniedAxisFault {

      ConsultParams consultParams = new ConsultParams(UUID
            .fromString("cc26f62e-fd52-42ff-ad83-afc26f96ea91"), null);

      EasyMock
            .expect(documentService.consultation(checkConsult(consultParams)))
            .andReturn(null);

      EasyMock.replay(documentService);

      try {
         Consultation request = createConsultationResponseType("src/test/resources/request/consultation_failure_uuidNotFound.xml");

         skeleton.consultationSecure(request).getConsultationResponse();

         Assert
               .fail("le test doit échouer car l'uuid n'existe pas dans le SAE");

      } catch (ConsultationAxisFault fault) {

         assertAxisFault(
               fault,
               "Il n'existe aucun document pour l'identifiant d'archivage 'cc26f62e-fd52-42ff-ad83-afc26f96ea91'",
               "ArchiveNonTrouvee", "sae");

      }
   }

   @Test
   public void consultation_failure_SAEConsultationServiceException()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, SaeAccessDeniedAxisFault {

      ConsultParams consultParams = new ConsultParams(UUID
            .fromString("cc4a5ec1-788d-4b41-baa8-d349947865bf"), null);

      EasyMock
            .expect(documentService.consultation(checkConsult(consultParams)))
            .andThrow(new SAEConsultationServiceException(new Exception()));

      EasyMock.replay(documentService);

      try {
         Consultation request = createConsultationResponseType("src/test/resources/request/consultation_success.xml");

         skeleton.consultationSecure(request).getConsultationResponse();

         Assert
               .fail("le test doit échouer à cause de la levée d'une exception de type "
                     + SAEConsultationServiceException.class);

      } catch (ConsultationAxisFault fault) {

         assertAxisFault(fault,
               "Une erreur s'est produite lors de la consultation",
               "ErreurInterneConsultation", "sae");

      }
   }

   @Test
   public void consultation_failure_RuntimeException()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, SaeAccessDeniedAxisFault {

      ConsultParams consultParams = new ConsultParams(UUID
            .fromString("cc4a5ec1-788d-4b41-baa8-d349947865bf"), null);

      EasyMock
            .expect(documentService.consultation(checkConsult(consultParams)))
            .andThrow(new RuntimeException("une runtime exception est levée"));

      EasyMock.replay(documentService);

      try {
         Consultation request = createConsultationResponseType("src/test/resources/request/consultation_success.xml");

         skeleton.consultationSecure(request).getConsultationResponse();

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

   public static <T extends ConsultParams> T checkConsult(T in) {
      EasyMock.reportMatcher(new ConsultParamComparator(in));
      return null;
   }
}
