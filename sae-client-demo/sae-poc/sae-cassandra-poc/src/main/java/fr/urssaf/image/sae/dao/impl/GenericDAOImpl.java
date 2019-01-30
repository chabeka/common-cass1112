/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.dao.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.in;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;



import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.dao.IGenericDAO;
import fr.urssaf.image.sae.model.GenericType;
import fr.urssaf.image.sae.model.Metadata;

/**
 * TODO (AC75095028) Description du type
 */

public class GenericDAOImpl<T, ID extends Serializable> implements IGenericDAO<T, ID> {

  @Autowired
  CassandraCQLClientFactory ccf;
  MappingManager manager;

  // @Autowired
  // private ApplicationContext context;

  protected Class<? extends T> daoType;
  Mapper<T> mapper;

  @SuppressWarnings("unchecked")
  public GenericDAOImpl() {
    final Type t = getClass().getGenericSuperclass();
    final ParameterizedType pt = (ParameterizedType) t;
    daoType = (Class) pt.getActualTypeArguments()[0];
  }

  /**
   * Retourne le nom du pojo en tant que nom de la colonne familly
   *
   * @return
   */
  public String getTypeArgumentsName() {
    // return daoType.getSimpleName();
    return "\"" + daoType.getSimpleName() + "\"";
  }
 
  public Mapper getMapper(){
	  manager = new MappingManager(ccf.getSession());
	  mapper = (Mapper<T>) manager.mapper(daoType);
	  return mapper;
  }
  /**
   * La session sur le keyspace dans le cluster
   *
   * @return
   */
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
      insert(entity);
      listEntites.add(entity);
    }
    return listEntites;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Result<T> findAll() {
    // final CassandraPersistentEntity<?> persistentEntity = getPersistentEntity();
    // final String query = "SELECT * FROM " + ccf.getKeyspace() + "." + getTypeArgumentsName();
    final Statement st = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    return (Result<T>) getMapper().map(getSession().execute(st));

  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<T> findAllById(final Iterable<ID> ids) {
    /*final List<ID> idsEntity = Streamable.of(ids).stream().collect(StreamUtils.toUnmodifiableList());
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    select.where(in("id", idsEntity));
    return (List<T>) getTemplate().select(select, daoType);*/
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <S extends T> S insert(final S entity) {
    final Insert insert = createInsertStatement(entity);
    
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <S extends T> List<S> insert(final Iterable<S> entities) {
    final List<S> listEntites = new ArrayList<>();
    for (final S entity : entities) {
      insert(entity);
      listEntites.add(entity);
    }
    return listEntites;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <S extends T> S save(final S entity) {
    return insert(entity);
  }

  /**
   * {@inheritDoc}
   */

  public Optional<T> findById(final ID id) {
    // final String query = "SELECT * FROM " + ccf.getKeyspace() + "." + getTypeArgumentsName() + " WHERE id=" + id;
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    select.where(eq("id", id));
    return null;
  }

  /**
   * {@inheritDoc}
   */

  public boolean existsById(final ID id) {
    return findById(id).isPresent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long count() {
    return 1L;//findAll().size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteById(final ID id) {
    // final String query = "DELETE * FROM " + ccf.getKeyspace() + "." + getTypeArgumentsName() + " WHERE id=" + id;
    //final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getPersistentEntity().getTableName().toString());
    //delete.where(eq("id", id));
    //getTemplate().deleteById(daoType, id);
  }

  /**
   * {@inheritDoc}
   */
 
  public void delete(final T entity) {
    //getTemplate().delete(entity);
  }

  /**
   * {@inheritDoc}
   */
 
  public void deleteAll(final Iterable<? extends T> entities) {
    entities.forEach(this::delete);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteAll() {
    //final CassandraPersistentEntity<?> persistentEntity = getPersistentEntity();
   // getCqlOperations().execute(QueryBuilder.truncate(ccf.getKeyspace(), persistentEntity.getTableName().toCql()));
  }


  public <S extends T> Insert createInsertStatement(final S entity) {
    final Map<String, Object> map = new LinkedHashMap<>();
    //getTemplate().getConverter().write(entity, map);
    //final Insert insert = QueryBuilder.insertInto(ccf.getKeyspace(), getPersistentEntity().getTableName().toString());
    //map.forEach(insert::value);
    return null;
  }

@Override
public <S extends T> Iterable<S> save(Iterable<S> entities) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public T findOne(Serializable id) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean exists(Serializable id) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void delete(Iterable<? extends T> entities) {
	// TODO Auto-generated method stub
	
}

@Override
public Iterable<T> findAll(Iterable<ID> ids) {
	// TODO Auto-generated method stub
	return null;
}




}
