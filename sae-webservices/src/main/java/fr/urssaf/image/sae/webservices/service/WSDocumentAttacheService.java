package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.GetDocFormatOrigine;
import fr.cirtil.www.saeservice.GetDocFormatOrigineResponse;
import fr.urssaf.image.sae.webservices.exception.GetDocFormatOrigineAxisFault;

/**
 * Service web de gestion des notes de documents
 * 
 * 
 */
public interface WSDocumentAttacheService {

   /**
    * Service pour l'opération <b>getDocFormatOrigine</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="getDocFormatOrigine" parameterOrder="input">
    *    &lt;wsdl:documentation>Opération de récupération d'un document au format d'origine&lt;/wsdl:documentation>   
    *    ...
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * @param request
    *           Objet contenant l'UUID du document parent
    * @return instance de {@link GetDocFormatOrigineResponse} contenant le
    *         contenu du document attaché
    * @throws GetDocFormatOrigineAxisFault
    *            Une exception est levée lors de la récupération d'un document
    *            attaché
    */
   GetDocFormatOrigineResponse getDocFormatOrigine(GetDocFormatOrigine request)
         throws GetDocFormatOrigineAxisFault;
}
