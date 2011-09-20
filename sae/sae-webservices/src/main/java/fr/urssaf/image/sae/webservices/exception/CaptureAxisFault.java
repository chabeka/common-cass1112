package fr.urssaf.image.sae.webservices.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Exception levée dans de le service de capture
 * 
 * 
 */
public class CaptureAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param localPart
    * @param cause
    */

   /**
    * Instanciation de {@link AxisFault#AxisFault}
    * 
    * <code>faultCode</code>:
    * <ul>
    * <li><code>namespaceURI</code>: urn:sae:faultcodes</li>
    * <li><code>localPart</code>:<code>localPart</code></li>
    * <li><code>prefix</code>:sae</li>
    * </ul>
    * 
    * @param localPart
    *           localPart du code du SOAPFault
    * @param cause
    *           exception levée qui génère la SOAPFault
    */
   public CaptureAxisFault(String localPart, Throwable cause) {

      super("Une erreur interne à l'application est survenue dans la capture.",
            SoapFaultCodeFactory.createSoapFaultCode("urn:sae:faultcodes",
                  localPart, "sae"), cause);

   }

   /**
    * 
    * Instanciation de {@link AxisFault#AxisFault}
    * 
    * <code>faultCode</code>:
    * <ul>
    * <li><code>namespaceURI</code>: urn:sae:faultcodes</li>
    * <li><code>localPart</code>:<code>localPart</code></li>
    * <li><code>prefix</code>:sae</li>
    * </ul>
    * 
    * @param localPart
    *           localPart du code du SOAPFault
    * @param message
    *           message de l'exception
    */
   public CaptureAxisFault(String localPart, String message) {

      super(message, SoapFaultCodeFactory.createSoapFaultCode(
            "urn:sae:faultcodes", localPart, "sae"));

   }

}
