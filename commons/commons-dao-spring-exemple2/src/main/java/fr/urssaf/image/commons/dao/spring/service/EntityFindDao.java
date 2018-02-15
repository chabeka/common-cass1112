package fr.urssaf.image.commons.dao.spring.service;

import java.util.List;

/**
 * Fonctions standards de lecture de la persistance :
 * <ul>
 *    <li>lecture complète, non ordonnée</li>
 *    <li>lecture complète, ordonnée ascendante ou descendante sur une propriété</li>
 *    <li>lecture complète, ordonnée ascendante ou descendante sur une propriété,
 *    et en ne récupérant qu'une page de résultat</li>
 * </ul>
 *
 * @param <P> classe de l'entité persistée
 */
public interface EntityFindDao<P> {

   
   /**
    * Lecture intégrale de la persistance, pas de tri
    * 
    * @return le contenu complet de la table
    */
   List<P> find();
   
    
   /**
    * Lecture intégrale de la persistance, triée par ordre croissant sur une propriété
    * 
    * @param order nom de la propriété sur laquelle il faut trier
    * @return liste des objets répondants aux critères
    */
   List<P> find(String order);
   
    
   /**
    * Lecture intégrale de la persistance, triée par ordre croissant ou décroissant
    * sur une propriété
    * 
    * @param order nom de la propriété sur laquelle il faut trier
    * @param inverse true s'il faut trier de manière décroissante
    * @return liste des objets répondants aux critères
    */
   List<P> find(String order,boolean inverse);
    
   
   /**
    * Lecture d'une partie de la persistance
    * 
    * @param firstResult index de la première entité à lire de la persistance 
    * (les index commencent à 0)
    * @param maxResult nombre maximal de résultat
    * @param order nom de la propriété sur laquelle il faut trier
    * @param inverse true s'il faut trier de manière décroissante
    * @return liste des objets répondants aux critères
    */
   List<P> find(int firstResult, int maxResult, String order, boolean inverse);
    
}
