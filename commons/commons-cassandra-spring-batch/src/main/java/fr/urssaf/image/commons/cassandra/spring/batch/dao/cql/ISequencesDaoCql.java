/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;

/**
 * TODO (AC75095028) Description du type
 */
public interface ISequencesDaoCql extends IGenericDAO<SequencesCql, String> {

  /**
   * Recupère le timestamp sur la colonne en fonction de l'identifiant de la colonne
   * 
   * @param id
   *          identifiant de la colonne
   * @return le timestamp de la colonne
   */
  public long getColunmClock(String id);
}
