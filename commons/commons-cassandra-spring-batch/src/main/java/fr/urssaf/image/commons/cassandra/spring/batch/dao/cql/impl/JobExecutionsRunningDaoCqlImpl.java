/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericIndexDAOImpl;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsRunningCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionsDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionsRunningDaoCql;

/**
 * Classe implémentant le DAO  {@link IJobExecutionsRunningDaoCql}
 *
 */
@Repository
public class JobExecutionsRunningDaoCqlImpl extends GenericIndexDAOImpl<JobExecutionsRunningCql, String> implements IJobExecutionsRunningDaoCql {

}
