/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericIndexDAOImpl;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepsDaoCql;

/**
 * Classe implémentant le DAO  {@link IJobStepsDaoCql}
 */
@Repository
public class JobStepsDaoCqlImpl extends GenericIndexDAOImpl<JobStepsCql, Long> implements IJobStepsDaoCql {

}
