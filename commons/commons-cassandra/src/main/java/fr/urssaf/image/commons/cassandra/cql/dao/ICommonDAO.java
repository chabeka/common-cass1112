/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.util.Assert;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Truncate;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Mapper.Option;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;
import fr.urssaf.image.commons.cassandra.utils.QueryUtils;
import fr.urssaf.image.commons.cassandra.utils.Utils;

/**
 * Interface mère de tous les DAO implementant toutes les opérations de CRUD sur une entité
 * 
 * @param <T>
 *          entité T
 * @param <ID>
 *          l'identifiant de l'entité
 */
public interface ICommonDAO<T, ID> {
  /**
   * Retourne le nom du pojo ou la valeur de l'attribut "name" de l'annotation
   * {@link com.datastax.driver.mapping.annotations.Table} comme étant le nom
   * de la colonne familly (le nom de la table)
   *
   * @return
   */
  public default String getTypeArgumentsName() {
    return ColumnUtil.getColumnFamily(getDaoType());
  }

  /**
   * Sauvegarde la liste des entités T fournies
   *
   * @param entites
   *           les entités à sauvegarder
   * @return les entités sauvegardées
   */
  public default List<T> saveAll(final Iterable<T> entites) {

    final List<T> listEntites = new ArrayList<>();
    for (final T entity : entites) {
      saveWithMapper(entity);
      listEntites.add(entity);
    }
    return listEntites;
  }

  /**
   * Sauvegarde l'entité T fournie
   *
   * @param entity
   *           entité à sauvegarder
   * @return l'entité sauvegardée
   */
  public default T save(final T entity) {
    Assert.notNull(entity, " l'entity ne peut etre null");
    final Insert insert = QueryBuilder.insertInto(getCcf().getKeyspace(), getTypeArgumentsName());
    final List<Field> fields = Utils.getEntityFileds(getDaoType());
    QueryUtils.createInsert(fields, insert, entity);
    getCcf().getSession().execute(insert);
    if (getLogger().isDebugEnabled()) {
      getLogger().info(insert.toString());
    }
    return entity;
  }

  /**
   * Retourne toutes les entitées de la table en utilsant le {@link Mapper} pour mapper
   * le resultat ({@link ResultSet}) avec le type T fournie en paramètre de la classe.
   * La liste retournée est une liste mapper de type T
   *
   * @return La {@link List} de type T
   */
  public default Iterator<T> findAllWithMapper() {
    final Statement st = QueryBuilder.select().from(getCcf().getKeyspace(), getTypeArgumentsName());
    return getMapper().map(getSession().execute(st)).iterator();
  }

  /**
   * Sauvegarde l'entité T fournie en utilisant le {@link Mapper} de datastax
   *
   * @param entity
   *           entité à sauvegarder
   * @return L'entité sauvegardée
   */
  public default T saveWithMapper(final T entity) {
    getMapper().save(entity);
    return entity;
  }

  /**
   * Sauvegarde l'entité T fournie en utilisant le {@link com.datastax.driver.mapping.Mapper} de datastax
   * avec une date d'expiration. A la fin de cette date d'expiration, la donnée est supprimer de la base
   *
   * @param entity
   *           entité à sauvegarder
   *           *
   * @param ttl
   *           date d'expiration exprimé en seconde
   * @return l'entité sauvegardée
   */
  public default T saveWithMapper(final T entity, final int ttl) {
    getMapper().setDefaultSaveOptions(Option.ttl(ttl));
    getMapper().save(entity);
    return entity;
  }

  /**
   * Supprime l'entité T fournit en utilisant le {@ Mapper}.
   *
   * @param entité
   *           à surpprimer
   */
  public default void deleteWithMapper(final T entity) {
    Assert.notNull(entity, " l'entity est requis");
    getMapper().delete(entity);
  }

  /**
   * Retourne un {@link ResultSet} contenant toutes les entitées T dans la table correspondante
   *
   * @return {@link ResultSet} contenant toutes les entitées
   */
  public default Iterator<T> findAll() {
    final Statement st = QueryBuilder.select().from(getCcf().getKeyspace(), getTypeArgumentsName());
    return (Iterator<T>) getSession().execute(st).iterator();
  }

  /**
   * Supprime toutes les entitées de la table
   */
  public default void deleteAll() {
    final Truncate truncate = QueryBuilder.truncate(getCcf().getKeyspace(), getTypeArgumentsName());
    getSession().execute(truncate);
  }

  /**
   * Retourne le nombre d'entité de la table correspondante
   *
   * @return the number of entities
   */

  public default Integer count() {
    Long count = 0L;
    final String query = " SELECT COUNT(*) FROM " + getCcf().getKeyspace() + "." + getTypeArgumentsName();
    final ResultSet result = getSession().execute(query);
    final Row row = result.one();
    if (row != null) {
      count = row.getLong("count");
    }
    return count.intValue();
  }

  /**
   * Retourne un iterateur sur les colonnes des anciennes CF
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public default Iterator<T> findAllByCFName(final String cfName, final String keyspace) {
    final String query = "SELECT * FROM " + "\"" + keyspace + "\".\"" + cfName + "\"";
    if (getLogger().isDebugEnabled()) {
      getLogger().info(query);
    }
    return (Iterator<T>) getSession().execute(query).iterator();
  }

  /**
   * {@inheritDoc}
   */
  public default void insertWithBatchStatement(final BatchStatement statement) {
    getSession().execute(statement);
  }

  /**
   * Inserrer un ensemble de colonne à partir d'une requete
   *
   * @param batch
   */

  public default void insertWithBatch(final Iterable<T> entities) {
    final String batch = QueryUtils.createInsertBatch(getDaoType(), entities, getTypeArgumentsName());
    getSession().execute(batch);
  }

  /**
   * {@inheritDoc}
   */

  public default void insertWithBatchStatement(final Iterable<T> entities) {
    Assert.notNull(entities, " la liste des entités ne peut etre null");
    final BatchStatement statement = new BatchStatement();
    for (final T entity : entities) {
      final Insert insert = QueryBuilder.insertInto(getTypeArgumentsName());
      final List<Field> fields = Utils.getEntityFileds(getDaoType());
      QueryUtils.createInsert(fields, insert, entity);
      statement.add(insert);
    }
    getSession().execute(statement);
  }

  /**
   * La session sur le keyspace dans le cluster
   *
   * @return
   */

  public default Session getSession() {
    return getCcf().getSession();
  }

  /**
   * @return le type T fournie en paramètre de la classe
   */
  Class<? extends T> getDaoType();

  /**
   * Retourne l'instance de la classe {@link CassandraCQLClientFactory}
   *
   * @return
   */
  CassandraCQLClientFactory getCcf();

  /**
   *  initialise l'instance de la classe {@link CassandraCQLClientFactory}
   */
  public void setCcf(CassandraCQLClientFactory  ccf);
  /**
   * @return
   */
  Mapper<T> getMapper();

  public Logger getLogger();
}
