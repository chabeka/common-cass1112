package fr.urssaf.image.sae.webservice.client.demo.service;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.webservice.client.demo.component.DefaultKeystore;
import fr.urssaf.image.sae.webservice.client.demo.component.DefaultServer;
import fr.urssaf.image.sae.webservice.client.demo.component.ServiceClient;
import fr.urssaf.image.sae.webservice.client.demo.security.signature.exception.XmlSignatureException;
import fr.urssaf.image.sae.webservice.client.demo.security.ws.SAML20Service;
import fr.urssaf.image.sae.webservice.client.demo.security.ws.WSSecurityService;
import fr.urssaf.image.sae.webservice.client.demo.util.ResourceUtils;
import fr.urssaf.image.sae.webservice.client.demo.util.StreamUtils;

/**
 * Client consommateur du Ping sécurisé<br>
 * <br>
 * Le message soap est sécurisé avec WS-Security
 * 
 * <pre>
 * <u>contenu du message soap</u>:
 * &lt;?xml version='1.0' encoding='UTF-8'?>
 * &lt;soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">   
 *       &lt;soapenv:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">   
 *        
 *        <i>la partie WS Security est ajouté ici </i> (voir {@link WSSecurityService#createWSSEHeader})
 *       
 *       &lt;/soapenv:Header>   
 *       &lt;soapenv:Body>      
 *          &lt;ns1:PingSecureRequest xmlns:ns1="http://www.cirtil.fr/saeService" />   
 *       &lt;/soapenv:Body>
 * &lt;/soapenv:Envelope>
 * 
 * </pre>
 * 
 * L'URL du web service est configuré avec {@link DefaultServer}<br>
 * Les messages et les réponses soap sont générés par
 * {@link ServiceClient#sendReceive}
 * 
 * 
 */
public class PingSecureService {

   private static final Logger LOG = LoggerFactory
         .getLogger(PingSecureService.class);

   private final URL serverURL;

   private static final String PING_REQUEST = "soap/pingSecure.xml";

   private final WSSecurityService wsService;

   private final SAML20Service assertionService;

   /**
    * instanciation de l'URL par défaut du web service
    */
   public PingSecureService() {

      this.serverURL = DefaultServer.getInstance().getUrl();
      this.wsService = new WSSecurityService();
      this.assertionService = new SAML20Service();

   }

   /**
    * Appel du web service 'PingSecure'<br>
    * <br>
    * Le message soap est configuré dans soap/pingSecure.xml<br>
    * <br>
    * La valeur [wsseSecurity] est remplacé par un entête WS-Security contenant
    * un jeton signé SAML 2.0 avec {@link SAML20Service}<br>
    * <br>
    * La signature du jeton SAML s'appuie sur le keystore par défault (voir
    * {@link DefaultKeystore})<br>
    * <br>
    * 
    * @param role
    *           role du pagm au format :  <i>&lt;droit applicatif>;&lt;périmètre de données><i>
    * @return message du ping sécurisé
    */
   public final String pingSecure(String role) {

      // récupération du keystore par défaut
      KeyStore keystore = DefaultKeystore.getInstance().getKeystore();
      String alias = DefaultKeystore.getInstance().getAlias();
      String password = DefaultKeystore.getInstance().getPassword();

      // instanciation des paramètres du jeton SAML
      DateTime systemDate = new DateTime();
      UUID identifiant = UUID.randomUUID();

      // pour des questions de dérives d'horloges la période de début et de fin
      // de validé du jeton est de 2heures
      DateTime notAfter = systemDate.plusHours(2);
      DateTime notBefore = systemDate.minusHours(2);

      // instanciation du jeton SAML 2.0 signé
      String assertion;
      try {

         assertion = this.assertionService.createAssertion20(role, notAfter,
               notBefore, systemDate, identifiant, keystore, alias, password);
      } catch (XmlSignatureException e) {
         throw new IllegalStateException(e);
      }

      // instanciation de l'entête WS-Security
      String wsseSecurity = this.wsService.createWSSEHeader(this.serverURL,
            "PingSecure", assertion, identifiant);

      // chargement du message pingSecure
      InputStream soapMessage = ResourceUtils.loadResource(this, PING_REQUEST);

      // paramétrage de l'entête
      String[] searchList = new String[] { "[wsseSecurity]" };
      String[] replacementList = new String[] { wsseSecurity };

      String soapMessageWS = StreamUtils.createObject(soapMessage, searchList,
            replacementList);

      // envoie du message soap
      ServiceClient client = new ServiceClient("PingSecure", serverURL);
      return client.sendReceive(soapMessageWS);

   }

   /**
    * Méthode executable pour le ping secure <br>
    * <br>
    * Appel de la méthode {@link #pingSecure(String)}<br>
    * <br>
    * arguments:
    * <ul>
    * <li>arg[0]: role</li>
    * </ul>
    * 
    * @param args
    *           arguments
    */
   public static void main(Object[] args) {

      if (ArrayUtils.isEmpty(args)) {
         throw new IllegalArgumentException("role is required");
      }

      String role = (String) args[0];
      PingSecureService service = new PingSecureService();

      LOG.debug("Response : \n" + service.pingSecure(role));

   }
}
