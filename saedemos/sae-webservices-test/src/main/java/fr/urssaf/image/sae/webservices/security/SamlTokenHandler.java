package fr.urssaf.image.sae.webservices.security;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.rampart.util.Axis2Util;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.message.WSSecHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.vi.service.WebServiceVICreateService;
import fr.urssaf.image.sae.vi.service.WebServiceVIService;
import fr.urssaf.image.sae.webservices.util.Constantes;

/**
 * Handler pour ajouter le jeton SAML 2.0 dans la la balise WS-security du web
 * service
 */
public class SamlTokenHandler extends AbstractHandler {

   private static final Logger LOG = LoggerFactory.getLogger(SamlTokenHandler.class);
   
   private static final String DEFAULT_ISSUER = Constantes.DEFAULT_ISSUER;
   private static final String DEFAULT_PAGM = Constantes.DEFAULT_PAGM;

   private final WebServiceVICreateService viService;

   private String issuer;

   private List<String> pagms;

   private MyKeyStore myKeyStore;

   /**
    * Constructeur
    */
   public SamlTokenHandler(WebServiceVICreateService viService) {
      super();
      this.viService = viService;
      this.issuer = DEFAULT_ISSUER;
      this.pagms = new ArrayList<String>();
      this.pagms.add(DEFAULT_PAGM);
      this.myKeyStore = MyKeystoreProvider.portailImageKeyStore;
   }

   public SamlTokenHandler(WebServiceVICreateService viService, String issuer,
         List<String> pagms) {
      super();
      this.viService = viService;
      this.issuer = issuer;
      this.pagms = pagms;
      this.myKeyStore = MyKeystoreProvider.portailImageKeyStore;
   }
   
   
   public SamlTokenHandler(WebServiceVICreateService viService, String issuer,
         String pagm) {
      super();
      this.viService = viService;
      this.issuer = issuer;
      this.pagms = new ArrayList<String>();
      this.pagms.add(pagm);
      this.myKeyStore = MyKeystoreProvider.portailImageKeyStore;
   }

   public SamlTokenHandler(WebServiceVICreateService viService, String issuer,
         List<String> pagms, MyKeyStore myKeyStore) {
      super();
      this.viService = viService;
      this.issuer = issuer;
      this.pagms = pagms;
      this.myKeyStore = myKeyStore;
   }

   /**
    * crée une balise wss security dans le header du SOAP<br>
    * crée un jeton SAML 2.0 en appelant la méthode
    * {@link WebServiceVIService#creerVIpourServiceWeb}<br>
    * Le jeton SAML est ajouté dans la partie WS-Security situé dans l'entête du
    * SOAP
    * 
    * {@inheritDoc}
    * 
    */
   @Override
   public final InvocationResponse invoke(MessageContext msgCtx)
         throws AxisFault {

      
      // Récupération de l'enveloppe SOAP, requise plusieurs fois ultérieurement
      Document doc;
      try {
         doc = Axis2Util.getDocumentFromSOAPEnvelope(msgCtx
               .getEnvelope(), true);
      } catch (WSSecurityException ex) {
         throw new IllegalStateException(ex);
      }
      SOAPEnvelope soapEnv = (SOAPEnvelope) doc.getDocumentElement();
      msgCtx.setEnvelope(soapEnv);
      soapEnv.build();
      
      // Ajout de l'en-tête WS-Security
      WSSecHeader secHeader = new WSSecHeader(null, false);
      try {
         secHeader.insertSecurityHeader(doc);
      } catch (WSSecurityException e) {
         throw new IllegalStateException(e);
      }
      
      // création du jeton SAML 2.0
      // List<String> roles = AuthenticateUtils.getRoles();
      // if (CollectionUtils.isNotEmpty(roles)) {

      Element token = this.viService.creerVIpourServiceWeb(pagms, issuer, null,
            myKeyStore.getKeystore(), myKeyStore.getAliasClePrivee(),
            myKeyStore.getPassword());

      SOAPHeader header = msgCtx.getEnvelope().getHeader();

      OMElement security = header.getFirstChildWithName(new QName(
            WSConstants.WSSE_NS, "Security"));

      try {

         security.addChild(org.apache.axis2.util.XMLUtils.toOM(token));
         
         // Log du message SOAP de request
         soapEnv.build();
         StringWriter sWriter = new StringWriter();
         soapEnv.serialize(sWriter);
         LOG.debug("Message SOAP de request : \r\n{}", sWriter.toString());
         

      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
      // }

      return InvocationResponse.CONTINUE;
   }
}
