package sae.integration.webservice.factory;

import java.net.URL;
import java.util.Arrays;

import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import sae.integration.webservice.modele.SaeService;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Permet de créer un client d'accès aux services SAE
 */
public class SaeServiceStubFactory {

   static final String VI_LOGIN = "SAE_INTEGRATION_TEST";

   static final String RECHERCHE_DOCUMENTAIRE_PASS = "y0TvbCdu9YJn";

   static final String CIME_PASS = "fIgMbK0ifknz";

   static final int TIMEOUT_IN_MS = 10 * 60 * 1000;

   private SaeServiceStubFactory() {
      // Classe statique
   }

   public static SaeServicePortType getServiceForRechercheDocumentaireGNT(final String url) {
      final AddViHeaderHandlerResolver handler = new AddViHeaderHandlerResolver("RECHERCHE-DOCUMENTAIRE.p12",
                                                                                RECHERCHE_DOCUMENTAIRE_PASS,
                                                                                Arrays.asList("PAGM_RECHERCHE_DOCUMENTAIRE_GNT"),
                                                                                "CS_RECHERCHE_DOCUMENTAIRE",
                                                                                VI_LOGIN);

      return getSaeService(url, handler);
   }

   public static SaeServicePortType getServiceForDevToutesActions(final String url) {
      final AddViHeaderHandlerResolver handler = new AddViHeaderHandlerResolver("RECHERCHE-DOCUMENTAIRE.p12",
                                                                                RECHERCHE_DOCUMENTAIRE_PASS,
                                                                                Arrays.asList("PAGM_TOUTES_ACTIONS"),
                                                                                "CS_DEV_TOUTES_ACTIONS",
                                                                                VI_LOGIN);
      return getSaeService(url, handler);
   }

   public static SaeServicePortType getServiceForCimeGNTCotisant(final String url) {
      final AddViHeaderHandlerResolver handler = new AddViHeaderHandlerResolver("CIME.p12",
                                                                                CIME_PASS,
                                                                                Arrays.asList("PAGM_CIME_GNT_COTISANT"),
                                                                                "CS_CIME",
                                                                                VI_LOGIN);
      return getSaeService(url, handler);
   }

   private static SaeServicePortType getSaeService(final String url, final AddViHeaderHandlerResolver handler) {
      final URL wsdlURL = SaeServiceStubFactory.class.getClassLoader().getResource("SaeService_WSDL/SaeService.wsdl");
      final SaeService saeService = new SaeService(wsdlURL);
      saeService.setHandlerResolver(handler);
      saeService.addPort(SaeService.SaeServicePort, SOAPBinding.SOAP12HTTP_MTOM_BINDING, url);
      final SaeServicePortType port = saeService.getSaeServicePort();
      final Client client = ClientProxy.getClient(port);
      setTimeOut(client);
      return port;
   }

   private static void setTimeOut(final Client client) {

      final HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
      final HTTPClientPolicy policy = httpConduit.getClient();
      // set time to wait for response in milliseconds. zero means unlimited
      policy.setReceiveTimeout(TIMEOUT_IN_MS);

      final ClientImpl clientImpl = (ClientImpl) client;
      clientImpl.setSynchronousTimeout(TIMEOUT_IN_MS);
   }
}
