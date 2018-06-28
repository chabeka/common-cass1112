package fr.urssaf.image.sae.pile.travaux.dao.cql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper.Option;
import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.commons.utils.ColumnUtil;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobQueueCql;

/**
 * DAO de la colonne famille <code>JobsQueue</code>
 */
@Repository
public class JobsQueueDaoCqlImpl extends GenericDAOImpl<JobQueueCql, UUID> implements IJobsQueueDaoCql {

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteByIdAndIndexColumn(final UUID id, final String colSituation, final long clock) {
    Assert.notNull(id, " id est requis");
    Assert.notNull(id, " colSituation est requis");
    Assert.notNull(clock, " le clock est requis");
    final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
    final String keyName = keyField.getName();
    delete.where(eq(keyName, id));
    delete.where(eq("jobsituation", colSituation));
    getMapper().setDefaultDeleteOptions(Option.timestamp(clock));
    getMapper().map(getSession().execute(delete));
  }
}
