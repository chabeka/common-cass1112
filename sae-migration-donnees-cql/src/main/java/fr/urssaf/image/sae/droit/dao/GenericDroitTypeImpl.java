package fr.urssaf.image.sae.droit.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.droit.model.GenericDroitType;

/**
 * (AC75095351)
 * Service permettant l'extraction des données du model thrift en utilisant
 * des requete cql. Ce qui engendre du mapping manuel pour contruire les
 * bean associés aux données extraites
 */
@Repository
public class GenericDroitTypeImpl extends GenericDAOImpl<GenericDroitType, UUID> implements IGenericDroitTypeDao {

  /**
   * @param ccf
   */
  public GenericDroitTypeImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
