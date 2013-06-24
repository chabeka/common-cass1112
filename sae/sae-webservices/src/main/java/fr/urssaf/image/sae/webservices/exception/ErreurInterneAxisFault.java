package fr.urssaf.image.sae.webservices.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Exception qui peut-être levée dans n'importe quelle opération, lorsque l'on
 * ne peut déterminer la cause du problème (erreur Runtime par exemple), et que
 * l'on ne sait pas sur quelle opération du service web on se trouve (par
 * exemple lorsque le problème se produit dans un aspect joué avant l'entrée
 * dans la méthode de l'opération)<br>
 * <br>
 * Dans le référentiel des SoapFault, elle correspond à sae:ErreurInterne
 */
public class ErreurInterneAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

   /**
    * Instanciation de {@link AxisFault#AxisFault}<br>
    * <br>
    * <code>faultCode</code>:
    * <ul>
    * <li><code>namespaceURI</code>: urn:sae:faultcodes</li>
    * <li><code>localPart</code>: ErreurInterne</li>
    * <li><code>prefix</code>: sae</li>
    * </ul>
    * 
    * 
    * @param cause
    *           cause de l'exception
    */
   public ErreurInterneAxisFault(Throwable cause) {

      super("Une erreur interne à l'application est survenue.",
            SoapFaultCodeFactory.createSoapFaultCode("urn:sae:faultcodes",
                  "ErreurInterne", "sae"), cause);

   }

}
