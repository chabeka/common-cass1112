/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.dao;

import java.util.Iterator;

/**
 * TODO (AC75095028) Description du type
 */
public interface IGenericIndexDAO<T, ID> extends ICommonDAO<T, ID> {

   /**
    * @param id
    * @return
    */
   Iterator<T> findAllWithMapperById(ID id);
}
