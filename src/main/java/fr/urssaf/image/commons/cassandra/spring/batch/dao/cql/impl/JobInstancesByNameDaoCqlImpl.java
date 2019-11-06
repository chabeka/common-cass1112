/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericIndexDAOImpl;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstancesByNameDaoCql;
import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class JobInstancesByNameDaoCqlImpl extends GenericIndexDAOImpl<JobInstancesByNameCql, String> implements IJobInstancesByNameDaoCql {

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteById(final String id) {
    Assert.notNull(id, " l'id est requis");
    final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());

    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à supprimer ne peut être null");

    final String keyName = keyField.getName();
    delete.where(eq(keyName, id));
    getMapper().map(getSession().execute(delete));
  }

}
