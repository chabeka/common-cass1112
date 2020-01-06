/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.jobspring.dao.impl;

import java.nio.ByteBuffer;

import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.jobspring.dao.IGenericJobExecutionDAO;
import fr.urssaf.image.sae.jobspring.model.GenericJobExecution;

/**
 * (AC75095028) Implémentationd de l'interface IGenericJobExecutionDAO
 */
@Component
public class GenericJobExecutionDAOImpl extends GenericDAOImpl<GenericJobExecution, ByteBuffer> implements IGenericJobExecutionDAO {

  /**
   * @param ccf
   */
  public GenericJobExecutionDAOImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
