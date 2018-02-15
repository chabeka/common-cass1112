package fr.urssaf.image.sae.webservices.handler;

import java.io.StringWriter;

import javax.xml.stream.XMLStreamException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;


/**
 * Handler permettant d'intercepter le message SOAP<br>
 * de requête pour la consommation d'un Webservice.
 * 
 *
 */
public class LogWarnSkeletonHandler extends AbstractHandler{

   @Override
   public final InvocationResponse invoke(MessageContext msgCtx) throws AxisFault {
      
      
      try {
         StringWriter sWriter = new StringWriter();
         msgCtx.getEnvelope().serialize(sWriter);
         
         msgCtx.setProperty("soapRequestMessage", sWriter);
         
      } catch (XMLStreamException e) {
         // Exception jamais levée.
      }
      
      return InvocationResponse.CONTINUE;
   }

}
