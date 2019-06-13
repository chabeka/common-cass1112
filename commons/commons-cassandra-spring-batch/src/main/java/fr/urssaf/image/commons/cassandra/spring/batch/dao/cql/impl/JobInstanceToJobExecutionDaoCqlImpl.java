/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import java.lang.reflect.Field;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericIndexDAOImpl;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceToJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class JobInstanceToJobExecutionDaoCqlImpl extends GenericIndexDAOImpl<JobInstanceToJobExecutionCql, Long> implements IJobInstanceToJobExecutionDaoCql {

  @Override
  public JobInstanceToJobExecutionCql getLastJobInstanceToJobExecution(final Long id) {
    Assert.notNull(id, "L'id ne peut être null");
    // final List<ID> idsEntity = Lists.newArrayList(ids);
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), "\"" + getTypeArgumentsName() + "\"");
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");

    final String keyName = keyField.getName();
    select.where(QueryBuilder.eq(keyName, id));
    select.limit(1);
    return getMapper().map(getSession().execute(select)).one();
  }
}
