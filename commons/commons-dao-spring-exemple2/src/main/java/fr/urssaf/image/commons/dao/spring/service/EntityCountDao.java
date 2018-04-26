package fr.urssaf.image.commons.dao.spring.service;


/**
 * Fonctions classiques de comptage d'entités persistantes
 * 
 */
public interface EntityCountDao {

   /**
    * Compte le nombre d'entités persistées
    * 
    * @return nombre d'entités persistées
    */
   int count();
    
}
