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

  /*
   * Redéfinition de la méthode delete
   * pour une entité spécifique dans le cas de PagmCql
   * car le delete de la classe mère ne tient compte que de la key
   * et tous les éléments qui ont le même idClient
   */
  @Override
  public void delete(final PagmCql pagmCql) {
    getMapper().delete(pagmCql);
  }

}

