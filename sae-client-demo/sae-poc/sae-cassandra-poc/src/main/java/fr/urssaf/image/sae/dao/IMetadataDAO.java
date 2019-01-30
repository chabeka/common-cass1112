/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.dao;

import java.util.List;

import com.datastax.driver.core.ResultSet;

import fr.urssaf.image.sae.model.GenericType;
import fr.urssaf.image.sae.model.Metadata;

/**
 * TODO (AC75095028) Description du type
 */
public interface IMetadataDAO extends IGenericDAO<Metadata, String> {
  ResultSet findAllMatadatas();

  List<GenericType> findAllGenericType();

  public void insert(final GenericType entity);

}
