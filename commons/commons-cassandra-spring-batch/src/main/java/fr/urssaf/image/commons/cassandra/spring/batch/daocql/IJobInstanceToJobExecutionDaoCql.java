/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;

/**
 * TODO (AC75095028) Description du type
 */
public interface IJobInstanceToJobExecutionDaoCql extends IGenericIndexDAO<JobInstanceToJobExecutionCql, Long> {

  /**
   * @param id
   * @return
   */
  JobInstanceToJobExecutionCql getLastJobInstanceToJobExecution(Long id);

}
