package fr.urssaf.image.commons.cassandra.cql.dao;

import java.util.Optional;


/**
 * (AC75095028)
 * Classe abstraite pour les DAO CASSANDRA
 *
 * @param <T>
 * @param <ID>
 * @param <CK>
 */
public interface IGenericCompositeDAO<T, ID, CK> extends IGenericDAO<T, ID> {

  /**
   * Recherche une entité T par son ID en utilisant le {@link com.datastax.driver.mapping.Mapper}
   * de datastax <a href="https://docs.datastax.com/en/developer/java-driver/2.1/manual/object_mapper/using/">Using the mapper</a>
   *
   * @param id
   *          l'ID de l'entité
   * @param ck
   *          Autre partie de clé de partition de l'entité (dans le cas de l'entité Correspondance c'est la version)
   * @return {@link Optional} contenant l'entité ou un {@link Optional} vide si l'entité n'existe pas
   */

  Optional<T> findWithMapperByIdComposite(final ID id, final CK ck);

}
