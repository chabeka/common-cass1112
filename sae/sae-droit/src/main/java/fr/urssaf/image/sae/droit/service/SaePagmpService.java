/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.exception.PagmpNotFoundException;

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

   /**
    * Modification d'un PAGMa Le PAGMp doit exister On ne peut modifier que la
    * description et le PRMD
    * 
    * @param pagmp
    *           PAGMp à modifier
    * @throws PagmpNotFoundException
    *            Exception levée si le PAGMp n'existe pas
    */
   void modifierPagmp(Pagmp pagmp) throws PagmpNotFoundException;
   
   /**
    * Teste l'existence en base du PAGMp
    * @param pagmp le pagmp à tester
    * @return true si le pagmp existe
    */
   boolean isPagmpExiste(Pagmp pagmp);

}
