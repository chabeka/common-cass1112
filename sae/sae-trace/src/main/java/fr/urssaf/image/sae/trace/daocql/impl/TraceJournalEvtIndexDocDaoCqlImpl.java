package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;

@Repository
public class TraceJournalEvtIndexDocDaoCqlImpl extends GenericDAOImpl<TraceJournalEvtIndexDocCql, UUID> implements ITraceJournalEvtIndexDocCqlDao {

}
