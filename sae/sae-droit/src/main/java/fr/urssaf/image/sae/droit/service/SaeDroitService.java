/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.model.ServiceContractDatas;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;

/**
 * Service de manipulation des droits du SAE
 * 
 */
public interface SaeDroitService {

   /**
    * Transformation d'une liste de PAGM en une liste de Droit de SAE
    * 
    * @param idClient
    *           code de l'application cliente du service
    * @param pagms
    *           liste des PAGM
    * @return la liste des droits du SAE résultatnt de la transformation des
    *         PAGM passés en paramètre
    * @throws ContratServiceNotFoundException
    *            exception levée si le contrat de service n'est pas trouvé
    * @throws PagmNotFoundException
    *            exception levée si un PAGM fourni n'est pas rattaché au contrat
    *            de service
    */
   SaeDroits loadSaeDroits(String idClient, List<String> pagms)
         throws ContratServiceNotFoundException, PagmNotFoundException;

   /**
    * Création d'un nouveau contrat de service
    * 
    * @param serviceContract
    *           propriétés du contrat de service à créer
    * @param pagms
    *           les PAGM liés au contrat de service à créer
    */
   void createContratService(ServiceContract serviceContract, List<Pagm> pagms);

   /**
    * Vérifie si le contrat de service existe ou non
    * 
    * @param idClient
    *           code Application cliente du service
    * @return <b>true</b> si le contrat existe, <b>false</b> sinon
    */
   boolean contratServiceExists(String idClient);

   /**
    * Récupère le contrat de service identifié
    * 
    * @param idClient
    *           code Application cliente du service
    * @return le contrat de service
    */
   ServiceContract getServiceContract(String idClient);

   /**
    * Référence le PAGM pour le contrat de service avec l'identifiant donné
    * 
    * @param idContratService
    *           identifiant du contrat de service
    * @param pagm
    *           pagm à référencer
    */
   void addPagmContratService(String idContratService, Pagm pagm);

   /**
    * Retourne la liste des PAGM d'un contrat de service
    * 
    * @param idContratService
    *           l'identifiant du contrat de service
    * @return la liste des PAGM du contrat de service
    */
   List<Pagm> getListePagm(String idContratService);
   
   /**
    * Méthode permettant de récupérer N contrats de services
    * @param maxResult nombre maximum de contrat de service à renvoyer
    * @return Liste de {@link ServiceContract}
    */
   List<ServiceContract> findAllContractService(int maxResult);
   
   /**
    * Retourne un contrat de service complet avec tous ses informations
    * @param id code client du contrat de service
    * @return {@link ServiceContractDatas}
    */
   ServiceContractDatas getFullContratService(String id);

}
