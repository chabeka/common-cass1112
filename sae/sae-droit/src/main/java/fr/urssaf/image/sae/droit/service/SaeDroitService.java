/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroitsEtFormat;
import fr.urssaf.image.sae.droit.model.SaeContratService;
import fr.urssaf.image.sae.droit.model.SaePagm;

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
    * @throws ContratServiceNotFoundException
    *            exception levée si le contrat de service n'est pas trouvé
    * @throws FormatControlProfilNotFoundException
    *            formatControlProfil inexistant
    */
   // SaeDroits loadSaeDroits(String idClient, List<String> pagms)
   // throws ContratServiceNotFoundException, PagmNotFoundException;
   // void loadSaeDroits(String idClient, List<String> pagms, VIContenuExtrait
   // viContenu)
   // throws ContratServiceNotFoundException;
   SaeDroitsEtFormat loadSaeDroits(String idClient, List<String> pagms)
         throws ContratServiceNotFoundException,
         FormatControlProfilNotFoundException;

   /**
    * Création d'un nouveau contrat de service (création dans
    * DroitContratService, DroitPagm, DroitPagma, DroitPagmp)
    * 
    * @param serviceContract
    *           propriétés du contrat de service à créer
    * @param listeSaePagm
    *           les PAGM liés au contrat de service à créer
    */
   void createContratService(ServiceContract serviceContract,
         List<SaePagm> listeSaePagm);

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
    * Méthode permettant de récupérer N contrats de services
    * 
    * @param maxResult
    *           nombre maximum de contrat de service à renvoyer
    * @return Liste de {@link ServiceContract}
    */
   List<ServiceContract> findAllContractService(int maxResult);

   /**
    * Méthode permettant de récupérer la liste des codes clients de tous les CS
    * 
    * @param maxResult
    *           nombre maximum de contrat de service à renvoyer
    * @return La liste des codes clients
    */
   List<String> findAllCodeClientCs(int maxResult);

   /**
    * Méthode permettant de récupérer N contrat de service complet
    * 
    * @param maxResult
    *           nombre maximum de contrat de service à renvoyer
    * @return Liste de {@link SaeContratService}
    */
   List<SaeContratService> findAllSaeContractService(int maxResult);

   /**
    * Retourne un contrat de service complet avec tous ses informations
    * 
    * @param ident
    *           code client du contrat de service
    * @return {@link SaeContratService}
    */
   SaeContratService getFullContratService(String ident);

   /**
    * Retourne la liste des PAGM d'un contrat de service
    * 
    * @param idContratService
    *           l'identifiant du contrat de service
    * @return la liste des PAGM du contrat de service
    */
   List<SaePagm> getListeSaePagm(String idContratService);

   /**
    * Ajout d'un PAGM à un contrat de service. Cette fonctionnalité créer une
    * nouvelle ligne dans DroitPagm ainsi que dans DroitPagma et DroitPagmp
    * 
    * @param idContratService
    *           Identifiant du contrat de service auquel appartient le PAGM
    * @param saePagm
    *           PAGM à ajouter
    */
   void ajouterPagmContratService(String idContratService, SaePagm saePagm);

   /**
    * Suppression d'un PAGM d'un contrat de service. Cette fonctionnalité
    * supprime les lignes dans DroitPagm ainsi que dans DroitPagma et DroitPagmp
    * 
    * @param idContratService
    *           Identifiant du contrat de service auquel appartient le PAGM
    * @param codePagm
    *           Le code du PAGM à supprimer
    */
   void supprimerPagmContratService(String idContratService, String codePagm);

   /**
    * Modification d'un PAGM d'un contrat de service donné. Cette fonctionnalité
    * permet de modifier les PAGM, PAGMa et PAGMp. Elle supprime et crée un
    * nouveau PAGM.
    * 
    * @param idContratService
    *           Identifiant du contrat de service auquel appartient le PAGM
    * @param saePagm
    *           PAGM à modifier
    */
   void modifierPagmContratService(String idContratService, SaePagm saePagm);

}
