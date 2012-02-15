package sae.client.demo.webservice.security;

import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyStore;
import java.util.UUID;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.log4j.Logger;
import org.apache.rampart.util.Axis2Util;
import org.joda.time.DateTime;
import org.w3c.dom.Document;

import sae.client.demo.webservice.security.signature.DefaultKeystore;
import sae.client.demo.webservice.security.signature.exception.XmlSignatureException;
import sae.client.demo.webservice.security.ws.SAML20Service;
import sae.client.demo.webservice.security.ws.WSSecurityService;



/**
 * Handler pour ajouter le Vecteur d'Identification dans l'en-tête SOAP<br>.
 * <br>
 * Ce handler est branché dans le fichier <code>axis2.xml</code>
 * 
 */
public class VIHandler extends AbstractHandler {

   
   private static final Logger LOG = Logger.getLogger(VIHandler.class);
   
   
   /**
    * Création d'une balise WS-Security dans le header du SOAP<br>
    * <br>
    * Insertion du VI dans cet balise WS-Security
    *  
    * {@inheritDoc}
    * 
    */
   @Override
   public final InvocationResponse invoke(MessageContext msgCtx)
         throws AxisFault {

      LOG.debug("Début de l'interception du message SOAP avant envoi");
      
      // Ajout de l'en-tête WS-Security chargé depuis un fichier de ressource XML
      LOG.debug("Insertion de l'en-tête WS-Security");
      try {
         
         
         // Génération de l'en-tête wsse
         String wsse = genererEnTeteWsse();
         
         // Insertion du VI dans l'en-tête SOAP
         
         Document doc = Axis2Util.getDocumentFromSOAPEnvelope(
               msgCtx.getEnvelope(), 
               true);
         
         msgCtx.setEnvelope((SOAPEnvelope) doc.getDocumentElement());
         
         SOAPHeader soapHeader = msgCtx.getEnvelope().getHeader();
         
         soapHeader.addChild(
               org.apache.axis2.util.XMLUtils.toOM(
                     new StringReader(wsse)));
         
         soapHeader.build();
         
         StringWriter sWriter = new StringWriter(); 
         msgCtx.getEnvelope().serialize(sWriter);
         
         // Trace
         LOG.debug(sWriter.toString());

         
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
      
      // fin
      LOG.debug("Fin de l'interception du message SOAP avant envoi");
      return InvocationResponse.CONTINUE;
      
   }
   
   
   private String genererEnTeteWsse() {
      
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

      // Génération du VI
      String assertion;
      try {

         SAML20Service assertionService = new SAML20Service();
         
         assertion = assertionService.createAssertion20(
               "ROLE_TOUS;FULL", 
               notAfter,
               notBefore, 
               systemDate, 
               identifiant, 
               keystore, 
               alias, 
               password);
         
      } catch (XmlSignatureException e) {
         throw new IllegalStateException(e);
      }
      
      
      // Génération de l'en-tête WS-Security
      WSSecurityService wsService = new WSSecurityService() ;
      String wsseSecurity = wsService.createWSSEHeader(
            assertion, identifiant);

      
      // Renvoie l'en-tête wsse
      return wsseSecurity;
      
      
   }
   
}
