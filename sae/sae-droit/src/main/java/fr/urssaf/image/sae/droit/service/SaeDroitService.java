/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.LockTimeoutException;
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
    * @throws LockTimeoutException
    *            exception levée si l'accès à Zookeeper est impossible
    */
   void createContratService(ServiceContract serviceContract, List<Pagm> pagms)
         throws LockTimeoutException;

}
