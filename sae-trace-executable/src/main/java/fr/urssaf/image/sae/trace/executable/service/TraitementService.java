/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.service;

import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Service permettant de réaliser les traitements
 * 
 */
public interface TraitementService {

   /**
    * Réalise la purge d'un registre
    * 
    * @param purgeType
    *           purge à lancer
    */
   void purgerRegistre(PurgeType purgeType);

}
