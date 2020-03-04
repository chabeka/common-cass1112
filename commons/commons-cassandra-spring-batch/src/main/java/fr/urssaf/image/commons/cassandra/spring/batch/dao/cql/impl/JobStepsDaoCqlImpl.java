/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericIndexDAOImpl;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepsDaoCql;

/**
 * Classe implémentant le DAO  {@link IJobStepsDaoCql}
 */
@Repository
public class JobStepsDaoCqlImpl extends GenericIndexDAOImpl<JobStepsCql, Long> implements IJobStepsDaoCql {

	@Override
	public List<JobStepsCql> getJobStepsCqlByJobName(String jobname) {
		  Assert.notNull(jobname, "jobname ne peut être null");
	      final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
	      select.where(QueryBuilder.eq("jobname", jobname));
	      List<JobStepsCql> list = getMapper().map(getSession().execute(select)).all();
	      return list;
	}

	@Override
	public List<JobStepsCql> getJobStepsCqlByJobNameAndSetName(String jobname, String stepname) {
		  Assert.notNull(jobname, "jobname ne peut être null");
		  Assert.notNull(stepname, "stepname ne peut être null");
	      final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
	      select.where(QueryBuilder.eq("jobname", jobname)).and(QueryBuilder.eq("stepname", stepname));
	      select.allowFiltering();
	      List<JobStepsCql> list = getMapper().map(getSession().execute(select)).all();
	      return list;
	}

}
