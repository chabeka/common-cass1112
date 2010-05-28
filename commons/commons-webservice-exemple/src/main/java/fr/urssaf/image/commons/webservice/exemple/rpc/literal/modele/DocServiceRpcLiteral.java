
/*
 * 
 */

package fr.urssaf.image.commons.webservice.exemple.rpc.literal.modele;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.8
 * Fri May 28 16:49:14 CEST 2010
 * Generated source version: 2.2.8
 * 
 */


@WebServiceClient(name = "DocServiceRpcLiteral", 
                  wsdlLocation = "http://localhost:9999/DocServiceRpcLiteral?WSDL",
                  targetNamespace = "http://literal.rpc.service.exemple.spring.webservice.commons.image.urssaf.fr/") 
public class DocServiceRpcLiteral extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://literal.rpc.service.exemple.spring.webservice.commons.image.urssaf.fr/", "DocServiceRpcLiteral");
    public final static QName DocumentServicePort = new QName("http://literal.rpc.service.exemple.spring.webservice.commons.image.urssaf.fr/", "DocumentServicePort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:9999/DocServiceRpcLiteral?WSDL");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from http://localhost:9999/DocServiceRpcLiteral?WSDL");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public DocServiceRpcLiteral(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public DocServiceRpcLiteral(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DocServiceRpcLiteral() {
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
