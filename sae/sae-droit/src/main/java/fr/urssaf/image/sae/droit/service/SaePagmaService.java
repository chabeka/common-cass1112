/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import fr.urssaf.image.sae.droit.dao.model.Pagma;

/**
 * Service de manipulation des Pagma
 * 
 */
public interface SaePagmaService {

   /**
    * Création d'un PAGMa
    * 
    * @param pagma
    *           PAGMa à créer
    */
   void createPagma(Pagma pagma);

}
