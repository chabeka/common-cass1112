
package sae.integration.manual;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.webservice.modele.TransfertRequestType;
import sae.integration.webservice.modele.TransfertResponseType;


public class TransfertTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(TransfertTest.class);

   @Test
   /**
    * Lance un transfert de masse, pour rattrapage
    */
   public void transfertWattTest() throws Exception {
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.FRONTAL_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(Environments.GNS_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_CSPP.getUrl());
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForWattGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final TransfertRequestType request = new TransfertRequestType();
      request.setUuid("eb1e14c6-61fe-4a2e-9bb7-d5897d83ad35");
      try {
         LOGGER.info("Lancement du transfert");
         final TransfertResponseType response = service.transfert(request);
         LOGGER.info("Transfert terminé");

      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }
   }
}