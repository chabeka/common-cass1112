package fr.urssaf.image.sae.webservices.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Exception qui peut-être levée dans n'importe quelle opération, lorsque l'on
 * détecte que la base de donnée est indisponible, et que l'on ne sait pas sur
 * quelle opération du service web on se trouve (par exemple lorsque le problème
 * se produit dans un aspect joué avant l'entrée dans la méthode de l'opération)<br>
 * <br>
 * Dans le référentiel des SoapFault, elle correspond à sae:ErreurInterne<br>
 */
public class ErreurInterneBddIndispoAxisFault extends AxisFault {

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
   public ErreurInterneBddIndispoAxisFault(Throwable cause) {

      super("La base de données est temporairement inaccessible",
            SoapFaultCodeFactory.createSoapFaultCode("urn:sae:faultcodes",
                  "ErreurInterne", "sae"), cause);

   }

}
