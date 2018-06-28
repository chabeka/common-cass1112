/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.dao.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.in;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Truncate;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Mapper.Option;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.google.common.collect.Lists;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.commons.utils.ColumnUtil;
import fr.urssaf.image.sae.commons.utils.QueryUtils;
import fr.urssaf.image.sae.commons.utils.Utils;

/**
 * TODO (AC75095028) Description du type
 */

public class GenericDAOImpl<T, ID> implements IGenericDAO<T, ID> {

  @Autowired
  protected CassandraCQLClientFactory ccf;

  @Autowired
  private JobClockSupport clockSupport;

  private MappingManager manager;

  protected Class<? extends T> daoType;

  protected Mapper<T> mapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(GenericDAOImpl.class);

  public GenericDAOImpl() {
    final Type t = getClass().getGenericSuperclass();
    final ParameterizedType pt = (ParameterizedType) t;
    daoType = (Class) pt.getActualTypeArguments()[0];
  }

  /**
   * Retourne le nom du pojo ou la valeur de l'attribut "name" de l'annotation
   * {@link com.datastax.driver.mapping.annotations.Table} comme etant le nom
   * de la colonne familly (le nom de la table)
   *
   * @return
   */
  public String getTypeArgumentsName() {
    return ColumnUtil.getColumnFamily(daoType);
  }

  /**
   * Mapper le type T à la table cassandra
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public Mapper<T> getMapper() {
    if (mapper == null) {
      manager = new MappingManager(ccf.getSession());
      if (ccf.getStartLocal()) {
        mapper = (Mapper<T>) manager.mapper(daoType, CassandraServerBeanCql.KEYSPACE_TU);
      } else {
        mapper = (Mapper<T>) manager.mapper(daoType);
      }
    }
    // mapper.setDefaultSaveOptions(Option.timestamp(clockSupport.currentCLock()));
    return mapper;
  }

  /**
   * La session sur le keyspace dans le cluster
   *
   * @return
   */
  @Override
  public Session getSession() {
    return ccf.getSession();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<T> saveAll(final Iterable<T> entites) {

    final List<T> listEntites = new ArrayList<>();
    for (final T entity : entites) {
      save(entity);
      listEntites.add(entity);
    }
    return listEntites;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<T> findAllWithMapper() {
    final Statement st = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    return getMapper().map(getSession().execute(st)).iterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<T> findAll() {
    final Statement st = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    return (Iterator<T>) getSession().execute(st).iterator();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Iterator<T> iterablefindAll(final String cfName, final String keyspace) {
    final String query = "SELECT * FROM \"" + keyspace + "\".\"" + cfName + "\"";
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info(query);
    }
    return (Iterator<T>) getSession().execute(query).iterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<T> findAllWithMapperById(final Iterable<ID> ids) {
    Assert.notNull(ids, "La liste des ids ne peut être null");
    final List<ID> idsEntity = Lists.newArrayList(ids);
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), "\"" + getTypeArgumentsName() + "\"");
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
    final String keyName = keyField.getName();
    select.where(in(keyName, idsEntity));
    return getMapper().map(getSession().execute(select)).all();
  }

  /**
   * Inserrer un ensemble de colonne avec une requete batch
   *
   * @param batch
   */
  @Override
  public void insertWithBatch(final Iterable<T> entities) {
    final String batch = QueryUtils.createInsertBatch(daoType, entities, getTypeArgumentsName());
    getSession().execute(batch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void insertWithBatchStatement(final Iterable<T> entities) {
    Assert.notNull(entities, " la liste des entités ne peut etre null");
    final BatchStatement statement = new BatchStatement();
    for (final T entity : entities) {
      final Insert insert = QueryBuilder.insertInto(getTypeArgumentsName());
      final List<Field> fields = Utils.getEntityFileds(daoType);
      QueryUtils.createInsert(fields, insert, entity);
      statement.add(insert);
    }
    getSession().execute(statement);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void insertWithBatchStatement(final BatchStatement statement) {
    getSession().execute(statement);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T save(final T entity) {
    Assert.notNull(entity, " l'entity ne peut etre null");
    final Insert insert = QueryBuilder.insertInto(ccf.getKeyspace(), getTypeArgumentsName());
    final List<Field> fields = Utils.getEntityFileds(daoType);
    QueryUtils.createInsert(fields, insert, entity);
    getSession().execute(insert);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info(insert.toString());
    }
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T saveWithMapper(final T entity) {
    getMapper().save(entity);
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T saveWithMapper(final T entity, final int ttl) {
    getMapper().setDefaultSaveOptions(Option.ttl(ttl));
    getMapper().save(entity);
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T saveWithMapper(final T entity, final int ttl, final long clock) {
    getMapper().setDefaultSaveOptions(Option.ttl(ttl), Option.timestamp(clock));
    getMapper().save(entity);
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<T> findWithMapperById(final ID id) {
    Assert.notNull(id, "L'identifiant ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
    final String keyName = keyField.getName();
    select.where(eq(keyName, id));
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
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
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
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
    Objects.requireNonNull(keyField, "Le field ne peut être null");
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
  public long count() {
    // TODO revoir
    final Statement st = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    return getSession().execute(st).all().size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteById(final ID id) {
    Assert.notNull(id, " l'id est requis");
    final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType, PartitionKey.class);
    final String keyName = keyField.getName();
    delete.where(eq(keyName, id));
    getMapper().map(getSession().execute(delete));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteWithMapper(final T entity) {
    Assert.notNull(entity, " l'entity est requis");
    getMapper().delete(entity);
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
   */
  @Override
  public void deleteAll() {
    final Truncate truncate = QueryBuilder.truncate(ccf.getKeyspace(), getTypeArgumentsName());
    getSession().execute(truncate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSet findAllById(final Iterable<ID> ids) {
    Assert.notNull(ids, " les ids des entités a supprimer sont requis");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    return getSession().execute(select);
  }

}
