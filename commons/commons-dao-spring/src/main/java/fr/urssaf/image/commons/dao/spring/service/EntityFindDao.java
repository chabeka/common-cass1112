
package fr.urssaf.image.commons.dao.spring.service;

import java.util.List;

/**
 * @author Bertrand BARAULT
 * 
 * Fonctions typiques d'une classe persistante M
 *
 * @param <M> classe persistante
 */
public interface EntityFindDao<M> {

    public List<M> find();
    
    /**
     * Renvoie tous les objets persistants tri�s
     * 
     * @param order nom de la colonne � trier
     * @return liste des objets persistants
     */
    public List<M> find(String order);
    
    /**
     * Renvoie tous les objets persistants tri�s
     * 
     * @param order nom de la colonne � trier
     * @param inverse sens du tri
     * @return liste des objets persistants
     */
    public List<M> find(String order,boolean inverse);
    
    /**
     * 
     * Renvoie tous les objets persistants tri�s et filtr�s sur un interval
     * 
     * @param firstResult index du premier objet
     * @param maxResult nombre de r�sultat
     * @param order nom de la colonne � trier
     * @param inverse sens du tri
     * @return liste des objets persistants
     */
    public List<M> find(int firstResult, int maxResult, String order, boolean inverse);

    
}
