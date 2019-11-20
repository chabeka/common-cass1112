/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;

/**
 * Interface DAO de {@link JobInstancesByNameCql}<br>
 * Les parametres:<br>
 * <b> JobInstancesByNameCql</b>
 * Type de d'objet contenue dans le registre<br>
 * <b> String</b>
 * le type d'Identifiant de l'objet
 */
public interface IJobInstancesByNameDaoCql extends IGenericIndexDAO<JobInstancesByNameCql, String> {

  /**
   * Supprimer le {@link JobInstancesByNameCql} en fonction de l'id
   * 
   * @param id
   *          l'id de l'instance à supprimer
   */
  public void deleteById(String id);
}
