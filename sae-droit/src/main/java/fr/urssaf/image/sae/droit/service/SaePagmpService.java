/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import fr.urssaf.image.sae.droit.dao.model.Pagmp;

/**
 * Service de manipulation des Pagmp
 * 
 */
public interface SaePagmpService {

   /**
    * Création d'un PAGMa
    * 
    * @param pagmp
    *           PAGMp à créer
    */
   void createPagmp(Pagmp pagmp);

}
