/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.jobspring.dao.impl;

import java.nio.ByteBuffer;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.jobspring.dao.IGenericJobSpringDAO;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class GenericJobSpringDAOImpl extends GenericDAOImpl<GenericJobSpring, ByteBuffer> implements IGenericJobSpringDAO {

  /**
   * @param ccf
   */
  public GenericJobSpringDAOImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
