package fr.urssaf.image.sae.integration.ihmweb.saeservice.security;

import java.io.StringWriter;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.rampart.util.Axis2Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

/**
 * Handler pour ajouter le jeton SAML 2.0 dans la la balise WS-security du web
 * service<br>
 * Le handler est configuré dans le fichier <code>axis2.xml</code>
 * 
 */
public class VIHandler extends AbstractHandler {

   private static final Logger LOG = LoggerFactory.getLogger(VIHandler.class);

   /**
    * Le nom de la propriété du MessageContext dans laquelle il faut renseigner
    * le chemin du fichier de ressource XML contenant le vecteur d'identifiant à
    * utiliser
    */
   public static final String PROP_FICHIER_VI = "ficRessourceVi";

   /**
    * Le nom de la propriété du MessageContext dans laquelle on renseigne le
    * message SOAP de sortie
    */
   public static final String PROP_MESSAGE_OUT = "messageOut";

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

      // Ajout de l'en-tête WS-Security chargé depuis un fichier de ressource
      // XML
      LOG.debug("Insertion de l'en-tête WS-Security");
      try {

         String ficRessourceVi = (String) msgCtx.getProperty(PROP_FICHIER_VI);
         LOG.debug("Insertion du VI du fichier " + ficRessourceVi);

         Document doc = Axis2Util.getDocumentFromSOAPEnvelope(msgCtx
               .getEnvelope(), true);

         SOAPEnvelope soapEnv = (SOAPEnvelope) doc.getDocumentElement();
         msgCtx.setEnvelope(soapEnv);
         soapEnv.build();

         SOAPHeader soapHeader = msgCtx.getEnvelope().getHeader();
         soapHeader.addChild(org.apache.axis2.util.XMLUtils
               .toOM(new ClassPathResource(ficRessourceVi).getInputStream()));
         soapEnv.build();

         StringWriter sWriter = new StringWriter();
         soapEnv.serialize(sWriter);
         LOG.debug(sWriter.toString());

         msgCtx.getConfigurationContext().setProperty(PROP_MESSAGE_OUT,
               sWriter.toString());

      } catch (Exception e) {
         throw new IllegalStateException(e);
      }

      // fin
      LOG.debug("Fin de l'interception du message SOAP avant envoi");
      return InvocationResponse.CONTINUE;

   }
}
