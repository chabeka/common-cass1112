/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.ISequencesDaoCql;
import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class SequencesDaoCqlImpl extends GenericDAOImpl<SequencesCql, String> implements ISequencesDaoCql {

  /**
   * @param ccf
   */
  @Autowired
  public SequencesDaoCqlImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getColunmClock(final String id) {
    Assert.notNull(id, "L'identifiant ne peut être null");

    // recuperer le nom de la primary key
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");
    final String keyName = keyField.getName();

    final Select select = QueryBuilder.select().writeTime("value").as("timestamp").from(ccf.getKeyspace(), getTypeArgumentsName());
    select.where(eq(keyName.toLowerCase(), id));
    final Row row = getSession().execute(select).one();
    return row.getLong("timestamp");
  }

}
