/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.dao.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
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

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericCompositeDAO;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;
import fr.urssaf.image.commons.cassandra.utils.QueryUtils;

/**
 * TODO (AC75095028) Description du type
 */

public class GenericDAOCompositeImpl<T, ID, CK> implements IGenericCompositeDAO<T, ID, CK> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GenericDAOCompositeImpl.class);

  @Autowired
  protected CassandraCQLClientFactory ccf;

  protected Class<? extends T> daoType;

  // private MappingManager manager;

  protected Mapper<T> mapper;

  public GenericDAOCompositeImpl() {
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
      // manager = new MappingManager(ccf.getSession());
      mapper = (Mapper<T>) ccf.getManager().mapper(daoType);
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
   * @return the logger
   */
  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<T> findWithMapperByIdComposite(final ID id, final CK ck) {
    Assert.notNull(id, "L'identifiant ne peut être null");
    Assert.notNull(ck, "L'autre partie de la clé ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());

    final List<Field> list = ColumnUtil.getCompositePartionKeyField(daoType);
    Assert.notNull(list, "La liste de  clés de l'entité à chercher ne peut être null");
    Assert.state(list.size() == 2, "On doit avoir deux clés  ");

    final String keyName1 = list.get(0).getName();
    Assert.notNull(keyName1, "Le nom d'une partie de la clé ne peut être null");
    final String keyName2 = list.get(1).getName();
    Assert.notNull(keyName2, "Le nom d'une partie de la clé ne peut être null");
    select.where(eq(keyName1.toLowerCase(), id)).and(eq(keyName2.toLowerCase(), ck));
    final ResultSet result = getSession().execute(select);
    return Optional.ofNullable(getMapper().map(result).one());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCcf(final CassandraCQLClientFactory ccf) {
    this.ccf = ccf;

  }

}
