/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.dao.support.facade;

import java.util.List;

/**
 * (AC75095351) Interface pour Support facade ContratService
 */
public interface IContratServiceSupportFacade <T>{
  /**
   * Création de l'entity
   * 
   * @param entity
   */
  public void create(final T entity);

  /**
   * Recherche de l'entity par code
   * 
   * @param code
   * @return
   */
  public T find(final String code);

  /**
   * Recherche de toutes les entités
   * 
   * @return
   */
  public List<T> findAll();

  /**
   * Suppression de l'entité qui a pour id le paramètre
   * 
   * @param id
   */
  public void delete(final String id);

  /**
   * Recherche de toutes les entités avec une valeur maximale à lire
   * 
   * @param maxKeysToRead
   * @return
   */
  public List<T> findAll(final int maxKeysToRead);
}