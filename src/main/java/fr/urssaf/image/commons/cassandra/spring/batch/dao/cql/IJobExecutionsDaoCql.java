/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.util.Optional;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;

/**
 * Interface DAO de {@link JobExecutionsCql}<br>
 * Les parametres:<br>
 * <b> JobExecutionsCql</b>
 * Type de d'objet contenue dans le registre<br>
 * <b> String</b>
 * le type d'Identifiant de l'objet
 */
public interface IJobExecutionsDaoCql extends IGenericIndexDAO<JobExecutionsCql, String> {

  /**
   * Recherche par colonne indexée
   *
   * @param id
   *          la colonne indexée
   * @return l'optional de {@link JobExecutionsCql}
   */
  public Optional<JobExecutionsCql> findByJobExecutionId(final Long id);
}
