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

   /**
    * Vérifie si le prmd existe ou non
    * 
    * @param code
    *           code du PRMD
    * @return <b>true</b> si le PRMD existe, <b>false</b> sinon
    */
   boolean prmdExists(String code);
   
   /**
    * Récupération d'un prmd 
    *  
    * @param code
    *           code du PRMD
    * @return {@link Prmd} trouvé
    */
   Prmd getPrmd(String code);
   
}
