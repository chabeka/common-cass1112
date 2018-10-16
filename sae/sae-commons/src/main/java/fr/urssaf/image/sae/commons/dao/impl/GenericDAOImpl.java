/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.dao.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.commons.utils.ColumnUtil;
import fr.urssaf.image.sae.commons.utils.QueryUtils;

/**
 * TODO (AC75095028) Description du type
 */

public class GenericDAOImpl<T, ID> implements IGenericDAO<T, ID> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GenericDAOImpl.class);

  @Autowired
  protected CassandraCQLClientFactory ccf;

  protected Class<? extends T> daoType;

  private MappingManager manager;

  protected Mapper<T> mapper;

  public GenericDAOImpl() {
    final Type t = getClass().getGenericSuperclass();
    final ParameterizedType pt = (ParameterizedType) t;
    daoType = (Class) pt.getActualTypeArguments()[0];
  }

  /**
   * @return the daoType
   */
  @Override
  public Class<? extends T> getDaoType() {
    return daoType;
  }

  /**
   * @return the ccf
   */
  @Override
  public CassandraCQLClientFactory getCcf() {
    return ccf;
  }

  /**
   * Mapper le type T à la table cassandra
   *
   * @return
   */
  @Override
  @SuppressWarnings("unchecked")
  public Mapper<T> getMapper() {
    if (mapper == null) {
      manager = new MappingManager(ccf.getSession());
      mapper = (Mapper<T>) manager.mapper(daoType);
    }
    return mapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<T> findWithMapperById(final ID id) {
    Assert.notNull(id, "L'identifiant ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");

    final String keyName = keyField.getName();
    select.where(eq(keyName.toLowerCase(), id));
    final ResultSet result = getSession().execute(select);
    return Optional.ofNullable(getMapper().map(result).one());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<T> IterableFindById(final ID id) {
    Assert.notNull(id, "L'identifiant ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");

    final String keyName = keyField.getName();
    select.where(eq(keyName, id));
    final ResultSet result = getSession().execute(select);
    return getMapper().map(result).iterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSet findById(final ID id) {
    Assert.notNull(id, " l'id est requis");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");

    final String keyName = keyField.getName();
    select.where(eq(keyName, id));
    final ResultSet result = getSession().execute(select);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existsById(final ID id) {
    Assert.notNull(id, " l'id est requis");
    return findWithMapperById(id).isPresent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteById(final ID id) {
    Assert.notNull(id, " l'id est requis");
    final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());

    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à supprimer ne peut être null");

    final String keyName = keyField.getName();
    delete.where(eq(keyName, id));
    getMapper().map(getSession().execute(delete));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final T entity) {
    Assert.notNull(entity, " l'entity est requis");
    final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());
    QueryUtils.createDeleteQuery(daoType, delete, entity);
    getSession().execute(delete);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteAll(final Iterable<T> entities) {
    Assert.notNull(entities, " les entités a supprimer sont requis");
    for (final T entity : entities) {
      delete(entity);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @Override
   *           public ResultSet findAllById(final Iterable<ID> ids) {
   *           Assert.notNull(ids, " les ids des entités a supprimer sont requis");
   *           final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
   *           return getSession().execute(select);
   *           }
   */

  /**
   * @return the logger
   */
  @Override
  public Logger getLogger() {
    return LOGGER;
  }

}
