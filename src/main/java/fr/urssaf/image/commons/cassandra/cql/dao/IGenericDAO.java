/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.dao;

import java.util.Iterator;
import java.util.Optional;

import com.datastax.driver.core.ResultSet;

/**
 * Interface pour effectuer un ensemble d'operations propres à certaines entités hors mis
 * les entités de types index
 *
 * @param <T>
 *          l'entité T
 * @param <ID>
 *          l'identifiant de l'entié
 */
public interface IGenericDAO<T, ID> extends ICommonDAO<T, ID> {

  /**
   * Verifie l'existance d'une entité T par son ID (key)
   *
   * @param id
   *           l'identitfiant de l'entité
   * @return True si l'entité existe sinon False
   */
  boolean existsById(final ID id);

  /**
   * Recherche une entité par son ID
   *
   * @param id
   *           doit être non {@literal null}.
   * @return {@link ResultSet} contenant l'entité avec l'ID fourni ou un {@link ResultSet} vide
   */
  ResultSet findById(final ID id);

  /**
   * Recherche une entité T par son ID en utilisant le {@link com.datastax.driver.mapping.Mapper}
   * de datastax <a href="https://docs.datastax.com/en/developer/java-driver/2.1/manual/object_mapper/using/">Using the mapper</a>
   *
   * @param id
   *           l'ID de l'entité
   * @return {@link Optional} contenant l'entité ou un {@link Optional} vide si l'entité n'existe pas
   */
  Optional<T> findWithMapperById(final ID id);

  /**
   * Recherche des entités par leur ID
   *
   * @param ids
   *           la liste des IDs des entités
   * @return {@link ResultSet} contenant la liste des entités T correspondant aux IDs fournis ou un {@link ResultSet} vide
   */
  // ResultSet findAllById(Iterable<ID> ids);

  /**
   * Supprime l'entité T avec l'ID
   *
   * @param id
   */
  void deleteById(final ID id);

  /**
   * Supprime l'entité T avec l'ID
   *
   * @param id
   */
  void deleteById(ID id, long clock);

  /**
   * Supprime l'entité T fournie.
   *
   * @param entité
   * @throws IllegalArgumentException
   *            in case the given entity is (@literal null}.
   */
  void delete(T entity);

  /**
   * Supprime les entitées fournies
   *
   * @param entitées
   *           à supprimer
   */
  void deleteAll(final Iterable<T> entities);

  /**
   * @param id
   * @return
   */
  Iterator<T> IterableFindById(ID id);

}
