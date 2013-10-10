package fr.urssaf.image.sae.webservices.security.exception;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;
import fr.urssaf.image.sae.webservices.security.igc.exception.LoadCertifsAndCrlException;

/**
 * Exception levée quand le chargement des certificats des AC racine ou des CRL
 * a échoué
 * 
 * 
 */
public class SaeCertificateAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

   /**
    * Instanciation de {@link AxisFault#AxisFault}
    * 
    * paramètres:
    * <ul>
    * <li>
    * <code>messageText<code>: "Une erreur interne du SAE s'est produite lors du chargement des certificats des AC racine ou des CRL"
    * </li>
    * <li><code>faultCode</code>:
    * <ul>
    * <li><code>namespaceURI</code>: urn:sae:faultcodes</li>
    * <li><code>localPart</code>:CertificateInitializationError</li>
    * <li><code>prefix</code>:sae</li>
    * </ul>
    * </li>
    * <li><code>cause</code>: l'exception elle-même</li>
    * </ul>
    * 
    * @param exception
    *           échec du chargement des AC racine ou des CRL
    */
   public SaeCertificateAxisFault(LoadCertifsAndCrlException exception) {

      super(
            "Une erreur interne du SAE s'est produite lors du chargement des certificats des AC racine ou des CRL",
            SoapFaultCodeFactory.createSoapFaultCode("urn:sae:faultcodes",
                  "CertificateInitializationError", "sae"), exception);
   }

}
