package fr.urssaf.image.sae.integration.ihmweb.saeservice.security;

import java.io.StringWriter;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler pour ajouter le jeton SAML 2.0 dans la la balise WS-security du web
 * service<br>
 * Le handler est configuré dans le fichier <code>axis2.xml</code>
 * 
 */
public class LogInMessageHandler extends AbstractHandler {

   private static final Logger LOG = LoggerFactory
         .getLogger(LogInMessageHandler.class);

   /**
    * Le nom de la propriété du MessageContext dans laquelle on renseigne le
    * message SOAP en entrée
    */
   public static final String PROP_MESSAGE_IN = "messageIn";

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

      LOG.debug("Début de l'interception du message SOAP en réception");

      // Ajout de l'en-tête WS-Security chargé depuis un fichier de ressource
      // XML
      try {

         StringWriter sWriter = new StringWriter();
         msgCtx.getEnvelope().serialize(sWriter);
         LOG.debug(sWriter.toString());

         msgCtx.getRootContext().setProperty(PROP_MESSAGE_IN,
               sWriter.toString());

      } catch (Exception e) {
         throw new IllegalStateException(e);
      }

      // fin
      LOG.debug("Fin de l'interception du message SOAP en réception");
      return InvocationResponse.CONTINUE;

   }
}
