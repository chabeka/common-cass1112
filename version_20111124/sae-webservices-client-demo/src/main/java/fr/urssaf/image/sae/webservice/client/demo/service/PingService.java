package fr.urssaf.image.sae.webservice.client.demo.service;

import java.net.URL;

import org.apache.log4j.Logger;

import fr.urssaf.image.sae.webservice.client.demo.component.DefaultServer;
import fr.urssaf.image.sae.webservice.client.demo.component.ServiceClient;
import fr.urssaf.image.sae.webservice.client.demo.util.ResourceUtils;

/**
 * Client consommateur du Ping
 * 
 * <pre>
 * <u>contenu du message soap</u>:
 * &lt;?xml version='1.0' encoding='UTF-8'?>
 *  &lt;soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">   
 *     &lt;soapenv:Body>      
 *        &lt;ns1:PingRequest xmlns:ns1="http://www.cirtil.fr/saeService" />   
 *     &lt;/soapenv:Body>
 * &lt;/soapenv:Envelope>
 * </pre>
 * 
 * L'URL du web service est configuré avec {@link DefaultServer}<br>
 * Les messages et les réponses soap sont générés par
 * {@link ServiceClient#sendReceive}
 * 
 * 
 */
public class PingService {

   private static final Logger LOG = Logger.getLogger(PingService.class);

   private final URL serverURL;

   private static final String PING_REQUEST = "soap/ping.xml";

   /**
    * instanciation de l'URL par défaut du web service
    */
   public PingService() {

      this.serverURL = DefaultServer.getInstance().getUrl();
   }

   /**
    * Appel du web service 'Ping'<br>
    * <br>
    * Le message soap est configuré dans soap/ping.xml
    * 
    * @return message ping
    */
   public final String ping() {

      // envoie du message soap à partir du chargement du message ping
      ServiceClient client = new ServiceClient("Ping", serverURL);
      return client.sendReceive(ResourceUtils.loadResource(this, PING_REQUEST));

   }

   /**
    * Méthode executable pour le ping <br>
    * <br>
    * Appel de la méthode {@link #ping()}<br>
    * 
    * @param args
    *           pas prise en compte
    */
   public static void main(String[] args) {

      PingService service = new PingService();
      LOG.debug("\n" + service.ping());

   }

}
