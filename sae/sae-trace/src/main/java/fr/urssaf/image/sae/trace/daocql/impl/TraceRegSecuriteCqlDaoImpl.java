/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteCqlDao;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class TraceRegSecuriteCqlDaoImpl extends GenericDAOImpl<TraceRegSecuriteCql, UUID> implements ITraceRegSecuriteCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceRegSecuriteCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
