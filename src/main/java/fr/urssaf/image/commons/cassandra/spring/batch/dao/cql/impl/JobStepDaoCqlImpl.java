package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepDaoCql;

@Repository
public class JobStepDaoCqlImpl extends GenericDAOImpl<JobStepCql, Long> implements IJobStepDaoCql{

	public JobStepDaoCqlImpl(CassandraCQLClientFactory ccf) {
		super(ccf);
	}

	@Override
	public List<JobStepCql> findJobStepByJobExecutionId(Long jobExecutionId) {
		 Assert.notNull(jobExecutionId, "L'identifiant ne peut être null");
	      final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
	      select.where(QueryBuilder.eq("jobexecutionid", jobExecutionId));
	      List<JobStepCql> list = getMapper().map(getSession().execute(select)).all();
	      return list;
	}

}
