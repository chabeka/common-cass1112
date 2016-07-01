package fr.urssaf.image.sae.webservices.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Exception levée dans de le service de récupération des états des traitements
 * de masse
 * 
 * 
 */
public class EtatTraitementsMasseAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

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
    *           exception levée qui génère la SOAPFault
    */
   public EtatTraitementsMasseAxisFault(String localPart, String message,
         Throwable cause) {

      super(message, SoapFaultCodeFactory.createSoapFaultCode(
            "urn:sae:faultcodes", localPart, "sae"), cause);

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
   public EtatTraitementsMasseAxisFault(String localPart, String message) {

      super(message, SoapFaultCodeFactory.createSoapFaultCode(
            "urn:sae:faultcodes", localPart, "sae"));

   }

   /**
    * Instanciation de {@link AxisFault#AxisFault}
    * 
    * @param cause
    *           cause de l'exception
    */
   public EtatTraitementsMasseAxisFault(Throwable cause) {

      super(
            "Une erreur interne à l'application est survenue lors de la récupération des états des traitements de masse.",
            SoapFaultCodeFactory.createSoapFaultCode("urn:sae:faultcodes",
                  "ErreurInterneEtatTraitementsMasse", "sae"), cause);

   }

}