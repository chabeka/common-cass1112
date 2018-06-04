/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql;

import java.util.Iterator;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;

/**
 * TODO (AC75095028) Description du type
 */
public interface IGenericIndexCqlDao<T, ID> extends IGenericDAO<T, ID> {

  /**
   * Retourne tous les index crée avant cette date
   * 
   * @param id
   * @return
   */
  Iterator<T> findByDateCreatedBeforeId(ID id);

  /**
   * Retourne tous les index créé après cette date
   * 
   * @param id
   * @return
   */
  Iterator<T> findByDateCreatedAfterId(ID id);

  /**
   * Retourne tous les index créé dans l'interval des dates debut et fin
   * 
   * @param dateStar
   * @param dateEnd
   * @param reversed
   * @param limit
   * @return
   */
  Iterator<T> findByDateInterval(String dateStar, String dateEnd, final boolean reversed, Integer limit);
}
