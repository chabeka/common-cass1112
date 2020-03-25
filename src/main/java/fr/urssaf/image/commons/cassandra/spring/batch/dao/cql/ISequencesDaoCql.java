/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;

/**
 * Interface DAO de {@link SequencesCql}<br>
 * Les parmatres:<br>
 * <b> SequencesCql</b>
 * Type de d'objet contenue dans le registre<br>
 * <b> String</b>
 * le type d'Identifiant de l'objet
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
