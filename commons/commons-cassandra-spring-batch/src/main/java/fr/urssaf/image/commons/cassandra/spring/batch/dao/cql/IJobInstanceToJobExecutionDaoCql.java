/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;

/**
 * TODO (AC75095028) Description du type
 */
public interface IJobInstanceToJobExecutionDaoCql extends IGenericIndexDAO<JobInstanceToJobExecutionCql, Long> {

  /**
   * Methode permettant de retourner la derniere instance de job instance par identifiant de job
   * 
   * @param id
   *          Identifiant de job
   * @return la derniere instance de job instance par identifiant de job
   */
  JobInstanceToJobExecutionCql getLastJobInstanceToJobExecution(Long id);

}
