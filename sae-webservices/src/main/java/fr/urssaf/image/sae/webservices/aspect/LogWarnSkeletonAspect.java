package fr.urssaf.image.sae.webservices.aspect;

import java.io.StringWriter;

import org.apache.axis2.context.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.webservices.skeleton.SaeServiceSkeleton;

/**
 * Aspect permettant de logger <br>
 * le message SOAP de requete lorsqu'une exception est levée<br>
 * lors de la consommation d'un webservice
 * 
 *
 */

@Component
public class LogWarnSkeletonAspect {

   private static final Logger LOG = LoggerFactory.getLogger(SaeServiceSkeleton.class);
   
   /**
    * Méthode permettant de logger les WARN
    */
   public final void logWarn() {
      
      MessageContext msgCtx = MessageContext.getCurrentMessageContext();
      
      StringWriter sWriter = (StringWriter) msgCtx.getProperty("soapRequestMessage");
      
      LOG.warn(sWriter.toString());
      
      BuildOrClearMDCAspect.clearLogContext();
   }
   
   
}
