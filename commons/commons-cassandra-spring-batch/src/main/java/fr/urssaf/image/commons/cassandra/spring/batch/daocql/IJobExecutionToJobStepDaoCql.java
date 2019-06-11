/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;

/**
 * TODO (AC75095028) Description du type
 */
public interface IJobExecutionToJobStepDaoCql extends IGenericIndexDAO<JobExecutionToJobStepCql, Long> {

}
