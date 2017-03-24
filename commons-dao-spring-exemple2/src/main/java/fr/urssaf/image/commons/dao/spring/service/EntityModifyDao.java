
package fr.urssaf.image.commons.dao.spring.service;


/**
 * Fonctions classiques de DML pour des entitées persistantes (insert, update, delete)
 * 
 * @param <P> classe de l'entité persistée
 */
public interface EntityModifyDao<P> {

    /**
     * Persiste une entité
     * 
     * @param obj entité à persister
     */
    void save(P obj);
    

    /**
     * Supprime une entité de la persistance
     * 
     * @param obj entité à supprimer
     */
    void delete(P obj);

    
    /**
     * Met à jour une entité dans la persistance
     * 
     * @param obj entité à mettre à jour
     */
    void update(P obj);

}
