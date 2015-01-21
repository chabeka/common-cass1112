package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheNbRes;
import fr.cirtil.www.saeservice.RechercheNbResResponse;
import fr.cirtil.www.saeservice.RechercheParIterateur;
import fr.cirtil.www.saeservice.RechercheParIterateurResponse;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;

/**
 * Interface du service web de Recherche du SAE. 
 * 
 */
public interface WSRechercheService {

   /**
    * Methode recherche du service Web du SAE
    * 
    * Cette methode retourne un objet {@link RechercheResponse} contenant une
    * liste {@link ResultatRechercheType}. <br>
    * 
    * Chaque objet ResultatRechercheType contient: 
    *    
    * <li>un id archivage</li>
    * <li>une liste de MetaDonnees</li>
    * 
    * @param request
    *        un objet contenant les critères de recherche
    * @throws RechercheAxis2Fault
    *        Une exception est levée lors de la recherche
    * @return RechercheResponse
    *        objet retourné
    */
    RechercheResponse search(Recherche request) throws RechercheAxis2Fault;
    
    
    /**
     * Methode recherche du service Web du SAE avec retour du nombre de documents
     * 
     * Cette methode retourne un objet {@link RechercheNbResResponse} contenant une liste 
     * {@link ResultatRechercheType}. <br>
     * 
     * Chaque objet ResultatRechercheType contient: 
     * 
     * <li>un id archivage</li>
     * <li>une liste de MetaDonnees</li>
     * 
     * @param request
     *        un objet contenant les critères de recherche
     * @throws RechercheAxis2Fault
     *        Une exception est levée lors de la recherche
     * @return RechercheResponse
     *        objet retourné
     */
    RechercheNbResResponse searchWithNbRes(RechercheNbRes request) throws RechercheAxis2Fault;

   /**
    * Service réalisant une recherche de documents par itérateur
    * 
    * @param request
    *           Objet contenant les critères de recherche
    * @return Instance de RechercheParIterateurResponse contenant le résultat de
    *         la recherche
    * @throws RechercheAxis2Fault
    *            Une exception est levée lors de la recherche
    */
   RechercheParIterateurResponse rechercheParIterateur(
         RechercheParIterateur request) throws RechercheAxis2Fault;
}
