
package sae.integration.manual;

import java.util.UUID;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environments;
import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ConsultationMTOMRequestType;
import sae.integration.webservice.modele.ConsultationMTOMResponseType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Test une consultation d'un document, sur environnement d'intégration client GNT
 */
public class ConsultationTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ConsultationTest.class);

   @Test
   /**
    * Consultation d'un document inexistant
    */
   public void consultationDocInexistantTest() throws Exception {
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_CSPP.getUrl());
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForRechercheDocumentaireGNT("http://hwi31intgntpajeboweb1.gidn.recouv/ged/services/SaeService/");

      final ConsultationMTOMRequestType request = new ConsultationMTOMRequestType();
      final String docId = UUID.randomUUID().toString();
      request.setIdArchive(docId);
      try {
         LOGGER.info("Lancement de la consultation du document");
         final ConsultationMTOMResponseType response = service.consultationMTOM(request);
         LOGGER.info("Taille du document : {}", IOUtils.readBytesFromStream(response.getContenu().getInputStream()).length);
      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Exception reçue : {}", e.getMessage());
         if (e.getMessage().contains("Il n'existe aucun document pour l'identifiant")) {
            LOGGER.info("Ok, on a reçu la bonne exception");
            return;
         }
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
         Assert.fail("Mauvaise exception reçue");
      }
      Assert.fail("On attendait une exception");
   }

   @Test
   public void consultationDocExistantTest() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.FRONTAL_INT_CLIENT.getUrl());

      final ConsultationMTOMRequestType request = new ConsultationMTOMRequestType();
      final String docId = "49afb93a-28d7-4ab8-88a9-722cd47b29f4";
      request.setIdArchive(docId);
      try {
         LOGGER.info("Lancement de la consultation du document");
         final ConsultationMTOMResponseType response = service.consultationMTOM(request);
         LOGGER.info("Taille du document : {}", IOUtils.readBytesFromStream(response.getContenu().getInputStream()).length);
      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Exception reçue : {}", e.getMessage());
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
      }
   }

   @Test
   public void consultationDocExistantTest2() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(Environments.FRONTAL_PREPROD.getUrl());

      final ConsultationMTOMRequestType request = new ConsultationMTOMRequestType();
      final String docId = "EB2B53A2-9273-48D9-BFF7-C799EA323769";
      request.setIdArchive(docId);
      try {
         LOGGER.info("Lancement de la consultation du document");
         final ConsultationMTOMResponseType response = service.consultationMTOM(request);
         LOGGER.info("Taille du document : {}", IOUtils.readBytesFromStream(response.getContenu().getInputStream()).length);
      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Exception reçue : {}", e.getMessage());
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
      }
   }

}