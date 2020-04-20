/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata.referential.dao.cql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IDictionaryDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;

/**
 * (AC75095351) Implémentation du dao cql Dictionary
 */
@Repository
public class DictionaryCqlDaoImpl extends GenericDAOImpl<Dictionary, String> implements IDictionaryDaoCql {

  /**
   * @param ccf
   */
  @Autowired
  public DictionaryCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
