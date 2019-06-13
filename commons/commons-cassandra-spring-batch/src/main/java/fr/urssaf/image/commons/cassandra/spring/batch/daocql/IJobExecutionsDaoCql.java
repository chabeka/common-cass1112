/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql;

import java.util.Optional;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;

/**
 * TODO (AC75095028) Description du type
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
