/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;
import fr.urssaf.image.sae.commons.dao.IGenericIndexDAO;

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
