package fr.urssaf.image.sae.webservices.handler;

import java.io.StringWriter;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handler permettant d'intercepter le message SOAP<br>
 * de requête pour la consommation d'un Webservice.
 * 
 *
 */
public class LogHandler extends AbstractHandler {

   /**
    * LOGGER
    */
   private static final Logger LOG = LoggerFactory.getLogger(LogHandler.class);

   public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
      logServiceInformation("Begin", msgContext);
      StringWriter sWriter = new StringWriter();
      sWriter.append(Long.toString(System.currentTimeMillis()));
      msgContext.setProperty("soapRequestTimeBegin", sWriter);

      return InvocationResponse.CONTINUE;
   }

   @Override
   public void flowComplete(MessageContext msgContext) {
      super.flowComplete(msgContext);
      StringWriter sWriter = (StringWriter) msgContext
            .getProperty("soapRequestTimeBegin");
      long timeBegin = Long.valueOf(sWriter.toString());
      long timeEnd = System.currentTimeMillis();
      long diff = (timeEnd - timeBegin) / 1000;
      logServiceInformation("End [" + Long.toString(diff) + "s]", msgContext);
   }

   @SuppressWarnings("static-access")
   private void logServiceInformation(String prefix, MessageContext msgContext) {
      LOG.info(prefix + " - messageContextID::"
            + msgContext.getLogCorrelationID());
   }

}
