/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.gte;
import static com.datastax.driver.core.querybuilder.QueryBuilder.lte;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.commons.utils.ColumnUtil;
import fr.urssaf.image.sae.trace.daocql.IGenericIndexCqlDao;

/**
 * TODO (AC75095028) Description du type
 *
 * @param <T>
 */
public class GenericIndexCqlDaoImpl<T, ID> extends GenericDAOImpl<T, ID> implements IGenericIndexCqlDao<T, ID> {

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<T> findByDateCreatedBefore(final ID id) {
    Objects.requireNonNull(id, "La liste des ids ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
    final String keyName = keyField.getName();
    select.where(lte(keyName, id));
    return getMapper().map(getSession().execute(select)).all().iterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<T> findByDateCreatedAfter(final ID id) {
    Objects.requireNonNull(id, "La liste des ids ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
    final String keyName = keyField.getName();
    select.where(gte(keyName, id));
    return getMapper().map(getSession().execute(select)).all().iterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<T> findByDateInterval(final String dateStar, final String dateEnd, final boolean reversed, final Integer limit) {
    Objects.requireNonNull(dateStar, "La date de debut ne peut etre null");
    Objects.requireNonNull(dateStar, "La date de fin ne peut etre null");

    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
    final String keyName = keyField.getName();
    final StringBuilder sb = new StringBuilder();
    // TODO

    return getMapper().map(getSession().execute(sb.toString())).all().iterator();
  }

}
