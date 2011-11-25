package fr.urssaf.image.sae.webservices.util;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.util.XMLUtils;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.rampart.util.Axis2Util;
import org.apache.ws.security.WSSecurityException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Classe utilitaire pour le framework <a
 * href="http://ws.apache.org/axiom/userguide/userguide.html">Axiom</a>
 * 
 * 
 */
public final class AxiomUtils {

   private AxiomUtils() {

   }

   /**
    * Methode d'encapsulation de
    * {@link Axis2Util#getSOAPEnvelopeFromDOMDocument}
    * 
    * @param msgSoap
    *           message soap
    * @return Objet SOAP
    */
   public static SOAPEnvelope parse(String msgSoap) {

      try {
         return Axis2Util.getSOAPEnvelopeFromDOMDocument(XMLUtils
               .newDocument(msgSoap), false);
      } catch (WSSecurityException e) {
         throw new IllegalStateException(e);
      } catch (ParserConfigurationException e) {
         throw new IllegalStateException(e);
      } catch (SAXException e) {
         throw new IllegalStateException(e);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }

   }

   /**
    * Méthode d'encapsulation de {@link Axis2Util#getDocumentFromSOAPEnvelope}
    * 
    * @param client
    *           client soap
    * @return objet document de la réponse soap
    * @throws AxisFault
    *            exception sur le message soap
    */
   public static Document loadDocumentResponse(ServiceClient client)
         throws AxisFault {

      MessageContext response = client.getLastOperationContext()
            .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
      SOAPEnvelope soapEnvelope = response.getEnvelope();

      try {

         return Axis2Util.getDocumentFromSOAPEnvelope(soapEnvelope, false);

      } catch (WSSecurityException e) {
         throw new IllegalStateException(e);
      }
   }
}
