
/*
 * 
 */

package fr.urssaf.image.commons.webservice.exemple.rpc.encoded.modele;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.6
 * Mon May 31 18:51:15 CEST 2010
 * Generated source version: 2.2.6
 * 
 */


@WebServiceClient(name = "DocServiceRpcEncoded", 
                  wsdlLocation = "http://localhost:9999/DocServiceRpcEncoded?WSDL",
                  targetNamespace = "http://encoded.rpc.service.exemple.spring.webservice.commons.image.urssaf.fr/") 
public class DocServiceRpcEncoded extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://encoded.rpc.service.exemple.spring.webservice.commons.image.urssaf.fr/", "DocServiceRpcEncoded");
    public final static QName DocumentServicePort = new QName("http://encoded.rpc.service.exemple.spring.webservice.commons.image.urssaf.fr/", "DocumentServicePort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:9999/DocServiceRpcEncoded?WSDL");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from http://localhost:9999/DocServiceRpcEncoded?WSDL");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public DocServiceRpcEncoded(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public DocServiceRpcEncoded(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DocServiceRpcEncoded() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns DocService
     */
    @WebEndpoint(name = "DocumentServicePort")
    public DocService getDocumentServicePort() {
        return super.getPort(DocumentServicePort, DocService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DocService
     */
    @WebEndpoint(name = "DocumentServicePort")
    public DocService getDocumentServicePort(WebServiceFeature... features) {
        return super.getPort(DocumentServicePort, DocService.class, features);
    }

}
