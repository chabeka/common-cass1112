
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
   public void pingSecureIntClientTest() throws Exception {

      // Intégration client CESU
      pingSecureGNS("http://hwi31intgnspajeboappli1.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31intgnspajeboappli2.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31intgntpajeboappli1.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31intgntpajeboappli2.gidn.recouv:8080/ged/services/SaeService/");

      // Intégration client PAJE
      pingSecureGNS("http://hwi31intgnscesuboappli1.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31intgnscesuboappli2.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31intgntcesuboappli1.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31intgntcesuboappli2.gidn.recouv:8080/ged/services/SaeService/");

      // Intégration client Normal
      pingSecureGNS("http://hwi31intgnsboappli1.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31intgnsboappli2.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31intgntv6boappli1.gidn.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31intgntv6boappli2.gidn.recouv:8080/ged/services/SaeService/");
   }

   @Test
   public void pingSecureSDITTest() throws Exception {

      // Intégration DITS GNS
      pingSecureGNS("http://hwi31ginc1gnscesuboappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31ginc1gnscesuboappli2.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31ginc1gnspajeboappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31ginc1gnspajeboappli2.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31ginc1gnsboappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31ginc1gnsboappli2.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31ginc2gnsboappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31ginc2gnsboappli2.cer31.recouv:8080/ged/services/SaeService/");

      // Intégration DITS GNT
      pingSecureGNT("http://hwi31ginc1gntcesuboappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31ginc1gntcesuboappli2.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31ginc1gntpajeboappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31ginc1gntpajeboappli2.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31ginc1gntv6boappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31ginc1gntv6boappli2.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31ginc2gntv6boappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31ginc2gntv6boappli2.cer31.recouv:8080/ged/services/SaeService/");
   }

   @Test
   public void pingSecureGIVNTest() throws Exception {

      // Intégration GIVN GNS
      pingSecureGNS("http://hwi31givngnsl4v6boappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31givngnsl4v6boappli2.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31givngnsl1boappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNS("http://hwi31givngnsl1boappli2.cer31.recouv:8080/ged/services/SaeService/");
      // Intégration GIVN GNT
      pingSecureGNT("http://hwi31givngntl1boappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31givngntl1boappli2.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31givngntl4v6boappli1.cer31.recouv:8080/ged/services/SaeService/");
      pingSecureGNT("http://hwi31givngntl4v6boappli2.cer31.recouv:8080/ged/services/SaeService/");
   }

   private void pingSecureGNS(final String url) {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(url);
      pingSecure(url, service);
   }

   private void pingSecureGNT(final String url) {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(url);
      pingSecure(url, service);
   }

   private void pingSecure(final String url, final SaeServicePortType service) {
      final PingSecureRequest request = new PingSecureRequest();

      try {
         LOGGER.info("Lancement du ping sur " + url);
         final PingSecureResponse response = service.pingSecure(request);
         LOGGER.info("Résultat : {}", response.getPingString());
      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
         // throw e;
      }
   }

   @Test
   public void pingSecureInjecteurTest() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForInjecteur("http://gnspajeint1v6.gidn.recouv/ged/services/SaeService");

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
