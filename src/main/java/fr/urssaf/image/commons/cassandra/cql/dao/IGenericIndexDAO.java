/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.dao;

import java.util.Iterator;

/**
 * Interface pour effectuer certaine operation propre aux entités de types index
 *
 * @param <I>
 *          entité I de type indexe
 * @param <ID>
 *          l'identifiant de l'entité
 */
public interface IGenericIndexDAO<I, ID> extends ICommonDAO<I, ID> {

   /**
    * @param id
    * @return
    */
  Iterator<I> findAllWithMapperById(ID id);
}
