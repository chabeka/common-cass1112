package fr.urssaf.image.sae.webservices.security.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Exception quand il n'y pas de VI dans le header du SOAP
 * 
 * 
 */
public class VIEmptyAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

   /**
    * Instanciation de {@link AxisFault#AxisFault(String, QName)}
    * 
    * paramètres:
    * <ul>
    * <li><code>messageText<code>: <code>'La référence au jeton de sécurité est introuvable'<code></li>
    * <li><code>faultCode</code>: <code>'wsse:SecurityTokenUnavailable'</code></li>
    * </ul>
    */
   public VIEmptyAxisFault() {
      super("La référence au jeton de sécurité est introuvable",
            SoapFaultCodeFactory
                  .createWsseSoapFaultCode("SecurityTokenUnavailable"));

   }
}
