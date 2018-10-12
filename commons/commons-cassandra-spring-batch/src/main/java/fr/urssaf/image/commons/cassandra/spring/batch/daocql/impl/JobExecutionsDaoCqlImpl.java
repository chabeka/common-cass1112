/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionsDaoCql;
import fr.urssaf.image.sae.commons.dao.impl.GenericIndexDAOImpl;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class JobExecutionsDaoCqlImpl extends GenericIndexDAOImpl<JobExecutionsCql, String> implements IJobExecutionsDaoCql {

   private static final String JOBEXECUTIONID = "jobexecutionid";

   @Override
   public Optional<JobExecutionsCql> findByJobExecutionId(final Long indexedValue) {
      Assert.notNull(indexedValue, "L'index ne peut être null");
      final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
      select.where(QueryBuilder.eq(JOBEXECUTIONID, indexedValue));
      return Optional.ofNullable(getMapper().map(getSession().execute(select)).one());
   }
}
