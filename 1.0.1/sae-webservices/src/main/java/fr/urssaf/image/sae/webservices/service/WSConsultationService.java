package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;

/**
 * Service web de consultation du SAE
 * 
 * 
 */
public interface WSConsultationService {

   /**
    * 
    * Service pour l'opération <b>Consultation</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="consultation">
    *    &lt;wsdl:documentation>Service de consultation documentaire du SAE</wsdl:documentation>
    *    ...      
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * La requête correspond à :
    * 
    * <pre>
    *   &lt;xsd:element name="consultation"
    *       type="sae:consultationRequestType"/>
    * </pre>
    * 
    * La réponse correspond à :
    * 
    * <pre>
    * &lt;wsdl:message name="consultationResponseMessage">
    * </pre>
    * 
    * @param request
    *           Requête de consultation du service web <b>Consultation</b>
    * @return Réponse du service web <b>Consultation</b>
    * 
    * @throws ConsultationAxisFault
    *            Une exception est levée lors de la consultation
    */
   ConsultationResponse consultation(Consultation request)
         throws ConsultationAxisFault;
}
