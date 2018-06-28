/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * TODO (AC75095028) Description du type
 */

/**
 * Classe abstraite pour les DAO CASSANDRA
 *
 * @param <T>
 * @param <ID>
 */
public interface IGenericDAO<T, ID> {

  public Session getSession();

  /**
   * Sauvegarde l'entité T fournie
   *
   * @param entity
   *          entité à sauvegarder
   * @return l'entité sauvegardée
   */
  T save(T entity);

  /**
   * Sauvegarde l'entité T fournie en utilisant le {@link com.datastax.driver.mapping.Mapper} de datastax
   *
   * @param entity
   *          entité à sauvegarder
   * @return l'entité sauvegardée
   */
  T saveWithMapper(final T entity);

  /**
   * Sauvegarde la liste des entités T fournies
   *
   * @param entites
   *          les entités à sauvegarder
   * @return les entités sauvegardées
   */
  List<T> saveAll(Iterable<T> entites);

  /**
   * Verifie l'existance d'une entité T par son ID (key)
   *
   * @param id
   *          l'identitfiant de l'entité
   * @return True si l'entité existe sinon False
   */
  boolean existsById(final ID id);

  /**
   * Recherche une entité par son ID
   *
   * @param id
   *          doit être non {@literal null}.
   * @return {@link ResultSet} contenant l'entité avec l'ID fourni ou un {@link ResultSet} vide
   */
  ResultSet findById(final ID id);

  /**
   * Recherche une entité T par son ID en utilisant le {@link com.datastax.driver.mapping.Mapper}
   * de datastax <a href="https://docs.datastax.com/en/developer/java-driver/2.1/manual/object_mapper/using/">Using the mapper</a>
   *
   * @param id
   *          l'ID de l'entité
   * @return {@link Optional} contenant l'entité ou un {@link Optional} vide si l'entité n'existe pas
   */
  Optional<T> findWithMapperById(final ID id);

  /**
   * Recherche des entités par leur ID
   *
   * @param ids
   *          la liste des IDs des entités
   * @return {@link ResultSet} contenant la liste des entités T correspondant aux IDs fournis ou un {@link ResultSet} vide
   */
  ResultSet findAllById(Iterable<ID> ids);

  /**
   * Recherche des entité T par leur ID en utilisant le {@link com.datastax.driver.mapping.Mapper}
   * de datastax <a href="https://docs.datastax.com/en/developer/java-driver/2.1/manual/object_mapper/using/">Using the mapper</a>
   * afin d'avoir une liste de retour mapper avec le type T
   *
   * @param ids
   *          la liste des IDs des entités
   * @return la liste des entités T correspondant aux IDs fournis
   */
  List<T> findAllWithMapperById(Iterable<ID> ids);

  /**
   * Retourne un {@link ResultSet} contenant toutes les entitées T dans la table correspondante
   *
   * @return {@link ResultSet} contenant toutes les entitées
   */
  Iterator<T> findAll();

  /**
   * Supprime l'entité T avec l'ID
   *
   * @param id
   */
  void deleteById(ID id);

  /**
   * Supprime l'entité T fournit en utilisant le {@link com.datastax.driver.mapping.Mapper}
   * pour mapper l'entité à la table correspondante.
   *
   * @param entité
   *          à surpprimer
   */
  void deleteWithMapper(final T entity);

  /**
   * Supprime l'entité T fournie.
   *
   * @param entité
   * @throws IllegalArgumentException
   *           in case the given entity is (@literal null}.
   */
  void delete(T entity);

  /**
   * Supprime toutes les entitées de la table
   */
  void deleteAll();

  /**
   * Supprime les entitées fournies
   *
   * @param entitées
   *          à supprimer
   */
  void deleteAll(final Iterable<T> entities);

  /**
   * Retourne le nombre d'entité de la table correspondante
   *
   * @return the number of entities
   */
  long count();

  /**
   * Insert d'un ensemble d'entitées en utilisant un bach
   *
   * @param entities
   *          la liste des entitées T à sauvegarder
   */
  void insertWithBatch(final Iterable<T> entities);

  void insertWithBatchStatement(final Iterable<T> entities);

  void insertWithBatchStatement(final BatchStatement statement);

  /**
   * Retourne toutes les entitées de la table en utilsant un mapper. La liste retournée est une liste mapper de type T
   *
   * @return la {@link List} de type T
   */
  Iterator<T> findAllWithMapper();

  /**
   * @param id
   * @return
   */
  Iterator<T> IterableFindById(ID id);

  /**
   * Retourne un iterateur sur les colonnes des anciennes CF
   *
   * @return
   */
  Iterator<T> iterablefindAll(String cfName, final String keyspace);

  /**
   * @param entity
   * @param ttl
   * @return
   */
  T saveWithMapper(T entity, int ttl);

  /**
   * @param entity
   * @param ttl
   * @param clock
   * @return
   */
  T saveWithMapper(T entity, int ttl, long clock);
}
