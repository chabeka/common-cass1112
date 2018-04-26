package fr.urssaf.image.sae.webservices.security.exception;

import org.apache.axis2.AxisFault;
import org.springframework.security.access.AccessDeniedException;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Exception levée quand les droits requis pour l'exécution d'une méthode ne
 * sont pas présent dans le contexte de sécurité
 * 
 * 
 */
public class SaeAccessDeniedAxisFault extends AxisFault {

   private static final long serialVersionUID = 1L;

   /**
    * Instanciation de {@link AxisFault#AxisFault}
    * 
    * paramètres:
    * <ul>
    * <li>
    * <code>messageText<code>: "Les droits présents dans le vecteur d'identification sont insuffisants pour effectuer l'action demandée"
    * </li>
    * <li><code>faultCode</code>:
    * <ul>
    * <li><code>namespaceURI</code>: urn:sae:faultcodes</li>
    * <li><code>localPart</code>:DroitsInsuffisants</li>
    * <li><code>prefix</code>:sae</li>
    * </ul>
    * </li>
    * <li><code>cause</code>: l'exception elle-même</li>
    * </ul>
    * 
    * @param exception
    *           autorisation insuffisante
    */
   public SaeAccessDeniedAxisFault(AccessDeniedException exception) {

      super(
            "Les droits présents dans le vecteur d'identification sont insuffisants pour effectuer l'action demandée",
            SoapFaultCodeFactory.createSoapFaultCode("urn:sae:faultcodes",
                  "DroitsInsuffisants", "sae"), exception);
   }

}
