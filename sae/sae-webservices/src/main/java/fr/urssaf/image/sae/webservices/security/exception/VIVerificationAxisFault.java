package fr.urssaf.image.sae.webservices.security.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.VIVerificationException;

/**
 * Exception levée quand le VI comporte une erreur
 * 
 * 
 */
public class VIVerificationAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

   /**
    * Instanciation de {@link AxisFault#AxisFault(String, QName, Throwable)}
    * 
    * paramètres:
    * <ul>
    * <li>
    * <code>messageText<code>: {@link VIVerificationException#getSoapFaultMessage() }
    * </li>
    * <li><code>faultCode</code>:
    * {@link VIVerificationException#getSoapFaultCode() }</li>
    * <li><code>cause</code>: l'exception elle-même</li>
    * </ul>
    * 
    * @param exception
    *           erreur dans le VI
    */
   public VIVerificationAxisFault(VIVerificationException exception) {

      super(exception.getSoapFaultMessage(), exception.getSoapFaultCode(),
            exception);
   }

}
