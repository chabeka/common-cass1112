package fr.urssaf.image.sae.webservices.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Exception levée dans le service d'ajout de note
 * 
 * 
 */
public class AjoutNoteAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

   /**
    * Instanciation de {@link AxisFault#AxisFault}
    * 
    * <code>faultCode</code>:
    * <ul>
    * <li><code>namespaceURI</code>: urn:sae:faultcodes</li>
    * <li><code>localPart</code>:AjoutNoteServiceError</li>
    * <li><code>prefix</code>:sae</li>
    * </ul>
    * 
    * 
    * @param cause
    *           cause de l'exception
    */
   public AjoutNoteAxisFault(Throwable cause) {

      super("Une erreur interne à l'application est survenue lors de l'ajout d'une note.",
            SoapFaultCodeFactory.createSoapFaultCode("urn:sae:faultcodes",
                  "ErreurInterneAjoutNote", "sae"), cause);

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
    * @param localPart
    *           localPart du code du SOAPFault
    * @param message
    *           message de l'exception
    */
   public AjoutNoteAxisFault(String localPart, String message) {

      super(message, SoapFaultCodeFactory.createSoapFaultCode(
            "urn:sae:faultcodes", localPart, "sae"));

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
    * @param localPart
    *           localPart du code du SOAPFault
    * @param message
    *           message de l'exception
    * @param cause
    *           cause de l'exception
    */
   public AjoutNoteAxisFault(String localPart, String message, Throwable cause) {

      super(message, SoapFaultCodeFactory.createSoapFaultCode(
            "urn:sae:faultcodes", localPart, "sae"), cause);

   }

}
