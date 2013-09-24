package sae.client.demo.webservice.security;

import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyStore;
import java.util.UUID;

import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import sae.client.demo.webservice.security.signature.DefaultKeystore;
import sae.client.demo.webservice.security.signature.exception.XmlSignatureException;
import sae.client.demo.webservice.security.ws.SAML20Service;
import sae.client.demo.webservice.security.ws.WSSecurityService;

/**
 * Handler Axis2 pour ajouter le Vecteur d'Identification dans l'en-tête SOAP.<br>
 * <br>
 * Ce handler peut être branché dans la phase "MessageOut".
 * 
 */
public class VIHandler extends AbstractHandler {

   private String issuer = "SaeDemos";

   private String pagm = "ROLE_TOUS;FULL";

   /**
    * Constructeur par défaut
    */
   public VIHandler() {

   }

   /**
    * Constructeur
    * 
    * @param issuer
    *           l'issuer à spécifier dans le VI
    * @param pagm
    *           le pagm à spécifier dans le VI
    */
   public VIHandler(String issuer, String pagm) {
      if (StringUtils.isNotBlank(issuer)) {
         this.issuer = issuer;
      }
      if (StringUtils.isNotBlank(pagm)) {
         this.pagm = pagm;
      }
   }

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

      // Ajout de l'en-tête WS-Security chargé depuis un fichier de ressource
      // XML
      try {

         // Génération de l'en-tête wsse
         String wsse = genererEnTeteWsse();

         SOAPHeader soapHeader = msgCtx.getEnvelope().getHeader();

         soapHeader.addChild(org.apache.axis2.util.XMLUtils
               .toOM(new StringReader(wsse)));

         soapHeader.build();

         StringWriter sWriter = new StringWriter();
         msgCtx.getEnvelope().serialize(sWriter);

      } catch (Exception e) {
         throw new IllegalStateException(e);
      }

      // fin
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

         assertion = assertionService.createAssertion20(issuer, pagm, notAfter,
               notBefore, systemDate, identifiant, keystore, alias, password);

      } catch (XmlSignatureException e) {
         throw new IllegalStateException(e);
      }

      // Génération de l'en-tête WS-Security
      WSSecurityService wsService = new WSSecurityService();
      String wsseSecurity = wsService.createWSSEHeader(assertion, identifiant);

      // Renvoie l'en-tête wsse
      return wsseSecurity;

   }

}
