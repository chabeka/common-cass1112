package fr.urssaf.image.dictao.client.handler;

import java.io.StringWriter;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.rampart.util.Axis2Util;
import org.apache.ws.security.WSSecurityException;
import org.w3c.dom.Document;

public class SoapHandler extends AbstractHandler {

   @Override
   public final InvocationResponse invoke(MessageContext msgCtx)
         throws AxisFault {

      Document doc;
      try {
         doc = Axis2Util
               .getDocumentFromSOAPEnvelope(msgCtx.getEnvelope(), true);
      } catch (WSSecurityException ex) {
         throw new IllegalStateException(ex);
      }
      
      SOAPEnvelope soapEnv = (SOAPEnvelope) doc.getDocumentElement();
      msgCtx.setEnvelope(soapEnv);
      soapEnv.build();

      StringWriter sWriter = new StringWriter();
      try {
         soapEnv.serialize(sWriter);
      } catch (XMLStreamException e) {
         throw new RuntimeException(e);
      }

      System.out.println("Message SOAP de sortie : \r\n" + sWriter.toString());

      return InvocationResponse.CONTINUE;

   }
}
