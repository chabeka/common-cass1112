package fr.urssaf.image.parser_opencsv.webservice.factory;

import java.io.IOException;
import java.net.URL;

import javax.xml.ws.soap.SOAPBinding;

import fr.urssaf.image.parser_opencsv.webservice.model.SaeService;

/**
 * Factory de création du Stub pour appeler le service web SaeService
 */
public class SaeServiceStubFactory {

   private final String endPoint;

   private final AddViHeaderHandlerResolver viHeaderResolver;

   private final static String WSDL_RESOURCE_PATH = "SaeService_WSDL/SaeService.wsdl";

   public SaeServiceStubFactory(final String endPoint, final AddViHeaderHandlerResolver viHeaderResolver) {
      this.endPoint = endPoint;
      this.viHeaderResolver = viHeaderResolver;
   }

   /**
    * Création d'un Stub paramétré avec l'authentification au service web du SAE
    *
    * @return le Stub
    * @throws IOException
    */
   public SaeService createStubAvecAuthentification() throws IOException {

      final URL wsdlURL = SaeServiceStubFactory.class.getClassLoader().getResource(WSDL_RESOURCE_PATH);
      final SaeService saeService = new SaeService(wsdlURL);

      // Ajout du port avec l'url du web service au service
      saeService.addPort(SaeService.SaeServicePort, SOAPBinding.SOAP12HTTP_MTOM_BINDING, endPoint);

      // Ajout d'un Handler pour insérer le VI dans l'entete
      saeService.setHandlerResolver(viHeaderResolver);
      System.out.println("============================== VI");
      System.out.println(viHeaderResolver);
      System.out.println(endPoint);
      return saeService;
   }

   /**
    * Création d'un Stub sans authentification
    *
    * @return le Stub
    * @throws IOException
    */
   public SaeService createStubSansAuthentification() throws IOException {

      final URL wsdlURL = SaeServiceStubFactory.class.getClassLoader().getResource(WSDL_RESOURCE_PATH);
      final SaeService saeService = new SaeService(wsdlURL);

      // Ajout du port avec l'url du web service au service
      saeService.addPort(SaeService.SaeServicePort, SOAPBinding.SOAP12HTTP_MTOM_BINDING, endPoint);

      return saeService;
   }

}
