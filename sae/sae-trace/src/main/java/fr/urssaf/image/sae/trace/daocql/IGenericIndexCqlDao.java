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

  Iterator<T> findByDateCreatedBefore(ID id);

  Iterator<T> findByDateCreatedAfter(ID id);

  Iterator<T> findByDateInterval(String dateStar, String dateEnd, final boolean reversed, Integer limit);
}
