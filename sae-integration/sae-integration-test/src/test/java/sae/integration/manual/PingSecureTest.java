
package sae.integration.manual;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.PingSecureRequest;
import sae.integration.webservice.modele.PingSecureResponse;
import sae.integration.webservice.modele.SaeServicePortType;


public class PingSecureTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(PingSecureTest.class);

   @Test
   public void pingSecureSaturneTest() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForSaturneGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final PingSecureRequest request = new PingSecureRequest();

      try {
         LOGGER.info("Lancement du pring");
         final PingSecureResponse response = service.pingSecure(request);
         LOGGER.info("Résultat : {}", response.getPingString());
      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }
   }
}