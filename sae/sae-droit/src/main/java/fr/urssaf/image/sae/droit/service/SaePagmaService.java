/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.exception.PagmaNotFoundException;

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

   /**
    * Modification d'un PAGMa 
    * Le PAGMa doit exister On ne peut modifier que la
    * liste des actions unitaires mais pas le code du PAGMa
    * 
    * @param pagma
    *           PAGMa à modifier
    */
   void modifierPagma(Pagma pagma) throws PagmaNotFoundException;

   /**
    * Teste l'existence en base du PAGMa
    * @param pagma le pagma à tester
    * @return true si le pagma existe
    */
   boolean isPagmaExiste(Pagma pagma);
}
