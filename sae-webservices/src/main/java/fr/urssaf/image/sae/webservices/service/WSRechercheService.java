package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;


/**
 * Interface du service web de Recherche du SAE. 
 * 
 */
public interface WSRechercheService {

   /**
    * Methode recherche du service Web du SAE
    * 
    * Cette methode retourne un objet contenant une liste de type ResultatRechercheType qui contient une liste de<br>
    *        MetaDonnees et un id archivage. 
    * 
    * 
    * @param request
    *        un objet contenant les critères de recherche
    * @throws RechercheAxis2Fault
    *        Une exception est levée lors de la recherche
    * @return RechercheResponse
    *        objet retourné
    */
    RechercheResponse search(Recherche request) throws RechercheAxis2Fault;
   
}
