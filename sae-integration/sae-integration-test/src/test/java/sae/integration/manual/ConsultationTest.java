
package sae.integration.manual;

import java.util.UUID;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.Environments;
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
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());

      final ConsultationMTOMRequestType request = new ConsultationMTOMRequestType();
      final String docId = UUID.randomUUID().toString();
      request.setIdArchive(docId);
      try {
         LOGGER.info("Lancement de la consultation du document");
         final ConsultationMTOMResponseType response = service.consultationMTOM(request);
         LOGGER.info("Taille du document : {}", response.getContenu().length);
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
}