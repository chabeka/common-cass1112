package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;

@Repository
public class TraceJournalEvtIndexDocDaoCqlImpl extends GenericDAOImpl<TraceJournalEvtIndexDocCql, UUID> implements ITraceJournalEvtIndexDocCqlDao {

}
