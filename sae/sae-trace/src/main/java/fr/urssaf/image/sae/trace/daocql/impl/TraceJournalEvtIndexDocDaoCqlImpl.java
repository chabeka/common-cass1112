package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;

@Repository
public class TraceJournalEvtIndexDocDaoCqlImpl extends GenericDAOImpl<TraceJournalEvtIndexDocCql, UUID> implements ITraceJournalEvtIndexDocCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceJournalEvtIndexDocDaoCqlImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
