/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.droit.dao.support.facade;

import java.util.List;

import me.prettyprint.hector.api.mutation.Mutator;

/**
 * (AC75095351) Interface pour Support facade : Pagm, Pagma, Pagmf, Pagmp, Prmd
 */
public interface IPagmsSupportFacade <T>{
  /**
   * Création d'une entité
   * 
   * @param entity
   */
  public void create(final T entity);

  /**
   * @param code
   * @return
   */
  public T find(final String code);

  /**
   * @return
   */
  public List<T> findAll();

  /**
   * @param id
   */
  public void delete(final String id);

  /**
   * @param entity
   * @param mutator
   */
  public void create(final T entity, final Mutator<String> mutator);

  /**
   * @param code
   * @param mutator
   */
  public void delete(final String code, final Mutator<String> mutator);
}
