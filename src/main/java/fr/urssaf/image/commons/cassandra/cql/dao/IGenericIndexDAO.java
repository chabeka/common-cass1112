/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.dao;

import java.util.Iterator;

/**
 * Interface commune de toutes les classes de DAO de type index.
 *  * @param <T>
 *          Type de d'objet index contenue dans le registre
 * @param <ID>
 *          Identifiant de l'objet index
 *
 */
public interface IGenericIndexDAO<T, ID> extends ICommonDAO<T, ID> {

   /**
    * @param id
    * @return
    */
   Iterator<T> findAllWithMapperById(ID id);
}
