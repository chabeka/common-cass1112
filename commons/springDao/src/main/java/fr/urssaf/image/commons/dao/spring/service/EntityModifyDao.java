
package fr.urssaf.image.commons.dao.spring.service;


/**
 * @author Bertrand BARAULT
 * 
 * Fonctions typiques d'une classe persistante M
 *
 * @param <M> classe persistante
 */
public interface EntityModifyDao<P> {

    /**
     * Ins�re un objet persistant
     * 
     * @param obj objet persistant � ins�rer
     */
    public void save(P obj);

    /**
     * Supprime un objet persistant
     * 
     * @param obj objet persistant � supprimer
     */
    public void delete(P obj);

    /**
     * Met � jour un objet persistant
     * 
     * @param obj objet persistant � mettre � jour
     */
    public void update(P obj);

}
