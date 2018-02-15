package fr.urssaf.image.sae.droit.service;

import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.Pagmf;


/**
 * Interface pour la manipulation du {@link Pagmf}.
 *
 */
public interface SaePagmfService {

   /**
    * Crée un nouveau {@link Pagmf}.
    * 
    * @param pagmf
    *       le {@link Pagmf} à créer.
    */
   void addPagmf(Pagmf pagmf);
   
   /**
    * Supprimer un {@link Pagmf}.
    * 
    * @param codePagmf
    *          le code du {@link Pagmf} à supprimer.
    */
   void deletePagmf(String codePagmf);
   
   /**
    * Récupère les informations relatives à un {@link Pagmf} donné.
    * 
    * @param codePagmf
    *          code correspondant à un {@link Pagmf}. - paramètre obligatoire.
    * @return le {@link Pagmf} correspondant.
    */
   Pagmf getPagmf(String codePagmf);
   
   /**
    * Récupère tous les {@link Pagmf} de la base.
    * 
    * @return tous les {@link Pagmf}.   
    */
   List<Pagmf> getAllPagmf();
   
}
