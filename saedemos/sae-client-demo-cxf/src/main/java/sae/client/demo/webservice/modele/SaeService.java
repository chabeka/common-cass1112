package sae.client.demo.webservice.modele;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 2.6.1
 * 2019-02-01T17:32:14.198+01:00
 * Generated source version: 2.6.1
 */
@WebServiceClient(name = "SaeService",
                  wsdlLocation = "src/test/resources/SaeService_WSDL/SaeService.wsdl",
                  targetNamespace = "http://www.cirtil.fr/saeService")
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SaeService extends Service {

   public final static URL WSDL_LOCATION;

   public final static QName SERVICE = new QName("http://www.cirtil.fr/saeService", "SaeService");

   public final static QName SaeServicePort = new QName("http://www.cirtil.fr/saeService", "SaeServicePort");
   static {
      URL url = null;
      try {
         url = new URL("src/test/resources/SaeService_WSDL/SaeService.wsdl");
      }
      catch (final MalformedURLException e) {
         java.util.logging.Logger.getLogger(SaeService.class.getName())
                                 .log(java.util.logging.Level.INFO,
                                      "Can not initialize the default wsdl from {0}",
                                      "src/test/resources/SaeService_WSDL/SaeService.wsdl");
      }
      WSDL_LOCATION = url;
   }

   public SaeService(final URL wsdlLocation) {
      super(wsdlLocation, SERVICE);
   }

   public SaeService(final URL wsdlLocation, final QName serviceName) {
      super(wsdlLocation, serviceName);
   }

   public SaeService() {
      super(WSDL_LOCATION, SERVICE);
   }

   // This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
   // API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
   // compliant code instead.
   public SaeService(final WebServiceFeature... features) {
      super(WSDL_LOCATION, SERVICE, features);
   }

   // This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
   // API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
   // compliant code instead.
   public SaeService(final URL wsdlLocation, final WebServiceFeature... features) {
      super(wsdlLocation, SERVICE, features);
   }

   // This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
   // API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
   // compliant code instead.
   public SaeService(final URL wsdlLocation, final QName serviceName, final WebServiceFeature... features) {
      super(wsdlLocation, serviceName, features);
   }

   /**
    * @return
    *         returns SaeServicePortType
    */
   @WebEndpoint(name = "SaeServicePort")
   public SaeServicePortType getSaeServicePort() {
      return super.getPort(SaeServicePort, SaeServicePortType.class);
   }

   /**
    * @param features
    *           A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in the <code>features</code> parameter will have their default values.
    * @return
    *         returns SaeServicePortType
    */
   @WebEndpoint(name = "SaeServicePort")
   public SaeServicePortType getSaeServicePort(final WebServiceFeature... features) {
      return super.getPort(SaeServicePort, SaeServicePortType.class, features);
   }

}
