/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.Date;
import java.util.Iterator;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;

/**
 * TODO (AC75095028) Description du type
 */
public interface IGenericIndexCqlDao<T, ID> extends IGenericDAO<T, ID> {

  Iterator<T> findByDateCreatedBefore(ID id);

  Iterator<T> findByDateCreatedAfter(ID id);

  Iterator<T> findByDateInterval(Date dateStar, Date dateEnd);
}
