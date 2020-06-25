/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.droit.dao.cql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.droit.dao.cql.IPagmDaoCql;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;


/**
 * (AC75095351) Implémentation du dao cql Pagm
 */
@Repository
public class PagmCqlDaoImpl extends GenericDAOImpl<PagmCql, String> implements IPagmDaoCql {

  /**
   * @param ccf
   */
  @Autowired
  public PagmCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}

