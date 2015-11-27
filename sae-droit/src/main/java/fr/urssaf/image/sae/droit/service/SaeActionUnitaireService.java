/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

/**
 * Service de manipulation des Actions Unitaires
 * 
 */
public interface SaeActionUnitaireService {

   /**
    * création d'une action unitaire
    * 
    * @param actionUnitaire
    *           action unitaire à créer
    */
   void createActionUnitaire(ActionUnitaire actionUnitaire);

}
