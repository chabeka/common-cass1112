/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.modeapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;

/**
 * (AC75095351) Implémentation du dao cql ModeAPI
 */
@Repository
public class ModeAPICqlDaoImpl extends GenericDAOImpl<ModeAPI, String> implements IModeAPIDaoCql {

  /**
   * @param ccf
   */
  @Autowired
  public ModeAPICqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
