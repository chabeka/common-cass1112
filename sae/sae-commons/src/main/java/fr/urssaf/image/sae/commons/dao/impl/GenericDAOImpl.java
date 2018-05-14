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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Truncate;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.collect.Lists;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.commons.context.JsonCodec;
import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.commons.utils.ColumnUtil;
import fr.urssaf.image.sae.commons.utils.QueryUtils;
import fr.urssaf.image.sae.commons.utils.Utils;
import me.prettyprint.cassandra.utils.Assert;

/**
 * TODO (AC75095028) Description du type
 */

public class GenericDAOImpl<T, ID> implements IGenericDAO<T, ID> {

  @Autowired
  protected CassandraCQLClientFactory ccf;

  private MappingManager manager;

  protected Class<? extends T> daoType;

  protected Mapper<T> mapper;

  @SuppressWarnings("unchecked")
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
   * @return
   */
  private boolean getStartLocal() {
    boolean startLocal = false;
    if (ccf.getServer() != null && ccf.getServer().getStartLocal()) {
      startLocal = true;
    }
    return startLocal;
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
      if (getStartLocal()) {
        mapper = (Mapper<T>) manager.mapper(daoType, ccf.getKeyspace());
      } else {
        mapper = (Mapper<T>) manager.mapper(daoType);
      }
      final JsonCodec<Map> myJsonCodec = new JsonCodec<Map>(Map.class);
      final CodecRegistry myCodecRegistry = ccf.getCluster().getConfiguration().getCodecRegistry();
      myCodecRegistry.register(myJsonCodec);
    }
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
  public <S extends T> List<S> saveAll(final Iterable<S> entites) {

    final List<S> listEntites = new ArrayList<>();
    for (final S entity : entites) {
      save(entity);
      listEntites.add(entity);
    }
    return listEntites;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<T> findAllWithMapper() {
    final Statement st = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    return getMapper().map(getSession().execute(st)).all();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public ResultSet findAll() {
    final Statement st = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    return getSession().execute(st);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<T> findAllWithMapperById(final Iterable<ID> ids) {
    Assert.noneNull(ids, "La liste des ids ne peut être null");
    final List<ID> idsEntity = Lists.newArrayList(ids);
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType);
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
  public void insertWithBatch(final Iterable<? extends T> entities) {
    final String batch = QueryUtils.createInsertBatch(daoType, entities, getTypeArgumentsName());
    getSession().execute(batch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void insertWithBatchStatement(final Iterable<? extends T> entities) {
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
  public <S extends T> S save(final S entity) {
    final Insert insert = QueryBuilder.insertInto(ccf.getKeyspace(), getTypeArgumentsName());
    final List<Field> fields = Utils.getEntityFileds(daoType);
    QueryUtils.createInsert(fields, insert, entity);
    getSession().execute(insert);
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <S extends T> S saveWithMapper(final S entity) {
    getMapper().save(entity);
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<T> findWithMapperById(final ID id) {
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType);
    final String keyName = keyField.getName();
    select.where(eq(keyName, id));
    final ResultSet result = getSession().execute(select);
    return Optional.ofNullable(getMapper().map(result).one());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSet findById(final ID id) {
    Assert.notNull(id, " l'id est requis");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType);
    Assert.notNull(keyField, "Le field ne peut être null");
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
    return findWithMapperById(id).isPresent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long count() {
    return findAll().all().size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteById(final ID id) {
    final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getKeyField(daoType);
    final String keyName = keyField.getName();
    delete.where(eq(keyName, id));
    getMapper().map(getSession().execute(delete));
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void deleteWithMapper(final T entity) {
    getMapper().delete(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final T entity) {
    final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());
    QueryUtils.createDeleteQuery(daoType, delete, entity);
    getSession().execute(delete);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteAll(final Iterable<? extends T> entities) {
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
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    return getSession().execute(select);
  }

}
