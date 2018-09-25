/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionsDaoCql;
import fr.urssaf.image.sae.commons.dao.impl.GenericIndexDAOImpl;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class JobExecutionsDaoCqlImpl extends GenericIndexDAOImpl<JobExecutionsCql, String> implements IJobExecutionsDaoCql {

}
