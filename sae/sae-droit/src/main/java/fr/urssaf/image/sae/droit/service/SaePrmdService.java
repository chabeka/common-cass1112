/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import fr.urssaf.image.sae.droit.dao.model.Prmd;

/**
 * Service de manipulation des Prmd
 * 
 */
public interface SaePrmdService {

   /**
    * Création d'un PAGMa
    * 
    * @param prmd
    *           PRMD à créer
    */
   void createPrmd(Prmd prmd);

}
