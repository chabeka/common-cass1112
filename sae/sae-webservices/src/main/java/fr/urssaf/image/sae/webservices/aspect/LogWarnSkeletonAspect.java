package fr.urssaf.image.sae.webservices.aspect;

import java.io.StringWriter;

import org.apache.axis2.context.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.webservices.skeleton.SaeServiceSkeleton;
import fr.urssaf.image.sae.webservices.support.TracesSupport;

/**
 * Aspect permettant de logger le message SOAP de requete lorsqu'une exception
 * est levée lors de la consommation d'un webservice.<br>
 * <br>
 * Invoque également le dispatcheur de traces.
 * 
 */
@Component
public class LogWarnSkeletonAspect {

   private static final Logger LOG = LoggerFactory
         .getLogger(SaeServiceSkeleton.class);

   @Autowired
   private TracesSupport tracesSupport;

   /**
    * Méthode permettant de logger les WARN
    * 
    * @param exception
    *           l'exception levée
    */
   public final void logWarn(Exception exception) {

      MessageContext msgCtx = MessageContext.getCurrentMessageContext();

      StringWriter sWriter = (StringWriter) msgCtx
            .getProperty("soapRequestMessage");

      String soapRequest = sWriter.toString();

      LOG.warn(soapRequest);

      tracesSupport.traceEchecWs(msgCtx, soapRequest, exception);

      BuildOrClearMDCAspect.clearLogContext();

   }

}
