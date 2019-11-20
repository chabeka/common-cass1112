/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.ISequencesDaoCql;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class SequencesDaoCqlImpl extends GenericDAOImpl<SequencesCql, String> implements ISequencesDaoCql {

  /**
   * @param ccf
   */
  @Autowired
  public SequencesDaoCqlImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
    // TODO Auto-generated constructor stub
  }

}
