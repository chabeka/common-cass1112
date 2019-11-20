/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class TraceJournalEvtIndexCqlDaoImpl extends GenericDAOImpl<TraceJournalEvtIndexCql, String> implements ITraceJournalEvtIndexCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceJournalEvtIndexCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
