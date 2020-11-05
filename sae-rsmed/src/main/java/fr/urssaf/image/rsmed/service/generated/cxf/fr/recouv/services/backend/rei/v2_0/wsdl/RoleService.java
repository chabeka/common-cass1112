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
 * 2020-11-05T10:03:07.997+01:00
 * Generated source version: 3.3.7
 *
 */
@WebServiceClient(name = "RoleService",
                  wsdlLocation = "file:/C:/projets/java/sae/sae_trunk/sae-rsmed/src/main/resources/ws/OpsService.wsdl",
                  targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
public class RoleService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.recouv.fr/services/backend/REI/v2.0/wsdl", "RoleService");
    public final static QName RoleServiceSOAP = new QName("http://www.recouv.fr/services/backend/REI/v2.0/wsdl", "RoleServiceSOAP");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/projets/java/sae/sae_trunk/sae-rsmed/src/main/resources/ws/OpsService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RoleService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/projets/java/sae/sae_trunk/sae-rsmed/src/main/resources/ws/OpsService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RoleService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RoleService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RoleService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public RoleService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public RoleService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public RoleService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns Role
     */
    @WebEndpoint(name = "RoleServiceSOAP")
    public Role getRoleServiceSOAP() {
        return super.getPort(RoleServiceSOAP, Role.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Role
     */
    @WebEndpoint(name = "RoleServiceSOAP")
    public Role getRoleServiceSOAP(WebServiceFeature... features) {
        return super.getPort(RoleServiceSOAP, Role.class, features);
    }

}
