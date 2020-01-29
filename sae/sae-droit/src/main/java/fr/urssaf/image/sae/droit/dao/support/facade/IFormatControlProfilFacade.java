/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.dao.support.facade;

import java.util.List;

import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;

/**
 * (AC75095351) Interface pour Support facade: FormatControlProfil et ActionUnitaire
 */
public interface IFormatControlProfilFacade <T>{

  public void create(final T entity);
  public T find(final String code);
  public List<T> findAll();

  public void delete(final String id) throws FormatControlProfilNotFoundException;
}
