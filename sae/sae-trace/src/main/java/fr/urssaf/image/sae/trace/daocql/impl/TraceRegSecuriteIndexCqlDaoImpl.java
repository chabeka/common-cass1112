/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteIndexCqlDao;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class TraceRegSecuriteIndexCqlDaoImpl extends GenericDAOImpl<TraceRegSecuriteIndexCql, String> implements ITraceRegSecuriteIndexCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceRegSecuriteIndexCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
