package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;

@Repository
public class TraceJournalEvtIndexDocDaoCqlImpl extends GenericDAOImpl<TraceJournalEvtIndexDocCql, String> implements ITraceJournalEvtIndexDocCqlDao {

}
