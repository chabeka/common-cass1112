package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.AjoutNote;
import fr.cirtil.www.saeservice.AjoutNoteResponse;
import fr.urssaf.image.sae.webservices.exception.AjoutNoteAxisFault;

/**
 * Service web de gestion des notes de documents
 * 
 * 
 */
public interface WSNoteService {

   /**
    * 
    * Service pour l'opération <b>AjoutNote</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="ajoutNote">
    *    &lt;wsdl:documentation>Service d'ajout de note à un document</wsdl:documentation>
    *    ...      
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * La requête correspond à :
    * 
    * <pre>
    *   &lt;xsd:element name="ajoutNote"
    *       type="sae:ajouteNoteRequestType"/>
    * </pre>
    * 
    * La réponse correspond à :
    * 
    * <pre>
    * &lt;wsdl:message name="ajoutNoteResponseMessage">
    * </pre>
    * 
    * @param request
    *           Requête d'ajout de note du service web <b>AjoutNote</b>
    * @return Réponse du service web <b>AjoutNote</b>
    * 
    * @throws AjoutNoteAxisFault
    *            Une exception est levée lors de l'ajout d'une note
    */
   AjoutNoteResponse ajoutNote(AjoutNote request) throws AjoutNoteAxisFault;

}
