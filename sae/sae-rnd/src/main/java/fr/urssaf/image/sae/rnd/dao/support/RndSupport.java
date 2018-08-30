/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.rnd.dao.support;

import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * TODO (ac75007394) Description du type
 *
 */
public interface RndSupport {

   /**
    * Création d'un RND dans la CF Rnd
    *
    * @param typeDoc
    *           le type de document à ajouter
    * @param clock
    *           Horloge de la création
    */
   void ajouterRnd(TypeDocument typeDoc, long clock);

   /**
    * Récupère le type de document correspondant au code passé en paramètre
    *
    * @param code
    *           le code RND dont on veut le type de document
    * @return le type de document recherché
    */
   TypeDocument getRnd(String code);

}