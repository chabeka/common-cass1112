package fr.urssaf.image.sae.integration.ihmweb.saeservice.security;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.axiom.om.OMNode;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.rampart.util.Axis2Util;
import org.apache.ws.security.WSSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Handler pour ajouter le jeton SAML 2.0 dans la la balise WS-security du web
 * service<br>
 * Le handler est ajouté lors de la construction du Stub.
 * 
 */
public class VIHandler extends AbstractHandler {

   private static final Logger LOG = LoggerFactory.getLogger(VIHandler.class);

   private ViService viService;
   
   /**
    * Le nom de la propriété du MessageContext dans laquelle il faut renseigner
    * le type du VI à générer
    */
   public static final String PROP_STYLE_VI = "ViStyle";
   
   /**
    * Le nom de la propriété du MessageContext dans laquelle on renseigne le
    * message SOAP de sortie
    */
   public static final String PROP_MESSAGE_OUT = "messageOut";

   /**
    * Constructeur
    * @param viService le service de génération des VI 
    */
   public VIHandler(ViService viService) {
      super();
      this.viService = viService;
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

      LOG.debug("Début de l'interception du message SOAP avant envoi");

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
      
      
      // Ajout de l'en-tête WS-Security construit dynamiquement selon le type
      // de VI demandé dans le contexte courant (dépend du cas de test)
      LOG.debug("Insertion de l'en-tête WS-Security");
      try {

         // Lecture du type de VI demandé pour le cas de test
         LOG.debug("Lecture du type de VI demandé pour le cas de test");
         ViStyle viStyle = (ViStyle)msgCtx.getProperty(PROP_STYLE_VI);
         LOG.debug("Type demandé : {}", viStyle);
         
         // Appel du service adéquat pour générer le VI
         LOG.debug("Génération du VI");
         String viEtWsse = viService.generationVi(viStyle);
         LOG.debug("VI généré : {}", viEtWsse);
         
         // Sauce technique pour insérer le VI dans le message SOAP
         if (StringUtils.isNotBlank(viEtWsse)) {
            
            LOG.debug("Insertion du VI dans le message SOAP");
            
            SOAPHeader soapHeader = msgCtx.getEnvelope().getHeader();
            
            StringReader strReader = new StringReader(viEtWsse);
            OMNode nodeWsseVi = org.apache.axis2.util.XMLUtils.toOM(strReader);
            
            soapHeader.addChild(nodeWsseVi);
            soapEnv.build();
            
            // VI inséré
            LOG.debug("VI inséré dans le message SOAP");
         
         } else {
            LOG.debug("Aucun VI à insérer");
         }

         // Concernant le message SOAP de request :
         //  - on le log avec Logback
         //  - on le stocke dans le contexte Axis2, pour affichage ultérieur
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
