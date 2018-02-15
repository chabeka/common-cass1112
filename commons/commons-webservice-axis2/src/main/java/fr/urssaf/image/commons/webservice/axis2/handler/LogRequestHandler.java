package fr.urssaf.image.commons.webservice.axis2.handler;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.io.output.XmlStreamWriter;
import org.apache.log4j.Logger;

public class LogRequestHandler extends AbstractHandler {

   private static final Logger LOG = Logger.getLogger(LogRequestHandler.class);

   @Override
   public final InvocationResponse invoke(MessageContext msgCtx)
         throws AxisFault {

      TransformerFactory factory = TransformerFactory.newInstance();

      try {
         Transformer transformer = factory.newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty(
               "{http://xml.apache.org/xslt}indent-amount", "4");

         ByteArrayOutputStream out = new ByteArrayOutputStream();
         transformer.transform(msgCtx.getEnvelope().getSAXSource(true),
               new StreamResult(new XmlStreamWriter(out, "UTF-8")));

         LOG.debug("\n" + out.toString());

      } catch (TransformerException e) {
         throw new IllegalStateException(e);
      }

      return InvocationResponse.CONTINUE;
   }

}
