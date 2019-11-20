/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.dao.cql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.droit.dao.cql.IActionUnitaireDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

/**
 * (AC75095351) Implémentation du dao cql ActionUnitaire
 */
@Repository
public class ActionUnitaireCqlDaoImpl extends GenericDAOImpl<ActionUnitaire, String> implements IActionUnitaireDaoCql {

  /**
   * @param ccf
   */
  @Autowired
  public ActionUnitaireCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
