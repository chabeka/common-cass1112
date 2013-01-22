/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Service permettant de réaliser les traitements de purge
 * 
 */
public interface PurgeService {

   /**
    * Réalise la purge d'un registre donné
    * 
    * @param typePurge
    *           la purge à lancer
    */
   void purgerRegistre(PurgeType typePurge);

}
