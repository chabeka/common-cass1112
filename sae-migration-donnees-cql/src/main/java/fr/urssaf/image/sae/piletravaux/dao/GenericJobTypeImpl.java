/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletravaux.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
//import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.piletravaux.model.GenericJobType;

/**
 * TODO (AC75095028) Description du type
 * Service permettant l'extraction des données du model thrift en utilisant
 * des requete cql. Ce qui engendre du mapping manuel pour contruire les
 * bean associés aux données extraites
 */
@Repository
public class GenericJobTypeImpl extends GenericDAOImpl<GenericJobType, UUID> implements IGenericJobTypeDao {

  /**
   * @param ccf
   */
  @Autowired
  public GenericJobTypeImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
