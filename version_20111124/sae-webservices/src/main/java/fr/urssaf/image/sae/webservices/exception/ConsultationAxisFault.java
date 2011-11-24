package fr.urssaf.image.sae.webservices.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Exception levée dans le service de consultation
 * 
 * 
 */
public class ConsultationAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

   /**
    * Instanciation de {@link AxisFault#AxisFault}
    * 
    * <code>faultCode</code>:
    * <ul>
    * <li><code>namespaceURI</code>: urn:sae:faultcodes</li>
    * <li><code>localPart</code>:ConsultationServiceError</li>
    * <li><code>prefix</code>:sae</li>
    * </ul>
    * 
    * 
    * @param cause
    *           cause de l'exception
    */
   public ConsultationAxisFault(Throwable cause) {

      super("Une erreur s'est produite lors de la consultation",
            SoapFaultCodeFactory.createSoapFaultCode("urn:sae:faultcodes",
                  "ConsultationErreur", "sae"), cause);

   }

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
    * @param message
    *           message de l'exception
    * @param localPart
    *           localPart du code du SOAPFault
    */
   public ConsultationAxisFault(String message, String localPart) {

      super(message, SoapFaultCodeFactory.createSoapFaultCode(
            "urn:sae:faultcodes", localPart, "sae"));

   }

}
