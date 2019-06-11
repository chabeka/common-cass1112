/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericIndexDAOImpl;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionToJobStepDaoCql;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class JobExecutionToJobStepDaoCqlImpl extends GenericIndexDAOImpl<JobExecutionToJobStepCql, Long> implements IJobExecutionToJobStepDaoCql {

}
