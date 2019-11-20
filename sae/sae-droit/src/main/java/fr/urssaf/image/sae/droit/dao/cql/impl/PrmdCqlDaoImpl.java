/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.dao.cql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.droit.dao.cql.IPrmdDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Prmd;

/**
 * (AC75095351) Implémentation du dao cql Prmd
 */
@Repository
public class PrmdCqlDaoImpl extends GenericDAOImpl<Prmd, String> implements IPrmdDaoCql {

  /**
   * @param ccf
   */
  @Autowired
  public PrmdCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
