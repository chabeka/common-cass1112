package fr.urssaf.image.sae.webservices.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMException;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;

/**
 * Classe de manipulation pour Axis2
 * 
 * 
 */
public final class Axis2Utils {

   private Axis2Utils() {

   }

   /**
    * appelle {@link MessageContext#setEnvelope} avec le parametre xml
    * 
    * @param ctx
    *           contexte du message
    * @param xml
    *           chaine de caractère du message soap
    */
   public static void initMessageContext(MessageContext ctx, String xml) {

      try {
         InputStream input = new FileInputStream(xml);

         StAXSOAPModelBuilder stax = new StAXSOAPModelBuilder(StAXUtils
               .createXMLStreamReader(input));

         ctx.setEnvelope(stax.getSOAPEnvelope());
      } catch (FileNotFoundException e) {
         throw new IllegalStateException(e);
      } catch (XMLStreamException e) {
         throw new IllegalStateException(e);
      } catch (AxisFault e) {
         throw new IllegalStateException(e);
      } catch (OMException e) {
         throw new IllegalStateException(e);
      }

   }

}
