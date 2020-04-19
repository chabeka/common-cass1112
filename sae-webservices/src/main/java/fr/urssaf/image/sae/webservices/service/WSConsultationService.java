package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationAffichable;
import fr.cirtil.www.saeservice.ConsultationAffichableResponse;
import fr.cirtil.www.saeservice.ConsultationGNTGNS;
import fr.cirtil.www.saeservice.ConsultationGNTGNSResponse;
import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
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

   /**
    * 
    * Service pour l'opération <b>ConsultationMTOM</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="consultationMTOM">
    *    &lt;wsdl:documentation>Service de consultation documentaire avec optimisation MTOM du SAE</wsdl:documentation>
    *    ...      
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * La requête correspond à :
    * 
    * <pre>
    *   &lt;xsd:element name="consultationMTOM"
    *       type="sae:consultationMTOMRequestType"/>
    * </pre>
    * 
    * La réponse correspond à :
    * 
    * <pre>
    * &lt;wsdl:message name="consultationMTOMResponseMessage">
    * </pre>
    * 
    * @param request
    *           Requête de consultationMTOM du service web
    *           <b>ConsultationMTOM</b>
    * @return Réponse du service web <b>ConsultationMTOM</b>
    * 
    * @throws ConsultationAxisFault
    *            Une exception est levée lors de la consultationMTOM
    */
   ConsultationMTOMResponse consultationMTOM(ConsultationMTOM request)
         throws ConsultationAxisFault;

   /**
    * 
    * Service pour l'opération <b>ConsultationAffichable</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="consultationAffichable">
    *    &lt;wsdl:documentation>Service de consultation d'un document dans un format affichable</wsdl:documentation>
    *    ...      
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * La requête correspond à :
    * 
    * <pre>
    *   &lt;xsd:element name="consultationAffichable"
    *       type="sae:ConsultationAffichableRequestType"/>
    * </pre>
    * 
    * La réponse correspond à :
    * 
    * <pre>
    * &lt;wsdl:message name="ConsultationAffichableResponseMessage">
    * </pre>
    * 
    * @param request
    *           Requête de ConsultationAffichable du service web
    *           <b>ConsultationAffichable</b>
    * @return Réponse du service web <b>ConsultationAffichable</b>
    * 
    * @throws ConsultationAxisFault
    *            Une exception est levée lors de la ConsultationAffichable
    */
   ConsultationAffichableResponse consultationAffichable(
         ConsultationAffichable request) throws ConsultationAxisFault;
   
   
   ConsultationGNTGNSResponse consultationGNTGNS(ConsultationGNTGNS request)
         throws ConsultationAxisFault, SearchingServiceEx, ConnectionServiceEx, SAEConsultationServiceException, UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx, AxisFault, SAEConsultationAffichableParametrageException, RemoteException;

}
