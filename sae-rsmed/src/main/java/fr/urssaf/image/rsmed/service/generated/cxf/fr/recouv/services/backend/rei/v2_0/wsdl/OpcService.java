package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.3.7
 * 2020-11-05T10:03:08.212+01:00
 * Generated source version: 3.3.7
 *
 */
@WebServiceClient(name = "OpcService",
                  wsdlLocation = "file:/C:/projets/java/sae/sae_trunk/sae-rsmed/src/main/resources/ws/OpsService.wsdl",
                  targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
public class OpcService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.recouv.fr/services/backend/REI/v2.0/wsdl", "OpcService");
    public final static QName OpcServiceSOAP = new QName("http://www.recouv.fr/services/backend/REI/v2.0/wsdl", "OpcServiceSOAP");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/projets/java/sae/sae_trunk/sae-rsmed/src/main/resources/ws/OpsService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(OpcService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/projets/java/sae/sae_trunk/sae-rsmed/src/main/resources/ws/OpsService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public OpcService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public OpcService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public OpcService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public OpcService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public OpcService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public OpcService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns Opc
     */
    @WebEndpoint(name = "OpcServiceSOAP")
    public Opc getOpcServiceSOAP() {
        return super.getPort(OpcServiceSOAP, Opc.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Opc
     */
    @WebEndpoint(name = "OpcServiceSOAP")
    public Opc getOpcServiceSOAP(WebServiceFeature... features) {
        return super.getPort(OpcServiceSOAP, Opc.class, features);
    }

}